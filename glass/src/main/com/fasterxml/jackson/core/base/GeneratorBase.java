package com.fasterxml.jackson.core.base;

import android.support.v4.media.TransportMediator;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import java.io.IOException;
import java.io.InputStream;

public abstract class GeneratorBase extends JsonGenerator {
    protected static final int DERIVED_FEATURES_MASK;
    public static final int SURR1_FIRST = 55296;
    public static final int SURR1_LAST = 56319;
    public static final int SURR2_FIRST = 56320;
    public static final int SURR2_LAST = 57343;
    protected final String WRITE_BINARY;
    protected final String WRITE_BOOLEAN;
    protected final String WRITE_NULL;
    protected final String WRITE_NUMBER;
    protected final String WRITE_RAW;
    protected final String WRITE_STRING;
    protected boolean _cfgNumbersAsStrings;
    protected boolean _closed;
    protected int _features;
    protected ObjectCodec _objectCodec;
    protected JsonWriteContext _writeContext;

    protected abstract void _releaseBuffers();

    protected abstract void _verifyValueWrite(String str) throws IOException;

    public abstract void flush() throws IOException;

    static {
        DERIVED_FEATURES_MASK = (Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask()) | Feature.STRICT_DUPLICATE_DETECTION.getMask();
    }

    protected GeneratorBase(int features, ObjectCodec codec) {
        this.WRITE_BINARY = "write a binary value";
        this.WRITE_BOOLEAN = "write a boolean value";
        this.WRITE_NULL = "write a null";
        this.WRITE_NUMBER = "write a number";
        this.WRITE_RAW = "write a raw (unencoded) value";
        this.WRITE_STRING = "write a string";
        this._features = features;
        this._objectCodec = codec;
        this._writeContext = JsonWriteContext.createRootContext(Feature.STRICT_DUPLICATE_DETECTION.enabledIn(features) ? DupDetector.rootDetector((JsonGenerator) this) : null);
        this._cfgNumbersAsStrings = Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(features);
    }

