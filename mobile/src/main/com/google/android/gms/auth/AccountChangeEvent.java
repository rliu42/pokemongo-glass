package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import spacemadness.com.lunarconsole.C1391R;

public class AccountChangeEvent implements SafeParcelable {
    public static final Creator<AccountChangeEvent> CREATOR;
    final int mVersion;
    final long zzRr;
    final String zzRs;
    final int zzRt;
    final int zzRu;
    final String zzRv;

    static {
        CREATOR = new zza();
    }

    AccountChangeEvent(int version, long id, String accountName, int changeType, int eventIndex, String changeData) {
        this.mVersion = version;
        this.zzRr = id;
        this.zzRs = (String) zzx.zzw(accountName);
        this.zzRt = changeType;
        this.zzRu = eventIndex;
        this.zzRv = changeData;
    }

    public AccountChangeEvent(long id, String accountName, int changeType, int eventIndex, String changeData) {
        this.mVersion = 1;
        this.zzRr = id;
        this.zzRs = (String) zzx.zzw(accountName);
        this.zzRt = changeType;
        this.zzRu = eventIndex;
        this.zzRv = changeData;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object that) {
        if (that == this) {
            return true;
        }
        if (!(that instanceof AccountChangeEvent)) {
            return false;
        }
        AccountChangeEvent accountChangeEvent = (AccountChangeEvent) that;
        return this.mVersion == accountChangeEvent.mVersion && this.zzRr == accountChangeEvent.zzRr && zzw.equal(this.zzRs, accountChangeEvent.zzRs) && this.zzRt == accountChangeEvent.zzRt && this.zzRu == accountChangeEvent.zzRu && zzw.equal(this.zzRv, accountChangeEvent.zzRv);
    }

    public String getAccountName() {
        return this.zzRs;
    }

    public String getChangeData() {
        return this.zzRv;
    }

    public int getChangeType() {
        return this.zzRt;
    }

    public int getEventIndex() {
        return this.zzRu;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.mVersion), Long.valueOf(this.zzRr), this.zzRs, Integer.valueOf(this.zzRt), Integer.valueOf(this.zzRu), this.zzRv);
    }

    public String toString() {
        String str = "UNKNOWN";
        switch (this.zzRt) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                str = "ADDED";
                break;
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                str = "REMOVED";
                break;
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                str = "RENAMED_FROM";
                break;
            case Place.TYPE_AQUARIUM /*4*/:
                str = "RENAMED_TO";
                break;
        }
        return "AccountChangeEvent {accountName = " + this.zzRs + ", changeType = " + str + ", changeData = " + this.zzRv + ", eventIndex = " + this.zzRu + "}";
    }

    public void writeToParcel(Parcel dest, int flags) {
        zza.zza(this, dest, flags);
    }
}
