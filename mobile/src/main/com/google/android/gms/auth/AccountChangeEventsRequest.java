package com.google.android.gms.auth;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class AccountChangeEventsRequest implements SafeParcelable {
    public static final Creator<AccountChangeEventsRequest> CREATOR;
    final int mVersion;
    Account zzQd;
    @Deprecated
    String zzRs;
    int zzRu;

    static {
        CREATOR = new zzb();
    }

    public AccountChangeEventsRequest() {
        this.mVersion = 1;
    }

    AccountChangeEventsRequest(int version, int eventIndex, String accountName, Account account) {
        this.mVersion = version;
        this.zzRu = eventIndex;
        this.zzRs = accountName;
        if (account != null || TextUtils.isEmpty(accountName)) {
            this.zzQd = account;
        } else {
            this.zzQd = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        }
    }

    public int describeContents() {
        return 0;
    }

    public Account getAccount() {
        return this.zzQd;
    }

    public String getAccountName() {
        return this.zzRs;
    }

    public int getEventIndex() {
        return this.zzRu;
    }

    public AccountChangeEventsRequest setAccount(Account account) {
        this.zzQd = account;
        return this;
    }

    public AccountChangeEventsRequest setAccountName(String accountName) {
        this.zzRs = accountName;
        return this;
    }

    public AccountChangeEventsRequest setEventIndex(int eventIndex) {
        this.zzRu = eventIndex;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzb.zza(this, dest, flags);
    }
}
