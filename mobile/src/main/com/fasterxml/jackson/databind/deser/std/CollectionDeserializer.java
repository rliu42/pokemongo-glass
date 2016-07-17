package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@JacksonStdImpl
public class CollectionDeserializer extends ContainerDeserializerBase<Collection<Object>> implements ContextualDeserializer {
    private static final long serialVersionUID = -1;
    protected final JavaType _collectionType;
    protected final JsonDeserializer<Object> _delegateDeserializer;
    protected final JsonDeserializer<Object> _valueDeserializer;
    protected final ValueInstantiator _valueInstantiator;
    protected final TypeDeserializer _valueTypeDeserializer;

    private static final class CollectionReferring extends Referring {
        private final CollectionReferringAccumulator _parent;
        public final List<Object> next;

        CollectionReferring(CollectionReferringAccumulator parent, UnresolvedForwardReference reference, Class<?> contentType) {
            super(reference, contentType);
            this.next = new ArrayList();
            this._parent = parent;
        }

        public void handleResolvedForwardReference(Object id, Object value) throws IOException {
            this._parent.resolveForwardReference(id, value);
        }
    }

    public static final class CollectionReferringAccumulator {
        private List<CollectionReferring> _accumulator;
        private final Class<?> _elementType;
        private final Collection<Object> _result;

        public CollectionReferringAccumulator(Class<?> elementType, Collection<Object> result) {
            this._accumulator = new ArrayList();
            this._elementType = elementType;
            this._result = result;
        }

        public void add(Object value) {
            if (this._accumulator.isEmpty()) {
                this._result.add(value);
            } else {
                ((CollectionReferring) this._accumulator.get(this._accumulator.size() - 1)).next.add(value);
            }
        }

        public Referring handleUnresolvedReference(UnresolvedForwardReference reference) {
            CollectionReferring id = new CollectionReferring(this, reference, this._elementType);
            this._accumulator.add(id);
            return id;
        }

        public void resolveForwardReference(Object id, Object value) throws IOException {
            Iterator<CollectionReferring> iterator = this._accumulator.iterator();
            Collection<Object> previous = this._result;
            while (iterator.hasNext()) {
                CollectionReferring ref = (CollectionReferring) iterator.next();
                if (ref.hasId(id)) {
                    iterator.remove();
                    previous.add(value);
                    previous.addAll(ref.next);
                    return;
                }
                previous = ref.next;
            }
            throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
        }
    }

