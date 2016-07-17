package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class CreatorProperty extends SettableBeanProperty {
    private static final long serialVersionUID = 1;
    protected final AnnotatedParameter _annotated;
    protected final int _creatorIndex;
    protected SettableBeanProperty _fallbackSetter;
    protected final Object _injectableValueId;

    public CreatorProperty(PropertyName name, JavaType type, PropertyName wrapperName, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedParameter param, int index, Object injectableValueId, PropertyMetadata metadata) {
        super(name, type, wrapperName, typeDeser, contextAnnotations, metadata);
        this._annotated = param;
        this._creatorIndex = index;
        this._injectableValueId = injectableValueId;
        this._fallbackSetter = null;
    }

    protected CreatorProperty(CreatorProperty src, PropertyName newName) {
        super((SettableBeanProperty) src, newName);
        this._annotated = src._annotated;
        this._creatorIndex = src._creatorIndex;
        this._injectableValueId = src._injectableValueId;
        this._fallbackSetter = src._fallbackSetter;
    }

    protected CreatorProperty(CreatorProperty src, JsonDeserializer<?> deser) {
        super((SettableBeanProperty) src, (JsonDeserializer) deser);
        this._annotated = src._annotated;
        this._creatorIndex = src._creatorIndex;
        this._injectableValueId = src._injectableValueId;
        this._fallbackSetter = src._fallbackSetter;
    }

    public CreatorProperty withName(PropertyName newName) {
        return new CreatorProperty(this, newName);
    }

    public CreatorProperty withValueDeserializer(JsonDeserializer<?> deser) {
        return new CreatorProperty(this, (JsonDeserializer) deser);
    }

    public void setFallbackSetter(SettableBeanProperty fallbackSetter) {
        this._fallbackSetter = fallbackSetter;
    }

    public Object findInjectableValue(DeserializationContext context, Object beanInstance) {
        if (this._injectableValueId != null) {
            return context.findInjectableValue(this._injectableValueId, this, beanInstance);
        }
        throw new IllegalStateException("Property '" + getName() + "' (type " + getClass().getName() + ") has no injectable value id configured");
    }

    public void inject(DeserializationContext context, Object beanInstance) throws IOException {
        set(beanInstance, findInjectableValue(context, beanInstance));
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        if (this._annotated == null) {
            return null;
        }
        return this._annotated.getAnnotation(acls);
    }

    public AnnotatedMember getMember() {
        return this._annotated;
    }

    public int getCreatorIndex() {
        return this._creatorIndex;
    }

    public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException, JsonProcessingException {
        set(instance, deserialize(jp, ctxt));
    }

    public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException, JsonProcessingException {
        return setAndReturn(instance, deserialize(jp, ctxt));
    }

    public void set(Object instance, Object value) throws IOException {
        if (this._fallbackSetter == null) {
            throw new IllegalStateException("No fallback setter/field defined: can not use creator property for " + getClass().getName());
        }
        this._fallbackSetter.set(instance, value);
    }

    public Object setAndReturn(Object instance, Object value) throws IOException {
        if (this._fallbackSetter != null) {
            return this._fallbackSetter.setAndReturn(instance, value);
        }
        throw new IllegalStateException("No fallback setter/field defined: can not use creator property for " + getClass().getName());
    }

    public Object getInjectableValueId() {
        return this._injectableValueId;
    }

    public String toString() {
        return "[creator property, name '" + getName() + "'; inject id '" + this._injectableValueId + "']";
    }
}
