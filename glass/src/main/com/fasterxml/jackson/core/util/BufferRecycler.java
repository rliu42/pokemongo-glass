package com.fasterxml.jackson.core.util;

import com.upsight.android.logger.UpsightLogger;

public class BufferRecycler {
    public static final int BYTE_BASE64_CODEC_BUFFER = 3;
    private static final int[] BYTE_BUFFER_LENGTHS;
    public static final int BYTE_READ_IO_BUFFER = 0;
    public static final int BYTE_WRITE_CONCAT_BUFFER = 2;
    public static final int BYTE_WRITE_ENCODING_BUFFER = 1;
    private static final int[] CHAR_BUFFER_LENGTHS;
    public static final int CHAR_CONCAT_BUFFER = 1;
    public static final int CHAR_NAME_COPY_BUFFER = 3;
    public static final int CHAR_TEXT_BUFFER = 2;
    public static final int CHAR_TOKEN_BUFFER = 0;
    protected final byte[][] _byteBuffers;
    protected final char[][] _charBuffers;

    static {
        BYTE_BUFFER_LENGTHS = new int[]{8000, 8000, 2000, 2000};
        CHAR_BUFFER_LENGTHS = new int[]{UpsightLogger.MAX_LENGTH, UpsightLogger.MAX_LENGTH, 200, 200};
    }

    public BufferRecycler() {
        this(4, 4);
    }

    protected BufferRecycler(int bbCount, int cbCount) {
        this._byteBuffers = new byte[bbCount][];
        this._charBuffers = new char[cbCount][];
    }

    public final byte[] allocByteBuffer(int ix) {
        return allocByteBuffer(ix, BYTE_READ_IO_BUFFER);
    }

    public byte[] allocByteBuffer(int ix, int minSize) {
        int DEF_SIZE = byteBufferLength(ix);
        if (minSize < DEF_SIZE) {
            minSize = DEF_SIZE;
        }
        byte[] buffer = this._byteBuffers[ix];
        if (buffer == null || buffer.length < minSize) {
            return balloc(minSize);
        }
        this._byteBuffers[ix] = null;
        return buffer;
    }

    public final void releaseByteBuffer(int ix, byte[] buffer) {
        this._byteBuffers[ix] = buffer;
    }

    public final char[] allocCharBuffer(int ix) {
        return allocCharBuffer(ix, BYTE_READ_IO_BUFFER);
    }

    public char[] allocCharBuffer(int ix, int minSize) {
        int DEF_SIZE = charBufferLength(ix);
        if (minSize < DEF_SIZE) {
            minSize = DEF_SIZE;
        }
        char[] buffer = this._charBuffers[ix];
        if (buffer == null || buffer.length < minSize) {
            return calloc(minSize);
        }
        this._charBuffers[ix] = null;
        return buffer;
    }

    public void releaseCharBuffer(int ix, char[] buffer) {
        this._charBuffers[ix] = buffer;
    }

    protected int byteBufferLength(int ix) {
        return BYTE_BUFFER_LENGTHS[ix];
    }

    protected int charBufferLength(int ix) {
        return CHAR_BUFFER_LENGTHS[ix];
    }

    protected byte[] balloc(int size) {
        return new byte[size];
    }

    protected char[] calloc(int size) {
        return new char[size];
    }
}
