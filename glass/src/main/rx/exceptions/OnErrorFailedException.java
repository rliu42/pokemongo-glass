package rx.exceptions;

public class OnErrorFailedException extends RuntimeException {
    private static final long serialVersionUID = -419289748403337611L;

    public OnErrorFailedException(String message, Throwable e) {
        super(message, e);
    }

    public OnErrorFailedException(Throwable e) {
        super(e.getMessage(), e);
    }
}
