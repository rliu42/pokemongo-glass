package rx.subscriptions;

import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Subscription;

public final class RefCountSubscription implements Subscription {
    static final State EMPTY_STATE;
    static final AtomicReferenceFieldUpdater<RefCountSubscription, State> STATE_UPDATER;
    private final Subscription actual;
    volatile State state;

    private static final class InnerSubscription implements Subscription {
        static final AtomicIntegerFieldUpdater<InnerSubscription> INNER_DONE_UPDATER;
        volatile int innerDone;
        final RefCountSubscription parent;

        static {
            INNER_DONE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(InnerSubscription.class, "innerDone");
        }

        public InnerSubscription(RefCountSubscription parent) {
            this.parent = parent;
        }

        public void unsubscribe() {
            if (INNER_DONE_UPDATER.compareAndSet(this, 0, 1)) {
                this.parent.unsubscribeAChild();
            }
        }

        public boolean isUnsubscribed() {
            return this.innerDone != 0;
        }
    }

    private static final class State {
        final int children;
        final boolean isUnsubscribed;

        State(boolean u, int c) {
            this.isUnsubscribed = u;
            this.children = c;
        }

        State addChild() {
            return new State(this.isUnsubscribed, this.children + 1);
        }

        State removeChild() {
            return new State(this.isUnsubscribed, this.children - 1);
        }

        State unsubscribe() {
            return new State(true, this.children);
        }
    }

    static {
        EMPTY_STATE = new State(false, 0);
        STATE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(RefCountSubscription.class, State.class, GameServices.STATE);
    }

    public RefCountSubscription(Subscription s) {
        this.state = EMPTY_STATE;
        if (s == null) {
            throw new IllegalArgumentException("s");
        }
        this.actual = s;
    }

    public Subscription get() {
        State oldState;
        do {
            oldState = this.state;
            if (oldState.isUnsubscribed) {
                return Subscriptions.unsubscribed();
            }
        } while (!STATE_UPDATER.compareAndSet(this, oldState, oldState.addChild()));
        return new InnerSubscription(this);
    }

    public boolean isUnsubscribed() {
        return this.state.isUnsubscribed;
    }

    public void unsubscribe() {
        State newState;
        State oldState;
        do {
            oldState = this.state;
            if (!oldState.isUnsubscribed) {
                newState = oldState.unsubscribe();
            } else {
                return;
            }
        } while (!STATE_UPDATER.compareAndSet(this, oldState, newState));
        unsubscribeActualIfApplicable(newState);
    }

    private void unsubscribeActualIfApplicable(State state) {
        if (state.isUnsubscribed && state.children == 0) {
            this.actual.unsubscribe();
        }
    }

    void unsubscribeAChild() {
        State newState;
        State oldState;
        do {
            oldState = this.state;
            newState = oldState.removeChild();
        } while (!STATE_UPDATER.compareAndSet(this, oldState, newState));
        unsubscribeActualIfApplicable(newState);
    }
}
