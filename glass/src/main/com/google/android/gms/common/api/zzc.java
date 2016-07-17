package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import spacemadness.com.lunarconsole.C1391R;

public class zzc implements Creator<Scope> {
    static void zza(Scope scope, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, scope.mVersionCode);
        zzb.zza(parcel, 2, scope.zznG(), false);
        zzb.zzI(parcel, zzaq);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzad(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzbl(x0);
    }

    public Scope zzad(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    i = zza.zzg(parcel, zzao);
                    break;
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    str = zza.zzp(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new Scope(i, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzap, parcel);
    }

    public Scope[] zzbl(int i) {
        return new Scope[i];
    }
}
