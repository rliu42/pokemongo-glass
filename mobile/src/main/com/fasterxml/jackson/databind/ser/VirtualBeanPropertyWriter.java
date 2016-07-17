package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.Annotations;
import java.io.Serializable;
import java.lang.reflect.Type;

public abstract class VirtualBeanPropertyWriter extends BeanPropertyWriter implements Serializable {
    private static final long serialVersionUID = 1;

    protected abstract Object value(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws Exception;

    public abstract VirtualBeanPropertyWriter withConfig(MapperConfig<?> mapperConfig, AnnotatedClass annotatedClass, BeanPropertyDefinition beanPropertyDefinition, JavaType javaType);

    protected VirtualBeanPropertyWriter(BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType) {
        this(propDef, contextAnnotations, declaredType, null, null, null, propDef.findInclusion());
    }

    protected VirtualBeanPropertyWriter() {
    }

    protected VirtualBeanPropertyWriter(BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serType, Include inclusion) {
        super(propDef, propDef.getPrimaryMember(), contextAnnotations, declaredType, ser, typeSer, serType, _suppressNulls(inclusion), _suppressableValue(inclusion));
    }

    protected VirtualBeanPropertyWriter(VirtualBeanPropertyWriter base) {
        super(base);
    }

    protected VirtualBeanPropertyWriter(VirtualBeanPropertyWriter base, PropertyName name) {
        super((BeanPropertyWriter) base, name);
    }

    protected static boolean _suppressNulls(Include inclusion) {
        return inclusion != Include.ALWAYS;
    }

    protected static Object _suppressableValue(Include inclusion) {
        if (inclusion == Include.NON_EMPTY || inclusion == Include.NON_EMPTY) {
            return MARKER_FOR_EMPTY;
        }
        return null;
    }

    public boolean isVirtual() {
        return true;
    }

    public Class<?> getPropertyType() {
        return this._declaredType.getRawClass();
    }

    public Type getGenericPropertyType() {
        return getPropertyType();
    }

    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
        Object value = value(bean, gen, prov);
        if (value != null) {
            JsonSerializer<Object> ser = this._serializer;
            if (ser == null) {
                Class<?> cls = value.getClass();
                PropertySerializerMap m = this._dynamicSerializers;
                ser = m.serializerFor(cls);
                if (ser == null) {
                    ser = _findAndAddDynamic(m, cls, prov);
                }
            }
            if (this._suppressableValue != null) {
                if (MARKER_FOR_EMPTY == this._suppressableValue) {
                    if (ser.isEmpty(prov, value)) {
                        return;
                    }
                } else if (this._suppressableValue.equals(value)) {
                    return;
                }
            }
            if (value != bean || !_handleSelfReference(bean, gen, prov, ser)) {
                gen.writeFieldName(this._name);
                if (this._typeSerializer == null) {
                    ser.serialize(value, gen, prov);
                } else {
                    ser.serializeWithType(value, gen, prov, this._typeSerializer);
                }
            }
        } else if (this._nullSerializer != null) {
            gen.writeFieldName(this._name);
            this._nullSerializer.serialize(null, gen, prov);
        }
    }

    public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
        Object value = value(bean, gen, prov);
        if (value != null) {
            JsonSerializer<Object> ser = this._serializer;
            if (ser == null) {
                Class<?> cls = value.getClass();
                PropertySerializerMap map = this._dynamicSerializers;
                ser = map.serializerFor(cls);
                if (ser == null) {
                    ser = _findAndAddDynamic(map, cls, prov);
                }
            }
            if (this._suppressableValue != null) {
                if (MARKER_FOR_EMPTY == this._suppressableValue) {
                    if (ser.isEmpty(prov, value)) {
                        serializeAsPlaceholder(bean, gen, prov);
                        return;
                    }
                } else if (this._suppressableValue.equals(value)) {
                    serializeAsPlaceholder(bean, gen, prov);
                    return;
                }
            }
            if (value != bean || !_handleSelfReference(bean, gen, prov, ser)) {
                if (this._typeSerializer == null) {
                    ser.serialize(value, gen, prov);
                } else {
                    ser.serializeWithType(value, gen, prov, this._typeSerializer);
                }
            }
        } else if (this._nullSerializer != null) {
            this._nullSerializer.serialize(null, gen, prov);
        } else {
            gen.writeNull();
        }
    }
}
