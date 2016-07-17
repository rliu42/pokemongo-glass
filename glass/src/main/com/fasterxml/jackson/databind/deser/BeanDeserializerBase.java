package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.BeanProperty.Std;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.deser.impl.ManagedReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.TypeWrappedDeserializer;
import com.fasterxml.jackson.databind.deser.impl.UnwrappedPropertyHandler;
import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import spacemadness.com.lunarconsole.C1391R;

public abstract class BeanDeserializerBase extends StdDeserializer<Object> implements ContextualDeserializer, ResolvableDeserializer, Serializable {
    protected static final PropertyName TEMP_PROPERTY_NAME;
    private static final long serialVersionUID = 1;
    protected SettableAnyProperty _anySetter;
    protected final Map<String, SettableBeanProperty> _backRefs;
    protected final BeanPropertyMap _beanProperties;
    protected final JavaType _beanType;
    private final transient Annotations _classAnnotations;
    protected JsonDeserializer<Object> _delegateDeserializer;
    protected ExternalTypeHandler _externalTypeIdHandler;
    protected final HashSet<String> _ignorableProps;
    protected final boolean _ignoreAllUnknown;
    protected final ValueInjector[] _injectables;
    protected final boolean _needViewProcesing;
    protected boolean _nonStandardCreation;
    protected final ObjectIdReader _objectIdReader;
    protected PropertyBasedCreator _propertyBasedCreator;
    protected final Shape _serializationShape;
    protected transient HashMap<ClassKey, JsonDeserializer<Object>> _subDeserializers;
    protected UnwrappedPropertyHandler _unwrappedPropertyHandler;
    protected final ValueInstantiator _valueInstantiator;
    protected boolean _vanillaProcessing;

