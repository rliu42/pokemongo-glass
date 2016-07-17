package rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import rx.Observable.OnSubscribe;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;

public final class BehaviorSubject<T> extends Subject<T, T> {
    private final NotificationLite<T> nl;
    private final SubjectSubscriptionManager<T> state;

    /* renamed from: rx.subjects.BehaviorSubject.1 */
    static class C13771 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ SubjectSubscriptionManager val$state;

        C13771(SubjectSubscriptionManager subjectSubscriptionManager) {
            this.val$state = subjectSubscriptionManager;
        }

        public void call(SubjectObserver<T> o) {
            o.emitFirst(this.val$state.get(), this.val$state.nl);
        }
    }

    public static <T> BehaviorSubject<T> create() {
        return create(null, false);
    }

    public static <T> BehaviorSubject<T> create(T defaultValue) {
        return create(defaultValue, true);
    }

    private static <T> BehaviorSubject<T> create(T defaultValue, boolean hasDefault) {
        SubjectSubscriptionManager<T> state = new SubjectSubscriptionManager();
        if (hasDefault) {
            state.set(NotificationLite.instance().next(defaultValue));
        }
        state.onAdded = new C13771(state);
        state.onTerminated = state.onAdded;
        return new BehaviorSubject(state, state);
    }

    protected BehaviorSubject(OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state) {
        super(onSubscribe);
        this.nl = NotificationLite.instance();
        this.state = state;
    }

    public void onCompleted() {
        if (this.state.get() == null || this.state.active) {
            Object n = this.nl.completed();
            for (SubjectObserver<T> bo : this.state.terminate(n)) {
                bo.emitNext(n, this.state.nl);
            }
        }
    }

    public void onError(Throwable e) {
        if (this.state.get() == null || this.state.active) {
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
        if (this.state.get() == null || this.state.active) {
            Object n = this.nl.next(v);
            for (SubjectObserver<T> bo : this.state.next(n)) {
                bo.emitNext(n, this.state.nl);
            }
        }
    }

    int subscriberCount() {
        return this.state.observers().length;
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Experimental
    public boolean hasValue() {
        return this.nl.isNext(this.state.get());
    }

    @Experimental
    public boolean hasThrowable() {
        return this.nl.isError(this.state.get());
    }

    @Experimental
    public boolean hasCompleted() {
        return this.nl.isCompleted(this.state.get());
    }

    @Experimental
    public T getValue() {
        Object o = this.state.get();
        if (this.nl.isNext(o)) {
            return this.nl.getValue(o);
        }
        return null;
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
        Object o = this.state.get();
        if (this.nl.isNext(o)) {
            if (a.length == 0) {
                a = (Object[]) ((Object[]) Array.newInstance(a.getClass().getComponentType(), 1));
            }
            a[0] = this.nl.getValue(o);
            if (a.length > 1) {
                a[1] = null;
            }
        } else if (a.length > 0) {
            a[0] = null;
        }
        return a;
    }
}
