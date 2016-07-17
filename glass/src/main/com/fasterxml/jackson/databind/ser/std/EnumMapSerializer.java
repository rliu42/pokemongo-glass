package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map.Entry;

@JacksonStdImpl
@Deprecated
public class EnumMapSerializer extends ContainerSerializer<EnumMap<? extends Enum<?>, ?>> implements ContextualSerializer {
    protected final EnumValues _keyEnums;
    protected final BeanProperty _property;
    protected final boolean _staticTyping;
    protected final JsonSerializer<Object> _valueSerializer;
    protected final JavaType _valueType;
    protected final TypeSerializer _valueTypeSerializer;

    public EnumMapSerializer(JavaType valueType, boolean staticTyping, EnumValues keyEnums, TypeSerializer vts, JsonSerializer<Object> valueSerializer) {
        boolean z = false;
        super(EnumMap.class, false);
        this._property = null;
        if (staticTyping || (valueType != null && valueType.isFinal())) {
            z = true;
        }
        this._staticTyping = z;
        this._valueType = valueType;
        this._keyEnums = keyEnums;
        this._valueTypeSerializer = vts;
        this._valueSerializer = valueSerializer;
    }

    public EnumMapSerializer(EnumMapSerializer src, BeanProperty property, JsonSerializer<?> ser) {
        super((ContainerSerializer) src);
        this._property = property;
        this._staticTyping = src._staticTyping;
        this._valueType = src._valueType;
        this._keyEnums = src._keyEnums;
        this._valueTypeSerializer = src._valueTypeSerializer;
        this._valueSerializer = ser;
    }

    public EnumMapSerializer _withValueTypeSerializer(TypeSerializer vts) {
        return new EnumMapSerializer(this._valueType, this._staticTyping, this._keyEnums, vts, this._valueSerializer);
    }

