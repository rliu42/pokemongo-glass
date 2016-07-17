package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.impl.FailingDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ViewMatcher;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;

public abstract class SettableBeanProperty implements BeanProperty, Serializable {
    protected static final JsonDeserializer<Object> MISSING_VALUE_DESERIALIZER;
    protected final transient Annotations _contextAnnotations;
    protected String _managedReferenceName;
    protected final PropertyMetadata _metadata;
    protected ObjectIdInfo _objectIdInfo;
    protected final PropertyName _propName;
    protected int _propertyIndex;
    protected final JavaType _type;
    protected final JsonDeserializer<Object> _valueDeserializer;
    protected final TypeDeserializer _valueTypeDeserializer;
    protected ViewMatcher _viewMatcher;
    protected final PropertyName _wrapperName;

    public abstract void deserializeAndSet(JsonParser jsonParser, DeserializationContext deserializationContext, Object obj) throws IOException;

    public abstract Object deserializeSetAndReturn(JsonParser jsonParser, DeserializationContext deserializationContext, Object obj) throws IOException;

    public abstract <A extends Annotation> A getAnnotation(Class<A> cls);

    public abstract AnnotatedMember getMember();

    public abstract void set(Object obj, Object obj2) throws IOException;

    public abstract Object setAndReturn(Object obj, Object obj2) throws IOException;

    public abstract SettableBeanProperty withName(PropertyName propertyName);

    public abstract SettableBeanProperty withValueDeserializer(JsonDeserializer<?> jsonDeserializer);

    static {
        MISSING_VALUE_DESERIALIZER = new FailingDeserializer("No _valueDeserializer assigned");
    }

