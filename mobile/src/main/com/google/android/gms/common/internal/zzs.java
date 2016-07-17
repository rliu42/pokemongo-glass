package com.google.android.gms.common.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.nianticproject.holoholo.sfida.constants.BluetoothGattSupport;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import spacemadness.com.lunarconsole.C1391R;

public interface zzs extends IInterface {

    public static abstract class zza extends Binder implements zzs {

        private static class zza implements zzs {
            private IBinder zznJ;

            zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    this.zznJ.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
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

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.zznJ.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2, String str3, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeStringArray(strArr);
                    this.zznJ.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStringArray(strArr);
                    this.zznJ.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2, String[] strArr, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStringArray(strArr);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2, String[] strArr, String str3, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStringArray(strArr);
                    obtain.writeString(str3);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
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

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2, String[] strArr, String str3, IBinder iBinder, String str4, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStringArray(strArr);
                    obtain.writeString(str3);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str4);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String[] strArr, String str2, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeStringArray(strArr);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, GetServiceRequest getServiceRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    if (getServiceRequest != null) {
                        obtain.writeInt(1);
                        getServiceRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzr com_google_android_gms_common_internal_zzr, ValidateAccountRequest validateAccountRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    if (validateAccountRequest != null) {
                        obtain.writeInt(1);
                        validateAccountRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzc(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzc(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzd(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzd(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zze(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zze(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzf(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzf(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzg(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzg(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzh(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzh(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzi(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzi(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzj(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzj(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzk(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzk(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzl(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzl(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzm(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.zznJ.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzm(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzn(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzo(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzp(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzpp() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    this.zznJ.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzq(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzr(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzs(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzt(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzr != null ? com_google_android_gms_common_internal_zzr.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzs zzaK(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzs)) ? new zza(iBinder) : (zzs) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ValidateAccountRequest validateAccountRequest = null;
            zzr zzaJ;
            int readInt;
            String readString;
            Bundle bundle;
            switch (code) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zza(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString(), data.readString(), data.createStringArray(), data.readString(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zza(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zza(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_AQUARIUM /*4*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zza(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_ART_GALLERY /*5*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzb(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_ATM /*6*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzc(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_BAKERY /*7*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzd(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zze(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_BAR /*9*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zza(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString(), data.readString(), data.createStringArray(), data.readString(), data.readStrongBinder(), data.readString(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zza(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString(), data.readString(), data.createStringArray());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_BICYCLE_STORE /*11*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzf(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_BOOK_STORE /*12*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzg(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_BOWLING_ALLEY /*13*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzh(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_BUS_STATION /*14*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzi(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CAFE /*15*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzj(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CAMPGROUND /*16*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzk(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CAR_DEALER /*17*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzl(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CAR_RENTAL /*18*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzm(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CAR_REPAIR /*19*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zza(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString(), data.readStrongBinder(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CAR_WASH /*20*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zza(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString(), data.createStringArray(), data.readString(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CASINO /*21*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzb(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CEMETERY /*22*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzc(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CHURCH /*23*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzn(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CITY_HALL /*24*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzd(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CLOTHING_STORE /*25*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzo(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_CONVENIENCE_STORE /*26*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zze(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_COURTHOUSE /*27*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzp(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_DENTIST /*28*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzpp();
                    reply.writeNoException();
                    return true;
                case Place.TYPE_DOCTOR /*30*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zza(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString(), data.readString(), data.createStringArray(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_ELECTRICIAN /*31*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzf(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_ELECTRONICS_STORE /*32*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzg(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_EMBASSY /*33*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zza(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString(), data.readString(), data.readString(), data.createStringArray());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_ESTABLISHMENT /*34*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zza(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_FINANCE /*35*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzh(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_FIRE_STATION /*36*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzi(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_FLORIST /*37*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzq(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_FOOD /*38*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzr(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_FURNITURE_STORE /*40*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzj(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_GAS_STATION /*41*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzs(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzk(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    zzt(zzaJ, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_GYM /*44*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzl(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_HAIR_CARE /*45*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzm(com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case Place.TYPE_HARDWARE_STORE /*46*/:
                    GetServiceRequest getServiceRequest;
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        getServiceRequest = (GetServiceRequest) GetServiceRequest.CREATOR.createFromParcel(data);
                    }
                    zza(zzaJ, getServiceRequest);
                    reply.writeNoException();
                    return true;
                case Place.TYPE_HEALTH /*47*/:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    zzaJ = com.google.android.gms.common.internal.zzr.zza.zzaJ(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        validateAccountRequest = (ValidateAccountRequest) ValidateAccountRequest.CREATOR.createFromParcel(data);
                    }
                    zza(zzaJ, validateAccountRequest);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.common.internal.IGmsServiceBroker");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void zza(zzr com_google_android_gms_common_internal_zzr, int i) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, IBinder iBinder, Bundle bundle) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2, String str3, String[] strArr) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2, String[] strArr) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2, String[] strArr, Bundle bundle) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2, String[] strArr, String str3, Bundle bundle) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String str2, String[] strArr, String str3, IBinder iBinder, String str4, Bundle bundle) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, int i, String str, String[] strArr, String str2, Bundle bundle) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, GetServiceRequest getServiceRequest) throws RemoteException;

    void zza(zzr com_google_android_gms_common_internal_zzr, ValidateAccountRequest validateAccountRequest) throws RemoteException;

    void zzb(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzb(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzc(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzc(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzd(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzd(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zze(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zze(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzf(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzf(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzg(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzg(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzh(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzh(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzi(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzi(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzj(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzj(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzk(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzk(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzl(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzl(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzm(zzr com_google_android_gms_common_internal_zzr, int i, String str) throws RemoteException;

    void zzm(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzn(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzo(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzp(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzpp() throws RemoteException;

    void zzq(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzr(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzs(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;

    void zzt(zzr com_google_android_gms_common_internal_zzr, int i, String str, Bundle bundle) throws RemoteException;
}
