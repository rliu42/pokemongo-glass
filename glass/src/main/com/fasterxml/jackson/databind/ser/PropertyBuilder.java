package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import spacemadness.com.lunarconsole.C1391R;

public class PropertyBuilder {
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final BeanDescription _beanDesc;
    protected final SerializationConfig _config;
    protected Object _defaultBean;
    protected final Include _defaultInclusion;

    /* renamed from: com.fasterxml.jackson.databind.ser.PropertyBuilder.1 */
    static /* synthetic */ class C01501 {
        static final /* synthetic */ int[] $SwitchMap$com$fasterxml$jackson$annotation$JsonInclude$Include;

        static {
            $SwitchMap$com$fasterxml$jackson$annotation$JsonInclude$Include = new int[Include.values().length];
            try {
                $SwitchMap$com$fasterxml$jackson$annotation$JsonInclude$Include[Include.NON_DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$annotation$JsonInclude$Include[Include.NON_ABSENT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$annotation$JsonInclude$Include[Include.NON_EMPTY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$annotation$JsonInclude$Include[Include.NON_NULL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$annotation$JsonInclude$Include[Include.ALWAYS.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public PropertyBuilder(SerializationConfig config, BeanDescription beanDesc) {
        this._config = config;
        this._beanDesc = beanDesc;
        this._defaultInclusion = beanDesc.findSerializationInclusion(config.getSerializationInclusion());
        this._annotationIntrospector = this._config.getAnnotationIntrospector();
    }

    public Annotations getClassAnnotations() {
        return this._beanDesc.getClassAnnotations();
    }

    protected BeanPropertyWriter buildWriter(SerializerProvider prov, BeanPropertyDefinition propDef, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotatedMember am, boolean defaultUseStaticTyping) throws JsonMappingException {
        JavaType serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);
        if (contentTypeSer != null) {
            if (serializationType == null) {
                serializationType = declaredType;
            }
            if (serializationType.getContentType() == null) {
                throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '" + propDef.getName() + "' (of type " + this._beanDesc.getType() + "); serialization type " + serializationType + " has no content");
            }
            serializationType = serializationType.withContentTypeHandler(contentTypeSer);
            serializationType.getContentType();
        }
        Object valueToSuppress = null;
        boolean suppressNulls = false;
        Include inclusion = propDef.findInclusion();
        if (inclusion == null || inclusion == Include.USE_DEFAULTS) {
            inclusion = this._defaultInclusion;
            if (inclusion == null) {
                inclusion = Include.ALWAYS;
            }
        }
        switch (C01501.$SwitchMap$com$fasterxml$jackson$annotation$JsonInclude$Include[inclusion.ordinal()]) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                valueToSuppress = getDefaultValue(propDef.getName(), am);
                if (valueToSuppress != null) {
                    if (valueToSuppress.getClass().isArray()) {
                        valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
                        break;
                    }
                }
                suppressNulls = true;
                break;
                break;
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                suppressNulls = true;
                if (declaredType.isReferenceType()) {
                    valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
                    break;
                }
                break;
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                suppressNulls = true;
                valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
                break;
            case Place.TYPE_AQUARIUM /*4*/:
                suppressNulls = true;
                break;
        }
        if (declaredType.isContainerType() && !this._config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)) {
            valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
        }
        BeanPropertyWriter bpw = new BeanPropertyWriter(propDef, am, this._beanDesc.getClassAnnotations(), declaredType, ser, typeSer, serializationType, suppressNulls, valueToSuppress);
        Object serDef = this._annotationIntrospector.findNullSerializer(am);
        if (serDef != null) {
            bpw.assignNullSerializer(prov.serializerInstance(am, serDef));
        }
        NameTransformer unwrapper = this._annotationIntrospector.findUnwrappingNameTransformer(am);
        if (unwrapper != null) {
            return bpw.unwrappingWriter(unwrapper);
        }
        return bpw;
    }

    protected JavaType findSerializationType(Annotated a, boolean useStaticTyping, JavaType declaredType) {
        Class<?> serClass = this._annotationIntrospector.findSerializationType(a);
        if (serClass != null) {
            Class<?> rawDeclared = declaredType.getRawClass();
            if (serClass.isAssignableFrom(rawDeclared)) {
                declaredType = declaredType.widenBy(serClass);
            } else if (rawDeclared.isAssignableFrom(serClass)) {
                declaredType = this._config.constructSpecializedType(declaredType, serClass);
            } else {
                throw new IllegalArgumentException("Illegal concrete-type annotation for method '" + a.getName() + "': class " + serClass.getName() + " not a super-type of (declared) class " + rawDeclared.getName());
            }
            useStaticTyping = true;
        }
        JavaType secondary = BasicSerializerFactory.modifySecondaryTypesByAnnotation(this._config, a, declaredType);
        if (secondary != declaredType) {
            useStaticTyping = true;
            declaredType = secondary;
        }
        Typing typing = this._annotationIntrospector.findSerializationTyping(a);
        if (!(typing == null || typing == Typing.DEFAULT_TYPING)) {
            useStaticTyping = typing == Typing.STATIC;
        }
        return useStaticTyping ? declaredType : null;
    }

    protected Object getDefaultBean() {
        if (this._defaultBean == null) {
            this._defaultBean = this._beanDesc.instantiateBean(this._config.canOverrideAccessModifiers());
            if (this._defaultBean == null) {
                throw new IllegalArgumentException("Class " + this._beanDesc.getClassInfo().getAnnotated().getName() + " has no default constructor; can not instantiate default bean value to support 'properties=JsonSerialize.Inclusion.NON_DEFAULT' annotation");
            }
        }
        return this._defaultBean;
    }

    protected Object getDefaultValue(String name, AnnotatedMember member) {
        Object defaultBean = getDefaultBean();
        try {
            return member.getValue(defaultBean);
        } catch (Exception e) {
            return _throwWrapped(e, name, defaultBean);
        }
    }

    protected Object _throwWrapped(Exception e, String propName, Object defaultBean) {
        Throwable t = e;
        while (t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        } else if (t instanceof RuntimeException) {
            throw ((RuntimeException) t);
        } else {
            throw new IllegalArgumentException("Failed to get property '" + propName + "' of default " + defaultBean.getClass().getName() + " instance");
        }
    }
}
