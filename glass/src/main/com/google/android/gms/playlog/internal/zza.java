package com.google.android.gms.playlog.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import java.util.List;
import spacemadness.com.lunarconsole.C1391R;

public interface zza extends IInterface {

    public static abstract class zza extends Binder implements zza {

        private static class zza implements zza {
            private IBinder zznJ;

            zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(String str, PlayLoggerContext playLoggerContext, LogEvent logEvent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(str);
                    if (playLoggerContext != null) {
                        obtain.writeInt(1);
                        playLoggerContext.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (logEvent != null) {
                        obtain.writeInt(1);
                        logEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(String str, PlayLoggerContext playLoggerContext, List<LogEvent> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(str);
                    if (playLoggerContext != null) {
                        obtain.writeInt(1);
                        playLoggerContext.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeTypedList(list);
                    this.zznJ.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(String str, PlayLoggerContext playLoggerContext, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(str);
                    if (playLoggerContext != null) {
                        obtain.writeInt(1);
                        playLoggerContext.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeByteArray(bArr);
                    this.zznJ.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static zza zzdz(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.playlog.internal.IPlayLogService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zza)) ? new zza(iBinder) : (zza) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PlayLoggerContext playLoggerContext = null;
            String readString;
            switch (code) {
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    LogEvent zzgi;
                    data.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    String readString2 = data.readString();
                    PlayLoggerContext zzgj = data.readInt() != 0 ? PlayLoggerContext.CREATOR.zzgj(data) : null;
                    if (data.readInt() != 0) {
                        zzgi = LogEvent.CREATOR.zzgi(data);
                    }
                    zza(readString2, zzgj, zzgi);
                    return true;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    data.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        playLoggerContext = PlayLoggerContext.CREATOR.zzgj(data);
                    }
                    zza(readString, playLoggerContext, data.createTypedArrayList(LogEvent.CREATOR));
                    return true;
                case Place.TYPE_AQUARIUM /*4*/:
                    data.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        playLoggerContext = PlayLoggerContext.CREATOR.zzgj(data);
                    }
                    zza(readString, playLoggerContext, data.createByteArray());
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.playlog.internal.IPlayLogService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void zza(String str, PlayLoggerContext playLoggerContext, LogEvent logEvent) throws RemoteException;

    void zza(String str, PlayLoggerContext playLoggerContext, List<LogEvent> list) throws RemoteException;

    void zza(String str, PlayLoggerContext playLoggerContext, byte[] bArr) throws RemoteException;
}
