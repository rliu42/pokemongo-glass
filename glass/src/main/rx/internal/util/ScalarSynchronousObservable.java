package rx.internal.util;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.internal.schedulers.EventLoopsScheduler;

public final class ScalarSynchronousObservable<T> extends Observable<T> {
    private final T f923t;

    /* renamed from: rx.internal.util.ScalarSynchronousObservable.1 */
    class C13441 implements OnSubscribe<T> {
        final /* synthetic */ Object val$t;

        C13441(Object obj) {
            this.val$t = obj;
        }

        public void call(Subscriber<? super T> s) {
            s.onNext(this.val$t);
            s.onCompleted();
        }
    }

    /* renamed from: rx.internal.util.ScalarSynchronousObservable.2 */
    class C13462 implements OnSubscribe<R> {
        final /* synthetic */ Func1 val$func;

        /* renamed from: rx.internal.util.ScalarSynchronousObservable.2.1 */
        class C13451 extends Subscriber<R> {
            final /* synthetic */ Subscriber val$child;

            C13451(Subscriber x0, Subscriber subscriber) {
                this.val$child = subscriber;
                super(x0);
            }

            public void onNext(R v) {
                this.val$child.onNext(v);
            }

            public void onError(Throwable e) {
                this.val$child.onError(e);
            }

            public void onCompleted() {
                this.val$child.onCompleted();
            }
        }

        C13462(Func1 func1) {
            this.val$func = func1;
        }

        public void call(Subscriber<? super R> child) {
            Observable<? extends R> o = (Observable) this.val$func.call(ScalarSynchronousObservable.this.f923t);
            if (o.getClass() == ScalarSynchronousObservable.class) {
                child.onNext(((ScalarSynchronousObservable) o).f923t);
                child.onCompleted();
                return;
            }
            o.unsafeSubscribe(new C13451(child, child));
        }
    }

    static final class DirectScheduledEmission<T> implements OnSubscribe<T> {
        private final EventLoopsScheduler es;
        private final T value;

        DirectScheduledEmission(EventLoopsScheduler es, T value) {
            this.es = es;
            this.value = value;
        }

        public void call(Subscriber<? super T> child) {
            child.add(this.es.scheduleDirect(new ScalarSynchronousAction(this.value, null)));
        }
    }

    static final class NormalScheduledEmission<T> implements OnSubscribe<T> {
        private final Scheduler scheduler;
        private final T value;

        NormalScheduledEmission(Scheduler scheduler, T value) {
            this.scheduler = scheduler;
            this.value = value;
        }

        public void call(Subscriber<? super T> subscriber) {
            Worker worker = this.scheduler.createWorker();
            subscriber.add(worker);
            worker.schedule(new ScalarSynchronousAction(this.value, null));
        }
    }

    static final class ScalarSynchronousAction<T> implements Action0 {
        private final Subscriber<? super T> subscriber;
        private final T value;

        private ScalarSynchronousAction(Subscriber<? super T> subscriber, T value) {
            this.subscriber = subscriber;
            this.value = value;
        }

        public void call() {
            try {
                this.subscriber.onNext(this.value);
                this.subscriber.onCompleted();
            } catch (Throwable t) {
                this.subscriber.onError(t);
            }
        }
    }

    public static final <T> ScalarSynchronousObservable<T> create(T t) {
        return new ScalarSynchronousObservable(t);
    }

    protected ScalarSynchronousObservable(T t) {
        super(new C13441(t));
        this.f923t = t;
    }

    public T get() {
        return this.f923t;
    }

    public Observable<T> scalarScheduleOn(Scheduler scheduler) {
        if (scheduler instanceof EventLoopsScheduler) {
            return Observable.create(new DirectScheduledEmission((EventLoopsScheduler) scheduler, this.f923t));
        }
        return Observable.create(new NormalScheduledEmission(scheduler, this.f923t));
    }

    public <R> Observable<R> scalarFlatMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        return Observable.create(new C13462(func));
    }
}
