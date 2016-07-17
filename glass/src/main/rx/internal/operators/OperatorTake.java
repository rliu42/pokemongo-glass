package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;

public final class OperatorTake<T> implements Operator<T, T> {
    final int limit;

    /* renamed from: rx.internal.operators.OperatorTake.1 */
    class C12931 extends Subscriber<T> {
        boolean completed;
        int count;
        final /* synthetic */ Subscriber val$child;

        /* renamed from: rx.internal.operators.OperatorTake.1.1 */
        class C12921 implements Producer {
            final AtomicLong requested;
            final /* synthetic */ Producer val$producer;

            C12921(Producer producer) {
                this.val$producer = producer;
                this.requested = new AtomicLong(0);
            }

            public void request(long n) {
                if (n > 0 && !C12931.this.completed) {
                    long c;
                    long r;
                    do {
                        r = this.requested.get();
                        c = Math.min(n, ((long) OperatorTake.this.limit) - r);
                        if (c == 0) {
                            return;
                        }
                    } while (!this.requested.compareAndSet(r, r + c));
                    this.val$producer.request(c);
                }
            }
        }

        C12931(Subscriber subscriber) {
            this.val$child = subscriber;
            this.count = 0;
            this.completed = false;
        }

        public void onCompleted() {
            if (!this.completed) {
                this.val$child.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (!this.completed) {
                this.val$child.onError(e);
            }
        }

        public void onNext(T i) {
            if (!isUnsubscribed()) {
                int i2 = this.count + 1;
                this.count = i2;
                if (i2 >= OperatorTake.this.limit) {
                    this.completed = true;
                }
                this.val$child.onNext(i);
                if (this.completed) {
                    this.val$child.onCompleted();
                    unsubscribe();
                }
            }
        }

        public void setProducer(Producer producer) {
            this.val$child.setProducer(new C12921(producer));
        }
    }

    public OperatorTake(int limit) {
        this.limit = limit;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Subscriber<T> parent = new C12931(child);
        if (this.limit == 0) {
            child.onCompleted();
            parent.unsubscribe();
        }
        child.add(parent);
        return parent;
    }
}
