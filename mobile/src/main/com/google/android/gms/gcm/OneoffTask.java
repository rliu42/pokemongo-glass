package com.google.android.gms.gcm;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;

public class OneoffTask extends Task {
    public static final Creator<OneoffTask> CREATOR;
    private final long zzaCC;
    private final long zzaCD;

    /* renamed from: com.google.android.gms.gcm.OneoffTask.1 */
    static class C04401 implements Creator<OneoffTask> {
        C04401() {
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return zzes(x0);
        }

        public /* synthetic */ Object[] newArray(int x0) {
            return zzgC(x0);
        }

        public OneoffTask zzes(Parcel parcel) {
            return new OneoffTask(null);
        }

        public OneoffTask[] zzgC(int i) {
            return new OneoffTask[i];
        }
    }

    public static class Builder extends com.google.android.gms.gcm.Task.Builder {
        private long zzaCE;
        private long zzaCF;

        public Builder() {
            this.zzaCE = -1;
            this.zzaCF = -1;
            this.isPersisted = false;
        }

        public OneoffTask build() {
            checkConditions();
            return new OneoffTask();
        }

        protected void checkConditions() {
            super.checkConditions();
            if (this.zzaCE == -1 || this.zzaCF == -1) {
                throw new IllegalArgumentException("Must specify an execution window using setExecutionWindow.");
            } else if (this.zzaCE >= this.zzaCF) {
                throw new IllegalArgumentException("Window start must be shorter than window end.");
            }
        }

        public Builder setExecutionWindow(long windowStartDelaySeconds, long windowEndDelaySeconds) {
            this.zzaCE = windowStartDelaySeconds;
            this.zzaCF = windowEndDelaySeconds;
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.extras = extras;
            return this;
        }

        public Builder setPersisted(boolean isPersisted) {
            this.isPersisted = isPersisted;
            return this;
        }

        public Builder setRequiredNetwork(int requiredNetworkState) {
            this.requiredNetworkState = requiredNetworkState;
            return this;
        }

        public Builder setRequiresCharging(boolean requiresCharging) {
            this.requiresCharging = requiresCharging;
            return this;
        }

        public Builder setService(Class<? extends GcmTaskService> gcmTaskService) {
            this.gcmTaskService = gcmTaskService.getName();
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setUpdateCurrent(boolean updateCurrent) {
            this.updateCurrent = updateCurrent;
            return this;
        }
    }

    static {
        CREATOR = new C04401();
    }

    @Deprecated
    private OneoffTask(Parcel in) {
        super(in);
        this.zzaCC = in.readLong();
        this.zzaCD = in.readLong();
    }

    private OneoffTask(Builder builder) {
        super((com.google.android.gms.gcm.Task.Builder) builder);
        this.zzaCC = builder.zzaCE;
        this.zzaCD = builder.zzaCF;
    }

    public long getWindowEnd() {
        return this.zzaCD;
    }

    public long getWindowStart() {
        return this.zzaCC;
    }

    public void toBundle(Bundle bundle) {
        super.toBundle(bundle);
        bundle.putLong("window_start", this.zzaCC);
        bundle.putLong("window_end", this.zzaCD);
    }

    public String toString() {
        return super.toString() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "windowStart=" + getWindowStart() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "windowEnd=" + getWindowEnd();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeLong(this.zzaCC);
        parcel.writeLong(this.zzaCD);
    }
}
