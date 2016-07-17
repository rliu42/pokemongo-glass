package com.google.android.gms.ads.internal.request;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class StringParcel implements SafeParcelable {
    public static final Creator<StringParcel> CREATOR;
    final int mVersionCode;
    String zzvY;

    static {
        CREATOR = new zzn();
    }

    StringParcel(int versionCode, String content) {
        this.mVersionCode = versionCode;
        this.zzvY = content;
    }

    public StringParcel(String content) {
        this.mVersionCode = 1;
        this.zzvY = content;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzn.zza(this, dest, flags);
    }

    public String zzfP() {
        return this.zzvY;
    }
}
