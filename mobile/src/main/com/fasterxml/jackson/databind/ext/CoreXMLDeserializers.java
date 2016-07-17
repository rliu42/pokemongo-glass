package com.fasterxml.jackson.databind.ext;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

public class CoreXMLDeserializers extends Base {
    protected static final int TYPE_DURATION = 1;
    protected static final int TYPE_G_CALENDAR = 2;
    protected static final int TYPE_QNAME = 3;
    static final DatatypeFactory _dataTypeFactory;

    public static class Std extends FromStringDeserializer<Object> {
        private static final long serialVersionUID = 1;
        protected final int _kind;

        public Std(Class<?> raw, int kind) {
            super(raw);
            this._kind = kind;
        }

        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (this._kind != CoreXMLDeserializers.TYPE_G_CALENDAR) {
                return super.deserialize(jp, ctxt);
            }
            Date d = _parseDate(jp, ctxt);
            if (d == null) {
                return null;
            }
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(d);
            TimeZone tz = ctxt.getTimeZone();
            if (tz != null) {
                calendar.setTimeZone(tz);
            }
            return CoreXMLDeserializers._dataTypeFactory.newXMLGregorianCalendar(calendar);
        }

        protected Object _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
            switch (this._kind) {
                case CoreXMLDeserializers.TYPE_DURATION /*1*/:
                    return CoreXMLDeserializers._dataTypeFactory.newDuration(value);
                case CoreXMLDeserializers.TYPE_QNAME /*3*/:
                    return QName.valueOf(value);
                default:
                    throw new IllegalStateException();
            }
        }
    }

    static {
        try {
            _dataTypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) {
        Class<?> raw = type.getRawClass();
        if (raw == QName.class) {
            return new Std(raw, TYPE_QNAME);
        }
        if (raw == XMLGregorianCalendar.class) {
            return new Std(raw, TYPE_G_CALENDAR);
        }
        if (raw == Duration.class) {
            return new Std(raw, TYPE_DURATION);
        }
        return null;
    }
}
