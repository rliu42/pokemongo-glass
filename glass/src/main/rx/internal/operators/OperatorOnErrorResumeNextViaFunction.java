package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.producers.ProducerArbiter;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.SerialSubscription;

public final class OperatorOnErrorResumeNextViaFunction<T> implements Operator<T, T> {
    private final Func1<Throwable, ? extends Observable<? extends T>> resumeFunction;

    /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.1 */
    class C12521 extends Subscriber<T> {
        private boolean done;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ ProducerArbiter val$pa;
        final /* synthetic */ SerialSubscription val$ssub;

        /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.1.1 */
        class C12511 extends Subscriber<T> {
            C12511() {
            }

            public void onNext(T t) {
                C12521.this.val$child.onNext(t);
            }

            public void onError(Throwable e) {
                C12521.this.val$child.onError(e);
            }

            public void onCompleted() {
                C12521.this.val$child.onCompleted();
            }

            public void setProducer(Producer producer) {
                C12521.this.val$pa.setProducer(producer);
            }
        }

        C12521(Subscriber subscriber, ProducerArbiter producerArbiter, SerialSubscription serialSubscription) {
            this.val$child = subscriber;
            this.val$pa = producerArbiter;
            this.val$ssub = serialSubscription;
            this.done = false;
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.val$child.onCompleted();
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
                Subscriber<T> next = new C12511();
                this.val$ssub.set(next);
                ((Observable) OperatorOnErrorResumeNextViaFunction.this.resumeFunction.call(e)).unsafeSubscribe(next);
            } catch (Throwable e2) {
                this.val$child.onError(e2);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                this.val$child.onNext(t);
            }
        }

        public void setProducer(Producer producer) {
            this.val$pa.setProducer(producer);
        }
    }

    public OperatorOnErrorResumeNextViaFunction(Func1<Throwable, ? extends Observable<? extends T>> f) {
        this.resumeFunction = f;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        ProducerArbiter pa = new ProducerArbiter();
        SerialSubscription ssub = new SerialSubscription();
        Subscriber<T> parent = new C12521(child, pa, ssub);
        child.add(ssub);
        ssub.set(parent);
        child.setProducer(pa);
        return parent;
    }
}
