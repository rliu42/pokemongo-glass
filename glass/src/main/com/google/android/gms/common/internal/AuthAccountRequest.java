package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Set;

public class AuthAccountRequest implements SafeParcelable {
    public static final Creator<AuthAccountRequest> CREATOR;
    final int mVersionCode;
    final IBinder zzaeH;
    final Scope[] zzaeI;

    static {
        CREATOR = new zzc();
    }

    AuthAccountRequest(int versionCode, IBinder accountAccessorBinder, Scope[] scopes) {
        this.mVersionCode = versionCode;
        this.zzaeH = accountAccessorBinder;
        this.zzaeI = scopes;
    }

    public AuthAccountRequest(zzp accountAccessor, Set<Scope> scopes) {
        this(1, accountAccessor.asBinder(), (Scope[]) scopes.toArray(new Scope[scopes.size()]));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzc.zza(this, dest, flags);
    }
}
