package com.google.android.gms.location.places.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.internal.AutocompletePredictionEntity.SubstringEntity;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.nianticproject.holoholo.sfida.constants.BluetoothGattSupport;
import java.util.List;
import spacemadness.com.lunarconsole.C1391R;

public class zza implements Creator<AutocompletePredictionEntity> {
    static void zza(AutocompletePredictionEntity autocompletePredictionEntity, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, autocompletePredictionEntity.zzaGO, false);
        zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, autocompletePredictionEntity.mVersionCode);
        zzb.zza(parcel, 2, autocompletePredictionEntity.zzaGt, false);
        zzb.zza(parcel, 3, autocompletePredictionEntity.zzaFT, false);
        zzb.zzc(parcel, 4, autocompletePredictionEntity.zzaGP, false);
        zzb.zzc(parcel, 5, autocompletePredictionEntity.zzaGQ);
        zzb.zza(parcel, 6, autocompletePredictionEntity.zzaGR, false);
        zzb.zzc(parcel, 7, autocompletePredictionEntity.zzaGS, false);
        zzb.zza(parcel, 8, autocompletePredictionEntity.zzaGT, false);
        zzb.zzc(parcel, 9, autocompletePredictionEntity.zzaGU, false);
        zzb.zzI(parcel, zzaq);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzeU(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzhr(x0);
    }

    public AutocompletePredictionEntity zzeU(Parcel parcel) {
        int i = 0;
        List list = null;
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String str = null;
        List list2 = null;
        String str2 = null;
        List list3 = null;
        String str3 = null;
        List list4 = null;
        String str4 = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    str3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    break;
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    str4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    break;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    list4 = com.google.android.gms.common.internal.safeparcel.zza.zzC(parcel, zzao);
                    break;
                case Place.TYPE_AQUARIUM /*4*/:
                    list3 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao, SubstringEntity.CREATOR);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    break;
                case Place.TYPE_ATM /*6*/:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    break;
                case Place.TYPE_BAKERY /*7*/:
                    list2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao, SubstringEntity.CREATOR);
                    break;
                case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    break;
                case Place.TYPE_BAR /*9*/:
                    list = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao, SubstringEntity.CREATOR);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new AutocompletePredictionEntity(i2, str4, list4, i, str3, list3, str2, list2, str, list);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzap, parcel);
    }

    public AutocompletePredictionEntity[] zzhr(int i) {
        return new AutocompletePredictionEntity[i];
    }
}
