package rx.internal.operators;

import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action1;

public class OperatorDoOnRequest<T> implements Operator<T, T> {
    private final Action1<Long> request;

    /* renamed from: rx.internal.operators.OperatorDoOnRequest.1 */
    class C12211 implements Producer {
        final /* synthetic */ ParentSubscriber val$parent;

        C12211(ParentSubscriber parentSubscriber) {
            this.val$parent = parentSubscriber;
        }

        public void request(long n) {
            OperatorDoOnRequest.this.request.call(Long.valueOf(n));
            this.val$parent.requestMore(n);
        }
    }

    private static final class ParentSubscriber<T> extends Subscriber<T> {
        private final Subscriber<? super T> child;

        private ParentSubscriber(Subscriber<? super T> child) {
            this.child = child;
        }

        private void requestMore(long n) {
            request(n);
        }

        public void onCompleted() {
            this.child.onCompleted();
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }

        public void onNext(T t) {
            this.child.onNext(t);
        }
    }

    public OperatorDoOnRequest(Action1<Long> request) {
        this.request = request;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        ParentSubscriber<T> parent = new ParentSubscriber(null);
        child.setProducer(new C12211(parent));
        child.add(parent);
        return parent;
    }
}
