package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class AnnotatedWithParams extends AnnotatedMember {
    private static final long serialVersionUID = 1;
    protected final AnnotationMap[] _paramAnnotations;

    public abstract Object call() throws Exception;

    public abstract Object call(Object[] objArr) throws Exception;

    public abstract Object call1(Object obj) throws Exception;

    public abstract Type getGenericParameterType(int i);

    public abstract int getParameterCount();

    public abstract Class<?> getRawParameterType(int i);

    protected AnnotatedWithParams(AnnotatedClass ctxt, AnnotationMap annotations, AnnotationMap[] paramAnnotations) {
        super(ctxt, annotations);
        this._paramAnnotations = paramAnnotations;
    }

    public final void addOrOverrideParam(int paramIndex, Annotation a) {
        AnnotationMap old = this._paramAnnotations[paramIndex];
        if (old == null) {
            old = new AnnotationMap();
            this._paramAnnotations[paramIndex] = old;
        }
        old.add(a);
    }

    protected AnnotatedParameter replaceParameterAnnotations(int index, AnnotationMap ann) {
        this._paramAnnotations[index] = ann;
        return getParameter(index);
    }

    protected JavaType getType(TypeBindings bindings, TypeVariable<?>[] typeParams) {
        if (typeParams != null && typeParams.length > 0) {
            bindings = bindings.childInstance();
            for (TypeVariable<?> var : typeParams) {
                bindings._addPlaceholder(var.getName());
                Type lowerBound = var.getBounds()[0];
                bindings.addBinding(var.getName(), lowerBound == null ? TypeFactory.unknownType() : bindings.resolveType(lowerBound));
            }
        }
        return bindings.resolveType(getGenericType());
    }

    public final <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._annotations.get(acls);
    }

    public final AnnotationMap getParameterAnnotations(int index) {
        if (this._paramAnnotations == null || index < 0 || index >= this._paramAnnotations.length) {
            return null;
        }
        return this._paramAnnotations[index];
    }

    public final AnnotatedParameter getParameter(int index) {
        return new AnnotatedParameter(this, getGenericParameterType(index), getParameterAnnotations(index), index);
    }

    public final JavaType resolveParameterType(int index, TypeBindings bindings) {
        return bindings.resolveType(getGenericParameterType(index));
    }

    public final int getAnnotationCount() {
        return this._annotations.size();
    }
}
