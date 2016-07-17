package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.Subscribers;

public final class OnSubscribeDelaySubscription<T> implements OnSubscribe<T> {
    final Scheduler scheduler;
    final Observable<? extends T> source;
    final long time;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OnSubscribeDelaySubscription.1 */
    class C11651 implements Action0 {
        final /* synthetic */ Subscriber val$s;

        C11651(Subscriber subscriber) {
            this.val$s = subscriber;
        }

        public void call() {
            if (!this.val$s.isUnsubscribed()) {
                OnSubscribeDelaySubscription.this.source.unsafeSubscribe(Subscribers.wrap(this.val$s));
            }
        }
    }

    public OnSubscribeDelaySubscription(Observable<? extends T> source, long time, TimeUnit unit, Scheduler scheduler) {
        this.source = source;
        this.time = time;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    public void call(Subscriber<? super T> s) {
        Worker worker = this.scheduler.createWorker();
        s.add(worker);
        worker.schedule(new C11651(s), this.time, this.unit);
    }
}
