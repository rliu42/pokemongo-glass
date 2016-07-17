package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import rx.Observable.Operator;
import rx.Subscriber;

public final class OperatorTakeLast<T> implements Operator<T, T> {
    private final int count;

    /* renamed from: rx.internal.operators.OperatorTakeLast.1 */
    class C12941 extends Subscriber<T> {
        final /* synthetic */ Deque val$deque;
        final /* synthetic */ NotificationLite val$notification;
        final /* synthetic */ TakeLastQueueProducer val$producer;
        final /* synthetic */ Subscriber val$subscriber;

        C12941(Subscriber x0, Deque deque, NotificationLite notificationLite, TakeLastQueueProducer takeLastQueueProducer, Subscriber subscriber) {
            this.val$deque = deque;
            this.val$notification = notificationLite;
            this.val$producer = takeLastQueueProducer;
            this.val$subscriber = subscriber;
            super(x0);
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onCompleted() {
            this.val$deque.offer(this.val$notification.completed());
            this.val$producer.startEmitting();
        }

        public void onError(Throwable e) {
            this.val$deque.clear();
            this.val$subscriber.onError(e);
        }

        public void onNext(T value) {
            if (OperatorTakeLast.this.count != 0) {
                if (this.val$deque.size() == OperatorTakeLast.this.count) {
                    this.val$deque.removeFirst();
                }
                this.val$deque.offerLast(this.val$notification.next(value));
            }
        }
    }

    public OperatorTakeLast(int count) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count cannot be negative");
        }
        this.count = count;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Deque<Object> deque = new ArrayDeque();
        NotificationLite<T> notification = NotificationLite.instance();
        TakeLastQueueProducer<T> producer = new TakeLastQueueProducer(notification, deque, subscriber);
        subscriber.setProducer(producer);
        return new C12941(subscriber, deque, notification, producer, subscriber);
    }
}
