package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.None;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

public abstract class DatabindContext {
    public abstract Class<?> getActiveView();

    public abstract AnnotationIntrospector getAnnotationIntrospector();

    public abstract Object getAttribute(Object obj);

    public abstract MapperConfig<?> getConfig();

    public abstract Locale getLocale();

    public abstract TimeZone getTimeZone();

    public abstract TypeFactory getTypeFactory();

    public abstract DatabindContext setAttribute(Object obj, Object obj2);

    public final boolean isEnabled(MapperFeature feature) {
        return getConfig().isEnabled(feature);
    }

    public final boolean canOverrideAccessModifiers() {
        return getConfig().canOverrideAccessModifiers();
    }

    public JavaType constructType(Type type) {
        return getTypeFactory().constructType(type);
    }

    public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
        return baseType.getRawClass() == subclass ? baseType : getConfig().constructSpecializedType(baseType, subclass);
    }

    public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo) throws JsonMappingException {
        Class<?> implClass = objectIdInfo.getGeneratorType();
        MapperConfig<?> config = getConfig();
        HandlerInstantiator hi = config.getHandlerInstantiator();
        ObjectIdGenerator<?> gen = hi == null ? null : hi.objectIdGeneratorInstance(config, annotated, implClass);
        if (gen == null) {
            gen = (ObjectIdGenerator) ClassUtil.createInstance(implClass, config.canOverrideAccessModifiers());
        }
        return gen.forScope(objectIdInfo.getScope());
    }

    public ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo) {
        Class<? extends ObjectIdResolver> implClass = objectIdInfo.getResolverType();
        MapperConfig<?> config = getConfig();
        HandlerInstantiator hi = config.getHandlerInstantiator();
        ObjectIdResolver resolver = hi == null ? null : hi.resolverIdGeneratorInstance(config, annotated, implClass);
        if (resolver == null) {
            return (ObjectIdResolver) ClassUtil.createInstance(implClass, config.canOverrideAccessModifiers());
        }
        return resolver;
    }

    public Converter<Object, Object> converterInstance(Annotated annotated, Object converterDef) throws JsonMappingException {
        Converter<?, ?> conv = null;
        if (converterDef == null) {
            return null;
        }
        if (converterDef instanceof Converter) {
            return (Converter) converterDef;
        }
        if (converterDef instanceof Class) {
            Class<?> converterClass = (Class) converterDef;
            if (converterClass == None.class || ClassUtil.isBogusClass(converterClass)) {
                return null;
            }
            if (Converter.class.isAssignableFrom(converterClass)) {
                MapperConfig<?> config = getConfig();
                HandlerInstantiator hi = config.getHandlerInstantiator();
                if (hi != null) {
                    conv = hi.converterInstance(config, annotated, converterClass);
                }
                if (conv == null) {
                    conv = (Converter) ClassUtil.createInstance(converterClass, config.canOverrideAccessModifiers());
                }
                return conv;
            }
            throw new IllegalStateException("AnnotationIntrospector returned Class " + converterClass.getName() + "; expected Class<Converter>");
        }
        throw new IllegalStateException("AnnotationIntrospector returned Converter definition of type " + converterDef.getClass().getName() + "; expected type Converter or Class<Converter> instead");
    }
}
