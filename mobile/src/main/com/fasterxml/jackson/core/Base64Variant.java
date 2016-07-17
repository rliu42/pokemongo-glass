package com.fasterxml.jackson.core;

import android.support.v4.media.TransportMediator;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;

public final class Base64Variant implements Serializable {
    public static final int BASE64_VALUE_INVALID = -1;
    public static final int BASE64_VALUE_PADDING = -2;
    private static final int INT_SPACE = 32;
    static final char PADDING_CHAR_NONE = '\u0000';
    private static final long serialVersionUID = 1;
    private final transient int[] _asciiToBase64;
    private final transient byte[] _base64ToAsciiB;
    private final transient char[] _base64ToAsciiC;
    protected final transient int _maxLineLength;
    protected final String _name;
    protected final transient char _paddingChar;
    protected final transient boolean _usesPadding;

    public Base64Variant(String name, String base64Alphabet, boolean usesPadding, char paddingChar, int maxLineLength) {
        this._asciiToBase64 = new int[AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS];
        this._base64ToAsciiC = new char[64];
        this._base64ToAsciiB = new byte[64];
        this._name = name;
        this._usesPadding = usesPadding;
        this._paddingChar = paddingChar;
        this._maxLineLength = maxLineLength;
        int alphaLen = base64Alphabet.length();
        if (alphaLen != 64) {
            throw new IllegalArgumentException("Base64Alphabet length must be exactly 64 (was " + alphaLen + ")");
        }
        base64Alphabet.getChars(0, alphaLen, this._base64ToAsciiC, 0);
        Arrays.fill(this._asciiToBase64, BASE64_VALUE_INVALID);
        for (int i = 0; i < alphaLen; i++) {
            char alpha = this._base64ToAsciiC[i];
            this._base64ToAsciiB[i] = (byte) alpha;
            this._asciiToBase64[alpha] = i;
        }
        if (usesPadding) {
            this._asciiToBase64[paddingChar] = BASE64_VALUE_PADDING;
        }
    }

    public Base64Variant(Base64Variant base, String name, int maxLineLength) {
        this(base, name, base._usesPadding, base._paddingChar, maxLineLength);
    }

    public Base64Variant(Base64Variant base, String name, boolean usesPadding, char paddingChar, int maxLineLength) {
        this._asciiToBase64 = new int[AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS];
        this._base64ToAsciiC = new char[64];
        this._base64ToAsciiB = new byte[64];
        this._name = name;
        byte[] srcB = base._base64ToAsciiB;
        System.arraycopy(srcB, 0, this._base64ToAsciiB, 0, srcB.length);
        char[] srcC = base._base64ToAsciiC;
        System.arraycopy(srcC, 0, this._base64ToAsciiC, 0, srcC.length);
        int[] srcV = base._asciiToBase64;
        System.arraycopy(srcV, 0, this._asciiToBase64, 0, srcV.length);
        this._usesPadding = usesPadding;
        this._paddingChar = paddingChar;
        this._maxLineLength = maxLineLength;
    }

    protected Object readResolve() {
        return Base64Variants.valueOf(this._name);
    }

    public String getName() {
        return this._name;
    }

    public boolean usesPadding() {
        return this._usesPadding;
    }

    public boolean usesPaddingChar(char c) {
        return c == this._paddingChar;
    }

    public boolean usesPaddingChar(int ch) {
        return ch == this._paddingChar;
    }

    public char getPaddingChar() {
        return this._paddingChar;
    }

    public byte getPaddingByte() {
        return (byte) this._paddingChar;
    }

    public int getMaxLineLength() {
        return this._maxLineLength;
    }

    public int decodeBase64Char(char c) {
        char ch = c;
        return ch <= TransportMediator.KEYCODE_MEDIA_PAUSE ? this._asciiToBase64[ch] : BASE64_VALUE_INVALID;
    }

    public int decodeBase64Char(int ch) {
        return ch <= TransportMediator.KEYCODE_MEDIA_PAUSE ? this._asciiToBase64[ch] : BASE64_VALUE_INVALID;
    }

    public int decodeBase64Byte(byte b) {
        byte ch = b;
        return ch <= TransportMediator.KEYCODE_MEDIA_PAUSE ? this._asciiToBase64[ch] : BASE64_VALUE_INVALID;
    }

    public char encodeBase64BitsAsChar(int value) {
        return this._base64ToAsciiC[value];
    }

