package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;
import spacemadness.com.lunarconsole.BuildConfig;

public final class MissingNode extends ValueNode {
    private static final MissingNode instance;

    static {
        instance = new MissingNode();
    }

    private MissingNode() {
    }

    public <T extends JsonNode> T deepCopy() {
        return this;
    }

    public static MissingNode getInstance() {
        return instance;
    }

    public JsonNodeType getNodeType() {
        return JsonNodeType.MISSING;
    }

    public JsonToken asToken() {
        return JsonToken.NOT_AVAILABLE;
    }

    public String asText() {
        return BuildConfig.FLAVOR;
    }

    public String asText(String defaultValue) {
        return defaultValue;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeNull();
    }

    public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
        jg.writeNull();
    }

    public boolean equals(Object o) {
        return o == this;
    }

    public String toString() {
        return BuildConfig.FLAVOR;
    }

    public int hashCode() {
        return JsonNodeType.MISSING.ordinal();
    }
}
