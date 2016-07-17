package com.fasterxml.jackson.core.json;

import android.support.v4.media.TransportMediator;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.nianticproject.holoholo.sfida.constants.BluetoothGattSupport;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import spacemadness.com.lunarconsole.BuildConfig;
import spacemadness.com.lunarconsole.C1391R;

public class UTF8StreamJsonParser extends ParserBase {
    static final byte BYTE_LF = (byte) 10;
    protected static final int[] _icLatin1;
    private static final int[] _icUTF8;
    protected boolean _bufferRecyclable;
    protected byte[] _inputBuffer;
    protected InputStream _inputStream;
    protected ObjectCodec _objectCodec;
    private int _quad1;
    protected int[] _quadBuffer;
    protected final ByteQuadsCanonicalizer _symbols;
    protected boolean _tokenIncomplete;

    static {
        _icUTF8 = CharTypes.getInputCodeUtf8();
        _icLatin1 = CharTypes.getInputCodeLatin1();
    }

    public UTF8StreamJsonParser(IOContext ctxt, int features, InputStream in, ObjectCodec codec, ByteQuadsCanonicalizer sym, byte[] inputBuffer, int start, int end, boolean bufferRecyclable) {
        super(ctxt, features);
        this._quadBuffer = new int[16];
        this._tokenIncomplete = false;
        this._inputStream = in;
        this._objectCodec = codec;
        this._symbols = sym;
        this._inputBuffer = inputBuffer;
        this._inputPtr = start;
        this._inputEnd = end;
        this._currInputRowStart = start;
        this._currInputProcessed = (long) (-start);
        this._bufferRecyclable = bufferRecyclable;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    public int releaseBuffered(OutputStream out) throws IOException {
        int count = this._inputEnd - this._inputPtr;
        if (count < 1) {
            return 0;
        }
        out.write(this._inputBuffer, this._inputPtr, count);
        return count;
    }

    public Object getInputSource() {
        return this._inputStream;
    }

    protected final boolean loadMore() throws IOException {
        this._currInputProcessed += (long) this._inputEnd;
        this._currInputRowStart -= this._inputEnd;
        if (this._inputStream == null) {
            return false;
        }
        int space = this._inputBuffer.length;
        if (space == 0) {
            return false;
        }
        int count = this._inputStream.read(this._inputBuffer, 0, space);
        if (count > 0) {
            this._inputPtr = 0;
            this._inputEnd = count;
            return true;
        }
        _closeInput();
        if (count != 0) {
            return false;
        }
        throw new IOException("InputStream.read() returned 0 characters when trying to read " + this._inputBuffer.length + " bytes");
    }

    protected final boolean _loadToHaveAtLeast(int minAvailable) throws IOException {
        if (this._inputStream == null) {
            return false;
        }
        int amount = this._inputEnd - this._inputPtr;
        if (amount <= 0 || this._inputPtr <= 0) {
            this._inputEnd = 0;
        } else {
            this._currInputProcessed += (long) this._inputPtr;
            this._currInputRowStart -= this._inputPtr;
            System.arraycopy(this._inputBuffer, this._inputPtr, this._inputBuffer, 0, amount);
            this._inputEnd = amount;
        }
        this._inputPtr = 0;
        while (this._inputEnd < minAvailable) {
            int count = this._inputStream.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
            if (count < 1) {
                _closeInput();
                if (count != 0) {
                    return false;
                }
                throw new IOException("InputStream.read() returned 0 characters when trying to read " + amount + " bytes");
            }
            this._inputEnd += count;
        }
        return true;
    }

    protected void _closeInput() throws IOException {
        if (this._inputStream != null) {
            if (this._ioContext.isResourceManaged() || isEnabled(Feature.AUTO_CLOSE_SOURCE)) {
                this._inputStream.close();
            }
            this._inputStream = null;
        }
    }

    protected void _releaseBuffers() throws IOException {
        super._releaseBuffers();
        this._symbols.release();
        if (this._bufferRecyclable) {
            byte[] buf = this._inputBuffer;
            if (buf != null) {
                this._inputBuffer = ByteArrayBuilder.NO_BYTES;
                this._ioContext.releaseReadIOBuffer(buf);
            }
        }
    }

    public String getText() throws IOException {
        if (this._currToken != JsonToken.VALUE_STRING) {
            return _getText2(this._currToken);
        }
        if (!this._tokenIncomplete) {
            return this._textBuffer.contentsAsString();
        }
        this._tokenIncomplete = false;
        return _finishAndReturnString();
    }

    public String getValueAsString() throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            if (!this._tokenIncomplete) {
                return this._textBuffer.contentsAsString();
            }
            this._tokenIncomplete = false;
            return _finishAndReturnString();
        } else if (this._currToken == JsonToken.FIELD_NAME) {
            return getCurrentName();
        } else {
            return super.getValueAsString(null);
        }
    }

    public String getValueAsString(String defValue) throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            if (!this._tokenIncomplete) {
                return this._textBuffer.contentsAsString();
            }
            this._tokenIncomplete = false;
            return _finishAndReturnString();
        } else if (this._currToken == JsonToken.FIELD_NAME) {
            return getCurrentName();
        } else {
            return super.getValueAsString(defValue);
        }
    }

    public int getValueAsInt() throws IOException {
        JsonToken t = this._currToken;
        if (t != JsonToken.VALUE_NUMBER_INT && t != JsonToken.VALUE_NUMBER_FLOAT) {
            return super.getValueAsInt(0);
        }
        if ((this._numTypesValid & 1) == 0) {
            if (this._numTypesValid == 0) {
                return _parseIntValue();
            }
            if ((this._numTypesValid & 1) == 0) {
                convertNumberToInt();
            }
        }
        return this._numberInt;
    }

    public int getValueAsInt(int defValue) throws IOException {
        JsonToken t = this._currToken;
        if (t != JsonToken.VALUE_NUMBER_INT && t != JsonToken.VALUE_NUMBER_FLOAT) {
            return super.getValueAsInt(defValue);
        }
        if ((this._numTypesValid & 1) == 0) {
            if (this._numTypesValid == 0) {
                return _parseIntValue();
            }
            if ((this._numTypesValid & 1) == 0) {
                convertNumberToInt();
            }
        }
        return this._numberInt;
    }

    protected final String _getText2(JsonToken t) {
        if (t == null) {
            return null;
        }
        switch (t.id()) {
            case Place.TYPE_ART_GALLERY /*5*/:
                return this._parsingContext.getCurrentName();
            case Place.TYPE_ATM /*6*/:
            case Place.TYPE_BAKERY /*7*/:
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                return this._textBuffer.contentsAsString();
            default:
                return t.asString();
        }
    }

    public char[] getTextCharacters() throws IOException {
        if (this._currToken == null) {
            return null;
        }
        switch (this._currToken.id()) {
            case Place.TYPE_ART_GALLERY /*5*/:
                if (!this._nameCopied) {
                    String name = this._parsingContext.getCurrentName();
                    int nameLen = name.length();
                    if (this._nameCopyBuffer == null) {
                        this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
                    } else if (this._nameCopyBuffer.length < nameLen) {
                        this._nameCopyBuffer = new char[nameLen];
                    }
                    name.getChars(0, nameLen, this._nameCopyBuffer, 0);
                    this._nameCopied = true;
                }
                return this._nameCopyBuffer;
            case Place.TYPE_ATM /*6*/:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                    break;
                }
                break;
            case Place.TYPE_BAKERY /*7*/:
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                break;
            default:
                return this._currToken.asCharArray();
        }
        return this._textBuffer.getTextBuffer();
    }

    public int getTextLength() throws IOException {
        if (this._currToken == null) {
            return 0;
        }
        switch (this._currToken.id()) {
            case Place.TYPE_ART_GALLERY /*5*/:
                return this._parsingContext.getCurrentName().length();
            case Place.TYPE_ATM /*6*/:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                    break;
                }
                break;
            case Place.TYPE_BAKERY /*7*/:
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                break;
            default:
                return this._currToken.asCharArray().length;
        }
        return this._textBuffer.size();
    }

    public int getTextOffset() throws IOException {
        if (this._currToken == null) {
            return 0;
        }
        switch (this._currToken.id()) {
            case Place.TYPE_ATM /*6*/:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                    break;
                }
                break;
            case Place.TYPE_BAKERY /*7*/:
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                break;
            default:
                return 0;
        }
        return this._textBuffer.getTextOffset();
    }

    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
        if (this._currToken != JsonToken.VALUE_STRING && (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT || this._binaryValue == null)) {
            _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        if (this._tokenIncomplete) {
            try {
                this._binaryValue = _decodeBase64(b64variant);
                this._tokenIncomplete = false;
            } catch (IllegalArgumentException iae) {
                throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + iae.getMessage());
            }
        } else if (this._binaryValue == null) {
            ByteArrayBuilder builder = _getByteArrayBuilder();
            _decodeBase64(getText(), builder, b64variant);
            this._binaryValue = builder.toByteArray();
        }
        return this._binaryValue;
    }

    public int readBinaryValue(Base64Variant b64variant, OutputStream out) throws IOException {
        if (this._tokenIncomplete && this._currToken == JsonToken.VALUE_STRING) {
            byte[] buf = this._ioContext.allocBase64Buffer();
            try {
                int _readBinary = _readBinary(b64variant, out, buf);
                return _readBinary;
            } finally {
                this._ioContext.releaseBase64Buffer(buf);
            }
        } else {
            byte[] b = getBinaryValue(b64variant);
            out.write(b);
            return b.length;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected int _readBinary(com.fasterxml.jackson.core.Base64Variant r11, java.io.OutputStream r12, byte[] r13) throws java.io.IOException {
        /*
        r10 = this;
        r5 = 0;
        r7 = r13.length;
        r4 = r7 + -3;
        r3 = 0;
    L_0x0005:
        r7 = r10._inputPtr;
        r8 = r10._inputEnd;
        if (r7 < r8) goto L_0x000e;
    L_0x000b:
        r10.loadMoreGuaranteed();
    L_0x000e:
        r7 = r10._inputBuffer;
        r8 = r10._inputPtr;
        r9 = r8 + 1;
        r10._inputPtr = r9;
        r7 = r7[r8];
        r1 = r7 & 255;
        r7 = 32;
        if (r1 <= r7) goto L_0x0005;
    L_0x001e:
        r0 = r11.decodeBase64Char(r1);
        if (r0 >= 0) goto L_0x003a;
    L_0x0024:
        r7 = 34;
        if (r1 != r7) goto L_0x0033;
    L_0x0028:
        r7 = 0;
        r10._tokenIncomplete = r7;
        if (r5 <= 0) goto L_0x0032;
    L_0x002d:
        r3 = r3 + r5;
        r7 = 0;
        r12.write(r13, r7, r5);
    L_0x0032:
        return r3;
    L_0x0033:
        r7 = 0;
        r0 = r10._decodeBase64Escape(r11, r1, r7);
        if (r0 < 0) goto L_0x0005;
    L_0x003a:
        if (r5 <= r4) goto L_0x0042;
    L_0x003c:
        r3 = r3 + r5;
        r7 = 0;
        r12.write(r13, r7, r5);
        r5 = 0;
    L_0x0042:
        r2 = r0;
        r7 = r10._inputPtr;
        r8 = r10._inputEnd;
        if (r7 < r8) goto L_0x004c;
    L_0x0049:
        r10.loadMoreGuaranteed();
    L_0x004c:
        r7 = r10._inputBuffer;
        r8 = r10._inputPtr;
        r9 = r8 + 1;
        r10._inputPtr = r9;
        r7 = r7[r8];
        r1 = r7 & 255;
        r0 = r11.decodeBase64Char(r1);
        if (r0 >= 0) goto L_0x0063;
    L_0x005e:
        r7 = 1;
        r0 = r10._decodeBase64Escape(r11, r1, r7);
    L_0x0063:
        r7 = r2 << 6;
        r2 = r7 | r0;
        r7 = r10._inputPtr;
        r8 = r10._inputEnd;
        if (r7 < r8) goto L_0x0070;
    L_0x006d:
        r10.loadMoreGuaranteed();
    L_0x0070:
        r7 = r10._inputBuffer;
        r8 = r10._inputPtr;
        r9 = r8 + 1;
        r10._inputPtr = r9;
        r7 = r7[r8];
        r1 = r7 & 255;
        r0 = r11.decodeBase64Char(r1);
        if (r0 >= 0) goto L_0x00e8;
    L_0x0082:
        r7 = -2;
        if (r0 == r7) goto L_0x009d;
    L_0x0085:
        r7 = 34;
        if (r1 != r7) goto L_0x0098;
    L_0x0089:
        r7 = r11.usesPadding();
        if (r7 != 0) goto L_0x0098;
    L_0x008f:
        r2 = r2 >> 4;
        r6 = r5 + 1;
        r7 = (byte) r2;
        r13[r5] = r7;
        r5 = r6;
        goto L_0x0028;
    L_0x0098:
        r7 = 2;
        r0 = r10._decodeBase64Escape(r11, r1, r7);
    L_0x009d:
        r7 = -2;
        if (r0 != r7) goto L_0x00e8;
    L_0x00a0:
        r7 = r10._inputPtr;
        r8 = r10._inputEnd;
        if (r7 < r8) goto L_0x00a9;
    L_0x00a6:
        r10.loadMoreGuaranteed();
    L_0x00a9:
        r7 = r10._inputBuffer;
        r8 = r10._inputPtr;
        r9 = r8 + 1;
        r10._inputPtr = r9;
        r7 = r7[r8];
        r1 = r7 & 255;
        r7 = r11.usesPaddingChar(r1);
        if (r7 != 0) goto L_0x00de;
    L_0x00bb:
        r7 = 3;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "expected padding character '";
        r8 = r8.append(r9);
        r9 = r11.getPaddingChar();
        r8 = r8.append(r9);
        r9 = "'";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r7 = r10.reportInvalidBase64Char(r11, r1, r7, r8);
        throw r7;
    L_0x00de:
        r2 = r2 >> 4;
        r6 = r5 + 1;
        r7 = (byte) r2;
        r13[r5] = r7;
        r5 = r6;
        goto L_0x0005;
    L_0x00e8:
        r7 = r2 << 6;
        r2 = r7 | r0;
        r7 = r10._inputPtr;
        r8 = r10._inputEnd;
        if (r7 < r8) goto L_0x00f5;
    L_0x00f2:
        r10.loadMoreGuaranteed();
    L_0x00f5:
        r7 = r10._inputBuffer;
        r8 = r10._inputPtr;
        r9 = r8 + 1;
        r10._inputPtr = r9;
        r7 = r7[r8];
        r1 = r7 & 255;
        r0 = r11.decodeBase64Char(r1);
        if (r0 >= 0) goto L_0x013c;
    L_0x0107:
        r7 = -2;
        if (r0 == r7) goto L_0x0129;
    L_0x010a:
        r7 = 34;
        if (r1 != r7) goto L_0x0124;
    L_0x010e:
        r7 = r11.usesPadding();
        if (r7 != 0) goto L_0x0124;
    L_0x0114:
        r2 = r2 >> 2;
        r6 = r5 + 1;
        r7 = r2 >> 8;
        r7 = (byte) r7;
        r13[r5] = r7;
        r5 = r6 + 1;
        r7 = (byte) r2;
        r13[r6] = r7;
        goto L_0x0028;
    L_0x0124:
        r7 = 3;
        r0 = r10._decodeBase64Escape(r11, r1, r7);
    L_0x0129:
        r7 = -2;
        if (r0 != r7) goto L_0x013c;
    L_0x012c:
        r2 = r2 >> 2;
        r6 = r5 + 1;
        r7 = r2 >> 8;
        r7 = (byte) r7;
        r13[r5] = r7;
        r5 = r6 + 1;
        r7 = (byte) r2;
        r13[r6] = r7;
        goto L_0x0005;
    L_0x013c:
        r7 = r2 << 6;
        r2 = r7 | r0;
        r6 = r5 + 1;
        r7 = r2 >> 16;
        r7 = (byte) r7;
        r13[r5] = r7;
        r5 = r6 + 1;
        r7 = r2 >> 8;
        r7 = (byte) r7;
        r13[r6] = r7;
        r6 = r5 + 1;
        r7 = (byte) r2;
        r13[r5] = r7;
        r5 = r6;
        goto L_0x0005;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.UTF8StreamJsonParser._readBinary(com.fasterxml.jackson.core.Base64Variant, java.io.OutputStream, byte[]):int");
    }

    public JsonLocation getTokenLocation() {
        return new JsonLocation(this._ioContext.getSourceReference(), getTokenCharacterOffset(), -1, getTokenLineNr(), getTokenColumnNr());
    }

    public JsonLocation getCurrentLocation() {
        return new JsonLocation(this._ioContext.getSourceReference(), this._currInputProcessed + ((long) this._inputPtr), -1, this._currInputRow, (this._inputPtr - this._currInputRowStart) + 1);
    }

    public JsonToken nextToken() throws IOException {
        this._numTypesValid = 0;
        if (this._currToken == JsonToken.FIELD_NAME) {
            return _nextAfterName();
        }
        if (this._tokenIncomplete) {
            _skipString();
        }
        int i = _skipWSOrEnd();
        if (i < 0) {
            close();
            this._currToken = null;
            return null;
        }
        this._tokenInputTotal = (this._currInputProcessed + ((long) this._inputPtr)) - 1;
        this._tokenInputRow = this._currInputRow;
        this._tokenInputCol = (this._inputPtr - this._currInputRowStart) - 1;
        this._binaryValue = null;
        JsonToken jsonToken;
        if (i == 93) {
            if (!this._parsingContext.inArray()) {
                _reportMismatchedEndMarker(i, '}');
            }
            this._parsingContext = this._parsingContext.getParent();
            jsonToken = JsonToken.END_ARRAY;
            this._currToken = jsonToken;
            return jsonToken;
        } else if (i == 125) {
            if (!this._parsingContext.inObject()) {
                _reportMismatchedEndMarker(i, ']');
            }
            this._parsingContext = this._parsingContext.getParent();
            jsonToken = JsonToken.END_OBJECT;
            this._currToken = jsonToken;
            return jsonToken;
        } else {
            if (this._parsingContext.expectComma()) {
                if (i != 44) {
                    _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
                }
                i = _skipWS();
            }
            if (!this._parsingContext.inObject()) {
                return _nextTokenNotInObject(i);
            }
            this._parsingContext.setCurrentName(_parseName(i));
            this._currToken = JsonToken.FIELD_NAME;
            i = _skipColon();
            if (i == 34) {
                this._tokenIncomplete = true;
                this._nextToken = JsonToken.VALUE_STRING;
                return this._currToken;
            }
            JsonToken t;
            switch (i) {
                case Place.TYPE_HAIR_CARE /*45*/:
                    t = _parseNegNumber();
                    break;
                case Place.TYPE_HINDU_TEMPLE /*48*/:
                case Place.TYPE_HOME_GOODS_STORE /*49*/:
                case Place.TYPE_HOSPITAL /*50*/:
                case Place.TYPE_INSURANCE_AGENCY /*51*/:
                case Place.TYPE_JEWELRY_STORE /*52*/:
                case Place.TYPE_LAUNDRY /*53*/:
                case Place.TYPE_LAWYER /*54*/:
                case Place.TYPE_LIBRARY /*55*/:
                case Place.TYPE_LIQUOR_STORE /*56*/:
                case Place.TYPE_LOCAL_GOVERNMENT_OFFICE /*57*/:
                    t = _parsePosNumber(i);
                    break;
                case Place.TYPE_TAXI_STAND /*91*/:
                    t = JsonToken.START_ARRAY;
                    break;
                case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                    _matchToken("false", 1);
                    t = JsonToken.VALUE_FALSE;
                    break;
                case 110:
                    _matchToken("null", 1);
                    t = JsonToken.VALUE_NULL;
                    break;
                case 116:
                    _matchToken("true", 1);
                    t = JsonToken.VALUE_TRUE;
                    break;
                case 123:
                    t = JsonToken.START_OBJECT;
                    break;
                default:
                    t = _handleUnexpectedValue(i);
                    break;
            }
            this._nextToken = t;
            return this._currToken;
        }
    }

    private final JsonToken _nextTokenNotInObject(int i) throws IOException {
        if (i == 34) {
            this._tokenIncomplete = true;
            JsonToken jsonToken = JsonToken.VALUE_STRING;
            this._currToken = jsonToken;
            return jsonToken;
        }
        switch (i) {
            case Place.TYPE_HAIR_CARE /*45*/:
                jsonToken = _parseNegNumber();
                this._currToken = jsonToken;
                return jsonToken;
            case Place.TYPE_HINDU_TEMPLE /*48*/:
            case Place.TYPE_HOME_GOODS_STORE /*49*/:
            case Place.TYPE_HOSPITAL /*50*/:
            case Place.TYPE_INSURANCE_AGENCY /*51*/:
            case Place.TYPE_JEWELRY_STORE /*52*/:
            case Place.TYPE_LAUNDRY /*53*/:
            case Place.TYPE_LAWYER /*54*/:
            case Place.TYPE_LIBRARY /*55*/:
            case Place.TYPE_LIQUOR_STORE /*56*/:
            case Place.TYPE_LOCAL_GOVERNMENT_OFFICE /*57*/:
                jsonToken = _parsePosNumber(i);
                this._currToken = jsonToken;
                return jsonToken;
            case Place.TYPE_TAXI_STAND /*91*/:
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                jsonToken = JsonToken.START_ARRAY;
                this._currToken = jsonToken;
                return jsonToken;
            case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                _matchToken("false", 1);
                jsonToken = JsonToken.VALUE_FALSE;
                this._currToken = jsonToken;
                return jsonToken;
            case 110:
                _matchToken("null", 1);
                jsonToken = JsonToken.VALUE_NULL;
                this._currToken = jsonToken;
                return jsonToken;
            case 116:
                _matchToken("true", 1);
                jsonToken = JsonToken.VALUE_TRUE;
                this._currToken = jsonToken;
                return jsonToken;
            case 123:
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                jsonToken = JsonToken.START_OBJECT;
                this._currToken = jsonToken;
                return jsonToken;
            default:
                jsonToken = _handleUnexpectedValue(i);
                this._currToken = jsonToken;
                return jsonToken;
        }
    }

    private final JsonToken _nextAfterName() {
        this._nameCopied = false;
        JsonToken t = this._nextToken;
        this._nextToken = null;
        if (t == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        } else if (t == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        this._currToken = t;
        return t;
    }

    public boolean nextFieldName(SerializableString str) throws IOException {
        this._numTypesValid = 0;
        if (this._currToken == JsonToken.FIELD_NAME) {
            _nextAfterName();
            return false;
        }
        if (this._tokenIncomplete) {
            _skipString();
        }
        int i = _skipWSOrEnd();
        if (i < 0) {
            close();
            this._currToken = null;
            return false;
        }
        this._tokenInputTotal = (this._currInputProcessed + ((long) this._inputPtr)) - 1;
        this._tokenInputRow = this._currInputRow;
        this._tokenInputCol = (this._inputPtr - this._currInputRowStart) - 1;
        this._binaryValue = null;
        if (i == 93) {
            if (!this._parsingContext.inArray()) {
                _reportMismatchedEndMarker(i, '}');
            }
            this._parsingContext = this._parsingContext.getParent();
            this._currToken = JsonToken.END_ARRAY;
            return false;
        } else if (i == 125) {
            if (!this._parsingContext.inObject()) {
                _reportMismatchedEndMarker(i, ']');
            }
            this._parsingContext = this._parsingContext.getParent();
            this._currToken = JsonToken.END_OBJECT;
            return false;
        } else {
            if (this._parsingContext.expectComma()) {
                if (i != 44) {
                    _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
                }
                i = _skipWS();
            }
            if (this._parsingContext.inObject()) {
                if (i == 34) {
                    byte[] nameBytes = str.asQuotedUTF8();
                    int len = nameBytes.length;
                    if ((this._inputPtr + len) + 4 < this._inputEnd) {
                        int end = this._inputPtr + len;
                        if (this._inputBuffer[end] == 34) {
                            int offset = 0;
                            int ptr = this._inputPtr;
                            while (ptr != end) {
                                if (nameBytes[offset] == this._inputBuffer[ptr]) {
                                    offset++;
                                    ptr++;
                                }
                            }
                            this._parsingContext.setCurrentName(str.getValue());
                            _isNextTokenNameYes(_skipColonFast(ptr + 1));
                            return true;
                        }
                    }
                }
                return _isNextTokenNameMaybe(i, str);
            }
            _nextTokenNotInObject(i);
            return false;
        }
    }

    public String nextFieldName() throws IOException {
        String str = null;
        this._numTypesValid = 0;
        if (this._currToken == JsonToken.FIELD_NAME) {
            _nextAfterName();
        } else {
            if (this._tokenIncomplete) {
                _skipString();
            }
            int i = _skipWSOrEnd();
            if (i < 0) {
                close();
                this._currToken = null;
            } else {
                this._tokenInputTotal = (this._currInputProcessed + ((long) this._inputPtr)) - 1;
                this._tokenInputRow = this._currInputRow;
                this._tokenInputCol = (this._inputPtr - this._currInputRowStart) - 1;
                this._binaryValue = null;
                if (i == 93) {
                    if (!this._parsingContext.inArray()) {
                        _reportMismatchedEndMarker(i, '}');
                    }
                    this._parsingContext = this._parsingContext.getParent();
                    this._currToken = JsonToken.END_ARRAY;
                } else if (i == 125) {
                    if (!this._parsingContext.inObject()) {
                        _reportMismatchedEndMarker(i, ']');
                    }
                    this._parsingContext = this._parsingContext.getParent();
                    this._currToken = JsonToken.END_OBJECT;
                } else {
                    if (this._parsingContext.expectComma()) {
                        if (i != 44) {
                            _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
                        }
                        i = _skipWS();
                    }
                    if (this._parsingContext.inObject()) {
                        str = _parseName(i);
                        this._parsingContext.setCurrentName(str);
                        this._currToken = JsonToken.FIELD_NAME;
                        i = _skipColon();
                        if (i == 34) {
                            this._tokenIncomplete = true;
                            this._nextToken = JsonToken.VALUE_STRING;
                        } else {
                            JsonToken t;
                            switch (i) {
                                case Place.TYPE_HAIR_CARE /*45*/:
                                    t = _parseNegNumber();
                                    break;
                                case Place.TYPE_HINDU_TEMPLE /*48*/:
                                case Place.TYPE_HOME_GOODS_STORE /*49*/:
                                case Place.TYPE_HOSPITAL /*50*/:
                                case Place.TYPE_INSURANCE_AGENCY /*51*/:
                                case Place.TYPE_JEWELRY_STORE /*52*/:
                                case Place.TYPE_LAUNDRY /*53*/:
                                case Place.TYPE_LAWYER /*54*/:
                                case Place.TYPE_LIBRARY /*55*/:
                                case Place.TYPE_LIQUOR_STORE /*56*/:
                                case Place.TYPE_LOCAL_GOVERNMENT_OFFICE /*57*/:
                                    t = _parsePosNumber(i);
                                    break;
                                case Place.TYPE_TAXI_STAND /*91*/:
                                    t = JsonToken.START_ARRAY;
                                    break;
                                case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                                    _matchToken("false", 1);
                                    t = JsonToken.VALUE_FALSE;
                                    break;
                                case 110:
                                    _matchToken("null", 1);
                                    t = JsonToken.VALUE_NULL;
                                    break;
                                case 116:
                                    _matchToken("true", 1);
                                    t = JsonToken.VALUE_TRUE;
                                    break;
                                case 123:
                                    t = JsonToken.START_OBJECT;
                                    break;
                                default:
                                    t = _handleUnexpectedValue(i);
                                    break;
                            }
                            this._nextToken = t;
                        }
                    } else {
                        _nextTokenNotInObject(i);
                    }
                }
            }
        }
        return str;
    }

    private final int _skipColonFast(int ptr) throws IOException {
        int ptr2 = ptr + 1;
        int i = this._inputBuffer[ptr];
        if (i == 58) {
            ptr = ptr2 + 1;
            i = this._inputBuffer[ptr2];
            if (i > 32) {
                if (!(i == 47 || i == 35)) {
                    this._inputPtr = ptr;
                    return i;
                }
            } else if (i == 32 || i == 9) {
                ptr2 = ptr + 1;
                i = this._inputBuffer[ptr];
                if (i <= 32 || i == 47 || i == 35) {
                    ptr = ptr2;
                } else {
                    this._inputPtr = ptr2;
                    ptr = ptr2;
                    return i;
                }
            }
            this._inputPtr = ptr - 1;
            return _skipColon2(true);
        }
        if (i == 32 || i == 9) {
            ptr = ptr2 + 1;
            i = this._inputBuffer[ptr2];
            ptr2 = ptr;
        }
        if (i == 58) {
            ptr = ptr2 + 1;
            i = this._inputBuffer[ptr2];
            if (i > 32) {
                if (!(i == 47 || i == 35)) {
                    this._inputPtr = ptr;
                    return i;
                }
            } else if (i == 32 || i == 9) {
                ptr2 = ptr + 1;
                i = this._inputBuffer[ptr];
                if (i <= 32 || i == 47 || i == 35) {
                    ptr = ptr2;
                } else {
                    this._inputPtr = ptr2;
                    ptr = ptr2;
                    return i;
                }
            }
            this._inputPtr = ptr - 1;
            return _skipColon2(true);
        }
        this._inputPtr = ptr2 - 1;
        ptr = ptr2;
        return _skipColon2(false);
    }

    private final void _isNextTokenNameYes(int i) throws IOException {
        this._currToken = JsonToken.FIELD_NAME;
        switch (i) {
            case Place.TYPE_ESTABLISHMENT /*34*/:
                this._tokenIncomplete = true;
                this._nextToken = JsonToken.VALUE_STRING;
            case Place.TYPE_HAIR_CARE /*45*/:
                this._nextToken = _parseNegNumber();
            case Place.TYPE_HINDU_TEMPLE /*48*/:
            case Place.TYPE_HOME_GOODS_STORE /*49*/:
            case Place.TYPE_HOSPITAL /*50*/:
            case Place.TYPE_INSURANCE_AGENCY /*51*/:
            case Place.TYPE_JEWELRY_STORE /*52*/:
            case Place.TYPE_LAUNDRY /*53*/:
            case Place.TYPE_LAWYER /*54*/:
            case Place.TYPE_LIBRARY /*55*/:
            case Place.TYPE_LIQUOR_STORE /*56*/:
            case Place.TYPE_LOCAL_GOVERNMENT_OFFICE /*57*/:
                this._nextToken = _parsePosNumber(i);
            case Place.TYPE_TAXI_STAND /*91*/:
                this._nextToken = JsonToken.START_ARRAY;
            case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                _matchToken("false", 1);
                this._nextToken = JsonToken.VALUE_FALSE;
            case 110:
                _matchToken("null", 1);
                this._nextToken = JsonToken.VALUE_NULL;
            case 116:
                _matchToken("true", 1);
                this._nextToken = JsonToken.VALUE_TRUE;
            case 123:
                this._nextToken = JsonToken.START_OBJECT;
            default:
                this._nextToken = _handleUnexpectedValue(i);
        }
    }

    private final boolean _isNextTokenNameMaybe(int i, SerializableString str) throws IOException {
        String n = _parseName(i);
        this._parsingContext.setCurrentName(n);
        boolean match = n.equals(str.getValue());
        this._currToken = JsonToken.FIELD_NAME;
        i = _skipColon();
        if (i == 34) {
            this._tokenIncomplete = true;
            this._nextToken = JsonToken.VALUE_STRING;
        } else {
            JsonToken t;
            switch (i) {
                case Place.TYPE_HAIR_CARE /*45*/:
                    t = _parseNegNumber();
                    break;
                case Place.TYPE_HINDU_TEMPLE /*48*/:
                case Place.TYPE_HOME_GOODS_STORE /*49*/:
                case Place.TYPE_HOSPITAL /*50*/:
                case Place.TYPE_INSURANCE_AGENCY /*51*/:
                case Place.TYPE_JEWELRY_STORE /*52*/:
                case Place.TYPE_LAUNDRY /*53*/:
                case Place.TYPE_LAWYER /*54*/:
                case Place.TYPE_LIBRARY /*55*/:
                case Place.TYPE_LIQUOR_STORE /*56*/:
                case Place.TYPE_LOCAL_GOVERNMENT_OFFICE /*57*/:
                    t = _parsePosNumber(i);
                    break;
                case Place.TYPE_TAXI_STAND /*91*/:
                    t = JsonToken.START_ARRAY;
                    break;
                case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                    _matchToken("false", 1);
                    t = JsonToken.VALUE_FALSE;
                    break;
                case 110:
                    _matchToken("null", 1);
                    t = JsonToken.VALUE_NULL;
                    break;
                case 116:
                    _matchToken("true", 1);
                    t = JsonToken.VALUE_TRUE;
                    break;
                case 123:
                    t = JsonToken.START_OBJECT;
                    break;
                default:
                    t = _handleUnexpectedValue(i);
                    break;
            }
            this._nextToken = t;
        }
        return match;
    }

    public String nextTextValue() throws IOException {
        if (this._currToken == JsonToken.FIELD_NAME) {
            this._nameCopied = false;
            JsonToken t = this._nextToken;
            this._nextToken = null;
            this._currToken = t;
            if (t == JsonToken.VALUE_STRING) {
                if (!this._tokenIncomplete) {
                    return this._textBuffer.contentsAsString();
                }
                this._tokenIncomplete = false;
                return _finishAndReturnString();
            } else if (t == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                return null;
            } else if (t != JsonToken.START_OBJECT) {
                return null;
            } else {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                return null;
            }
        } else if (nextToken() == JsonToken.VALUE_STRING) {
            return getText();
        } else {
            return null;
        }
    }

    public int nextIntValue(int defaultValue) throws IOException {
        if (this._currToken != JsonToken.FIELD_NAME) {
            return nextToken() == JsonToken.VALUE_NUMBER_INT ? getIntValue() : defaultValue;
        } else {
            this._nameCopied = false;
            JsonToken t = this._nextToken;
            this._nextToken = null;
            this._currToken = t;
            if (t == JsonToken.VALUE_NUMBER_INT) {
                return getIntValue();
            }
            if (t == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                return defaultValue;
            } else if (t != JsonToken.START_OBJECT) {
                return defaultValue;
            } else {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                return defaultValue;
            }
        }
    }

    public long nextLongValue(long defaultValue) throws IOException {
        if (this._currToken != JsonToken.FIELD_NAME) {
            return nextToken() == JsonToken.VALUE_NUMBER_INT ? getLongValue() : defaultValue;
        } else {
            this._nameCopied = false;
            JsonToken t = this._nextToken;
            this._nextToken = null;
            this._currToken = t;
            if (t == JsonToken.VALUE_NUMBER_INT) {
                return getLongValue();
            }
            if (t == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                return defaultValue;
            } else if (t != JsonToken.START_OBJECT) {
                return defaultValue;
            } else {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                return defaultValue;
            }
        }
    }

    public Boolean nextBooleanValue() throws IOException {
        JsonToken t;
        if (this._currToken == JsonToken.FIELD_NAME) {
            this._nameCopied = false;
            t = this._nextToken;
            this._nextToken = null;
            this._currToken = t;
            if (t == JsonToken.VALUE_TRUE) {
                return Boolean.TRUE;
            }
            if (t == JsonToken.VALUE_FALSE) {
                return Boolean.FALSE;
            }
            if (t == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                return null;
            } else if (t != JsonToken.START_OBJECT) {
                return null;
            } else {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                return null;
            }
        }
        t = nextToken();
        if (t == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        }
        if (t == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        }
        return null;
    }

    protected JsonToken _parsePosNumber(int c) throws IOException {
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        if (c == 48) {
            c = _verifyNoLeadingZeroes();
        }
        outBuf[0] = (char) c;
        int intLen = 1;
        int outPtr = 1;
        int end = (this._inputPtr + outBuf.length) - 1;
        if (end > this._inputEnd) {
            end = this._inputEnd;
        }
        while (this._inputPtr < end) {
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            c = bArr[i] & MotionEventCompat.ACTION_MASK;
            if (c >= 48 && c <= 57) {
                intLen++;
                int outPtr2 = outPtr + 1;
                outBuf[outPtr] = (char) c;
                outPtr = outPtr2;
            } else if (c == 46 || c == 101 || c == 69) {
                return _parseFloat(outBuf, outPtr, c, false, intLen);
            } else {
                this._inputPtr--;
                this._textBuffer.setCurrentLength(outPtr);
                if (this._parsingContext.inRoot()) {
                    _verifyRootSpace(c);
                }
                return resetInt(false, intLen);
            }
        }
        return _parseNumber2(outBuf, outPtr, false, intLen);
    }

    protected JsonToken _parseNegNumber() throws IOException {
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int i = 0 + 1;
        outBuf[0] = '-';
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        int c = bArr[i2] & MotionEventCompat.ACTION_MASK;
        if (c < 48 || c > 57) {
            int i3 = i;
            return _handleInvalidNumberStart(c, true);
        }
        if (c == 48) {
            c = _verifyNoLeadingZeroes();
        }
        i3 = i + 1;
        outBuf[i] = (char) c;
        int intLen = 1;
        int end = (this._inputPtr + outBuf.length) - 2;
        if (end > this._inputEnd) {
            end = this._inputEnd;
        }
        while (this._inputPtr < end) {
            bArr = this._inputBuffer;
            i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            c = bArr[i2] & MotionEventCompat.ACTION_MASK;
            if (c >= 48 && c <= 57) {
                intLen++;
                i = i3 + 1;
                outBuf[i3] = (char) c;
                i3 = i;
            } else if (c == 46 || c == 101 || c == 69) {
                return _parseFloat(outBuf, i3, c, true, intLen);
            } else {
                this._inputPtr--;
                this._textBuffer.setCurrentLength(i3);
                if (this._parsingContext.inRoot()) {
                    _verifyRootSpace(c);
                }
                return resetInt(true, intLen);
            }
        }
        return _parseNumber2(outBuf, i3, true, intLen);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.fasterxml.jackson.core.JsonToken _parseNumber2(char[] r8, int r9, boolean r10, int r11) throws java.io.IOException {
        /*
        r7 = this;
    L_0x0000:
        r0 = r7._inputPtr;
        r1 = r7._inputEnd;
        if (r0 < r1) goto L_0x0016;
    L_0x0006:
        r0 = r7.loadMore();
        if (r0 != 0) goto L_0x0016;
    L_0x000c:
        r0 = r7._textBuffer;
        r0.setCurrentLength(r9);
        r0 = r7.resetInt(r10, r11);
    L_0x0015:
        return r0;
    L_0x0016:
        r0 = r7._inputBuffer;
        r1 = r7._inputPtr;
        r2 = r1 + 1;
        r7._inputPtr = r2;
        r0 = r0[r1];
        r3 = r0 & 255;
        r0 = 57;
        if (r3 > r0) goto L_0x002a;
    L_0x0026:
        r0 = 48;
        if (r3 >= r0) goto L_0x0040;
    L_0x002a:
        r0 = 46;
        if (r3 == r0) goto L_0x0036;
    L_0x002e:
        r0 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r3 == r0) goto L_0x0036;
    L_0x0032:
        r0 = 69;
        if (r3 != r0) goto L_0x0053;
    L_0x0036:
        r0 = r7;
        r1 = r8;
        r2 = r9;
        r4 = r10;
        r5 = r11;
        r0 = r0._parseFloat(r1, r2, r3, r4, r5);
        goto L_0x0015;
    L_0x0040:
        r0 = r8.length;
        if (r9 < r0) goto L_0x004a;
    L_0x0043:
        r0 = r7._textBuffer;
        r8 = r0.finishCurrentSegment();
        r9 = 0;
    L_0x004a:
        r6 = r9 + 1;
        r0 = (char) r3;
        r8[r9] = r0;
        r11 = r11 + 1;
        r9 = r6;
        goto L_0x0000;
    L_0x0053:
        r0 = r7._inputPtr;
        r0 = r0 + -1;
        r7._inputPtr = r0;
        r0 = r7._textBuffer;
        r0.setCurrentLength(r9);
        r0 = r7._parsingContext;
        r0 = r0.inRoot();
        if (r0 == 0) goto L_0x0075;
    L_0x0066:
        r0 = r7._inputBuffer;
        r1 = r7._inputPtr;
        r2 = r1 + 1;
        r7._inputPtr = r2;
        r0 = r0[r1];
        r0 = r0 & 255;
        r7._verifyRootSpace(r0);
    L_0x0075:
        r0 = r7.resetInt(r10, r11);
        goto L_0x0015;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.UTF8StreamJsonParser._parseNumber2(char[], int, boolean, int):com.fasterxml.jackson.core.JsonToken");
    }

    private final int _verifyNoLeadingZeroes() throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            return 48;
        }
        int ch = this._inputBuffer[this._inputPtr] & MotionEventCompat.ACTION_MASK;
        if (ch < 48 || ch > 57) {
            return 48;
        }
        if (!isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
            reportInvalidNumber("Leading zeroes not allowed");
        }
        this._inputPtr++;
        if (ch != 48) {
            return ch;
        }
        do {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                return ch;
            }
            ch = this._inputBuffer[this._inputPtr] & MotionEventCompat.ACTION_MASK;
            if (ch < 48 || ch > 57) {
                return 48;
            }
            this._inputPtr++;
        } while (ch == 48);
        return ch;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.fasterxml.jackson.core.JsonToken _parseFloat(char[] r10, int r11, int r12, boolean r13, int r14) throws java.io.IOException {
        /*
        r9 = this;
        r8 = 57;
        r7 = 48;
        r2 = 0;
        r0 = 0;
        r4 = 46;
        if (r12 != r4) goto L_0x0024;
    L_0x000a:
        r3 = r11 + 1;
        r4 = (char) r12;
        r10[r11] = r4;
        r11 = r3;
    L_0x0010:
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x00c5;
    L_0x0016:
        r4 = r9.loadMore();
        if (r4 != 0) goto L_0x00c5;
    L_0x001c:
        r0 = 1;
    L_0x001d:
        if (r2 != 0) goto L_0x0024;
    L_0x001f:
        r4 = "Decimal point not followed by a digit";
        r9.reportUnexpectedNumberChar(r12, r4);
    L_0x0024:
        r1 = 0;
        r4 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r12 == r4) goto L_0x002d;
    L_0x0029:
        r4 = 69;
        if (r12 != r4) goto L_0x00a8;
    L_0x002d:
        r4 = r10.length;
        if (r11 < r4) goto L_0x0037;
    L_0x0030:
        r4 = r9._textBuffer;
        r10 = r4.finishCurrentSegment();
        r11 = 0;
    L_0x0037:
        r3 = r11 + 1;
        r4 = (char) r12;
        r10[r11] = r4;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x0045;
    L_0x0042:
        r9.loadMoreGuaranteed();
    L_0x0045:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r12 = r4 & 255;
        r4 = 45;
        if (r12 == r4) goto L_0x0059;
    L_0x0055:
        r4 = 43;
        if (r12 != r4) goto L_0x00fa;
    L_0x0059:
        r4 = r10.length;
        if (r3 < r4) goto L_0x00f7;
    L_0x005c:
        r4 = r9._textBuffer;
        r10 = r4.finishCurrentSegment();
        r11 = 0;
    L_0x0063:
        r3 = r11 + 1;
        r4 = (char) r12;
        r10[r11] = r4;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x0071;
    L_0x006e:
        r9.loadMoreGuaranteed();
    L_0x0071:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r12 = r4 & 255;
        r11 = r3;
    L_0x007e:
        if (r12 > r8) goto L_0x00a1;
    L_0x0080:
        if (r12 < r7) goto L_0x00a1;
    L_0x0082:
        r1 = r1 + 1;
        r4 = r10.length;
        if (r11 < r4) goto L_0x008e;
    L_0x0087:
        r4 = r9._textBuffer;
        r10 = r4.finishCurrentSegment();
        r11 = 0;
    L_0x008e:
        r3 = r11 + 1;
        r4 = (char) r12;
        r10[r11] = r4;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x00e9;
    L_0x0099:
        r4 = r9.loadMore();
        if (r4 != 0) goto L_0x00e9;
    L_0x009f:
        r0 = 1;
        r11 = r3;
    L_0x00a1:
        if (r1 != 0) goto L_0x00a8;
    L_0x00a3:
        r4 = "Exponent indicator not followed by a digit";
        r9.reportUnexpectedNumberChar(r12, r4);
    L_0x00a8:
        if (r0 != 0) goto L_0x00bb;
    L_0x00aa:
        r4 = r9._inputPtr;
        r4 = r4 + -1;
        r9._inputPtr = r4;
        r4 = r9._parsingContext;
        r4 = r4.inRoot();
        if (r4 == 0) goto L_0x00bb;
    L_0x00b8:
        r9._verifyRootSpace(r12);
    L_0x00bb:
        r4 = r9._textBuffer;
        r4.setCurrentLength(r11);
        r4 = r9.resetFloat(r13, r14, r2, r1);
        return r4;
    L_0x00c5:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r12 = r4 & 255;
        if (r12 < r7) goto L_0x001d;
    L_0x00d3:
        if (r12 > r8) goto L_0x001d;
    L_0x00d5:
        r2 = r2 + 1;
        r4 = r10.length;
        if (r11 < r4) goto L_0x00e1;
    L_0x00da:
        r4 = r9._textBuffer;
        r10 = r4.finishCurrentSegment();
        r11 = 0;
    L_0x00e1:
        r3 = r11 + 1;
        r4 = (char) r12;
        r10[r11] = r4;
        r11 = r3;
        goto L_0x0010;
    L_0x00e9:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r12 = r4 & 255;
        r11 = r3;
        goto L_0x007e;
    L_0x00f7:
        r11 = r3;
        goto L_0x0063;
    L_0x00fa:
        r11 = r3;
        goto L_0x007e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.UTF8StreamJsonParser._parseFloat(char[], int, int, boolean, int):com.fasterxml.jackson.core.JsonToken");
    }

    private final void _verifyRootSpace(int ch) throws IOException {
        this._inputPtr++;
        switch (ch) {
            case Place.TYPE_BAR /*9*/:
            case Place.TYPE_ELECTRONICS_STORE /*32*/:
            case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                this._currInputRow++;
                this._currInputRowStart = this._inputPtr;
            case Place.TYPE_BOWLING_ALLEY /*13*/:
                _skipCR();
            default:
                _reportMissingRootWS(ch);
        }
    }

    protected final String _parseName(int i) throws IOException {
        if (i != 34) {
            return _handleOddName(i);
        }
        if (this._inputPtr + 13 > this._inputEnd) {
            return slowParseName();
        }
        byte[] input = this._inputBuffer;
        int[] codes = _icLatin1;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        int q = input[i2] & MotionEventCompat.ACTION_MASK;
        if (codes[q] == 0) {
            i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            i = input[i2] & MotionEventCompat.ACTION_MASK;
            if (codes[i] == 0) {
                q = (q << 8) | i;
                i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                i = input[i2] & MotionEventCompat.ACTION_MASK;
                if (codes[i] == 0) {
                    q = (q << 8) | i;
                    i2 = this._inputPtr;
                    this._inputPtr = i2 + 1;
                    i = input[i2] & MotionEventCompat.ACTION_MASK;
                    if (codes[i] == 0) {
                        q = (q << 8) | i;
                        i2 = this._inputPtr;
                        this._inputPtr = i2 + 1;
                        i = input[i2] & MotionEventCompat.ACTION_MASK;
                        if (codes[i] == 0) {
                            this._quad1 = q;
                            return parseMediumName(i);
                        } else if (i == 34) {
                            return findName(q, 4);
                        } else {
                            return parseName(q, i, 4);
                        }
                    } else if (i == 34) {
                        return findName(q, 3);
                    } else {
                        return parseName(q, i, 3);
                    }
                } else if (i == 34) {
                    return findName(q, 2);
                } else {
                    return parseName(q, i, 2);
                }
            } else if (i == 34) {
                return findName(q, 1);
            } else {
                return parseName(q, i, 1);
            }
        } else if (q == 34) {
            return BuildConfig.FLAVOR;
        } else {
            return parseName(0, q, 0);
        }
    }

    protected final String parseMediumName(int q2) throws IOException {
        byte[] input = this._inputBuffer;
        int[] codes = _icLatin1;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = input[i] & MotionEventCompat.ACTION_MASK;
        if (codes[i2] == 0) {
            q2 = (q2 << 8) | i2;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            i2 = input[i] & MotionEventCompat.ACTION_MASK;
            if (codes[i2] == 0) {
                q2 = (q2 << 8) | i2;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = input[i] & MotionEventCompat.ACTION_MASK;
                if (codes[i2] == 0) {
                    q2 = (q2 << 8) | i2;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    i2 = input[i] & MotionEventCompat.ACTION_MASK;
                    if (codes[i2] == 0) {
                        return parseMediumName2(i2, q2);
                    }
                    if (i2 == 34) {
                        return findName(this._quad1, q2, 4);
                    }
                    return parseName(this._quad1, q2, i2, 4);
                } else if (i2 == 34) {
                    return findName(this._quad1, q2, 3);
                } else {
                    return parseName(this._quad1, q2, i2, 3);
                }
            } else if (i2 == 34) {
                return findName(this._quad1, q2, 2);
            } else {
                return parseName(this._quad1, q2, i2, 2);
            }
        } else if (i2 == 34) {
            return findName(this._quad1, q2, 1);
        } else {
            return parseName(this._quad1, q2, i2, 1);
        }
    }

    protected final String parseMediumName2(int q3, int q2) throws IOException {
        byte[] input = this._inputBuffer;
        int[] codes = _icLatin1;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = input[i] & MotionEventCompat.ACTION_MASK;
        if (codes[i2] == 0) {
            q3 = (q3 << 8) | i2;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            i2 = input[i] & MotionEventCompat.ACTION_MASK;
            if (codes[i2] == 0) {
                q3 = (q3 << 8) | i2;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = input[i] & MotionEventCompat.ACTION_MASK;
                if (codes[i2] == 0) {
                    q3 = (q3 << 8) | i2;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    i2 = input[i] & MotionEventCompat.ACTION_MASK;
                    if (codes[i2] == 0) {
                        return parseLongName(i2, q2, q3);
                    }
                    if (i2 == 34) {
                        return findName(this._quad1, q2, q3, 4);
                    }
                    return parseName(this._quad1, q2, q3, i2, 4);
                } else if (i2 == 34) {
                    return findName(this._quad1, q2, q3, 3);
                } else {
                    return parseName(this._quad1, q2, q3, i2, 3);
                }
            } else if (i2 == 34) {
                return findName(this._quad1, q2, q3, 2);
            } else {
                return parseName(this._quad1, q2, q3, i2, 2);
            }
        } else if (i2 == 34) {
            return findName(this._quad1, q2, q3, 1);
        } else {
            return parseName(this._quad1, q2, q3, i2, 1);
        }
    }

    protected final String parseLongName(int q, int q2, int q3) throws IOException {
        this._quadBuffer[0] = this._quad1;
        this._quadBuffer[1] = q2;
        this._quadBuffer[2] = q3;
        byte[] input = this._inputBuffer;
        int[] codes = _icLatin1;
        int qlen = 3;
        while (this._inputPtr + 4 <= this._inputEnd) {
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = input[i] & MotionEventCompat.ACTION_MASK;
            if (codes[i2] == 0) {
                q = (q << 8) | i2;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = input[i] & MotionEventCompat.ACTION_MASK;
                if (codes[i2] == 0) {
                    q = (q << 8) | i2;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    i2 = input[i] & MotionEventCompat.ACTION_MASK;
                    if (codes[i2] == 0) {
                        q = (q << 8) | i2;
                        i = this._inputPtr;
                        this._inputPtr = i + 1;
                        i2 = input[i] & MotionEventCompat.ACTION_MASK;
                        if (codes[i2] == 0) {
                            if (qlen >= this._quadBuffer.length) {
                                this._quadBuffer = growArrayBy(this._quadBuffer, qlen);
                            }
                            int qlen2 = qlen + 1;
                            this._quadBuffer[qlen] = q;
                            q = i2;
                            qlen = qlen2;
                        } else if (i2 == 34) {
                            return findName(this._quadBuffer, qlen, q, 4);
                        } else {
                            return parseEscapedName(this._quadBuffer, qlen, q, i2, 4);
                        }
                    } else if (i2 == 34) {
                        return findName(this._quadBuffer, qlen, q, 3);
                    } else {
                        return parseEscapedName(this._quadBuffer, qlen, q, i2, 3);
                    }
                } else if (i2 == 34) {
                    return findName(this._quadBuffer, qlen, q, 2);
                } else {
                    return parseEscapedName(this._quadBuffer, qlen, q, i2, 2);
                }
            } else if (i2 == 34) {
                return findName(this._quadBuffer, qlen, q, 1);
            } else {
                return parseEscapedName(this._quadBuffer, qlen, q, i2, 1);
            }
        }
        return parseEscapedName(this._quadBuffer, qlen, 0, q, 0);
    }

    protected String slowParseName() throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(": was expecting closing '\"' for name");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i] & MotionEventCompat.ACTION_MASK;
        if (i2 == 34) {
            return BuildConfig.FLAVOR;
        }
        return parseEscapedName(this._quadBuffer, 0, 0, i2, 0);
    }

    private final String parseName(int q1, int ch, int lastQuadBytes) throws IOException {
        return parseEscapedName(this._quadBuffer, 0, q1, ch, lastQuadBytes);
    }

    private final String parseName(int q1, int q2, int ch, int lastQuadBytes) throws IOException {
        this._quadBuffer[0] = q1;
        return parseEscapedName(this._quadBuffer, 1, q2, ch, lastQuadBytes);
    }

    private final String parseName(int q1, int q2, int q3, int ch, int lastQuadBytes) throws IOException {
        this._quadBuffer[0] = q1;
        this._quadBuffer[1] = q2;
        return parseEscapedName(this._quadBuffer, 2, q3, ch, lastQuadBytes);
    }

    protected final String parseEscapedName(int[] quads, int qlen, int currQuad, int ch, int currQuadBytes) throws IOException {
        int[] codes = _icLatin1;
        while (true) {
            int qlen2;
            byte[] bArr;
            int i;
            if (codes[ch] != 0) {
                if (ch == 34) {
                    break;
                }
                if (ch != 92) {
                    _throwUnquotedSpace(ch, Twitter.NAME);
                } else {
                    ch = _decodeEscaped();
                }
                if (ch > TransportMediator.KEYCODE_MEDIA_PAUSE) {
                    if (currQuadBytes >= 4) {
                        if (qlen >= quads.length) {
                            quads = growArrayBy(quads, quads.length);
                            this._quadBuffer = quads;
                        }
                        qlen2 = qlen + 1;
                        quads[qlen] = currQuad;
                        currQuad = 0;
                        currQuadBytes = 0;
                    } else {
                        qlen2 = qlen;
                    }
                    if (ch < AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT) {
                        currQuad = (currQuad << 8) | ((ch >> 6) | 192);
                        currQuadBytes++;
                        qlen = qlen2;
                    } else {
                        currQuad = (currQuad << 8) | ((ch >> 12) | 224);
                        currQuadBytes++;
                        if (currQuadBytes >= 4) {
                            if (qlen2 >= quads.length) {
                                quads = growArrayBy(quads, quads.length);
                                this._quadBuffer = quads;
                            }
                            qlen = qlen2 + 1;
                            quads[qlen2] = currQuad;
                            currQuad = 0;
                            currQuadBytes = 0;
                        } else {
                            qlen = qlen2;
                        }
                        currQuad = (currQuad << 8) | (((ch >> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                        currQuadBytes++;
                    }
                    ch = (ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
                    qlen2 = qlen;
                    if (currQuadBytes >= 4) {
                        currQuadBytes++;
                        currQuad = (currQuad << 8) | ch;
                        qlen = qlen2;
                    } else {
                        if (qlen2 >= quads.length) {
                            quads = growArrayBy(quads, quads.length);
                            this._quadBuffer = quads;
                        }
                        qlen = qlen2 + 1;
                        quads[qlen2] = currQuad;
                        currQuad = ch;
                        currQuadBytes = 1;
                    }
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        _reportInvalidEOF(" in field name");
                    }
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    ch = bArr[i] & MotionEventCompat.ACTION_MASK;
                }
            }
            qlen2 = qlen;
            if (currQuadBytes >= 4) {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            } else {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            }
            _reportInvalidEOF(" in field name");
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = bArr[i] & MotionEventCompat.ACTION_MASK;
        }
        if (currQuadBytes > 0) {
            if (qlen >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            qlen2 = qlen + 1;
            quads[qlen] = pad(currQuad, currQuadBytes);
            qlen = qlen2;
        }
        String name = this._symbols.findName(quads, qlen);
        if (name == null) {
            return addName(quads, qlen, currQuadBytes);
        }
        return name;
    }

    protected String _handleOddName(int ch) throws IOException {
        if (ch == 39 && isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            return _parseAposName();
        }
        int qlen;
        if (!isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            _reportUnexpectedChar((char) _decodeCharForError(ch), "was expecting double-quote to start field name");
        }
        int[] codes = CharTypes.getInputCodeUtf8JsNames();
        if (codes[ch] != 0) {
            _reportUnexpectedChar(ch, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int[] quads = this._quadBuffer;
        int currQuad = 0;
        int currQuadBytes = 0;
        int qlen2 = 0;
        while (true) {
            if (currQuadBytes < 4) {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            } else {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            ch = this._inputBuffer[this._inputPtr] & MotionEventCompat.ACTION_MASK;
            if (codes[ch] != 0) {
                break;
            }
            this._inputPtr++;
            qlen2 = qlen;
        }
        if (currQuadBytes > 0) {
            if (qlen >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            qlen2 = qlen + 1;
            quads[qlen] = currQuad;
            qlen = qlen2;
        }
        String name = this._symbols.findName(quads, qlen);
        if (name == null) {
            return addName(quads, qlen, currQuadBytes);
        }
        return name;
    }

    protected String _parseAposName() throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(": was expecting closing ''' for name");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int ch = bArr[i] & MotionEventCompat.ACTION_MASK;
        if (ch == 39) {
            return BuildConfig.FLAVOR;
        }
        int qlen;
        int[] quads = this._quadBuffer;
        int currQuad = 0;
        int currQuadBytes = 0;
        int[] codes = _icLatin1;
        int qlen2 = 0;
        while (ch != 39) {
            if (!(ch == 34 || codes[ch] == 0)) {
                if (ch != 92) {
                    _throwUnquotedSpace(ch, Twitter.NAME);
                } else {
                    ch = _decodeEscaped();
                }
                if (ch > TransportMediator.KEYCODE_MEDIA_PAUSE) {
                    if (currQuadBytes >= 4) {
                        if (qlen2 >= quads.length) {
                            quads = growArrayBy(quads, quads.length);
                            this._quadBuffer = quads;
                        }
                        qlen = qlen2 + 1;
                        quads[qlen2] = currQuad;
                        currQuad = 0;
                        currQuadBytes = 0;
                        qlen2 = qlen;
                    }
                    if (ch < AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT) {
                        currQuad = (currQuad << 8) | ((ch >> 6) | 192);
                        currQuadBytes++;
                        qlen = qlen2;
                    } else {
                        currQuad = (currQuad << 8) | ((ch >> 12) | 224);
                        currQuadBytes++;
                        if (currQuadBytes >= 4) {
                            if (qlen2 >= quads.length) {
                                quads = growArrayBy(quads, quads.length);
                                this._quadBuffer = quads;
                            }
                            qlen = qlen2 + 1;
                            quads[qlen2] = currQuad;
                            currQuad = 0;
                            currQuadBytes = 0;
                        } else {
                            qlen = qlen2;
                        }
                        currQuad = (currQuad << 8) | (((ch >> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                        currQuadBytes++;
                    }
                    ch = (ch & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
                    qlen2 = qlen;
                }
            }
            if (currQuadBytes < 4) {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            } else {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = bArr[i] & MotionEventCompat.ACTION_MASK;
            qlen2 = qlen;
        }
        if (currQuadBytes > 0) {
            if (qlen2 >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            qlen = qlen2 + 1;
            quads[qlen2] = pad(currQuad, currQuadBytes);
        } else {
            qlen = qlen2;
        }
        String name = this._symbols.findName(quads, qlen);
        if (name == null) {
            return addName(quads, qlen, currQuadBytes);
        }
        return name;
    }

    private final String findName(int q1, int lastQuadBytes) throws JsonParseException {
        q1 = pad(q1, lastQuadBytes);
        String name = this._symbols.findName(q1);
        if (name != null) {
            return name;
        }
        this._quadBuffer[0] = q1;
        return addName(this._quadBuffer, 1, lastQuadBytes);
    }

    private final String findName(int q1, int q2, int lastQuadBytes) throws JsonParseException {
        q2 = pad(q2, lastQuadBytes);
        String name = this._symbols.findName(q1, q2);
        if (name != null) {
            return name;
        }
        this._quadBuffer[0] = q1;
        this._quadBuffer[1] = q2;
        return addName(this._quadBuffer, 2, lastQuadBytes);
    }

    private final String findName(int q1, int q2, int q3, int lastQuadBytes) throws JsonParseException {
        q3 = pad(q3, lastQuadBytes);
        String name = this._symbols.findName(q1, q2, q3);
        if (name != null) {
            return name;
        }
        int[] quads = this._quadBuffer;
        quads[0] = q1;
        quads[1] = q2;
        quads[2] = pad(q3, lastQuadBytes);
        return addName(quads, 3, lastQuadBytes);
    }

    private final String findName(int[] quads, int qlen, int lastQuad, int lastQuadBytes) throws JsonParseException {
        if (qlen >= quads.length) {
            quads = growArrayBy(quads, quads.length);
            this._quadBuffer = quads;
        }
        int qlen2 = qlen + 1;
        quads[qlen] = pad(lastQuad, lastQuadBytes);
        String name = this._symbols.findName(quads, qlen2);
        if (name == null) {
            return addName(quads, qlen2, lastQuadBytes);
        }
        return name;
    }

    private final String addName(int[] quads, int qlen, int lastQuadBytes) throws JsonParseException {
        int lastQuad;
        int byteLen = ((qlen << 2) - 4) + lastQuadBytes;
        if (lastQuadBytes < 4) {
            lastQuad = quads[qlen - 1];
            quads[qlen - 1] = lastQuad << ((4 - lastQuadBytes) << 3);
        } else {
            lastQuad = 0;
        }
        char[] cbuf = this._textBuffer.emptyAndGetCurrentSegment();
        int ix = 0;
        int cix = 0;
        while (ix < byteLen) {
            int i;
            int i2 = (quads[ix >> 2] >> ((3 - (ix & 3)) << 3)) & MotionEventCompat.ACTION_MASK;
            ix++;
            if (i2 > TransportMediator.KEYCODE_MEDIA_PAUSE) {
                int needed;
                if ((i2 & 224) == 192) {
                    i2 &= 31;
                    needed = 1;
                } else if ((i2 & 240) == 224) {
                    i2 &= 15;
                    needed = 2;
                } else if ((i2 & 248) == 240) {
                    i2 &= 7;
                    needed = 3;
                } else {
                    _reportInvalidInitial(i2);
                    i2 = 1;
                    needed = 1;
                }
                if (ix + needed > byteLen) {
                    _reportInvalidEOF(" in field name");
                }
                int ch2 = quads[ix >> 2] >> ((3 - (ix & 3)) << 3);
                ix++;
                if ((ch2 & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
                    _reportInvalidOther(ch2);
                }
                i2 = (i2 << 6) | (ch2 & 63);
                if (needed > 1) {
                    ch2 = quads[ix >> 2] >> ((3 - (ix & 3)) << 3);
                    ix++;
                    if ((ch2 & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
                        _reportInvalidOther(ch2);
                    }
                    i2 = (i2 << 6) | (ch2 & 63);
                    if (needed > 2) {
                        ch2 = quads[ix >> 2] >> ((3 - (ix & 3)) << 3);
                        ix++;
                        if ((ch2 & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
                            _reportInvalidOther(ch2 & MotionEventCompat.ACTION_MASK);
                        }
                        i2 = (i2 << 6) | (ch2 & 63);
                    }
                }
                if (needed > 2) {
                    i2 -= AccessibilityNodeInfoCompat.ACTION_CUT;
                    if (cix >= cbuf.length) {
                        cbuf = this._textBuffer.expandCurrentSegment();
                    }
                    i = cix + 1;
                    cbuf[cix] = (char) (GeneratorBase.SURR1_FIRST + (i2 >> 10));
                    i2 = GeneratorBase.SURR2_FIRST | (i2 & Place.TYPE_SUBLOCALITY_LEVEL_1);
                    if (i >= cbuf.length) {
                        cbuf = this._textBuffer.expandCurrentSegment();
                    }
                    cix = i + 1;
                    cbuf[i] = (char) i2;
                }
            }
            i = cix;
            if (i >= cbuf.length) {
                cbuf = this._textBuffer.expandCurrentSegment();
            }
            cix = i + 1;
            cbuf[i] = (char) i2;
        }
        String baseName = new String(cbuf, 0, cix);
        if (lastQuadBytes < 4) {
            quads[qlen - 1] = lastQuad;
        }
        return this._symbols.addName(baseName, quads, qlen);
    }

    protected void _finishString() throws IOException {
        int ptr = this._inputPtr;
        if (ptr >= this._inputEnd) {
            loadMoreGuaranteed();
            ptr = this._inputPtr;
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = _icUTF8;
        int max = Math.min(this._inputEnd, outBuf.length + ptr);
        byte[] inputBuffer = this._inputBuffer;
        int outPtr = 0;
        while (ptr < max) {
            int c = inputBuffer[ptr] & MotionEventCompat.ACTION_MASK;
            if (codes[c] != 0) {
                if (c == 34) {
                    this._inputPtr = ptr + 1;
                    this._textBuffer.setCurrentLength(outPtr);
                    return;
                }
                this._inputPtr = ptr;
                _finishString2(outBuf, outPtr);
            }
            ptr++;
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = (char) c;
            outPtr = outPtr2;
        }
        this._inputPtr = ptr;
        _finishString2(outBuf, outPtr);
    }

    protected String _finishAndReturnString() throws IOException {
        int ptr = this._inputPtr;
        if (ptr >= this._inputEnd) {
            loadMoreGuaranteed();
            ptr = this._inputPtr;
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = _icUTF8;
        int max = Math.min(this._inputEnd, outBuf.length + ptr);
        byte[] inputBuffer = this._inputBuffer;
        int outPtr = 0;
        while (ptr < max) {
            int c = inputBuffer[ptr] & MotionEventCompat.ACTION_MASK;
            if (codes[c] != 0) {
                if (c == 34) {
                    this._inputPtr = ptr + 1;
                    return this._textBuffer.setCurrentAndReturn(outPtr);
                }
                this._inputPtr = ptr;
                _finishString2(outBuf, outPtr);
                return this._textBuffer.contentsAsString();
            }
            ptr++;
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = (char) c;
            outPtr = outPtr2;
        }
        this._inputPtr = ptr;
        _finishString2(outBuf, outPtr);
        return this._textBuffer.contentsAsString();
    }

    private final void _finishString2(char[] outBuf, int outPtr) throws IOException {
        int[] codes = _icUTF8;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            int ptr = this._inputPtr;
            if (ptr >= this._inputEnd) {
                loadMoreGuaranteed();
                ptr = this._inputPtr;
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int max = Math.min(this._inputEnd, (outBuf.length - outPtr) + ptr);
            int ptr2 = ptr;
            int outPtr2 = outPtr;
            while (ptr2 < max) {
                ptr = ptr2 + 1;
                int c = inputBuffer[ptr2] & MotionEventCompat.ACTION_MASK;
                if (codes[c] != 0) {
                    this._inputPtr = ptr;
                    if (c == 34) {
                        this._textBuffer.setCurrentLength(outPtr2);
                        return;
                    }
                    switch (codes[c]) {
                        case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                            c = _decodeEscaped();
                            outPtr = outPtr2;
                            break;
                        case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                            c = _decodeUtf8_2(c);
                            outPtr = outPtr2;
                            break;
                        case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                            if (this._inputEnd - this._inputPtr < 2) {
                                c = _decodeUtf8_3(c);
                                outPtr = outPtr2;
                                break;
                            }
                            c = _decodeUtf8_3fast(c);
                            outPtr = outPtr2;
                            break;
                        case Place.TYPE_AQUARIUM /*4*/:
                            c = _decodeUtf8_4(c);
                            outPtr = outPtr2 + 1;
                            outBuf[outPtr2] = (char) (GeneratorBase.SURR1_FIRST | (c >> 10));
                            if (outPtr >= outBuf.length) {
                                outBuf = this._textBuffer.finishCurrentSegment();
                                outPtr = 0;
                            }
                            c = GeneratorBase.SURR2_FIRST | (c & Place.TYPE_SUBLOCALITY_LEVEL_1);
                            break;
                        default:
                            if (c >= 32) {
                                _reportInvalidChar(c);
                                outPtr = outPtr2;
                                break;
                            }
                            _throwUnquotedSpace(c, "string value");
                            outPtr = outPtr2;
                            break;
                    }
                    if (outPtr >= outBuf.length) {
                        outBuf = this._textBuffer.finishCurrentSegment();
                        outPtr = 0;
                    }
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (char) c;
                    outPtr = outPtr2;
                } else {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (char) c;
                    ptr2 = ptr;
                    outPtr2 = outPtr;
                }
            }
            this._inputPtr = ptr2;
            outPtr = outPtr2;
        }
    }

    protected void _skipString() throws java.io.IOException {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:42)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:66)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r7 = this;
        r6 = 0;
        r7._tokenIncomplete = r6;
        r1 = _icUTF8;
        r2 = r7._inputBuffer;
    L_0x0007:
        r4 = r7._inputPtr;
        r3 = r7._inputEnd;
        if (r4 < r3) goto L_0x004e;
    L_0x000d:
        r7.loadMoreGuaranteed();
        r4 = r7._inputPtr;
        r3 = r7._inputEnd;
        r5 = r4;
    L_0x0015:
        if (r5 >= r3) goto L_0x0028;
    L_0x0017:
        r4 = r5 + 1;
        r6 = r2[r5];
        r0 = r6 & 255;
        r6 = r1[r0];
        if (r6 == 0) goto L_0x004e;
    L_0x0021:
        r7._inputPtr = r4;
        r6 = 34;
        if (r0 != r6) goto L_0x002b;
    L_0x0027:
        return;
    L_0x0028:
        r7._inputPtr = r5;
        goto L_0x0007;
    L_0x002b:
        r6 = r1[r0];
        switch(r6) {
            case 1: goto L_0x003a;
            case 2: goto L_0x003e;
            case 3: goto L_0x0042;
            case 4: goto L_0x0046;
            default: goto L_0x0030;
        };
    L_0x0030:
        r6 = 32;
        if (r0 >= r6) goto L_0x004a;
    L_0x0034:
        r6 = "string value";
        r7._throwUnquotedSpace(r0, r6);
        goto L_0x0007;
    L_0x003a:
        r7._decodeEscaped();
        goto L_0x0007;
    L_0x003e:
        r7._skipUtf8_2(r0);
        goto L_0x0007;
    L_0x0042:
        r7._skipUtf8_3(r0);
        goto L_0x0007;
    L_0x0046:
        r7._skipUtf8_4(r0);
        goto L_0x0007;
    L_0x004a:
        r7._reportInvalidChar(r0);
        goto L_0x0007;
    L_0x004e:
        r5 = r4;
        goto L_0x0015;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.UTF8StreamJsonParser._skipString():void");
    }

    protected JsonToken _handleUnexpectedValue(int c) throws IOException {
        switch (c) {
            case Place.TYPE_FUNERAL_HOME /*39*/:
                break;
            case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    _reportInvalidEOFInValue();
                }
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                return _handleInvalidNumberStart(bArr[i] & MotionEventCompat.ACTION_MASK, false);
            case Place.TYPE_PHYSIOTHERAPIST /*73*/:
                _matchToken("Infinity", 1);
                if (!isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    _reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                    break;
                }
                return resetAsNaN("Infinity", Double.POSITIVE_INFINITY);
            case Place.TYPE_REAL_ESTATE_AGENCY /*78*/:
                _matchToken("NaN", 1);
                if (!isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                    break;
                }
                return resetAsNaN("NaN", Double.NaN);
            case Place.TYPE_TRAVEL_AGENCY /*93*/:
            case 125:
                _reportUnexpectedChar(c, "expected a value");
                break;
        }
        if (isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            return _handleApos();
        }
        if (Character.isJavaIdentifierStart(c)) {
            _reportInvalidToken(BuildConfig.FLAVOR + ((char) c), "('true', 'false' or 'null')");
        }
        _reportUnexpectedChar(c, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        return null;
    }

    protected JsonToken _handleApos() throws IOException {
        int outPtr = 0;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = _icUTF8;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int max = this._inputEnd;
            int max2 = this._inputPtr + (outBuf.length - outPtr);
            if (max2 < max) {
                max = max2;
            }
            while (this._inputPtr < max) {
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int c = inputBuffer[i] & MotionEventCompat.ACTION_MASK;
                int outPtr2;
                if (c != 39 && codes[c] == 0) {
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (char) c;
                    outPtr = outPtr2;
                } else if (c == 39) {
                    this._textBuffer.setCurrentLength(outPtr);
                    return JsonToken.VALUE_STRING;
                } else {
                    switch (codes[c]) {
                        case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                            if (c != 39) {
                                c = _decodeEscaped();
                                break;
                            }
                            break;
                        case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                            c = _decodeUtf8_2(c);
                            break;
                        case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                            if (this._inputEnd - this._inputPtr < 2) {
                                c = _decodeUtf8_3(c);
                                break;
                            }
                            c = _decodeUtf8_3fast(c);
                            break;
                        case Place.TYPE_AQUARIUM /*4*/:
                            c = _decodeUtf8_4(c);
                            outPtr2 = outPtr + 1;
                            outBuf[outPtr] = (char) (GeneratorBase.SURR1_FIRST | (c >> 10));
                            if (outPtr2 >= outBuf.length) {
                                outBuf = this._textBuffer.finishCurrentSegment();
                                outPtr = 0;
                            } else {
                                outPtr = outPtr2;
                            }
                            c = GeneratorBase.SURR2_FIRST | (c & Place.TYPE_SUBLOCALITY_LEVEL_1);
                            break;
                        default:
                            if (c < 32) {
                                _throwUnquotedSpace(c, "string value");
                            }
                            _reportInvalidChar(c);
                            break;
                    }
                    if (outPtr >= outBuf.length) {
                        outBuf = this._textBuffer.finishCurrentSegment();
                        outPtr = 0;
                    }
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (char) c;
                    outPtr = outPtr2;
                }
            }
        }
    }

    protected JsonToken _handleInvalidNumberStart(int ch, boolean neg) throws IOException {
        while (ch == 73) {
            String match;
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOFInValue();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = bArr[i];
            if (ch != 78) {
                if (ch != 110) {
                    break;
                }
                match = neg ? "-Infinity" : "+Infinity";
            } else {
                match = neg ? "-INF" : "+INF";
            }
            _matchToken(match, 3);
            if (isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                return resetAsNaN(match, neg ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
            }
            _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
        }
        reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
        return null;
    }

    protected final void _matchToken(String matchStr, int i) throws IOException {
        int len = matchStr.length();
        if (this._inputPtr + len >= this._inputEnd) {
            _matchToken2(matchStr, i);
            return;
        }
        do {
            if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
                _reportInvalidToken(matchStr.substring(0, i));
            }
            this._inputPtr++;
            i++;
        } while (i < len);
        int ch = this._inputBuffer[this._inputPtr] & MotionEventCompat.ACTION_MASK;
        if (ch >= 48 && ch != 93 && ch != 125) {
            _checkMatchEnd(matchStr, i, ch);
        }
    }

    private final void _matchToken2(String matchStr, int i) throws IOException {
        int len = matchStr.length();
        do {
            if ((this._inputPtr >= this._inputEnd && !loadMore()) || this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
                _reportInvalidToken(matchStr.substring(0, i));
            }
            this._inputPtr++;
            i++;
        } while (i < len);
        if (this._inputPtr < this._inputEnd || loadMore()) {
            int ch = this._inputBuffer[this._inputPtr] & MotionEventCompat.ACTION_MASK;
            if (ch >= 48 && ch != 93 && ch != 125) {
                _checkMatchEnd(matchStr, i, ch);
            }
        }
    }

    private final void _checkMatchEnd(String matchStr, int i, int ch) throws IOException {
        if (Character.isJavaIdentifierPart((char) _decodeCharForError(ch))) {
            _reportInvalidToken(matchStr.substring(0, i));
        }
    }

    private final int _skipWS() throws IOException {
        while (this._inputPtr < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i] & MotionEventCompat.ACTION_MASK;
            if (i2 > 32) {
                if (i2 != 47 && i2 != 35) {
                    return i2;
                }
                this._inputPtr--;
                return _skipWS2();
            } else if (i2 != 32) {
                if (i2 == 10) {
                    this._currInputRow++;
                    this._currInputRowStart = this._inputPtr;
                } else if (i2 == 13) {
                    _skipCR();
                } else if (i2 != 9) {
                    _throwInvalidSpace(i2);
                }
            }
        }
        return _skipWS2();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int _skipWS2() throws java.io.IOException {
        /*
        r5 = this;
        r4 = 32;
    L_0x0002:
        r1 = r5._inputPtr;
        r2 = r5._inputEnd;
        if (r1 < r2) goto L_0x000e;
    L_0x0008:
        r1 = r5.loadMore();
        if (r1 == 0) goto L_0x0050;
    L_0x000e:
        r1 = r5._inputBuffer;
        r2 = r5._inputPtr;
        r3 = r2 + 1;
        r5._inputPtr = r3;
        r1 = r1[r2];
        r0 = r1 & 255;
        if (r0 <= r4) goto L_0x002f;
    L_0x001c:
        r1 = 47;
        if (r0 != r1) goto L_0x0024;
    L_0x0020:
        r5._skipComment();
        goto L_0x0002;
    L_0x0024:
        r1 = 35;
        if (r0 != r1) goto L_0x002e;
    L_0x0028:
        r1 = r5._skipYAMLComment();
        if (r1 != 0) goto L_0x0002;
    L_0x002e:
        return r0;
    L_0x002f:
        if (r0 == r4) goto L_0x0002;
    L_0x0031:
        r1 = 10;
        if (r0 != r1) goto L_0x0040;
    L_0x0035:
        r1 = r5._currInputRow;
        r1 = r1 + 1;
        r5._currInputRow = r1;
        r1 = r5._inputPtr;
        r5._currInputRowStart = r1;
        goto L_0x0002;
    L_0x0040:
        r1 = 13;
        if (r0 != r1) goto L_0x0048;
    L_0x0044:
        r5._skipCR();
        goto L_0x0002;
    L_0x0048:
        r1 = 9;
        if (r0 == r1) goto L_0x0002;
    L_0x004c:
        r5._throwInvalidSpace(r0);
        goto L_0x0002;
    L_0x0050:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unexpected end-of-input within/between ";
        r1 = r1.append(r2);
        r2 = r5._parsingContext;
        r2 = r2.getTypeDesc();
        r1 = r1.append(r2);
        r2 = " entries";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r1 = r5._constructError(r1);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.UTF8StreamJsonParser._skipWS2():int");
    }

    private final int _skipWSOrEnd() throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            return _eofAsNextChar();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i] & MotionEventCompat.ACTION_MASK;
        if (i2 <= 32) {
            if (i2 != 32) {
                if (i2 == 10) {
                    this._currInputRow++;
                    this._currInputRowStart = this._inputPtr;
                } else if (i2 == 13) {
                    _skipCR();
                } else if (i2 != 9) {
                    _throwInvalidSpace(i2);
                }
            }
            while (this._inputPtr < this._inputEnd) {
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = bArr[i] & MotionEventCompat.ACTION_MASK;
                if (i2 > 32) {
                    if (i2 != 47 && i2 != 35) {
                        return i2;
                    }
                    this._inputPtr--;
                    return _skipWSOrEnd2();
                } else if (i2 != 32) {
                    if (i2 == 10) {
                        this._currInputRow++;
                        this._currInputRowStart = this._inputPtr;
                    } else if (i2 == 13) {
                        _skipCR();
                    } else if (i2 != 9) {
                        _throwInvalidSpace(i2);
                    }
                }
            }
            return _skipWSOrEnd2();
        } else if (i2 != 47 && i2 != 35) {
            return i2;
        } else {
            this._inputPtr--;
            return _skipWSOrEnd2();
        }
    }

    private final int _skipWSOrEnd2() throws IOException {
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                return _eofAsNextChar();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i] & MotionEventCompat.ACTION_MASK;
            if (i2 > 32) {
                if (i2 == 47) {
                    _skipComment();
                } else if (i2 != 35 || !_skipYAMLComment()) {
                    return i2;
                }
            } else if (i2 != 32) {
                if (i2 == 10) {
                    this._currInputRow++;
                    this._currInputRowStart = this._inputPtr;
                } else if (i2 == 13) {
                    _skipCR();
                } else if (i2 != 9) {
                    _throwInvalidSpace(i2);
                }
            }
        }
    }

    private final int _skipColon() throws IOException {
        if (this._inputPtr + 4 >= this._inputEnd) {
            return _skipColon2(false);
        }
        int i = this._inputBuffer[this._inputPtr];
        byte[] bArr;
        int i2;
        if (i == 58) {
            bArr = this._inputBuffer;
            i2 = this._inputPtr + 1;
            this._inputPtr = i2;
            i = bArr[i2];
            if (i <= 32) {
                if (i == 32 || i == 9) {
                    bArr = this._inputBuffer;
                    i2 = this._inputPtr + 1;
                    this._inputPtr = i2;
                    i = bArr[i2];
                    if (i > 32) {
                        if (i == 47 || i == 35) {
                            return _skipColon2(true);
                        }
                        this._inputPtr++;
                        return i;
                    }
                }
                return _skipColon2(true);
            } else if (i == 47 || i == 35) {
                return _skipColon2(true);
            } else {
                this._inputPtr++;
                return i;
            }
        }
        if (i == 32 || i == 9) {
            bArr = this._inputBuffer;
            i2 = this._inputPtr + 1;
            this._inputPtr = i2;
            i = bArr[i2];
        }
        if (i != 58) {
            return _skipColon2(false);
        }
        bArr = this._inputBuffer;
        i2 = this._inputPtr + 1;
        this._inputPtr = i2;
        i = bArr[i2];
        if (i <= 32) {
            if (i == 32 || i == 9) {
                bArr = this._inputBuffer;
                i2 = this._inputPtr + 1;
                this._inputPtr = i2;
                i = bArr[i2];
                if (i > 32) {
                    if (i == 47 || i == 35) {
                        return _skipColon2(true);
                    }
                    this._inputPtr++;
                    return i;
                }
            }
            return _skipColon2(true);
        } else if (i == 47 || i == 35) {
            return _skipColon2(true);
        } else {
            this._inputPtr++;
            return i;
        }
    }

    private final int _skipColon2(boolean gotColon) throws IOException {
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i] & MotionEventCompat.ACTION_MASK;
            if (i2 > 32) {
                if (i2 == 47) {
                    _skipComment();
                } else if (i2 != 35 || !_skipYAMLComment()) {
                    if (gotColon) {
                        return i2;
                    }
                    if (i2 != 58) {
                        if (i2 < 32) {
                            _throwInvalidSpace(i2);
                        }
                        _reportUnexpectedChar(i2, "was expecting a colon to separate field name and value");
                    }
                    gotColon = true;
                }
            } else if (i2 != 32) {
                if (i2 == 10) {
                    this._currInputRow++;
                    this._currInputRowStart = this._inputPtr;
                } else if (i2 == 13) {
                    _skipCR();
                } else if (i2 != 9) {
                    _throwInvalidSpace(i2);
                }
            }
        }
        throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
    }

    private final void _skipComment() throws IOException {
        if (!isEnabled(Feature.ALLOW_COMMENTS)) {
            _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in a comment");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int c = bArr[i] & MotionEventCompat.ACTION_MASK;
        if (c == 47) {
            _skipLine();
        } else if (c == 42) {
            _skipCComment();
        } else {
            _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
        }
    }

    private final void _skipCComment() throws IOException {
        int[] codes = CharTypes.getInputCodeComment();
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & MotionEventCompat.ACTION_MASK;
                int code = codes[i2];
                if (code != 0) {
                    switch (code) {
                        case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                            _skipUtf8_2(i2);
                            continue;
                        case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                            _skipUtf8_3(i2);
                            continue;
                        case Place.TYPE_AQUARIUM /*4*/:
                            _skipUtf8_4(i2);
                            continue;
                        case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                            this._currInputRow++;
                            this._currInputRowStart = this._inputPtr;
                            continue;
                        case Place.TYPE_BOWLING_ALLEY /*13*/:
                            _skipCR();
                            continue;
                        case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                                break;
                            } else if (this._inputBuffer[this._inputPtr] == 47) {
                                this._inputPtr++;
                                return;
                            } else {
                                continue;
                            }
                        default:
                            _reportInvalidChar(i2);
                            continue;
                    }
                }
            }
            _reportInvalidEOF(" in a comment");
            return;
        }
    }

    private final boolean _skipYAMLComment() throws IOException {
        if (!isEnabled(Feature.ALLOW_YAML_COMMENTS)) {
            return false;
        }
        _skipLine();
        return true;
    }

    private final void _skipLine() throws IOException {
        int[] codes = CharTypes.getInputCodeComment();
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & MotionEventCompat.ACTION_MASK;
                int code = codes[i2];
                if (code != 0) {
                    switch (code) {
                        case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                            _skipUtf8_2(i2);
                            break;
                        case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                            _skipUtf8_3(i2);
                            break;
                        case Place.TYPE_AQUARIUM /*4*/:
                            _skipUtf8_4(i2);
                            break;
                        case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                            this._currInputRow++;
                            this._currInputRowStart = this._inputPtr;
                            return;
                        case Place.TYPE_BOWLING_ALLEY /*13*/:
                            _skipCR();
                            return;
                        case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                            break;
                        default:
                            if (code >= 0) {
                                break;
                            }
                            _reportInvalidChar(i2);
                            break;
                    }
                }
            } else {
                return;
            }
        }
    }

    protected char _decodeEscaped() throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in character escape sequence");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int c = bArr[i];
        switch (c) {
            case Place.TYPE_ESTABLISHMENT /*34*/:
            case Place.TYPE_HEALTH /*47*/:
            case Place.TYPE_TRAIN_STATION /*92*/:
                return (char) c;
            case 98:
                return '\b';
            case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                return '\f';
            case 110:
                return '\n';
            case 114:
                return '\r';
            case 116:
                return '\t';
            case 117:
                int value = 0;
                for (int i2 = 0; i2 < 4; i2++) {
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        _reportInvalidEOF(" in character escape sequence");
                    }
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    int ch = bArr[i];
                    int digit = CharTypes.charToHex(ch);
                    if (digit < 0) {
                        _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
                    }
                    value = (value << 4) | digit;
                }
                return (char) value;
            default:
                return _handleUnrecognizedCharacterEscape((char) _decodeCharForError(c));
        }
    }

    protected int _decodeCharForError(int firstByte) throws IOException {
        int c = firstByte & MotionEventCompat.ACTION_MASK;
        if (c <= TransportMediator.KEYCODE_MEDIA_PAUSE) {
            return c;
        }
        int needed;
        if ((c & 224) == 192) {
            c &= 31;
            needed = 1;
        } else if ((c & 240) == 224) {
            c &= 15;
            needed = 2;
        } else if ((c & 248) == 240) {
            c &= 7;
            needed = 3;
        } else {
            _reportInvalidInitial(c & MotionEventCompat.ACTION_MASK);
            needed = 1;
        }
        int d = nextByte();
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK);
        }
        c = (c << 6) | (d & 63);
        if (needed <= 1) {
            return c;
        }
        d = nextByte();
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK);
        }
        c = (c << 6) | (d & 63);
        if (needed <= 2) {
            return c;
        }
        d = nextByte();
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK);
        }
        return (c << 6) | (d & 63);
    }

    private final int _decodeUtf8_2(int c) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        return ((c & 31) << 6) | (d & 63);
    }

    private final int _decodeUtf8_3(int c1) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        c1 &= 15;
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        int c = (c1 << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        return (c << 6) | (d & 63);
    }

    private final int _decodeUtf8_3fast(int c1) throws IOException {
        c1 &= 15;
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        int c = (c1 << 6) | (d & 63);
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        return (c << 6) | (d & 63);
    }

    private final int _decodeUtf8_4(int c) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        c = ((c & 7) << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        c = (c << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        return ((c << 6) | (d & 63)) - AccessibilityNodeInfoCompat.ACTION_CUT;
    }

    private final void _skipUtf8_2(int c) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        c = bArr[i];
        if ((c & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(c & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
    }

    private final void _skipUtf8_3(int c) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        c = bArr[i];
        if ((c & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(c & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        c = bArr[i];
        if ((c & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(c & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
    }

    private final void _skipUtf8_4(int c) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & 192) != AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            _reportInvalidOther(d & MotionEventCompat.ACTION_MASK, this._inputPtr);
        }
    }

    protected final void _skipCR() throws IOException {
        if ((this._inputPtr < this._inputEnd || loadMore()) && this._inputBuffer[this._inputPtr] == 10) {
            this._inputPtr++;
        }
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    private int nextByte() throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        return bArr[i] & MotionEventCompat.ACTION_MASK;
    }

    protected void _reportInvalidToken(String matchedPart) throws IOException {
        _reportInvalidToken(matchedPart, "'null', 'true', 'false' or NaN");
    }

    protected void _reportInvalidToken(String matchedPart, String msg) throws IOException {
        StringBuilder sb = new StringBuilder(matchedPart);
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = (char) _decodeCharForError(bArr[i]);
            if (!Character.isJavaIdentifierPart(c)) {
                break;
            }
            sb.append(c);
        }
        _reportError("Unrecognized token '" + sb.toString() + "': was expecting " + msg);
    }

    protected void _reportInvalidChar(int c) throws JsonParseException {
        if (c < 32) {
            _throwInvalidSpace(c);
        }
        _reportInvalidInitial(c);
    }

    protected void _reportInvalidInitial(int mask) throws JsonParseException {
        _reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(mask));
    }

    protected void _reportInvalidOther(int mask) throws JsonParseException {
        _reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(mask));
    }

    protected void _reportInvalidOther(int mask, int ptr) throws JsonParseException {
        this._inputPtr = ptr;
        _reportInvalidOther(mask);
    }

    public static int[] growArrayBy(int[] arr, int more) {
        if (arr == null) {
            return new int[more];
        }
        return Arrays.copyOf(arr, arr.length + more);
    }

    protected final byte[] _decodeBase64(Base64Variant b64variant) throws IOException {
        int ch;
        ByteArrayBuilder builder = _getByteArrayBuilder();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = bArr[i] & MotionEventCompat.ACTION_MASK;
            if (ch > 32) {
                int bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    if (ch == 34) {
                        return builder.toByteArray();
                    }
                    bits = _decodeBase64Escape(b64variant, ch, 0);
                    if (bits < 0) {
                        continue;
                    }
                }
                int decodedData = bits;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                ch = bArr[i] & MotionEventCompat.ACTION_MASK;
                bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    bits = _decodeBase64Escape(b64variant, ch, 1);
                }
                decodedData = (decodedData << 6) | bits;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                ch = bArr[i] & MotionEventCompat.ACTION_MASK;
                bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    if (bits != -2) {
                        if (ch != 34 || b64variant.usesPadding()) {
                            bits = _decodeBase64Escape(b64variant, ch, 2);
                        } else {
                            builder.append(decodedData >> 4);
                            return builder.toByteArray();
                        }
                    }
                    if (bits == -2) {
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        bArr = this._inputBuffer;
                        i = this._inputPtr;
                        this._inputPtr = i + 1;
                        ch = bArr[i] & MotionEventCompat.ACTION_MASK;
                        if (!b64variant.usesPaddingChar(ch)) {
                            break;
                        }
                        builder.append(decodedData >> 4);
                    }
                }
                decodedData = (decodedData << 6) | bits;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                ch = bArr[i] & MotionEventCompat.ACTION_MASK;
                bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    if (bits != -2) {
                        if (ch != 34 || b64variant.usesPadding()) {
                            bits = _decodeBase64Escape(b64variant, ch, 3);
                        } else {
                            builder.appendTwoBytes(decodedData >> 2);
                            return builder.toByteArray();
                        }
                    }
                    if (bits == -2) {
                        builder.appendTwoBytes(decodedData >> 2);
                    }
                }
                builder.appendThreeBytes((decodedData << 6) | bits);
            }
        }
        throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
    }

    private static final int pad(int q, int bytes) {
        return bytes == 4 ? q : q | (-1 << (bytes << 3));
    }
}
