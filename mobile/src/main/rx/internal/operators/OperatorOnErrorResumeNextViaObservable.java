package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.plugins.RxJavaPlugins;

public final class OperatorOnErrorResumeNextViaObservable<T> implements Operator<T, T> {
    final Observable<? extends T> resumeSequence;

    /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaObservable.1 */
    class C12541 extends Subscriber<T> {
        private boolean done;
        final /* synthetic */ Subscriber val$child;

        /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaObservable.1.1 */
        class C12531 implements Producer {
            final /* synthetic */ Producer val$producer;

            C12531(Producer producer) {
                this.val$producer = producer;
            }

            public void request(long n) {
                this.val$producer.request(n);
            }
        }

        C12541(Subscriber subscriber) {
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
            RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
            unsubscribe();
            OperatorOnErrorResumeNextViaObservable.this.resumeSequence.unsafeSubscribe(this.val$child);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.val$child.onCompleted();
            }
        }

        public void setProducer(Producer producer) {
            this.val$child.setProducer(new C12531(producer));
        }
    }

    public OperatorOnErrorResumeNextViaObservable(Observable<? extends T> resumeSequence) {
        this.resumeSequence = resumeSequence;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Subscriber<T> s = new C12541(child);
        child.add(s);
        return s;
    }
}
