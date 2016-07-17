package com.fasterxml.jackson.databind.module;

import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class SimpleAbstractTypeResolver extends AbstractTypeResolver implements Serializable {
    private static final long serialVersionUID = 8635483102371490919L;
    protected final HashMap<ClassKey, Class<?>> _mappings;

    public SimpleAbstractTypeResolver() {
        this._mappings = new HashMap();
    }

    public <T> SimpleAbstractTypeResolver addMapping(Class<T> superType, Class<? extends T> subType) {
        if (superType == subType) {
            throw new IllegalArgumentException("Can not add mapping from class to itself");
        } else if (!superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException("Can not add mapping from class " + superType.getName() + " to " + subType.getName() + ", as latter is not a subtype of former");
        } else if (Modifier.isAbstract(superType.getModifiers())) {
            this._mappings.put(new ClassKey(superType), subType);
            return this;
        } else {
            throw new IllegalArgumentException("Can not add mapping from class " + superType.getName() + " since it is not abstract");
        }
    }

    public JavaType findTypeMapping(DeserializationConfig config, JavaType type) {
        Class<?> dst = (Class) this._mappings.get(new ClassKey(type.getRawClass()));
        if (dst == null) {
            return null;
        }
        return config.getTypeFactory().constructSpecializedType(type, dst);
    }

    public JavaType resolveAbstractType(DeserializationConfig config, JavaType type) {
        return null;
    }
}
