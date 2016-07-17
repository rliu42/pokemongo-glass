package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;

public final class OperatorDelay<T> implements Operator<T, T> {
    final long delay;
    final Scheduler scheduler;
    final Observable<? extends T> source;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OperatorDelay.1 */
    class C12131 extends Subscriber<T> {
        boolean done;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ Worker val$worker;

        /* renamed from: rx.internal.operators.OperatorDelay.1.1 */
        class C12101 implements Action0 {
            C12101() {
            }

            public void call() {
                if (!C12131.this.done) {
                    C12131.this.done = true;
                    C12131.this.val$child.onCompleted();
                }
            }
        }

        /* renamed from: rx.internal.operators.OperatorDelay.1.2 */
        class C12112 implements Action0 {
            final /* synthetic */ Throwable val$e;

            C12112(Throwable th) {
                this.val$e = th;
            }

            public void call() {
                if (!C12131.this.done) {
                    C12131.this.done = true;
                    C12131.this.val$child.onError(this.val$e);
                    C12131.this.val$worker.unsubscribe();
                }
            }
        }

        /* renamed from: rx.internal.operators.OperatorDelay.1.3 */
        class C12123 implements Action0 {
            final /* synthetic */ Object val$t;

            C12123(Object obj) {
                this.val$t = obj;
            }

            public void call() {
                if (!C12131.this.done) {
                    C12131.this.val$child.onNext(this.val$t);
                }
            }
        }

        C12131(Subscriber x0, Worker worker, Subscriber subscriber) {
            this.val$worker = worker;
            this.val$child = subscriber;
            super(x0);
        }

        public void onCompleted() {
            this.val$worker.schedule(new C12101(), OperatorDelay.this.delay, OperatorDelay.this.unit);
        }

        public void onError(Throwable e) {
            this.val$worker.schedule(new C12112(e));
        }

        public void onNext(T t) {
            this.val$worker.schedule(new C12123(t), OperatorDelay.this.delay, OperatorDelay.this.unit);
        }
    }

    public OperatorDelay(Observable<? extends T> source, long delay, TimeUnit unit, Scheduler scheduler) {
        this.source = source;
        this.delay = delay;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        return new C12131(child, worker, child);
    }
}
