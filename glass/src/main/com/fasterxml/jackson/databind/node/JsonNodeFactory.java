package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.util.RawValue;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonNodeFactory implements Serializable, JsonNodeCreator {
    private static final JsonNodeFactory decimalsAsIs;
    private static final JsonNodeFactory decimalsNormalized;
    public static final JsonNodeFactory instance;
    private static final long serialVersionUID = 1;
    private final boolean _cfgBigDecimalExact;

    static {
        decimalsNormalized = new JsonNodeFactory(false);
        decimalsAsIs = new JsonNodeFactory(true);
        instance = decimalsNormalized;
    }

    public JsonNodeFactory(boolean bigDecimalExact) {
        this._cfgBigDecimalExact = bigDecimalExact;
    }

    protected JsonNodeFactory() {
        this(false);
    }

    public static JsonNodeFactory withExactBigDecimals(boolean bigDecimalExact) {
        return bigDecimalExact ? decimalsAsIs : decimalsNormalized;
    }

    public BooleanNode booleanNode(boolean v) {
        return v ? BooleanNode.getTrue() : BooleanNode.getFalse();
    }

    public NullNode nullNode() {
        return NullNode.getInstance();
    }

    public NumericNode numberNode(byte v) {
        return IntNode.valueOf(v);
    }

    public ValueNode numberNode(Byte value) {
        return value == null ? nullNode() : IntNode.valueOf(value.intValue());
    }

    public NumericNode numberNode(short v) {
        return ShortNode.valueOf(v);
    }

    public ValueNode numberNode(Short value) {
        return value == null ? nullNode() : ShortNode.valueOf(value.shortValue());
    }

    public NumericNode numberNode(int v) {
        return IntNode.valueOf(v);
    }

    public ValueNode numberNode(Integer value) {
        return value == null ? nullNode() : IntNode.valueOf(value.intValue());
    }

    public NumericNode numberNode(long v) {
        return LongNode.valueOf(v);
    }

    public ValueNode numberNode(Long value) {
        if (value == null) {
            return nullNode();
        }
        return LongNode.valueOf(value.longValue());
    }

    public NumericNode numberNode(BigInteger v) {
        return BigIntegerNode.valueOf(v);
    }

    public NumericNode numberNode(float v) {
        return FloatNode.valueOf(v);
    }

    public ValueNode numberNode(Float value) {
        return value == null ? nullNode() : FloatNode.valueOf(value.floatValue());
    }

    public NumericNode numberNode(double v) {
        return DoubleNode.valueOf(v);
    }

    public ValueNode numberNode(Double value) {
        return value == null ? nullNode() : DoubleNode.valueOf(value.doubleValue());
    }

    public NumericNode numberNode(BigDecimal v) {
        if (this._cfgBigDecimalExact) {
            return DecimalNode.valueOf(v);
        }
        return v.compareTo(BigDecimal.ZERO) == 0 ? DecimalNode.ZERO : DecimalNode.valueOf(v.stripTrailingZeros());
    }

    public TextNode textNode(String text) {
        return TextNode.valueOf(text);
    }

    public BinaryNode binaryNode(byte[] data) {
        return BinaryNode.valueOf(data);
    }

    public BinaryNode binaryNode(byte[] data, int offset, int length) {
        return BinaryNode.valueOf(data, offset, length);
    }

    public ArrayNode arrayNode() {
        return new ArrayNode(this);
    }

    public ObjectNode objectNode() {
        return new ObjectNode(this);
    }

    public ValueNode pojoNode(Object pojo) {
        return new POJONode(pojo);
    }

    public ValueNode rawValueNode(RawValue value) {
        return new POJONode(value);
    }

    protected boolean _inIntRange(long l) {
        return ((long) ((int) l)) == l;
    }
}
