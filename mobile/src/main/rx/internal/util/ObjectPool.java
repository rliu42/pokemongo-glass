package rx.internal.util;

import com.google.android.gms.location.places.Place;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import rx.Scheduler.Worker;
import rx.functions.Action0;
import rx.internal.util.unsafe.MpmcArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.schedulers.Schedulers;

public abstract class ObjectPool<T> {
    private final int maxSize;
    private Queue<T> pool;
    private Worker schedulerWorker;

    /* renamed from: rx.internal.util.ObjectPool.1 */
    class C13401 implements Action0 {
        final /* synthetic */ int val$max;
        final /* synthetic */ int val$min;

        C13401(int i, int i2) {
            this.val$min = i;
            this.val$max = i2;
        }

        public void call() {
            int size = ObjectPool.this.pool.size();
            int i;
            if (size < this.val$min) {
                int sizeToBeAdded = this.val$max - size;
                for (i = 0; i < sizeToBeAdded; i++) {
                    ObjectPool.this.pool.add(ObjectPool.this.createObject());
                }
            } else if (size > this.val$max) {
                int sizeToBeRemoved = size - this.val$max;
                for (i = 0; i < sizeToBeRemoved; i++) {
                    ObjectPool.this.pool.poll();
                }
            }
        }
    }

    protected abstract T createObject();

    public ObjectPool() {
        this(0, 0, 67);
    }

    private ObjectPool(int min, int max, long validationInterval) {
        this.maxSize = max;
        initialize(min);
        this.schedulerWorker = Schedulers.computation().createWorker();
        this.schedulerWorker.schedulePeriodically(new C13401(min, max), validationInterval, validationInterval, TimeUnit.SECONDS);
    }

    public T borrowObject() {
        T object = this.pool.poll();
        if (object == null) {
            return createObject();
        }
        return object;
    }

    public void returnObject(T object) {
        if (object != null) {
            this.pool.offer(object);
        }
    }

    public void shutdown() {
        this.schedulerWorker.unsubscribe();
    }

    private void initialize(int min) {
        if (UnsafeAccess.isUnsafeAvailable()) {
            this.pool = new MpmcArrayQueue(Math.max(this.maxSize, Place.TYPE_SUBLOCALITY_LEVEL_2));
        } else {
            this.pool = new ConcurrentLinkedQueue();
        }
        for (int i = 0; i < min; i++) {
            this.pool.add(createObject());
        }
    }
}
