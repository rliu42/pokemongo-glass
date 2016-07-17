package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import java.io.IOException;

public class AsExistingPropertyTypeSerializer extends AsPropertyTypeSerializer {
    public AsExistingPropertyTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName) {
        super(idRes, property, propName);
    }

    public AsExistingPropertyTypeSerializer forProperty(BeanProperty prop) {
        return this._property == prop ? this : new AsExistingPropertyTypeSerializer(this._idResolver, prop, this._typePropertyName);
    }

    public As getTypeInclusion() {
        return As.EXISTING_PROPERTY;
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator jgen) throws IOException {
        String typeId = idFromValue(value);
        if (typeId != null && jgen.canWriteTypeId()) {
            jgen.writeTypeId(typeId);
        }
        jgen.writeStartObject();
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        String typeId = idFromValueAndType(value, type);
        if (typeId != null && jgen.canWriteTypeId()) {
            jgen.writeTypeId(typeId);
        }
        jgen.writeStartObject();
    }

    public void writeCustomTypePrefixForObject(Object value, JsonGenerator jgen, String typeId) throws IOException {
        if (typeId != null && jgen.canWriteTypeId()) {
            jgen.writeTypeId(typeId);
        }
        jgen.writeStartObject();
    }
}
