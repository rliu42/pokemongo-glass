package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public abstract class DateTimeSerializerBase<T> extends StdScalarSerializer<T> implements ContextualSerializer {
    protected final DateFormat _customFormat;
    protected final Boolean _useTimestamp;

    protected abstract long _timestamp(T t);

    public abstract void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException;

    public abstract DateTimeSerializerBase<T> withFormat(Boolean bool, DateFormat dateFormat);

    protected DateTimeSerializerBase(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
        super(type);
        this._useTimestamp = useTimestamp;
        this._customFormat = customFormat;
    }

    public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
        Boolean asNumber = null;
        if (property == null) {
            return this;
        }
        Value format = serializers.getAnnotationIntrospector().findFormat(property.getMember());
        if (format == null) {
            return this;
        }
        if (format.getShape().isNumeric()) {
            return withFormat(Boolean.TRUE, null);
        }
        if (format.getShape() == Shape.STRING) {
            asNumber = Boolean.FALSE;
        }
        TimeZone tz = format.getTimeZone();
        if (format.hasPattern()) {
            SimpleDateFormat df = new SimpleDateFormat(format.getPattern(), format.hasLocale() ? format.getLocale() : serializers.getLocale());
            if (tz == null) {
                tz = serializers.getTimeZone();
            }
            df.setTimeZone(tz);
            return withFormat(asNumber, df);
        } else if (tz == null) {
            return this;
        } else {
            DateFormat df2 = serializers.getConfig().getDateFormat();
            if (df2.getClass() == StdDateFormat.class) {
                df2 = StdDateFormat.getISO8601Format(tz, format.hasLocale() ? format.getLocale() : serializers.getLocale());
            } else {
                df2 = (DateFormat) df2.clone();
                df2.setTimeZone(tz);
            }
            return withFormat(asNumber, df2);
        }
    }

    @Deprecated
    public boolean isEmpty(T value) {
        return value == null || _timestamp(value) == 0;
    }

    public boolean isEmpty(SerializerProvider serializers, T value) {
        return value == null || _timestamp(value) == 0;
    }

    public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
        return createSchemaNode(_asTimestamp(serializers) ? "number" : "string", true);
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        _acceptJsonFormatVisitor(visitor, typeHint, _asTimestamp(visitor.getProvider()));
    }

    protected boolean _asTimestamp(SerializerProvider serializers) {
        if (this._useTimestamp != null) {
            return this._useTimestamp.booleanValue();
        }
        if (this._customFormat != null) {
            return false;
        }
        if (serializers != null) {
            return serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }
        throw new IllegalArgumentException("Null SerializerProvider passed for " + handledType().getName());
    }

    protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber) throws JsonMappingException {
        if (asNumber) {
            JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
            if (v2 != null) {
                v2.numberType(NumberType.LONG);
                v2.format(JsonValueFormat.UTC_MILLISEC);
                return;
            }
            return;
        }
        JsonStringFormatVisitor v22 = visitor.expectStringFormat(typeHint);
        if (v22 != null) {
            v22.format(JsonValueFormat.DATE_TIME);
        }
    }
}
