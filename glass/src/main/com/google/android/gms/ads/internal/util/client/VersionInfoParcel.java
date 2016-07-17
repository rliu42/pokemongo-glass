package com.google.android.gms.ads.internal.util.client;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgr;

@zzgr
public final class VersionInfoParcel implements SafeParcelable {
    public static final zzc CREATOR;
    public final int versionCode;
    public String zzJu;
    public int zzJv;
    public int zzJw;
    public boolean zzJx;

    static {
        CREATOR = new zzc();
    }

    public VersionInfoParcel(int buddyApkVersion, int clientJarVersion, boolean isClientJar) {
        this(1, "afma-sdk-a-v" + buddyApkVersion + "." + clientJarVersion + "." + (isClientJar ? "0" : "1"), buddyApkVersion, clientJarVersion, isClientJar);
    }

    VersionInfoParcel(int versionCode, String afmaVersion, int buddyApkVersion, int clientJarVersion, boolean isClientJar) {
        this.versionCode = versionCode;
        this.zzJu = afmaVersion;
        this.zzJv = buddyApkVersion;
        this.zzJw = clientJarVersion;
        this.zzJx = isClientJar;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }
}
