package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzp.zza;
import java.util.Collection;

public class GetServiceRequest implements SafeParcelable {
    public static final Creator<GetServiceRequest> CREATOR;
    final int version;
    final int zzafq;
    int zzafr;
    String zzafs;
    IBinder zzaft;
    Scope[] zzafu;
    Bundle zzafv;
    Account zzafw;

    static {
        CREATOR = new zzi();
    }

    public GetServiceRequest(int serviceId) {
        this.version = 2;
        this.zzafr = GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        this.zzafq = serviceId;
    }

    GetServiceRequest(int version, int serviceId, int clientVersion, String callingPackage, IBinder accountAccessorBinder, Scope[] scopes, Bundle extraArgs, Account clientRequestedAccount) {
        this.version = version;
        this.zzafq = serviceId;
        this.zzafr = clientVersion;
        this.zzafs = callingPackage;
        if (version < 2) {
            this.zzafw = zzaG(accountAccessorBinder);
        } else {
            this.zzaft = accountAccessorBinder;
            this.zzafw = clientRequestedAccount;
        }
        this.zzafu = scopes;
        this.zzafv = extraArgs;
    }

    private Account zzaG(IBinder iBinder) {
        return iBinder != null ? zza.zzb(zza.zzaH(iBinder)) : null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzi.zza(this, dest, flags);
    }

    public GetServiceRequest zzc(Account account) {
        this.zzafw = account;
        return this;
    }

    public GetServiceRequest zzc(zzp com_google_android_gms_common_internal_zzp) {
        if (com_google_android_gms_common_internal_zzp != null) {
            this.zzaft = com_google_android_gms_common_internal_zzp.asBinder();
        }
        return this;
    }

    public GetServiceRequest zzcl(String str) {
        this.zzafs = str;
        return this;
    }

    public GetServiceRequest zzd(Collection<Scope> collection) {
        this.zzafu = (Scope[]) collection.toArray(new Scope[collection.size()]);
        return this;
    }

    public GetServiceRequest zzg(Bundle bundle) {
        this.zzafv = bundle;
        return this;
    }
}
