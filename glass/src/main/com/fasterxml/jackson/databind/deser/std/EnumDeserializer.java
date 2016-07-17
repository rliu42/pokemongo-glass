package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.CompactStringObjectMap;
import com.fasterxml.jackson.databind.util.EnumResolver;
import java.io.IOException;
import java.lang.reflect.Method;

@JacksonStdImpl
public class EnumDeserializer extends StdScalarDeserializer<Object> {
    private static final long serialVersionUID = 1;
    protected final CompactStringObjectMap _enumLookup;
    protected Object[] _enumsByIndex;

    protected static class FactoryBasedDeserializer extends StdDeserializer<Object> implements ContextualDeserializer {
        private static final long serialVersionUID = 1;
        protected final JsonDeserializer<?> _deser;
        protected final Method _factory;
        protected final Class<?> _inputType;

        public FactoryBasedDeserializer(Class<?> cls, AnnotatedMethod f, Class<?> inputType) {
            super((Class) cls);
            this._factory = f.getAnnotated();
            this._inputType = inputType;
            this._deser = null;
        }

        protected FactoryBasedDeserializer(FactoryBasedDeserializer base, JsonDeserializer<?> deser) {
            super(base._valueClass);
            this._inputType = base._inputType;
            this._factory = base._factory;
            this._deser = deser;
        }

        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            if (this._deser != null || this._inputType == String.class) {
                return this;
            }
            return new FactoryBasedDeserializer(this, ctxt.findContextualValueDeserializer(ctxt.constructType(this._inputType), property));
        }

        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Object deserialize;
            if (this._deser != null) {
                deserialize = this._deser.deserialize(p, ctxt);
            } else {
                JsonToken curr = p.getCurrentToken();
                if (curr == JsonToken.VALUE_STRING || curr == JsonToken.FIELD_NAME) {
                    deserialize = p.getText();
                } else {
                    deserialize = p.getValueAsString();
                }
            }
            try {
                return this._factory.invoke(this._valueClass, new Object[]{deserialize});
            } catch (Exception e) {
                Throwable t = ClassUtil.getRootCause(e);
                if (t instanceof IOException) {
                    throw ((IOException) t);
                }
                throw ctxt.instantiationException(this._valueClass, t);
            }
        }

        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            if (this._deser == null) {
                return deserialize(p, ctxt);
            }
            return typeDeserializer.deserializeTypedFromAny(p, ctxt);
        }
    }

    public EnumDeserializer(EnumResolver res) {
        super(res.getEnumClass());
        this._enumLookup = res.constructLookup();
        this._enumsByIndex = res.getRawEnums();
    }

    public static JsonDeserializer<?> deserializerForCreator(DeserializationConfig config, Class<?> enumClass, AnnotatedMethod factory) {
        Class<?> paramClass = factory.getRawParameterType(0);
        if (config.canOverrideAccessModifiers()) {
            ClassUtil.checkAndFixAccess(factory.getMember());
        }
        return new FactoryBasedDeserializer(enumClass, factory, paramClass);
    }

    public boolean isCachable() {
        return true;
    }

    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken curr = p.getCurrentToken();
        if (curr == JsonToken.VALUE_STRING || curr == JsonToken.FIELD_NAME) {
            String name = p.getText();
            Object result = this._enumLookup.find(name);
            if (result == null) {
                return _deserializeAltString(p, ctxt, name);
            }
            return result;
        } else if (curr != JsonToken.VALUE_NUMBER_INT) {
            return _deserializeOther(p, ctxt);
        } else {
            _checkFailOnNumber(ctxt);
            int index = p.getIntValue();
            if (index >= 0 && index <= this._enumsByIndex.length) {
                return this._enumsByIndex[index];
            }
            if (ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
                return null;
            }
            throw ctxt.weirdNumberException(Integer.valueOf(index), _enumClass(), "index value outside legal index range [0.." + (this._enumsByIndex.length - 1) + "]");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.Object _deserializeAltString(com.fasterxml.jackson.core.JsonParser r6, com.fasterxml.jackson.databind.DeserializationContext r7, java.lang.String r8) throws java.io.IOException {
        /*
        r5 = this;
        r2 = 0;
        r8 = r8.trim();
        r3 = r8.length();
        if (r3 != 0) goto L_0x0014;
    L_0x000b:
        r3 = com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
        r3 = r7.isEnabled(r3);
        if (r3 == 0) goto L_0x0035;
    L_0x0013:
        return r2;
    L_0x0014:
        r3 = 0;
        r0 = r8.charAt(r3);
        r3 = 48;
        if (r0 < r3) goto L_0x0035;
    L_0x001d:
        r3 = 57;
        if (r0 > r3) goto L_0x0035;
    L_0x0021:
        r1 = java.lang.Integer.parseInt(r8);	 Catch:{ NumberFormatException -> 0x0034 }
        r5._checkFailOnNumber(r7);	 Catch:{ NumberFormatException -> 0x0034 }
        if (r1 < 0) goto L_0x0035;
    L_0x002a:
        r3 = r5._enumsByIndex;	 Catch:{ NumberFormatException -> 0x0034 }
        r3 = r3.length;	 Catch:{ NumberFormatException -> 0x0034 }
        if (r1 > r3) goto L_0x0035;
    L_0x002f:
        r3 = r5._enumsByIndex;	 Catch:{ NumberFormatException -> 0x0034 }
        r2 = r3[r1];	 Catch:{ NumberFormatException -> 0x0034 }
        goto L_0x0013;
    L_0x0034:
        r3 = move-exception;
    L_0x0035:
        r3 = com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL;
        r3 = r7.isEnabled(r3);
        if (r3 != 0) goto L_0x0013;
    L_0x003d:
        r2 = r5._enumClass();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "value not one of declared Enum instance names: ";
        r3 = r3.append(r4);
        r4 = r5._enumLookup;
        r4 = r4.keys();
        r3 = r3.append(r4);
        r3 = r3.toString();
        r2 = r7.weirdStringException(r8, r2, r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.databind.deser.std.EnumDeserializer._deserializeAltString(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext, java.lang.String):java.lang.Object");
    }

    protected Object _deserializeOther(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken curr = p.getCurrentToken();
        if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS) && p.isExpectedStartArrayToken()) {
            p.nextToken();
            Object parsed = deserialize(p, ctxt);
            if (p.nextToken() == JsonToken.END_ARRAY) {
                return parsed;
            }
            throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single '" + _enumClass().getName() + "' value but there was more than a single value in the array");
        }
        throw ctxt.mappingException(_enumClass());
    }

    protected void _checkFailOnNumber(DeserializationContext ctxt) throws IOException {
        if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
            throw ctxt.mappingException("Not allowed to deserialize Enum value out of JSON number (disable DeserializationConfig.DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS to allow)");
        }
    }

    protected Class<?> _enumClass() {
        return handledType();
    }
}
