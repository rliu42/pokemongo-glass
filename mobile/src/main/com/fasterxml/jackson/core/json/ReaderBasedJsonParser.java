package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.constants.BluetoothGattSupport;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import org.apache.commons.io.IOUtils;
import spacemadness.com.lunarconsole.BuildConfig;

public class ReaderBasedJsonParser extends ParserBase {
    protected static final int[] _icLatin1;
    protected boolean _bufferRecyclable;
    protected final int _hashSeed;
    protected char[] _inputBuffer;
    protected ObjectCodec _objectCodec;
    protected Reader _reader;
    protected final CharsToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete;

    static {
        _icLatin1 = CharTypes.getInputCodeLatin1();
    }

    public ReaderBasedJsonParser(IOContext ctxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st, char[] inputBuffer, int start, int end, boolean bufferRecyclable) {
        super(ctxt, features);
        this._tokenIncomplete = false;
        this._reader = r;
        this._inputBuffer = inputBuffer;
        this._inputPtr = start;
        this._inputEnd = end;
        this._objectCodec = codec;
        this._symbols = st;
        this._hashSeed = st.hashSeed();
        this._bufferRecyclable = bufferRecyclable;
    }

    public ReaderBasedJsonParser(IOContext ctxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st) {
        super(ctxt, features);
        this._tokenIncomplete = false;
        this._reader = r;
        this._inputBuffer = ctxt.allocTokenBuffer();
        this._inputPtr = 0;
        this._inputEnd = 0;
        this._objectCodec = codec;
        this._symbols = st;
        this._hashSeed = st.hashSeed();
        this._bufferRecyclable = true;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    public int releaseBuffered(Writer w) throws IOException {
        int count = this._inputEnd - this._inputPtr;
        if (count < 1) {
            return 0;
        }
        w.write(this._inputBuffer, this._inputPtr, count);
        return count;
    }

    public Object getInputSource() {
        return this._reader;
    }

    protected boolean loadMore() throws IOException {
        this._currInputProcessed += (long) this._inputEnd;
        this._currInputRowStart -= this._inputEnd;
        if (this._reader == null) {
            return false;
        }
        int count = this._reader.read(this._inputBuffer, 0, this._inputBuffer.length);
        if (count > 0) {
            this._inputPtr = 0;
            this._inputEnd = count;
            return true;
        }
        _closeInput();
        if (count != 0) {
            return false;
        }
        throw new IOException("Reader returned 0 characters when trying to read " + this._inputEnd);
    }

    protected char getNextChar(String eofMsg) throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(eofMsg);
        }
        char[] cArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        return cArr[i];
    }

    protected void _closeInput() throws IOException {
        if (this._reader != null) {
            if (this._ioContext.isResourceManaged() || isEnabled(Feature.AUTO_CLOSE_SOURCE)) {
                this._reader.close();
            }
            this._reader = null;
        }
    }

    protected void _releaseBuffers() throws IOException {
        super._releaseBuffers();
        this._symbols.release();
        if (this._bufferRecyclable) {
            char[] buf = this._inputBuffer;
            if (buf != null) {
                this._inputBuffer = null;
                this._ioContext.releaseTokenBuffer(buf);
            }
        }
    }

    public final String getText() throws IOException {
        JsonToken t = this._currToken;
        if (t != JsonToken.VALUE_STRING) {
            return _getText2(t);
        }
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            _finishString();
        }
        return this._textBuffer.contentsAsString();
    }

    public final String getValueAsString() throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            if (this._tokenIncomplete) {
                this._tokenIncomplete = false;
                _finishString();
            }
            return this._textBuffer.contentsAsString();
        } else if (this._currToken == JsonToken.FIELD_NAME) {
            return getCurrentName();
        } else {
            return super.getValueAsString(null);
        }
    }

    public final String getValueAsString(String defValue) throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            if (this._tokenIncomplete) {
                this._tokenIncomplete = false;
                _finishString();
            }
            return this._textBuffer.contentsAsString();
        } else if (this._currToken == JsonToken.FIELD_NAME) {
            return getCurrentName();
        } else {
            return super.getValueAsString(defValue);
        }
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

    public final char[] getTextCharacters() throws IOException {
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

    public final int getTextLength() throws IOException {
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

    public final int getTextOffset() throws IOException {
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
        r1 = r7[r8];
        r7 = 32;
        if (r1 <= r7) goto L_0x0005;
    L_0x001c:
        r0 = r11.decodeBase64Char(r1);
        if (r0 >= 0) goto L_0x0038;
    L_0x0022:
        r7 = 34;
        if (r1 != r7) goto L_0x0031;
    L_0x0026:
        r7 = 0;
        r10._tokenIncomplete = r7;
        if (r5 <= 0) goto L_0x0030;
    L_0x002b:
        r3 = r3 + r5;
        r7 = 0;
        r12.write(r13, r7, r5);
    L_0x0030:
        return r3;
    L_0x0031:
        r7 = 0;
        r0 = r10._decodeBase64Escape(r11, r1, r7);
        if (r0 < 0) goto L_0x0005;
    L_0x0038:
        if (r5 <= r4) goto L_0x0040;
    L_0x003a:
        r3 = r3 + r5;
        r7 = 0;
        r12.write(r13, r7, r5);
        r5 = 0;
    L_0x0040:
        r2 = r0;
        r7 = r10._inputPtr;
        r8 = r10._inputEnd;
        if (r7 < r8) goto L_0x004a;
    L_0x0047:
        r10.loadMoreGuaranteed();
    L_0x004a:
        r7 = r10._inputBuffer;
        r8 = r10._inputPtr;
        r9 = r8 + 1;
        r10._inputPtr = r9;
        r1 = r7[r8];
        r0 = r11.decodeBase64Char(r1);
        if (r0 >= 0) goto L_0x005f;
    L_0x005a:
        r7 = 1;
        r0 = r10._decodeBase64Escape(r11, r1, r7);
    L_0x005f:
        r7 = r2 << 6;
        r2 = r7 | r0;
        r7 = r10._inputPtr;
        r8 = r10._inputEnd;
        if (r7 < r8) goto L_0x006c;
    L_0x0069:
        r10.loadMoreGuaranteed();
    L_0x006c:
        r7 = r10._inputBuffer;
        r8 = r10._inputPtr;
        r9 = r8 + 1;
        r10._inputPtr = r9;
        r1 = r7[r8];
        r0 = r11.decodeBase64Char(r1);
        if (r0 >= 0) goto L_0x00e0;
    L_0x007c:
        r7 = -2;
        if (r0 == r7) goto L_0x0097;
    L_0x007f:
        r7 = 34;
        if (r1 != r7) goto L_0x0092;
    L_0x0083:
        r7 = r11.usesPadding();
        if (r7 != 0) goto L_0x0092;
    L_0x0089:
        r2 = r2 >> 4;
        r6 = r5 + 1;
        r7 = (byte) r2;
        r13[r5] = r7;
        r5 = r6;
        goto L_0x0026;
    L_0x0092:
        r7 = 2;
        r0 = r10._decodeBase64Escape(r11, r1, r7);
    L_0x0097:
        r7 = -2;
        if (r0 != r7) goto L_0x00e0;
    L_0x009a:
        r7 = r10._inputPtr;
        r8 = r10._inputEnd;
        if (r7 < r8) goto L_0x00a3;
    L_0x00a0:
        r10.loadMoreGuaranteed();
    L_0x00a3:
        r7 = r10._inputBuffer;
        r8 = r10._inputPtr;
        r9 = r8 + 1;
        r10._inputPtr = r9;
        r1 = r7[r8];
        r7 = r11.usesPaddingChar(r1);
        if (r7 != 0) goto L_0x00d6;
    L_0x00b3:
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
    L_0x00d6:
        r2 = r2 >> 4;
        r6 = r5 + 1;
        r7 = (byte) r2;
        r13[r5] = r7;
        r5 = r6;
        goto L_0x0005;
    L_0x00e0:
        r7 = r2 << 6;
        r2 = r7 | r0;
        r7 = r10._inputPtr;
        r8 = r10._inputEnd;
        if (r7 < r8) goto L_0x00ed;
    L_0x00ea:
        r10.loadMoreGuaranteed();
    L_0x00ed:
        r7 = r10._inputBuffer;
        r8 = r10._inputPtr;
        r9 = r8 + 1;
        r10._inputPtr = r9;
        r1 = r7[r8];
        r0 = r11.decodeBase64Char(r1);
        if (r0 >= 0) goto L_0x0132;
    L_0x00fd:
        r7 = -2;
        if (r0 == r7) goto L_0x011f;
    L_0x0100:
        r7 = 34;
        if (r1 != r7) goto L_0x011a;
    L_0x0104:
        r7 = r11.usesPadding();
        if (r7 != 0) goto L_0x011a;
    L_0x010a:
        r2 = r2 >> 2;
        r6 = r5 + 1;
        r7 = r2 >> 8;
        r7 = (byte) r7;
        r13[r5] = r7;
        r5 = r6 + 1;
        r7 = (byte) r2;
        r13[r6] = r7;
        goto L_0x0026;
    L_0x011a:
        r7 = 3;
        r0 = r10._decodeBase64Escape(r11, r1, r7);
    L_0x011f:
        r7 = -2;
        if (r0 != r7) goto L_0x0132;
    L_0x0122:
        r2 = r2 >> 2;
        r6 = r5 + 1;
        r7 = r2 >> 8;
        r7 = (byte) r7;
        r13[r5] = r7;
        r5 = r6 + 1;
        r7 = (byte) r2;
        r13[r6] = r7;
        goto L_0x0005;
    L_0x0132:
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
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.ReaderBasedJsonParser._readBinary(com.fasterxml.jackson.core.Base64Variant, java.io.OutputStream, byte[]):int");
    }

    public final JsonToken nextToken() throws IOException {
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
                i = _skipComma(i);
            }
            boolean inObject = this._parsingContext.inObject();
            if (inObject) {
                this._parsingContext.setCurrentName(i == 34 ? _parseName() : _handleOddName(i));
                this._currToken = JsonToken.FIELD_NAME;
                i = _skipColon();
            }
            switch (i) {
                case Place.TYPE_ESTABLISHMENT /*34*/:
                    this._tokenIncomplete = true;
                    jsonToken = JsonToken.VALUE_STRING;
                    break;
                case Place.TYPE_HAIR_CARE /*45*/:
                    jsonToken = _parseNegNumber();
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
                    jsonToken = _parsePosNumber(i);
                    break;
                case Place.TYPE_TAXI_STAND /*91*/:
                    if (!inObject) {
                        this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                    }
                    jsonToken = JsonToken.START_ARRAY;
                    break;
                case Place.TYPE_TRAVEL_AGENCY /*93*/:
                case 125:
                    _reportUnexpectedChar(i, "expected a value");
                    break;
                case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                    _matchFalse();
                    jsonToken = JsonToken.VALUE_FALSE;
                    break;
                case 110:
                    _matchNull();
                    jsonToken = JsonToken.VALUE_NULL;
                    break;
                case 116:
                    break;
                case 123:
                    if (!inObject) {
                        this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                    }
                    jsonToken = JsonToken.START_OBJECT;
                    break;
                default:
                    jsonToken = _handleOddValue(i);
                    break;
            }
            _matchTrue();
            jsonToken = JsonToken.VALUE_TRUE;
            if (inObject) {
                this._nextToken = jsonToken;
                return this._currToken;
            }
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
                        i = _skipComma(i);
                    }
                    if (this._parsingContext.inObject()) {
                        str = i == 34 ? _parseName() : _handleOddName(i);
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
                                    _matchFalse();
                                    t = JsonToken.VALUE_FALSE;
                                    break;
                                case 110:
                                    _matchNull();
                                    t = JsonToken.VALUE_NULL;
                                    break;
                                case 116:
                                    _matchTrue();
                                    t = JsonToken.VALUE_TRUE;
                                    break;
                                case 123:
                                    t = JsonToken.START_OBJECT;
                                    break;
                                default:
                                    t = _handleOddValue(i);
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
                jsonToken = _handleOddValue(i);
                this._currToken = jsonToken;
                return jsonToken;
        }
    }

    public final String nextTextValue() throws IOException {
        if (this._currToken == JsonToken.FIELD_NAME) {
            this._nameCopied = false;
            JsonToken t = this._nextToken;
            this._nextToken = null;
            this._currToken = t;
            if (t == JsonToken.VALUE_STRING) {
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                }
                return this._textBuffer.contentsAsString();
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

    public final int nextIntValue(int defaultValue) throws IOException {
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

    public final long nextLongValue(long defaultValue) throws IOException {
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

    public final Boolean nextBooleanValue() throws IOException {
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
        if (t == null) {
            return null;
        }
        int id = t.id();
        if (id == 9) {
            return Boolean.TRUE;
        }
        if (id == 10) {
            return Boolean.FALSE;
        }
        return null;
    }

    protected final JsonToken _parsePosNumber(int ch) throws IOException {
        int ptr = this._inputPtr;
        int startPtr = ptr - 1;
        int inputLen = this._inputEnd;
        if (ch == 48) {
            return _parseNumber2(false, startPtr);
        }
        int intLen = 1;
        int ptr2 = ptr;
        while (ptr2 < inputLen) {
            ptr = ptr2 + 1;
            ch = this._inputBuffer[ptr2];
            if (ch >= 48 && ch <= 57) {
                intLen++;
                ptr2 = ptr;
            } else if (ch == 46 || ch == 101 || ch == 69) {
                this._inputPtr = ptr;
                return _parseFloat(ch, startPtr, ptr, false, intLen);
            } else {
                ptr--;
                this._inputPtr = ptr;
                if (this._parsingContext.inRoot()) {
                    _verifyRootSpace(ch);
                }
                this._textBuffer.resetWithShared(this._inputBuffer, startPtr, ptr - startPtr);
                return resetInt(false, intLen);
            }
        }
        this._inputPtr = startPtr;
        ptr = ptr2;
        return _parseNumber2(false, startPtr);
    }

    private final JsonToken _parseFloat(int ch, int startPtr, int ptr, boolean neg, int intLen) throws IOException {
        int ptr2;
        int inputLen = this._inputEnd;
        int fractLen = 0;
        if (ch == 46) {
            ptr2 = ptr;
            while (ptr2 < inputLen) {
                ptr = ptr2 + 1;
                ch = this._inputBuffer[ptr2];
                if (ch >= 48 && ch <= 57) {
                    fractLen++;
                    ptr2 = ptr;
                } else if (fractLen == 0) {
                    reportUnexpectedNumberChar(ch, "Decimal point not followed by a digit");
                }
            }
            ptr = ptr2;
            return _parseNumber2(neg, startPtr);
        }
        ptr2 = ptr;
        int expLen = 0;
        if (ch == 101 || ch == 69) {
            if (ptr2 >= inputLen) {
                this._inputPtr = startPtr;
                ptr = ptr2;
                return _parseNumber2(neg, startPtr);
            }
            ptr = ptr2 + 1;
            ch = this._inputBuffer[ptr2];
            if (ch != 45 && ch != 43) {
                ptr2 = ptr;
            } else if (ptr >= inputLen) {
                this._inputPtr = startPtr;
                return _parseNumber2(neg, startPtr);
            } else {
                ptr2 = ptr + 1;
                ch = this._inputBuffer[ptr];
            }
            while (ch <= 57 && ch >= 48) {
                expLen++;
                if (ptr2 >= inputLen) {
                    this._inputPtr = startPtr;
                    ptr = ptr2;
                    return _parseNumber2(neg, startPtr);
                }
                ptr = ptr2 + 1;
                ch = this._inputBuffer[ptr2];
                ptr2 = ptr;
            }
            if (expLen == 0) {
                reportUnexpectedNumberChar(ch, "Exponent indicator not followed by a digit");
            }
        }
        ptr = ptr2 - 1;
        this._inputPtr = ptr;
        if (this._parsingContext.inRoot()) {
            _verifyRootSpace(ch);
        }
        this._textBuffer.resetWithShared(this._inputBuffer, startPtr, ptr - startPtr);
        return resetFloat(neg, intLen, fractLen, expLen);
    }

    protected final JsonToken _parseNegNumber() throws IOException {
        int ptr = this._inputPtr;
        int startPtr = ptr - 1;
        int inputLen = this._inputEnd;
        if (ptr >= inputLen) {
            return _parseNumber2(true, startPtr);
        }
        int ptr2 = ptr + 1;
        int ch = this._inputBuffer[ptr];
        if (ch > 57 || ch < 48) {
            this._inputPtr = ptr2;
            ptr = ptr2;
            return _handleInvalidNumberStart(ch, true);
        } else if (ch == 48) {
            ptr = ptr2;
            return _parseNumber2(true, startPtr);
        } else {
            int intLen = 1;
            while (ptr2 < inputLen) {
                ptr = ptr2 + 1;
                ch = this._inputBuffer[ptr2];
                if (ch >= 48 && ch <= 57) {
                    intLen++;
                    ptr2 = ptr;
                } else if (ch == 46 || ch == 101 || ch == 69) {
                    this._inputPtr = ptr;
                    return _parseFloat(ch, startPtr, ptr, true, intLen);
                } else {
                    ptr--;
                    this._inputPtr = ptr;
                    if (this._parsingContext.inRoot()) {
                        _verifyRootSpace(ch);
                    }
                    this._textBuffer.resetWithShared(this._inputBuffer, startPtr, ptr - startPtr);
                    return resetInt(true, intLen);
                }
            }
            ptr = ptr2;
            return _parseNumber2(true, startPtr);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.fasterxml.jackson.core.JsonToken _parseNumber2(boolean r12, int r13) throws java.io.IOException {
        /*
        r11 = this;
        if (r12 == 0) goto L_0x0004;
    L_0x0002:
        r13 = r13 + 1;
    L_0x0004:
        r11._inputPtr = r13;
        r8 = r11._textBuffer;
        r5 = r8.emptyAndGetCurrentSegment();
        r6 = 0;
        if (r12 == 0) goto L_0x0016;
    L_0x000f:
        r7 = r6 + 1;
        r8 = 45;
        r5[r6] = r8;
        r6 = r7;
    L_0x0016:
        r4 = 0;
        r8 = r11._inputPtr;
        r9 = r11._inputEnd;
        if (r8 >= r9) goto L_0x005e;
    L_0x001d:
        r8 = r11._inputBuffer;
        r9 = r11._inputPtr;
        r10 = r9 + 1;
        r11._inputPtr = r10;
        r0 = r8[r9];
    L_0x0027:
        r8 = 48;
        if (r0 != r8) goto L_0x002f;
    L_0x002b:
        r0 = r11._verifyNoLeadingZeroes();
    L_0x002f:
        r1 = 0;
    L_0x0030:
        r8 = 48;
        if (r0 < r8) goto L_0x0171;
    L_0x0034:
        r8 = 57;
        if (r0 > r8) goto L_0x0171;
    L_0x0038:
        r4 = r4 + 1;
        r8 = r5.length;
        if (r6 < r8) goto L_0x0044;
    L_0x003d:
        r8 = r11._textBuffer;
        r5 = r8.finishCurrentSegment();
        r6 = 0;
    L_0x0044:
        r7 = r6 + 1;
        r5[r6] = r0;
        r8 = r11._inputPtr;
        r9 = r11._inputEnd;
        if (r8 < r9) goto L_0x0065;
    L_0x004e:
        r8 = r11.loadMore();
        if (r8 != 0) goto L_0x0065;
    L_0x0054:
        r0 = 0;
        r1 = 1;
    L_0x0056:
        if (r4 != 0) goto L_0x0071;
    L_0x0058:
        r8 = r11._handleInvalidNumberStart(r0, r12);
        r6 = r7;
    L_0x005d:
        return r8;
    L_0x005e:
        r8 = "No digit following minus sign";
        r0 = r11.getNextChar(r8);
        goto L_0x0027;
    L_0x0065:
        r8 = r11._inputBuffer;
        r9 = r11._inputPtr;
        r10 = r9 + 1;
        r11._inputPtr = r10;
        r0 = r8[r9];
        r6 = r7;
        goto L_0x0030;
    L_0x0071:
        r3 = 0;
        r8 = 46;
        if (r0 != r8) goto L_0x016e;
    L_0x0076:
        r6 = r7 + 1;
        r5[r7] = r0;
    L_0x007a:
        r8 = r11._inputPtr;
        r9 = r11._inputEnd;
        if (r8 < r9) goto L_0x0127;
    L_0x0080:
        r8 = r11.loadMore();
        if (r8 != 0) goto L_0x0127;
    L_0x0086:
        r1 = 1;
    L_0x0087:
        if (r3 != 0) goto L_0x008e;
    L_0x0089:
        r8 = "Decimal point not followed by a digit";
        r11.reportUnexpectedNumberChar(r0, r8);
    L_0x008e:
        r2 = 0;
        r8 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r0 == r8) goto L_0x0097;
    L_0x0093:
        r8 = 69;
        if (r0 != r8) goto L_0x0109;
    L_0x0097:
        r8 = r5.length;
        if (r6 < r8) goto L_0x00a1;
    L_0x009a:
        r8 = r11._textBuffer;
        r5 = r8.finishCurrentSegment();
        r6 = 0;
    L_0x00a1:
        r7 = r6 + 1;
        r5[r6] = r0;
        r8 = r11._inputPtr;
        r9 = r11._inputEnd;
        if (r8 >= r9) goto L_0x014c;
    L_0x00ab:
        r8 = r11._inputBuffer;
        r9 = r11._inputPtr;
        r10 = r9 + 1;
        r11._inputPtr = r10;
        r0 = r8[r9];
    L_0x00b5:
        r8 = 45;
        if (r0 == r8) goto L_0x00bd;
    L_0x00b9:
        r8 = 43;
        if (r0 != r8) goto L_0x016b;
    L_0x00bd:
        r8 = r5.length;
        if (r7 < r8) goto L_0x0168;
    L_0x00c0:
        r8 = r11._textBuffer;
        r5 = r8.finishCurrentSegment();
        r6 = 0;
    L_0x00c7:
        r7 = r6 + 1;
        r5[r6] = r0;
        r8 = r11._inputPtr;
        r9 = r11._inputEnd;
        if (r8 >= r9) goto L_0x0154;
    L_0x00d1:
        r8 = r11._inputBuffer;
        r9 = r11._inputPtr;
        r10 = r9 + 1;
        r11._inputPtr = r10;
        r0 = r8[r9];
    L_0x00db:
        r6 = r7;
    L_0x00dc:
        r8 = 57;
        if (r0 > r8) goto L_0x0102;
    L_0x00e0:
        r8 = 48;
        if (r0 < r8) goto L_0x0102;
    L_0x00e4:
        r2 = r2 + 1;
        r8 = r5.length;
        if (r6 < r8) goto L_0x00f0;
    L_0x00e9:
        r8 = r11._textBuffer;
        r5 = r8.finishCurrentSegment();
        r6 = 0;
    L_0x00f0:
        r7 = r6 + 1;
        r5[r6] = r0;
        r8 = r11._inputPtr;
        r9 = r11._inputEnd;
        if (r8 < r9) goto L_0x015b;
    L_0x00fa:
        r8 = r11.loadMore();
        if (r8 != 0) goto L_0x015b;
    L_0x0100:
        r1 = 1;
        r6 = r7;
    L_0x0102:
        if (r2 != 0) goto L_0x0109;
    L_0x0104:
        r8 = "Exponent indicator not followed by a digit";
        r11.reportUnexpectedNumberChar(r0, r8);
    L_0x0109:
        if (r1 != 0) goto L_0x011c;
    L_0x010b:
        r8 = r11._inputPtr;
        r8 = r8 + -1;
        r11._inputPtr = r8;
        r8 = r11._parsingContext;
        r8 = r8.inRoot();
        if (r8 == 0) goto L_0x011c;
    L_0x0119:
        r11._verifyRootSpace(r0);
    L_0x011c:
        r8 = r11._textBuffer;
        r8.setCurrentLength(r6);
        r8 = r11.reset(r12, r4, r3, r2);
        goto L_0x005d;
    L_0x0127:
        r8 = r11._inputBuffer;
        r9 = r11._inputPtr;
        r10 = r9 + 1;
        r11._inputPtr = r10;
        r0 = r8[r9];
        r8 = 48;
        if (r0 < r8) goto L_0x0087;
    L_0x0135:
        r8 = 57;
        if (r0 > r8) goto L_0x0087;
    L_0x0139:
        r3 = r3 + 1;
        r8 = r5.length;
        if (r6 < r8) goto L_0x0145;
    L_0x013e:
        r8 = r11._textBuffer;
        r5 = r8.finishCurrentSegment();
        r6 = 0;
    L_0x0145:
        r7 = r6 + 1;
        r5[r6] = r0;
        r6 = r7;
        goto L_0x007a;
    L_0x014c:
        r8 = "expected a digit for number exponent";
        r0 = r11.getNextChar(r8);
        goto L_0x00b5;
    L_0x0154:
        r8 = "expected a digit for number exponent";
        r0 = r11.getNextChar(r8);
        goto L_0x00db;
    L_0x015b:
        r8 = r11._inputBuffer;
        r9 = r11._inputPtr;
        r10 = r9 + 1;
        r11._inputPtr = r10;
        r0 = r8[r9];
        r6 = r7;
        goto L_0x00dc;
    L_0x0168:
        r6 = r7;
        goto L_0x00c7;
    L_0x016b:
        r6 = r7;
        goto L_0x00dc;
    L_0x016e:
        r6 = r7;
        goto L_0x008e;
    L_0x0171:
        r7 = r6;
        goto L_0x0056;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.ReaderBasedJsonParser._parseNumber2(boolean, int):com.fasterxml.jackson.core.JsonToken");
    }

    private final char _verifyNoLeadingZeroes() throws IOException {
        if (this._inputPtr < this._inputEnd) {
            char ch = this._inputBuffer[this._inputPtr];
            if (ch < '0' || ch > '9') {
                return '0';
            }
        }
        return _verifyNLZ2();
    }

    private char _verifyNLZ2() throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            return '0';
        }
        char ch = this._inputBuffer[this._inputPtr];
        if (ch < '0' || ch > '9') {
            return '0';
        }
        if (!isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
            reportInvalidNumber("Leading zeroes not allowed");
        }
        this._inputPtr++;
        if (ch != '0') {
            return ch;
        }
        do {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                return ch;
            }
            ch = this._inputBuffer[this._inputPtr];
            if (ch < '0' || ch > '9') {
                return '0';
            }
            this._inputPtr++;
        } while (ch == '0');
        return ch;
    }

    protected JsonToken _handleInvalidNumberStart(int ch, boolean negative) throws IOException {
        double d = Double.NEGATIVE_INFINITY;
        if (ch == 73) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOFInValue();
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = cArr[i];
            String match;
            if (ch == 78) {
                match = negative ? "-INF" : "+INF";
                _matchToken(match, 3);
                if (isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    if (!negative) {
                        d = Double.POSITIVE_INFINITY;
                    }
                    return resetAsNaN(match, d);
                }
                _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
            } else if (ch == 110) {
                match = negative ? "-Infinity" : "+Infinity";
                _matchToken(match, 3);
                if (isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    if (!negative) {
                        d = Double.POSITIVE_INFINITY;
                    }
                    return resetAsNaN(match, d);
                }
                _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
            }
        }
        reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
        return null;
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

    protected final String _parseName() throws IOException {
        int start;
        int ptr = this._inputPtr;
        int hash = this._hashSeed;
        int[] codes = _icLatin1;
        while (ptr < this._inputEnd) {
            int ch = this._inputBuffer[ptr];
            if (ch >= codes.length || codes[ch] == 0) {
                hash = (hash * 33) + ch;
                ptr++;
            } else {
                if (ch == 34) {
                    start = this._inputPtr;
                    this._inputPtr = ptr + 1;
                    return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
                }
                start = this._inputPtr;
                this._inputPtr = ptr;
                return _parseName2(start, hash, 34);
            }
        }
        start = this._inputPtr;
        this._inputPtr = ptr;
        return _parseName2(start, hash, 34);
    }

    private String _parseName2(int startPtr, int hash, int endChar) throws IOException {
        this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
        char[] outBuf = this._textBuffer.getCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(": was expecting closing '" + ((char) endChar) + "' for name");
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = cArr[i];
            char i2 = c;
            if (i2 <= '\\') {
                if (i2 == '\\') {
                    c = _decodeEscaped();
                } else if (i2 <= endChar) {
                    if (i2 == endChar) {
                        this._textBuffer.setCurrentLength(outPtr);
                        TextBuffer tb = this._textBuffer;
                        return this._symbols.findSymbol(tb.getTextBuffer(), tb.getTextOffset(), tb.size(), hash);
                    } else if (i2 < ' ') {
                        _throwUnquotedSpace(i2, Twitter.NAME);
                    }
                }
            }
            hash = (hash * 33) + c;
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            if (outPtr2 >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            } else {
                outPtr = outPtr2;
            }
        }
    }

    protected String _handleOddName(int i) throws IOException {
        if (i == 39 && isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            return _parseAposName();
        }
        int start;
        if (!isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            _reportUnexpectedChar(i, "was expecting double-quote to start field name");
        }
        int[] codes = CharTypes.getInputCodeLatin1JsNames();
        int maxCode = codes.length;
        boolean firstOk = i < maxCode ? codes[i] == 0 : Character.isJavaIdentifierPart((char) i);
        if (!firstOk) {
            _reportUnexpectedChar(i, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int ptr = this._inputPtr;
        int hash = this._hashSeed;
        int inputLen = this._inputEnd;
        if (ptr < inputLen) {
            do {
                int ch = this._inputBuffer[ptr];
                if (ch < maxCode) {
                    if (codes[ch] != 0) {
                        start = this._inputPtr - 1;
                        this._inputPtr = ptr;
                        return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
                    }
                } else if (!Character.isJavaIdentifierPart((char) ch)) {
                    start = this._inputPtr - 1;
                    this._inputPtr = ptr;
                    return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
                }
                hash = (hash * 33) + ch;
                ptr++;
            } while (ptr < inputLen);
        }
        start = this._inputPtr - 1;
        this._inputPtr = ptr;
        return _handleOddName2(start, hash, codes);
    }

    protected String _parseAposName() throws IOException {
        int start;
        int ptr = this._inputPtr;
        int hash = this._hashSeed;
        int inputLen = this._inputEnd;
        if (ptr < inputLen) {
            int[] codes = _icLatin1;
            int maxCode = codes.length;
            do {
                int ch = this._inputBuffer[ptr];
                if (ch != 39) {
                    if (ch < maxCode && codes[ch] != 0) {
                        break;
                    }
                    hash = (hash * 33) + ch;
                    ptr++;
                } else {
                    start = this._inputPtr;
                    this._inputPtr = ptr + 1;
                    return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
                }
            } while (ptr < inputLen);
        }
        start = this._inputPtr;
        this._inputPtr = ptr;
        return _parseName2(start, hash, 39);
    }

    protected JsonToken _handleOddValue(int i) throws IOException {
        switch (i) {
            case Place.TYPE_FUNERAL_HOME /*39*/:
                if (isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
                    return _handleApos();
                }
                break;
            case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    _reportInvalidEOFInValue();
                }
                char[] cArr = this._inputBuffer;
                int i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                return _handleInvalidNumberStart(cArr[i2], false);
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
        }
        if (Character.isJavaIdentifierStart(i)) {
            _reportInvalidToken(BuildConfig.FLAVOR + ((char) i), "('true', 'false' or 'null')");
        }
        _reportUnexpectedChar(i, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        return null;
    }

    protected JsonToken _handleApos() throws IOException {
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(": was expecting closing quote for a string value");
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = cArr[i];
            char i2 = c;
            if (i2 <= '\\') {
                if (i2 == '\\') {
                    c = _decodeEscaped();
                } else if (i2 <= '\'') {
                    if (i2 == '\'') {
                        this._textBuffer.setCurrentLength(outPtr);
                        return JsonToken.VALUE_STRING;
                    } else if (i2 < ' ') {
                        _throwUnquotedSpace(i2, "string value");
                    }
                }
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            outPtr = outPtr2;
        }
    }

    private String _handleOddName2(int startPtr, int hash, int[] codes) throws IOException {
        this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
        char[] outBuf = this._textBuffer.getCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        char maxCode = codes.length;
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            char c = this._inputBuffer[this._inputPtr];
            char i = c;
            if (i > maxCode) {
                if (!Character.isJavaIdentifierPart(c)) {
                    break;
                }
            } else if (codes[i] != 0) {
                break;
            }
            this._inputPtr++;
            hash = (hash * 33) + i;
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            if (outPtr2 >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            } else {
                outPtr = outPtr2;
            }
        }
        this._textBuffer.setCurrentLength(outPtr);
        TextBuffer tb = this._textBuffer;
        return this._symbols.findSymbol(tb.getTextBuffer(), tb.getTextOffset(), tb.size(), hash);
    }

    protected final void _finishString() throws IOException {
        int ptr = this._inputPtr;
        int inputLen = this._inputEnd;
        if (ptr < inputLen) {
            int[] codes = _icLatin1;
            int maxCode = codes.length;
            do {
                int ch = this._inputBuffer[ptr];
                if (ch >= maxCode || codes[ch] == 0) {
                    ptr++;
                } else if (ch == 34) {
                    this._textBuffer.resetWithShared(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
                    this._inputPtr = ptr + 1;
                    return;
                }
            } while (ptr < inputLen);
        }
        this._textBuffer.resetWithCopy(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
        this._inputPtr = ptr;
        _finishString2();
    }

    protected void _finishString2() throws IOException {
        char[] outBuf = this._textBuffer.getCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        int[] codes = _icLatin1;
        char maxCode = codes.length;
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(": was expecting closing quote for a string value");
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = cArr[i];
            char i2 = c;
            if (i2 < maxCode && codes[i2] != 0) {
                if (i2 == '\"') {
                    this._textBuffer.setCurrentLength(outPtr);
                    return;
                } else if (i2 == '\\') {
                    c = _decodeEscaped();
                } else if (i2 < ' ') {
                    _throwUnquotedSpace(i2, "string value");
                }
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            outPtr = outPtr2;
        }
    }

    protected final void _skipString() throws IOException {
        this._tokenIncomplete = false;
        int inPtr = this._inputPtr;
        int inLen = this._inputEnd;
        char[] inBuf = this._inputBuffer;
        while (true) {
            if (inPtr >= inLen) {
                this._inputPtr = inPtr;
                if (!loadMore()) {
                    _reportInvalidEOF(": was expecting closing quote for a string value");
                }
                inPtr = this._inputPtr;
                inLen = this._inputEnd;
            }
            int inPtr2 = inPtr + 1;
            char i = inBuf[inPtr];
            if (i <= '\\') {
                if (i == '\\') {
                    this._inputPtr = inPtr2;
                    char c = _decodeEscaped();
                    inPtr = this._inputPtr;
                    inLen = this._inputEnd;
                } else if (i <= '\"') {
                    if (i == '\"') {
                        this._inputPtr = inPtr2;
                        return;
                    } else if (i < ' ') {
                        this._inputPtr = inPtr2;
                        _throwUnquotedSpace(i, "string value");
                    }
                }
            }
            inPtr = inPtr2;
        }
    }

    protected final void _skipCR() throws IOException {
        if ((this._inputPtr < this._inputEnd || loadMore()) && this._inputBuffer[this._inputPtr] == '\n') {
            this._inputPtr++;
        }
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    private final int _skipColon() throws IOException {
        if (this._inputPtr + 4 >= this._inputEnd) {
            return _skipColon2(false);
        }
        char c = this._inputBuffer[this._inputPtr];
        char[] cArr;
        int i;
        int i2;
        if (c == ':') {
            cArr = this._inputBuffer;
            i = this._inputPtr + 1;
            this._inputPtr = i;
            i2 = cArr[i];
            if (i2 <= 32) {
                if (i2 == 32 || i2 == 9) {
                    cArr = this._inputBuffer;
                    i = this._inputPtr + 1;
                    this._inputPtr = i;
                    i2 = cArr[i];
                    if (i2 > 32) {
                        if (i2 == 47 || i2 == 35) {
                            return _skipColon2(true);
                        }
                        this._inputPtr++;
                        return i2;
                    }
                }
                return _skipColon2(true);
            } else if (i2 == 47 || i2 == 35) {
                return _skipColon2(true);
            } else {
                this._inputPtr++;
                return i2;
            }
        }
        if (c == ' ' || c == '\t') {
            cArr = this._inputBuffer;
            i = this._inputPtr + 1;
            this._inputPtr = i;
            c = cArr[i];
        }
        if (c != ':') {
            return _skipColon2(false);
        }
        cArr = this._inputBuffer;
        i = this._inputPtr + 1;
        this._inputPtr = i;
        i2 = cArr[i];
        if (i2 <= 32) {
            if (i2 == 32 || i2 == 9) {
                cArr = this._inputBuffer;
                i = this._inputPtr + 1;
                this._inputPtr = i;
                i2 = cArr[i];
                if (i2 > 32) {
                    if (i2 == 47 || i2 == 35) {
                        return _skipColon2(true);
                    }
                    this._inputPtr++;
                    return i2;
                }
            }
            return _skipColon2(true);
        } else if (i2 == 47 || i2 == 35) {
            return _skipColon2(true);
        } else {
            this._inputPtr++;
            return i2;
        }
    }

    private final int _skipColon2(boolean gotColon) throws IOException {
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = cArr[i];
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
            } else if (i2 < 32) {
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

    private final int _skipComma(int i) throws IOException {
        if (i != 44) {
            _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
        }
        while (this._inputPtr < this._inputEnd) {
            char[] cArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            i = cArr[i2];
            if (i > 32) {
                if (i != 47 && i != 35) {
                    return i;
                }
                this._inputPtr--;
                return _skipAfterComma2();
            } else if (i < 32) {
                if (i == 10) {
                    this._currInputRow++;
                    this._currInputRowStart = this._inputPtr;
                } else if (i == 13) {
                    _skipCR();
                } else if (i != 9) {
                    _throwInvalidSpace(i);
                }
            }
        }
        return _skipAfterComma2();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int _skipAfterComma2() throws java.io.IOException {
        /*
        r5 = this;
        r4 = 32;
    L_0x0002:
        r1 = r5._inputPtr;
        r2 = r5._inputEnd;
        if (r1 < r2) goto L_0x000e;
    L_0x0008:
        r1 = r5.loadMore();
        if (r1 == 0) goto L_0x004e;
    L_0x000e:
        r1 = r5._inputBuffer;
        r2 = r5._inputPtr;
        r3 = r2 + 1;
        r5._inputPtr = r3;
        r0 = r1[r2];
        if (r0 <= r4) goto L_0x002d;
    L_0x001a:
        r1 = 47;
        if (r0 != r1) goto L_0x0022;
    L_0x001e:
        r5._skipComment();
        goto L_0x0002;
    L_0x0022:
        r1 = 35;
        if (r0 != r1) goto L_0x002c;
    L_0x0026:
        r1 = r5._skipYAMLComment();
        if (r1 != 0) goto L_0x0002;
    L_0x002c:
        return r0;
    L_0x002d:
        if (r0 >= r4) goto L_0x0002;
    L_0x002f:
        r1 = 10;
        if (r0 != r1) goto L_0x003e;
    L_0x0033:
        r1 = r5._currInputRow;
        r1 = r1 + 1;
        r5._currInputRow = r1;
        r1 = r5._inputPtr;
        r5._currInputRowStart = r1;
        goto L_0x0002;
    L_0x003e:
        r1 = 13;
        if (r0 != r1) goto L_0x0046;
    L_0x0042:
        r5._skipCR();
        goto L_0x0002;
    L_0x0046:
        r1 = 9;
        if (r0 == r1) goto L_0x0002;
    L_0x004a:
        r5._throwInvalidSpace(r0);
        goto L_0x0002;
    L_0x004e:
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
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.ReaderBasedJsonParser._skipAfterComma2():int");
    }

    private final int _skipWSOrEnd() throws IOException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            return _eofAsNextChar();
        }
        char[] cArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = cArr[i];
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
                cArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = cArr[i];
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

    private int _skipWSOrEnd2() throws IOException {
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                return _eofAsNextChar();
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = cArr[i];
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

    private void _skipComment() throws IOException {
        if (!isEnabled(Feature.ALLOW_COMMENTS)) {
            _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in a comment");
        }
        char[] cArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        char c = cArr[i];
        if (c == IOUtils.DIR_SEPARATOR_UNIX) {
            _skipLine();
        } else if (c == '*') {
            _skipCComment();
        } else {
            _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
        }
    }

    private void _skipCComment() throws IOException {
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = cArr[i];
            if (i2 <= 42) {
                if (i2 == 42) {
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        break;
                    } else if (this._inputBuffer[this._inputPtr] == IOUtils.DIR_SEPARATOR_UNIX) {
                        this._inputPtr++;
                        return;
                    }
                } else if (i2 < 32) {
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
        _reportInvalidEOF(" in a comment");
    }

    private boolean _skipYAMLComment() throws IOException {
        if (!isEnabled(Feature.ALLOW_YAML_COMMENTS)) {
            return false;
        }
        _skipLine();
        return true;
    }

    private void _skipLine() throws IOException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                char[] cArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = cArr[i];
                if (i2 < 32) {
                    if (i2 == 10) {
                        this._currInputRow++;
                        this._currInputRowStart = this._inputPtr;
                        return;
                    } else if (i2 == 13) {
                        _skipCR();
                        return;
                    } else if (i2 != 9) {
                        _throwInvalidSpace(i2);
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
        char[] cArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        char c = cArr[i];
        switch (c) {
            case Place.TYPE_ESTABLISHMENT /*34*/:
            case Place.TYPE_HEALTH /*47*/:
            case Place.TYPE_TRAIN_STATION /*92*/:
                return c;
            case 'b':
                return '\b';
            case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            case 'u':
                int value = 0;
                for (int i2 = 0; i2 < 4; i2++) {
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        _reportInvalidEOF(" in character escape sequence");
                    }
                    cArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    int ch = cArr[i];
                    int digit = CharTypes.charToHex(ch);
                    if (digit < 0) {
                        _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
                    }
                    value = (value << 4) | digit;
                }
                return (char) value;
            default:
                return _handleUnrecognizedCharacterEscape(c);
        }
    }

    private final void _matchTrue() throws IOException {
        int ptr = this._inputPtr;
        if (ptr + 3 < this._inputEnd) {
            char[] b = this._inputBuffer;
            if (b[ptr] == 'r') {
                ptr++;
                if (b[ptr] == 'u') {
                    ptr++;
                    if (b[ptr] == 'e') {
                        ptr++;
                        char c = b[ptr];
                        if (c < '0' || c == ']' || c == '}') {
                            this._inputPtr = ptr;
                            return;
                        }
                    }
                }
            }
        }
        _matchToken("true", 1);
    }

    private final void _matchFalse() throws IOException {
        int ptr = this._inputPtr;
        if (ptr + 4 < this._inputEnd) {
            char[] b = this._inputBuffer;
            if (b[ptr] == 'a') {
                ptr++;
                if (b[ptr] == 'l') {
                    ptr++;
                    if (b[ptr] == 's') {
                        ptr++;
                        if (b[ptr] == 'e') {
                            ptr++;
                            char c = b[ptr];
                            if (c < '0' || c == ']' || c == '}') {
                                this._inputPtr = ptr;
                                return;
                            }
                        }
                    }
                }
            }
        }
        _matchToken("false", 1);
    }

    private final void _matchNull() throws IOException {
        int ptr = this._inputPtr;
        if (ptr + 3 < this._inputEnd) {
            char[] b = this._inputBuffer;
            if (b[ptr] == 'u') {
                ptr++;
                if (b[ptr] == 'l') {
                    ptr++;
                    if (b[ptr] == 'l') {
                        ptr++;
                        char c = b[ptr];
                        if (c < '0' || c == ']' || c == '}') {
                            this._inputPtr = ptr;
                            return;
                        }
                    }
                }
            }
        }
        _matchToken("null", 1);
    }

    protected final void _matchToken(String matchStr, int i) throws IOException {
        int len = matchStr.length();
        do {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidToken(matchStr.substring(0, i));
            }
            if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
                _reportInvalidToken(matchStr.substring(0, i));
            }
            this._inputPtr++;
            i++;
        } while (i < len);
        if (this._inputPtr < this._inputEnd || loadMore()) {
            char c = this._inputBuffer[this._inputPtr];
            if (c >= '0' && c != ']' && c != '}' && Character.isJavaIdentifierPart(c)) {
                _reportInvalidToken(matchStr.substring(0, i));
            }
        }
    }

    protected byte[] _decodeBase64(Base64Variant b64variant) throws IOException {
        char ch;
        ByteArrayBuilder builder = _getByteArrayBuilder();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = cArr[i];
            if (ch > ' ') {
                int bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    if (ch == '\"') {
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
                cArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                ch = cArr[i];
                bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    bits = _decodeBase64Escape(b64variant, ch, 1);
                }
                decodedData = (decodedData << 6) | bits;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                cArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                ch = cArr[i];
                bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    if (bits != -2) {
                        if (ch != '\"' || b64variant.usesPadding()) {
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
                        cArr = this._inputBuffer;
                        i = this._inputPtr;
                        this._inputPtr = i + 1;
                        ch = cArr[i];
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
                cArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                ch = cArr[i];
                bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    if (bits != -2) {
                        if (ch != '\"' || b64variant.usesPadding()) {
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

    protected void _reportInvalidToken(String matchedPart) throws IOException {
        _reportInvalidToken(matchedPart, "'null', 'true', 'false' or NaN");
    }

    protected void _reportInvalidToken(String matchedPart, String msg) throws IOException {
        StringBuilder sb = new StringBuilder(matchedPart);
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            char c = this._inputBuffer[this._inputPtr];
            if (!Character.isJavaIdentifierPart(c)) {
                break;
            }
            this._inputPtr++;
            sb.append(c);
        }
        _reportError("Unrecognized token '" + sb.toString() + "': was expecting " + msg);
    }
}
