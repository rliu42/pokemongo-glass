package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;

public final class TypeWrappedSerializer extends JsonSerializer<Object> {
    protected final JsonSerializer<Object> _serializer;
    protected final TypeSerializer _typeSerializer;

    public TypeWrappedSerializer(TypeSerializer typeSer, JsonSerializer<?> ser) {
        this._typeSerializer = typeSer;
        this._serializer = ser;
    }

    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        this._serializer.serializeWithType(value, jgen, provider, this._typeSerializer);
    }

    public void serializeWithType(Object value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        this._serializer.serializeWithType(value, jgen, provider, typeSer);
    }

    public Class<Object> handledType() {
        return Object.class;
    }

    public JsonSerializer<Object> valueSerializer() {
        return this._serializer;
    }

    public TypeSerializer typeSerializer() {
        return this._typeSerializer;
    }
}
