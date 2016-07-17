package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.concurrent.TimeUnit;

public final class PlaceRequest implements SafeParcelable {
    public static final Creator<PlaceRequest> CREATOR;
    static final long zzaGv;
    private final int mPriority;
    final int mVersionCode;
    private final long zzaEE;
    private final long zzaEj;
    private final PlaceFilter zzaGw;

    static {
        CREATOR = new zzk();
        zzaGv = TimeUnit.HOURS.toMillis(1);
    }

    public PlaceRequest(int versionCode, PlaceFilter filter, long interval, int priority, long expireAt) {
        this.mVersionCode = versionCode;
        this.zzaGw = filter;
        this.zzaEE = interval;
        this.mPriority = priority;
        this.zzaEj = expireAt;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PlaceRequest)) {
            return false;
        }
        PlaceRequest placeRequest = (PlaceRequest) object;
        return zzw.equal(this.zzaGw, placeRequest.zzaGw) && this.zzaEE == placeRequest.zzaEE && this.mPriority == placeRequest.mPriority && this.zzaEj == placeRequest.zzaEj;
    }

    public long getExpirationTime() {
        return this.zzaEj;
    }

    public long getInterval() {
        return this.zzaEE;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzaGw, Long.valueOf(this.zzaEE), Integer.valueOf(this.mPriority), Long.valueOf(this.zzaEj));
    }

    public String toString() {
        return zzw.zzv(this).zzg("filter", this.zzaGw).zzg("interval", Long.valueOf(this.zzaEE)).zzg("priority", Integer.valueOf(this.mPriority)).zzg("expireAt", Long.valueOf(this.zzaEj)).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzk.zza(this, parcel, flags);
    }

    public PlaceFilter zzwO() {
        return this.zzaGw;
    }
}
