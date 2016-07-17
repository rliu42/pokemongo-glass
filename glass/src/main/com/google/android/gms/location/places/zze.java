package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.location.LocationStatusCodes;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import spacemadness.com.lunarconsole.C1391R;

public class zze implements Creator<NearbyAlertRequest> {
    static void zza(NearbyAlertRequest nearbyAlertRequest, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, nearbyAlertRequest.zzwK());
        zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, nearbyAlertRequest.getVersionCode());
        zzb.zzc(parcel, 2, nearbyAlertRequest.zzwN());
        zzb.zza(parcel, 3, nearbyAlertRequest.zzwO(), i, false);
        zzb.zza(parcel, 4, nearbyAlertRequest.zzwP(), i, false);
        zzb.zza(parcel, 5, nearbyAlertRequest.zzwQ());
        zzb.zzc(parcel, 6, nearbyAlertRequest.zzwR());
        zzb.zzI(parcel, zzaq);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzeN(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzhi(x0);
    }

    public NearbyAlertRequest zzeN(Parcel parcel) {
        NearbyAlertFilter nearbyAlertFilter = null;
        int i = 0;
        int zzap = zza.zzap(parcel);
        int i2 = -1;
        boolean z = false;
        PlaceFilter placeFilter = null;
        int i3 = 0;
        int i4 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    i3 = zza.zzg(parcel, zzao);
                    break;
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    i2 = zza.zzg(parcel, zzao);
                    break;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    placeFilter = (PlaceFilter) zza.zza(parcel, zzao, PlaceFilter.CREATOR);
                    break;
                case Place.TYPE_AQUARIUM /*4*/:
                    nearbyAlertFilter = (NearbyAlertFilter) zza.zza(parcel, zzao, NearbyAlertFilter.CREATOR);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    z = zza.zzc(parcel, zzao);
                    break;
                case Place.TYPE_ATM /*6*/:
                    i = zza.zzg(parcel, zzao);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i4 = zza.zzg(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new NearbyAlertRequest(i4, i3, i2, placeFilter, nearbyAlertFilter, z, i);
        }
        throw new zza.zza("Overread allowed size end=" + zzap, parcel);
    }

    public NearbyAlertRequest[] zzhi(int i) {
        return new NearbyAlertRequest[i];
    }
}
