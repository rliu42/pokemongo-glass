package rx.internal.operators;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Observable;
import rx.Observable.Operator;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.SerialSubscription;

public final class OperatorRetryWithPredicate<T> implements Operator<T, Observable<T>> {
    final Func2<Integer, Throwable, Boolean> predicate;

    static final class SourceSubscriber<T> extends Subscriber<Observable<T>> {
        static final AtomicIntegerFieldUpdater<SourceSubscriber> ATTEMPTS_UPDATER;
        volatile int attempts;
        final Subscriber<? super T> child;
        final Worker inner;
        final Func2<Integer, Throwable, Boolean> predicate;
        final SerialSubscription serialSubscription;

        /* renamed from: rx.internal.operators.OperatorRetryWithPredicate.SourceSubscriber.1 */
        class C12661 implements Action0 {
            final /* synthetic */ Observable val$o;

            /* renamed from: rx.internal.operators.OperatorRetryWithPredicate.SourceSubscriber.1.1 */
            class C12651 extends Subscriber<T> {
                boolean done;
                final /* synthetic */ Action0 val$_self;

                C12651(Action0 action0) {
                    this.val$_self = action0;
                }

                public void onCompleted() {
                    if (!this.done) {
                        this.done = true;
                        SourceSubscriber.this.child.onCompleted();
                    }
                }

                public void onError(Throwable e) {
                    if (!this.done) {
                        this.done = true;
                        if (!((Boolean) SourceSubscriber.this.predicate.call(Integer.valueOf(SourceSubscriber.this.attempts), e)).booleanValue() || SourceSubscriber.this.inner.isUnsubscribed()) {
                            SourceSubscriber.this.child.onError(e);
                        } else {
                            SourceSubscriber.this.inner.schedule(this.val$_self);
                        }
                    }
                }

                public void onNext(T v) {
                    if (!this.done) {
                        SourceSubscriber.this.child.onNext(v);
                    }
                }
            }

            C12661(Observable observable) {
                this.val$o = observable;
            }

            public void call() {
                SourceSubscriber.ATTEMPTS_UPDATER.incrementAndGet(SourceSubscriber.this);
                Subscriber<T> subscriber = new C12651(this);
                SourceSubscriber.this.serialSubscription.set(subscriber);
                this.val$o.unsafeSubscribe(subscriber);
            }
        }

        static {
            ATTEMPTS_UPDATER = AtomicIntegerFieldUpdater.newUpdater(SourceSubscriber.class, "attempts");
        }

        public SourceSubscriber(Subscriber<? super T> child, Func2<Integer, Throwable, Boolean> predicate, Worker inner, SerialSubscription serialSubscription) {
            this.child = child;
            this.predicate = predicate;
            this.inner = inner;
            this.serialSubscription = serialSubscription;
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }

        public void onNext(Observable<T> o) {
            this.inner.schedule(new C12661(o));
        }
    }

    public OperatorRetryWithPredicate(Func2<Integer, Throwable, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super Observable<T>> call(Subscriber<? super T> child) {
        Worker inner = Schedulers.trampoline().createWorker();
        child.add(inner);
        SerialSubscription serialSubscription = new SerialSubscription();
        child.add(serialSubscription);
        return new SourceSubscriber(child, this.predicate, inner, serialSubscription);
    }
}
