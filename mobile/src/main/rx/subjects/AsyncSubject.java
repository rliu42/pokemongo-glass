package rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import rx.Observable.OnSubscribe;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;

public final class AsyncSubject<T> extends Subject<T, T> {
    volatile Object lastValue;
    private final NotificationLite<T> nl;
    final SubjectSubscriptionManager<T> state;

    /* renamed from: rx.subjects.AsyncSubject.1 */
    static class C13761 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ SubjectSubscriptionManager val$state;

        C13761(SubjectSubscriptionManager subjectSubscriptionManager) {
            this.val$state = subjectSubscriptionManager;
        }

        public void call(SubjectObserver<T> o) {
            Object v = this.val$state.get();
            NotificationLite<T> nl = this.val$state.nl;
            o.accept(v, nl);
            if (v == null || !(nl.isCompleted(v) || nl.isError(v))) {
                o.onCompleted();
            }
        }
    }

    public static <T> AsyncSubject<T> create() {
        SubjectSubscriptionManager<T> state = new SubjectSubscriptionManager();
        state.onTerminated = new C13761(state);
        return new AsyncSubject(state, state);
    }

    protected AsyncSubject(OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state) {
        super(onSubscribe);
        this.nl = NotificationLite.instance();
        this.state = state;
    }

    public void onCompleted() {
        if (this.state.active) {
            Object last = this.lastValue;
            if (last == null) {
                last = this.nl.completed();
            }
            for (SubjectObserver<T> bo : this.state.terminate(last)) {
                if (last == this.nl.completed()) {
                    bo.onCompleted();
                } else {
                    bo.onNext(this.nl.getValue(last));
                    bo.onCompleted();
                }
            }
        }
    }

    public void onError(Throwable e) {
        if (this.state.active) {
            List<Throwable> errors = null;
            for (SubjectObserver<T> bo : this.state.terminate(this.nl.error(e))) {
                try {
                    bo.onError(e);
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
        this.lastValue = this.nl.next(v);
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Experimental
    public boolean hasValue() {
        return !this.nl.isError(this.state.get()) && this.nl.isNext(this.lastValue);
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
    public T getValue() {
        Object v = this.lastValue;
        if (this.nl.isError(this.state.get()) || !this.nl.isNext(v)) {
            return null;
        }
        return this.nl.getValue(v);
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
    public T[] getValues(T[] a) {
        Object v = this.lastValue;
        if (!this.nl.isError(this.state.get()) && this.nl.isNext(v)) {
            T val = this.nl.getValue(v);
            if (a.length == 0) {
                a = (Object[]) ((Object[]) Array.newInstance(a.getClass().getComponentType(), 1));
            }
            a[0] = val;
            if (a.length > 1) {
                a[1] = null;
            }
        } else if (a.length > 0) {
            a[0] = null;
        }
        return a;
    }
}
