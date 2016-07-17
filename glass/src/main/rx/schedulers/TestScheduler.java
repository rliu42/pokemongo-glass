package rx.schedulers;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.Subscriptions;

public class TestScheduler extends Scheduler {
    private static long counter;
    private final Queue<TimedAction> queue;
    private long time;

    private static class CompareActionsByTime implements Comparator<TimedAction> {
        private CompareActionsByTime() {
        }

        public int compare(TimedAction action1, TimedAction action2) {
            if (action1.time == action2.time) {
                return Long.valueOf(action1.count).compareTo(Long.valueOf(action2.count));
            }
            return Long.valueOf(action1.time).compareTo(Long.valueOf(action2.time));
        }
    }

    private final class InnerTestScheduler extends Worker {
        private final BooleanSubscription f928s;

        /* renamed from: rx.schedulers.TestScheduler.InnerTestScheduler.1 */
        class C13721 implements Action0 {
            final /* synthetic */ TimedAction val$timedAction;

            C13721(TimedAction timedAction) {
                this.val$timedAction = timedAction;
            }

            public void call() {
                TestScheduler.this.queue.remove(this.val$timedAction);
            }
        }

        /* renamed from: rx.schedulers.TestScheduler.InnerTestScheduler.2 */
        class C13732 implements Action0 {
            final /* synthetic */ TimedAction val$timedAction;

            C13732(TimedAction timedAction) {
                this.val$timedAction = timedAction;
            }

            public void call() {
                TestScheduler.this.queue.remove(this.val$timedAction);
            }
        }

        private InnerTestScheduler() {
            this.f928s = new BooleanSubscription();
        }

        public void unsubscribe() {
            this.f928s.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.f928s.isUnsubscribed();
        }

        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            TimedAction timedAction = new TimedAction(TestScheduler.this.time + unit.toNanos(delayTime), action, null);
            TestScheduler.this.queue.add(timedAction);
            return Subscriptions.create(new C13721(timedAction));
        }

        public Subscription schedule(Action0 action) {
            TimedAction timedAction = new TimedAction(0, action, null);
            TestScheduler.this.queue.add(timedAction);
            return Subscriptions.create(new C13732(timedAction));
        }

        public long now() {
            return TestScheduler.this.now();
        }
    }

    private static final class TimedAction {
        private final Action0 action;
        private final long count;
        private final Worker scheduler;
        private final long time;

        private TimedAction(Worker scheduler, long time, Action0 action) {
            this.count = TestScheduler.access$108();
            this.time = time;
            this.action = action;
            this.scheduler = scheduler;
        }

        public String toString() {
            return String.format("TimedAction(time = %d, action = %s)", new Object[]{Long.valueOf(this.time), this.action.toString()});
        }
    }

    public TestScheduler() {
        this.queue = new PriorityQueue(11, new CompareActionsByTime());
    }

    static /* synthetic */ long access$108() {
        long j = counter;
        counter = 1 + j;
        return j;
    }

    static {
        counter = 0;
    }

    public long now() {
        return TimeUnit.NANOSECONDS.toMillis(this.time);
    }

    public void advanceTimeBy(long delayTime, TimeUnit unit) {
        advanceTimeTo(this.time + unit.toNanos(delayTime), TimeUnit.NANOSECONDS);
    }

    public void advanceTimeTo(long delayTime, TimeUnit unit) {
        triggerActions(unit.toNanos(delayTime));
    }

    public void triggerActions() {
        triggerActions(this.time);
    }

    private void triggerActions(long targetTimeInNanos) {
        while (!this.queue.isEmpty()) {
            TimedAction current = (TimedAction) this.queue.peek();
            if (current.time > targetTimeInNanos) {
                break;
            }
            this.time = current.time == 0 ? this.time : current.time;
            this.queue.remove();
            if (!current.scheduler.isUnsubscribed()) {
                current.action.call();
            }
        }
        this.time = targetTimeInNanos;
    }

    public Worker createWorker() {
        return new InnerTestScheduler();
    }
}
