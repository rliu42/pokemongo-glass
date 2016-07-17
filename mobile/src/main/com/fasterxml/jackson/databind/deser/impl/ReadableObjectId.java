package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class ReadableObjectId {
    protected final IdKey _key;
    protected LinkedList<Referring> _referringProperties;
    protected ObjectIdResolver _resolver;
    @Deprecated
    public final Object id;
    @Deprecated
    public Object item;

    public static abstract class Referring {
        private final Class<?> _beanType;
        private final UnresolvedForwardReference _reference;

        public abstract void handleResolvedForwardReference(Object obj, Object obj2) throws IOException;

        public Referring(UnresolvedForwardReference ref, Class<?> beanType) {
            this._reference = ref;
            this._beanType = beanType;
        }

        public JsonLocation getLocation() {
            return this._reference.getLocation();
        }

        public Class<?> getBeanType() {
            return this._beanType;
        }

        public boolean hasId(Object id) {
            return id.equals(this._reference.getUnresolvedId());
        }
    }

    @Deprecated
    public ReadableObjectId(Object id) {
        this.id = id;
        this._key = null;
    }

    public ReadableObjectId(IdKey key) {
        this._key = key;
        this.id = key.key;
    }

    public void setResolver(ObjectIdResolver resolver) {
        this._resolver = resolver;
    }

    public IdKey getKey() {
        return this._key;
    }

    public void appendReferring(Referring currentReferring) {
        if (this._referringProperties == null) {
            this._referringProperties = new LinkedList();
        }
        this._referringProperties.add(currentReferring);
    }

    public void bindItem(Object ob) throws IOException {
        this._resolver.bindItem(this._key, ob);
        this.item = ob;
        if (this._referringProperties != null) {
            Iterator<Referring> it = this._referringProperties.iterator();
            this._referringProperties = null;
            while (it.hasNext()) {
                ((Referring) it.next()).handleResolvedForwardReference(this.id, ob);
            }
        }
    }

    public Object resolve() {
        Object resolveId = this._resolver.resolveId(this._key);
        this.item = resolveId;
        return resolveId;
    }

    public boolean hasReferringProperties() {
        return (this._referringProperties == null || this._referringProperties.isEmpty()) ? false : true;
    }

    public Iterator<Referring> referringProperties() {
        if (this._referringProperties == null) {
            return Collections.emptyList().iterator();
        }
        return this._referringProperties.iterator();
    }

    public boolean tryToResolveUnresolved(DeserializationContext ctxt) {
        return false;
    }

    public String toString() {
        return String.valueOf(this._key);
    }
}
