package com.fasterxml.jackson.databind.deser.std;

import android.support.v4.view.MotionEventCompat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@JacksonStdImpl
public class StdKeyDeserializer extends KeyDeserializer implements Serializable {
    public static final int TYPE_BOOLEAN = 1;
    public static final int TYPE_BYTE = 2;
    public static final int TYPE_CALENDAR = 11;
    public static final int TYPE_CHAR = 4;
    public static final int TYPE_CLASS = 15;
    public static final int TYPE_CURRENCY = 16;
    public static final int TYPE_DATE = 10;
    public static final int TYPE_DOUBLE = 8;
    public static final int TYPE_FLOAT = 7;
    public static final int TYPE_INT = 5;
    public static final int TYPE_LOCALE = 9;
    public static final int TYPE_LONG = 6;
    public static final int TYPE_SHORT = 3;
    public static final int TYPE_URI = 13;
    public static final int TYPE_URL = 14;
    public static final int TYPE_UUID = 12;
    private static final long serialVersionUID = 1;
    protected final FromStringDeserializer<?> _deser;
    protected final Class<?> _keyClass;
    protected final int _kind;

    static final class DelegatingKD extends KeyDeserializer implements Serializable {
        private static final long serialVersionUID = 1;
        protected final JsonDeserializer<?> _delegate;
        protected final Class<?> _keyClass;

        protected DelegatingKD(Class<?> cls, JsonDeserializer<?> deser) {
            this._keyClass = cls;
            this._delegate = deser;
        }

        public final Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (key == null) {
                return null;
            }
            try {
                Object result = this._delegate.deserialize(ctxt.getParser(), ctxt);
                if (result != null) {
                    return result;
                }
                throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation");
            } catch (Exception re) {
                throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation: " + re.getMessage());
            }
        }

