package com.google.android.gms.ads.internal.overlay;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgr;

@zzgr
public final class AdLauncherIntentInfoParcel implements SafeParcelable {
    public static final zzb CREATOR;
    public final String intentAction;
    public final String mimeType;
    public final String packageName;
    public final String url;
    public final int versionCode;
    public final String zzAL;
    public final String zzAM;
    public final String zzAN;

    static {
        CREATOR = new zzb();
    }

    public AdLauncherIntentInfoParcel(int versionCode, String intentAction, String url, String mimeType, String packageName, String componentName, String intentFlagsString, String intentExtrasString) {
        this.versionCode = versionCode;
        this.intentAction = intentAction;
        this.url = url;
        this.mimeType = mimeType;
        this.packageName = packageName;
        this.zzAL = componentName;
        this.zzAM = intentFlagsString;
        this.zzAN = intentExtrasString;
    }

    public AdLauncherIntentInfoParcel(String intentAction, String url, String mimeType, String packageName, String componentName, String intentFlagsString, String intentExtrasString) {
        this(1, intentAction, url, mimeType, packageName, componentName, intentFlagsString, intentExtrasString);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
