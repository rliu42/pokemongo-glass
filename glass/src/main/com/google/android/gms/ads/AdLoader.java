package com.google.android.gms.ads;

import android.content.Context;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener;
import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd.OnCustomClickListener;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd.OnCustomTemplateAdLoadedListener;
import com.google.android.gms.ads.internal.client.zzc;
import com.google.android.gms.ads.internal.client.zzd;
import com.google.android.gms.ads.internal.client.zzh;
import com.google.android.gms.ads.internal.client.zzp;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzy;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzdb;
import com.google.android.gms.internal.zzdc;
import com.google.android.gms.internal.zzdd;
import com.google.android.gms.internal.zzde;
import com.google.android.gms.internal.zzel;

public class AdLoader {
    private final Context mContext;
    private final zzh zznL;
    private final zzp zznM;

    public static class Builder {
        private final Context mContext;
        private final zzq zznN;

        Builder(Context context, zzq builder) {
            this.mContext = context;
            this.zznN = builder;
        }

        public Builder(Context context, String adUnitID) {
            this(context, zzd.zza(context, adUnitID, new zzel()));
        }

        public AdLoader build() {
            try {
                return new AdLoader(this.mContext, this.zznN.zzbk());
            } catch (Throwable e) {
                zzb.zzb("Failed to build AdLoader.", e);
                return null;
            }
        }

        public Builder forAppInstallAd(OnAppInstallAdLoadedListener listener) {
            try {
                this.zznN.zza(new zzdb(listener));
            } catch (Throwable e) {
                zzb.zzd("Failed to add app install ad listener", e);
            }
            return this;
        }

        public Builder forContentAd(OnContentAdLoadedListener listener) {
            try {
                this.zznN.zza(new zzdc(listener));
            } catch (Throwable e) {
                zzb.zzd("Failed to add content ad listener", e);
            }
            return this;
        }

        public Builder forCustomTemplateAd(String templateId, OnCustomTemplateAdLoadedListener adLoadedListener, OnCustomClickListener customClickListener) {
            try {
                this.zznN.zza(templateId, new zzde(adLoadedListener), customClickListener == null ? null : new zzdd(customClickListener));
            } catch (Throwable e) {
                zzb.zzd("Failed to add custom template ad listener", e);
            }
            return this;
        }

        public Builder withAdListener(AdListener listener) {
            try {
                this.zznN.zzb(new zzc(listener));
            } catch (Throwable e) {
                zzb.zzd("Failed to set AdListener.", e);
            }
            return this;
        }

        public Builder withNativeAdOptions(NativeAdOptions options) {
            try {
                this.zznN.zza(new NativeAdOptionsParcel(options));
            } catch (Throwable e) {
                zzb.zzd("Failed to specify native ad options", e);
            }
            return this;
        }
    }

    AdLoader(Context context, zzp adLoader) {
        this(context, adLoader, zzh.zzcB());
    }

    AdLoader(Context context, zzp adLoader, zzh parcelFactory) {
        this.mContext = context;
        this.zznM = adLoader;
        this.zznL = parcelFactory;
    }

    private void zza(zzy com_google_android_gms_ads_internal_client_zzy) {
        try {
            this.zznM.zzf(this.zznL.zza(this.mContext, com_google_android_gms_ads_internal_client_zzy));
        } catch (Throwable e) {
            zzb.zzb("Failed to load ad.", e);
        }
    }

    public String getMediationAdapterClassName() {
        try {
            return this.zznM.getMediationAdapterClassName();
        } catch (Throwable e) {
            zzb.zzd("Failed to get the mediation adapter class name.", e);
            return null;
        }
    }

    public boolean isLoading() {
        try {
            return this.zznM.isLoading();
        } catch (Throwable e) {
            zzb.zzd("Failed to check if ad is loading.", e);
            return false;
        }
    }

    public void loadAd(AdRequest adRequest) {
        zza(adRequest.zzaF());
    }

    public void loadAd(PublisherAdRequest publisherAdRequest) {
        zza(publisherAdRequest.zzaF());
    }
}
