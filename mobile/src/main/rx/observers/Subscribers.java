package rx.observers;

import rx.Observer;
import rx.Subscriber;
import rx.annotations.Experimental;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;

public final class Subscribers {

    /* renamed from: rx.observers.Subscribers.1 */
    static class C13591 extends Subscriber<T> {
        final /* synthetic */ Observer val$o;

        C13591(Observer observer) {
            this.val$o = observer;
        }

        public void onCompleted() {
            this.val$o.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$o.onError(e);
        }

        public void onNext(T t) {
            this.val$o.onNext(t);
        }
    }

    /* renamed from: rx.observers.Subscribers.2 */
    static class C13602 extends Subscriber<T> {
        final /* synthetic */ Action1 val$onNext;

        C13602(Action1 action1) {
            this.val$onNext = action1;
        }

        public final void onCompleted() {
        }

        public final void onError(Throwable e) {
            throw new OnErrorNotImplementedException(e);
        }

        public final void onNext(T args) {
            this.val$onNext.call(args);
        }
    }

    /* renamed from: rx.observers.Subscribers.3 */
    static class C13613 extends Subscriber<T> {
        final /* synthetic */ Action1 val$onError;
        final /* synthetic */ Action1 val$onNext;

        C13613(Action1 action1, Action1 action12) {
            this.val$onError = action1;
            this.val$onNext = action12;
        }

        public final void onCompleted() {
        }

        public final void onError(Throwable e) {
            this.val$onError.call(e);
        }

        public final void onNext(T args) {
            this.val$onNext.call(args);
        }
    }

    /* renamed from: rx.observers.Subscribers.4 */
    static class C13624 extends Subscriber<T> {
        final /* synthetic */ Action0 val$onComplete;
        final /* synthetic */ Action1 val$onError;
        final /* synthetic */ Action1 val$onNext;

        C13624(Action0 action0, Action1 action1, Action1 action12) {
            this.val$onComplete = action0;
            this.val$onError = action1;
            this.val$onNext = action12;
        }

        public final void onCompleted() {
            this.val$onComplete.call();
        }

        public final void onError(Throwable e) {
            this.val$onError.call(e);
        }

        public final void onNext(T args) {
            this.val$onNext.call(args);
        }
    }

    /* renamed from: rx.observers.Subscribers.5 */
    static class C13635 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$subscriber;

        C13635(Subscriber x0, Subscriber subscriber) {
            this.val$subscriber = subscriber;
            super(x0);
        }

        public void onCompleted() {
            this.val$subscriber.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$subscriber.onError(e);
        }

        public void onNext(T t) {
            this.val$subscriber.onNext(t);
        }
    }

    private Subscribers() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Subscriber<T> empty() {
        return from(Observers.empty());
    }

    public static <T> Subscriber<T> from(Observer<? super T> o) {
        return new C13591(o);
    }

    public static final <T> Subscriber<T> create(Action1<? super T> onNext) {
        if (onNext != null) {
            return new C13602(onNext);
        }
        throw new IllegalArgumentException("onNext can not be null");
    }

    public static final <T> Subscriber<T> create(Action1<? super T> onNext, Action1<Throwable> onError) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (onError != null) {
            return new C13613(onError, onNext);
        } else {
            throw new IllegalArgumentException("onError can not be null");
        }
    }

    public static final <T> Subscriber<T> create(Action1<? super T> onNext, Action1<Throwable> onError, Action0 onComplete) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        } else if (onComplete != null) {
            return new C13624(onComplete, onError, onNext);
        } else {
            throw new IllegalArgumentException("onComplete can not be null");
        }
    }

    @Experimental
    public static <T> Subscriber<T> wrap(Subscriber<? super T> subscriber) {
        return new C13635(subscriber, subscriber);
    }
}
