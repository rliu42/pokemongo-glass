package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JacksonStdImpl
public class MapDeserializer extends ContainerDeserializerBase<Map<Object, Object>> implements ContextualDeserializer, ResolvableDeserializer {
    private static final long serialVersionUID = 1;
    protected JsonDeserializer<Object> _delegateDeserializer;
    protected final boolean _hasDefaultCreator;
    protected HashSet<String> _ignorableProperties;
    protected final KeyDeserializer _keyDeserializer;
    protected final JavaType _mapType;
    protected PropertyBasedCreator _propertyBasedCreator;
    protected boolean _standardStringKey;
    protected final JsonDeserializer<Object> _valueDeserializer;
    protected final ValueInstantiator _valueInstantiator;
    protected final TypeDeserializer _valueTypeDeserializer;

    static final class MapReferring extends Referring {
        private final MapReferringAccumulator _parent;
        public final Object key;
        public final Map<Object, Object> next;

        MapReferring(MapReferringAccumulator parent, UnresolvedForwardReference ref, Class<?> valueType, Object key) {
            super(ref, valueType);
            this.next = new LinkedHashMap();
            this._parent = parent;
            this.key = key;
        }

        public void handleResolvedForwardReference(Object id, Object value) throws IOException {
            this._parent.resolveForwardReference(id, value);
        }
    }

    private static final class MapReferringAccumulator {
        private List<MapReferring> _accumulator;
        private Map<Object, Object> _result;
        private final Class<?> _valueType;

        public MapReferringAccumulator(Class<?> valueType, Map<Object, Object> result) {
            this._accumulator = new ArrayList();
            this._valueType = valueType;
            this._result = result;
        }

        public void put(Object key, Object value) {
            if (this._accumulator.isEmpty()) {
                this._result.put(key, value);
            } else {
                ((MapReferring) this._accumulator.get(this._accumulator.size() - 1)).next.put(key, value);
            }
        }

        public Referring handleUnresolvedReference(UnresolvedForwardReference reference, Object key) {
            MapReferring id = new MapReferring(this, reference, this._valueType, key);
            this._accumulator.add(id);
            return id;
        }

        public void resolveForwardReference(Object id, Object value) throws IOException {
            Iterator<MapReferring> iterator = this._accumulator.iterator();
            Map<Object, Object> previous = this._result;
            while (iterator.hasNext()) {
                MapReferring ref = (MapReferring) iterator.next();
                if (ref.hasId(id)) {
                    iterator.remove();
                    previous.put(ref.key, value);
                    previous.putAll(ref.next);
                    return;
                }
                previous = ref.next;
            }
            throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
        }
    }

    public MapDeserializer(JavaType mapType, ValueInstantiator valueInstantiator, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser) {
        super(mapType);
        this._mapType = mapType;
        this._keyDeserializer = keyDeser;
        this._valueDeserializer = valueDeser;
        this._valueTypeDeserializer = valueTypeDeser;
        this._valueInstantiator = valueInstantiator;
        this._hasDefaultCreator = valueInstantiator.canCreateUsingDefault();
        this._delegateDeserializer = null;
        this._propertyBasedCreator = null;
        this._standardStringKey = _isStdKeyDeser(mapType, keyDeser);
    }

    protected MapDeserializer(MapDeserializer src) {
        super(src._mapType);
        this._mapType = src._mapType;
        this._keyDeserializer = src._keyDeserializer;
        this._valueDeserializer = src._valueDeserializer;
        this._valueTypeDeserializer = src._valueTypeDeserializer;
        this._valueInstantiator = src._valueInstantiator;
        this._propertyBasedCreator = src._propertyBasedCreator;
        this._delegateDeserializer = src._delegateDeserializer;
        this._hasDefaultCreator = src._hasDefaultCreator;
        this._ignorableProperties = src._ignorableProperties;
        this._standardStringKey = src._standardStringKey;
    }

