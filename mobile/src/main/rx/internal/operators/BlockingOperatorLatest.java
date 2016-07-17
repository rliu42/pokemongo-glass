package rx.internal.operators;

import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public final class BlockingOperatorLatest {

    /* renamed from: rx.internal.operators.BlockingOperatorLatest.1 */
    static class C11511 implements Iterable<T> {
        final /* synthetic */ Observable val$source;

        C11511(Observable observable) {
            this.val$source = observable;
        }

        public Iterator<T> iterator() {
            Subscriber lio = new LatestObserverIterator();
            this.val$source.materialize().subscribe(lio);
            return lio;
        }
    }

    static final class LatestObserverIterator<T> extends Subscriber<Notification<? extends T>> implements Iterator<T> {
        static final AtomicReferenceFieldUpdater<LatestObserverIterator, Notification> REFERENCE_UPDATER;
        Notification<? extends T> iNotif;
        final Semaphore notify;
        volatile Notification<? extends T> value;

        LatestObserverIterator() {
            this.notify = new Semaphore(0);
        }

        static {
            REFERENCE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(LatestObserverIterator.class, Notification.class, GameServices.SCORE_VALUE);
        }

        public void onNext(Notification<? extends T> args) {
            if (REFERENCE_UPDATER.getAndSet(this, args) == null) {
                this.notify.release();
            }
        }

        public void onError(Throwable e) {
        }

        public void onCompleted() {
        }

        public boolean hasNext() {
            if (this.iNotif == null || !this.iNotif.isOnError()) {
                if ((this.iNotif == null || !this.iNotif.isOnCompleted()) && this.iNotif == null) {
                    try {
                        this.notify.acquire();
                        this.iNotif = (Notification) REFERENCE_UPDATER.getAndSet(this, null);
                        if (this.iNotif.isOnError()) {
                            throw Exceptions.propagate(this.iNotif.getThrowable());
                        }
                    } catch (InterruptedException ex) {
                        unsubscribe();
                        Thread.currentThread().interrupt();
                        this.iNotif = Notification.createOnError(ex);
                        throw Exceptions.propagate(ex);
                    }
                }
                return !this.iNotif.isOnCompleted();
            } else {
                throw Exceptions.propagate(this.iNotif.getThrowable());
            }
        }

        public T next() {
            if (hasNext() && this.iNotif.isOnNext()) {
                T v = this.iNotif.getValue();
                this.iNotif = null;
                return v;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException("Read-only iterator.");
        }
    }

    private BlockingOperatorLatest() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> latest(Observable<? extends T> source) {
        return new C11511(source);
    }
}
