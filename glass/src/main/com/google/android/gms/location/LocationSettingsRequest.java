package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class LocationSettingsRequest implements SafeParcelable {
    public static final Creator<LocationSettingsRequest> CREATOR;
    private final int mVersionCode;
    private final boolean zzaEL;
    private final boolean zzaEM;
    private final boolean zzaEN;
    private final List<LocationRequest> zzasK;

    public static final class Builder {
        private boolean zzaEL;
        private boolean zzaEM;
        private boolean zzaEN;
        private final ArrayList<LocationRequest> zzaEO;

        public Builder() {
            this.zzaEO = new ArrayList();
            this.zzaEL = false;
            this.zzaEM = false;
            this.zzaEN = false;
        }

        public Builder addAllLocationRequests(Collection<LocationRequest> requests) {
            this.zzaEO.addAll(requests);
            return this;
        }

        public Builder addLocationRequest(LocationRequest request) {
            this.zzaEO.add(request);
            return this;
        }

        public LocationSettingsRequest build() {
            return new LocationSettingsRequest(this.zzaEL, this.zzaEM, this.zzaEN, null);
        }

        public Builder setAlwaysShow(boolean show) {
            this.zzaEL = show;
            return this;
        }

        public Builder setNeedBle(boolean needBle) {
            this.zzaEM = needBle;
            return this;
        }
    }

    static {
        CREATOR = new zzf();
    }

    LocationSettingsRequest(int version, List<LocationRequest> locationRequests, boolean alwaysShow, boolean needBle, boolean optInUserLocationReporting) {
        this.mVersionCode = version;
        this.zzasK = locationRequests;
        this.zzaEL = alwaysShow;
        this.zzaEM = needBle;
        this.zzaEN = optInUserLocationReporting;
    }

    private LocationSettingsRequest(List<LocationRequest> locationRequests, boolean alwaysShow, boolean needBle, boolean optInUserLocationReporting) {
        this(2, (List) locationRequests, alwaysShow, needBle, optInUserLocationReporting);
    }

    public int describeContents() {
        return 0;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzf.zza(this, dest, flags);
    }

    public List<LocationRequest> zztd() {
        return Collections.unmodifiableList(this.zzasK);
    }

    public boolean zzwx() {
        return this.zzaEL;
    }

    public boolean zzwy() {
        return this.zzaEM;
    }

    public boolean zzwz() {
        return this.zzaEN;
    }
}
