package com.fasterxml.jackson.core.filter;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import spacemadness.com.lunarconsole.C1391R;

public class FilteringParserDelegate extends JsonParserDelegate {
    protected boolean _allowMultipleMatches;
    protected JsonToken _currToken;
    protected TokenFilterContext _exposedContext;
    protected TokenFilterContext _headContext;
    @Deprecated
    protected boolean _includeImmediateParent;
    protected boolean _includePath;
    protected TokenFilter _itemFilter;
    protected JsonToken _lastClearedToken;
    protected int _matchCount;
    protected TokenFilter rootFilter;

    public FilteringParserDelegate(JsonParser p, TokenFilter f, boolean includePath, boolean allowMultipleMatches) {
        super(p);
        this._includeImmediateParent = false;
        this.rootFilter = f;
        this._itemFilter = f;
        this._headContext = TokenFilterContext.createRootContext(f);
        this._includePath = includePath;
        this._allowMultipleMatches = allowMultipleMatches;
    }

    public TokenFilter getFilter() {
        return this.rootFilter;
    }

    public int getMatchCount() {
        return this._matchCount;
    }

    public JsonToken getCurrentToken() {
        return this._currToken;
    }

    public final int getCurrentTokenId() {
        JsonToken t = this._currToken;
        return t == null ? 0 : t.id();
    }

    public boolean hasCurrentToken() {
        return this._currToken != null;
    }

    public boolean hasTokenId(int id) {
        JsonToken t = this._currToken;
        if (t == null) {
            if (id == 0) {
                return true;
            }
            return false;
        } else if (t.id() != id) {
            return false;
        } else {
            return true;
        }
    }

    public final boolean hasToken(JsonToken t) {
        return this._currToken == t;
    }

    public boolean isExpectedStartArrayToken() {
        return this._currToken == JsonToken.START_ARRAY;
    }

    public boolean isExpectedStartObjectToken() {
        return this._currToken == JsonToken.START_OBJECT;
    }

    public JsonLocation getCurrentLocation() {
        return this.delegate.getCurrentLocation();
    }

    public JsonStreamContext getParsingContext() {
        return _filterContext();
    }

    public String getCurrentName() throws IOException {
        JsonStreamContext ctxt = _filterContext();
        if (this._currToken != JsonToken.START_OBJECT && this._currToken != JsonToken.START_ARRAY) {
            return ctxt.getCurrentName();
        }
        JsonStreamContext parent = ctxt.getParent();
        return parent == null ? null : parent.getCurrentName();
    }

    public void clearCurrentToken() {
        if (this._currToken != null) {
            this._lastClearedToken = this._currToken;
            this._currToken = null;
        }
    }

    public JsonToken getLastClearedToken() {
        return this._lastClearedToken;
    }

    public void overrideCurrentName(String name) {
        throw new UnsupportedOperationException("Can not currently override name during filtering read");
    }

