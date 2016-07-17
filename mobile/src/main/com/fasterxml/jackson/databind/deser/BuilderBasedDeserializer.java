package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.impl.BeanAsArrayBuilderDeserializer;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.nianticproject.holoholo.sfida.constants.BluetoothGattSupport;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import spacemadness.com.lunarconsole.C1391R;

public class BuilderBasedDeserializer extends BeanDeserializerBase {
    private static final long serialVersionUID = 1;
    protected final AnnotatedMethod _buildMethod;

    /* renamed from: com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer.1 */
    static /* synthetic */ class C01311 {
        static final /* synthetic */ int[] $SwitchMap$com$fasterxml$jackson$core$JsonToken;

        static {
            $SwitchMap$com$fasterxml$jackson$core$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonToken[JsonToken.START_ARRAY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$core$JsonToken[JsonToken.END_OBJECT.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    public BuilderBasedDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews) {
        super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
        this._buildMethod = builder.getBuildMethod();
        if (this._objectIdReader != null) {
            throw new IllegalArgumentException("Can not use Object Id with Builder-based deserialization (type " + beanDesc.getType() + ")");
        }
    }

    protected BuilderBasedDeserializer(BuilderBasedDeserializer src) {
        this(src, src._ignoreAllUnknown);
    }

    protected BuilderBasedDeserializer(BuilderBasedDeserializer src, boolean ignoreAllUnknown) {
        super((BeanDeserializerBase) src, ignoreAllUnknown);
        this._buildMethod = src._buildMethod;
    }

    protected BuilderBasedDeserializer(BuilderBasedDeserializer src, NameTransformer unwrapper) {
        super((BeanDeserializerBase) src, unwrapper);
        this._buildMethod = src._buildMethod;
    }

    public BuilderBasedDeserializer(BuilderBasedDeserializer src, ObjectIdReader oir) {
        super((BeanDeserializerBase) src, oir);
        this._buildMethod = src._buildMethod;
    }

    public BuilderBasedDeserializer(BuilderBasedDeserializer src, HashSet<String> ignorableProps) {
        super((BeanDeserializerBase) src, (HashSet) ignorableProps);
        this._buildMethod = src._buildMethod;
    }

    public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper) {
        return new BuilderBasedDeserializer(this, unwrapper);
    }

    public BuilderBasedDeserializer withObjectIdReader(ObjectIdReader oir) {
        return new BuilderBasedDeserializer(this, oir);
    }

    public BuilderBasedDeserializer withIgnorableProperties(HashSet<String> ignorableProps) {
        return new BuilderBasedDeserializer(this, (HashSet) ignorableProps);
    }

    protected BeanAsArrayBuilderDeserializer asArrayDeserializer() {
        return new BeanAsArrayBuilderDeserializer(this, this._beanProperties.getPropertiesInInsertionOrder(), this._buildMethod);
    }

    protected final Object finishBuild(DeserializationContext ctxt, Object builder) throws IOException {
        if (this._buildMethod == null) {
            return builder;
        }
        try {
            return this._buildMethod.getMember().invoke(builder, new Object[0]);
        } catch (Exception e) {
            wrapInstantiationProblem(e, ctxt);
            return null;
        }
    }

    public final Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
            if (this._vanillaProcessing) {
                return finishBuild(ctxt, vanillaDeserialize(jp, ctxt, t));
            }
            return finishBuild(ctxt, deserializeFromObject(jp, ctxt));
        }
        switch (C01311.$SwitchMap$com$fasterxml$jackson$core$JsonToken[t.ordinal()]) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                return finishBuild(ctxt, deserializeFromString(jp, ctxt));
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                return finishBuild(ctxt, deserializeFromNumber(jp, ctxt));
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                return finishBuild(ctxt, deserializeFromDouble(jp, ctxt));
            case Place.TYPE_AQUARIUM /*4*/:
                return jp.getEmbeddedObject();
            case Place.TYPE_ART_GALLERY /*5*/:
            case Place.TYPE_ATM /*6*/:
                return finishBuild(ctxt, deserializeFromBoolean(jp, ctxt));
            case Place.TYPE_BAKERY /*7*/:
                return finishBuild(ctxt, deserializeFromArray(jp, ctxt));
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
            case Place.TYPE_BAR /*9*/:
                return finishBuild(ctxt, deserializeFromObject(jp, ctxt));
            default:
                throw ctxt.mappingException(handledType());
        }
    }

    public Object deserialize(JsonParser jp, DeserializationContext ctxt, Object builder) throws IOException, JsonProcessingException {
        return finishBuild(ctxt, _deserialize(jp, ctxt, builder));
    }

    protected final Object _deserialize(JsonParser jp, DeserializationContext ctxt, Object builder) throws IOException, JsonProcessingException {
        if (this._injectables != null) {
            injectValues(ctxt, builder);
        }
        if (this._unwrappedPropertyHandler != null) {
            return deserializeWithUnwrapped(jp, ctxt, builder);
        }
        if (this._externalTypeIdHandler != null) {
            return deserializeWithExternalTypeId(jp, ctxt, builder);
        }
        if (this._needViewProcesing) {
            Class<?> view = ctxt.getActiveView();
            if (view != null) {
                return deserializeWithView(jp, ctxt, builder, view);
            }
        }
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        while (t == JsonToken.FIELD_NAME) {
            String propName = jp.getCurrentName();
            jp.nextToken();
            SettableBeanProperty prop = this._beanProperties.find(propName);
            if (prop != null) {
                try {
                    builder = prop.deserializeSetAndReturn(jp, ctxt, builder);
                } catch (Exception e) {
                    wrapAndThrow((Throwable) e, builder, propName, ctxt);
                }
            } else {
                handleUnknownVanilla(jp, ctxt, handledType(), propName);
            }
            t = jp.nextToken();
        }
        return builder;
    }

    private final Object vanillaDeserialize(JsonParser jp, DeserializationContext ctxt, JsonToken t) throws IOException, JsonProcessingException {
        Object bean = this._valueInstantiator.createUsingDefault(ctxt);
        while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
            String propName = jp.getCurrentName();
            jp.nextToken();
            SettableBeanProperty prop = this._beanProperties.find(propName);
            if (prop != null) {
                try {
                    bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
                } catch (Exception e) {
                    wrapAndThrow((Throwable) e, bean, propName, ctxt);
                }
            } else {
                handleUnknownVanilla(jp, ctxt, bean, propName);
            }
            jp.nextToken();
        }
        return bean;
    }

    public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (!this._nonStandardCreation) {
            Object bean = this._valueInstantiator.createUsingDefault(ctxt);
            if (this._injectables != null) {
                injectValues(ctxt, bean);
            }
            if (this._needViewProcesing) {
                Class<?> view = ctxt.getActiveView();
                if (view != null) {
                    return deserializeWithView(jp, ctxt, bean, view);
                }
            }
            while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
                String propName = jp.getCurrentName();
                jp.nextToken();
                SettableBeanProperty prop = this._beanProperties.find(propName);
                if (prop != null) {
                    try {
                        bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
                    } catch (Exception e) {
                        wrapAndThrow((Throwable) e, bean, propName, ctxt);
                    }
                } else {
                    handleUnknownVanilla(jp, ctxt, bean, propName);
                }
                jp.nextToken();
            }
            return bean;
        } else if (this._unwrappedPropertyHandler != null) {
            return deserializeWithUnwrapped(jp, ctxt);
        } else {
            if (this._externalTypeIdHandler != null) {
                return deserializeWithExternalTypeId(jp, ctxt);
            }
            return deserializeFromObjectUsingNonDefault(jp, ctxt);
        }
    }

    protected final Object _deserializeUsingPropertyBased(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        PropertyBasedCreator creator = this._propertyBasedCreator;
        PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt, this._objectIdReader);
        TokenBuffer unknown = null;
        JsonToken t = jp.getCurrentToken();
        while (t == JsonToken.FIELD_NAME) {
            Object bean;
            String propName = jp.getCurrentName();
            jp.nextToken();
            SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
            if (creatorProp != null) {
                if (buffer.assignParameter(creatorProp, creatorProp.deserialize(jp, ctxt))) {
                    jp.nextToken();
                    try {
                        bean = creator.build(ctxt, buffer);
                        if (bean.getClass() != this._beanType.getRawClass()) {
                            return handlePolymorphic(jp, ctxt, bean, unknown);
                        }
                        if (unknown != null) {
                            bean = handleUnknownProperties(ctxt, bean, unknown);
                        }
                        return _deserialize(jp, ctxt, bean);
                    } catch (Exception e) {
                        wrapAndThrow((Throwable) e, (Object) this._beanType.getRawClass(), propName, ctxt);
                    }
                } else {
                    continue;
                }
            } else if (!buffer.readIdProperty(propName)) {
                SettableBeanProperty prop = this._beanProperties.find(propName);
                if (prop != null) {
                    buffer.bufferProperty(prop, prop.deserialize(jp, ctxt));
                } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                    handleIgnoredProperty(jp, ctxt, handledType(), propName);
                } else if (this._anySetter != null) {
                    buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(jp, ctxt));
                } else {
                    if (unknown == null) {
                        unknown = new TokenBuffer(jp, ctxt);
                    }
                    unknown.writeFieldName(propName);
                    unknown.copyCurrentStructure(jp);
                }
            }
            t = jp.nextToken();
        }
        try {
            bean = creator.build(ctxt, buffer);
            if (unknown == null) {
                return bean;
            }
            if (bean.getClass() != this._beanType.getRawClass()) {
                return handlePolymorphic(null, ctxt, bean, unknown);
            }
            return handleUnknownProperties(ctxt, bean, unknown);
        } catch (Exception e2) {
            wrapInstantiationProblem(e2, ctxt);
            return null;
        }
    }

    protected final Object deserializeWithView(JsonParser jp, DeserializationContext ctxt, Object bean, Class<?> activeView) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        while (t == JsonToken.FIELD_NAME) {
            String propName = jp.getCurrentName();
            jp.nextToken();
            SettableBeanProperty prop = this._beanProperties.find(propName);
            if (prop == null) {
                handleUnknownVanilla(jp, ctxt, bean, propName);
            } else if (prop.visibleInView(activeView)) {
                try {
                    bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
                } catch (Exception e) {
                    wrapAndThrow((Throwable) e, bean, propName, ctxt);
                }
            } else {
                jp.skipChildren();
            }
            t = jp.nextToken();
        }
        return bean;
    }

    protected Object deserializeWithUnwrapped(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._delegateDeserializer != null) {
            return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
        }
        if (this._propertyBasedCreator != null) {
            return deserializeUsingPropertyBasedWithUnwrapped(jp, ctxt);
        }
        TokenBuffer tokens = new TokenBuffer(jp, ctxt);
        tokens.writeStartObject();
        Object bean = this._valueInstantiator.createUsingDefault(ctxt);
        if (this._injectables != null) {
            injectValues(ctxt, bean);
        }
        Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
        while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
            String propName = jp.getCurrentName();
            jp.nextToken();
            SettableBeanProperty prop = this._beanProperties.find(propName);
            if (prop != null) {
                if (activeView == null || prop.visibleInView(activeView)) {
                    try {
                        bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
                    } catch (Exception e) {
                        wrapAndThrow((Throwable) e, bean, propName, ctxt);
                    }
                } else {
                    jp.skipChildren();
                }
            } else if (this._ignorableProps == null || !this._ignorableProps.contains(propName)) {
                tokens.writeFieldName(propName);
                tokens.copyCurrentStructure(jp);
                if (this._anySetter != null) {
                    try {
                        this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
                    } catch (Exception e2) {
                        wrapAndThrow((Throwable) e2, bean, propName, ctxt);
                    }
                }
            } else {
                handleIgnoredProperty(jp, ctxt, bean, propName);
            }
            jp.nextToken();
        }
        tokens.writeEndObject();
        this._unwrappedPropertyHandler.processUnwrapped(jp, ctxt, bean, tokens);
        return bean;
    }

    protected Object deserializeWithUnwrapped(JsonParser jp, DeserializationContext ctxt, Object bean) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        TokenBuffer tokens = new TokenBuffer(jp, ctxt);
        tokens.writeStartObject();
        Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
        while (t == JsonToken.FIELD_NAME) {
            String propName = jp.getCurrentName();
            SettableBeanProperty prop = this._beanProperties.find(propName);
            jp.nextToken();
            if (prop != null) {
                if (activeView == null || prop.visibleInView(activeView)) {
                    try {
                        bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
                    } catch (Exception e) {
                        wrapAndThrow((Throwable) e, bean, propName, ctxt);
                    }
                } else {
                    jp.skipChildren();
                }
            } else if (this._ignorableProps == null || !this._ignorableProps.contains(propName)) {
                tokens.writeFieldName(propName);
                tokens.copyCurrentStructure(jp);
                if (this._anySetter != null) {
                    this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
                }
            } else {
                handleIgnoredProperty(jp, ctxt, bean, propName);
            }
            t = jp.nextToken();
        }
        tokens.writeEndObject();
        this._unwrappedPropertyHandler.processUnwrapped(jp, ctxt, bean, tokens);
        return bean;
    }

    protected Object deserializeUsingPropertyBasedWithUnwrapped(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        PropertyBasedCreator creator = this._propertyBasedCreator;
        PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
        TokenBuffer tokens = new TokenBuffer(p, ctxt);
        tokens.writeStartObject();
        JsonToken t = p.getCurrentToken();
        while (t == JsonToken.FIELD_NAME) {
            String propName = p.getCurrentName();
            p.nextToken();
            SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
            if (creatorProp != null) {
                if (buffer.assignParameter(creatorProp, creatorProp.deserialize(p, ctxt))) {
                    t = p.nextToken();
                    try {
                        Object bean = creator.build(ctxt, buffer);
                        while (t == JsonToken.FIELD_NAME) {
                            p.nextToken();
                            tokens.copyCurrentStructure(p);
                            t = p.nextToken();
                        }
                        tokens.writeEndObject();
                        if (bean.getClass() == this._beanType.getRawClass()) {
                            return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
                        }
                        throw ctxt.mappingException("Can not create polymorphic instances with unwrapped values");
                    } catch (Exception e) {
                        wrapAndThrow((Throwable) e, (Object) this._beanType.getRawClass(), propName, ctxt);
                    }
                } else {
                    continue;
                }
            } else if (!buffer.readIdProperty(propName)) {
                SettableBeanProperty prop = this._beanProperties.find(propName);
                if (prop != null) {
                    buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
                } else if (this._ignorableProps == null || !this._ignorableProps.contains(propName)) {
                    tokens.writeFieldName(propName);
                    tokens.copyCurrentStructure(p);
                    if (this._anySetter != null) {
                        buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(p, ctxt));
                    }
                } else {
                    handleIgnoredProperty(p, ctxt, handledType(), propName);
                }
            }
            t = p.nextToken();
        }
        try {
            return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, creator.build(ctxt, buffer), tokens);
        } catch (Exception e2) {
            wrapInstantiationProblem(e2, ctxt);
            return null;
        }
    }

    protected Object deserializeWithExternalTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._propertyBasedCreator != null) {
            return deserializeUsingPropertyBasedWithExternalTypeId(jp, ctxt);
        }
        return deserializeWithExternalTypeId(jp, ctxt, this._valueInstantiator.createUsingDefault(ctxt));
    }

    protected Object deserializeWithExternalTypeId(JsonParser jp, DeserializationContext ctxt, Object bean) throws IOException, JsonProcessingException {
        Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
        ExternalTypeHandler ext = this._externalTypeIdHandler.start();
        while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
            String propName = jp.getCurrentName();
            jp.nextToken();
            SettableBeanProperty prop = this._beanProperties.find(propName);
            if (prop != null) {
                if (activeView == null || prop.visibleInView(activeView)) {
                    try {
                        bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
                    } catch (Exception e) {
                        wrapAndThrow((Throwable) e, bean, propName, ctxt);
                    }
                } else {
                    jp.skipChildren();
                }
            } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                handleIgnoredProperty(jp, ctxt, bean, propName);
            } else if (!ext.handlePropertyValue(jp, ctxt, propName, bean)) {
                if (this._anySetter != null) {
                    try {
                        this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
                    } catch (Exception e2) {
                        wrapAndThrow((Throwable) e2, bean, propName, ctxt);
                    }
                } else {
                    handleUnknownProperty(jp, ctxt, bean, propName);
                }
            }
            jp.nextToken();
        }
        return ext.complete(jp, ctxt, bean);
    }

    protected Object deserializeUsingPropertyBasedWithExternalTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        throw new IllegalStateException("Deserialization with Builder, External type id, @JsonCreator not yet implemented");
    }
}
