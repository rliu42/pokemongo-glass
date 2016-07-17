package rx.internal.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import rx.Observable;
import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.observers.SerializedObserver;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.CompositeSubscription;

public final class OperatorWindowWithStartEndObservable<T, U, V> implements Operator<Observable<T>, T> {
    final Func1<? super U, ? extends Observable<? extends V>> windowClosingSelector;
    final Observable<? extends U> windowOpenings;

    /* renamed from: rx.internal.operators.OperatorWindowWithStartEndObservable.1 */
    class C13261 extends Subscriber<U> {
        final /* synthetic */ SourceSubscriber val$sub;

        C13261(SourceSubscriber sourceSubscriber) {
            this.val$sub = sourceSubscriber;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(U t) {
            this.val$sub.beginWindow(t);
        }

        public void onError(Throwable e) {
            this.val$sub.onError(e);
        }

        public void onCompleted() {
            this.val$sub.onCompleted();
        }
    }

    static final class SerializedSubject<T> {
        final Observer<T> consumer;
        final Observable<T> producer;

        public SerializedSubject(Observer<T> consumer, Observable<T> producer) {
            this.consumer = new SerializedObserver(consumer);
            this.producer = producer;
        }
    }

    final class SourceSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final List<SerializedSubject<T>> chunks;
        final CompositeSubscription csub;
        boolean done;
        final Object guard;

        /* renamed from: rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.1 */
        class C13271 extends Subscriber<V> {
            boolean once;
            final /* synthetic */ SerializedSubject val$s;

            C13271(SerializedSubject serializedSubject) {
                this.val$s = serializedSubject;
                this.once = true;
            }

            public void onNext(V v) {
                onCompleted();
            }

            public void onError(Throwable e) {
            }

            public void onCompleted() {
                if (this.once) {
                    this.once = false;
                    SourceSubscriber.this.endWindow(this.val$s);
                    SourceSubscriber.this.csub.remove(this);
                }
            }
        }

        public SourceSubscriber(Subscriber<? super Observable<T>> child, CompositeSubscription csub) {
            this.child = new SerializedSubscriber(child);
            this.guard = new Object();
            this.chunks = new LinkedList();
            this.csub = csub;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(T t) {
            synchronized (this.guard) {
                if (this.done) {
                    return;
                }
                List<SerializedSubject<T>> list = new ArrayList(this.chunks);
                for (SerializedSubject<T> cs : list) {
                    cs.consumer.onNext(t);
                }
            }
        }

        public void onError(Throwable e) {
            try {
                synchronized (this.guard) {
                    if (this.done) {
                        this.csub.unsubscribe();
                        return;
                    }
                    this.done = true;
                    List<SerializedSubject<T>> list = new ArrayList(this.chunks);
                    this.chunks.clear();
                    for (SerializedSubject<T> cs : list) {
                        cs.consumer.onError(e);
                    }
                    this.child.onError(e);
                    this.csub.unsubscribe();
                }
            } catch (Throwable th) {
                this.csub.unsubscribe();
            }
        }

        public void onCompleted() {
            try {
                synchronized (this.guard) {
                    if (this.done) {
                        this.csub.unsubscribe();
                        return;
                    }
                    this.done = true;
                    List<SerializedSubject<T>> list = new ArrayList(this.chunks);
                    this.chunks.clear();
                    for (SerializedSubject<T> cs : list) {
                        cs.consumer.onCompleted();
                    }
                    this.child.onCompleted();
                    this.csub.unsubscribe();
                }
            } catch (Throwable th) {
                this.csub.unsubscribe();
            }
        }

        void beginWindow(U token) {
            SerializedSubject<T> s = createSerializedSubject();
            synchronized (this.guard) {
                if (this.done) {
                    return;
                }
                this.chunks.add(s);
                this.child.onNext(s.producer);
                try {
                    Observable<? extends V> end = (Observable) OperatorWindowWithStartEndObservable.this.windowClosingSelector.call(token);
                    Subscriber<V> v = new C13271(s);
                    this.csub.add(v);
                    end.unsafeSubscribe(v);
                } catch (Throwable e) {
                    onError(e);
                }
            }
        }

        void endWindow(SerializedSubject<T> window) {
            boolean terminate = false;
            synchronized (this.guard) {
                if (this.done) {
                    return;
                }
                Iterator<SerializedSubject<T>> it = this.chunks.iterator();
                while (it.hasNext()) {
                    if (((SerializedSubject) it.next()) == window) {
                        terminate = true;
                        it.remove();
                        break;
                    }
                }
                if (terminate) {
                    window.consumer.onCompleted();
                }
            }
        }

        SerializedSubject<T> createSerializedSubject() {
            BufferUntilSubscriber<T> bus = BufferUntilSubscriber.create();
            return new SerializedSubject(bus, bus);
        }
    }

    public OperatorWindowWithStartEndObservable(Observable<? extends U> windowOpenings, Func1<? super U, ? extends Observable<? extends V>> windowClosingSelector) {
        this.windowOpenings = windowOpenings;
        this.windowClosingSelector = windowClosingSelector;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        CompositeSubscription csub = new CompositeSubscription();
        child.add(csub);
        SourceSubscriber sub = new SourceSubscriber(child, csub);
        Subscriber<U> open = new C13261(sub);
        csub.add(sub);
        csub.add(open);
        this.windowOpenings.unsafeSubscribe(open);
        return sub;
    }
}
