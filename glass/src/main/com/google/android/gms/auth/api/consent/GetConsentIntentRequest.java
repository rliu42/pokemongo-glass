package com.google.android.gms.auth.api.consent;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.firstparty.shared.ScopeDetail;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public class GetConsentIntentRequest implements SafeParcelable {
    public static final Creator<GetConsentIntentRequest> CREATOR;
    private final int mVersionCode;
    private final Account zzQd;
    private final String zzSb;
    private final int zzSc;
    private final String zzSd;
    final ScopeDetail[] zzSe;
    private final boolean zzSf;
    private final int zzSg;

    static {
        CREATOR = new zzb();
    }

    GetConsentIntentRequest(int versionCode, String callingPackage, int callingUid, String service, Account account, ScopeDetail[] scopeDetails, boolean hasTitle, int title) {
        this.mVersionCode = versionCode;
        this.zzSb = callingPackage;
        this.zzSc = callingUid;
        this.zzSd = service;
        this.zzQd = (Account) zzx.zzw(account);
        this.zzSe = scopeDetails;
        this.zzSf = hasTitle;
        this.zzSg = title;
    }

    public int describeContents() {
        return 0;
    }

    public Account getAccount() {
        return this.zzQd;
    }

    public String getCallingPackage() {
        return this.zzSb;
    }

    public int getCallingUid() {
        return this.zzSc;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzb.zza(this, dest, flags);
    }

    public String zzlF() {
        return this.zzSd;
    }

    public boolean zzlG() {
        return this.zzSf;
    }

    public int zzlH() {
        return this.zzSg;
    }
}
