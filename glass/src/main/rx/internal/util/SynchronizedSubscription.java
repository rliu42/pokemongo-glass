package rx.internal.util;

import rx.Subscription;

public class SynchronizedSubscription implements Subscription {
    private final Subscription f924s;

    public SynchronizedSubscription(Subscription s) {
        this.f924s = s;
    }

    public synchronized void unsubscribe() {
        this.f924s.unsubscribe();
    }

    public synchronized boolean isUnsubscribed() {
        return this.f924s.isUnsubscribed();
    }
}
