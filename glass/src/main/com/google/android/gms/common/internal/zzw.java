package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class zzw {

    public static final class zza {
        private final Object zzJm;
        private final List<String> zzago;

        private zza(Object obj) {
            this.zzJm = zzx.zzw(obj);
            this.zzago = new ArrayList();
        }

        public String toString() {
            StringBuilder append = new StringBuilder(100).append(this.zzJm.getClass().getSimpleName()).append('{');
            int size = this.zzago.size();
            for (int i = 0; i < size; i++) {
                append.append((String) this.zzago.get(i));
                if (i < size - 1) {
                    append.append(", ");
                }
            }
            return append.append('}').toString();
        }

        public zza zzg(String str, Object obj) {
            this.zzago.add(((String) zzx.zzw(str)) + "=" + String.valueOf(obj));
            return this;
        }
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static zza zzv(Object obj) {
        return new zza(null);
    }
}
