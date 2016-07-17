package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

public final class OperatorDebounceWithTime<T> implements Operator<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OperatorDebounceWithTime.1 */
    class C12091 extends Subscriber<T> {
        final Subscriber<?> self;
        final DebounceState<T> state;
        final /* synthetic */ SerializedSubscriber val$s;
        final /* synthetic */ SerialSubscription val$ssub;
        final /* synthetic */ Worker val$worker;

        /* renamed from: rx.internal.operators.OperatorDebounceWithTime.1.1 */
        class C12081 implements Action0 {
            final /* synthetic */ int val$index;

            C12081(int i) {
                this.val$index = i;
            }

            public void call() {
                C12091.this.state.emit(this.val$index, C12091.this.val$s, C12091.this.self);
            }
        }

        C12091(Subscriber x0, SerialSubscription serialSubscription, Worker worker, SerializedSubscriber serializedSubscriber) {
            this.val$ssub = serialSubscription;
            this.val$worker = worker;
            this.val$s = serializedSubscriber;
            super(x0);
            this.state = new DebounceState();
            this.self = this;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(T t) {
            this.val$ssub.set(this.val$worker.schedule(new C12081(this.state.next(t)), OperatorDebounceWithTime.this.timeout, OperatorDebounceWithTime.this.unit));
        }

        public void onError(Throwable e) {
            this.val$s.onError(e);
            unsubscribe();
            this.state.clear();
        }

        public void onCompleted() {
            this.state.emitAndComplete(this.val$s, this);
        }
    }

    static final class DebounceState<T> {
        boolean emitting;
        boolean hasValue;
        int index;
        boolean terminate;
        T value;

        DebounceState() {
        }

        public synchronized int next(T value) {
            int i;
            this.value = value;
            this.hasValue = true;
            i = this.index + 1;
            this.index = i;
            return i;
        }

        public void emit(int index, Subscriber<T> onNextAndComplete, Subscriber<?> onError) {
            synchronized (this) {
                if (!this.emitting && this.hasValue && index == this.index) {
                    T localValue = this.value;
                    this.value = null;
                    this.hasValue = false;
                    this.emitting = true;
                    try {
                        onNextAndComplete.onNext(localValue);
                        synchronized (this) {
                            if (this.terminate) {
                                onNextAndComplete.onCompleted();
                                return;
                            }
                            this.emitting = false;
                            return;
                        }
                    } catch (Throwable e) {
                        onError.onError(e);
                        return;
                    }
                }
            }
        }

        public void emitAndComplete(Subscriber<T> onNextAndComplete, Subscriber<?> onError) {
            synchronized (this) {
                if (this.emitting) {
                    this.terminate = true;
                    return;
                }
                T localValue = this.value;
                boolean localHasValue = this.hasValue;
                this.value = null;
                this.hasValue = false;
                this.emitting = true;
                if (localHasValue) {
                    try {
                        onNextAndComplete.onNext(localValue);
                    } catch (Throwable e) {
                        onError.onError(e);
                        return;
                    }
                }
                onNextAndComplete.onCompleted();
            }
        }

        public synchronized void clear() {
            this.index++;
            this.value = null;
            this.hasValue = false;
        }
    }

    public OperatorDebounceWithTime(long timeout, TimeUnit unit, Scheduler scheduler) {
        this.timeout = timeout;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Worker worker = this.scheduler.createWorker();
        SerializedSubscriber<T> s = new SerializedSubscriber(child);
        SerialSubscription ssub = new SerialSubscription();
        s.add(worker);
        s.add(ssub);
        return new C12091(child, ssub, worker, s);
    }
}
