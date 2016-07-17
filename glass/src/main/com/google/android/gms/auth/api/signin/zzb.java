package com.google.android.gms.auth.api.signin;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import java.util.ArrayList;
import spacemadness.com.lunarconsole.C1391R;

public class zzb implements Creator<FacebookSignInConfig> {
    static void zza(FacebookSignInConfig facebookSignInConfig, Parcel parcel, int i) {
        int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, facebookSignInConfig.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, facebookSignInConfig.zzlR(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 3, facebookSignInConfig.zzlS(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzP(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzaG(x0);
    }

    public FacebookSignInConfig zzP(Parcel parcel) {
        ArrayList arrayList = null;
        int zzap = zza.zzap(parcel);
        int i = 0;
        Intent intent = null;
        while (parcel.dataPosition() < zzap) {
            Intent intent2;
            int zzg;
            ArrayList arrayList2;
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    ArrayList arrayList3 = arrayList;
                    intent2 = intent;
                    zzg = zza.zzg(parcel, zzao);
                    arrayList2 = arrayList3;
                    break;
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    zzg = i;
                    Intent intent3 = (Intent) zza.zza(parcel, zzao, Intent.CREATOR);
                    arrayList2 = arrayList;
                    intent2 = intent3;
                    break;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    arrayList2 = zza.zzD(parcel, zzao);
                    intent2 = intent;
                    zzg = i;
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    arrayList2 = arrayList;
                    intent2 = intent;
                    zzg = i;
                    break;
            }
            i = zzg;
            intent = intent2;
            arrayList = arrayList2;
        }
        if (parcel.dataPosition() == zzap) {
            return new FacebookSignInConfig(i, intent, arrayList);
        }
        throw new zza.zza("Overread allowed size end=" + zzap, parcel);
    }

    public FacebookSignInConfig[] zzaG(int i) {
        return new FacebookSignInConfig[i];
    }
}
