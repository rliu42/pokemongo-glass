package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class IdToken implements SafeParcelable {
    public static final Creator<IdToken> CREATOR;
    final int mVersionCode;
    private final String zzSk;
    private final String zzSs;

    static {
        CREATOR = new zzd();
    }

    IdToken(int version, String accountType, String idToken) {
        this.mVersionCode = version;
        this.zzSk = accountType;
        this.zzSs = idToken;
    }

    public IdToken(String accountType, String idToken) {
        this(1, accountType, idToken);
    }

    public int describeContents() {
        return 0;
    }

    public String getAccountType() {
        return this.zzSk;
    }

    public String getIdToken() {
        return this.zzSs;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzd.zza(this, out, flags);
    }
}
