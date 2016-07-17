package com.fasterxml.jackson.databind.exc;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;

public class InvalidFormatException extends JsonMappingException {
    private static final long serialVersionUID = 1;
    protected final Class<?> _targetType;
    protected final Object _value;

    public InvalidFormatException(String msg, Object value, Class<?> targetType) {
        super(msg);
        this._value = value;
        this._targetType = targetType;
    }

    public InvalidFormatException(String msg, JsonLocation loc, Object value, Class<?> targetType) {
        super(msg, loc);
        this._value = value;
        this._targetType = targetType;
    }

    public static InvalidFormatException from(JsonParser jp, String msg, Object value, Class<?> targetType) {
        return new InvalidFormatException(msg, jp.getTokenLocation(), value, targetType);
    }

    public Object getValue() {
        return this._value;
    }

    public Class<?> getTargetType() {
        return this._targetType;
    }
}
