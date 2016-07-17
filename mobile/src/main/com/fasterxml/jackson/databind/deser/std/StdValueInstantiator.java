package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import java.io.IOException;
import java.io.Serializable;

@JacksonStdImpl
public class StdValueInstantiator extends ValueInstantiator implements Serializable {
    private static final long serialVersionUID = 1;
    protected SettableBeanProperty[] _constructorArguments;
    protected AnnotatedWithParams _defaultCreator;
    protected SettableBeanProperty[] _delegateArguments;
    protected AnnotatedWithParams _delegateCreator;
    protected JavaType _delegateType;
    protected AnnotatedWithParams _fromBooleanCreator;
    protected AnnotatedWithParams _fromDoubleCreator;
    protected AnnotatedWithParams _fromIntCreator;
    protected AnnotatedWithParams _fromLongCreator;
    protected AnnotatedWithParams _fromStringCreator;
    protected AnnotatedParameter _incompleteParameter;
    protected final String _valueTypeDesc;
    protected AnnotatedWithParams _withArgsCreator;

    public StdValueInstantiator(DeserializationConfig config, Class<?> valueType) {
        this._valueTypeDesc = valueType == null ? "UNKNOWN TYPE" : valueType.getName();
    }

    public StdValueInstantiator(DeserializationConfig config, JavaType valueType) {
        this._valueTypeDesc = valueType == null ? "UNKNOWN TYPE" : valueType.toString();
    }

    protected StdValueInstantiator(StdValueInstantiator src) {
        this._valueTypeDesc = src._valueTypeDesc;
        this._defaultCreator = src._defaultCreator;
        this._constructorArguments = src._constructorArguments;
        this._withArgsCreator = src._withArgsCreator;
        this._delegateType = src._delegateType;
        this._delegateCreator = src._delegateCreator;
        this._delegateArguments = src._delegateArguments;
        this._fromStringCreator = src._fromStringCreator;
        this._fromIntCreator = src._fromIntCreator;
        this._fromLongCreator = src._fromLongCreator;
        this._fromDoubleCreator = src._fromDoubleCreator;
        this._fromBooleanCreator = src._fromBooleanCreator;
    }

    public void configureFromObjectSettings(AnnotatedWithParams defaultCreator, AnnotatedWithParams delegateCreator, JavaType delegateType, SettableBeanProperty[] delegateArgs, AnnotatedWithParams withArgsCreator, SettableBeanProperty[] constructorArgs) {
        this._defaultCreator = defaultCreator;
        this._delegateCreator = delegateCreator;
        this._delegateType = delegateType;
        this._delegateArguments = delegateArgs;
        this._withArgsCreator = withArgsCreator;
        this._constructorArguments = constructorArgs;
    }

    public void configureFromStringCreator(AnnotatedWithParams creator) {
        this._fromStringCreator = creator;
    }

    public void configureFromIntCreator(AnnotatedWithParams creator) {
        this._fromIntCreator = creator;
    }

    public void configureFromLongCreator(AnnotatedWithParams creator) {
        this._fromLongCreator = creator;
    }

    public void configureFromDoubleCreator(AnnotatedWithParams creator) {
        this._fromDoubleCreator = creator;
    }

    public void configureFromBooleanCreator(AnnotatedWithParams creator) {
        this._fromBooleanCreator = creator;
    }

    public void configureIncompleteParameter(AnnotatedParameter parameter) {
        this._incompleteParameter = parameter;
    }

    public String getValueTypeDesc() {
        return this._valueTypeDesc;
    }

    public boolean canCreateFromString() {
        return this._fromStringCreator != null;
    }

    public boolean canCreateFromInt() {
        return this._fromIntCreator != null;
    }

    public boolean canCreateFromLong() {
        return this._fromLongCreator != null;
    }

    public boolean canCreateFromDouble() {
        return this._fromDoubleCreator != null;
    }

    public boolean canCreateFromBoolean() {
        return this._fromBooleanCreator != null;
    }

    public boolean canCreateUsingDefault() {
        return this._defaultCreator != null;
    }

    public boolean canCreateUsingDelegate() {
        return this._delegateType != null;
    }

    public boolean canCreateFromObjectWith() {
        return this._withArgsCreator != null;
    }

    public JavaType getDelegateType(DeserializationConfig config) {
        return this._delegateType;
    }

