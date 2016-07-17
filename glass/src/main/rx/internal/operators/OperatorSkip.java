package rx.internal.operators;

import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;

public final class OperatorSkip<T> implements Operator<T, T> {
    final int toSkip;

    /* renamed from: rx.internal.operators.OperatorSkip.1 */
    class C12761 extends Subscriber<T> {
        int skipped;
        final /* synthetic */ Subscriber val$child;

        C12761(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
            this.skipped = 0;
        }

        public void onCompleted() {
            this.val$child.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$child.onError(e);
        }

        public void onNext(T t) {
            if (this.skipped >= OperatorSkip.this.toSkip) {
                this.val$child.onNext(t);
            } else {
                this.skipped++;
            }
        }

        public void setProducer(Producer producer) {
            this.val$child.setProducer(producer);
            producer.request((long) OperatorSkip.this.toSkip);
        }
    }

    public OperatorSkip(int n) {
        this.toSkip = n;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        return new C12761(child, child);
    }
}
