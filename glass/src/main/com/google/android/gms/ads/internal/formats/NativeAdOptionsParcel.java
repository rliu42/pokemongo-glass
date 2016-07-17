package com.google.android.gms.ads.internal.formats;

import android.os.Parcel;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgr;

@zzgr
public class NativeAdOptionsParcel implements SafeParcelable {
    public static final zzi CREATOR;
    public final int versionCode;
    public final boolean zzwR;
    public final int zzwS;
    public final boolean zzwT;

    static {
        CREATOR = new zzi();
    }

    public NativeAdOptionsParcel(int versionCode, boolean shouldReturnUrlsForImageAssets, int imageOrientation, boolean shouldRequestMultipleImages) {
        this.versionCode = versionCode;
        this.zzwR = shouldReturnUrlsForImageAssets;
        this.zzwS = imageOrientation;
        this.zzwT = shouldRequestMultipleImages;
    }

    public NativeAdOptionsParcel(NativeAdOptions options) {
        this(1, options.shouldReturnUrlsForImageAssets(), options.getImageOrientation(), options.shouldRequestMultipleImages());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzi.zza(this, out, flags);
    }
}
