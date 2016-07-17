package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Observable.Operator;
import rx.Observer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.SerializedObserver;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.Subscriptions;

public final class OperatorWindowWithTime<T> implements Operator<Observable<T>, T> {
    static final Object NEXT_SUBJECT;
    static final NotificationLite<Object> nl;
    final Scheduler scheduler;
    final int size;
    final long timeshift;
    final long timespan;
    final TimeUnit unit;

    static final class CountedSerializedSubject<T> {
        final Observer<T> consumer;
        int count;
        final Observable<T> producer;

        public CountedSerializedSubject(Observer<T> consumer, Observable<T> producer) {
            this.consumer = new SerializedObserver(consumer);
            this.producer = producer;
        }
    }

    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        boolean emitting;
        final Object guard;
        List<Object> queue;
        volatile State<T> state;
        final Worker worker;

        /* renamed from: rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.1 */
        class C13281 implements Action0 {
            final /* synthetic */ OperatorWindowWithTime val$this$0;

            C13281(OperatorWindowWithTime operatorWindowWithTime) {
                this.val$this$0 = operatorWindowWithTime;
            }

            public void call() {
                if (ExactSubscriber.this.state.consumer == null) {
                    ExactSubscriber.this.unsubscribe();
                }
            }
        }

        /* renamed from: rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.2 */
        class C13292 implements Action0 {
            C13292() {
            }

            public void call() {
                ExactSubscriber.this.nextWindow();
            }
        }

        public ExactSubscriber(Subscriber<? super Observable<T>> child, Worker worker) {
            this.child = new SerializedSubscriber(child);
            this.worker = worker;
            this.guard = new Object();
            this.state = State.empty();
            child.add(Subscriptions.create(new C13281(OperatorWindowWithTime.this)));
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(T t) {
            synchronized (this.guard) {
                if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(t);
                    return;
                }
                this.emitting = true;
                if (emitValue(t)) {
                    List<Object> localQueue;
                    do {
                        synchronized (this.guard) {
                            localQueue = this.queue;
                            if (localQueue == null) {
                                this.emitting = false;
                                if (!true) {
                                    synchronized (this.guard) {
                                        this.emitting = false;
                                    }
                                    return;
                                }
                                return;
                            }
                            this.queue = null;
                            try {
                            } catch (Throwable th) {
                                if (null == null) {
                                    synchronized (this.guard) {
                                    }
                                    this.emitting = false;
                                }
                            }
                        }
                    } while (drain(localQueue));
                    if (null == null) {
                        synchronized (this.guard) {
                            this.emitting = false;
                        }
                    }
                } else if (null == null) {
                    synchronized (this.guard) {
                        this.emitting = false;
                    }
                }
            }
        }

        boolean drain(List<Object> queue) {
            if (queue == null) {
                return true;
            }
            for (T o : queue) {
                if (o == OperatorWindowWithTime.NEXT_SUBJECT) {
                    if (!replaceSubject()) {
                        return false;
                    }
                } else if (OperatorWindowWithTime.nl.isError(o)) {
                    error(OperatorWindowWithTime.nl.getError(o));
                    return true;
                } else if (OperatorWindowWithTime.nl.isCompleted(o)) {
                    complete();
                    return true;
                } else if (!emitValue(o)) {
                    return false;
                }
            }
            return true;
        }

        boolean replaceSubject() {
            Observer<T> s = this.state.consumer;
            if (s != null) {
                s.onCompleted();
            }
            if (this.child.isUnsubscribed()) {
                this.state = this.state.clear();
                unsubscribe();
                return false;
            }
            BufferUntilSubscriber<T> bus = BufferUntilSubscriber.create();
            this.state = this.state.create(bus, bus);
            this.child.onNext(bus);
            return true;
        }

