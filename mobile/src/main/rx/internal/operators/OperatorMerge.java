package rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.MissingBackpressureException;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.ScalarSynchronousObservable;
import rx.subscriptions.CompositeSubscription;

public final class OperatorMerge<T> implements Operator<T, Observable<? extends T>> {
    final boolean delayErrors;
    final int maxConcurrent;

    private static final class HolderDelayErrors {
        static final OperatorMerge<Object> INSTANCE;

        private HolderDelayErrors() {
        }

        static {
            INSTANCE = new OperatorMerge(Integer.MAX_VALUE, null);
        }
    }

    private static final class HolderNoDelay {
        static final OperatorMerge<Object> INSTANCE;

        private HolderNoDelay() {
        }

        static {
            INSTANCE = new OperatorMerge(Integer.MAX_VALUE, null);
        }
    }

    static final class InnerSubscriber<T> extends Subscriber<T> {
        static final int limit;
        volatile boolean done;
        final long id;
        int outstanding;
        final MergeSubscriber<T> parent;
        volatile RxRingBuffer queue;

        static {
            limit = RxRingBuffer.SIZE / 4;
        }

        public InnerSubscriber(MergeSubscriber<T> parent, long id) {
            this.parent = parent;
            this.id = id;
        }

        public void onStart() {
            this.outstanding = RxRingBuffer.SIZE;
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            this.parent.tryEmit(this, t);
        }

        public void onError(Throwable e) {
            this.done = true;
            this.parent.getOrCreateErrorQueue().offer(e);
            this.parent.emit();
        }

        public void onCompleted() {
            this.done = true;
            this.parent.emit();
        }

        public void requestMore(long n) {
            int r = this.outstanding - ((int) n);
            if (r > limit) {
                this.outstanding = r;
                return;
            }
            this.outstanding = RxRingBuffer.SIZE;
            int k = RxRingBuffer.SIZE - r;
            if (k > 0) {
                request((long) k);
            }
        }
    }

    static final class MergeProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -1214379189873595503L;
        final MergeSubscriber<T> subscriber;

        public MergeProducer(MergeSubscriber<T> subscriber) {
            this.subscriber = subscriber;
        }

