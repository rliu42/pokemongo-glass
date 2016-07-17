package rx.internal.producers;

import rx.Producer;

public final class ProducerArbiter implements Producer {
    static final Producer NULL_PRODUCER;
    Producer currentProducer;
    boolean emitting;
    long missedProduced;
    Producer missedProducer;
    long missedRequested;
    long requested;

    /* renamed from: rx.internal.producers.ProducerArbiter.1 */
    static class C13351 implements Producer {
        C13351() {
        }

        public void request(long n) {
        }
    }

    static {
        NULL_PRODUCER = new C13351();
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

    public void produced(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n > 0 required");
        }
        synchronized (this) {
            if (this.emitting) {
                this.missedProduced += n;
                return;
            }
            this.emitting = true;
            try {
                long r = this.requested;
                if (r != Long.MAX_VALUE) {
                    long u = r - n;
                    if (u < 0) {
                        throw new IllegalStateException("more items arrived than were requested");
                    }
                    this.requested = u;
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

    public void setProducer(Producer newProducer) {
        synchronized (this) {
            if (this.emitting) {
                if (newProducer == null) {
                    newProducer = NULL_PRODUCER;
                }
                this.missedProducer = newProducer;
                return;
            }
            this.emitting = true;
            try {
                this.currentProducer = newProducer;
                if (newProducer != null) {
                    newProducer.request(this.requested);
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
    public void emitLoop() {
        /*
        r14 = this;
    L_0x0000:
        monitor-enter(r14);
        r4 = r14.missedRequested;	 Catch:{ all -> 0x0053 }
        r0 = r14.missedProduced;	 Catch:{ all -> 0x0053 }
        r2 = r14.missedProducer;	 Catch:{ all -> 0x0053 }
        r12 = 0;
        r12 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
        if (r12 != 0) goto L_0x001a;
    L_0x000d:
        r12 = 0;
        r12 = (r0 > r12 ? 1 : (r0 == r12 ? 0 : -1));
        if (r12 != 0) goto L_0x001a;
    L_0x0013:
        if (r2 != 0) goto L_0x001a;
    L_0x0015:
        r12 = 0;
        r14.emitting = r12;	 Catch:{ all -> 0x0053 }
        monitor-exit(r14);	 Catch:{ all -> 0x0053 }
        return;
    L_0x001a:
        r12 = 0;
        r14.missedRequested = r12;	 Catch:{ all -> 0x0053 }
        r12 = 0;
        r14.missedProduced = r12;	 Catch:{ all -> 0x0053 }
        r12 = 0;
        r14.missedProducer = r12;	 Catch:{ all -> 0x0053 }
        monitor-exit(r14);	 Catch:{ all -> 0x0053 }
        r6 = r14.requested;
        r12 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r12 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1));
        if (r12 == 0) goto L_0x0049;
    L_0x0031:
        r8 = r6 + r4;
        r12 = 0;
        r12 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
        if (r12 < 0) goto L_0x0042;
    L_0x0039:
        r12 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r12 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
        if (r12 != 0) goto L_0x0056;
    L_0x0042:
        r6 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r14.requested = r6;
    L_0x0049:
        if (r2 == 0) goto L_0x0070;
    L_0x004b:
        r12 = NULL_PRODUCER;
        if (r2 != r12) goto L_0x006a;
    L_0x004f:
        r12 = 0;
        r14.currentProducer = r12;
        goto L_0x0000;
    L_0x0053:
        r12 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0053 }
        throw r12;
    L_0x0056:
        r10 = r8 - r0;
        r12 = 0;
        r12 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r12 >= 0) goto L_0x0066;
    L_0x005e:
        r12 = new java.lang.IllegalStateException;
        r13 = "more produced than requested";
        r12.<init>(r13);
        throw r12;
    L_0x0066:
        r6 = r10;
        r14.requested = r10;
        goto L_0x0049;
    L_0x006a:
        r14.currentProducer = r2;
        r2.request(r6);
        goto L_0x0000;
    L_0x0070:
        r3 = r14.currentProducer;
        if (r3 == 0) goto L_0x0000;
    L_0x0074:
        r12 = 0;
        r12 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
        if (r12 == 0) goto L_0x0000;
    L_0x007a:
        r3.request(r4);
        goto L_0x0000;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerArbiter.emitLoop():void");
    }
}
