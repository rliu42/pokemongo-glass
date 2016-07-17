package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import java.io.IOException;

@JacksonStdImpl
public final class StringArrayDeserializer extends StdDeserializer<String[]> implements ContextualDeserializer {
    public static final StringArrayDeserializer instance;
    private static final long serialVersionUID = 1;
    protected JsonDeserializer<String> _elementDeserializer;

    static {
        instance = new StringArrayDeserializer();
    }

    public StringArrayDeserializer() {
        super(String[].class);
        this._elementDeserializer = null;
    }

    protected StringArrayDeserializer(JsonDeserializer<?> deser) {
        super(String[].class);
        this._elementDeserializer = deser;
    }

    public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (!jp.isExpectedStartArrayToken()) {
            return handleNonArray(jp, ctxt);
        }
        if (this._elementDeserializer != null) {
            return _deserializeCustom(jp, ctxt);
        }
        ObjectBuffer buffer = ctxt.leaseObjectBuffer();
        Object chunk = buffer.resetAndStart();
        int ix = 0;
        while (true) {
            int ix2;
            String value = jp.nextTextValue();
            if (value == null) {
                JsonToken t = jp.getCurrentToken();
                if (t == JsonToken.END_ARRAY) {
                    String[] result = (String[]) buffer.completeAndClearBuffer((Object[]) chunk, ix, String.class);
                    ctxt.returnObjectBuffer(buffer);
                    return result;
                }
                try {
                    if (t != JsonToken.VALUE_NULL) {
                        value = _parseString(jp, ctxt);
                    }
                } catch (Throwable e) {
                    throw JsonMappingException.wrapWithPath(e, chunk, buffer.bufferedSize() + ix);
                }
            }
            if (ix >= chunk.length) {
                chunk = buffer.appendCompletedChunk(chunk);
                ix2 = 0;
            } else {
                ix2 = ix;
            }
            ix = ix2 + 1;
            chunk[ix2] = value;
        }
    }

    protected final String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectBuffer buffer = ctxt.leaseObjectBuffer();
        Object[] chunk = buffer.resetAndStart();
        JsonDeserializer<String> deser = this._elementDeserializer;
        int ix = 0;
        while (true) {
            String value;
            int ix2;
            if (jp.nextTextValue() == null) {
                JsonToken t = jp.getCurrentToken();
                if (t == JsonToken.END_ARRAY) {
                    String[] result = (String[]) buffer.completeAndClearBuffer(chunk, ix, String.class);
                    ctxt.returnObjectBuffer(buffer);
                    return result;
                }
                try {
                    value = t == JsonToken.VALUE_NULL ? (String) deser.getNullValue(ctxt) : (String) deser.deserialize(jp, ctxt);
                } catch (Throwable e) {
                    throw JsonMappingException.wrapWithPath(e, (Object) String.class, ix);
                }
            }
            value = (String) deser.deserialize(jp, ctxt);
            if (ix >= chunk.length) {
                chunk = buffer.appendCompletedChunk(chunk);
                ix2 = 0;
            } else {
                ix2 = ix;
            }
            ix = ix2 + 1;
            chunk[ix2] = value;
        }
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    private final String[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String[] strArr = null;
        if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            String[] strArr2 = new String[1];
            if (jp.getCurrentToken() != JsonToken.VALUE_NULL) {
                strArr = _parseString(jp, ctxt);
            }
            strArr2[0] = strArr;
            return strArr2;
        } else if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
            return null;
        } else {
            throw ctxt.mappingException(this._valueClass);
        }
    }

    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<?> deser = findConvertingContentDeserializer(ctxt, property, this._elementDeserializer);
        JavaType type = ctxt.constructType(String.class);
        if (deser == null) {
            deser = ctxt.findContextualValueDeserializer(type, property);
        } else {
            deser = ctxt.handleSecondaryContextualization(deser, property, type);
        }
        if (deser != null && isDefaultDeserializer(deser)) {
            deser = null;
        }
        if (this._elementDeserializer != deser) {
            return new StringArrayDeserializer(deser);
        }
        return this;
    }
}
