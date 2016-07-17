package crittercism.android;

import crittercism.android.C1050c.C1049a;
import crittercism.android.C1078k.C1077a;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public final class aa extends SSLSocket implements ae {
    private SSLSocket f277a;
    private C1068e f278b;
    private C1058d f279c;
    private final Queue f280d;
    private C1092w f281e;
    private C1093x f282f;

    public aa(SSLSocket sSLSocket, C1068e c1068e, C1058d c1058d) {
        this.f280d = new LinkedList();
        if (sSLSocket == null) {
            throw new NullPointerException("delegate was null");
        } else if (c1068e == null) {
            throw new NullPointerException("dispatch was null");
        } else {
            this.f277a = sSLSocket;
            this.f278b = c1068e;
            this.f279c = c1058d;
        }
    }

    public final void addHandshakeCompletedListener(HandshakeCompletedListener listener) {
        this.f277a.addHandshakeCompletedListener(listener);
    }

    public final boolean getEnableSessionCreation() {
        return this.f277a.getEnableSessionCreation();
    }

    public final String[] getEnabledCipherSuites() {
        return this.f277a.getEnabledCipherSuites();
    }

    public final String[] getEnabledProtocols() {
        return this.f277a.getEnabledProtocols();
    }

    public final boolean getNeedClientAuth() {
        return this.f277a.getNeedClientAuth();
    }

    public final SSLSession getSession() {
        return this.f277a.getSession();
    }

    public final String[] getSupportedCipherSuites() {
        return this.f277a.getSupportedCipherSuites();
    }

    public final String[] getSupportedProtocols() {
        return this.f277a.getSupportedProtocols();
    }

    public final boolean getUseClientMode() {
        return this.f277a.getUseClientMode();
    }

    public final boolean getWantClientAuth() {
        return this.f277a.getWantClientAuth();
    }

    public final void removeHandshakeCompletedListener(HandshakeCompletedListener listener) {
        this.f277a.removeHandshakeCompletedListener(listener);
    }

    public final void setEnableSessionCreation(boolean flag) {
        this.f277a.setEnableSessionCreation(flag);
    }

    public final void setEnabledCipherSuites(String[] suites) {
        this.f277a.setEnabledCipherSuites(suites);
    }

    public final void setEnabledProtocols(String[] protocols) {
        this.f277a.setEnabledProtocols(protocols);
    }

    public final void setNeedClientAuth(boolean need) {
        this.f277a.setNeedClientAuth(need);
    }

    public final void setUseClientMode(boolean mode) {
        this.f277a.setUseClientMode(mode);
    }

    public final void setWantClientAuth(boolean want) {
        this.f277a.setWantClientAuth(want);
    }

    public final void startHandshake() {
        try {
            this.f277a.startHandshake();
        } catch (Throwable e) {
            try {
                C1050c a = m231a(true);
                a.m640b();
                a.m643c();
                a.m649f();
                a.m638a(e);
                this.f278b.m761a(a, C1049a.SSL_SOCKET_START_HANDSHAKE);
            } catch (ThreadDeath e2) {
                throw e2;
            } catch (Throwable th) {
                dx.m752a(th);
            }
            throw e;
        }
    }

    public final void bind(SocketAddress localAddr) {
        this.f277a.bind(localAddr);
    }

    public final void close() {
        this.f277a.close();
        try {
            if (this.f282f != null) {
                this.f282f.m845d();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public final void connect(SocketAddress remoteAddr, int timeout) {
        this.f277a.connect(remoteAddr, timeout);
    }

    public final void connect(SocketAddress remoteAddr) {
        this.f277a.connect(remoteAddr);
    }

    public final SocketChannel getChannel() {
        return this.f277a.getChannel();
    }

    public final InetAddress getInetAddress() {
        return this.f277a.getInetAddress();
    }

    public final InputStream getInputStream() {
        InputStream inputStream = this.f277a.getInputStream();
        if (inputStream == null) {
            return inputStream;
        }
        try {
            if (this.f282f != null && this.f282f.m841a(inputStream)) {
                return this.f282f;
            }
            this.f282f = new C1093x(this, inputStream, this.f278b);
            return this.f282f;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return inputStream;
        }
    }

    public final boolean getKeepAlive() {
        return this.f277a.getKeepAlive();
    }

    public final InetAddress getLocalAddress() {
        return this.f277a.getLocalAddress();
    }

    public final int getLocalPort() {
        return this.f277a.getLocalPort();
    }

    public final SocketAddress getLocalSocketAddress() {
        return this.f277a.getLocalSocketAddress();
    }

    public final boolean getOOBInline() {
        return this.f277a.getOOBInline();
    }

    public final OutputStream getOutputStream() {
        OutputStream outputStream = this.f277a.getOutputStream();
        if (outputStream == null) {
            return outputStream;
        }
        try {
            if (this.f281e != null && this.f281e.m829a(outputStream)) {
                return this.f281e;
            }
            C1092w c1092w = this.f281e;
            this.f281e = new C1092w(this, outputStream);
            return this.f281e;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return outputStream;
        }
    }

    public final int getPort() {
        return this.f277a.getPort();
    }

    public final int getReceiveBufferSize() {
        return this.f277a.getReceiveBufferSize();
    }

    public final SocketAddress getRemoteSocketAddress() {
        return this.f277a.getRemoteSocketAddress();
    }

    public final boolean getReuseAddress() {
        return this.f277a.getReuseAddress();
    }

    public final int getSendBufferSize() {
        return this.f277a.getSendBufferSize();
    }

    public final int getSoLinger() {
        return this.f277a.getSoLinger();
    }

    public final int getSoTimeout() {
        return this.f277a.getSoTimeout();
    }

    public final boolean getTcpNoDelay() {
        return this.f277a.getTcpNoDelay();
    }

    public final int getTrafficClass() {
        return this.f277a.getTrafficClass();
    }

    public final boolean isBound() {
        return this.f277a.isBound();
    }

    public final boolean isClosed() {
        return this.f277a.isClosed();
    }

    public final boolean isConnected() {
        return this.f277a.isConnected();
    }

    public final boolean isInputShutdown() {
        return this.f277a.isInputShutdown();
    }

    public final boolean isOutputShutdown() {
        return this.f277a.isOutputShutdown();
    }

    public final void sendUrgentData(int value) {
        this.f277a.sendUrgentData(value);
    }

    public final void setKeepAlive(boolean keepAlive) {
        this.f277a.setKeepAlive(keepAlive);
    }

    public final void setOOBInline(boolean oobinline) {
        this.f277a.setOOBInline(oobinline);
    }

    public final void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        this.f277a.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    public final void setReceiveBufferSize(int size) {
        this.f277a.setReceiveBufferSize(size);
    }

    public final void setReuseAddress(boolean reuse) {
        this.f277a.setReuseAddress(reuse);
    }

    public final void setSendBufferSize(int size) {
        this.f277a.setSendBufferSize(size);
    }

    public final void setSoLinger(boolean on, int timeout) {
        this.f277a.setSoLinger(on, timeout);
    }

    public final void setSoTimeout(int timeout) {
        this.f277a.setSoTimeout(timeout);
    }

    public final void setTcpNoDelay(boolean on) {
        this.f277a.setTcpNoDelay(on);
    }

    public final void setTrafficClass(int value) {
        this.f277a.setTrafficClass(value);
    }

    public final void shutdownInput() {
        this.f277a.shutdownInput();
    }

    public final void shutdownOutput() {
        this.f277a.shutdownOutput();
    }

    public final String toString() {
        return this.f277a.toString();
    }

    public final boolean equals(Object o) {
        return this.f277a.equals(o);
    }

    public final int hashCode() {
        return this.f277a.hashCode();
    }

    public final C1050c m232a() {
        return m231a(false);
    }

    private C1050c m231a(boolean z) {
        C1050c c1050c = new C1050c();
        InetAddress inetAddress = this.f277a.getInetAddress();
        if (inetAddress != null) {
            c1050c.m639a(inetAddress);
        }
        if (z) {
            c1050c.m632a(getPort());
        }
        c1050c.m635a(C1077a.HTTPS);
        if (this.f279c != null) {
            c1050c.f589j = this.f279c.m692a();
        }
        if (bc.m450b()) {
            c1050c.m634a(bc.m448a());
        }
        return c1050c;
    }

    public final void m233a(C1050c c1050c) {
        if (c1050c != null) {
            synchronized (this.f280d) {
                this.f280d.add(c1050c);
            }
        }
    }

    public final C1050c m234b() {
        C1050c c1050c;
        synchronized (this.f280d) {
            c1050c = (C1050c) this.f280d.poll();
        }
        return c1050c;
    }
}
