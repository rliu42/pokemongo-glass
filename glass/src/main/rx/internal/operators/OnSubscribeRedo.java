package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import rx.Notification;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observable.Operator;
import rx.Producer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.internal.producers.ProducerArbiter;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.SerialSubscription;

public final class OnSubscribeRedo<T> implements OnSubscribe<T> {
    static final Func1<Observable<? extends Notification<?>>, Observable<?>> REDO_INFINITE;
    private final Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> controlHandlerFunction;
    private final Scheduler scheduler;
    private final Observable<T> source;
    private final boolean stopOnComplete;
    private final boolean stopOnError;

    /* renamed from: rx.internal.operators.OnSubscribeRedo.1 */
    static class C11711 implements Func1<Observable<? extends Notification<?>>, Observable<?>> {

        /* renamed from: rx.internal.operators.OnSubscribeRedo.1.1 */
        class C11701 implements Func1<Notification<?>, Notification<?>> {
            C11701() {
            }

            public Notification<?> call(Notification<?> notification) {
                return Notification.createOnNext(null);
            }
        }

        C11711() {
        }

        public Observable<?> call(Observable<? extends Notification<?>> ts) {
            return ts.map(new C11701());
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRedo.2 */
    class C11732 implements Action0 {
        final /* synthetic */ ProducerArbiter val$arbiter;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ AtomicLong val$consumerCapacity;
        final /* synthetic */ SerialSubscription val$sourceSubscriptions;
        final /* synthetic */ BehaviorSubject val$terminals;

        /* renamed from: rx.internal.operators.OnSubscribeRedo.2.1 */
        class C11721 extends Subscriber<T> {
            boolean done;

            C11721() {
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    unsubscribe();
                    C11732.this.val$terminals.onNext(Notification.createOnCompleted());
                }
            }

            public void onError(Throwable e) {
                if (!this.done) {
                    this.done = true;
                    unsubscribe();
                    C11732.this.val$terminals.onNext(Notification.createOnError(e));
                }
            }

            public void onNext(T v) {
                if (!this.done) {
                    C11732.this.val$child.onNext(v);
                    decrementConsumerCapacity();
                    C11732.this.val$arbiter.produced(1);
                }
            }

            private void decrementConsumerCapacity() {
                long cc;
                do {
                    cc = C11732.this.val$consumerCapacity.get();
                    if (cc == Long.MAX_VALUE) {
                        return;
                    }
                } while (!C11732.this.val$consumerCapacity.compareAndSet(cc, cc - 1));
            }

            public void setProducer(Producer producer) {
                C11732.this.val$arbiter.setProducer(producer);
            }
        }

        C11732(Subscriber subscriber, BehaviorSubject behaviorSubject, ProducerArbiter producerArbiter, AtomicLong atomicLong, SerialSubscription serialSubscription) {
            this.val$child = subscriber;
            this.val$terminals = behaviorSubject;
            this.val$arbiter = producerArbiter;
            this.val$consumerCapacity = atomicLong;
            this.val$sourceSubscriptions = serialSubscription;
        }

        public void call() {
            if (!this.val$child.isUnsubscribed()) {
                Subscriber<T> terminalDelegatingSubscriber = new C11721();
                this.val$sourceSubscriptions.set(terminalDelegatingSubscriber);
                OnSubscribeRedo.this.source.unsafeSubscribe(terminalDelegatingSubscriber);
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRedo.3 */
    class C11753 implements Operator<Notification<?>, Notification<?>> {

        /* renamed from: rx.internal.operators.OnSubscribeRedo.3.1 */
        class C11741 extends Subscriber<Notification<?>> {
            final /* synthetic */ Subscriber val$filteredTerminals;

            C11741(Subscriber x0, Subscriber subscriber) {
                this.val$filteredTerminals = subscriber;
                super(x0);
            }

            public void onCompleted() {
                this.val$filteredTerminals.onCompleted();
            }

            public void onError(Throwable e) {
                this.val$filteredTerminals.onError(e);
            }

            public void onNext(Notification<?> t) {
                if (t.isOnCompleted() && OnSubscribeRedo.this.stopOnComplete) {
                    this.val$filteredTerminals.onCompleted();
                } else if (t.isOnError() && OnSubscribeRedo.this.stopOnError) {
                    this.val$filteredTerminals.onError(t.getThrowable());
                } else {
                    this.val$filteredTerminals.onNext(t);
                }
            }

            public void setProducer(Producer producer) {
                producer.request(Long.MAX_VALUE);
            }
        }

        C11753() {
        }

        public Subscriber<? super Notification<?>> call(Subscriber<? super Notification<?>> filteredTerminals) {
            return new C11741(filteredTerminals, filteredTerminals);
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRedo.4 */
    class C11774 implements Action0 {
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ AtomicLong val$consumerCapacity;
        final /* synthetic */ Observable val$restarts;
        final /* synthetic */ AtomicBoolean val$resumeBoundary;
        final /* synthetic */ Action0 val$subscribeToSource;
        final /* synthetic */ Worker val$worker;

        /* renamed from: rx.internal.operators.OnSubscribeRedo.4.1 */
        class C11761 extends Subscriber<Object> {
            C11761(Subscriber x0) {
                super(x0);
            }

            public void onCompleted() {
                C11774.this.val$child.onCompleted();
            }

            public void onError(Throwable e) {
                C11774.this.val$child.onError(e);
            }

            public void onNext(Object t) {
                if (!C11774.this.val$child.isUnsubscribed()) {
                    if (C11774.this.val$consumerCapacity.get() > 0) {
                        C11774.this.val$worker.schedule(C11774.this.val$subscribeToSource);
                    } else {
                        C11774.this.val$resumeBoundary.compareAndSet(false, true);
                    }
                }
            }

            public void setProducer(Producer producer) {
                producer.request(Long.MAX_VALUE);
            }
        }

        C11774(Observable observable, Subscriber subscriber, AtomicLong atomicLong, Worker worker, Action0 action0, AtomicBoolean atomicBoolean) {
            this.val$restarts = observable;
            this.val$child = subscriber;
            this.val$consumerCapacity = atomicLong;
            this.val$worker = worker;
            this.val$subscribeToSource = action0;
            this.val$resumeBoundary = atomicBoolean;
        }

        public void call() {
            this.val$restarts.unsafeSubscribe(new C11761(this.val$child));
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRedo.5 */
    class C11785 implements Producer {
        final /* synthetic */ ProducerArbiter val$arbiter;
        final /* synthetic */ AtomicLong val$consumerCapacity;
        final /* synthetic */ AtomicBoolean val$resumeBoundary;
        final /* synthetic */ Action0 val$subscribeToSource;
        final /* synthetic */ Worker val$worker;

        C11785(AtomicLong atomicLong, ProducerArbiter producerArbiter, AtomicBoolean atomicBoolean, Worker worker, Action0 action0) {
            this.val$consumerCapacity = atomicLong;
            this.val$arbiter = producerArbiter;
            this.val$resumeBoundary = atomicBoolean;
            this.val$worker = worker;
            this.val$subscribeToSource = action0;
        }

        public void request(long n) {
            if (n > 0) {
                BackpressureUtils.getAndAddRequest(this.val$consumerCapacity, n);
                this.val$arbiter.request(n);
                if (this.val$resumeBoundary.compareAndSet(true, false)) {
                    this.val$worker.schedule(this.val$subscribeToSource);
                }
            }
        }
    }

    public static final class RedoFinite implements Func1<Observable<? extends Notification<?>>, Observable<?>> {
        private final long count;

        /* renamed from: rx.internal.operators.OnSubscribeRedo.RedoFinite.1 */
        class C11791 implements Func1<Notification<?>, Notification<?>> {
            int num;

            C11791() {
                this.num = 0;
            }

            public Notification<?> call(Notification<?> terminalNotification) {
                if (RedoFinite.this.count == 0) {
                    return terminalNotification;
                }
                this.num++;
                if (((long) this.num) <= RedoFinite.this.count) {
                    return Notification.createOnNext(Integer.valueOf(this.num));
                }
                return terminalNotification;
            }
        }

        public RedoFinite(long count) {
            this.count = count;
        }

        public Observable<?> call(Observable<? extends Notification<?>> ts) {
            return ts.map(new C11791()).dematerialize();
        }
    }

    public static final class RetryWithPredicate implements Func1<Observable<? extends Notification<?>>, Observable<? extends Notification<?>>> {
        private Func2<Integer, Throwable, Boolean> predicate;

        /* renamed from: rx.internal.operators.OnSubscribeRedo.RetryWithPredicate.1 */
        class C11801 implements Func2<Notification<Integer>, Notification<?>, Notification<Integer>> {
            C11801() {
            }

            public Notification<Integer> call(Notification<Integer> n, Notification<?> term) {
                int value = ((Integer) n.getValue()).intValue();
                if (((Boolean) RetryWithPredicate.this.predicate.call(Integer.valueOf(value), term.getThrowable())).booleanValue()) {
                    return Notification.createOnNext(Integer.valueOf(value + 1));
                }
                return term;
            }
        }

        public RetryWithPredicate(Func2<Integer, Throwable, Boolean> predicate) {
            this.predicate = predicate;
        }

        public Observable<? extends Notification<?>> call(Observable<? extends Notification<?>> ts) {
            return ts.scan(Notification.createOnNext(Integer.valueOf(0)), new C11801());
        }
    }

    static {
        REDO_INFINITE = new C11711();
    }

    public static <T> Observable<T> retry(Observable<T> source) {
        return retry((Observable) source, REDO_INFINITE);
    }

    public static <T> Observable<T> retry(Observable<T> source, long count) {
        if (count >= 0) {
            return count == 0 ? source : retry((Observable) source, new RedoFinite(count));
        } else {
            throw new IllegalArgumentException("count >= 0 expected");
        }
    }

    public static <T> Observable<T> retry(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler) {
        return Observable.create(new OnSubscribeRedo(source, notificationHandler, true, false, Schedulers.trampoline()));
    }

    public static <T> Observable<T> retry(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        return Observable.create(new OnSubscribeRedo(source, notificationHandler, true, false, scheduler));
    }

    public static <T> Observable<T> repeat(Observable<T> source) {
        return repeat((Observable) source, Schedulers.trampoline());
    }

    public static <T> Observable<T> repeat(Observable<T> source, Scheduler scheduler) {
        return repeat((Observable) source, REDO_INFINITE, scheduler);
    }

    public static <T> Observable<T> repeat(Observable<T> source, long count) {
        return repeat((Observable) source, count, Schedulers.trampoline());
    }

    public static <T> Observable<T> repeat(Observable<T> source, long count, Scheduler scheduler) {
        if (count == 0) {
            return Observable.empty();
        }
        if (count >= 0) {
            return repeat((Observable) source, new RedoFinite(count - 1), scheduler);
        }
        throw new IllegalArgumentException("count >= 0 expected");
    }

    public static <T> Observable<T> repeat(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler) {
        return Observable.create(new OnSubscribeRedo(source, notificationHandler, false, true, Schedulers.trampoline()));
    }

    public static <T> Observable<T> repeat(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        return Observable.create(new OnSubscribeRedo(source, notificationHandler, false, true, scheduler));
    }

    public static <T> Observable<T> redo(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        return Observable.create(new OnSubscribeRedo(source, notificationHandler, false, false, scheduler));
    }

    private OnSubscribeRedo(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> f, boolean stopOnComplete, boolean stopOnError, Scheduler scheduler) {
        this.source = source;
        this.controlHandlerFunction = f;
        this.stopOnComplete = stopOnComplete;
        this.stopOnError = stopOnError;
        this.scheduler = scheduler;
    }

    public void call(Subscriber<? super T> child) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        AtomicLong consumerCapacity = new AtomicLong(0);
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        SerialSubscription sourceSubscriptions = new SerialSubscription();
        child.add(sourceSubscriptions);
        BehaviorSubject<Notification<?>> terminals = BehaviorSubject.create();
        terminals.subscribe((Subscriber) Subscribers.empty());
        ProducerArbiter arbiter = new ProducerArbiter();
        Action0 subscribeToSource = new C11732(child, terminals, arbiter, consumerCapacity, sourceSubscriptions);
        worker.schedule(new C11774((Observable) this.controlHandlerFunction.call(terminals.lift(new C11753())), child, consumerCapacity, worker, subscribeToSource, atomicBoolean));
        child.setProducer(new C11785(consumerCapacity, arbiter, atomicBoolean, worker, subscribeToSource));
    }
}
