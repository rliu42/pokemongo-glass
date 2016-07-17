package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Subscriber;

public final class OperatorTakeLastTimed<T> implements Operator<T, T> {
    private final long ageMillis;
    private final int count;
    private final Scheduler scheduler;

    /* renamed from: rx.internal.operators.OperatorTakeLastTimed.1 */
    class C12961 extends Subscriber<T> {
        final /* synthetic */ Deque val$buffer;
        final /* synthetic */ NotificationLite val$notification;
        final /* synthetic */ TakeLastQueueProducer val$producer;
        final /* synthetic */ Subscriber val$subscriber;
        final /* synthetic */ Deque val$timestampBuffer;

        C12961(Subscriber x0, Deque deque, Deque deque2, NotificationLite notificationLite, Subscriber subscriber, TakeLastQueueProducer takeLastQueueProducer) {
            this.val$buffer = deque;
            this.val$timestampBuffer = deque2;
            this.val$notification = notificationLite;
            this.val$subscriber = subscriber;
            this.val$producer = takeLastQueueProducer;
            super(x0);
        }

        protected void runEvictionPolicy(long now) {
            while (OperatorTakeLastTimed.this.count >= 0 && this.val$buffer.size() > OperatorTakeLastTimed.this.count) {
                this.val$timestampBuffer.pollFirst();
                this.val$buffer.pollFirst();
            }
            while (!this.val$buffer.isEmpty() && ((Long) this.val$timestampBuffer.peekFirst()).longValue() < now - OperatorTakeLastTimed.this.ageMillis) {
                this.val$timestampBuffer.pollFirst();
                this.val$buffer.pollFirst();
            }
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(T args) {
            long t = OperatorTakeLastTimed.this.scheduler.now();
            this.val$timestampBuffer.add(Long.valueOf(t));
            this.val$buffer.add(this.val$notification.next(args));
            runEvictionPolicy(t);
        }

        public void onError(Throwable e) {
            this.val$timestampBuffer.clear();
            this.val$buffer.clear();
            this.val$subscriber.onError(e);
        }

        public void onCompleted() {
            runEvictionPolicy(OperatorTakeLastTimed.this.scheduler.now());
            this.val$timestampBuffer.clear();
            this.val$buffer.offer(this.val$notification.completed());
            this.val$producer.startEmitting();
        }
    }

    public OperatorTakeLastTimed(long time, TimeUnit unit, Scheduler scheduler) {
        this.ageMillis = unit.toMillis(time);
        this.scheduler = scheduler;
        this.count = -1;
    }

    public OperatorTakeLastTimed(int count, long time, TimeUnit unit, Scheduler scheduler) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count could not be negative");
        }
        this.ageMillis = unit.toMillis(time);
        this.scheduler = scheduler;
        this.count = count;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Deque<Object> buffer = new ArrayDeque();
        Deque<Long> timestampBuffer = new ArrayDeque();
        NotificationLite<T> notification = NotificationLite.instance();
        TakeLastQueueProducer<T> producer = new TakeLastQueueProducer(notification, buffer, subscriber);
        subscriber.setProducer(producer);
        return new C12961(subscriber, buffer, timestampBuffer, notification, subscriber, producer);
    }
}
