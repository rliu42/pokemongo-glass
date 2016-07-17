package rx.internal.operators;

import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;

public class OperatorDoOnEach<T> implements Operator<T, T> {
    private final Observer<? super T> doOnEachObserver;

    /* renamed from: rx.internal.operators.OperatorDoOnEach.1 */
    class C12201 extends Subscriber<T> {
        private boolean done;
        final /* synthetic */ Subscriber val$observer;

        C12201(Subscriber x0, Subscriber subscriber) {
            this.val$observer = subscriber;
            super(x0);
            this.done = false;
        }

        public void onCompleted() {
            if (!this.done) {
                try {
                    OperatorDoOnEach.this.doOnEachObserver.onCompleted();
                    this.done = true;
                    this.val$observer.onCompleted();
                } catch (Throwable e) {
                    onError(e);
                }
            }
        }

        public void onError(Throwable e) {
            Exceptions.throwIfFatal(e);
            if (!this.done) {
                this.done = true;
                try {
                    OperatorDoOnEach.this.doOnEachObserver.onError(e);
                    this.val$observer.onError(e);
                } catch (Throwable e2) {
                    this.val$observer.onError(e2);
                }
            }
        }

        public void onNext(T value) {
            if (!this.done) {
                try {
                    OperatorDoOnEach.this.doOnEachObserver.onNext(value);
                    this.val$observer.onNext(value);
                } catch (Throwable e) {
                    onError(OnErrorThrowable.addValueAsLastCause(e, value));
                }
            }
        }
    }

    public OperatorDoOnEach(Observer<? super T> doOnEachObserver) {
        this.doOnEachObserver = doOnEachObserver;
    }

    public Subscriber<? super T> call(Subscriber<? super T> observer) {
        return new C12201(observer, observer);
    }
}
