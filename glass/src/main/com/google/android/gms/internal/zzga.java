package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.purchase.InAppPurchase;

@zzgr
public class zzga implements InAppPurchase {
    private final zzfr zzCM;

    public zzga(zzfr com_google_android_gms_internal_zzfr) {
        this.zzCM = com_google_android_gms_internal_zzfr;
    }

    public String getProductId() {
        try {
            return this.zzCM.getProductId();
        } catch (Throwable e) {
            zzb.zzd("Could not forward getProductId to InAppPurchase", e);
            return null;
        }
    }

    public void recordPlayBillingResolution(int billingResponseCode) {
        try {
            this.zzCM.recordPlayBillingResolution(billingResponseCode);
        } catch (Throwable e) {
            zzb.zzd("Could not forward recordPlayBillingResolution to InAppPurchase", e);
        }
    }

    public void recordResolution(int resolution) {
        try {
            this.zzCM.recordResolution(resolution);
        } catch (Throwable e) {
            zzb.zzd("Could not forward recordResolution to InAppPurchase", e);
        }
    }
}
