package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.net.InetAddress;

/* renamed from: crittercism.android.k */
public final class C1078k {
    InetAddress f832a;
    String f833b;
    public String f834c;
    C1077a f835d;
    int f836e;
    boolean f837f;

    /* renamed from: crittercism.android.k.a */
    public enum C1077a {
        HTTP(Scheme.HTTP, 80),
        HTTPS(Scheme.HTTPS, 443);
        
        private String f830c;
        private int f831d;

        private C1077a(String str, int i) {
            this.f830c = str;
            this.f831d = i;
        }
    }

    public C1078k() {
        this.f834c = "/";
        this.f835d = null;
        this.f836e = -1;
        this.f837f = false;
    }
}
