package rx.internal.schedulers;

import com.google.android.gms.location.LocationStatusCodes;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.internal.util.RxThreadFactory;
import rx.internal.util.SubscriptionList;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public class NewThreadWorker extends Worker implements Subscription {
    private static final ConcurrentHashMap<ScheduledThreadPoolExecutor, ScheduledThreadPoolExecutor> EXECUTORS;
    private static final String FREQUENCY_KEY = "rx.scheduler.jdk6.purge-frequency-millis";
    private static final AtomicReference<ScheduledExecutorService> PURGE;
    private static final boolean PURGE_FORCE;
    private static final String PURGE_FORCE_KEY = "rx.scheduler.jdk6.purge-force";
    public static final int PURGE_FREQUENCY;
    private static final String PURGE_THREAD_PREFIX = "RxSchedulerPurge-";
    private final ScheduledExecutorService executor;
    volatile boolean isUnsubscribed;
    private final RxJavaSchedulersHook schedulersHook;

    /* renamed from: rx.internal.schedulers.NewThreadWorker.1 */
    static class C13371 implements Runnable {
        C13371() {
        }

        public void run() {
            NewThreadWorker.purgeExecutors();
        }
    }

    static {
        EXECUTORS = new ConcurrentHashMap();
        PURGE = new AtomicReference();
        PURGE_FORCE = Boolean.getBoolean(PURGE_FORCE_KEY);
        PURGE_FREQUENCY = Integer.getInteger(FREQUENCY_KEY, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE).intValue();
    }

    public static void registerExecutor(ScheduledThreadPoolExecutor service) {
        while (((ScheduledExecutorService) PURGE.get()) == null) {
            ScheduledExecutorService exec = Executors.newScheduledThreadPool(1, new RxThreadFactory(PURGE_THREAD_PREFIX));
            if (PURGE.compareAndSet(null, exec)) {
                exec.scheduleAtFixedRate(new C13371(), (long) PURGE_FREQUENCY, (long) PURGE_FREQUENCY, TimeUnit.MILLISECONDS);
                break;
            }
        }
        EXECUTORS.putIfAbsent(service, service);
    }

    public static void deregisterExecutor(ScheduledExecutorService service) {
        EXECUTORS.remove(service);
    }

    static void purgeExecutors() {
        try {
            Iterator<ScheduledThreadPoolExecutor> it = EXECUTORS.keySet().iterator();
            while (it.hasNext()) {
                ScheduledThreadPoolExecutor exec = (ScheduledThreadPoolExecutor) it.next();
                if (exec.isShutdown()) {
                    it.remove();
                } else {
                    exec.purge();
                }
            }
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            RxJavaPlugins.getInstance().getErrorHandler().handleError(t);
        }
    }

    public static boolean tryEnableCancelPolicy(ScheduledExecutorService exec) {
        if (!PURGE_FORCE) {
            Method[] arr$ = exec.getClass().getMethods();
            int len$ = arr$.length;
            for (int i$ = PURGE_FREQUENCY; i$ < len$; i$++) {
                Method m = arr$[i$];
                if (m.getName().equals("setRemoveOnCancelPolicy") && m.getParameterTypes().length == 1 && m.getParameterTypes()[PURGE_FREQUENCY] == Boolean.TYPE) {
                    try {
                        m.invoke(exec, new Object[]{Boolean.valueOf(true)});
                        return true;
                    } catch (Exception ex) {
                        RxJavaPlugins.getInstance().getErrorHandler().handleError(ex);
                    }
                }
            }
        }
        return PURGE_FORCE;
    }

    public NewThreadWorker(ThreadFactory threadFactory) {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1, threadFactory);
        if (!tryEnableCancelPolicy(exec) && (exec instanceof ScheduledThreadPoolExecutor)) {
            registerExecutor((ScheduledThreadPoolExecutor) exec);
        }
        this.schedulersHook = RxJavaPlugins.getInstance().getSchedulersHook();
        this.executor = exec;
    }

    public Subscription schedule(Action0 action) {
        return schedule(action, 0, null);
    }

    public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
        if (this.isUnsubscribed) {
            return Subscriptions.unsubscribed();
        }
        return scheduleActual(action, delayTime, unit);
    }

    public ScheduledAction scheduleActual(Action0 action, long delayTime, TimeUnit unit) {
        Future f;
        ScheduledAction run = new ScheduledAction(this.schedulersHook.onSchedule(action));
        if (delayTime <= 0) {
            f = this.executor.submit(run);
        } else {
            f = this.executor.schedule(run, delayTime, unit);
        }
        run.add(f);
        return run;
    }

    public ScheduledAction scheduleActual(Action0 action, long delayTime, TimeUnit unit, CompositeSubscription parent) {
        Future f;
        ScheduledAction run = new ScheduledAction(this.schedulersHook.onSchedule(action), parent);
        parent.add(run);
        if (delayTime <= 0) {
            f = this.executor.submit(run);
        } else {
            f = this.executor.schedule(run, delayTime, unit);
        }
        run.add(f);
        return run;
    }

    public ScheduledAction scheduleActual(Action0 action, long delayTime, TimeUnit unit, SubscriptionList parent) {
        Future f;
        ScheduledAction run = new ScheduledAction(this.schedulersHook.onSchedule(action), parent);
        parent.add(run);
        if (delayTime <= 0) {
            f = this.executor.submit(run);
        } else {
            f = this.executor.schedule(run, delayTime, unit);
        }
        run.add(f);
        return run;
    }

    public void unsubscribe() {
        this.isUnsubscribed = true;
        this.executor.shutdownNow();
        deregisterExecutor(this.executor);
    }

    public boolean isUnsubscribed() {
        return this.isUnsubscribed;
    }
}
