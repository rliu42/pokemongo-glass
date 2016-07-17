package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceSerializer extends StdSerializer<AtomicReference<?>> {
    private static final long serialVersionUID = 1;

    @Deprecated
    public AtomicReferenceSerializer() {
        super(AtomicReference.class, false);
    }

    public AtomicReferenceSerializer(ReferenceType type) {
        super((JavaType) type);
    }

    public boolean isEmpty(SerializerProvider provider, AtomicReference<?> value) {
        return value == null || value.get() == null;
    }

    public void serialize(AtomicReference<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        provider.defaultSerializeValue(value.get(), jgen);
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return createSchemaNode("any", true);
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        visitor.expectAnyFormat(typeHint);
    }
}
