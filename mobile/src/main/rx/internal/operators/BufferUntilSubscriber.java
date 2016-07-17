package rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.functions.Action0;
import rx.subjects.Subject;

public final class BufferUntilSubscriber<T> extends Subject<T, T> {
    private static final Observer EMPTY_OBSERVER;
    private boolean forward;
    final State<T> state;

    /* renamed from: rx.internal.operators.BufferUntilSubscriber.1 */
    static class C11591 implements Observer {
        C11591() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Object t) {
        }
    }

    static final class OnSubscribeAction<T> implements OnSubscribe<T> {
        final State<T> state;

        /* renamed from: rx.internal.operators.BufferUntilSubscriber.OnSubscribeAction.1 */
        class C11601 implements Action0 {
            C11601() {
            }

            public void call() {
                OnSubscribeAction.this.state.observerRef = BufferUntilSubscriber.EMPTY_OBSERVER;
            }
        }

        public OnSubscribeAction(State<T> state) {
            this.state = state;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void call(rx.Subscriber<? super T> r7) {
            /*
            r6 = this;
            r3 = r6.state;
            r4 = 0;
            r3 = r3.casObserverRef(r4, r7);
            if (r3 == 0) goto L_0x005e;
        L_0x0009:
            r3 = new rx.internal.operators.BufferUntilSubscriber$OnSubscribeAction$1;
            r3.<init>();
            r3 = rx.subscriptions.Subscriptions.create(r3);
            r7.add(r3);
            r2 = 0;
            r3 = r6.state;
            r4 = r3.guard;
            monitor-enter(r4);
            r3 = r6.state;	 Catch:{ all -> 0x0040 }
            r3 = r3.emitting;	 Catch:{ all -> 0x0040 }
            if (r3 != 0) goto L_0x0027;
        L_0x0021:
            r3 = r6.state;	 Catch:{ all -> 0x0040 }
            r5 = 1;
            r3.emitting = r5;	 Catch:{ all -> 0x0040 }
            r2 = 1;
        L_0x0027:
            monitor-exit(r4);	 Catch:{ all -> 0x0040 }
            if (r2 == 0) goto L_0x0058;
        L_0x002a:
            r0 = rx.internal.operators.NotificationLite.instance();
        L_0x002e:
            r3 = r6.state;
            r3 = r3.buffer;
            r1 = r3.poll();
            if (r1 == 0) goto L_0x0043;
        L_0x0038:
            r3 = r6.state;
            r3 = r3.observerRef;
            r0.accept(r3, r1);
            goto L_0x002e;
        L_0x0040:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0040 }
            throw r3;
        L_0x0043:
            r3 = r6.state;
            r4 = r3.guard;
            monitor-enter(r4);
            r3 = r6.state;	 Catch:{ all -> 0x005b }
            r3 = r3.buffer;	 Catch:{ all -> 0x005b }
            r3 = r3.isEmpty();	 Catch:{ all -> 0x005b }
            if (r3 == 0) goto L_0x0059;
        L_0x0052:
            r3 = r6.state;	 Catch:{ all -> 0x005b }
            r5 = 0;
            r3.emitting = r5;	 Catch:{ all -> 0x005b }
            monitor-exit(r4);	 Catch:{ all -> 0x005b }
        L_0x0058:
            return;
        L_0x0059:
            monitor-exit(r4);	 Catch:{ all -> 0x005b }
            goto L_0x002e;
        L_0x005b:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x005b }
            throw r3;
        L_0x005e:
            r3 = new java.lang.IllegalStateException;
            r4 = "Only one subscriber allowed!";
            r3.<init>(r4);
            r7.onError(r3);
            goto L_0x0058;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.BufferUntilSubscriber.OnSubscribeAction.call(rx.Subscriber):void");
        }
    }

    static final class State<T> {
        static final AtomicReferenceFieldUpdater<State, Observer> OBSERVER_UPDATER;
        final ConcurrentLinkedQueue<Object> buffer;
        boolean emitting;
        Object guard;
        final NotificationLite<T> nl;
        volatile Observer<? super T> observerRef;

        State() {
            this.observerRef = null;
            this.guard = new Object();
            this.emitting = false;
            this.buffer = new ConcurrentLinkedQueue();
            this.nl = NotificationLite.instance();
        }

        static {
            OBSERVER_UPDATER = AtomicReferenceFieldUpdater.newUpdater(State.class, Observer.class, "observerRef");
        }

        boolean casObserverRef(Observer<? super T> expected, Observer<? super T> next) {
            return OBSERVER_UPDATER.compareAndSet(this, expected, next);
        }
    }

    public static <T> BufferUntilSubscriber<T> create() {
        return new BufferUntilSubscriber(new State());
    }

    private BufferUntilSubscriber(State<T> state) {
        super(new OnSubscribeAction(state));
        this.forward = false;
        this.state = state;
    }

    private void emit(Object v) {
        synchronized (this.state.guard) {
            this.state.buffer.add(v);
            if (!(this.state.observerRef == null || this.state.emitting)) {
                this.forward = true;
                this.state.emitting = true;
            }
        }
        if (this.forward) {
            while (true) {
                Object o = this.state.buffer.poll();
                if (o != null) {
                    this.state.nl.accept(this.state.observerRef, o);
                } else {
                    return;
                }
            }
        }
    }

    public void onCompleted() {
        if (this.forward) {
            this.state.observerRef.onCompleted();
        } else {
            emit(this.state.nl.completed());
        }
    }

    public void onError(Throwable e) {
        if (this.forward) {
            this.state.observerRef.onError(e);
        } else {
            emit(this.state.nl.error(e));
        }
    }

    public void onNext(T t) {
        if (this.forward) {
            this.state.observerRef.onNext(t);
        } else {
            emit(this.state.nl.next(t));
        }
    }

    public boolean hasObservers() {
        boolean z;
        synchronized (this.state.guard) {
            z = this.state.observerRef != null;
        }
        return z;
    }

    static {
        EMPTY_OBSERVER = new C11591();
    }
}