    public CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator) {
        this(collectionType, valueDeser, valueTypeDeser, valueInstantiator, null);
    }

    protected CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator, JsonDeserializer<Object> delegateDeser) {
        super(collectionType);
        this._collectionType = collectionType;
        this._valueDeserializer = valueDeser;
        this._valueTypeDeserializer = valueTypeDeser;
        this._valueInstantiator = valueInstantiator;
        this._delegateDeserializer = delegateDeser;
    }

    protected CollectionDeserializer(CollectionDeserializer src) {
        super(src._collectionType);
        this._collectionType = src._collectionType;
        this._valueDeserializer = src._valueDeserializer;
        this._valueTypeDeserializer = src._valueTypeDeserializer;
        this._valueInstantiator = src._valueInstantiator;
        this._delegateDeserializer = src._delegateDeserializer;
    }

    protected CollectionDeserializer withResolved(JsonDeserializer<?> dd, JsonDeserializer<?> vd, TypeDeserializer vtd) {
        if (dd == this._delegateDeserializer && vd == this._valueDeserializer && vtd == this._valueTypeDeserializer) {
            return this;
        }
        return new CollectionDeserializer(this._collectionType, vd, vtd, this._valueInstantiator, dd);
    }

    public boolean isCachable() {
        return this._valueDeserializer == null && this._valueTypeDeserializer == null && this._delegateDeserializer == null;
    }

    public CollectionDeserializer createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> delegateDeser = null;
        if (this._valueInstantiator != null && this._valueInstantiator.canCreateUsingDelegate()) {
            JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
            if (delegateType == null) {
                throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._collectionType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
            }
            delegateDeser = findDeserializer(ctxt, delegateType, property);
        }
        JsonDeserializer<?> valueDeser = findConvertingContentDeserializer(ctxt, property, this._valueDeserializer);
        JavaType vt = this._collectionType.getContentType();
        if (valueDeser == null) {
            valueDeser = ctxt.findContextualValueDeserializer(vt, property);
        } else {
            valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, vt);
        }
        TypeDeserializer valueTypeDeser = this._valueTypeDeserializer;
        if (valueTypeDeser != null) {
            valueTypeDeser = valueTypeDeser.forProperty(property);
        }
        return withResolved(delegateDeser, valueDeser, valueTypeDeser);
    }

    public JavaType getContentType() {
        return this._collectionType.getContentType();
    }

    public JsonDeserializer<Object> getContentDeserializer() {
        return this._valueDeserializer;
    }

    public Collection<Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (this._delegateDeserializer != null) {
            return (Collection) this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
        }
        if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
            String str = p.getText();
            if (str.length() == 0) {
                return (Collection) this._valueInstantiator.createFromString(ctxt, str);
            }
        }
        return deserialize(p, ctxt, (Collection) this._valueInstantiator.createUsingDefault(ctxt));
    }

    public Collection<Object> deserialize(JsonParser p, DeserializationContext ctxt, Collection<Object> result) throws IOException {
        if (!p.isExpectedStartArrayToken()) {
            return handleNonArray(p, ctxt, result);
        }
        p.setCurrentValue(result);
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        CollectionReferringAccumulator referringAccumulator = valueDes.getObjectIdReader() == null ? null : new CollectionReferringAccumulator(this._collectionType.getContentType().getRawClass(), result);
        while (true) {
            JsonToken t = p.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return result;
            }
            try {
                Object value;
                if (t == JsonToken.VALUE_NULL) {
                    value = valueDes.getNullValue(ctxt);
                } else if (typeDeser == null) {
                    value = valueDes.deserialize(p, ctxt);
                } else {
                    value = valueDes.deserializeWithType(p, ctxt, typeDeser);
                }
                if (referringAccumulator != null) {
                    referringAccumulator.add(value);
                } else {
                    result.add(value);
                }
            } catch (UnresolvedForwardReference reference) {
                if (referringAccumulator == null) {
                    break;
                    throw JsonMappingException.from(p, "Unresolved forward reference but no identity info", reference);
                }
                reference.getRoid().appendReferring(referringAccumulator.handleUnresolvedReference(reference));
            } catch (Throwable e) {
                boolean wrap = ctxt == null || ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS);
                if (wrap || !(e instanceof RuntimeException)) {
                    throw JsonMappingException.wrapWithPath(e, (Object) result, result.size());
                }
                throw ((RuntimeException) e);
            }
        }
        throw JsonMappingException.from(p, "Unresolved forward reference but no identity info", reference);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    protected final Collection<Object> handleNonArray(JsonParser p, DeserializationContext ctxt, Collection<Object> result) throws IOException {
        if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            JsonDeserializer<Object> valueDes = this._valueDeserializer;
            TypeDeserializer typeDeser = this._valueTypeDeserializer;
            try {
                Object value;
                if (p.getCurrentToken() == JsonToken.VALUE_NULL) {
                    value = valueDes.getNullValue(ctxt);
                } else if (typeDeser == null) {
                    value = valueDes.deserialize(p, ctxt);
                } else {
                    value = valueDes.deserializeWithType(p, ctxt, typeDeser);
                }
                result.add(value);
                return result;
            } catch (Throwable e) {
                throw JsonMappingException.wrapWithPath(e, (Object) Object.class, result.size());
            }
        }
        throw ctxt.mappingException(this._collectionType.getRawClass());
    }
}
