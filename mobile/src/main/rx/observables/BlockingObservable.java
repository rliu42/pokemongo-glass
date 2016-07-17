package rx.observables;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.operators.BlockingOperatorLatest;
import rx.internal.operators.BlockingOperatorMostRecent;
import rx.internal.operators.BlockingOperatorNext;
import rx.internal.operators.BlockingOperatorToFuture;
import rx.internal.operators.BlockingOperatorToIterator;
import rx.internal.util.UtilityFunctions;

public final class BlockingObservable<T> {
    private final Observable<? extends T> f925o;

    /* renamed from: rx.observables.BlockingObservable.1 */
    class C13501 extends Subscriber<T> {
        final /* synthetic */ AtomicReference val$exceptionFromOnError;
        final /* synthetic */ CountDownLatch val$latch;
        final /* synthetic */ Action1 val$onNext;

        C13501(CountDownLatch countDownLatch, AtomicReference atomicReference, Action1 action1) {
            this.val$latch = countDownLatch;
            this.val$exceptionFromOnError = atomicReference;
            this.val$onNext = action1;
        }

        public void onCompleted() {
            this.val$latch.countDown();
        }

        public void onError(Throwable e) {
            this.val$exceptionFromOnError.set(e);
            this.val$latch.countDown();
        }

        public void onNext(T args) {
            this.val$onNext.call(args);
        }
    }

    /* renamed from: rx.observables.BlockingObservable.2 */
    class C13512 implements Iterable<T> {
        C13512() {
        }

        public Iterator<T> iterator() {
            return BlockingObservable.this.getIterator();
        }
    }

    /* renamed from: rx.observables.BlockingObservable.3 */
    class C13523 extends Subscriber<T> {
        final /* synthetic */ CountDownLatch val$latch;
        final /* synthetic */ AtomicReference val$returnException;
        final /* synthetic */ AtomicReference val$returnItem;

        C13523(CountDownLatch countDownLatch, AtomicReference atomicReference, AtomicReference atomicReference2) {
            this.val$latch = countDownLatch;
            this.val$returnException = atomicReference;
            this.val$returnItem = atomicReference2;
        }

        public void onCompleted() {
            this.val$latch.countDown();
        }

        public void onError(Throwable e) {
            this.val$returnException.set(e);
            this.val$latch.countDown();
        }

        public void onNext(T item) {
            this.val$returnItem.set(item);
        }
    }

    private BlockingObservable(Observable<? extends T> o) {
        this.f925o = o;
    }

    public static <T> BlockingObservable<T> from(Observable<? extends T> o) {
        return new BlockingObservable(o);
    }

    public void forEach(Action1<? super T> onNext) {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> exceptionFromOnError = new AtomicReference();
        Subscription subscription = this.f925o.subscribe(new C13501(latch, exceptionFromOnError, onNext));
        try {
            latch.await();
            if (exceptionFromOnError.get() == null) {
                return;
            }
            if (exceptionFromOnError.get() instanceof RuntimeException) {
                throw ((RuntimeException) exceptionFromOnError.get());
            }
            throw new RuntimeException((Throwable) exceptionFromOnError.get());
        } catch (InterruptedException e) {
            subscription.unsubscribe();
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for subscription to complete.", e);
        }
    }

    public Iterator<T> getIterator() {
        return BlockingOperatorToIterator.toIterator(this.f925o);
    }

    public T first() {
        return blockForSingle(this.f925o.first());
    }

    public T first(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f925o.first(predicate));
    }

    public T firstOrDefault(T defaultValue) {
        return blockForSingle(this.f925o.map(UtilityFunctions.identity()).firstOrDefault(defaultValue));
    }

    public T firstOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f925o.filter(predicate).map(UtilityFunctions.identity()).firstOrDefault(defaultValue));
    }

    public T last() {
        return blockForSingle(this.f925o.last());
    }

    public T last(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f925o.last(predicate));
    }

    public T lastOrDefault(T defaultValue) {
        return blockForSingle(this.f925o.map(UtilityFunctions.identity()).lastOrDefault(defaultValue));
    }

    public T lastOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f925o.filter(predicate).map(UtilityFunctions.identity()).lastOrDefault(defaultValue));
    }

    public Iterable<T> mostRecent(T initialValue) {
        return BlockingOperatorMostRecent.mostRecent(this.f925o, initialValue);
    }

    public Iterable<T> next() {
        return BlockingOperatorNext.next(this.f925o);
    }

    public Iterable<T> latest() {
        return BlockingOperatorLatest.latest(this.f925o);
    }

    public T single() {
        return blockForSingle(this.f925o.single());
    }

    public T single(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f925o.single(predicate));
    }

    public T singleOrDefault(T defaultValue) {
        return blockForSingle(this.f925o.map(UtilityFunctions.identity()).singleOrDefault(defaultValue));
    }

    public T singleOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f925o.filter(predicate).map(UtilityFunctions.identity()).singleOrDefault(defaultValue));
    }

    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(this.f925o);
    }

    public Iterable<T> toIterable() {
        return new C13512();
    }

    private T blockForSingle(Observable<? extends T> observable) {
        AtomicReference<T> returnItem = new AtomicReference();
        AtomicReference<Throwable> returnException = new AtomicReference();
        CountDownLatch latch = new CountDownLatch(1);
        Subscription subscription = observable.subscribe(new C13523(latch, returnException, returnItem));
        try {
            latch.await();
            if (returnException.get() == null) {
                return returnItem.get();
            }
            if (returnException.get() instanceof RuntimeException) {
                throw ((RuntimeException) returnException.get());
            }
            throw new RuntimeException((Throwable) returnException.get());
        } catch (InterruptedException e) {
            subscription.unsubscribe();
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for subscription to complete.", e);
        }
    }
}
