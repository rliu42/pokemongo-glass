package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzx;

public class BooleanResult implements Result {
    private final Status zzSC;
    private final boolean zzaaE;

    public BooleanResult(Status status, boolean value) {
        this.zzSC = (Status) zzx.zzb((Object) status, (Object) "Status must not be null");
        this.zzaaE = value;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BooleanResult)) {
            return false;
        }
        BooleanResult booleanResult = (BooleanResult) obj;
        return this.zzSC.equals(booleanResult.zzSC) && this.zzaaE == booleanResult.zzaaE;
    }

    public Status getStatus() {
        return this.zzSC;
    }

    public boolean getValue() {
        return this.zzaaE;
    }

    public final int hashCode() {
        return (this.zzaaE ? 1 : 0) + ((this.zzSC.hashCode() + 527) * 31);
    }
}
