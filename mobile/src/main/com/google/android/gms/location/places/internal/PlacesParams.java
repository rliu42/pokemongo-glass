package com.google.android.gms.location.places.internal;

import android.os.Parcel;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.Locale;

public class PlacesParams implements SafeParcelable {
    public static final zzt CREATOR;
    public static final PlacesParams zzaHQ;
    public final int versionCode;
    public final String zzaGG;
    public final String zzaHR;
    public final String zzaHS;
    public final String zzaHT;
    public final String zzaHU;
    public final int zzaHV;

    static {
        zzaHQ = new PlacesParams(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, Locale.getDefault(), null);
        CREATOR = new zzt();
    }

    public PlacesParams(int versionCode, String clientPackageName, String localeString, String accountName, String gCoreClientName, String chargedPackageName, int gCoreClientJarVersion) {
        this.versionCode = versionCode;
        this.zzaHR = clientPackageName;
        this.zzaHS = localeString;
        this.zzaHT = accountName;
        this.zzaGG = gCoreClientName;
        this.zzaHU = chargedPackageName;
        this.zzaHV = gCoreClientJarVersion;
    }

    public PlacesParams(String clientPackageName, Locale locale, String accountName) {
        this(1, clientPackageName, locale.toString(), accountName, null, null, GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    public PlacesParams(String clientPackageName, Locale locale, String accountName, String gCoreClientName, String chargedPackageName) {
        this(1, clientPackageName, locale.toString(), accountName, gCoreClientName, chargedPackageName, GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    public int describeContents() {
        zzt com_google_android_gms_location_places_internal_zzt = CREATOR;
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !(object instanceof PlacesParams)) {
            return false;
        }
        PlacesParams placesParams = (PlacesParams) object;
        return this.zzaHS.equals(placesParams.zzaHS) && this.zzaHR.equals(placesParams.zzaHR) && zzw.equal(this.zzaHT, placesParams.zzaHT) && zzw.equal(this.zzaGG, placesParams.zzaGG) && zzw.equal(this.zzaHU, placesParams.zzaHU);
    }

    public int hashCode() {
        return zzw.hashCode(this.zzaHR, this.zzaHS, this.zzaHT, this.zzaGG, this.zzaHU);
    }

    public String toString() {
        return zzw.zzv(this).zzg("clientPackageName", this.zzaHR).zzg("locale", this.zzaHS).zzg("accountName", this.zzaHT).zzg("gCoreClientName", this.zzaGG).zzg("chargedPackageName", this.zzaHU).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        zzt com_google_android_gms_location_places_internal_zzt = CREATOR;
        zzt.zza(this, out, flags);
    }
}
