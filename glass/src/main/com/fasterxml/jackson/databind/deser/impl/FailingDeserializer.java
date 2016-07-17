package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class FailingDeserializer extends StdDeserializer<Object> {
    private static final long serialVersionUID = 1;
    protected final String _message;

    public FailingDeserializer(String m) {
        super(Object.class);
        this._message = m;
    }

    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws JsonMappingException {
        throw ctxt.mappingException(this._message);
    }
}