    protected SettableBeanProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations) {
        this(propDef.getFullName(), type, propDef.getWrapperName(), typeDeser, contextAnnotations, propDef.getMetadata());
    }

    @Deprecated
    protected SettableBeanProperty(String propName, JavaType type, PropertyName wrapper, TypeDeserializer typeDeser, Annotations contextAnnotations, boolean isRequired) {
        this(new PropertyName(propName), type, wrapper, typeDeser, contextAnnotations, PropertyMetadata.construct(isRequired, null, null, null));
    }

    protected SettableBeanProperty(PropertyName propName, JavaType type, PropertyName wrapper, TypeDeserializer typeDeser, Annotations contextAnnotations, PropertyMetadata metadata) {
        this._propertyIndex = -1;
        if (propName == null) {
            this._propName = PropertyName.NO_NAME;
        } else {
            this._propName = propName.internSimpleName();
        }
        this._type = type;
        this._wrapperName = wrapper;
        this._metadata = metadata;
        this._contextAnnotations = contextAnnotations;
        this._viewMatcher = null;
        if (typeDeser != null) {
            typeDeser = typeDeser.forProperty(this);
        }
        this._valueTypeDeserializer = typeDeser;
        this._valueDeserializer = MISSING_VALUE_DESERIALIZER;
    }

    protected SettableBeanProperty(PropertyName propName, JavaType type, PropertyMetadata metadata, JsonDeserializer<Object> valueDeser) {
        this._propertyIndex = -1;
        if (propName == null) {
            this._propName = PropertyName.NO_NAME;
        } else {
            this._propName = propName.internSimpleName();
        }
        this._type = type;
        this._wrapperName = null;
        this._metadata = metadata;
        this._contextAnnotations = null;
        this._viewMatcher = null;
        this._valueTypeDeserializer = null;
        this._valueDeserializer = valueDeser;
    }

    protected SettableBeanProperty(SettableBeanProperty src) {
        this._propertyIndex = -1;
        this._propName = src._propName;
        this._type = src._type;
        this._wrapperName = src._wrapperName;
        this._metadata = src._metadata;
        this._contextAnnotations = src._contextAnnotations;
        this._valueDeserializer = src._valueDeserializer;
        this._valueTypeDeserializer = src._valueTypeDeserializer;
        this._managedReferenceName = src._managedReferenceName;
        this._propertyIndex = src._propertyIndex;
        this._viewMatcher = src._viewMatcher;
    }

    protected SettableBeanProperty(SettableBeanProperty src, JsonDeserializer<?> deser) {
        this._propertyIndex = -1;
        this._propName = src._propName;
        this._type = src._type;
        this._wrapperName = src._wrapperName;
        this._metadata = src._metadata;
        this._contextAnnotations = src._contextAnnotations;
        this._valueTypeDeserializer = src._valueTypeDeserializer;
        this._managedReferenceName = src._managedReferenceName;
        this._propertyIndex = src._propertyIndex;
        if (deser == null) {
            this._valueDeserializer = MISSING_VALUE_DESERIALIZER;
        } else {
            this._valueDeserializer = deser;
        }
        this._viewMatcher = src._viewMatcher;
    }

    @Deprecated
    protected SettableBeanProperty(SettableBeanProperty src, String newName) {
        this(src, new PropertyName(newName));
    }

    protected SettableBeanProperty(SettableBeanProperty src, PropertyName newName) {
        this._propertyIndex = -1;
        this._propName = newName;
        this._type = src._type;
        this._wrapperName = src._wrapperName;
        this._metadata = src._metadata;
        this._contextAnnotations = src._contextAnnotations;
        this._valueDeserializer = src._valueDeserializer;
        this._valueTypeDeserializer = src._valueTypeDeserializer;
        this._managedReferenceName = src._managedReferenceName;
        this._propertyIndex = src._propertyIndex;
        this._viewMatcher = src._viewMatcher;
    }

    public SettableBeanProperty withSimpleName(String simpleName) {
        PropertyName n = this._propName == null ? new PropertyName(simpleName) : this._propName.withSimpleName(simpleName);
        return n == this._propName ? this : withName(n);
    }

    @Deprecated
    public SettableBeanProperty withName(String simpleName) {
        return withName(new PropertyName(simpleName));
    }

    public void setManagedReferenceName(String n) {
        this._managedReferenceName = n;
    }

    public void setObjectIdInfo(ObjectIdInfo objectIdInfo) {
        this._objectIdInfo = objectIdInfo;
    }

    public void setViews(Class<?>[] views) {
        if (views == null) {
            this._viewMatcher = null;
        } else {
            this._viewMatcher = ViewMatcher.construct(views);
        }
    }

    public void assignIndex(int index) {
        if (this._propertyIndex != -1) {
            throw new IllegalStateException("Property '" + getName() + "' already had index (" + this._propertyIndex + "), trying to assign " + index);
        }
        this._propertyIndex = index;
    }

    public final String getName() {
        return this._propName.getSimpleName();
    }

    public PropertyName getFullName() {
        return this._propName;
    }

    public boolean isRequired() {
        return this._metadata.isRequired();
    }

    public PropertyMetadata getMetadata() {
        return this._metadata;
    }

    public JavaType getType() {
        return this._type;
    }

    public PropertyName getWrapperName() {
        return this._wrapperName;
    }

    public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
        return this._contextAnnotations.get(acls);
    }

    public Value findFormatOverrides(AnnotationIntrospector intr) {
        if (intr != null) {
            AnnotatedMember m = getMember();
            if (m != null) {
                return intr.findFormat(m);
            }
        }
        return null;
    }

    public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor) throws JsonMappingException {
        if (isRequired()) {
            objectVisitor.property(this);
        } else {
            objectVisitor.optionalProperty(this);
        }
    }

    protected final Class<?> getDeclaringClass() {
        return getMember().getDeclaringClass();
    }

    public String getManagedReferenceName() {
        return this._managedReferenceName;
    }

    public ObjectIdInfo getObjectIdInfo() {
        return this._objectIdInfo;
    }

    public boolean hasValueDeserializer() {
        return (this._valueDeserializer == null || this._valueDeserializer == MISSING_VALUE_DESERIALIZER) ? false : true;
    }

    public boolean hasValueTypeDeserializer() {
        return this._valueTypeDeserializer != null;
    }

    public JsonDeserializer<Object> getValueDeserializer() {
        JsonDeserializer<Object> deser = this._valueDeserializer;
        if (deser == MISSING_VALUE_DESERIALIZER) {
            return null;
        }
        return deser;
    }

    public TypeDeserializer getValueTypeDeserializer() {
        return this._valueTypeDeserializer;
    }

    public boolean visibleInView(Class<?> activeView) {
        return this._viewMatcher == null || this._viewMatcher.isVisibleForView(activeView);
    }

    public boolean hasViews() {
        return this._viewMatcher != null;
    }

    public int getPropertyIndex() {
        return this._propertyIndex;
    }

    public int getCreatorIndex() {
        return -1;
    }

    public Object getInjectableValueId() {
        return null;
    }

    public final Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.getCurrentToken() == JsonToken.VALUE_NULL) {
            return this._valueDeserializer.getNullValue(ctxt);
        }
        if (this._valueTypeDeserializer != null) {
            return this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
        }
        return this._valueDeserializer.deserialize(p, ctxt);
    }

    protected void _throwAsIOE(Exception e, Object value) throws IOException {
        if (e instanceof IllegalArgumentException) {
            String actType = value == null ? "[NULL]" : value.getClass().getName();
            StringBuilder msg = new StringBuilder("Problem deserializing property '").append(getName());
            msg.append("' (expected type: ").append(getType());
            msg.append("; actual type: ").append(actType).append(")");
            String origMsg = e.getMessage();
            if (origMsg != null) {
                msg.append(", problem: ").append(origMsg);
            } else {
                msg.append(" (no error message provided)");
            }
            throw new JsonMappingException(msg.toString(), null, e);
        }
        _throwAsIOE(e);
    }

    protected IOException _throwAsIOE(Exception e) throws IOException {
        if (e instanceof IOException) {
            throw ((IOException) e);
        } else if (e instanceof RuntimeException) {
            throw ((RuntimeException) e);
        } else {
            Throwable th = e;
            while (th.getCause() != null) {
                th = th.getCause();
            }
            throw new JsonMappingException(th.getMessage(), null, th);
        }
    }

    public String toString() {
        return "[property '" + getName() + "']";
    }
}
