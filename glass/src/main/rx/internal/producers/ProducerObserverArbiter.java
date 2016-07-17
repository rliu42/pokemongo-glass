package rx.internal.producers;

import java.util.ArrayList;
import java.util.List;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;

public final class ProducerObserverArbiter<T> implements Producer, Observer<T> {
    static final Producer NULL_PRODUCER;
    final Subscriber<? super T> child;
    Producer currentProducer;
    boolean emitting;
    volatile boolean hasError;
    Producer missedProducer;
    long missedRequested;
    Object missedTerminal;
    List<T> queue;
    long requested;

    /* renamed from: rx.internal.producers.ProducerObserverArbiter.1 */
    static class C13361 implements Producer {
        C13361() {
        }

        public void request(long n) {
        }
    }

    static {
        NULL_PRODUCER = new C13361();
    }

    public ProducerObserverArbiter(Subscriber<? super T> child) {
        this.child = child;
    }

    public void onNext(T t) {
        synchronized (this) {
            if (this.emitting) {
                List<T> q = this.queue;
                if (q == null) {
                    q = new ArrayList(4);
                    this.queue = q;
                }
                q.add(t);
                return;
            }
            try {
                this.child.onNext(t);
                long r = this.requested;
                if (r != Long.MAX_VALUE) {
                    this.requested = r - 1;
                }
                emitLoop();
                if (!true) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
            } catch (Throwable th) {
                if (!false) {
                    synchronized (this) {
                    }
                    this.emitting = false;
                }
            }
        }
    }

