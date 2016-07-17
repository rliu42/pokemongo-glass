package rx.internal.util;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Producer;
import rx.annotations.Experimental;

@Experimental
public final class BackpressureDrainManager implements Producer {
    protected static final AtomicLongFieldUpdater<BackpressureDrainManager> REQUESTED_COUNT;
    protected final BackpressureQueueCallback actual;
    protected boolean emitting;
    protected Throwable exception;
    protected volatile long requestedCount;
    protected volatile boolean terminated;

    public interface BackpressureQueueCallback {
        boolean accept(Object obj);

        void complete(Throwable th);

        Object peek();

        Object poll();
    }

    static {
        REQUESTED_COUNT = AtomicLongFieldUpdater.newUpdater(BackpressureDrainManager.class, "requestedCount");
    }

    public BackpressureDrainManager(BackpressureQueueCallback actual) {
        this.actual = actual;
    }

    public final boolean isTerminated() {
        return this.terminated;
    }

    public final void terminate() {
        this.terminated = true;
    }

    public final void terminate(Throwable error) {
        if (!this.terminated) {
            this.exception = error;
            this.terminated = true;
        }
    }

    public final void terminateAndDrain() {
        this.terminated = true;
        drain();
    }

    public final void terminateAndDrain(Throwable error) {
        if (!this.terminated) {
            this.exception = error;
            this.terminated = true;
            drain();
        }
    }

    public final void request(long n) {
        if (n != 0) {
            boolean mayDrain;
            long r;
            long u;
            do {
                r = this.requestedCount;
                mayDrain = r == 0;
                if (r == Long.MAX_VALUE) {
                    break;
                } else if (n == Long.MAX_VALUE) {
                    u = n;
                    mayDrain = true;
                } else if (r > Long.MAX_VALUE - n) {
                    u = Long.MAX_VALUE;
                } else {
                    u = r + n;
                }
            } while (!REQUESTED_COUNT.compareAndSet(this, r, u));
            if (mayDrain) {
                drain();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void drain() {
        /*
        r14 = this;
        monitor-enter(r14);
        r9 = r14.emitting;	 Catch:{ all -> 0x0034 }
        if (r9 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r14);	 Catch:{ all -> 0x0034 }
    L_0x0006:
        return;
    L_0x0007:
        r9 = 1;
        r14.emitting = r9;	 Catch:{ all -> 0x0034 }
        r8 = r14.terminated;	 Catch:{ all -> 0x0034 }
        monitor-exit(r14);	 Catch:{ all -> 0x0034 }
        r4 = r14.requestedCount;
        r7 = 0;
        r0 = r14.actual;	 Catch:{ all -> 0x0090 }
    L_0x0012:
        r2 = 0;
    L_0x0013:
        r10 = 0;
        r9 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r9 > 0) goto L_0x001b;
    L_0x0019:
        if (r8 == 0) goto L_0x003d;
    L_0x001b:
        if (r8 == 0) goto L_0x0066;
    L_0x001d:
        r6 = r0.peek();	 Catch:{ all -> 0x0090 }
        if (r6 != 0) goto L_0x0037;
    L_0x0023:
        r7 = 1;
        r1 = r14.exception;	 Catch:{ all -> 0x0090 }
        r0.complete(r1);	 Catch:{ all -> 0x0090 }
        if (r7 != 0) goto L_0x0006;
    L_0x002b:
        monitor-enter(r14);
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x0031 }
        monitor-exit(r14);	 Catch:{ all -> 0x0031 }
        goto L_0x0006;
    L_0x0031:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0031 }
        throw r9;
    L_0x0034:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0034 }
        throw r9;
    L_0x0037:
        r10 = 0;
        r9 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r9 != 0) goto L_0x0066;
    L_0x003d:
        monitor-enter(r14);	 Catch:{ all -> 0x0090 }
        r8 = r14.terminated;	 Catch:{ all -> 0x008d }
        r9 = r0.peek();	 Catch:{ all -> 0x008d }
        if (r9 == 0) goto L_0x0084;
    L_0x0046:
        r3 = 1;
    L_0x0047:
        r10 = r14.requestedCount;	 Catch:{ all -> 0x008d }
        r12 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r9 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r9 != 0) goto L_0x0099;
    L_0x0052:
        if (r3 != 0) goto L_0x0086;
    L_0x0054:
        if (r8 != 0) goto L_0x0086;
    L_0x0056:
        r7 = 1;
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x008d }
        monitor-exit(r14);	 Catch:{ all -> 0x008d }
        if (r7 != 0) goto L_0x0006;
    L_0x005d:
        monitor-enter(r14);
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x0063 }
        monitor-exit(r14);	 Catch:{ all -> 0x0063 }
        goto L_0x0006;
    L_0x0063:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0063 }
        throw r9;
    L_0x0066:
        r6 = r0.poll();	 Catch:{ all -> 0x0090 }
        if (r6 == 0) goto L_0x003d;
    L_0x006c:
        r9 = r0.accept(r6);	 Catch:{ all -> 0x0090 }
        if (r9 == 0) goto L_0x007e;
    L_0x0072:
        r7 = 1;
        if (r7 != 0) goto L_0x0006;
    L_0x0075:
        monitor-enter(r14);
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x007b }
        monitor-exit(r14);	 Catch:{ all -> 0x007b }
        goto L_0x0006;
    L_0x007b:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x007b }
        throw r9;
    L_0x007e:
        r10 = 1;
        r4 = r4 - r10;
        r2 = r2 + 1;
        goto L_0x0013;
    L_0x0084:
        r3 = 0;
        goto L_0x0047;
    L_0x0086:
        r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
    L_0x008b:
        monitor-exit(r14);	 Catch:{ all -> 0x008d }
        goto L_0x0012;
    L_0x008d:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x008d }
        throw r9;	 Catch:{ all -> 0x0090 }
    L_0x0090:
        r9 = move-exception;
        if (r7 != 0) goto L_0x0098;
    L_0x0093:
        monitor-enter(r14);
        r10 = 0;
        r14.emitting = r10;	 Catch:{ all -> 0x00be }
        monitor-exit(r14);	 Catch:{ all -> 0x00be }
    L_0x0098:
        throw r9;
    L_0x0099:
        r9 = REQUESTED_COUNT;	 Catch:{ all -> 0x008d }
        r10 = -r2;
        r10 = (long) r10;	 Catch:{ all -> 0x008d }
        r4 = r9.addAndGet(r14, r10);	 Catch:{ all -> 0x008d }
        r10 = 0;
        r9 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r9 == 0) goto L_0x00a9;
    L_0x00a7:
        if (r3 != 0) goto L_0x008b;
    L_0x00a9:
        if (r8 == 0) goto L_0x00ad;
    L_0x00ab:
        if (r3 == 0) goto L_0x008b;
    L_0x00ad:
        r7 = 1;
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x008d }
        monitor-exit(r14);	 Catch:{ all -> 0x008d }
        if (r7 != 0) goto L_0x0006;
    L_0x00b4:
        monitor-enter(r14);
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x00bb }
        monitor-exit(r14);	 Catch:{ all -> 0x00bb }
        goto L_0x0006;
    L_0x00bb:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x00bb }
        throw r9;
    L_0x00be:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x00be }
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.util.BackpressureDrainManager.drain():void");
    }
}
