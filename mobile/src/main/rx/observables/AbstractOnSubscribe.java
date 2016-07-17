package rx.observables;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.annotations.Experimental;
import rx.exceptions.CompositeException;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.internal.operators.BackpressureUtils;

@Experimental
public abstract class AbstractOnSubscribe<T, S> implements OnSubscribe<T> {
    private static final Func1<Object, Object> NULL_FUNC1;

    /* renamed from: rx.observables.AbstractOnSubscribe.1 */
    static class C13491 implements Func1<Object, Object> {
        C13491() {
        }

        public Object call(Object t1) {
            return null;
        }
    }

    private static final class LambdaOnSubscribe<T, S> extends AbstractOnSubscribe<T, S> {
        final Action1<SubscriptionState<T, S>> next;
        final Func1<? super Subscriber<? super T>, ? extends S> onSubscribe;
        final Action1<? super S> onTerminated;

        public /* bridge */ /* synthetic */ void call(Object x0) {
            super.call((Subscriber) x0);
        }

        private LambdaOnSubscribe(Action1<SubscriptionState<T, S>> next, Func1<? super Subscriber<? super T>, ? extends S> onSubscribe, Action1<? super S> onTerminated) {
            this.next = next;
            this.onSubscribe = onSubscribe;
            this.onTerminated = onTerminated;
        }

        protected S onSubscribe(Subscriber<? super T> subscriber) {
            return this.onSubscribe.call(subscriber);
        }

        protected void onTerminated(S state) {
            this.onTerminated.call(state);
        }

        protected void next(SubscriptionState<T, S> state) {
            this.next.call(state);
        }
    }

    private static final class SubscriptionCompleter<T, S> extends AtomicBoolean implements Subscription {
        private static final long serialVersionUID = 7993888274897325004L;
        private final SubscriptionState<T, S> state;

        private SubscriptionCompleter(SubscriptionState<T, S> state) {
            this.state = state;
        }

        public boolean isUnsubscribed() {
            return get();
        }

        public void unsubscribe() {
            if (compareAndSet(false, true)) {
                this.state.free();
            }
        }
    }

    private static final class SubscriptionProducer<T, S> implements Producer {
        final SubscriptionState<T, S> state;

        private SubscriptionProducer(SubscriptionState<T, S> state) {
            this.state = state;
        }

        public void request(long n) {
            if (n > 0 && BackpressureUtils.getAndAddRequest(this.state.requestCount, n) == 0) {
                if (n == Long.MAX_VALUE) {
                    while (!this.state.subscriber.isUnsubscribed()) {
                        if (!doNext()) {
                            return;
                        }
                    }
                } else if (!this.state.subscriber.isUnsubscribed()) {
                    while (doNext() && this.state.requestCount.decrementAndGet() > 0) {
                        if (this.state.subscriber.isUnsubscribed()) {
                            return;
                        }
                    }
                }
            }
        }

        protected boolean doNext() {
            if (!this.state.use()) {
                return false;
            }
            try {
                int p = this.state.phase();
                this.state.parent.next(this.state);
                if (!this.state.verify()) {
                    throw new IllegalStateException("No event produced or stop called @ Phase: " + p + " -> " + this.state.phase() + ", Calls: " + this.state.calls());
                } else if (this.state.accept() || this.state.stopRequested()) {
                    this.state.terminate();
                    return false;
                } else {
                    this.state.calls = 1 + this.state.calls;
                    this.state.free();
                    return true;
                }
            } catch (Throwable t) {
                this.state.terminate();
                this.state.subscriber.onError(t);
            } finally {
                this.state.free();
            }
        }
    }

    public static final class SubscriptionState<T, S> {
        private long calls;
        private boolean hasCompleted;
        private boolean hasOnNext;
        private final AtomicInteger inUse;
        private final AbstractOnSubscribe<T, S> parent;
        private int phase;
        private final AtomicLong requestCount;
        private final S state;
        private boolean stopRequested;
        private final Subscriber<? super T> subscriber;
        private Throwable theException;
        private T theValue;

        private SubscriptionState(AbstractOnSubscribe<T, S> parent, Subscriber<? super T> subscriber, S state) {
            this.parent = parent;
            this.subscriber = subscriber;
            this.state = state;
            this.requestCount = new AtomicLong();
            this.inUse = new AtomicInteger(1);
        }

