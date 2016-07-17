package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public final class AnnotatedMethod extends AnnotatedWithParams implements Serializable {
    private static final long serialVersionUID = 1;
    protected final transient Method _method;
    protected Class<?>[] _paramClasses;
    protected Serialization _serialization;

    private static final class Serialization implements Serializable {
        private static final long serialVersionUID = 1;
        protected Class<?>[] args;
        protected Class<?> clazz;
        protected String name;

        public Serialization(Method setter) {
            this.clazz = setter.getDeclaringClass();
            this.name = setter.getName();
            this.args = setter.getParameterTypes();
        }
    }

    public AnnotatedMethod(AnnotatedClass ctxt, Method method, AnnotationMap classAnn, AnnotationMap[] paramAnnotations) {
        super(ctxt, classAnn, paramAnnotations);
        if (method == null) {
            throw new IllegalArgumentException("Can not construct AnnotatedMethod with null Method");
        }
        this._method = method;
    }

    protected AnnotatedMethod(Serialization ser) {
        super(null, null, null);
        this._method = null;
        this._serialization = ser;
    }

    public AnnotatedMethod withMethod(Method m) {
        return new AnnotatedMethod(this._context, m, this._annotations, this._paramAnnotations);
    }

    public AnnotatedMethod withAnnotations(AnnotationMap ann) {
        return new AnnotatedMethod(this._context, this._method, ann, this._paramAnnotations);
    }

    public Method getAnnotated() {
        return this._method;
    }

    public int getModifiers() {
        return this._method.getModifiers();
    }

    public String getName() {
        return this._method.getName();
    }

    public Type getGenericType() {
        return this._method.getGenericReturnType();
    }

    public Class<?> getRawType() {
        return this._method.getReturnType();
    }

    public JavaType getType(TypeBindings bindings) {
        return getType(bindings, this._method.getTypeParameters());
    }

    public final Object call() throws Exception {
        return this._method.invoke(null, new Object[0]);
    }

    public final Object call(Object[] args) throws Exception {
        return this._method.invoke(null, args);
    }

    public final Object call1(Object arg) throws Exception {
        return this._method.invoke(null, new Object[]{arg});
    }

    public Class<?> getDeclaringClass() {
        return this._method.getDeclaringClass();
    }

    public Method getMember() {
        return this._method;
    }

    public void setValue(Object pojo, Object value) throws IllegalArgumentException {
        try {
            this._method.invoke(pojo, new Object[]{value});
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to setValue() with method " + getFullName() + ": " + e.getMessage(), e);
        } catch (InvocationTargetException e2) {
            throw new IllegalArgumentException("Failed to setValue() with method " + getFullName() + ": " + e2.getMessage(), e2);
        }
    }

    public Object getValue(Object pojo) throws IllegalArgumentException {
        try {
            return this._method.invoke(pojo, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to getValue() with method " + getFullName() + ": " + e.getMessage(), e);
        } catch (InvocationTargetException e2) {
            throw new IllegalArgumentException("Failed to getValue() with method " + getFullName() + ": " + e2.getMessage(), e2);
        }
    }

    public int getParameterCount() {
        return getRawParameterTypes().length;
    }

    public String getFullName() {
        return getDeclaringClass().getName() + "#" + getName() + "(" + getParameterCount() + " params)";
    }

    public Class<?>[] getRawParameterTypes() {
        if (this._paramClasses == null) {
            this._paramClasses = this._method.getParameterTypes();
        }
        return this._paramClasses;
    }

    public Type[] getGenericParameterTypes() {
        return this._method.getGenericParameterTypes();
    }

    public Class<?> getRawParameterType(int index) {
        Class<?>[] types = getRawParameterTypes();
        return index >= types.length ? null : types[index];
    }

    public Type getGenericParameterType(int index) {
        Type[] types = this._method.getGenericParameterTypes();
        return index >= types.length ? null : types[index];
    }

    public Class<?> getRawReturnType() {
        return this._method.getReturnType();
    }

    public Type getGenericReturnType() {
        return this._method.getGenericReturnType();
    }

    public boolean hasReturnType() {
        Class<?> rt = getRawReturnType();
        return (rt == Void.TYPE || rt == Void.class) ? false : true;
    }

    public String toString() {
        return "[method " + getFullName() + "]";
    }

    public int hashCode() {
        return this._method.getName().hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        if (((AnnotatedMethod) o)._method != this._method) {
            return false;
        }
        return true;
    }

    Object writeReplace() {
        return new AnnotatedMethod(new Serialization(this._method));
    }

    Object readResolve() {
        Class<?> clazz = this._serialization.clazz;
        try {
            Method m = clazz.getDeclaredMethod(this._serialization.name, this._serialization.args);
            if (!m.isAccessible()) {
                ClassUtil.checkAndFixAccess(m);
            }
            return new AnnotatedMethod(null, m, null, null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not find method '" + this._serialization.name + "' from Class '" + clazz.getName());
        }
    }
}
