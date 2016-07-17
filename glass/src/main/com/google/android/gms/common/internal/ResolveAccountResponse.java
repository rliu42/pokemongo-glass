package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzp.zza;

public class ResolveAccountResponse implements SafeParcelable {
    public static final Creator<ResolveAccountResponse> CREATOR;
    final int mVersionCode;
    private boolean zzabG;
    IBinder zzaeH;
    private ConnectionResult zzagq;
    private boolean zzagr;

    static {
        CREATOR = new zzz();
    }

    public ResolveAccountResponse(int connectionResultStatusCode) {
        this(new ConnectionResult(connectionResultStatusCode, null));
    }

    ResolveAccountResponse(int versionCode, IBinder accountAccessorBinder, ConnectionResult connectionResult, boolean saveDefaultAccount, boolean isFromCrossClientAuth) {
        this.mVersionCode = versionCode;
        this.zzaeH = accountAccessorBinder;
        this.zzagq = connectionResult;
        this.zzabG = saveDefaultAccount;
        this.zzagr = isFromCrossClientAuth;
    }

    public ResolveAccountResponse(ConnectionResult result) {
        this(1, null, result, false, false);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResolveAccountResponse)) {
            return false;
        }
        ResolveAccountResponse resolveAccountResponse = (ResolveAccountResponse) o;
        return this.zzagq.equals(resolveAccountResponse.zzagq) && zzpq().equals(resolveAccountResponse.zzpq());
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzz.zza(this, dest, flags);
    }

    public zzp zzpq() {
        return zza.zzaH(this.zzaeH);
    }

    public ConnectionResult zzpr() {
        return this.zzagq;
    }

    public boolean zzps() {
        return this.zzabG;
    }

    public boolean zzpt() {
        return this.zzagr;
    }
}
