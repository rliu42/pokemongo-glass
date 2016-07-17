package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/* renamed from: crittercism.android.o */
public final class C1082o extends C1080m {
    private static final String[] f846f;

    static {
        f846f = new String[]{"libcore.net.http.HttpURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnection"};
    }

    public C1082o(C1068e c1068e, C1058d c1058d) {
        super(c1068e, c1058d, f846f);
    }

    protected final URLConnection openConnection(URL u) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) super.openConnection(u);
        try {
            return new C1086r(httpURLConnection, this.f840c, this.f841d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return httpURLConnection;
        }
    }

    protected final URLConnection openConnection(URL u, Proxy proxy) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) super.openConnection(u, proxy);
        try {
            return new C1086r(httpURLConnection, this.f840c, this.f841d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return httpURLConnection;
        }
    }

    protected final int getDefaultPort() {
        return 80;
    }

    protected final String m806a() {
        return Scheme.HTTP;
    }
}
