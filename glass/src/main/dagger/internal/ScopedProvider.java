package dagger.internal;

import javax.inject.Provider;

public final class ScopedProvider<T> implements Provider<T> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final Object UNINITIALIZED;
    private final Factory<T> factory;
    private volatile Object instance;

    static {
        $assertionsDisabled = !ScopedProvider.class.desiredAssertionStatus();
        UNINITIALIZED = new Object();
    }

    private ScopedProvider(Factory<T> factory) {
        this.instance = UNINITIALIZED;
        if ($assertionsDisabled || factory != null) {
            this.factory = factory;
            return;
        }
        throw new AssertionError();
    }

    public T get() {
        Object result = this.instance;
        if (result == UNINITIALIZED) {
            synchronized (this) {
                result = this.instance;
                if (result == UNINITIALIZED) {
                    result = this.factory.get();
                    this.instance = result;
                }
            }
        }
        return result;
    }

    public static <T> Provider<T> create(Factory<T> factory) {
        if (factory != null) {
            return new ScopedProvider(factory);
        }
        throw new NullPointerException();
    }
}
