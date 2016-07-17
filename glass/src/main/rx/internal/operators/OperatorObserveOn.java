package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Observable.Operator;
import rx.Producer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.MissingBackpressureException;
import rx.functions.Action0;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.SynchronizedQueue;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.schedulers.ImmediateScheduler;
import rx.schedulers.TrampolineScheduler;

public final class OperatorObserveOn<T> implements Operator<T, T> {
    private final Scheduler scheduler;

    private static final class ObserveOnSubscriber<T> extends Subscriber<T> {
        static final AtomicLongFieldUpdater<ObserveOnSubscriber> COUNTER_UPDATER;
        static final AtomicLongFieldUpdater<ObserveOnSubscriber> REQUESTED;
        final Action0 action;
        final Subscriber<? super T> child;
        volatile long counter;
        volatile Throwable error;
        volatile boolean finished;
        final NotificationLite<T> on;
        final Queue<Object> queue;
        final Worker recursiveScheduler;
        volatile long requested;
        final ScheduledUnsubscribe scheduledUnsubscribe;

        /* renamed from: rx.internal.operators.OperatorObserveOn.ObserveOnSubscriber.1 */
        class C12421 implements Producer {
            C12421() {
            }

            public void request(long n) {
                BackpressureUtils.getAndAddRequest(ObserveOnSubscriber.REQUESTED, ObserveOnSubscriber.this, n);
                ObserveOnSubscriber.this.schedule();
            }
        }

        /* renamed from: rx.internal.operators.OperatorObserveOn.ObserveOnSubscriber.2 */
        class C12432 implements Action0 {
            C12432() {
            }

            public void call() {
                ObserveOnSubscriber.this.pollQueue();
            }
        }

        static {
            REQUESTED = AtomicLongFieldUpdater.newUpdater(ObserveOnSubscriber.class, "requested");
            COUNTER_UPDATER = AtomicLongFieldUpdater.newUpdater(ObserveOnSubscriber.class, "counter");
        }

        public ObserveOnSubscriber(Scheduler scheduler, Subscriber<? super T> child) {
            this.on = NotificationLite.instance();
            this.finished = false;
            this.requested = 0;
            this.action = new C12432();
            this.child = child;
            this.recursiveScheduler = scheduler.createWorker();
            if (UnsafeAccess.isUnsafeAvailable()) {
                this.queue = new SpscArrayQueue(RxRingBuffer.SIZE);
            } else {
                this.queue = new SynchronizedQueue(RxRingBuffer.SIZE);
            }
            this.scheduledUnsubscribe = new ScheduledUnsubscribe(this.recursiveScheduler);
        }

        void init() {
            this.child.add(this.scheduledUnsubscribe);
            this.child.setProducer(new C12421());
            this.child.add(this.recursiveScheduler);
            this.child.add(this);
        }

        public void onStart() {
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            if (!isUnsubscribed()) {
                if (this.queue.offer(this.on.next(t))) {
                    schedule();
                } else {
                    onError(new MissingBackpressureException());
                }
            }
        }

        public void onCompleted() {
            if (!isUnsubscribed() && !this.finished) {
                this.finished = true;
                schedule();
            }
        }

        public void onError(Throwable e) {
            if (!isUnsubscribed() && !this.finished) {
                this.error = e;
                unsubscribe();
                this.finished = true;
                schedule();
            }
        }

        protected void schedule() {
            if (COUNTER_UPDATER.getAndIncrement(this) == 0) {
                this.recursiveScheduler.schedule(this.action);
            }
        }

        void pollQueue() {
            int emitted = 0;
            do {
                this.counter = 1;
                long produced = 0;
                long r = this.requested;
                while (!this.child.isUnsubscribed()) {
                    if (this.finished) {
                        Throwable error = this.error;
                        if (error != null) {
                            this.queue.clear();
                            this.child.onError(error);
                            return;
                        } else if (this.queue.isEmpty()) {
                            this.child.onCompleted();
                            return;
                        }
                    }
                    if (r > 0) {
                        Object o = this.queue.poll();
                        if (o != null) {
                            this.child.onNext(this.on.getValue(o));
                            r--;
                            emitted++;
                            produced++;
                        }
                    }
                    if (produced > 0 && this.requested != Long.MAX_VALUE) {
                        REQUESTED.addAndGet(this, -produced);
                    }
                }
                return;
            } while (COUNTER_UPDATER.decrementAndGet(this) > 0);
            if (emitted > 0) {
                request((long) emitted);
            }
        }
    }

    static final class ScheduledUnsubscribe implements Subscription {
        static final AtomicIntegerFieldUpdater<ScheduledUnsubscribe> ONCE_UPDATER;
        volatile int once;
        volatile boolean unsubscribed;
        final Worker worker;

        /* renamed from: rx.internal.operators.OperatorObserveOn.ScheduledUnsubscribe.1 */
        class C12441 implements Action0 {
            C12441() {
            }

            public void call() {
                ScheduledUnsubscribe.this.worker.unsubscribe();
                ScheduledUnsubscribe.this.unsubscribed = true;
            }
        }

        static {
            ONCE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(ScheduledUnsubscribe.class, "once");
        }

        public ScheduledUnsubscribe(Worker worker) {
            this.unsubscribed = false;
            this.worker = worker;
        }

        public boolean isUnsubscribed() {
            return this.unsubscribed;
        }

        public void unsubscribe() {
            if (ONCE_UPDATER.getAndSet(this, 1) == 0) {
                this.worker.schedule(new C12441());
            }
        }
    }

    public OperatorObserveOn(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        if ((this.scheduler instanceof ImmediateScheduler) || (this.scheduler instanceof TrampolineScheduler)) {
            return child;
        }
        Subscriber parent = new ObserveOnSubscriber(this.scheduler, child);
        parent.init();
        return parent;
    }
}
