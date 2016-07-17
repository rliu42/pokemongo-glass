package com.google.android.gms.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import spacemadness.com.lunarconsole.C1391R;

public interface zzkd extends IInterface {

    public static abstract class zza extends Binder implements zzkd {

        private static class zza implements zzkd {
            private IBinder zznJ;

            zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(Account account, int i, zzkc com_google_android_gms_internal_zzkc) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
                    if (account != null) {
                        obtain.writeInt(1);
                        account.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzkc != null ? com_google_android_gms_internal_zzkc.asBinder() : null);
                    this.zznJ.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzkd zzao(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzkd)) ? new zza(iBinder) : (zzkd) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    data.enforceInterface("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
                    zza(data.readInt() != 0 ? (Account) Account.CREATOR.createFromParcel(data) : null, data.readInt(), com.google.android.gms.internal.zzkc.zza.zzan(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void zza(Account account, int i, zzkc com_google_android_gms_internal_zzkc) throws RemoteException;
}
