package com.google.android.gms.ads.internal.request;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Messenger;
import android.os.Parcel;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgr;
import java.util.Collections;
import java.util.List;

@zzgr
public final class AdRequestInfoParcel implements SafeParcelable {
    public static final zzf CREATOR;
    public final ApplicationInfo applicationInfo;
    public final int versionCode;
    public final String zzEA;
    public final long zzEB;
    public final String zzEC;
    public final List<String> zzED;
    public final List<String> zzEE;
    public final long zzEF;
    public final CapabilityParcel zzEG;
    public final String zzEH;
    public final Bundle zzEm;
    public final AdRequestParcel zzEn;
    public final PackageInfo zzEo;
    public final String zzEp;
    public final String zzEq;
    public final String zzEr;
    public final Bundle zzEs;
    public final int zzEt;
    public final Bundle zzEu;
    public final boolean zzEv;
    public final Messenger zzEw;
    public final int zzEx;
    public final int zzEy;
    public final float zzEz;
    public final NativeAdOptionsParcel zzqB;
    public final List<String> zzqD;
    public final String zzqg;
    public final String zzqh;
    public final VersionInfoParcel zzqj;
    public final AdSizeParcel zzqn;

    @zzgr
    public static final class zza {
        public final ApplicationInfo applicationInfo;
        public final String zzEA;
        public final long zzEB;
        public final String zzEC;
        public final List<String> zzED;
        public final List<String> zzEE;
        public final CapabilityParcel zzEG;
        public final String zzEH;
        public final Bundle zzEm;
        public final AdRequestParcel zzEn;
        public final PackageInfo zzEo;
        public final String zzEq;
        public final String zzEr;
        public final Bundle zzEs;
        public final int zzEt;
        public final Bundle zzEu;
        public final boolean zzEv;
        public final Messenger zzEw;
        public final int zzEx;
        public final int zzEy;
        public final float zzEz;
        public final NativeAdOptionsParcel zzqB;
        public final List<String> zzqD;
        public final String zzqg;
        public final String zzqh;
        public final VersionInfoParcel zzqj;
        public final AdSizeParcel zzqn;

        public zza(Bundle bundle, AdRequestParcel adRequestParcel, AdSizeParcel adSizeParcel, String str, ApplicationInfo applicationInfo, PackageInfo packageInfo, String str2, String str3, VersionInfoParcel versionInfoParcel, Bundle bundle2, List<String> list, List<String> list2, Bundle bundle3, boolean z, Messenger messenger, int i, int i2, float f, String str4, long j, String str5, List<String> list3, String str6, NativeAdOptionsParcel nativeAdOptionsParcel, CapabilityParcel capabilityParcel, String str7) {
            this.zzEm = bundle;
            this.zzEn = adRequestParcel;
            this.zzqn = adSizeParcel;
            this.zzqh = str;
            this.applicationInfo = applicationInfo;
            this.zzEo = packageInfo;
            this.zzEq = str2;
            this.zzEr = str3;
            this.zzqj = versionInfoParcel;
            this.zzEs = bundle2;
            this.zzEv = z;
            this.zzEw = messenger;
            this.zzEx = i;
            this.zzEy = i2;
            this.zzEz = f;
            if (list == null || list.size() <= 0) {
                this.zzEt = 0;
                this.zzqD = null;
                this.zzEE = null;
            } else {
                this.zzEt = 3;
                this.zzqD = list;
                this.zzEE = list2;
            }
            this.zzEu = bundle3;
            this.zzEA = str4;
            this.zzEB = j;
            this.zzEC = str5;
            this.zzED = list3;
            this.zzqg = str6;
            this.zzqB = nativeAdOptionsParcel;
            this.zzEG = capabilityParcel;
            this.zzEH = str7;
        }
    }

    static {
        CREATOR = new zzf();
    }

