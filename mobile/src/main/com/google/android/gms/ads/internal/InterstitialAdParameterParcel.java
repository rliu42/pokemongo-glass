package com.google.android.gms.ads.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgr;

@zzgr
public final class InterstitialAdParameterParcel implements SafeParcelable {
    public static final zzl CREATOR;
    public final int versionCode;
    public final boolean zzpt;
    public final boolean zzpu;
    public final String zzpv;
    public final boolean zzpw;
    public final float zzpx;

    static {
        CREATOR = new zzl();
    }

    InterstitialAdParameterParcel(int versionCode, boolean transparentBackground, boolean hideStatusBar, String backgroundImage, boolean blur, float blurRadius) {
        this.versionCode = versionCode;
        this.zzpt = transparentBackground;
        this.zzpu = hideStatusBar;
        this.zzpv = backgroundImage;
        this.zzpw = blur;
        this.zzpx = blurRadius;
    }

    public InterstitialAdParameterParcel(boolean transparentBackground, boolean hideStatusBar, String backgroundImage, boolean blur, float blurRadius) {
        this(2, transparentBackground, hideStatusBar, backgroundImage, blur, blurRadius);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzl.zza(this, out, flags);
    }
}
