package com.google.android.gms.location.places;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class PlaceFilter extends zza implements SafeParcelable {
    public static final zzg CREATOR;
    final int mVersionCode;
    final List<Integer> zzaFX;
    private final Set<Integer> zzaFY;
    final List<String> zzaFZ;
    final List<UserDataType> zzaGa;
    private final Set<String> zzaGb;
    private final Set<UserDataType> zzaGc;
    final boolean zzaGl;

    @Deprecated
    public static final class zza {
        private boolean zzaGl;
        private Collection<Integer> zzaGm;
        private Collection<UserDataType> zzaGn;
        private String[] zzaGo;

        private zza() {
            this.zzaGm = null;
            this.zzaGl = false;
            this.zzaGn = null;
            this.zzaGo = null;
        }

        public PlaceFilter zzwU() {
            Collection collection = null;
            Collection arrayList = this.zzaGm != null ? new ArrayList(this.zzaGm) : null;
            Collection arrayList2 = this.zzaGn != null ? new ArrayList(this.zzaGn) : null;
            if (this.zzaGo != null) {
                collection = Arrays.asList(this.zzaGo);
            }
            return new PlaceFilter(arrayList, this.zzaGl, collection, arrayList2);
        }
    }

    static {
        CREATOR = new zzg();
    }

    public PlaceFilter() {
        this(false, null);
    }

    PlaceFilter(int versionCode, List<Integer> placeTypesList, boolean requireOpenNow, List<String> placeIdsList, List<UserDataType> requestedUserDataTypesList) {
        this.mVersionCode = versionCode;
        this.zzaFX = placeTypesList == null ? Collections.emptyList() : Collections.unmodifiableList(placeTypesList);
        this.zzaGl = requireOpenNow;
        this.zzaGa = requestedUserDataTypesList == null ? Collections.emptyList() : Collections.unmodifiableList(requestedUserDataTypesList);
        this.zzaFZ = placeIdsList == null ? Collections.emptyList() : Collections.unmodifiableList(placeIdsList);
        this.zzaFY = zza.zzs(this.zzaFX);
        this.zzaGc = zza.zzs(this.zzaGa);
        this.zzaGb = zza.zzs(this.zzaFZ);
    }

    public PlaceFilter(Collection<Integer> restrictToPlaceTypes, boolean requireOpenNow, Collection<String> restrictToPlaceIds, Collection<UserDataType> requestedUserDataTypes) {
        this(0, zza.zzf(restrictToPlaceTypes), requireOpenNow, zza.zzf(restrictToPlaceIds), zza.zzf(requestedUserDataTypes));
    }

    public PlaceFilter(boolean requireOpenNow, Collection<String> restrictToPlaceIds) {
        this(null, requireOpenNow, restrictToPlaceIds, null);
    }

    @Deprecated
    public static PlaceFilter zzwT() {
        return new zza().zzwU();
    }

    public int describeContents() {
        zzg com_google_android_gms_location_places_zzg = CREATOR;
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PlaceFilter)) {
            return false;
        }
        PlaceFilter placeFilter = (PlaceFilter) object;
        return this.zzaFY.equals(placeFilter.zzaFY) && this.zzaGl == placeFilter.zzaGl && this.zzaGc.equals(placeFilter.zzaGc) && this.zzaGb.equals(placeFilter.zzaGb);
    }

    public Set<String> getPlaceIds() {
        return this.zzaGb;
    }

    public Set<Integer> getPlaceTypes() {
        return this.zzaFY;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzaFY, Boolean.valueOf(this.zzaGl), this.zzaGc, this.zzaGb);
    }

    public boolean isRestrictedToPlacesOpenNow() {
        return this.zzaGl;
    }

    public String toString() {
        com.google.android.gms.common.internal.zzw.zza zzv = zzw.zzv(this);
        if (!this.zzaFY.isEmpty()) {
            zzv.zzg("types", this.zzaFY);
        }
        zzv.zzg("requireOpenNow", Boolean.valueOf(this.zzaGl));
        if (!this.zzaGb.isEmpty()) {
            zzv.zzg("placeIds", this.zzaGb);
        }
        if (!this.zzaGc.isEmpty()) {
            zzv.zzg("requestedUserDataTypes", this.zzaGc);
        }
        return zzv.toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzg com_google_android_gms_location_places_zzg = CREATOR;
        zzg.zza(this, parcel, flags);
    }

    public Set<UserDataType> zzwS() {
        return this.zzaGc;
    }
}
