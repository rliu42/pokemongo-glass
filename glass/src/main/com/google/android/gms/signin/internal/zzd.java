package com.google.android.gms.signin.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Scope;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import java.util.List;
import spacemadness.com.lunarconsole.C1391R;

public interface zzd extends IInterface {

    public static abstract class zza extends Binder implements zzd {
        public zza() {
            attachInterface(this, "com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    data.enforceInterface("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
                    zza(data.readString(), data.createTypedArrayList(Scope.CREATOR), com.google.android.gms.signin.internal.zzf.zza.zzdN(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    data.enforceInterface("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
                    zza(data.readString(), data.readString(), com.google.android.gms.signin.internal.zzf.zza.zzdN(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void zza(String str, String str2, zzf com_google_android_gms_signin_internal_zzf) throws RemoteException;

    void zza(String str, List<Scope> list, zzf com_google_android_gms_signin_internal_zzf) throws RemoteException;
}
