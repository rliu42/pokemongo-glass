package com.google.android.gms.auth.api.signin.internal;

public class zzc {
    static int zzTo;
    private int zzTp;

    static {
        zzTo = 31;
    }

    public zzc() {
        this.zzTp = 1;
    }

    public zzc zzP(boolean z) {
        this.zzTp = (z ? 1 : 0) + (this.zzTp * zzTo);
        return this;
    }

    public zzc zzl(Object obj) {
        this.zzTp = (obj == null ? 0 : obj.hashCode()) + (this.zzTp * zzTo);
        return this;
    }

    public int zzmd() {
        return this.zzTp;
    }
}
