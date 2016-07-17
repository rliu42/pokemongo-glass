package rx.internal.operators;

import java.util.HashSet;
import java.util.Set;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.util.UtilityFunctions;

public final class OperatorDistinct<T, U> implements Operator<T, T> {
    final Func1<? super T, ? extends U> keySelector;

    /* renamed from: rx.internal.operators.OperatorDistinct.1 */
    class C12181 extends Subscriber<T> {
        Set<U> keyMemory;
        final /* synthetic */ Subscriber val$child;

        C12181(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
            this.keyMemory = new HashSet();
        }

        public void onNext(T t) {
            if (this.keyMemory.add(OperatorDistinct.this.keySelector.call(t))) {
                this.val$child.onNext(t);
            } else {
                request(1);
            }
        }

        public void onError(Throwable e) {
            this.keyMemory = null;
            this.val$child.onError(e);
        }

        public void onCompleted() {
            this.keyMemory = null;
            this.val$child.onCompleted();
        }
    }

    private static class Holder {
        static final OperatorDistinct<?, ?> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorDistinct(UtilityFunctions.identity());
        }
    }

    public static <T> OperatorDistinct<T, T> instance() {
        return Holder.INSTANCE;
    }

    public OperatorDistinct(Func1<? super T, ? extends U> keySelector) {
        this.keySelector = keySelector;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        return new C12181(child, child);
    }
}
