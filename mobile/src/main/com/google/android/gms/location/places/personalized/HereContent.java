package com.google.android.gms.location.places.personalized;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.util.List;

public class HereContent implements SafeParcelable {
    public static final zzb CREATOR;
    final int mVersionCode;
    private final String zzaHW;
    private final List<Action> zzaHX;

    public static final class Action implements SafeParcelable {
        public static final zza CREATOR;
        final int mVersionCode;
        private final String zzQg;
        private final String zzajf;

        static {
            CREATOR = new zza();
        }

        Action(int versionCode, String title, String uri) {
            this.mVersionCode = versionCode;
            this.zzajf = title;
            this.zzQg = uri;
        }

        public int describeContents() {
            zza com_google_android_gms_location_places_personalized_zza = CREATOR;
            return 0;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof Action)) {
                return false;
            }
            Action action = (Action) object;
            return zzw.equal(this.zzajf, action.zzajf) && zzw.equal(this.zzQg, action.zzQg);
        }

        public String getTitle() {
            return this.zzajf;
        }

        public String getUri() {
            return this.zzQg;
        }

        public int hashCode() {
            return zzw.hashCode(this.zzajf, this.zzQg);
        }

        public String toString() {
            return zzw.zzv(this).zzg(Keys.TITLE, this.zzajf).zzg("uri", this.zzQg).toString();
        }

        public void writeToParcel(Parcel parcel, int flags) {
            zza com_google_android_gms_location_places_personalized_zza = CREATOR;
            zza.zza(this, parcel, flags);
        }
    }

    static {
        CREATOR = new zzb();
    }

    HereContent(int versionCode, String data, List<Action> actions) {
        this.mVersionCode = versionCode;
        this.zzaHW = data;
        this.zzaHX = actions;
    }

    public int describeContents() {
        zzb com_google_android_gms_location_places_personalized_zzb = CREATOR;
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof HereContent)) {
            return false;
        }
        HereContent hereContent = (HereContent) object;
        return zzw.equal(this.zzaHW, hereContent.zzaHW) && zzw.equal(this.zzaHX, hereContent.zzaHX);
    }

    public List<Action> getActions() {
        return this.zzaHX;
    }

    public String getData() {
        return this.zzaHW;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzaHW, this.zzaHX);
    }

    public String toString() {
        return zzw.zzv(this).zzg(ModelColumns.DATA, this.zzaHW).zzg("actions", this.zzaHX).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzb com_google_android_gms_location_places_personalized_zzb = CREATOR;
        zzb.zza(this, parcel, flags);
    }
}
