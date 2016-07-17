package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty.Std;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
import com.fasterxml.jackson.databind.deser.impl.NoClassDefFoundDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.deser.std.ThrowableDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BeanDeserializerFactory extends BasicDeserializerFactory implements Serializable {
    private static final Class<?>[] INIT_CAUSE_PARAMS;
    private static final Class<?>[] NO_VIEWS;
    public static final BeanDeserializerFactory instance;
    private static final long serialVersionUID = 1;

    static {
        INIT_CAUSE_PARAMS = new Class[]{Throwable.class};
        NO_VIEWS = new Class[0];
        instance = new BeanDeserializerFactory(new DeserializerFactoryConfig());
    }

    public BeanDeserializerFactory(DeserializerFactoryConfig config) {
        super(config);
    }

    public DeserializerFactory withConfig(DeserializerFactoryConfig config) {
        if (this._factoryConfig == config) {
            return this;
        }
        if (getClass() != BeanDeserializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
        }
        this(config);
        return this;
    }

    public JsonDeserializer<Object> createBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        DeserializationConfig config = ctxt.getConfig();
        JsonDeserializer<Object> custom = _findCustomBeanDeserializer(type, config, beanDesc);
        if (custom != null) {
            return custom;
        }
        if (type.isThrowable()) {
            return buildThrowableDeserializer(ctxt, type, beanDesc);
        }
        if (type.isAbstract()) {
            JavaType concreteType = materializeAbstractType(ctxt, type, beanDesc);
            if (concreteType != null) {
                return buildBeanDeserializer(ctxt, concreteType, config.introspect(concreteType));
            }
        }
        JsonDeserializer<Object> deser = findStdDeserializer(ctxt, type, beanDesc);
        if (deser != null) {
            return deser;
        }
        if (isPotentialBeanType(type.getRawClass())) {
            return buildBeanDeserializer(ctxt, type, beanDesc);
        }
        return null;
    }

    public JsonDeserializer<Object> createBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType, BeanDescription beanDesc, Class<?> builderClass) throws JsonMappingException {
        return buildBuilderBasedDeserializer(ctxt, valueType, ctxt.getConfig().introspectForBuilder(ctxt.constructType(builderClass)));
    }

    protected JsonDeserializer<?> findStdDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        JsonDeserializer<?> deser = findDefaultDeserializer(ctxt, type, beanDesc);
        if (deser != null && this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
                deser = mod.modifyDeserializer(ctxt.getConfig(), beanDesc, deser);
            }
        }
        return deser;
    }

    protected JavaType materializeAbstractType(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        JavaType abstractType = beanDesc.getType();
        for (AbstractTypeResolver r : this._factoryConfig.abstractTypeResolvers()) {
            JavaType concrete = r.resolveAbstractType(ctxt.getConfig(), abstractType);
            if (concrete != null) {
                return concrete;
            }
        }
        return null;
    }

    public JsonDeserializer<Object> buildBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        try {
            JsonDeserializer<Object> deserializer;
            ValueInstantiator valueInstantiator = findValueInstantiator(ctxt, beanDesc);
            BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, beanDesc);
            builder.setValueInstantiator(valueInstantiator);
            addBeanProps(ctxt, beanDesc, builder);
            addObjectIdReader(ctxt, beanDesc, builder);
            addReferenceProperties(ctxt, beanDesc, builder);
            addInjectables(ctxt, beanDesc, builder);
            DeserializationConfig config = ctxt.getConfig();
            if (this._factoryConfig.hasDeserializerModifiers()) {
                for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
                    builder = mod.updateBuilder(config, beanDesc, builder);
                }
            }
            if (!type.isAbstract() || valueInstantiator.canInstantiate()) {
                deserializer = builder.build();
            } else {
                deserializer = builder.buildAbstract();
            }
            if (!this._factoryConfig.hasDeserializerModifiers()) {
                return deserializer;
            }
            for (BeanDeserializerModifier mod2 : this._factoryConfig.deserializerModifiers()) {
                deserializer = mod2.modifyDeserializer(config, beanDesc, deserializer);
            }
            return deserializer;
        } catch (NoClassDefFoundError error) {
            return new NoClassDefFoundDeserializer(error);
        }
    }

    protected JsonDeserializer<Object> buildBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType, BeanDescription builderDesc) throws JsonMappingException {
        ValueInstantiator valueInstantiator = findValueInstantiator(ctxt, builderDesc);
        DeserializationConfig config = ctxt.getConfig();
        BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, builderDesc);
        builder.setValueInstantiator(valueInstantiator);
        addBeanProps(ctxt, builderDesc, builder);
        addObjectIdReader(ctxt, builderDesc, builder);
        addReferenceProperties(ctxt, builderDesc, builder);
        addInjectables(ctxt, builderDesc, builder);
        Value builderConfig = builderDesc.findPOJOBuilderConfig();
        String buildMethodName = builderConfig == null ? "build" : builderConfig.buildMethodName;
        AnnotatedMethod buildMethod = builderDesc.findMethod(buildMethodName, null);
        if (buildMethod != null && config.canOverrideAccessModifiers()) {
            ClassUtil.checkAndFixAccess(buildMethod.getMember());
        }
        builder.setPOJOBuilder(buildMethod, builderConfig);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
                builder = mod.updateBuilder(config, builderDesc, builder);
            }
        }
        JsonDeserializer<?> deserializer = builder.buildBuilderBased(valueType, buildMethodName);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod2 : this._factoryConfig.deserializerModifiers()) {
                deserializer = mod2.modifyDeserializer(config, builderDesc, deserializer);
            }
        }
        return deserializer;
    }

    protected void addObjectIdReader(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        ObjectIdInfo objectIdInfo = beanDesc.getObjectIdInfo();
        if (objectIdInfo != null) {
            SettableBeanProperty idProp;
            JavaType idType;
            ObjectIdGenerator<?> gen;
            Class<?> implClass = objectIdInfo.getGeneratorType();
            ObjectIdResolver resolver = ctxt.objectIdResolverInstance(beanDesc.getClassInfo(), objectIdInfo);
            if (implClass == PropertyGenerator.class) {
                PropertyName propName = objectIdInfo.getPropertyName();
                idProp = builder.findProperty(propName);
                if (idProp == null) {
                    throw new IllegalArgumentException("Invalid Object Id definition for " + beanDesc.getBeanClass().getName() + ": can not find property with name '" + propName + "'");
                }
                idType = idProp.getType();
                gen = new PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
            } else {
                idType = ctxt.getTypeFactory().findTypeParameters(ctxt.constructType(implClass), ObjectIdGenerator.class)[0];
                idProp = null;
                gen = ctxt.objectIdGeneratorInstance(beanDesc.getClassInfo(), objectIdInfo);
            }
            builder.setObjectIdReader(ObjectIdReader.construct(idType, objectIdInfo.getPropertyName(), gen, ctxt.findRootValueDeserializer(idType), idProp, resolver));
        }
    }

    public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        DeserializationConfig config = ctxt.getConfig();
        BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, beanDesc);
        builder.setValueInstantiator(findValueInstantiator(ctxt, beanDesc));
        addBeanProps(ctxt, beanDesc, builder);
        AnnotatedMember am = beanDesc.findMethod("initCause", INIT_CAUSE_PARAMS);
        if (am != null) {
            SettableBeanProperty prop = constructSettableProperty(ctxt, beanDesc, SimpleBeanPropertyDefinition.construct(ctxt.getConfig(), am, new PropertyName("cause")), am.getGenericParameterType(0));
            if (prop != null) {
                builder.addOrReplaceProperty(prop, true);
            }
        }
        builder.addIgnorable("localizedMessage");
        builder.addIgnorable("suppressed");
        builder.addIgnorable(Keys.MESSAGE);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
                builder = mod.updateBuilder(config, beanDesc, builder);
            }
        }
        JsonDeserializer<?> build = builder.build();
        if (build instanceof BeanDeserializer) {
            build = new ThrowableDeserializer((BeanDeserializer) build);
        }
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod2 : this._factoryConfig.deserializerModifiers()) {
                build = mod2.modifyDeserializer(config, beanDesc, build);
            }
        }
        return build;
    }

    protected BeanDeserializerBuilder constructBeanDeserializerBuilder(DeserializationContext ctxt, BeanDescription beanDesc) {
        return new BeanDeserializerBuilder(beanDesc, ctxt.getConfig());
    }

    protected void addBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        boolean useGettersAsSetters;
        List<BeanPropertyDefinition> propDefs;
        SettableBeanProperty prop;
        Class<?>[] views;
        SettableBeanProperty[] creatorProps = builder.getValueInstantiator().getFromObjectArguments(ctxt.getConfig());
        boolean isConcrete = !beanDesc.getType().isAbstract();
        AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
        Boolean B = intr.findIgnoreUnknownProperties(beanDesc.getClassInfo());
        if (B != null) {
            builder.setIgnoreUnknownProperties(B.booleanValue());
        }
        Set<String> ignored = ArrayBuilders.arrayToSet(intr.findPropertiesToIgnore(beanDesc.getClassInfo(), false));
        for (String addIgnorable : ignored) {
            builder.addIgnorable(addIgnorable);
        }
        AnnotatedMethod anySetter = beanDesc.findAnySetter();
        if (anySetter != null) {
            builder.setAnySetter(constructAnySetter(ctxt, beanDesc, anySetter));
        }
        if (anySetter == null) {
            Collection<String> ignored2 = beanDesc.getIgnoredPropertyNames();
            if (ignored2 != null) {
                for (String addIgnorable2 : ignored2) {
                    builder.addIgnorable(addIgnorable2);
                }
            }
        }
        if (ctxt.isEnabled(MapperFeature.USE_GETTERS_AS_SETTERS)) {
            if (ctxt.isEnabled(MapperFeature.AUTO_DETECT_GETTERS)) {
                useGettersAsSetters = true;
                propDefs = filterBeanProps(ctxt, beanDesc, builder, beanDesc.findProperties(), ignored);
                if (this._factoryConfig.hasDeserializerModifiers()) {
                    for (BeanDeserializerModifier updateProperties : this._factoryConfig.deserializerModifiers()) {
                        propDefs = updateProperties.updateProperties(ctxt.getConfig(), beanDesc, propDefs);
                    }
                }
                for (BeanPropertyDefinition propDef : propDefs) {
                    prop = null;
                    if (propDef.hasSetter()) {
                        prop = constructSettableProperty(ctxt, beanDesc, propDef, propDef.getSetter().getGenericParameterType(0));
                    } else if (propDef.hasField()) {
                        prop = constructSettableProperty(ctxt, beanDesc, propDef, propDef.getField().getGenericType());
                    } else if (useGettersAsSetters && propDef.hasGetter()) {
                        Class<?> rawPropertyType = propDef.getGetter().getRawType();
                        if (Collection.class.isAssignableFrom(rawPropertyType) || Map.class.isAssignableFrom(rawPropertyType)) {
                            prop = constructSetterlessProperty(ctxt, beanDesc, propDef);
                        }
                    }
                    if (!isConcrete && propDef.hasConstructorParameter()) {
                        String name = propDef.getName();
                        SettableBeanProperty cprop = null;
                        if (creatorProps != null) {
                            for (SettableBeanProperty cp : creatorProps) {
                                if (name.equals(cp.getName()) && (cp instanceof CreatorProperty)) {
                                    cprop = (CreatorProperty) cp;
                                    break;
                                }
                            }
                        }
                        if (cprop == null) {
                            throw ctxt.mappingException("Could not find creator property with name '%s' (in class %s)", name, beanDesc.getBeanClass().getName());
                        }
                        if (prop != null) {
                            cprop.setFallbackSetter(prop);
                        }
                        prop = cprop;
                        builder.addCreatorProperty(cprop);
                    } else if (prop != null) {
                        views = propDef.findViews();
                        if (views == null) {
                            if (!ctxt.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
                                views = NO_VIEWS;
                            }
                        }
                        prop.setViews(views);
                        builder.addProperty(prop);
                    }
                }
            }
        }
        useGettersAsSetters = false;
        propDefs = filterBeanProps(ctxt, beanDesc, builder, beanDesc.findProperties(), ignored);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            while (i$.hasNext()) {
                propDefs = updateProperties.updateProperties(ctxt.getConfig(), beanDesc, propDefs);
            }
        }
        for (BeanPropertyDefinition propDef2 : propDefs) {
            prop = null;
            if (propDef2.hasSetter()) {
                prop = constructSettableProperty(ctxt, beanDesc, propDef2, propDef2.getSetter().getGenericParameterType(0));
            } else if (propDef2.hasField()) {
                prop = constructSettableProperty(ctxt, beanDesc, propDef2, propDef2.getField().getGenericType());
            } else {
                Class<?> rawPropertyType2 = propDef2.getGetter().getRawType();
                prop = constructSetterlessProperty(ctxt, beanDesc, propDef2);
            }
            if (!isConcrete) {
            }
            if (prop != null) {
                views = propDef2.findViews();
                if (views == null) {
                    if (ctxt.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
                        views = NO_VIEWS;
                    }
                }
                prop.setViews(views);
                builder.addProperty(prop);
            }
        }
    }

    protected List<BeanPropertyDefinition> filterBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder, List<BeanPropertyDefinition> propDefsIn, Set<String> ignored) throws JsonMappingException {
        ArrayList<BeanPropertyDefinition> result = new ArrayList(Math.max(4, propDefsIn.size()));
        HashMap<Class<?>, Boolean> ignoredTypes = new HashMap();
        for (BeanPropertyDefinition property : propDefsIn) {
            String name = property.getName();
            if (!ignored.contains(name)) {
                if (!property.hasConstructorParameter()) {
                    Class<?> rawPropertyType = null;
                    if (property.hasSetter()) {
                        rawPropertyType = property.getSetter().getRawParameterType(0);
                    } else if (property.hasField()) {
                        rawPropertyType = property.getField().getRawType();
                    }
                    if (rawPropertyType != null && isIgnorableType(ctxt.getConfig(), beanDesc, rawPropertyType, ignoredTypes)) {
                        builder.addIgnorable(name);
                    }
                }
                result.add(property);
            }
        }
        return result;
    }

    protected void addReferenceProperties(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        Map<String, AnnotatedMember> refs = beanDesc.findBackReferenceProperties();
        if (refs != null) {
            for (Entry<String, AnnotatedMember> en : refs.entrySet()) {
                Type genericType;
                String name = (String) en.getKey();
                AnnotatedMember m = (AnnotatedMember) en.getValue();
                if (m instanceof AnnotatedMethod) {
                    genericType = ((AnnotatedMethod) m).getGenericParameterType(0);
                } else {
                    genericType = m.getRawType();
                }
                builder.addBackReferenceProperty(name, constructSettableProperty(ctxt, beanDesc, SimpleBeanPropertyDefinition.construct(ctxt.getConfig(), m), genericType));
            }
        }
    }

    protected void addInjectables(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        Map<Object, AnnotatedMember> raw = beanDesc.findInjectables();
        if (raw != null) {
            boolean fixAccess = ctxt.canOverrideAccessModifiers();
            for (Entry<Object, AnnotatedMember> entry : raw.entrySet()) {
                AnnotatedMember m = (AnnotatedMember) entry.getValue();
                if (fixAccess) {
                    m.fixAccess();
                }
                builder.addInjectable(PropertyName.construct(m.getName()), beanDesc.resolveType(m.getGenericType()), beanDesc.getClassAnnotations(), m, entry.getKey());
            }
        }
    }

    protected SettableAnyProperty constructAnySetter(DeserializationContext ctxt, BeanDescription beanDesc, AnnotatedMethod setter) throws JsonMappingException {
        if (ctxt.canOverrideAccessModifiers()) {
            setter.fixAccess();
        }
        JavaType type = beanDesc.bindingsForBeanType().resolveType(setter.getGenericParameterType(1));
        Std property = new Std(PropertyName.construct(setter.getName()), type, null, beanDesc.getClassAnnotations(), (AnnotatedMember) setter, PropertyMetadata.STD_OPTIONAL);
        type = resolveType(ctxt, beanDesc, type, setter);
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(ctxt, setter);
        type = modifyTypeByAnnotation(ctxt, setter, type);
        if (deser == null) {
            deser = (JsonDeserializer) type.getValueHandler();
        }
        return new SettableAnyProperty(property, setter, type, deser, (TypeDeserializer) type.getTypeHandler());
    }

    protected SettableBeanProperty constructSettableProperty(DeserializationContext ctxt, BeanDescription beanDesc, BeanPropertyDefinition propDef, Type jdkType) throws JsonMappingException {
        SettableBeanProperty prop;
        AnnotatedMember mutator = propDef.getNonConstructorMutator();
        if (ctxt.canOverrideAccessModifiers()) {
            mutator.fixAccess();
        }
        JavaType t0 = beanDesc.resolveType(jdkType);
        Std property = new Std(propDef.getFullName(), t0, propDef.getWrapperName(), beanDesc.getClassAnnotations(), mutator, propDef.getMetadata());
        JavaType type = resolveType(ctxt, beanDesc, t0, mutator);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(ctxt, mutator);
        type = modifyTypeByAnnotation(ctxt, mutator, type);
        TypeDeserializer typeDeser = (TypeDeserializer) type.getTypeHandler();
        if (mutator instanceof AnnotatedMethod) {
            prop = new MethodProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), (AnnotatedMethod) mutator);
        } else {
            prop = new FieldProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), (AnnotatedField) mutator);
        }
        if (propDeser != null) {
            prop = prop.withValueDeserializer(propDeser);
        }
        ReferenceProperty ref = propDef.findReferenceType();
        if (ref != null && ref.isManagedReference()) {
            prop.setManagedReferenceName(ref.getName());
        }
        ObjectIdInfo objectIdInfo = propDef.findObjectIdInfo();
        if (objectIdInfo != null) {
            prop.setObjectIdInfo(objectIdInfo);
        }
        return prop;
    }

    protected SettableBeanProperty constructSetterlessProperty(DeserializationContext ctxt, BeanDescription beanDesc, BeanPropertyDefinition propDef) throws JsonMappingException {
        AnnotatedMethod getter = propDef.getGetter();
        if (ctxt.canOverrideAccessModifiers()) {
            getter.fixAccess();
        }
        JavaType type = getter.getType(beanDesc.bindingsForBeanType());
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(ctxt, getter);
        type = resolveType(ctxt, beanDesc, modifyTypeByAnnotation(ctxt, getter, type), getter);
        SettableBeanProperty prop = new SetterlessProperty(propDef, type, (TypeDeserializer) type.getTypeHandler(), beanDesc.getClassAnnotations(), getter);
        if (propDeser != null) {
            return prop.withValueDeserializer(propDeser);
        }
        return prop;
    }

    protected boolean isPotentialBeanType(Class<?> type) {
        String typeStr = ClassUtil.canBeABeanType(type);
        if (typeStr != null) {
            throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
        } else if (ClassUtil.isProxyType(type)) {
            throw new IllegalArgumentException("Can not deserialize Proxy class " + type.getName() + " as a Bean");
        } else {
            typeStr = ClassUtil.isLocalType(type, true);
            if (typeStr == null) {
                return true;
            }
            throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
        }
    }

    protected boolean isIgnorableType(DeserializationConfig config, BeanDescription beanDesc, Class<?> type, Map<Class<?>, Boolean> ignoredTypes) {
        Boolean status = (Boolean) ignoredTypes.get(type);
        if (status != null) {
            return status.booleanValue();
        }
        status = config.getAnnotationIntrospector().isIgnorableType(config.introspectClassAnnotations((Class) type).getClassInfo());
        return status == null ? false : status.booleanValue();
    }
}
