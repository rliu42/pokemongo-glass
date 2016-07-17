package com.google.android.gms.auth;

import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import java.util.List;

public class TokenData implements SafeParcelable {
    public static final zzd CREATOR;
    final int mVersionCode;
    private final Long zzRA;
    private final boolean zzRB;
    private final boolean zzRC;
    private final List<String> zzRD;
    private final String zzRz;

    static {
        CREATOR = new zzd();
    }

    TokenData(int versionCode, String token, Long expirationTimeSecs, boolean isCached, boolean isSnowballed, List<String> grantedScopes) {
        this.mVersionCode = versionCode;
        this.zzRz = zzx.zzcr(token);
        this.zzRA = expirationTimeSecs;
        this.zzRB = isCached;
        this.zzRC = isSnowballed;
        this.zzRD = grantedScopes;
    }

    public static TokenData zza(Bundle bundle, String str) {
        bundle.setClassLoader(TokenData.class.getClassLoader());
        Bundle bundle2 = bundle.getBundle(str);
        if (bundle2 == null) {
            return null;
        }
        bundle2.setClassLoader(TokenData.class.getClassLoader());
        return (TokenData) bundle2.getParcelable("TokenData");
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof TokenData)) {
            return false;
        }
        TokenData tokenData = (TokenData) o;
        return TextUtils.equals(this.zzRz, tokenData.zzRz) && zzw.equal(this.zzRA, tokenData.zzRA) && this.zzRB == tokenData.zzRB && this.zzRC == tokenData.zzRC && zzw.equal(this.zzRD, tokenData.zzRD);
    }

    public String getToken() {
        return this.zzRz;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzRz, this.zzRA, Boolean.valueOf(this.zzRB), Boolean.valueOf(this.zzRC), this.zzRD);
    }

    public void writeToParcel(Parcel out, int flags) {
        zzd.zza(this, out, flags);
    }

    public Long zzlA() {
        return this.zzRA;
    }

    public boolean zzlB() {
        return this.zzRB;
    }

    public boolean zzlC() {
        return this.zzRC;
    }

    public List<String> zzlD() {
        return this.zzRD;
    }
}