        boolean emitValue(T t) {
            State<T> s = this.state;
            if (s.consumer == null) {
                if (!replaceSubject()) {
                    return false;
                }
                s = this.state;
            }
            s.consumer.onNext(t);
            if (s.count == OperatorWindowWithTime.this.size - 1) {
                s.consumer.onCompleted();
                s = s.clear();
            } else {
                s = s.next();
            }
            this.state = s;
            return true;
        }

        public void onError(Throwable e) {
            synchronized (this.guard) {
                if (this.emitting) {
                    this.queue = Collections.singletonList(OperatorWindowWithTime.nl.error(e));
                    return;
                }
                this.queue = null;
                this.emitting = true;
                error(e);
            }
        }

        void error(Throwable e) {
            Observer<T> s = this.state.consumer;
            this.state = this.state.clear();
            if (s != null) {
                s.onError(e);
            }
            this.child.onError(e);
            unsubscribe();
        }

        void complete() {
            Observer<T> s = this.state.consumer;
            this.state = this.state.clear();
            if (s != null) {
                s.onCompleted();
            }
            this.child.onCompleted();
            unsubscribe();
        }

        public void onCompleted() {
            synchronized (this.guard) {
                if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(OperatorWindowWithTime.nl.completed());
                    return;
                }
                List<Object> localQueue = this.queue;
                this.queue = null;
                this.emitting = true;
                try {
                    drain(localQueue);
                    complete();
                } catch (Throwable e) {
                    error(e);
                }
            }
        }

        void scheduleExact() {
            this.worker.schedulePeriodically(new C13292(), 0, OperatorWindowWithTime.this.timespan, OperatorWindowWithTime.this.unit);
        }

        void nextWindow() {
            synchronized (this.guard) {
                if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(OperatorWindowWithTime.NEXT_SUBJECT);
                    return;
                }
                this.emitting = true;
                if (replaceSubject()) {
                    List<Object> localQueue;
                    do {
                        synchronized (this.guard) {
                            localQueue = this.queue;
                            if (localQueue == null) {
                                this.emitting = false;
                                if (!true) {
                                    synchronized (this.guard) {
                                        this.emitting = false;
                                    }
                                    return;
                                }
                                return;
                            }
                            this.queue = null;
                            try {
                            } catch (Throwable th) {
                                if (null == null) {
                                    synchronized (this.guard) {
                                    }
                                    this.emitting = false;
                                }
                            }
                        }
                    } while (drain(localQueue));
                    if (null == null) {
                        synchronized (this.guard) {
                            this.emitting = false;
                        }
                    }
                } else if (null == null) {
                    synchronized (this.guard) {
                        this.emitting = false;
                    }
                }
            }
        }
    }

    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final List<CountedSerializedSubject<T>> chunks;
        boolean done;
        final Object guard;
        final Worker worker;

        /* renamed from: rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.1 */
        class C13301 implements Action0 {
            C13301() {
            }

            public void call() {
                InexactSubscriber.this.startNewChunk();
            }
        }

        /* renamed from: rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.2 */
        class C13312 implements Action0 {
            final /* synthetic */ CountedSerializedSubject val$chunk;

            C13312(CountedSerializedSubject countedSerializedSubject) {
                this.val$chunk = countedSerializedSubject;
            }

            public void call() {
                InexactSubscriber.this.terminateChunk(this.val$chunk);
            }
        }

        public InexactSubscriber(Subscriber<? super Observable<T>> child, Worker worker) {
            super(child);
            this.child = child;
            this.worker = worker;
            this.guard = new Object();
            this.chunks = new LinkedList();
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(T t) {
            synchronized (this.guard) {
                if (this.done) {
                    return;
                }
                List<CountedSerializedSubject<T>> list = new ArrayList(this.chunks);
                Iterator<CountedSerializedSubject<T>> it = this.chunks.iterator();
                while (it.hasNext()) {
                    CountedSerializedSubject<T> cs = (CountedSerializedSubject) it.next();
                    int i = cs.count + 1;
                    cs.count = i;
                    if (i == OperatorWindowWithTime.this.size) {
                        it.remove();
                    }
                }
                for (CountedSerializedSubject<T> cs2 : list) {
                    cs2.consumer.onNext(t);
                    if (cs2.count == OperatorWindowWithTime.this.size) {
                        cs2.consumer.onCompleted();
                    }
                }
            }
        }

        public void onError(Throwable e) {
            synchronized (this.guard) {
                if (this.done) {
                    return;
                }
                this.done = true;
                List<CountedSerializedSubject<T>> list = new ArrayList(this.chunks);
                this.chunks.clear();
                for (CountedSerializedSubject<T> cs : list) {
                    cs.consumer.onError(e);
                }
                this.child.onError(e);
            }
        }

        public void onCompleted() {
            synchronized (this.guard) {
                if (this.done) {
                    return;
                }
                this.done = true;
                List<CountedSerializedSubject<T>> list = new ArrayList(this.chunks);
                this.chunks.clear();
                for (CountedSerializedSubject<T> cs : list) {
                    cs.consumer.onCompleted();
                }
                this.child.onCompleted();
            }
        }

        void scheduleChunk() {
            this.worker.schedulePeriodically(new C13301(), OperatorWindowWithTime.this.timeshift, OperatorWindowWithTime.this.timeshift, OperatorWindowWithTime.this.unit);
        }

        void startNewChunk() {
            CountedSerializedSubject<T> chunk = createCountedSerializedSubject();
            synchronized (this.guard) {
                if (this.done) {
                    return;
                }
                this.chunks.add(chunk);
                try {
                    this.child.onNext(chunk.producer);
                    this.worker.schedule(new C13312(chunk), OperatorWindowWithTime.this.timespan, OperatorWindowWithTime.this.unit);
                } catch (Throwable e) {
                    onError(e);
                }
            }
        }

        void terminateChunk(CountedSerializedSubject<T> chunk) {
            boolean terminate = false;
            synchronized (this.guard) {
                if (this.done) {
                    return;
                }
                Iterator<CountedSerializedSubject<T>> it = this.chunks.iterator();
                while (it.hasNext()) {
                    if (((CountedSerializedSubject) it.next()) == chunk) {
                        terminate = true;
                        it.remove();
                        break;
                    }
                }
                if (terminate) {
                    chunk.consumer.onCompleted();
                }
            }
        }

        CountedSerializedSubject<T> createCountedSerializedSubject() {
            BufferUntilSubscriber<T> bus = BufferUntilSubscriber.create();
            return new CountedSerializedSubject(bus, bus);
        }
    }

    static final class State<T> {
        static final State<Object> EMPTY;
        final Observer<T> consumer;
        final int count;
        final Observable<T> producer;

        static {
            EMPTY = new State(null, null, 0);
        }

        public State(Observer<T> consumer, Observable<T> producer, int count) {
            this.consumer = consumer;
            this.producer = producer;
            this.count = count;
        }

        public State<T> next() {
            return new State(this.consumer, this.producer, this.count + 1);
        }

        public State<T> create(Observer<T> consumer, Observable<T> producer) {
            return new State(consumer, producer, 0);
        }

        public State<T> clear() {
            return empty();
        }

        public static <T> State<T> empty() {
            return EMPTY;
        }
    }

    public OperatorWindowWithTime(long timespan, long timeshift, TimeUnit unit, int size, Scheduler scheduler) {
        this.timespan = timespan;
        this.timeshift = timeshift;
        this.unit = unit;
        this.size = size;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        Worker worker = this.scheduler.createWorker();
        if (this.timespan == this.timeshift) {
            ExactSubscriber s = new ExactSubscriber(child, worker);
            s.add(worker);
            s.scheduleExact();
            return s;
        }
        Subscriber s2 = new InexactSubscriber(child, worker);
        s2.add(worker);
        s2.startNewChunk();
        s2.scheduleChunk();
        return s2;
    }

    static {
        NEXT_SUBJECT = new Object();
        nl = NotificationLite.instance();
    }
}
