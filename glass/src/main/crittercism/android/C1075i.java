package crittercism.android;

import android.os.Build.VERSION;
import crittercism.android.C1091v.C1090a;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/* renamed from: crittercism.android.i */
public final class C1075i {
    public static final C1090a f816a;
    public static C1074b f817b;
    private static final List f818c;
    private ad f819d;
    private ab f820e;
    private ab f821f;
    private C1091v f822g;
    private C1068e f823h;
    private C1058d f824i;
    private C1074b f825j;
    private C1090a f826k;

    /* renamed from: crittercism.android.i.a */
    static class C1073a implements Runnable {
        private boolean f809a;
        private boolean f810b;
        private C1075i f811c;

        public C1073a(C1075i c1075i) {
            this.f810b = false;
            this.f811c = c1075i;
            this.f809a = true;
        }

        public final boolean m782a() {
            return this.f810b;
        }

        public final void run() {
            if (this.f809a) {
                this.f810b = this.f811c.m793c();
            } else {
                this.f811c.m792b();
            }
        }
    }

    /* renamed from: crittercism.android.i.b */
    public enum C1074b {
        SOCKET_MONITOR,
        STREAM_MONITOR,
        NONE
    }

    static {
        f816a = C1090a.HTTPS_ONLY;
        f817b = C1074b.NONE;
        f818c = new LinkedList();
        try {
            if (!((URLStreamHandler) C1076j.m794a(C1076j.m795a(URL.class, URLStreamHandler.class), new URL("https://www.google.com"))).getClass().getName().contains("okhttp") || VERSION.SDK_INT < 19) {
                f817b = C1074b.STREAM_MONITOR;
            } else {
                f817b = C1074b.SOCKET_MONITOR;
            }
        } catch (Exception e) {
            f817b = C1074b.NONE;
        }
    }

    public C1075i(C1068e c1068e, C1058d c1058d) {
        this.f825j = f817b;
        this.f826k = f816a;
        this.f823h = c1068e;
        this.f824i = c1058d;
    }

    public final boolean m791a() {
        if (ac.m241c()) {
            try {
                boolean e;
                boolean a;
                ac.m243e();
                int h = m790h() | 0;
                if (VERSION.SDK_INT >= 19) {
                    e = h | m787e();
                } else {
                    e = h | m793c();
                }
                if (VERSION.SDK_INT >= 17) {
                    a = e | C1094y.m848a(this.f823h, this.f824i);
                } else {
                    a = e;
                }
                if (this.f825j == C1074b.SOCKET_MONITOR) {
                    SSLSocketFactory defaultSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
                    if (defaultSSLSocketFactory instanceof ab) {
                        this.f820e = (ab) defaultSSLSocketFactory;
                    } else {
                        this.f820e = new ab(defaultSSLSocketFactory, this.f823h, this.f824i);
                        HttpsURLConnection.setDefaultSSLSocketFactory(this.f820e);
                    }
                    return a | 1;
                } else if (this.f825j == C1074b.STREAM_MONITOR) {
                    return a | m788f();
                } else {
                    return a;
                }
            } catch (Throwable e2) {
                dx.m751a(e2.toString(), e2);
                return false;
            }
        }
        C1075i.m783a("Unable to install OPTMZ", ac.m242d());
        return false;
    }

    private boolean m787e() {
        Object c1073a = new C1073a(this);
        Thread thread = new Thread(c1073a);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
        return c1073a.m782a();
    }

    private boolean m788f() {
        boolean z = false;
        try {
            this.f822g = new C1091v(this.f826k, this.f823h, this.f824i);
            z = this.f822g.m820b();
        } catch (ClassNotFoundException e) {
        }
        return z;
    }

    public final void m792b() {
        try {
            SSLSocketFactory g = C1075i.m789g();
            if (g instanceof ab) {
                C1075i.m784a(((ab) g).m236a());
            }
            this.f821f = null;
        } catch (Throwable e) {
            C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", e);
        } catch (Throwable e2) {
            C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", e2);
        } catch (Throwable e22) {
            C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", e22);
        }
    }

