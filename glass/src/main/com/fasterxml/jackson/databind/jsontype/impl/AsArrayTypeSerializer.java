package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import java.io.IOException;

public class AsArrayTypeSerializer extends TypeSerializerBase {
    public AsArrayTypeSerializer(TypeIdResolver idRes, BeanProperty property) {
        super(idRes, property);
    }

    public AsArrayTypeSerializer forProperty(BeanProperty prop) {
        return this._property == prop ? this : new AsArrayTypeSerializer(this._idResolver, prop);
    }

    public As getTypeInclusion() {
        return As.WRAPPER_ARRAY;
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator jgen) throws IOException {
        String typeId = idFromValue(value);
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartArray();
            jgen.writeString(typeId);
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
        jgen.writeStartObject();
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        String typeId = idFromValueAndType(value, type);
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartArray();
            jgen.writeString(typeId);
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
        jgen.writeStartObject();
    }

    public void writeTypePrefixForArray(Object value, JsonGenerator jgen) throws IOException {
        String typeId = idFromValue(value);
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartArray();
            jgen.writeString(typeId);
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
        jgen.writeStartArray();
    }

    public void writeTypePrefixForArray(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        String typeId = idFromValueAndType(value, type);
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartArray();
            jgen.writeString(typeId);
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
        jgen.writeStartArray();
    }

    public void writeTypePrefixForScalar(Object value, JsonGenerator jgen) throws IOException {
        String typeId = idFromValue(value);
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartArray();
            jgen.writeString(typeId);
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
    }

    public void writeTypePrefixForScalar(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        String typeId = idFromValueAndType(value, type);
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartArray();
            jgen.writeString(typeId);
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
    }

    public void writeTypeSuffixForObject(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeEndObject();
        if (!jgen.canWriteTypeId()) {
            jgen.writeEndArray();
        }
    }

    public void writeTypeSuffixForArray(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeEndArray();
        if (!jgen.canWriteTypeId()) {
            jgen.writeEndArray();
        }
    }

    public void writeTypeSuffixForScalar(Object value, JsonGenerator jgen) throws IOException {
        if (!jgen.canWriteTypeId()) {
            jgen.writeEndArray();
        }
    }

    public void writeCustomTypePrefixForObject(Object value, JsonGenerator jgen, String typeId) throws IOException {
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartArray();
            jgen.writeString(typeId);
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
        jgen.writeStartObject();
    }

    public void writeCustomTypePrefixForArray(Object value, JsonGenerator jgen, String typeId) throws IOException {
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartArray();
            jgen.writeString(typeId);
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
        jgen.writeStartArray();
    }

    public void writeCustomTypePrefixForScalar(Object value, JsonGenerator jgen, String typeId) throws IOException {
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartArray();
            jgen.writeString(typeId);
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
    }

    public void writeCustomTypeSuffixForObject(Object value, JsonGenerator jgen, String typeId) throws IOException {
        if (!jgen.canWriteTypeId()) {
            writeTypeSuffixForObject(value, jgen);
        }
    }

    public void writeCustomTypeSuffixForArray(Object value, JsonGenerator jgen, String typeId) throws IOException {
        if (!jgen.canWriteTypeId()) {
            writeTypeSuffixForArray(value, jgen);
        }
    }

    public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator jgen, String typeId) throws IOException {
        if (!jgen.canWriteTypeId()) {
            writeTypeSuffixForScalar(value, jgen);
        }
    }
}
