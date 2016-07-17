package rx.internal.operators;

import java.util.Arrays;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.plugins.RxJavaPlugins;

public final class OperatorOnErrorReturn<T> implements Operator<T, T> {
    final Func1<Throwable, ? extends T> resultFunction;

    /* renamed from: rx.internal.operators.OperatorOnErrorReturn.1 */
    class C12561 extends Subscriber<T> {
        private boolean done;
        final /* synthetic */ Subscriber val$child;

        /* renamed from: rx.internal.operators.OperatorOnErrorReturn.1.1 */
        class C12551 implements Producer {
            final /* synthetic */ Producer val$producer;

            C12551(Producer producer) {
                this.val$producer = producer;
            }

            public void request(long n) {
                this.val$producer.request(n);
            }
        }

        C12561(Subscriber subscriber) {
            this.val$child = subscriber;
            this.done = false;
        }

        public void onNext(T t) {
            if (!this.done) {
                this.val$child.onNext(t);
            }
        }

        public void onError(Throwable e) {
            if (this.done) {
                Exceptions.throwIfFatal(e);
                return;
            }
            this.done = true;
            try {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                unsubscribe();
                this.val$child.onNext(OperatorOnErrorReturn.this.resultFunction.call(e));
                this.val$child.onCompleted();
            } catch (Throwable x) {
                this.val$child.onError(new CompositeException(Arrays.asList(new Throwable[]{e, x})));
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.val$child.onCompleted();
            }
        }

        public void setProducer(Producer producer) {
            this.val$child.setProducer(new C12551(producer));
        }
    }

    public OperatorOnErrorReturn(Func1<Throwable, ? extends T> resultFunction) {
        this.resultFunction = resultFunction;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Subscriber<T> parent = new C12561(child);
        child.add(parent);
        return parent;
    }
}
