package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import rx.Observable.Operator;
import rx.Subscriber;

public class OperatorSkipLast<T> implements Operator<T, T> {
    private final int count;

    /* renamed from: rx.internal.operators.OperatorSkipLast.1 */
    class C12771 extends Subscriber<T> {
        private final Deque<Object> deque;
        private final NotificationLite<T> on;
        final /* synthetic */ Subscriber val$subscriber;

        C12771(Subscriber x0, Subscriber subscriber) {
            this.val$subscriber = subscriber;
            super(x0);
            this.on = NotificationLite.instance();
            this.deque = new ArrayDeque();
        }

        public void onCompleted() {
            this.val$subscriber.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$subscriber.onError(e);
        }

        public void onNext(T value) {
            if (OperatorSkipLast.this.count == 0) {
                this.val$subscriber.onNext(value);
                return;
            }
            if (this.deque.size() == OperatorSkipLast.this.count) {
                this.val$subscriber.onNext(this.on.getValue(this.deque.removeFirst()));
            } else {
                request(1);
            }
            this.deque.offerLast(this.on.next(value));
        }
    }

    public OperatorSkipLast(int count) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count could not be negative");
        }
        this.count = count;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        return new C12771(subscriber, subscriber);
    }
}
