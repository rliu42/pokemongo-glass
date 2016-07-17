package com.fasterxml.jackson.core.json;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.NumberOutput;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import spacemadness.com.lunarconsole.C1391R;

public class UTF8JsonGenerator extends JsonGeneratorImpl {
    private static final byte BYTE_0 = (byte) 48;
    private static final byte BYTE_BACKSLASH = (byte) 92;
    private static final byte BYTE_COLON = (byte) 58;
    private static final byte BYTE_COMMA = (byte) 44;
    private static final byte BYTE_LBRACKET = (byte) 91;
    private static final byte BYTE_LCURLY = (byte) 123;
    private static final byte BYTE_QUOTE = (byte) 34;
    private static final byte BYTE_RBRACKET = (byte) 93;
    private static final byte BYTE_RCURLY = (byte) 125;
    private static final byte BYTE_u = (byte) 117;
    private static final byte[] FALSE_BYTES;
    static final byte[] HEX_CHARS;
    private static final int MAX_BYTES_TO_BUFFER = 512;
    private static final byte[] NULL_BYTES;
    private static final byte[] TRUE_BYTES;
    protected boolean _bufferRecyclable;
    protected boolean _cfgUnqNames;
    protected char[] _charBuffer;
    protected final int _charBufferLength;
    protected byte[] _entityBuffer;
    protected byte[] _outputBuffer;
    protected final int _outputEnd;
    protected final int _outputMaxContiguous;
    protected final OutputStream _outputStream;
    protected int _outputTail;

    static {
        HEX_CHARS = CharTypes.copyHexBytes();
        NULL_BYTES = new byte[]{(byte) 110, BYTE_u, (byte) 108, (byte) 108};
        TRUE_BYTES = new byte[]{(byte) 116, (byte) 114, BYTE_u, (byte) 101};
        FALSE_BYTES = new byte[]{(byte) 102, (byte) 97, (byte) 108, (byte) 115, (byte) 101};
    }

