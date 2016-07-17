package crittercism.android;

import crittercism.android.C1050c.C1049a;
import java.io.InputStream;

/* renamed from: crittercism.android.x */
public final class C1093x extends InputStream implements al {
    private ae f879a;
    private C1050c f880b;
    private InputStream f881c;
    private C1068e f882d;
    private af f883e;

    public C1093x(ae aeVar, InputStream inputStream, C1068e c1068e) {
        if (aeVar == null) {
            throw new NullPointerException("socket was null");
        } else if (inputStream == null) {
            throw new NullPointerException("delegate was null");
        } else if (c1068e == null) {
            throw new NullPointerException("dispatch was null");
        } else {
            this.f879a = aeVar;
            this.f881c = inputStream;
            this.f882d = c1068e;
            this.f883e = m842b();
            if (this.f883e == null) {
                throw new NullPointerException("parser was null");
            }
        }
    }

    public final int available() {
        return this.f881c.available();
    }

    public final void close() {
        try {
            this.f883e.m260f();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
        this.f881c.close();
    }

    public final void mark(int readlimit) {
        this.f881c.mark(readlimit);
    }

    public final boolean markSupported() {
        return this.f881c.markSupported();
    }

    private void m833a(Exception exception) {
        try {
            C1050c e = m835e();
            e.m638a((Throwable) exception);
            this.f882d.m761a(e, C1049a.PARSING_INPUT_STREAM_LOG_ERROR);
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (IllegalStateException e3) {
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public final int read() {
        try {
            int read = this.f881c.read();
            try {
                this.f883e.m252a(read);
            } catch (ThreadDeath e) {
                throw e;
            } catch (IllegalStateException e2) {
                this.f883e = as.f322d;
            } catch (Throwable th) {
                this.f883e = as.f322d;
                dx.m752a(th);
            }
            return read;
        } catch (Exception e3) {
            m833a(e3);
            throw e3;
        }
    }

    public final int read(byte[] buffer) {
        try {
            int read = this.f881c.read(buffer);
            m834a(buffer, 0, read);
            return read;
        } catch (Exception e) {
            m833a(e);
            throw e;
        }
    }

    public final int read(byte[] buffer, int offset, int length) {
        try {
            int read = this.f881c.read(buffer, offset, length);
            m834a(buffer, offset, read);
            return read;
        } catch (Exception e) {
            m833a(e);
            throw e;
        }
    }

    private void m834a(byte[] bArr, int i, int i2) {
        try {
            this.f883e.m251a(bArr, i, i2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (IllegalStateException e2) {
            this.f883e = as.f322d;
        } catch (Throwable th) {
            this.f883e = as.f322d;
            dx.m752a(th);
        }
    }

    public final synchronized void reset() {
        this.f881c.reset();
    }

    public final long skip(long byteCount) {
        return this.f881c.skip(byteCount);
    }

    public final void m840a(String str, String str2) {
    }

    public final void m837a(int i) {
        C1050c e = m835e();
        e.m643c();
        e.f584e = i;
    }

    public final void m838a(af afVar) {
        this.f883e = afVar;
    }

    public final af m836a() {
        return this.f883e;
    }

    public final void m843b(int i) {
        C1050c c1050c = null;
        C1050c c1050c2 = this.f880b;
        if (this.f880b != null) {
            int i2 = this.f880b.f584e;
            if (i2 >= 100 && i2 < 200) {
                c1050c = new C1050c(this.f880b.m631a());
                c1050c.m648e(this.f880b.f580a);
                c1050c.m646d(this.f880b.f583d);
                c1050c.f585f = this.f880b.f585f;
            }
            this.f880b.m641b((long) i);
            this.f882d.m761a(this.f880b, C1049a.INPUT_STREAM_FINISHED);
        }
        this.f880b = c1050c;
    }

    private C1050c m835e() {
        if (this.f880b == null) {
            this.f880b = this.f879a.m230b();
        }
        if (this.f880b != null) {
            return this.f880b;
        }
        throw new IllegalStateException("No statistics were queued up.");
    }

    public final af m842b() {
        return new ap(this);
    }

    public final String m844c() {
        return m835e().f585f;
    }

    public final void m839a(String str) {
    }

    public final boolean m841a(InputStream inputStream) {
        return this.f881c == inputStream;
    }

    public final void m845d() {
        if (this.f880b != null) {
            cn cnVar = this.f880b.f586g;
            Object obj = (cnVar.f659a == co.Android.ordinal() && cnVar.f660b == cm.OK.m666a()) ? 1 : null;
            if (obj != null && this.f883e != null) {
                this.f883e.m260f();
            }
        }
    }
}
