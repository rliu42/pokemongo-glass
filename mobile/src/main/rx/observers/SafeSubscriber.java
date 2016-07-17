package rx.observers;

import rx.Subscriber;
import rx.exceptions.Exceptions;

public class SafeSubscriber<T> extends Subscriber<T> {
    private final Subscriber<? super T> actual;
    boolean done;

    public SafeSubscriber(Subscriber<? super T> actual) {
        super(actual);
        this.done = false;
        this.actual = actual;
    }

    public void onCompleted() {
        if (!this.done) {
            this.done = true;
            try {
                this.actual.onCompleted();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                _onError(e);
            } finally {
                unsubscribe();
            }
        }
    }

    public void onError(Throwable e) {
        Exceptions.throwIfFatal(e);
        if (!this.done) {
            this.done = true;
            _onError(e);
        }
    }

    public void onNext(T args) {
        try {
            if (!this.done) {
                this.actual.onNext(args);
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            onError(e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void _onError(java.lang.Throwable r11) {
        /*
        r10 = this;
        r9 = 2;
        r8 = 1;
        r7 = 0;
        r3 = rx.plugins.RxJavaPlugins.getInstance();	 Catch:{ Throwable -> 0x0017 }
        r3 = r3.getErrorHandler();	 Catch:{ Throwable -> 0x0017 }
        r3.handleError(r11);	 Catch:{ Throwable -> 0x0017 }
    L_0x000e:
        r3 = r10.actual;	 Catch:{ Throwable -> 0x001c }
        r3.onError(r11);	 Catch:{ Throwable -> 0x001c }
        r10.unsubscribe();	 Catch:{ RuntimeException -> 0x00a4 }
        return;
    L_0x0017:
        r1 = move-exception;
        r10.handlePluginException(r1);
        goto L_0x000e;
    L_0x001c:
        r0 = move-exception;
        r3 = r0 instanceof rx.exceptions.OnErrorNotImplementedException;
        if (r3 == 0) goto L_0x004f;
    L_0x0021:
        r10.unsubscribe();	 Catch:{ Throwable -> 0x0027 }
        r0 = (rx.exceptions.OnErrorNotImplementedException) r0;
        throw r0;
    L_0x0027:
        r2 = move-exception;
        r3 = rx.plugins.RxJavaPlugins.getInstance();	 Catch:{ Throwable -> 0x004a }
        r3 = r3.getErrorHandler();	 Catch:{ Throwable -> 0x004a }
        r3.handleError(r2);	 Catch:{ Throwable -> 0x004a }
    L_0x0033:
        r3 = new java.lang.RuntimeException;
        r4 = "Observer.onError not implemented and error while unsubscribing.";
        r5 = new rx.exceptions.CompositeException;
        r6 = new java.lang.Throwable[r9];
        r6[r7] = r11;
        r6[r8] = r2;
        r6 = java.util.Arrays.asList(r6);
        r5.<init>(r6);
        r3.<init>(r4, r5);
        throw r3;
    L_0x004a:
        r1 = move-exception;
        r10.handlePluginException(r1);
        goto L_0x0033;
    L_0x004f:
        r3 = rx.plugins.RxJavaPlugins.getInstance();	 Catch:{ Throwable -> 0x0074 }
        r3 = r3.getErrorHandler();	 Catch:{ Throwable -> 0x0074 }
        r3.handleError(r0);	 Catch:{ Throwable -> 0x0074 }
    L_0x005a:
        r10.unsubscribe();	 Catch:{ Throwable -> 0x0079 }
        r3 = new rx.exceptions.OnErrorFailedException;
        r4 = "Error occurred when trying to propagate error to Observer.onError";
        r5 = new rx.exceptions.CompositeException;
        r6 = new java.lang.Throwable[r9];
        r6[r7] = r11;
        r6[r8] = r0;
        r6 = java.util.Arrays.asList(r6);
        r5.<init>(r6);
        r3.<init>(r4, r5);
        throw r3;
    L_0x0074:
        r1 = move-exception;
        r10.handlePluginException(r1);
        goto L_0x005a;
    L_0x0079:
        r2 = move-exception;
        r3 = rx.plugins.RxJavaPlugins.getInstance();	 Catch:{ Throwable -> 0x009f }
        r3 = r3.getErrorHandler();	 Catch:{ Throwable -> 0x009f }
        r3.handleError(r2);	 Catch:{ Throwable -> 0x009f }
    L_0x0085:
        r3 = new rx.exceptions.OnErrorFailedException;
        r4 = "Error occurred when trying to propagate error to Observer.onError and during unsubscription.";
        r5 = new rx.exceptions.CompositeException;
        r6 = 3;
        r6 = new java.lang.Throwable[r6];
        r6[r7] = r11;
        r6[r8] = r0;
        r6[r9] = r2;
        r6 = java.util.Arrays.asList(r6);
        r5.<init>(r6);
        r3.<init>(r4, r5);
        throw r3;
    L_0x009f:
        r1 = move-exception;
        r10.handlePluginException(r1);
        goto L_0x0085;
    L_0x00a4:
        r2 = move-exception;
        r3 = rx.plugins.RxJavaPlugins.getInstance();	 Catch:{ Throwable -> 0x00b6 }
        r3 = r3.getErrorHandler();	 Catch:{ Throwable -> 0x00b6 }
        r3.handleError(r2);	 Catch:{ Throwable -> 0x00b6 }
    L_0x00b0:
        r3 = new rx.exceptions.OnErrorFailedException;
        r3.<init>(r2);
        throw r3;
    L_0x00b6:
        r1 = move-exception;
        r10.handlePluginException(r1);
        goto L_0x00b0;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.observers.SafeSubscriber._onError(java.lang.Throwable):void");
    }

    private void handlePluginException(Throwable pluginException) {
        System.err.println("RxJavaErrorHandler threw an Exception. It shouldn't. => " + pluginException.getMessage());
        pluginException.printStackTrace();
    }

    public Subscriber<? super T> getActual() {
        return this.actual;
    }
}
