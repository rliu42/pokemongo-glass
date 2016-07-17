package crittercism.android;

import java.io.OutputStream;

/* renamed from: crittercism.android.w */
public final class C1092w extends OutputStream implements al {
    private ae f875a;
    private OutputStream f876b;
    private C1050c f877c;
    private af f878d;

    public C1092w(ae aeVar, OutputStream outputStream) {
        if (aeVar == null) {
            throw new NullPointerException("socket was null");
        } else if (outputStream == null) {
            throw new NullPointerException("output stream was null");
        } else {
            this.f875a = aeVar;
            this.f876b = outputStream;
            this.f878d = m830b();
            if (this.f878d == null) {
                throw new NullPointerException("parser was null");
            }
        }
    }

    public final void flush() {
        this.f876b.flush();
    }

    public final void close() {
        this.f876b.close();
    }

    public final void write(int oneByte) {
        this.f876b.write(oneByte);
        try {
            this.f878d.m252a(oneByte);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            this.f878d = as.f322d;
        }
    }

    public final void write(byte[] buffer) {
        this.f876b.write(buffer);
        if (buffer != null) {
            m822a(buffer, 0, buffer.length);
        }
    }

    public final void write(byte[] buffer, int offset, int byteCount) {
        this.f876b.write(buffer, offset, byteCount);
        if (buffer != null) {
            m822a(buffer, offset, byteCount);
        }
    }

    private void m822a(byte[] bArr, int i, int i2) {
        try {
            this.f878d.m251a(bArr, i, i2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            this.f878d = as.f322d;
        }
    }

    public final void m828a(String str, String str2) {
        C1050c d = m823d();
        d.m640b();
        d.f585f = str;
        d.f588i = null;
        C1078k c1078k = d.f587h;
        if (str2 != null) {
            c1078k.f834c = str2;
        }
        this.f875a.m229a(d);
    }

    public final void m825a(int i) {
    }

    public final void m826a(af afVar) {
        this.f878d = afVar;
    }

    public final af m824a() {
        return this.f878d;
    }

    public final void m831b(int i) {
        C1050c c1050c = this.f877c;
        this.f877c = null;
        if (c1050c != null) {
            c1050c.m646d((long) i);
        }
    }

    private C1050c m823d() {
        if (this.f877c == null) {
            this.f877c = this.f875a.m228a();
        }
        C1050c c1050c = this.f877c;
        return this.f877c;
    }

    public final af m830b() {
        return new an(this);
    }

    public final String m832c() {
        C1050c d = m823d();
        if (d != null) {
            return d.f585f;
        }
        return null;
    }

    public final void m827a(String str) {
        C1050c d = m823d();
        if (d != null) {
            d.m642b(str);
        }
    }

    public final boolean m829a(OutputStream outputStream) {
        return this.f876b == outputStream;
    }
}
