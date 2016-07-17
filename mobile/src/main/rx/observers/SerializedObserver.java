package rx.observers;

import rx.Observer;
import rx.exceptions.Exceptions;

public class SerializedObserver<T> implements Observer<T> {
    private static final Object COMPLETE_SENTINEL;
    private static final int MAX_DRAIN_ITERATION = Integer.MAX_VALUE;
    private static final Object NULL_SENTINEL;
    private final Observer<? super T> actual;
    private boolean emitting;
    private FastList queue;
    private boolean terminated;

    private static final class ErrorSentinel {
        final Throwable f926e;

        ErrorSentinel(Throwable e) {
            this.f926e = e;
        }
    }

    static final class FastList {
        Object[] array;
        int size;

        FastList() {
        }

        public void add(Object o) {
            int s = this.size;
            Object[] a = this.array;
            if (a == null) {
                a = new Object[16];
                this.array = a;
            } else if (s == a.length) {
                Object[] array2 = new Object[((s >> 2) + s)];
                System.arraycopy(a, 0, array2, 0, s);
                a = array2;
                this.array = a;
            }
            a[s] = o;
            this.size = s + 1;
        }
    }

    static {
        NULL_SENTINEL = new Object();
        COMPLETE_SENTINEL = new Object();
    }

    public SerializedObserver(Observer<? super T> s) {
        this.emitting = false;
        this.terminated = false;
        this.actual = s;
    }

    public void onCompleted() {
        synchronized (this) {
            if (this.terminated) {
                return;
            }
            this.terminated = true;
            if (this.emitting) {
                if (this.queue == null) {
                    this.queue = new FastList();
                }
                this.queue.add(COMPLETE_SENTINEL);
                return;
            }
            this.emitting = true;
            FastList list = this.queue;
            this.queue = null;
            drainQueue(list);
            this.actual.onCompleted();
        }
    }

    public void onError(Throwable e) {
        Exceptions.throwIfFatal(e);
        synchronized (this) {
            if (this.terminated) {
            } else if (this.emitting) {
                if (this.queue == null) {
                    this.queue = new FastList();
                }
                this.queue.add(new ErrorSentinel(e));
            } else {
                this.emitting = true;
                FastList list = this.queue;
                this.queue = null;
                drainQueue(list);
                this.actual.onError(e);
                synchronized (this) {
                    this.emitting = false;
                }
            }
        }
    }

    public void onNext(T t) {
        synchronized (this) {
            if (this.terminated) {
            } else if (this.emitting) {
                if (this.queue == null) {
                    this.queue = new FastList();
                }
                FastList fastList = this.queue;
                if (t == null) {
                    t = NULL_SENTINEL;
                }
                fastList.add(t);
            } else {
                this.emitting = true;
                FastList list = this.queue;
                this.queue = null;
                int iter = MAX_DRAIN_ITERATION;
                do {
                    try {
                        drainQueue(list);
                        if (iter == MAX_DRAIN_ITERATION) {
                            this.actual.onNext(t);
                        }
                        iter--;
                        if (iter > 0) {
                            synchronized (this) {
                                list = this.queue;
                                this.queue = null;
                                if (list != null) {
                                    while (true) {
                                        break;
                                    }
                                }
                                this.emitting = false;
                                if (!true) {
                                    synchronized (this) {
                                        if (this.terminated) {
                                            list = this.queue;
                                            this.queue = null;
                                        } else {
                                            this.emitting = false;
                                        }
                                    }
                                    return;
                                }
                                return;
                            }
                        }
                    } catch (Throwable th) {
                        if (null == null) {
                            synchronized (this) {
                            }
                            if (this.terminated) {
                                list = this.queue;
                                this.queue = null;
                            } else {
                                this.emitting = false;
                            }
                        }
                    }
                } while (iter > 0);
                if (null == null) {
                    synchronized (this) {
                        if (this.terminated) {
                            list = this.queue;
                            this.queue = null;
                        } else {
                            this.emitting = false;
                            list = null;
                        }
                    }
                }
                drainQueue(list);
            }
        }
    }

    void drainQueue(FastList list) {
        if (list != null && list.size != 0) {
            Object[] arr$ = list.array;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                T v = arr$[i$];
                if (v != null) {
                    if (v == NULL_SENTINEL) {
                        this.actual.onNext(null);
                    } else if (v == COMPLETE_SENTINEL) {
                        this.actual.onCompleted();
                    } else if (v.getClass() == ErrorSentinel.class) {
                        this.actual.onError(((ErrorSentinel) v).f926e);
                    } else {
                        this.actual.onNext(v);
                    }
                    i$++;
                } else {
                    return;
                }
            }
        }
    }
}
