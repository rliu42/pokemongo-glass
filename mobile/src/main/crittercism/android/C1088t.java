package crittercism.android;

import java.io.InputStream;

/* renamed from: crittercism.android.t */
public final class C1088t extends InputStream {
    private final InputStream f861a;
    private final C1068e f862b;
    private final C1050c f863c;

    public C1088t(InputStream inputStream, C1068e c1068e, C1050c c1050c) {
        if (inputStream == null) {
            throw new NullPointerException("delegate was null");
        } else if (c1068e == null) {
            throw new NullPointerException("dispatch was null");
        } else if (c1050c == null) {
            throw new NullPointerException("stats were null");
        } else {
            this.f861a = inputStream;
            this.f862b = c1068e;
            this.f863c = c1050c;
        }
    }

    public final int available() {
        return this.f861a.available();
    }

    public final void close() {
        this.f861a.close();
    }

    public final void mark(int readlimit) {
        this.f861a.mark(readlimit);
    }

    public final boolean markSupported() {
        return this.f861a.markSupported();
    }

    private void m814a(int i, int i2) {
        try {
            if (this.f863c == null) {
                return;
            }
            if (i == -1) {
                this.f862b.m760a(this.f863c);
            } else {
                this.f863c.m633a((long) i2);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    private void m815a(Exception exception) {
        try {
            this.f863c.m638a((Throwable) exception);
            this.f862b.m760a(this.f863c);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public final int read() {
        try {
            int read = this.f861a.read();
            m814a(read, 1);
            return read;
        } catch (Exception e) {
            m815a(e);
            throw e;
        }
    }

    public final int read(byte[] buffer) {
        try {
            int read = this.f861a.read(buffer);
            m814a(read, read);
            return read;
        } catch (Exception e) {
            m815a(e);
            throw e;
        }
    }

    public final int read(byte[] buffer, int offset, int length) {
        try {
            int read = this.f861a.read(buffer, offset, length);
            m814a(read, read);
            return read;
        } catch (Exception e) {
            m815a(e);
            throw e;
        }
    }

    public final synchronized void reset() {
        this.f861a.reset();
    }

    public final long skip(long byteCount) {
        long skip = this.f861a.skip(byteCount);
        try {
            if (this.f863c != null) {
                this.f863c.m633a(skip);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
        return skip;
    }
}
