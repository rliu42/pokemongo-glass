package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@zzgr
public class zzdy extends zzdv {
    private static final Set<String> zzyk;
    private static final DecimalFormat zzyl;
    private File zzym;
    private boolean zzyn;

    static {
        zzyk = Collections.synchronizedSet(new HashSet());
        zzyl = new DecimalFormat("#,###");
    }

    public zzdy(zziz com_google_android_gms_internal_zziz) {
        super(com_google_android_gms_internal_zziz);
        File cacheDir = com_google_android_gms_internal_zziz.getContext().getCacheDir();
        if (cacheDir == null) {
            zzb.zzaH("Context.getCacheDir() returned null");
            return;
        }
        this.zzym = new File(cacheDir, "admobVideoStreams");
        if (!this.zzym.isDirectory() && !this.zzym.mkdirs()) {
            zzb.zzaH("Could not create preload cache directory at " + this.zzym.getAbsolutePath());
            this.zzym = null;
        } else if (!this.zzym.setReadable(true, false) || !this.zzym.setExecutable(true, false)) {
            zzb.zzaH("Could not set cache file permissions at " + this.zzym.getAbsolutePath());
            this.zzym = null;
        }
    }

    private File zza(File file) {
        return new File(this.zzym, file.getName() + ".done");
    }

    private static void zzb(File file) {
        if (file.isFile()) {
            file.setLastModified(System.currentTimeMillis());
            return;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
        }
    }

