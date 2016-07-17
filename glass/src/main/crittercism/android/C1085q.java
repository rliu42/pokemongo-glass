package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

/* renamed from: crittercism.android.q */
public final class C1085q extends C1080m {
    private static final String[] f848f;

    static {
        f848f = new String[]{"libcore.net.http.HttpsURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.https.HttpsURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.https.HttpsURLConnection"};
    }

    public C1085q(C1068e c1068e, C1058d c1058d) {
        super(c1068e, c1058d, f848f);
    }

    protected final URLConnection openConnection(URL u) {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.openConnection(u);
        try {
            return new C1087s(httpsURLConnection, this.f840c, this.f841d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return httpsURLConnection;
        }
    }

    protected final URLConnection openConnection(URL u, Proxy proxy) {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.openConnection(u, proxy);
        try {
            return new C1087s(httpsURLConnection, this.f840c, this.f841d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return httpsURLConnection;
        }
    }

    protected final int getDefaultPort() {
        return 443;
    }

    protected final String m807a() {
        return Scheme.HTTPS;
    }
}