    public int encodeBase64Chunk(int b24, char[] buffer, int ptr) {
        int i = ptr + 1;
        buffer[ptr] = this._base64ToAsciiC[(b24 >> 18) & 63];
        ptr = i + 1;
        buffer[i] = this._base64ToAsciiC[(b24 >> 12) & 63];
        i = ptr + 1;
        buffer[ptr] = this._base64ToAsciiC[(b24 >> 6) & 63];
        ptr = i + 1;
        buffer[i] = this._base64ToAsciiC[b24 & 63];
        return ptr;
    }

    public void encodeBase64Chunk(StringBuilder sb, int b24) {
        sb.append(this._base64ToAsciiC[(b24 >> 18) & 63]);
        sb.append(this._base64ToAsciiC[(b24 >> 12) & 63]);
        sb.append(this._base64ToAsciiC[(b24 >> 6) & 63]);
        sb.append(this._base64ToAsciiC[b24 & 63]);
    }

    public int encodeBase64Partial(int bits, int outputBytes, char[] buffer, int outPtr) {
        int i = outPtr + 1;
        buffer[outPtr] = this._base64ToAsciiC[(bits >> 18) & 63];
        outPtr = i + 1;
        buffer[i] = this._base64ToAsciiC[(bits >> 12) & 63];
        if (this._usesPadding) {
            i = outPtr + 1;
            buffer[outPtr] = outputBytes == 2 ? this._base64ToAsciiC[(bits >> 6) & 63] : this._paddingChar;
            outPtr = i + 1;
            buffer[i] = this._paddingChar;
            return outPtr;
        } else if (outputBytes != 2) {
            return outPtr;
        } else {
            i = outPtr + 1;
            buffer[outPtr] = this._base64ToAsciiC[(bits >> 6) & 63];
            return i;
        }
    }

    public void encodeBase64Partial(StringBuilder sb, int bits, int outputBytes) {
        sb.append(this._base64ToAsciiC[(bits >> 18) & 63]);
        sb.append(this._base64ToAsciiC[(bits >> 12) & 63]);
        if (this._usesPadding) {
            sb.append(outputBytes == 2 ? this._base64ToAsciiC[(bits >> 6) & 63] : this._paddingChar);
            sb.append(this._paddingChar);
        } else if (outputBytes == 2) {
            sb.append(this._base64ToAsciiC[(bits >> 6) & 63]);
        }
    }

    public byte encodeBase64BitsAsByte(int value) {
        return this._base64ToAsciiB[value];
    }

    public int encodeBase64Chunk(int b24, byte[] buffer, int ptr) {
        int i = ptr + 1;
        buffer[ptr] = this._base64ToAsciiB[(b24 >> 18) & 63];
        ptr = i + 1;
        buffer[i] = this._base64ToAsciiB[(b24 >> 12) & 63];
        i = ptr + 1;
        buffer[ptr] = this._base64ToAsciiB[(b24 >> 6) & 63];
        ptr = i + 1;
        buffer[i] = this._base64ToAsciiB[b24 & 63];
        return ptr;
    }

    public int encodeBase64Partial(int bits, int outputBytes, byte[] buffer, int outPtr) {
        int i = outPtr + 1;
        buffer[outPtr] = this._base64ToAsciiB[(bits >> 18) & 63];
        outPtr = i + 1;
        buffer[i] = this._base64ToAsciiB[(bits >> 12) & 63];
        if (this._usesPadding) {
            byte b;
            byte pb = (byte) this._paddingChar;
            i = outPtr + 1;
            if (outputBytes == 2) {
                b = this._base64ToAsciiB[(bits >> 6) & 63];
            } else {
                b = pb;
            }
            buffer[outPtr] = b;
            outPtr = i + 1;
            buffer[i] = pb;
            return outPtr;
        } else if (outputBytes != 2) {
            return outPtr;
        } else {
            i = outPtr + 1;
            buffer[outPtr] = this._base64ToAsciiB[(bits >> 6) & 63];
            return i;
        }
    }

    public String encode(byte[] input) {
        return encode(input, false);
    }

