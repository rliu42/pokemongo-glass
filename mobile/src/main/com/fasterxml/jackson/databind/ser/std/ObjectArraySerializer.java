package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
import com.fasterxml.jackson.databind.type.ArrayType;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

@JacksonStdImpl
public class ObjectArraySerializer extends ArraySerializerBase<Object[]> implements ContextualSerializer {
    protected PropertySerializerMap _dynamicSerializers;
    protected JsonSerializer<Object> _elementSerializer;
    protected final JavaType _elementType;
    protected final boolean _staticTyping;
    protected final TypeSerializer _valueTypeSerializer;

    public ObjectArraySerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> elementSerializer) {
        super(Object[].class);
        this._elementType = elemType;
        this._staticTyping = staticTyping;
        this._valueTypeSerializer = vts;
        this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
        this._elementSerializer = elementSerializer;
    }

    public ObjectArraySerializer(ObjectArraySerializer src, TypeSerializer vts) {
        super((ArraySerializerBase) src);
        this._elementType = src._elementType;
        this._valueTypeSerializer = vts;
        this._staticTyping = src._staticTyping;
        this._dynamicSerializers = src._dynamicSerializers;
        this._elementSerializer = src._elementSerializer;
    }

    public ObjectArraySerializer(ObjectArraySerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle) {
        super(src, property, unwrapSingle);
        this._elementType = src._elementType;
        this._valueTypeSerializer = vts;
        this._staticTyping = src._staticTyping;
        this._dynamicSerializers = src._dynamicSerializers;
        this._elementSerializer = elementSerializer;
    }

    public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
        return new ObjectArraySerializer(this, prop, this._valueTypeSerializer, this._elementSerializer, unwrapSingle);
    }

    public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
        return new ObjectArraySerializer(this._elementType, this._staticTyping, vts, this._elementSerializer);
    }

    public ObjectArraySerializer withResolved(BeanProperty prop, TypeSerializer vts, JsonSerializer<?> ser, Boolean unwrapSingle) {
        return (this._property == prop && ser == this._elementSerializer && this._valueTypeSerializer == vts && this._unwrapSingle == unwrapSingle) ? this : new ObjectArraySerializer(this, prop, vts, ser, unwrapSingle);
    }

    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        TypeSerializer vts = this._valueTypeSerializer;
        if (vts != null) {
            vts = vts.forProperty(property);
        }
        JsonSerializer<?> ser = null;
        Boolean unwrapSingle = null;
        if (property != null) {
            AnnotatedMember m = property.getMember();
            AnnotationIntrospector intr = provider.getAnnotationIntrospector();
            if (m != null) {
                Object serDef = intr.findContentSerializer(m);
                if (serDef != null) {
                    ser = provider.serializerInstance(m, serDef);
                }
            }
            Value format = property.findFormatOverrides(intr);
            if (format != null) {
                unwrapSingle = format.getFeature(Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
            }
        }
        if (ser == null) {
            ser = this._elementSerializer;
        }
        ser = findConvertingContentSerializer(provider, property, ser);
        if (ser != null) {
            ser = provider.handleSecondaryContextualization(ser, property);
        } else if (this._elementType != null && (this._staticTyping || hasContentTypeAnnotation(provider, property))) {
            ser = provider.findValueSerializer(this._elementType, property);
        }
        return withResolved(property, vts, ser, unwrapSingle);
    }

    public JavaType getContentType() {
        return this._elementType;
    }

    public JsonSerializer<?> getContentSerializer() {
        return this._elementSerializer;
    }

    public boolean isEmpty(SerializerProvider prov, Object[] value) {
        return value == null || value.length == 0;
    }

    public boolean hasSingleElement(Object[] value) {
        return value.length == 1;
    }

    public final void serialize(Object[] value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        int len = value.length;
        if (len == 1 && ((this._unwrapSingle == null && provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) || this._unwrapSingle == Boolean.TRUE)) {
            serializeContents(value, gen, provider);
            return;
        }
        gen.writeStartArray(len);
        serializeContents(value, gen, provider);
        gen.writeEndArray();
    }

    public void serializeContents(Object[] value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        int len = value.length;
        if (len != 0) {
            if (this._elementSerializer != null) {
                serializeContentsUsing(value, gen, provider, this._elementSerializer);
            } else if (this._valueTypeSerializer != null) {
                serializeTypedContents(value, gen, provider);
            } else {
                int i = 0;
                Object obj = null;
                try {
                    PropertySerializerMap serializers = this._dynamicSerializers;
                    while (i < len) {
                        obj = value[i];
                        if (obj == null) {
                            provider.defaultSerializeNull(gen);
                        } else {
                            Class cc = obj.getClass();
                            JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                            if (serializer == null) {
                                if (this._elementType.hasGenericTypes()) {
                                    serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
                                } else {
                                    serializer = _findAndAddDynamic(serializers, cc, provider);
                                }
                            }
                            serializer.serialize(obj, gen, provider);
                        }
                        i++;
                    }
                } catch (IOException ioe) {
                    throw ioe;
                } catch (Throwable e) {
                    Throwable t = e;
                    while ((t instanceof InvocationTargetException) && t.getCause() != null) {
                        t = t.getCause();
                    }
                    if (t instanceof Error) {
                        throw ((Error) t);
                    }
                    throw JsonMappingException.wrapWithPath(t, obj, i);
                }
            }
        }
    }

    public void serializeContentsUsing(Object[] value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException {
        int len = value.length;
        TypeSerializer typeSer = this._valueTypeSerializer;
        int i = 0;
        Object obj = null;
        while (i < len) {
            try {
                obj = value[i];
                if (obj == null) {
                    provider.defaultSerializeNull(jgen);
                } else if (typeSer == null) {
                    ser.serialize(obj, jgen, provider);
                } else {
                    ser.serializeWithType(obj, jgen, provider, typeSer);
                }
                i++;
            } catch (IOException ioe) {
                throw ioe;
            } catch (Throwable e) {
                Throwable t = e;
                while ((t instanceof InvocationTargetException) && t.getCause() != null) {
                    t = t.getCause();
                }
                if (t instanceof Error) {
                    throw ((Error) t);
                }
                throw JsonMappingException.wrapWithPath(t, obj, i);
            }
        }
    }

    public void serializeTypedContents(Object[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        int len = value.length;
        TypeSerializer typeSer = this._valueTypeSerializer;
        int i = 0;
        try {
            PropertySerializerMap serializers = this._dynamicSerializers;
            while (i < len) {
                Object elem = value[i];
                if (elem == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    Class cc = elem.getClass();
                    JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                    if (serializer == null) {
                        serializer = _findAndAddDynamic(serializers, cc, provider);
                    }
                    serializer.serializeWithType(elem, jgen, provider, typeSer);
                }
                i++;
            }
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            Throwable t = e;
            while ((t instanceof InvocationTargetException) && t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof Error) {
                throw ((Error) t);
            }
            throw JsonMappingException.wrapWithPath(t, null, 0);
        }
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        ObjectNode o = createSchemaNode("array", true);
        if (typeHint != null) {
            JavaType javaType = provider.constructType(typeHint);
            if (javaType.isArrayType()) {
                Class componentType = ((ArrayType) javaType).getContentType().getRawClass();
                if (componentType == Object.class) {
                    o.set("items", JsonSchema.getDefaultSchemaNode());
                } else {
                    JsonSerializer<Object> ser = provider.findValueSerializer(componentType, this._property);
                    o.set("items", ser instanceof SchemaAware ? ((SchemaAware) ser).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode());
                }
            }
        }
        return o;
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        JsonArrayFormatVisitor arrayVisitor = visitor.expectArrayFormat(typeHint);
        if (arrayVisitor != null) {
            JavaType contentType = visitor.getProvider().getTypeFactory().moreSpecificType(this._elementType, typeHint.getContentType());
            if (contentType == null) {
                throw new JsonMappingException("Could not resolve type");
            }
            JsonSerializer<?> valueSer = this._elementSerializer;
            if (valueSer == null) {
                valueSer = visitor.getProvider().findValueSerializer(contentType, this._property);
            }
            arrayVisitor.itemsFormat(valueSer, contentType);
        }
    }

    protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
        SerializerAndMapResult result = map.findAndAddSecondarySerializer((Class) type, provider, this._property);
        if (map != result.map) {
            this._dynamicSerializers = result.map;
        }
        return result.serializer;
    }

    protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider) throws JsonMappingException {
        SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
        if (map != result.map) {
            this._dynamicSerializers = result.map;
        }
        return result.serializer;
    }
}
