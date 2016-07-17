package rx.subscriptions;

import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Subscription;

public final class MultipleAssignmentSubscription implements Subscription {
    static final AtomicReferenceFieldUpdater<MultipleAssignmentSubscription, State> STATE_UPDATER;
    volatile State state;

    private static final class State {
        final boolean isUnsubscribed;
        final Subscription subscription;

        State(boolean u, Subscription s) {
            this.isUnsubscribed = u;
            this.subscription = s;
        }

        State unsubscribe() {
            return new State(true, this.subscription);
        }

        State set(Subscription s) {
            return new State(this.isUnsubscribed, s);
        }
    }

    public MultipleAssignmentSubscription() {
        this.state = new State(false, Subscriptions.empty());
    }

    static {
        STATE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(MultipleAssignmentSubscription.class, State.class, GameServices.STATE);
    }

    public boolean isUnsubscribed() {
        return this.state.isUnsubscribed;
    }

    public void unsubscribe() {
        State oldState;
        do {
            oldState = this.state;
            if (!oldState.isUnsubscribed) {
            } else {
                return;
            }
        } while (!STATE_UPDATER.compareAndSet(this, oldState, oldState.unsubscribe()));
        oldState.subscription.unsubscribe();
    }

    public void set(Subscription s) {
        if (s == null) {
            throw new IllegalArgumentException("Subscription can not be null");
        }
        State oldState;
        do {
            oldState = this.state;
            if (oldState.isUnsubscribed) {
                s.unsubscribe();
                return;
            }
        } while (!STATE_UPDATER.compareAndSet(this, oldState, oldState.set(s)));
    }

    public Subscription get() {
        return this.state.subscription;
    }
}
