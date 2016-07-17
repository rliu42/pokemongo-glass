package rx.schedulers;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.NewThreadWorker;
import rx.internal.util.RxThreadFactory;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

final class CachedThreadScheduler extends Scheduler {
    private static final RxThreadFactory EVICTOR_THREAD_FACTORY;
    private static final String EVICTOR_THREAD_NAME_PREFIX = "RxCachedWorkerPoolEvictor-";
    private static final RxThreadFactory WORKER_THREAD_FACTORY;
    private static final String WORKER_THREAD_NAME_PREFIX = "RxCachedThreadScheduler-";

    private static final class CachedWorkerPool {
        private static CachedWorkerPool INSTANCE;
        private final ScheduledExecutorService evictExpiredWorkerExecutor;
        private final ConcurrentLinkedQueue<ThreadWorker> expiringWorkerQueue;
        private final long keepAliveTime;

        /* renamed from: rx.schedulers.CachedThreadScheduler.CachedWorkerPool.1 */
        class C13671 implements Runnable {
            C13671() {
            }

            public void run() {
                CachedWorkerPool.this.evictExpiredWorkers();
            }
        }

        CachedWorkerPool(long keepAliveTime, TimeUnit unit) {
            this.keepAliveTime = unit.toNanos(keepAliveTime);
            this.expiringWorkerQueue = new ConcurrentLinkedQueue();
            this.evictExpiredWorkerExecutor = Executors.newScheduledThreadPool(1, CachedThreadScheduler.EVICTOR_THREAD_FACTORY);
            this.evictExpiredWorkerExecutor.scheduleWithFixedDelay(new C13671(), this.keepAliveTime, this.keepAliveTime, TimeUnit.NANOSECONDS);
        }

        static {
            INSTANCE = new CachedWorkerPool(60, TimeUnit.SECONDS);
        }

        ThreadWorker get() {
            while (!this.expiringWorkerQueue.isEmpty()) {
                ThreadWorker threadWorker = (ThreadWorker) this.expiringWorkerQueue.poll();
                if (threadWorker != null) {
                    return threadWorker;
                }
            }
            return new ThreadWorker(CachedThreadScheduler.WORKER_THREAD_FACTORY);
        }

        void release(ThreadWorker threadWorker) {
            threadWorker.setExpirationTime(now() + this.keepAliveTime);
            this.expiringWorkerQueue.offer(threadWorker);
        }

        void evictExpiredWorkers() {
            if (!this.expiringWorkerQueue.isEmpty()) {
                long currentTimestamp = now();
                Iterator i$ = this.expiringWorkerQueue.iterator();
                while (i$.hasNext()) {
                    ThreadWorker threadWorker = (ThreadWorker) i$.next();
                    if (threadWorker.getExpirationTime() > currentTimestamp) {
                        return;
                    }
                    if (this.expiringWorkerQueue.remove(threadWorker)) {
                        threadWorker.unsubscribe();
                    }
                }
            }
        }

        long now() {
            return System.nanoTime();
        }
    }

    private static final class EventLoopWorker extends Worker {
        static final AtomicIntegerFieldUpdater<EventLoopWorker> ONCE_UPDATER;
        private final CompositeSubscription innerSubscription;
        volatile int once;
        private final ThreadWorker threadWorker;

        static {
            ONCE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(EventLoopWorker.class, "once");
        }

        EventLoopWorker(ThreadWorker threadWorker) {
            this.innerSubscription = new CompositeSubscription();
            this.threadWorker = threadWorker;
        }

        public void unsubscribe() {
            if (ONCE_UPDATER.compareAndSet(this, 0, 1)) {
                CachedWorkerPool.INSTANCE.release(this.threadWorker);
            }
            this.innerSubscription.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.innerSubscription.isUnsubscribed();
        }

        public Subscription schedule(Action0 action) {
            return schedule(action, 0, null);
        }

        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            if (this.innerSubscription.isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            Subscription s = this.threadWorker.scheduleActual(action, delayTime, unit);
            this.innerSubscription.add(s);
            s.addParent(this.innerSubscription);
            return s;
        }
    }

    private static final class ThreadWorker extends NewThreadWorker {
        private long expirationTime;

        ThreadWorker(ThreadFactory threadFactory) {
            super(threadFactory);
            this.expirationTime = 0;
        }

        public long getExpirationTime() {
            return this.expirationTime;
        }

        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }
    }

    CachedThreadScheduler() {
    }

    static {
        WORKER_THREAD_FACTORY = new RxThreadFactory(WORKER_THREAD_NAME_PREFIX);
        EVICTOR_THREAD_FACTORY = new RxThreadFactory(EVICTOR_THREAD_NAME_PREFIX);
    }

    public Worker createWorker() {
        return new EventLoopWorker(CachedWorkerPool.INSTANCE.get());
    }
}
