package org.apache.commons.io.input;

import com.google.android.gms.location.places.Place;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Tailer implements Runnable {
    private static final int DEFAULT_BUFSIZE = 4096;
    private static final int DEFAULT_DELAY_MILLIS = 1000;
    private static final String RAF_MODE = "r";
    private final long delayMillis;
    private final boolean end;
    private final File file;
    private final byte[] inbuf;
    private final TailerListener listener;
    private final boolean reOpen;
    private volatile boolean run;

    public Tailer(File file, TailerListener listener) {
        this(file, listener, 1000);
    }

    public Tailer(File file, TailerListener listener, long delayMillis) {
        this(file, listener, delayMillis, false);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end) {
        this(file, listener, delayMillis, end, (int) DEFAULT_BUFSIZE);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
        this(file, listener, delayMillis, end, reOpen, DEFAULT_BUFSIZE);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
        this(file, listener, delayMillis, end, false, bufSize);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
        this.run = true;
        this.file = file;
        this.delayMillis = delayMillis;
        this.end = end;
        this.inbuf = new byte[bufSize];
        this.listener = listener;
        listener.init(this);
        this.reOpen = reOpen;
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
        Tailer tailer = new Tailer(file, listener, delayMillis, end, bufSize);
        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
        return tailer;
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
        Tailer tailer = new Tailer(file, listener, delayMillis, end, reOpen, bufSize);
        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
        return tailer;
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end) {
        return create(file, listener, delayMillis, end, (int) DEFAULT_BUFSIZE);
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
        return create(file, listener, delayMillis, end, reOpen, DEFAULT_BUFSIZE);
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis) {
        return create(file, listener, delayMillis, false);
    }

    public static Tailer create(File file, TailerListener listener) {
        return create(file, listener, 1000, false);
    }

    public File getFile() {
        return this.file;
    }

    public long getDelay() {
        return this.delayMillis;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r14 = this;
        r8 = 0;
        r2 = 0;
        r6 = 0;
        r9 = r8;
    L_0x0006:
        r11 = r14.run;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        if (r11 == 0) goto L_0x003f;
    L_0x000a:
        if (r9 != 0) goto L_0x003f;
    L_0x000c:
        r8 = new java.io.RandomAccessFile;	 Catch:{ FileNotFoundException -> 0x001e }
        r11 = r14.file;	 Catch:{ FileNotFoundException -> 0x001e }
        r12 = "r";
        r8.<init>(r11, r12);	 Catch:{ FileNotFoundException -> 0x001e }
    L_0x0015:
        if (r8 != 0) goto L_0x0029;
    L_0x0017:
        r12 = r14.delayMillis;	 Catch:{ InterruptedException -> 0x0026 }
        java.lang.Thread.sleep(r12);	 Catch:{ InterruptedException -> 0x0026 }
        r9 = r8;
        goto L_0x0006;
    L_0x001e:
        r0 = move-exception;
        r11 = r14.listener;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r11.fileNotFound();	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r8 = r9;
        goto L_0x0015;
    L_0x0026:
        r11 = move-exception;
        r9 = r8;
        goto L_0x0006;
    L_0x0029:
        r11 = r14.end;	 Catch:{ Exception -> 0x00c8 }
        if (r11 == 0) goto L_0x003c;
    L_0x002d:
        r11 = r14.file;	 Catch:{ Exception -> 0x00c8 }
        r6 = r11.length();	 Catch:{ Exception -> 0x00c8 }
    L_0x0033:
        r2 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x00c8 }
        r8.seek(r6);	 Catch:{ Exception -> 0x00c8 }
        r9 = r8;
        goto L_0x0006;
    L_0x003c:
        r6 = 0;
        goto L_0x0033;
    L_0x003f:
        r11 = r14.run;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        if (r11 == 0) goto L_0x00b0;
    L_0x0043:
        r11 = r14.file;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r1 = org.apache.commons.io.FileUtils.isFileNewer(r11, r2);	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r11 = r14.file;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r4 = r11.length();	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r11 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r11 >= 0) goto L_0x0072;
    L_0x0053:
        r11 = r14.listener;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r11.fileRotated();	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r10 = r9;
        r8 = new java.io.RandomAccessFile;	 Catch:{ FileNotFoundException -> 0x0069 }
        r11 = r14.file;	 Catch:{ FileNotFoundException -> 0x0069 }
        r12 = "r";
        r8.<init>(r11, r12);	 Catch:{ FileNotFoundException -> 0x0069 }
        r6 = 0;
        org.apache.commons.io.IOUtils.closeQuietly(r10);	 Catch:{ FileNotFoundException -> 0x00cc }
        r9 = r8;
        goto L_0x003f;
    L_0x0069:
        r0 = move-exception;
        r8 = r9;
    L_0x006b:
        r11 = r14.listener;	 Catch:{ Exception -> 0x00c8 }
        r11.fileNotFound();	 Catch:{ Exception -> 0x00c8 }
        r9 = r8;
        goto L_0x003f;
    L_0x0072:
        r11 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r11 <= 0) goto L_0x00a0;
    L_0x0076:
        r6 = r14.readLines(r9);	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r2 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
    L_0x007e:
        r11 = r14.reOpen;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        if (r11 == 0) goto L_0x0085;
    L_0x0082:
        org.apache.commons.io.IOUtils.closeQuietly(r9);	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
    L_0x0085:
        r12 = r14.delayMillis;	 Catch:{ InterruptedException -> 0x00ca }
        java.lang.Thread.sleep(r12);	 Catch:{ InterruptedException -> 0x00ca }
    L_0x008a:
        r11 = r14.run;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        if (r11 == 0) goto L_0x00ce;
    L_0x008e:
        r11 = r14.reOpen;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        if (r11 == 0) goto L_0x00ce;
    L_0x0092:
        r8 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r11 = r14.file;	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r12 = "r";
        r8.<init>(r11, r12);	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r8.seek(r6);	 Catch:{ Exception -> 0x00c8 }
    L_0x009e:
        r9 = r8;
        goto L_0x003f;
    L_0x00a0:
        if (r1 == 0) goto L_0x007e;
    L_0x00a2:
        r6 = 0;
        r9.seek(r6);	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r6 = r14.readLines(r9);	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        r2 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x00b5, all -> 0x00c0 }
        goto L_0x007e;
    L_0x00b0:
        org.apache.commons.io.IOUtils.closeQuietly(r9);
        r8 = r9;
    L_0x00b4:
        return;
    L_0x00b5:
        r0 = move-exception;
        r8 = r9;
    L_0x00b7:
        r11 = r14.listener;	 Catch:{ all -> 0x00c6 }
        r11.handle(r0);	 Catch:{ all -> 0x00c6 }
        org.apache.commons.io.IOUtils.closeQuietly(r8);
        goto L_0x00b4;
    L_0x00c0:
        r11 = move-exception;
        r8 = r9;
    L_0x00c2:
        org.apache.commons.io.IOUtils.closeQuietly(r8);
        throw r11;
    L_0x00c6:
        r11 = move-exception;
        goto L_0x00c2;
    L_0x00c8:
        r0 = move-exception;
        goto L_0x00b7;
    L_0x00ca:
        r11 = move-exception;
        goto L_0x008a;
    L_0x00cc:
        r0 = move-exception;
        goto L_0x006b;
    L_0x00ce:
        r8 = r9;
        goto L_0x009e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.input.Tailer.run():void");
    }

    public void stop() {
        this.run = false;
    }

    private long readLines(RandomAccessFile reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        long pos = reader.getFilePointer();
        long rePos = pos;
        boolean seenCR = false;
        while (this.run) {
            int num = reader.read(this.inbuf);
            if (num != -1) {
                for (int i = 0; i < num; i++) {
                    byte ch = this.inbuf[i];
                    switch (ch) {
                        case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                            seenCR = false;
                            this.listener.handle(sb.toString());
                            sb.setLength(0);
                            rePos = (((long) i) + pos) + 1;
                            break;
                        case Place.TYPE_BOWLING_ALLEY /*13*/:
                            if (seenCR) {
                                sb.append('\r');
                            }
                            seenCR = true;
                            break;
                        default:
                            if (seenCR) {
                                seenCR = false;
                                this.listener.handle(sb.toString());
                                sb.setLength(0);
                                rePos = (((long) i) + pos) + 1;
                            }
                            sb.append((char) ch);
                            break;
                    }
                }
                pos = reader.getFilePointer();
            } else {
                reader.seek(rePos);
                return rePos;
            }
        }
        reader.seek(rePos);
        return rePos;
    }
}
