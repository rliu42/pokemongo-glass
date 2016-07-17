package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;

public final class BlockingOperatorToIterator {

    /* renamed from: rx.internal.operators.BlockingOperatorToIterator.1 */
    static class C11571 extends Subscriber<Notification<? extends T>> {
        final /* synthetic */ BlockingQueue val$notifications;

        C11571(BlockingQueue blockingQueue) {
            this.val$notifications = blockingQueue;
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            this.val$notifications.offer(Notification.createOnError(e));
        }

        public void onNext(Notification<? extends T> args) {
            this.val$notifications.offer(args);
        }
    }

    /* renamed from: rx.internal.operators.BlockingOperatorToIterator.2 */
    static class C11582 implements Iterator<T> {
        private Notification<? extends T> buf;
        final /* synthetic */ BlockingQueue val$notifications;
        final /* synthetic */ Subscription val$subscription;

        C11582(BlockingQueue blockingQueue, Subscription subscription) {
            this.val$notifications = blockingQueue;
            this.val$subscription = subscription;
        }

        public boolean hasNext() {
            if (this.buf == null) {
                this.buf = take();
            }
            if (!this.buf.isOnError()) {
                return !this.buf.isOnCompleted();
            } else {
                throw Exceptions.propagate(this.buf.getThrowable());
            }
        }

        public T next() {
            if (hasNext()) {
                T result = this.buf.getValue();
                this.buf = null;
                return result;
            }
            throw new NoSuchElementException();
        }

        private Notification<? extends T> take() {
            try {
                return (Notification) this.val$notifications.take();
            } catch (InterruptedException e) {
                this.val$subscription.unsubscribe();
                throw Exceptions.propagate(e);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Read-only iterator");
        }
    }

    private BlockingOperatorToIterator() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterator<T> toIterator(Observable<? extends T> source) {
        BlockingQueue<Notification<? extends T>> notifications = new LinkedBlockingQueue();
        return new C11582(notifications, source.materialize().subscribe(new C11571(notifications)));
    }
}
