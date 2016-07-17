package crittercism.android;

import android.support.v4.os.EnvironmentCompat;
import crittercism.android.C1050c.C1049a;
import crittercism.android.C1078k.C1077a;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketImpl;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;

public final class ac extends SocketImpl implements ae {
    private static Field f286a;
    private static Field f287b;
    private static Field f288c;
    private static Field f289d;
    private static Method[] f290e;
    private static boolean f291f;
    private static Throwable f292g;
    private final Queue f293h;
    private C1068e f294i;
    private C1058d f295j;
    private SocketImpl f296k;
    private C1092w f297l;
    private C1093x f298m;

    /* renamed from: crittercism.android.ac.1 */
    static class C09951 extends SocketImpl {
        C09951() {
        }

        public final void setOption(int i, Object obj) {
        }

        public final Object getOption(int i) {
            return null;
        }

        protected final void sendUrgentData(int i) {
        }

        protected final void listen(int i) {
        }

        protected final OutputStream getOutputStream() {
            return null;
        }

        protected final InputStream getInputStream() {
            return null;
        }

        protected final void create(boolean z) {
        }

        protected final void connect(SocketAddress socketAddress, int i) {
        }

        protected final void connect(InetAddress inetAddress, int i) {
        }

        protected final void connect(String str, int i) {
        }

        protected final void close() {
        }

        protected final void bind(InetAddress inetAddress, int i) {
        }

        protected final int available() {
            return 0;
        }

        protected final void accept(SocketImpl socketImpl) {
        }

        protected final FileDescriptor getFileDescriptor() {
            return null;
        }

        protected final InetAddress getInetAddress() {
            return null;
        }

        protected final int getLocalPort() {
            return 0;
        }

        protected final int getPort() {
            return 0;
        }

        protected final void setPerformancePreferences(int i, int i2, int i3) {
        }

        protected final void shutdownInput() {
        }

        protected final void shutdownOutput() {
        }

        protected final boolean supportsUrgentData() {
            return false;
        }

        public final String toString() {
            return null;
        }
    }

    /* renamed from: crittercism.android.ac.2 */
    static class C09962 implements Executor {
        C09962() {
        }

        public final void execute(Runnable runnable) {
        }
    }