        public void request(long n) {
            if (n > 0) {
                if (get() != Long.MAX_VALUE) {
                    BackpressureUtils.getAndAddRequest(this, n);
                    this.subscriber.emit();
                }
            } else if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            }
        }

        public long produced(int n) {
            return addAndGet((long) (-n));
        }
    }

    static final class MergeSubscriber<T> extends Subscriber<Observable<? extends T>> {
        static final InnerSubscriber<?>[] EMPTY;
        final Subscriber<? super T> child;
        final boolean delayErrors;
        volatile boolean done;
        boolean emitting;
        volatile ConcurrentLinkedQueue<Throwable> errors;
        final Object innerGuard;
        volatile InnerSubscriber<?>[] innerSubscribers;
        long lastId;
        int lastIndex;
        final int maxConcurrent;
        boolean missed;
        final NotificationLite<T> nl;
        MergeProducer<T> producer;
        volatile RxRingBuffer queue;
        volatile CompositeSubscription subscriptions;
        long uniqueId;

        static {
            EMPTY = new InnerSubscriber[0];
        }

        public MergeSubscriber(Subscriber<? super T> child, boolean delayErrors, int maxConcurrent) {
            this.child = child;
            this.delayErrors = delayErrors;
            this.maxConcurrent = maxConcurrent;
            this.nl = NotificationLite.instance();
            this.innerGuard = new Object();
            this.innerSubscribers = EMPTY;
            request((long) Math.min(maxConcurrent, RxRingBuffer.SIZE));
        }

        Queue<Throwable> getOrCreateErrorQueue() {
            ConcurrentLinkedQueue<Throwable> q = this.errors;
            if (q == null) {
                synchronized (this) {
                    try {
                        q = this.errors;
                        if (q == null) {
                            ConcurrentLinkedQueue<Throwable> q2 = new ConcurrentLinkedQueue();
                            try {
                                this.errors = q2;
                                q = q2;
                            } catch (Throwable th) {
                                Throwable th2 = th;
                                q = q2;
                                throw th2;
                            }
                        }
                    } catch (Throwable th3) {
                        th2 = th3;
                        throw th2;
                    }
                }
            }
            return q;
        }

        CompositeSubscription getOrCreateComposite() {
            Throwable th;
            CompositeSubscription c = this.subscriptions;
            if (c == null) {
                boolean shouldAdd = false;
                synchronized (this) {
                    c = this.subscriptions;
                    if (c == null) {
                        CompositeSubscription c2 = new CompositeSubscription();
                        try {
                            this.subscriptions = c2;
                            shouldAdd = true;
                            c = c2;
                        } catch (Throwable th2) {
                            th = th2;
                            c = c2;
                            throw th;
                        }
                    }
                    try {
                        if (shouldAdd) {
                            add(c);
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th;
                    }
                }
            }
            return c;
        }

        public void onNext(Observable<? extends T> t) {
            if (t != null) {
                if (t instanceof ScalarSynchronousObservable) {
                    tryEmit(((ScalarSynchronousObservable) t).get());
                    return;
                }
                long j = this.uniqueId;
                this.uniqueId = 1 + j;
                InnerSubscriber<T> inner = new InnerSubscriber(this, j);
                addInner(inner);
                t.unsafeSubscribe(inner);
                emit();
            }
        }

        private void reportError() {
            List<Throwable> list = new ArrayList(this.errors);
            if (list.size() == 1) {
                this.child.onError((Throwable) list.get(0));
            } else {
                this.child.onError(new CompositeException(list));
            }
        }

        public void onError(Throwable e) {
            getOrCreateErrorQueue().offer(e);
            this.done = true;
            emit();
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        void addInner(InnerSubscriber<T> inner) {
            getOrCreateComposite().add(inner);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] a = this.innerSubscribers;
                int n = a.length;
                InnerSubscriber<?>[] b = new InnerSubscriber[(n + 1)];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = inner;
                this.innerSubscribers = b;
            }
        }

        void removeInner(InnerSubscriber<T> inner) {
            RxRingBuffer q = inner.queue;
            if (q != null) {
                q.release();
            }
            this.subscriptions.remove(inner);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] a = this.innerSubscribers;
                int n = a.length;
                int j = -1;
                for (int i = 0; i < n; i++) {
                    if (inner.equals(a[i])) {
                        j = i;
                        break;
                    }
                }
                if (j < 0) {
                } else if (n == 1) {
                    this.innerSubscribers = EMPTY;
                } else {
                    InnerSubscriber<?>[] b = new InnerSubscriber[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    this.innerSubscribers = b;
                }
            }
        }

        void tryEmit(InnerSubscriber<T> subscriber, T value) {
            boolean success = false;
            long r = this.producer.get();
            if (r != 0) {
                synchronized (this) {
                    if (!this.emitting) {
                        this.emitting = true;
                        success = true;
                    }
                }
            }
            if (success) {
                emitScalar(subscriber, value, r);
            } else {
                queueScalar(subscriber, value);
            }
        }

        protected void queueScalar(InnerSubscriber<T> subscriber, T value) {
            RxRingBuffer q = subscriber.queue;
            if (q == null) {
                q = RxRingBuffer.getSpscInstance();
                subscriber.add(q);
                subscriber.queue = q;
            }
            try {
                q.onNext(this.nl.next(value));
                emit();
            } catch (MissingBackpressureException ex) {
                subscriber.unsubscribe();
                subscriber.onError(ex);
            } catch (IllegalStateException ex2) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.unsubscribe();
                    subscriber.onError(ex2);
                }
            }
        }

        protected void emitScalar(InnerSubscriber<T> subscriber, T value, long r) {
            boolean skipFinal = false;
            try {
                this.child.onNext(value);
            } catch (Throwable th) {
                if (!skipFinal) {
                    synchronized (this) {
                    }
                    this.emitting = false;
                }
            }
            if (r != Long.MAX_VALUE) {
                this.producer.produced(1);
            }
            subscriber.requestMore(1);
            synchronized (this) {
                skipFinal = true;
                if (this.missed) {
                    this.missed = false;
                    if (1 == null) {
                        synchronized (this) {
                            this.emitting = false;
                        }
                    }
                    emitLoop();
                    return;
                }
                this.emitting = false;
                if (1 == null) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
            }
        }

        public void requestMore(long n) {
            request(n);
        }

        void tryEmit(T value) {
            boolean success = false;
            long r = this.producer.get();
            if (r != 0) {
                synchronized (this) {
                    if (!this.emitting) {
                        this.emitting = true;
                        success = true;
                    }
                }
            }
            if (success) {
                emitScalar(value, r);
            } else {
                queueScalar(value);
            }
        }

        protected void queueScalar(T value) {
            RxRingBuffer q = this.queue;
            if (q == null) {
                q = RxRingBuffer.getSpscInstance();
                add(q);
                this.queue = q;
            }
            try {
                q.onNext(this.nl.next(value));
                emit();
            } catch (MissingBackpressureException ex) {
                unsubscribe();
                onError(ex);
            } catch (IllegalStateException ex2) {
                if (!isUnsubscribed()) {
                    unsubscribe();
                    onError(ex2);
                }
            }
        }

        protected void emitScalar(T value, long r) {
            boolean skipFinal = false;
            try {
                this.child.onNext(value);
            } catch (Throwable th) {
                if (!skipFinal) {
                    synchronized (this) {
                    }
                    this.emitting = false;
                }
            }
            if (r != Long.MAX_VALUE) {
                this.producer.produced(1);
            }
            requestMore(1);
            synchronized (this) {
                skipFinal = true;
                if (this.missed) {
                    this.missed = false;
                    if (1 == null) {
                        synchronized (this) {
                            this.emitting = false;
                        }
                    }
                    emitLoop();
                    return;
                }
                this.emitting = false;
                if (1 == null) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
            }
        }

        void emit() {
            synchronized (this) {
                if (this.emitting) {
                    this.missed = true;
                    return;
                }
                this.emitting = true;
                emitLoop();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void emitLoop() {
            /*
            r32 = this;
            r23 = 0;
            r0 = r32;
            r4 = r0.child;	 Catch:{ all -> 0x010b }
        L_0x0006:
            r30 = r32.checkTerminate();	 Catch:{ all -> 0x010b }
            if (r30 == 0) goto L_0x001e;
        L_0x000c:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x0010:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x001b }
            monitor-exit(r32);	 Catch:{ all -> 0x001b }
        L_0x001a:
            return;
        L_0x001b:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x001b }
            throw r30;
        L_0x001e:
            r0 = r32;
            r0 = r0.queue;	 Catch:{ all -> 0x010b }
            r26 = r0;
            r0 = r32;
            r0 = r0.producer;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r20 = r30.get();	 Catch:{ all -> 0x010b }
            r30 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 != 0) goto L_0x0063;
        L_0x0037:
            r28 = 1;
        L_0x0039:
            r19 = 0;
            if (r26 == 0) goto L_0x0079;
        L_0x003d:
            r22 = 0;
            r16 = 0;
        L_0x0041:
            r30 = 0;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 <= 0) goto L_0x0068;
        L_0x0047:
            r16 = r26.poll();	 Catch:{ all -> 0x010b }
            r30 = r32.checkTerminate();	 Catch:{ all -> 0x010b }
            if (r30 == 0) goto L_0x0066;
        L_0x0051:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x0055:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x0060 }
            monitor-exit(r32);	 Catch:{ all -> 0x0060 }
            goto L_0x001a;
        L_0x0060:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x0060 }
            throw r30;
        L_0x0063:
            r28 = 0;
            goto L_0x0039;
        L_0x0066:
            if (r16 != 0) goto L_0x00bb;
        L_0x0068:
            if (r22 <= 0) goto L_0x0071;
        L_0x006a:
            if (r28 == 0) goto L_0x0119;
        L_0x006c:
            r20 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        L_0x0071:
            r30 = 0;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 == 0) goto L_0x0079;
        L_0x0077:
            if (r16 != 0) goto L_0x003d;
        L_0x0079:
            r0 = r32;
            r5 = r0.done;	 Catch:{ all -> 0x010b }
            r0 = r32;
            r0 = r0.queue;	 Catch:{ all -> 0x010b }
            r26 = r0;
            r0 = r32;
            r9 = r0.innerSubscribers;	 Catch:{ all -> 0x010b }
            r15 = r9.length;	 Catch:{ all -> 0x010b }
            if (r5 == 0) goto L_0x012e;
        L_0x008a:
            if (r26 == 0) goto L_0x0092;
        L_0x008c:
            r30 = r26.isEmpty();	 Catch:{ all -> 0x010b }
            if (r30 == 0) goto L_0x012e;
        L_0x0092:
            if (r15 != 0) goto L_0x012e;
        L_0x0094:
            r0 = r32;
            r6 = r0.errors;	 Catch:{ all -> 0x010b }
            if (r6 == 0) goto L_0x00a0;
        L_0x009a:
            r30 = r6.isEmpty();	 Catch:{ all -> 0x010b }
            if (r30 == 0) goto L_0x0129;
        L_0x00a0:
            r4.onCompleted();	 Catch:{ all -> 0x010b }
        L_0x00a3:
            if (r26 == 0) goto L_0x00a8;
        L_0x00a5:
            r26.release();	 Catch:{ all -> 0x010b }
        L_0x00a8:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x00ac:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x00b8 }
            monitor-exit(r32);	 Catch:{ all -> 0x00b8 }
            goto L_0x001a;
        L_0x00b8:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x00b8 }
            throw r30;
        L_0x00bb:
            r0 = r32;
            r0 = r0.nl;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r0 = r30;
            r1 = r16;
            r29 = r0.getValue(r1);	 Catch:{ all -> 0x010b }
            r0 = r29;
            r4.onNext(r0);	 Catch:{ Throwable -> 0x00d8 }
        L_0x00ce:
            r19 = r19 + 1;
            r22 = r22 + 1;
            r30 = 1;
            r20 = r20 - r30;
            goto L_0x0041;
        L_0x00d8:
            r27 = move-exception;
            r0 = r32;
            r0 = r0.delayErrors;	 Catch:{ all -> 0x010b }
            r30 = r0;
            if (r30 != 0) goto L_0x00ff;
        L_0x00e1:
            rx.exceptions.Exceptions.throwIfFatal(r27);	 Catch:{ all -> 0x010b }
            r23 = 1;
            r32.unsubscribe();	 Catch:{ all -> 0x010b }
            r0 = r27;
            r4.onError(r0);	 Catch:{ all -> 0x010b }
            if (r23 != 0) goto L_0x001a;
        L_0x00f0:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x00fc }
            monitor-exit(r32);	 Catch:{ all -> 0x00fc }
            goto L_0x001a;
        L_0x00fc:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x00fc }
            throw r30;
        L_0x00ff:
            r30 = r32.getOrCreateErrorQueue();	 Catch:{ all -> 0x010b }
            r0 = r30;
            r1 = r27;
            r0.offer(r1);	 Catch:{ all -> 0x010b }
            goto L_0x00ce;
        L_0x010b:
            r30 = move-exception;
            if (r23 != 0) goto L_0x0118;
        L_0x010e:
            monitor-enter(r32);
            r31 = 0;
            r0 = r31;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x02c2 }
            monitor-exit(r32);	 Catch:{ all -> 0x02c2 }
        L_0x0118:
            throw r30;
        L_0x0119:
            r0 = r32;
            r0 = r0.producer;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r0 = r30;
            r1 = r22;
            r20 = r0.produced(r1);	 Catch:{ all -> 0x010b }
            goto L_0x0071;
        L_0x0129:
            r32.reportError();	 Catch:{ all -> 0x010b }
            goto L_0x00a3;
        L_0x012e:
            r10 = 0;
            if (r15 <= 0) goto L_0x0276;
        L_0x0131:
            r0 = r32;
            r0 = r0.lastId;	 Catch:{ all -> 0x010b }
            r24 = r0;
            r0 = r32;
            r8 = r0.lastIndex;	 Catch:{ all -> 0x010b }
            if (r15 <= r8) goto L_0x0149;
        L_0x013d:
            r30 = r9[r8];	 Catch:{ all -> 0x010b }
            r0 = r30;
            r0 = r0.id;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r30 = (r30 > r24 ? 1 : (r30 == r24 ? 0 : -1));
            if (r30 == 0) goto L_0x016f;
        L_0x0149:
            if (r15 > r8) goto L_0x014c;
        L_0x014b:
            r8 = 0;
        L_0x014c:
            r14 = r8;
            r7 = 0;
        L_0x014e:
            if (r7 >= r15) goto L_0x015c;
        L_0x0150:
            r30 = r9[r14];	 Catch:{ all -> 0x010b }
            r0 = r30;
            r0 = r0.id;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r30 = (r30 > r24 ? 1 : (r30 == r24 ? 0 : -1));
            if (r30 != 0) goto L_0x018c;
        L_0x015c:
            r8 = r14;
            r0 = r32;
            r0.lastIndex = r14;	 Catch:{ all -> 0x010b }
            r30 = r9[r14];	 Catch:{ all -> 0x010b }
            r0 = r30;
            r0 = r0.id;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r0 = r30;
            r2 = r32;
            r2.lastId = r0;	 Catch:{ all -> 0x010b }
        L_0x016f:
            r14 = r8;
            r7 = 0;
        L_0x0171:
            if (r7 >= r15) goto L_0x0264;
        L_0x0173:
            r30 = r32.checkTerminate();	 Catch:{ all -> 0x010b }
            if (r30 == 0) goto L_0x0194;
        L_0x0179:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x017d:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x0189 }
            monitor-exit(r32);	 Catch:{ all -> 0x0189 }
            goto L_0x001a;
        L_0x0189:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x0189 }
            throw r30;
        L_0x018c:
            r14 = r14 + 1;
            if (r14 != r15) goto L_0x0191;
        L_0x0190:
            r14 = 0;
        L_0x0191:
            r7 = r7 + 1;
            goto L_0x014e;
        L_0x0194:
            r13 = r9[r14];	 Catch:{ all -> 0x010b }
            r16 = 0;
        L_0x0198:
            r17 = 0;
        L_0x019a:
            r30 = 0;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 <= 0) goto L_0x01bf;
        L_0x01a0:
            r30 = r32.checkTerminate();	 Catch:{ all -> 0x010b }
            if (r30 == 0) goto L_0x01b9;
        L_0x01a6:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x01aa:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x01b6 }
            monitor-exit(r32);	 Catch:{ all -> 0x01b6 }
            goto L_0x001a;
        L_0x01b6:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x01b6 }
            throw r30;
        L_0x01b9:
            r0 = r13.queue;	 Catch:{ all -> 0x010b }
            r18 = r0;
            if (r18 != 0) goto L_0x020f;
        L_0x01bf:
            if (r17 <= 0) goto L_0x01db;
        L_0x01c1:
            if (r28 != 0) goto L_0x0254;
        L_0x01c3:
            r0 = r32;
            r0 = r0.producer;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r0 = r30;
            r1 = r17;
            r20 = r0.produced(r1);	 Catch:{ all -> 0x010b }
        L_0x01d1:
            r0 = r17;
            r0 = (long) r0;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r0 = r30;
            r13.requestMore(r0);	 Catch:{ all -> 0x010b }
        L_0x01db:
            r30 = 0;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 == 0) goto L_0x01e3;
        L_0x01e1:
            if (r16 != 0) goto L_0x0198;
        L_0x01e3:
            r11 = r13.done;	 Catch:{ all -> 0x010b }
            r12 = r13.queue;	 Catch:{ all -> 0x010b }
            if (r11 == 0) goto L_0x025e;
        L_0x01e9:
            if (r12 == 0) goto L_0x01f1;
        L_0x01eb:
            r30 = r12.isEmpty();	 Catch:{ all -> 0x010b }
            if (r30 == 0) goto L_0x025e;
        L_0x01f1:
            r0 = r32;
            r0.removeInner(r13);	 Catch:{ all -> 0x010b }
            r30 = r32.checkTerminate();	 Catch:{ all -> 0x010b }
            if (r30 == 0) goto L_0x025b;
        L_0x01fc:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x0200:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x020c }
            monitor-exit(r32);	 Catch:{ all -> 0x020c }
            goto L_0x001a;
        L_0x020c:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x020c }
            throw r30;
        L_0x020f:
            r16 = r18.poll();	 Catch:{ all -> 0x010b }
            if (r16 == 0) goto L_0x01bf;
        L_0x0215:
            r0 = r32;
            r0 = r0.nl;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r0 = r30;
            r1 = r16;
            r29 = r0.getValue(r1);	 Catch:{ all -> 0x010b }
            r0 = r29;
            r4.onNext(r0);	 Catch:{ Throwable -> 0x0230 }
            r30 = 1;
            r20 = r20 - r30;
            r17 = r17 + 1;
            goto L_0x019a;
        L_0x0230:
            r27 = move-exception;
            r23 = 1;
            rx.exceptions.Exceptions.throwIfFatal(r27);	 Catch:{ all -> 0x010b }
            r0 = r27;
            r4.onError(r0);	 Catch:{ all -> 0x024f }
            r32.unsubscribe();	 Catch:{ all -> 0x010b }
            if (r23 != 0) goto L_0x001a;
        L_0x0240:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x024c }
            monitor-exit(r32);	 Catch:{ all -> 0x024c }
            goto L_0x001a;
        L_0x024c:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x024c }
            throw r30;
        L_0x024f:
            r30 = move-exception;
            r32.unsubscribe();	 Catch:{ all -> 0x010b }
            throw r30;	 Catch:{ all -> 0x010b }
        L_0x0254:
            r20 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            goto L_0x01d1;
        L_0x025b:
            r19 = r19 + 1;
            r10 = 1;
        L_0x025e:
            r30 = 0;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 != 0) goto L_0x02ab;
        L_0x0264:
            r0 = r32;
            r0.lastIndex = r14;	 Catch:{ all -> 0x010b }
            r30 = r9[r14];	 Catch:{ all -> 0x010b }
            r0 = r30;
            r0 = r0.id;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r0 = r30;
            r2 = r32;
            r2.lastId = r0;	 Catch:{ all -> 0x010b }
        L_0x0276:
            if (r19 <= 0) goto L_0x0284;
        L_0x0278:
            r0 = r19;
            r0 = (long) r0;	 Catch:{ all -> 0x010b }
            r30 = r0;
            r0 = r32;
            r1 = r30;
            r0.request(r1);	 Catch:{ all -> 0x010b }
        L_0x0284:
            if (r10 != 0) goto L_0x0006;
        L_0x0286:
            monitor-enter(r32);	 Catch:{ all -> 0x010b }
            r0 = r32;
            r0 = r0.missed;	 Catch:{ all -> 0x02bf }
            r30 = r0;
            if (r30 != 0) goto L_0x02b4;
        L_0x028f:
            r23 = 1;
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x02bf }
            monitor-exit(r32);	 Catch:{ all -> 0x02bf }
            if (r23 != 0) goto L_0x001a;
        L_0x029c:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x02a8 }
            monitor-exit(r32);	 Catch:{ all -> 0x02a8 }
            goto L_0x001a;
        L_0x02a8:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x02a8 }
            throw r30;
        L_0x02ab:
            r14 = r14 + 1;
            if (r14 != r15) goto L_0x02b0;
        L_0x02af:
            r14 = 0;
        L_0x02b0:
            r7 = r7 + 1;
            goto L_0x0171;
        L_0x02b4:
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.missed = r0;	 Catch:{ all -> 0x02bf }
            monitor-exit(r32);	 Catch:{ all -> 0x02bf }
            goto L_0x0006;
        L_0x02bf:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x02bf }
            throw r30;	 Catch:{ all -> 0x010b }
        L_0x02c2:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x02c2 }
            throw r30;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMerge.MergeSubscriber.emitLoop():void");
        }

        boolean checkTerminate() {
            if (this.child.isUnsubscribed()) {
                return true;
            }
            Queue<Throwable> e = this.errors;
            if (this.delayErrors || e == null || e.isEmpty()) {
                return false;
            }
            try {
                reportError();
                return true;
            } finally {
                unsubscribe();
            }
        }
    }

    public static <T> OperatorMerge<T> instance(boolean delayErrors) {
        if (delayErrors) {
            return HolderDelayErrors.INSTANCE;
        }
        return HolderNoDelay.INSTANCE;
    }

    public static <T> OperatorMerge<T> instance(boolean delayErrors, int maxConcurrent) {
        if (maxConcurrent == Integer.MAX_VALUE) {
            return instance(delayErrors);
        }
        return new OperatorMerge(delayErrors, maxConcurrent);
    }

    private OperatorMerge(boolean delayErrors, int maxConcurrent) {
        this.delayErrors = delayErrors;
        this.maxConcurrent = maxConcurrent;
    }

    public Subscriber<Observable<? extends T>> call(Subscriber<? super T> child) {
        MergeSubscriber<T> subscriber = new MergeSubscriber(child, this.delayErrors, this.maxConcurrent);
        MergeProducer<T> producer = new MergeProducer(subscriber);
        subscriber.producer = producer;
        child.add(subscriber);
        child.setProducer(producer);
        return subscriber;
    }
}
