package rx.schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import rx.internal.schedulers.NewThreadWorker;
import rx.internal.util.RxThreadFactory;

final class GenericScheduledExecutorService {
    private static final GenericScheduledExecutorService INSTANCE;
    private static final RxThreadFactory THREAD_FACTORY;
    private static final String THREAD_NAME_PREFIX = "RxScheduledExecutorPool-";
    private final ScheduledExecutorService executor;

    static {
        THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX);
        INSTANCE = new GenericScheduledExecutorService();
    }

    private GenericScheduledExecutorService() {
        int count = Runtime.getRuntime().availableProcessors();
        if (count > 4) {
            count /= 2;
        }
        if (count > 8) {
            count = 8;
        }
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(count, THREAD_FACTORY);
        if (!NewThreadWorker.tryEnableCancelPolicy(exec) && (exec instanceof ScheduledThreadPoolExecutor)) {
            NewThreadWorker.registerExecutor((ScheduledThreadPoolExecutor) exec);
        }
        this.executor = exec;
    }

    public static ScheduledExecutorService getInstance() {
        return INSTANCE.executor;
    }
}
