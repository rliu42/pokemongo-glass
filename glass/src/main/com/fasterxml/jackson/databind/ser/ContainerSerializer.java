package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public abstract class ContainerSerializer<T> extends StdSerializer<T> {
    protected abstract ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer typeSerializer);

    public abstract JsonSerializer<?> getContentSerializer();

    public abstract JavaType getContentType();

    public abstract boolean hasSingleElement(T t);

    protected ContainerSerializer(Class<T> t) {
        super((Class) t);
    }

    protected ContainerSerializer(JavaType fullType) {
        super(fullType);
    }

    protected ContainerSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected ContainerSerializer(ContainerSerializer<?> src) {
        super(src._handledType, false);
    }

    public ContainerSerializer<?> withValueTypeSerializer(TypeSerializer vts) {
        return vts == null ? this : _withValueTypeSerializer(vts);
    }

    @Deprecated
    public boolean isEmpty(T value) {
        return isEmpty(null, value);
    }

    protected boolean hasContentTypeAnnotation(SerializerProvider provider, BeanProperty property) {
        if (property != null) {
            AnnotationIntrospector intr = provider.getAnnotationIntrospector();
            AnnotatedMember m = property.getMember();
            if (!(m == null || intr == null || intr.findSerializationContentType(m, property.getType()) == null)) {
                return true;
            }
        }
        return false;
    }
}
