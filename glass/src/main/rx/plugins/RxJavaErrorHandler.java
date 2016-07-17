package rx.plugins;

import rx.annotations.Experimental;
import rx.exceptions.Exceptions;

public abstract class RxJavaErrorHandler {
    protected static final String ERROR_IN_RENDERING_SUFFIX = ".errorRendering";

    public void handleError(Throwable e) {
    }

    @Experimental
    public final String handleOnNextValueRendering(Object item) {
        try {
            return render(item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return item.getClass().getName() + ERROR_IN_RENDERING_SUFFIX;
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            return item.getClass().getName() + ERROR_IN_RENDERING_SUFFIX;
        }
    }

    @Experimental
    protected String render(Object item) throws InterruptedException {
        return null;
    }
}
