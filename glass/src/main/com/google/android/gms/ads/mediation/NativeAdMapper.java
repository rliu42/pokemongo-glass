package com.google.android.gms.ads.mediation;

import android.os.Bundle;
import android.view.View;

public abstract class NativeAdMapper {
    protected Bundle mExtras;
    protected boolean mOverrideClickHandling;
    protected boolean mOverrideImpressionRecording;

    public NativeAdMapper() {
        this.mExtras = new Bundle();
    }

    public final Bundle getExtras() {
        return this.mExtras;
    }

    public final boolean getOverrideClickHandling() {
        return this.mOverrideClickHandling;
    }

    public final boolean getOverrideImpressionRecording() {
        return this.mOverrideImpressionRecording;
    }

    public void handleClick(View view) {
    }

    public void recordImpression() {
    }

    public final void setExtras(Bundle extras) {
        this.mExtras = extras;
    }

    public final void setOverrideClickHandling(boolean overrideClickHandling) {
        this.mOverrideClickHandling = overrideClickHandling;
    }

    public final void setOverrideImpressionRecording(boolean overrideImpressionRecording) {
        this.mOverrideImpressionRecording = overrideImpressionRecording;
    }

    public void trackView(View view) {
    }
}
