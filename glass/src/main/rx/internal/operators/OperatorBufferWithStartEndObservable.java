package rx.internal.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import rx.Observable;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.functions.Func1;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.CompositeSubscription;

public final class OperatorBufferWithStartEndObservable<T, TOpening, TClosing> implements Operator<List<T>, T> {
    final Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosing;
    final Observable<? extends TOpening> bufferOpening;

    /* renamed from: rx.internal.operators.OperatorBufferWithStartEndObservable.1 */
    class C11981 extends Subscriber<TOpening> {
        final /* synthetic */ BufferingSubscriber val$bsub;

        C11981(BufferingSubscriber bufferingSubscriber) {
            this.val$bsub = bufferingSubscriber;
        }

        public void onNext(TOpening t) {
            this.val$bsub.startBuffer(t);
        }

        public void onError(Throwable e) {
            this.val$bsub.onError(e);
        }

        public void onCompleted() {
            this.val$bsub.onCompleted();
        }
    }

    final class BufferingSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        final List<List<T>> chunks;
        final CompositeSubscription closingSubscriptions;
        boolean done;

        /* renamed from: rx.internal.operators.OperatorBufferWithStartEndObservable.BufferingSubscriber.1 */
        class C11991 extends Subscriber<TClosing> {
            final /* synthetic */ List val$chunk;

            C11991(List list) {
                this.val$chunk = list;
            }

            public void onNext(TClosing tClosing) {
                BufferingSubscriber.this.closingSubscriptions.remove(this);
                BufferingSubscriber.this.endBuffer(this.val$chunk);
            }

            public void onError(Throwable e) {
                BufferingSubscriber.this.onError(e);
            }

            public void onCompleted() {
                BufferingSubscriber.this.closingSubscriptions.remove(this);
                BufferingSubscriber.this.endBuffer(this.val$chunk);
            }
        }

        public BufferingSubscriber(Subscriber<? super List<T>> child) {
            this.child = child;
            this.chunks = new LinkedList();
            this.closingSubscriptions = new CompositeSubscription();
            add(this.closingSubscriptions);
        }

        public void onNext(T t) {
            synchronized (this) {
                for (List<T> chunk : this.chunks) {
                    chunk.add(t);
                }
            }
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.done = true;
                this.chunks.clear();
                this.child.onError(e);
                unsubscribe();
            }
        }

        public void onCompleted() {
            try {
                synchronized (this) {
                    if (this.done) {
                        return;
                    }
                    this.done = true;
                    List<List<T>> toEmit = new LinkedList(this.chunks);
                    this.chunks.clear();
                    for (List<T> chunk : toEmit) {
                        this.child.onNext(chunk);
                    }
                    this.child.onCompleted();
                    unsubscribe();
                }
            } catch (Throwable t) {
                this.child.onError(t);
            }
        }

        void startBuffer(TOpening v) {
            List<T> chunk = new ArrayList();
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.chunks.add(chunk);
                try {
                    Observable<? extends TClosing> cobs = (Observable) OperatorBufferWithStartEndObservable.this.bufferClosing.call(v);
                    Subscriber<TClosing> closeSubscriber = new C11991(chunk);
                    this.closingSubscriptions.add(closeSubscriber);
                    cobs.unsafeSubscribe(closeSubscriber);
                } catch (Throwable t) {
                    onError(t);
                }
            }
        }

        void endBuffer(List<T> toEnd) {
            boolean canEnd = false;
            synchronized (this) {
                if (this.done) {
                    return;
                }
                Iterator<List<T>> it = this.chunks.iterator();
                while (it.hasNext()) {
                    if (((List) it.next()) == toEnd) {
                        canEnd = true;
                        it.remove();
                        break;
                    }
                }
                if (canEnd) {
                    this.child.onNext(toEnd);
                }
            }
        }
    }

    public OperatorBufferWithStartEndObservable(Observable<? extends TOpening> bufferOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosingSelector) {
        this.bufferOpening = bufferOpenings;
        this.bufferClosing = bufferClosingSelector;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        BufferingSubscriber bsub = new BufferingSubscriber(new SerializedSubscriber(child));
        Subscriber<TOpening> openSubscriber = new C11981(bsub);
        child.add(openSubscriber);
        child.add(bsub);
        this.bufferOpening.unsafeSubscribe(openSubscriber);
        return bsub;
    }
}
