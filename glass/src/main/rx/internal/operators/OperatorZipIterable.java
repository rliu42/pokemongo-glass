package rx.internal.operators;

import java.util.Iterator;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.functions.Func2;
import rx.observers.Subscribers;

public final class OperatorZipIterable<T1, T2, R> implements Operator<R, T1> {
    final Iterable<? extends T2> iterable;
    final Func2<? super T1, ? super T2, ? extends R> zipFunction;

    /* renamed from: rx.internal.operators.OperatorZipIterable.1 */
    class C13341 extends Subscriber<T1> {
        boolean once;
        final /* synthetic */ Iterator val$iterator;
        final /* synthetic */ Subscriber val$subscriber;

        C13341(Subscriber x0, Subscriber subscriber, Iterator it) {
            this.val$subscriber = subscriber;
            this.val$iterator = it;
            super(x0);
        }

        public void onCompleted() {
            if (!this.once) {
                this.once = true;
                this.val$subscriber.onCompleted();
            }
        }

        public void onError(Throwable e) {
            this.val$subscriber.onError(e);
        }

        public void onNext(T1 t) {
            try {
                this.val$subscriber.onNext(OperatorZipIterable.this.zipFunction.call(t, this.val$iterator.next()));
                if (!this.val$iterator.hasNext()) {
                    onCompleted();
                }
            } catch (Throwable e) {
                onError(e);
            }
        }
    }

    public OperatorZipIterable(Iterable<? extends T2> iterable, Func2<? super T1, ? super T2, ? extends R> zipFunction) {
        this.iterable = iterable;
        this.zipFunction = zipFunction;
    }

    public Subscriber<? super T1> call(Subscriber<? super R> subscriber) {
        Iterator<? extends T2> iterator = this.iterable.iterator();
        try {
            if (!iterator.hasNext()) {
                subscriber.onCompleted();
                return Subscribers.empty();
            }
        } catch (Throwable e) {
            subscriber.onError(e);
        }
        return new C13341(subscriber, subscriber, iterator);
    }
}
