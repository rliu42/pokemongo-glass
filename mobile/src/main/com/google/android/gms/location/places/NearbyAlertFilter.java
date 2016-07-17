package com.google.android.gms.location.places;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzw.zza;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class NearbyAlertFilter extends zza implements SafeParcelable {
    public static final zzd CREATOR;
    final int mVersionCode;
    final List<Integer> zzaFX;
    private final Set<Integer> zzaFY;
    final List<String> zzaFZ;
    final List<UserDataType> zzaGa;
    private final Set<String> zzaGb;
    private final Set<UserDataType> zzaGc;

    static {
        CREATOR = new zzd();
    }

    NearbyAlertFilter(int versionCode, List<String> placeIdsList, List<Integer> placeTypesList, List<UserDataType> requestedUserDataTypesList) {
        this.mVersionCode = versionCode;
        this.zzaFX = placeTypesList == null ? Collections.emptyList() : Collections.unmodifiableList(placeTypesList);
        this.zzaGa = requestedUserDataTypesList == null ? Collections.emptyList() : Collections.unmodifiableList(requestedUserDataTypesList);
        this.zzaFZ = placeIdsList == null ? Collections.emptyList() : Collections.unmodifiableList(placeIdsList);
        this.zzaFY = zza.zzs(this.zzaFX);
        this.zzaGc = zza.zzs(this.zzaGa);
        this.zzaGb = zza.zzs(this.zzaFZ);
    }

    @Deprecated
    public static NearbyAlertFilter zza(Collection<String> collection, Collection<Integer> collection2, Collection<UserDataType> collection3) {
        if ((collection != null && !collection.isEmpty()) || ((collection2 != null && !collection2.isEmpty()) || (collection3 != null && !collection3.isEmpty()))) {
            return new NearbyAlertFilter(0, zza.zzf(collection), zza.zzf(collection2), zza.zzf(collection3));
        }
        throw new IllegalArgumentException("NearbyAlertFilters must contain at least onePlaceId, PlaceType, or UserDataType to match results with.");
    }

    public int describeContents() {
        zzd com_google_android_gms_location_places_zzd = CREATOR;
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof NearbyAlertFilter)) {
            return false;
        }
        NearbyAlertFilter nearbyAlertFilter = (NearbyAlertFilter) object;
        return this.zzaFY.equals(nearbyAlertFilter.zzaFY) && this.zzaGc.equals(nearbyAlertFilter.zzaGc) && this.zzaGb.equals(nearbyAlertFilter.zzaGb);
    }

    public Set<String> getPlaceIds() {
        return this.zzaGb;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzaFY, this.zzaGc, this.zzaGb);
    }

    public String toString() {
        zza zzv = zzw.zzv(this);
        if (!this.zzaFY.isEmpty()) {
            zzv.zzg("types", this.zzaFY);
        }
        if (!this.zzaGb.isEmpty()) {
            zzv.zzg("placeIds", this.zzaGb);
        }
        if (!this.zzaGc.isEmpty()) {
            zzv.zzg("requestedUserDataTypes", this.zzaGc);
        }
        return zzv.toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzd com_google_android_gms_location_places_zzd = CREATOR;
        zzd.zza(this, parcel, flags);
    }
}
