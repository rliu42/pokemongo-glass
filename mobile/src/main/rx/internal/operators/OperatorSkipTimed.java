package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;

public final class OperatorSkipTimed<T> implements Operator<T, T> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OperatorSkipTimed.1 */
    class C12791 implements Action0 {
        final /* synthetic */ AtomicBoolean val$gate;

        C12791(AtomicBoolean atomicBoolean) {
            this.val$gate = atomicBoolean;
        }

        public void call() {
            this.val$gate.set(true);
        }
    }

    /* renamed from: rx.internal.operators.OperatorSkipTimed.2 */
    class C12802 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ AtomicBoolean val$gate;

        C12802(Subscriber x0, AtomicBoolean atomicBoolean, Subscriber subscriber) {
            this.val$gate = atomicBoolean;
            this.val$child = subscriber;
            super(x0);
        }

        public void onNext(T t) {
            if (this.val$gate.get()) {
                this.val$child.onNext(t);
            }
        }

        public void onError(Throwable e) {
            try {
                this.val$child.onError(e);
            } finally {
                unsubscribe();
            }
        }

        public void onCompleted() {
            try {
                this.val$child.onCompleted();
            } finally {
                unsubscribe();
            }
        }
    }

    public OperatorSkipTimed(long time, TimeUnit unit, Scheduler scheduler) {
        this.time = time;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        AtomicBoolean gate = new AtomicBoolean();
        worker.schedule(new C12791(gate), this.time, this.unit);
        return new C12802(child, gate, child);
    }
}
