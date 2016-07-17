package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public final class WritableObjectId {
    public final ObjectIdGenerator<?> generator;
    public Object id;
    protected boolean idWritten;

    public WritableObjectId(ObjectIdGenerator<?> generator) {
        this.idWritten = false;
        this.generator = generator;
    }

    public boolean writeAsId(JsonGenerator gen, SerializerProvider provider, ObjectIdWriter w) throws IOException {
        if (this.id == null || (!this.idWritten && !w.alwaysAsId)) {
            return false;
        }
        if (gen.canWriteObjectId()) {
            gen.writeObjectRef(String.valueOf(this.id));
        } else {
            w.serializer.serialize(this.id, gen, provider);
        }
        return true;
    }

    public Object generateId(Object forPojo) {
        Object generateId = this.generator.generateId(forPojo);
        this.id = generateId;
        return generateId;
    }

    public void writeAsField(JsonGenerator gen, SerializerProvider provider, ObjectIdWriter w) throws IOException {
        this.idWritten = true;
        if (gen.canWriteObjectId()) {
            gen.writeObjectId(String.valueOf(this.id));
            return;
        }
        SerializableString name = w.propertyName;
        if (name != null) {
            gen.writeFieldName(name);
            w.serializer.serialize(this.id, gen, provider);
        }
    }
}
