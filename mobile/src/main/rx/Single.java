package rx;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import rx.Observable.Operator;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.functions.Func6;
import rx.functions.Func7;
import rx.functions.Func8;
import rx.functions.Func9;
import rx.internal.operators.OnSubscribeToObservableFuture;
import rx.internal.operators.OperatorMap;
import rx.internal.operators.OperatorObserveOn;
import rx.internal.operators.OperatorOnErrorReturn;
import rx.internal.operators.OperatorSubscribeOn;
import rx.internal.operators.OperatorTimeout;
import rx.internal.operators.OperatorZip;
import rx.internal.producers.SingleDelayedProducer;
import rx.observers.SafeSubscriber;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

@Experimental
public class Single<T> {
    private static final RxJavaObservableExecutionHook hook;
    final rx.Observable.OnSubscribe<T> onSubscribe;

    /* renamed from: rx.Single.1 */
    class C11201 implements rx.Observable.OnSubscribe<T> {
        final /* synthetic */ OnSubscribe val$f;

        /* renamed from: rx.Single.1.1 */
        class C11191 extends SingleSubscriber<T> {
            final /* synthetic */ Subscriber val$child;
            final /* synthetic */ SingleDelayedProducer val$producer;

            C11191(SingleDelayedProducer singleDelayedProducer, Subscriber subscriber) {
                this.val$producer = singleDelayedProducer;
                this.val$child = subscriber;
            }

            public void onSuccess(T value) {
                this.val$producer.setValue(value);
            }

            public void onError(Throwable error) {
                this.val$child.onError(error);
            }
        }

        C11201(OnSubscribe onSubscribe) {
            this.val$f = onSubscribe;
        }

        public void call(Subscriber<? super T> child) {
            SingleDelayedProducer<T> producer = new SingleDelayedProducer(child);
            child.setProducer(producer);
            SingleSubscriber<T> ss = new C11191(producer, child);
            child.add(ss);
            this.val$f.call(ss);
        }
    }

    /* renamed from: rx.Single.2 */
    class C11212 implements rx.Observable.OnSubscribe<R> {
        final /* synthetic */ Operator val$lift;

        C11212(Operator operator) {
            this.val$lift = operator;
        }

        public void call(Subscriber<? super R> o) {
            OnErrorNotImplementedException e;
            Subscriber<? super T> st;
            try {
                st = (Subscriber) Single.hook.onLift(this.val$lift).call(o);
                st.onStart();
                Single.this.onSubscribe.call(st);
            } catch (Throwable e2) {
                if (e2 instanceof OnErrorNotImplementedException) {
                    e = (OnErrorNotImplementedException) e2;
                } else {
                    o.onError(e2);
                }
            }
        }
    }

    public interface OnSubscribe<T> extends Action1<SingleSubscriber<? super T>> {
    }

    /* renamed from: rx.Single.3 */
    static class C11223 implements OnSubscribe<T> {
        final /* synthetic */ Throwable val$exception;

        C11223(Throwable th) {
            this.val$exception = th;
        }

        public void call(SingleSubscriber<? super T> te) {
            te.onError(this.val$exception);
        }
    }

    /* renamed from: rx.Single.4 */
    static class C11234 implements OnSubscribe<T> {
        final /* synthetic */ Object val$value;

        C11234(Object obj) {
            this.val$value = obj;
        }

        public void call(SingleSubscriber<? super T> te) {
            te.onSuccess(this.val$value);
        }
    }

    /* renamed from: rx.Single.5 */
    static class C11255 implements OnSubscribe<T> {
        final /* synthetic */ Single val$source;

        /* renamed from: rx.Single.5.1 */
        class C11241 extends SingleSubscriber<Single<? extends T>> {
            final /* synthetic */ SingleSubscriber val$child;

            C11241(SingleSubscriber singleSubscriber) {
                this.val$child = singleSubscriber;
            }

            public void onSuccess(Single<? extends T> innerSingle) {
                innerSingle.subscribe(this.val$child);
            }

            public void onError(Throwable error) {
                this.val$child.onError(error);
            }
        }

        C11255(Single single) {
            this.val$source = single;
        }

        public void call(SingleSubscriber<? super T> child) {
            this.val$source.subscribe(new C11241(child));
        }
    }

    /* renamed from: rx.Single.6 */
    class C11266 extends Subscriber<T> {
        C11266() {
        }

        public final void onCompleted() {
        }

