package rx.subjects;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.annotations.Experimental;

public abstract class Subject<T, R> extends Observable<R> implements Observer<T> {
    private static final Object[] EMPTY_ARRAY;

    public abstract boolean hasObservers();

    protected Subject(OnSubscribe<R> onSubscribe) {
        super(onSubscribe);
    }

    public final SerializedSubject<T, R> toSerialized() {
        if (getClass() == SerializedSubject.class) {
            return (SerializedSubject) this;
        }
        return new SerializedSubject(this);
    }

    @Experimental
    public boolean hasThrowable() {
        throw new UnsupportedOperationException();
    }

    @Experimental
    public boolean hasCompleted() {
        throw new UnsupportedOperationException();
    }

    @Experimental
    public Throwable getThrowable() {
        throw new UnsupportedOperationException();
    }

    @Experimental
    public boolean hasValue() {
        throw new UnsupportedOperationException();
    }

    @Experimental
    public T getValue() {
        throw new UnsupportedOperationException();
    }

    static {
        EMPTY_ARRAY = new Object[0];
    }

    @Experimental
    public Object[] getValues() {
        T[] r = getValues(EMPTY_ARRAY);
        if (r == EMPTY_ARRAY) {
            return new Object[0];
        }
        return r;
    }

    @Experimental
    public T[] getValues(T[] tArr) {
        throw new UnsupportedOperationException();
    }
}
