package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.databind.util.NameTransformer;
import java.io.IOException;

public class BeanAsArraySerializer extends BeanSerializerBase {
    private static final long serialVersionUID = 1;
    protected final BeanSerializerBase _defaultSerializer;

    public BeanAsArraySerializer(BeanSerializerBase src) {
        super(src, (ObjectIdWriter) null);
        this._defaultSerializer = src;
    }

    protected BeanAsArraySerializer(BeanSerializerBase src, String[] toIgnore) {
        super(src, toIgnore);
        this._defaultSerializer = src;
    }

    protected BeanAsArraySerializer(BeanSerializerBase src, ObjectIdWriter oiw, Object filterId) {
        super(src, oiw, filterId);
        this._defaultSerializer = src;
    }

    public JsonSerializer<Object> unwrappingSerializer(NameTransformer transformer) {
        return this._defaultSerializer.unwrappingSerializer(transformer);
    }

    public boolean isUnwrappingSerializer() {
        return false;
    }

    public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
        return this._defaultSerializer.withObjectIdWriter(objectIdWriter);
    }

    public BeanSerializerBase withFilterId(Object filterId) {
        return new BeanAsArraySerializer(this, this._objectIdWriter, filterId);
    }

    protected BeanAsArraySerializer withIgnorals(String[] toIgnore) {
        return new BeanAsArraySerializer(this, toIgnore);
    }

    protected BeanSerializerBase asArraySerializer() {
        return this;
    }

    public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        if (this._objectIdWriter != null) {
            _serializeWithObjectId(bean, gen, provider, typeSer);
            return;
        }
        String typeStr = this._typeId == null ? null : _customTypeId(bean);
        if (typeStr == null) {
            typeSer.writeTypePrefixForArray(bean, gen);
        } else {
            typeSer.writeCustomTypePrefixForArray(bean, gen, typeStr);
        }
        serializeAsArray(bean, gen, provider);
        if (typeStr == null) {
            typeSer.writeTypeSuffixForArray(bean, gen);
        } else {
            typeSer.writeCustomTypeSuffixForArray(bean, gen, typeStr);
        }
    }

    public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED) && hasSingleElement(provider)) {
            serializeAsArray(bean, gen, provider);
            return;
        }
        gen.writeStartArray();
        gen.setCurrentValue(bean);
        serializeAsArray(bean, gen, provider);
        gen.writeEndArray();
    }

    private boolean hasSingleElement(SerializerProvider provider) {
        BeanPropertyWriter[] props;
        if (this._filteredProps == null || provider.getActiveView() == null) {
            props = this._props;
        } else {
            props = this._filteredProps;
        }
        if (props.length == 1) {
            return true;
        }
        return false;
    }

    protected final void serializeAsArray(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        BeanPropertyWriter[] props;
        if (this._filteredProps == null || provider.getActiveView() == null) {
            props = this._props;
        } else {
            props = this._filteredProps;
        }
        int i = 0;
        try {
            int len = props.length;
            while (i < len) {
                BeanPropertyWriter prop = props[i];
                if (prop == null) {
                    gen.writeNull();
                } else {
                    prop.serializeAsElement(bean, gen, provider);
                }
                i++;
            }
        } catch (Exception e) {
            wrapAndThrow(provider, (Throwable) e, bean, i == props.length ? "[anySetter]" : props[i].getName());
        } catch (Throwable e2) {
            JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)", e2);
            mapE.prependPath(new Reference(bean, i == props.length ? "[anySetter]" : props[i].getName()));
            throw mapE;
        }
    }

    public String toString() {
        return "BeanAsArraySerializer for " + handledType().getName();
    }
}
