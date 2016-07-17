package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class CollectionSerializer extends AsArraySerializerBase<Collection<?>> {
    private static final long serialVersionUID = 1;

    public CollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> valueSerializer) {
        super(Collection.class, elemType, staticTyping, vts, (JsonSerializer) valueSerializer);
    }

    @Deprecated
    public CollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer) {
        this(elemType, staticTyping, vts, valueSerializer);
    }

    public CollectionSerializer(CollectionSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer, Boolean unwrapSingle) {
        super((AsArraySerializerBase) src, property, vts, (JsonSerializer) valueSerializer, unwrapSingle);
    }

    public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
        return new CollectionSerializer(this, this._property, vts, this._elementSerializer, this._unwrapSingle);
    }

    public CollectionSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle) {
        return new CollectionSerializer(this, property, vts, (JsonSerializer) elementSerializer, unwrapSingle);
    }

    public boolean isEmpty(SerializerProvider prov, Collection<?> value) {
        return value == null || value.isEmpty();
    }

    public boolean hasSingleElement(Collection<?> value) {
        Iterator<?> it = value.iterator();
        if (!it.hasNext()) {
            return false;
        }
        it.next();
        if (it.hasNext()) {
            return false;
        }
        return true;
    }

    public final void serialize(Collection<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        int len = value.size();
        if (len == 1 && ((this._unwrapSingle == null && provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) || this._unwrapSingle == Boolean.TRUE)) {
            serializeContents((Collection) value, jgen, provider);
            return;
        }
        jgen.writeStartArray(len);
        serializeContents((Collection) value, jgen, provider);
        jgen.writeEndArray();
    }

    public void serializeContents(Collection<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (this._elementSerializer != null) {
            serializeContentsUsing(value, jgen, provider, this._elementSerializer);
            return;
        }
        Iterator<?> it = value.iterator();
        if (it.hasNext()) {
            PropertySerializerMap serializers = this._dynamicSerializers;
            TypeSerializer typeSer = this._valueTypeSerializer;
            int i = 0;
            do {
                Object elem = it.next();
                if (elem == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    Class<?> cc = elem.getClass();
                    JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                    if (serializer == null) {
                        if (this._elementType.hasGenericTypes()) {
                            serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
                        } else {
                            try {
                                serializer = _findAndAddDynamic(serializers, (Class) cc, provider);
                            } catch (Exception e) {
                                wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                                return;
                            }
                        }
                        serializers = this._dynamicSerializers;
                    }
                    if (typeSer == null) {
                        serializer.serialize(elem, jgen, provider);
                    } else {
                        serializer.serializeWithType(elem, jgen, provider, typeSer);
                    }
                }
                i++;
            } while (it.hasNext());
        }
    }

    public void serializeContentsUsing(Collection<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException, JsonGenerationException {
        Iterator<?> it = value.iterator();
        if (it.hasNext()) {
            TypeSerializer typeSer = this._valueTypeSerializer;
            int i = 0;
            do {
                Object elem = it.next();
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
                i++;
            } while (it.hasNext());
        }
    }
}
