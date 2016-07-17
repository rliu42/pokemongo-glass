package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.plugins.RxJavaPlugins;

public final class OperatorOnExceptionResumeNextViaObservable<T> implements Operator<T, T> {
    final Observable<? extends T> resumeSequence;

    /* renamed from: rx.internal.operators.OperatorOnExceptionResumeNextViaObservable.1 */
    class C12581 extends Subscriber<T> {
        private boolean done;
        final /* synthetic */ Subscriber val$child;

        /* renamed from: rx.internal.operators.OperatorOnExceptionResumeNextViaObservable.1.1 */
        class C12571 implements Producer {
            final /* synthetic */ Producer val$producer;

            C12571(Producer producer) {
                this.val$producer = producer;
            }

            public void request(long n) {
                this.val$producer.request(n);
            }
        }

        C12581(Subscriber subscriber) {
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
            if (e instanceof Exception) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                unsubscribe();
                OperatorOnExceptionResumeNextViaObservable.this.resumeSequence.unsafeSubscribe(this.val$child);
                return;
            }
            this.val$child.onError(e);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.val$child.onCompleted();
            }
        }

        public void setProducer(Producer producer) {
            this.val$child.setProducer(new C12571(producer));
        }
    }

    public OperatorOnExceptionResumeNextViaObservable(Observable<? extends T> resumeSequence) {
        this.resumeSequence = resumeSequence;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Subscriber<T> s = new C12581(child);
        child.add(s);
        return s;
    }
}
