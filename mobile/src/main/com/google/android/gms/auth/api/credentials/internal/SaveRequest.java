package com.google.android.gms.auth.api.credentials.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class SaveRequest implements SafeParcelable {
    public static final Creator<SaveRequest> CREATOR;
    final int mVersionCode;
    private final Credential zzSD;

    static {
        CREATOR = new zzi();
    }

    SaveRequest(int version, Credential credential) {
        this.mVersionCode = version;
        this.zzSD = credential;
    }

    public SaveRequest(Credential credential) {
        this(1, credential);
    }

    public int describeContents() {
        return 0;
    }

    public Credential getCredential() {
        return this.zzSD;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzi.zza(this, out, flags);
    }
}
