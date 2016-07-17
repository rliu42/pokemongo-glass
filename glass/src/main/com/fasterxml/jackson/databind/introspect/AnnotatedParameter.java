package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.type.TypeFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import spacemadness.com.lunarconsole.BuildConfig;

public final class AnnotatedParameter extends AnnotatedMember {
    private static final long serialVersionUID = 1;
    protected final int _index;
    protected final AnnotatedWithParams _owner;
    protected final Type _type;

    public AnnotatedParameter(AnnotatedWithParams owner, Type type, AnnotationMap annotations, int index) {
        super(owner == null ? null : owner.getContextClass(), annotations);
        this._owner = owner;
        this._type = type;
        this._index = index;
    }

    public AnnotatedParameter withAnnotations(AnnotationMap ann) {
        return ann == this._annotations ? this : this._owner.replaceParameterAnnotations(this._index, ann);
    }

    public AnnotatedElement getAnnotated() {
        return null;
    }

    public int getModifiers() {
        return this._owner.getModifiers();
    }

    public String getName() {
        return BuildConfig.FLAVOR;
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._annotations == null ? null : this._annotations.get(acls);
    }

    public Type getGenericType() {
        return this._type;
    }

    public Class<?> getRawType() {
        if (this._type instanceof Class) {
            return (Class) this._type;
        }
        return TypeFactory.defaultInstance().constructType(this._type).getRawClass();
    }

    public Class<?> getDeclaringClass() {
        return this._owner.getDeclaringClass();
    }

    public Member getMember() {
        return this._owner.getMember();
    }

    public void setValue(Object pojo, Object value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot call setValue() on constructor parameter of " + getDeclaringClass().getName());
    }

    public Object getValue(Object pojo) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot call getValue() on constructor parameter of " + getDeclaringClass().getName());
    }

    public Type getParameterType() {
        return this._type;
    }

    public AnnotatedWithParams getOwner() {
        return this._owner;
    }

    public int getIndex() {
        return this._index;
    }

    public int hashCode() {
        return this._owner.hashCode() + this._index;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        AnnotatedParameter other = (AnnotatedParameter) o;
        if (other._owner.equals(this._owner) && other._index == this._index) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "[parameter #" + getIndex() + ", annotations: " + this._annotations + "]";
    }
}
