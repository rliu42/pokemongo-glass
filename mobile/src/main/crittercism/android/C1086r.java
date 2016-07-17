package crittercism.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.util.Map;

/* renamed from: crittercism.android.r */
public final class C1086r extends HttpURLConnection {
    private C1068e f849a;
    private HttpURLConnection f850b;
    private C1050c f851c;
    private C1058d f852d;
    private boolean f853e;
    private boolean f854f;

    public C1086r(HttpURLConnection httpURLConnection, C1068e c1068e, C1058d c1058d) {
        super(httpURLConnection.getURL());
        this.f853e = false;
        this.f854f = false;
        this.f850b = httpURLConnection;
        this.f849a = c1068e;
        this.f852d = c1058d;
        this.f851c = new C1050c(httpURLConnection.getURL());
    }

    private void m808a() {
        try {
            if (!this.f854f) {
                this.f854f = true;
                this.f851c.f585f = this.f850b.getRequestMethod();
                this.f851c.m640b();
                this.f851c.f589j = this.f852d.m692a();
                if (bc.m450b()) {
                    this.f851c.m634a(bc.m448a());
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    private void m809a(Throwable th) {
        try {
            if (!this.f853e) {
                this.f853e = true;
                this.f851c.m643c();
                this.f851c.m638a(th);
                this.f849a.m760a(this.f851c);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th2) {
            dx.m752a(th);
        }
    }

    private void m810b() {
        Object obj = null;
        try {
            if (!this.f853e) {
                this.f853e = true;
                this.f851c.m643c();
                if (this.f850b.getHeaderFields() != null) {
                    C1084p c1084p = new C1084p(this.f850b.getHeaderFields());
                    int b = c1084p.m805b("Content-Length");
                    if (b != -1) {
                        this.f851c.m641b((long) b);
                        obj = 1;
                    }
                    long a = c1084p.m804a("X-Android-Sent-Millis");
                    long a2 = c1084p.m804a("X-Android-Received-Millis");
                    if (!(a == Long.MAX_VALUE || a2 == Long.MAX_VALUE)) {
                        this.f851c.m648e(a);
                        this.f851c.m650f(a2);
                    }
                }
                try {
                    this.f851c.f584e = this.f850b.getResponseCode();
                } catch (IOException e) {
                }
                if (obj != null) {
                    this.f849a.m760a(this.f851c);
                }
            }
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public final void connect() {
        this.f850b.connect();
    }

    public final boolean getAllowUserInteraction() {
        return this.f850b.getAllowUserInteraction();
    }

    public final Object getContent() {
        m808a();
        try {
            Object content = this.f850b.getContent();
            m810b();
            return content;
        } catch (Throwable e) {
            m809a(e);
            throw e;
        }
    }

    public final Object getContent(Class[] types) {
        m808a();
        try {
            Object content = this.f850b.getContent(types);
            m810b();
            return content;
        } catch (Throwable e) {
            m809a(e);
            throw e;
        }
    }

    public final String getContentEncoding() {
        m808a();
        String contentEncoding = this.f850b.getContentEncoding();
        m810b();
        return contentEncoding;
    }

    public final int getContentLength() {
        return this.f850b.getContentLength();
    }

    public final String getContentType() {
        m808a();
        String contentType = this.f850b.getContentType();
        m810b();
        return contentType;
    }

    public final long getDate() {
        return this.f850b.getDate();
    }

    public final boolean getDefaultUseCaches() {
        return this.f850b.getDefaultUseCaches();
    }

    public final boolean getDoInput() {
        return this.f850b.getDoInput();
    }

    public final boolean getDoOutput() {
        return this.f850b.getDoOutput();
    }

    public final long getExpiration() {
        return this.f850b.getExpiration();
    }

    public final String getHeaderField(int pos) {
        m808a();
        String headerField = this.f850b.getHeaderField(pos);
        m810b();
        return headerField;
    }

    public final Map getHeaderFields() {
        m808a();
        Map headerFields = this.f850b.getHeaderFields();
        m810b();
        return headerFields;
    }

    public final Map getRequestProperties() {
        return this.f850b.getRequestProperties();
    }

    public final void addRequestProperty(String field, String newValue) {
        this.f850b.addRequestProperty(field, newValue);
    }

    public final String getHeaderField(String key) {
        m808a();
        String headerField = this.f850b.getHeaderField(key);
        m810b();
        return headerField;
    }

    public final long getHeaderFieldDate(String field, long defaultValue) {
        m808a();
        long headerFieldDate = this.f850b.getHeaderFieldDate(field, defaultValue);
        m810b();
        return headerFieldDate;
    }

    public final int getHeaderFieldInt(String field, int defaultValue) {
        m808a();
        int headerFieldInt = this.f850b.getHeaderFieldInt(field, defaultValue);
        m810b();
        return headerFieldInt;
    }

    public final String getHeaderFieldKey(int posn) {
        m808a();
        String headerFieldKey = this.f850b.getHeaderFieldKey(posn);
        m810b();
        return headerFieldKey;
    }

    public final long getIfModifiedSince() {
        return this.f850b.getIfModifiedSince();
    }

    public final InputStream getInputStream() {
        m808a();
        try {
            InputStream inputStream = this.f850b.getInputStream();
            m810b();
            if (inputStream != null) {
                try {
                    return new C1088t(inputStream, this.f849a, this.f851c);
                } catch (ThreadDeath e) {
                    throw e;
                } catch (Throwable th) {
                    dx.m752a(th);
                }
            }
            return inputStream;
        } catch (Throwable th2) {
            m809a(th2);
            throw th2;
        }
    }

    public final long getLastModified() {
        return this.f850b.getLastModified();
    }

    public final OutputStream getOutputStream() {
        OutputStream outputStream = this.f850b.getOutputStream();
        if (outputStream != null) {
            try {
                return new C1089u(outputStream, this.f851c);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.m752a(th);
            }
        }
        return outputStream;
    }

    public final Permission getPermission() {
        return this.f850b.getPermission();
    }

    public final String getRequestProperty(String field) {
        return this.f850b.getRequestProperty(field);
    }

    public final URL getURL() {
        return this.f850b.getURL();
    }

    public final boolean getUseCaches() {
        return this.f850b.getUseCaches();
    }

    public final void setAllowUserInteraction(boolean newValue) {
        this.f850b.setAllowUserInteraction(newValue);
    }

    public final void setDefaultUseCaches(boolean newValue) {
        this.f850b.setDefaultUseCaches(newValue);
    }

    public final void setDoInput(boolean newValue) {
        this.f850b.setDoInput(newValue);
    }

    public final void setDoOutput(boolean newValue) {
        this.f850b.setDoOutput(newValue);
    }

    public final void setIfModifiedSince(long newValue) {
        this.f850b.setIfModifiedSince(newValue);
    }

    public final void setRequestProperty(String field, String newValue) {
        this.f850b.setRequestProperty(field, newValue);
    }

    public final void setUseCaches(boolean newValue) {
        this.f850b.setUseCaches(newValue);
    }

    public final void setConnectTimeout(int timeoutMillis) {
        this.f850b.setConnectTimeout(timeoutMillis);
    }

    public final int getConnectTimeout() {
        return this.f850b.getConnectTimeout();
    }

    public final void setReadTimeout(int timeoutMillis) {
        this.f850b.setReadTimeout(timeoutMillis);
    }

    public final int getReadTimeout() {
        return this.f850b.getReadTimeout();
    }

    public final String toString() {
        return this.f850b.toString();
    }

    public final void disconnect() {
        this.f850b.disconnect();
        try {
            if (this.f853e && !this.f851c.f581b) {
                this.f849a.m760a(this.f851c);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public final boolean usingProxy() {
        return this.f850b.usingProxy();
    }

    public final InputStream getErrorStream() {
        m808a();
        InputStream errorStream = this.f850b.getErrorStream();
        m810b();
        if (errorStream != null) {
            try {
                return new C1088t(errorStream, this.f849a, this.f851c);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.m752a(th);
            }
        }
        return errorStream;
    }

    public final String getRequestMethod() {
        return this.f850b.getRequestMethod();
    }

    public final int getResponseCode() {
        m808a();
        try {
            int responseCode = this.f850b.getResponseCode();
            m810b();
            return responseCode;
        } catch (Throwable e) {
            m809a(e);
            throw e;
        }
    }

    public final String getResponseMessage() {
        m808a();
        try {
            String responseMessage = this.f850b.getResponseMessage();
            m810b();
            return responseMessage;
        } catch (Throwable e) {
            m809a(e);
            throw e;
        }
    }

    public final void setRequestMethod(String method) {
        this.f850b.setRequestMethod(method);
    }

    public final boolean getInstanceFollowRedirects() {
        return this.f850b.getInstanceFollowRedirects();
    }

    public final void setInstanceFollowRedirects(boolean followRedirects) {
        this.f850b.setInstanceFollowRedirects(followRedirects);
    }

    public final void setFixedLengthStreamingMode(int contentLength) {
        this.f850b.setFixedLengthStreamingMode(contentLength);
    }

    public final void setChunkedStreamingMode(int chunkLength) {
        this.f850b.setChunkedStreamingMode(chunkLength);
    }

    public final boolean equals(Object o) {
        return this.f850b.equals(o);
    }

    public final int hashCode() {
        return this.f850b.hashCode();
    }
}
