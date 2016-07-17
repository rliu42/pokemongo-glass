package rx;

import rx.annotations.Experimental;
import rx.internal.util.SubscriptionList;

@Experimental
public abstract class SingleSubscriber<T> implements Subscription {
    private final SubscriptionList cs;

    public abstract void onError(Throwable th);

    public abstract void onSuccess(T t);

    public SingleSubscriber() {
        this.cs = new SubscriptionList();
    }

    public final void add(Subscription s) {
        this.cs.add(s);
    }

    public final void unsubscribe() {
        this.cs.unsubscribe();
    }

    public final boolean isUnsubscribed() {
        return this.cs.isUnsubscribed();
    }
}
