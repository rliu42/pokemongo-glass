package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.internal.zzgr;

@zzgr
public class ThinAdSizeParcel extends AdSizeParcel {
    public ThinAdSizeParcel(AdSizeParcel originalAdSize) {
        super(originalAdSize.versionCode, originalAdSize.zzte, originalAdSize.height, originalAdSize.heightPixels, originalAdSize.zztf, originalAdSize.width, originalAdSize.widthPixels, originalAdSize.zztg, originalAdSize.zzth, originalAdSize.zzti);
    }

    public void writeToParcel(Parcel parcel, int flags) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, this.versionCode);
        zzb.zza(parcel, 2, this.zzte, false);
        zzb.zzc(parcel, 3, this.height);
        zzb.zzc(parcel, 6, this.width);
        zzb.zzI(parcel, zzaq);
    }
}
