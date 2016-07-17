package com.google.android.gms.ads.internal.request;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgr;
import java.util.Collections;
import java.util.List;

@zzgr
public final class AdResponseParcel implements SafeParcelable {
    public static final zzh CREATOR;
    public String body;
    public final int errorCode;
    public final int orientation;
    public final int versionCode;
    public final String zzBF;
    public final long zzEJ;
    public final boolean zzEK;
    public final long zzEL;
    public final List<String> zzEM;
    public final String zzEN;
    public final long zzEO;
    public final String zzEP;
    public final boolean zzEQ;
    public final String zzER;
    public final String zzES;
    public final boolean zzET;
    public final boolean zzEU;
    public final boolean zzEV;
    public final int zzEW;
    public LargeParcelTeleporter zzEX;
    public String zzEY;
    public String zzEZ;
    public final boolean zzEv;
    public final boolean zzth;
    public boolean zzti;
    public final List<String> zzyY;
    public final List<String> zzyZ;
    public final long zzzc;
    private AdRequestInfoParcel zzzz;

    static {
        CREATOR = new zzh();
    }

    public AdResponseParcel(int errorCode) {
        this(14, null, null, null, errorCode, null, -1, false, -1, null, -1, -1, null, -1, null, false, null, null, false, false, false, true, false, 0, null, null, null, false);
    }

    public AdResponseParcel(int errorCode, long refreshIntervalInMillis) {
        this(14, null, null, null, errorCode, null, -1, false, -1, null, refreshIntervalInMillis, -1, null, -1, null, false, null, null, false, false, false, true, false, 0, null, null, null, false);
    }

    AdResponseParcel(int versionCode, String baseUrl, String body, List<String> clickUrls, int errorCode, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, boolean isJavascriptTag, String passbackUrl, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus, LargeParcelTeleporter bodyTeleporter, String csiLatencyInfo, String gwsQueryId, boolean isFluid) {
        this.versionCode = versionCode;
        this.zzBF = baseUrl;
        this.body = body;
        this.zzyY = clickUrls != null ? Collections.unmodifiableList(clickUrls) : null;
        this.errorCode = errorCode;
        this.zzyZ = impressionUrls != null ? Collections.unmodifiableList(impressionUrls) : null;
        this.zzEJ = interstitialTimeoutInMillis;
        this.zzEK = isMediation;
        this.zzEL = mediationConfigCacheTimeInMillis;
        this.zzEM = manualTrackingUrls != null ? Collections.unmodifiableList(manualTrackingUrls) : null;
        this.zzzc = refreshIntervalInMillis;
        this.orientation = orientation;
        this.zzEN = adSizeString;
        this.zzEO = fetchTime;
        this.zzEP = debugDialog;
        this.zzEQ = isJavascriptTag;
        this.zzER = passbackUrl;
        this.zzES = activeViewJSON;
        this.zzET = isCustomRenderAllowed;
        this.zzth = isNative;
        this.zzEv = useHTTPS;
        this.zzEU = contentUrlOptedOut;
        this.zzEV = isPrefetched;
        this.zzEW = panTokenStatus;
        this.zzEX = bodyTeleporter;
        this.zzEY = csiLatencyInfo;
        this.zzEZ = gwsQueryId;
        if (this.body == null && this.zzEX != null) {
            StringParcel stringParcel = (StringParcel) this.zzEX.zza(StringParcel.CREATOR);
            if (!(stringParcel == null || TextUtils.isEmpty(stringParcel.zzfP()))) {
                this.body = stringParcel.zzfP();
            }
        }
        this.zzti = isFluid;
    }

    public AdResponseParcel(AdRequestInfoParcel adRequestInfo, String baseUrl, String body, List<String> clickUrls, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus, String gwsQueryId, boolean isFluid) {
        this(14, baseUrl, body, clickUrls, -2, impressionUrls, interstitialTimeoutInMillis, isMediation, mediationConfigCacheTimeInMillis, manualTrackingUrls, refreshIntervalInMillis, orientation, adSizeString, fetchTime, debugDialog, false, null, activeViewJSON, isCustomRenderAllowed, isNative, useHTTPS, contentUrlOptedOut, isPrefetched, panTokenStatus, null, null, gwsQueryId, isFluid);
        this.zzzz = adRequestInfo;
    }

    public AdResponseParcel(AdRequestInfoParcel adRequestInfo, String baseUrl, String body, List<String> clickUrls, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, boolean isJavascriptTag, String passbackUrl, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus, String gwsQueryId, boolean isFluid) {
        this(14, baseUrl, body, clickUrls, -2, impressionUrls, interstitialTimeoutInMillis, isMediation, mediationConfigCacheTimeInMillis, manualTrackingUrls, refreshIntervalInMillis, orientation, adSizeString, fetchTime, debugDialog, isJavascriptTag, passbackUrl, activeViewJSON, isCustomRenderAllowed, isNative, useHTTPS, contentUrlOptedOut, isPrefetched, panTokenStatus, null, null, gwsQueryId, isFluid);
        this.zzzz = adRequestInfo;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (!(this.zzzz == null || this.zzzz.versionCode < 9 || TextUtils.isEmpty(this.body))) {
            this.zzEX = new LargeParcelTeleporter(new StringParcel(this.body));
            this.body = null;
        }
        zzh.zza(this, out, flags);
    }
}
