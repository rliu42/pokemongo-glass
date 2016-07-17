package rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.observables.ConnectableObservable;
import rx.observers.Subscribers;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

public final class OperatorMulticast<T, R> extends ConnectableObservable<R> {
    final AtomicReference<Subject<? super T, ? extends R>> connectedSubject;
    final Object guard;
    private Subscription guardedSubscription;
    final Observable<? extends T> source;
    final Func0<? extends Subject<? super T, ? extends R>> subjectFactory;
    private Subscriber<T> subscription;
    final List<Subscriber<? super R>> waitingForConnect;

    /* renamed from: rx.internal.operators.OperatorMulticast.1 */
    class C12391 implements OnSubscribe<R> {
        final /* synthetic */ AtomicReference val$connectedSubject;
        final /* synthetic */ Object val$guard;
        final /* synthetic */ List val$waitingForConnect;

        C12391(Object obj, AtomicReference atomicReference, List list) {
            this.val$guard = obj;
            this.val$connectedSubject = atomicReference;
            this.val$waitingForConnect = list;
        }

        public void call(Subscriber<? super R> subscriber) {
            synchronized (this.val$guard) {
                if (this.val$connectedSubject.get() == null) {
                    this.val$waitingForConnect.add(subscriber);
                } else {
                    ((Subject) this.val$connectedSubject.get()).unsafeSubscribe(subscriber);
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorMulticast.2 */
    class C12402 implements Action0 {
        final /* synthetic */ AtomicReference val$gs;

        C12402(AtomicReference atomicReference) {
            this.val$gs = atomicReference;
        }

        public void call() {
            synchronized (OperatorMulticast.this.guard) {
                if (OperatorMulticast.this.guardedSubscription == this.val$gs.get()) {
                    Subscription s = OperatorMulticast.this.subscription;
                    OperatorMulticast.this.subscription = null;
                    OperatorMulticast.this.guardedSubscription = null;
                    OperatorMulticast.this.connectedSubject.set(null);
                    if (s != null) {
                        s.unsubscribe();
                        return;
                    }
                    return;
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorMulticast.3 */
    class C12413 extends Subscriber<R> {
        final /* synthetic */ Subscriber val$s;

        C12413(Subscriber x0, Subscriber subscriber) {
            this.val$s = subscriber;
            super(x0);
        }

        public void onNext(R t) {
            this.val$s.onNext(t);
        }

        public void onError(Throwable e) {
            this.val$s.onError(e);
        }

        public void onCompleted() {
            this.val$s.onCompleted();
        }
    }

    public OperatorMulticast(Observable<? extends T> source, Func0<? extends Subject<? super T, ? extends R>> subjectFactory) {
        this(new Object(), new AtomicReference(), new ArrayList(), source, subjectFactory);
    }

    private OperatorMulticast(Object guard, AtomicReference<Subject<? super T, ? extends R>> connectedSubject, List<Subscriber<? super R>> waitingForConnect, Observable<? extends T> source, Func0<? extends Subject<? super T, ? extends R>> subjectFactory) {
        super(new C12391(guard, connectedSubject, waitingForConnect));
        this.guard = guard;
        this.connectedSubject = connectedSubject;
        this.waitingForConnect = waitingForConnect;
        this.source = source;
        this.subjectFactory = subjectFactory;
    }

    public void connect(Action1<? super Subscription> connection) {
        synchronized (this.guard) {
            if (this.subscription != null) {
                connection.call(this.guardedSubscription);
                return;
            }
            Subject<? super T, ? extends R> subject = (Subject) this.subjectFactory.call();
            this.subscription = Subscribers.from(subject);
            AtomicReference<Subscription> gs = new AtomicReference();
            gs.set(Subscriptions.create(new C12402(gs)));
            this.guardedSubscription = (Subscription) gs.get();
            for (Subscriber<? super R> s : this.waitingForConnect) {
                subject.unsafeSubscribe(new C12413(s, s));
            }
            this.waitingForConnect.clear();
            this.connectedSubject.set(subject);
            connection.call(this.guardedSubscription);
            synchronized (this.guard) {
                Subscriber sub = this.subscription;
            }
            if (sub != null) {
                this.source.subscribe(sub);
            }
        }
    }
}
