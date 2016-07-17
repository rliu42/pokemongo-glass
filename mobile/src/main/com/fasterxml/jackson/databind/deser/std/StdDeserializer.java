package com.fasterxml.jackson.databind.deser.std;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.nianticproject.holoholo.sfida.constants.BluetoothGattSupport;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

public abstract class StdDeserializer<T> extends JsonDeserializer<T> implements Serializable {
    protected static final int F_MASK_INT_COERCIONS;
    private static final long serialVersionUID = 1;
    protected final Class<?> _valueClass;

    static {
        F_MASK_INT_COERCIONS = DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.getMask() | DeserializationFeature.USE_LONG_FOR_INTS.getMask();
    }

    protected StdDeserializer(Class<?> vc) {
        this._valueClass = vc;
    }

    protected StdDeserializer(JavaType valueType) {
        this._valueClass = valueType == null ? null : valueType.getRawClass();
    }

    protected StdDeserializer(StdDeserializer<?> src) {
        this._valueClass = src._valueClass;
    }

    public Class<?> handledType() {
        return this._valueClass;
    }

    @Deprecated
    public final Class<?> getValueClass() {
        return this._valueClass;
    }

    public JavaType getValueType() {
        return null;
    }

    protected boolean isDefaultDeserializer(JsonDeserializer<?> deserializer) {
        return ClassUtil.isJacksonStdImpl((Object) deserializer);
    }

    protected boolean isDefaultKeyDeserializer(KeyDeserializer keyDeser) {
        return ClassUtil.isJacksonStdImpl((Object) keyDeser);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
    }

