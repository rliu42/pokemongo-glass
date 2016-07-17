package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import java.io.IOException;
import spacemadness.com.lunarconsole.BuildConfig;

public class AsWrapperTypeSerializer extends TypeSerializerBase {
    public AsWrapperTypeSerializer(TypeIdResolver idRes, BeanProperty property) {
        super(idRes, property);
    }

    public AsWrapperTypeSerializer forProperty(BeanProperty prop) {
        return this._property == prop ? this : new AsWrapperTypeSerializer(this._idResolver, prop);
    }

    public As getTypeInclusion() {
        return As.WRAPPER_OBJECT;
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator jgen) throws IOException {
        String typeId = idFromValue(value);
        if (jgen.canWriteTypeId()) {
            if (typeId != null) {
                jgen.writeTypeId(typeId);
            }
            jgen.writeStartObject();
            return;
        }
        jgen.writeStartObject();
        jgen.writeObjectFieldStart(_validTypeId(typeId));
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        String typeId = idFromValueAndType(value, type);
        if (jgen.canWriteTypeId()) {
            if (typeId != null) {
                jgen.writeTypeId(typeId);
            }
            jgen.writeStartObject();
            return;
        }
        jgen.writeStartObject();
        jgen.writeObjectFieldStart(_validTypeId(typeId));
    }

    public void writeTypePrefixForArray(Object value, JsonGenerator jgen) throws IOException {
        String typeId = idFromValue(value);
        if (jgen.canWriteTypeId()) {
            if (typeId != null) {
                jgen.writeTypeId(typeId);
            }
            jgen.writeStartArray();
            return;
        }
        jgen.writeStartObject();
        jgen.writeArrayFieldStart(_validTypeId(typeId));
    }

    public void writeTypePrefixForArray(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        String typeId = idFromValueAndType(value, type);
        if (jgen.canWriteTypeId()) {
            if (typeId != null) {
                jgen.writeTypeId(typeId);
            }
            jgen.writeStartArray();
            return;
        }
        jgen.writeStartObject();
        jgen.writeArrayFieldStart(_validTypeId(typeId));
    }

    public void writeTypePrefixForScalar(Object value, JsonGenerator jgen) throws IOException {
        String typeId = idFromValue(value);
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartObject();
            jgen.writeFieldName(_validTypeId(typeId));
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
    }

    public void writeTypePrefixForScalar(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        String typeId = idFromValueAndType(value, type);
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartObject();
            jgen.writeFieldName(_validTypeId(typeId));
        } else if (typeId != null) {
            jgen.writeTypeId(typeId);
        }
    }

    public void writeTypeSuffixForObject(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeEndObject();
        if (!jgen.canWriteTypeId()) {
            jgen.writeEndObject();
        }
    }

    public void writeTypeSuffixForArray(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeEndArray();
        if (!jgen.canWriteTypeId()) {
            jgen.writeEndObject();
        }
    }

    public void writeTypeSuffixForScalar(Object value, JsonGenerator jgen) throws IOException {
        if (!jgen.canWriteTypeId()) {
            jgen.writeEndObject();
        }
    }

    public void writeCustomTypePrefixForObject(Object value, JsonGenerator jgen, String typeId) throws IOException {
        if (jgen.canWriteTypeId()) {
            if (typeId != null) {
                jgen.writeTypeId(typeId);
            }
            jgen.writeStartObject();
            return;
        }
        jgen.writeStartObject();
        jgen.writeObjectFieldStart(_validTypeId(typeId));
    }

    public void writeCustomTypePrefixForArray(Object value, JsonGenerator jgen, String typeId) throws IOException {
        if (jgen.canWriteTypeId()) {
            if (typeId != null) {
                jgen.writeTypeId(typeId);
            }
            jgen.writeStartArray();
            return;
        }
        jgen.writeStartObject();
        jgen.writeArrayFieldStart(_validTypeId(typeId));
    }

    public void writeCustomTypePrefixForScalar(Object value, JsonGenerator jgen, String typeId) throws IOException {
        if (!jgen.canWriteTypeId()) {
            jgen.writeStartObject();
            jgen.writeFieldName(_validTypeId(typeId));
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

    protected String _validTypeId(String typeId) {
        return typeId == null ? BuildConfig.FLAVOR : typeId;
    }
}
