package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.util.EnumMap;
import java.util.EnumSet;

public class ClassNameIdResolver extends TypeIdResolverBase {
    public ClassNameIdResolver(JavaType baseType, TypeFactory typeFactory) {
        super(baseType, typeFactory);
    }

    public Id getMechanism() {
        return Id.CLASS;
    }

    public void registerSubtype(Class<?> cls, String name) {
    }

    public String idFromValue(Object value) {
        return _idFrom(value, value.getClass());
    }

    public String idFromValueAndType(Object value, Class<?> type) {
        return _idFrom(value, type);
    }

    @Deprecated
    public JavaType typeFromId(String id) {
        return _typeFromId(id, this._typeFactory);
    }

    public JavaType typeFromId(DatabindContext context, String id) {
        return _typeFromId(id, context.getTypeFactory());
    }

    protected JavaType _typeFromId(String id, TypeFactory typeFactory) {
        if (id.indexOf(60) > 0) {
            return typeFactory.constructFromCanonical(id);
        }
        try {
            return typeFactory.constructSpecializedType(this._baseType, typeFactory.findClass(id));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): no such class found");
        } catch (Exception e2) {
            throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): " + e2.getMessage(), e2);
        }
    }

    protected final String _idFrom(Object value, Class<?> cls) {
        if (Enum.class.isAssignableFrom(cls) && !cls.isEnum()) {
            cls = cls.getSuperclass();
        }
        String str = cls.getName();
        if (str.startsWith("java.util")) {
            if (value instanceof EnumSet) {
                return TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, ClassUtil.findEnumType((EnumSet) value)).toCanonical();
            } else if (value instanceof EnumMap) {
                return TypeFactory.defaultInstance().constructMapType(EnumMap.class, ClassUtil.findEnumType((EnumMap) value), Object.class).toCanonical();
            } else {
                String end = str.substring(9);
                if ((end.startsWith(".Arrays$") || end.startsWith(".Collections$")) && str.indexOf("List") >= 0) {
                    return "java.util.ArrayList";
                }
                return str;
            }
        } else if (str.indexOf(36) < 0 || ClassUtil.getOuterClass(cls) == null || ClassUtil.getOuterClass(this._baseType.getRawClass()) != null) {
            return str;
        } else {
            return this._baseType.getRawClass().getName();
        }
    }

    public String getDescForKnownTypeIds() {
        return "class name used as type id";
    }
}
