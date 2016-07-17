package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Subscriber;

public final class OperatorThrottleFirst<T> implements Operator<T, T> {
    private final Scheduler scheduler;
    private final long timeInMilliseconds;

    /* renamed from: rx.internal.operators.OperatorThrottleFirst.1 */
    class C13021 extends Subscriber<T> {
        private long lastOnNext;
        final /* synthetic */ Subscriber val$subscriber;

        C13021(Subscriber x0, Subscriber subscriber) {
            this.val$subscriber = subscriber;
            super(x0);
            this.lastOnNext = 0;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(T v) {
            long now = OperatorThrottleFirst.this.scheduler.now();
            if (this.lastOnNext == 0 || now - this.lastOnNext >= OperatorThrottleFirst.this.timeInMilliseconds) {
                this.lastOnNext = now;
                this.val$subscriber.onNext(v);
            }
        }

        public void onCompleted() {
            this.val$subscriber.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$subscriber.onError(e);
        }
    }

    public OperatorThrottleFirst(long windowDuration, TimeUnit unit, Scheduler scheduler) {
        this.timeInMilliseconds = unit.toMillis(windowDuration);
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        return new C13021(subscriber, subscriber);
    }
}
