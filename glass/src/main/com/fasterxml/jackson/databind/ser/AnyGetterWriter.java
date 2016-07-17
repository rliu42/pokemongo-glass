package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import java.util.Map;

public class AnyGetterWriter {
    protected final AnnotatedMember _accessor;
    protected MapSerializer _mapSerializer;
    protected final BeanProperty _property;
    protected JsonSerializer<Object> _serializer;

    public AnyGetterWriter(BeanProperty property, AnnotatedMember accessor, JsonSerializer<?> serializer) {
        this._accessor = accessor;
        this._property = property;
        this._serializer = serializer;
        if (serializer instanceof MapSerializer) {
            this._mapSerializer = (MapSerializer) serializer;
        }
    }

    public void getAndSerialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws Exception {
        Object value = this._accessor.getValue(bean);
        if (value != null) {
            if (!(value instanceof Map)) {
                throw new JsonMappingException("Value returned by 'any-getter' (" + this._accessor.getName() + "()) not java.util.Map but " + value.getClass().getName());
            } else if (this._mapSerializer != null) {
                this._mapSerializer.serializeFields((Map) value, gen, provider);
            } else {
                this._serializer.serialize(value, gen, provider);
            }
        }
    }

    public void getAndFilter(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyFilter filter) throws Exception {
        Object value = this._accessor.getValue(bean);
        if (value != null) {
            if (!(value instanceof Map)) {
                throw new JsonMappingException("Value returned by 'any-getter' (" + this._accessor.getName() + "()) not java.util.Map but " + value.getClass().getName());
            } else if (this._mapSerializer != null) {
                this._mapSerializer.serializeFilteredFields((Map) value, gen, provider, filter, null);
            } else {
                this._serializer.serialize(value, gen, provider);
            }
        }
    }

    public void resolve(SerializerProvider provider) throws JsonMappingException {
        if (this._serializer instanceof ContextualSerializer) {
            JsonSerializer<?> ser = provider.handlePrimaryContextualization(this._serializer, this._property);
            this._serializer = ser;
            if (ser instanceof MapSerializer) {
                this._mapSerializer = (MapSerializer) ser;
            }
        }
    }
}
