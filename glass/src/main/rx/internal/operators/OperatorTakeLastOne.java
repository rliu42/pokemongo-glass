package rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;

public class OperatorTakeLastOne<T> implements Operator<T, T> {

    /* renamed from: rx.internal.operators.OperatorTakeLastOne.1 */
    class C12951 implements Producer {
        final /* synthetic */ ParentSubscriber val$parent;

        C12951(ParentSubscriber parentSubscriber) {
            this.val$parent = parentSubscriber;
        }

        public void request(long n) {
            this.val$parent.requestMore(n);
        }
    }

    private static class Holder {
        static final OperatorTakeLastOne<Object> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorTakeLastOne();
        }
    }

    private static class ParentSubscriber<T> extends Subscriber<T> {
        private static final Object ABSENT;
        private static final int NOT_REQUESTED_COMPLETED = 1;
        private static final int NOT_REQUESTED_NOT_COMPLETED = 0;
        private static final int REQUESTED_COMPLETED = 3;
        private static final int REQUESTED_NOT_COMPLETED = 2;
        private final Subscriber<? super T> child;
        private T last;
        private final AtomicInteger state;

        static {
            ABSENT = new Object();
        }

        ParentSubscriber(Subscriber<? super T> child) {
            this.last = ABSENT;
            this.state = new AtomicInteger(NOT_REQUESTED_NOT_COMPLETED);
            this.child = child;
        }

        void requestMore(long n) {
            if (n > 0) {
                while (true) {
                    int s = this.state.get();
                    if (s == 0) {
                        if (this.state.compareAndSet(NOT_REQUESTED_NOT_COMPLETED, REQUESTED_NOT_COMPLETED)) {
                            return;
                        }
                    } else if (s != NOT_REQUESTED_COMPLETED) {
                        return;
                    } else {
                        if (this.state.compareAndSet(NOT_REQUESTED_COMPLETED, REQUESTED_COMPLETED)) {
                            emit();
                            return;
                        }
                    }
                }
            }
        }

        public void onCompleted() {
            if (this.last == ABSENT) {
                this.child.onCompleted();
                return;
            }
            while (true) {
                int s = this.state.get();
                if (s == 0) {
                    if (this.state.compareAndSet(NOT_REQUESTED_NOT_COMPLETED, NOT_REQUESTED_COMPLETED)) {
                        return;
                    }
                } else if (s != REQUESTED_NOT_COMPLETED) {
                    return;
                } else {
                    if (this.state.compareAndSet(REQUESTED_NOT_COMPLETED, REQUESTED_COMPLETED)) {
                        emit();
                        return;
                    }
                }
            }
        }

        private void emit() {
            if (isUnsubscribed()) {
                this.last = null;
                return;
            }
            T t = this.last;
            this.last = null;
            if (t != ABSENT) {
                try {
                    this.child.onNext(t);
                } catch (Throwable e) {
                    this.child.onError(e);
                    return;
                }
            }
            if (!isUnsubscribed()) {
                this.child.onCompleted();
            }
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }

        public void onNext(T t) {
            this.last = t;
        }
    }

    public static <T> OperatorTakeLastOne<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorTakeLastOne() {
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        ParentSubscriber<T> parent = new ParentSubscriber(child);
        child.setProducer(new C12951(parent));
        child.add(parent);
        return parent;
    }
}
