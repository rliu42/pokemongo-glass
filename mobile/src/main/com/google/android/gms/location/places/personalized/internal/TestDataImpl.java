package com.google.android.gms.location.places.personalized.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.location.places.personalized.zzf;

public class TestDataImpl extends zzf implements SafeParcelable {
    public static final zza CREATOR;
    final int mVersionCode;
    private final String zzaIe;

    static {
        CREATOR = new zza();
    }

    TestDataImpl(int versionCode, String testName) {
        this.mVersionCode = versionCode;
        this.zzaIe = testName;
    }

    public int describeContents() {
        zza com_google_android_gms_location_places_personalized_internal_zza = CREATOR;
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TestDataImpl)) {
            return false;
        }
        return this.zzaIe.equals(((TestDataImpl) object).zzaIe);
    }

    public int hashCode() {
        return this.zzaIe.hashCode();
    }

    public String toString() {
        return zzw.zzv(this).zzg("testName", this.zzaIe).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zza com_google_android_gms_location_places_personalized_internal_zza = CREATOR;
        zza.zza(this, parcel, flags);
    }

    public String zzxv() {
        return this.zzaIe;
    }
}
