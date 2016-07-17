package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

@JacksonStdImpl
public class JsonValueSerializer extends StdSerializer<Object> implements ContextualSerializer, JsonFormatVisitable, SchemaAware {
    protected final Method _accessorMethod;
    protected final boolean _forceTypeInformation;
    protected final BeanProperty _property;
    protected final JsonSerializer<Object> _valueSerializer;

    public JsonValueSerializer(Method valueMethod, JsonSerializer<?> ser) {
        super(valueMethod.getReturnType(), false);
        this._accessorMethod = valueMethod;
        this._valueSerializer = ser;
        this._property = null;
        this._forceTypeInformation = true;
    }

    public JsonValueSerializer(JsonValueSerializer src, BeanProperty property, JsonSerializer<?> ser, boolean forceTypeInfo) {
        super(_notNullClass(src.handledType()));
        this._accessorMethod = src._accessorMethod;
        this._valueSerializer = ser;
        this._property = property;
        this._forceTypeInformation = forceTypeInfo;
    }

    private static final Class<Object> _notNullClass(Class<?> cls) {
        return cls == null ? Object.class : cls;
    }

    public JsonValueSerializer withResolved(BeanProperty property, JsonSerializer<?> ser, boolean forceTypeInfo) {
        return (this._property == property && this._valueSerializer == ser && forceTypeInfo == this._forceTypeInformation) ? this : new JsonValueSerializer(this, property, ser, forceTypeInfo);
    }

    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        JsonSerializer<?> ser = this._valueSerializer;
        if (ser != null) {
            return withResolved(property, provider.handlePrimaryContextualization(ser, property), this._forceTypeInformation);
        }
        if (!provider.isEnabled(MapperFeature.USE_STATIC_TYPING) && !Modifier.isFinal(this._accessorMethod.getReturnType().getModifiers())) {
            return this;
        }
        JavaType t = provider.constructType(this._accessorMethod.getGenericReturnType());
        ser = provider.findPrimaryPropertySerializer(t, property);
        return withResolved(property, ser, isNaturalTypeWithStdHandling(t.getRawClass(), ser));
    }

    public void serialize(Object bean, JsonGenerator jgen, SerializerProvider prov) throws IOException {
        try {
            Object value = this._accessorMethod.invoke(bean, new Object[0]);
            if (value == null) {
                prov.defaultSerializeNull(jgen);
                return;
            }
            JsonSerializer<Object> ser = this._valueSerializer;
            if (ser == null) {
                ser = prov.findTypedValueSerializer(value.getClass(), true, this._property);
            }
            ser.serialize(value, jgen, prov);
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
            throw JsonMappingException.wrapWithPath(t, bean, this._accessorMethod.getName() + "()");
        }
    }

    public void serializeWithType(Object bean, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer0) throws IOException {
        try {
            Object value = this._accessorMethod.invoke(bean, new Object[0]);
            if (value == null) {
                provider.defaultSerializeNull(jgen);
                return;
            }
            JsonSerializer<Object> ser = this._valueSerializer;
            if (ser == null) {
                ser = provider.findValueSerializer(value.getClass(), this._property);
            } else if (this._forceTypeInformation) {
                typeSer0.writeTypePrefixForScalar(bean, jgen);
                ser.serialize(value, jgen, provider);
                typeSer0.writeTypeSuffixForScalar(bean, jgen);
                return;
            }
            ser.serializeWithType(value, jgen, provider, typeSer0);
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
            throw JsonMappingException.wrapWithPath(t, bean, this._accessorMethod.getName() + "()");
        }
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        if (this._valueSerializer instanceof SchemaAware) {
            return ((SchemaAware) this._valueSerializer).getSchema(provider, null);
        }
        return JsonSchema.getDefaultSchemaNode();
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        Class<?> decl = typeHint == null ? null : typeHint.getRawClass();
        if (decl == null) {
            decl = this._accessorMethod.getDeclaringClass();
        }
        if (decl == null || !decl.isEnum() || !_acceptJsonFormatVisitorForEnum(visitor, typeHint, decl)) {
            JsonSerializer<Object> ser = this._valueSerializer;
            if (ser == null) {
                if (typeHint == null) {
                    if (this._property != null) {
                        typeHint = this._property.getType();
                    }
                    if (typeHint == null) {
                        typeHint = visitor.getProvider().constructType(this._handledType);
                    }
                }
                ser = visitor.getProvider().findTypedValueSerializer(typeHint, false, this._property);
                if (ser == null) {
                    visitor.expectAnyFormat(typeHint);
                    return;
                }
            }
            ser.acceptJsonFormatVisitor(visitor, null);
        }
    }

    protected boolean _acceptJsonFormatVisitorForEnum(JsonFormatVisitorWrapper visitor, JavaType typeHint, Class<?> enumType) throws JsonMappingException {
        JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(typeHint);
        if (stringVisitor != null) {
            Set<String> enums = new LinkedHashSet();
            Object[] arr$ = enumType.getEnumConstants();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Object en = arr$[i$];
                try {
                    enums.add(String.valueOf(this._accessorMethod.invoke(en, new Object[0])));
                    i$++;
                } catch (Throwable e) {
                    Throwable t = e;
                    while ((t instanceof InvocationTargetException) && t.getCause() != null) {
                        t = t.getCause();
                    }
                    if (t instanceof Error) {
                        throw ((Error) t);
                    }
                    throw JsonMappingException.wrapWithPath(t, en, this._accessorMethod.getName() + "()");
                }
            }
            stringVisitor.enumTypes(enums);
        }
        return true;
    }

    protected boolean isNaturalTypeWithStdHandling(Class<?> rawType, JsonSerializer<?> ser) {
        if (rawType.isPrimitive()) {
            if (!(rawType == Integer.TYPE || rawType == Boolean.TYPE || rawType == Double.TYPE)) {
                return false;
            }
        } else if (!(rawType == String.class || rawType == Integer.class || rawType == Boolean.class || rawType == Double.class)) {
            return false;
        }
        return isDefaultSerializer(ser);
    }

    public String toString() {
        return "(@JsonValue serializer for method " + this._accessorMethod.getDeclaringClass() + "#" + this._accessorMethod.getName() + ")";
    }
}
