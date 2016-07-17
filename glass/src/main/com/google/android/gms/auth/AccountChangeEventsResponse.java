package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.List;

public class AccountChangeEventsResponse implements SafeParcelable {
    public static final Creator<AccountChangeEventsResponse> CREATOR;
    final int mVersion;
    final List<AccountChangeEvent> zzoQ;

    static {
        CREATOR = new zzc();
    }

    AccountChangeEventsResponse(int version, List<AccountChangeEvent> events) {
        this.mVersion = version;
        this.zzoQ = (List) zzx.zzw(events);
    }

    public AccountChangeEventsResponse(List<AccountChangeEvent> events) {
        this.mVersion = 1;
        this.zzoQ = (List) zzx.zzw(events);
    }

    public int describeContents() {
        return 0;
    }

    public List<AccountChangeEvent> getEvents() {
        return this.zzoQ;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzc.zza(this, dest, flags);
    }
}