        public final void onError(Throwable e) {
            throw new OnErrorNotImplementedException(e);
        }

        public final void onNext(T t) {
        }
    }

    /* renamed from: rx.Single.7 */
    class C11277 extends Subscriber<T> {
        final /* synthetic */ Action1 val$onSuccess;

        C11277(Action1 action1) {
            this.val$onSuccess = action1;
        }

        public final void onCompleted() {
        }

        public final void onError(Throwable e) {
            throw new OnErrorNotImplementedException(e);
        }

        public final void onNext(T args) {
            this.val$onSuccess.call(args);
        }
    }

    /* renamed from: rx.Single.8 */
    class C11288 extends Subscriber<T> {
        final /* synthetic */ Action1 val$onError;
        final /* synthetic */ Action1 val$onSuccess;

        C11288(Action1 action1, Action1 action12) {
            this.val$onError = action1;
            this.val$onSuccess = action12;
        }

        public final void onCompleted() {
        }

        public final void onError(Throwable e) {
            this.val$onError.call(e);
        }

        public final void onNext(T args) {
            this.val$onSuccess.call(args);
        }
    }

    /* renamed from: rx.Single.9 */
    class C11299 extends Subscriber<T> {
        final /* synthetic */ SingleSubscriber val$te;

        C11299(SingleSubscriber singleSubscriber) {
            this.val$te = singleSubscriber;
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            this.val$te.onError(e);
        }

        public void onNext(T t) {
            this.val$te.onSuccess(t);
        }
    }

    public interface Transformer<T, R> extends Func1<Single<T>, Single<R>> {
    }

    protected Single(OnSubscribe<T> f) {
        this.onSubscribe = new C11201(f);
    }

    private Single(rx.Observable.OnSubscribe<T> f) {
        this.onSubscribe = f;
    }

    static {
        hook = RxJavaPlugins.getInstance().getObservableExecutionHook();
    }

    public static final <T> Single<T> create(OnSubscribe<T> f) {
        return new Single((OnSubscribe) f);
    }

    private final <R> Single<R> lift(Operator<? extends R, ? super T> lift) {
        return new Single(new C11212(lift));
    }

    public <R> Single<R> compose(Transformer<? super T, ? extends R> transformer) {
        return (Single) transformer.call(this);
    }

    private static <T> Observable<T> asObservable(Single<T> t) {
        return Observable.create(t.onSubscribe);
    }

