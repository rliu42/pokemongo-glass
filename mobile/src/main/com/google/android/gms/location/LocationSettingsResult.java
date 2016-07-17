package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class LocationSettingsResult implements Result, SafeParcelable {
    public static final Creator<LocationSettingsResult> CREATOR;
    private final int mVersionCode;
    private final Status zzSC;
    private final LocationSettingsStates zzaEP;

    static {
        CREATOR = new zzg();
    }

    LocationSettingsResult(int version, Status status, LocationSettingsStates states) {
        this.mVersionCode = version;
        this.zzSC = status;
        this.zzaEP = states;
    }

    public LocationSettingsResult(Status status) {
        this(1, status, null);
    }

    public int describeContents() {
        return 0;
    }

    public LocationSettingsStates getLocationSettingsStates() {
        return this.zzaEP;
    }

    public Status getStatus() {
        return this.zzSC;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzg.zza(this, dest, flags);
    }
}
