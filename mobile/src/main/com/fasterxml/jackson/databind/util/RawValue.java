package com.fasterxml.jackson.databind.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;

public class RawValue implements JsonSerializable {
    protected Object _value;

    public RawValue(String v) {
        this._value = v;
    }

    public RawValue(SerializableString v) {
        this._value = v;
    }

    public RawValue(JsonSerializable v) {
        this._value = v;
    }

    protected RawValue(Object value, boolean bogus) {
        this._value = value;
    }

    public Object rawValue() {
        return this._value;
    }

    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (this._value instanceof JsonSerializable) {
            ((JsonSerializable) this._value).serialize(gen, serializers);
        } else {
            _serialize(gen);
        }
    }

    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        if (this._value instanceof JsonSerializable) {
            ((JsonSerializable) this._value).serializeWithType(gen, serializers, typeSer);
        } else if (this._value instanceof SerializableString) {
            serialize(gen, serializers);
        }
    }

    public void serialize(JsonGenerator gen) throws IOException {
        if (this._value instanceof JsonSerializable) {
            gen.writeObject(this._value);
        } else {
            _serialize(gen);
        }
    }

    protected void _serialize(JsonGenerator gen) throws IOException {
        if (this._value instanceof SerializableString) {
            gen.writeRawValue((SerializableString) this._value);
        } else {
            gen.writeRawValue(String.valueOf(this._value));
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RawValue)) {
            return false;
        }
        RawValue other = (RawValue) o;
        if (this._value == other._value) {
            return true;
        }
        if (this._value == null || !this._value.equals(other._value)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this._value == null ? 0 : this._value.hashCode();
    }

    public String toString() {
        String str = "[RawValue of type %s]";
        Object[] objArr = new Object[1];
        objArr[0] = this._value == null ? "NULL" : this._value.getClass().getName();
        return String.format(str, objArr);
    }
}
