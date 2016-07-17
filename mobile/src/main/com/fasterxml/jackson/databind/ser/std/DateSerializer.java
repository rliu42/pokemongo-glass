package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

@JacksonStdImpl
public class DateSerializer extends DateTimeSerializerBase<Date> {
    public static final DateSerializer instance;

    static {
        instance = new DateSerializer();
    }

    public DateSerializer() {
        this(null, null);
    }

    public DateSerializer(Boolean useTimestamp, DateFormat customFormat) {
        super(Date.class, useTimestamp, customFormat);
    }

    public DateSerializer withFormat(Boolean timestamp, DateFormat customFormat) {
        return new DateSerializer(timestamp, customFormat);
    }

    protected long _timestamp(Date value) {
        return value == null ? 0 : value.getTime();
    }

    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (_asTimestamp(provider)) {
            gen.writeNumber(_timestamp(value));
        } else if (this._customFormat != null) {
            synchronized (this._customFormat) {
                gen.writeString(this._customFormat.format(value));
            }
        } else {
            provider.defaultSerializeDateValue(value, gen);
        }
    }
}