    public String encode(byte[] input, boolean addQuotes) {
        int inputEnd = input.length;
        StringBuilder sb = new StringBuilder(((inputEnd >> 2) + inputEnd) + (inputEnd >> 3));
        if (addQuotes) {
            sb.append('\"');
        }
        int chunksBeforeLF = getMaxLineLength() >> 2;
        int safeInputEnd = inputEnd - 3;
        int inputPtr = 0;
        while (inputPtr <= safeInputEnd) {
            int inputPtr2 = inputPtr + 1;
            inputPtr = inputPtr2 + 1;
            inputPtr2 = inputPtr + 1;
            encodeBase64Chunk(sb, (((input[inputPtr] << 8) | (input[inputPtr2] & MotionEventCompat.ACTION_MASK)) << 8) | (input[inputPtr] & MotionEventCompat.ACTION_MASK));
            chunksBeforeLF += BASE64_VALUE_INVALID;
            if (chunksBeforeLF <= 0) {
                sb.append(IOUtils.DIR_SEPARATOR_WINDOWS);
                sb.append('n');
                chunksBeforeLF = getMaxLineLength() >> 2;
            }
            inputPtr = inputPtr2;
        }
        int inputLeft = inputEnd - inputPtr;
        if (inputLeft > 0) {
            inputPtr2 = inputPtr + 1;
            int b24 = input[inputPtr] << 16;
            if (inputLeft == 2) {
                b24 |= (input[inputPtr2] & MotionEventCompat.ACTION_MASK) << 8;
                inputPtr2++;
            }
            encodeBase64Partial(sb, b24, inputLeft);
        }
        if (addQuotes) {
            sb.append('\"');
        }
        return sb.toString();
    }

    public byte[] decode(String input) throws IllegalArgumentException {
        ByteArrayBuilder b = new ByteArrayBuilder();
        decode(input, b);
        return b.toByteArray();
    }

    public void decode(String str, ByteArrayBuilder builder) throws IllegalArgumentException {
        int ptr = 0;
        int len = str.length();
        while (ptr < len) {
            int ptr2;
            char ch;
            while (true) {
                ptr2 = ptr + 1;
                ch = str.charAt(ptr);
                if (ptr2 >= len) {
                    ptr = ptr2;
                    return;
                } else if (ch > ' ') {
                    break;
                } else {
                    ptr = ptr2;
                }
            }
            int bits = decodeBase64Char(ch);
            if (bits < 0) {
                _reportInvalidBase64(ch, 0, null);
            }
            int decodedData = bits;
            if (ptr2 >= len) {
                _reportBase64EOF();
            }
            ptr = ptr2 + 1;
            ch = str.charAt(ptr2);
            bits = decodeBase64Char(ch);
            if (bits < 0) {
                _reportInvalidBase64(ch, 1, null);
            }
            decodedData = (decodedData << 6) | bits;
            if (ptr >= len) {
                if (usesPadding()) {
                    _reportBase64EOF();
                } else {
                    builder.append(decodedData >> 4);
                    return;
                }
            }
            ptr2 = ptr + 1;
            ch = str.charAt(ptr);
            bits = decodeBase64Char(ch);
            if (bits < 0) {
                if (bits != BASE64_VALUE_PADDING) {
                    _reportInvalidBase64(ch, 2, null);
                }
                if (ptr2 >= len) {
                    _reportBase64EOF();
                }
                ptr = ptr2 + 1;
                ch = str.charAt(ptr2);
                if (!usesPaddingChar(ch)) {
                    _reportInvalidBase64(ch, 3, "expected padding character '" + getPaddingChar() + "'");
                }
                builder.append(decodedData >> 4);
            } else {
                decodedData = (decodedData << 6) | bits;
                if (ptr2 >= len) {
                    if (usesPadding()) {
                        _reportBase64EOF();
                    } else {
                        builder.appendTwoBytes(decodedData >> 2);
                        ptr = ptr2;
                        return;
                    }
                }
                ptr = ptr2 + 1;
                ch = str.charAt(ptr2);
                bits = decodeBase64Char(ch);
                if (bits < 0) {
                    if (bits != BASE64_VALUE_PADDING) {
                        _reportInvalidBase64(ch, 3, null);
                    }
                    builder.appendTwoBytes(decodedData >> 2);
                } else {
                    builder.appendThreeBytes((decodedData << 6) | bits);
                }
            }
        }
    }

    public String toString() {
        return this._name;
    }

    public boolean equals(Object o) {
        return o == this;
    }

    public int hashCode() {
        return this._name.hashCode();
    }

    protected void _reportInvalidBase64(char ch, int bindex, String msg) throws IllegalArgumentException {
        String base;
        if (ch <= ' ') {
            base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
        } else if (usesPaddingChar(ch)) {
            base = "Unexpected padding character ('" + getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        } else if (!Character.isDefined(ch) || Character.isISOControl(ch)) {
            base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        } else {
            base = "Illegal character '" + ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        }
        if (msg != null) {
            base = base + ": " + msg;
        }
        throw new IllegalArgumentException(base);
    }

    protected void _reportBase64EOF() throws IllegalArgumentException {
        throw new IllegalArgumentException("Unexpected end-of-String in base64 content");
    }
}
