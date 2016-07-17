package rx.observers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import rx.Notification;
import rx.Observer;
import rx.Subscriber;
import rx.annotations.Experimental;
import rx.exceptions.CompositeException;

public class TestSubscriber<T> extends Subscriber<T> {
    private static final Observer<Object> INERT;
    private final long initialRequest;
    private volatile Thread lastSeenThread;
    private final CountDownLatch latch;
    private final TestObserver<T> testObserver;

    /* renamed from: rx.observers.TestSubscriber.1 */
    static class C13651 implements Observer<Object> {
        C13651() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Object t) {
        }
    }

    static {
        INERT = new C13651();
    }

    @Experimental
    public TestSubscriber(long initialRequest) {
        this(INERT, initialRequest);
    }

    @Experimental
    public TestSubscriber(Observer<T> delegate, long initialRequest) {
        this.latch = new CountDownLatch(1);
        if (delegate == null) {
            throw new NullPointerException();
        }
        this.testObserver = new TestObserver(delegate);
        this.initialRequest = initialRequest;
    }

    public TestSubscriber(Subscriber<T> delegate) {
        this(delegate, -1);
    }

    public TestSubscriber(Observer<T> delegate) {
        this(delegate, -1);
    }

    public TestSubscriber() {
        this(-1);
    }

    @Experimental
    public static <T> TestSubscriber<T> create() {
        return new TestSubscriber();
    }

    @Experimental
    public static <T> TestSubscriber<T> create(long initialRequest) {
        return new TestSubscriber(initialRequest);
    }

    @Experimental
    public static <T> TestSubscriber<T> create(Observer<T> delegate, long initialRequest) {
        return new TestSubscriber(delegate, initialRequest);
    }

    @Experimental
    public static <T> TestSubscriber<T> create(Subscriber<T> delegate) {
        return new TestSubscriber((Subscriber) delegate);
    }

    @Experimental
    public static <T> TestSubscriber<T> create(Observer<T> delegate) {
        return new TestSubscriber((Observer) delegate);
    }

    public void onStart() {
        if (this.initialRequest >= 0) {
            requestMore(this.initialRequest);
        }
    }

    public void onCompleted() {
        try {
            this.lastSeenThread = Thread.currentThread();
            this.testObserver.onCompleted();
        } finally {
            this.latch.countDown();
        }
    }

    public List<Notification<T>> getOnCompletedEvents() {
        return this.testObserver.getOnCompletedEvents();
    }

    public void onError(Throwable e) {
        try {
            this.lastSeenThread = Thread.currentThread();
            this.testObserver.onError(e);
        } finally {
            this.latch.countDown();
        }
    }

    public List<Throwable> getOnErrorEvents() {
        return this.testObserver.getOnErrorEvents();
    }

    public void onNext(T t) {
        this.lastSeenThread = Thread.currentThread();
        this.testObserver.onNext(t);
    }

    public void requestMore(long n) {
        request(n);
    }

    public List<T> getOnNextEvents() {
        return this.testObserver.getOnNextEvents();
    }

    public void assertReceivedOnNext(List<T> items) {
        this.testObserver.assertReceivedOnNext(items);
    }

    public void assertTerminalEvent() {
        this.testObserver.assertTerminalEvent();
    }

    public void assertUnsubscribed() {
        if (!isUnsubscribed()) {
            throw new AssertionError("Not unsubscribed.");
        }
    }

    public void assertNoErrors() {
        if (getOnErrorEvents().size() > 0) {
            throw new RuntimeException("Unexpected onError events: " + getOnErrorEvents().size(), (Throwable) getOnErrorEvents().get(0));
        }
    }

    public void awaitTerminalEvent() {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    public void awaitTerminalEvent(long timeout, TimeUnit unit) {
        try {
            this.latch.await(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    public void awaitTerminalEventAndUnsubscribeOnTimeout(long timeout, TimeUnit unit) {
        try {
            if (!this.latch.await(timeout, unit)) {
                unsubscribe();
            }
        } catch (InterruptedException e) {
            unsubscribe();
        }
    }

    public Thread getLastSeenThread() {
        return this.lastSeenThread;
    }

    @Experimental
    public void assertCompleted() {
        int s = this.testObserver.getOnCompletedEvents().size();
        if (s == 0) {
            throw new AssertionError("Not completed!");
        } else if (s > 1) {
            throw new AssertionError("Completed multiple times: " + s);
        }
    }

    @Experimental
    public void assertNotCompleted() {
        int s = this.testObserver.getOnCompletedEvents().size();
        if (s == 1) {
            throw new AssertionError("Completed!");
        } else if (s > 1) {
            throw new AssertionError("Completed multiple times: " + s);
        }
    }

    @Experimental
    public void assertError(Class<? extends Throwable> clazz) {
        List<Throwable> err = this.testObserver.getOnErrorEvents();
        if (err.size() == 0) {
            throw new AssertionError("No errors");
        } else if (err.size() > 1) {
            ae = new AssertionError("Multiple errors: " + err.size());
            ae.initCause(new CompositeException(err));
            throw ae;
        } else if (!clazz.isInstance(err.get(0))) {
            ae = new AssertionError("Exceptions differ; expected: " + clazz + ", actual: " + err.get(0));
            ae.initCause((Throwable) err.get(0));
            throw ae;
        }
    }

    @Experimental
    public void assertError(Throwable throwable) {
        List<Throwable> err = this.testObserver.getOnErrorEvents();
        if (err.size() == 0) {
            throw new AssertionError("No errors");
        } else if (err.size() > 1) {
            ae = new AssertionError("Multiple errors: " + err.size());
            ae.initCause(new CompositeException(err));
            throw ae;
        } else if (!throwable.equals(err.get(0))) {
            ae = new AssertionError("Exceptions differ; expected: " + throwable + ", actual: " + err.get(0));
            ae.initCause((Throwable) err.get(0));
            throw ae;
        }
    }

    @Experimental
    public void assertNoTerminalEvent() {
        List<Throwable> err = this.testObserver.getOnErrorEvents();
        int s = this.testObserver.getOnCompletedEvents().size();
        if (err.size() <= 0 && s <= 0) {
            return;
        }
        if (err.isEmpty()) {
            throw new AssertionError("Found " + err.size() + " errors and " + s + " completion events instead of none");
        } else if (err.size() == 1) {
            ae = new AssertionError("Found " + err.size() + " errors and " + s + " completion events instead of none");
            ae.initCause((Throwable) err.get(0));
            throw ae;
        } else {
            ae = new AssertionError("Found " + err.size() + " errors and " + s + " completion events instead of none");
            ae.initCause(new CompositeException(err));
            throw ae;
        }
    }

    @Experimental
    public void assertNoValues() {
        int s = this.testObserver.getOnNextEvents().size();
        if (s > 0) {
            throw new AssertionError("No onNext events expected yet some received: " + s);
        }
    }

    @Experimental
    public void assertValueCount(int count) {
        int s = this.testObserver.getOnNextEvents().size();
        if (s != count) {
            throw new AssertionError("Number of onNext events differ; expected: " + count + ", actual: " + s);
        }
    }

    @Experimental
    public void assertValues(T... values) {
        assertReceivedOnNext(Arrays.asList(values));
    }

    @Experimental
    public void assertValue(T value) {
        assertReceivedOnNext(Collections.singletonList(value));
    }
}
