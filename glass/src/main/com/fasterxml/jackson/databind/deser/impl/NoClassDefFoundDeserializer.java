package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class NoClassDefFoundDeserializer<T> extends JsonDeserializer<T> {
    private final NoClassDefFoundError _cause;

    public NoClassDefFoundDeserializer(NoClassDefFoundError cause) {
        this._cause = cause;
    }

    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        throw this._cause;
    }
}
