package rx.internal.operators;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;

public final class OperatorSingle<T> implements Operator<T, T> {
    private final T defaultValue;
    private final boolean hasDefaultValue;

    /* renamed from: rx.internal.operators.OperatorSingle.1 */
    class C12751 implements Producer {
        private final AtomicBoolean requestedTwo;
        final /* synthetic */ ParentSubscriber val$parent;

        C12751(ParentSubscriber parentSubscriber) {
            this.val$parent = parentSubscriber;
            this.requestedTwo = new AtomicBoolean(false);
        }

        public void request(long n) {
            if (n > 0 && this.requestedTwo.compareAndSet(false, true)) {
                this.val$parent.requestMore(2);
            }
        }
    }

    private static class Holder {
        static final OperatorSingle<?> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorSingle();
        }
    }

    private static final class ParentSubscriber<T> extends Subscriber<T> {
        private final Subscriber<? super T> child;
        private final T defaultValue;
        private final boolean hasDefaultValue;
        private boolean hasTooManyElements;
        private boolean isNonEmpty;
        private T value;

        ParentSubscriber(Subscriber<? super T> child, boolean hasDefaultValue, T defaultValue) {
            this.isNonEmpty = false;
            this.hasTooManyElements = false;
            this.child = child;
            this.hasDefaultValue = hasDefaultValue;
            this.defaultValue = defaultValue;
        }

        void requestMore(long n) {
            request(n);
        }

        public void onNext(T value) {
            if (this.isNonEmpty) {
                this.hasTooManyElements = true;
                this.child.onError(new IllegalArgumentException("Sequence contains too many elements"));
                unsubscribe();
                return;
            }
            this.value = value;
            this.isNonEmpty = true;
        }

        public void onCompleted() {
            if (!this.hasTooManyElements) {
                if (this.isNonEmpty) {
                    this.child.onNext(this.value);
                    this.child.onCompleted();
                } else if (this.hasDefaultValue) {
                    this.child.onNext(this.defaultValue);
                    this.child.onCompleted();
                } else {
                    this.child.onError(new NoSuchElementException("Sequence contains no elements"));
                }
            }
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }
    }

    public static <T> OperatorSingle<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorSingle() {
        this(false, null);
    }

    public OperatorSingle(T defaultValue) {
        this(true, defaultValue);
    }

    private OperatorSingle(boolean hasDefaultValue, T defaultValue) {
        this.hasDefaultValue = hasDefaultValue;
        this.defaultValue = defaultValue;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        ParentSubscriber<T> parent = new ParentSubscriber(child, this.hasDefaultValue, this.defaultValue);
        child.setProducer(new C12751(parent));
        child.add(parent);
        return parent;
    }
}
