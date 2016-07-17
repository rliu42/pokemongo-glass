package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.Annotations;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

public final class ManagedReferenceProperty extends SettableBeanProperty {
    private static final long serialVersionUID = 1;
    protected final SettableBeanProperty _backProperty;
    protected final boolean _isContainer;
    protected final SettableBeanProperty _managedProperty;
    protected final String _referenceName;

    public ManagedReferenceProperty(SettableBeanProperty forward, String refName, SettableBeanProperty backward, Annotations contextAnnotations, boolean isContainer) {
        super(forward.getFullName(), forward.getType(), forward.getWrapperName(), forward.getValueTypeDeserializer(), contextAnnotations, forward.getMetadata());
        this._referenceName = refName;
        this._managedProperty = forward;
        this._backProperty = backward;
        this._isContainer = isContainer;
    }

    protected ManagedReferenceProperty(ManagedReferenceProperty src, JsonDeserializer<?> deser) {
        super((SettableBeanProperty) src, (JsonDeserializer) deser);
        this._referenceName = src._referenceName;
        this._isContainer = src._isContainer;
        this._managedProperty = src._managedProperty;
        this._backProperty = src._backProperty;
    }

    protected ManagedReferenceProperty(ManagedReferenceProperty src, PropertyName newName) {
        super((SettableBeanProperty) src, newName);
        this._referenceName = src._referenceName;
        this._isContainer = src._isContainer;
        this._managedProperty = src._managedProperty;
        this._backProperty = src._backProperty;
    }

    public ManagedReferenceProperty withName(PropertyName newName) {
        return new ManagedReferenceProperty(this, newName);
    }

    public ManagedReferenceProperty withValueDeserializer(JsonDeserializer<?> deser) {
        return new ManagedReferenceProperty(this, (JsonDeserializer) deser);
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._managedProperty.getAnnotation(acls);
    }

    public AnnotatedMember getMember() {
        return this._managedProperty.getMember();
    }

    public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        set(instance, this._managedProperty.deserialize(p, ctxt));
    }

    public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        return setAndReturn(instance, deserialize(p, ctxt));
    }

    public final void set(Object instance, Object value) throws IOException {
        setAndReturn(instance, value);
    }

    public Object setAndReturn(Object instance, Object value) throws IOException {
        if (value != null) {
            if (!this._isContainer) {
                this._backProperty.set(value, instance);
            } else if (value instanceof Object[]) {
                for (Object ob : (Object[]) value) {
                    if (ob != null) {
                        this._backProperty.set(ob, instance);
                    }
                }
            } else if (value instanceof Collection) {
                for (Object ob2 : (Collection) value) {
                    if (ob2 != null) {
                        this._backProperty.set(ob2, instance);
                    }
                }
            } else if (value instanceof Map) {
                for (Object ob22 : ((Map) value).values()) {
                    if (ob22 != null) {
                        this._backProperty.set(ob22, instance);
                    }
                }
            } else {
                throw new IllegalStateException("Unsupported container type (" + value.getClass().getName() + ") when resolving reference '" + this._referenceName + "'");
            }
        }
        return this._managedProperty.setAndReturn(instance, value);
    }
}
