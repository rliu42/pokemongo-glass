package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;
import java.util.List;

public abstract class ValueNode extends BaseJsonNode {
    public abstract JsonToken asToken();

    protected ValueNode() {
    }

    protected JsonNode _at(JsonPointer ptr) {
        return MissingNode.getInstance();
    }

    public <T extends JsonNode> T deepCopy() {
        return this;
    }

    public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
        typeSer.writeTypePrefixForScalar(this, jg);
        serialize(jg, provider);
        typeSer.writeTypeSuffixForScalar(this, jg);
    }

    public String toString() {
        return asText();
    }

    public final JsonNode get(int index) {
        return null;
    }

    public final JsonNode path(int index) {
        return MissingNode.getInstance();
    }

    public final boolean has(int index) {
        return false;
    }

    public final boolean hasNonNull(int index) {
        return false;
    }

    public final JsonNode get(String fieldName) {
        return null;
    }

    public final JsonNode path(String fieldName) {
        return MissingNode.getInstance();
    }

    public final boolean has(String fieldName) {
        return false;
    }

    public final boolean hasNonNull(String fieldName) {
        return false;
    }

    public final JsonNode findValue(String fieldName) {
        return null;
    }

    public final ObjectNode findParent(String fieldName) {
        return null;
    }

    public final List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
        return foundSoFar;
    }

    public final List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        return foundSoFar;
    }

    public final List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        return foundSoFar;
    }
}
