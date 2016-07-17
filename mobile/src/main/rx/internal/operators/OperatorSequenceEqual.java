package rx.internal.operators;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.internal.util.UtilityFunctions;

public final class OperatorSequenceEqual {
    private static final Object LOCAL_ONCOMPLETED;

    /* renamed from: rx.internal.operators.OperatorSequenceEqual.1 */
    static class C12721 implements Func1<T, Object> {
        C12721() {
        }

        public Object call(T t1) {
            return t1;
        }
    }

    /* renamed from: rx.internal.operators.OperatorSequenceEqual.2 */
    static class C12732 implements Func2<Object, Object, Boolean> {
        final /* synthetic */ Func2 val$equality;

        C12732(Func2 func2) {
            this.val$equality = func2;
        }

        public Boolean call(Object t1, Object t2) {
            boolean c1;
            if (t1 == OperatorSequenceEqual.LOCAL_ONCOMPLETED) {
                c1 = true;
            } else {
                c1 = false;
            }
            boolean c2;
            if (t2 == OperatorSequenceEqual.LOCAL_ONCOMPLETED) {
                c2 = true;
            } else {
                c2 = false;
            }
            if (c1 && c2) {
                return Boolean.valueOf(true);
            }
            if (c1 || c2) {
                return Boolean.valueOf(false);
            }
            return (Boolean) this.val$equality.call(t1, t2);
        }
    }

    private OperatorSequenceEqual() {
        throw new IllegalStateException("No instances!");
    }

    static {
        LOCAL_ONCOMPLETED = new Object();
    }

    static <T> Observable<Object> materializeLite(Observable<T> source) {
        return Observable.concat(source.map(new C12721()), Observable.just(LOCAL_ONCOMPLETED));
    }

    public static <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second, Func2<? super T, ? super T, Boolean> equality) {
        return Observable.zip(materializeLite(first), materializeLite(second), new C12732(equality)).all(UtilityFunctions.identity());
    }
}
