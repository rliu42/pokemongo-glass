package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public abstract class DefaultDeserializationContext extends DeserializationContext implements Serializable {
    private static final long serialVersionUID = 1;
    private List<ObjectIdResolver> _objectIdResolvers;
    protected transient LinkedHashMap<IdKey, ReadableObjectId> _objectIds;

    public static final class Impl extends DefaultDeserializationContext {
        private static final long serialVersionUID = 1;

        public Impl(DeserializerFactory df) {
            super(df, null);
        }

        protected Impl(Impl src, DeserializationConfig config, JsonParser jp, InjectableValues values) {
            super(src, config, jp, values);
        }

        protected Impl(Impl src) {
            super(src);
        }

        protected Impl(Impl src, DeserializerFactory factory) {
            super((DefaultDeserializationContext) src, factory);
        }

        public DefaultDeserializationContext copy() {
            if (getClass() != Impl.class) {
                return super.copy();
            }
            return new Impl(this);
        }

        public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp, InjectableValues values) {
            return new Impl(this, config, jp, values);
        }

        public DefaultDeserializationContext with(DeserializerFactory factory) {
            return new Impl(this, factory);
        }
    }

    public abstract DefaultDeserializationContext createInstance(DeserializationConfig deserializationConfig, JsonParser jsonParser, InjectableValues injectableValues);

    public abstract DefaultDeserializationContext with(DeserializerFactory deserializerFactory);

    protected DefaultDeserializationContext(DeserializerFactory df, DeserializerCache cache) {
        super(df, cache);
    }

    protected DefaultDeserializationContext(DefaultDeserializationContext src, DeserializationConfig config, JsonParser jp, InjectableValues values) {
        super(src, config, jp, values);
    }

    protected DefaultDeserializationContext(DefaultDeserializationContext src, DeserializerFactory factory) {
        super((DeserializationContext) src, factory);
    }

    protected DefaultDeserializationContext(DefaultDeserializationContext src) {
        super((DeserializationContext) src);
    }

    public DefaultDeserializationContext copy() {
        throw new IllegalStateException("DefaultDeserializationContext sub-class not overriding copy()");
    }

    public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen, ObjectIdResolver resolverType) {
        if (id == null) {
            return null;
        }
        ReadableObjectId entry;
        IdKey key = gen.key(id);
        if (this._objectIds == null) {
            this._objectIds = new LinkedHashMap();
        } else {
            entry = (ReadableObjectId) this._objectIds.get(key);
            if (entry != null) {
                return entry;
            }
        }
        ObjectIdResolver resolver = null;
        if (this._objectIdResolvers != null) {
            for (ObjectIdResolver res : this._objectIdResolvers) {
                if (res.canUseFor(resolverType)) {
                    resolver = res;
                    break;
                }
            }
        }
        this._objectIdResolvers = new ArrayList(8);
        if (resolver == null) {
            resolver = resolverType.newForDeserialization(this);
            this._objectIdResolvers.add(resolver);
        }
        entry = new ReadableObjectId(key);
        entry.setResolver(resolver);
        this._objectIds.put(key, entry);
        return entry;
    }

    @Deprecated
    public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen) {
        return findObjectId(id, gen, new SimpleObjectIdResolver());
    }

    public void checkUnresolvedObjectId() throws UnresolvedForwardReference {
        if (this._objectIds != null && isEnabled(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS)) {
            UnresolvedForwardReference exception = null;
            for (Entry<IdKey, ReadableObjectId> entry : this._objectIds.entrySet()) {
                ReadableObjectId roid = (ReadableObjectId) entry.getValue();
                if (roid.hasReferringProperties() && !tryToResolveUnresolvedObjectId(roid)) {
                    if (exception == null) {
                        exception = new UnresolvedForwardReference("Unresolved forward references for: ");
                    }
                    Object key = roid.getKey().key;
                    Iterator<Referring> iterator = roid.referringProperties();
                    while (iterator.hasNext()) {
                        Referring referring = (Referring) iterator.next();
                        exception.addUnresolvedId(key, referring.getBeanType(), referring.getLocation());
                    }
                }
            }
            if (exception != null) {
                throw exception;
            }
        }
    }

    protected boolean tryToResolveUnresolvedObjectId(ReadableObjectId roid) {
        return roid.tryToResolveUnresolved(this);
    }

    public JsonDeserializer<Object> deserializerInstance(Annotated ann, Object deserDef) throws JsonMappingException {
        JsonDeserializer<Object> deser = null;
        if (deserDef != null) {
            if (deserDef instanceof JsonDeserializer) {
                deser = (JsonDeserializer) deserDef;
            } else if (deserDef instanceof Class) {
                Class<?> deserClass = (Class) deserDef;
                if (!(deserClass == None.class || ClassUtil.isBogusClass(deserClass))) {
                    if (JsonDeserializer.class.isAssignableFrom(deserClass)) {
                        HandlerInstantiator hi = this._config.getHandlerInstantiator();
                        if (hi != null) {
                            deser = hi.deserializerInstance(this._config, ann, deserClass);
                        }
                        if (deser == null) {
                            deser = (JsonDeserializer) ClassUtil.createInstance(deserClass, this._config.canOverrideAccessModifiers());
                        }
                    } else {
                        throw new IllegalStateException("AnnotationIntrospector returned Class " + deserClass.getName() + "; expected Class<JsonDeserializer>");
                    }
                }
            } else {
                throw new IllegalStateException("AnnotationIntrospector returned deserializer definition of type " + deserDef.getClass().getName() + "; expected type JsonDeserializer or Class<JsonDeserializer> instead");
            }
            if (deser instanceof ResolvableDeserializer) {
                ((ResolvableDeserializer) deser).resolve(this);
            }
        }
        return deser;
    }

    public final KeyDeserializer keyDeserializerInstance(Annotated ann, Object deserDef) throws JsonMappingException {
        KeyDeserializer deser = null;
        if (deserDef != null) {
            if (deserDef instanceof KeyDeserializer) {
                deser = (KeyDeserializer) deserDef;
            } else if (deserDef instanceof Class) {
                Class<?> deserClass = (Class) deserDef;
                if (!(deserClass == KeyDeserializer.None.class || ClassUtil.isBogusClass(deserClass))) {
                    if (KeyDeserializer.class.isAssignableFrom(deserClass)) {
                        HandlerInstantiator hi = this._config.getHandlerInstantiator();
                        if (hi != null) {
                            deser = hi.keyDeserializerInstance(this._config, ann, deserClass);
                        }
                        if (deser == null) {
                            deser = (KeyDeserializer) ClassUtil.createInstance(deserClass, this._config.canOverrideAccessModifiers());
                        }
                    } else {
                        throw new IllegalStateException("AnnotationIntrospector returned Class " + deserClass.getName() + "; expected Class<KeyDeserializer>");
                    }
                }
            } else {
                throw new IllegalStateException("AnnotationIntrospector returned key deserializer definition of type " + deserDef.getClass().getName() + "; expected type KeyDeserializer or Class<KeyDeserializer> instead");
            }
            if (deser instanceof ResolvableDeserializer) {
                ((ResolvableDeserializer) deser).resolve(this);
            }
        }
        return deser;
    }
}
