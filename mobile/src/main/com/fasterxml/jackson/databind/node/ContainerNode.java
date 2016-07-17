package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.RawValue;
import java.math.BigDecimal;
import java.math.BigInteger;
import spacemadness.com.lunarconsole.BuildConfig;

public abstract class ContainerNode<T extends ContainerNode<T>> extends BaseJsonNode implements JsonNodeCreator {
    protected final JsonNodeFactory _nodeFactory;

    public abstract JsonToken asToken();

    public abstract JsonNode get(int i);

    public abstract JsonNode get(String str);

    public abstract T removeAll();

    public abstract int size();

    protected ContainerNode(JsonNodeFactory nc) {
        this._nodeFactory = nc;
    }

    public String asText() {
        return BuildConfig.FLAVOR;
    }

    public final ArrayNode arrayNode() {
        return this._nodeFactory.arrayNode();
    }

    public final ObjectNode objectNode() {
        return this._nodeFactory.objectNode();
    }

    public final NullNode nullNode() {
        return this._nodeFactory.nullNode();
    }

    public final BooleanNode booleanNode(boolean v) {
        return this._nodeFactory.booleanNode(v);
    }

    public final NumericNode numberNode(byte v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(short v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(int v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(long v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(BigInteger v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(float v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(double v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(BigDecimal v) {
        return this._nodeFactory.numberNode(v);
    }

    public final ValueNode numberNode(Byte v) {
        return this._nodeFactory.numberNode(v);
    }

    public final ValueNode numberNode(Short v) {
        return this._nodeFactory.numberNode(v);
    }

    public final ValueNode numberNode(Integer v) {
        return this._nodeFactory.numberNode(v);
    }

    public final ValueNode numberNode(Long v) {
        return this._nodeFactory.numberNode(v);
    }

    public final ValueNode numberNode(Float v) {
        return this._nodeFactory.numberNode(v);
    }

    public final ValueNode numberNode(Double v) {
        return this._nodeFactory.numberNode(v);
    }

    public final TextNode textNode(String text) {
        return this._nodeFactory.textNode(text);
    }

    public final BinaryNode binaryNode(byte[] data) {
        return this._nodeFactory.binaryNode(data);
    }

    public final BinaryNode binaryNode(byte[] data, int offset, int length) {
        return this._nodeFactory.binaryNode(data, offset, length);
    }

    public final ValueNode pojoNode(Object pojo) {
        return this._nodeFactory.pojoNode(pojo);
    }

    public final ValueNode rawValueNode(RawValue value) {
        return this._nodeFactory.rawValueNode(value);
    }
}
