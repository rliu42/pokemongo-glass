package com.upsight.android.internal.persistence.subscription;

import com.squareup.otto.Bus;
import com.upsight.android.internal.persistence.subscription.DataStoreEvent.Action;
import com.upsight.android.persistence.UpsightSubscription;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class Subscriptions {
    public static final int MAX_QUEUE_LENGTH = 10;

    /* renamed from: com.upsight.android.internal.persistence.subscription.Subscriptions.1 */
    static class C09231 implements Action1<T> {
        final /* synthetic */ Bus val$bus;
        final /* synthetic */ String val$type;

        C09231(Bus bus, String str) {
            this.val$bus = bus;
            this.val$type = str;
        }

        public void call(T t) {
            this.val$bus.post(new DataStoreEvent(Action.Created, this.val$type, t));
        }
    }

    /* renamed from: com.upsight.android.internal.persistence.subscription.Subscriptions.2 */
    static class C09242 implements Action1<T> {
        final /* synthetic */ Bus val$bus;
        final /* synthetic */ String val$type;

        C09242(Bus bus, String str) {
            this.val$bus = bus;
            this.val$type = str;
        }

        public void call(T t) {
            this.val$bus.post(new DataStoreEvent(Action.Updated, this.val$type, t));
        }
    }

    /* renamed from: com.upsight.android.internal.persistence.subscription.Subscriptions.3 */
    static class C09253 implements Action1<T> {
        final /* synthetic */ Bus val$bus;
        final /* synthetic */ String val$type;

        C09253(Bus bus, String str) {
            this.val$bus = bus;
            this.val$type = str;
        }

        public void call(T t) {
            this.val$bus.post(new DataStoreEvent(Action.Removed, this.val$type, t));
        }
    }

    public static <T> Action1<T> publishCreated(Bus bus, String type) {
        return new C09231(bus, type);
    }

    public static <T> Action1<T> publishUpdated(Bus bus, String type) {
        return new C09242(bus, type);
    }

    public static <T> Action1<T> publishRemoved(Bus bus, String type) {
        return new C09253(bus, type);
    }

    public static Observable<DataStoreEvent> toObservable(Bus bus) {
        return Observable.create(new OnSubscribeBus(bus)).onBackpressureBlock(MAX_QUEUE_LENGTH);
    }

    public static UpsightSubscription from(Subscription subscription) {
        return new SubscriptionAdapter(subscription);
    }

    public static AnnotatedSubscriber create(Object target) {
        SubscriptionHandlerVisitor visitor = new SubscriptionHandlerVisitor(target);
        new ClassSubscriptionReader(target.getClass()).accept(visitor);
        return new AnnotatedSubscriber(visitor.getHandlers());
    }

    private Subscriptions() {
    }
}
