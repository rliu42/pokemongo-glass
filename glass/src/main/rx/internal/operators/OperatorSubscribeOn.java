package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;

public class OperatorSubscribeOn<T> implements Operator<T, Observable<T>> {
    private final Scheduler scheduler;

    /* renamed from: rx.internal.operators.OperatorSubscribeOn.1 */
    class C12891 extends Subscriber<Observable<T>> {
        final /* synthetic */ Worker val$inner;
        final /* synthetic */ Subscriber val$subscriber;

        /* renamed from: rx.internal.operators.OperatorSubscribeOn.1.1 */
        class C12881 implements Action0 {
            final /* synthetic */ Observable val$o;

            /* renamed from: rx.internal.operators.OperatorSubscribeOn.1.1.1 */
            class C12871 extends Subscriber<T> {
                final /* synthetic */ Thread val$t;

                /* renamed from: rx.internal.operators.OperatorSubscribeOn.1.1.1.1 */
                class C12861 implements Producer {
                    final /* synthetic */ Producer val$producer;

                    /* renamed from: rx.internal.operators.OperatorSubscribeOn.1.1.1.1.1 */
                    class C12851 implements Action0 {
                        final /* synthetic */ long val$n;

                        C12851(long j) {
                            this.val$n = j;
                        }

                        public void call() {
                            C12861.this.val$producer.request(this.val$n);
                        }
                    }

                    C12861(Producer producer) {
                        this.val$producer = producer;
                    }

                    public void request(long n) {
                        if (Thread.currentThread() == C12871.this.val$t) {
                            this.val$producer.request(n);
                        } else {
                            C12891.this.val$inner.schedule(new C12851(n));
                        }
                    }
                }

                C12871(Subscriber x0, Thread thread) {
                    this.val$t = thread;
                    super(x0);
                }

                public void onCompleted() {
                    C12891.this.val$subscriber.onCompleted();
                }

                public void onError(Throwable e) {
                    C12891.this.val$subscriber.onError(e);
                }

                public void onNext(T t) {
                    C12891.this.val$subscriber.onNext(t);
                }

                public void setProducer(Producer producer) {
                    C12891.this.val$subscriber.setProducer(new C12861(producer));
                }
            }

            C12881(Observable observable) {
                this.val$o = observable;
            }

            public void call() {
                this.val$o.unsafeSubscribe(new C12871(C12891.this.val$subscriber, Thread.currentThread()));
            }
        }

        C12891(Subscriber x0, Subscriber subscriber, Worker worker) {
            this.val$subscriber = subscriber;
            this.val$inner = worker;
            super(x0);
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            this.val$subscriber.onError(e);
        }

        public void onNext(Observable<T> o) {
            this.val$inner.schedule(new C12881(o));
        }
    }

    public OperatorSubscribeOn(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Subscriber<? super Observable<T>> call(Subscriber<? super T> subscriber) {
        Worker inner = this.scheduler.createWorker();
        subscriber.add(inner);
        return new C12891(subscriber, subscriber, inner);
    }
}
