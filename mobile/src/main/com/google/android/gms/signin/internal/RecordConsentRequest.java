package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class RecordConsentRequest implements SafeParcelable {
    public static final Creator<RecordConsentRequest> CREATOR;
    final int mVersionCode;
    private final Account zzQd;
    private final String zzTl;
    private final Scope[] zzaVk;

    static {
        CREATOR = new zzg();
    }

    RecordConsentRequest(int versionCode, Account account, Scope[] scopesToConsent, String serverClientId) {
        this.mVersionCode = versionCode;
        this.zzQd = account;
        this.zzaVk = scopesToConsent;
        this.zzTl = serverClientId;
    }

    public int describeContents() {
        return 0;
    }

    public Account getAccount() {
        return this.zzQd;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzg.zza(this, dest, flags);
    }

    public Scope[] zzCj() {
        return this.zzaVk;
    }

    public String zzmb() {
        return this.zzTl;
    }
}
