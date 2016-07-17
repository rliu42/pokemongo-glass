package rx.internal.operators;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public final class OnSubscribeToObservableFuture {

    static class ToObservableFuture<T> implements OnSubscribe<T> {
        private final Future<? extends T> that;
        private final long time;
        private final TimeUnit unit;

        /* renamed from: rx.internal.operators.OnSubscribeToObservableFuture.ToObservableFuture.1 */
        class C11871 implements Action0 {
            C11871() {
            }

            public void call() {
                ToObservableFuture.this.that.cancel(true);
            }
        }

        public ToObservableFuture(Future<? extends T> that) {
            this.that = that;
            this.time = 0;
            this.unit = null;
        }

        public ToObservableFuture(Future<? extends T> that, long time, TimeUnit unit) {
            this.that = that;
            this.time = time;
            this.unit = unit;
        }

        public void call(Subscriber<? super T> subscriber) {
            subscriber.add(Subscriptions.create(new C11871()));
            try {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(this.unit == null ? this.that.get() : this.that.get(this.time, this.unit));
                    subscriber.onCompleted();
                }
            } catch (Throwable e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            }
        }
    }

    private OnSubscribeToObservableFuture() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> OnSubscribe<T> toObservableFuture(Future<? extends T> that) {
        return new ToObservableFuture(that);
    }

    public static <T> OnSubscribe<T> toObservableFuture(Future<? extends T> that, long time, TimeUnit unit) {
        return new ToObservableFuture(that, time, unit);
    }
}