    public void onError(Throwable e) {
        boolean emit;
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = e;
                emit = false;
            } else {
                this.emitting = true;
                emit = true;
            }
        }
        if (emit) {
            this.child.onError(e);
        } else {
            this.hasError = true;
        }
    }

    public void onCompleted() {
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = Boolean.valueOf(true);
                return;
            }
            this.emitting = true;
            this.child.onCompleted();
        }
    }

    public void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        } else if (n != 0) {
            synchronized (this) {
                if (this.emitting) {
                    this.missedRequested += n;
                    return;
                }
                this.emitting = true;
                try {
                    long u = this.requested + n;
                    if (u < 0) {
                        u = Long.MAX_VALUE;
                    }
                    this.requested = u;
                    Producer p = this.currentProducer;
                    if (p != null) {
                        p.request(n);
                    }
                    emitLoop();
                    if (!true) {
                        synchronized (this) {
                            this.emitting = false;
                        }
                    }
                } catch (Throwable th) {
                    if (!false) {
                        synchronized (this) {
                        }
                        this.emitting = false;
                    }
                }
            }
        }
    }

    public void setProducer(Producer p) {
        synchronized (this) {
            if (this.emitting) {
                if (p == null) {
                    p = NULL_PRODUCER;
                }
                this.missedProducer = p;
                return;
            }
            this.emitting = true;
            try {
                this.currentProducer = p;
                long r = this.requested;
                if (!(p == null || r == 0)) {
                    p.request(r);
                }
                emitLoop();
                if (!true) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
            } catch (Throwable th) {
                if (!false) {
                    synchronized (this) {
                    }
                    this.emitting = false;
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void emitLoop() {
        /*
        r24 = this;
        r0 = r24;
        r4 = r0.child;
    L_0x0004:
        monitor-enter(r24);
        r0 = r24;
        r12 = r0.missedRequested;	 Catch:{ all -> 0x0065 }
        r0 = r24;
        r11 = r0.missedProducer;	 Catch:{ all -> 0x0065 }
        r0 = r24;
        r14 = r0.missedTerminal;	 Catch:{ all -> 0x0065 }
        r0 = r24;
        r0 = r0.queue;	 Catch:{ all -> 0x0065 }
        r16 = r0;
        r22 = 0;
        r22 = (r12 > r22 ? 1 : (r12 == r22 ? 0 : -1));
        if (r22 != 0) goto L_0x002d;
    L_0x001d:
        if (r11 != 0) goto L_0x002d;
    L_0x001f:
        if (r16 != 0) goto L_0x002d;
    L_0x0021:
        if (r14 != 0) goto L_0x002d;
    L_0x0023:
        r22 = 0;
        r0 = r22;
        r1 = r24;
        r1.emitting = r0;	 Catch:{ all -> 0x0065 }
        monitor-exit(r24);	 Catch:{ all -> 0x0065 }
    L_0x002c:
        return;
    L_0x002d:
        r22 = 0;
        r0 = r22;
        r2 = r24;
        r2.missedRequested = r0;	 Catch:{ all -> 0x0065 }
        r22 = 0;
        r0 = r22;
        r1 = r24;
        r1.missedProducer = r0;	 Catch:{ all -> 0x0065 }
        r22 = 0;
        r0 = r22;
        r1 = r24;
        r1.queue = r0;	 Catch:{ all -> 0x0065 }
        r22 = 0;
        r0 = r22;
        r1 = r24;
        r1.missedTerminal = r0;	 Catch:{ all -> 0x0065 }
        monitor-exit(r24);	 Catch:{ all -> 0x0065 }
        if (r16 == 0) goto L_0x0056;
    L_0x0050:
        r22 = r16.isEmpty();
        if (r22 == 0) goto L_0x0068;
    L_0x0056:
        r5 = 1;
    L_0x0057:
        if (r14 == 0) goto L_0x0070;
    L_0x0059:
        r22 = java.lang.Boolean.TRUE;
        r0 = r22;
        if (r14 == r0) goto L_0x006a;
    L_0x005f:
        r14 = (java.lang.Throwable) r14;
        r4.onError(r14);
        goto L_0x002c;
    L_0x0065:
        r22 = move-exception;
        monitor-exit(r24);	 Catch:{ all -> 0x0065 }
        throw r22;
    L_0x0068:
        r5 = 0;
        goto L_0x0057;
    L_0x006a:
        if (r5 == 0) goto L_0x0070;
    L_0x006c:
        r4.onCompleted();
        goto L_0x002c;
    L_0x0070:
        r6 = 0;
        if (r16 == 0) goto L_0x00af;
    L_0x0074:
        r10 = r16.iterator();
    L_0x0078:
        r22 = r10.hasNext();
        if (r22 == 0) goto L_0x00a4;
    L_0x007e:
        r17 = r10.next();
        r22 = r4.isUnsubscribed();
        if (r22 != 0) goto L_0x002c;
    L_0x0088:
        r0 = r24;
        r0 = r0.hasError;
        r22 = r0;
        if (r22 != 0) goto L_0x0004;
    L_0x0090:
        r0 = r17;
        r4.onNext(r0);	 Catch:{ Throwable -> 0x0096 }
        goto L_0x0078;
    L_0x0096:
        r8 = move-exception;
        rx.exceptions.Exceptions.throwIfFatal(r8);
        r0 = r17;
        r9 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r8, r0);
        r4.onError(r9);
        goto L_0x002c;
    L_0x00a4:
        r22 = r16.size();
        r0 = r22;
        r0 = (long) r0;
        r22 = r0;
        r6 = r6 + r22;
    L_0x00af:
        r0 = r24;
        r0 = r0.requested;
        r18 = r0;
        r22 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r22 = (r18 > r22 ? 1 : (r18 == r22 ? 0 : -1));
        if (r22 == 0) goto L_0x00fa;
    L_0x00be:
        r22 = 0;
        r22 = (r12 > r22 ? 1 : (r12 == r22 ? 0 : -1));
        if (r22 == 0) goto L_0x00d3;
    L_0x00c4:
        r20 = r18 + r12;
        r22 = 0;
        r22 = (r20 > r22 ? 1 : (r20 == r22 ? 0 : -1));
        if (r22 >= 0) goto L_0x00d1;
    L_0x00cc:
        r20 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
    L_0x00d1:
        r18 = r20;
    L_0x00d3:
        r22 = 0;
        r22 = (r6 > r22 ? 1 : (r6 == r22 ? 0 : -1));
        if (r22 == 0) goto L_0x00f4;
    L_0x00d9:
        r22 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r22 = (r18 > r22 ? 1 : (r18 == r22 ? 0 : -1));
        if (r22 == 0) goto L_0x00f4;
    L_0x00e2:
        r20 = r18 - r6;
        r22 = 0;
        r22 = (r20 > r22 ? 1 : (r20 == r22 ? 0 : -1));
        if (r22 >= 0) goto L_0x00f2;
    L_0x00ea:
        r22 = new java.lang.IllegalStateException;
        r23 = "More produced than requested";
        r22.<init>(r23);
        throw r22;
    L_0x00f2:
        r18 = r20;
    L_0x00f4:
        r0 = r18;
        r2 = r24;
        r2.requested = r0;
    L_0x00fa:
        if (r11 == 0) goto L_0x011d;
    L_0x00fc:
        r22 = NULL_PRODUCER;
        r0 = r22;
        if (r11 != r0) goto L_0x010c;
    L_0x0102:
        r22 = 0;
        r0 = r22;
        r1 = r24;
        r1.currentProducer = r0;
        goto L_0x0004;
    L_0x010c:
        r0 = r24;
        r0.currentProducer = r11;
        r22 = 0;
        r22 = (r18 > r22 ? 1 : (r18 == r22 ? 0 : -1));
        if (r22 == 0) goto L_0x0004;
    L_0x0116:
        r0 = r18;
        r11.request(r0);
        goto L_0x0004;
    L_0x011d:
        r0 = r24;
        r15 = r0.currentProducer;
        if (r15 == 0) goto L_0x0004;
    L_0x0123:
        r22 = 0;
        r22 = (r12 > r22 ? 1 : (r12 == r22 ? 0 : -1));
        if (r22 == 0) goto L_0x0004;
    L_0x0129:
        r15.request(r12);
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerObserverArbiter.emitLoop():void");
    }
}