        public Class<?> getKeyClass() {
            return this._keyClass;
        }
    }

    @JacksonStdImpl
    static final class EnumKD extends StdKeyDeserializer {
        private static final long serialVersionUID = 1;
        protected final AnnotatedMethod _factory;
        protected final EnumResolver _resolver;

        protected EnumKD(EnumResolver er, AnnotatedMethod factory) {
            super(-1, er.getEnumClass());
            this._resolver = er;
            this._factory = factory;
        }

        public Object _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            Object call1;
            if (this._factory != null) {
                try {
                    call1 = this._factory.call1(key);
                } catch (Exception e) {
                    ClassUtil.unwrapAndThrowAsIAE(e);
                }
                return call1;
            }
            call1 = this._resolver.findEnum(key);
            if (call1 == null && !ctxt.getConfig().isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
                throw ctxt.weirdKeyException(this._keyClass, key, "not one of values for Enum class");
            }
            return call1;
        }
    }

    static final class StringCtorKeyDeserializer extends StdKeyDeserializer {
        private static final long serialVersionUID = 1;
        protected final Constructor<?> _ctor;

        public StringCtorKeyDeserializer(Constructor<?> ctor) {
            super(-1, ctor.getDeclaringClass());
            this._ctor = ctor;
        }

        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            Constructor constructor = this._ctor;
            Object[] objArr = new Object[StdKeyDeserializer.TYPE_BOOLEAN];
            objArr[0] = key;
            return constructor.newInstance(objArr);
        }
    }

    static final class StringFactoryKeyDeserializer extends StdKeyDeserializer {
        private static final long serialVersionUID = 1;
        final Method _factoryMethod;

        public StringFactoryKeyDeserializer(Method fm) {
            super(-1, fm.getDeclaringClass());
            this._factoryMethod = fm;
        }

        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            Method method = this._factoryMethod;
            Object[] objArr = new Object[StdKeyDeserializer.TYPE_BOOLEAN];
            objArr[0] = key;
            return method.invoke(null, objArr);
        }
    }

    @JacksonStdImpl
    static final class StringKD extends StdKeyDeserializer {
        private static final StringKD sObject;
        private static final StringKD sString;
        private static final long serialVersionUID = 1;

        static {
            sString = new StringKD(String.class);
            sObject = new StringKD(Object.class);
        }

        private StringKD(Class<?> nominalType) {
            super(-1, nominalType);
        }

        public static StringKD forType(Class<?> nominalType) {
            if (nominalType == String.class) {
                return sString;
            }
            if (nominalType == Object.class) {
                return sObject;
            }
            return new StringKD(nominalType);
        }

        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return key;
        }
    }

    protected StdKeyDeserializer(int kind, Class<?> cls) {
        this(kind, cls, null);
    }

    protected StdKeyDeserializer(int kind, Class<?> cls, FromStringDeserializer<?> deser) {
        this._kind = kind;
        this._keyClass = cls;
        this._deser = deser;
    }

    public static StdKeyDeserializer forType(Class<?> raw) {
        if (raw == String.class || raw == Object.class) {
            return StringKD.forType(raw);
        }
        int kind;
        if (raw == UUID.class) {
            kind = TYPE_UUID;
        } else if (raw == Integer.class) {
            kind = TYPE_INT;
        } else if (raw == Long.class) {
            kind = TYPE_LONG;
        } else if (raw == Date.class) {
            kind = TYPE_DATE;
        } else if (raw == Calendar.class) {
            kind = TYPE_CALENDAR;
        } else if (raw == Boolean.class) {
            kind = TYPE_BOOLEAN;
        } else if (raw == Byte.class) {
            kind = TYPE_BYTE;
        } else if (raw == Character.class) {
            kind = TYPE_CHAR;
        } else if (raw == Short.class) {
            kind = TYPE_SHORT;
        } else if (raw == Float.class) {
            kind = TYPE_FLOAT;
        } else if (raw == Double.class) {
            kind = TYPE_DOUBLE;
        } else if (raw == URI.class) {
            kind = TYPE_URI;
        } else if (raw == URL.class) {
            kind = TYPE_URL;
        } else if (raw == Class.class) {
            kind = TYPE_CLASS;
        } else if (raw == Locale.class) {
            return new StdKeyDeserializer(TYPE_LOCALE, raw, FromStringDeserializer.findDeserializer(Locale.class));
        } else {
            if (raw == Currency.class) {
                return new StdKeyDeserializer(TYPE_CURRENCY, raw, FromStringDeserializer.findDeserializer(Currency.class));
            }
            return null;
        }
        return new StdKeyDeserializer(kind, raw);
    }

    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (key == null) {
            return null;
        }
        try {
            Object result = _parse(key, ctxt);
            if (result != null) {
                return result;
            }
            if (this._keyClass.isEnum() && ctxt.getConfig().isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
                return null;
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation");
        } catch (Exception re) {
            throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation: " + re.getMessage());
        }
    }

    public Class<?> getKeyClass() {
        return this._keyClass;
    }

    protected Object _parse(String key, DeserializationContext ctxt) throws Exception {
        int value;
        switch (this._kind) {
            case TYPE_BOOLEAN /*1*/:
                if ("true".equals(key)) {
                    return Boolean.TRUE;
                }
                if ("false".equals(key)) {
                    return Boolean.FALSE;
                }
                throw ctxt.weirdKeyException(this._keyClass, key, "value not 'true' or 'false'");
            case TYPE_BYTE /*2*/:
                value = _parseInt(key);
                if (value >= -128 && value <= MotionEventCompat.ACTION_MASK) {
                    return Byte.valueOf((byte) value);
                }
                throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 8-bit value");
            case TYPE_SHORT /*3*/:
                value = _parseInt(key);
                if (value >= -32768 && value <= 32767) {
                    return Short.valueOf((short) value);
                }
                throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 16-bit value");
            case TYPE_CHAR /*4*/:
                if (key.length() == TYPE_BOOLEAN) {
                    return Character.valueOf(key.charAt(0));
                }
                throw ctxt.weirdKeyException(this._keyClass, key, "can only convert 1-character Strings");
            case TYPE_INT /*5*/:
                return Integer.valueOf(_parseInt(key));
            case TYPE_LONG /*6*/:
                return Long.valueOf(_parseLong(key));
            case TYPE_FLOAT /*7*/:
                return Float.valueOf((float) _parseDouble(key));
            case TYPE_DOUBLE /*8*/:
                return Double.valueOf(_parseDouble(key));
            case TYPE_LOCALE /*9*/:
                try {
                    return this._deser._deserialize(key, ctxt);
                } catch (IOException e) {
                    throw ctxt.weirdKeyException(this._keyClass, key, "unable to parse key as locale");
                }
            case TYPE_DATE /*10*/:
                return ctxt.parseDate(key);
            case TYPE_CALENDAR /*11*/:
                Date date = ctxt.parseDate(key);
                if (date != null) {
                    return ctxt.constructCalendar(date);
                }
                return null;
            case TYPE_UUID /*12*/:
                return UUID.fromString(key);
            case TYPE_URI /*13*/:
                return URI.create(key);
            case TYPE_URL /*14*/:
                return new URL(key);
            case TYPE_CLASS /*15*/:
                try {
                    return ctxt.findClass(key);
                } catch (Exception e2) {
                    throw ctxt.weirdKeyException(this._keyClass, key, "unable to parse key as Class");
                }
            case TYPE_CURRENCY /*16*/:
                try {
                    return this._deser._deserialize(key, ctxt);
                } catch (IOException e3) {
                    throw ctxt.weirdKeyException(this._keyClass, key, "unable to parse key as currency");
                }
            default:
                return null;
        }
    }

    protected int _parseInt(String key) throws IllegalArgumentException {
        return Integer.parseInt(key);
    }

    protected long _parseLong(String key) throws IllegalArgumentException {
        return Long.parseLong(key);
    }

    protected double _parseDouble(String key) throws IllegalArgumentException {
        return NumberInput.parseDouble(key);
    }
}
