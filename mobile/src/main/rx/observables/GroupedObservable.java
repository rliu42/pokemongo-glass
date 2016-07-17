package rx.observables;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

public class GroupedObservable<K, T> extends Observable<T> {
    private final K key;

    /* renamed from: rx.observables.GroupedObservable.1 */
    static class C13541 implements OnSubscribe<T> {
        final /* synthetic */ Observable val$o;

        C13541(Observable observable) {
            this.val$o = observable;
        }

        public void call(Subscriber<? super T> s) {
            this.val$o.unsafeSubscribe(s);
        }
    }

    public static <K, T> GroupedObservable<K, T> from(K key, Observable<T> o) {
        return new GroupedObservable(key, new C13541(o));
    }

    public static final <K, T> GroupedObservable<K, T> create(K key, OnSubscribe<T> f) {
        return new GroupedObservable(key, f);
    }

    protected GroupedObservable(K key, OnSubscribe<T> onSubscribe) {
        super(onSubscribe);
        this.key = key;
    }

    public K getKey() {
        return this.key;
    }
}
