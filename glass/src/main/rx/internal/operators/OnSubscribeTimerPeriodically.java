package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;

public final class OnSubscribeTimerPeriodically implements OnSubscribe<Long> {
    final long initialDelay;
    final long period;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OnSubscribeTimerPeriodically.1 */
    class C11861 implements Action0 {
        long counter;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ Worker val$worker;

        C11861(Subscriber subscriber, Worker worker) {
            this.val$child = subscriber;
            this.val$worker = worker;
        }

        public void call() {
            try {
                Subscriber subscriber = this.val$child;
                long j = this.counter;
                this.counter = 1 + j;
                subscriber.onNext(Long.valueOf(j));
            } catch (Throwable e) {
                this.val$child.onError(e);
            } finally {
                this.val$worker.unsubscribe();
            }
        }
    }

    public OnSubscribeTimerPeriodically(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        this.initialDelay = initialDelay;
        this.period = period;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    public void call(Subscriber<? super Long> child) {
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        worker.schedulePeriodically(new C11861(child, worker), this.initialDelay, this.period, this.unit);
    }
}
