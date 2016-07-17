package com.google.android.gms.internal;

import android.content.Context;
import android.location.Location;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.request.zzj.zza;
import com.google.android.gms.ads.internal.request.zzk;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzdz.zzd;
import com.google.android.gms.internal.zzis.zzc;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

@zzgr
public final class zzgt extends zza {
    private static zzgt zzFA;
    private static final Object zzpy;
    private final Context mContext;
    private final zzgs zzFB;
    private final zzbr zzFC;
    private final zzdz zzrf;

    /* renamed from: com.google.android.gms.internal.zzgt.1 */
    static class C05491 implements Runnable {
        final /* synthetic */ zzdz zzFD;
        final /* synthetic */ zzgv zzFE;
        final /* synthetic */ zzce zzFF;
        final /* synthetic */ String zzFG;
        final /* synthetic */ zzcg zzoD;

        /* renamed from: com.google.android.gms.internal.zzgt.1.1 */
        class C05471 implements zzc<zzbe> {
            final /* synthetic */ zzce zzFH;
            final /* synthetic */ C05491 zzFI;

            C05471(C05491 c05491, zzce com_google_android_gms_internal_zzce) {
                this.zzFI = c05491;
                this.zzFH = com_google_android_gms_internal_zzce;
            }

            public void zzb(zzbe com_google_android_gms_internal_zzbe) {
                this.zzFI.zzoD.zza(this.zzFH, "jsf");
                this.zzFI.zzoD.zzdo();
                com_google_android_gms_internal_zzbe.zza("/invalidRequest", this.zzFI.zzFE.zzFR);
                com_google_android_gms_internal_zzbe.zza("/loadAdURL", this.zzFI.zzFE.zzFS);
                try {
                    com_google_android_gms_internal_zzbe.zza("AFMA_buildAdURL", this.zzFI.zzFG);
                } catch (Throwable e) {
                    zzb.zzb("Error requesting an ad url", e);
                }
            }

            public /* synthetic */ void zzc(Object obj) {
                zzb((zzbe) obj);
            }
        }

        /* renamed from: com.google.android.gms.internal.zzgt.1.2 */
        class C05482 implements zzis.zza {
            final /* synthetic */ C05491 zzFI;

            C05482(C05491 c05491) {
                this.zzFI = c05491;
            }

            public void run() {
            }
        }

        C05491(zzdz com_google_android_gms_internal_zzdz, zzgv com_google_android_gms_internal_zzgv, zzcg com_google_android_gms_internal_zzcg, zzce com_google_android_gms_internal_zzce, String str) {
            this.zzFD = com_google_android_gms_internal_zzdz;
            this.zzFE = com_google_android_gms_internal_zzgv;
            this.zzoD = com_google_android_gms_internal_zzcg;
            this.zzFF = com_google_android_gms_internal_zzce;
            this.zzFG = str;
        }

