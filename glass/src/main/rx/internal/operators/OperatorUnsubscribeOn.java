package rx.internal.operators;

import rx.Observable.Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public class OperatorUnsubscribeOn<T> implements Operator<T, T> {
    private final Scheduler scheduler;

    /* renamed from: rx.internal.operators.OperatorUnsubscribeOn.1 */
    class C13191 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$subscriber;

        C13191(Subscriber subscriber) {
            this.val$subscriber = subscriber;
        }

        public void onCompleted() {
            this.val$subscriber.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$subscriber.onError(e);
        }

        public void onNext(T t) {
            this.val$subscriber.onNext(t);
        }
    }

    /* renamed from: rx.internal.operators.OperatorUnsubscribeOn.2 */
    class C13212 implements Action0 {
        final /* synthetic */ Subscriber val$parent;

        /* renamed from: rx.internal.operators.OperatorUnsubscribeOn.2.1 */
        class C13201 implements Action0 {
            final /* synthetic */ Worker val$inner;

            C13201(Worker worker) {
                this.val$inner = worker;
            }

            public void call() {
                C13212.this.val$parent.unsubscribe();
                this.val$inner.unsubscribe();
            }
        }

        C13212(Subscriber subscriber) {
            this.val$parent = subscriber;
        }

        public void call() {
            Worker inner = OperatorUnsubscribeOn.this.scheduler.createWorker();
            inner.schedule(new C13201(inner));
        }
    }

    public OperatorUnsubscribeOn(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Subscriber<T> parent = new C13191(subscriber);
        subscriber.add(Subscriptions.create(new C13212(parent)));
        return parent;
    }
}
