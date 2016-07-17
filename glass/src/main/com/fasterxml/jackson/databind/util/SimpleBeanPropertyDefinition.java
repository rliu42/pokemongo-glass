package com.fasterxml.jackson.databind.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import java.util.Collections;
import java.util.Iterator;

public class SimpleBeanPropertyDefinition extends BeanPropertyDefinition {
    protected final PropertyName _fullName;
    protected final Include _inclusion;
    protected final AnnotationIntrospector _introspector;
    protected final AnnotatedMember _member;
    protected final PropertyMetadata _metadata;
    @Deprecated
    protected final String _name;

    @Deprecated
    public SimpleBeanPropertyDefinition(AnnotatedMember member) {
        this(member, member.getName(), null);
    }

    @Deprecated
    public SimpleBeanPropertyDefinition(AnnotatedMember member, String name) {
        this(member, new PropertyName(name), null, null, null);
    }

    protected SimpleBeanPropertyDefinition(AnnotatedMember member, PropertyName fullName, AnnotationIntrospector intr, PropertyMetadata metadata, Include inclusion) {
        this._introspector = intr;
        this._member = member;
        this._fullName = fullName;
        this._name = fullName.getSimpleName();
        if (metadata == null) {
            metadata = PropertyMetadata.STD_OPTIONAL;
        }
        this._metadata = metadata;
        this._inclusion = inclusion;
    }

    @Deprecated
    protected SimpleBeanPropertyDefinition(AnnotatedMember member, String name, AnnotationIntrospector intr) {
        this(member, new PropertyName(name), intr, null, null);
    }

    public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member) {
        return new SimpleBeanPropertyDefinition(member, PropertyName.construct(member.getName()), config == null ? null : config.getAnnotationIntrospector(), null, null);
    }

    @Deprecated
    public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member, String name) {
        return new SimpleBeanPropertyDefinition(member, PropertyName.construct(name), config == null ? null : config.getAnnotationIntrospector(), null, null);
    }

    public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member, PropertyName name) {
        return construct(config, member, name, null, null);
    }

    public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member, PropertyName name, PropertyMetadata metadata, Include inclusion) {
        return new SimpleBeanPropertyDefinition(member, name, config == null ? null : config.getAnnotationIntrospector(), metadata, inclusion);
    }

    @Deprecated
    public BeanPropertyDefinition withName(String newName) {
        return withSimpleName(newName);
    }

    public BeanPropertyDefinition withSimpleName(String newName) {
        return (!this._fullName.hasSimpleName(newName) || this._fullName.hasNamespace()) ? new SimpleBeanPropertyDefinition(this._member, new PropertyName(newName), this._introspector, this._metadata, this._inclusion) : this;
    }

    public BeanPropertyDefinition withName(PropertyName newName) {
        if (this._fullName.equals(newName)) {
            return this;
        }
        return new SimpleBeanPropertyDefinition(this._member, newName, this._introspector, this._metadata, this._inclusion);
    }

    public BeanPropertyDefinition withMetadata(PropertyMetadata metadata) {
        if (metadata.equals(this._metadata)) {
            return this;
        }
        return new SimpleBeanPropertyDefinition(this._member, this._fullName, this._introspector, metadata, this._inclusion);
    }

    public BeanPropertyDefinition withInclusion(Include inclusion) {
        return this._inclusion == inclusion ? this : new SimpleBeanPropertyDefinition(this._member, this._fullName, this._introspector, this._metadata, inclusion);
    }

    public String getName() {
        return this._fullName.getSimpleName();
    }

    public PropertyName getFullName() {
        return this._fullName;
    }

    public boolean hasName(PropertyName name) {
        return this._fullName.equals(name);
    }

    public String getInternalName() {
        return getName();
    }

    public PropertyName getWrapperName() {
        return (this._introspector != null || this._member == null) ? this._introspector.findWrapperName(this._member) : null;
    }

    public boolean isExplicitlyIncluded() {
        return false;
    }

    public boolean isExplicitlyNamed() {
        return false;
    }

    public PropertyMetadata getMetadata() {
        return this._metadata;
    }

    public Include findInclusion() {
        return this._inclusion;
    }

    public boolean hasGetter() {
        return getGetter() != null;
    }

    public boolean hasSetter() {
        return getSetter() != null;
    }

    public boolean hasField() {
        return this._member instanceof AnnotatedField;
    }

    public boolean hasConstructorParameter() {
        return this._member instanceof AnnotatedParameter;
    }

    public AnnotatedMethod getGetter() {
        if ((this._member instanceof AnnotatedMethod) && ((AnnotatedMethod) this._member).getParameterCount() == 0) {
            return (AnnotatedMethod) this._member;
        }
        return null;
    }

    public AnnotatedMethod getSetter() {
        if ((this._member instanceof AnnotatedMethod) && ((AnnotatedMethod) this._member).getParameterCount() == 1) {
            return (AnnotatedMethod) this._member;
        }
        return null;
    }

    public AnnotatedField getField() {
        return this._member instanceof AnnotatedField ? (AnnotatedField) this._member : null;
    }

    public AnnotatedParameter getConstructorParameter() {
        return this._member instanceof AnnotatedParameter ? (AnnotatedParameter) this._member : null;
    }

    public Iterator<AnnotatedParameter> getConstructorParameters() {
        AnnotatedParameter param = getConstructorParameter();
        if (param == null) {
            return EmptyIterator.instance();
        }
        return Collections.singleton(param).iterator();
    }

    public AnnotatedMember getAccessor() {
        AnnotatedMember acc = getGetter();
        if (acc == null) {
            return getField();
        }
        return acc;
    }

    public AnnotatedMember getMutator() {
        AnnotatedMember acc = getConstructorParameter();
        if (acc != null) {
            return acc;
        }
        acc = getSetter();
        if (acc == null) {
            return getField();
        }
        return acc;
    }

    public AnnotatedMember getNonConstructorMutator() {
        AnnotatedMember acc = getSetter();
        if (acc == null) {
            return getField();
        }
        return acc;
    }

    public AnnotatedMember getPrimaryMember() {
        return this._member;
    }
}
