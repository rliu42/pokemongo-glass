package rx.internal.operators;

import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

public final class OperatorSampleWithObservable<T, U> implements Operator<T, T> {
    static final Object EMPTY_TOKEN;
    final Observable<U> sampler;

    /* renamed from: rx.internal.operators.OperatorSampleWithObservable.1 */
    class C12671 extends Subscriber<U> {
        final /* synthetic */ SerializedSubscriber val$s;
        final /* synthetic */ AtomicReference val$value;

        C12671(Subscriber x0, AtomicReference atomicReference, SerializedSubscriber serializedSubscriber) {
            this.val$value = atomicReference;
            this.val$s = serializedSubscriber;
            super(x0);
        }

        public void onNext(U u) {
            T localValue = this.val$value.getAndSet(OperatorSampleWithObservable.EMPTY_TOKEN);
            if (localValue != OperatorSampleWithObservable.EMPTY_TOKEN) {
                this.val$s.onNext(localValue);
            }
        }

        public void onError(Throwable e) {
            this.val$s.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            this.val$s.onCompleted();
            unsubscribe();
        }
    }

    /* renamed from: rx.internal.operators.OperatorSampleWithObservable.2 */
    class C12682 extends Subscriber<T> {
        final /* synthetic */ SerializedSubscriber val$s;
        final /* synthetic */ AtomicReference val$value;

        C12682(Subscriber x0, AtomicReference atomicReference, SerializedSubscriber serializedSubscriber) {
            this.val$value = atomicReference;
            this.val$s = serializedSubscriber;
            super(x0);
        }

        public void onNext(T t) {
            this.val$value.set(t);
        }

        public void onError(Throwable e) {
            this.val$s.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            this.val$s.onCompleted();
            unsubscribe();
        }
    }

    static {
        EMPTY_TOKEN = new Object();
    }

    public OperatorSampleWithObservable(Observable<U> sampler) {
        this.sampler = sampler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        SerializedSubscriber<T> s = new SerializedSubscriber(child);
        AtomicReference<Object> value = new AtomicReference(EMPTY_TOKEN);
        Subscriber<U> samplerSub = new C12671(child, value, s);
        Subscriber<T> result = new C12682(child, value, s);
        this.sampler.unsafeSubscribe(samplerSub);
        return result;
    }
}
