package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.Named;
import java.lang.annotation.Annotation;

public interface BeanProperty extends Named {

    public static class Std implements BeanProperty {
        protected final Annotations _contextAnnotations;
        protected final AnnotatedMember _member;
        protected final PropertyMetadata _metadata;
        protected final PropertyName _name;
        protected final JavaType _type;
        protected final PropertyName _wrapperName;

        public Std(PropertyName name, JavaType type, PropertyName wrapperName, Annotations contextAnnotations, AnnotatedMember member, PropertyMetadata metadata) {
            this._name = name;
            this._type = type;
            this._wrapperName = wrapperName;
            this._metadata = metadata;
            this._member = member;
            this._contextAnnotations = contextAnnotations;
        }

        public Std(Std base, JavaType newType) {
            this(base._name, newType, base._wrapperName, base._contextAnnotations, base._member, base._metadata);
        }

        @Deprecated
        public Std(String name, JavaType type, PropertyName wrapperName, Annotations contextAnnotations, AnnotatedMember member, boolean isRequired) {
            this(new PropertyName(name), type, wrapperName, contextAnnotations, member, isRequired ? PropertyMetadata.STD_REQUIRED : PropertyMetadata.STD_OPTIONAL);
        }

        public Std withType(JavaType type) {
            return new Std(this, type);
        }

        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return this._member == null ? null : this._member.getAnnotation(acls);
        }

        public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
            return this._contextAnnotations == null ? null : this._contextAnnotations.get(acls);
        }

        public Value findFormatOverrides(AnnotationIntrospector intr) {
            return null;
        }

        public String getName() {
            return this._name.getSimpleName();
        }

        public PropertyName getFullName() {
            return this._name;
        }

        public JavaType getType() {
            return this._type;
        }

        public PropertyName getWrapperName() {
            return this._wrapperName;
        }

        public boolean isRequired() {
            return this._metadata.isRequired();
        }

        public PropertyMetadata getMetadata() {
            return this._metadata;
        }

        public AnnotatedMember getMember() {
            return this._member;
        }

        public boolean isVirtual() {
            return false;
        }

        public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor) {
            throw new UnsupportedOperationException("Instances of " + getClass().getName() + " should not get visited");
        }
    }

    void depositSchemaProperty(JsonObjectFormatVisitor jsonObjectFormatVisitor) throws JsonMappingException;

    Value findFormatOverrides(AnnotationIntrospector annotationIntrospector);

    <A extends Annotation> A getAnnotation(Class<A> cls);

    <A extends Annotation> A getContextAnnotation(Class<A> cls);

    PropertyName getFullName();

    AnnotatedMember getMember();

    PropertyMetadata getMetadata();

    String getName();

    JavaType getType();

    PropertyName getWrapperName();

    boolean isRequired();
}
