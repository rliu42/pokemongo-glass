package com.google.android.gms.location;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.Comparator;

public class DetectedActivity implements SafeParcelable {
    public static final DetectedActivityCreator CREATOR;
    public static final int IN_VEHICLE = 0;
    public static final int ON_BICYCLE = 1;
    public static final int ON_FOOT = 2;
    public static final int RUNNING = 8;
    public static final int STILL = 3;
    public static final int TILTING = 5;
    public static final int UNKNOWN = 4;
    public static final int WALKING = 7;
    public static final Comparator<DetectedActivity> zzaEf;
    private final int mVersionCode;
    int zzaEg;
    int zzaEh;

    /* renamed from: com.google.android.gms.location.DetectedActivity.1 */
    static class C06261 implements Comparator<DetectedActivity> {
        C06261() {
        }

        public /* synthetic */ int compare(Object x0, Object x1) {
            return zza((DetectedActivity) x0, (DetectedActivity) x1);
        }

        public int zza(DetectedActivity detectedActivity, DetectedActivity detectedActivity2) {
            int compareTo = Integer.valueOf(detectedActivity2.getConfidence()).compareTo(Integer.valueOf(detectedActivity.getConfidence()));
            return compareTo == 0 ? Integer.valueOf(detectedActivity.getType()).compareTo(Integer.valueOf(detectedActivity2.getType())) : compareTo;
        }
    }

    static {
        zzaEf = new C06261();
        CREATOR = new DetectedActivityCreator();
    }

    public DetectedActivity(int activityType, int confidence) {
        this.mVersionCode = ON_BICYCLE;
        this.zzaEg = activityType;
        this.zzaEh = confidence;
    }

    public DetectedActivity(int versionCode, int activityType, int confidence) {
        this.mVersionCode = versionCode;
        this.zzaEg = activityType;
        this.zzaEh = confidence;
    }

    private int zzgK(int i) {
        return i > 15 ? UNKNOWN : i;
    }

    public static String zzgL(int i) {
        switch (i) {
            case IN_VEHICLE /*0*/:
                return "IN_VEHICLE";
            case ON_BICYCLE /*1*/:
                return "ON_BICYCLE";
            case ON_FOOT /*2*/:
                return "ON_FOOT";
            case STILL /*3*/:
                return "STILL";
            case UNKNOWN /*4*/:
                return "UNKNOWN";
            case TILTING /*5*/:
                return "TILTING";
            case WALKING /*7*/:
                return "WALKING";
            case RUNNING /*8*/:
                return "RUNNING";
            default:
                return Integer.toString(i);
        }
    }

    public int describeContents() {
        return IN_VEHICLE;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DetectedActivity detectedActivity = (DetectedActivity) o;
        return this.zzaEg == detectedActivity.zzaEg && this.zzaEh == detectedActivity.zzaEh;
    }

    public int getConfidence() {
        return this.zzaEh;
    }

    public int getType() {
        return zzgK(this.zzaEg);
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        Object[] objArr = new Object[ON_FOOT];
        objArr[IN_VEHICLE] = Integer.valueOf(this.zzaEg);
        objArr[ON_BICYCLE] = Integer.valueOf(this.zzaEh);
        return zzw.hashCode(objArr);
    }

    public String toString() {
        return "DetectedActivity [type=" + zzgL(getType()) + ", confidence=" + this.zzaEh + "]";
    }

    public void writeToParcel(Parcel out, int flags) {
        DetectedActivityCreator.zza(this, out, flags);
    }
}
