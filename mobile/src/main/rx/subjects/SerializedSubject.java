package rx.subjects;

import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.annotations.Experimental;
import rx.observers.SerializedObserver;

public class SerializedSubject<T, R> extends Subject<T, R> {
    private final Subject<T, R> actual;
    private final SerializedObserver<T> observer;

    /* renamed from: rx.subjects.SerializedSubject.1 */
    class C13841 implements OnSubscribe<R> {
        final /* synthetic */ Subject val$actual;

        C13841(Subject subject) {
            this.val$actual = subject;
        }

        public void call(Subscriber<? super R> child) {
            this.val$actual.unsafeSubscribe(child);
        }
    }

    public SerializedSubject(Subject<T, R> actual) {
        super(new C13841(actual));
        this.actual = actual;
        this.observer = new SerializedObserver(actual);
    }

    public void onCompleted() {
        this.observer.onCompleted();
    }

    public void onError(Throwable e) {
        this.observer.onError(e);
    }

    public void onNext(T t) {
        this.observer.onNext(t);
    }

    public boolean hasObservers() {
        return this.actual.hasObservers();
    }

    @Experimental
    public boolean hasCompleted() {
        return this.actual.hasCompleted();
    }

    @Experimental
    public boolean hasThrowable() {
        return this.actual.hasThrowable();
    }

    @Experimental
    public boolean hasValue() {
        return this.actual.hasValue();
    }

    @Experimental
    public Throwable getThrowable() {
        return this.actual.getThrowable();
    }

    @Experimental
    public T getValue() {
        return this.actual.getValue();
    }

    @Experimental
    public Object[] getValues() {
        return this.actual.getValues();
    }

    @Experimental
    public T[] getValues(T[] a) {
        return this.actual.getValues(a);
    }
}
