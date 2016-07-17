package rx.internal.operators;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.internal.util.BackpressureDrainManager;
import rx.internal.util.BackpressureDrainManager.BackpressureQueueCallback;

public class OperatorOnBackpressureBlock<T> implements Operator<T, T> {
    final int max;

    static final class BlockingSubscriber<T> extends Subscriber<T> implements BackpressureQueueCallback {
        final Subscriber<? super T> child;
        final BackpressureDrainManager manager;
        final NotificationLite<T> nl;
        final BlockingQueue<Object> queue;

        public BlockingSubscriber(int max, Subscriber<? super T> child) {
            this.nl = NotificationLite.instance();
            this.queue = new ArrayBlockingQueue(max);
            this.child = child;
            this.manager = new BackpressureDrainManager(this);
        }

        void init() {
            this.child.add(this);
            this.child.setProducer(this.manager);
        }

        public void onNext(T t) {
            try {
                this.queue.put(this.nl.next(t));
                this.manager.drain();
            } catch (InterruptedException ex) {
                if (!isUnsubscribed()) {
                    onError(ex);
                }
            }
        }

        public void onError(Throwable e) {
            this.manager.terminateAndDrain(e);
        }

        public void onCompleted() {
            this.manager.terminateAndDrain();
        }

        public boolean accept(Object value) {
            return this.nl.accept(this.child, value);
        }

        public void complete(Throwable exception) {
            if (exception != null) {
                this.child.onError(exception);
            } else {
                this.child.onCompleted();
            }
        }

        public Object peek() {
            return this.queue.peek();
        }

        public Object poll() {
            return this.queue.poll();
        }
    }

    public OperatorOnBackpressureBlock(int max) {
        this.max = max;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        BlockingSubscriber<T> s = new BlockingSubscriber(this.max, child);
        s.init();
        return s;
    }
}
