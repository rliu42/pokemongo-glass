package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public final class FieldProperty extends SettableBeanProperty {
    private static final long serialVersionUID = 1;
    protected final AnnotatedField _annotated;
    protected final transient Field _field;

    public FieldProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedField field) {
        super(propDef, type, typeDeser, contextAnnotations);
        this._annotated = field;
        this._field = field.getAnnotated();
    }

    protected FieldProperty(FieldProperty src, JsonDeserializer<?> deser) {
        super((SettableBeanProperty) src, (JsonDeserializer) deser);
        this._annotated = src._annotated;
        this._field = src._field;
    }

    protected FieldProperty(FieldProperty src, PropertyName newName) {
        super((SettableBeanProperty) src, newName);
        this._annotated = src._annotated;
        this._field = src._field;
    }

    protected FieldProperty(FieldProperty src) {
        super(src);
        this._annotated = src._annotated;
        Field f = this._annotated.getAnnotated();
        if (f == null) {
            throw new IllegalArgumentException("Missing field (broken JDK (de)serialization?)");
        }
        this._field = f;
    }

    public FieldProperty withName(PropertyName newName) {
        return new FieldProperty(this, newName);
    }

    public FieldProperty withValueDeserializer(JsonDeserializer<?> deser) {
        return new FieldProperty(this, (JsonDeserializer) deser);
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._annotated == null ? null : this._annotated.getAnnotation(acls);
    }

    public AnnotatedMember getMember() {
        return this._annotated;
    }

    public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException {
        Object value = deserialize(jp, ctxt);
        try {
            this._field.set(instance, value);
        } catch (Exception e) {
            _throwAsIOE(e, value);
        }
    }

    public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException {
        Object value = deserialize(jp, ctxt);
        try {
            this._field.set(instance, value);
        } catch (Exception e) {
            _throwAsIOE(e, value);
        }
        return instance;
    }

    public final void set(Object instance, Object value) throws IOException {
        try {
            this._field.set(instance, value);
        } catch (Exception e) {
            _throwAsIOE(e, value);
        }
    }

    public Object setAndReturn(Object instance, Object value) throws IOException {
        try {
            this._field.set(instance, value);
        } catch (Exception e) {
            _throwAsIOE(e, value);
        }
        return instance;
    }

    Object readResolve() {
        return new FieldProperty(this);
    }
}
