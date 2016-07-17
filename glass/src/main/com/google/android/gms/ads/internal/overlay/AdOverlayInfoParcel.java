package com.google.android.gms.ads.internal.overlay;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.ads.internal.InterstitialAdParameterParcel;
import com.google.android.gms.ads.internal.client.zza;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzdg;
import com.google.android.gms.internal.zzdm;
import com.google.android.gms.internal.zzgr;
import com.google.android.gms.internal.zziz;

@zzgr
public final class AdOverlayInfoParcel implements SafeParcelable {
    public static final zzf CREATOR;
    public final int orientation;
    public final String url;
    public final int versionCode;
    public final AdLauncherIntentInfoParcel zzBA;
    public final zza zzBB;
    public final zzg zzBC;
    public final zziz zzBD;
    public final zzdg zzBE;
    public final String zzBF;
    public final boolean zzBG;
    public final String zzBH;
    public final zzn zzBI;
    public final int zzBJ;
    public final zzdm zzBK;
    public final String zzBL;
    public final InterstitialAdParameterParcel zzBM;
    public final VersionInfoParcel zzqj;

    static {
        CREATOR = new zzf();
    }

    AdOverlayInfoParcel(int versionCode, AdLauncherIntentInfoParcel adLauncherIntentInfo, IBinder wrappedAdClickListener, IBinder wrappedAdOverlayListener, IBinder wrappedAdWebView, IBinder wrappedAppEventGmsgListener, String baseUrl, boolean customClose, String html, IBinder wrappedLeaveApplicationListener, int orientation, int overlayType, String url, VersionInfoParcel versionInfo, IBinder wrappedInAppPurchaseGmsgListener, String debugMessage, InterstitialAdParameterParcel interstitialAdParameter) {
        this.versionCode = versionCode;
        this.zzBA = adLauncherIntentInfo;
        this.zzBB = (zza) zze.zzp(zzd.zza.zzbk(wrappedAdClickListener));
        this.zzBC = (zzg) zze.zzp(zzd.zza.zzbk(wrappedAdOverlayListener));
        this.zzBD = (zziz) zze.zzp(zzd.zza.zzbk(wrappedAdWebView));
        this.zzBE = (zzdg) zze.zzp(zzd.zza.zzbk(wrappedAppEventGmsgListener));
        this.zzBF = baseUrl;
        this.zzBG = customClose;
        this.zzBH = html;
        this.zzBI = (zzn) zze.zzp(zzd.zza.zzbk(wrappedLeaveApplicationListener));
        this.orientation = orientation;
        this.zzBJ = overlayType;
        this.url = url;
        this.zzqj = versionInfo;
        this.zzBK = (zzdm) zze.zzp(zzd.zza.zzbk(wrappedInAppPurchaseGmsgListener));
        this.zzBL = debugMessage;
        this.zzBM = interstitialAdParameter;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzn leaveApplicationListener, zziz adWebView, int orientation, VersionInfoParcel versionInfo, String debugMessage, InterstitialAdParameterParcel interstitialAdParameter) {
        this.versionCode = 4;
        this.zzBA = null;
        this.zzBB = adClickListener;
        this.zzBC = adOverlayListener;
        this.zzBD = adWebView;
        this.zzBE = null;
        this.zzBF = null;
        this.zzBG = false;
        this.zzBH = null;
        this.zzBI = leaveApplicationListener;
        this.orientation = orientation;
        this.zzBJ = 1;
        this.url = null;
        this.zzqj = versionInfo;
        this.zzBK = null;
        this.zzBL = debugMessage;
        this.zzBM = interstitialAdParameter;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzn leaveApplicationListener, zziz adWebView, boolean customClose, int orientation, VersionInfoParcel versionInfo) {
        this.versionCode = 4;
        this.zzBA = null;
        this.zzBB = adClickListener;
        this.zzBC = adOverlayListener;
        this.zzBD = adWebView;
        this.zzBE = null;
        this.zzBF = null;
        this.zzBG = customClose;
        this.zzBH = null;
        this.zzBI = leaveApplicationListener;
        this.orientation = orientation;
        this.zzBJ = 2;
        this.url = null;
        this.zzqj = versionInfo;
        this.zzBK = null;
        this.zzBL = null;
        this.zzBM = null;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzdg appEventGmsgListener, zzn leaveApplicationListener, zziz adWebView, boolean customClose, int orientation, String url, VersionInfoParcel versionInfo, zzdm inAppPurchaseGmsgListener) {
        this.versionCode = 4;
        this.zzBA = null;
        this.zzBB = adClickListener;
        this.zzBC = adOverlayListener;
        this.zzBD = adWebView;
        this.zzBE = appEventGmsgListener;
        this.zzBF = null;
        this.zzBG = customClose;
        this.zzBH = null;
        this.zzBI = leaveApplicationListener;
        this.orientation = orientation;
        this.zzBJ = 3;
        this.url = url;
        this.zzqj = versionInfo;
        this.zzBK = inAppPurchaseGmsgListener;
        this.zzBL = null;
        this.zzBM = null;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzdg appEventGmsgListener, zzn leaveApplicationListener, zziz adWebView, boolean customClose, int orientation, String html, String baseUrl, VersionInfoParcel versionInfo, zzdm inAppPurchaseGmsgListener) {
        this.versionCode = 4;
        this.zzBA = null;
        this.zzBB = adClickListener;
        this.zzBC = adOverlayListener;
        this.zzBD = adWebView;
        this.zzBE = appEventGmsgListener;
        this.zzBF = baseUrl;
        this.zzBG = customClose;
        this.zzBH = html;
        this.zzBI = leaveApplicationListener;
        this.orientation = orientation;
        this.zzBJ = 3;
        this.url = null;
        this.zzqj = versionInfo;
        this.zzBK = inAppPurchaseGmsgListener;
        this.zzBL = null;
        this.zzBM = null;
    }

