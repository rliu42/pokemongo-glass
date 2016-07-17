package com.google.android.gms.location;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.zzc;

public final class LocationSettingsStates implements SafeParcelable {
    public static final Creator<LocationSettingsStates> CREATOR;
    private final int mVersionCode;
    private final boolean zzaEQ;
    private final boolean zzaER;
    private final boolean zzaES;
    private final boolean zzaET;
    private final boolean zzaEU;
    private final boolean zzaEV;
    private final boolean zzaEW;

    static {
        CREATOR = new zzh();
    }

    LocationSettingsStates(int version, boolean gpsUsable, boolean nlpUsable, boolean bleUsable, boolean gpsPresent, boolean nlpPresent, boolean blePresent, boolean userLocationReportingOn) {
        this.mVersionCode = version;
        this.zzaEQ = gpsUsable;
        this.zzaER = nlpUsable;
        this.zzaES = bleUsable;
        this.zzaET = gpsPresent;
        this.zzaEU = nlpPresent;
        this.zzaEV = blePresent;
        this.zzaEW = userLocationReportingOn;
    }

    public static LocationSettingsStates fromIntent(Intent intent) {
        return (LocationSettingsStates) zzc.zza(intent, "com.google.android.gms.location.LOCATION_SETTINGS_STATES", CREATOR);
    }

    public int describeContents() {
        return 0;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public boolean isBlePresent() {
        return this.zzaEV;
    }

    public boolean isBleUsable() {
        return this.zzaES;
    }

    public boolean isGpsPresent() {
        return this.zzaET;
    }

    public boolean isGpsUsable() {
        return this.zzaEQ;
    }

    public boolean isLocationPresent() {
        return this.zzaET || this.zzaEU;
    }

    public boolean isLocationUsable() {
        return this.zzaEQ || this.zzaER;
    }

    public boolean isNetworkLocationPresent() {
        return this.zzaEU;
    }

    public boolean isNetworkLocationUsable() {
        return this.zzaER;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzh.zza(this, dest, flags);
    }

    public boolean zzwA() {
        return this.zzaEW;
    }
}
