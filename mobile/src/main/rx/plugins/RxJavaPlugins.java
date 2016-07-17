package rx.plugins;

import java.util.concurrent.atomic.AtomicReference;

public class RxJavaPlugins {
    static final RxJavaErrorHandler DEFAULT_ERROR_HANDLER;
    private static final RxJavaPlugins INSTANCE;
    private final AtomicReference<RxJavaErrorHandler> errorHandler;
    private final AtomicReference<RxJavaObservableExecutionHook> observableExecutionHook;
    private final AtomicReference<RxJavaSchedulersHook> schedulersHook;

    /* renamed from: rx.plugins.RxJavaPlugins.1 */
    static class C13661 extends RxJavaErrorHandler {
        C13661() {
        }
    }

    static {
        INSTANCE = new RxJavaPlugins();
        DEFAULT_ERROR_HANDLER = new C13661();
    }

    public static RxJavaPlugins getInstance() {
        return INSTANCE;
    }

    RxJavaPlugins() {
        this.errorHandler = new AtomicReference();
        this.observableExecutionHook = new AtomicReference();
        this.schedulersHook = new AtomicReference();
    }

    void reset() {
        INSTANCE.errorHandler.set(null);
        INSTANCE.observableExecutionHook.set(null);
        INSTANCE.schedulersHook.set(null);
    }

    public RxJavaErrorHandler getErrorHandler() {
        if (this.errorHandler.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaErrorHandler.class);
            if (impl == null) {
                this.errorHandler.compareAndSet(null, DEFAULT_ERROR_HANDLER);
            } else {
                this.errorHandler.compareAndSet(null, (RxJavaErrorHandler) impl);
            }
        }
        return (RxJavaErrorHandler) this.errorHandler.get();
    }

    public void registerErrorHandler(RxJavaErrorHandler impl) {
        if (!this.errorHandler.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered: " + this.errorHandler.get());
        }
    }

    public RxJavaObservableExecutionHook getObservableExecutionHook() {
        if (this.observableExecutionHook.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaObservableExecutionHook.class);
            if (impl == null) {
                this.observableExecutionHook.compareAndSet(null, RxJavaObservableExecutionHookDefault.getInstance());
            } else {
                this.observableExecutionHook.compareAndSet(null, (RxJavaObservableExecutionHook) impl);
            }
        }
        return (RxJavaObservableExecutionHook) this.observableExecutionHook.get();
    }

    public void registerObservableExecutionHook(RxJavaObservableExecutionHook impl) {
        if (!this.observableExecutionHook.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered: " + this.observableExecutionHook.get());
        }
    }

    private static Object getPluginImplementationViaProperty(Class<?> pluginClass) {
        String classSimpleName = pluginClass.getSimpleName();
        String implementingClass = System.getProperty("rxjava.plugin." + classSimpleName + ".implementation");
        if (implementingClass == null) {
            return null;
        }
        try {
            return Class.forName(implementingClass).asSubclass(pluginClass).newInstance();
        } catch (ClassCastException e) {
            throw new RuntimeException(classSimpleName + " implementation is not an instance of " + classSimpleName + ": " + implementingClass);
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException(classSimpleName + " implementation class not found: " + implementingClass, e2);
        } catch (InstantiationException e3) {
            throw new RuntimeException(classSimpleName + " implementation not able to be instantiated: " + implementingClass, e3);
        } catch (IllegalAccessException e4) {
            throw new RuntimeException(classSimpleName + " implementation not able to be accessed: " + implementingClass, e4);
        }
    }

    public RxJavaSchedulersHook getSchedulersHook() {
        if (this.schedulersHook.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaSchedulersHook.class);
            if (impl == null) {
                this.schedulersHook.compareAndSet(null, RxJavaSchedulersHook.getDefaultInstance());
            } else {
                this.schedulersHook.compareAndSet(null, (RxJavaSchedulersHook) impl);
            }
        }
        return (RxJavaSchedulersHook) this.schedulersHook.get();
    }

    public void registerSchedulersHook(RxJavaSchedulersHook impl) {
        if (!this.schedulersHook.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered: " + this.schedulersHook.get());
        }
    }
}
