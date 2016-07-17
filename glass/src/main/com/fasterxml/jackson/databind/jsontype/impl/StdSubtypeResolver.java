package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class StdSubtypeResolver extends SubtypeResolver implements Serializable {
    private static final long serialVersionUID = 1;
    protected LinkedHashSet<NamedType> _registeredSubtypes;

    public void registerSubtypes(NamedType... types) {
        if (this._registeredSubtypes == null) {
            this._registeredSubtypes = new LinkedHashSet();
        }
        for (NamedType type : types) {
            this._registeredSubtypes.add(type);
        }
    }

    public void registerSubtypes(Class<?>... classes) {
        NamedType[] types = new NamedType[classes.length];
        int len = classes.length;
        for (int i = 0; i < len; i++) {
            types[i] = new NamedType(classes[i]);
        }
        registerSubtypes(types);
    }

    public Collection<NamedType> collectAndResolveSubtypesByClass(MapperConfig<?> config, AnnotatedMember property, JavaType baseType) {
        Iterator i$;
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        Class<?> rawBase = baseType == null ? property.getRawType() : baseType.getRawClass();
        HashMap<NamedType, NamedType> collected = new HashMap();
        if (this._registeredSubtypes != null) {
            i$ = this._registeredSubtypes.iterator();
            while (i$.hasNext()) {
                NamedType subtype = (NamedType) i$.next();
                if (rawBase.isAssignableFrom(subtype.getType())) {
                    _collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config), subtype, config, ai, collected);
                }
            }
        }
        Collection<NamedType> st = ai.findSubtypes(property);
        if (st != null) {
            for (NamedType nt : st) {
                _collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(nt.getType(), ai, config), nt, config, ai, collected);
            }
        }
        _collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(rawBase, ai, config), new NamedType(rawBase, null), config, ai, collected);
        return new ArrayList(collected.values());
    }

    public Collection<NamedType> collectAndResolveSubtypesByClass(MapperConfig<?> config, AnnotatedClass type) {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        HashMap<NamedType, NamedType> subtypes = new HashMap();
        if (this._registeredSubtypes != null) {
            Class<?> rawBase = type.getRawType();
            Iterator i$ = this._registeredSubtypes.iterator();
            while (i$.hasNext()) {
                NamedType subtype = (NamedType) i$.next();
                if (rawBase.isAssignableFrom(subtype.getType())) {
                    _collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config), subtype, config, ai, subtypes);
                }
            }
        }
        _collectAndResolve(type, new NamedType(type.getRawType(), null), config, ai, subtypes);
        return new ArrayList(subtypes.values());
    }

    public Collection<NamedType> collectAndResolveSubtypesByTypeId(MapperConfig<?> config, AnnotatedMember property, JavaType baseType) {
        Iterator i$;
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        Class<?> rawBase = baseType == null ? property.getRawType() : baseType.getRawClass();
        Set<Class<?>> typesHandled = new HashSet();
        Map<String, NamedType> byName = new LinkedHashMap();
        _collectAndResolveByTypeId(AnnotatedClass.constructWithoutSuperTypes(rawBase, ai, config), new NamedType(rawBase, null), config, typesHandled, byName);
        Collection<NamedType> st = ai.findSubtypes(property);
        if (st != null) {
            for (NamedType nt : st) {
                _collectAndResolveByTypeId(AnnotatedClass.constructWithoutSuperTypes(nt.getType(), ai, config), nt, config, typesHandled, byName);
            }
        }
        if (this._registeredSubtypes != null) {
            i$ = this._registeredSubtypes.iterator();
            while (i$.hasNext()) {
                NamedType subtype = (NamedType) i$.next();
                if (rawBase.isAssignableFrom(subtype.getType())) {
                    _collectAndResolveByTypeId(AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config), subtype, config, typesHandled, byName);
                }
            }
        }
        return _combineNamedAndUnnamed(typesHandled, byName);
    }

    public Collection<NamedType> collectAndResolveSubtypesByTypeId(MapperConfig<?> config, AnnotatedClass type) {
        Set<Class<?>> typesHandled = new HashSet();
        Map<String, NamedType> byName = new LinkedHashMap();
        _collectAndResolveByTypeId(type, new NamedType(type.getRawType(), null), config, typesHandled, byName);
        if (this._registeredSubtypes != null) {
            Class<?> rawBase = type.getRawType();
            Iterator i$ = this._registeredSubtypes.iterator();
            while (i$.hasNext()) {
                NamedType subtype = (NamedType) i$.next();
                if (rawBase.isAssignableFrom(subtype.getType())) {
                    _collectAndResolveByTypeId(AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), config.getAnnotationIntrospector(), config), subtype, config, typesHandled, byName);
                }
            }
        }
        return _combineNamedAndUnnamed(typesHandled, byName);
    }

    @Deprecated
    public Collection<NamedType> collectAndResolveSubtypes(AnnotatedMember property, MapperConfig<?> config, AnnotationIntrospector ai, JavaType baseType) {
        return collectAndResolveSubtypesByClass(config, property, baseType);
    }

    @Deprecated
    public Collection<NamedType> collectAndResolveSubtypes(AnnotatedClass type, MapperConfig<?> config, AnnotationIntrospector ai) {
        return collectAndResolveSubtypesByClass(config, type);
    }

    protected void _collectAndResolve(AnnotatedClass annotatedType, NamedType namedType, MapperConfig<?> config, AnnotationIntrospector ai, HashMap<NamedType, NamedType> collectedSubtypes) {
        if (!namedType.hasName()) {
            String name = ai.findTypeName(annotatedType);
            if (name != null) {
                namedType = new NamedType(namedType.getType(), name);
            }
        }
        if (!collectedSubtypes.containsKey(namedType)) {
            collectedSubtypes.put(namedType, namedType);
            Collection<NamedType> st = ai.findSubtypes(annotatedType);
            if (st != null && !st.isEmpty()) {
                for (NamedType subtype : st) {
                    _collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config), subtype, config, ai, collectedSubtypes);
                }
            }
        } else if (namedType.hasName() && !((NamedType) collectedSubtypes.get(namedType)).hasName()) {
            collectedSubtypes.put(namedType, namedType);
        }
    }

    protected void _collectAndResolveByTypeId(AnnotatedClass annotatedType, NamedType namedType, MapperConfig<?> config, Set<Class<?>> typesHandled, Map<String, NamedType> byName) {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        if (!namedType.hasName()) {
            String name = ai.findTypeName(annotatedType);
            if (name != null) {
                namedType = new NamedType(namedType.getType(), name);
            }
        }
        if (namedType.hasName()) {
            byName.put(namedType.getName(), namedType);
        }
        if (typesHandled.add(namedType.getType())) {
            Collection<NamedType> st = ai.findSubtypes(annotatedType);
            if (st != null && !st.isEmpty()) {
                for (NamedType subtype : st) {
                    _collectAndResolveByTypeId(AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config), subtype, config, typesHandled, byName);
                }
            }
        }
    }

    protected Collection<NamedType> _combineNamedAndUnnamed(Set<Class<?>> typesHandled, Map<String, NamedType> byName) {
        ArrayList<NamedType> result = new ArrayList(byName.values());
        for (NamedType t : byName.values()) {
            typesHandled.remove(t.getType());
        }
        for (Class<?> cls : typesHandled) {
            result.add(new NamedType(cls));
        }
        return result;
    }
}