    public JsonToken nextToken() throws IOException {
        JsonToken t;
        TokenFilterContext ctxt = this._exposedContext;
        if (ctxt != null) {
            do {
                t = ctxt.nextTokenToRead();
                if (t != null) {
                    this._currToken = t;
                    return t;
                } else if (ctxt == this._headContext) {
                    this._exposedContext = null;
                    if (ctxt.inArray()) {
                        t = this.delegate.getCurrentToken();
                        this._currToken = t;
                        return t;
                    }
                } else {
                    ctxt = this._headContext.findChildOf(ctxt);
                    this._exposedContext = ctxt;
                }
            } while (ctxt != null);
            throw _constructError("Unexpected problem: chain of filtered context broken");
        }
        t = this.delegate.nextToken();
        if (t == null) {
            this._currToken = t;
            return t;
        }
        TokenFilter f;
        switch (t.id()) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                f = this._itemFilter;
                if (f == TokenFilter.INCLUDE_ALL) {
                    this._headContext = this._headContext.createChildObjectContext(f, true);
                    this._currToken = t;
                    return t;
                } else if (f == null) {
                    this.delegate.skipChildren();
                    break;
                } else {
                    f = this._headContext.checkValue(f);
                    if (f == null) {
                        this.delegate.skipChildren();
                        break;
                    }
                    if (f != TokenFilter.INCLUDE_ALL) {
                        f = f.filterStartObject();
                    }
                    this._itemFilter = f;
                    if (f == TokenFilter.INCLUDE_ALL) {
                        this._headContext = this._headContext.createChildObjectContext(f, true);
                        this._currToken = t;
                        return t;
                    }
                    this._headContext = this._headContext.createChildObjectContext(f, false);
                    if (this._includePath) {
                        t = _nextTokenWithBuffering(this._headContext);
                        if (t != null) {
                            this._currToken = t;
                            return t;
                        }
                    }
                }
                break;
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
            case Place.TYPE_AQUARIUM /*4*/:
                boolean returnEnd = this._headContext.isStartHandled();
                f = this._headContext.getFilter();
                if (!(f == null || f == TokenFilter.INCLUDE_ALL)) {
                    f.filterFinishArray();
                }
                this._headContext = this._headContext.getParent();
                this._itemFilter = this._headContext.getFilter();
                if (returnEnd) {
                    this._currToken = t;
                    return t;
                }
                break;
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                f = this._itemFilter;
                if (f == TokenFilter.INCLUDE_ALL) {
                    this._headContext = this._headContext.createChildArrayContext(f, true);
                    this._currToken = t;
                    return t;
                } else if (f == null) {
                    this.delegate.skipChildren();
                    break;
                } else {
                    f = this._headContext.checkValue(f);
                    if (f == null) {
                        this.delegate.skipChildren();
                        break;
                    }
                    if (f != TokenFilter.INCLUDE_ALL) {
                        f = f.filterStartArray();
                    }
                    this._itemFilter = f;
                    if (f == TokenFilter.INCLUDE_ALL) {
                        this._headContext = this._headContext.createChildArrayContext(f, true);
                        this._currToken = t;
                        return t;
                    }
                    this._headContext = this._headContext.createChildArrayContext(f, false);
                    if (this._includePath) {
                        t = _nextTokenWithBuffering(this._headContext);
                        if (t != null) {
                            this._currToken = t;
                            return t;
                        }
                    }
                }
                break;
            case Place.TYPE_ART_GALLERY /*5*/:
                String name = this.delegate.getCurrentName();
                f = this._headContext.setFieldName(name);
                if (f == TokenFilter.INCLUDE_ALL) {
                    this._itemFilter = f;
                    if (!(this._includePath || !this._includeImmediateParent || this._headContext.isStartHandled())) {
                        t = this._headContext.nextTokenToRead();
                        this._exposedContext = this._headContext;
                    }
                    this._currToken = t;
                    return t;
                } else if (f == null) {
                    this.delegate.nextToken();
                    this.delegate.skipChildren();
                    break;
                } else {
                    f = f.includeProperty(name);
                    if (f == null) {
                        this.delegate.nextToken();
                        this.delegate.skipChildren();
                        break;
                    }
                    this._itemFilter = f;
                    if (f == TokenFilter.INCLUDE_ALL && this._includePath) {
                        this._currToken = t;
                        return t;
                    } else if (this._includePath) {
                        t = _nextTokenWithBuffering(this._headContext);
                        if (t != null) {
                            this._currToken = t;
                            return t;
                        }
                    }
                }
                break;
            default:
                f = this._itemFilter;
                if (f == TokenFilter.INCLUDE_ALL) {
                    this._currToken = t;
                    return t;
                } else if (f != null) {
                    f = this._headContext.checkValue(f);
                    if (f == TokenFilter.INCLUDE_ALL || (f != null && f.includeValue(this.delegate))) {
                        this._currToken = t;
                        return t;
                    }
                }
                break;
        }
        return _nextToken2();
    }

    protected final JsonToken _nextToken2() throws IOException {
        while (true) {
            JsonToken t = this.delegate.nextToken();
            if (t == null) {
                this._currToken = t;
                return t;
            }
            TokenFilter f;
            switch (t.id()) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    f = this._itemFilter;
                    if (f == TokenFilter.INCLUDE_ALL) {
                        this._headContext = this._headContext.createChildObjectContext(f, true);
                        this._currToken = t;
                        return t;
                    } else if (f == null) {
                        this.delegate.skipChildren();
                        break;
                    } else {
                        f = this._headContext.checkValue(f);
                        if (f == null) {
                            this.delegate.skipChildren();
                            break;
                        }
                        if (f != TokenFilter.INCLUDE_ALL) {
                            f = f.filterStartObject();
                        }
                        this._itemFilter = f;
                        if (f == TokenFilter.INCLUDE_ALL) {
                            this._headContext = this._headContext.createChildObjectContext(f, true);
                            this._currToken = t;
                            return t;
                        }
                        this._headContext = this._headContext.createChildObjectContext(f, false);
                        if (this._includePath) {
                            t = _nextTokenWithBuffering(this._headContext);
                            if (t == null) {
                                break;
                            }
                            this._currToken = t;
                            return t;
                        }
                        continue;
                    }
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                case Place.TYPE_AQUARIUM /*4*/:
                    boolean returnEnd = this._headContext.isStartHandled();
                    f = this._headContext.getFilter();
                    if (!(f == null || f == TokenFilter.INCLUDE_ALL)) {
                        f.filterFinishArray();
                    }
                    this._headContext = this._headContext.getParent();
                    this._itemFilter = this._headContext.getFilter();
                    if (!returnEnd) {
                        break;
                    }
                    this._currToken = t;
                    return t;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    f = this._itemFilter;
                    if (f == TokenFilter.INCLUDE_ALL) {
                        this._headContext = this._headContext.createChildArrayContext(f, true);
                        this._currToken = t;
                        return t;
                    } else if (f == null) {
                        this.delegate.skipChildren();
                        break;
                    } else {
                        f = this._headContext.checkValue(f);
                        if (f == null) {
                            this.delegate.skipChildren();
                            break;
                        }
                        if (f != TokenFilter.INCLUDE_ALL) {
                            f = f.filterStartArray();
                        }
                        this._itemFilter = f;
                        if (f == TokenFilter.INCLUDE_ALL) {
                            this._headContext = this._headContext.createChildArrayContext(f, true);
                            this._currToken = t;
                            return t;
                        }
                        this._headContext = this._headContext.createChildArrayContext(f, false);
                        if (this._includePath) {
                            t = _nextTokenWithBuffering(this._headContext);
                            if (t == null) {
                                break;
                            }
                            this._currToken = t;
                            return t;
                        }
                        continue;
                    }
                case Place.TYPE_ART_GALLERY /*5*/:
                    String name = this.delegate.getCurrentName();
                    f = this._headContext.setFieldName(name);
                    if (f == TokenFilter.INCLUDE_ALL) {
                        this._itemFilter = f;
                        this._currToken = t;
                        return t;
                    } else if (f == null) {
                        this.delegate.nextToken();
                        this.delegate.skipChildren();
                        break;
                    } else {
                        f = f.includeProperty(name);
                        if (f == null) {
                            this.delegate.nextToken();
                            this.delegate.skipChildren();
                            break;
                        }
                        this._itemFilter = f;
                        if (f == TokenFilter.INCLUDE_ALL) {
                            if (!this._includePath) {
                                break;
                            }
                            this._currToken = t;
                            return t;
                        } else if (this._includePath) {
                            t = _nextTokenWithBuffering(this._headContext);
                            if (t == null) {
                                break;
                            }
                            this._currToken = t;
                            return t;
                        } else {
                            continue;
                        }
                    }
                default:
                    f = this._itemFilter;
                    if (f == TokenFilter.INCLUDE_ALL) {
                        this._currToken = t;
                        return t;
                    } else if (f != null) {
                        f = this._headContext.checkValue(f);
                        if (f == TokenFilter.INCLUDE_ALL || (f != null && f.includeValue(this.delegate))) {
                            this._currToken = t;
                            return t;
                        }
                    } else {
                        continue;
                    }
            }
        }
    }

    protected final JsonToken _nextTokenWithBuffering(TokenFilterContext buffRoot) throws IOException {
        while (true) {
            JsonToken t = this.delegate.nextToken();
            if (t == null) {
                return t;
            }
            TokenFilter f;
            switch (t.id()) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    f = this._itemFilter;
                    if (f != TokenFilter.INCLUDE_ALL) {
                        if (f != null) {
                            f = this._headContext.checkValue(f);
                            if (f != null) {
                                if (f != TokenFilter.INCLUDE_ALL) {
                                    f = f.filterStartObject();
                                }
                                this._itemFilter = f;
                                if (f != TokenFilter.INCLUDE_ALL) {
                                    this._headContext = this._headContext.createChildObjectContext(f, false);
                                    break;
                                }
                                this._headContext = this._headContext.createChildObjectContext(f, true);
                                return _nextBuffered(buffRoot);
                            }
                            this.delegate.skipChildren();
                            break;
                        }
                        this.delegate.skipChildren();
                        break;
                    }
                    this._headContext = this._headContext.createChildObjectContext(f, true);
                    return t;
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                case Place.TYPE_AQUARIUM /*4*/:
                    boolean gotEnd;
                    boolean returnEnd;
                    f = this._headContext.getFilter();
                    if (!(f == null || f == TokenFilter.INCLUDE_ALL)) {
                        f.filterFinishArray();
                    }
                    if (this._headContext == buffRoot) {
                        gotEnd = true;
                    } else {
                        gotEnd = false;
                    }
                    if (gotEnd && this._headContext.isStartHandled()) {
                        returnEnd = true;
                    } else {
                        returnEnd = false;
                    }
                    this._headContext = this._headContext.getParent();
                    this._itemFilter = this._headContext.getFilter();
                    if (!returnEnd) {
                        if (!gotEnd && this._headContext != buffRoot) {
                            break;
                        }
                        return null;
                    }
                    return t;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    f = this._headContext.checkValue(this._itemFilter);
                    if (f != null) {
                        if (f != TokenFilter.INCLUDE_ALL) {
                            f = f.filterStartArray();
                        }
                        this._itemFilter = f;
                        if (f != TokenFilter.INCLUDE_ALL) {
                            this._headContext = this._headContext.createChildArrayContext(f, false);
                            break;
                        }
                        this._headContext = this._headContext.createChildArrayContext(f, true);
                        return _nextBuffered(buffRoot);
                    }
                    this.delegate.skipChildren();
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    String name = this.delegate.getCurrentName();
                    f = this._headContext.setFieldName(name);
                    if (f != TokenFilter.INCLUDE_ALL) {
                        if (f != null) {
                            f = f.includeProperty(name);
                            if (f != null) {
                                this._itemFilter = f;
                                if (f != TokenFilter.INCLUDE_ALL) {
                                    break;
                                }
                                return _nextBuffered(buffRoot);
                            }
                            this.delegate.nextToken();
                            this.delegate.skipChildren();
                            break;
                        }
                        this.delegate.nextToken();
                        this.delegate.skipChildren();
                        break;
                    }
                    this._itemFilter = f;
                    return _nextBuffered(buffRoot);
                default:
                    f = this._itemFilter;
                    if (f == TokenFilter.INCLUDE_ALL) {
                        return _nextBuffered(buffRoot);
                    }
                    if (f != null) {
                        f = this._headContext.checkValue(f);
                        if (f == TokenFilter.INCLUDE_ALL || (f != null && f.includeValue(this.delegate))) {
                            return _nextBuffered(buffRoot);
                        }
                    }
                    continue;
            }
        }
    }

    private JsonToken _nextBuffered(TokenFilterContext buffRoot) throws IOException {
        this._exposedContext = buffRoot;
        TokenFilterContext ctxt = buffRoot;
        JsonToken t = ctxt.nextTokenToRead();
        if (t != null) {
            return t;
        }
        while (ctxt != this._headContext) {
            ctxt = this._exposedContext.findChildOf(ctxt);
            this._exposedContext = ctxt;
            if (ctxt == null) {
                throw _constructError("Unexpected problem: chain of filtered context broken");
            }
            t = this._exposedContext.nextTokenToRead();
            if (t != null) {
                return t;
            }
        }
        throw _constructError("Internal error: failed to locate expected buffered tokens");
    }

    public JsonToken nextValue() throws IOException {
        JsonToken t = nextToken();
        if (t == JsonToken.FIELD_NAME) {
            return nextToken();
        }
        return t;
    }

    public JsonParser skipChildren() throws IOException {
        if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
            int open = 1;
            while (true) {
                JsonToken t = nextToken();
                if (t == null) {
                    break;
                } else if (t.isStructStart()) {
                    open++;
                } else if (t.isStructEnd()) {
                    open--;
                    if (open == 0) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        return this;
    }

    public String getText() throws IOException {
        return this.delegate.getText();
    }

    public boolean hasTextCharacters() {
        return this.delegate.hasTextCharacters();
    }

    public char[] getTextCharacters() throws IOException {
        return this.delegate.getTextCharacters();
    }

    public int getTextLength() throws IOException {
        return this.delegate.getTextLength();
    }

    public int getTextOffset() throws IOException {
        return this.delegate.getTextOffset();
    }

    public BigInteger getBigIntegerValue() throws IOException {
        return this.delegate.getBigIntegerValue();
    }

    public boolean getBooleanValue() throws IOException {
        return this.delegate.getBooleanValue();
    }

    public byte getByteValue() throws IOException {
        return this.delegate.getByteValue();
    }

    public short getShortValue() throws IOException {
        return this.delegate.getShortValue();
    }

    public BigDecimal getDecimalValue() throws IOException {
        return this.delegate.getDecimalValue();
    }

    public double getDoubleValue() throws IOException {
        return this.delegate.getDoubleValue();
    }

    public float getFloatValue() throws IOException {
        return this.delegate.getFloatValue();
    }

    public int getIntValue() throws IOException {
        return this.delegate.getIntValue();
    }

    public long getLongValue() throws IOException {
        return this.delegate.getLongValue();
    }

    public NumberType getNumberType() throws IOException {
        return this.delegate.getNumberType();
    }

    public Number getNumberValue() throws IOException {
        return this.delegate.getNumberValue();
    }

    public int getValueAsInt() throws IOException {
        return this.delegate.getValueAsInt();
    }

    public int getValueAsInt(int defaultValue) throws IOException {
        return this.delegate.getValueAsInt(defaultValue);
    }

    public long getValueAsLong() throws IOException {
        return this.delegate.getValueAsLong();
    }

    public long getValueAsLong(long defaultValue) throws IOException {
        return this.delegate.getValueAsLong(defaultValue);
    }

    public double getValueAsDouble() throws IOException {
        return this.delegate.getValueAsDouble();
    }

    public double getValueAsDouble(double defaultValue) throws IOException {
        return this.delegate.getValueAsDouble(defaultValue);
    }

    public boolean getValueAsBoolean() throws IOException {
        return this.delegate.getValueAsBoolean();
    }

    public boolean getValueAsBoolean(boolean defaultValue) throws IOException {
        return this.delegate.getValueAsBoolean(defaultValue);
    }

    public String getValueAsString() throws IOException {
        return this.delegate.getValueAsString();
    }

    public String getValueAsString(String defaultValue) throws IOException {
        return this.delegate.getValueAsString(defaultValue);
    }

    public Object getEmbeddedObject() throws IOException {
        return this.delegate.getEmbeddedObject();
    }

    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
        return this.delegate.getBinaryValue(b64variant);
    }

    public int readBinaryValue(Base64Variant b64variant, OutputStream out) throws IOException {
        return this.delegate.readBinaryValue(b64variant, out);
    }

    public JsonLocation getTokenLocation() {
        return this.delegate.getTokenLocation();
    }

    protected JsonStreamContext _filterContext() {
        if (this._exposedContext != null) {
            return this._exposedContext;
        }
        return this._headContext;
    }
}
