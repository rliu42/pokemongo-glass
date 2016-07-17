package rx.internal.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.SerializedSubscriber;

public final class OperatorBufferWithTime<T> implements Operator<List<T>, T> {
    final int count;
    final Scheduler scheduler;
    final long timeshift;
    final long timespan;
    final TimeUnit unit;

    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        List<T> chunk;
        boolean done;
        final Worker inner;

        /* renamed from: rx.internal.operators.OperatorBufferWithTime.ExactSubscriber.1 */
        class C12001 implements Action0 {
            C12001() {
            }

            public void call() {
                ExactSubscriber.this.emit();
            }
        }

        public ExactSubscriber(Subscriber<? super List<T>> child, Worker inner) {
            this.child = child;
            this.inner = inner;
            this.chunk = new ArrayList();
        }

        public void onNext(T t) {
            List<T> toEmit = null;
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.chunk.add(t);
                if (this.chunk.size() == OperatorBufferWithTime.this.count) {
                    toEmit = this.chunk;
                    this.chunk = new ArrayList();
                }
                if (toEmit != null) {
                    this.child.onNext(toEmit);
                }
            }
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.done = true;
                this.chunk = null;
                this.child.onError(e);
                unsubscribe();
            }
        }

        public void onCompleted() {
            try {
                this.inner.unsubscribe();
                synchronized (this) {
                    if (this.done) {
                        return;
                    }
                    this.done = true;
                    List<T> toEmit = this.chunk;
                    this.chunk = null;
                    this.child.onNext(toEmit);
                    this.child.onCompleted();
                    unsubscribe();
                }
            } catch (Throwable t) {
                this.child.onError(t);
            }
        }

        void scheduleExact() {
            this.inner.schedulePeriodically(new C12001(), OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.unit);
        }

        void emit() {
            synchronized (this) {
                if (this.done) {
                    return;
                }
                List<T> toEmit = this.chunk;
                this.chunk = new ArrayList();
                try {
                    this.child.onNext(toEmit);
                } catch (Throwable t) {
                    onError(t);
                }
            }
        }
    }

    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        final List<List<T>> chunks;
        boolean done;
        final Worker inner;

        /* renamed from: rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.1 */
        class C12011 implements Action0 {
            C12011() {
            }

            public void call() {
                InexactSubscriber.this.startNewChunk();
            }
        }

        /* renamed from: rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.2 */
        class C12022 implements Action0 {
            final /* synthetic */ List val$chunk;

            C12022(List list) {
                this.val$chunk = list;
            }

            public void call() {
                InexactSubscriber.this.emitChunk(this.val$chunk);
            }
        }

        public InexactSubscriber(Subscriber<? super List<T>> child, Worker inner) {
            this.child = child;
            this.inner = inner;
            this.chunks = new LinkedList();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r8) {
            /*
            r7 = this;
            r3 = 0;
            monitor-enter(r7);
            r5 = r7.done;	 Catch:{ all -> 0x0050 }
            if (r5 == 0) goto L_0x0008;
        L_0x0006:
            monitor-exit(r7);	 Catch:{ all -> 0x0050 }
        L_0x0007:
            return;
        L_0x0008:
            r5 = r7.chunks;	 Catch:{ all -> 0x0050 }
            r2 = r5.iterator();	 Catch:{ all -> 0x0050 }
            r4 = r3;
        L_0x000f:
            r5 = r2.hasNext();	 Catch:{ all -> 0x0055 }
            if (r5 == 0) goto L_0x0037;
        L_0x0015:
            r0 = r2.next();	 Catch:{ all -> 0x0055 }
            r0 = (java.util.List) r0;	 Catch:{ all -> 0x0055 }
            r0.add(r8);	 Catch:{ all -> 0x0055 }
            r5 = r0.size();	 Catch:{ all -> 0x0055 }
            r6 = rx.internal.operators.OperatorBufferWithTime.this;	 Catch:{ all -> 0x0055 }
            r6 = r6.count;	 Catch:{ all -> 0x0055 }
            if (r5 != r6) goto L_0x005a;
        L_0x0028:
            r2.remove();	 Catch:{ all -> 0x0055 }
            if (r4 != 0) goto L_0x0058;
        L_0x002d:
            r3 = new java.util.LinkedList;	 Catch:{ all -> 0x0055 }
            r3.<init>();	 Catch:{ all -> 0x0055 }
        L_0x0032:
            r3.add(r0);	 Catch:{ all -> 0x0050 }
        L_0x0035:
            r4 = r3;
            goto L_0x000f;
        L_0x0037:
            monitor-exit(r7);	 Catch:{ all -> 0x0055 }
            if (r4 == 0) goto L_0x0053;
        L_0x003a:
            r1 = r4.iterator();
        L_0x003e:
            r5 = r1.hasNext();
            if (r5 == 0) goto L_0x0053;
        L_0x0044:
            r0 = r1.next();
            r0 = (java.util.List) r0;
            r5 = r7.child;
            r5.onNext(r0);
            goto L_0x003e;
        L_0x0050:
            r5 = move-exception;
        L_0x0051:
            monitor-exit(r7);	 Catch:{ all -> 0x0050 }
            throw r5;
        L_0x0053:
            r3 = r4;
            goto L_0x0007;
        L_0x0055:
            r5 = move-exception;
            r3 = r4;
            goto L_0x0051;
        L_0x0058:
            r3 = r4;
            goto L_0x0032;
        L_0x005a:
            r3 = r4;
            goto L_0x0035;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.done = true;
                this.chunks.clear();
                this.child.onError(e);
                unsubscribe();
            }
        }

        public void onCompleted() {
            try {
                synchronized (this) {
                    if (this.done) {
                        return;
                    }
                    this.done = true;
                    List<List<T>> sizeReached = new LinkedList(this.chunks);
                    this.chunks.clear();
                    for (List<T> chunk : sizeReached) {
                        this.child.onNext(chunk);
                    }
                    this.child.onCompleted();
                    unsubscribe();
                }
            } catch (Throwable t) {
                this.child.onError(t);
            }
        }

        void scheduleChunk() {
            this.inner.schedulePeriodically(new C12011(), OperatorBufferWithTime.this.timeshift, OperatorBufferWithTime.this.timeshift, OperatorBufferWithTime.this.unit);
        }

        void startNewChunk() {
            List<T> chunk = new ArrayList();
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.chunks.add(chunk);
                this.inner.schedule(new C12022(chunk), OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.unit);
            }
        }

        void emitChunk(List<T> chunkToEmit) {
            boolean emit = false;
            synchronized (this) {
                if (this.done) {
                    return;
                }
                Iterator<List<T>> it = this.chunks.iterator();
                while (it.hasNext()) {
                    if (((List) it.next()) == chunkToEmit) {
                        it.remove();
                        emit = true;
                        break;
                    }
                }
                if (emit) {
                    try {
                        this.child.onNext(chunkToEmit);
                    } catch (Throwable t) {
                        onError(t);
                    }
                }
            }
        }
    }

    public OperatorBufferWithTime(long timespan, long timeshift, TimeUnit unit, int count, Scheduler scheduler) {
        this.timespan = timespan;
        this.timeshift = timeshift;
        this.unit = unit;
        this.count = count;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        Worker inner = this.scheduler.createWorker();
        SerializedSubscriber<List<T>> serialized = new SerializedSubscriber(child);
        if (this.timespan == this.timeshift) {
            ExactSubscriber bsub = new ExactSubscriber(serialized, inner);
            bsub.add(inner);
            child.add(bsub);
            bsub.scheduleExact();
            return bsub;
        }
        Subscriber bsub2 = new InexactSubscriber(serialized, inner);
        bsub2.add(inner);
        child.add(bsub2);
        bsub2.startNewChunk();
        bsub2.scheduleChunk();
        return bsub2;
    }
}
