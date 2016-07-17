package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;
import rx.internal.producers.SingleDelayedProducer;

public final class OperatorAll<T> implements Operator<Boolean, T> {
    private final Func1<? super T, Boolean> predicate;

    /* renamed from: rx.internal.operators.OperatorAll.1 */
    class C11891 extends Subscriber<T> {
        boolean done;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ SingleDelayedProducer val$producer;

        C11891(SingleDelayedProducer singleDelayedProducer, Subscriber subscriber) {
            this.val$producer = singleDelayedProducer;
            this.val$child = subscriber;
        }

        public void onNext(T t) {
            try {
                if (!((Boolean) OperatorAll.this.predicate.call(t)).booleanValue() && !this.done) {
                    this.done = true;
                    this.val$producer.setValue(Boolean.valueOf(false));
                    unsubscribe();
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                onError(OnErrorThrowable.addValueAsLastCause(e, t));
            }
        }

        public void onError(Throwable e) {
            this.val$child.onError(e);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.val$producer.setValue(Boolean.valueOf(true));
            }
        }
    }

    public OperatorAll(Func1<? super T, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super T> call(Subscriber<? super Boolean> child) {
        SingleDelayedProducer<Boolean> producer = new SingleDelayedProducer(child);
        Subscriber<T> s = new C11891(producer, child);
        child.add(s);
        child.setProducer(producer);
        return s;
    }
}
