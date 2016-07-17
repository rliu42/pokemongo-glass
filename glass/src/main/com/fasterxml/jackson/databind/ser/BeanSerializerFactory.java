package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty.Std;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.FilteredBeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class BeanSerializerFactory extends BasicSerializerFactory implements Serializable {
    public static final BeanSerializerFactory instance;
    private static final long serialVersionUID = 1;

    static {
        instance = new BeanSerializerFactory(null);
    }

    protected BeanSerializerFactory(SerializerFactoryConfig config) {
        super(config);
    }

    public SerializerFactory withConfig(SerializerFactoryConfig config) {
        if (this._factoryConfig == config) {
            return this;
        }
        if (getClass() != BeanSerializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
        }
        this(config);
        return this;
    }

    protected Iterable<Serializers> customSerializers() {
        return this._factoryConfig.serializers();
    }

    public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType origType) throws JsonMappingException {
        SerializationConfig config = prov.getConfig();
        BeanDescription beanDesc = config.introspect(origType);
        JsonSerializer<?> ser = findSerializerFromAnnotation(prov, beanDesc.getClassInfo());
        if (ser != null) {
            return ser;
        }
        boolean staticTyping;
        JavaType type = modifyTypeByAnnotation(config, beanDesc.getClassInfo(), origType);
        if (type == origType) {
            staticTyping = false;
        } else {
            staticTyping = true;
            if (!type.hasRawClass(origType.getRawClass())) {
                beanDesc = config.introspect(type);
            }
        }
        Converter<Object, Object> conv = beanDesc.findSerializationConverter();
        if (conv == null) {
            return _createSerializer2(prov, type, beanDesc, staticTyping);
        }
        JavaType delegateType = conv.getOutputType(prov.getTypeFactory());
        if (!delegateType.hasRawClass(type.getRawClass())) {
            beanDesc = config.introspect(delegateType);
            ser = findSerializerFromAnnotation(prov, beanDesc.getClassInfo());
        }
        if (ser == null && !delegateType.isJavaLangObject()) {
            ser = _createSerializer2(prov, delegateType, beanDesc, true);
        }
        return new StdDelegatingSerializer(conv, delegateType, ser);
    }

    protected JsonSerializer<?> _createSerializer2(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping) throws JsonMappingException {
        JsonSerializer<?> jsonSerializer = null;
        SerializationConfig config = prov.getConfig();
        if (type.isContainerType()) {
            if (!staticTyping) {
                staticTyping = usesStaticTyping(config, beanDesc, null);
            }
            jsonSerializer = buildContainerSerializer(prov, type, beanDesc, staticTyping);
            if (jsonSerializer != null) {
                return jsonSerializer;
            }
        }
        for (Serializers serializers : customSerializers()) {
            jsonSerializer = serializers.findSerializer(config, type, beanDesc);
            if (jsonSerializer != null) {
                break;
            }
        }
        if (jsonSerializer == null) {
            jsonSerializer = findSerializerByAnnotations(prov, type, beanDesc);
        }
        if (jsonSerializer == null) {
            jsonSerializer = findSerializerByLookup(type, config, beanDesc, staticTyping);
            if (jsonSerializer == null) {
                jsonSerializer = findSerializerByPrimaryType(prov, type, beanDesc, staticTyping);
                if (jsonSerializer == null) {
                    jsonSerializer = findBeanSerializer(prov, type, beanDesc);
                    if (jsonSerializer == null) {
                        jsonSerializer = findSerializerByAddonType(config, type, beanDesc, staticTyping);
                        if (jsonSerializer == null) {
                            jsonSerializer = prov.getUnknownTypeSerializer(beanDesc.getBeanClass());
                        }
                    }
                }
            }
        }
        if (jsonSerializer != null && this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
                jsonSerializer = mod.modifySerializer(config, beanDesc, jsonSerializer);
            }
        }
        return jsonSerializer;
    }

    public JsonSerializer<Object> findBeanSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        if (isPotentialBeanType(type.getRawClass()) || type.isEnumType()) {
            return constructBeanSerializer(prov, beanDesc);
        }
        return null;
    }

    public TypeSerializer findPropertyTypeSerializer(JavaType baseType, SerializationConfig config, AnnotatedMember accessor) throws JsonMappingException {
        TypeResolverBuilder<?> b = config.getAnnotationIntrospector().findPropertyTypeResolver(config, accessor, baseType);
        if (b == null) {
            return createTypeSerializer(config, baseType);
        }
        return b.buildTypeSerializer(config, baseType, config.getSubtypeResolver().collectAndResolveSubtypesByClass(config, accessor, baseType));
    }

    public TypeSerializer findPropertyContentTypeSerializer(JavaType containerType, SerializationConfig config, AnnotatedMember accessor) throws JsonMappingException {
        JavaType contentType = containerType.getContentType();
        TypeResolverBuilder<?> b = config.getAnnotationIntrospector().findPropertyContentTypeResolver(config, accessor, containerType);
        if (b == null) {
            return createTypeSerializer(config, contentType);
        }
        return b.buildTypeSerializer(config, contentType, config.getSubtypeResolver().collectAndResolveSubtypesByClass(config, accessor, contentType));
    }

    protected JsonSerializer<Object> constructBeanSerializer(SerializerProvider prov, BeanDescription beanDesc) throws JsonMappingException {
        if (beanDesc.getBeanClass() == Object.class) {
            return prov.getUnknownTypeSerializer(Object.class);
        }
        MapperConfig config = prov.getConfig();
        BeanSerializerBuilder builder = constructBeanSerializerBuilder(beanDesc);
        builder.setConfig(config);
        List<BeanPropertyWriter> props = findBeanProperties(prov, beanDesc, builder);
        if (props == null) {
            props = new ArrayList();
        } else {
            props = removeOverlappingTypeIds(prov, beanDesc, builder, props);
        }
        prov.getAnnotationIntrospector().findAndAddVirtualProperties(config, beanDesc.getClassInfo(), props);
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier changeProperties : this._factoryConfig.serializerModifiers()) {
                props = changeProperties.changeProperties(config, beanDesc, props);
            }
        }
        props = filterBeanProperties(config, beanDesc, props);
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier changeProperties2 : this._factoryConfig.serializerModifiers()) {
                props = changeProperties2.orderProperties(config, beanDesc, props);
            }
        }
        builder.setObjectIdWriter(constructObjectIdHandler(prov, beanDesc, props));
        builder.setProperties(props);
        builder.setFilterId(findFilterId(config, beanDesc));
        AnnotatedMember anyGetter = beanDesc.findAnyGetter();
        if (anyGetter != null) {
            if (config.canOverrideAccessModifiers()) {
                anyGetter.fixAccess();
            }
            JavaType type = anyGetter.getType(beanDesc.bindingsForBeanType());
            boolean staticTyping = config.isEnabled(MapperFeature.USE_STATIC_TYPING);
            JavaType valueType = type.getContentType();
            TypeSerializer typeSer = createTypeSerializer(config, valueType);
            JsonSerializer<?> anySer = findSerializerFromAnnotation(prov, anyGetter);
            if (anySer == null) {
                anySer = MapSerializer.construct(null, type, staticTyping, typeSer, null, null, null);
            }
            builder.setAnyGetter(new AnyGetterWriter(new Std(PropertyName.construct(anyGetter.getName()), valueType, null, beanDesc.getClassAnnotations(), anyGetter, PropertyMetadata.STD_OPTIONAL), anyGetter, anySer));
        }
        processViews(config, builder);
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier changeProperties22 : this._factoryConfig.serializerModifiers()) {
                builder = changeProperties22.updateBuilder(config, beanDesc, builder);
            }
        }
        JsonSerializer<Object> ser = builder.build();
        if (ser == null && beanDesc.hasKnownClassAnnotations()) {
            return builder.createDummy();
        }
        return ser;
    }

    protected ObjectIdWriter constructObjectIdHandler(SerializerProvider prov, BeanDescription beanDesc, List<BeanPropertyWriter> props) throws JsonMappingException {
        ObjectIdInfo objectIdInfo = beanDesc.getObjectIdInfo();
        if (objectIdInfo == null) {
            return null;
        }
        Class<?> implClass = objectIdInfo.getGeneratorType();
        if (implClass == PropertyGenerator.class) {
            String propName = objectIdInfo.getPropertyName().getSimpleName();
            int len = props.size();
            for (int i = 0; i != len; i++) {
                BeanPropertyWriter prop = (BeanPropertyWriter) props.get(i);
                if (propName.equals(prop.getName())) {
                    BeanPropertyWriter idProp = prop;
                    if (i > 0) {
                        props.remove(i);
                        props.add(0, idProp);
                    }
                    return ObjectIdWriter.construct(idProp.getType(), (PropertyName) null, new PropertyBasedObjectIdGenerator(objectIdInfo, idProp), objectIdInfo.getAlwaysAsId());
                }
            }
            throw new IllegalArgumentException("Invalid Object Id definition for " + beanDesc.getBeanClass().getName() + ": can not find property with name '" + propName + "'");
        }
        return ObjectIdWriter.construct(prov.getTypeFactory().findTypeParameters(prov.constructType(implClass), ObjectIdGenerator.class)[0], objectIdInfo.getPropertyName(), prov.objectIdGeneratorInstance(beanDesc.getClassInfo(), objectIdInfo), objectIdInfo.getAlwaysAsId());
    }

    protected BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter writer, Class<?>[] inViews) {
        return FilteredBeanPropertyWriter.constructViewBased(writer, inViews);
    }

    protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BeanDescription beanDesc) {
        return new PropertyBuilder(config, beanDesc);
    }

    protected BeanSerializerBuilder constructBeanSerializerBuilder(BeanDescription beanDesc) {
        return new BeanSerializerBuilder(beanDesc);
    }

    protected boolean isPotentialBeanType(Class<?> type) {
        return ClassUtil.canBeABeanType(type) == null && !ClassUtil.isProxyType(type);
    }

    protected List<BeanPropertyWriter> findBeanProperties(SerializerProvider prov, BeanDescription beanDesc, BeanSerializerBuilder builder) throws JsonMappingException {
        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        SerializationConfig config = prov.getConfig();
        removeIgnorableTypes(config, beanDesc, properties);
        if (config.isEnabled(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS)) {
            removeSetterlessGetters(config, beanDesc, properties);
        }
        if (properties.isEmpty()) {
            return null;
        }
        boolean staticTyping = usesStaticTyping(config, beanDesc, null);
        PropertyBuilder pb = constructPropertyBuilder(config, beanDesc);
        List<BeanPropertyWriter> result = new ArrayList(properties.size());
        TypeBindings typeBind = beanDesc.bindingsForBeanType();
        for (BeanPropertyDefinition property : properties) {
            AnnotatedMember accessor = property.getAccessor();
            if (!property.isTypeId()) {
                ReferenceProperty refType = property.findReferenceType();
                if (refType == null || !refType.isBackReference()) {
                    if (accessor instanceof AnnotatedMethod) {
                        result.add(_constructWriter(prov, property, typeBind, pb, staticTyping, (AnnotatedMethod) accessor));
                    } else {
                        result.add(_constructWriter(prov, property, typeBind, pb, staticTyping, (AnnotatedField) accessor));
                    }
                }
            } else if (accessor != null) {
                if (config.canOverrideAccessModifiers()) {
                    accessor.fixAccess();
                }
                builder.setTypeId(accessor);
            }
        }
        return result;
    }

    protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> props) {
        String[] ignored = config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo(), true);
        if (ignored != null && ignored.length > 0) {
            HashSet<String> ignoredSet = ArrayBuilders.arrayToSet(ignored);
            Iterator<BeanPropertyWriter> it = props.iterator();
            while (it.hasNext()) {
                if (ignoredSet.contains(((BeanPropertyWriter) it.next()).getName())) {
                    it.remove();
                }
            }
        }
        return props;
    }

    protected void processViews(SerializationConfig config, BeanSerializerBuilder builder) {
        List<BeanPropertyWriter> props = builder.getProperties();
        boolean includeByDefault = config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
        int propCount = props.size();
        int viewsFound = 0;
        BeanPropertyWriter[] filtered = new BeanPropertyWriter[propCount];
        for (int i = 0; i < propCount; i++) {
            BeanPropertyWriter bpw = (BeanPropertyWriter) props.get(i);
            Class<?>[] views = bpw.getViews();
            if (views != null) {
                viewsFound++;
                filtered[i] = constructFilteredBeanWriter(bpw, views);
            } else if (includeByDefault) {
                filtered[i] = bpw;
            }
        }
        if (!includeByDefault || viewsFound != 0) {
            builder.setFilteredProperties(filtered);
        }
    }

    protected void removeIgnorableTypes(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyDefinition> properties) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        HashMap<Class<?>, Boolean> ignores = new HashMap();
        Iterator<BeanPropertyDefinition> it = properties.iterator();
        while (it.hasNext()) {
            AnnotatedMember accessor = ((BeanPropertyDefinition) it.next()).getAccessor();
            if (accessor == null) {
                it.remove();
            } else {
                Class<?> type = accessor.getRawType();
                Boolean result = (Boolean) ignores.get(type);
                if (result == null) {
                    result = intr.isIgnorableType(config.introspectClassAnnotations((Class) type).getClassInfo());
                    if (result == null) {
                        result = Boolean.FALSE;
                    }
                    ignores.put(type, result);
                }
                if (result.booleanValue()) {
                    it.remove();
                }
            }
        }
    }

    protected void removeSetterlessGetters(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyDefinition> properties) {
        Iterator<BeanPropertyDefinition> it = properties.iterator();
        while (it.hasNext()) {
            BeanPropertyDefinition property = (BeanPropertyDefinition) it.next();
            if (!(property.couldDeserialize() || property.isExplicitlyIncluded())) {
                it.remove();
            }
        }
    }

    protected List<BeanPropertyWriter> removeOverlappingTypeIds(SerializerProvider prov, BeanDescription beanDesc, BeanSerializerBuilder builder, List<BeanPropertyWriter> props) {
        int end = props.size();
        for (int i = 0; i < end; i++) {
            BeanPropertyWriter bpw = (BeanPropertyWriter) props.get(i);
            TypeSerializer td = bpw.getTypeSerializer();
            if (td != null && td.getTypeInclusion() == As.EXTERNAL_PROPERTY) {
                PropertyName typePropName = PropertyName.construct(td.getPropertyName());
                for (BeanPropertyWriter w2 : props) {
                    if (w2 != bpw && w2.wouldConflictWithName(typePropName)) {
                        bpw.assignTypeSerializer(null);
                        break;
                    }
                }
            }
        }
        return props;
    }

    protected BeanPropertyWriter _constructWriter(SerializerProvider prov, BeanPropertyDefinition propDef, TypeBindings typeContext, PropertyBuilder pb, boolean staticTyping, AnnotatedMember accessor) throws JsonMappingException {
        PropertyName name = propDef.getFullName();
        if (prov.canOverrideAccessModifiers()) {
            accessor.fixAccess();
        }
        JavaType type = accessor.getType(typeContext);
        Std property = new Std(name, type, propDef.getWrapperName(), pb.getClassAnnotations(), accessor, propDef.getMetadata());
        JsonSerializer<?> annotatedSerializer = findSerializerFromAnnotation(prov, accessor);
        if (annotatedSerializer instanceof ResolvableSerializer) {
            ((ResolvableSerializer) annotatedSerializer).resolve(prov);
        }
        annotatedSerializer = prov.handlePrimaryContextualization(annotatedSerializer, property);
        TypeSerializer contentTypeSer = null;
        if (ClassUtil.isCollectionMapOrArray(type.getRawClass()) || type.isCollectionLikeType() || type.isMapLikeType()) {
            contentTypeSer = findPropertyContentTypeSerializer(type, prov.getConfig(), accessor);
        }
        return pb.buildWriter(prov, propDef, type, annotatedSerializer, findPropertyTypeSerializer(type, prov.getConfig(), accessor), contentTypeSer, accessor, staticTyping);
    }
}
