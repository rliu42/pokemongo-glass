package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BinderWrapper implements Parcelable {
    public static final Creator<BinderWrapper> CREATOR;
    private IBinder zzaeJ;

    /* renamed from: com.google.android.gms.common.internal.BinderWrapper.1 */
    static class C02231 implements Creator<BinderWrapper> {
        C02231() {
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return zzaj(x0);
        }

        public /* synthetic */ Object[] newArray(int x0) {
            return zzbC(x0);
        }

        public BinderWrapper zzaj(Parcel parcel) {
            return new BinderWrapper(null);
        }

        public BinderWrapper[] zzbC(int i) {
            return new BinderWrapper[i];
        }
    }

    static {
        CREATOR = new C02231();
    }

    public BinderWrapper() {
        this.zzaeJ = null;
    }

    public BinderWrapper(IBinder binder) {
        this.zzaeJ = null;
        this.zzaeJ = binder;
    }

    private BinderWrapper(Parcel in) {
        this.zzaeJ = null;
        this.zzaeJ = in.readStrongBinder();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(this.zzaeJ);
    }
}
