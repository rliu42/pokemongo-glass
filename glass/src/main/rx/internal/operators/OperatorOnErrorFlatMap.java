package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;
import rx.plugins.RxJavaPlugins;

public final class OperatorOnErrorFlatMap<T> implements Operator<T, T> {
    private final Func1<OnErrorThrowable, ? extends Observable<? extends T>> resumeFunction;

    /* renamed from: rx.internal.operators.OperatorOnErrorFlatMap.1 */
    class C12501 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$child;

        /* renamed from: rx.internal.operators.OperatorOnErrorFlatMap.1.1 */
        class C12491 extends Subscriber<T> {
            C12491() {
            }

            public void onCompleted() {
            }

            public void onError(Throwable e) {
                C12501.this.val$child.onError(e);
            }

            public void onNext(T t) {
                C12501.this.val$child.onNext(t);
            }
        }

        C12501(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
        }

        public void onCompleted() {
            this.val$child.onCompleted();
        }

        public void onError(Throwable e) {
            try {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                ((Observable) OperatorOnErrorFlatMap.this.resumeFunction.call(OnErrorThrowable.from(e))).unsafeSubscribe(new C12491());
            } catch (Throwable e2) {
                this.val$child.onError(e2);
            }
        }

        public void onNext(T t) {
            this.val$child.onNext(t);
        }
    }

    public OperatorOnErrorFlatMap(Func1<OnErrorThrowable, ? extends Observable<? extends T>> f) {
        this.resumeFunction = f;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        return new C12501(child, child);
    }
}