    public UTF8JsonGenerator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out) {
        boolean z = true;
        super(ctxt, features, codec);
        this._outputTail = 0;
        this._outputStream = out;
        this._bufferRecyclable = true;
        this._outputBuffer = ctxt.allocWriteEncodingBuffer();
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (isEnabled(Feature.ESCAPE_NON_ASCII)) {
            setHighestNonEscapedChar(TransportMediator.KEYCODE_MEDIA_PAUSE);
        }
        if (Feature.QUOTE_FIELD_NAMES.enabledIn(features)) {
            z = false;
        }
        this._cfgUnqNames = z;
    }

    public UTF8JsonGenerator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out, byte[] outputBuffer, int outputOffset, boolean bufferRecyclable) {
        boolean z = false;
        super(ctxt, features, codec);
        this._outputTail = 0;
        this._outputStream = out;
        this._bufferRecyclable = bufferRecyclable;
        this._outputTail = outputOffset;
        this._outputBuffer = outputBuffer;
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (!Feature.QUOTE_FIELD_NAMES.enabledIn(features)) {
            z = true;
        }
        this._cfgUnqNames = z;
    }

    public Object getOutputTarget() {
        return this._outputStream;
    }

    public int getOutputBuffered() {
        return this._outputTail;
    }

    public void writeFieldName(String name) throws IOException {
        if (this._cfgPrettyPrinter != null) {
            _writePPFieldName(name);
            return;
        }
        int status = this._writeContext.writeFieldName(name);
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (status == 1) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_COMMA;
        }
        if (this._cfgUnqNames) {
            _writeStringSegments(name, false);
            return;
        }
        int len = name.length();
        if (len > this._charBufferLength) {
            _writeStringSegments(name, true);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        if (len <= this._outputMaxContiguous) {
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(name, 0, len);
        } else {
            _writeStringSegments(name, 0, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeFieldName(SerializableString name) throws IOException {
        if (this._cfgPrettyPrinter != null) {
            _writePPFieldName(name);
            return;
        }
        int status = this._writeContext.writeFieldName(name.getValue());
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (status == 1) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_COMMA;
        }
        if (this._cfgUnqNames) {
            _writeUnq(name);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        int len = name.appendQuotedUTF8(this._outputBuffer, this._outputTail);
        if (len < 0) {
            _writeBytes(name.asQuotedUTF8());
        } else {
            this._outputTail += len;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    private final void _writeUnq(SerializableString name) throws IOException {
        int len = name.appendQuotedUTF8(this._outputBuffer, this._outputTail);
        if (len < 0) {
            _writeBytes(name.asQuotedUTF8());
        } else {
            this._outputTail += len;
        }
    }

    public final void writeStartArray() throws IOException {
        _verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_LBRACKET;
    }

    public final void writeEndArray() throws IOException {
        if (!this._writeContext.inArray()) {
            _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_RBRACKET;
        }
        this._writeContext = this._writeContext.getParent();
    }

    public final void writeStartObject() throws IOException {
        _verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_LCURLY;
    }

    public final void writeEndObject() throws IOException {
        if (!this._writeContext.inObject()) {
            _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_RCURLY;
        }
        this._writeContext = this._writeContext.getParent();
    }

    protected final void _writePPFieldName(String name) throws IOException {
        int status = this._writeContext.writeFieldName(name);
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (status == 1) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        if (this._cfgUnqNames) {
            _writeStringSegments(name, false);
            return;
        }
        int len = name.length();
        if (len > this._charBufferLength) {
            _writeStringSegments(name, true);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        name.getChars(0, len, this._charBuffer, 0);
        if (len <= this._outputMaxContiguous) {
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(this._charBuffer, 0, len);
        } else {
            _writeStringSegments(this._charBuffer, 0, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    protected final void _writePPFieldName(SerializableString name) throws IOException {
        boolean addQuotes = true;
        int status = this._writeContext.writeFieldName(name.getValue());
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (status == 1) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        if (this._cfgUnqNames) {
            addQuotes = false;
        }
        if (addQuotes) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
        }
        _writeBytes(name.asQuotedUTF8());
        if (addQuotes) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            bArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
        }
    }

    public void writeString(String text) throws IOException {
        _verifyValueWrite("write a string");
        if (text == null) {
            _writeNull();
            return;
        }
        int len = text.length();
        if (len > this._outputMaxContiguous) {
            _writeStringSegments(text, true);
            return;
        }
        if (this._outputTail + len >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        _writeStringSegment(text, 0, len);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeString(char[] text, int offset, int len) throws IOException {
        _verifyValueWrite("write a string");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        if (len <= this._outputMaxContiguous) {
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(text, offset, len);
        } else {
            _writeStringSegments(text, offset, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public final void writeString(SerializableString text) throws IOException {
        _verifyValueWrite("write a string");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        int len = text.appendQuotedUTF8(this._outputBuffer, this._outputTail);
        if (len < 0) {
            _writeBytes(text.asQuotedUTF8());
        } else {
            this._outputTail += len;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
        _verifyValueWrite("write a string");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        _writeBytes(text, offset, length);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeUTF8String(byte[] text, int offset, int len) throws IOException {
        _verifyValueWrite("write a string");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        if (len <= this._outputMaxContiguous) {
            _writeUTF8Segment(text, offset, len);
        } else {
            _writeUTF8Segments(text, offset, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeRaw(String text) throws IOException, JsonGenerationException {
        int start = 0;
        int len = text.length();
        while (len > 0) {
            int len2;
            char[] buf = this._charBuffer;
            int blen = buf.length;
            if (len < blen) {
                len2 = len;
            } else {
                len2 = blen;
            }
            text.getChars(start, start + len2, buf, 0);
            writeRaw(buf, 0, len2);
            start += len2;
            len -= len2;
        }
    }

    public void writeRaw(String text, int offset, int len) throws IOException, JsonGenerationException {
        while (len > 0) {
            int len2;
            char[] buf = this._charBuffer;
            int blen = buf.length;
            if (len < blen) {
                len2 = len;
            } else {
                len2 = blen;
            }
            text.getChars(offset, offset + len2, buf, 0);
            writeRaw(buf, 0, len2);
            offset += len2;
            len -= len2;
        }
    }

    public void writeRaw(SerializableString text) throws IOException, JsonGenerationException {
        byte[] raw = text.asUnquotedUTF8();
        if (raw.length > 0) {
            _writeBytes(raw);
        }
    }

    public void writeRawValue(SerializableString text) throws IOException {
        _verifyValueWrite("write a raw (unencoded) value");
        byte[] raw = text.asUnquotedUTF8();
        if (raw.length > 0) {
            _writeBytes(raw);
        }
    }

    public final void writeRaw(char[] cbuf, int offset, int len) throws IOException, JsonGenerationException {
        int len3 = (len + len) + len;
        if (this._outputTail + len3 > this._outputEnd) {
            if (this._outputEnd < len3) {
                _writeSegmentedRaw(cbuf, offset, len);
                return;
            }
            _flushBuffer();
        }
        len += offset;
        while (offset < len) {
            char ch;
            while (true) {
                ch = cbuf[offset];
                if (ch > '\u007f') {
                    break;
                }
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ch;
                offset++;
                if (offset >= len) {
                    return;
                }
            }
            int offset2 = offset + 1;
            ch = cbuf[offset];
            if (ch < '\u0800') {
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((ch >> 6) | 192);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                offset = offset2;
            } else {
                offset = _outputRawMultiByteChar(ch, cbuf, offset2, len);
            }
        }
    }

    public void writeRaw(char ch) throws IOException, JsonGenerationException {
        if (this._outputTail + 3 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bbuf = this._outputBuffer;
        int i;
        if (ch <= '\u007f') {
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ch;
        } else if (ch < '\u0800') {
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch >> 6) | 192);
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        } else {
            _outputRawMultiByteChar(ch, null, 0, 0);
        }
    }

    private final void _writeSegmentedRaw(char[] cbuf, int offset, int len) throws IOException, JsonGenerationException {
        int end = this._outputEnd;
        byte[] bbuf = this._outputBuffer;
        while (offset < len) {
            char ch;
            while (true) {
                ch = cbuf[offset];
                if (ch >= '\u0080') {
                    break;
                }
                if (this._outputTail >= end) {
                    _flushBuffer();
                }
                int i = this._outputTail;
                this._outputTail = i + 1;
                bbuf[i] = (byte) ch;
                offset++;
                if (offset >= len) {
                    return;
                }
            }
            if (this._outputTail + 3 >= this._outputEnd) {
                _flushBuffer();
            }
            int offset2 = offset + 1;
            ch = cbuf[offset];
            if (ch < '\u0800') {
                i = this._outputTail;
                this._outputTail = i + 1;
                bbuf[i] = (byte) ((ch >> 6) | 192);
                i = this._outputTail;
                this._outputTail = i + 1;
                bbuf[i] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                offset = offset2;
            } else {
                offset = _outputRawMultiByteChar(ch, cbuf, offset2, len);
            }
        }
    }

    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write a binary value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        _writeBinary(b64variant, data, offset, offset + len);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) throws IOException, JsonGenerationException {
        int bytes;
        _verifyValueWrite("write a binary value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        byte[] encodingBuffer = this._ioContext.allocBase64Buffer();
        if (dataLength < 0) {
            try {
                bytes = _writeBinary(b64variant, data, encodingBuffer);
            } catch (Throwable th) {
                this._ioContext.releaseBase64Buffer(encodingBuffer);
            }
        } else {
            int missing = _writeBinary(b64variant, data, encodingBuffer, dataLength);
            if (missing > 0) {
                _reportError("Too few bytes available: missing " + missing + " bytes (out of " + dataLength + ")");
            }
            bytes = dataLength;
        }
        this._ioContext.releaseBase64Buffer(encodingBuffer);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        return bytes;
    }

    public void writeNumber(short s) throws IOException, JsonGenerationException {
        _verifyValueWrite("write a number");
        if (this._outputTail + 6 >= this._outputEnd) {
            _flushBuffer();
        }
        if (this._cfgNumbersAsStrings) {
            _writeQuotedShort(s);
        } else {
            this._outputTail = NumberOutput.outputInt((int) s, this._outputBuffer, this._outputTail);
        }
    }

    private final void _writeQuotedShort(short s) throws IOException {
        if (this._outputTail + 8 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        this._outputTail = NumberOutput.outputInt((int) s, this._outputBuffer, this._outputTail);
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeNumber(int i) throws IOException, JsonGenerationException {
        _verifyValueWrite("write a number");
        if (this._outputTail + 11 >= this._outputEnd) {
            _flushBuffer();
        }
        if (this._cfgNumbersAsStrings) {
            _writeQuotedInt(i);
        } else {
            this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
        }
    }

    private final void _writeQuotedInt(int i) throws IOException {
        if (this._outputTail + 13 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = BYTE_QUOTE;
        this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
        bArr = this._outputBuffer;
        i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = BYTE_QUOTE;
    }

    public void writeNumber(long l) throws IOException, JsonGenerationException {
        _verifyValueWrite("write a number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedLong(l);
            return;
        }
        if (this._outputTail + 21 >= this._outputEnd) {
            _flushBuffer();
        }
        this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
    }

    private final void _writeQuotedLong(long l) throws IOException {
        if (this._outputTail + 23 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeNumber(BigInteger value) throws IOException, JsonGenerationException {
        _verifyValueWrite("write a number");
        if (value == null) {
            _writeNull();
        } else if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(value.toString());
        } else {
            writeRaw(value.toString());
        }
    }

    public void writeNumber(double d) throws IOException, JsonGenerationException {
        if (this._cfgNumbersAsStrings || ((Double.isNaN(d) || Double.isInfinite(d)) && isEnabled(Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
            writeString(String.valueOf(d));
            return;
        }
        _verifyValueWrite("write a number");
        writeRaw(String.valueOf(d));
    }

    public void writeNumber(float f) throws IOException, JsonGenerationException {
        if (this._cfgNumbersAsStrings || ((Float.isNaN(f) || Float.isInfinite(f)) && isEnabled(Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
            writeString(String.valueOf(f));
            return;
        }
        _verifyValueWrite("write a number");
        writeRaw(String.valueOf(f));
    }

    public void writeNumber(BigDecimal value) throws IOException, JsonGenerationException {
        _verifyValueWrite("write a number");
        if (value == null) {
            _writeNull();
        } else if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(isEnabled(Feature.WRITE_BIGDECIMAL_AS_PLAIN) ? value.toPlainString() : value.toString());
        } else if (isEnabled(Feature.WRITE_BIGDECIMAL_AS_PLAIN)) {
            writeRaw(value.toPlainString());
        } else {
            writeRaw(value.toString());
        }
    }

    public void writeNumber(String encodedValue) throws IOException, JsonGenerationException {
        _verifyValueWrite("write a number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(encodedValue);
        } else {
            writeRaw(encodedValue);
        }
    }

    private final void _writeQuotedRaw(String value) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        writeRaw(value);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeBoolean(boolean state) throws IOException, JsonGenerationException {
        _verifyValueWrite("write a boolean value");
        if (this._outputTail + 5 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] keyword = state ? TRUE_BYTES : FALSE_BYTES;
        int len = keyword.length;
        System.arraycopy(keyword, 0, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    public void writeNull() throws IOException, JsonGenerationException {
        _verifyValueWrite("write a null");
        _writeNull();
    }

    protected final void _verifyValueWrite(String typeMsg) throws IOException {
        int status = this._writeContext.writeValue();
        if (status == 5) {
            _reportError("Can not " + typeMsg + ", expecting field name");
        }
        if (this._cfgPrettyPrinter == null) {
            byte b;
            switch (status) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    b = BYTE_COMMA;
                    break;
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    b = BYTE_COLON;
                    break;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    if (this._rootValueSeparator != null) {
                        byte[] raw = this._rootValueSeparator.asUnquotedUTF8();
                        if (raw.length > 0) {
                            _writeBytes(raw);
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            this._outputBuffer[this._outputTail] = b;
            this._outputTail++;
            return;
        }
        _verifyPrettyValueWrite(typeMsg, status);
    }

    protected final void _verifyPrettyValueWrite(String typeMsg, int status) throws IOException {
        switch (status) {
            case C1391R.styleable.AdsAttrs_adSize /*0*/:
                if (this._writeContext.inArray()) {
                    this._cfgPrettyPrinter.beforeArrayValues(this);
                } else if (this._writeContext.inObject()) {
                    this._cfgPrettyPrinter.beforeObjectEntries(this);
                }
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                this._cfgPrettyPrinter.writeArrayValueSeparator(this);
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                this._cfgPrettyPrinter.writeRootValueSeparator(this);
            default:
                _throwInternal();
        }
    }

    public void flush() throws IOException {
        _flushBuffer();
        if (this._outputStream != null && isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
            this._outputStream.flush();
        }
    }

    public void close() throws IOException {
        super.close();
        if (this._outputBuffer != null && isEnabled(Feature.AUTO_CLOSE_JSON_CONTENT)) {
            while (true) {
                JsonStreamContext ctxt = getOutputContext();
                if (!ctxt.inArray()) {
                    if (!ctxt.inObject()) {
                        break;
                    }
                    writeEndObject();
                } else {
                    writeEndArray();
                }
            }
        }
        _flushBuffer();
        this._outputTail = 0;
        if (this._outputStream != null) {
            if (this._ioContext.isResourceManaged() || isEnabled(Feature.AUTO_CLOSE_TARGET)) {
                this._outputStream.close();
            } else if (isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
                this._outputStream.flush();
            }
        }
        _releaseBuffers();
    }

    protected void _releaseBuffers() {
        byte[] buf = this._outputBuffer;
        if (buf != null && this._bufferRecyclable) {
            this._outputBuffer = null;
            this._ioContext.releaseWriteEncodingBuffer(buf);
        }
        char[] cbuf = this._charBuffer;
        if (cbuf != null) {
            this._charBuffer = null;
            this._ioContext.releaseConcatBuffer(cbuf);
        }
    }

    private final void _writeBytes(byte[] bytes) throws IOException {
        int len = bytes.length;
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
            if (len > MAX_BYTES_TO_BUFFER) {
                this._outputStream.write(bytes, 0, len);
                return;
            }
        }
        System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    private final void _writeBytes(byte[] bytes, int offset, int len) throws IOException {
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
            if (len > MAX_BYTES_TO_BUFFER) {
                this._outputStream.write(bytes, offset, len);
                return;
            }
        }
        System.arraycopy(bytes, offset, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    private final void _writeStringSegments(String text, boolean addQuotes) throws IOException {
        if (addQuotes) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
        }
        int left = text.length();
        int offset = 0;
        while (left > 0) {
            int len = Math.min(this._outputMaxContiguous, left);
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(text, offset, len);
            offset += len;
            left -= len;
        }
        if (addQuotes) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            bArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
        }
    }

    private final void _writeStringSegments(char[] cbuf, int offset, int totalLen) throws IOException {
        do {
            int len = Math.min(this._outputMaxContiguous, totalLen);
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(cbuf, offset, len);
            offset += len;
            totalLen -= len;
        } while (totalLen > 0);
    }

    private final void _writeStringSegments(String text, int offset, int totalLen) throws IOException {
        do {
            int len = Math.min(this._outputMaxContiguous, totalLen);
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(text, offset, len);
            offset += len;
            totalLen -= len;
        } while (totalLen > 0);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void _writeStringSegment(char[] r7, int r8, int r9) throws java.io.IOException {
        /*
        r6 = this;
        r9 = r9 + r8;
        r3 = r6._outputTail;
        r2 = r6._outputBuffer;
        r1 = r6._outputEscapes;
        r4 = r3;
    L_0x0008:
        if (r8 >= r9) goto L_0x0014;
    L_0x000a:
        r0 = r7[r8];
        r5 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        if (r0 > r5) goto L_0x0014;
    L_0x0010:
        r5 = r1[r0];
        if (r5 == 0) goto L_0x0020;
    L_0x0014:
        r6._outputTail = r4;
        if (r8 >= r9) goto L_0x001f;
    L_0x0018:
        r5 = r6._characterEscapes;
        if (r5 == 0) goto L_0x0029;
    L_0x001c:
        r6._writeCustomStringSegment2(r7, r8, r9);
    L_0x001f:
        return;
    L_0x0020:
        r3 = r4 + 1;
        r5 = (byte) r0;
        r2[r4] = r5;
        r8 = r8 + 1;
        r4 = r3;
        goto L_0x0008;
    L_0x0029:
        r5 = r6._maximumNonEscapedChar;
        if (r5 != 0) goto L_0x0031;
    L_0x002d:
        r6._writeStringSegment2(r7, r8, r9);
        goto L_0x001f;
    L_0x0031:
        r6._writeStringSegmentASCII2(r7, r8, r9);
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.UTF8JsonGenerator._writeStringSegment(char[], int, int):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void _writeStringSegment(java.lang.String r7, int r8, int r9) throws java.io.IOException {
        /*
        r6 = this;
        r9 = r9 + r8;
        r3 = r6._outputTail;
        r2 = r6._outputBuffer;
        r1 = r6._outputEscapes;
        r4 = r3;
    L_0x0008:
        if (r8 >= r9) goto L_0x0016;
    L_0x000a:
        r0 = r7.charAt(r8);
        r5 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        if (r0 > r5) goto L_0x0016;
    L_0x0012:
        r5 = r1[r0];
        if (r5 == 0) goto L_0x0022;
    L_0x0016:
        r6._outputTail = r4;
        if (r8 >= r9) goto L_0x0021;
    L_0x001a:
        r5 = r6._characterEscapes;
        if (r5 == 0) goto L_0x002b;
    L_0x001e:
        r6._writeCustomStringSegment2(r7, r8, r9);
    L_0x0021:
        return;
    L_0x0022:
        r3 = r4 + 1;
        r5 = (byte) r0;
        r2[r4] = r5;
        r8 = r8 + 1;
        r4 = r3;
        goto L_0x0008;
    L_0x002b:
        r5 = r6._maximumNonEscapedChar;
        if (r5 != 0) goto L_0x0033;
    L_0x002f:
        r6._writeStringSegment2(r7, r8, r9);
        goto L_0x0021;
    L_0x0033:
        r6._writeStringSegmentASCII2(r7, r8, r9);
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.UTF8JsonGenerator._writeStringSegment(java.lang.String, int, int):void");
    }

    private final void _writeStringSegment2(char[] cbuf, int offset, int end) throws IOException {
        if (this._outputTail + ((end - offset) * 6) > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < end) {
            offset = offset2 + 1;
            int ch = cbuf[offset2];
            if (ch > TransportMediator.KEYCODE_MEDIA_PAUSE) {
                if (ch <= 2047) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ((ch >> 6) | 192);
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                    outputPtr = outputPtr2;
                } else {
                    outputPtr = _outputMultiByteChar(ch, outputPtr2);
                }
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else if (escCodes[ch] == 0) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = (byte) ch;
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else {
                int escape = escCodes[ch];
                if (escape > 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = BYTE_BACKSLASH;
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) escape;
                    offset2 = offset;
                } else {
                    outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                    offset2 = offset;
                }
            }
        }
        this._outputTail = outputPtr2;
    }

    private final void _writeStringSegment2(String text, int offset, int end) throws IOException {
        if (this._outputTail + ((end - offset) * 6) > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < end) {
            offset = offset2 + 1;
            int ch = text.charAt(offset2);
            if (ch > TransportMediator.KEYCODE_MEDIA_PAUSE) {
                if (ch <= 2047) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ((ch >> 6) | 192);
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                    outputPtr = outputPtr2;
                } else {
                    outputPtr = _outputMultiByteChar(ch, outputPtr2);
                }
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else if (escCodes[ch] == 0) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = (byte) ch;
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else {
                int escape = escCodes[ch];
                if (escape > 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = BYTE_BACKSLASH;
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) escape;
                    offset2 = offset;
                } else {
                    outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                    offset2 = offset;
                }
            }
        }
        this._outputTail = outputPtr2;
    }

    private final void _writeStringSegmentASCII2(char[] cbuf, int offset, int end) throws IOException {
        if (this._outputTail + ((end - offset) * 6) > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int maxUnescaped = this._maximumNonEscapedChar;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < end) {
            offset = offset2 + 1;
            int ch = cbuf[offset2];
            if (ch <= TransportMediator.KEYCODE_MEDIA_PAUSE) {
                if (escCodes[ch] == 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ch;
                    outputPtr2 = outputPtr;
                    offset2 = offset;
                } else {
                    int escape = escCodes[ch];
                    if (escape > 0) {
                        outputPtr = outputPtr2 + 1;
                        outputBuffer[outputPtr2] = BYTE_BACKSLASH;
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) escape;
                        offset2 = offset;
                    } else {
                        outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                        offset2 = offset;
                    }
                }
            } else if (ch > maxUnescaped) {
                outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                offset2 = offset;
            } else {
                if (ch <= 2047) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ((ch >> 6) | 192);
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                    outputPtr = outputPtr2;
                } else {
                    outputPtr = _outputMultiByteChar(ch, outputPtr2);
                }
                outputPtr2 = outputPtr;
                offset2 = offset;
            }
        }
        this._outputTail = outputPtr2;
    }

    private final void _writeStringSegmentASCII2(String text, int offset, int end) throws IOException {
        if (this._outputTail + ((end - offset) * 6) > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int maxUnescaped = this._maximumNonEscapedChar;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < end) {
            offset = offset2 + 1;
            int ch = text.charAt(offset2);
            if (ch <= TransportMediator.KEYCODE_MEDIA_PAUSE) {
                if (escCodes[ch] == 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ch;
                    outputPtr2 = outputPtr;
                    offset2 = offset;
                } else {
                    int escape = escCodes[ch];
                    if (escape > 0) {
                        outputPtr = outputPtr2 + 1;
                        outputBuffer[outputPtr2] = BYTE_BACKSLASH;
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) escape;
                        offset2 = offset;
                    } else {
                        outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                        offset2 = offset;
                    }
                }
            } else if (ch > maxUnescaped) {
                outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                offset2 = offset;
            } else {
                if (ch <= 2047) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ((ch >> 6) | 192);
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                    outputPtr = outputPtr2;
                } else {
                    outputPtr = _outputMultiByteChar(ch, outputPtr2);
                }
                outputPtr2 = outputPtr;
                offset2 = offset;
            }
        }
        this._outputTail = outputPtr2;
    }

    private final void _writeCustomStringSegment2(char[] cbuf, int offset, int end) throws IOException {
        if (this._outputTail + ((end - offset) * 6) > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int maxUnescaped = this._maximumNonEscapedChar <= 0 ? SupportMenu.USER_MASK : this._maximumNonEscapedChar;
        CharacterEscapes customEscapes = this._characterEscapes;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < end) {
            offset = offset2 + 1;
            int ch = cbuf[offset2];
            SerializableString esc;
            if (ch <= TransportMediator.KEYCODE_MEDIA_PAUSE) {
                if (escCodes[ch] == 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ch;
                    outputPtr2 = outputPtr;
                    offset2 = offset;
                } else {
                    int escape = escCodes[ch];
                    if (escape > 0) {
                        outputPtr = outputPtr2 + 1;
                        outputBuffer[outputPtr2] = BYTE_BACKSLASH;
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) escape;
                        offset2 = offset;
                    } else if (escape == -2) {
                        esc = customEscapes.getEscapeSequence(ch);
                        if (esc == null) {
                            _reportError("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(ch) + ", although was supposed to have one");
                        }
                        outputPtr2 = _writeCustomEscape(outputBuffer, outputPtr2, esc, end - offset);
                        offset2 = offset;
                    } else {
                        outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                        offset2 = offset;
                    }
                }
            } else if (ch > maxUnescaped) {
                outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                offset2 = offset;
            } else {
                esc = customEscapes.getEscapeSequence(ch);
                if (esc != null) {
                    outputPtr2 = _writeCustomEscape(outputBuffer, outputPtr2, esc, end - offset);
                    offset2 = offset;
                } else {
                    if (ch <= 2047) {
                        outputPtr = outputPtr2 + 1;
                        outputBuffer[outputPtr2] = (byte) ((ch >> 6) | 192);
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                        outputPtr = outputPtr2;
                    } else {
                        outputPtr = _outputMultiByteChar(ch, outputPtr2);
                    }
                    outputPtr2 = outputPtr;
                    offset2 = offset;
                }
            }
        }
        this._outputTail = outputPtr2;
    }

    private final void _writeCustomStringSegment2(String text, int offset, int end) throws IOException {
        if (this._outputTail + ((end - offset) * 6) > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int maxUnescaped = this._maximumNonEscapedChar <= 0 ? SupportMenu.USER_MASK : this._maximumNonEscapedChar;
        CharacterEscapes customEscapes = this._characterEscapes;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < end) {
            offset = offset2 + 1;
            int ch = text.charAt(offset2);
            SerializableString esc;
            if (ch <= TransportMediator.KEYCODE_MEDIA_PAUSE) {
                if (escCodes[ch] == 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ch;
                    outputPtr2 = outputPtr;
                    offset2 = offset;
                } else {
                    int escape = escCodes[ch];
                    if (escape > 0) {
                        outputPtr = outputPtr2 + 1;
                        outputBuffer[outputPtr2] = BYTE_BACKSLASH;
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) escape;
                        offset2 = offset;
                    } else if (escape == -2) {
                        esc = customEscapes.getEscapeSequence(ch);
                        if (esc == null) {
                            _reportError("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(ch) + ", although was supposed to have one");
                        }
                        outputPtr2 = _writeCustomEscape(outputBuffer, outputPtr2, esc, end - offset);
                        offset2 = offset;
                    } else {
                        outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                        offset2 = offset;
                    }
                }
            } else if (ch > maxUnescaped) {
                outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                offset2 = offset;
            } else {
                esc = customEscapes.getEscapeSequence(ch);
                if (esc != null) {
                    outputPtr2 = _writeCustomEscape(outputBuffer, outputPtr2, esc, end - offset);
                    offset2 = offset;
                } else {
                    if (ch <= 2047) {
                        outputPtr = outputPtr2 + 1;
                        outputBuffer[outputPtr2] = (byte) ((ch >> 6) | 192);
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                        outputPtr = outputPtr2;
                    } else {
                        outputPtr = _outputMultiByteChar(ch, outputPtr2);
                    }
                    outputPtr2 = outputPtr;
                    offset2 = offset;
                }
            }
        }
        this._outputTail = outputPtr2;
    }

    private final int _writeCustomEscape(byte[] outputBuffer, int outputPtr, SerializableString esc, int remainingChars) throws IOException, JsonGenerationException {
        byte[] raw = esc.asUnquotedUTF8();
        int len = raw.length;
        if (len > 6) {
            return _handleLongCustomEscape(outputBuffer, outputPtr, this._outputEnd, raw, remainingChars);
        }
        System.arraycopy(raw, 0, outputBuffer, outputPtr, len);
        return outputPtr + len;
    }

    private final int _handleLongCustomEscape(byte[] outputBuffer, int outputPtr, int outputEnd, byte[] raw, int remainingChars) throws IOException, JsonGenerationException {
        int len = raw.length;
        if (outputPtr + len > outputEnd) {
            this._outputTail = outputPtr;
            _flushBuffer();
            outputPtr = this._outputTail;
            if (len > outputBuffer.length) {
                this._outputStream.write(raw, 0, len);
                return outputPtr;
            }
            System.arraycopy(raw, 0, outputBuffer, outputPtr, len);
            outputPtr += len;
        }
        if ((remainingChars * 6) + outputPtr <= outputEnd) {
            return outputPtr;
        }
        _flushBuffer();
        return this._outputTail;
    }

    private final void _writeUTF8Segments(byte[] utf8, int offset, int totalLen) throws IOException, JsonGenerationException {
        do {
            int len = Math.min(this._outputMaxContiguous, totalLen);
            _writeUTF8Segment(utf8, offset, len);
            offset += len;
            totalLen -= len;
        } while (totalLen > 0);
    }

    private final void _writeUTF8Segment(byte[] utf8, int offset, int len) throws IOException, JsonGenerationException {
        int ptr;
        int[] escCodes = this._outputEscapes;
        int end = offset + len;
        int ptr2 = offset;
        while (ptr2 < end) {
            ptr = ptr2 + 1;
            int ch = utf8[ptr2];
            if (ch < 0 || escCodes[ch] == 0) {
                ptr2 = ptr;
            } else {
                _writeUTF8Segment2(utf8, offset, len);
                return;
            }
        }
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
        }
        System.arraycopy(utf8, offset, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
        ptr = ptr2;
    }

    private final void _writeUTF8Segment2(byte[] utf8, int offset, int len) throws IOException, JsonGenerationException {
        int outputPtr = this._outputTail;
        if ((len * 6) + outputPtr > this._outputEnd) {
            _flushBuffer();
            outputPtr = this._outputTail;
        }
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        len += offset;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < len) {
            offset = offset2 + 1;
            byte b = utf8[offset2];
            byte ch = b;
            if (ch < null || escCodes[ch] == 0) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = b;
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else {
                int escape = escCodes[ch];
                if (escape > 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = BYTE_BACKSLASH;
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) escape;
                    outputPtr = outputPtr2;
                } else {
                    outputPtr = _writeGenericEscape(ch, outputPtr2);
                }
                outputPtr2 = outputPtr;
                offset2 = offset;
            }
        }
        this._outputTail = outputPtr2;
    }

    protected final void _writeBinary(Base64Variant b64variant, byte[] input, int inputPtr, int inputEnd) throws IOException, JsonGenerationException {
        int safeInputEnd = inputEnd - 3;
        int safeOutputEnd = this._outputEnd - 6;
        int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
        int inputPtr2 = inputPtr;
        while (inputPtr2 <= safeInputEnd) {
            if (this._outputTail > safeOutputEnd) {
                _flushBuffer();
            }
            inputPtr = inputPtr2 + 1;
            inputPtr2 = inputPtr + 1;
            inputPtr = inputPtr2 + 1;
            this._outputTail = b64variant.encodeBase64Chunk((((input[inputPtr2] << 8) | (input[inputPtr] & MotionEventCompat.ACTION_MASK)) << 8) | (input[inputPtr2] & MotionEventCompat.ACTION_MASK), this._outputBuffer, this._outputTail);
            chunksBeforeLF--;
            if (chunksBeforeLF <= 0) {
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = BYTE_BACKSLASH;
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 110;
                chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
            }
            inputPtr2 = inputPtr;
        }
        int inputLeft = inputEnd - inputPtr2;
        if (inputLeft > 0) {
            if (this._outputTail > safeOutputEnd) {
                _flushBuffer();
            }
            inputPtr = inputPtr2 + 1;
            int b24 = input[inputPtr2] << 16;
            if (inputLeft == 2) {
                b24 |= (input[inputPtr] & MotionEventCompat.ACTION_MASK) << 8;
                inputPtr++;
            }
            this._outputTail = b64variant.encodeBase64Partial(b24, inputLeft, this._outputBuffer, this._outputTail);
            return;
        }
    }

    protected final int _writeBinary(Base64Variant b64variant, InputStream data, byte[] readBuffer, int bytesLeft) throws IOException, JsonGenerationException {
        int inputPtr = 0;
        int inputEnd = 0;
        int lastFullOffset = -3;
        int safeOutputEnd = this._outputEnd - 6;
        int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
        while (bytesLeft > 2) {
            if (inputPtr > lastFullOffset) {
                inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, bytesLeft);
                inputPtr = 0;
                if (inputEnd < 3) {
                    break;
                }
                lastFullOffset = inputEnd - 3;
            }
            if (this._outputTail > safeOutputEnd) {
                _flushBuffer();
            }
            int inputPtr2 = inputPtr + 1;
            inputPtr = inputPtr2 + 1;
            inputPtr2 = inputPtr + 1;
            bytesLeft -= 3;
            this._outputTail = b64variant.encodeBase64Chunk((((readBuffer[inputPtr] << 8) | (readBuffer[inputPtr2] & MotionEventCompat.ACTION_MASK)) << 8) | (readBuffer[inputPtr] & MotionEventCompat.ACTION_MASK), this._outputBuffer, this._outputTail);
            chunksBeforeLF--;
            if (chunksBeforeLF <= 0) {
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = BYTE_BACKSLASH;
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 110;
                chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
            }
            inputPtr = inputPtr2;
        }
        if (bytesLeft <= 0) {
            return bytesLeft;
        }
        inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, bytesLeft);
        if (inputEnd <= 0) {
            return bytesLeft;
        }
        int amount;
        if (this._outputTail > safeOutputEnd) {
            _flushBuffer();
        }
        inputPtr2 = 0 + 1;
        int b24 = readBuffer[0] << 16;
        if (inputPtr2 < inputEnd) {
            b24 |= (readBuffer[inputPtr2] & MotionEventCompat.ACTION_MASK) << 8;
            amount = 2;
        } else {
            amount = 1;
        }
        this._outputTail = b64variant.encodeBase64Partial(b24, amount, this._outputBuffer, this._outputTail);
        inputPtr = inputPtr2;
        return bytesLeft - amount;
    }

    protected final int _writeBinary(Base64Variant b64variant, InputStream data, byte[] readBuffer) throws IOException, JsonGenerationException {
        int inputPtr = 0;
        int inputEnd = 0;
        int lastFullOffset = -3;
        int bytesDone = 0;
        int safeOutputEnd = this._outputEnd - 6;
        int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
        while (true) {
            if (inputPtr > lastFullOffset) {
                inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, readBuffer.length);
                inputPtr = 0;
                if (inputEnd < 3) {
                    break;
                }
                lastFullOffset = inputEnd - 3;
            }
            if (this._outputTail > safeOutputEnd) {
                _flushBuffer();
            }
            int inputPtr2 = inputPtr + 1;
            inputPtr = inputPtr2 + 1;
            inputPtr2 = inputPtr + 1;
            bytesDone += 3;
            this._outputTail = b64variant.encodeBase64Chunk((((readBuffer[inputPtr] << 8) | (readBuffer[inputPtr2] & MotionEventCompat.ACTION_MASK)) << 8) | (readBuffer[inputPtr] & MotionEventCompat.ACTION_MASK), this._outputBuffer, this._outputTail);
            chunksBeforeLF--;
            if (chunksBeforeLF <= 0) {
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = BYTE_BACKSLASH;
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 110;
                chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
            }
            inputPtr = inputPtr2;
        }
        if (0 >= inputEnd) {
            return bytesDone;
        }
        if (this._outputTail > safeOutputEnd) {
            _flushBuffer();
        }
        inputPtr2 = 0 + 1;
        int b24 = readBuffer[0] << 16;
        int amount = 1;
        if (inputPtr2 < inputEnd) {
            b24 |= (readBuffer[inputPtr2] & MotionEventCompat.ACTION_MASK) << 8;
            amount = 2;
        }
        bytesDone += amount;
        this._outputTail = b64variant.encodeBase64Partial(b24, amount, this._outputBuffer, this._outputTail);
        inputPtr = inputPtr2;
        return bytesDone;
    }

    private final int _readMore(InputStream in, byte[] readBuffer, int inputPtr, int inputEnd, int maxRead) throws IOException {
        int i = 0;
        int inputPtr2 = inputPtr;
        while (inputPtr2 < inputEnd) {
            int i2 = i + 1;
            inputPtr = inputPtr2 + 1;
            readBuffer[i] = readBuffer[inputPtr2];
            i = i2;
            inputPtr2 = inputPtr;
        }
        inputEnd = i;
        maxRead = Math.min(maxRead, readBuffer.length);
        do {
            int length = maxRead - inputEnd;
            if (length == 0) {
                break;
            }
            int count = in.read(readBuffer, inputEnd, length);
            if (count < 0) {
                return inputEnd;
            }
            inputEnd += count;
        } while (inputEnd < 3);
        return inputEnd;
    }

    private final int _outputRawMultiByteChar(int ch, char[] cbuf, int inputOffset, int inputLen) throws IOException {
        if (ch < GeneratorBase.SURR1_FIRST || ch > GeneratorBase.SURR2_LAST) {
            byte[] bbuf = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch >> 12) | 224);
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) (((ch >> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            return inputOffset;
        }
        if (inputOffset >= inputLen || cbuf == null) {
            _reportError("Split surrogate on writeRaw() input (last character)");
        }
        _outputSurrogates(ch, cbuf[inputOffset]);
        return inputOffset + 1;
    }

    protected final void _outputSurrogates(int surr1, int surr2) throws IOException {
        int c = _decodeSurrogate(surr1, surr2);
        if (this._outputTail + 4 > this._outputEnd) {
            _flushBuffer();
        }
        byte[] bbuf = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) ((c >> 18) | 240);
        i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) (((c >> 12) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) (((c >> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) ((c & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
    }

    private final int _outputMultiByteChar(int ch, int outputPtr) throws IOException {
        byte[] bbuf = this._outputBuffer;
        if (ch < GeneratorBase.SURR1_FIRST || ch > GeneratorBase.SURR2_LAST) {
            int i = outputPtr + 1;
            bbuf[outputPtr] = (byte) ((ch >> 12) | 224);
            outputPtr = i + 1;
            bbuf[i] = (byte) (((ch >> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            i = outputPtr + 1;
            bbuf[outputPtr] = (byte) ((ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            return i;
        }
        i = outputPtr + 1;
        bbuf[outputPtr] = BYTE_BACKSLASH;
        outputPtr = i + 1;
        bbuf[i] = BYTE_u;
        i = outputPtr + 1;
        bbuf[outputPtr] = HEX_CHARS[(ch >> 12) & 15];
        outputPtr = i + 1;
        bbuf[i] = HEX_CHARS[(ch >> 8) & 15];
        i = outputPtr + 1;
        bbuf[outputPtr] = HEX_CHARS[(ch >> 4) & 15];
        outputPtr = i + 1;
        bbuf[i] = HEX_CHARS[ch & 15];
        return outputPtr;
    }

    private final void _writeNull() throws IOException {
        if (this._outputTail + 4 >= this._outputEnd) {
            _flushBuffer();
        }
        System.arraycopy(NULL_BYTES, 0, this._outputBuffer, this._outputTail, 4);
        this._outputTail += 4;
    }

    private int _writeGenericEscape(int charToEscape, int outputPtr) throws IOException {
        byte[] bbuf = this._outputBuffer;
        int i = outputPtr + 1;
        bbuf[outputPtr] = BYTE_BACKSLASH;
        outputPtr = i + 1;
        bbuf[i] = BYTE_u;
        if (charToEscape > MotionEventCompat.ACTION_MASK) {
            int hi = (charToEscape >> 8) & MotionEventCompat.ACTION_MASK;
            i = outputPtr + 1;
            bbuf[outputPtr] = HEX_CHARS[hi >> 4];
            outputPtr = i + 1;
            bbuf[i] = HEX_CHARS[hi & 15];
            charToEscape &= MotionEventCompat.ACTION_MASK;
        } else {
            i = outputPtr + 1;
            bbuf[outputPtr] = BYTE_0;
            outputPtr = i + 1;
            bbuf[i] = BYTE_0;
        }
        i = outputPtr + 1;
        bbuf[outputPtr] = HEX_CHARS[charToEscape >> 4];
        outputPtr = i + 1;
        bbuf[i] = HEX_CHARS[charToEscape & 15];
        return outputPtr;
    }

    protected final void _flushBuffer() throws IOException {
        int len = this._outputTail;
        if (len > 0) {
            this._outputTail = 0;
            this._outputStream.write(this._outputBuffer, 0, len);
        }
    }
}
