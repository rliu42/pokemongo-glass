package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

public class PropertyBasedObjectIdGenerator extends PropertyGenerator {
    private static final long serialVersionUID = 1;

    public PropertyBasedObjectIdGenerator(Class<?> scope) {
        super(scope);
    }

    public Object generateId(Object forPojo) {
        throw new UnsupportedOperationException();
    }

    public ObjectIdGenerator<Object> forScope(Class<?> scope) {
        if (scope == this._scope) {
            return this;
        }
        this(scope);
        return this;
    }

    public ObjectIdGenerator<Object> newForSerialization(Object context) {
        return this;
    }

    public IdKey key(Object key) {
        if (key == null) {
            return null;
        }
        return new IdKey(getClass(), this._scope, key);
    }
}