    public void abort() {
        this.zzyn = true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean zzab(java.lang.String r27) {
        /*
        r26 = this;
        r0 = r26;
        r2 = r0.zzym;
        if (r2 != 0) goto L_0x0013;
    L_0x0006:
        r2 = 0;
        r3 = "noCacheDir";
        r4 = 0;
        r0 = r26;
        r1 = r27;
        r0.zza(r1, r2, r3, r4);
        r2 = 0;
    L_0x0012:
        return r2;
    L_0x0013:
        r3 = r26.zzdK();
        r2 = com.google.android.gms.internal.zzby.zzuy;
        r2 = r2.get();
        r2 = (java.lang.Integer) r2;
        r2 = r2.intValue();
        if (r3 <= r2) goto L_0x003d;
    L_0x0025:
        r2 = r26.zzdL();
        if (r2 != 0) goto L_0x0013;
    L_0x002b:
        r2 = "Unable to expire stream cache";
        com.google.android.gms.ads.internal.util.client.zzb.zzaH(r2);
        r2 = 0;
        r3 = "expireFailed";
        r4 = 0;
        r0 = r26;
        r1 = r27;
        r0.zza(r1, r2, r3, r4);
        r2 = 0;
        goto L_0x0012;
    L_0x003d:
        r2 = r26.zzac(r27);
        r11 = new java.io.File;
        r0 = r26;
        r3 = r0.zzym;
        r11.<init>(r3, r2);
        r0 = r26;
        r12 = r0.zza(r11);
        r2 = r11.isFile();
        if (r2 == 0) goto L_0x0086;
    L_0x0056:
        r2 = r12.isFile();
        if (r2 == 0) goto L_0x0086;
    L_0x005c:
        r2 = r11.length();
        r2 = (int) r2;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Stream cache hit at ";
        r3 = r3.append(r4);
        r0 = r27;
        r3 = r3.append(r0);
        r3 = r3.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaF(r3);
        r3 = r11.getAbsolutePath();
        r0 = r26;
        r1 = r27;
        r0.zza(r1, r3, r2);
        r2 = 1;
        goto L_0x0012;
    L_0x0086:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r26;
        r3 = r0.zzym;
        r3 = r3.getAbsolutePath();
        r2 = r2.append(r3);
        r0 = r27;
        r2 = r2.append(r0);
        r13 = r2.toString();
        r3 = zzyk;
        monitor-enter(r3);
        r2 = zzyk;	 Catch:{ all -> 0x00d6 }
        r2 = r2.contains(r13);	 Catch:{ all -> 0x00d6 }
        if (r2 == 0) goto L_0x00d9;
    L_0x00ac:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d6 }
        r2.<init>();	 Catch:{ all -> 0x00d6 }
        r4 = "Stream cache already in progress at ";
        r2 = r2.append(r4);	 Catch:{ all -> 0x00d6 }
        r0 = r27;
        r2 = r2.append(r0);	 Catch:{ all -> 0x00d6 }
        r2 = r2.toString();	 Catch:{ all -> 0x00d6 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH(r2);	 Catch:{ all -> 0x00d6 }
        r2 = r11.getAbsolutePath();	 Catch:{ all -> 0x00d6 }
        r4 = "inProgress";
        r5 = 0;
        r0 = r26;
        r1 = r27;
        r0.zza(r1, r2, r4, r5);	 Catch:{ all -> 0x00d6 }
        r2 = 0;
        monitor-exit(r3);	 Catch:{ all -> 0x00d6 }
        goto L_0x0012;
    L_0x00d6:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x00d6 }
        throw r2;
    L_0x00d9:
        r2 = zzyk;	 Catch:{ all -> 0x00d6 }
        r2.add(r13);	 Catch:{ all -> 0x00d6 }
        monitor-exit(r3);	 Catch:{ all -> 0x00d6 }
        r5 = 0;
        r9 = "error";
        r8 = 0;
        r2 = new java.net.URL;	 Catch:{ IOException -> 0x03b8 }
        r0 = r27;
        r2.<init>(r0);	 Catch:{ IOException -> 0x03b8 }
        r3 = r2.openConnection();	 Catch:{ IOException -> 0x03b8 }
        r2 = com.google.android.gms.internal.zzby.zzuD;	 Catch:{ IOException -> 0x03b8 }
        r2 = r2.get();	 Catch:{ IOException -> 0x03b8 }
        r2 = (java.lang.Integer) r2;	 Catch:{ IOException -> 0x03b8 }
        r2 = r2.intValue();	 Catch:{ IOException -> 0x03b8 }
        r3.setConnectTimeout(r2);	 Catch:{ IOException -> 0x03b8 }
        r3.setReadTimeout(r2);	 Catch:{ IOException -> 0x03b8 }
        r2 = r3 instanceof java.net.HttpURLConnection;	 Catch:{ IOException -> 0x03b8 }
        if (r2 == 0) goto L_0x01af;
    L_0x0104:
        r0 = r3;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ IOException -> 0x03b8 }
        r2 = r0;
        r2 = r2.getResponseCode();	 Catch:{ IOException -> 0x03b8 }
        r4 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r2 < r4) goto L_0x01af;
    L_0x0110:
        r4 = "badUrl";
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x03bd }
        r3.<init>();	 Catch:{ IOException -> 0x03bd }
        r6 = "HTTP request failed. Code: ";
        r3 = r3.append(r6);	 Catch:{ IOException -> 0x03bd }
        r6 = java.lang.Integer.toString(r2);	 Catch:{ IOException -> 0x03bd }
        r3 = r3.append(r6);	 Catch:{ IOException -> 0x03bd }
        r3 = r3.toString();	 Catch:{ IOException -> 0x03bd }
        r6 = new java.io.IOException;	 Catch:{ IOException -> 0x014e }
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x014e }
        r7.<init>();	 Catch:{ IOException -> 0x014e }
        r8 = "HTTP status code ";
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x014e }
        r2 = r7.append(r2);	 Catch:{ IOException -> 0x014e }
        r7 = " at ";
        r2 = r2.append(r7);	 Catch:{ IOException -> 0x014e }
        r0 = r27;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x014e }
        r2 = r2.toString();	 Catch:{ IOException -> 0x014e }
        r6.<init>(r2);	 Catch:{ IOException -> 0x014e }
        throw r6;	 Catch:{ IOException -> 0x014e }
    L_0x014e:
        r2 = move-exception;
    L_0x014f:
        r5.close();	 Catch:{ IOException -> 0x03b2, NullPointerException -> 0x03b5 }
    L_0x0152:
        r0 = r26;
        r5 = r0.zzyn;
        if (r5 == 0) goto L_0x0392;
    L_0x0158:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r5 = "Preload aborted for URL \"";
        r2 = r2.append(r5);
        r0 = r27;
        r2 = r2.append(r0);
        r5 = "\"";
        r2 = r2.append(r5);
        r2 = r2.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaG(r2);
    L_0x0176:
        r2 = r11.exists();
        if (r2 == 0) goto L_0x019c;
    L_0x017c:
        r2 = r11.delete();
        if (r2 != 0) goto L_0x019c;
    L_0x0182:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r5 = "Could not delete partial cache file at ";
        r2 = r2.append(r5);
        r5 = r11.getAbsolutePath();
        r2 = r2.append(r5);
        r2 = r2.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaH(r2);
    L_0x019c:
        r2 = r11.getAbsolutePath();
        r0 = r26;
        r1 = r27;
        r0.zza(r1, r2, r4, r3);
        r2 = zzyk;
        r2.remove(r13);
        r2 = 0;
        goto L_0x0012;
    L_0x01af:
        r6 = r3.getContentLength();	 Catch:{ IOException -> 0x03b8 }
        if (r6 >= 0) goto L_0x01e3;
    L_0x01b5:
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x03b8 }
        r2.<init>();	 Catch:{ IOException -> 0x03b8 }
        r3 = "Stream cache aborted, missing content-length header at ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x03b8 }
        r0 = r27;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x03b8 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x03b8 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH(r2);	 Catch:{ IOException -> 0x03b8 }
        r2 = r11.getAbsolutePath();	 Catch:{ IOException -> 0x03b8 }
        r3 = "contentLengthMissing";
        r4 = 0;
        r0 = r26;
        r1 = r27;
        r0.zza(r1, r2, r3, r4);	 Catch:{ IOException -> 0x03b8 }
        r2 = zzyk;	 Catch:{ IOException -> 0x03b8 }
        r2.remove(r13);	 Catch:{ IOException -> 0x03b8 }
        r2 = 0;
        goto L_0x0012;
    L_0x01e3:
        r2 = zzyl;	 Catch:{ IOException -> 0x03b8 }
        r14 = (long) r6;	 Catch:{ IOException -> 0x03b8 }
        r4 = r2.format(r14);	 Catch:{ IOException -> 0x03b8 }
        r2 = com.google.android.gms.internal.zzby.zzuz;	 Catch:{ IOException -> 0x03b8 }
        r2 = r2.get();	 Catch:{ IOException -> 0x03b8 }
        r2 = (java.lang.Integer) r2;	 Catch:{ IOException -> 0x03b8 }
        r14 = r2.intValue();	 Catch:{ IOException -> 0x03b8 }
        if (r6 <= r14) goto L_0x0242;
    L_0x01f8:
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x03b8 }
        r2.<init>();	 Catch:{ IOException -> 0x03b8 }
        r3 = "Content length ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x03b8 }
        r2 = r2.append(r4);	 Catch:{ IOException -> 0x03b8 }
        r3 = " exceeds limit at ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x03b8 }
        r0 = r27;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x03b8 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x03b8 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH(r2);	 Catch:{ IOException -> 0x03b8 }
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x03b8 }
        r2.<init>();	 Catch:{ IOException -> 0x03b8 }
        r3 = "File too big for full file cache. Size: ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x03b8 }
        r2 = r2.append(r4);	 Catch:{ IOException -> 0x03b8 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x03b8 }
        r3 = r11.getAbsolutePath();	 Catch:{ IOException -> 0x03b8 }
        r4 = "sizeExceeded";
        r0 = r26;
        r1 = r27;
        r0.zza(r1, r3, r4, r2);	 Catch:{ IOException -> 0x03b8 }
        r2 = zzyk;	 Catch:{ IOException -> 0x03b8 }
        r2.remove(r13);	 Catch:{ IOException -> 0x03b8 }
        r2 = 0;
        goto L_0x0012;
    L_0x0242:
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x03b8 }
        r2.<init>();	 Catch:{ IOException -> 0x03b8 }
        r7 = "Caching ";
        r2 = r2.append(r7);	 Catch:{ IOException -> 0x03b8 }
        r2 = r2.append(r4);	 Catch:{ IOException -> 0x03b8 }
        r4 = " bytes from ";
        r2 = r2.append(r4);	 Catch:{ IOException -> 0x03b8 }
        r0 = r27;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x03b8 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x03b8 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF(r2);	 Catch:{ IOException -> 0x03b8 }
        r2 = r3.getInputStream();	 Catch:{ IOException -> 0x03b8 }
        r15 = java.nio.channels.Channels.newChannel(r2);	 Catch:{ IOException -> 0x03b8 }
        r10 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x03b8 }
        r10.<init>(r11);	 Catch:{ IOException -> 0x03b8 }
        r16 = r10.getChannel();	 Catch:{ IOException -> 0x033e }
        r2 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r17 = java.nio.ByteBuffer.allocate(r2);	 Catch:{ IOException -> 0x033e }
        r18 = com.google.android.gms.ads.internal.zzp.zzbz();	 Catch:{ IOException -> 0x033e }
        r5 = 0;
        r20 = r18.currentTimeMillis();	 Catch:{ IOException -> 0x033e }
        r2 = com.google.android.gms.internal.zzby.zzuC;	 Catch:{ IOException -> 0x033e }
        r2 = r2.get();	 Catch:{ IOException -> 0x033e }
        r2 = (java.lang.Long) r2;	 Catch:{ IOException -> 0x033e }
        r2 = r2.longValue();	 Catch:{ IOException -> 0x033e }
        r19 = new com.google.android.gms.internal.zzik;	 Catch:{ IOException -> 0x033e }
        r0 = r19;
        r0.<init>(r2);	 Catch:{ IOException -> 0x033e }
        r2 = com.google.android.gms.internal.zzby.zzuB;	 Catch:{ IOException -> 0x033e }
        r2 = r2.get();	 Catch:{ IOException -> 0x033e }
        r2 = (java.lang.Long) r2;	 Catch:{ IOException -> 0x033e }
        r22 = r2.longValue();	 Catch:{ IOException -> 0x033e }
    L_0x02a3:
        r0 = r17;
        r2 = r15.read(r0);	 Catch:{ IOException -> 0x033e }
        if (r2 < 0) goto L_0x0344;
    L_0x02ab:
        r5 = r5 + r2;
        if (r5 <= r14) goto L_0x02d3;
    L_0x02ae:
        r4 = "sizeExceeded";
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x03c1 }
        r2.<init>();	 Catch:{ IOException -> 0x03c1 }
        r3 = "File too big for full file cache. Size: ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x03c1 }
        r3 = java.lang.Integer.toString(r5);	 Catch:{ IOException -> 0x03c1 }
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x03c1 }
        r3 = r2.toString();	 Catch:{ IOException -> 0x03c1 }
        r2 = new java.io.IOException;	 Catch:{ IOException -> 0x02cf }
        r5 = "stream cache file size limit exceeded";
        r2.<init>(r5);	 Catch:{ IOException -> 0x02cf }
        throw r2;	 Catch:{ IOException -> 0x02cf }
    L_0x02cf:
        r2 = move-exception;
        r5 = r10;
        goto L_0x014f;
    L_0x02d3:
        r17.flip();	 Catch:{ IOException -> 0x033e }
    L_0x02d6:
        r2 = r16.write(r17);	 Catch:{ IOException -> 0x033e }
        if (r2 > 0) goto L_0x02d6;
    L_0x02dc:
        r17.clear();	 Catch:{ IOException -> 0x033e }
        r2 = r18.currentTimeMillis();	 Catch:{ IOException -> 0x033e }
        r2 = r2 - r20;
        r24 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r24 = r24 * r22;
        r2 = (r2 > r24 ? 1 : (r2 == r24 ? 0 : -1));
        if (r2 <= 0) goto L_0x0314;
    L_0x02ed:
        r4 = "downloadTimeout";
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x03c1 }
        r2.<init>();	 Catch:{ IOException -> 0x03c1 }
        r3 = "Timeout exceeded. Limit: ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x03c1 }
        r3 = java.lang.Long.toString(r22);	 Catch:{ IOException -> 0x03c1 }
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x03c1 }
        r3 = " sec";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x03c1 }
        r3 = r2.toString();	 Catch:{ IOException -> 0x03c1 }
        r2 = new java.io.IOException;	 Catch:{ IOException -> 0x02cf }
        r5 = "stream cache time limit exceeded";
        r2.<init>(r5);	 Catch:{ IOException -> 0x02cf }
        throw r2;	 Catch:{ IOException -> 0x02cf }
    L_0x0314:
        r0 = r26;
        r2 = r0.zzyn;	 Catch:{ IOException -> 0x033e }
        if (r2 == 0) goto L_0x032a;
    L_0x031a:
        r3 = "externalAbort";
        r2 = new java.io.IOException;	 Catch:{ IOException -> 0x0324 }
        r4 = "abort requested";
        r2.<init>(r4);	 Catch:{ IOException -> 0x0324 }
        throw r2;	 Catch:{ IOException -> 0x0324 }
    L_0x0324:
        r2 = move-exception;
        r4 = r3;
        r5 = r10;
        r3 = r8;
        goto L_0x014f;
    L_0x032a:
        r2 = r19.tryAcquire();	 Catch:{ IOException -> 0x033e }
        if (r2 == 0) goto L_0x02a3;
    L_0x0330:
        r4 = r11.getAbsolutePath();	 Catch:{ IOException -> 0x033e }
        r7 = 0;
        r2 = r26;
        r3 = r27;
        r2.zza(r3, r4, r5, r6, r7);	 Catch:{ IOException -> 0x033e }
        goto L_0x02a3;
    L_0x033e:
        r2 = move-exception;
        r3 = r8;
        r4 = r9;
        r5 = r10;
        goto L_0x014f;
    L_0x0344:
        r10.close();	 Catch:{ IOException -> 0x033e }
        r2 = 3;
        r2 = com.google.android.gms.ads.internal.util.client.zzb.zzN(r2);	 Catch:{ IOException -> 0x033e }
        if (r2 == 0) goto L_0x0377;
    L_0x034e:
        r2 = zzyl;	 Catch:{ IOException -> 0x033e }
        r6 = (long) r5;	 Catch:{ IOException -> 0x033e }
        r2 = r2.format(r6);	 Catch:{ IOException -> 0x033e }
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x033e }
        r3.<init>();	 Catch:{ IOException -> 0x033e }
        r4 = "Preloaded ";
        r3 = r3.append(r4);	 Catch:{ IOException -> 0x033e }
        r2 = r3.append(r2);	 Catch:{ IOException -> 0x033e }
        r3 = " bytes from ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x033e }
        r0 = r27;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x033e }
        r2 = r2.toString();	 Catch:{ IOException -> 0x033e }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF(r2);	 Catch:{ IOException -> 0x033e }
    L_0x0377:
        r2 = 1;
        r3 = 0;
        r11.setReadable(r2, r3);	 Catch:{ IOException -> 0x033e }
        zzb(r12);	 Catch:{ IOException -> 0x033e }
        r2 = r11.getAbsolutePath();	 Catch:{ IOException -> 0x033e }
        r0 = r26;
        r1 = r27;
        r0.zza(r1, r2, r5);	 Catch:{ IOException -> 0x033e }
        r2 = zzyk;	 Catch:{ IOException -> 0x033e }
        r2.remove(r13);	 Catch:{ IOException -> 0x033e }
        r2 = 1;
        goto L_0x0012;
    L_0x0392:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Preload failed for URL \"";
        r5 = r5.append(r6);
        r0 = r27;
        r5 = r5.append(r0);
        r6 = "\"";
        r5 = r5.append(r6);
        r5 = r5.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzd(r5, r2);
        goto L_0x0176;
    L_0x03b2:
        r5 = move-exception;
        goto L_0x0152;
    L_0x03b5:
        r5 = move-exception;
        goto L_0x0152;
    L_0x03b8:
        r2 = move-exception;
        r3 = r8;
        r4 = r9;
        goto L_0x014f;
    L_0x03bd:
        r2 = move-exception;
        r3 = r8;
        goto L_0x014f;
    L_0x03c1:
        r2 = move-exception;
        r3 = r8;
        r5 = r10;
        goto L_0x014f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdy.zzab(java.lang.String):boolean");
    }

    public int zzdK() {
        int i = 0;
        if (this.zzym != null) {
            for (File name : this.zzym.listFiles()) {
                if (!name.getName().endsWith(".done")) {
                    i++;
                }
            }
        }
        return i;
    }

    public boolean zzdL() {
        if (this.zzym == null) {
            return false;
        }
        boolean delete;
        File file = null;
        long j = Long.MAX_VALUE;
        File[] listFiles = this.zzym.listFiles();
        int length = listFiles.length;
        int i = 0;
        while (i < length) {
            long lastModified;
            File file2;
            File file3 = listFiles[i];
            if (!file3.getName().endsWith(".done")) {
                lastModified = file3.lastModified();
                if (lastModified < j) {
                    file2 = file3;
                    i++;
                    file = file2;
                    j = lastModified;
                }
            }
            lastModified = j;
            file2 = file;
            i++;
            file = file2;
            j = lastModified;
        }
        if (file != null) {
            delete = file.delete();
            File zza = zza(file);
            if (zza.isFile()) {
                delete &= zza.delete();
            }
        } else {
            delete = false;
        }
        return delete;
    }
}
