package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;

public final class OperatorFilter<T> implements Operator<T, T> {
    private final Func1<? super T, Boolean> predicate;

    /* renamed from: rx.internal.operators.OperatorFilter.1 */
    class C12231 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$child;

        C12231(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
        }

        public void onCompleted() {
            this.val$child.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$child.onError(e);
        }

        public void onNext(T t) {
            try {
                if (((Boolean) OperatorFilter.this.predicate.call(t)).booleanValue()) {
                    this.val$child.onNext(t);
                } else {
                    request(1);
                }
            } catch (Throwable e) {
                this.val$child.onError(OnErrorThrowable.addValueAsLastCause(e, t));
            }
        }
    }

    public OperatorFilter(Func1<? super T, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        return new C12231(child, child);
    }
}
