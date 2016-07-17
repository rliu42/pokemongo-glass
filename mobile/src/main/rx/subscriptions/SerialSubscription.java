package rx.subscriptions;

import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Subscription;

public final class SerialSubscription implements Subscription {
    static final AtomicReferenceFieldUpdater<SerialSubscription, State> STATE_UPDATER;
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

    public SerialSubscription() {
        this.state = new State(false, Subscriptions.empty());
    }

    static {
        STATE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(SerialSubscription.class, State.class, GameServices.STATE);
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
        oldState.subscription.unsubscribe();
    }

    public Subscription get() {
        return this.state.subscription;
    }
}
