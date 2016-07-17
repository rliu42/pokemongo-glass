package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ResolveAccountRequest implements SafeParcelable {
    public static final Creator<ResolveAccountRequest> CREATOR;
    final int mVersionCode;
    private final Account zzQd;
    private final int zzagp;

    static {
        CREATOR = new zzy();
    }

    ResolveAccountRequest(int versionCode, Account account, int sessionId) {
        this.mVersionCode = versionCode;
        this.zzQd = account;
        this.zzagp = sessionId;
    }

    public ResolveAccountRequest(Account account, int sessionId) {
        this(1, account, sessionId);
    }

    public int describeContents() {
        return 0;
    }

    public Account getAccount() {
        return this.zzQd;
    }

    public int getSessionId() {
        return this.zzagp;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzy.zza(this, dest, flags);
    }
}
