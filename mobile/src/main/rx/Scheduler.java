package rx;

import java.util.concurrent.TimeUnit;
import rx.functions.Action0;
import rx.subscriptions.MultipleAssignmentSubscription;

public abstract class Scheduler {

    public static abstract class Worker implements Subscription {

        /* renamed from: rx.Scheduler.Worker.1 */
        class C11181 implements Action0 {
            long count;
            final /* synthetic */ Action0 val$action;
            final /* synthetic */ MultipleAssignmentSubscription val$mas;
            final /* synthetic */ long val$periodInNanos;
            final /* synthetic */ long val$startInNanos;

            C11181(MultipleAssignmentSubscription multipleAssignmentSubscription, Action0 action0, long j, long j2) {
                this.val$mas = multipleAssignmentSubscription;
                this.val$action = action0;
                this.val$startInNanos = j;
                this.val$periodInNanos = j2;
                this.count = 0;
            }

            public void call() {
                if (!this.val$mas.isUnsubscribed()) {
                    this.val$action.call();
                    long j = this.val$startInNanos;
                    long j2 = this.count + 1;
                    this.count = j2;
                    this.val$mas.set(Worker.this.schedule(this, (j + (j2 * this.val$periodInNanos)) - TimeUnit.MILLISECONDS.toNanos(Worker.this.now()), TimeUnit.NANOSECONDS));
                }
            }
        }

        public abstract Subscription schedule(Action0 action0);

        public abstract Subscription schedule(Action0 action0, long j, TimeUnit timeUnit);

        public Subscription schedulePeriodically(Action0 action, long initialDelay, long period, TimeUnit unit) {
            long periodInNanos = unit.toNanos(period);
            long startInNanos = TimeUnit.MILLISECONDS.toNanos(now()) + unit.toNanos(initialDelay);
            MultipleAssignmentSubscription mas = new MultipleAssignmentSubscription();
            Action0 recursiveAction = new C11181(mas, action, startInNanos, periodInNanos);
            MultipleAssignmentSubscription s = new MultipleAssignmentSubscription();
            mas.set(s);
            s.set(schedule(recursiveAction, initialDelay, unit));
            return mas;
        }

        public long now() {
            return System.currentTimeMillis();
        }
    }

    public abstract Worker createWorker();

    public long now() {
        return System.currentTimeMillis();
    }
}