    /* renamed from: com.fasterxml.jackson.databind.deser.BeanDeserializerBase.1 */
    static /* synthetic */ class C01301 {
        static final /* synthetic */ int[] $SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType;

        static {
            $SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType = new int[NumberType.values().length];
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType[NumberType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType[NumberType.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType[NumberType.FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType[NumberType.DOUBLE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    protected abstract Object _deserializeUsingPropertyBased(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException;

    protected abstract BeanDeserializerBase asArrayDeserializer();

    public abstract Object deserializeFromObject(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException;

    public abstract JsonDeserializer<Object> unwrappingDeserializer(NameTransformer nameTransformer);

    public abstract BeanDeserializerBase withIgnorableProperties(HashSet<String> hashSet);

    public abstract BeanDeserializerBase withObjectIdReader(ObjectIdReader objectIdReader);

    static {
        TEMP_PROPERTY_NAME = new PropertyName("#temporary-name");
    }

    protected BeanDeserializerBase(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews) {
        boolean z;
        boolean z2 = true;
        Shape shape = null;
        super(beanDesc.getType());
        this._classAnnotations = beanDesc.getClassInfo().getAnnotations();
        this._beanType = beanDesc.getType();
        this._valueInstantiator = builder.getValueInstantiator();
        this._beanProperties = properties;
        this._backRefs = backRefs;
        this._ignorableProps = ignorableProps;
        this._ignoreAllUnknown = ignoreAllUnknown;
        this._anySetter = builder.getAnySetter();
        List<ValueInjector> injectables = builder.getInjectables();
        ValueInjector[] valueInjectorArr = (injectables == null || injectables.isEmpty()) ? null : (ValueInjector[]) injectables.toArray(new ValueInjector[injectables.size()]);
        this._injectables = valueInjectorArr;
        this._objectIdReader = builder.getObjectIdReader();
        if (this._unwrappedPropertyHandler != null || this._valueInstantiator.canCreateUsingDelegate() || this._valueInstantiator.canCreateFromObjectWith() || !this._valueInstantiator.canCreateUsingDefault()) {
            z = true;
        } else {
            z = false;
        }
        this._nonStandardCreation = z;
        Value format = beanDesc.findExpectedFormat(null);
        if (format != null) {
            shape = format.getShape();
        }
        this._serializationShape = shape;
        this._needViewProcesing = hasViews;
        if (this._nonStandardCreation || this._injectables != null || this._needViewProcesing || this._objectIdReader != null) {
            z2 = false;
        }
        this._vanillaProcessing = z2;
    }

    protected BeanDeserializerBase(BeanDeserializerBase src) {
        this(src, src._ignoreAllUnknown);
    }

    protected BeanDeserializerBase(BeanDeserializerBase src, boolean ignoreAllUnknown) {
        super(src._beanType);
        this._classAnnotations = src._classAnnotations;
        this._beanType = src._beanType;
        this._valueInstantiator = src._valueInstantiator;
        this._delegateDeserializer = src._delegateDeserializer;
        this._propertyBasedCreator = src._propertyBasedCreator;
        this._beanProperties = src._beanProperties;
        this._backRefs = src._backRefs;
        this._ignorableProps = src._ignorableProps;
        this._ignoreAllUnknown = ignoreAllUnknown;
        this._anySetter = src._anySetter;
        this._injectables = src._injectables;
        this._objectIdReader = src._objectIdReader;
        this._nonStandardCreation = src._nonStandardCreation;
        this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
        this._needViewProcesing = src._needViewProcesing;
        this._serializationShape = src._serializationShape;
        this._vanillaProcessing = src._vanillaProcessing;
    }

    protected BeanDeserializerBase(BeanDeserializerBase src, NameTransformer unwrapper) {
        boolean z;
        super(src._beanType);
        this._classAnnotations = src._classAnnotations;
        this._beanType = src._beanType;
        this._valueInstantiator = src._valueInstantiator;
        this._delegateDeserializer = src._delegateDeserializer;
        this._propertyBasedCreator = src._propertyBasedCreator;
        this._backRefs = src._backRefs;
        this._ignorableProps = src._ignorableProps;
        if (unwrapper != null || src._ignoreAllUnknown) {
            z = true;
        } else {
            z = false;
        }
        this._ignoreAllUnknown = z;
        this._anySetter = src._anySetter;
        this._injectables = src._injectables;
        this._objectIdReader = src._objectIdReader;
        this._nonStandardCreation = src._nonStandardCreation;
        UnwrappedPropertyHandler uph = src._unwrappedPropertyHandler;
        if (unwrapper != null) {
            if (uph != null) {
                uph = uph.renameAll(unwrapper);
            }
            this._beanProperties = src._beanProperties.renameAll(unwrapper);
        } else {
            this._beanProperties = src._beanProperties;
        }
        this._unwrappedPropertyHandler = uph;
        this._needViewProcesing = src._needViewProcesing;
        this._serializationShape = src._serializationShape;
        this._vanillaProcessing = false;
    }

    public BeanDeserializerBase(BeanDeserializerBase src, ObjectIdReader oir) {
        super(src._beanType);
        this._classAnnotations = src._classAnnotations;
        this._beanType = src._beanType;
        this._valueInstantiator = src._valueInstantiator;
        this._delegateDeserializer = src._delegateDeserializer;
        this._propertyBasedCreator = src._propertyBasedCreator;
        this._backRefs = src._backRefs;
        this._ignorableProps = src._ignorableProps;
        this._ignoreAllUnknown = src._ignoreAllUnknown;
        this._anySetter = src._anySetter;
        this._injectables = src._injectables;
        this._nonStandardCreation = src._nonStandardCreation;
        this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
        this._needViewProcesing = src._needViewProcesing;
        this._serializationShape = src._serializationShape;
        this._objectIdReader = oir;
        if (oir == null) {
            this._beanProperties = src._beanProperties;
            this._vanillaProcessing = src._vanillaProcessing;
            return;
        }
        this._beanProperties = src._beanProperties.withProperty(new ObjectIdValueProperty(oir, PropertyMetadata.STD_REQUIRED));
        this._vanillaProcessing = false;
    }

    public BeanDeserializerBase(BeanDeserializerBase src, HashSet<String> ignorableProps) {
        super(src._beanType);
        this._classAnnotations = src._classAnnotations;
        this._beanType = src._beanType;
        this._valueInstantiator = src._valueInstantiator;
        this._delegateDeserializer = src._delegateDeserializer;
        this._propertyBasedCreator = src._propertyBasedCreator;
        this._backRefs = src._backRefs;
        this._ignorableProps = ignorableProps;
        this._ignoreAllUnknown = src._ignoreAllUnknown;
        this._anySetter = src._anySetter;
        this._injectables = src._injectables;
        this._nonStandardCreation = src._nonStandardCreation;
        this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
        this._needViewProcesing = src._needViewProcesing;
        this._serializationShape = src._serializationShape;
        this._vanillaProcessing = src._vanillaProcessing;
        this._objectIdReader = src._objectIdReader;
        this._beanProperties = src._beanProperties;
    }

    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        SettableBeanProperty[] creatorProps;
        SettableBeanProperty prop;
        TypeDeserializer typeDeser;
        Builder extTypes = null;
        if (this._valueInstantiator.canCreateFromObjectWith()) {
            creatorProps = this._valueInstantiator.getFromObjectArguments(ctxt.getConfig());
            for (SettableBeanProperty prop2 : creatorProps) {
                if (prop2.hasValueTypeDeserializer()) {
                    typeDeser = prop2.getValueTypeDeserializer();
                    if (typeDeser.getTypeInclusion() == As.EXTERNAL_PROPERTY) {
                        if (extTypes == null) {
                            extTypes = new Builder();
                        }
                        extTypes.addExternal(prop2, typeDeser);
                    }
                }
            }
        } else {
            creatorProps = null;
        }
        UnwrappedPropertyHandler unwrapped = null;
        Iterator i$ = this._beanProperties.iterator();
        while (i$.hasNext()) {
            SettableBeanProperty origProp = (SettableBeanProperty) i$.next();
            prop2 = origProp;
            if (prop2.hasValueDeserializer()) {
                JsonDeserializer<Object> deser = prop2.getValueDeserializer();
                JsonDeserializer<?> cd = ctxt.handlePrimaryContextualization(deser, prop2, prop2.getType());
                if (cd != deser) {
                    prop2 = prop2.withValueDeserializer(cd);
                }
            } else {
                JsonDeserializer<?> deser2 = findConvertingDeserializer(ctxt, prop2);
                if (deser2 == null) {
                    deser2 = findDeserializer(ctxt, prop2.getType(), prop2);
                }
                prop2 = prop2.withValueDeserializer(deser2);
            }
            prop2 = _resolveManagedReferenceProperty(ctxt, prop2);
            if (!(prop2 instanceof ManagedReferenceProperty)) {
                prop2 = _resolvedObjectIdProperty(ctxt, prop2);
            }
            SettableBeanProperty u = _resolveUnwrappedProperty(ctxt, prop2);
            if (u != null) {
                prop2 = u;
                if (unwrapped == null) {
                    unwrapped = new UnwrappedPropertyHandler();
                }
                unwrapped.addProperty(prop2);
                this._beanProperties.remove(prop2);
            } else {
                prop2 = _resolveInnerClassValuedProperty(ctxt, prop2);
                if (prop2 != origProp) {
                    this._beanProperties.replace(prop2);
                    if (creatorProps != null) {
                        int len = creatorProps.length;
                        for (int i = 0; i < len; i++) {
                            if (creatorProps[i] == origProp) {
                                creatorProps[i] = prop2;
                                break;
                            }
                        }
                    }
                }
                if (prop2.hasValueTypeDeserializer()) {
                    typeDeser = prop2.getValueTypeDeserializer();
                    if (typeDeser.getTypeInclusion() == As.EXTERNAL_PROPERTY) {
                        if (extTypes == null) {
                            extTypes = new Builder();
                        }
                        extTypes.addExternal(prop2, typeDeser);
                        this._beanProperties.remove(prop2);
                    }
                }
            }
        }
        if (!(this._anySetter == null || this._anySetter.hasValueDeserializer())) {
            this._anySetter = this._anySetter.withValueDeserializer(findDeserializer(ctxt, this._anySetter.getType(), this._anySetter.getProperty()));
        }
        if (this._valueInstantiator.canCreateUsingDelegate()) {
            JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
            if (delegateType == null) {
                throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._beanType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
            }
            Std property = new Std(TEMP_PROPERTY_NAME, delegateType, null, this._classAnnotations, this._valueInstantiator.getDelegateCreator(), PropertyMetadata.STD_OPTIONAL);
            TypeDeserializer td = (TypeDeserializer) delegateType.getTypeHandler();
            if (td == null) {
                td = ctxt.getConfig().findTypeDeserializer(delegateType);
            }
            JsonDeserializer<Object> dd = findDeserializer(ctxt, delegateType, property);
            if (td != null) {
                dd = new TypeWrappedDeserializer(td.forProperty(property), dd);
            }
            this._delegateDeserializer = dd;
        }
        if (creatorProps != null) {
            this._propertyBasedCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, creatorProps);
        }
        if (extTypes != null) {
            this._externalTypeIdHandler = extTypes.build();
            this._nonStandardCreation = true;
        }
        this._unwrappedPropertyHandler = unwrapped;
        if (unwrapped != null) {
            this._nonStandardCreation = true;
        }
        boolean z = this._vanillaProcessing && !this._nonStandardCreation;
        this._vanillaProcessing = z;
    }

    protected JsonDeserializer<Object> findConvertingDeserializer(DeserializationContext ctxt, SettableBeanProperty prop) throws JsonMappingException {
        AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
        if (intr != null) {
            Object convDef = intr.findDeserializationConverter(prop.getMember());
            if (convDef != null) {
                Converter<Object, Object> conv = ctxt.converterInstance(prop.getMember(), convDef);
                JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
                return new StdDelegatingDeserializer(conv, delegateType, ctxt.findContextualValueDeserializer(delegateType, prop));
            }
        }
        return null;
    }

    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        ObjectIdReader oir = this._objectIdReader;
        AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
        AnnotatedMember accessor = (property == null || intr == null) ? null : property.getMember();
        if (!(accessor == null || intr == null)) {
            ObjectIdInfo objectIdInfo = intr.findObjectIdInfo(accessor);
            if (objectIdInfo != null) {
                SettableBeanProperty idProp;
                JavaType idType;
                ObjectIdGenerator<?> idGen;
                objectIdInfo = intr.findObjectReferenceInfo(accessor, objectIdInfo);
                Class<?> implClass = objectIdInfo.getGeneratorType();
                ObjectIdResolver resolver = ctxt.objectIdResolverInstance(accessor, objectIdInfo);
                if (implClass == PropertyGenerator.class) {
                    PropertyName propName = objectIdInfo.getPropertyName();
                    idProp = findProperty(propName);
                    if (idProp == null) {
                        throw new IllegalArgumentException("Invalid Object Id definition for " + handledType().getName() + ": can not find property with name '" + propName + "'");
                    }
                    idType = idProp.getType();
                    idGen = new PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
                } else {
                    idType = ctxt.getTypeFactory().findTypeParameters(ctxt.constructType(implClass), ObjectIdGenerator.class)[0];
                    idProp = null;
                    idGen = ctxt.objectIdGeneratorInstance(accessor, objectIdInfo);
                }
                oir = ObjectIdReader.construct(idType, objectIdInfo.getPropertyName(), idGen, ctxt.findRootValueDeserializer(idType), idProp, resolver);
            }
        }
        BeanDeserializerBase contextual = this;
        if (!(oir == null || oir == this._objectIdReader)) {
            contextual = withObjectIdReader(oir);
        }
        if (accessor != null) {
            String[] ignorals = intr.findPropertiesToIgnore(accessor, false);
            if (!(ignorals == null || ignorals.length == 0)) {
                contextual = contextual.withIgnorableProperties(ArrayBuilders.setAndArray(contextual._ignorableProps, ignorals));
            }
        }
        Shape shape = null;
        if (accessor != null) {
            Value format = intr.findFormat(accessor);
            if (format != null) {
                shape = format.getShape();
            }
        }
        if (shape == null) {
            shape = this._serializationShape;
        }
        if (shape == Shape.ARRAY) {
            return contextual.asArrayDeserializer();
        }
        return contextual;
    }

    protected SettableBeanProperty _resolveManagedReferenceProperty(DeserializationContext ctxt, SettableBeanProperty prop) {
        String refName = prop.getManagedReferenceName();
        if (refName == null) {
            return prop;
        }
        SettableBeanProperty backProp = prop.getValueDeserializer().findBackReference(refName);
        if (backProp == null) {
            throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': no back reference property found from type " + prop.getType());
        }
        JavaType referredType = this._beanType;
        JavaType backRefType = backProp.getType();
        boolean isContainer = prop.getType().isContainerType();
        if (backRefType.getRawClass().isAssignableFrom(referredType.getRawClass())) {
            return new ManagedReferenceProperty(prop, refName, backProp, this._classAnnotations, isContainer);
        }
        throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': back reference type (" + backRefType.getRawClass().getName() + ") not compatible with managed type (" + referredType.getRawClass().getName() + ")");
    }

    protected SettableBeanProperty _resolvedObjectIdProperty(DeserializationContext ctxt, SettableBeanProperty prop) {
        ObjectIdInfo objectIdInfo = prop.getObjectIdInfo();
        return (objectIdInfo == null && prop.getValueDeserializer().getObjectIdReader() == null) ? prop : new ObjectIdReferenceProperty(prop, objectIdInfo);
    }

    protected SettableBeanProperty _resolveUnwrappedProperty(DeserializationContext ctxt, SettableBeanProperty prop) {
        AnnotatedMember am = prop.getMember();
        if (am != null) {
            NameTransformer unwrapper = ctxt.getAnnotationIntrospector().findUnwrappingNameTransformer(am);
            if (unwrapper != null) {
                JsonDeserializer<Object> orig = prop.getValueDeserializer();
                JsonDeserializer<Object> unwrapping = orig.unwrappingDeserializer(unwrapper);
                if (!(unwrapping == orig || unwrapping == null)) {
                    return prop.withValueDeserializer(unwrapping);
                }
            }
        }
        return null;
    }

    protected SettableBeanProperty _resolveInnerClassValuedProperty(DeserializationContext ctxt, SettableBeanProperty prop) {
        JsonDeserializer<Object> deser = prop.getValueDeserializer();
        if (!(deser instanceof BeanDeserializerBase) || ((BeanDeserializerBase) deser).getValueInstantiator().canCreateUsingDefault()) {
            return prop;
        }
        Class<?> valueClass = prop.getType().getRawClass();
        Class<?> enclosing = ClassUtil.getOuterClass(valueClass);
        if (enclosing == null || enclosing != this._beanType.getRawClass()) {
            return prop;
        }
        for (Constructor ctor : valueClass.getConstructors()) {
            Class<?>[] paramTypes = ctor.getParameterTypes();
            if (paramTypes.length == 1 && paramTypes[0] == enclosing) {
                if (ctxt.getConfig().canOverrideAccessModifiers()) {
                    ClassUtil.checkAndFixAccess(ctor);
                }
                return new InnerClassProperty(prop, ctor);
            }
        }
        return prop;
    }

    public boolean isCachable() {
        return true;
    }

    public Class<?> handledType() {
        return this._beanType.getRawClass();
    }

    public ObjectIdReader getObjectIdReader() {
        return this._objectIdReader;
    }

    public boolean hasProperty(String propertyName) {
        return this._beanProperties.find(propertyName) != null;
    }

    public boolean hasViews() {
        return this._needViewProcesing;
    }

    public int getPropertyCount() {
        return this._beanProperties.size();
    }

    public Collection<Object> getKnownPropertyNames() {
        ArrayList<Object> names = new ArrayList();
        Iterator i$ = this._beanProperties.iterator();
        while (i$.hasNext()) {
            names.add(((SettableBeanProperty) i$.next()).getName());
        }
        return names;
    }

    @Deprecated
    public final Class<?> getBeanClass() {
        return this._beanType.getRawClass();
    }

    public JavaType getValueType() {
        return this._beanType;
    }

    public Iterator<SettableBeanProperty> properties() {
        if (this._beanProperties != null) {
            return this._beanProperties.iterator();
        }
        throw new IllegalStateException("Can only call after BeanDeserializer has been resolved");
    }

    public Iterator<SettableBeanProperty> creatorProperties() {
        if (this._propertyBasedCreator == null) {
            return Collections.emptyList().iterator();
        }
        return this._propertyBasedCreator.properties().iterator();
    }

    public SettableBeanProperty findProperty(PropertyName propertyName) {
        return findProperty(propertyName.getSimpleName());
    }

    public SettableBeanProperty findProperty(String propertyName) {
        SettableBeanProperty prop = this._beanProperties == null ? null : this._beanProperties.find(propertyName);
        if (prop != null || this._propertyBasedCreator == null) {
            return prop;
        }
        return this._propertyBasedCreator.findCreatorProperty(propertyName);
    }

    public SettableBeanProperty findProperty(int propertyIndex) {
        SettableBeanProperty prop = this._beanProperties == null ? null : this._beanProperties.find(propertyIndex);
        if (prop != null || this._propertyBasedCreator == null) {
            return prop;
        }
        return this._propertyBasedCreator.findCreatorProperty(propertyIndex);
    }

    public SettableBeanProperty findBackReference(String logicalName) {
        if (this._backRefs == null) {
            return null;
        }
        return (SettableBeanProperty) this._backRefs.get(logicalName);
    }

    public ValueInstantiator getValueInstantiator() {
        return this._valueInstantiator;
    }

    public void replaceProperty(SettableBeanProperty original, SettableBeanProperty replacement) {
        this._beanProperties.replace(replacement);
    }

    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        if (this._objectIdReader != null) {
            if (p.canReadObjectId()) {
                Object id = p.getObjectId();
                if (id != null) {
                    return _handleTypedObjectId(p, ctxt, typeDeserializer.deserializeTypedFromObject(p, ctxt), id);
                }
            }
            JsonToken t = p.getCurrentToken();
            if (t != null) {
                if (t.isScalarValue()) {
                    return deserializeFromObjectId(p, ctxt);
                }
                if (t == JsonToken.START_OBJECT) {
                    t = p.nextToken();
                }
                if (t == JsonToken.FIELD_NAME && this._objectIdReader.maySerializeAsObject() && this._objectIdReader.isValidReferencePropertyName(p.getCurrentName(), p)) {
                    return deserializeFromObjectId(p, ctxt);
                }
            }
        }
        return typeDeserializer.deserializeTypedFromObject(p, ctxt);
    }

    protected Object _handleTypedObjectId(JsonParser jp, DeserializationContext ctxt, Object pojo, Object rawId) throws IOException {
        Object id;
        JsonDeserializer<Object> idDeser = this._objectIdReader.getDeserializer();
        if (idDeser.handledType() == rawId.getClass()) {
            id = rawId;
        } else {
            id = _convertObjectId(jp, ctxt, rawId, idDeser);
        }
        ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver).bindItem(pojo);
        SettableBeanProperty idProp = this._objectIdReader.idProperty;
        if (idProp != null) {
            return idProp.setAndReturn(pojo, id);
        }
        return pojo;
    }

    protected Object _convertObjectId(JsonParser p, DeserializationContext ctxt, Object rawId, JsonDeserializer<Object> idDeser) throws IOException {
        TokenBuffer buf = new TokenBuffer(p, ctxt);
        if (rawId instanceof String) {
            buf.writeString((String) rawId);
        } else if (rawId instanceof Long) {
            buf.writeNumber(((Long) rawId).longValue());
        } else if (rawId instanceof Integer) {
            buf.writeNumber(((Integer) rawId).intValue());
        } else {
            buf.writeObject(rawId);
        }
        JsonParser bufParser = buf.asParser();
        bufParser.nextToken();
        return idDeser.deserialize(bufParser, ctxt);
    }

    protected Object deserializeWithObjectId(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return deserializeFromObject(jp, ctxt);
    }

    protected Object deserializeFromObjectId(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Object id = this._objectIdReader.readObjectReference(jp, ctxt);
        ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
        Object pojo = roid.resolve();
        if (pojo != null) {
            return pojo;
        }
        throw new UnresolvedForwardReference("Could not resolve Object Id [" + id + "] (for " + this._beanType + ").", jp.getCurrentLocation(), roid);
    }

    protected Object deserializeFromObjectUsingNonDefault(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (this._delegateDeserializer != null) {
            return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
        }
        if (this._propertyBasedCreator != null) {
            return _deserializeUsingPropertyBased(jp, ctxt);
        }
        if (this._beanType.isAbstract()) {
            throw JsonMappingException.from(jp, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
        }
        throw JsonMappingException.from(jp, "No suitable constructor found for type " + this._beanType + ": can not instantiate from JSON object (missing default constructor or creator, or perhaps need to add/enable type information?)");
    }

    public Object deserializeFromNumber(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (this._objectIdReader != null) {
            return deserializeFromObjectId(jp, ctxt);
        }
        Object bean;
        switch (C01301.$SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                if (this._delegateDeserializer == null || this._valueInstantiator.canCreateFromInt()) {
                    return this._valueInstantiator.createFromInt(ctxt, jp.getIntValue());
                }
                bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
                if (this._injectables == null) {
                    return bean;
                }
                injectValues(ctxt, bean);
                return bean;
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                if (this._delegateDeserializer == null || this._valueInstantiator.canCreateFromInt()) {
                    return this._valueInstantiator.createFromLong(ctxt, jp.getLongValue());
                }
                bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
                if (this._injectables == null) {
                    return bean;
                }
                injectValues(ctxt, bean);
                return bean;
            default:
                if (this._delegateDeserializer != null) {
                    bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
                    if (this._injectables == null) {
                        return bean;
                    }
                    injectValues(ctxt, bean);
                    return bean;
                }
                throw ctxt.instantiationException(handledType(), "no suitable creator method found to deserialize from JSON integer number");
        }
    }

    public Object deserializeFromString(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (this._objectIdReader != null) {
            return deserializeFromObjectId(p, ctxt);
        }
        if (this._delegateDeserializer == null || this._valueInstantiator.canCreateFromString()) {
            return this._valueInstantiator.createFromString(ctxt, p.getText());
        }
        Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
        if (this._injectables == null) {
            return bean;
        }
        injectValues(ctxt, bean);
        return bean;
    }

    public Object deserializeFromDouble(JsonParser p, DeserializationContext ctxt) throws IOException {
        switch (C01301.$SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType[p.getNumberType().ordinal()]) {
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
            case Place.TYPE_AQUARIUM /*4*/:
                if (this._delegateDeserializer == null || this._valueInstantiator.canCreateFromDouble()) {
                    return this._valueInstantiator.createFromDouble(ctxt, p.getDoubleValue());
                }
                Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
                if (this._injectables == null) {
                    return bean;
                }
                injectValues(ctxt, bean);
                return bean;
            default:
                if (this._delegateDeserializer != null) {
                    return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
                }
                throw ctxt.instantiationException(handledType(), "no suitable creator method found to deserialize from JSON floating-point number");
        }
    }

    public Object deserializeFromBoolean(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (this._delegateDeserializer == null || this._valueInstantiator.canCreateFromBoolean()) {
            return this._valueInstantiator.createFromBoolean(ctxt, p.getCurrentToken() == JsonToken.VALUE_TRUE);
        }
        Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
        if (this._injectables == null) {
            return bean;
        }
        injectValues(ctxt, bean);
        return bean;
    }

    public Object deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (this._delegateDeserializer != null) {
            try {
                Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
                if (this._injectables == null) {
                    return bean;
                }
                injectValues(ctxt, bean);
                return bean;
            } catch (Exception e) {
                wrapInstantiationProblem(e, ctxt);
            }
        }
        if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
            if (p.nextToken() == JsonToken.END_ARRAY && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
                return null;
            }
            Object value = deserialize(p, ctxt);
            if (p.nextToken() == JsonToken.END_ARRAY) {
                return value;
            }
            throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single '" + this._valueClass.getName() + "' value but there was more than a single value in the array");
        } else if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
            throw ctxt.mappingException(handledType());
        } else if (p.nextToken() == JsonToken.END_ARRAY) {
            return null;
        } else {
            throw ctxt.mappingException(handledType(), JsonToken.START_ARRAY);
        }
    }

    public Object deserializeFromEmbedded(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (this._objectIdReader != null) {
            return deserializeFromObjectId(jp, ctxt);
        }
        return jp.getEmbeddedObject();
    }

    protected void injectValues(DeserializationContext ctxt, Object bean) throws IOException, JsonProcessingException {
        for (ValueInjector injector : this._injectables) {
            injector.inject(ctxt, bean);
        }
    }

    protected Object handleUnknownProperties(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException, JsonProcessingException {
        unknownTokens.writeEndObject();
        JsonParser bufferParser = unknownTokens.asParser();
        while (bufferParser.nextToken() != JsonToken.END_OBJECT) {
            String propName = bufferParser.getCurrentName();
            bufferParser.nextToken();
            handleUnknownProperty(bufferParser, ctxt, bean, propName);
        }
        return bean;
    }

    protected void handleUnknownVanilla(JsonParser jp, DeserializationContext ctxt, Object bean, String propName) throws IOException, JsonProcessingException {
        if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
            handleIgnoredProperty(jp, ctxt, bean, propName);
        } else if (this._anySetter != null) {
            try {
                this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
            } catch (Throwable e) {
                wrapAndThrow(e, bean, propName, ctxt);
            }
        } else {
            handleUnknownProperty(jp, ctxt, bean, propName);
        }
    }

    protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object beanOrClass, String propName) throws IOException, JsonProcessingException {
        if (this._ignoreAllUnknown) {
            jp.skipChildren();
            return;
        }
        if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
            handleIgnoredProperty(jp, ctxt, beanOrClass, propName);
        }
        super.handleUnknownProperty(jp, ctxt, beanOrClass, propName);
    }

    protected void handleIgnoredProperty(JsonParser jp, DeserializationContext ctxt, Object beanOrClass, String propName) throws IOException, JsonProcessingException {
        if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)) {
            throw IgnoredPropertyException.from(jp, beanOrClass, propName, getKnownPropertyNames());
        }
        jp.skipChildren();
    }

    protected Object handlePolymorphic(JsonParser jp, DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException, JsonProcessingException {
        JsonDeserializer<Object> subDeser = _findSubclassDeserializer(ctxt, bean, unknownTokens);
        if (subDeser != null) {
            if (unknownTokens != null) {
                unknownTokens.writeEndObject();
                JsonParser p2 = unknownTokens.asParser();
                p2.nextToken();
                bean = subDeser.deserialize(p2, ctxt, bean);
            }
            if (jp != null) {
                bean = subDeser.deserialize(jp, ctxt, bean);
            }
            return bean;
        }
        if (unknownTokens != null) {
            bean = handleUnknownProperties(ctxt, bean, unknownTokens);
        }
        if (jp != null) {
            bean = deserialize(jp, ctxt, bean);
        }
        return bean;
    }

    protected JsonDeserializer<Object> _findSubclassDeserializer(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException, JsonProcessingException {
        synchronized (this) {
            JsonDeserializer<Object> subDeser = this._subDeserializers == null ? null : (JsonDeserializer) this._subDeserializers.get(new ClassKey(bean.getClass()));
        }
        if (subDeser != null) {
            return subDeser;
        }
        subDeser = ctxt.findRootValueDeserializer(ctxt.constructType(bean.getClass()));
        if (subDeser != null) {
            synchronized (this) {
                if (this._subDeserializers == null) {
                    this._subDeserializers = new HashMap();
                }
                this._subDeserializers.put(new ClassKey(bean.getClass()), subDeser);
            }
        }
        return subDeser;
    }

    public void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt) throws IOException {
        throw JsonMappingException.wrapWithPath(throwOrReturnThrowable(t, ctxt), bean, fieldName);
    }

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, int index, DeserializationContext ctxt) throws IOException {
        throw JsonMappingException.wrapWithPath(throwOrReturnThrowable(t, ctxt), bean, index);
    }

    private Throwable throwOrReturnThrowable(Throwable t, DeserializationContext ctxt) throws IOException {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = ctxt == null || ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            if (!(wrap && (t instanceof JsonProcessingException))) {
                throw ((IOException) t);
            }
        } else if (!wrap && (t instanceof RuntimeException)) {
            throw ((RuntimeException) t);
        }
        return t;
    }

    protected void wrapInstantiationProblem(Throwable t, DeserializationContext ctxt) throws IOException {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = ctxt == null || ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            throw ((IOException) t);
        } else if (wrap || !(t instanceof RuntimeException)) {
            throw ctxt.instantiationException(this._beanType.getRawClass(), t);
        } else {
            throw ((RuntimeException) t);
        }
    }
}
