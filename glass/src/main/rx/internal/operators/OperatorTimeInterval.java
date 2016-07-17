package rx.internal.operators;

import rx.Observable.Operator;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.TimeInterval;

public final class OperatorTimeInterval<T> implements Operator<TimeInterval<T>, T> {
    private final Scheduler scheduler;

    /* renamed from: rx.internal.operators.OperatorTimeInterval.1 */
    class C13031 extends Subscriber<T> {
        private long lastTimestamp;
        final /* synthetic */ Subscriber val$subscriber;

        C13031(Subscriber x0, Subscriber subscriber) {
            this.val$subscriber = subscriber;
            super(x0);
            this.lastTimestamp = OperatorTimeInterval.this.scheduler.now();
        }

        public void onNext(T args) {
            long nowTimestamp = OperatorTimeInterval.this.scheduler.now();
            this.val$subscriber.onNext(new TimeInterval(nowTimestamp - this.lastTimestamp, args));
            this.lastTimestamp = nowTimestamp;
        }

        public void onCompleted() {
            this.val$subscriber.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$subscriber.onError(e);
        }
    }

    public OperatorTimeInterval(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super TimeInterval<T>> subscriber) {
        return new C13031(subscriber, subscriber);
    }
}
