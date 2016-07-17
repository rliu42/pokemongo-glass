package com.google.android.gms.internal;

import android.content.SharedPreferences;
import com.google.android.gms.ads.internal.zzp;

@zzgr
public abstract class zzbu<T> {
    private final String zzue;
    private final T zzuf;

    /* renamed from: com.google.android.gms.internal.zzbu.1 */
    static class C04761 extends zzbu<Boolean> {
        C04761(String str, Boolean bool) {
            super(bool, null);
        }

        public /* synthetic */ Object zza(SharedPreferences sharedPreferences) {
            return zzb(sharedPreferences);
        }

        public Boolean zzb(SharedPreferences sharedPreferences) {
            return Boolean.valueOf(sharedPreferences.getBoolean(getKey(), ((Boolean) zzde()).booleanValue()));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzbu.2 */
    static class C04772 extends zzbu<Integer> {
        C04772(String str, Integer num) {
            super(num, null);
        }

        public /* synthetic */ Object zza(SharedPreferences sharedPreferences) {
            return zzc(sharedPreferences);
        }

        public Integer zzc(SharedPreferences sharedPreferences) {
            return Integer.valueOf(sharedPreferences.getInt(getKey(), ((Integer) zzde()).intValue()));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzbu.3 */
    static class C04783 extends zzbu<Long> {
        C04783(String str, Long l) {
            super(l, null);
        }

        public /* synthetic */ Object zza(SharedPreferences sharedPreferences) {
            return zzd(sharedPreferences);
        }

        public Long zzd(SharedPreferences sharedPreferences) {
            return Long.valueOf(sharedPreferences.getLong(getKey(), ((Long) zzde()).longValue()));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzbu.4 */
    static class C04794 extends zzbu<String> {
        C04794(String str, String str2) {
            super(str2, null);
        }

        public /* synthetic */ Object zza(SharedPreferences sharedPreferences) {
            return zze(sharedPreferences);
        }

        public String zze(SharedPreferences sharedPreferences) {
            return sharedPreferences.getString(getKey(), (String) zzde());
        }
    }

    private zzbu(String str, T t) {
        this.zzue = str;
        this.zzuf = t;
        zzp.zzbD().zza(this);
    }

    public static zzbu<String> zzP(String str) {
        zzbu<String> zzc = zzc(str, (String) null);
        zzp.zzbD().zzb(zzc);
        return zzc;
    }

    public static zzbu<String> zzQ(String str) {
        zzbu<String> zzc = zzc(str, (String) null);
        zzp.zzbD().zzc(zzc);
        return zzc;
    }

    public static zzbu<Integer> zza(String str, int i) {
        return new C04772(str, Integer.valueOf(i));
    }

    public static zzbu<Boolean> zza(String str, Boolean bool) {
        return new C04761(str, bool);
    }

    public static zzbu<Long> zzb(String str, long j) {
        return new C04783(str, Long.valueOf(j));
    }

    public static zzbu<String> zzc(String str, String str2) {
        return new C04794(str, str2);
    }

    public T get() {
        return zzp.zzbE().zzd(this);
    }

    public String getKey() {
        return this.zzue;
    }

    protected abstract T zza(SharedPreferences sharedPreferences);

    public T zzde() {
        return this.zzuf;
    }
}
