package rx.internal.operators;

import rx.Notification;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;

public final class OperatorMaterialize<T> implements Operator<Notification<T>, T> {

    /* renamed from: rx.internal.operators.OperatorMaterialize.1 */
    class C12371 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$child;

        C12371(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
        }

        public void onCompleted() {
            this.val$child.onNext(Notification.createOnCompleted());
            this.val$child.onCompleted();
        }

        public void onError(Throwable e) {
            RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
            this.val$child.onNext(Notification.createOnError(e));
            this.val$child.onCompleted();
        }

        public void onNext(T t) {
            this.val$child.onNext(Notification.createOnNext(t));
        }
    }

    private static final class Holder {
        static final OperatorMaterialize<Object> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorMaterialize();
        }
    }

    public static <T> OperatorMaterialize<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorMaterialize() {
    }

    public Subscriber<? super T> call(Subscriber<? super Notification<T>> child) {
        return new C12371(child, child);
    }
}
