package crittercism.android;

import java.lang.Thread.UncaughtExceptionHandler;

public final class ay implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler f332a;
    private final az f333b;

    public ay(az azVar, UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.f333b = azVar;
        this.f332a = uncaughtExceptionHandler;
    }

    public final void uncaughtException(Thread thread, Throwable exception) {
        try {
            this.f333b.m395a(exception);
            if (this.f332a != null && !(this.f332a instanceof ay)) {
                this.f332a.uncaughtException(Thread.currentThread(), exception);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            if (!(this.f332a == null || (this.f332a instanceof ay))) {
                this.f332a.uncaughtException(Thread.currentThread(), exception);
            }
        }
    }
}
