package com.google.android.gms.auth.api.signin.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.location.LocationRequest;
import spacemadness.com.lunarconsole.C1391R;

public interface zze extends IInterface {

    public static abstract class zza extends Binder implements zze {

        private static class zza implements zze {
            private IBinder zznJ;

            zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(zzd com_google_android_gms_auth_api_signin_internal_zzd, GoogleSignInConfig googleSignInConfig) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    obtain.writeStrongBinder(com_google_android_gms_auth_api_signin_internal_zzd != null ? com_google_android_gms_auth_api_signin_internal_zzd.asBinder() : null);
                    if (googleSignInConfig != null) {
                        obtain.writeInt(1);
                        googleSignInConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(101, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzd com_google_android_gms_auth_api_signin_internal_zzd, SignInConfiguration signInConfiguration) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    obtain.writeStrongBinder(com_google_android_gms_auth_api_signin_internal_zzd != null ? com_google_android_gms_auth_api_signin_internal_zzd.asBinder() : null);
                    if (signInConfiguration != null) {
                        obtain.writeInt(1);
                        signInConfiguration.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(zzd com_google_android_gms_auth_api_signin_internal_zzd, GoogleSignInConfig googleSignInConfig) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    obtain.writeStrongBinder(com_google_android_gms_auth_api_signin_internal_zzd != null ? com_google_android_gms_auth_api_signin_internal_zzd.asBinder() : null);
                    if (googleSignInConfig != null) {
                        obtain.writeInt(1);
                        googleSignInConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(zzd com_google_android_gms_auth_api_signin_internal_zzd, SignInConfiguration signInConfiguration) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    obtain.writeStrongBinder(com_google_android_gms_auth_api_signin_internal_zzd != null ? com_google_android_gms_auth_api_signin_internal_zzd.asBinder() : null);
                    if (signInConfiguration != null) {
                        obtain.writeInt(1);
                        signInConfiguration.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzc(zzd com_google_android_gms_auth_api_signin_internal_zzd, GoogleSignInConfig googleSignInConfig) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    obtain.writeStrongBinder(com_google_android_gms_auth_api_signin_internal_zzd != null ? com_google_android_gms_auth_api_signin_internal_zzd.asBinder() : null);
                    if (googleSignInConfig != null) {
                        obtain.writeInt(1);
                        googleSignInConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(103, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zze zzaz(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zze)) ? new zza(iBinder) : (zze) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            GoogleSignInConfig googleSignInConfig = null;
            zzd zzay;
            SignInConfiguration signInConfiguration;
            switch (code) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    data.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    zzay = com.google.android.gms.auth.api.signin.internal.zzd.zza.zzay(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        signInConfiguration = (SignInConfiguration) SignInConfiguration.CREATOR.createFromParcel(data);
                    }
                    zza(zzay, signInConfiguration);
                    reply.writeNoException();
                    return true;
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    data.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    zzay = com.google.android.gms.auth.api.signin.internal.zzd.zza.zzay(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        signInConfiguration = (SignInConfiguration) SignInConfiguration.CREATOR.createFromParcel(data);
                    }
                    zzb(zzay, signInConfiguration);
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    zzay = com.google.android.gms.auth.api.signin.internal.zzd.zza.zzay(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        googleSignInConfig = (GoogleSignInConfig) GoogleSignInConfig.CREATOR.createFromParcel(data);
                    }
                    zza(zzay, googleSignInConfig);
                    reply.writeNoException();
                    return true;
                case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                    data.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    zzay = com.google.android.gms.auth.api.signin.internal.zzd.zza.zzay(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        googleSignInConfig = (GoogleSignInConfig) GoogleSignInConfig.CREATOR.createFromParcel(data);
                    }
                    zzb(zzay, googleSignInConfig);
                    reply.writeNoException();
                    return true;
                case 103:
                    data.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    zzay = com.google.android.gms.auth.api.signin.internal.zzd.zza.zzay(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        googleSignInConfig = (GoogleSignInConfig) GoogleSignInConfig.CREATOR.createFromParcel(data);
                    }
                    zzc(zzay, googleSignInConfig);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void zza(zzd com_google_android_gms_auth_api_signin_internal_zzd, GoogleSignInConfig googleSignInConfig) throws RemoteException;

    void zza(zzd com_google_android_gms_auth_api_signin_internal_zzd, SignInConfiguration signInConfiguration) throws RemoteException;

    void zzb(zzd com_google_android_gms_auth_api_signin_internal_zzd, GoogleSignInConfig googleSignInConfig) throws RemoteException;

    void zzb(zzd com_google_android_gms_auth_api_signin_internal_zzd, SignInConfiguration signInConfiguration) throws RemoteException;

    void zzc(zzd com_google_android_gms_auth_api_signin_internal_zzd, GoogleSignInConfig googleSignInConfig) throws RemoteException;
}
