package rx.internal.operators;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

public final class OnSubscribeCache<T> implements OnSubscribe<T> {
    static final AtomicIntegerFieldUpdater<OnSubscribeCache> SRC_SUBSCRIBED_UPDATER;
    protected final Subject<? super T, ? extends T> cache;
    protected final Observable<? extends T> source;
    volatile int sourceSubscribed;

    static {
        SRC_SUBSCRIBED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(OnSubscribeCache.class, "sourceSubscribed");
    }

    public OnSubscribeCache(Observable<? extends T> source) {
        this((Observable) source, ReplaySubject.create());
    }

    public OnSubscribeCache(Observable<? extends T> source, int capacity) {
        this((Observable) source, ReplaySubject.create(capacity));
    }

    OnSubscribeCache(Observable<? extends T> source, Subject<? super T, ? extends T> cache) {
        this.source = source;
        this.cache = cache;
    }

    public void call(Subscriber<? super T> s) {
        if (SRC_SUBSCRIBED_UPDATER.compareAndSet(this, 0, 1)) {
            this.source.subscribe(this.cache);
        }
        this.cache.unsafeSubscribe(s);
    }
}