    protected MapDeserializer(MapDeserializer src, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, HashSet<String> ignorable) {
        super(src._mapType);
        this._mapType = src._mapType;
        this._keyDeserializer = keyDeser;
        this._valueDeserializer = valueDeser;
        this._valueTypeDeserializer = valueTypeDeser;
        this._valueInstantiator = src._valueInstantiator;
        this._propertyBasedCreator = src._propertyBasedCreator;
        this._delegateDeserializer = src._delegateDeserializer;
        this._hasDefaultCreator = src._hasDefaultCreator;
        this._ignorableProperties = ignorable;
        this._standardStringKey = _isStdKeyDeser(this._mapType, keyDeser);
    }

    protected MapDeserializer withResolved(KeyDeserializer keyDeser, TypeDeserializer valueTypeDeser, JsonDeserializer<?> valueDeser, HashSet<String> ignorable) {
        return (this._keyDeserializer == keyDeser && this._valueDeserializer == valueDeser && this._valueTypeDeserializer == valueTypeDeser && this._ignorableProperties == ignorable) ? this : new MapDeserializer(this, keyDeser, (JsonDeserializer) valueDeser, valueTypeDeser, (HashSet) ignorable);
    }

    protected final boolean _isStdKeyDeser(JavaType mapType, KeyDeserializer keyDeser) {
        if (keyDeser == null) {
            return true;
        }
        JavaType keyType = mapType.getKeyType();
        if (keyType == null) {
            return true;
        }
        Class<?> rawKeyType = keyType.getRawClass();
        if ((rawKeyType == String.class || rawKeyType == Object.class) && isDefaultKeyDeserializer(keyDeser)) {
            return true;
        }
        return false;
    }

