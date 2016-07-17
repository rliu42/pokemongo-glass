package rx.internal.operators;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Subscriber;
import rx.subjects.Subject;

public final class OperatorReplay {

    /* renamed from: rx.internal.operators.OperatorReplay.1 */
    static class C12631 implements OnSubscribe<T> {
        final /* synthetic */ Observable val$observedOn;

        C12631(Observable observable) {
            this.val$observedOn = observable;
        }

        public void call(Subscriber<? super T> o) {
            OperatorReplay.subscriberOf(this.val$observedOn).call(o);
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay.2 */
    static class C12642 implements OnSubscribe<T> {
        final /* synthetic */ Observable val$target;

        C12642(Observable observable) {
            this.val$target = observable;
        }

        public void call(Subscriber<? super T> t1) {
            this.val$target.unsafeSubscribe(t1);
        }
    }

    public static final class SubjectWrapper<T> extends Subject<T, T> {
        final Subject<T, T> subject;

        public SubjectWrapper(OnSubscribe<T> func, Subject<T, T> subject) {
            super(func);
            this.subject = subject;
        }

        public void onNext(T args) {
            this.subject.onNext(args);
        }

        public void onError(Throwable e) {
            this.subject.onError(e);
        }

        public void onCompleted() {
            this.subject.onCompleted();
        }

        public boolean hasObservers() {
            return this.subject.hasObservers();
        }
    }

    private OperatorReplay() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Subject<T, T> createScheduledSubject(Subject<T, T> subject, Scheduler scheduler) {
        return new SubjectWrapper(new C12631(subject.observeOn(scheduler)), subject);
    }

    public static <T> OnSubscribe<T> subscriberOf(Observable<T> target) {
        return new C12642(target);
    }
}
