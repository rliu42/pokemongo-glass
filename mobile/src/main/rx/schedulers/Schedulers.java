package rx.schedulers;

import java.util.concurrent.Executor;
import rx.Scheduler;
import rx.internal.schedulers.EventLoopsScheduler;
import rx.plugins.RxJavaPlugins;

public final class Schedulers {
    private static final Schedulers INSTANCE;
    private final Scheduler computationScheduler;
    private final Scheduler ioScheduler;
    private final Scheduler newThreadScheduler;

    static {
        INSTANCE = new Schedulers();
    }

    private Schedulers() {
        Scheduler c = RxJavaPlugins.getInstance().getSchedulersHook().getComputationScheduler();
        if (c != null) {
            this.computationScheduler = c;
        } else {
            this.computationScheduler = new EventLoopsScheduler();
        }
        Scheduler io = RxJavaPlugins.getInstance().getSchedulersHook().getIOScheduler();
        if (io != null) {
            this.ioScheduler = io;
        } else {
            this.ioScheduler = new CachedThreadScheduler();
        }
        Scheduler nt = RxJavaPlugins.getInstance().getSchedulersHook().getNewThreadScheduler();
        if (nt != null) {
            this.newThreadScheduler = nt;
        } else {
            this.newThreadScheduler = NewThreadScheduler.instance();
        }
    }

    public static Scheduler immediate() {
        return ImmediateScheduler.instance();
    }

    public static Scheduler trampoline() {
        return TrampolineScheduler.instance();
    }

    public static Scheduler newThread() {
        return INSTANCE.newThreadScheduler;
    }

    public static Scheduler computation() {
        return INSTANCE.computationScheduler;
    }

    public static Scheduler io() {
        return INSTANCE.ioScheduler;
    }

    public static TestScheduler test() {
        return new TestScheduler();
    }

    public static Scheduler from(Executor executor) {
        return new ExecutorScheduler(executor);
    }
}