    private final Single<Observable<T>> nest() {
        return just(asObservable(this));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2) {
        return Observable.concat(asObservable(t1), asObservable(t2));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8, Single<? extends T> t9) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8), asObservable(t9));
    }

    public static final <T> Single<T> error(Throwable exception) {
        return create(new C11223(exception));
    }

    public static final <T> Single<T> from(Future<? extends T> future) {
        return new Single(OnSubscribeToObservableFuture.toObservableFuture(future));
    }

    public static final <T> Single<T> from(Future<? extends T> future, long timeout, TimeUnit unit) {
        return new Single(OnSubscribeToObservableFuture.toObservableFuture(future, timeout, unit));
    }

    public static final <T> Single<T> from(Future<? extends T> future, Scheduler scheduler) {
        return new Single(OnSubscribeToObservableFuture.toObservableFuture(future)).subscribeOn(scheduler);
    }

    public static final <T> Single<T> just(T value) {
        return create(new C11234(value));
    }

    public static final <T> Single<T> merge(Single<? extends Single<? extends T>> source) {
        return create(new C11255(source));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2) {
        return Observable.merge(asObservable(t1), asObservable(t2));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8, Single<? extends T> t9) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8), asObservable(t9));
    }

    public static final <T1, T2, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Func2<? super T1, ? super T2, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2)}).lift(new OperatorZip((Func2) zipFunction));
    }

    public static final <T1, T2, T3, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Func3<? super T1, ? super T2, ? super T3, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3)}).lift(new OperatorZip((Func3) zipFunction));
    }

    public static final <T1, T2, T3, T4, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4)}).lift(new OperatorZip((Func4) zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Single<? extends T5> o5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4), asObservable(o5)}).lift(new OperatorZip((Func5) zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Single<? extends T5> o5, Single<? extends T6> o6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4), asObservable(o5), asObservable(o6)}).lift(new OperatorZip((Func6) zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Single<? extends T5> o5, Single<? extends T6> o6, Single<? extends T7> o7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4), asObservable(o5), asObservable(o6), asObservable(o7)}).lift(new OperatorZip((Func7) zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Single<? extends T5> o5, Single<? extends T6> o6, Single<? extends T7> o7, Single<? extends T8> o8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4), asObservable(o5), asObservable(o6), asObservable(o7), asObservable(o8)}).lift(new OperatorZip((Func8) zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Single<? extends T5> o5, Single<? extends T6> o6, Single<? extends T7> o7, Single<? extends T8> o8, Single<? extends T9> o9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4), asObservable(o5), asObservable(o6), asObservable(o7), asObservable(o8), asObservable(o9)}).lift(new OperatorZip((Func9) zipFunction));
    }

    public final Observable<T> concatWith(Single<? extends T> t1) {
        return concat(this, t1);
    }

    public final <R> Single<R> flatMap(Func1<? super T, ? extends Single<? extends R>> func) {
        return merge(map(func));
    }

    public final <R> Observable<R> flatMapObservable(Func1<? super T, ? extends Observable<? extends R>> func) {
        return Observable.merge(asObservable(map(func)));
    }

    public final <R> Single<R> map(Func1<? super T, ? extends R> func) {
        return lift(new OperatorMap(func));
    }

    public final Observable<T> mergeWith(Single<? extends T> t1) {
        return merge(this, t1);
    }

    public final Single<T> observeOn(Scheduler scheduler) {
        return lift(new OperatorObserveOn(scheduler));
    }

    public final Single<T> onErrorReturn(Func1<Throwable, ? extends T> resumeFunction) {
        return lift(new OperatorOnErrorReturn(resumeFunction));
    }

    public final Subscription subscribe() {
        return subscribe(new C11266());
    }

    public final Subscription subscribe(Action1<? super T> onSuccess) {
        if (onSuccess != null) {
            return subscribe(new C11277(onSuccess));
        }
        throw new IllegalArgumentException("onSuccess can not be null");
    }

    public final Subscription subscribe(Action1<? super T> onSuccess, Action1<Throwable> onError) {
        if (onSuccess == null) {
            throw new IllegalArgumentException("onSuccess can not be null");
        } else if (onError != null) {
            return subscribe(new C11288(onError, onSuccess));
        } else {
            throw new IllegalArgumentException("onError can not be null");
        }
    }

    public final void unsafeSubscribe(Subscriber<? super T> subscriber) {
        try {
            subscriber.onStart();
            this.onSubscribe.call(subscriber);
            hook.onSubscribeReturn(subscriber);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            try {
                subscriber.onError(hook.onSubscribeError(e));
            } catch (OnErrorNotImplementedException e2) {
                throw e2;
            } catch (Throwable e22) {
                hook.onSubscribeError(new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e22));
            }
        }
    }

    public final Subscription subscribe(Subscriber<? super T> subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("observer can not be null");
        } else if (this.onSubscribe == null) {
            throw new IllegalStateException("onSubscribe function can not be null.");
        } else {
            subscriber.onStart();
            if (!(subscriber instanceof SafeSubscriber)) {
                subscriber = new SafeSubscriber(subscriber);
            }
            try {
                this.onSubscribe.call(subscriber);
                return hook.onSubscribeReturn(subscriber);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                try {
                    subscriber.onError(hook.onSubscribeError(e));
                    return Subscriptions.empty();
                } catch (OnErrorNotImplementedException e2) {
                    throw e2;
                } catch (Throwable e22) {
                    hook.onSubscribeError(new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e22));
                }
            }
        }
    }

    public final Subscription subscribe(SingleSubscriber<? super T> te) {
        Subscriber s = new C11299(te);
        te.add(s);
        subscribe(s);
        return s;
    }

    public final Single<T> subscribeOn(Scheduler scheduler) {
        return nest().lift(new OperatorSubscribeOn(scheduler));
    }

    public final Observable<T> toObservable() {
        return asObservable(this);
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout(timeout, timeUnit, null, Schedulers.computation());
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout(timeout, timeUnit, null, scheduler);
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit, Single<? extends T> other) {
        return timeout(timeout, timeUnit, other, Schedulers.computation());
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit, Single<? extends T> other, Scheduler scheduler) {
        if (other == null) {
            other = error(new TimeoutException());
        }
        return lift(new OperatorTimeout(timeout, timeUnit, asObservable(other), scheduler));
    }

    public final <T2, R> Single<R> zipWith(Single<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return zip(this, other, zipFunction);
    }
}
