package crittercism.android;

import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/* renamed from: crittercism.android.z */
public final class C1095z extends SSLContextSpi {
    private static Method[] f888a;
    private static boolean f889b;
    private SSLContextSpi f890c;
    private C1068e f891d;
    private C1058d f892e;

    static {
        f888a = new Method[7];
        f889b = false;
        try {
            f888a[0] = SSLContextSpi.class.getDeclaredMethod("engineCreateSSLEngine", new Class[0]);
            f888a[1] = SSLContextSpi.class.getDeclaredMethod("engineCreateSSLEngine", new Class[]{String.class, Integer.TYPE});
            f888a[2] = SSLContextSpi.class.getDeclaredMethod("engineGetClientSessionContext", new Class[0]);
            f888a[3] = SSLContextSpi.class.getDeclaredMethod("engineGetServerSessionContext", new Class[0]);
            f888a[4] = SSLContextSpi.class.getDeclaredMethod("engineGetServerSocketFactory", new Class[0]);
            f888a[5] = SSLContextSpi.class.getDeclaredMethod("engineGetSocketFactory", new Class[0]);
            f888a[6] = SSLContextSpi.class.getDeclaredMethod("engineInit", new Class[]{KeyManager[].class, TrustManager[].class, SecureRandom.class});
            C1076j.m796a(f888a);
            C1095z c1095z = new C1095z(new C1095z(), null, null);
            c1095z.engineCreateSSLEngine();
            c1095z.engineCreateSSLEngine(null, 0);
            c1095z.engineGetClientSessionContext();
            c1095z.engineGetServerSessionContext();
            c1095z.engineGetServerSocketFactory();
            c1095z.engineGetSocketFactory();
            c1095z.engineInit(null, null, null);
            f889b = true;
        } catch (Throwable th) {
            dx.m756c();
            f889b = false;
        }
    }

    private C1095z(SSLContextSpi sSLContextSpi, C1068e c1068e, C1058d c1058d) {
        this.f890c = sSLContextSpi;
        this.f891d = c1068e;
        this.f892e = c1058d;
    }

    public static C1095z m850a(SSLContextSpi sSLContextSpi, C1068e c1068e, C1058d c1058d) {
        if (f889b) {
            return new C1095z(sSLContextSpi, c1068e, c1058d);
        }
        return null;
    }

    private C1095z() {
    }

    public static boolean m853a() {
        return f889b;
    }

    private Object m851a(int i, Object... objArr) {
        Throwable e;
        if (this.f890c == null) {
            return null;
        }
        try {
            return f888a[i].invoke(this.f890c, objArr);
        } catch (Throwable e2) {
            throw new ck(e2);
        } catch (Throwable e22) {
            throw new ck(e22);
        } catch (Throwable e222) {
            Throwable th = e222;
            e222 = th.getTargetException();
            if (e222 == null) {
                throw new ck(th);
            } else if (e222 instanceof Exception) {
                throw ((Exception) e222);
            } else if (e222 instanceof Error) {
                throw ((Error) e222);
            } else {
                throw new ck(th);
            }
        } catch (Throwable e2222) {
            throw new ck(e2222);
        }
    }

    private Object m854b(int i, Object... objArr) {
        try {
            return m851a(i, objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e2) {
            throw new ck(e2);
        }
    }

    private Object m852a(Object... objArr) {
        try {
            return m851a(6, objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (KeyManagementException e2) {
            throw e2;
        } catch (Throwable e3) {
            throw new ck(e3);
        }
    }

    protected final SSLEngine engineCreateSSLEngine() {
        return (SSLEngine) m854b(0, new Object[0]);
    }

    protected final SSLEngine engineCreateSSLEngine(String host, int port) {
        return (SSLEngine) m854b(1, host, Integer.valueOf(port));
    }

    protected final SSLSessionContext engineGetClientSessionContext() {
        return (SSLSessionContext) m854b(2, new Object[0]);
    }

    protected final SSLSessionContext engineGetServerSessionContext() {
        return (SSLSessionContext) m854b(3, new Object[0]);
    }

    protected final SSLServerSocketFactory engineGetServerSocketFactory() {
        return (SSLServerSocketFactory) m854b(4, new Object[0]);
    }

    protected final SSLSocketFactory engineGetSocketFactory() {
        SSLSocketFactory sSLSocketFactory = (SSLSocketFactory) m854b(5, new Object[0]);
        if (sSLSocketFactory == null) {
            return sSLSocketFactory;
        }
        try {
            return new ab(sSLSocketFactory, this.f891d, this.f892e);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return sSLSocketFactory;
        }
    }

    protected final void engineInit(KeyManager[] km, TrustManager[] tm, SecureRandom sr) {
        m852a(km, tm, sr);
    }

    public final boolean equals(Object o) {
        SSLContextSpi sSLContextSpi = this.f890c;
        return this.f890c.equals(o);
    }

    public final int hashCode() {
        SSLContextSpi sSLContextSpi = this.f890c;
        return this.f890c.hashCode();
    }

    public final String toString() {
        SSLContextSpi sSLContextSpi = this.f890c;
        return this.f890c.toString();
    }
}