    static {
        f290e = new Method[20];
        f291f = false;
        f292g = null;
        try {
            Class cls = SocketImpl.class;
            f286a = cls.getDeclaredField("address");
            f287b = cls.getDeclaredField("fd");
            f288c = cls.getDeclaredField("localport");
            f289d = cls.getDeclaredField("port");
            AccessibleObject accessibleObject = f286a;
            AccessibleObject[] accessibleObjectArr = new AccessibleObject[]{f287b, f288c, f289d};
            if (accessibleObject != null) {
                accessibleObject.setAccessible(true);
            }
            if (accessibleObjectArr.length > 0) {
                C1076j.m796a(accessibleObjectArr);
            }
            f290e[0] = cls.getDeclaredMethod("accept", new Class[]{SocketImpl.class});
            f290e[1] = cls.getDeclaredMethod("available", new Class[0]);
            f290e[2] = cls.getDeclaredMethod("bind", new Class[]{InetAddress.class, Integer.TYPE});
            f290e[3] = cls.getDeclaredMethod("close", new Class[0]);
            f290e[4] = cls.getDeclaredMethod("connect", new Class[]{InetAddress.class, Integer.TYPE});
            f290e[5] = cls.getDeclaredMethod("connect", new Class[]{SocketAddress.class, Integer.TYPE});
            f290e[6] = cls.getDeclaredMethod("connect", new Class[]{String.class, Integer.TYPE});
            f290e[7] = cls.getDeclaredMethod("create", new Class[]{Boolean.TYPE});
            f290e[8] = cls.getDeclaredMethod("getFileDescriptor", new Class[0]);
            f290e[9] = cls.getDeclaredMethod("getInetAddress", new Class[0]);
            f290e[10] = cls.getDeclaredMethod("getInputStream", new Class[0]);
            f290e[11] = cls.getDeclaredMethod("getLocalPort", new Class[0]);
            f290e[12] = cls.getDeclaredMethod("getOutputStream", new Class[0]);
            f290e[13] = cls.getDeclaredMethod("getPort", new Class[0]);
            f290e[14] = cls.getDeclaredMethod("listen", new Class[]{Integer.TYPE});
            f290e[15] = cls.getDeclaredMethod("sendUrgentData", new Class[]{Integer.TYPE});
            f290e[16] = cls.getDeclaredMethod("setPerformancePreferences", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
            f290e[17] = cls.getDeclaredMethod("shutdownInput", new Class[0]);
            f290e[18] = cls.getDeclaredMethod("shutdownOutput", new Class[0]);
            f290e[19] = cls.getDeclaredMethod("supportsUrgentData", new Class[0]);
            C1076j.m796a(f290e);
            f291f = true;
        } catch (Throwable e) {
            f291f = false;
            f292g = e;
        } catch (Throwable e2) {
            Throwable th = e2;
            f291f = false;
            int i = 0;
            while (i < 20) {
                if (f290e[i] == null) {
                    break;
                }
                i++;
            }
            i = 20;
            f292g = new ck("Bad method: " + i, th);
        } catch (Throwable e22) {
            Throwable th2 = e22;
            f291f = false;
            String str = EnvironmentCompat.MEDIA_UNKNOWN;
            if (f286a == null) {
                str = "address";
            } else if (f287b == null) {
                str = "fd";
            } else if (f288c == null) {
                str = "localport";
            } else if (f289d == null) {
                str = "port";
            }
            f292g = new ck("No such field: " + str, th2);
        } catch (Throwable e222) {
            f291f = false;
            f292g = e222;
        }
    }

    public ac(C1068e c1068e, C1058d c1058d, SocketImpl socketImpl) {
        this.f293h = new LinkedList();
        if (c1068e == null) {
            throw new NullPointerException("dispatch was null");
        } else if (socketImpl == null) {
            throw new NullPointerException("delegate was null");
        } else {
            this.f294i = c1068e;
            this.f295j = c1058d;
            this.f296k = socketImpl;
            m244f();
        }
    }

    public static boolean m241c() {
        return f291f;
    }

    public static Throwable m242d() {
        return f292g;
    }

    private void m244f() {
        try {
            this.address = (InetAddress) f286a.get(this.f296k);
            this.fd = (FileDescriptor) f287b.get(this.f296k);
            this.localport = f288c.getInt(this.f296k);
            this.port = f289d.getInt(this.f296k);
        } catch (Throwable e) {
            throw new ck(e);
        } catch (Throwable e2) {
            throw new ck(e2);
        }
    }

    private Object m238a(int i, Object... objArr) {
        Throwable e;
        try {
            f286a.set(this.f296k, this.address);
            f287b.set(this.f296k, this.fd);
            f288c.setInt(this.f296k, this.localport);
            f289d.setInt(this.f296k, this.port);
            try {
                Object invoke = f290e[i].invoke(this.f296k, objArr);
                m244f();
                return invoke;
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
                    throw new ck(e222);
                }
            } catch (Throwable e2222) {
                throw new ck(e2222);
            } catch (Throwable e22222) {
                throw new ck(e22222);
            } catch (Throwable th2) {
                m244f();
            }
        } catch (Throwable e222222) {
            throw new ck(e222222);
        } catch (Throwable e2222222) {
            throw new ck(e2222222);
        }
    }

