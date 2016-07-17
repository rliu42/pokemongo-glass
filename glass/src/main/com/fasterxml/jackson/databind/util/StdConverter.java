package com.fasterxml.jackson.databind.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public abstract class StdConverter<IN, OUT> implements Converter<IN, OUT> {
    public abstract OUT convert(IN in);

    public JavaType getInputType(TypeFactory typeFactory) {
        JavaType[] types = typeFactory.findTypeParameters(getClass(), Converter.class);
        if (types != null && types.length >= 2) {
            return types[0];
        }
        throw new IllegalStateException("Can not find OUT type parameter for Converter of type " + getClass().getName());
    }

    public JavaType getOutputType(TypeFactory typeFactory) {
        JavaType[] types = typeFactory.findTypeParameters(getClass(), Converter.class);
        if (types != null && types.length >= 2) {
            return types[1];
        }
        throw new IllegalStateException("Can not find OUT type parameter for Converter of type " + getClass().getName());
    }
}
