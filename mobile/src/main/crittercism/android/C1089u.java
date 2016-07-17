package crittercism.android;

import java.io.OutputStream;

/* renamed from: crittercism.android.u */
public final class C1089u extends OutputStream {
    private final OutputStream f864a;
    private final C1050c f865b;

    public C1089u(OutputStream outputStream, C1050c c1050c) {
        if (outputStream == null) {
            throw new NullPointerException("delegate was null");
        } else if (c1050c == null) {
            throw new NullPointerException("stats were null");
        } else {
            this.f864a = outputStream;
            this.f865b = c1050c;
        }
    }

    public final void flush() {
        this.f864a.flush();
    }

    public final void close() {
        this.f864a.close();
    }

    public final void write(int oneByte) {
        try {
            if (this.f865b != null) {
                this.f865b.m640b();
                this.f865b.m644c(1);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
        this.f864a.write(oneByte);
    }

    public final void write(byte[] buffer) {
        if (this.f865b != null) {
            this.f865b.m640b();
            if (buffer != null) {
                this.f865b.m644c((long) buffer.length);
            }
        }
        this.f864a.write(buffer);
    }

    public final void write(byte[] buffer, int offset, int byteCount) {
        if (this.f865b != null) {
            this.f865b.m640b();
            if (buffer != null) {
                this.f865b.m644c((long) byteCount);
            }
        }
        this.f864a.write(buffer, offset, byteCount);
    }
}