    AdRequestInfoParcel(int versionCode, Bundle adPositionBundle, AdRequestParcel adRequest, AdSizeParcel adSize, String adUnitId, ApplicationInfo applicationInfo, PackageInfo packageInfo, String querySpamSignals, String sequenceNumber, String sessionId, VersionInfoParcel versionInfo, Bundle stats, int nativeVersion, List<String> nativeTemplates, Bundle contentInfo, boolean useHTTPS, Messenger prefetchMessenger, int screenWidth, int screenHeight, float screenDensity, String viewHierarchy, long correlationId, String requestId, List<String> experimentIds, String slotId, NativeAdOptionsParcel nativeAdOptions, List<String> nativeCustomTemplateIds, long connectionStartTime, CapabilityParcel capabilityParcel, String anchorStatus) {
        this.versionCode = versionCode;
        this.zzEm = adPositionBundle;
        this.zzEn = adRequest;
        this.zzqn = adSize;
        this.zzqh = adUnitId;
        this.applicationInfo = applicationInfo;
        this.zzEo = packageInfo;
        this.zzEp = querySpamSignals;
        this.zzEq = sequenceNumber;
        this.zzEr = sessionId;
        this.zzqj = versionInfo;
        this.zzEs = stats;
        this.zzEt = nativeVersion;
        this.zzqD = nativeTemplates;
        this.zzEE = nativeCustomTemplateIds == null ? Collections.emptyList() : Collections.unmodifiableList(nativeCustomTemplateIds);
        this.zzEu = contentInfo;
        this.zzEv = useHTTPS;
        this.zzEw = prefetchMessenger;
        this.zzEx = screenWidth;
        this.zzEy = screenHeight;
        this.zzEz = screenDensity;
        this.zzEA = viewHierarchy;
        this.zzEB = correlationId;
        this.zzEC = requestId;
        this.zzED = experimentIds == null ? Collections.emptyList() : Collections.unmodifiableList(experimentIds);
        this.zzqg = slotId;
        this.zzqB = nativeAdOptions;
        this.zzEF = connectionStartTime;
        this.zzEG = capabilityParcel;
        this.zzEH = anchorStatus;
    }

    public AdRequestInfoParcel(Bundle adPositionBundle, AdRequestParcel adRequest, AdSizeParcel adSize, String adUnitId, ApplicationInfo applicationInfo, PackageInfo packageInfo, String querySpamSignals, String sequenceNumber, String sessionId, VersionInfoParcel versionInfo, Bundle stats, int nativeVersion, List<String> nativeTemplates, List<String> nativeCustomTemplateIds, Bundle contentInfo, boolean useHTTPS, Messenger prefetchMessenger, int screenWidth, int screenHeight, float screenDensity, String viewHierarchy, long correlationId, String requestId, List<String> experimentIds, String slotId, NativeAdOptionsParcel nativeAdOptionsParcel, long connectionStartTime, CapabilityParcel capabilityParcel, String anchorStatus) {
        this(12, adPositionBundle, adRequest, adSize, adUnitId, applicationInfo, packageInfo, querySpamSignals, sequenceNumber, sessionId, versionInfo, stats, nativeVersion, nativeTemplates, contentInfo, useHTTPS, prefetchMessenger, screenWidth, screenHeight, screenDensity, viewHierarchy, correlationId, requestId, experimentIds, slotId, nativeAdOptionsParcel, nativeCustomTemplateIds, connectionStartTime, capabilityParcel, anchorStatus);
    }

    public AdRequestInfoParcel(zza partialAdRequestInfo, String querySpamSignals, long connectionStartTime) {
        String str = querySpamSignals;
        long j = connectionStartTime;
        this(partialAdRequestInfo.zzEm, partialAdRequestInfo.zzEn, partialAdRequestInfo.zzqn, partialAdRequestInfo.zzqh, partialAdRequestInfo.applicationInfo, partialAdRequestInfo.zzEo, str, partialAdRequestInfo.zzEq, partialAdRequestInfo.zzEr, partialAdRequestInfo.zzqj, partialAdRequestInfo.zzEs, partialAdRequestInfo.zzEt, partialAdRequestInfo.zzqD, partialAdRequestInfo.zzEE, partialAdRequestInfo.zzEu, partialAdRequestInfo.zzEv, partialAdRequestInfo.zzEw, partialAdRequestInfo.zzEx, partialAdRequestInfo.zzEy, partialAdRequestInfo.zzEz, partialAdRequestInfo.zzEA, partialAdRequestInfo.zzEB, partialAdRequestInfo.zzEC, partialAdRequestInfo.zzED, partialAdRequestInfo.zzqg, partialAdRequestInfo.zzqB, j, partialAdRequestInfo.zzEG, partialAdRequestInfo.zzEH);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }
}
