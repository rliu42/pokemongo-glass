package com.google.android.gms.location.places.internal;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public final class PlaceImpl implements SafeParcelable, Place {
    public static final zzl CREATOR;
    private final String mName;
    final int mVersionCode;
    private final LatLng zzaFS;
    private final List<Integer> zzaFT;
    private final String zzaFU;
    private final Uri zzaFV;
    private Locale zzaHc;
    private final Bundle zzaHi;
    @Deprecated
    private final PlaceLocalization zzaHj;
    private final float zzaHk;
    private final LatLngBounds zzaHl;
    private final String zzaHm;
    private final boolean zzaHn;
    private final float zzaHo;
    private final int zzaHp;
    private final long zzaHq;
    private final List<Integer> zzaHr;
    private final String zzaHs;
    private final List<String> zzaHt;
    final boolean zzaHu;
    private final Map<Integer, String> zzaHv;
    private final TimeZone zzaHw;
    private zzp zzaHx;
    private final String zzapU;
    private final String zzwN;

    public static class zza {
        private String mName;
        private int mVersionCode;
        private LatLng zzaFS;
        private String zzaFU;
        private Uri zzaFV;
        private float zzaHk;
        private LatLngBounds zzaHl;
        private String zzaHm;
        private boolean zzaHn;
        private float zzaHo;
        private int zzaHp;
        private long zzaHq;
        private String zzaHs;
        private List<String> zzaHt;
        private boolean zzaHu;
        private Bundle zzaHy;
        private List<Integer> zzaHz;
        private String zzapU;
        private String zzwN;

        public zza() {
            this.mVersionCode = 0;
        }

        public zza zza(LatLng latLng) {
            this.zzaFS = latLng;
            return this;
        }

        public zza zza(LatLngBounds latLngBounds) {
            this.zzaHl = latLngBounds;
            return this;
        }

        public zza zzai(boolean z) {
            this.zzaHn = z;
            return this;
        }

        public zza zzaj(boolean z) {
            this.zzaHu = z;
            return this;
        }

        public zza zzdA(String str) {
            this.zzwN = str;
            return this;
        }

        public zza zzdB(String str) {
            this.mName = str;
            return this;
        }

        public zza zzdC(String str) {
            this.zzapU = str;
            return this;
        }

        public zza zzdD(String str) {
            this.zzaFU = str;
            return this;
        }

        public zza zzf(float f) {
            this.zzaHk = f;
            return this;
        }

        public zza zzg(float f) {
            this.zzaHo = f;
            return this;
        }

        public zza zzhs(int i) {
            this.zzaHp = i;
            return this;
        }

        public zza zzl(Uri uri) {
            this.zzaFV = uri;
            return this;
        }

        public zza zzt(List<Integer> list) {
            this.zzaHz = list;
            return this;
        }

        public zza zzu(List<String> list) {
            this.zzaHt = list;
            return this;
        }

        public PlaceImpl zzxn() {
            return new PlaceImpl(this.mVersionCode, this.zzwN, this.zzaHz, Collections.emptyList(), this.zzaHy, this.mName, this.zzapU, this.zzaFU, this.zzaHs, this.zzaHt, this.zzaFS, this.zzaHk, this.zzaHl, this.zzaHm, this.zzaFV, this.zzaHn, this.zzaHo, this.zzaHp, this.zzaHq, this.zzaHu, PlaceLocalization.zza(this.mName, this.zzapU, this.zzaFU, this.zzaHs, this.zzaHt));
        }
    }

    static {
        CREATOR = new zzl();
    }

    PlaceImpl(int versionCode, String id, List<Integer> placeTypes, List<Integer> typesDeprecated, Bundle addressComponents, String name, String address, String phoneNumber, String regularOpenHours, List<String> attributions, LatLng latlng, float levelNumber, LatLngBounds viewport, String timeZoneId, Uri websiteUri, boolean isPermanentlyClosed, float rating, int priceLevel, long timestampSecs, boolean isLoggingEnabled, PlaceLocalization localization) {
        this.mVersionCode = versionCode;
        this.zzwN = id;
        this.zzaFT = Collections.unmodifiableList(placeTypes);
        this.zzaHr = typesDeprecated;
        if (addressComponents == null) {
            addressComponents = new Bundle();
        }
        this.zzaHi = addressComponents;
        this.mName = name;
        this.zzapU = address;
        this.zzaFU = phoneNumber;
        this.zzaHs = regularOpenHours;
        if (attributions == null) {
            attributions = Collections.emptyList();
        }
        this.zzaHt = attributions;
        this.zzaFS = latlng;
        this.zzaHk = levelNumber;
        this.zzaHl = viewport;
        if (timeZoneId == null) {
            timeZoneId = "UTC";
        }
        this.zzaHm = timeZoneId;
        this.zzaFV = websiteUri;
        this.zzaHn = isPermanentlyClosed;
        this.zzaHo = rating;
        this.zzaHp = priceLevel;
        this.zzaHq = timestampSecs;
        this.zzaHv = Collections.unmodifiableMap(new HashMap());
        this.zzaHw = null;
        this.zzaHc = null;
        this.zzaHu = isLoggingEnabled;
        this.zzaHj = localization;
    }

    private void zzdz(String str) {
        if (this.zzaHu && this.zzaHx != null) {
            this.zzaHx.zzE(this.zzwN, str);
        }
    }

    public int describeContents() {
        zzl com_google_android_gms_location_places_internal_zzl = CREATOR;
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PlaceImpl)) {
            return false;
        }
        PlaceImpl placeImpl = (PlaceImpl) object;
        return this.zzwN.equals(placeImpl.zzwN) && zzw.equal(this.zzaHc, placeImpl.zzaHc) && this.zzaHq == placeImpl.zzaHq;
    }

    public /* synthetic */ Object freeze() {
        return zzxm();
    }

    public String getAddress() {
        zzdz("getAddress");
        return this.zzapU;
    }

    public String getId() {
        zzdz("getId");
        return this.zzwN;
    }

    public LatLng getLatLng() {
        zzdz("getLatLng");
        return this.zzaFS;
    }

    public Locale getLocale() {
        zzdz("getLocale");
        return this.zzaHc;
    }

    public String getName() {
        zzdz("getName");
        return this.mName;
    }

    public String getPhoneNumber() {
        zzdz("getPhoneNumber");
        return this.zzaFU;
    }

    public List<Integer> getPlaceTypes() {
        zzdz("getPlaceTypes");
        return this.zzaFT;
    }

    public int getPriceLevel() {
        zzdz("getPriceLevel");
        return this.zzaHp;
    }

    public float getRating() {
        zzdz("getRating");
        return this.zzaHo;
    }

    public LatLngBounds getViewport() {
        zzdz("getViewport");
        return this.zzaHl;
    }

    public Uri getWebsiteUri() {
        zzdz("getWebsiteUri");
        return this.zzaFV;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzwN, this.zzaHc, Long.valueOf(this.zzaHq));
    }

    public boolean isDataValid() {
        return true;
    }

    public void setLocale(Locale locale) {
        this.zzaHc = locale;
    }

    public String toString() {
        return zzw.zzv(this).zzg(TriggerIfContentAvailable.ID, this.zzwN).zzg("placeTypes", this.zzaFT).zzg("locale", this.zzaHc).zzg(Twitter.NAME, this.mName).zzg("address", this.zzapU).zzg("phoneNumber", this.zzaFU).zzg("latlng", this.zzaFS).zzg("viewport", this.zzaHl).zzg("websiteUri", this.zzaFV).zzg("isPermanentlyClosed", Boolean.valueOf(this.zzaHn)).zzg("priceLevel", Integer.valueOf(this.zzaHp)).zzg("timestampSecs", Long.valueOf(this.zzaHq)).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzl com_google_android_gms_location_places_internal_zzl = CREATOR;
        zzl.zza(this, parcel, flags);
    }

    public void zza(zzp com_google_android_gms_location_places_internal_zzp) {
        this.zzaHx = com_google_android_gms_location_places_internal_zzp;
    }

    public List<Integer> zzxd() {
        zzdz("getTypesDeprecated");
        return this.zzaHr;
    }

    public float zzxe() {
        zzdz("getLevelNumber");
        return this.zzaHk;
    }

    public String zzxf() {
        zzdz("getRegularOpenHours");
        return this.zzaHs;
    }

    public List<String> zzxg() {
        zzdz("getAttributions");
        return this.zzaHt;
    }

    public boolean zzxh() {
        zzdz("isPermanentlyClosed");
        return this.zzaHn;
    }

    public long zzxi() {
        return this.zzaHq;
    }

    public Bundle zzxj() {
        return this.zzaHi;
    }

    public String zzxk() {
        return this.zzaHm;
    }

    @Deprecated
    public PlaceLocalization zzxl() {
        zzdz("getLocalization");
        return this.zzaHj;
    }

    public Place zzxm() {
        return this;
    }
}
