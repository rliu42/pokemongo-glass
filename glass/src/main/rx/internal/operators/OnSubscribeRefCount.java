package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public final class OnSubscribeRefCount<T> implements OnSubscribe<T> {
    private volatile CompositeSubscription baseSubscription;
    private final ReentrantLock lock;
    private final ConnectableObservable<? extends T> source;
    private final AtomicInteger subscriptionCount;

    /* renamed from: rx.internal.operators.OnSubscribeRefCount.1 */
    class C11811 implements Action1<Subscription> {
        final /* synthetic */ Subscriber val$subscriber;
        final /* synthetic */ AtomicBoolean val$writeLocked;

        C11811(Subscriber subscriber, AtomicBoolean atomicBoolean) {
            this.val$subscriber = subscriber;
            this.val$writeLocked = atomicBoolean;
        }

        public void call(Subscription subscription) {
            try {
                OnSubscribeRefCount.this.baseSubscription.add(subscription);
                OnSubscribeRefCount.this.doSubscribe(this.val$subscriber, OnSubscribeRefCount.this.baseSubscription);
            } finally {
                OnSubscribeRefCount.this.lock.unlock();
                this.val$writeLocked.set(false);
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRefCount.2 */
    class C11822 extends Subscriber<T> {
        final /* synthetic */ CompositeSubscription val$currentBase;
        final /* synthetic */ Subscriber val$subscriber;

        C11822(Subscriber x0, Subscriber subscriber, CompositeSubscription compositeSubscription) {
            this.val$subscriber = subscriber;
            this.val$currentBase = compositeSubscription;
            super(x0);
        }

        public void onError(Throwable e) {
            cleanup();
            this.val$subscriber.onError(e);
        }

        public void onNext(T t) {
            this.val$subscriber.onNext(t);
        }

        public void onCompleted() {
            cleanup();
            this.val$subscriber.onCompleted();
        }

        void cleanup() {
            OnSubscribeRefCount.this.lock.lock();
            try {
                if (OnSubscribeRefCount.this.baseSubscription == this.val$currentBase) {
                    OnSubscribeRefCount.this.baseSubscription.unsubscribe();
                    OnSubscribeRefCount.this.baseSubscription = new CompositeSubscription();
                    OnSubscribeRefCount.this.subscriptionCount.set(0);
                }
                OnSubscribeRefCount.this.lock.unlock();
            } catch (Throwable th) {
                OnSubscribeRefCount.this.lock.unlock();
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRefCount.3 */
    class C11833 implements Action0 {
        final /* synthetic */ CompositeSubscription val$current;

        C11833(CompositeSubscription compositeSubscription) {
            this.val$current = compositeSubscription;
        }

        public void call() {
            OnSubscribeRefCount.this.lock.lock();
            try {
                if (OnSubscribeRefCount.this.baseSubscription == this.val$current && OnSubscribeRefCount.this.subscriptionCount.decrementAndGet() == 0) {
                    OnSubscribeRefCount.this.baseSubscription.unsubscribe();
                    OnSubscribeRefCount.this.baseSubscription = new CompositeSubscription();
                }
                OnSubscribeRefCount.this.lock.unlock();
            } catch (Throwable th) {
                OnSubscribeRefCount.this.lock.unlock();
            }
        }
    }

    public OnSubscribeRefCount(ConnectableObservable<? extends T> source) {
        this.baseSubscription = new CompositeSubscription();
        this.subscriptionCount = new AtomicInteger(0);
        this.lock = new ReentrantLock();
        this.source = source;
    }

    public void call(Subscriber<? super T> subscriber) {
        this.lock.lock();
        if (this.subscriptionCount.incrementAndGet() == 1) {
            AtomicBoolean writeLocked = new AtomicBoolean(true);
            try {
                this.source.connect(onSubscribe(subscriber, writeLocked));
                if (writeLocked.get()) {
                    this.lock.unlock();
                }
            } catch (Throwable th) {
                if (writeLocked.get()) {
                    this.lock.unlock();
                }
            }
        } else {
            try {
                doSubscribe(subscriber, this.baseSubscription);
            } finally {
                this.lock.unlock();
            }
        }
    }

    private Action1<Subscription> onSubscribe(Subscriber<? super T> subscriber, AtomicBoolean writeLocked) {
        return new C11811(subscriber, writeLocked);
    }

    void doSubscribe(Subscriber<? super T> subscriber, CompositeSubscription currentBase) {
        subscriber.add(disconnect(currentBase));
        this.source.unsafeSubscribe(new C11822(subscriber, subscriber, currentBase));
    }

    private Subscription disconnect(CompositeSubscription current) {
        return Subscriptions.create(new C11833(current));
    }
}
