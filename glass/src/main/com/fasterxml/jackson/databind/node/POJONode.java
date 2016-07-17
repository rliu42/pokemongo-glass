package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;
import java.io.IOException;

public class POJONode extends ValueNode {
    protected final Object _value;

    public POJONode(Object v) {
        this._value = v;
    }

    public JsonNodeType getNodeType() {
        return JsonNodeType.POJO;
    }

    public JsonToken asToken() {
        return JsonToken.VALUE_EMBEDDED_OBJECT;
    }

    public byte[] binaryValue() throws IOException {
        if (this._value instanceof byte[]) {
            return (byte[]) this._value;
        }
        return super.binaryValue();
    }

    public String asText() {
        return this._value == null ? "null" : this._value.toString();
    }

    public String asText(String defaultValue) {
        return this._value == null ? defaultValue : this._value.toString();
    }

    public boolean asBoolean(boolean defaultValue) {
        if (this._value == null || !(this._value instanceof Boolean)) {
            return defaultValue;
        }
        return ((Boolean) this._value).booleanValue();
    }

    public int asInt(int defaultValue) {
        if (this._value instanceof Number) {
            return ((Number) this._value).intValue();
        }
        return defaultValue;
    }

    public long asLong(long defaultValue) {
        if (this._value instanceof Number) {
            return ((Number) this._value).longValue();
        }
        return defaultValue;
    }

    public double asDouble(double defaultValue) {
        if (this._value instanceof Number) {
            return ((Number) this._value).doubleValue();
        }
        return defaultValue;
    }

    public final void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (this._value == null) {
            serializers.defaultSerializeNull(gen);
        } else if (this._value instanceof JsonSerializable) {
            ((JsonSerializable) this._value).serialize(gen, serializers);
        } else {
            gen.writeObject(this._value);
        }
    }

    public Object getPojo() {
        return this._value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof POJONode)) {
            return false;
        }
        return _pojoEquals((POJONode) o);
    }

    protected boolean _pojoEquals(POJONode other) {
        if (this._value == null) {
            return other._value == null;
        } else {
            return this._value.equals(other._value);
        }
    }

    public int hashCode() {
        return this._value.hashCode();
    }

    public String toString() {
        if (this._value instanceof byte[]) {
            return String.format("(binary value of %d bytes)", new Object[]{Integer.valueOf(((byte[]) this._value).length)});
        } else if (!(this._value instanceof RawValue)) {
            return String.valueOf(this._value);
        } else {
            return String.format("(raw value '%s')", new Object[]{((RawValue) this._value).toString()});
        }
    }
}
