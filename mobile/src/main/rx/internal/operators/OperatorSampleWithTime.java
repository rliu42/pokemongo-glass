package rx.internal.operators;

import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.SerializedSubscriber;

public final class OperatorSampleWithTime<T> implements Operator<T, T> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    static final class SamplerSubscriber<T> extends Subscriber<T> implements Action0 {
        private static final Object EMPTY_TOKEN;
        static final AtomicReferenceFieldUpdater<SamplerSubscriber, Object> VALUE_UPDATER;
        private final Subscriber<? super T> subscriber;
        volatile Object value;

        static {
            EMPTY_TOKEN = new Object();
            VALUE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(SamplerSubscriber.class, Object.class, GameServices.SCORE_VALUE);
        }

        public SamplerSubscriber(Subscriber<? super T> subscriber) {
            this.value = EMPTY_TOKEN;
            this.subscriber = subscriber;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(T t) {
            this.value = t;
        }

        public void onError(Throwable e) {
            this.subscriber.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            this.subscriber.onCompleted();
            unsubscribe();
        }

        public void call() {
            T localValue = VALUE_UPDATER.getAndSet(this, EMPTY_TOKEN);
            if (localValue != EMPTY_TOKEN) {
                try {
                    this.subscriber.onNext(localValue);
                } catch (Throwable e) {
                    onError(e);
                }
            }
        }
    }

    public OperatorSampleWithTime(long time, TimeUnit unit, Scheduler scheduler) {
        this.time = time;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        SerializedSubscriber<T> s = new SerializedSubscriber(child);
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        SamplerSubscriber<T> sampler = new SamplerSubscriber(s);
        child.add(sampler);
        worker.schedulePeriodically(sampler, this.time, this.time, this.unit);
        return sampler;
    }
}
