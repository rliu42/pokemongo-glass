package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public final class Scope implements SafeParcelable {
    public static final Creator<Scope> CREATOR;
    final int mVersionCode;
    private final String zzaba;

    static {
        CREATOR = new zzc();
    }

    Scope(int versionCode, String scopeUri) {
        zzx.zzh(scopeUri, "scopeUri must not be null or empty");
        this.mVersionCode = versionCode;
        this.zzaba = scopeUri;
    }

    public Scope(String scopeUri) {
        this(1, scopeUri);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        return this == o ? true : !(o instanceof Scope) ? false : this.zzaba.equals(((Scope) o).zzaba);
    }

    public int hashCode() {
        return this.zzaba.hashCode();
    }

    public String toString() {
        return this.zzaba;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzc.zza(this, dest, flags);
    }

    public String zznG() {
        return this.zzaba;
    }
}
