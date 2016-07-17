package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgr;

@zzgr
public final class SearchAdRequestParcel implements SafeParcelable {
    public static final zzae CREATOR;
    public final int backgroundColor;
    public final int versionCode;
    public final int zztP;
    public final int zztQ;
    public final int zztR;
    public final int zztS;
    public final int zztT;
    public final int zztU;
    public final int zztV;
    public final String zztW;
    public final int zztX;
    public final String zztY;
    public final int zztZ;
    public final int zzua;
    public final String zzub;

    static {
        CREATOR = new zzae();
    }

    SearchAdRequestParcel(int versionCode, int anchorTextColor, int backgroundColor, int backgroundGradientBottom, int backgroundGradientTop, int borderColor, int borderThickness, int borderType, int callButtonColor, String channels, int descriptionTextColor, String fontFace, int headerTextColor, int headerTextSize, String query) {
        this.versionCode = versionCode;
        this.zztP = anchorTextColor;
        this.backgroundColor = backgroundColor;
        this.zztQ = backgroundGradientBottom;
        this.zztR = backgroundGradientTop;
        this.zztS = borderColor;
        this.zztT = borderThickness;
        this.zztU = borderType;
        this.zztV = callButtonColor;
        this.zztW = channels;
        this.zztX = descriptionTextColor;
        this.zztY = fontFace;
        this.zztZ = headerTextColor;
        this.zzua = headerTextSize;
        this.zzub = query;
    }

    public SearchAdRequestParcel(SearchAdRequest searchAdRequest) {
        this.versionCode = 1;
        this.zztP = searchAdRequest.getAnchorTextColor();
        this.backgroundColor = searchAdRequest.getBackgroundColor();
        this.zztQ = searchAdRequest.getBackgroundGradientBottom();
        this.zztR = searchAdRequest.getBackgroundGradientTop();
        this.zztS = searchAdRequest.getBorderColor();
        this.zztT = searchAdRequest.getBorderThickness();
        this.zztU = searchAdRequest.getBorderType();
        this.zztV = searchAdRequest.getCallButtonColor();
        this.zztW = searchAdRequest.getCustomChannels();
        this.zztX = searchAdRequest.getDescriptionTextColor();
        this.zztY = searchAdRequest.getFontFace();
        this.zztZ = searchAdRequest.getHeaderTextColor();
        this.zzua = searchAdRequest.getHeaderTextSize();
        this.zzub = searchAdRequest.getQuery();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzae.zza(this, out, flags);
    }
}
