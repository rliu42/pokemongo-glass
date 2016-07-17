package com.google.android.gms.location.places.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;

public class PlaceLikelihoodEntity implements SafeParcelable, PlaceLikelihood {
    public static final Creator<PlaceLikelihoodEntity> CREATOR;
    final int mVersionCode;
    final PlaceImpl zzaHA;
    final float zzaHB;

    static {
        CREATOR = new zzm();
    }

    PlaceLikelihoodEntity(int versionCode, PlaceImpl place, float likelihood) {
        this.mVersionCode = versionCode;
        this.zzaHA = place;
        this.zzaHB = likelihood;
    }

    public static PlaceLikelihoodEntity zza(PlaceImpl placeImpl, float f) {
        return new PlaceLikelihoodEntity(0, (PlaceImpl) zzx.zzw(placeImpl), f);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PlaceLikelihoodEntity)) {
            return false;
        }
        PlaceLikelihoodEntity placeLikelihoodEntity = (PlaceLikelihoodEntity) object;
        return this.zzaHA.equals(placeLikelihoodEntity.zzaHA) && this.zzaHB == placeLikelihoodEntity.zzaHB;
    }

    public /* synthetic */ Object freeze() {
        return zzxo();
    }

    public float getLikelihood() {
        return this.zzaHB;
    }

    public Place getPlace() {
        return this.zzaHA;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzaHA, Float.valueOf(this.zzaHB));
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return zzw.zzv(this).zzg("place", this.zzaHA).zzg("likelihood", Float.valueOf(this.zzaHB)).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzm.zza(this, parcel, flags);
    }

    public PlaceLikelihood zzxo() {
        return this;
    }
}
