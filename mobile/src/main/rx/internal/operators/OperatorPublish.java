package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.MissingBackpressureException;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.SynchronizedQueue;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.observables.ConnectableObservable;
import rx.subscriptions.Subscriptions;

public final class OperatorPublish<T> extends ConnectableObservable<T> {
    final AtomicReference<PublishSubscriber<T>> current;
    final Observable<? extends T> source;

    /* renamed from: rx.internal.operators.OperatorPublish.1 */
    static class C12591 implements OnSubscribe<T> {
        final /* synthetic */ AtomicReference val$curr;

        C12591(AtomicReference atomicReference) {
            this.val$curr = atomicReference;
        }

        public void call(Subscriber<? super T> child) {
            while (true) {
                PublishSubscriber<T> r = (PublishSubscriber) this.val$curr.get();
                if (r == null || r.isUnsubscribed()) {
                    PublishSubscriber<T> u = new PublishSubscriber(this.val$curr);
                    u.init();
                    if (this.val$curr.compareAndSet(r, u)) {
                        r = u;
                    } else {
                        continue;
                    }
                }
                InnerProducer<T> inner = new InnerProducer(r, child);
                if (r.add(inner)) {
                    child.add(inner);
                    child.setProducer(inner);
                    return;
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorPublish.2 */
    static class C12612 implements OnSubscribe<R> {
        final /* synthetic */ Func1 val$selector;
        final /* synthetic */ Observable val$source;

        /* renamed from: rx.internal.operators.OperatorPublish.2.1 */
        class C12601 implements Action1<Subscription> {
            final /* synthetic */ Subscriber val$child;

            C12601(Subscriber subscriber) {
                this.val$child = subscriber;
            }

            public void call(Subscription t1) {
                this.val$child.add(t1);
            }
        }

        C12612(Observable observable, Func1 func1) {
            this.val$source = observable;
            this.val$selector = func1;
        }

        public void call(Subscriber<? super R> child) {
            ConnectableObservable<T> op = OperatorPublish.create(this.val$source);
            ((Observable) this.val$selector.call(op)).unsafeSubscribe(child);
            op.connect(new C12601(child));
        }
    }

    static final class InnerProducer<T> extends AtomicLong implements Producer, Subscription {
        static final long NOT_REQUESTED = -4611686018427387904L;
        static final long UNSUBSCRIBED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        final PublishSubscriber<T> parent;

        public InnerProducer(PublishSubscriber<T> parent, Subscriber<? super T> child) {
            this.parent = parent;
            this.child = child;
            lazySet(NOT_REQUESTED);
        }

        public void request(long n) {
            if (n >= 0) {
                long r;
                long u;
                do {
                    r = get();
                    if (r == UNSUBSCRIBED) {
                        return;
                    }
                    if (r >= 0 && n == 0) {
                        return;
                    }
                    if (r == NOT_REQUESTED) {
                        u = n;
                    } else {
                        u = r + n;
                        if (u < 0) {
                            u = Long.MAX_VALUE;
                        }
                    }
                } while (!compareAndSet(r, u));
                this.parent.dispatch();
            }
        }

        public long produced(long n) {
            if (n <= 0) {
                throw new IllegalArgumentException("Cant produce zero or less");
            }
            long u;
            long r;
            do {
                r = get();
                if (r == NOT_REQUESTED) {
                    throw new IllegalStateException("Produced without request");
                } else if (r == UNSUBSCRIBED) {
                    return UNSUBSCRIBED;
                } else {
                    u = r - n;
                    if (u < 0) {
                        throw new IllegalStateException("More produced (" + n + ") than requested (" + r + ")");
                    }
                }
            } while (!compareAndSet(r, u));
            return u;
        }

        public boolean isUnsubscribed() {
            return get() == UNSUBSCRIBED;
        }

        public void unsubscribe() {
            if (get() != UNSUBSCRIBED && getAndSet(UNSUBSCRIBED) != UNSUBSCRIBED) {
                this.parent.remove(this);
                this.parent.dispatch();
            }
        }
    }

    static final class PublishSubscriber<T> extends Subscriber<T> implements Subscription {
        static final InnerProducer[] EMPTY;
        static final InnerProducer[] TERMINATED;
        final AtomicReference<PublishSubscriber<T>> current;
        boolean emitting;
        boolean missed;
        final NotificationLite<T> nl;
        final AtomicReference<InnerProducer[]> producers;
        final Queue<Object> queue;
        final AtomicBoolean shouldConnect;
        volatile Object terminalEvent;

        /* renamed from: rx.internal.operators.OperatorPublish.PublishSubscriber.1 */
        class C12621 implements Action0 {
            C12621() {
            }

            public void call() {
                PublishSubscriber.this.producers.getAndSet(PublishSubscriber.TERMINATED);
                PublishSubscriber.this.current.compareAndSet(PublishSubscriber.this, null);
            }
        }

        static {
            EMPTY = new InnerProducer[0];
            TERMINATED = new InnerProducer[0];
        }

        public PublishSubscriber(AtomicReference<PublishSubscriber<T>> current) {
            this.queue = UnsafeAccess.isUnsafeAvailable() ? new SpscArrayQueue(RxRingBuffer.SIZE) : new SynchronizedQueue(RxRingBuffer.SIZE);
            this.nl = NotificationLite.instance();
            this.producers = new AtomicReference(EMPTY);
            this.current = current;
            this.shouldConnect = new AtomicBoolean();
        }

        void init() {
            add(Subscriptions.create(new C12621()));
        }

        public void onStart() {
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            if (this.queue.offer(this.nl.next(t))) {
                dispatch();
            } else {
                onError(new MissingBackpressureException());
            }
        }

        public void onError(Throwable e) {
            if (this.terminalEvent == null) {
                this.terminalEvent = this.nl.error(e);
                dispatch();
            }
        }

        public void onCompleted() {
            if (this.terminalEvent == null) {
                this.terminalEvent = this.nl.completed();
                dispatch();
            }
        }

        boolean add(InnerProducer<T> producer) {
            if (producer == null) {
                throw new NullPointerException();
            }
            InnerProducer[] c;
            InnerProducer[] u;
            do {
                c = (InnerProducer[]) this.producers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerProducer[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.producers.compareAndSet(c, u));
            return true;
        }

        void remove(InnerProducer<T> producer) {
            InnerProducer[] c;
            InnerProducer[] u;
            do {
                c = (InnerProducer[]) this.producers.get();
                if (c != EMPTY && c != TERMINATED) {
                    int j = -1;
                    int len = c.length;
                    for (int i = 0; i < len; i++) {
                        if (c[i].equals(producer)) {
                            j = i;
                            break;
                        }
                    }
                    if (j < 0) {
                        return;
                    }
                    if (len == 1) {
                        u = EMPTY;
                    } else {
                        u = new InnerProducer[(len - 1)];
                        System.arraycopy(c, 0, u, 0, j);
                        System.arraycopy(c, j + 1, u, j, (len - j) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.producers.compareAndSet(c, u));
        }

        boolean checkTerminated(Object term, boolean empty) {
            if (term != null) {
                if (!this.nl.isCompleted(term)) {
                    Throwable t = this.nl.getError(term);
                    this.current.compareAndSet(this, null);
                    try {
                        for (InnerProducer<?> ip : (InnerProducer[]) this.producers.getAndSet(TERMINATED)) {
                            ip.child.onError(t);
                        }
                        return true;
                    } finally {
                        unsubscribe();
                    }
                } else if (empty) {
                    this.current.compareAndSet(this, null);
                    try {
                        for (InnerProducer<?> ip2 : (InnerProducer[]) this.producers.getAndSet(TERMINATED)) {
                            ip2.child.onCompleted();
                        }
                        return true;
                    } finally {
                        unsubscribe();
                    }
                }
            }
            return false;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void dispatch() {
            /*
            r26 = this;
            monitor-enter(r26);
            r0 = r26;
            r0 = r0.emitting;	 Catch:{ all -> 0x0052 }
            r22 = r0;
            if (r22 == 0) goto L_0x0013;
        L_0x0009:
            r22 = 1;
            r0 = r22;
            r1 = r26;
            r1.missed = r0;	 Catch:{ all -> 0x0052 }
            monitor-exit(r26);	 Catch:{ all -> 0x0052 }
        L_0x0012:
            return;
        L_0x0013:
            r22 = 1;
            r0 = r22;
            r1 = r26;
            r1.emitting = r0;	 Catch:{ all -> 0x0052 }
            r22 = 0;
            r0 = r22;
            r1 = r26;
            r1.missed = r0;	 Catch:{ all -> 0x0052 }
            monitor-exit(r26);	 Catch:{ all -> 0x0052 }
            r16 = 0;
        L_0x0026:
            r0 = r26;
            r0 = r0.terminalEvent;	 Catch:{ all -> 0x00d1 }
            r18 = r0;
            r0 = r26;
            r0 = r0.queue;	 Catch:{ all -> 0x00d1 }
            r22 = r0;
            r6 = r22.isEmpty();	 Catch:{ all -> 0x00d1 }
            r0 = r26;
            r1 = r18;
            r22 = r0.checkTerminated(r1, r6);	 Catch:{ all -> 0x00d1 }
            if (r22 == 0) goto L_0x0055;
        L_0x0040:
            r16 = 1;
            if (r16 != 0) goto L_0x0012;
        L_0x0044:
            monitor-enter(r26);
            r22 = 0;
            r0 = r22;
            r1 = r26;
            r1.emitting = r0;	 Catch:{ all -> 0x004f }
            monitor-exit(r26);	 Catch:{ all -> 0x004f }
            goto L_0x0012;
        L_0x004f:
            r22 = move-exception;
            monitor-exit(r26);	 Catch:{ all -> 0x004f }
            throw r22;
        L_0x0052:
            r22 = move-exception;
            monitor-exit(r26);	 Catch:{ all -> 0x0052 }
            throw r22;
        L_0x0055:
            if (r6 != 0) goto L_0x012f;
        L_0x0057:
            r0 = r26;
            r0 = r0.producers;	 Catch:{ all -> 0x00d1 }
            r22 = r0;
            r11 = r22.get();	 Catch:{ all -> 0x00d1 }
            r11 = (rx.internal.operators.OperatorPublish.InnerProducer[]) r11;	 Catch:{ all -> 0x00d1 }
            r9 = r11.length;	 Catch:{ all -> 0x00d1 }
            r12 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r19 = 0;
            r4 = r11;
            r10 = r4.length;	 Catch:{ all -> 0x00d1 }
            r7 = 0;
        L_0x006e:
            if (r7 >= r10) goto L_0x008c;
        L_0x0070:
            r8 = r4[r7];	 Catch:{ all -> 0x00d1 }
            r14 = r8.get();	 Catch:{ all -> 0x00d1 }
            r22 = 0;
            r22 = (r14 > r22 ? 1 : (r14 == r22 ? 0 : -1));
            if (r22 < 0) goto L_0x0083;
        L_0x007c:
            r12 = java.lang.Math.min(r12, r14);	 Catch:{ all -> 0x00d1 }
        L_0x0080:
            r7 = r7 + 1;
            goto L_0x006e;
        L_0x0083:
            r22 = -9223372036854775808;
            r22 = (r14 > r22 ? 1 : (r14 == r22 ? 0 : -1));
            if (r22 != 0) goto L_0x0080;
        L_0x0089:
            r19 = r19 + 1;
            goto L_0x0080;
        L_0x008c:
            r0 = r19;
            if (r9 != r0) goto L_0x00df;
        L_0x0090:
            r0 = r26;
            r0 = r0.terminalEvent;	 Catch:{ all -> 0x00d1 }
            r18 = r0;
            r0 = r26;
            r0 = r0.queue;	 Catch:{ all -> 0x00d1 }
            r22 = r0;
            r20 = r22.poll();	 Catch:{ all -> 0x00d1 }
            if (r20 != 0) goto L_0x00c3;
        L_0x00a2:
            r22 = 1;
        L_0x00a4:
            r0 = r26;
            r1 = r18;
            r2 = r22;
            r22 = r0.checkTerminated(r1, r2);	 Catch:{ all -> 0x00d1 }
            if (r22 == 0) goto L_0x00c6;
        L_0x00b0:
            r16 = 1;
            if (r16 != 0) goto L_0x0012;
        L_0x00b4:
            monitor-enter(r26);
            r22 = 0;
            r0 = r22;
            r1 = r26;
            r1.emitting = r0;	 Catch:{ all -> 0x00c0 }
            monitor-exit(r26);	 Catch:{ all -> 0x00c0 }
            goto L_0x0012;
        L_0x00c0:
            r22 = move-exception;
            monitor-exit(r26);	 Catch:{ all -> 0x00c0 }
            throw r22;
        L_0x00c3:
            r22 = 0;
            goto L_0x00a4;
        L_0x00c6:
            r22 = 1;
            r0 = r26;
            r1 = r22;
            r0.request(r1);	 Catch:{ all -> 0x00d1 }
            goto L_0x0026;
        L_0x00d1:
            r22 = move-exception;
            if (r16 != 0) goto L_0x00de;
        L_0x00d4:
            monitor-enter(r26);
            r23 = 0;
            r0 = r23;
            r1 = r26;
            r1.emitting = r0;	 Catch:{ all -> 0x01aa }
            monitor-exit(r26);	 Catch:{ all -> 0x01aa }
        L_0x00de:
            throw r22;
        L_0x00df:
            r5 = 0;
        L_0x00e0:
            r0 = (long) r5;
            r22 = r0;
            r22 = (r22 > r12 ? 1 : (r22 == r12 ? 0 : -1));
            if (r22 >= 0) goto L_0x011b;
        L_0x00e7:
            r0 = r26;
            r0 = r0.terminalEvent;	 Catch:{ all -> 0x00d1 }
            r18 = r0;
            r0 = r26;
            r0 = r0.queue;	 Catch:{ all -> 0x00d1 }
            r22 = r0;
            r20 = r22.poll();	 Catch:{ all -> 0x00d1 }
            if (r20 != 0) goto L_0x0117;
        L_0x00f9:
            r6 = 1;
        L_0x00fa:
            r0 = r26;
            r1 = r18;
            r22 = r0.checkTerminated(r1, r6);	 Catch:{ all -> 0x00d1 }
            if (r22 == 0) goto L_0x0119;
        L_0x0104:
            r16 = 1;
            if (r16 != 0) goto L_0x0012;
        L_0x0108:
            monitor-enter(r26);
            r22 = 0;
            r0 = r22;
            r1 = r26;
            r1.emitting = r0;	 Catch:{ all -> 0x0114 }
            monitor-exit(r26);	 Catch:{ all -> 0x0114 }
            goto L_0x0012;
        L_0x0114:
            r22 = move-exception;
            monitor-exit(r26);	 Catch:{ all -> 0x0114 }
            throw r22;
        L_0x0117:
            r6 = 0;
            goto L_0x00fa;
        L_0x0119:
            if (r6 == 0) goto L_0x0154;
        L_0x011b:
            if (r5 <= 0) goto L_0x0127;
        L_0x011d:
            r0 = (long) r5;
            r22 = r0;
            r0 = r26;
            r1 = r22;
            r0.request(r1);	 Catch:{ all -> 0x00d1 }
        L_0x0127:
            r22 = 0;
            r22 = (r12 > r22 ? 1 : (r12 == r22 ? 0 : -1));
            if (r22 == 0) goto L_0x012f;
        L_0x012d:
            if (r6 == 0) goto L_0x0026;
        L_0x012f:
            monitor-enter(r26);	 Catch:{ all -> 0x00d1 }
            r0 = r26;
            r0 = r0.missed;	 Catch:{ all -> 0x01a7 }
            r22 = r0;
            if (r22 != 0) goto L_0x019c;
        L_0x0138:
            r22 = 0;
            r0 = r22;
            r1 = r26;
            r1.emitting = r0;	 Catch:{ all -> 0x01a7 }
            r16 = 1;
            monitor-exit(r26);	 Catch:{ all -> 0x01a7 }
            if (r16 != 0) goto L_0x0012;
        L_0x0145:
            monitor-enter(r26);
            r22 = 0;
            r0 = r22;
            r1 = r26;
            r1.emitting = r0;	 Catch:{ all -> 0x0151 }
            monitor-exit(r26);	 Catch:{ all -> 0x0151 }
            goto L_0x0012;
        L_0x0151:
            r22 = move-exception;
            monitor-exit(r26);	 Catch:{ all -> 0x0151 }
            throw r22;
        L_0x0154:
            r0 = r26;
            r0 = r0.nl;	 Catch:{ all -> 0x00d1 }
            r22 = r0;
            r0 = r22;
            r1 = r20;
            r21 = r0.getValue(r1);	 Catch:{ all -> 0x00d1 }
            r4 = r11;
            r10 = r4.length;	 Catch:{ all -> 0x00d1 }
            r7 = 0;
        L_0x0165:
            if (r7 >= r10) goto L_0x0198;
        L_0x0167:
            r8 = r4[r7];	 Catch:{ all -> 0x00d1 }
            r22 = r8.get();	 Catch:{ all -> 0x00d1 }
            r24 = 0;
            r22 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
            if (r22 <= 0) goto L_0x0185;
        L_0x0173:
            r0 = r8.child;	 Catch:{ Throwable -> 0x0188 }
            r22 = r0;
            r0 = r22;
            r1 = r21;
            r0.onNext(r1);	 Catch:{ Throwable -> 0x0188 }
            r22 = 1;
            r0 = r22;
            r8.produced(r0);	 Catch:{ all -> 0x00d1 }
        L_0x0185:
            r7 = r7 + 1;
            goto L_0x0165;
        L_0x0188:
            r17 = move-exception;
            r8.unsubscribe();	 Catch:{ all -> 0x00d1 }
            r0 = r8.child;	 Catch:{ all -> 0x00d1 }
            r22 = r0;
            r0 = r22;
            r1 = r17;
            r0.onError(r1);	 Catch:{ all -> 0x00d1 }
            goto L_0x0185;
        L_0x0198:
            r5 = r5 + 1;
            goto L_0x00e0;
        L_0x019c:
            r22 = 0;
            r0 = r22;
            r1 = r26;
            r1.missed = r0;	 Catch:{ all -> 0x01a7 }
            monitor-exit(r26);	 Catch:{ all -> 0x01a7 }
            goto L_0x0026;
        L_0x01a7:
            r22 = move-exception;
            monitor-exit(r26);	 Catch:{ all -> 0x01a7 }
            throw r22;	 Catch:{ all -> 0x00d1 }
        L_0x01aa:
            r22 = move-exception;
            monitor-exit(r26);	 Catch:{ all -> 0x01aa }
            throw r22;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorPublish.PublishSubscriber.dispatch():void");
        }
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source) {
        AtomicReference<PublishSubscriber<T>> curr = new AtomicReference();
        return new OperatorPublish(new C12591(curr), source, curr);
    }

    public static <T, R> Observable<R> create(Observable<? extends T> source, Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return Observable.create(new C12612(source, selector));
    }

    private OperatorPublish(OnSubscribe<T> onSubscribe, Observable<? extends T> source, AtomicReference<PublishSubscriber<T>> current) {
        super(onSubscribe);
        this.source = source;
        this.current = current;
    }

    public void connect(Action1<? super Subscription> connection) {
        PublishSubscriber<T> ps;
        PublishSubscriber<T> u;
        boolean doConnect;
        do {
            ps = (PublishSubscriber) this.current.get();
            if (ps != null && !ps.isUnsubscribed()) {
                break;
            }
            u = new PublishSubscriber(this.current);
            u.init();
        } while (!this.current.compareAndSet(ps, u));
        ps = u;
        if (ps.shouldConnect.get() || !ps.shouldConnect.compareAndSet(false, true)) {
            doConnect = false;
        } else {
            doConnect = true;
        }
        connection.call(ps);
        if (doConnect) {
            this.source.unsafeSubscribe(ps);
        }
    }
}
