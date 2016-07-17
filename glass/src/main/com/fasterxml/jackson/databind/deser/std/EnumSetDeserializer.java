package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.util.EnumSet;

public class EnumSetDeserializer extends StdDeserializer<EnumSet<?>> implements ContextualDeserializer {
    private static final long serialVersionUID = 1;
    protected final Class<Enum> _enumClass;
    protected JsonDeserializer<Enum<?>> _enumDeserializer;
    protected final JavaType _enumType;

    public EnumSetDeserializer(JavaType enumType, JsonDeserializer<?> deser) {
        super(EnumSet.class);
        this._enumType = enumType;
        this._enumClass = enumType.getRawClass();
        if (this._enumClass.isEnum()) {
            this._enumDeserializer = deser;
            return;
        }
        throw new IllegalArgumentException("Type " + enumType + " not Java Enum type");
    }

    public EnumSetDeserializer withDeserializer(JsonDeserializer<?> deser) {
        return this._enumDeserializer == deser ? this : new EnumSetDeserializer(this._enumType, deser);
    }

    public boolean isCachable() {
        if (this._enumType.getValueHandler() != null) {
            return false;
        }
        return true;
    }

    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<?> deser = this._enumDeserializer;
        if (deser == null) {
            deser = ctxt.findContextualValueDeserializer(this._enumType, property);
        } else {
            deser = ctxt.handleSecondaryContextualization(deser, property, this._enumType);
        }
        return withDeserializer(deser);
    }

    public EnumSet<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.isExpectedStartArrayToken()) {
            Object result = constructSet();
            while (true) {
                try {
                    JsonToken t = jp.nextToken();
                    if (t == JsonToken.END_ARRAY) {
                        return result;
                    }
                    if (t == JsonToken.VALUE_NULL) {
                        break;
                    }
                    Enum<?> value = (Enum) this._enumDeserializer.deserialize(jp, ctxt);
                    if (value != null) {
                        result.add(value);
                    }
                } catch (Throwable e) {
                    throw JsonMappingException.wrapWithPath(e, result, result.size());
                }
            }
            throw ctxt.mappingException(this._enumClass);
        }
        throw ctxt.mappingException(EnumSet.class);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    private EnumSet constructSet() {
        return EnumSet.noneOf(this._enumClass);
    }
}
