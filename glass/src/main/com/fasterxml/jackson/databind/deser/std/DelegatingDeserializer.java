package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.util.Collection;

public abstract class DelegatingDeserializer extends StdDeserializer<Object> implements ContextualDeserializer, ResolvableDeserializer {
    private static final long serialVersionUID = 1;
    protected final JsonDeserializer<?> _delegatee;

    protected abstract JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> jsonDeserializer);

    public DelegatingDeserializer(JsonDeserializer<?> delegatee) {
        super(_figureType(delegatee));
        this._delegatee = delegatee;
    }

    private static Class<?> _figureType(JsonDeserializer<?> deser) {
        Class<?> cls = deser.handledType();
        return cls != null ? cls : Object.class;
    }

    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        if (this._delegatee instanceof ResolvableDeserializer) {
            ((ResolvableDeserializer) this._delegatee).resolve(ctxt);
        }
    }

    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<?> del = ctxt.handleSecondaryContextualization(this._delegatee, property, ctxt.constructType(this._delegatee.handledType()));
        return del == this._delegatee ? this : newDelegatingInstance(del);
    }

    @Deprecated
    protected JsonDeserializer<?> _createContextual(DeserializationContext ctxt, BeanProperty property, JsonDeserializer<?> newDelegatee) {
        return newDelegatee == this._delegatee ? this : newDelegatingInstance(newDelegatee);
    }

    public SettableBeanProperty findBackReference(String logicalName) {
        return this._delegatee.findBackReference(logicalName);
    }

    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return this._delegatee.deserialize(jp, ctxt);
    }

    public Object deserialize(JsonParser jp, DeserializationContext ctxt, Object intoValue) throws IOException, JsonProcessingException {
        return this._delegatee.deserialize(jp, ctxt, intoValue);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return this._delegatee.deserializeWithType(jp, ctxt, typeDeserializer);
    }

    public JsonDeserializer<?> replaceDelegatee(JsonDeserializer<?> delegatee) {
        return delegatee == this._delegatee ? this : newDelegatingInstance(delegatee);
    }

    public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return this._delegatee.getNullValue(ctxt);
    }

    public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
        return this._delegatee.getEmptyValue(ctxt);
    }

    @Deprecated
    public Object getNullValue() {
        return this._delegatee.getNullValue();
    }

    @Deprecated
    public Object getEmptyValue() {
        return this._delegatee.getEmptyValue();
    }

    public Collection<Object> getKnownPropertyNames() {
        return this._delegatee.getKnownPropertyNames();
    }

    public boolean isCachable() {
        return this._delegatee.isCachable();
    }

    public ObjectIdReader getObjectIdReader() {
        return this._delegatee.getObjectIdReader();
    }

    public JsonDeserializer<?> getDelegatee() {
        return this._delegatee;
    }
}
