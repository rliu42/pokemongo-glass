package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.TimeZone;

public class DateDeserializers {
    private static final HashSet<String> _classNames;

    protected static abstract class DateBasedDeserializer<T> extends StdScalarDeserializer<T> implements ContextualDeserializer {
        protected final DateFormat _customFormat;
        protected final String _formatString;

        protected abstract DateBasedDeserializer<T> withDateFormat(DateFormat dateFormat, String str);

        protected DateBasedDeserializer(Class<?> clz) {
            super((Class) clz);
            this._customFormat = null;
            this._formatString = null;
        }

        protected DateBasedDeserializer(DateBasedDeserializer<T> base, DateFormat format, String formatStr) {
            super(base._valueClass);
            this._customFormat = format;
            this._formatString = formatStr;
        }

        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            if (property == null) {
                return this;
            }
            Value format = ctxt.getAnnotationIntrospector().findFormat(property.getMember());
            if (format == null) {
                return this;
            }
            TimeZone tz = format.getTimeZone();
            if (format.hasPattern()) {
                String pattern = format.getPattern();
                SimpleDateFormat df = new SimpleDateFormat(pattern, format.hasLocale() ? format.getLocale() : ctxt.getLocale());
                if (tz == null) {
                    tz = ctxt.getTimeZone();
                }
                df.setTimeZone(tz);
                return withDateFormat(df, pattern);
            } else if (tz == null) {
                return this;
            } else {
                DateFormat df2 = ctxt.getConfig().getDateFormat();
                if (df2.getClass() == StdDateFormat.class) {
                    df2 = ((StdDateFormat) df2).withTimeZone(tz).withLocale(format.hasLocale() ? format.getLocale() : ctxt.getLocale());
                } else {
                    df2 = (DateFormat) df2.clone();
                    df2.setTimeZone(tz);
                }
                return withDateFormat(df2, this._formatString);
            }
        }

        protected Date _parseDate(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (this._customFormat != null) {
                JsonToken t = p.getCurrentToken();
                if (t == JsonToken.VALUE_STRING) {
                    String str = p.getText().trim();
                    if (str.length() == 0) {
                        return (Date) getEmptyValue(ctxt);
                    }
                    Date parse;
                    synchronized (this._customFormat) {
                        try {
                            parse = this._customFormat.parse(str);
                        } catch (ParseException e) {
                            throw new IllegalArgumentException("Failed to parse Date value '" + str + "' (format: \"" + this._formatString + "\"): " + e.getMessage());
                        }
                    }
                    return parse;
                } else if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    p.nextToken();
                    Date parsed = _parseDate(p, ctxt);
                    if (p.nextToken() == JsonToken.END_ARRAY) {
                        return parsed;
                    }
                    throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'java.util.Date' value but there was more than a single value in the array");
                }
            }
            return super._parseDate(p, ctxt);
        }
    }

    @JacksonStdImpl
    public static class CalendarDeserializer extends DateBasedDeserializer<Calendar> {
        protected final Class<? extends Calendar> _calendarClass;

        public /* bridge */ /* synthetic */ JsonDeserializer createContextual(DeserializationContext x0, BeanProperty x1) throws JsonMappingException {
            return super.createContextual(x0, x1);
        }

        public CalendarDeserializer() {
            super(Calendar.class);
            this._calendarClass = null;
        }

        public CalendarDeserializer(Class<? extends Calendar> cc) {
            super(cc);
            this._calendarClass = cc;
        }

        public CalendarDeserializer(CalendarDeserializer src, DateFormat df, String formatString) {
            super(src, df, formatString);
            this._calendarClass = src._calendarClass;
        }

        protected CalendarDeserializer withDateFormat(DateFormat df, String formatString) {
            return new CalendarDeserializer(this, df, formatString);
        }

        public Calendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Date d = _parseDate(jp, ctxt);
            if (d == null) {
                return null;
            }
            if (this._calendarClass == null) {
                return ctxt.constructCalendar(d);
            }
            try {
                Calendar c = (Calendar) this._calendarClass.newInstance();
                c.setTimeInMillis(d.getTime());
                TimeZone tz = ctxt.getTimeZone();
                if (tz == null) {
                    return c;
                }
                c.setTimeZone(tz);
                return c;
            } catch (Throwable e) {
                throw ctxt.instantiationException(this._calendarClass, e);
            }
        }
    }

    public static class DateDeserializer extends DateBasedDeserializer<Date> {
        public static final DateDeserializer instance;

        public /* bridge */ /* synthetic */ JsonDeserializer createContextual(DeserializationContext x0, BeanProperty x1) throws JsonMappingException {
            return super.createContextual(x0, x1);
        }

        static {
            instance = new DateDeserializer();
        }

        public DateDeserializer() {
            super(Date.class);
        }

        public DateDeserializer(DateDeserializer base, DateFormat df, String formatString) {
            super(base, df, formatString);
        }

        protected DateDeserializer withDateFormat(DateFormat df, String formatString) {
            return new DateDeserializer(this, df, formatString);
        }

        public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return _parseDate(jp, ctxt);
        }
    }

    public static class SqlDateDeserializer extends DateBasedDeserializer<java.sql.Date> {
        public /* bridge */ /* synthetic */ JsonDeserializer createContextual(DeserializationContext x0, BeanProperty x1) throws JsonMappingException {
            return super.createContextual(x0, x1);
        }

        public SqlDateDeserializer() {
            super(java.sql.Date.class);
        }

        public SqlDateDeserializer(SqlDateDeserializer src, DateFormat df, String formatString) {
            super(src, df, formatString);
        }

        protected SqlDateDeserializer withDateFormat(DateFormat df, String formatString) {
            return new SqlDateDeserializer(this, df, formatString);
        }

        public java.sql.Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Date d = _parseDate(jp, ctxt);
            return d == null ? null : new java.sql.Date(d.getTime());
        }
    }

    public static class TimestampDeserializer extends DateBasedDeserializer<Timestamp> {
        public /* bridge */ /* synthetic */ JsonDeserializer createContextual(DeserializationContext x0, BeanProperty x1) throws JsonMappingException {
            return super.createContextual(x0, x1);
        }

        public TimestampDeserializer() {
            super(Timestamp.class);
        }

        public TimestampDeserializer(TimestampDeserializer src, DateFormat df, String formatString) {
            super(src, df, formatString);
        }

        protected TimestampDeserializer withDateFormat(DateFormat df, String formatString) {
            return new TimestampDeserializer(this, df, formatString);
        }

        public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new Timestamp(_parseDate(jp, ctxt).getTime());
        }
    }

    static {
        _classNames = new HashSet();
        for (Class<?> cls : new Class[]{Calendar.class, GregorianCalendar.class, java.sql.Date.class, Date.class, Timestamp.class}) {
            _classNames.add(cls.getName());
        }
    }

    public static JsonDeserializer<?> find(Class<?> rawType, String clsName) {
        if (_classNames.contains(clsName)) {
            if (rawType == Calendar.class) {
                return new CalendarDeserializer();
            }
            if (rawType == Date.class) {
                return DateDeserializer.instance;
            }
            if (rawType == java.sql.Date.class) {
                return new SqlDateDeserializer();
            }
            if (rawType == Timestamp.class) {
                return new TimestampDeserializer();
            }
            if (rawType == GregorianCalendar.class) {
                return new CalendarDeserializer(GregorianCalendar.class);
            }
        }
        return null;
    }
}
