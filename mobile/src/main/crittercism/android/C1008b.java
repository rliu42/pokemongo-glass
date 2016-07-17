package crittercism.android;

import android.util.SparseArray;

/* renamed from: crittercism.android.b */
public enum C1008b {
    MOBILE(0),
    WIFI(1),
    UNKNOWN(2),
    NOT_CONNECTED(3);
    
    private static SparseArray f398e;
    private int f400f;

    static {
        SparseArray sparseArray = new SparseArray();
        f398e = sparseArray;
        sparseArray.put(0, MOBILE);
        f398e.put(1, WIFI);
    }

    private C1008b(int i) {
        this.f400f = i;
    }

    public final int m427a() {
        return this.f400f;
    }

    public static C1008b m426a(int i) {
        C1008b c1008b = (C1008b) f398e.get(i);
        if (c1008b == null) {
            return UNKNOWN;
        }
        return c1008b;
    }
}