        public void run() {
            zzd zzdO = this.zzFD.zzdO();
            this.zzFE.zzb(zzdO);
            this.zzoD.zza(this.zzFF, "rwc");
            zzdO.zza(new C05471(this, this.zzoD.zzdn()), new C05482(this));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzgt.2 */
    static class C05502 implements Runnable {
        final /* synthetic */ zzgv zzFE;
        final /* synthetic */ zzce zzFF;
        final /* synthetic */ String zzFG;
        final /* synthetic */ AdRequestInfoParcel zzFJ;
        final /* synthetic */ zzbr zzFK;
        final /* synthetic */ zzcg zzoD;
        final /* synthetic */ Context zzry;

        C05502(Context context, AdRequestInfoParcel adRequestInfoParcel, zzgv com_google_android_gms_internal_zzgv, zzcg com_google_android_gms_internal_zzcg, zzce com_google_android_gms_internal_zzce, String str, zzbr com_google_android_gms_internal_zzbr) {
            this.zzry = context;
            this.zzFJ = adRequestInfoParcel;
            this.zzFE = com_google_android_gms_internal_zzgv;
            this.zzoD = com_google_android_gms_internal_zzcg;
            this.zzFF = com_google_android_gms_internal_zzce;
            this.zzFG = str;
            this.zzFK = com_google_android_gms_internal_zzbr;
        }

        public void run() {
            zziz zza = zzp.zzbw().zza(this.zzry, new AdSizeParcel(), false, false, null, this.zzFJ.zzqj);
            if (zzp.zzby().zzgu()) {
                zza.clearCache(true);
            }
            zza.getWebView().setWillNotDraw(true);
            this.zzFE.zze(zza);
            this.zzoD.zza(this.zzFF, "rwc");
            zzja.zza zzb = zzgt.zza(this.zzFG, this.zzoD, this.zzoD.zzdn());
            zzja zzhe = zza.zzhe();
            zzhe.zza("/invalidRequest", this.zzFE.zzFR);
            zzhe.zza("/loadAdURL", this.zzFE.zzFS);
            zzhe.zza("/log", zzdj.zzxv);
            zzhe.zza(zzb);
            zzb.zzaF("Loading the JS library.");
            zza.loadUrl(this.zzFK.zzdc());
        }
    }

    /* renamed from: com.google.android.gms.internal.zzgt.3 */
    static class C05513 implements Runnable {
        final /* synthetic */ zzgv zzFE;

        C05513(zzgv com_google_android_gms_internal_zzgv) {
            this.zzFE = com_google_android_gms_internal_zzgv;
        }

        public void run() {
            this.zzFE.zzfT();
            if (this.zzFE.zzfR() != null) {
                this.zzFE.zzfR().release();
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzgt.4 */
    static class C05524 implements zzja.zza {
        final /* synthetic */ String zzFG;
        final /* synthetic */ zzce zzFH;
        final /* synthetic */ zzcg zzoD;

        C05524(zzcg com_google_android_gms_internal_zzcg, zzce com_google_android_gms_internal_zzce, String str) {
            this.zzoD = com_google_android_gms_internal_zzcg;
            this.zzFH = com_google_android_gms_internal_zzce;
            this.zzFG = str;
        }

        public void zza(zziz com_google_android_gms_internal_zziz, boolean z) {
            this.zzoD.zza(this.zzFH, "jsf");
            this.zzoD.zzdo();
            com_google_android_gms_internal_zziz.zza("AFMA_buildAdURL", this.zzFG);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzgt.5 */
    class C05535 implements zzdz.zzb<zzbb> {
        final /* synthetic */ zzgt zzFL;

        C05535(zzgt com_google_android_gms_internal_zzgt) {
            this.zzFL = com_google_android_gms_internal_zzgt;
        }

        public void zza(zzbb com_google_android_gms_internal_zzbb) {
            com_google_android_gms_internal_zzbb.zza("/log", zzdj.zzxv);
        }

        public /* synthetic */ void zzc(Object obj) {
            zza((zzbb) obj);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzgt.6 */
    class C05546 implements Runnable {
        final /* synthetic */ AdRequestInfoParcel zzFJ;
        final /* synthetic */ zzgt zzFL;
        final /* synthetic */ zzk zzFM;

        C05546(zzgt com_google_android_gms_internal_zzgt, AdRequestInfoParcel adRequestInfoParcel, zzk com_google_android_gms_ads_internal_request_zzk) {
            this.zzFL = com_google_android_gms_internal_zzgt;
            this.zzFJ = adRequestInfoParcel;
            this.zzFM = com_google_android_gms_ads_internal_request_zzk;
        }

        public void run() {
            AdResponseParcel zze;
            try {
                zze = this.zzFL.zze(this.zzFJ);
            } catch (Throwable e) {
                zzp.zzby().zzc(e, true);
                zzb.zzd("Could not fetch ad response due to an Exception.", e);
                zze = null;
            }
            if (zze == null) {
                zze = new AdResponseParcel(0);
            }
            try {
                this.zzFM.zzb(zze);
            } catch (Throwable e2) {
                zzb.zzd("Fail to forward ad response.", e2);
            }
        }
    }

    static {
        zzpy = new Object();
    }

    zzgt(Context context, zzbr com_google_android_gms_internal_zzbr, zzgs com_google_android_gms_internal_zzgs) {
        this.mContext = context;
        this.zzFB = com_google_android_gms_internal_zzgs;
        this.zzFC = com_google_android_gms_internal_zzbr;
        this.zzrf = new zzdz(context.getApplicationContext() != null ? context.getApplicationContext() : context, new VersionInfoParcel(8115000, 8115000, true), com_google_android_gms_internal_zzbr.zzdc(), new C05535(this), new zzdz.zzc());
    }

    private static AdResponseParcel zza(Context context, zzdz com_google_android_gms_internal_zzdz, zzbr com_google_android_gms_internal_zzbr, zzgs com_google_android_gms_internal_zzgs, AdRequestInfoParcel adRequestInfoParcel) {
        zzb.zzaF("Starting ad request from service.");
        zzby.initialize(context);
        zzcg com_google_android_gms_internal_zzcg = new zzcg(((Boolean) zzby.zzuQ.get()).booleanValue(), "load_ad", adRequestInfoParcel.zzqn.zzte);
        if (adRequestInfoParcel.versionCode > 10 && adRequestInfoParcel.zzEF != -1) {
            com_google_android_gms_internal_zzcg.zza(com_google_android_gms_internal_zzcg.zzb(adRequestInfoParcel.zzEF), "cts");
        }
        zzce zzdn = com_google_android_gms_internal_zzcg.zzdn();
        com_google_android_gms_internal_zzgs.zzFv.init();
        zzgy zzC = zzp.zzbB().zzC(context);
        if (zzC.zzGE == -1) {
            zzb.zzaF("Device is offline.");
            return new AdResponseParcel(2);
        }
        String uuid = adRequestInfoParcel.versionCode >= 7 ? adRequestInfoParcel.zzEC : UUID.randomUUID().toString();
        zzgv com_google_android_gms_internal_zzgv = new zzgv(uuid, adRequestInfoParcel.applicationInfo.packageName);
        if (adRequestInfoParcel.zzEn.extras != null) {
            String string = adRequestInfoParcel.zzEn.extras.getString("_ad");
            if (string != null) {
                return zzgu.zza(context, adRequestInfoParcel, string);
            }
        }
        Location zzd = com_google_android_gms_internal_zzgs.zzFv.zzd(250);
        String zzc = com_google_android_gms_internal_zzgs.zzFw.zzc(context, adRequestInfoParcel.zzEo.packageName);
        List zza = com_google_android_gms_internal_zzgs.zzFu.zza(adRequestInfoParcel);
        JSONObject zza2 = zzgu.zza(context, adRequestInfoParcel, zzC, com_google_android_gms_internal_zzgs.zzFy.zzD(context), zzd, com_google_android_gms_internal_zzbr, zzc, com_google_android_gms_internal_zzgs.zzFx.zzax(adRequestInfoParcel.zzEp), zza);
        if (adRequestInfoParcel.versionCode < 7) {
            try {
                zza2.put("request_id", uuid);
            } catch (JSONException e) {
            }
        }
        if (zza2 == null) {
            return new AdResponseParcel(0);
        }
        String jSONObject = zza2.toString();
        com_google_android_gms_internal_zzcg.zza(zzdn, "arc");
        zzce zzdn2 = com_google_android_gms_internal_zzcg.zzdn();
        if (((Boolean) zzby.zzum.get()).booleanValue()) {
            zzid.zzIE.post(new C05491(com_google_android_gms_internal_zzdz, com_google_android_gms_internal_zzgv, com_google_android_gms_internal_zzcg, zzdn2, jSONObject));
        } else {
            zzid.zzIE.post(new C05502(context, adRequestInfoParcel, com_google_android_gms_internal_zzgv, com_google_android_gms_internal_zzcg, zzdn2, jSONObject, com_google_android_gms_internal_zzbr));
        }
        AdResponseParcel adResponseParcel;
        try {
            zzgx com_google_android_gms_internal_zzgx = (zzgx) com_google_android_gms_internal_zzgv.zzfS().get(10, TimeUnit.SECONDS);
            if (com_google_android_gms_internal_zzgx == null) {
                adResponseParcel = new AdResponseParcel(0);
                return adResponseParcel;
            } else if (com_google_android_gms_internal_zzgx.getErrorCode() != -2) {
                adResponseParcel = new AdResponseParcel(com_google_android_gms_internal_zzgx.getErrorCode());
                zzid.zzIE.post(new C05513(com_google_android_gms_internal_zzgv));
                return adResponseParcel;
            } else {
                if (com_google_android_gms_internal_zzcg.zzdq() != null) {
                    com_google_android_gms_internal_zzcg.zza(com_google_android_gms_internal_zzcg.zzdq(), "rur");
                }
                jSONObject = null;
                if (com_google_android_gms_internal_zzgx.zzfW()) {
                    jSONObject = com_google_android_gms_internal_zzgs.zzFt.zzaw(adRequestInfoParcel.zzEo.packageName);
                }
                adResponseParcel = zza(adRequestInfoParcel, context, adRequestInfoParcel.zzqj.zzJu, com_google_android_gms_internal_zzgx.getUrl(), jSONObject, zzc, com_google_android_gms_internal_zzgx, com_google_android_gms_internal_zzcg, com_google_android_gms_internal_zzgs);
                if (adResponseParcel.zzEW == 1) {
                    com_google_android_gms_internal_zzgs.zzFw.clearToken(context, adRequestInfoParcel.zzEo.packageName);
                }
                com_google_android_gms_internal_zzcg.zza(zzdn, "tts");
                adResponseParcel.zzEY = com_google_android_gms_internal_zzcg.zzdp();
                zzid.zzIE.post(new C05513(com_google_android_gms_internal_zzgv));
                return adResponseParcel;
            }
        } catch (Exception e2) {
            adResponseParcel = new AdResponseParcel(0);
            return adResponseParcel;
        } finally {
            zzid.zzIE.post(new C05513(com_google_android_gms_internal_zzgv));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.android.gms.ads.internal.request.AdResponseParcel zza(com.google.android.gms.ads.internal.request.AdRequestInfoParcel r13, android.content.Context r14, java.lang.String r15, java.lang.String r16, java.lang.String r17, java.lang.String r18, com.google.android.gms.internal.zzgx r19, com.google.android.gms.internal.zzcg r20, com.google.android.gms.internal.zzgs r21) {
        /*
        if (r20 == 0) goto L_0x00f6;
    L_0x0002:
        r2 = r20.zzdn();
        r3 = r2;
    L_0x0007:
        r8 = new com.google.android.gms.internal.zzgw;	 Catch:{ IOException -> 0x010e }
        r8.<init>(r13);	 Catch:{ IOException -> 0x010e }
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x010e }
        r2.<init>();	 Catch:{ IOException -> 0x010e }
        r4 = "AdRequestServiceImpl: Sending request: ";
        r2 = r2.append(r4);	 Catch:{ IOException -> 0x010e }
        r0 = r16;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x010e }
        r2 = r2.toString();	 Catch:{ IOException -> 0x010e }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF(r2);	 Catch:{ IOException -> 0x010e }
        r4 = new java.net.URL;	 Catch:{ IOException -> 0x010e }
        r0 = r16;
        r4.<init>(r0);	 Catch:{ IOException -> 0x010e }
        r2 = 0;
        r5 = com.google.android.gms.ads.internal.zzp.zzbz();	 Catch:{ IOException -> 0x010e }
        r10 = r5.elapsedRealtime();	 Catch:{ IOException -> 0x010e }
        r6 = r2;
        r7 = r4;
    L_0x0036:
        if (r21 == 0) goto L_0x003f;
    L_0x0038:
        r0 = r21;
        r2 = r0.zzFz;	 Catch:{ IOException -> 0x010e }
        r2.zzfY();	 Catch:{ IOException -> 0x010e }
    L_0x003f:
        r2 = r7.openConnection();	 Catch:{ IOException -> 0x010e }
        r2 = (java.net.HttpURLConnection) r2;	 Catch:{ IOException -> 0x010e }
        r4 = com.google.android.gms.ads.internal.zzp.zzbv();	 Catch:{ all -> 0x0100 }
        r5 = 0;
        r4.zza(r14, r15, r5, r2);	 Catch:{ all -> 0x0100 }
        r4 = android.text.TextUtils.isEmpty(r17);	 Catch:{ all -> 0x0100 }
        if (r4 != 0) goto L_0x005a;
    L_0x0053:
        r4 = "x-afma-drt-cookie";
        r0 = r17;
        r2.addRequestProperty(r4, r0);	 Catch:{ all -> 0x0100 }
    L_0x005a:
        r4 = android.text.TextUtils.isEmpty(r18);	 Catch:{ all -> 0x0100 }
        if (r4 != 0) goto L_0x007a;
    L_0x0060:
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0100 }
        r4.<init>();	 Catch:{ all -> 0x0100 }
        r5 = "Bearer ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0100 }
        r0 = r18;
        r4 = r4.append(r0);	 Catch:{ all -> 0x0100 }
        r4 = r4.toString();	 Catch:{ all -> 0x0100 }
        r5 = "Authorization";
        r2.addRequestProperty(r5, r4);	 Catch:{ all -> 0x0100 }
    L_0x007a:
        if (r19 == 0) goto L_0x00a6;
    L_0x007c:
        r4 = r19.zzfV();	 Catch:{ all -> 0x0100 }
        r4 = android.text.TextUtils.isEmpty(r4);	 Catch:{ all -> 0x0100 }
        if (r4 != 0) goto L_0x00a6;
    L_0x0086:
        r4 = 1;
        r2.setDoOutput(r4);	 Catch:{ all -> 0x0100 }
        r4 = r19.zzfV();	 Catch:{ all -> 0x0100 }
        r9 = r4.getBytes();	 Catch:{ all -> 0x0100 }
        r4 = r9.length;	 Catch:{ all -> 0x0100 }
        r2.setFixedLengthStreamingMode(r4);	 Catch:{ all -> 0x0100 }
        r5 = 0;
        r4 = new java.io.BufferedOutputStream;	 Catch:{ all -> 0x00fa }
        r12 = r2.getOutputStream();	 Catch:{ all -> 0x00fa }
        r4.<init>(r12);	 Catch:{ all -> 0x00fa }
        r4.write(r9);	 Catch:{ all -> 0x01d0 }
        com.google.android.gms.internal.zzmt.zzb(r4);	 Catch:{ all -> 0x0100 }
    L_0x00a6:
        r9 = r2.getResponseCode();	 Catch:{ all -> 0x0100 }
        r12 = r2.getHeaderFields();	 Catch:{ all -> 0x0100 }
        r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r9 < r4) goto L_0x0136;
    L_0x00b2:
        r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r9 >= r4) goto L_0x0136;
    L_0x00b6:
        r6 = r7.toString();	 Catch:{ all -> 0x0100 }
        r5 = 0;
        r4 = new java.io.InputStreamReader;	 Catch:{ all -> 0x0130 }
        r7 = r2.getInputStream();	 Catch:{ all -> 0x0130 }
        r4.<init>(r7);	 Catch:{ all -> 0x0130 }
        r5 = com.google.android.gms.ads.internal.zzp.zzbv();	 Catch:{ all -> 0x01cd }
        r5 = r5.zza(r4);	 Catch:{ all -> 0x01cd }
        com.google.android.gms.internal.zzmt.zzb(r4);	 Catch:{ all -> 0x0100 }
        zza(r6, r12, r5, r9);	 Catch:{ all -> 0x0100 }
        r8.zzb(r6, r12, r5);	 Catch:{ all -> 0x0100 }
        if (r20 == 0) goto L_0x00e4;
    L_0x00d7:
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ all -> 0x0100 }
        r5 = 0;
        r6 = "ufe";
        r4[r5] = r6;	 Catch:{ all -> 0x0100 }
        r0 = r20;
        r0.zza(r3, r4);	 Catch:{ all -> 0x0100 }
    L_0x00e4:
        r3 = r8.zzj(r10);	 Catch:{ all -> 0x0100 }
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x00f4;
    L_0x00ed:
        r0 = r21;
        r2 = r0.zzFz;	 Catch:{ IOException -> 0x010e }
        r2.zzfZ();	 Catch:{ IOException -> 0x010e }
    L_0x00f4:
        r2 = r3;
    L_0x00f5:
        return r2;
    L_0x00f6:
        r2 = 0;
        r3 = r2;
        goto L_0x0007;
    L_0x00fa:
        r3 = move-exception;
        r4 = r5;
    L_0x00fc:
        com.google.android.gms.internal.zzmt.zzb(r4);	 Catch:{ all -> 0x0100 }
        throw r3;	 Catch:{ all -> 0x0100 }
    L_0x0100:
        r3 = move-exception;
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x010d;
    L_0x0106:
        r0 = r21;
        r2 = r0.zzFz;	 Catch:{ IOException -> 0x010e }
        r2.zzfZ();	 Catch:{ IOException -> 0x010e }
    L_0x010d:
        throw r3;	 Catch:{ IOException -> 0x010e }
    L_0x010e:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Error while connecting to ad server: ";
        r3 = r3.append(r4);
        r2 = r2.getMessage();
        r2 = r3.append(r2);
        r2 = r2.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaH(r2);
        r2 = new com.google.android.gms.ads.internal.request.AdResponseParcel;
        r3 = 2;
        r2.<init>(r3);
        goto L_0x00f5;
    L_0x0130:
        r3 = move-exception;
        r4 = r5;
    L_0x0132:
        com.google.android.gms.internal.zzmt.zzb(r4);	 Catch:{ all -> 0x0100 }
        throw r3;	 Catch:{ all -> 0x0100 }
    L_0x0136:
        r4 = r7.toString();	 Catch:{ all -> 0x0100 }
        r5 = 0;
        zza(r4, r12, r5, r9);	 Catch:{ all -> 0x0100 }
        r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r9 < r4) goto L_0x018f;
    L_0x0142:
        r4 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r9 >= r4) goto L_0x018f;
    L_0x0146:
        r4 = "Location";
        r4 = r2.getHeaderField(r4);	 Catch:{ all -> 0x0100 }
        r5 = android.text.TextUtils.isEmpty(r4);	 Catch:{ all -> 0x0100 }
        if (r5 == 0) goto L_0x016b;
    L_0x0152:
        r3 = "No location header to follow redirect.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaH(r3);	 Catch:{ all -> 0x0100 }
        r3 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x0100 }
        r4 = 0;
        r3.<init>(r4);	 Catch:{ all -> 0x0100 }
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x0169;
    L_0x0162:
        r0 = r21;
        r2 = r0.zzFz;	 Catch:{ IOException -> 0x010e }
        r2.zzfZ();	 Catch:{ IOException -> 0x010e }
    L_0x0169:
        r2 = r3;
        goto L_0x00f5;
    L_0x016b:
        r5 = new java.net.URL;	 Catch:{ all -> 0x0100 }
        r5.<init>(r4);	 Catch:{ all -> 0x0100 }
        r4 = r6 + 1;
        r6 = 5;
        if (r4 <= r6) goto L_0x01ba;
    L_0x0175:
        r3 = "Too many redirects.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaH(r3);	 Catch:{ all -> 0x0100 }
        r3 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x0100 }
        r4 = 0;
        r3.<init>(r4);	 Catch:{ all -> 0x0100 }
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x018c;
    L_0x0185:
        r0 = r21;
        r2 = r0.zzFz;	 Catch:{ IOException -> 0x010e }
        r2.zzfZ();	 Catch:{ IOException -> 0x010e }
    L_0x018c:
        r2 = r3;
        goto L_0x00f5;
    L_0x018f:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0100 }
        r3.<init>();	 Catch:{ all -> 0x0100 }
        r4 = "Received error HTTP response code: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0100 }
        r3 = r3.append(r9);	 Catch:{ all -> 0x0100 }
        r3 = r3.toString();	 Catch:{ all -> 0x0100 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH(r3);	 Catch:{ all -> 0x0100 }
        r3 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x0100 }
        r4 = 0;
        r3.<init>(r4);	 Catch:{ all -> 0x0100 }
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x01b7;
    L_0x01b0:
        r0 = r21;
        r2 = r0.zzFz;	 Catch:{ IOException -> 0x010e }
        r2.zzfZ();	 Catch:{ IOException -> 0x010e }
    L_0x01b7:
        r2 = r3;
        goto L_0x00f5;
    L_0x01ba:
        r8.zzh(r12);	 Catch:{ all -> 0x0100 }
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x01c9;
    L_0x01c2:
        r0 = r21;
        r2 = r0.zzFz;	 Catch:{ IOException -> 0x010e }
        r2.zzfZ();	 Catch:{ IOException -> 0x010e }
    L_0x01c9:
        r6 = r4;
        r7 = r5;
        goto L_0x0036;
    L_0x01cd:
        r3 = move-exception;
        goto L_0x0132;
    L_0x01d0:
        r3 = move-exception;
        goto L_0x00fc;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzgt.zza(com.google.android.gms.ads.internal.request.AdRequestInfoParcel, android.content.Context, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.google.android.gms.internal.zzgx, com.google.android.gms.internal.zzcg, com.google.android.gms.internal.zzgs):com.google.android.gms.ads.internal.request.AdResponseParcel");
    }

    public static zzgt zza(Context context, zzbr com_google_android_gms_internal_zzbr, zzgs com_google_android_gms_internal_zzgs) {
        zzgt com_google_android_gms_internal_zzgt;
        synchronized (zzpy) {
            if (zzFA == null) {
                if (context.getApplicationContext() != null) {
                    context = context.getApplicationContext();
                }
                zzFA = new zzgt(context, com_google_android_gms_internal_zzbr, com_google_android_gms_internal_zzgs);
            }
            com_google_android_gms_internal_zzgt = zzFA;
        }
        return com_google_android_gms_internal_zzgt;
    }

    private static zzja.zza zza(String str, zzcg com_google_android_gms_internal_zzcg, zzce com_google_android_gms_internal_zzce) {
        return new C05524(com_google_android_gms_internal_zzcg, com_google_android_gms_internal_zzce, str);
    }

    private static void zza(String str, Map<String, List<String>> map, String str2, int i) {
        if (zzb.zzN(2)) {
            zzb.m37v("Http Response: {\n  URL:\n    " + str + "\n  Headers:");
            if (map != null) {
                for (String str3 : map.keySet()) {
                    zzb.m37v("    " + str3 + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR);
                    for (String str32 : (List) map.get(str32)) {
                        zzb.m37v("      " + str32);
                    }
                }
            }
            zzb.m37v("  Body:");
            if (str2 != null) {
                for (int i2 = 0; i2 < Math.min(str2.length(), 100000); i2 += LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                    zzb.m37v(str2.substring(i2, Math.min(str2.length(), i2 + LocationStatusCodes.GEOFENCE_NOT_AVAILABLE)));
                }
            } else {
                zzb.m37v("    null");
            }
            zzb.m37v("  Response Code:\n    " + i + "\n}");
        }
    }

    public void zza(AdRequestInfoParcel adRequestInfoParcel, zzk com_google_android_gms_ads_internal_request_zzk) {
        zzp.zzby().zzb(this.mContext, adRequestInfoParcel.zzqj);
        zzid.zzb(new C05546(this, adRequestInfoParcel, com_google_android_gms_ads_internal_request_zzk));
    }

    public AdResponseParcel zze(AdRequestInfoParcel adRequestInfoParcel) {
        return zza(this.mContext, this.zzrf, this.zzFC, this.zzFB, adRequestInfoParcel);
    }
}
