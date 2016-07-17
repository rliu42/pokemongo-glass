package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import java.io.IOException;

public class AsExternalTypeSerializer extends TypeSerializerBase {
    protected final String _typePropertyName;

    public AsExternalTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName) {
        super(idRes, property);
        this._typePropertyName = propName;
    }

    public AsExternalTypeSerializer forProperty(BeanProperty prop) {
        return this._property == prop ? this : new AsExternalTypeSerializer(this._idResolver, prop, this._typePropertyName);
    }

    public String getPropertyName() {
        return this._typePropertyName;
    }

    public As getTypeInclusion() {
        return As.EXTERNAL_PROPERTY;
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator gen) throws IOException {
        _writeObjectPrefix(value, gen);
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator gen, Class<?> cls) throws IOException {
        _writeObjectPrefix(value, gen);
    }

    public void writeTypePrefixForArray(Object value, JsonGenerator gen) throws IOException {
        _writeArrayPrefix(value, gen);
    }

    public void writeTypePrefixForArray(Object value, JsonGenerator gen, Class<?> cls) throws IOException {
        _writeArrayPrefix(value, gen);
    }

    public void writeTypePrefixForScalar(Object value, JsonGenerator gen) throws IOException {
        _writeScalarPrefix(value, gen);
    }

    public void writeTypePrefixForScalar(Object value, JsonGenerator gen, Class<?> cls) throws IOException {
        _writeScalarPrefix(value, gen);
    }

    public void writeTypeSuffixForObject(Object value, JsonGenerator gen) throws IOException {
        _writeObjectSuffix(value, gen, idFromValue(value));
    }

    public void writeTypeSuffixForArray(Object value, JsonGenerator gen) throws IOException {
        _writeArraySuffix(value, gen, idFromValue(value));
    }

    public void writeTypeSuffixForScalar(Object value, JsonGenerator gen) throws IOException {
        _writeScalarSuffix(value, gen, idFromValue(value));
    }

    public void writeCustomTypePrefixForScalar(Object value, JsonGenerator gen, String typeId) throws IOException {
        _writeScalarPrefix(value, gen);
    }

    public void writeCustomTypePrefixForObject(Object value, JsonGenerator gen, String typeId) throws IOException {
        _writeObjectPrefix(value, gen);
    }

    public void writeCustomTypePrefixForArray(Object value, JsonGenerator gen, String typeId) throws IOException {
        _writeArrayPrefix(value, gen);
    }

    public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator gen, String typeId) throws IOException {
        _writeScalarSuffix(value, gen, typeId);
    }

    public void writeCustomTypeSuffixForObject(Object value, JsonGenerator gen, String typeId) throws IOException {
        _writeObjectSuffix(value, gen, typeId);
    }

    public void writeCustomTypeSuffixForArray(Object value, JsonGenerator gen, String typeId) throws IOException {
        _writeArraySuffix(value, gen, typeId);
    }

    protected final void _writeScalarPrefix(Object value, JsonGenerator gen) throws IOException {
    }

    protected final void _writeObjectPrefix(Object value, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
    }

    protected final void _writeArrayPrefix(Object value, JsonGenerator gen) throws IOException {
        gen.writeStartArray();
    }

    protected final void _writeScalarSuffix(Object value, JsonGenerator gen, String typeId) throws IOException {
        if (typeId != null) {
            gen.writeStringField(this._typePropertyName, typeId);
        }
    }

    protected final void _writeObjectSuffix(Object value, JsonGenerator gen, String typeId) throws IOException {
        gen.writeEndObject();
        if (typeId != null) {
            gen.writeStringField(this._typePropertyName, typeId);
        }
    }

    protected final void _writeArraySuffix(Object value, JsonGenerator gen, String typeId) throws IOException {
        gen.writeEndArray();
        if (typeId != null) {
            gen.writeStringField(this._typePropertyName, typeId);
        }
    }
}
