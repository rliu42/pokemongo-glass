package com.google.android.gms.location;

import android.content.Intent;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;

public final class LocationAvailability implements SafeParcelable {
    public static final LocationAvailabilityCreator CREATOR;
    private final int mVersionCode;
    int zzaEA;
    int zzaEB;
    long zzaEC;
    int zzaED;

    static {
        CREATOR = new LocationAvailabilityCreator();
    }

    LocationAvailability(int versionCode, int locationStatus, int cellStatus, int wifiStatus, long elapsedRealtimeNs) {
        this.mVersionCode = versionCode;
        this.zzaED = locationStatus;
        this.zzaEA = cellStatus;
        this.zzaEB = wifiStatus;
        this.zzaEC = elapsedRealtimeNs;
    }

    public static LocationAvailability extractLocationAvailability(Intent intent) {
        return !hasLocationAvailability(intent) ? null : (LocationAvailability) intent.getExtras().getParcelable("com.google.android.gms.location.EXTRA_LOCATION_AVAILABILITY");
    }

    public static boolean hasLocationAvailability(Intent intent) {
        return intent == null ? false : intent.hasExtra("com.google.android.gms.location.EXTRA_LOCATION_AVAILABILITY");
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (!(other instanceof LocationAvailability)) {
            return false;
        }
        LocationAvailability locationAvailability = (LocationAvailability) other;
        return this.zzaED == locationAvailability.zzaED && this.zzaEA == locationAvailability.zzaEA && this.zzaEB == locationAvailability.zzaEB && this.zzaEC == locationAvailability.zzaEC;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.zzaED), Integer.valueOf(this.zzaEA), Integer.valueOf(this.zzaEB), Long.valueOf(this.zzaEC));
    }

    public boolean isLocationAvailable() {
        return this.zzaED < LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
    }

    public String toString() {
        return "LocationAvailability[isLocationAvailable: " + isLocationAvailable() + "]";
    }

    public void writeToParcel(Parcel parcel, int flags) {
        LocationAvailabilityCreator.zza(this, parcel, flags);
    }
}
