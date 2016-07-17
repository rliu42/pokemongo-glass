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
import java.util.List;

@JacksonStdImpl
public final class IndexedListSerializer extends AsArraySerializerBase<List<?>> {
    private static final long serialVersionUID = 1;

    public IndexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> valueSerializer) {
        super(List.class, elemType, staticTyping, vts, (JsonSerializer) valueSerializer);
    }

    public IndexedListSerializer(IndexedListSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer, Boolean unwrapSingle) {
        super((AsArraySerializerBase) src, property, vts, (JsonSerializer) valueSerializer, unwrapSingle);
    }

    public IndexedListSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle) {
        return new IndexedListSerializer(this, property, vts, elementSerializer, unwrapSingle);
    }

    public boolean isEmpty(SerializerProvider prov, List<?> value) {
        return value == null || value.isEmpty();
    }

    public boolean hasSingleElement(List<?> value) {
        return value.size() == 1;
    }

    public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
        return new IndexedListSerializer(this, this._property, vts, this._elementSerializer, this._unwrapSingle);
    }

    public final void serialize(List<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        int len = value.size();
        if (len == 1 && ((this._unwrapSingle == null && provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) || this._unwrapSingle == Boolean.TRUE)) {
            serializeContents((List) value, gen, provider);
            return;
        }
        gen.writeStartArray(len);
        serializeContents((List) value, gen, provider);
        gen.writeEndArray();
    }

    public void serializeContents(List<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (this._elementSerializer != null) {
            serializeContentsUsing(value, jgen, provider, this._elementSerializer);
        } else if (this._valueTypeSerializer != null) {
            serializeTypedContents(value, jgen, provider);
        } else {
            int len = value.size();
            if (len != 0) {
                int i = 0;
                try {
                    PropertySerializerMap serializers = this._dynamicSerializers;
                    while (i < len) {
                        Object elem = value.get(i);
                        if (elem == null) {
                            provider.defaultSerializeNull(jgen);
                        } else {
                            Class<?> cc = elem.getClass();
                            JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                            if (serializer == null) {
                                if (this._elementType.hasGenericTypes()) {
                                    serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
                                } else {
                                    serializer = _findAndAddDynamic(serializers, (Class) cc, provider);
                                }
                                serializers = this._dynamicSerializers;
                            }
                            serializer.serialize(elem, jgen, provider);
                        }
                        i++;
                    }
                } catch (Exception e) {
                    wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                }
            }
        }
    }

    public void serializeContentsUsing(List<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException {
        int len = value.size();
        if (len != 0) {
            TypeSerializer typeSer = this._valueTypeSerializer;
            for (int i = 0; i < len; i++) {
                Object elem = value.get(i);
                if (elem == null) {
                    try {
                        provider.defaultSerializeNull(jgen);
                    } catch (Exception e) {
                        wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                    }
                } else if (typeSer == null) {
                    ser.serialize(elem, jgen, provider);
                } else {
                    ser.serializeWithType(elem, jgen, provider, typeSer);
                }
            }
        }
    }

    public void serializeTypedContents(List<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        int len = value.size();
        if (len != 0) {
            int i = 0;
            try {
                TypeSerializer typeSer = this._valueTypeSerializer;
                PropertySerializerMap serializers = this._dynamicSerializers;
                while (i < len) {
                    Object elem = value.get(i);
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    } else {
                        Class<?> cc = elem.getClass();
                        JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                        if (serializer == null) {
                            if (this._elementType.hasGenericTypes()) {
                                serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
                            } else {
                                serializer = _findAndAddDynamic(serializers, (Class) cc, provider);
                            }
                            serializers = this._dynamicSerializers;
                        }
                        serializer.serializeWithType(elem, jgen, provider, typeSer);
                    }
                    i++;
                }
            } catch (Exception e) {
                wrapAndThrow(provider, (Throwable) e, (Object) value, i);
            }
        }
    }
}
