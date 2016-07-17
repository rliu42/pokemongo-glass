package com.unity3d.player;

import android.os.Build.VERSION;

/* renamed from: com.unity3d.player.q */
public final class C0849q {
    static final boolean f200a;
    static final boolean f201b;
    static final boolean f202c;
    static final boolean f203d;
    static final boolean f204e;
    static final boolean f205f;
    static final boolean f206g;
    static final boolean f207h;
    static final C0830f f208i;
    static final C0826e f209j;
    static final C0833h f210k;
    static final C0832g f211l;
    static final C0834i f212m;

    static {
        C0834i c0834i = null;
        boolean z = true;
        f200a = VERSION.SDK_INT >= 11;
        f201b = VERSION.SDK_INT >= 12;
        f202c = VERSION.SDK_INT >= 14;
        f203d = VERSION.SDK_INT >= 16;
        f204e = VERSION.SDK_INT >= 17;
        f205f = VERSION.SDK_INT >= 19;
        f206g = VERSION.SDK_INT >= 21;
        if (VERSION.SDK_INT < 23) {
            z = false;
        }
        f207h = z;
        f208i = f200a ? new C0831d() : null;
        f209j = f201b ? new C0827c() : null;
        f210k = f203d ? new C0842l() : null;
        f211l = f204e ? new C0840k() : null;
        if (f207h) {
            c0834i = new C0845n();
        }
        f212m = c0834i;
    }
}
