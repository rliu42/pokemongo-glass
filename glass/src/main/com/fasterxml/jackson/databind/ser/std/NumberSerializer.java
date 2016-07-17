package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

@JacksonStdImpl
public class NumberSerializer extends StdScalarSerializer<Number> {
    public static final NumberSerializer instance;
    protected final boolean _isInt;

    static {
        instance = new NumberSerializer(Number.class);
    }

    @Deprecated
    public NumberSerializer() {
        super(Number.class);
        this._isInt = false;
    }

    public NumberSerializer(Class<? extends Number> rawType) {
        boolean z = false;
        super(rawType, false);
        if (rawType == BigInteger.class) {
            z = true;
        }
        this._isInt = z;
    }

    public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value instanceof BigDecimal) {
            jgen.writeNumber((BigDecimal) value);
        } else if (value instanceof BigInteger) {
            jgen.writeNumber((BigInteger) value);
        } else if (value instanceof Integer) {
            jgen.writeNumber(value.intValue());
        } else if (value instanceof Long) {
            jgen.writeNumber(value.longValue());
        } else if (value instanceof Double) {
            jgen.writeNumber(value.doubleValue());
        } else if (value instanceof Float) {
            jgen.writeNumber(value.floatValue());
        } else if ((value instanceof Byte) || (value instanceof Short)) {
            jgen.writeNumber(value.intValue());
        } else {
            jgen.writeNumber(value.toString());
        }
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return createSchemaNode(this._isInt ? "integer" : "number", true);
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        if (this._isInt) {
            JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
            if (v2 != null) {
                v2.numberType(NumberType.BIG_INTEGER);
                return;
            }
            return;
        }
        JsonNumberFormatVisitor v22 = visitor.expectNumberFormat(typeHint);
        if (v22 != null && handledType() == BigDecimal.class) {
            v22.numberType(NumberType.BIG_DECIMAL);
        }
    }
}
