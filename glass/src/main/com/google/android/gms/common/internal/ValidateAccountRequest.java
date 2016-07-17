package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ValidateAccountRequest implements SafeParcelable {
    public static final Creator<ValidateAccountRequest> CREATOR;
    final int mVersionCode;
    private final String zzSb;
    final IBinder zzaeH;
    private final Scope[] zzaeI;
    private final int zzagu;
    private final Bundle zzagv;

    static {
        CREATOR = new zzad();
    }

    ValidateAccountRequest(int versionCode, int clientVersion, IBinder accountAccessorBinder, Scope[] scopes, Bundle extraArgs, String callingPackage) {
        this.mVersionCode = versionCode;
        this.zzagu = clientVersion;
        this.zzaeH = accountAccessorBinder;
        this.zzaeI = scopes;
        this.zzagv = extraArgs;
        this.zzSb = callingPackage;
    }

    public ValidateAccountRequest(zzp accountAccessor, Scope[] scopes, String callingPackage, Bundle extraArgs) {
        this(1, GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE, accountAccessor == null ? null : accountAccessor.asBinder(), scopes, extraArgs, callingPackage);
    }

    public int describeContents() {
        return 0;
    }

    public String getCallingPackage() {
        return this.zzSb;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzad.zza(this, dest, flags);
    }

    public int zzpu() {
        return this.zzagu;
    }

    public Scope[] zzpv() {
        return this.zzaeI;
    }

    public Bundle zzpw() {
        return this.zzagv;
    }
}
