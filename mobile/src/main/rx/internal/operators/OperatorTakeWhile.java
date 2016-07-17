package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;
import rx.functions.Func2;

public final class OperatorTakeWhile<T> implements Operator<T, T> {
    private final Func2<? super T, ? super Integer, Boolean> predicate;

    /* renamed from: rx.internal.operators.OperatorTakeWhile.1 */
    class C13001 implements Func2<T, Integer, Boolean> {
        final /* synthetic */ Func1 val$underlying;

        C13001(Func1 func1) {
            this.val$underlying = func1;
        }

        public Boolean call(T input, Integer index) {
            return (Boolean) this.val$underlying.call(input);
        }
    }

    /* renamed from: rx.internal.operators.OperatorTakeWhile.2 */
    class C13012 extends Subscriber<T> {
        private int counter;
        private boolean done;
        final /* synthetic */ Subscriber val$subscriber;

        C13012(Subscriber x0, boolean x1, Subscriber subscriber) {
            this.val$subscriber = subscriber;
            super(x0, x1);
            this.counter = 0;
            this.done = false;
        }

        public void onNext(T t) {
            try {
                Func2 access$000 = OperatorTakeWhile.this.predicate;
                int i = this.counter;
                this.counter = i + 1;
                if (((Boolean) access$000.call(t, Integer.valueOf(i))).booleanValue()) {
                    this.val$subscriber.onNext(t);
                    return;
                }
                this.done = true;
                this.val$subscriber.onCompleted();
                unsubscribe();
            } catch (Throwable e) {
                this.done = true;
                Exceptions.throwIfFatal(e);
                this.val$subscriber.onError(OnErrorThrowable.addValueAsLastCause(e, t));
                unsubscribe();
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.val$subscriber.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (!this.done) {
                this.val$subscriber.onError(e);
            }
        }
    }

    public OperatorTakeWhile(Func1<? super T, Boolean> underlying) {
        this(new C13001(underlying));
    }

    public OperatorTakeWhile(Func2<? super T, ? super Integer, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Subscriber<T> s = new C13012(subscriber, false, subscriber);
        subscriber.add(s);
        return s;
    }
}
