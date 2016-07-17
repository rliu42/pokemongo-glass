package com.google.android.gms.location.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.Locale;
import spacemadness.com.lunarconsole.C1391R;

public class ParcelableGeofence implements SafeParcelable, Geofence {
    public static final zzo CREATOR;
    private final int mVersionCode;
    private final String zzBY;
    private final int zzaEi;
    private final short zzaEk;
    private final double zzaEl;
    private final double zzaEm;
    private final float zzaEn;
    private final int zzaEo;
    private final int zzaEp;
    private final long zzaFO;

    static {
        CREATOR = new zzo();
    }

    public ParcelableGeofence(int version, String requestId, int transitionTypes, short type, double latitude, double longitude, float radius, long expireAt, int notificationResponsiveness, int loiteringDelayMillis) {
        zzdx(requestId);
        zze(radius);
        zza(latitude, longitude);
        transitionTypes = zzhc(transitionTypes);
        this.mVersionCode = version;
        this.zzaEk = type;
        this.zzBY = requestId;
        this.zzaEl = latitude;
        this.zzaEm = longitude;
        this.zzaEn = radius;
        this.zzaFO = expireAt;
        this.zzaEi = transitionTypes;
        this.zzaEo = notificationResponsiveness;
        this.zzaEp = loiteringDelayMillis;
    }

    public ParcelableGeofence(String requestId, int transitionTypes, short type, double latitude, double longitude, float radius, long expireAt, int notificationResponsiveness, int loiteringDelayMillis) {
        this(1, requestId, transitionTypes, type, latitude, longitude, radius, expireAt, notificationResponsiveness, loiteringDelayMillis);
    }

    private static void zza(double d, double d2) {
        if (d > 90.0d || d < -90.0d) {
            throw new IllegalArgumentException("invalid latitude: " + d);
        } else if (d2 > 180.0d || d2 < -180.0d) {
            throw new IllegalArgumentException("invalid longitude: " + d2);
        }
    }

    private static void zzdx(String str) {
        if (str == null || str.length() > 100) {
            throw new IllegalArgumentException("requestId is null or too long: " + str);
        }
    }

    private static void zze(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("invalid radius: " + f);
        }
    }

    private static int zzhc(int i) {
        int i2 = i & 7;
        if (i2 != 0) {
            return i2;
        }
        throw new IllegalArgumentException("No supported transition specified: " + i);
    }

    private static String zzhd(int i) {
        switch (i) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                return "CIRCLE";
            default:
                return null;
        }
    }

    public static ParcelableGeofence zzn(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        ParcelableGeofence zzeJ = CREATOR.zzeJ(obtain);
        obtain.recycle();
        return zzeJ;
    }

    public int describeContents() {
        zzo com_google_android_gms_location_internal_zzo = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ParcelableGeofence)) {
            return false;
        }
        ParcelableGeofence parcelableGeofence = (ParcelableGeofence) obj;
        return this.zzaEn != parcelableGeofence.zzaEn ? false : this.zzaEl != parcelableGeofence.zzaEl ? false : this.zzaEm != parcelableGeofence.zzaEm ? false : this.zzaEk == parcelableGeofence.zzaEk;
    }

    public long getExpirationTime() {
        return this.zzaFO;
    }

    public double getLatitude() {
        return this.zzaEl;
    }

    public double getLongitude() {
        return this.zzaEm;
    }

    public int getNotificationResponsiveness() {
        return this.zzaEo;
    }

    public String getRequestId() {
        return this.zzBY;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.zzaEl);
        int i = ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31;
        long doubleToLongBits2 = Double.doubleToLongBits(this.zzaEm);
        return (((((((i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)))) * 31) + Float.floatToIntBits(this.zzaEn)) * 31) + this.zzaEk) * 31) + this.zzaEi;
    }

    public String toString() {
        return String.format(Locale.US, "Geofence[%s id:%s transitions:%d %.6f, %.6f %.0fm, resp=%ds, dwell=%dms, @%d]", new Object[]{zzhd(this.zzaEk), this.zzBY, Integer.valueOf(this.zzaEi), Double.valueOf(this.zzaEl), Double.valueOf(this.zzaEm), Float.valueOf(this.zzaEn), Integer.valueOf(this.zzaEo / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE), Integer.valueOf(this.zzaEp), Long.valueOf(this.zzaFO)});
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzo com_google_android_gms_location_internal_zzo = CREATOR;
        zzo.zza(this, parcel, flags);
    }

    public short zzwI() {
        return this.zzaEk;
    }

    public float zzwJ() {
        return this.zzaEn;
    }

    public int zzwK() {
        return this.zzaEi;
    }

    public int zzwL() {
        return this.zzaEp;
    }
}
