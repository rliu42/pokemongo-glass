package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public final class InnerClassProperty extends SettableBeanProperty {
    private static final long serialVersionUID = 1;
    protected AnnotatedConstructor _annotated;
    protected final transient Constructor<?> _creator;
    protected final SettableBeanProperty _delegate;

    public InnerClassProperty(SettableBeanProperty delegate, Constructor<?> ctor) {
        super(delegate);
        this._delegate = delegate;
        this._creator = ctor;
    }

    protected InnerClassProperty(InnerClassProperty src, AnnotatedConstructor ann) {
        super(src);
        this._delegate = src._delegate;
        this._annotated = ann;
        this._creator = this._annotated == null ? null : this._annotated.getAnnotated();
        if (this._creator == null) {
            throw new IllegalArgumentException("Missing constructor (broken JDK (de)serialization?)");
        }
    }

    protected InnerClassProperty(InnerClassProperty src, JsonDeserializer<?> deser) {
        super((SettableBeanProperty) src, (JsonDeserializer) deser);
        this._delegate = src._delegate.withValueDeserializer(deser);
        this._creator = src._creator;
    }

    protected InnerClassProperty(InnerClassProperty src, PropertyName newName) {
        super((SettableBeanProperty) src, newName);
        this._delegate = src._delegate.withName(newName);
        this._creator = src._creator;
    }

    public InnerClassProperty withName(PropertyName newName) {
        return new InnerClassProperty(this, newName);
    }

    public InnerClassProperty withValueDeserializer(JsonDeserializer<?> deser) {
        return new InnerClassProperty(this, (JsonDeserializer) deser);
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._delegate.getAnnotation(acls);
    }

    public AnnotatedMember getMember() {
        return this._delegate.getMember();
    }

    public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object bean) throws IOException {
        Object nullValue;
        if (jp.getCurrentToken() == JsonToken.VALUE_NULL) {
            nullValue = this._valueDeserializer.getNullValue(ctxt);
        } else if (this._valueTypeDeserializer != null) {
            nullValue = this._valueDeserializer.deserializeWithType(jp, ctxt, this._valueTypeDeserializer);
        } else {
            try {
                nullValue = this._creator.newInstance(new Object[]{bean});
            } catch (Exception e) {
                ClassUtil.unwrapAndThrowAsIAE(e, "Failed to instantiate class " + this._creator.getDeclaringClass().getName() + ", problem: " + e.getMessage());
                nullValue = null;
            }
            this._valueDeserializer.deserialize(jp, ctxt, nullValue);
        }
        set(bean, nullValue);
    }

    public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException {
        return setAndReturn(instance, deserialize(jp, ctxt));
    }

    public final void set(Object instance, Object value) throws IOException {
        this._delegate.set(instance, value);
    }

    public Object setAndReturn(Object instance, Object value) throws IOException {
        return this._delegate.setAndReturn(instance, value);
    }

    Object readResolve() {
        return new InnerClassProperty(this, this._annotated);
    }

    Object writeReplace() {
        return this._annotated != null ? this : new InnerClassProperty(this, new AnnotatedConstructor(null, this._creator, null, null));
    }
}