    public void setIgnorableProperties(String[] ignorable) {
        HashSet arrayToSet = (ignorable == null || ignorable.length == 0) ? null : ArrayBuilders.arrayToSet(ignorable);
        this._ignorableProperties = arrayToSet;
    }

    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        if (this._valueInstantiator.canCreateUsingDelegate()) {
            JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
            if (delegateType == null) {
                throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._mapType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
            }
            this._delegateDeserializer = findDeserializer(ctxt, delegateType, null);
        }
        if (this._valueInstantiator.canCreateFromObjectWith()) {
            this._propertyBasedCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, this._valueInstantiator.getFromObjectArguments(ctxt.getConfig()));
        }
        this._standardStringKey = _isStdKeyDeser(this._mapType, this._keyDeserializer);
    }

    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        KeyDeserializer kd = this._keyDeserializer;
        if (kd == null) {
            kd = ctxt.findKeyDeserializer(this._mapType.getKeyType(), property);
        } else if (kd instanceof ContextualKeyDeserializer) {
            kd = ((ContextualKeyDeserializer) kd).createContextual(ctxt, property);
        }
        JsonDeserializer<?> vd = this._valueDeserializer;
        if (property != null) {
            vd = findConvertingContentDeserializer(ctxt, property, vd);
        }
        JavaType vt = this._mapType.getContentType();
        if (vd == null) {
            vd = ctxt.findContextualValueDeserializer(vt, property);
        } else {
            vd = ctxt.handleSecondaryContextualization(vd, property, vt);
        }
        TypeDeserializer vtd = this._valueTypeDeserializer;
        if (vtd != null) {
            vtd = vtd.forProperty(property);
        }
        HashSet<String> ignored = this._ignorableProperties;
        AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
        if (!(intr == null || property == null)) {
            AnnotatedMember member = property.getMember();
            if (member != null) {
                String[] moreToIgnore = intr.findPropertiesToIgnore(member, false);
                if (moreToIgnore != null) {
                    ignored = ignored == null ? new HashSet() : new HashSet(ignored);
                    for (String str : moreToIgnore) {
                        ignored.add(str);
                    }
                }
            }
        }
        return withResolved(kd, vtd, vd, ignored);
    }

    public JavaType getContentType() {
        return this._mapType.getContentType();
    }

    public JsonDeserializer<Object> getContentDeserializer() {
        return this._valueDeserializer;
    }

    public boolean isCachable() {
        return this._valueDeserializer == null && this._keyDeserializer == null && this._valueTypeDeserializer == null && this._ignorableProperties == null;
    }

    public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (this._propertyBasedCreator != null) {
            return _deserializeUsingCreator(p, ctxt);
        }
        if (this._delegateDeserializer != null) {
            return (Map) this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
        }
        if (this._hasDefaultCreator) {
            JsonToken t = p.getCurrentToken();
            if (t == JsonToken.START_OBJECT || t == JsonToken.FIELD_NAME || t == JsonToken.END_OBJECT) {
                Map<Object, Object> result = (Map) this._valueInstantiator.createUsingDefault(ctxt);
                if (this._standardStringKey) {
                    _readAndBindStringMap(p, ctxt, result);
                    return result;
                }
                _readAndBind(p, ctxt, result);
                return result;
            } else if (t == JsonToken.VALUE_STRING) {
                return (Map) this._valueInstantiator.createFromString(ctxt, p.getText());
            } else {
                return (Map) _deserializeFromEmpty(p, ctxt);
            }
        }
        throw ctxt.instantiationException(getMapClass(), "No default constructor found");
    }

    public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
        p.setCurrentValue(result);
        JsonToken t = p.getCurrentToken();
        if (t == JsonToken.START_OBJECT || t == JsonToken.FIELD_NAME) {
            if (this._standardStringKey) {
                _readAndBindStringMap(p, ctxt, result);
            } else {
                _readAndBind(p, ctxt, result);
            }
            return result;
        }
        throw ctxt.mappingException(getMapClass());
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    public final Class<?> getMapClass() {
        return this._mapType.getRawClass();
    }

    public JavaType getValueType() {
        return this._mapType;
    }

    protected final void _readAndBind(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
        String keyStr;
        JsonToken t;
        KeyDeserializer keyDes = this._keyDeserializer;
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        MapReferringAccumulator referringAccumulator = null;
        boolean useObjectId = valueDes.getObjectIdReader() != null;
        if (useObjectId) {
            referringAccumulator = new MapReferringAccumulator(this._mapType.getContentType().getRawClass(), result);
        }
        if (p.isExpectedStartObjectToken()) {
            keyStr = p.nextFieldName();
        } else {
            t = p.getCurrentToken();
            if (t != JsonToken.END_OBJECT) {
                if (t != JsonToken.FIELD_NAME) {
                    throw ctxt.mappingException(this._mapType.getRawClass(), p.getCurrentToken());
                }
                keyStr = p.getCurrentName();
            } else {
                return;
            }
        }
        while (keyStr != null) {
            Object key = keyDes.deserializeKey(keyStr, ctxt);
            t = p.nextToken();
            if (this._ignorableProperties == null || !this._ignorableProperties.contains(keyStr)) {
                try {
                    Object value;
                    if (t == JsonToken.VALUE_NULL) {
                        value = valueDes.getNullValue(ctxt);
                    } else if (typeDeser == null) {
                        value = valueDes.deserialize(p, ctxt);
                    } else {
                        value = valueDes.deserializeWithType(p, ctxt, typeDeser);
                    }
                    if (useObjectId) {
                        referringAccumulator.put(key, value);
                    } else {
                        result.put(key, value);
                    }
                } catch (UnresolvedForwardReference reference) {
                    handleUnresolvedReference(p, referringAccumulator, key, reference);
                } catch (Exception e) {
                    wrapAndThrow(e, result, keyStr);
                }
            } else {
                p.skipChildren();
            }
            keyStr = p.nextFieldName();
        }
    }

    protected final void _readAndBindStringMap(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
        String key;
        JsonToken t;
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        MapReferringAccumulator referringAccumulator = null;
        boolean useObjectId = valueDes.getObjectIdReader() != null;
        if (useObjectId) {
            referringAccumulator = new MapReferringAccumulator(this._mapType.getContentType().getRawClass(), result);
        }
        if (p.isExpectedStartObjectToken()) {
            key = p.nextFieldName();
        } else {
            t = p.getCurrentToken();
            if (t != JsonToken.END_OBJECT) {
                if (t != JsonToken.FIELD_NAME) {
                    throw ctxt.mappingException(this._mapType.getRawClass(), p.getCurrentToken());
                }
                key = p.getCurrentName();
            } else {
                return;
            }
        }
        while (key != null) {
            t = p.nextToken();
            if (this._ignorableProperties == null || !this._ignorableProperties.contains(key)) {
                try {
                    Object value;
                    if (t == JsonToken.VALUE_NULL) {
                        value = valueDes.getNullValue(ctxt);
                    } else if (typeDeser == null) {
                        value = valueDes.deserialize(p, ctxt);
                    } else {
                        value = valueDes.deserializeWithType(p, ctxt, typeDeser);
                    }
                    if (useObjectId) {
                        referringAccumulator.put(key, value);
                    } else {
                        result.put(key, value);
                    }
                } catch (UnresolvedForwardReference reference) {
                    handleUnresolvedReference(p, referringAccumulator, key, reference);
                } catch (Exception e) {
                    wrapAndThrow(e, result, key);
                }
            } else {
                p.skipChildren();
            }
            key = p.nextFieldName();
        }
    }

    public Map<Object, Object> _deserializeUsingCreator(JsonParser p, DeserializationContext ctxt) throws IOException {
        String key;
        PropertyBasedCreator creator = this._propertyBasedCreator;
        PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, null);
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        if (p.isExpectedStartObjectToken()) {
            key = p.nextFieldName();
        } else if (p.hasToken(JsonToken.FIELD_NAME)) {
            key = p.getCurrentName();
        } else {
            key = null;
        }
        while (key != null) {
            JsonToken t = p.nextToken();
            if (this._ignorableProperties == null || !this._ignorableProperties.contains(key)) {
                SettableBeanProperty prop = creator.findCreatorProperty(key);
                if (prop == null) {
                    Object value;
                    Object actualKey = this._keyDeserializer.deserializeKey(key, ctxt);
                    if (t == JsonToken.VALUE_NULL) {
                        value = valueDes.getNullValue(ctxt);
                    } else if (typeDeser == null) {
                        try {
                            value = valueDes.deserialize(p, ctxt);
                        } catch (Exception e) {
                            wrapAndThrow(e, this._mapType.getRawClass(), key);
                            return null;
                        }
                    } else {
                        value = valueDes.deserializeWithType(p, ctxt, typeDeser);
                    }
                    buffer.bufferMapProperty(actualKey, value);
                } else if (buffer.assignParameter(prop, prop.deserialize(p, ctxt))) {
                    p.nextToken();
                    try {
                        Map<Object, Object> result = (Map) creator.build(ctxt, buffer);
                        _readAndBind(p, ctxt, result);
                        return result;
                    } catch (Exception e2) {
                        wrapAndThrow(e2, this._mapType.getRawClass(), key);
                        return null;
                    }
                }
            } else {
                p.skipChildren();
            }
            key = p.nextFieldName();
        }
        try {
            return (Map) creator.build(ctxt, buffer);
        } catch (Exception e22) {
            wrapAndThrow(e22, this._mapType.getRawClass(), null);
            return null;
        }
    }

    @Deprecated
    protected void wrapAndThrow(Throwable t, Object ref) throws IOException {
        wrapAndThrow(t, ref, null);
    }

    private void handleUnresolvedReference(JsonParser jp, MapReferringAccumulator accumulator, Object key, UnresolvedForwardReference reference) throws JsonMappingException {
        if (accumulator == null) {
            throw JsonMappingException.from(jp, "Unresolved forward reference but no identity info.", reference);
        }
        reference.getRoid().appendReferring(accumulator.handleUnresolvedReference(reference, key));
    }
}
