package com.fasterxml.jackson.core.io;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.google.android.gms.location.places.Place;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class UTF32Reader extends Reader {
    protected static final int LAST_VALID_UNICODE_CHAR = 1114111;
    protected static final char NC = '\u0000';
    protected final boolean _bigEndian;
    protected byte[] _buffer;
    protected int _byteCount;
    protected int _charCount;
    protected final IOContext _context;
    protected InputStream _in;
    protected int _length;
    protected final boolean _managedBuffers;
    protected int _ptr;
    protected char _surrogate;
    protected char[] _tmpBuf;

    public UTF32Reader(IOContext ctxt, InputStream in, byte[] buf, int ptr, int len, boolean isBigEndian) {
        boolean z = false;
        this._surrogate = '\u0000';
        this._charCount = 0;
        this._byteCount = 0;
        this._tmpBuf = null;
        this._context = ctxt;
        this._in = in;
        this._buffer = buf;
        this._ptr = ptr;
        this._length = len;
        this._bigEndian = isBigEndian;
        if (in != null) {
            z = true;
        }
        this._managedBuffers = z;
    }

    public void close() throws IOException {
        InputStream in = this._in;
        if (in != null) {
            this._in = null;
            freeBuffers();
            in.close();
        }
    }

    public int read() throws IOException {
        if (this._tmpBuf == null) {
            this._tmpBuf = new char[1];
        }
        if (read(this._tmpBuf, 0, 1) < 1) {
            return -1;
        }
        return this._tmpBuf[0];
    }

    public int read(char[] cbuf, int start, int len) throws IOException {
        if (this._buffer == null) {
            return -1;
        }
        if (len < 1) {
            return len;
        }
        int outPtr;
        if (start < 0 || start + len > cbuf.length) {
            reportBounds(cbuf, start, len);
        }
        len += start;
        int outPtr2 = start;
        if (this._surrogate != '\u0000') {
            outPtr = outPtr2 + 1;
            cbuf[outPtr2] = this._surrogate;
            this._surrogate = '\u0000';
        } else {
            int left = this._length - this._ptr;
            if (left < 4 && !loadMore(left)) {
                return -1;
            }
            outPtr = outPtr2;
        }
        while (outPtr < len) {
            int ch;
            int ptr = this._ptr;
            if (this._bigEndian) {
                ch = (((this._buffer[ptr] << 24) | ((this._buffer[ptr + 1] & MotionEventCompat.ACTION_MASK) << 16)) | ((this._buffer[ptr + 2] & MotionEventCompat.ACTION_MASK) << 8)) | (this._buffer[ptr + 3] & MotionEventCompat.ACTION_MASK);
            } else {
                ch = (((this._buffer[ptr] & MotionEventCompat.ACTION_MASK) | ((this._buffer[ptr + 1] & MotionEventCompat.ACTION_MASK) << 8)) | ((this._buffer[ptr + 2] & MotionEventCompat.ACTION_MASK) << 16)) | (this._buffer[ptr + 3] << 24);
            }
            this._ptr += 4;
            if (ch > SupportMenu.USER_MASK) {
                if (ch > LAST_VALID_UNICODE_CHAR) {
                    reportInvalid(ch, outPtr - start, "(above " + Integer.toHexString(LAST_VALID_UNICODE_CHAR) + ") ");
                }
                ch -= AccessibilityNodeInfoCompat.ACTION_CUT;
                outPtr2 = outPtr + 1;
                cbuf[outPtr] = (char) (GeneratorBase.SURR1_FIRST + (ch >> 10));
                ch = GeneratorBase.SURR2_FIRST | (ch & Place.TYPE_SUBLOCALITY_LEVEL_1);
                if (outPtr2 >= len) {
                    this._surrogate = (char) ch;
                    break;
                }
            }
            outPtr2 = outPtr;
            outPtr = outPtr2 + 1;
            cbuf[outPtr2] = (char) ch;
            if (this._ptr >= this._length) {
                outPtr2 = outPtr;
                break;
            }
        }
        outPtr2 = outPtr;
        len = outPtr2 - start;
        this._charCount += len;
        return len;
    }

    private void reportUnexpectedEOF(int gotBytes, int needed) throws IOException {
        throw new CharConversionException("Unexpected EOF in the middle of a 4-byte UTF-32 char: got " + gotBytes + ", needed " + needed + ", at char #" + this._charCount + ", byte #" + (this._byteCount + gotBytes) + ")");
    }

    private void reportInvalid(int value, int offset, String msg) throws IOException {
        throw new CharConversionException("Invalid UTF-32 character 0x" + Integer.toHexString(value) + msg + " at char #" + (this._charCount + offset) + ", byte #" + ((this._byteCount + this._ptr) - 1) + ")");
    }

    private boolean loadMore(int available) throws IOException {
        int count;
        this._byteCount += this._length - available;
        if (available > 0) {
            if (this._ptr > 0) {
                System.arraycopy(this._buffer, this._ptr, this._buffer, 0, available);
                this._ptr = 0;
            }
            this._length = available;
        } else {
            this._ptr = 0;
            count = this._in == null ? -1 : this._in.read(this._buffer);
            if (count < 1) {
                this._length = 0;
                if (count >= 0) {
                    reportStrangeStream();
                } else if (!this._managedBuffers) {
                    return false;
                } else {
                    freeBuffers();
                    return false;
                }
            }
            this._length = count;
        }
        while (this._length < 4) {
            if (this._in == null) {
                count = -1;
            } else {
                count = this._in.read(this._buffer, this._length, this._buffer.length - this._length);
            }
            if (count < 1) {
                if (count < 0) {
                    if (this._managedBuffers) {
                        freeBuffers();
                    }
                    reportUnexpectedEOF(this._length, 4);
                }
                reportStrangeStream();
            }
            this._length += count;
        }
        return true;
    }

    private void freeBuffers() {
        byte[] buf = this._buffer;
        if (buf != null) {
            this._buffer = null;
            this._context.releaseReadIOBuffer(buf);
        }
    }

    private void reportBounds(char[] cbuf, int start, int len) throws IOException {
        throw new ArrayIndexOutOfBoundsException("read(buf," + start + "," + len + "), cbuf[" + cbuf.length + "]");
    }

    private void reportStrangeStream() throws IOException {
        throw new IOException("Strange I/O stream, returned 0 bytes on read");
    }
}
