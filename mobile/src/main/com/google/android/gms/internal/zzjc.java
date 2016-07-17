package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.util.Map;
import org.json.JSONObject;

@zzgr
class zzjc extends FrameLayout implements zziz {
    private final zziz zzKk;
    private final zziy zzKl;

    public zzjc(zziz com_google_android_gms_internal_zziz) {
        super(com_google_android_gms_internal_zziz.zzha());
        this.zzKk = com_google_android_gms_internal_zziz;
        this.zzKl = new zziy(com_google_android_gms_internal_zziz.zzha(), this, this);
        zzja zzhe = this.zzKk.zzhe();
        if (zzhe != null) {
            zzhe.zze(this);
        }
        addView(this.zzKk.getView());
    }

    public void clearCache(boolean includeDiskFiles) {
        this.zzKk.clearCache(includeDiskFiles);
    }

    public void destroy() {
        this.zzKk.destroy();
    }

    public String getRequestId() {
        return this.zzKk.getRequestId();
    }

    public int getRequestedOrientation() {
        return this.zzKk.getRequestedOrientation();
    }

    public View getView() {
        return this;
    }

    public WebView getWebView() {
        return this.zzKk.getWebView();
    }

    public boolean isDestroyed() {
        return this.zzKk.isDestroyed();
    }

    public void loadData(String data, String mimeType, String encoding) {
        this.zzKk.loadData(data, mimeType, encoding);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        this.zzKk.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    public void loadUrl(String url) {
        this.zzKk.loadUrl(url);
    }

    public void setBackgroundColor(int color) {
        this.zzKk.setBackgroundColor(color);
    }

    public void setContext(Context context) {
        this.zzKk.setContext(context);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.zzKk.setOnClickListener(listener);
    }

    public void setOnTouchListener(OnTouchListener listener) {
        this.zzKk.setOnTouchListener(listener);
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.zzKk.setRequestedOrientation(requestedOrientation);
    }

    public void setWebChromeClient(WebChromeClient client) {
        this.zzKk.setWebChromeClient(client);
    }

    public void setWebViewClient(WebViewClient client) {
        this.zzKk.setWebViewClient(client);
    }

    public void stopLoading() {
        this.zzKk.stopLoading();
    }

    public void zzC(boolean z) {
        this.zzKk.zzC(z);
    }

    public void zzD(boolean z) {
        this.zzKk.zzD(z);
    }

    public void zzE(boolean z) {
        this.zzKk.zzE(z);
    }

    public void zza(Context context, AdSizeParcel adSizeParcel, zzcg com_google_android_gms_internal_zzcg) {
        this.zzKk.zza(context, adSizeParcel, com_google_android_gms_internal_zzcg);
    }

    public void zza(AdSizeParcel adSizeParcel) {
        this.zzKk.zza(adSizeParcel);
    }

    public void zza(zzaz com_google_android_gms_internal_zzaz, boolean z) {
        this.zzKk.zza(com_google_android_gms_internal_zzaz, z);
    }

    public void zza(String str, String str2) {
        this.zzKk.zza(str, str2);
    }

    public void zza(String str, JSONObject jSONObject) {
        this.zzKk.zza(str, jSONObject);
    }

    public void zzaI(String str) {
        this.zzKk.zzaI(str);
    }

    public void zzaJ(String str) {
        this.zzKk.zzaJ(str);
    }

    public AdSizeParcel zzaN() {
        return this.zzKk.zzaN();
    }

    public void zzb(zzd com_google_android_gms_ads_internal_overlay_zzd) {
        this.zzKk.zzb(com_google_android_gms_ads_internal_overlay_zzd);
    }

    public void zzb(String str, Map<String, ?> map) {
        this.zzKk.zzb(str, (Map) map);
    }

    public void zzb(String str, JSONObject jSONObject) {
        this.zzKk.zzb(str, jSONObject);
    }

    public void zzc(zzd com_google_android_gms_ads_internal_overlay_zzd) {
        this.zzKk.zzc(com_google_android_gms_ads_internal_overlay_zzd);
    }

    public void zzeJ() {
        this.zzKk.zzeJ();
    }

    public void zzgY() {
        this.zzKk.zzgY();
    }

    public Activity zzgZ() {
        return this.zzKk.zzgZ();
    }

    public Context zzha() {
        return this.zzKk.zzha();
    }

    public com.google.android.gms.ads.internal.zzd zzhb() {
        return this.zzKk.zzhb();
    }

    public zzd zzhc() {
        return this.zzKk.zzhc();
    }

    public zzd zzhd() {
        return this.zzKk.zzhd();
    }

    public zzja zzhe() {
        return this.zzKk.zzhe();
    }

    public boolean zzhf() {
        return this.zzKk.zzhf();
    }

    public zzan zzhg() {
        return this.zzKk.zzhg();
    }

    public VersionInfoParcel zzhh() {
        return this.zzKk.zzhh();
    }

    public boolean zzhi() {
        return this.zzKk.zzhi();
    }

    public void zzhj() {
        this.zzKl.onDestroy();
        this.zzKk.zzhj();
    }

    public boolean zzhk() {
        return this.zzKk.zzhk();
    }

    public zziy zzhl() {
        return this.zzKl;
    }

    public zzce zzhm() {
        return this.zzKk.zzhm();
    }

    public zzcf zzhn() {
        return this.zzKk.zzhn();
    }

    public void zzho() {
        this.zzKk.zzho();
    }

    public void zzhp() {
        this.zzKk.zzhp();
    }

    public void zzv(int i) {
        this.zzKk.zzv(i);
    }
}
