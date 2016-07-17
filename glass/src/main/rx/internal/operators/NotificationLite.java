package rx.internal.operators;

import java.io.Serializable;
import rx.Notification.Kind;
import rx.Observer;

public final class NotificationLite<T> {
    private static final NotificationLite INSTANCE;
    private static final Object ON_COMPLETED_SENTINEL;
    private static final Object ON_NEXT_NULL_SENTINEL;

    /* renamed from: rx.internal.operators.NotificationLite.1 */
    static class C11611 implements Serializable {
        private static final long serialVersionUID = 1;

        C11611() {
        }

        public String toString() {
            return "Notification=>Completed";
        }
    }

    /* renamed from: rx.internal.operators.NotificationLite.2 */
    static class C11622 implements Serializable {
        private static final long serialVersionUID = 2;

        C11622() {
        }

        public String toString() {
            return "Notification=>NULL";
        }
    }

    private static class OnErrorSentinel implements Serializable {
        private static final long serialVersionUID = 3;
        private final Throwable f914e;

        public OnErrorSentinel(Throwable e) {
            this.f914e = e;
        }

        public String toString() {
            return "Notification=>Error:" + this.f914e;
        }
    }

    private NotificationLite() {
    }

    static {
        INSTANCE = new NotificationLite();
        ON_COMPLETED_SENTINEL = new C11611();
        ON_NEXT_NULL_SENTINEL = new C11622();
    }

    public static <T> NotificationLite<T> instance() {
        return INSTANCE;
    }

    public Object next(T t) {
        if (t == null) {
            return ON_NEXT_NULL_SENTINEL;
        }
        return t;
    }

    public Object completed() {
        return ON_COMPLETED_SENTINEL;
    }

    public Object error(Throwable e) {
        return new OnErrorSentinel(e);
    }

    public boolean accept(Observer<? super T> o, Object n) {
        if (n == ON_COMPLETED_SENTINEL) {
            o.onCompleted();
            return true;
        } else if (n == ON_NEXT_NULL_SENTINEL) {
            o.onNext(null);
            return false;
        } else if (n == null) {
            throw new IllegalArgumentException("The lite notification can not be null");
        } else if (n.getClass() == OnErrorSentinel.class) {
            o.onError(((OnErrorSentinel) n).f914e);
            return true;
        } else {
            o.onNext(n);
            return false;
        }
    }

    public boolean isCompleted(Object n) {
        return n == ON_COMPLETED_SENTINEL;
    }

    public boolean isError(Object n) {
        return n instanceof OnErrorSentinel;
    }

    public boolean isNull(Object n) {
        return n == ON_NEXT_NULL_SENTINEL;
    }

    public boolean isNext(Object n) {
        return (n == null || isError(n) || isCompleted(n)) ? false : true;
    }

    public Kind kind(Object n) {
        if (n == null) {
            throw new IllegalArgumentException("The lite notification can not be null");
        } else if (n == ON_COMPLETED_SENTINEL) {
            return Kind.OnCompleted;
        } else {
            if (n instanceof OnErrorSentinel) {
                return Kind.OnError;
            }
            return Kind.OnNext;
        }
    }

    public T getValue(Object n) {
        return n == ON_NEXT_NULL_SENTINEL ? null : n;
    }

    public Throwable getError(Object n) {
        return ((OnErrorSentinel) n).f914e;
    }
}
