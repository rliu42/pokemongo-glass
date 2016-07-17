package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import java.io.IOException;

public class StdDelegatingDeserializer<T> extends StdDeserializer<T> implements ContextualDeserializer, ResolvableDeserializer {
    private static final long serialVersionUID = 1;
    protected final Converter<Object, T> _converter;
    protected final JsonDeserializer<Object> _delegateDeserializer;
    protected final JavaType _delegateType;

    public StdDelegatingDeserializer(Converter<?, T> converter) {
        super(Object.class);
        this._converter = converter;
        this._delegateType = null;
        this._delegateDeserializer = null;
    }

    public StdDelegatingDeserializer(Converter<Object, T> converter, JavaType delegateType, JsonDeserializer<?> delegateDeserializer) {
        super(delegateType);
        this._converter = converter;
        this._delegateType = delegateType;
        this._delegateDeserializer = delegateDeserializer;
    }

    protected StdDelegatingDeserializer(StdDelegatingDeserializer<T> src) {
        super((StdDeserializer) src);
        this._converter = src._converter;
        this._delegateType = src._delegateType;
        this._delegateDeserializer = src._delegateDeserializer;
    }

    protected StdDelegatingDeserializer<T> withDelegate(Converter<Object, T> converter, JavaType delegateType, JsonDeserializer<?> delegateDeserializer) {
        if (getClass() == StdDelegatingDeserializer.class) {
            return new StdDelegatingDeserializer(converter, delegateType, delegateDeserializer);
        }
        throw new IllegalStateException("Sub-class " + getClass().getName() + " must override 'withDelegate'");
    }

    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        if (this._delegateDeserializer != null && (this._delegateDeserializer instanceof ResolvableDeserializer)) {
            ((ResolvableDeserializer) this._delegateDeserializer).resolve(ctxt);
        }
    }

    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (this._delegateDeserializer != null) {
            JsonDeserializer<?> deser = ctxt.handleSecondaryContextualization(this._delegateDeserializer, property, this._delegateType);
            if (deser != this._delegateDeserializer) {
                return withDelegate(this._converter, this._delegateType, deser);
            }
            return this;
        }
        JavaType delegateType = this._converter.getInputType(ctxt.getTypeFactory());
        return withDelegate(this._converter, delegateType, ctxt.findContextualValueDeserializer(delegateType, property));
    }

    public JsonDeserializer<?> getDelegatee() {
        return this._delegateDeserializer;
    }

    public Class<?> handledType() {
        return this._delegateDeserializer.handledType();
    }

    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Object delegateValue = this._delegateDeserializer.deserialize(jp, ctxt);
        if (delegateValue == null) {
            return null;
        }
        return convertValue(delegateValue);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        Object delegateValue = this._delegateDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        if (delegateValue == null) {
            return null;
        }
        return convertValue(delegateValue);
    }

    public T deserialize(JsonParser p, DeserializationContext ctxt, Object intoValue) throws IOException {
        if (this._delegateType.getRawClass().isAssignableFrom(intoValue.getClass())) {
            return this._delegateDeserializer.deserialize(p, ctxt, intoValue);
        }
        return _handleIncompatibleUpdateValue(p, ctxt, intoValue);
    }

    protected Object _handleIncompatibleUpdateValue(JsonParser p, DeserializationContext ctxt, Object intoValue) throws IOException {
        throw new UnsupportedOperationException(String.format("Can not update object of type %s (using deserializer for type %s)" + intoValue.getClass().getName(), new Object[]{this._delegateType}));
    }

    protected T convertValue(Object delegateValue) {
        return this._converter.convert(delegateValue);
    }
}
