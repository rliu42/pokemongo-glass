package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public final class DeserializerCache implements Serializable {
    private static final long serialVersionUID = 1;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _cachedDeserializers;
    protected final HashMap<JavaType, JsonDeserializer<Object>> _incompleteDeserializers;

    public DeserializerCache() {
        this._cachedDeserializers = new ConcurrentHashMap(64, 0.75f, 4);
        this._incompleteDeserializers = new HashMap(8);
    }

    Object writeReplace() {
        this._incompleteDeserializers.clear();
        return this;
    }

    public int cachedDeserializersCount() {
        return this._cachedDeserializers.size();
    }

    public void flushCachedDeserializers() {
        this._cachedDeserializers.clear();
    }

    public JsonDeserializer<Object> findValueDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType propertyType) throws JsonMappingException {
        JsonDeserializer<Object> deser = _findCachedDeserializer(propertyType);
        if (deser != null) {
            return deser;
        }
        deser = _createAndCacheValueDeserializer(ctxt, factory, propertyType);
        if (deser == null) {
            return _handleUnknownValueDeserializer(propertyType);
        }
        return deser;
    }

    public KeyDeserializer findKeyDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type) throws JsonMappingException {
        KeyDeserializer kd = factory.createKeyDeserializer(ctxt, type);
        if (kd == null) {
            return _handleUnknownKeyDeserializer(type);
        }
        if (!(kd instanceof ResolvableDeserializer)) {
            return kd;
        }
        ((ResolvableDeserializer) kd).resolve(ctxt);
        return kd;
    }

    public boolean hasValueDeserializerFor(DeserializationContext ctxt, DeserializerFactory factory, JavaType type) throws JsonMappingException {
        JsonDeserializer<Object> deser = _findCachedDeserializer(type);
        if (deser == null) {
            deser = _createAndCacheValueDeserializer(ctxt, factory, type);
        }
        return deser != null;
    }

    protected JsonDeserializer<Object> _findCachedDeserializer(JavaType type) {
        if (type == null) {
            throw new IllegalArgumentException("Null JavaType passed");
        } else if (_hasCustomValueHandler(type)) {
            return null;
        } else {
            return (JsonDeserializer) this._cachedDeserializers.get(type);
        }
    }

    protected JsonDeserializer<Object> _createAndCacheValueDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type) throws JsonMappingException {
        synchronized (this._incompleteDeserializers) {
            JsonDeserializer<Object> deser = _findCachedDeserializer(type);
            if (deser != null) {
                return deser;
            }
            count = this._incompleteDeserializers.size();
            if (count > 0) {
                deser = (JsonDeserializer) this._incompleteDeserializers.get(type);
                if (deser != null) {
                    return deser;
                }
            }
            try {
                JsonDeserializer<Object> _createAndCache2 = _createAndCache2(ctxt, factory, type);
                if (count == 0) {
                    if (this._incompleteDeserializers.size() > 0) {
                        this._incompleteDeserializers.clear();
                    }
                }
                return _createAndCache2;
            } catch (Throwable th) {
                int count;
                if (count == 0) {
                    if (this._incompleteDeserializers.size() > 0) {
                        this._incompleteDeserializers.clear();
                    }
                }
            }
        }
    }

    protected JsonDeserializer<Object> _createAndCache2(DeserializationContext ctxt, DeserializerFactory factory, JavaType type) throws JsonMappingException {
        try {
            JsonDeserializer<Object> deser = _createDeserializer(ctxt, factory, type);
            if (deser == null) {
                return null;
            }
            boolean addToCache;
            boolean isResolvable = deser instanceof ResolvableDeserializer;
            if (_hasCustomValueHandler(type) || !deser.isCachable()) {
                addToCache = false;
            } else {
                addToCache = true;
            }
            if (isResolvable) {
                this._incompleteDeserializers.put(type, deser);
                ((ResolvableDeserializer) deser).resolve(ctxt);
                this._incompleteDeserializers.remove(type);
            }
            if (!addToCache) {
                return deser;
            }
            this._cachedDeserializers.put(type, deser);
            return deser;
        } catch (IllegalArgumentException iae) {
            throw new JsonMappingException(iae.getMessage(), null, iae);
        }
    }

    protected JsonDeserializer<Object> _createDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type) throws JsonMappingException {
        DeserializationConfig config = ctxt.getConfig();
        if (type.isAbstract() || type.isMapLikeType() || type.isCollectionLikeType()) {
            type = factory.mapAbstractType(config, type);
        }
        BeanDescription beanDesc = config.introspect(type);
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(ctxt, beanDesc.getClassInfo());
        if (deser != null) {
            return deser;
        }
        JavaType newType = modifyTypeByAnnotation(ctxt, beanDesc.getClassInfo(), type);
        if (newType != type) {
            type = newType;
            beanDesc = config.introspect(newType);
        }
        Class<?> builder = beanDesc.findPOJOBuilder();
        if (builder != null) {
            return factory.createBuilderBasedDeserializer(ctxt, type, beanDesc, builder);
        }
        Converter<Object, Object> conv = beanDesc.findDeserializationConverter();
        if (conv == null) {
            return _createDeserializer2(ctxt, factory, type, beanDesc);
        }
        JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
        if (!delegateType.hasRawClass(type.getRawClass())) {
            beanDesc = config.introspect(delegateType);
        }
        return new StdDelegatingDeserializer(conv, delegateType, _createDeserializer2(ctxt, factory, delegateType, beanDesc));
    }

    protected JsonDeserializer<?> _createDeserializer2(DeserializationContext ctxt, DeserializerFactory factory, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        DeserializationConfig config = ctxt.getConfig();
        if (type.isEnumType()) {
            return factory.createEnumDeserializer(ctxt, type, beanDesc);
        }
        if (type.isContainerType()) {
            if (type.isArrayType()) {
                return factory.createArrayDeserializer(ctxt, (ArrayType) type, beanDesc);
            }
            if (type.isMapLikeType()) {
                MapLikeType mlt = (MapLikeType) type;
                if (mlt.isTrueMapType()) {
                    return factory.createMapDeserializer(ctxt, (MapType) mlt, beanDesc);
                }
                return factory.createMapLikeDeserializer(ctxt, mlt, beanDesc);
            } else if (type.isCollectionLikeType()) {
                Value format = beanDesc.findExpectedFormat(null);
                if (format == null || format.getShape() != Shape.OBJECT) {
                    CollectionLikeType clt = (CollectionLikeType) type;
                    if (clt.isTrueCollectionType()) {
                        return factory.createCollectionDeserializer(ctxt, (CollectionType) clt, beanDesc);
                    }
                    return factory.createCollectionLikeDeserializer(ctxt, clt, beanDesc);
                }
            }
        }
        if (JsonNode.class.isAssignableFrom(type.getRawClass())) {
            return factory.createTreeDeserializer(config, type, beanDesc);
        }
        return factory.createBeanDeserializer(ctxt, type, beanDesc);
    }

    protected JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationContext ctxt, Annotated ann) throws JsonMappingException {
        Object deserDef = ctxt.getAnnotationIntrospector().findDeserializer(ann);
        if (deserDef == null) {
            return null;
        }
        return findConvertingDeserializer(ctxt, ann, ctxt.deserializerInstance(ann, deserDef));
    }

    protected JsonDeserializer<Object> findConvertingDeserializer(DeserializationContext ctxt, Annotated a, JsonDeserializer<Object> deser) throws JsonMappingException {
        Converter<Object, Object> conv = findConverter(ctxt, a);
        return conv == null ? deser : new StdDelegatingDeserializer(conv, conv.getInputType(ctxt.getTypeFactory()), deser);
    }

    protected Converter<Object, Object> findConverter(DeserializationContext ctxt, Annotated a) throws JsonMappingException {
        Object convDef = ctxt.getAnnotationIntrospector().findDeserializationConverter(a);
        if (convDef == null) {
            return null;
        }
        return ctxt.converterInstance(a, convDef);
    }

    private JavaType modifyTypeByAnnotation(DeserializationContext ctxt, Annotated a, JavaType type) throws JsonMappingException {
        AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
        Class<?> subclass = intr.findDeserializationType(a, type);
        if (subclass != null) {
            try {
                type = ctxt.getTypeFactory().constructSpecializedType(type, subclass);
            } catch (IllegalArgumentException iae) {
                throw new JsonMappingException("Failed to narrow type " + type + " with concrete-type annotation (value " + subclass.getName() + "), method '" + a.getName() + "': " + iae.getMessage(), null, iae);
            }
        }
        if (!type.isContainerType()) {
            return type;
        }
        Class<?> keyClass = intr.findDeserializationKeyType(a, type.getKeyType());
        if (keyClass != null) {
            if (type instanceof MapLikeType) {
                try {
                    type = ((MapLikeType) type).narrowKey(keyClass);
                } catch (IllegalArgumentException iae2) {
                    throw new JsonMappingException("Failed to narrow key type " + type + " with key-type annotation (" + keyClass.getName() + "): " + iae2.getMessage(), null, iae2);
                }
            }
            throw new JsonMappingException("Illegal key-type annotation: type " + type + " is not a Map(-like) type");
        }
        JavaType keyType = type.getKeyType();
        if (keyType != null && keyType.getValueHandler() == null) {
            Object kdDef = intr.findKeyDeserializer(a);
            if (kdDef != null) {
                KeyDeserializer kd = ctxt.keyDeserializerInstance(a, kdDef);
                if (kd != null) {
                    type = ((MapLikeType) type).withKeyValueHandler(kd);
                    keyType = type.getKeyType();
                }
            }
        }
        Class<?> cc = intr.findDeserializationContentType(a, type.getContentType());
        if (cc != null) {
            try {
                type = type.narrowContentsBy(cc);
            } catch (IllegalArgumentException iae22) {
                throw new JsonMappingException("Failed to narrow content type " + type + " with content-type annotation (" + cc.getName() + "): " + iae22.getMessage(), null, iae22);
            }
        }
        if (type.getContentType().getValueHandler() != null) {
            return type;
        }
        Object cdDef = intr.findContentDeserializer(a);
        if (cdDef == null) {
            return type;
        }
        JsonDeserializer<?> cd = null;
        if (cdDef instanceof JsonDeserializer) {
            JsonDeserializer cdDef2 = (JsonDeserializer) cdDef;
        } else {
            Class<?> cdClass = _verifyAsClass(cdDef, "findContentDeserializer", None.class);
            if (cdClass != null) {
                cd = ctxt.deserializerInstance(a, cdClass);
            }
        }
        if (cd != null) {
            return type.withContentValueHandler(cd);
        }
        return type;
    }

    private boolean _hasCustomValueHandler(JavaType t) {
        if (!t.isContainerType()) {
            return false;
        }
        JavaType ct = t.getContentType();
        if (ct == null) {
            return false;
        }
        if (ct.getValueHandler() == null && ct.getTypeHandler() == null) {
            return false;
        }
        return true;
    }

    private Class<?> _verifyAsClass(Object src, String methodName, Class<?> noneClass) {
        if (src == null) {
            return null;
        }
        if (src instanceof Class) {
            Class<?> cls = (Class) src;
            if (cls == noneClass || ClassUtil.isBogusClass(cls)) {
                return null;
            }
            return cls;
        }
        throw new IllegalStateException("AnnotationIntrospector." + methodName + "() returned value of type " + src.getClass().getName() + ": expected type JsonSerializer or Class<JsonSerializer> instead");
    }

    protected JsonDeserializer<Object> _handleUnknownValueDeserializer(JavaType type) throws JsonMappingException {
        if (ClassUtil.isConcrete(type.getRawClass())) {
            throw new JsonMappingException("Can not find a Value deserializer for type " + type);
        }
        throw new JsonMappingException("Can not find a Value deserializer for abstract type " + type);
    }

    protected KeyDeserializer _handleUnknownKeyDeserializer(JavaType type) throws JsonMappingException {
        throw new JsonMappingException("Can not find a (Map) Key deserializer for type " + type);
    }
}
