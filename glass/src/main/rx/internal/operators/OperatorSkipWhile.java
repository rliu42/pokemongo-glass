package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

public final class OperatorSkipWhile<T> implements Operator<T, T> {
    private final Func2<? super T, Integer, Boolean> predicate;

    /* renamed from: rx.internal.operators.OperatorSkipWhile.1 */
    class C12831 extends Subscriber<T> {
        int index;
        boolean skipping;
        final /* synthetic */ Subscriber val$child;

        C12831(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
            this.skipping = true;
        }

        public void onNext(T t) {
            if (this.skipping) {
                Func2 access$000 = OperatorSkipWhile.this.predicate;
                int i = this.index;
                this.index = i + 1;
                if (((Boolean) access$000.call(t, Integer.valueOf(i))).booleanValue()) {
                    request(1);
                    return;
                }
                this.skipping = false;
                this.val$child.onNext(t);
                return;
            }
            this.val$child.onNext(t);
        }

        public void onError(Throwable e) {
            this.val$child.onError(e);
        }

        public void onCompleted() {
            this.val$child.onCompleted();
        }
    }

    /* renamed from: rx.internal.operators.OperatorSkipWhile.2 */
    static class C12842 implements Func2<T, Integer, Boolean> {
        final /* synthetic */ Func1 val$predicate;

        C12842(Func1 func1) {
            this.val$predicate = func1;
        }

        public Boolean call(T t1, Integer t2) {
            return (Boolean) this.val$predicate.call(t1);
        }
    }

    public OperatorSkipWhile(Func2<? super T, Integer, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        return new C12831(child, child);
    }

    public static <T> Func2<T, Integer, Boolean> toPredicate2(Func1<? super T, Boolean> predicate) {
        return new C12842(predicate);
    }
}
