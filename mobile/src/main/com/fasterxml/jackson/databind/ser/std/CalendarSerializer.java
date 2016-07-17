package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

@JacksonStdImpl
public class CalendarSerializer extends DateTimeSerializerBase<Calendar> {
    public static final CalendarSerializer instance;

    static {
        instance = new CalendarSerializer();
    }

    public CalendarSerializer() {
        this(null, null);
    }

    public CalendarSerializer(Boolean useTimestamp, DateFormat customFormat) {
        super(Calendar.class, useTimestamp, customFormat);
    }

    public CalendarSerializer withFormat(Boolean timestamp, DateFormat customFormat) {
        return new CalendarSerializer(timestamp, customFormat);
    }

    protected long _timestamp(Calendar value) {
        return value == null ? 0 : value.getTimeInMillis();
    }

    public void serialize(Calendar value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (_asTimestamp(provider)) {
            jgen.writeNumber(_timestamp(value));
        } else if (this._customFormat != null) {
            synchronized (this._customFormat) {
                jgen.writeString(this._customFormat.format(value.getTime()));
            }
        } else {
            provider.defaultSerializeDateValue(value.getTime(), jgen);
        }
    }
}
