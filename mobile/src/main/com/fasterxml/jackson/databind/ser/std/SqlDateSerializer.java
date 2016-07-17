package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.DateFormat;

@JacksonStdImpl
public class SqlDateSerializer extends DateTimeSerializerBase<Date> {
    public SqlDateSerializer() {
        this(Boolean.FALSE);
    }

    protected SqlDateSerializer(Boolean useTimestamp) {
        super(Date.class, useTimestamp, null);
    }

    public SqlDateSerializer withFormat(Boolean timestamp, DateFormat customFormat) {
        return new SqlDateSerializer(timestamp);
    }

    protected long _timestamp(Date value) {
        return value == null ? 0 : value.getTime();
    }

    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (_asTimestamp(provider)) {
            gen.writeNumber(_timestamp(value));
        } else {
            gen.writeString(value.toString());
        }
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return createSchemaNode("string", true);
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        _acceptJsonFormatVisitor(visitor, typeHint, this._useTimestamp.booleanValue());
    }
}
