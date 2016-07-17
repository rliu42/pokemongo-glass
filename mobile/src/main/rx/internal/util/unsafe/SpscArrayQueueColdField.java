package rx.internal.util.unsafe;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;

/* compiled from: SpscArrayQueue */
abstract class SpscArrayQueueColdField<E> extends ConcurrentCircularArrayQueue<E> {
    private static final Integer MAX_LOOK_AHEAD_STEP;
    protected final int lookAheadStep;

    static {
        MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
    }

    public SpscArrayQueueColdField(int capacity) {
        super(capacity);
        this.lookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP.intValue());
    }
}
