package rx.plugins;

class RxJavaObservableExecutionHookDefault extends RxJavaObservableExecutionHook {
    private static RxJavaObservableExecutionHookDefault INSTANCE;

    RxJavaObservableExecutionHookDefault() {
    }

    static {
        INSTANCE = new RxJavaObservableExecutionHookDefault();
    }

    public static RxJavaObservableExecutionHook getInstance() {
        return INSTANCE;
    }
}