    public EnumMapSerializer withValueSerializer(BeanProperty prop, JsonSerializer<?> ser) {
        return (this._property == prop && ser == this._valueSerializer) ? this : new EnumMapSerializer(this, prop, ser);
    }

    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        JsonSerializer<?> ser = null;
        if (property != null) {
            AnnotatedMember m = property.getMember();
            if (m != null) {
                Object serDef = provider.getAnnotationIntrospector().findContentSerializer(m);
                if (serDef != null) {
                    ser = provider.serializerInstance(m, serDef);
                }
            }
        }
        if (ser == null) {
            ser = this._valueSerializer;
        }
        ser = findConvertingContentSerializer(provider, property, ser);
        if (ser != null) {
            ser = provider.handleSecondaryContextualization(ser, property);
        } else if (this._staticTyping) {
            return withValueSerializer(property, provider.findValueSerializer(this._valueType, property));
        }
        if (ser != this._valueSerializer) {
            return withValueSerializer(property, ser);
        }
        return this;
    }

    public JavaType getContentType() {
        return this._valueType;
    }

    public JsonSerializer<?> getContentSerializer() {
        return this._valueSerializer;
    }

    public boolean isEmpty(SerializerProvider prov, EnumMap<? extends Enum<?>, ?> value) {
        return value == null || value.isEmpty();
    }

    public boolean hasSingleElement(EnumMap<? extends Enum<?>, ?> value) {
        return value.size() == 1;
    }

    public void serialize(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        if (!value.isEmpty()) {
            serializeContents(value, jgen, provider);
        }
        jgen.writeEndObject();
    }

    public void serializeWithType(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
        typeSer.writeTypePrefixForObject(value, jgen);
        if (!value.isEmpty()) {
            serializeContents(value, jgen, provider);
        }
        typeSer.writeTypeSuffixForObject(value, jgen);
    }

    protected void serializeContents(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (this._valueSerializer != null) {
            serializeContentsUsing(value, jgen, provider, this._valueSerializer);
            return;
        }
        JsonSerializer<Object> prevSerializer = null;
        Class<?> prevClass = null;
        EnumValues keyEnums = this._keyEnums;
        boolean skipNulls = !provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
        boolean useToString = provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        TypeSerializer vts = this._valueTypeSerializer;
        for (Entry<? extends Enum<?>, ?> entry : value.entrySet()) {
            Object valueElem = entry.getValue();
            if (!skipNulls || valueElem != null) {
                Enum<?> key = (Enum) entry.getKey();
                if (useToString) {
                    jgen.writeFieldName(key.toString());
                } else {
                    if (keyEnums == null) {
                        keyEnums = ((EnumSerializer) ((StdSerializer) provider.findValueSerializer(key.getDeclaringClass(), this._property))).getEnumValues();
                    }
                    jgen.writeFieldName(keyEnums.serializedValueFor(key));
                }
                if (valueElem == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    JsonSerializer<Object> currSerializer;
                    Class<?> cc = valueElem.getClass();
                    if (cc == prevClass) {
                        currSerializer = prevSerializer;
                    } else {
                        currSerializer = provider.findValueSerializer((Class) cc, this._property);
                        prevSerializer = currSerializer;
                        prevClass = cc;
                    }
                    if (vts == null) {
                        try {
                            currSerializer.serialize(valueElem, jgen, provider);
                        } catch (Exception e) {
                            wrapAndThrow(provider, (Throwable) e, (Object) value, ((Enum) entry.getKey()).name());
                        }
                    } else {
                        currSerializer.serializeWithType(valueElem, jgen, provider, vts);
                    }
                }
            }
        }
    }

    protected void serializeContentsUsing(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> valueSer) throws IOException, JsonGenerationException {
        EnumValues keyEnums = this._keyEnums;
        boolean skipNulls = !provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
        boolean useToString = provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        TypeSerializer vts = this._valueTypeSerializer;
        for (Entry<? extends Enum<?>, ?> entry : value.entrySet()) {
            Object valueElem = entry.getValue();
            if (!skipNulls || valueElem != null) {
                Enum<?> key = (Enum) entry.getKey();
                if (useToString) {
                    jgen.writeFieldName(key.toString());
                } else {
                    if (keyEnums == null) {
                        keyEnums = ((EnumSerializer) ((StdSerializer) provider.findValueSerializer(key.getDeclaringClass(), this._property))).getEnumValues();
                    }
                    jgen.writeFieldName(keyEnums.serializedValueFor(key));
                }
                if (valueElem == null) {
                    provider.defaultSerializeNull(jgen);
                } else if (vts == null) {
                    try {
                        valueSer.serialize(valueElem, jgen, provider);
                    } catch (Exception e) {
                        wrapAndThrow(provider, (Throwable) e, (Object) value, ((Enum) entry.getKey()).name());
                    }
                } else {
                    valueSer.serializeWithType(valueElem, jgen, provider, vts);
                }
            }
        }
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        ObjectNode o = createSchemaNode("object", true);
        if (typeHint instanceof ParameterizedType) {
            Type[] typeArgs = ((ParameterizedType) typeHint).getActualTypeArguments();
            if (typeArgs.length == 2) {
                JavaType enumType = provider.constructType(typeArgs[0]);
                JavaType valueType = provider.constructType(typeArgs[1]);
                ObjectNode propsNode = JsonNodeFactory.instance.objectNode();
                for (Enum<?> enumValue : (Enum[]) enumType.getRawClass().getEnumConstants()) {
                    JsonSerializer<Object> ser = provider.findValueSerializer(valueType.getRawClass(), this._property);
                    propsNode.set(provider.getConfig().getAnnotationIntrospector().findEnumValue(enumValue), ser instanceof SchemaAware ? ((SchemaAware) ser).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode());
                }
                o.set("properties", propsNode);
            }
        }
        return o;
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        if (visitor != null) {
            JsonObjectFormatVisitor objectVisitor = visitor.expectObjectFormat(typeHint);
            if (objectVisitor != null) {
                JavaType valueType = typeHint.containedType(1);
                JsonSerializer<Object> ser = this._valueSerializer;
                if (ser == null && valueType != null) {
                    ser = visitor.getProvider().findValueSerializer(valueType, this._property);
                }
                if (valueType == null) {
                    valueType = visitor.getProvider().constructType(Object.class);
                }
                EnumValues keyEnums = this._keyEnums;
                if (keyEnums == null) {
                    JavaType enumType = typeHint.containedType(0);
                    if (enumType == null) {
                        throw new IllegalStateException("Can not resolve Enum type of EnumMap: " + typeHint);
                    }
                    JsonSerializer<?> enumSer = visitor.getProvider().findValueSerializer(enumType, this._property);
                    if (enumSer instanceof EnumSerializer) {
                        keyEnums = ((EnumSerializer) enumSer).getEnumValues();
                    } else {
                        throw new IllegalStateException("Can not resolve Enum type of EnumMap: " + typeHint);
                    }
                }
                for (Entry<?, SerializableString> entry : keyEnums.internalMap().entrySet()) {
                    String name = ((SerializableString) entry.getValue()).getValue();
                    if (ser == null) {
                        ser = visitor.getProvider().findValueSerializer(entry.getKey().getClass(), this._property);
                    }
                    objectVisitor.property(name, ser, valueType);
                }
            }
        }
    }
}
