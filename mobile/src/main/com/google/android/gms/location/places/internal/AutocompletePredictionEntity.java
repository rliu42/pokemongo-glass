package com.google.android.gms.location.places.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.style.CharacterStyle;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePrediction.Substring;
import java.util.Collections;
import java.util.List;

public class AutocompletePredictionEntity implements SafeParcelable, AutocompletePrediction {
    public static final Creator<AutocompletePredictionEntity> CREATOR;
    private static final List<SubstringEntity> zzaGN;
    final int mVersionCode;
    final List<Integer> zzaFT;
    final String zzaGO;
    final List<SubstringEntity> zzaGP;
    final int zzaGQ;
    final String zzaGR;
    final List<SubstringEntity> zzaGS;
    final String zzaGT;
    final List<SubstringEntity> zzaGU;
    final String zzaGt;

    public static class SubstringEntity implements SafeParcelable, Substring {
        public static final Creator<SubstringEntity> CREATOR;
        final int mLength;
        final int mOffset;
        final int mVersionCode;

        static {
            CREATOR = new zzv();
        }

        public SubstringEntity(int versionCode, int offset, int length) {
            this.mVersionCode = versionCode;
            this.mOffset = offset;
            this.mLength = length;
        }

        public int describeContents() {
            return 0;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof SubstringEntity)) {
                return false;
            }
            SubstringEntity substringEntity = (SubstringEntity) object;
            return zzw.equal(Integer.valueOf(this.mOffset), Integer.valueOf(substringEntity.mOffset)) && zzw.equal(Integer.valueOf(this.mLength), Integer.valueOf(substringEntity.mLength));
        }

        public int getLength() {
            return this.mLength;
        }

        public int getOffset() {
            return this.mOffset;
        }

        public int hashCode() {
            return zzw.hashCode(Integer.valueOf(this.mOffset), Integer.valueOf(this.mLength));
        }

        public String toString() {
            return zzw.zzv(this).zzg("offset", Integer.valueOf(this.mOffset)).zzg("length", Integer.valueOf(this.mLength)).toString();
        }

        public void writeToParcel(Parcel parcel, int flags) {
            zzv.zza(this, parcel, flags);
        }
    }

    static {
        CREATOR = new zza();
        zzaGN = Collections.emptyList();
    }

    AutocompletePredictionEntity(int versionCode, String placeId, List<Integer> placeTypes, int personalizationType, String fullText, List<SubstringEntity> fullTextMatchedSubstrings, String primaryText, List<SubstringEntity> primaryTextMatchedSubstrings, String secondaryText, List<SubstringEntity> secondaryTextMatchedSubstrings) {
        this.mVersionCode = versionCode;
        this.zzaGt = placeId;
        this.zzaFT = placeTypes;
        this.zzaGQ = personalizationType;
        this.zzaGO = fullText;
        this.zzaGP = fullTextMatchedSubstrings;
        this.zzaGR = primaryText;
        this.zzaGS = primaryTextMatchedSubstrings;
        this.zzaGT = secondaryText;
        this.zzaGU = secondaryTextMatchedSubstrings;
    }

    public static AutocompletePredictionEntity zza(String str, List<Integer> list, int i, String str2, List<SubstringEntity> list2, String str3, List<SubstringEntity> list3, String str4, List<SubstringEntity> list4) {
        return new AutocompletePredictionEntity(0, str, list, i, (String) zzx.zzw(str2), list2, str3, list3, str4, list4);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AutocompletePredictionEntity)) {
            return false;
        }
        AutocompletePredictionEntity autocompletePredictionEntity = (AutocompletePredictionEntity) object;
        return zzw.equal(this.zzaGt, autocompletePredictionEntity.zzaGt) && zzw.equal(this.zzaFT, autocompletePredictionEntity.zzaFT) && zzw.equal(Integer.valueOf(this.zzaGQ), Integer.valueOf(autocompletePredictionEntity.zzaGQ)) && zzw.equal(this.zzaGO, autocompletePredictionEntity.zzaGO) && zzw.equal(this.zzaGP, autocompletePredictionEntity.zzaGP) && zzw.equal(this.zzaGR, autocompletePredictionEntity.zzaGR) && zzw.equal(this.zzaGS, autocompletePredictionEntity.zzaGS) && zzw.equal(this.zzaGT, autocompletePredictionEntity.zzaGT) && zzw.equal(this.zzaGU, autocompletePredictionEntity.zzaGU);
    }

    public /* synthetic */ Object freeze() {
        return zzwV();
    }

    public String getDescription() {
        return this.zzaGO;
    }

    public CharSequence getFullText(CharacterStyle matchStyle) {
        return zzc.zza(this.zzaGO, this.zzaGP, matchStyle);
    }

    public List<? extends Substring> getMatchedSubstrings() {
        return this.zzaGP;
    }

    public String getPlaceId() {
        return this.zzaGt;
    }

    public List<Integer> getPlaceTypes() {
        return this.zzaFT;
    }

    public CharSequence getPrimaryText(CharacterStyle matchStyle) {
        return zzc.zza(this.zzaGR, this.zzaGS, matchStyle);
    }

    public CharSequence getSecondaryText(CharacterStyle matchStyle) {
        return zzc.zza(this.zzaGT, this.zzaGU, matchStyle);
    }

    public int hashCode() {
        return zzw.hashCode(this.zzaGt, this.zzaFT, Integer.valueOf(this.zzaGQ), this.zzaGO, this.zzaGP, this.zzaGR, this.zzaGS, this.zzaGT, this.zzaGU);
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return zzw.zzv(this).zzg("placeId", this.zzaGt).zzg("placeTypes", this.zzaFT).zzg("fullText", this.zzaGO).zzg("fullTextMatchedSubstrings", this.zzaGP).zzg("primaryText", this.zzaGR).zzg("primaryTextMatchedSubstrings", this.zzaGS).zzg("secondaryText", this.zzaGT).zzg("secondaryTextMatchedSubstrings", this.zzaGU).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zza.zza(this, parcel, flags);
    }

    public AutocompletePrediction zzwV() {
        return this;
    }
}
