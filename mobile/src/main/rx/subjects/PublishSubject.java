package rx.subjects;

import java.util.ArrayList;
import java.util.List;
import rx.Observable.OnSubscribe;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;

public final class PublishSubject<T> extends Subject<T, T> {
    private final NotificationLite<T> nl;
    final SubjectSubscriptionManager<T> state;

    /* renamed from: rx.subjects.PublishSubject.1 */
    static class C13781 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ SubjectSubscriptionManager val$state;

        C13781(SubjectSubscriptionManager subjectSubscriptionManager) {
            this.val$state = subjectSubscriptionManager;
        }

        public void call(SubjectObserver<T> o) {
            o.emitFirst(this.val$state.get(), this.val$state.nl);
        }
    }

    public static <T> PublishSubject<T> create() {
        SubjectSubscriptionManager<T> state = new SubjectSubscriptionManager();
        state.onTerminated = new C13781(state);
        return new PublishSubject(state, state);
    }

    protected PublishSubject(OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state) {
        super(onSubscribe);
        this.nl = NotificationLite.instance();
        this.state = state;
    }

    public void onCompleted() {
        if (this.state.active) {
            Object n = this.nl.completed();
            for (SubjectObserver<T> bo : this.state.terminate(n)) {
                bo.emitNext(n, this.state.nl);
            }
        }
    }

    public void onError(Throwable e) {
        if (this.state.active) {
            Object n = this.nl.error(e);
            List<Throwable> errors = null;
            for (SubjectObserver<T> bo : this.state.terminate(n)) {
                try {
                    bo.emitNext(n, this.state.nl);
                } catch (Throwable e2) {
                    if (errors == null) {
                        errors = new ArrayList();
                    }
                    errors.add(e2);
                }
            }
            Exceptions.throwIfAny(errors);
        }
    }

    public void onNext(T v) {
        for (SubjectObserver<T> bo : this.state.observers()) {
            bo.onNext(v);
        }
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Experimental
    public boolean hasThrowable() {
        return this.nl.isError(this.state.get());
    }

    @Experimental
    public boolean hasCompleted() {
        Object o = this.state.get();
        return (o == null || this.nl.isError(o)) ? false : true;
    }

    @Experimental
    public Throwable getThrowable() {
        Object o = this.state.get();
        if (this.nl.isError(o)) {
            return this.nl.getError(o);
        }
        return null;
    }

    @Experimental
    public boolean hasValue() {
        return false;
    }

    @Experimental
    public T getValue() {
        return null;
    }

    @Experimental
    public Object[] getValues() {
        return new Object[0];
    }

    @Experimental
    public T[] getValues(T[] a) {
        if (a.length > 0) {
            a[0] = null;
        }
        return a;
    }
}