    protected final boolean _parseBooleanPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException {
        boolean z = true;
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_TRUE) {
            return true;
        }
        if (t == JsonToken.VALUE_FALSE || t == JsonToken.VALUE_NULL) {
            return false;
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            if (jp.getNumberType() != NumberType.INT) {
                return _parseBooleanFromNumber(jp, ctxt);
            }
            if (jp.getIntValue() == 0) {
                z = false;
            }
            return z;
        } else if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if ("true".equals(text) || "True".equals(text)) {
                return true;
            }
            if ("false".equals(text) || "False".equals(text) || text.length() == 0 || _hasTextualNull(text)) {
                return false;
            }
            throw ctxt.weirdStringException(text, this._valueClass, "only \"true\" or \"false\" recognized");
        } else if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
            jp.nextToken();
            boolean parsed = _parseBooleanPrimitive(jp, ctxt);
            if (jp.nextToken() == JsonToken.END_ARRAY) {
                return parsed;
            }
            throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'boolean' value but there was more than a single value in the array");
        } else {
            throw ctxt.mappingException(this._valueClass, t);
        }
    }

    protected final Boolean _parseBoolean(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken t = p.getCurrentToken();
        if (t == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        }
        if (t == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            if (p.getNumberType() == NumberType.INT) {
                return p.getIntValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
            } else {
                return Boolean.valueOf(_parseBooleanFromNumber(p, ctxt));
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return (Boolean) getNullValue(ctxt);
        } else {
            if (t == JsonToken.VALUE_STRING) {
                String text = p.getText().trim();
                if ("true".equals(text) || "True".equals(text)) {
                    return Boolean.TRUE;
                }
                if ("false".equals(text) || "False".equals(text)) {
                    return Boolean.FALSE;
                }
                if (text.length() == 0) {
                    return (Boolean) getEmptyValue(ctxt);
                }
                if (_hasTextualNull(text)) {
                    return (Boolean) getNullValue(ctxt);
                }
                throw ctxt.weirdStringException(text, this._valueClass, "only \"true\" or \"false\" recognized");
            } else if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                p.nextToken();
                Boolean parsed = _parseBoolean(p, ctxt);
                if (p.nextToken() == JsonToken.END_ARRAY) {
                    return parsed;
                }
                throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Boolean' value but there was more than a single value in the array");
            } else {
                throw ctxt.mappingException(this._valueClass, t);
            }
        }
    }

    protected final boolean _parseBooleanFromNumber(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.getNumberType() == NumberType.LONG) {
            return (jp.getLongValue() == 0 ? Boolean.FALSE : Boolean.TRUE).booleanValue();
        }
        String str = jp.getText();
        if ("0.0".equals(str) || "0".equals(str)) {
            return Boolean.FALSE.booleanValue();
        }
        return Boolean.TRUE.booleanValue();
    }

    protected Byte _parseByte(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken t = p.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return Byte.valueOf(p.getByteValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = p.getText().trim();
            if (_hasTextualNull(text)) {
                return (Byte) getNullValue(ctxt);
            }
            try {
                if (text.length() == 0) {
                    return (Byte) getEmptyValue(ctxt);
                }
                int value = NumberInput.parseInt(text);
                if (value >= -128 && value <= MotionEventCompat.ACTION_MASK) {
                    return Byte.valueOf((byte) value);
                }
                throw ctxt.weirdStringException(text, this._valueClass, "overflow, value can not be represented as 8-bit value");
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(text, this._valueClass, "not a valid Byte value");
            }
        } else if (t == JsonToken.VALUE_NUMBER_FLOAT) {
            if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
                _failDoubleToIntCoercion(p, ctxt, "Byte");
            }
            return Byte.valueOf(p.getByteValue());
        } else if (t == JsonToken.VALUE_NULL) {
            return (Byte) getNullValue(ctxt);
        } else {
            if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                p.nextToken();
                Byte parsed = _parseByte(p, ctxt);
                if (p.nextToken() == JsonToken.END_ARRAY) {
                    return parsed;
                }
                throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Byte' value but there was more than a single value in the array");
            }
            throw ctxt.mappingException(this._valueClass, t);
        }
    }

    protected Short _parseShort(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken t = p.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return Short.valueOf(p.getShortValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = p.getText().trim();
            try {
                if (text.length() == 0) {
                    return (Short) getEmptyValue(ctxt);
                }
                if (_hasTextualNull(text)) {
                    return (Short) getNullValue(ctxt);
                }
                int value = NumberInput.parseInt(text);
                if (value >= -32768 && value <= 32767) {
                    return Short.valueOf((short) value);
                }
                throw ctxt.weirdStringException(text, this._valueClass, "overflow, value can not be represented as 16-bit value");
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(text, this._valueClass, "not a valid Short value");
            }
        } else if (t == JsonToken.VALUE_NUMBER_FLOAT) {
            if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
                _failDoubleToIntCoercion(p, ctxt, "Short");
            }
            return Short.valueOf(p.getShortValue());
        } else if (t == JsonToken.VALUE_NULL) {
            return (Short) getNullValue(ctxt);
        } else {
            if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                p.nextToken();
                Short parsed = _parseShort(p, ctxt);
                if (p.nextToken() == JsonToken.END_ARRAY) {
                    return parsed;
                }
                throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Short' value but there was more than a single value in the array");
            }
            throw ctxt.mappingException(this._valueClass, t);
        }
    }

    protected final short _parseShortPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException {
        int value = _parseIntPrimitive(jp, ctxt);
        if (value >= -32768 && value <= 32767) {
            return (short) value;
        }
        throw ctxt.weirdStringException(String.valueOf(value), this._valueClass, "overflow, value can not be represented as 16-bit value");
    }

    protected final int _parseIntPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            return p.getIntValue();
        }
        JsonToken t = p.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            String text = p.getText().trim();
            if (_hasTextualNull(text)) {
                return F_MASK_INT_COERCIONS;
            }
            try {
                int len = text.length();
                if (len > 9) {
                    long l = Long.parseLong(text);
                    if (l >= -2147483648L && l <= 2147483647L) {
                        return (int) l;
                    }
                    throw ctxt.weirdStringException(text, this._valueClass, "Overflow: numeric value (" + text + ") out of range of int (" + ExploreByTouchHelper.INVALID_ID + " - " + Integer.MAX_VALUE + ")");
                } else if (len != 0) {
                    return NumberInput.parseInt(text);
                } else {
                    return F_MASK_INT_COERCIONS;
                }
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(text, this._valueClass, "not a valid int value");
            }
        } else if (t == JsonToken.VALUE_NUMBER_FLOAT) {
            if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
                _failDoubleToIntCoercion(p, ctxt, "int");
            }
            return p.getValueAsInt();
        } else if (t == JsonToken.VALUE_NULL) {
            return F_MASK_INT_COERCIONS;
        } else {
            if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                p.nextToken();
                int parsed = _parseIntPrimitive(p, ctxt);
                if (p.nextToken() == JsonToken.END_ARRAY) {
                    return parsed;
                }
                throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'int' value but there was more than a single value in the array");
            }
            throw ctxt.mappingException(this._valueClass, t);
        }
    }

    protected final Integer _parseInteger(JsonParser p, DeserializationContext ctxt) throws IOException {
        switch (p.getCurrentTokenId()) {
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    p.nextToken();
                    Integer parsed = _parseInteger(p, ctxt);
                    if (p.nextToken() == JsonToken.END_ARRAY) {
                        return parsed;
                    }
                    throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Integer' value but there was more than a single value in the array");
                }
                break;
            case Place.TYPE_ATM /*6*/:
                String text = p.getText().trim();
                try {
                    int len = text.length();
                    if (_hasTextualNull(text)) {
                        return (Integer) getNullValue(ctxt);
                    }
                    if (len > 9) {
                        long l = Long.parseLong(text);
                        if (l >= -2147483648L && l <= 2147483647L) {
                            return Integer.valueOf((int) l);
                        }
                        throw ctxt.weirdStringException(text, this._valueClass, "Overflow: numeric value (" + text + ") out of range of Integer (" + ExploreByTouchHelper.INVALID_ID + " - " + Integer.MAX_VALUE + ")");
                    } else if (len == 0) {
                        return (Integer) getEmptyValue(ctxt);
                    } else {
                        return Integer.valueOf(NumberInput.parseInt(text));
                    }
                } catch (IllegalArgumentException e) {
                    throw ctxt.weirdStringException(text, this._valueClass, "not a valid Integer value");
                }
            case Place.TYPE_BAKERY /*7*/:
                return Integer.valueOf(p.getIntValue());
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
                    _failDoubleToIntCoercion(p, ctxt, "Integer");
                }
                return Integer.valueOf(p.getValueAsInt());
            case Place.TYPE_BICYCLE_STORE /*11*/:
                return (Integer) getNullValue(ctxt);
        }
        throw ctxt.mappingException(this._valueClass, p.getCurrentToken());
    }

    protected final Long _parseLong(JsonParser p, DeserializationContext ctxt) throws IOException {
        switch (p.getCurrentTokenId()) {
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    p.nextToken();
                    Long parsed = _parseLong(p, ctxt);
                    if (p.nextToken() == JsonToken.END_ARRAY) {
                        return parsed;
                    }
                    throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Long' value but there was more than a single value in the array");
                }
                break;
            case Place.TYPE_ATM /*6*/:
                String text = p.getText().trim();
                if (text.length() == 0) {
                    return (Long) getEmptyValue(ctxt);
                }
                if (_hasTextualNull(text)) {
                    return (Long) getNullValue(ctxt);
                }
                try {
                    return Long.valueOf(NumberInput.parseLong(text));
                } catch (IllegalArgumentException e) {
                    throw ctxt.weirdStringException(text, this._valueClass, "not a valid Long value");
                }
            case Place.TYPE_BAKERY /*7*/:
                return Long.valueOf(p.getLongValue());
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
                    _failDoubleToIntCoercion(p, ctxt, "Long");
                }
                return Long.valueOf(p.getValueAsLong());
            case Place.TYPE_BICYCLE_STORE /*11*/:
                return (Long) getNullValue(ctxt);
        }
        throw ctxt.mappingException(this._valueClass, p.getCurrentToken());
    }

    protected final long _parseLongPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
        switch (p.getCurrentTokenId()) {
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    p.nextToken();
                    long parsed = _parseLongPrimitive(p, ctxt);
                    if (p.nextToken() == JsonToken.END_ARRAY) {
                        return parsed;
                    }
                    throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'long' value but there was more than a single value in the array");
                }
                break;
            case Place.TYPE_ATM /*6*/:
                String text = p.getText().trim();
                if (text.length() == 0 || _hasTextualNull(text)) {
                    return 0;
                }
                try {
                    return NumberInput.parseLong(text);
                } catch (IllegalArgumentException e) {
                    throw ctxt.weirdStringException(text, this._valueClass, "not a valid long value");
                }
            case Place.TYPE_BAKERY /*7*/:
                return p.getLongValue();
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
                    _failDoubleToIntCoercion(p, ctxt, "long");
                }
                return p.getValueAsLong();
            case Place.TYPE_BICYCLE_STORE /*11*/:
                return 0;
        }
        throw ctxt.mappingException(this._valueClass, p.getCurrentToken());
    }

    protected final Float _parseFloat(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Float.valueOf(jp.getFloatValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return (Float) getEmptyValue(ctxt);
            }
            if (_hasTextualNull(text)) {
                return (Float) getNullValue(ctxt);
            }
            switch (text.charAt(F_MASK_INT_COERCIONS)) {
                case Place.TYPE_HAIR_CARE /*45*/:
                    if (_isNegInf(text)) {
                        return Float.valueOf(Float.NEGATIVE_INFINITY);
                    }
                    break;
                case Place.TYPE_PHYSIOTHERAPIST /*73*/:
                    if (_isPosInf(text)) {
                        return Float.valueOf(Float.POSITIVE_INFINITY);
                    }
                    break;
                case Place.TYPE_REAL_ESTATE_AGENCY /*78*/:
                    if (_isNaN(text)) {
                        return Float.valueOf(Float.NaN);
                    }
                    break;
            }
            try {
                return Float.valueOf(Float.parseFloat(text));
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(text, this._valueClass, "not a valid Float value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return (Float) getNullValue(ctxt);
        } else {
            if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                jp.nextToken();
                Float parsed = _parseFloat(jp, ctxt);
                if (jp.nextToken() == JsonToken.END_ARRAY) {
                    return parsed;
                }
                throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Byte' value but there was more than a single value in the array");
            }
            throw ctxt.mappingException(this._valueClass, t);
        }
    }

    protected final float _parseFloatPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getFloatValue();
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0 || _hasTextualNull(text)) {
                return 0.0f;
            }
            switch (text.charAt(F_MASK_INT_COERCIONS)) {
                case Place.TYPE_HAIR_CARE /*45*/:
                    if (_isNegInf(text)) {
                        return Float.NEGATIVE_INFINITY;
                    }
                    break;
                case Place.TYPE_PHYSIOTHERAPIST /*73*/:
                    if (_isPosInf(text)) {
                        return Float.POSITIVE_INFINITY;
                    }
                    break;
                case Place.TYPE_REAL_ESTATE_AGENCY /*78*/:
                    if (_isNaN(text)) {
                        return Float.NaN;
                    }
                    break;
            }
            try {
                return Float.parseFloat(text);
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(text, this._valueClass, "not a valid float value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return 0.0f;
        } else {
            if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                jp.nextToken();
                float parsed = _parseFloatPrimitive(jp, ctxt);
                if (jp.nextToken() == JsonToken.END_ARRAY) {
                    return parsed;
                }
                throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'float' value but there was more than a single value in the array");
            }
            throw ctxt.mappingException(this._valueClass, t);
        }
    }

    protected final Double _parseDouble(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Double.valueOf(jp.getDoubleValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return (Double) getEmptyValue(ctxt);
            }
            if (_hasTextualNull(text)) {
                return (Double) getNullValue(ctxt);
            }
            switch (text.charAt(F_MASK_INT_COERCIONS)) {
                case Place.TYPE_HAIR_CARE /*45*/:
                    if (_isNegInf(text)) {
                        return Double.valueOf(Double.NEGATIVE_INFINITY);
                    }
                    break;
                case Place.TYPE_PHYSIOTHERAPIST /*73*/:
                    if (_isPosInf(text)) {
                        return Double.valueOf(Double.POSITIVE_INFINITY);
                    }
                    break;
                case Place.TYPE_REAL_ESTATE_AGENCY /*78*/:
                    if (_isNaN(text)) {
                        return Double.valueOf(Double.NaN);
                    }
                    break;
            }
            try {
                return Double.valueOf(parseDouble(text));
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(text, this._valueClass, "not a valid Double value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return (Double) getNullValue(ctxt);
        } else {
            if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                jp.nextToken();
                Double parsed = _parseDouble(jp, ctxt);
                if (jp.nextToken() == JsonToken.END_ARRAY) {
                    return parsed;
                }
                throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Double' value but there was more than a single value in the array");
            }
            throw ctxt.mappingException(this._valueClass, t);
        }
    }

    protected final double _parseDoublePrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getDoubleValue();
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0 || _hasTextualNull(text)) {
                return 0.0d;
            }
            switch (text.charAt(F_MASK_INT_COERCIONS)) {
                case Place.TYPE_HAIR_CARE /*45*/:
                    if (_isNegInf(text)) {
                        return Double.NEGATIVE_INFINITY;
                    }
                    break;
                case Place.TYPE_PHYSIOTHERAPIST /*73*/:
                    if (_isPosInf(text)) {
                        return Double.POSITIVE_INFINITY;
                    }
                    break;
                case Place.TYPE_REAL_ESTATE_AGENCY /*78*/:
                    if (_isNaN(text)) {
                        return Double.NaN;
                    }
                    break;
            }
            try {
                return parseDouble(text);
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(text, this._valueClass, "not a valid double value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return 0.0d;
        } else {
            if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                jp.nextToken();
                double parsed = _parseDoublePrimitive(jp, ctxt);
                if (jp.nextToken() == JsonToken.END_ARRAY) {
                    return parsed;
                }
                throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Byte' value but there was more than a single value in the array");
            }
            throw ctxt.mappingException(this._valueClass, t);
        }
    }

    protected Date _parseDate(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return new Date(jp.getLongValue());
        }
        if (t == JsonToken.VALUE_NULL) {
            return (Date) getNullValue(ctxt);
        }
        if (t == JsonToken.VALUE_STRING) {
            try {
                String value = jp.getText().trim();
                if (value.length() == 0) {
                    return (Date) getEmptyValue(ctxt);
                }
                if (_hasTextualNull(value)) {
                    return (Date) getNullValue(ctxt);
                }
                return ctxt.parseDate(value);
            } catch (IllegalArgumentException iae) {
                throw ctxt.weirdStringException(null, this._valueClass, "not a valid representation (error: " + iae.getMessage() + ")");
            }
        } else if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
            jp.nextToken();
            Date parsed = _parseDate(jp, ctxt);
            if (jp.nextToken() == JsonToken.END_ARRAY) {
                return parsed;
            }
            throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'java.util.Date' value but there was more than a single value in the array");
        } else {
            throw ctxt.mappingException(this._valueClass, t);
        }
    }

    protected static final double parseDouble(String numStr) throws NumberFormatException {
        if (NumberInput.NASTY_SMALL_DOUBLE.equals(numStr)) {
            return Double.MIN_VALUE;
        }
        return Double.parseDouble(numStr);
    }

    protected final String _parseString(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            return jp.getText();
        }
        if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
            jp.nextToken();
            String parsed = _parseString(jp, ctxt);
            if (jp.nextToken() == JsonToken.END_ARRAY) {
                return parsed;
            }
            throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'String' value but there was more than a single value in the array");
        }
        String value = jp.getValueAsString();
        if (value != null) {
            return value;
        }
        throw ctxt.mappingException(String.class, jp.getCurrentToken());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected T _deserializeFromEmpty(com.fasterxml.jackson.core.JsonParser r5, com.fasterxml.jackson.databind.DeserializationContext r6) throws java.io.IOException {
        /*
        r4 = this;
        r3 = 0;
        r1 = r5.getCurrentToken();
        r2 = com.fasterxml.jackson.core.JsonToken.START_ARRAY;
        if (r1 != r2) goto L_0x0025;
    L_0x0009:
        r2 = com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT;
        r2 = r6.isEnabled(r2);
        if (r2 == 0) goto L_0x003f;
    L_0x0011:
        r1 = r5.nextToken();
        r2 = com.fasterxml.jackson.core.JsonToken.END_ARRAY;
        if (r1 != r2) goto L_0x001a;
    L_0x0019:
        return r3;
    L_0x001a:
        r2 = r4.handledType();
        r3 = com.fasterxml.jackson.core.JsonToken.START_ARRAY;
        r2 = r6.mappingException(r2, r3);
        throw r2;
    L_0x0025:
        r2 = com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
        if (r1 != r2) goto L_0x003f;
    L_0x0029:
        r2 = com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
        r2 = r6.isEnabled(r2);
        if (r2 == 0) goto L_0x003f;
    L_0x0031:
        r2 = r5.getText();
        r0 = r2.trim();
        r2 = r0.isEmpty();
        if (r2 != 0) goto L_0x0019;
    L_0x003f:
        r2 = r4.handledType();
        r2 = r6.mappingException(r2);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.databind.deser.std.StdDeserializer._deserializeFromEmpty(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext):T");
    }

    protected boolean _hasTextualNull(String value) {
        return "null".equals(value);
    }

    protected final boolean _isNegInf(String text) {
        return "-Infinity".equals(text) || "-INF".equals(text);
    }

    protected final boolean _isPosInf(String text) {
        return "Infinity".equals(text) || "INF".equals(text);
    }

    protected final boolean _isNaN(String text) {
        return "NaN".equals(text);
    }

    protected Object _coerceIntegral(JsonParser p, DeserializationContext ctxt) throws IOException {
        int feats = ctxt.getDeserializationFeatures();
        if (DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.enabledIn(feats)) {
            return p.getBigIntegerValue();
        }
        if (DeserializationFeature.USE_LONG_FOR_INTS.enabledIn(feats)) {
            return Long.valueOf(p.getLongValue());
        }
        return p.getBigIntegerValue();
    }

    protected JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, JavaType type, BeanProperty property) throws JsonMappingException {
        return ctxt.findContextualValueDeserializer(type, property);
    }

    protected final boolean _isIntNumber(String text) {
        int len = text.length();
        if (len <= 0) {
            return false;
        }
        int i;
        char c = text.charAt(F_MASK_INT_COERCIONS);
        if (c == '-' || c == '+') {
            i = 1;
        } else {
            i = F_MASK_INT_COERCIONS;
        }
        while (i < len) {
            int ch = text.charAt(i);
            if (ch > 57 || ch < 48) {
                return false;
            }
            i++;
        }
        return true;
    }

    protected JsonDeserializer<?> findConvertingContentDeserializer(DeserializationContext ctxt, BeanProperty prop, JsonDeserializer<?> existingDeserializer) throws JsonMappingException {
        AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
        if (!(intr == null || prop == null)) {
            AnnotatedMember member = prop.getMember();
            if (member != null) {
                Object convDef = intr.findDeserializationContentConverter(member);
                if (convDef != null) {
                    Converter<Object, Object> conv = ctxt.converterInstance(prop.getMember(), convDef);
                    JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
                    if (existingDeserializer == null) {
                        existingDeserializer = ctxt.findContextualValueDeserializer(delegateType, prop);
                    }
                    return new StdDelegatingDeserializer(conv, delegateType, existingDeserializer);
                }
            }
        }
        return existingDeserializer;
    }

    protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object instanceOrClass, String propName) throws IOException {
        if (instanceOrClass == null) {
            instanceOrClass = handledType();
        }
        if (!ctxt.handleUnknownProperty(jp, this, instanceOrClass, propName)) {
            ctxt.reportUnknownProperty(instanceOrClass, propName, this);
            jp.skipChildren();
        }
    }

    protected void _failDoubleToIntCoercion(JsonParser jp, DeserializationContext ctxt, String type) throws IOException {
        throw ctxt.mappingException("Can not coerce a floating-point value ('%s') into %s; enable `DeserializationFeature.ACCEPT_FLOAT_AS_INT` to allow", jp.getValueAsString(), type);
    }
}
