package crittercism.android;

import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public final class ab extends SSLSocketFactory {
    private SSLSocketFactory f283a;
    private C1068e f284b;
    private C1058d f285c;

    public ab(SSLSocketFactory sSLSocketFactory, C1068e c1068e, C1058d c1058d) {
        this.f283a = sSLSocketFactory;
        this.f284b = c1068e;
        this.f285c = c1058d;
    }

    public final SSLSocketFactory m236a() {
        return this.f283a;
    }

    public final String[] getDefaultCipherSuites() {
        return this.f283a.getDefaultCipherSuites();
    }

    public final String[] getSupportedCipherSuites() {
        return this.f283a.getSupportedCipherSuites();
    }

    private Socket m235a(Socket socket) {
        if (socket == null) {
            return socket;
        }
        try {
            if (!(socket instanceof SSLSocket)) {
                return socket;
            }
            return new aa((SSLSocket) socket, this.f284b, this.f285c);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return socket;
        }
    }

    public final Socket createSocket(Socket s, String host, int port, boolean autoClose) {
        return m235a(this.f283a.createSocket(s, host, port, autoClose));
    }

    public final Socket createSocket(String host, int port) {
        return m235a(this.f283a.createSocket(host, port));
    }

    public final Socket createSocket(String host, int port, InetAddress localHost, int localPort) {
        return m235a(this.f283a.createSocket(host, port, localHost, localPort));
    }

    public final Socket createSocket(InetAddress host, int port) {
        return m235a(this.f283a.createSocket(host, port));
    }

    public final Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) {
        return m235a(this.f283a.createSocket(address, port, localAddress, localPort));
    }

    public final Socket createSocket() {
        return m235a(this.f283a.createSocket());
    }
}