    public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
        return this._constructorArguments;
    }

    public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
        if (this._defaultCreator == null) {
            throw new IllegalStateException("No default constructor for " + getValueTypeDesc());
        }
        try {
            return this._defaultCreator.call();
        } catch (ExceptionInInitializerError e) {
            throw wrapException(e);
        } catch (Exception e2) {
            throw wrapException(e2);
        }
    }

    public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
        if (this._withArgsCreator == null) {
            throw new IllegalStateException("No with-args constructor for " + getValueTypeDesc());
        }
        try {
            return this._withArgsCreator.call(args);
        } catch (ExceptionInInitializerError e) {
            throw wrapException(e);
        } catch (Exception e2) {
            throw wrapException(e2);
        }
    }

    public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
        if (this._delegateCreator == null) {
            throw new IllegalStateException("No delegate constructor for " + getValueTypeDesc());
        }
        try {
            if (this._delegateArguments == null) {
                return this._delegateCreator.call1(delegate);
            }
            int len = this._delegateArguments.length;
            Object[] args = new Object[len];
            for (int i = 0; i < len; i++) {
                SettableBeanProperty prop = this._delegateArguments[i];
                if (prop == null) {
                    args[i] = delegate;
                } else {
                    args[i] = ctxt.findInjectableValue(prop.getInjectableValueId(), prop, null);
                }
            }
            return this._delegateCreator.call(args);
        } catch (ExceptionInInitializerError e) {
            throw wrapException(e);
        } catch (Exception e2) {
            throw wrapException(e2);
        }
    }

    public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
        if (this._fromStringCreator == null) {
            return _createFromStringFallbacks(ctxt, value);
        }
        try {
            return this._fromStringCreator.call1(value);
        } catch (Exception e) {
            throw wrapException(e);
        } catch (ExceptionInInitializerError e2) {
            throw wrapException(e2);
        }
    }

    public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
        try {
            if (this._fromIntCreator != null) {
                return this._fromIntCreator.call1(Integer.valueOf(value));
            }
            if (this._fromLongCreator != null) {
                return this._fromLongCreator.call1(Long.valueOf((long) value));
            }
            throw ctxt.mappingException("Can not instantiate value of type %s from Integral number (%s); no single-int-arg constructor/factory method", getValueTypeDesc(), Integer.valueOf(value));
        } catch (Exception e) {
            throw wrapException(e);
        } catch (ExceptionInInitializerError e2) {
            throw wrapException(e2);
        }
    }

    public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
        try {
            if (this._fromLongCreator != null) {
                return this._fromLongCreator.call1(Long.valueOf(value));
            }
            throw ctxt.mappingException("Can not instantiate value of type %s from Long integral number (%s); no single-long-arg constructor/factory method", getValueTypeDesc(), Long.valueOf(value));
        } catch (Exception e) {
            throw wrapException(e);
        } catch (ExceptionInInitializerError e2) {
            throw wrapException(e2);
        }
    }

    public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
        try {
            if (this._fromDoubleCreator != null) {
                return this._fromDoubleCreator.call1(Double.valueOf(value));
            }
            throw ctxt.mappingException("Can not instantiate value of type %s from Floating-point number (%s); no one-double/Double-arg constructor/factory method", getValueTypeDesc(), Double.valueOf(value));
        } catch (Exception e) {
            throw wrapException(e);
        } catch (ExceptionInInitializerError e2) {
            throw wrapException(e2);
        }
    }

    public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
        try {
            if (this._fromBooleanCreator != null) {
                return this._fromBooleanCreator.call1(Boolean.valueOf(value));
            }
            throw ctxt.mappingException("Can not instantiate value of type %s from Boolean value (%s); no single-boolean/Boolean-arg constructor/factory method", getValueTypeDesc(), Boolean.valueOf(value));
        } catch (Exception e) {
            throw wrapException(e);
        } catch (ExceptionInInitializerError e2) {
            throw wrapException(e2);
        }
    }

    public AnnotatedWithParams getDelegateCreator() {
        return this._delegateCreator;
    }

    public AnnotatedWithParams getDefaultCreator() {
        return this._defaultCreator;
    }

    public AnnotatedWithParams getWithArgsCreator() {
        return this._withArgsCreator;
    }

    public AnnotatedParameter getIncompleteParameter() {
        return this._incompleteParameter;
    }

    protected JsonMappingException wrapException(Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof JsonMappingException) {
            return (JsonMappingException) t;
        }
        return new JsonMappingException("Instantiation of " + getValueTypeDesc() + " value failed: " + t.getMessage(), t);
    }
}
