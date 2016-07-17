package com.google.android.gms.ads.internal.client;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zzgr;
import java.util.List;

@zzgr
public final class AdRequestParcel implements SafeParcelable {
    public static final zzg CREATOR;
    public final Bundle extras;
    public final int versionCode;
    public final long zzsB;
    public final int zzsC;
    public final List<String> zzsD;
    public final boolean zzsE;
    public final int zzsF;
    public final boolean zzsG;
    public final String zzsH;
    public final SearchAdRequestParcel zzsI;
    public final Location zzsJ;
    public final String zzsK;
    public final Bundle zzsL;
    public final Bundle zzsM;
    public final List<String> zzsN;
    public final String zzsO;
    public final String zzsP;

    static {
        CREATOR = new zzg();
    }

    public AdRequestParcel(int versionCode, long birthday, Bundle extras, int gender, List<String> keywords, boolean isTestDevice, int tagForChildDirectedTreatment, boolean manualImpressionsEnabled, String publisherProvidedId, SearchAdRequestParcel searchAdRequestParcel, Location location, String contentUrl, Bundle networkExtras, Bundle customTargeting, List<String> categoryExclusions, String requestAgent, String requestPackage) {
        this.versionCode = versionCode;
        this.zzsB = birthday;
        if (extras == null) {
            extras = new Bundle();
        }
        this.extras = extras;
        this.zzsC = gender;
        this.zzsD = keywords;
        this.zzsE = isTestDevice;
        this.zzsF = tagForChildDirectedTreatment;
        this.zzsG = manualImpressionsEnabled;
        this.zzsH = publisherProvidedId;
        this.zzsI = searchAdRequestParcel;
        this.zzsJ = location;
        this.zzsK = contentUrl;
        this.zzsL = networkExtras;
        this.zzsM = customTargeting;
        this.zzsN = categoryExclusions;
        this.zzsO = requestAgent;
        this.zzsP = requestPackage;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (!(other instanceof AdRequestParcel)) {
            return false;
        }
        AdRequestParcel adRequestParcel = (AdRequestParcel) other;
        return this.versionCode == adRequestParcel.versionCode && this.zzsB == adRequestParcel.zzsB && zzw.equal(this.extras, adRequestParcel.extras) && this.zzsC == adRequestParcel.zzsC && zzw.equal(this.zzsD, adRequestParcel.zzsD) && this.zzsE == adRequestParcel.zzsE && this.zzsF == adRequestParcel.zzsF && this.zzsG == adRequestParcel.zzsG && zzw.equal(this.zzsH, adRequestParcel.zzsH) && zzw.equal(this.zzsI, adRequestParcel.zzsI) && zzw.equal(this.zzsJ, adRequestParcel.zzsJ) && zzw.equal(this.zzsK, adRequestParcel.zzsK) && zzw.equal(this.zzsL, adRequestParcel.zzsL) && zzw.equal(this.zzsM, adRequestParcel.zzsM) && zzw.equal(this.zzsN, adRequestParcel.zzsN) && zzw.equal(this.zzsO, adRequestParcel.zzsO) && zzw.equal(this.zzsP, adRequestParcel.zzsP);
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.versionCode), Long.valueOf(this.zzsB), this.extras, Integer.valueOf(this.zzsC), this.zzsD, Boolean.valueOf(this.zzsE), Integer.valueOf(this.zzsF), Boolean.valueOf(this.zzsG), this.zzsH, this.zzsI, this.zzsJ, this.zzsK, this.zzsL, this.zzsM, this.zzsN, this.zzsO, this.zzsP);
    }

    public void writeToParcel(Parcel out, int flags) {
        zzg.zza(this, out, flags);
    }
}
