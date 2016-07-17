package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase;
import java.io.IOException;
import java.util.Iterator;

@JacksonStdImpl
public class IteratorSerializer extends AsArraySerializerBase<Iterator<?>> {
    public IteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts) {
        super(Iterator.class, elemType, staticTyping, vts, null);
    }

    public IteratorSerializer(IteratorSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer, Boolean unwrapSingle) {
        super((AsArraySerializerBase) src, property, vts, (JsonSerializer) valueSerializer, unwrapSingle);
    }

    public boolean isEmpty(SerializerProvider prov, Iterator<?> value) {
        return value == null || !value.hasNext();
    }

    public boolean hasSingleElement(Iterator<?> it) {
        return false;
    }

    public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
        return new IteratorSerializer(this, this._property, vts, this._elementSerializer, this._unwrapSingle);
    }

    public IteratorSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle) {
        return new IteratorSerializer(this, property, vts, elementSerializer, unwrapSingle);
    }

    public final void serialize(Iterator<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (((this._unwrapSingle == null && provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) || this._unwrapSingle == Boolean.TRUE) && hasSingleElement((Iterator) value)) {
            serializeContents((Iterator) value, gen, provider);
            return;
        }
        gen.writeStartArray();
        serializeContents((Iterator) value, gen, provider);
        gen.writeEndArray();
    }

    public void serializeContents(Iterator<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value.hasNext()) {
            TypeSerializer typeSer = this._valueTypeSerializer;
            JsonSerializer<Object> prevSerializer = null;
            Class<?> prevClass = null;
            do {
                Object elem = value.next();
                if (elem == null) {
                    provider.defaultSerializeNull(gen);
                } else {
                    JsonSerializer<Object> currSerializer = this._elementSerializer;
                    if (currSerializer == null) {
                        Class<?> cc = elem.getClass();
                        if (cc == prevClass) {
                            currSerializer = prevSerializer;
                        } else {
                            currSerializer = provider.findValueSerializer((Class) cc, this._property);
                            prevSerializer = currSerializer;
                            prevClass = cc;
                        }
                    }
                    if (typeSer == null) {
                        currSerializer.serialize(elem, gen, provider);
                    } else {
                        currSerializer.serializeWithType(elem, gen, provider, typeSer);
                    }
                }
            } while (value.hasNext());
        }
    }
}
