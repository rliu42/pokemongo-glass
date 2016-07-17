package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class MapProperty extends PropertyWriter {
    protected Object _key;
    protected JsonSerializer<Object> _keySerializer;
    protected final BeanProperty _property;
    protected final TypeSerializer _typeSerializer;
    protected JsonSerializer<Object> _valueSerializer;

    @Deprecated
    public MapProperty(TypeSerializer typeSer) {
        this(typeSer, null);
    }

    public MapProperty(TypeSerializer typeSer, BeanProperty prop) {
        this._typeSerializer = typeSer;
        this._property = prop;
    }

    public void reset(Object key, JsonSerializer<Object> keySer, JsonSerializer<Object> valueSer) {
        this._key = key;
        this._keySerializer = keySer;
        this._valueSerializer = valueSer;
    }

    public String getName() {
        if (this._key instanceof String) {
            return (String) this._key;
        }
        return String.valueOf(this._key);
    }

    public PropertyName getFullName() {
        return new PropertyName(getName());
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._property == null ? null : this._property.getAnnotation(acls);
    }

    public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
        return this._property == null ? null : this._property.getContextAnnotation(acls);
    }

    public void serializeAsField(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        this._keySerializer.serialize(this._key, jgen, provider);
        if (this._typeSerializer == null) {
            this._valueSerializer.serialize(value, jgen, provider);
        } else {
            this._valueSerializer.serializeWithType(value, jgen, provider, this._typeSerializer);
        }
    }

    public void serializeAsOmittedField(Object value, JsonGenerator jgen, SerializerProvider provider) throws Exception {
        if (!jgen.canOmitFields()) {
            jgen.writeOmittedField(getName());
        }
    }

    public void serializeAsElement(Object value, JsonGenerator jgen, SerializerProvider provider) throws Exception {
        if (this._typeSerializer == null) {
            this._valueSerializer.serialize(value, jgen, provider);
        } else {
            this._valueSerializer.serializeWithType(value, jgen, provider, this._typeSerializer);
        }
    }

    public void serializeAsPlaceholder(Object value, JsonGenerator jgen, SerializerProvider provider) throws Exception {
        jgen.writeNull();
    }

    public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor) throws JsonMappingException {
    }

    @Deprecated
    public void depositSchemaProperty(ObjectNode propertiesNode, SerializerProvider provider) throws JsonMappingException {
    }
}
