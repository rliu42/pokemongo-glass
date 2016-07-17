package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

public class PropertyBasedObjectIdGenerator extends PropertyGenerator {
    private static final long serialVersionUID = 1;
    protected final BeanPropertyWriter _property;

    public PropertyBasedObjectIdGenerator(ObjectIdInfo oid, BeanPropertyWriter prop) {
        this(oid.getScope(), prop);
    }

    protected PropertyBasedObjectIdGenerator(Class<?> scope, BeanPropertyWriter prop) {
        super(scope);
        this._property = prop;
    }

    public boolean canUseFor(ObjectIdGenerator<?> gen) {
        if (gen.getClass() != getClass()) {
            return false;
        }
        PropertyBasedObjectIdGenerator other = (PropertyBasedObjectIdGenerator) gen;
        if (other.getScope() == this._scope && other._property == this._property) {
            return true;
        }
        return false;
    }

    public Object generateId(Object forPojo) {
        try {
            return this._property.get(forPojo);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new IllegalStateException("Problem accessing property '" + this._property.getName() + "': " + e2.getMessage(), e2);
        }
    }

    public ObjectIdGenerator<Object> forScope(Class<?> scope) {
        return scope == this._scope ? this : new PropertyBasedObjectIdGenerator((Class) scope, this._property);
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
