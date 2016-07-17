package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.functions.Action0;

public final class OperatorFinally<T> implements Operator<T, T> {
    final Action0 action;

    /* renamed from: rx.internal.operators.OperatorFinally.1 */
    class C12241 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$child;

        C12241(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
        }

        public void onNext(T t) {
            this.val$child.onNext(t);
        }

        public void onError(Throwable e) {
            try {
                this.val$child.onError(e);
            } finally {
                OperatorFinally.this.action.call();
            }
        }

        public void onCompleted() {
            try {
                this.val$child.onCompleted();
            } finally {
                OperatorFinally.this.action.call();
            }
        }
    }

    public OperatorFinally(Action0 action) {
        this.action = action;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        return new C12241(child, child);
    }
}
