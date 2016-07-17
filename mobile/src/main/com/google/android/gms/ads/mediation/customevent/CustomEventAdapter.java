package com.google.android.gms.ads.mediation.customevent;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationBannerListener;
import com.google.android.gms.ads.mediation.MediationInterstitialAdapter;
import com.google.android.gms.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.mediation.MediationNativeAdapter;
import com.google.android.gms.ads.mediation.MediationNativeListener;
import com.google.android.gms.ads.mediation.NativeAdMapper;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;

public final class CustomEventAdapter implements MediationBannerAdapter, MediationInterstitialAdapter, MediationNativeAdapter {
    CustomEventBanner zzKL;
    CustomEventInterstitial zzKM;
    CustomEventNative zzKN;
    private View zzaY;

    static final class zza implements CustomEventBannerListener {
        private final CustomEventAdapter zzKO;
        private final MediationBannerListener zzaQ;

        public zza(CustomEventAdapter customEventAdapter, MediationBannerListener mediationBannerListener) {
            this.zzKO = customEventAdapter;
            this.zzaQ = mediationBannerListener;
        }

        public void onAdClicked() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdClicked.");
            this.zzaQ.onAdClicked(this.zzKO);
        }

        public void onAdClosed() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdClosed.");
            this.zzaQ.onAdClosed(this.zzKO);
        }

        public void onAdFailedToLoad(int errorCode) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdFailedToLoad.");
            this.zzaQ.onAdFailedToLoad(this.zzKO, errorCode);
        }

        public void onAdLeftApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdLeftApplication.");
            this.zzaQ.onAdLeftApplication(this.zzKO);
        }

        public void onAdLoaded(View view) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdLoaded.");
            this.zzKO.zza(view);
            this.zzaQ.onAdLoaded(this.zzKO);
        }

        public void onAdOpened() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdOpened.");
            this.zzaQ.onAdOpened(this.zzKO);
        }
    }

    class zzb implements CustomEventInterstitialListener {
        private final CustomEventAdapter zzKO;
        final /* synthetic */ CustomEventAdapter zzKP;
        private final MediationInterstitialListener zzaR;

        public zzb(CustomEventAdapter customEventAdapter, CustomEventAdapter customEventAdapter2, MediationInterstitialListener mediationInterstitialListener) {
            this.zzKP = customEventAdapter;
            this.zzKO = customEventAdapter2;
            this.zzaR = mediationInterstitialListener;
        }

        public void onAdClicked() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdClicked.");
            this.zzaR.onAdClicked(this.zzKO);
        }

        public void onAdClosed() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdClosed.");
            this.zzaR.onAdClosed(this.zzKO);
        }

        public void onAdFailedToLoad(int errorCode) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onFailedToReceiveAd.");
            this.zzaR.onAdFailedToLoad(this.zzKO, errorCode);
        }

        public void onAdLeftApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdLeftApplication.");
            this.zzaR.onAdLeftApplication(this.zzKO);
        }

        public void onAdLoaded() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onReceivedAd.");
            this.zzaR.onAdLoaded(this.zzKP);
        }

        public void onAdOpened() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdOpened.");
            this.zzaR.onAdOpened(this.zzKO);
        }
    }

    static class zzc implements CustomEventNativeListener {
        private final CustomEventAdapter zzKO;
        private final MediationNativeListener zzaS;

        public zzc(CustomEventAdapter customEventAdapter, MediationNativeListener mediationNativeListener) {
            this.zzKO = customEventAdapter;
            this.zzaS = mediationNativeListener;
        }

        public void onAdClicked() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdClicked.");
            this.zzaS.onAdClicked(this.zzKO);
        }

        public void onAdClosed() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdClosed.");
            this.zzaS.onAdClosed(this.zzKO);
        }

        public void onAdFailedToLoad(int errorCode) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdFailedToLoad.");
            this.zzaS.onAdFailedToLoad(this.zzKO, errorCode);
        }

        public void onAdLeftApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdLeftApplication.");
            this.zzaS.onAdLeftApplication(this.zzKO);
        }

        public void onAdLoaded(NativeAdMapper ad) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdLoaded.");
            this.zzaS.onAdLoaded(this.zzKO, ad);
        }

        public void onAdOpened() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Custom event adapter called onAdOpened.");
            this.zzaS.onAdOpened(this.zzKO);
        }
    }

    private void zza(View view) {
        this.zzaY = view;
    }

    private static <T> T zzj(String str) {
        try {
            return Class.forName(str).newInstance();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not instantiate custom event adapter: " + str + ". " + th.getMessage());
            return null;
        }
    }

    public View getBannerView() {
        return this.zzaY;
    }

    public void onDestroy() {
        if (this.zzKL != null) {
            this.zzKL.onDestroy();
        }
        if (this.zzKM != null) {
            this.zzKM.onDestroy();
        }
        if (this.zzKN != null) {
            this.zzKN.onDestroy();
        }
    }

    public void onPause() {
        if (this.zzKL != null) {
            this.zzKL.onPause();
        }
        if (this.zzKM != null) {
            this.zzKM.onPause();
        }
        if (this.zzKN != null) {
            this.zzKN.onPause();
        }
    }

    public void onResume() {
        if (this.zzKL != null) {
            this.zzKL.onResume();
        }
        if (this.zzKM != null) {
            this.zzKM.onResume();
        }
        if (this.zzKN != null) {
            this.zzKN.onResume();
        }
    }

    public void requestBannerAd(Context context, MediationBannerListener listener, Bundle serverParameters, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        this.zzKL = (CustomEventBanner) zzj(serverParameters.getString("class_name"));
        if (this.zzKL == null) {
            listener.onAdFailedToLoad(this, 0);
            return;
        }
        this.zzKL.requestBannerAd(context, new zza(this, listener), serverParameters.getString("parameter"), adSize, mediationAdRequest, customEventExtras == null ? null : customEventExtras.getBundle(serverParameters.getString("class_name")));
    }

    public void requestInterstitialAd(Context context, MediationInterstitialListener listener, Bundle serverParameters, MediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        this.zzKM = (CustomEventInterstitial) zzj(serverParameters.getString("class_name"));
        if (this.zzKM == null) {
            listener.onAdFailedToLoad(this, 0);
            return;
        }
        this.zzKM.requestInterstitialAd(context, zza(listener), serverParameters.getString("parameter"), mediationAdRequest, customEventExtras == null ? null : customEventExtras.getBundle(serverParameters.getString("class_name")));
    }

    public void requestNativeAd(Context context, MediationNativeListener listener, Bundle serverParameters, NativeMediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        this.zzKN = (CustomEventNative) zzj(serverParameters.getString("class_name"));
        if (this.zzKN == null) {
            listener.onAdFailedToLoad(this, 0);
            return;
        }
        this.zzKN.requestNativeAd(context, new zzc(this, listener), serverParameters.getString("parameter"), mediationAdRequest, customEventExtras == null ? null : customEventExtras.getBundle(serverParameters.getString("class_name")));
    }

    public void showInterstitial() {
        this.zzKM.showInterstitial();
    }

    zzb zza(MediationInterstitialListener mediationInterstitialListener) {
        return new zzb(this, this, mediationInterstitialListener);
    }
}
