package com.google.android.gms.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import java.util.concurrent.Future;

@zzgr
public final class zzib {

    public interface zzb {
        void zzd(Bundle bundle);
    }

    private static abstract class zza extends zzhz {
        private zza() {
        }

        public void onStop() {
        }
    }

    /* renamed from: com.google.android.gms.internal.zzib.1 */
    static class C05631 extends zza {
        final /* synthetic */ boolean zzIo;
        final /* synthetic */ Context zzry;

        C05631(Context context, boolean z) {
            this.zzry = context;
            this.zzIo = z;
            super();
        }

        public void zzbn() {
            Editor edit = zzib.zzv(this.zzry).edit();
            edit.putBoolean("use_https", this.zzIo);
            edit.apply();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzib.2 */
    static class C05642 extends zza {
        final /* synthetic */ zzb zzIp;
        final /* synthetic */ Context zzry;

        C05642(Context context, zzb com_google_android_gms_internal_zzib_zzb) {
            this.zzry = context;
            this.zzIp = com_google_android_gms_internal_zzib_zzb;
            super();
        }

        public void zzbn() {
            SharedPreferences zzG = zzib.zzv(this.zzry);
            Bundle bundle = new Bundle();
            bundle.putBoolean("use_https", zzG.getBoolean("use_https", true));
            if (this.zzIp != null) {
                this.zzIp.zzd(bundle);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzib.3 */
    static class C05653 extends zza {
        final /* synthetic */ int zzIq;
        final /* synthetic */ Context zzry;

        C05653(Context context, int i) {
            this.zzry = context;
            this.zzIq = i;
            super();
        }

        public void zzbn() {
            Editor edit = zzib.zzv(this.zzry).edit();
            edit.putInt("webview_cache_version", this.zzIq);
            edit.apply();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzib.4 */
    static class C05664 extends zza {
        final /* synthetic */ zzb zzIp;
        final /* synthetic */ Context zzry;

        C05664(Context context, zzb com_google_android_gms_internal_zzib_zzb) {
            this.zzry = context;
            this.zzIp = com_google_android_gms_internal_zzib_zzb;
            super();
        }

        public void zzbn() {
            SharedPreferences zzG = zzib.zzv(this.zzry);
            Bundle bundle = new Bundle();
            bundle.putInt("webview_cache_version", zzG.getInt("webview_cache_version", 0));
            if (this.zzIp != null) {
                this.zzIp.zzd(bundle);
            }
        }
    }

    public static Future zza(Context context, int i) {
        return new C05653(context, i).zzgz();
    }

    public static Future zza(Context context, zzb com_google_android_gms_internal_zzib_zzb) {
        return new C05642(context, com_google_android_gms_internal_zzib_zzb).zzgz();
    }

    public static Future zza(Context context, boolean z) {
        return new C05631(context, z).zzgz();
    }

    public static Future zzb(Context context, zzb com_google_android_gms_internal_zzib_zzb) {
        return new C05664(context, com_google_android_gms_internal_zzib_zzb).zzgz();
    }

    private static SharedPreferences zzv(Context context) {
        return context.getSharedPreferences("admob", 0);
    }
}
