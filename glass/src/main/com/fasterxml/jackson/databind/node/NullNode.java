package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public final class NullNode extends ValueNode {
    public static final NullNode instance;

    static {
        instance = new NullNode();
    }

    private NullNode() {
    }

    public static NullNode getInstance() {
        return instance;
    }

    public JsonNodeType getNodeType() {
        return JsonNodeType.NULL;
    }

    public JsonToken asToken() {
        return JsonToken.VALUE_NULL;
    }

    public String asText(String defaultValue) {
        return defaultValue;
    }

    public String asText() {
        return "null";
    }

    public final void serialize(JsonGenerator g, SerializerProvider provider) throws IOException {
        provider.defaultSerializeNull(g);
    }

    public boolean equals(Object o) {
        return o == this;
    }

    public int hashCode() {
        return JsonNodeType.NULL.ordinal();
    }
}
