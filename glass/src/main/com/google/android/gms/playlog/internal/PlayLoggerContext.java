package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;

public class PlayLoggerContext implements SafeParcelable {
    public static final zze CREATOR;
    public final String packageName;
    public final int versionCode;
    public final int zzaRR;
    public final int zzaRS;
    public final String zzaRT;
    public final String zzaRU;
    public final boolean zzaRV;
    public final String zzaRW;
    public final boolean zzaRX;
    public final int zzaRY;

    static {
        CREATOR = new zze();
    }

    public PlayLoggerContext(int versionCode, String packageName, int packageVersionCode, int logSource, String uploadAccountName, String loggingId, boolean logAndroidId, String logSourceName, boolean isAnonymous, int qosTier) {
        this.versionCode = versionCode;
        this.packageName = packageName;
        this.zzaRR = packageVersionCode;
        this.zzaRS = logSource;
        this.zzaRT = uploadAccountName;
        this.zzaRU = loggingId;
        this.zzaRV = logAndroidId;
        this.zzaRW = logSourceName;
        this.zzaRX = isAnonymous;
        this.zzaRY = qosTier;
    }

    @Deprecated
    public PlayLoggerContext(String packageName, int packageVersionCode, int logSource, String uploadAccountName, String loggingId, boolean logAndroidId) {
        this.versionCode = 1;
        this.packageName = (String) zzx.zzw(packageName);
        this.zzaRR = packageVersionCode;
        this.zzaRS = logSource;
        this.zzaRW = null;
        this.zzaRT = uploadAccountName;
        this.zzaRU = loggingId;
        this.zzaRV = logAndroidId;
        this.zzaRX = false;
        this.zzaRY = 0;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PlayLoggerContext)) {
            return false;
        }
        PlayLoggerContext playLoggerContext = (PlayLoggerContext) object;
        return this.versionCode == playLoggerContext.versionCode && this.packageName.equals(playLoggerContext.packageName) && this.zzaRR == playLoggerContext.zzaRR && this.zzaRS == playLoggerContext.zzaRS && zzw.equal(this.zzaRW, playLoggerContext.zzaRW) && zzw.equal(this.zzaRT, playLoggerContext.zzaRT) && zzw.equal(this.zzaRU, playLoggerContext.zzaRU) && this.zzaRV == playLoggerContext.zzaRV && this.zzaRX == playLoggerContext.zzaRX && this.zzaRY == playLoggerContext.zzaRY;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.versionCode), this.packageName, Integer.valueOf(this.zzaRR), Integer.valueOf(this.zzaRS), this.zzaRW, this.zzaRT, this.zzaRU, Boolean.valueOf(this.zzaRV), Boolean.valueOf(this.zzaRX), Integer.valueOf(this.zzaRY));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PlayLoggerContext[");
        stringBuilder.append("versionCode=").append(this.versionCode).append(',');
        stringBuilder.append("package=").append(this.packageName).append(',');
        stringBuilder.append("packageVersionCode=").append(this.zzaRR).append(',');
        stringBuilder.append("logSource=").append(this.zzaRS).append(',');
        stringBuilder.append("logSourceName=").append(this.zzaRW).append(',');
        stringBuilder.append("uploadAccount=").append(this.zzaRT).append(',');
        stringBuilder.append("loggingId=").append(this.zzaRU).append(',');
        stringBuilder.append("logAndroidId=").append(this.zzaRV).append(',');
        stringBuilder.append("isAnonymous=").append(this.zzaRX).append(',');
        stringBuilder.append("qosTier=").append(this.zzaRY);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        zze.zza(this, out, flags);
    }
}
