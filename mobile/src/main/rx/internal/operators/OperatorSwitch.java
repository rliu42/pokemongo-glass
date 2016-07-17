package rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

public final class OperatorSwitch<T> implements Operator<T, Observable<? extends T>> {

    private static final class Holder {
        static final OperatorSwitch<Object> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorSwitch();
        }
    }

    private static final class SwitchSubscriber<T> extends Subscriber<Observable<? extends T>> {
        boolean active;
        InnerSubscriber currentSubscriber;
        boolean emitting;
        final Object guard;
        int index;
        volatile boolean infinite;
        long initialRequested;
        boolean mainDone;
        final NotificationLite<?> nl;
        List<Object> queue;
        final SerializedSubscriber<T> f918s;
        final SerialSubscription ssub;

        /* renamed from: rx.internal.operators.OperatorSwitch.SwitchSubscriber.1 */
        class C12911 implements Producer {
            C12911() {
            }

            public void request(long n) {
                if (!SwitchSubscriber.this.infinite) {
                    InnerSubscriber localSubscriber;
                    if (n == Long.MAX_VALUE) {
                        SwitchSubscriber.this.infinite = true;
                    }
                    synchronized (SwitchSubscriber.this.guard) {
                        localSubscriber = SwitchSubscriber.this.currentSubscriber;
                        long r;
                        if (SwitchSubscriber.this.currentSubscriber == null) {
                            r = SwitchSubscriber.this.initialRequested + n;
                            if (r < 0) {
                                SwitchSubscriber.this.infinite = true;
                            } else {
                                SwitchSubscriber.this.initialRequested = r;
                            }
                        } else {
                            r = SwitchSubscriber.this.currentSubscriber.requested + n;
                            if (r < 0) {
                                SwitchSubscriber.this.infinite = true;
                            } else {
                                SwitchSubscriber.this.currentSubscriber.requested = r;
                            }
                        }
                    }
                    if (localSubscriber == null) {
                        return;
                    }
                    if (SwitchSubscriber.this.infinite) {
                        localSubscriber.requestMore(Long.MAX_VALUE);
                    } else {
                        localSubscriber.requestMore(n);
                    }
                }
            }
        }

        final class InnerSubscriber extends Subscriber<T> {
            private final int id;
            private final long initialRequested;
            private long requested;

            public InnerSubscriber(int id, long initialRequested) {
                this.requested = 0;
                this.id = id;
                this.initialRequested = initialRequested;
            }

            public void onStart() {
                requestMore(this.initialRequested);
            }

            public void requestMore(long n) {
                request(n);
            }

            public void onNext(T t) {
                SwitchSubscriber.this.emit(t, this.id, this);
            }

            public void onError(Throwable e) {
                SwitchSubscriber.this.error(e, this.id);
            }

            public void onCompleted() {
                SwitchSubscriber.this.complete(this.id);
            }
        }

        public SwitchSubscriber(Subscriber<? super T> child) {
            this.guard = new Object();
            this.nl = NotificationLite.instance();
            this.infinite = false;
            this.f918s = new SerializedSubscriber(child);
            this.ssub = new SerialSubscription();
            child.add(this.ssub);
            child.setProducer(new C12911());
        }

        public void onNext(Observable<? extends T> t) {
            synchronized (this.guard) {
                long remainingRequest;
                int id = this.index + 1;
                this.index = id;
                this.active = true;
                if (this.infinite) {
                    remainingRequest = Long.MAX_VALUE;
                } else {
                    remainingRequest = this.currentSubscriber == null ? this.initialRequested : this.currentSubscriber.requested;
                }
                this.currentSubscriber = new InnerSubscriber(id, remainingRequest);
                this.currentSubscriber.requested = remainingRequest;
            }
            this.ssub.set(this.currentSubscriber);
            t.unsafeSubscribe(this.currentSubscriber);
        }

        public void onError(Throwable e) {
            this.f918s.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            synchronized (this.guard) {
                this.mainDone = true;
                if (this.active) {
                } else if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(this.nl.completed());
                } else {
                    List<Object> localQueue = this.queue;
                    this.queue = null;
                    this.emitting = true;
                    drain(localQueue);
                    this.f918s.onCompleted();
                    unsubscribe();
                }
            }
        }

        void emit(T value, int id, InnerSubscriber innerSubscriber) {
            synchronized (this.guard) {
                if (id != this.index) {
                } else if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    if (innerSubscriber.requested != Long.MAX_VALUE) {
                        innerSubscriber.requested = innerSubscriber.requested - 1;
                    }
                    this.queue.add(value);
                } else {
                    List<Object> localQueue = this.queue;
                    this.queue = null;
                    this.emitting = true;
                    boolean once = true;
                    boolean skipFinal = false;
                    do {
                        drain(localQueue);
                        if (once) {
                            once = false;
                            synchronized (this.guard) {
                                if (innerSubscriber.requested != Long.MAX_VALUE) {
                                    innerSubscriber.requested = innerSubscriber.requested - 1;
                                }
                            }
                            this.f918s.onNext(value);
                        }
                        synchronized (this.guard) {
                            localQueue = this.queue;
                            this.queue = null;
                            if (localQueue == null) {
                                this.emitting = false;
                                skipFinal = true;
                                break;
                            }
                            try {
                            } catch (Throwable th) {
                                if (null == null) {
                                    synchronized (this.guard) {
                                    }
                                    this.emitting = false;
                                }
                            }
                        }
                    } while (!this.f918s.isUnsubscribed());
                    if (!skipFinal) {
                        synchronized (this.guard) {
                            this.emitting = false;
                        }
                    }
                }
            }
        }

        void drain(List<Object> localQueue) {
            if (localQueue != null) {
                for (T o : localQueue) {
                    if (this.nl.isCompleted(o)) {
                        this.f918s.onCompleted();
                        return;
                    } else if (this.nl.isError(o)) {
                        this.f918s.onError(this.nl.getError(o));
                        return;
                    } else {
                        this.f918s.onNext(o);
                    }
                }
            }
        }

        void error(Throwable e, int id) {
            synchronized (this.guard) {
                if (id != this.index) {
                } else if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(this.nl.error(e));
                } else {
                    List<Object> localQueue = this.queue;
                    this.queue = null;
                    this.emitting = true;
                    drain(localQueue);
                    this.f918s.onError(e);
                    unsubscribe();
                }
            }
        }

        void complete(int id) {
            synchronized (this.guard) {
                if (id != this.index) {
                    return;
                }
                this.active = false;
                if (!this.mainDone) {
                } else if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(this.nl.completed());
                } else {
                    List<Object> localQueue = this.queue;
                    this.queue = null;
                    this.emitting = true;
                    drain(localQueue);
                    this.f918s.onCompleted();
                    unsubscribe();
                }
            }
        }
    }

    public static <T> OperatorSwitch<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorSwitch() {
    }

    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> child) {
        SwitchSubscriber<T> sws = new SwitchSubscriber(child);
        child.add(sws);
        return sws;
    }
}
