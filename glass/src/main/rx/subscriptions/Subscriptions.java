package rx.subscriptions;

import java.util.concurrent.Future;
import rx.Subscription;
import rx.annotations.Experimental;
import rx.functions.Action0;

public final class Subscriptions {
    private static final Unsubscribed UNSUBSCRIBED;

    private static final class FutureSubscription implements Subscription {
        final Future<?> f929f;

        public FutureSubscription(Future<?> f) {
            this.f929f = f;
        }

        public void unsubscribe() {
            this.f929f.cancel(true);
        }

        public boolean isUnsubscribed() {
            return this.f929f.isCancelled();
        }
    }

    private static final class Unsubscribed implements Subscription {
        private Unsubscribed() {
        }

        public void unsubscribe() {
        }

        public boolean isUnsubscribed() {
            return true;
        }
    }

    private Subscriptions() {
        throw new IllegalStateException("No instances!");
    }

    public static Subscription empty() {
        return BooleanSubscription.create();
    }

    @Experimental
    public static Subscription unsubscribed() {
        return UNSUBSCRIBED;
    }

    public static Subscription create(Action0 unsubscribe) {
        return BooleanSubscription.create(unsubscribe);
    }

    public static Subscription from(Future<?> f) {
        return new FutureSubscription(f);
    }

    public static CompositeSubscription from(Subscription... subscriptions) {
        return new CompositeSubscription(subscriptions);
    }

    static {
        UNSUBSCRIBED = new Unsubscribed();
    }
}
