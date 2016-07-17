package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Observable.OnSubscribe;
import rx.Producer;
import rx.Subscriber;

public final class OnSubscribeRange implements OnSubscribe<Integer> {
    private final int end;
    private final int start;

    private static final class RangeProducer implements Producer {
        private static final AtomicLongFieldUpdater<RangeProducer> REQUESTED_UPDATER;
        private final int end;
        private long index;
        private final Subscriber<? super Integer> f916o;
        private volatile long requested;

        static {
            REQUESTED_UPDATER = AtomicLongFieldUpdater.newUpdater(RangeProducer.class, "requested");
        }

        private RangeProducer(Subscriber<? super Integer> o, int start, int end) {
            this.f916o = o;
            this.index = (long) start;
            this.end = end;
        }

        public void request(long n) {
            if (this.requested != Long.MAX_VALUE) {
                long i;
                if (n == Long.MAX_VALUE && REQUESTED_UPDATER.compareAndSet(this, 0, Long.MAX_VALUE)) {
                    i = this.index;
                    while (i <= ((long) this.end)) {
                        if (!this.f916o.isUnsubscribed()) {
                            this.f916o.onNext(Integer.valueOf((int) i));
                            i++;
                        } else {
                            return;
                        }
                    }
                    if (!this.f916o.isUnsubscribed()) {
                        this.f916o.onCompleted();
                    }
                } else if (n > 0 && BackpressureUtils.getAndAddRequest(REQUESTED_UPDATER, this, n) == 0) {
                    long e;
                    do {
                        long r = this.requested;
                        long idx = this.index;
                        long numLeft = (((long) this.end) - idx) + 1;
                        e = Math.min(numLeft, r);
                        boolean completeOnFinish = numLeft <= r;
                        long stopAt = e + idx;
                        i = idx;
                        while (i < stopAt) {
                            if (!this.f916o.isUnsubscribed()) {
                                this.f916o.onNext(Integer.valueOf((int) i));
                                i++;
                            } else {
                                return;
                            }
                        }
                        this.index = stopAt;
                        if (completeOnFinish) {
                            this.f916o.onCompleted();
                            return;
                        }
                    } while (REQUESTED_UPDATER.addAndGet(this, -e) != 0);
                }
            }
        }
    }

    public OnSubscribeRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void call(Subscriber<? super Integer> o) {
        o.setProducer(new RangeProducer(this.start, this.end, null));
    }
}
