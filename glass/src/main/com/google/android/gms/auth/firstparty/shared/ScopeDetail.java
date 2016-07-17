package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.List;

public class ScopeDetail implements SafeParcelable {
    public static final zzc CREATOR;
    String description;
    final int version;
    String zzTH;
    String zzTI;
    String zzTJ;
    String zzTK;
    List<String> zzTL;
    public FACLData zzTM;

    static {
        CREATOR = new zzc();
    }

    ScopeDetail(int version, String description, String detail, String iconBase64, String paclPickerDataBase64, String service, List<String> warnings, FACLData friendPickerData) {
        this.version = version;
        this.description = description;
        this.zzTH = detail;
        this.zzTI = iconBase64;
        this.zzTJ = paclPickerDataBase64;
        this.zzTK = service;
        this.zzTL = warnings;
        this.zzTM = friendPickerData;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzc.zza(this, dest, flags);
    }
}