    private Object m239b(int i, Object... objArr) {
        try {
            return m238a(i, objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e2) {
            throw new ck(e2);
        }
    }

    private Object m240c(int i, Object... objArr) {
        try {
            return m238a(i, objArr);
        } catch (IOException e) {
            throw e;
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Throwable e3) {
            throw new ck(e3);
        }
    }

    public final InputStream getInputStream() {
        InputStream inputStream = (InputStream) m240c(10, new Object[0]);
        if (inputStream == null) {
            return inputStream;
        }
        try {
            if (this.f298m != null && this.f298m.m841a(inputStream)) {
                return this.f298m;
            }
            this.f298m = new C1093x(this, inputStream, this.f294i);
            return this.f298m;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return inputStream;
        }
    }

    public final OutputStream getOutputStream() {
        OutputStream outputStream = (OutputStream) m240c(12, new Object[0]);
        if (outputStream == null) {
            return outputStream;
        }
        try {
            if (this.f297l != null && this.f297l.m829a(outputStream)) {
                return this.f297l;
            }
            this.f297l = new C1092w(this, outputStream);
            return this.f297l;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return outputStream;
        }
    }

    public final void create(boolean stream) {
        m240c(7, Boolean.valueOf(stream));
    }

    public final void connect(String host, int port) {
        try {
            m240c(6, host, Integer.valueOf(port));
        } catch (Throwable e) {
            if (host != null) {
                try {
                    C1050c a = m237a(false);
                    a.m640b();
                    a.m643c();
                    a.m649f();
                    a.m642b(host);
                    a.m632a(port);
                    a.m638a(e);
                    this.f294i.m761a(a, C1049a.SOCKET_IMPL_CONNECT);
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.m752a(th);
                }
            }
            throw e;
        }
    }

    public final void connect(InetAddress address, int port) {
        try {
            m240c(4, address, Integer.valueOf(port));
        } catch (Throwable e) {
            if (address != null) {
                try {
                    C1050c a = m237a(false);
                    a.m640b();
                    a.m643c();
                    a.m649f();
                    a.m639a(address);
                    a.m632a(port);
                    a.m638a(e);
                    this.f294i.m761a(a, C1049a.SOCKET_IMPL_CONNECT);
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.m752a(th);
                }
            }
            throw e;
        }
    }

    public final void connect(SocketAddress address, int timeout) {
        try {
            m240c(5, address, Integer.valueOf(timeout));
        } catch (Throwable e) {
            if (address != null) {
                try {
                    if (address instanceof InetSocketAddress) {
                        C1050c a = m237a(false);
                        InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
                        a.m640b();
                        a.m643c();
                        a.m649f();
                        a.m639a(inetSocketAddress.getAddress());
                        a.m632a(inetSocketAddress.getPort());
                        a.m638a(e);
                        this.f294i.m761a(a, C1049a.SOCKET_IMPL_CONNECT);
                    }
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.m752a(th);
                }
            }
            throw e;
        }
    }

    public final void bind(InetAddress host, int port) {
        m240c(2, host, Integer.valueOf(port));
    }

    public final void listen(int backlog) {
        m240c(14, Integer.valueOf(backlog));
    }

    public final void accept(SocketImpl s) {
        m240c(0, s);
    }

    public final int available() {
        Integer num = (Integer) m240c(1, new Object[0]);
        if (num != null) {
            return num.intValue();
        }
        throw new ck("Received a null Integer");
    }

    public final void close() {
        m240c(3, new Object[0]);
        try {
            if (this.f298m != null) {
                this.f298m.m845d();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public final void shutdownInput() {
        m240c(17, new Object[0]);
    }

    public final void shutdownOutput() {
        m240c(18, new Object[0]);
    }

    public final FileDescriptor getFileDescriptor() {
        return (FileDescriptor) m239b(8, new Object[0]);
    }

    public final InetAddress getInetAddress() {
        return (InetAddress) m239b(9, new Object[0]);
    }

    public final int getPort() {
        return ((Integer) m239b(13, new Object[0])).intValue();
    }

    public final boolean supportsUrgentData() {
        return ((Boolean) m239b(19, new Object[0])).booleanValue();
    }

    public final void sendUrgentData(int data) {
        m240c(15, Integer.valueOf(data));
    }

    public final int getLocalPort() {
        return ((Integer) m239b(11, new Object[0])).intValue();
    }

    public final String toString() {
        return this.f296k.toString();
    }

    public final void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        m239b(16, Integer.valueOf(connectionTime), Integer.valueOf(latency), Integer.valueOf(bandwidth));
    }

    public final void setOption(int optID, Object value) {
        this.f296k.setOption(optID, value);
    }

    public final Object getOption(int optID) {
        return this.f296k.getOption(optID);
    }

    private C1050c m237a(boolean z) {
        C1050c c1050c = new C1050c();
        InetAddress inetAddress = getInetAddress();
        if (inetAddress != null) {
            c1050c.m639a(inetAddress);
        }
        int port = getPort();
        if (port > 0) {
            c1050c.m632a(port);
        }
        if (z) {
            c1050c.m635a(C1077a.HTTP);
        }
        if (this.f295j != null) {
            c1050c.f589j = this.f295j.m692a();
        }
        if (bc.m450b()) {
            c1050c.m634a(bc.m448a());
        }
        return c1050c;
    }

    public final C1050c m245a() {
        return m237a(true);
    }

    public final void m246a(C1050c c1050c) {
        synchronized (this.f293h) {
            this.f293h.add(c1050c);
        }
    }

    public final C1050c m247b() {
        C1050c c1050c;
        synchronized (this.f293h) {
            c1050c = (C1050c) this.f293h.poll();
        }
        return c1050c;
    }

    public static void m243e() {
        if (f291f) {
            SocketImpl acVar = new ac(new C1068e(new C09962()), null, new C09951());
            try {
                acVar.setOption(0, new Object());
                acVar.getOption(0);
                acVar.sendUrgentData(0);
                acVar.listen(0);
                acVar.getOutputStream();
                acVar.getInputStream();
                acVar.create(false);
                acVar.connect(null, 0);
                acVar.connect(null, 0);
                acVar.connect(null, 0);
                acVar.close();
                acVar.bind(null, 0);
                acVar.available();
                acVar.accept(acVar);
                acVar.getFileDescriptor();
                acVar.getInetAddress();
                acVar.getLocalPort();
                acVar.getPort();
                acVar.setPerformancePreferences(0, 0, 0);
                acVar.shutdownInput();
                acVar.shutdownOutput();
                acVar.supportsUrgentData();
            } catch (IOException e) {
            } catch (ck e2) {
                throw e2;
            } catch (Throwable th) {
                ck ckVar = new ck(th);
            }
        } else {
            throw new ck(f292g);
        }
    }
}
