package com.google.android.gms.gcm;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PendingCallback implements Parcelable {
    public static final Creator<PendingCallback> CREATOR;
    final IBinder zzaeJ;

    /* renamed from: com.google.android.gms.gcm.PendingCallback.1 */
    static class C04411 implements Creator<PendingCallback> {
        C04411() {
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return zzet(x0);
        }

        public /* synthetic */ Object[] newArray(int x0) {
            return zzgD(x0);
        }

        public PendingCallback zzet(Parcel parcel) {
            return new PendingCallback(parcel);
        }

        public PendingCallback[] zzgD(int i) {
            return new PendingCallback[i];
        }
    }

    static {
        CREATOR = new C04411();
    }

    public PendingCallback(Parcel in) {
        this.zzaeJ = in.readStrongBinder();
    }

    public int describeContents() {
        return 0;
    }

    public IBinder getIBinder() {
        return this.zzaeJ;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStrongBinder(this.zzaeJ);
    }
}
