package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;

public final class ObjectIdWriter {
    public final boolean alwaysAsId;
    public final ObjectIdGenerator<?> generator;
    public final JavaType idType;
    public final SerializableString propertyName;
    public final JsonSerializer<Object> serializer;

    protected ObjectIdWriter(JavaType t, SerializableString propName, ObjectIdGenerator<?> gen, JsonSerializer<?> ser, boolean alwaysAsId) {
        this.idType = t;
        this.propertyName = propName;
        this.generator = gen;
        this.serializer = ser;
        this.alwaysAsId = alwaysAsId;
    }

    public static ObjectIdWriter construct(JavaType idType, PropertyName propName, ObjectIdGenerator<?> generator, boolean alwaysAsId) {
        return construct(idType, propName == null ? null : propName.getSimpleName(), (ObjectIdGenerator) generator, alwaysAsId);
    }

    @Deprecated
    public static ObjectIdWriter construct(JavaType idType, String propName, ObjectIdGenerator<?> generator, boolean alwaysAsId) {
        return new ObjectIdWriter(idType, propName == null ? null : new SerializedString(propName), generator, null, alwaysAsId);
    }

    public ObjectIdWriter withSerializer(JsonSerializer<?> ser) {
        return new ObjectIdWriter(this.idType, this.propertyName, this.generator, ser, this.alwaysAsId);
    }

    public ObjectIdWriter withAlwaysAsId(boolean newState) {
        return newState == this.alwaysAsId ? this : new ObjectIdWriter(this.idType, this.propertyName, this.generator, this.serializer, newState);
    }
}
