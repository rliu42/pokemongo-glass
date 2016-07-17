package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CheckServerAuthResult implements SafeParcelable {
    public static final Creator<CheckServerAuthResult> CREATOR;
    final int mVersionCode;
    final boolean zzaVi;
    final List<Scope> zzaVj;

    static {
        CREATOR = new zzc();
    }

    CheckServerAuthResult(int versionCode, boolean newAuthCodeRequired, List<Scope> additionalScopes) {
        this.mVersionCode = versionCode;
        this.zzaVi = newAuthCodeRequired;
        this.zzaVj = additionalScopes;
    }

    public CheckServerAuthResult(boolean newAuthCodeRequired, Set<Scope> additionalScopes) {
        this(1, newAuthCodeRequired, zze(additionalScopes));
    }

    private static List<Scope> zze(Set<Scope> set) {
        return set == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(set));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzc.zza(this, dest, flags);
    }
}
