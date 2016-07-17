package rx.internal.util;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Subscription;
import rx.functions.Func1;

public final class SubscriptionIndexedRingBuffer<T extends Subscription> implements Subscription {
    private static final Func1<Subscription, Boolean> UNSUBSCRIBE;
    private static final AtomicIntegerFieldUpdater<SubscriptionIndexedRingBuffer> UNSUBSCRIBED;
    private volatile IndexedRingBuffer<T> subscriptions;
    private volatile int unsubscribed;

    /* renamed from: rx.internal.util.SubscriptionIndexedRingBuffer.1 */
    static class C13471 implements Func1<Subscription, Boolean> {
        C13471() {
        }

        public Boolean call(Subscription s) {
            s.unsubscribe();
            return Boolean.TRUE;
        }
    }

    static {
        UNSUBSCRIBED = AtomicIntegerFieldUpdater.newUpdater(SubscriptionIndexedRingBuffer.class, "unsubscribed");
        UNSUBSCRIBE = new C13471();
    }

    public SubscriptionIndexedRingBuffer() {
        this.subscriptions = IndexedRingBuffer.getInstance();
        this.unsubscribed = 0;
    }

    public boolean isUnsubscribed() {
        return this.unsubscribed == 1;
    }

    public synchronized int add(T s) {
        int i;
        if (this.unsubscribed == 1 || this.subscriptions == null) {
            s.unsubscribe();
            i = -1;
        } else {
            i = this.subscriptions.add(s);
            if (this.unsubscribed == 1) {
                s.unsubscribe();
            }
        }
        return i;
    }

    public void remove(int n) {
        if (this.unsubscribed != 1 && this.subscriptions != null && n >= 0) {
            Subscription t = (Subscription) this.subscriptions.remove(n);
            if (t != null && t != null) {
                t.unsubscribe();
            }
        }
    }

    public void removeSilently(int n) {
        if (this.unsubscribed != 1 && this.subscriptions != null && n >= 0) {
            this.subscriptions.remove(n);
        }
    }

    public void unsubscribe() {
        if (UNSUBSCRIBED.compareAndSet(this, 0, 1) && this.subscriptions != null) {
            unsubscribeFromAll(this.subscriptions);
            IndexedRingBuffer<T> s = this.subscriptions;
            this.subscriptions = null;
            s.unsubscribe();
        }
    }

    public int forEach(Func1<T, Boolean> action) {
        return forEach(action, 0);
    }

    public synchronized int forEach(Func1<T, Boolean> action, int startIndex) {
        int i;
        if (this.unsubscribed == 1 || this.subscriptions == null) {
            i = 0;
        } else {
            i = this.subscriptions.forEach(action, startIndex);
        }
        return i;
    }

    private static void unsubscribeFromAll(IndexedRingBuffer<? extends Subscription> subscriptions) {
        if (subscriptions != null) {
            subscriptions.forEach(UNSUBSCRIBE);
        }
    }
}
