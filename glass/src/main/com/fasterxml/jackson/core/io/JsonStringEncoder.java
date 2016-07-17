package com.fasterxml.jackson.core.io;

import android.support.v4.media.TransportMediator;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.util.TextBuffer;
import java.lang.ref.SoftReference;
import org.apache.commons.io.IOUtils;

public final class JsonStringEncoder {
    private static final byte[] HB;
    private static final char[] HC;
    private static final int SURR1_FIRST = 55296;
    private static final int SURR1_LAST = 56319;
    private static final int SURR2_FIRST = 56320;
    private static final int SURR2_LAST = 57343;
    protected static final ThreadLocal<SoftReference<JsonStringEncoder>> _threadEncoder;
    protected ByteArrayBuilder _bytes;
    protected final char[] _qbuf;
    protected TextBuffer _text;

    static {
        HC = CharTypes.copyHexChars();
        HB = CharTypes.copyHexBytes();
        _threadEncoder = new ThreadLocal();
    }

    public JsonStringEncoder() {
        this._qbuf = new char[6];
        this._qbuf[0] = IOUtils.DIR_SEPARATOR_WINDOWS;
        this._qbuf[2] = '0';
        this._qbuf[3] = '0';
    }

    public static JsonStringEncoder getInstance() {
        SoftReference<JsonStringEncoder> ref = (SoftReference) _threadEncoder.get();
        JsonStringEncoder enc = ref == null ? null : (JsonStringEncoder) ref.get();
        if (enc != null) {
            return enc;
        }
        enc = new JsonStringEncoder();
        _threadEncoder.set(new SoftReference(enc));
        return enc;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public char[] quoteAsString(java.lang.String r20) {
        /*
        r19 = this;
        r0 = r19;
        r0 = r0._text;
        r16 = r0;
        if (r16 != 0) goto L_0x0015;
    L_0x0008:
        r16 = new com.fasterxml.jackson.core.util.TextBuffer;
        r17 = 0;
        r16.<init>(r17);
        r0 = r16;
        r1 = r19;
        r1._text = r0;
    L_0x0015:
        r14 = r16.emptyAndGetCurrentSegment();
        r6 = com.fasterxml.jackson.core.io.CharTypes.get7BitOutputEscapes();
        r5 = r6.length;
        r8 = 0;
        r10 = r20.length();
        r12 = 0;
    L_0x0024:
        if (r8 >= r10) goto L_0x009a;
    L_0x0026:
        r0 = r20;
        r2 = r0.charAt(r8);
        if (r2 >= r5) goto L_0x0085;
    L_0x002e:
        r17 = r6[r2];
        if (r17 == 0) goto L_0x0085;
    L_0x0032:
        r9 = r8 + 1;
        r0 = r20;
        r3 = r0.charAt(r8);
        r4 = r6[r3];
        if (r4 >= 0) goto L_0x00a6;
    L_0x003e:
        r0 = r19;
        r0 = r0._qbuf;
        r17 = r0;
        r0 = r19;
        r1 = r17;
        r11 = r0._appendNumeric(r3, r1);
    L_0x004c:
        r17 = r12 + r11;
        r0 = r14.length;
        r18 = r0;
        r0 = r17;
        r1 = r18;
        if (r0 <= r1) goto L_0x00b5;
    L_0x0057:
        r0 = r14.length;
        r17 = r0;
        r7 = r17 - r12;
        if (r7 <= 0) goto L_0x006d;
    L_0x005e:
        r0 = r19;
        r0 = r0._qbuf;
        r17 = r0;
        r18 = 0;
        r0 = r17;
        r1 = r18;
        java.lang.System.arraycopy(r0, r1, r14, r12, r7);
    L_0x006d:
        r14 = r16.finishCurrentSegment();
        r15 = r11 - r7;
        r0 = r19;
        r0 = r0._qbuf;
        r17 = r0;
        r18 = 0;
        r0 = r17;
        r1 = r18;
        java.lang.System.arraycopy(r0, r7, r14, r1, r15);
        r12 = r15;
    L_0x0083:
        r8 = r9;
        goto L_0x0024;
    L_0x0085:
        r0 = r14.length;
        r17 = r0;
        r0 = r17;
        if (r12 < r0) goto L_0x0091;
    L_0x008c:
        r14 = r16.finishCurrentSegment();
        r12 = 0;
    L_0x0091:
        r13 = r12 + 1;
        r14[r12] = r2;
        r8 = r8 + 1;
        if (r8 < r10) goto L_0x00a4;
    L_0x0099:
        r12 = r13;
    L_0x009a:
        r0 = r16;
        r0.setCurrentLength(r12);
        r17 = r16.contentsAsArray();
        return r17;
    L_0x00a4:
        r12 = r13;
        goto L_0x0026;
    L_0x00a6:
        r0 = r19;
        r0 = r0._qbuf;
        r17 = r0;
        r0 = r19;
        r1 = r17;
        r11 = r0._appendNamed(r4, r1);
        goto L_0x004c;
    L_0x00b5:
        r0 = r19;
        r0 = r0._qbuf;
        r17 = r0;
        r18 = 0;
        r0 = r17;
        r1 = r18;
        java.lang.System.arraycopy(r0, r1, r14, r12, r11);
        r12 = r12 + r11;
        goto L_0x0083;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.io.JsonStringEncoder.quoteAsString(java.lang.String):char[]");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] quoteAsUTF8(java.lang.String r13) {
        /*
        r12 = this;
        r11 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r0 = r12._bytes;
        if (r0 != 0) goto L_0x000e;
    L_0x0006:
        r0 = new com.fasterxml.jackson.core.util.ByteArrayBuilder;
        r10 = 0;
        r0.<init>(r10);
        r12._bytes = r0;
    L_0x000e:
        r5 = 0;
        r4 = r13.length();
        r8 = 0;
        r7 = r0.resetAndGetFirstSegment();
    L_0x0018:
        if (r5 >= r4) goto L_0x0056;
    L_0x001a:
        r2 = com.fasterxml.jackson.core.io.CharTypes.get7BitOutputEscapes();
    L_0x001e:
        r1 = r13.charAt(r5);
        if (r1 > r11) goto L_0x0028;
    L_0x0024:
        r10 = r2[r1];
        if (r10 == 0) goto L_0x0044;
    L_0x0028:
        r10 = r7.length;
        if (r8 < r10) goto L_0x0030;
    L_0x002b:
        r7 = r0.finishCurrentSegment();
        r8 = 0;
    L_0x0030:
        r6 = r5 + 1;
        r1 = r13.charAt(r5);
        if (r1 > r11) goto L_0x005f;
    L_0x0038:
        r3 = r2[r1];
        r8 = r12._appendByte(r1, r3, r0, r8);
        r7 = r0.getCurrentSegment();
        r5 = r6;
        goto L_0x0018;
    L_0x0044:
        r10 = r7.length;
        if (r8 < r10) goto L_0x004c;
    L_0x0047:
        r7 = r0.finishCurrentSegment();
        r8 = 0;
    L_0x004c:
        r9 = r8 + 1;
        r10 = (byte) r1;
        r7[r8] = r10;
        r5 = r5 + 1;
        if (r5 < r4) goto L_0x005d;
    L_0x0055:
        r8 = r9;
    L_0x0056:
        r10 = r12._bytes;
        r10 = r10.completeAndCoalesce(r8);
        return r10;
    L_0x005d:
        r8 = r9;
        goto L_0x001e;
    L_0x005f:
        r10 = 2047; // 0x7ff float:2.868E-42 double:1.0114E-320;
        if (r1 > r10) goto L_0x0081;
    L_0x0063:
        r9 = r8 + 1;
        r10 = r1 >> 6;
        r10 = r10 | 192;
        r10 = (byte) r10;
        r7[r8] = r10;
        r10 = r1 & 63;
        r1 = r10 | 128;
        r8 = r9;
        r5 = r6;
    L_0x0072:
        r10 = r7.length;
        if (r8 < r10) goto L_0x007a;
    L_0x0075:
        r7 = r0.finishCurrentSegment();
        r8 = 0;
    L_0x007a:
        r9 = r8 + 1;
        r10 = (byte) r1;
        r7[r8] = r10;
        r8 = r9;
        goto L_0x0018;
    L_0x0081:
        r10 = 55296; // 0xd800 float:7.7486E-41 double:2.732E-319;
        if (r1 < r10) goto L_0x008b;
    L_0x0086:
        r10 = 57343; // 0xdfff float:8.0355E-41 double:2.8331E-319;
        if (r1 <= r10) goto L_0x00ae;
    L_0x008b:
        r9 = r8 + 1;
        r10 = r1 >> 12;
        r10 = r10 | 224;
        r10 = (byte) r10;
        r7[r8] = r10;
        r10 = r7.length;
        if (r9 < r10) goto L_0x0107;
    L_0x0097:
        r7 = r0.finishCurrentSegment();
        r8 = 0;
    L_0x009c:
        r9 = r8 + 1;
        r10 = r1 >> 6;
        r10 = r10 & 63;
        r10 = r10 | 128;
        r10 = (byte) r10;
        r7[r8] = r10;
        r10 = r1 & 63;
        r1 = r10 | 128;
        r8 = r9;
        r5 = r6;
        goto L_0x0072;
    L_0x00ae:
        r10 = 56319; // 0xdbff float:7.892E-41 double:2.78253E-319;
        if (r1 <= r10) goto L_0x00b6;
    L_0x00b3:
        _illegal(r1);
    L_0x00b6:
        if (r6 < r4) goto L_0x00bb;
    L_0x00b8:
        _illegal(r1);
    L_0x00bb:
        r5 = r6 + 1;
        r10 = r13.charAt(r6);
        r1 = _convert(r1, r10);
        r10 = 1114111; // 0x10ffff float:1.561202E-39 double:5.50444E-318;
        if (r1 <= r10) goto L_0x00cd;
    L_0x00ca:
        _illegal(r1);
    L_0x00cd:
        r9 = r8 + 1;
        r10 = r1 >> 18;
        r10 = r10 | 240;
        r10 = (byte) r10;
        r7[r8] = r10;
        r10 = r7.length;
        if (r9 < r10) goto L_0x0105;
    L_0x00d9:
        r7 = r0.finishCurrentSegment();
        r8 = 0;
    L_0x00de:
        r9 = r8 + 1;
        r10 = r1 >> 12;
        r10 = r10 & 63;
        r10 = r10 | 128;
        r10 = (byte) r10;
        r7[r8] = r10;
        r10 = r7.length;
        if (r9 < r10) goto L_0x0103;
    L_0x00ec:
        r7 = r0.finishCurrentSegment();
        r8 = 0;
    L_0x00f1:
        r9 = r8 + 1;
        r10 = r1 >> 6;
        r10 = r10 & 63;
        r10 = r10 | 128;
        r10 = (byte) r10;
        r7[r8] = r10;
        r10 = r1 & 63;
        r1 = r10 | 128;
        r8 = r9;
        goto L_0x0072;
    L_0x0103:
        r8 = r9;
        goto L_0x00f1;
    L_0x0105:
        r8 = r9;
        goto L_0x00de;
    L_0x0107:
        r8 = r9;
        goto L_0x009c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.io.JsonStringEncoder.quoteAsUTF8(java.lang.String):byte[]");
    }

    public byte[] encodeAsUTF8(String text) {
        int inputPtr;
        ByteArrayBuilder byteBuilder = this._bytes;
        if (byteBuilder == null) {
            byteBuilder = new ByteArrayBuilder(null);
            this._bytes = byteBuilder;
        }
        int inputEnd = text.length();
        int outputPtr = 0;
        byte[] outputBuffer = byteBuilder.resetAndGetFirstSegment();
        int outputEnd = outputBuffer.length;
        int inputPtr2 = 0;
        loop0:
        while (inputPtr2 < inputEnd) {
            int outputPtr2;
            inputPtr = inputPtr2 + 1;
            int c = text.charAt(inputPtr2);
            inputPtr2 = inputPtr;
            while (c <= TransportMediator.KEYCODE_MEDIA_PAUSE) {
                if (outputPtr >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr = 0;
                }
                outputPtr2 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) c;
                if (inputPtr2 >= inputEnd) {
                    outputPtr = outputPtr2;
                    inputPtr = inputPtr2;
                    break loop0;
                }
                inputPtr = inputPtr2 + 1;
                c = text.charAt(inputPtr2);
                outputPtr = outputPtr2;
                inputPtr2 = inputPtr;
            }
            if (outputPtr >= outputEnd) {
                outputBuffer = byteBuilder.finishCurrentSegment();
                outputEnd = outputBuffer.length;
                outputPtr2 = 0;
            } else {
                outputPtr2 = outputPtr;
            }
            if (c < AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = (byte) ((c >> 6) | 192);
                inputPtr = inputPtr2;
            } else if (c < SURR1_FIRST || c > SURR2_LAST) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = (byte) ((c >> 12) | 224);
                if (outputPtr >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr = 0;
                }
                outputPtr2 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) (((c >> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                outputPtr = outputPtr2;
                inputPtr = inputPtr2;
            } else {
                if (c > SURR1_LAST) {
                    _illegal(c);
                }
                if (inputPtr2 >= inputEnd) {
                    _illegal(c);
                }
                inputPtr = inputPtr2 + 1;
                c = _convert(c, text.charAt(inputPtr2));
                if (c > 1114111) {
                    _illegal(c);
                }
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = (byte) ((c >> 18) | 240);
                if (outputPtr >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr = 0;
                }
                outputPtr2 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) (((c >> 12) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                if (outputPtr2 >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr = 0;
                } else {
                    outputPtr = outputPtr2;
                }
                outputPtr2 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) (((c >> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                outputPtr = outputPtr2;
            }
            if (outputPtr >= outputEnd) {
                outputBuffer = byteBuilder.finishCurrentSegment();
                outputEnd = outputBuffer.length;
                outputPtr = 0;
            }
            outputPtr2 = outputPtr + 1;
            outputBuffer[outputPtr] = (byte) ((c & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            outputPtr = outputPtr2;
            inputPtr2 = inputPtr;
        }
        inputPtr = inputPtr2;
        return this._bytes.completeAndCoalesce(outputPtr);
    }

    private int _appendNumeric(int value, char[] qbuf) {
        qbuf[1] = 'u';
        qbuf[4] = HC[value >> 4];
        qbuf[5] = HC[value & 15];
        return 6;
    }

    private int _appendNamed(int esc, char[] qbuf) {
        qbuf[1] = (char) esc;
        return 2;
    }

    private int _appendByte(int ch, int esc, ByteArrayBuilder bb, int ptr) {
        bb.setCurrentSegmentLength(ptr);
        bb.append(92);
        if (esc < 0) {
            bb.append(117);
            if (ch > MotionEventCompat.ACTION_MASK) {
                int hi = ch >> 8;
                bb.append(HB[hi >> 4]);
                bb.append(HB[hi & 15]);
                ch &= MotionEventCompat.ACTION_MASK;
            } else {
                bb.append(48);
                bb.append(48);
            }
            bb.append(HB[ch >> 4]);
            bb.append(HB[ch & 15]);
        } else {
            bb.append((byte) esc);
        }
        return bb.getCurrentSegmentLength();
    }

    private static int _convert(int p1, int p2) {
        if (p2 >= SURR2_FIRST && p2 <= SURR2_LAST) {
            return (AccessibilityNodeInfoCompat.ACTION_CUT + ((p1 - SURR1_FIRST) << 10)) + (p2 - SURR2_FIRST);
        }
        throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(p1) + ", second 0x" + Integer.toHexString(p2) + "; illegal combination");
    }

    private static void _illegal(int c) {
        throw new IllegalArgumentException(UTF8Writer.illegalSurrogateDesc(c));
    }
}
