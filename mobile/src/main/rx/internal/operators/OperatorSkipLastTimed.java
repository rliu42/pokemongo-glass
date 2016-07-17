package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Timestamped;

public class OperatorSkipLastTimed<T> implements Operator<T, T> {
    private final Scheduler scheduler;
    private final long timeInMillis;

    /* renamed from: rx.internal.operators.OperatorSkipLastTimed.1 */
    class C12781 extends Subscriber<T> {
        private Deque<Timestamped<T>> buffer;
        final /* synthetic */ Subscriber val$subscriber;

        C12781(Subscriber x0, Subscriber subscriber) {
            this.val$subscriber = subscriber;
            super(x0);
            this.buffer = new ArrayDeque();
        }

        private void emitItemsOutOfWindow(long now) {
            long limit = now - OperatorSkipLastTimed.this.timeInMillis;
            while (!this.buffer.isEmpty()) {
                Timestamped<T> v = (Timestamped) this.buffer.getFirst();
                if (v.getTimestampMillis() < limit) {
                    this.buffer.removeFirst();
                    this.val$subscriber.onNext(v.getValue());
                } else {
                    return;
                }
            }
        }

        public void onNext(T value) {
            long now = OperatorSkipLastTimed.this.scheduler.now();
            emitItemsOutOfWindow(now);
            this.buffer.offerLast(new Timestamped(now, value));
        }

        public void onError(Throwable e) {
            this.val$subscriber.onError(e);
        }

        public void onCompleted() {
            emitItemsOutOfWindow(OperatorSkipLastTimed.this.scheduler.now());
            this.val$subscriber.onCompleted();
        }
    }

    public OperatorSkipLastTimed(long time, TimeUnit unit, Scheduler scheduler) {
        this.timeInMillis = unit.toMillis(time);
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        return new C12781(subscriber, subscriber);
    }
}
