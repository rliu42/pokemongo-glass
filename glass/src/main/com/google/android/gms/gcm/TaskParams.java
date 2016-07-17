package com.google.android.gms.gcm;

import android.os.Bundle;

public class TaskParams {
    private final Bundle extras;
    private final String tag;

    public TaskParams(String tag) {
        this(tag, null);
    }

    public TaskParams(String tag, Bundle extras) {
        this.tag = tag;
        this.extras = extras;
    }

    public Bundle getExtras() {
        return this.extras;
    }

    public String getTag() {
        return this.tag;
    }
}
