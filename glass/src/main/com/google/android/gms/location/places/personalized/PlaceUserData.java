package com.google.android.gms.location.places.personalized;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.location.places.personalized.internal.TestDataImpl;
import java.util.List;

public class PlaceUserData implements SafeParcelable {
    public static final zze CREATOR;
    final int mVersionCode;
    private final String zzRs;
    private final String zzaGt;
    private final List<TestDataImpl> zzaIb;
    private final List<PlaceAlias> zzaIc;
    private final List<HereContent> zzaId;

    static {
        CREATOR = new zze();
    }

    PlaceUserData(int versionCode, String accountName, String placeId, List<TestDataImpl> testDataImpls, List<PlaceAlias> placeAliases, List<HereContent> hereContents) {
        this.mVersionCode = versionCode;
        this.zzRs = accountName;
        this.zzaGt = placeId;
        this.zzaIb = testDataImpls;
        this.zzaIc = placeAliases;
        this.zzaId = hereContents;
    }

    public int describeContents() {
        zze com_google_android_gms_location_places_personalized_zze = CREATOR;
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PlaceUserData)) {
            return false;
        }
        PlaceUserData placeUserData = (PlaceUserData) object;
        return this.zzRs.equals(placeUserData.zzRs) && this.zzaGt.equals(placeUserData.zzaGt) && this.zzaIb.equals(placeUserData.zzaIb) && this.zzaIc.equals(placeUserData.zzaIc) && this.zzaId.equals(placeUserData.zzaId);
    }

    public String getPlaceId() {
        return this.zzaGt;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzRs, this.zzaGt, this.zzaIb, this.zzaIc, this.zzaId);
    }

    public String toString() {
        return zzw.zzv(this).zzg("accountName", this.zzRs).zzg("placeId", this.zzaGt).zzg("testDataImpls", this.zzaIb).zzg("placeAliases", this.zzaIc).zzg("hereContents", this.zzaId).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zze com_google_android_gms_location_places_personalized_zze = CREATOR;
        zze.zza(this, parcel, flags);
    }

    public String zzxr() {
        return this.zzRs;
    }

    public List<PlaceAlias> zzxs() {
        return this.zzaIc;
    }

    public List<HereContent> zzxt() {
        return this.zzaId;
    }

    public List<TestDataImpl> zzxu() {
        return this.zzaIb;
    }
}
