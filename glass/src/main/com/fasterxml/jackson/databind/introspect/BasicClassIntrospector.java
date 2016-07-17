package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.LRUMap;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class BasicClassIntrospector extends ClassIntrospector implements Serializable {
    protected static final BasicBeanDescription BOOLEAN_DESC;
    protected static final BasicBeanDescription INT_DESC;
    protected static final BasicBeanDescription LONG_DESC;
    protected static final BasicBeanDescription STRING_DESC;
    @Deprecated
    public static final BasicClassIntrospector instance;
    private static final long serialVersionUID = 1;
    protected final LRUMap<JavaType, BasicBeanDescription> _cachedFCA;

    static {
        STRING_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(String.class), AnnotatedClass.constructWithoutSuperTypes(String.class, null, null));
        BOOLEAN_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Boolean.TYPE), AnnotatedClass.constructWithoutSuperTypes(Boolean.TYPE, null, null));
        INT_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Integer.TYPE), AnnotatedClass.constructWithoutSuperTypes(Integer.TYPE, null, null));
        LONG_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Long.TYPE), AnnotatedClass.constructWithoutSuperTypes(Long.TYPE, null, null));
        instance = new BasicClassIntrospector();
    }

    public BasicClassIntrospector() {
        this._cachedFCA = new LRUMap(16, 64);
    }

    public BasicBeanDescription forSerialization(SerializationConfig cfg, JavaType type, MixInResolver r) {
        BasicBeanDescription desc = _findStdTypeDesc(type);
        if (desc == null) {
            desc = _findStdJdkCollectionDesc(cfg, type, r);
            if (desc == null) {
                desc = BasicBeanDescription.forSerialization(collectProperties(cfg, type, r, true, "set"));
            }
            this._cachedFCA.putIfAbsent(type, desc);
        }
        return desc;
    }

    public BasicBeanDescription forDeserialization(DeserializationConfig cfg, JavaType type, MixInResolver r) {
        BasicBeanDescription desc = _findStdTypeDesc(type);
        if (desc == null) {
            desc = _findStdJdkCollectionDesc(cfg, type, r);
            if (desc == null) {
                desc = BasicBeanDescription.forDeserialization(collectProperties(cfg, type, r, false, "set"));
            }
            this._cachedFCA.putIfAbsent(type, desc);
        }
        return desc;
    }

    public BasicBeanDescription forDeserializationWithBuilder(DeserializationConfig cfg, JavaType type, MixInResolver r) {
        BasicBeanDescription desc = BasicBeanDescription.forDeserialization(collectPropertiesWithBuilder(cfg, type, r, false));
        this._cachedFCA.putIfAbsent(type, desc);
        return desc;
    }

    public BasicBeanDescription forCreation(DeserializationConfig cfg, JavaType type, MixInResolver r) {
        BasicBeanDescription desc = _findStdTypeDesc(type);
        if (desc != null) {
            return desc;
        }
        desc = _findStdJdkCollectionDesc(cfg, type, r);
        if (desc != null) {
            return desc;
        }
        return BasicBeanDescription.forDeserialization(collectProperties(cfg, type, r, false, "set"));
    }

    public BasicBeanDescription forClassAnnotations(MapperConfig<?> cfg, JavaType type, MixInResolver r) {
        BasicBeanDescription desc = _findStdTypeDesc(type);
        if (desc != null) {
            return desc;
        }
        desc = (BasicBeanDescription) this._cachedFCA.get(type);
        if (desc != null) {
            return desc;
        }
        desc = BasicBeanDescription.forOtherUse(cfg, type, AnnotatedClass.construct(type.getRawClass(), cfg.isAnnotationProcessingEnabled() ? cfg.getAnnotationIntrospector() : null, r));
        this._cachedFCA.put(type, desc);
        return desc;
    }

    public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> cfg, JavaType type, MixInResolver r) {
        BasicBeanDescription desc = _findStdTypeDesc(type);
        if (desc != null) {
            return desc;
        }
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        Class rawClass = type.getRawClass();
        if (!useAnnotations) {
            ai = null;
        }
        return BasicBeanDescription.forOtherUse(cfg, type, AnnotatedClass.constructWithoutSuperTypes(rawClass, ai, r));
    }

    protected POJOPropertiesCollector collectProperties(MapperConfig<?> config, JavaType type, MixInResolver r, boolean forSerialization, String mutatorPrefix) {
        return constructPropertyCollector(config, AnnotatedClass.construct(type.getRawClass(), config.isAnnotationProcessingEnabled() ? config.getAnnotationIntrospector() : null, r), type, forSerialization, mutatorPrefix);
    }

    protected POJOPropertiesCollector collectPropertiesWithBuilder(MapperConfig<?> config, JavaType type, MixInResolver r, boolean forSerialization) {
        AnnotationIntrospector ai;
        if (config.isAnnotationProcessingEnabled()) {
            ai = config.getAnnotationIntrospector();
        } else {
            ai = null;
        }
        AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), ai, r);
        Value builderConfig = ai == null ? null : ai.findPOJOBuilderConfig(ac);
        return constructPropertyCollector(config, ac, type, forSerialization, builderConfig == null ? AssociateOnce.WITH : builderConfig.withPrefix);
    }

    protected POJOPropertiesCollector constructPropertyCollector(MapperConfig<?> config, AnnotatedClass ac, JavaType type, boolean forSerialization, String mutatorPrefix) {
        return new POJOPropertiesCollector(config, forSerialization, type, ac, mutatorPrefix);
    }

    protected BasicBeanDescription _findStdTypeDesc(JavaType type) {
        Class<?> cls = type.getRawClass();
        if (cls.isPrimitive()) {
            if (cls == Boolean.TYPE) {
                return BOOLEAN_DESC;
            }
            if (cls == Integer.TYPE) {
                return INT_DESC;
            }
            if (cls == Long.TYPE) {
                return LONG_DESC;
            }
        } else if (cls == String.class) {
            return STRING_DESC;
        }
        return null;
    }

    protected boolean _isStdJDKCollection(JavaType type) {
        if (!type.isContainerType() || type.isArrayType()) {
            return false;
        }
        Class<?> raw = type.getRawClass();
        Package pkg = raw.getPackage();
        if (pkg == null) {
            return false;
        }
        String pkgName = pkg.getName();
        if (!pkgName.startsWith("java.lang") && !pkgName.startsWith("java.util")) {
            return false;
        }
        if (Collection.class.isAssignableFrom(raw) || Map.class.isAssignableFrom(raw)) {
            return true;
        }
        return false;
    }

    protected BasicBeanDescription _findStdJdkCollectionDesc(MapperConfig<?> cfg, JavaType type, MixInResolver r) {
        AnnotationIntrospector annotationIntrospector = null;
        if (!_isStdJDKCollection(type)) {
            return null;
        }
        Class rawClass = type.getRawClass();
        if (cfg.isAnnotationProcessingEnabled()) {
            annotationIntrospector = cfg.getAnnotationIntrospector();
        }
        return BasicBeanDescription.forOtherUse(cfg, type, AnnotatedClass.construct(rawClass, annotationIntrospector, r));
    }
}