    protected GeneratorBase(int features, ObjectCodec codec, JsonWriteContext ctxt) {
        this.WRITE_BINARY = "write a binary value";
        this.WRITE_BOOLEAN = "write a boolean value";
        this.WRITE_NULL = "write a null";
        this.WRITE_NUMBER = "write a number";
        this.WRITE_RAW = "write a raw (unencoded) value";
        this.WRITE_STRING = "write a string";
        this._features = features;
        this._objectCodec = codec;
        this._writeContext = ctxt;
        this._cfgNumbersAsStrings = Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(features);
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public Object getCurrentValue() {
        return this._writeContext.getCurrentValue();
    }

    public void setCurrentValue(Object v) {
        this._writeContext.setCurrentValue(v);
    }

    public final boolean isEnabled(Feature f) {
        return (this._features & f.getMask()) != 0;
    }

    public int getFeatureMask() {
        return this._features;
    }

    public JsonGenerator enable(Feature f) {
        int mask = f.getMask();
        this._features |= mask;
        if ((DERIVED_FEATURES_MASK & mask) != 0) {
            if (f == Feature.WRITE_NUMBERS_AS_STRINGS) {
                this._cfgNumbersAsStrings = true;
            } else if (f == Feature.ESCAPE_NON_ASCII) {
                setHighestNonEscapedChar(TransportMediator.KEYCODE_MEDIA_PAUSE);
            } else if (f == Feature.STRICT_DUPLICATE_DETECTION && this._writeContext.getDupDetector() == null) {
                this._writeContext = this._writeContext.withDupDetector(DupDetector.rootDetector((JsonGenerator) this));
            }
        }
        return this;
    }

    public JsonGenerator disable(Feature f) {
        int mask = f.getMask();
        this._features &= mask ^ -1;
        if ((DERIVED_FEATURES_MASK & mask) != 0) {
            if (f == Feature.WRITE_NUMBERS_AS_STRINGS) {
                this._cfgNumbersAsStrings = false;
            } else if (f == Feature.ESCAPE_NON_ASCII) {
                setHighestNonEscapedChar(DERIVED_FEATURES_MASK);
            } else if (f == Feature.STRICT_DUPLICATE_DETECTION) {
                this._writeContext = this._writeContext.withDupDetector(null);
            }
        }
        return this;
    }

    public JsonGenerator setFeatureMask(int newMask) {
        int changed = newMask ^ this._features;
        this._features = newMask;
        if ((DERIVED_FEATURES_MASK & changed) != 0) {
            this._cfgNumbersAsStrings = Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(newMask);
            if (Feature.ESCAPE_NON_ASCII.enabledIn(changed)) {
                if (Feature.ESCAPE_NON_ASCII.enabledIn(newMask)) {
                    setHighestNonEscapedChar(TransportMediator.KEYCODE_MEDIA_PAUSE);
                } else {
                    setHighestNonEscapedChar(DERIVED_FEATURES_MASK);
                }
            }
            if (Feature.STRICT_DUPLICATE_DETECTION.enabledIn(changed)) {
                if (!Feature.STRICT_DUPLICATE_DETECTION.enabledIn(newMask)) {
                    this._writeContext = this._writeContext.withDupDetector(null);
                } else if (this._writeContext.getDupDetector() == null) {
                    this._writeContext = this._writeContext.withDupDetector(DupDetector.rootDetector((JsonGenerator) this));
                }
            }
        }
        return this;
    }

    public JsonGenerator useDefaultPrettyPrinter() {
        return getPrettyPrinter() != null ? this : setPrettyPrinter(_constructDefaultPrettyPrinter());
    }

    public JsonGenerator setCodec(ObjectCodec oc) {
        this._objectCodec = oc;
        return this;
    }

    public final ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public final JsonWriteContext getOutputContext() {
        return this._writeContext;
    }

    public void writeFieldName(SerializableString name) throws IOException {
        writeFieldName(name.getValue());
    }

    public void writeString(SerializableString text) throws IOException {
        writeString(text.getValue());
    }

    public void writeRawValue(String text) throws IOException {
        _verifyValueWrite("write raw value");
        writeRaw(text);
    }

    public void writeRawValue(String text, int offset, int len) throws IOException {
        _verifyValueWrite("write raw value");
        writeRaw(text, offset, len);
    }

    public void writeRawValue(char[] text, int offset, int len) throws IOException {
        _verifyValueWrite("write raw value");
        writeRaw(text, offset, len);
    }

    public void writeRawValue(SerializableString text) throws IOException {
        _verifyValueWrite("write raw value");
        writeRaw(text);
    }

    public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) throws IOException {
        _reportUnsupportedOperation();
        return DERIVED_FEATURES_MASK;
    }

    public void writeObject(Object value) throws IOException {
        if (value == null) {
            writeNull();
        } else if (this._objectCodec != null) {
            this._objectCodec.writeValue(this, value);
        } else {
            _writeSimpleObject(value);
        }
    }

    public void writeTree(TreeNode rootNode) throws IOException {
        if (rootNode == null) {
            writeNull();
        } else if (this._objectCodec == null) {
            throw new IllegalStateException("No ObjectCodec defined");
        } else {
            this._objectCodec.writeValue(this, rootNode);
        }
    }

    public void close() throws IOException {
        this._closed = true;
    }

    public boolean isClosed() {
        return this._closed;
    }

    protected PrettyPrinter _constructDefaultPrettyPrinter() {
        return new DefaultPrettyPrinter();
    }

    protected final int _decodeSurrogate(int surr1, int surr2) throws IOException {
        if (surr2 < SURR2_FIRST || surr2 > SURR2_LAST) {
            _reportError("Incomplete surrogate pair: first char 0x" + Integer.toHexString(surr1) + ", second 0x" + Integer.toHexString(surr2));
        }
        return (AccessibilityNodeInfoCompat.ACTION_CUT + ((surr1 - SURR1_FIRST) << 10)) + (surr2 - SURR2_FIRST);
    }
}
