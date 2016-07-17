package com.google.android.gms.location.places.personalized;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.personalized.internal.TestDataImpl;
import java.util.List;
import spacemadness.com.lunarconsole.C1391R;

public class zze implements Creator<PlaceUserData> {
    static void zza(PlaceUserData placeUserData, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, placeUserData.zzxr(), false);
        zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, placeUserData.mVersionCode);
        zzb.zza(parcel, 2, placeUserData.getPlaceId(), false);
        zzb.zzc(parcel, 5, placeUserData.zzxu(), false);
        zzb.zzc(parcel, 6, placeUserData.zzxs(), false);
        zzb.zzc(parcel, 7, placeUserData.zzxt(), false);
        zzb.zzI(parcel, zzaq);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfd(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzhB(x0);
    }

    public PlaceUserData zzfd(Parcel parcel) {
        List list = null;
        int zzap = zza.zzap(parcel);
        int i = 0;
        List list2 = null;
        List list3 = null;
        String str = null;
        String str2 = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    str2 = zza.zzp(parcel, zzao);
                    break;
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    str = zza.zzp(parcel, zzao);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    list3 = zza.zzc(parcel, zzao, TestDataImpl.CREATOR);
                    break;
                case Place.TYPE_ATM /*6*/:
                    list2 = zza.zzc(parcel, zzao, PlaceAlias.CREATOR);
                    break;
                case Place.TYPE_BAKERY /*7*/:
                    list = zza.zzc(parcel, zzao, HereContent.CREATOR);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i = zza.zzg(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new PlaceUserData(i, str2, str, list3, list2, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzap, parcel);
    }

    public PlaceUserData[] zzhB(int i) {
        return new PlaceUserData[i];
    }
}
