package rx.internal.operators;

import java.util.Deque;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Producer;
import rx.Subscriber;

final class TakeLastQueueProducer<T> implements Producer {
    private static final AtomicLongFieldUpdater<TakeLastQueueProducer> REQUESTED_UPDATER;
    private final Deque<Object> deque;
    private volatile boolean emittingStarted;
    private final NotificationLite<T> notification;
    private volatile long requested;
    private final Subscriber<? super T> subscriber;

    public TakeLastQueueProducer(NotificationLite<T> n, Deque<Object> q, Subscriber<? super T> subscriber) {
        this.emittingStarted = false;
        this.requested = 0;
        this.notification = n;
        this.deque = q;
        this.subscriber = subscriber;
    }

    static {
        REQUESTED_UPDATER = AtomicLongFieldUpdater.newUpdater(TakeLastQueueProducer.class, "requested");
    }

    void startEmitting() {
        if (!this.emittingStarted) {
            this.emittingStarted = true;
            emit(0);
        }
    }

    public void request(long n) {
        if (this.requested != Long.MAX_VALUE) {
            long _c;
            if (n == Long.MAX_VALUE) {
                _c = REQUESTED_UPDATER.getAndSet(this, Long.MAX_VALUE);
            } else {
                _c = BackpressureUtils.getAndAddRequest(REQUESTED_UPDATER, this, n);
            }
            if (this.emittingStarted) {
                emit(_c);
            }
        }
    }

    void emit(long previousRequested) {
        if (this.requested == Long.MAX_VALUE) {
            if (previousRequested == 0) {
                try {
                    for (Object value : this.deque) {
                        if (!this.subscriber.isUnsubscribed()) {
                            this.notification.accept(this.subscriber, value);
                        } else {
                            return;
                        }
                    }
                    this.deque.clear();
                } catch (Throwable e) {
                    this.subscriber.onError(e);
                } finally {
                    this.deque.clear();
                }
            }
        } else if (previousRequested == 0) {
            while (true) {
                long newRequested;
                long numToEmit = this.requested;
                int emitted = 0;
                while (true) {
                    numToEmit--;
                    if (numToEmit < 0) {
                        break;
                    }
                    Object o = this.deque.poll();
                    if (o == null) {
                        break;
                    } else if (!this.subscriber.isUnsubscribed() && !this.notification.accept(this.subscriber, o)) {
                        emitted++;
                    } else {
                        return;
                    }
                }
                long oldRequested;
                do {
                    oldRequested = this.requested;
                    newRequested = oldRequested - ((long) emitted);
                    if (oldRequested == Long.MAX_VALUE) {
                        break;
                    }
                } while (!REQUESTED_UPDATER.compareAndSet(this, oldRequested, newRequested));
                if (newRequested == 0) {
                    return;
                }
            }
        }
    }
}
