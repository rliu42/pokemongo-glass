package com.fasterxml.jackson.databind.introspect;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

public class VirtualAnnotatedMember extends AnnotatedMember implements Serializable {
    private static final long serialVersionUID = 1;
    protected final Class<?> _declaringClass;
    protected final String _name;
    protected final Class<?> _rawType;

    public VirtualAnnotatedMember(AnnotatedClass contextClass, Class<?> declaringClass, String name, Class<?> rawType) {
        super(contextClass, null);
        this._declaringClass = declaringClass;
        this._rawType = rawType;
        this._name = name;
    }

    public Annotated withAnnotations(AnnotationMap fallback) {
        return this;
    }

    public Field getAnnotated() {
        return null;
    }

    public int getModifiers() {
        return 0;
    }

    public String getName() {
        return this._name;
    }

    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        return null;
    }

    public Type getGenericType() {
        return this._rawType;
    }

    public Class<?> getRawType() {
        return this._rawType;
    }

    public Class<?> getDeclaringClass() {
        return this._declaringClass;
    }

    public Member getMember() {
        return null;
    }

    public void setValue(Object pojo, Object value) throws IllegalArgumentException {
        throw new IllegalArgumentException("Can not set virtual property '" + this._name + "'");
    }

    public Object getValue(Object pojo) throws IllegalArgumentException {
        throw new IllegalArgumentException("Can not get virtual property '" + this._name + "'");
    }

    public String getFullName() {
        return getDeclaringClass().getName() + "#" + getName();
    }

    public int getAnnotationCount() {
        return 0;
    }

    public int hashCode() {
        return this._name.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        VirtualAnnotatedMember other = (VirtualAnnotatedMember) o;
        if (other._declaringClass == this._declaringClass && other._name.equals(this._name)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "[field " + getFullName() + "]";
    }
}
