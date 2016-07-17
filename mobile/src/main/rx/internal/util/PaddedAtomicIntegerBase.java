package rx.internal.util;

import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

abstract class PaddedAtomicIntegerBase extends FrontPadding {
    private static final long serialVersionUID = 6513142711280243198L;
    private static final AtomicIntegerFieldUpdater<PaddedAtomicIntegerBase> updater;
    private volatile int value;

    PaddedAtomicIntegerBase() {
    }

    static {
        updater = AtomicIntegerFieldUpdater.newUpdater(PaddedAtomicIntegerBase.class, GameServices.SCORE_VALUE);
    }

    public final int get() {
        return this.value;
    }

    public final void set(int newValue) {
        this.value = newValue;
    }

    public final void lazySet(int newValue) {
        updater.lazySet(this, newValue);
    }

    public final boolean compareAndSet(int expect, int update) {
        return updater.compareAndSet(this, expect, update);
    }

    public final boolean weakCompareAndSet(int expect, int update) {
        return updater.weakCompareAndSet(this, expect, update);
    }

    public final int getAndSet(int newValue) {
        return updater.getAndSet(this, this.value);
    }

    public final int getAndAdd(int delta) {
        return updater.getAndAdd(this, delta);
    }

    public final int incrementAndGet() {
        return updater.incrementAndGet(this);
    }

    public final int decrementAndGet() {
        return updater.decrementAndGet(this);
    }

    public final int getAndIncrement() {
        return updater.getAndIncrement(this);
    }

    public final int getAndDecrement() {
        return updater.getAndDecrement(this);
    }

    public final int addAndGet(int delta) {
        return updater.addAndGet(this, delta);
    }

    public String toString() {
        return String.valueOf(get());
    }
}