    public final boolean m793c() {
        try {
            SSLSocketFactory g = C1075i.m789g();
            if (g == null) {
                C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", new NullPointerException("Delegate factory was null"));
                return false;
            } else if (g instanceof ab) {
                return false;
            } else {
                SSLSocketFactory abVar = new ab(g, this.f823h, this.f824i);
                try {
                    C1075i.m784a(abVar);
                    this.f821f = abVar;
                    return true;
                } catch (Throwable e) {
                    C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", e);
                    return false;
                } catch (Throwable e2) {
                    C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", e2);
                    return false;
                } catch (Throwable e22) {
                    C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", e22);
                    return false;
                }
            }
        } catch (Throwable e222) {
            C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", e222);
            return false;
        } catch (Throwable e2222) {
            C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", e2222);
            return false;
        } catch (Throwable e22222) {
            C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", e22222);
            return false;
        } catch (Throwable e222222) {
            C1075i.m783a("Unable to install OPTIMZ for SSL HttpClient connections", e222222);
            return false;
        }
    }

    private static SSLSocketFactory m789g() {
        return (SSLSocketFactory) C1076j.m795a(org.apache.http.conn.ssl.SSLSocketFactory.class, SSLSocketFactory.class).get(org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory());
    }

    private static void m784a(SSLSocketFactory sSLSocketFactory) {
        C1076j.m795a(org.apache.http.conn.ssl.SSLSocketFactory.class, SSLSocketFactory.class).set(org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory(), sSLSocketFactory);
    }

    private boolean m790h() {
        Class cls = null;
        try {
            ad adVar;
            SocketImplFactory socketImplFactory = (SocketImplFactory) C1076j.m794a(C1076j.m795a(Socket.class, SocketImplFactory.class), null);
            if (socketImplFactory == null) {
                try {
                    SocketImpl socketImpl = (SocketImpl) C1076j.m794a(C1076j.m795a(Socket.class, SocketImpl.class), new Socket());
                    if (socketImpl == null) {
                        throw new cl("SocketImpl was null");
                    }
                    cls = socketImpl.getClass();
                } catch (Throwable e) {
                    C1075i.m783a("Unable to install OPTIMZ for http connections", e);
                    return false;
                }
            } else if (socketImplFactory instanceof ad) {
                return true;
            }
            if (socketImplFactory != null) {
                try {
                    SocketImplFactory adVar2 = new ad(socketImplFactory, this.f823h, this.f824i);
                    C1075i.m785a(adVar2);
                    adVar = adVar2;
                } catch (Throwable e2) {
                    C1075i.m783a("Unable to install OPTIMZ for http connections", e2);
                    return false;
                } catch (Throwable e22) {
                    C1075i.m783a("Unable to install OPTIMZ for http connections", e22);
                    return false;
                }
            } else if (cls != null) {
                adVar = new ad(cls, this.f823h, this.f824i);
                Socket.setSocketImplFactory(adVar);
            } else {
                C1075i.m783a("Unable to install OPTIMZ for http connections", new NullPointerException("Null SocketImpl"));
                return false;
            }
            this.f819d = adVar;
            return true;
        } catch (Throwable e222) {
            C1075i.m783a("Unable to install OPTIMZ for http connections", e222);
            return false;
        }
    }

    private static boolean m785a(SocketImplFactory socketImplFactory) {
        try {
            Field a = C1076j.m795a(Socket.class, SocketImplFactory.class);
            try {
                a.setAccessible(true);
                a.set(null, socketImplFactory);
                return true;
            } catch (Throwable e) {
                C1075i.m783a("Unable to install OPTIMZ for http connections", e);
                return true;
            } catch (Throwable e2) {
                C1075i.m783a("Unable to install OPTIMZ for http connections", e2);
                return false;
            } catch (Throwable e22) {
                C1075i.m783a("Unable to install OPTIMZ for http connections", e22);
                return false;
            }
        } catch (Throwable e222) {
            C1075i.m783a("Unable to install OPTIMZ for http connections", e222);
            return false;
        }
    }

    private static void m783a(String str, Throwable th) {
        synchronized (f818c) {
            f818c.add(th);
        }
        dx.m757c(str);
    }

    public static void m786d() {
        synchronized (f818c) {
            for (Throwable a : f818c) {
                dx.m752a(a);
            }
            f818c.clear();
        }
    }
}