    public AdOverlayInfoParcel(AdLauncherIntentInfoParcel adLauncherIntentInfo, zza adClickListener, zzg adOverlayListener, zzn leaveApplicationListener, VersionInfoParcel versionInfo) {
        this.versionCode = 4;
        this.zzBA = adLauncherIntentInfo;
        this.zzBB = adClickListener;
        this.zzBC = adOverlayListener;
        this.zzBD = null;
        this.zzBE = null;
        this.zzBF = null;
        this.zzBG = false;
        this.zzBH = null;
        this.zzBI = leaveApplicationListener;
        this.orientation = -1;
        this.zzBJ = 4;
        this.url = null;
        this.zzqj = versionInfo;
        this.zzBK = null;
        this.zzBL = null;
        this.zzBM = null;
    }

    public static void zza(Intent intent, AdOverlayInfoParcel adOverlayInfoParcel) {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", adOverlayInfoParcel);
        intent.putExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", bundle);
    }

    public static AdOverlayInfoParcel zzb(Intent intent) {
        try {
            Bundle bundleExtra = intent.getBundleExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
            bundleExtra.setClassLoader(AdOverlayInfoParcel.class.getClassLoader());
            return (AdOverlayInfoParcel) bundleExtra.getParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
        } catch (Exception e) {
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }

    IBinder zzeK() {
        return zze.zzy(this.zzBB).asBinder();
    }

    IBinder zzeL() {
        return zze.zzy(this.zzBC).asBinder();
    }

    IBinder zzeM() {
        return zze.zzy(this.zzBD).asBinder();
    }

    IBinder zzeN() {
        return zze.zzy(this.zzBE).asBinder();
    }

    IBinder zzeO() {
        return zze.zzy(this.zzBK).asBinder();
    }

    IBinder zzeP() {
        return zze.zzy(this.zzBI).asBinder();
    }
}
