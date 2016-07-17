package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;

public class FACLConfig implements SafeParcelable {
    public static final zza CREATOR;
    final int version;
    boolean zzTA;
    boolean zzTB;
    boolean zzTC;
    boolean zzTx;
    String zzTy;
    boolean zzTz;

    static {
        CREATOR = new zza();
    }

    FACLConfig(int version, boolean isAllCirclesVisible, String visibleEdges, boolean isAllContactsVisible, boolean showCircles, boolean showContacts, boolean hasShowCircles) {
        this.version = version;
        this.zzTx = isAllCirclesVisible;
        this.zzTy = visibleEdges;
        this.zzTz = isAllContactsVisible;
        this.zzTA = showCircles;
        this.zzTB = showContacts;
        this.zzTC = hasShowCircles;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof FACLConfig)) {
            return false;
        }
        FACLConfig fACLConfig = (FACLConfig) o;
        return this.zzTx == fACLConfig.zzTx && TextUtils.equals(this.zzTy, fACLConfig.zzTy) && this.zzTz == fACLConfig.zzTz && this.zzTA == fACLConfig.zzTA && this.zzTB == fACLConfig.zzTB && this.zzTC == fACLConfig.zzTC;
    }

    public int hashCode() {
        return zzw.hashCode(Boolean.valueOf(this.zzTx), this.zzTy, Boolean.valueOf(this.zzTz), Boolean.valueOf(this.zzTA), Boolean.valueOf(this.zzTB), Boolean.valueOf(this.zzTC));
    }

    public void writeToParcel(Parcel dest, int flags) {
        zza.zza(this, dest, flags);
    }
}
