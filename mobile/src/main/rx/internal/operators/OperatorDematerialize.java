package rx.internal.operators;

import com.nianticproject.holoholo.sfida.SfidaMessage;
import rx.Notification;
import rx.Notification.Kind;
import rx.Observable.Operator;
import rx.Subscriber;
import spacemadness.com.lunarconsole.C1391R;

public final class OperatorDematerialize<T> implements Operator<T, Notification<T>> {

    /* renamed from: rx.internal.operators.OperatorDematerialize.1 */
    class C12161 extends Subscriber<Notification<T>> {
        boolean terminated;
        final /* synthetic */ Subscriber val$child;

        C12161(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
        }

        public void onNext(Notification<T> t) {
            switch (C12172.$SwitchMap$rx$Notification$Kind[t.getKind().ordinal()]) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    if (!this.terminated) {
                        this.val$child.onNext(t.getValue());
                    }
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    onError(t.getThrowable());
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    onCompleted();
                default:
            }
        }

        public void onError(Throwable e) {
            if (!this.terminated) {
                this.terminated = true;
                this.val$child.onError(e);
            }
        }

        public void onCompleted() {
            if (!this.terminated) {
                this.terminated = true;
                this.val$child.onCompleted();
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorDematerialize.2 */
    static /* synthetic */ class C12172 {
        static final /* synthetic */ int[] $SwitchMap$rx$Notification$Kind;

        static {
            $SwitchMap$rx$Notification$Kind = new int[Kind.values().length];
            try {
                $SwitchMap$rx$Notification$Kind[Kind.OnNext.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$rx$Notification$Kind[Kind.OnError.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$rx$Notification$Kind[Kind.OnCompleted.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private static final class Holder {
        static final OperatorDematerialize<Object> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorDematerialize();
        }
    }

    public static OperatorDematerialize instance() {
        return Holder.INSTANCE;
    }

    private OperatorDematerialize() {
    }

    public Subscriber<? super Notification<T>> call(Subscriber<? super T> child) {
        return new C12161(child, child);
    }
}
