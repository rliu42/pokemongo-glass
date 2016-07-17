package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.util.UtilityFunctions;

public final class OperatorDistinctUntilChanged<T, U> implements Operator<T, T> {
    final Func1<? super T, ? extends U> keySelector;

    /* renamed from: rx.internal.operators.OperatorDistinctUntilChanged.1 */
    class C12191 extends Subscriber<T> {
        boolean hasPrevious;
        U previousKey;
        final /* synthetic */ Subscriber val$child;

        C12191(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
        }

        public void onNext(T t) {
            U currentKey = this.previousKey;
            U key = OperatorDistinctUntilChanged.this.keySelector.call(t);
            this.previousKey = key;
            if (!this.hasPrevious) {
                this.hasPrevious = true;
                this.val$child.onNext(t);
            } else if (currentKey == key || (key != null && key.equals(currentKey))) {
                request(1);
            } else {
                this.val$child.onNext(t);
            }
        }

        public void onError(Throwable e) {
            this.val$child.onError(e);
        }

        public void onCompleted() {
            this.val$child.onCompleted();
        }
    }

    private static class Holder {
        static final OperatorDistinctUntilChanged<?, ?> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorDistinctUntilChanged(UtilityFunctions.identity());
        }
    }

    public static <T> OperatorDistinctUntilChanged<T, T> instance() {
        return Holder.INSTANCE;
    }

    public OperatorDistinctUntilChanged(Func1<? super T, ? extends U> keySelector) {
        this.keySelector = keySelector;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        return new C12191(child, child);
    }
}
