package rx.internal.operators;

import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;

@Experimental
public final class OperatorTakeUntilPredicate<T> implements Operator<T, T> {
    private final Func1<? super T, Boolean> stopPredicate;

    /* renamed from: rx.internal.operators.OperatorTakeUntilPredicate.1 */
    class C12991 implements Producer {
        final /* synthetic */ ParentSubscriber val$parent;

        C12991(ParentSubscriber parentSubscriber) {
            this.val$parent = parentSubscriber;
        }

        public void request(long n) {
            this.val$parent.downstreamRequest(n);
        }
    }

    private final class ParentSubscriber extends Subscriber<T> {
        private final Subscriber<? super T> child;
        private boolean done;

        private ParentSubscriber(Subscriber<? super T> child) {
            this.done = false;
            this.child = child;
        }

        public void onNext(T t) {
            this.child.onNext(t);
            try {
                if (((Boolean) OperatorTakeUntilPredicate.this.stopPredicate.call(t)).booleanValue()) {
                    this.done = true;
                    this.child.onCompleted();
                    unsubscribe();
                }
            } catch (Throwable e) {
                this.done = true;
                Exceptions.throwIfFatal(e);
                this.child.onError(OnErrorThrowable.addValueAsLastCause(e, t));
                unsubscribe();
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.child.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (!this.done) {
                this.child.onError(e);
            }
        }

        void downstreamRequest(long n) {
            request(n);
        }
    }

    public OperatorTakeUntilPredicate(Func1<? super T, Boolean> stopPredicate) {
        this.stopPredicate = stopPredicate;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        ParentSubscriber parent = new ParentSubscriber(child, null);
        child.add(parent);
        child.setProducer(new C12991(parent));
        return parent;
    }
}
