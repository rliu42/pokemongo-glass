package rx.internal.operators;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.observers.SafeSubscriber;
import rx.subjects.Subject;

public final class OnSubscribeMulticastSelector<TInput, TIntermediate, TResult> implements OnSubscribe<TResult> {
    final Func1<? super Observable<TIntermediate>, ? extends Observable<TResult>> resultSelector;
    final Observable<? extends TInput> source;
    final Func0<? extends Subject<? super TInput, ? extends TIntermediate>> subjectFactory;

    /* renamed from: rx.internal.operators.OnSubscribeMulticastSelector.1 */
    class C11681 implements Action1<Subscription> {
        final /* synthetic */ SafeSubscriber val$s;

        C11681(SafeSubscriber safeSubscriber) {
            this.val$s = safeSubscriber;
        }

        public void call(Subscription t1) {
            this.val$s.add(t1);
        }
    }

    public OnSubscribeMulticastSelector(Observable<? extends TInput> source, Func0<? extends Subject<? super TInput, ? extends TIntermediate>> subjectFactory, Func1<? super Observable<TIntermediate>, ? extends Observable<TResult>> resultSelector) {
        this.source = source;
        this.subjectFactory = subjectFactory;
        this.resultSelector = resultSelector;
    }

    public void call(Subscriber<? super TResult> child) {
        try {
            ConnectableObservable<TIntermediate> connectable = new OperatorMulticast(this.source, this.subjectFactory);
            Observable<TResult> observable = (Observable) this.resultSelector.call(connectable);
            SafeSubscriber<TResult> s = new SafeSubscriber(child);
            observable.unsafeSubscribe(s);
            connectable.connect(new C11681(s));
        } catch (Throwable t) {
            child.onError(t);
        }
    }
}
