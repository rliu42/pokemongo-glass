package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;
import rx.functions.Func2;

public final class OperatorScan<R, T> implements Operator<R, T> {
    private static final Object NO_INITIAL_VALUE;
    private final Func2<R, ? super T, R> accumulator;
    private final Func0<R> initialValueFactory;

    /* renamed from: rx.internal.operators.OperatorScan.1 */
    class C12691 implements Func0<R> {
        final /* synthetic */ Object val$initialValue;

        C12691(Object obj) {
            this.val$initialValue = obj;
        }

        public R call() {
            return this.val$initialValue;
        }
    }

    /* renamed from: rx.internal.operators.OperatorScan.2 */
    class C12712 extends Subscriber<T> {
        private final R initialValue;
        boolean initialized;
        final /* synthetic */ Subscriber val$child;
        private R value;

        /* renamed from: rx.internal.operators.OperatorScan.2.1 */
        class C12701 implements Producer {
            final AtomicBoolean excessive;
            final AtomicBoolean once;
            final /* synthetic */ Producer val$producer;

            C12701(Producer producer) {
                this.val$producer = producer;
                this.once = new AtomicBoolean();
                this.excessive = new AtomicBoolean();
            }

            public void request(long n) {
                if (this.once.compareAndSet(false, true)) {
                    if (C12712.this.initialValue == OperatorScan.NO_INITIAL_VALUE || n == Long.MAX_VALUE) {
                        this.val$producer.request(n);
                    } else if (n == 1) {
                        this.excessive.set(true);
                        this.val$producer.request(1);
                    } else {
                        this.val$producer.request(n - 1);
                    }
                } else if (n <= 1 || !this.excessive.compareAndSet(true, false) || n == Long.MAX_VALUE) {
                    this.val$producer.request(n);
                } else {
                    this.val$producer.request(n - 1);
                }
            }
        }

        C12712(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
            this.initialValue = OperatorScan.this.initialValueFactory.call();
            this.value = this.initialValue;
            this.initialized = false;
        }

        public void onNext(T currentValue) {
            emitInitialValueIfNeeded(this.val$child);
            if (this.value == OperatorScan.NO_INITIAL_VALUE) {
                this.value = currentValue;
            } else {
                try {
                    this.value = OperatorScan.this.accumulator.call(this.value, currentValue);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.val$child.onError(OnErrorThrowable.addValueAsLastCause(e, currentValue));
                    return;
                }
            }
            this.val$child.onNext(this.value);
        }

        public void onError(Throwable e) {
            this.val$child.onError(e);
        }

        public void onCompleted() {
            emitInitialValueIfNeeded(this.val$child);
            this.val$child.onCompleted();
        }

        private void emitInitialValueIfNeeded(Subscriber<? super R> child) {
            if (!this.initialized) {
                this.initialized = true;
                if (this.initialValue != OperatorScan.NO_INITIAL_VALUE) {
                    child.onNext(this.initialValue);
                }
            }
        }

        public void setProducer(Producer producer) {
            this.val$child.setProducer(new C12701(producer));
        }
    }

    static {
        NO_INITIAL_VALUE = new Object();
    }

    public OperatorScan(R initialValue, Func2<R, ? super T, R> accumulator) {
        this(new C12691(initialValue), (Func2) accumulator);
    }

    public OperatorScan(Func0<R> initialValueFactory, Func2<R, ? super T, R> accumulator) {
        this.initialValueFactory = initialValueFactory;
        this.accumulator = accumulator;
    }

    public OperatorScan(Func2<R, ? super T, R> accumulator) {
        this(NO_INITIAL_VALUE, (Func2) accumulator);
    }

    public Subscriber<? super T> call(Subscriber<? super R> child) {
        return new C12712(child, child);
    }
}
