package rx.internal.operators;

import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.functions.Func2;
import rx.observers.SerializedSubscriber;

public final class OperatorWithLatestFrom<T, U, R> implements Operator<R, T> {
    static final Object EMPTY;
    final Observable<? extends U> other;
    final Func2<? super T, ? super U, ? extends R> resultSelector;

    /* renamed from: rx.internal.operators.OperatorWithLatestFrom.1 */
    class C13321 extends Subscriber<T> {
        final /* synthetic */ AtomicReference val$current;
        final /* synthetic */ SerializedSubscriber val$s;

        C13321(Subscriber x0, boolean x1, AtomicReference atomicReference, SerializedSubscriber serializedSubscriber) {
            this.val$current = atomicReference;
            this.val$s = serializedSubscriber;
            super(x0, x1);
        }

        public void onNext(T t) {
            U o = this.val$current.get();
            if (o != OperatorWithLatestFrom.EMPTY) {
                try {
                    this.val$s.onNext(OperatorWithLatestFrom.this.resultSelector.call(t, o));
                } catch (Throwable e) {
                    onError(e);
                }
            }
        }

        public void onError(Throwable e) {
            this.val$s.onError(e);
            this.val$s.unsubscribe();
        }

        public void onCompleted() {
            this.val$s.onCompleted();
            this.val$s.unsubscribe();
        }
    }

    /* renamed from: rx.internal.operators.OperatorWithLatestFrom.2 */
    class C13332 extends Subscriber<U> {
        final /* synthetic */ AtomicReference val$current;
        final /* synthetic */ SerializedSubscriber val$s;

        C13332(AtomicReference atomicReference, SerializedSubscriber serializedSubscriber) {
            this.val$current = atomicReference;
            this.val$s = serializedSubscriber;
        }

        public void onNext(U t) {
            this.val$current.set(t);
        }

        public void onError(Throwable e) {
            this.val$s.onError(e);
            this.val$s.unsubscribe();
        }

        public void onCompleted() {
            if (this.val$current.get() == OperatorWithLatestFrom.EMPTY) {
                this.val$s.onCompleted();
                this.val$s.unsubscribe();
            }
        }
    }

    static {
        EMPTY = new Object();
    }

    public OperatorWithLatestFrom(Observable<? extends U> other, Func2<? super T, ? super U, ? extends R> resultSelector) {
        this.other = other;
        this.resultSelector = resultSelector;
    }

    public Subscriber<? super T> call(Subscriber<? super R> child) {
        SerializedSubscriber<R> s = new SerializedSubscriber(child, false);
        child.add(s);
        AtomicReference<Object> current = new AtomicReference(EMPTY);
        Subscriber<T> subscriber = new C13321(s, true, current, s);
        Subscriber<U> otherSubscriber = new C13332(current, s);
        s.add(subscriber);
        s.add(otherSubscriber);
        this.other.unsafeSubscribe(otherSubscriber);
        return subscriber;
    }
}