        public S state() {
            return this.state;
        }

        public int phase() {
            return this.phase;
        }

        public void phase(int newPhase) {
            this.phase = newPhase;
        }

        public void advancePhase() {
            advancePhaseBy(1);
        }

        public void advancePhaseBy(int amount) {
            this.phase += amount;
        }

        public long calls() {
            return this.calls;
        }

        public void onNext(T value) {
            if (this.hasOnNext) {
                throw new IllegalStateException("onNext not consumed yet!");
            } else if (this.hasCompleted) {
                throw new IllegalStateException("Already terminated", this.theException);
            } else {
                this.theValue = value;
                this.hasOnNext = true;
            }
        }

        public void onError(Throwable e) {
            if (e == null) {
                throw new NullPointerException("e != null required");
            } else if (this.hasCompleted) {
                throw new IllegalStateException("Already terminated", this.theException);
            } else {
                this.theException = e;
                this.hasCompleted = true;
            }
        }

        public void onCompleted() {
            if (this.hasCompleted) {
                throw new IllegalStateException("Already terminated", this.theException);
            }
            this.hasCompleted = true;
        }

        public void stop() {
            this.stopRequested = true;
        }

        protected boolean accept() {
            Throwable e;
            if (this.hasOnNext) {
                T value = this.theValue;
                this.theValue = null;
                this.hasOnNext = false;
                try {
                    this.subscriber.onNext(value);
                } catch (Throwable t) {
                    this.hasCompleted = true;
                    e = this.theException;
                    this.theException = null;
                    if (e == null) {
                        this.subscriber.onError(t);
                        return true;
                    }
                    this.subscriber.onError(new CompositeException(Arrays.asList(new Throwable[]{t, e})));
                    return true;
                }
            }
            if (!this.hasCompleted) {
                return false;
            }
            e = this.theException;
            this.theException = null;
            if (e != null) {
                this.subscriber.onError(e);
                return true;
            }
            this.subscriber.onCompleted();
            return true;
        }

        protected boolean verify() {
            return this.hasOnNext || this.hasCompleted || this.stopRequested;
        }

        protected boolean stopRequested() {
            return this.stopRequested;
        }

        protected boolean use() {
            int i = this.inUse.get();
            if (i == 0) {
                return false;
            }
            if (i == 1 && this.inUse.compareAndSet(1, 2)) {
                return true;
            }
            throw new IllegalStateException("This is not reentrant nor threadsafe!");
        }

        protected void free() {
            if (this.inUse.get() > 0 && this.inUse.decrementAndGet() == 0) {
                this.parent.onTerminated(this.state);
            }
        }

        protected void terminate() {
            int i;
            do {
                i = this.inUse.get();
                if (i <= 0) {
                    return;
                }
            } while (!this.inUse.compareAndSet(i, 0));
            this.parent.onTerminated(this.state);
        }
    }

    protected abstract void next(SubscriptionState<T, S> subscriptionState);

    protected S onSubscribe(Subscriber<? super T> subscriber) {
        return null;
    }

    protected void onTerminated(S s) {
    }

    public final void call(Subscriber<? super T> subscriber) {
        SubscriptionState<T, S> state = new SubscriptionState(subscriber, onSubscribe(subscriber), null);
        subscriber.add(new SubscriptionCompleter(null));
        subscriber.setProducer(new SubscriptionProducer(null));
    }

    public final Observable<T> toObservable() {
        return Observable.create(this);
    }

    static {
        NULL_FUNC1 = new C13491();
    }

    public static <T, S> AbstractOnSubscribe<T, S> create(Action1<SubscriptionState<T, S>> next) {
        return create(next, NULL_FUNC1, Actions.empty());
    }

    public static <T, S> AbstractOnSubscribe<T, S> create(Action1<SubscriptionState<T, S>> next, Func1<? super Subscriber<? super T>, ? extends S> onSubscribe) {
        return create(next, onSubscribe, Actions.empty());
    }

    public static <T, S> AbstractOnSubscribe<T, S> create(Action1<SubscriptionState<T, S>> next, Func1<? super Subscriber<? super T>, ? extends S> onSubscribe, Action1<? super S> onTerminated) {
        return new LambdaOnSubscribe(onSubscribe, onTerminated, null);
    }
}
