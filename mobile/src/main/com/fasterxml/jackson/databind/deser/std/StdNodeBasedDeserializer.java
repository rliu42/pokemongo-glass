package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

public abstract class StdNodeBasedDeserializer<T> extends StdDeserializer<T> implements ResolvableDeserializer {
    private static final long serialVersionUID = 1;
    protected JsonDeserializer<Object> _treeDeserializer;

    public abstract T convert(JsonNode jsonNode, DeserializationContext deserializationContext) throws IOException;

    protected StdNodeBasedDeserializer(JavaType targetType) {
        super(targetType);
    }

    protected StdNodeBasedDeserializer(Class<T> targetType) {
        super((Class) targetType);
    }

    protected StdNodeBasedDeserializer(StdNodeBasedDeserializer<?> src) {
        super((StdDeserializer) src);
        this._treeDeserializer = src._treeDeserializer;
    }

    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        this._treeDeserializer = ctxt.findRootValueDeserializer(ctxt.constructType(JsonNode.class));
    }

    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return convert((JsonNode) this._treeDeserializer.deserialize(jp, ctxt), ctxt);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer td) throws IOException, JsonProcessingException {
        return convert((JsonNode) this._treeDeserializer.deserializeWithType(jp, ctxt, td), ctxt);
    }
}
