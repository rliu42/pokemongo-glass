package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.NumberOutput;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class DoubleNode extends NumericNode {
    protected final double _value;

    public DoubleNode(double v) {
        this._value = v;
    }

    public static DoubleNode valueOf(double v) {
        return new DoubleNode(v);
    }

    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_FLOAT;
    }

    public NumberType numberType() {
        return NumberType.DOUBLE;
    }

    public boolean isFloatingPointNumber() {
        return true;
    }

    public boolean isDouble() {
        return true;
    }

    public boolean canConvertToInt() {
        return this._value >= -2.147483648E9d && this._value <= 2.147483647E9d;
    }

    public boolean canConvertToLong() {
        return this._value >= -9.223372036854776E18d && this._value <= 9.223372036854776E18d;
    }

    public Number numberValue() {
        return Double.valueOf(this._value);
    }

    public short shortValue() {
        return (short) ((int) this._value);
    }

    public int intValue() {
        return (int) this._value;
    }

    public long longValue() {
        return (long) this._value;
    }

    public float floatValue() {
        return (float) this._value;
    }

    public double doubleValue() {
        return this._value;
    }

    public BigDecimal decimalValue() {
        return BigDecimal.valueOf(this._value);
    }

    public BigInteger bigIntegerValue() {
        return decimalValue().toBigInteger();
    }

    public String asText() {
        return NumberOutput.toString(this._value);
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeNumber(this._value);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof DoubleNode)) {
            return false;
        }
        if (Double.compare(this._value, ((DoubleNode) o)._value) != 0) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long l = Double.doubleToLongBits(this._value);
        return ((int) l) ^ ((int) (l >> 32));
    }
}
