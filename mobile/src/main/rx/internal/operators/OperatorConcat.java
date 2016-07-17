package rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.internal.producers.ProducerArbiter;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;
import rx.subscriptions.Subscriptions;

public final class OperatorConcat<T> implements Operator<T, Observable<? extends T>> {

    static class ConcatInnerSubscriber<T> extends Subscriber<T> {
        private static final AtomicIntegerFieldUpdater<ConcatInnerSubscriber> ONCE;
        private final ProducerArbiter arbiter;
        private final Subscriber<T> child;
        private volatile int once;
        private final ConcatSubscriber<T> parent;

        static {
            ONCE = AtomicIntegerFieldUpdater.newUpdater(ConcatInnerSubscriber.class, "once");
        }

        public ConcatInnerSubscriber(ConcatSubscriber<T> parent, Subscriber<T> child, ProducerArbiter arbiter) {
            this.once = 0;
            this.parent = parent;
            this.child = child;
            this.arbiter = arbiter;
        }

        public void onNext(T t) {
            this.child.onNext(t);
            this.parent.decrementRequested();
            this.arbiter.produced(1);
        }

        public void onError(Throwable e) {
            if (ONCE.compareAndSet(this, 0, 1)) {
                this.parent.onError(e);
            }
        }

        public void onCompleted() {
            if (ONCE.compareAndSet(this, 0, 1)) {
                this.parent.completeInner();
            }
        }

        public void setProducer(Producer producer) {
            this.arbiter.setProducer(producer);
        }
    }

    static final class ConcatProducer<T> implements Producer {
        final ConcatSubscriber<T> cs;

        ConcatProducer(ConcatSubscriber<T> cs) {
            this.cs = cs;
        }

        public void request(long n) {
            this.cs.requestFromChild(n);
        }
    }

    static final class ConcatSubscriber<T> extends Subscriber<Observable<? extends T>> {
        private static final AtomicLongFieldUpdater<ConcatSubscriber> REQUESTED;
        static final AtomicIntegerFieldUpdater<ConcatSubscriber> WIP;
        private final ProducerArbiter arbiter;
        private final Subscriber<T> child;
        private final SerialSubscription current;
        volatile ConcatInnerSubscriber<T> currentSubscriber;
        final NotificationLite<Observable<? extends T>> nl;
        final ConcurrentLinkedQueue<Object> queue;
        private volatile long requested;
        volatile int wip;

        /* renamed from: rx.internal.operators.OperatorConcat.ConcatSubscriber.1 */
        class C12051 implements Action0 {
            C12051() {
            }

            public void call() {
                ConcatSubscriber.this.queue.clear();
            }
        }

        static {
            WIP = AtomicIntegerFieldUpdater.newUpdater(ConcatSubscriber.class, "wip");
            REQUESTED = AtomicLongFieldUpdater.newUpdater(ConcatSubscriber.class, "requested");
        }

        public ConcatSubscriber(Subscriber<T> s, SerialSubscription current) {
            super(s);
            this.nl = NotificationLite.instance();
            this.child = s;
            this.current = current;
            this.arbiter = new ProducerArbiter();
            this.queue = new ConcurrentLinkedQueue();
            add(Subscriptions.create(new C12051()));
        }

        public void onStart() {
            request(2);
        }

        private void requestFromChild(long n) {
            if (n > 0) {
                long previous = BackpressureUtils.getAndAddRequest(REQUESTED, this, n);
                this.arbiter.request(n);
                if (previous == 0 && this.currentSubscriber == null && this.wip > 0) {
                    subscribeNext();
                }
            }
        }

        private void decrementRequested() {
            REQUESTED.decrementAndGet(this);
        }

        public void onNext(Observable<? extends T> t) {
            this.queue.add(this.nl.next(t));
            if (WIP.getAndIncrement(this) == 0) {
                subscribeNext();
            }
        }

        public void onError(Throwable e) {
            this.child.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            this.queue.add(this.nl.completed());
            if (WIP.getAndIncrement(this) == 0) {
                subscribeNext();
            }
        }

        void completeInner() {
            this.currentSubscriber = null;
            if (WIP.decrementAndGet(this) > 0) {
                subscribeNext();
            }
            request(1);
        }

        void subscribeNext() {
            if (this.requested > 0) {
                Object o = this.queue.poll();
                if (this.nl.isCompleted(o)) {
                    this.child.onCompleted();
                    return;
                } else if (o != null) {
                    Observable<? extends T> obs = (Observable) this.nl.getValue(o);
                    this.currentSubscriber = new ConcatInnerSubscriber(this, this.child, this.arbiter);
                    this.current.set(this.currentSubscriber);
                    obs.unsafeSubscribe(this.currentSubscriber);
                    return;
                } else {
                    return;
                }
            }
            if (this.nl.isCompleted(this.queue.peek())) {
                this.child.onCompleted();
            }
        }
    }

    private static final class Holder {
        static final OperatorConcat<Object> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorConcat();
        }
    }

    public static <T> OperatorConcat<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorConcat() {
    }

    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> child) {
        SerializedSubscriber<T> s = new SerializedSubscriber(child);
        SerialSubscription current = new SerialSubscription();
        child.add(current);
        ConcatSubscriber<T> cs = new ConcatSubscriber(s, current);
        child.setProducer(new ConcatProducer(cs));
        return cs;
    }
}
