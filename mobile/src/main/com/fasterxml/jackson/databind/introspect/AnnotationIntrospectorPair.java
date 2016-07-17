package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer.None;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnnotationIntrospectorPair extends AnnotationIntrospector implements Serializable {
    private static final long serialVersionUID = 1;
    protected final AnnotationIntrospector _primary;
    protected final AnnotationIntrospector _secondary;

    public AnnotationIntrospectorPair(AnnotationIntrospector p, AnnotationIntrospector s) {
        this._primary = p;
        this._secondary = s;
    }

    public Version version() {
        return this._primary.version();
    }

    public static AnnotationIntrospector create(AnnotationIntrospector primary, AnnotationIntrospector secondary) {
        if (primary == null) {
            return secondary;
        }
        if (secondary == null) {
            return primary;
        }
        return new AnnotationIntrospectorPair(primary, secondary);
    }

    public Collection<AnnotationIntrospector> allIntrospectors() {
        return allIntrospectors(new ArrayList());
    }

    public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> result) {
        this._primary.allIntrospectors(result);
        this._secondary.allIntrospectors(result);
        return result;
    }

    public boolean isAnnotationBundle(Annotation ann) {
        return this._primary.isAnnotationBundle(ann) || this._secondary.isAnnotationBundle(ann);
    }

    public PropertyName findRootName(AnnotatedClass ac) {
        PropertyName name1 = this._primary.findRootName(ac);
        if (name1 == null) {
            return this._secondary.findRootName(ac);
        }
        if (name1.hasSimpleName()) {
            return name1;
        }
        PropertyName name2 = this._secondary.findRootName(ac);
        return name2 != null ? name2 : name1;
    }

    @Deprecated
    public String[] findPropertiesToIgnore(Annotated ac) {
        String[] result = this._primary.findPropertiesToIgnore(ac);
        if (result == null) {
            return this._secondary.findPropertiesToIgnore(ac);
        }
        return result;
    }

    public String[] findPropertiesToIgnore(Annotated ac, boolean forSerialization) {
        String[] result = this._primary.findPropertiesToIgnore(ac, forSerialization);
        if (result == null) {
            return this._secondary.findPropertiesToIgnore(ac, forSerialization);
        }
        return result;
    }

    public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
        Boolean result = this._primary.findIgnoreUnknownProperties(ac);
        if (result == null) {
            return this._secondary.findIgnoreUnknownProperties(ac);
        }
        return result;
    }

    public Boolean isIgnorableType(AnnotatedClass ac) {
        Boolean result = this._primary.isIgnorableType(ac);
        if (result == null) {
            return this._secondary.isIgnorableType(ac);
        }
        return result;
    }

    @Deprecated
    public Object findFilterId(AnnotatedClass ac) {
        Object id = this._primary.findFilterId(ac);
        if (id == null) {
            return this._secondary.findFilterId(ac);
        }
        return id;
    }

    public Object findFilterId(Annotated ann) {
        Object id = this._primary.findFilterId(ann);
        if (id == null) {
            return this._secondary.findFilterId(ann);
        }
        return id;
    }

    public Object findNamingStrategy(AnnotatedClass ac) {
        Object str = this._primary.findNamingStrategy(ac);
        if (str == null) {
            return this._secondary.findNamingStrategy(ac);
        }
        return str;
    }

    public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker) {
        return this._primary.findAutoDetectVisibility(ac, this._secondary.findAutoDetectVisibility(ac, checker));
    }

    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType) {
        TypeResolverBuilder<?> b = this._primary.findTypeResolver(config, ac, baseType);
        if (b == null) {
            return this._secondary.findTypeResolver(config, ac, baseType);
        }
        return b;
    }

    public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType) {
        TypeResolverBuilder<?> b = this._primary.findPropertyTypeResolver(config, am, baseType);
        if (b == null) {
            return this._secondary.findPropertyTypeResolver(config, am, baseType);
        }
        return b;
    }

    public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType) {
        TypeResolverBuilder<?> b = this._primary.findPropertyContentTypeResolver(config, am, baseType);
        if (b == null) {
            return this._secondary.findPropertyContentTypeResolver(config, am, baseType);
        }
        return b;
    }

    public List<NamedType> findSubtypes(Annotated a) {
        List<NamedType> types1 = this._primary.findSubtypes(a);
        List<NamedType> types2 = this._secondary.findSubtypes(a);
        if (types1 == null || types1.isEmpty()) {
            return types2;
        }
        if (types2 == null || types2.isEmpty()) {
            return types1;
        }
        List<NamedType> result = new ArrayList(types1.size() + types2.size());
        result.addAll(types1);
        result.addAll(types2);
        return result;
    }

    public String findTypeName(AnnotatedClass ac) {
        String name = this._primary.findTypeName(ac);
        if (name == null || name.length() == 0) {
            return this._secondary.findTypeName(ac);
        }
        return name;
    }

    public ReferenceProperty findReferenceType(AnnotatedMember member) {
        ReferenceProperty r = this._primary.findReferenceType(member);
        return r == null ? this._secondary.findReferenceType(member) : r;
    }

    public NameTransformer findUnwrappingNameTransformer(AnnotatedMember member) {
        NameTransformer r = this._primary.findUnwrappingNameTransformer(member);
        return r == null ? this._secondary.findUnwrappingNameTransformer(member) : r;
    }

    public Object findInjectableValueId(AnnotatedMember m) {
        Object r = this._primary.findInjectableValueId(m);
        return r == null ? this._secondary.findInjectableValueId(m) : r;
    }

    public boolean hasIgnoreMarker(AnnotatedMember m) {
        return this._primary.hasIgnoreMarker(m) || this._secondary.hasIgnoreMarker(m);
    }

    public Boolean hasRequiredMarker(AnnotatedMember m) {
        Boolean r = this._primary.hasRequiredMarker(m);
        return r == null ? this._secondary.hasRequiredMarker(m) : r;
    }

    public Object findSerializer(Annotated am) {
        Object r = this._primary.findSerializer(am);
        return _isExplicitClassOrOb(r, None.class) ? r : this._secondary.findSerializer(am);
    }

    public Object findKeySerializer(Annotated a) {
        Object r = this._primary.findKeySerializer(a);
        return _isExplicitClassOrOb(r, None.class) ? r : this._secondary.findKeySerializer(a);
    }

    public Object findContentSerializer(Annotated a) {
        Object r = this._primary.findContentSerializer(a);
        return _isExplicitClassOrOb(r, None.class) ? r : this._secondary.findContentSerializer(a);
    }

    public Object findNullSerializer(Annotated a) {
        Object r = this._primary.findNullSerializer(a);
        return _isExplicitClassOrOb(r, None.class) ? r : this._secondary.findNullSerializer(a);
    }

    public Include findSerializationInclusion(Annotated a, Include defValue) {
        return this._primary.findSerializationInclusion(a, this._secondary.findSerializationInclusion(a, defValue));
    }

    public Include findSerializationInclusionForContent(Annotated a, Include defValue) {
        return this._primary.findSerializationInclusion(a, this._secondary.findSerializationInclusion(a, defValue));
    }

    public Value findPropertyInclusion(Annotated a) {
        Value v2 = this._secondary.findPropertyInclusion(a);
        Value v1 = this._secondary.findPropertyInclusion(a);
        return v2 == null ? v1 : v2.withOverrides(v1);
    }

    public Class<?> findSerializationType(Annotated a) {
        Class<?> r = this._primary.findSerializationType(a);
        return r == null ? this._secondary.findSerializationType(a) : r;
    }

    public Class<?> findSerializationKeyType(Annotated am, JavaType baseType) {
        Class<?> r = this._primary.findSerializationKeyType(am, baseType);
        return r == null ? this._secondary.findSerializationKeyType(am, baseType) : r;
    }

    public Class<?> findSerializationContentType(Annotated am, JavaType baseType) {
        Class<?> r = this._primary.findSerializationContentType(am, baseType);
        return r == null ? this._secondary.findSerializationContentType(am, baseType) : r;
    }

    public Typing findSerializationTyping(Annotated a) {
        Typing r = this._primary.findSerializationTyping(a);
        return r == null ? this._secondary.findSerializationTyping(a) : r;
    }

    public Object findSerializationConverter(Annotated a) {
        Object r = this._primary.findSerializationConverter(a);
        return r == null ? this._secondary.findSerializationConverter(a) : r;
    }

    public Object findSerializationContentConverter(AnnotatedMember a) {
        Object r = this._primary.findSerializationContentConverter(a);
        return r == null ? this._secondary.findSerializationContentConverter(a) : r;
    }

    public Class<?>[] findViews(Annotated a) {
        Class<?>[] result = this._primary.findViews(a);
        if (result == null) {
            return this._secondary.findViews(a);
        }
        return result;
    }

    public Boolean isTypeId(AnnotatedMember member) {
        Boolean b = this._primary.isTypeId(member);
        return b == null ? this._secondary.isTypeId(member) : b;
    }

    public ObjectIdInfo findObjectIdInfo(Annotated ann) {
        ObjectIdInfo r = this._primary.findObjectIdInfo(ann);
        return r == null ? this._secondary.findObjectIdInfo(ann) : r;
    }

    public ObjectIdInfo findObjectReferenceInfo(Annotated ann, ObjectIdInfo objectIdInfo) {
        return this._primary.findObjectReferenceInfo(ann, this._secondary.findObjectReferenceInfo(ann, objectIdInfo));
    }

    public JsonFormat.Value findFormat(Annotated ann) {
        JsonFormat.Value r = this._primary.findFormat(ann);
        return r == null ? this._secondary.findFormat(ann) : r;
    }

    public PropertyName findWrapperName(Annotated ann) {
        PropertyName name = this._primary.findWrapperName(ann);
        if (name == null) {
            return this._secondary.findWrapperName(ann);
        }
        if (name != PropertyName.USE_DEFAULT) {
            return name;
        }
        PropertyName name2 = this._secondary.findWrapperName(ann);
        if (name2 != null) {
            return name2;
        }
        return name;
    }

    public String findPropertyDefaultValue(Annotated ann) {
        String str = this._primary.findPropertyDefaultValue(ann);
        return (str == null || str.isEmpty()) ? this._secondary.findPropertyDefaultValue(ann) : str;
    }

    public String findPropertyDescription(Annotated ann) {
        String r = this._primary.findPropertyDescription(ann);
        return r == null ? this._secondary.findPropertyDescription(ann) : r;
    }

    public Integer findPropertyIndex(Annotated ann) {
        Integer r = this._primary.findPropertyIndex(ann);
        return r == null ? this._secondary.findPropertyIndex(ann) : r;
    }

    public String findImplicitPropertyName(AnnotatedMember param) {
        String r = this._primary.findImplicitPropertyName(param);
        return r == null ? this._secondary.findImplicitPropertyName(param) : r;
    }

    public Access findPropertyAccess(Annotated ann) {
        Access acc = this._primary.findPropertyAccess(ann);
        if (acc != null && acc != Access.AUTO) {
            return acc;
        }
        acc = this._secondary.findPropertyAccess(ann);
        if (acc != null) {
            return acc;
        }
        return Access.AUTO;
    }

    public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
        String[] r = this._primary.findSerializationPropertyOrder(ac);
        return r == null ? this._secondary.findSerializationPropertyOrder(ac) : r;
    }

    public Boolean findSerializationSortAlphabetically(Annotated ann) {
        Boolean r = this._primary.findSerializationSortAlphabetically(ann);
        return r == null ? this._secondary.findSerializationSortAlphabetically(ann) : r;
    }

    public void findAndAddVirtualProperties(MapperConfig<?> config, AnnotatedClass ac, List<BeanPropertyWriter> properties) {
        this._primary.findAndAddVirtualProperties(config, ac, properties);
        this._secondary.findAndAddVirtualProperties(config, ac, properties);
    }

    public PropertyName findNameForSerialization(Annotated a) {
        PropertyName n = this._primary.findNameForSerialization(a);
        if (n == null) {
            return this._secondary.findNameForSerialization(a);
        }
        if (n != PropertyName.USE_DEFAULT) {
            return n;
        }
        PropertyName n2 = this._secondary.findNameForSerialization(a);
        if (n2 != null) {
            return n2;
        }
        return n;
    }

    public boolean hasAsValueAnnotation(AnnotatedMethod am) {
        return this._primary.hasAsValueAnnotation(am) || this._secondary.hasAsValueAnnotation(am);
    }

    public String findEnumValue(Enum<?> value) {
        String r = this._primary.findEnumValue(value);
        return r == null ? this._secondary.findEnumValue(value) : r;
    }

    public Object findDeserializer(Annotated am) {
        Object r = this._primary.findDeserializer(am);
        return _isExplicitClassOrOb(r, JsonDeserializer.None.class) ? r : this._secondary.findDeserializer(am);
    }

    public Object findKeyDeserializer(Annotated am) {
        Object r = this._primary.findKeyDeserializer(am);
        return _isExplicitClassOrOb(r, KeyDeserializer.None.class) ? r : this._secondary.findKeyDeserializer(am);
    }

    public Object findContentDeserializer(Annotated am) {
        Object r = this._primary.findContentDeserializer(am);
        return _isExplicitClassOrOb(r, JsonDeserializer.None.class) ? r : this._secondary.findContentDeserializer(am);
    }

    public Class<?> findDeserializationType(Annotated am, JavaType baseType) {
        Class<?> r = this._primary.findDeserializationType(am, baseType);
        return r != null ? r : this._secondary.findDeserializationType(am, baseType);
    }

    public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType) {
        Class<?> result = this._primary.findDeserializationKeyType(am, baseKeyType);
        return result == null ? this._secondary.findDeserializationKeyType(am, baseKeyType) : result;
    }

    public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType) {
        Class<?> result = this._primary.findDeserializationContentType(am, baseContentType);
        return result == null ? this._secondary.findDeserializationContentType(am, baseContentType) : result;
    }

    public Object findDeserializationConverter(Annotated a) {
        Object ob = this._primary.findDeserializationConverter(a);
        return ob == null ? this._secondary.findDeserializationConverter(a) : ob;
    }

    public Object findDeserializationContentConverter(AnnotatedMember a) {
        Object ob = this._primary.findDeserializationContentConverter(a);
        return ob == null ? this._secondary.findDeserializationContentConverter(a) : ob;
    }

    public Object findValueInstantiator(AnnotatedClass ac) {
        Object result = this._primary.findValueInstantiator(ac);
        return result == null ? this._secondary.findValueInstantiator(ac) : result;
    }

    public Class<?> findPOJOBuilder(AnnotatedClass ac) {
        Class<?> result = this._primary.findPOJOBuilder(ac);
        return result == null ? this._secondary.findPOJOBuilder(ac) : result;
    }

    public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac) {
        JsonPOJOBuilder.Value result = this._primary.findPOJOBuilderConfig(ac);
        return result == null ? this._secondary.findPOJOBuilderConfig(ac) : result;
    }

    public PropertyName findNameForDeserialization(Annotated a) {
        PropertyName n = this._primary.findNameForDeserialization(a);
        if (n == null) {
            return this._secondary.findNameForDeserialization(a);
        }
        if (n != PropertyName.USE_DEFAULT) {
            return n;
        }
        PropertyName n2 = this._secondary.findNameForDeserialization(a);
        if (n2 != null) {
            return n2;
        }
        return n;
    }

    public boolean hasAnySetterAnnotation(AnnotatedMethod am) {
        return this._primary.hasAnySetterAnnotation(am) || this._secondary.hasAnySetterAnnotation(am);
    }

    public boolean hasAnyGetterAnnotation(AnnotatedMethod am) {
        return this._primary.hasAnyGetterAnnotation(am) || this._secondary.hasAnyGetterAnnotation(am);
    }

    public boolean hasCreatorAnnotation(Annotated a) {
        return this._primary.hasCreatorAnnotation(a) || this._secondary.hasCreatorAnnotation(a);
    }

    public Mode findCreatorBinding(Annotated a) {
        Mode mode = this._primary.findCreatorBinding(a);
        return mode != null ? mode : this._secondary.findCreatorBinding(a);
    }

    protected boolean _isExplicitClassOrOb(Object maybeCls, Class<?> implicit) {
        if (maybeCls == null) {
            return false;
        }
        if (!(maybeCls instanceof Class)) {
            return true;
        }
        Class<?> cls = (Class) maybeCls;
        if (cls == implicit || ClassUtil.isBogusClass(cls)) {
            return false;
        }
        return true;
    }
}
