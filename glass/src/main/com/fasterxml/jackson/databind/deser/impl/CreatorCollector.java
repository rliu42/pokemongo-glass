package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.squareup.otto.Bus;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreatorCollector {
    protected static final int C_BOOLEAN = 5;
    protected static final int C_DEFAULT = 0;
    protected static final int C_DELEGATE = 6;
    protected static final int C_DOUBLE = 4;
    protected static final int C_INT = 2;
    protected static final int C_LONG = 3;
    protected static final int C_PROPS = 7;
    protected static final int C_STRING = 1;
    protected static final String[] TYPE_DESCS;
    protected final BeanDescription _beanDesc;
    protected final boolean _canFixAccess;
    protected final AnnotatedWithParams[] _creators;
    protected SettableBeanProperty[] _delegateArgs;
    protected int _explicitCreators;
    protected boolean _hasNonDefaultCreator;
    protected AnnotatedParameter _incompleteParameter;
    protected SettableBeanProperty[] _propertyBasedArgs;

    protected static final class Vanilla extends ValueInstantiator implements Serializable {
        public static final int TYPE_COLLECTION = 1;
        public static final int TYPE_HASH_MAP = 3;
        public static final int TYPE_MAP = 2;
        private static final long serialVersionUID = 1;
        private final int _type;

        public Vanilla(int t) {
            this._type = t;
        }

        public String getValueTypeDesc() {
            switch (this._type) {
                case TYPE_COLLECTION /*1*/:
                    return ArrayList.class.getName();
                case TYPE_MAP /*2*/:
                    return LinkedHashMap.class.getName();
                case TYPE_HASH_MAP /*3*/:
                    return HashMap.class.getName();
                default:
                    return Object.class.getName();
            }
        }

        public boolean canInstantiate() {
            return true;
        }

        public boolean canCreateUsingDefault() {
            return true;
        }

        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            switch (this._type) {
                case TYPE_COLLECTION /*1*/:
                    return new ArrayList();
                case TYPE_MAP /*2*/:
                    return new LinkedHashMap();
                case TYPE_HASH_MAP /*3*/:
                    return new HashMap();
                default:
                    throw new IllegalStateException("Unknown type " + this._type);
            }
        }
    }

    static {
        TYPE_DESCS = new String[]{Bus.DEFAULT_IDENTIFIER, "String", "int", "long", "double", "boolean", "delegate", "property-based"};
    }

    public CreatorCollector(BeanDescription beanDesc, boolean canFixAccess) {
        this._creators = new AnnotatedWithParams[8];
        this._explicitCreators = C_DEFAULT;
        this._hasNonDefaultCreator = false;
        this._beanDesc = beanDesc;
        this._canFixAccess = canFixAccess;
    }

    public ValueInstantiator constructValueInstantiator(DeserializationConfig config) {
        JavaType delegateType;
        boolean maybeVanilla = !this._hasNonDefaultCreator;
        if (maybeVanilla || this._creators[C_DELEGATE] == null) {
            delegateType = null;
        } else {
            int ix = C_DEFAULT;
            if (this._delegateArgs != null) {
                int len = this._delegateArgs.length;
                for (int i = C_DEFAULT; i < len; i += C_STRING) {
                    if (this._delegateArgs[i] == null) {
                        ix = i;
                        break;
                    }
                }
            }
            delegateType = this._beanDesc.bindingsForBeanType().resolveType(this._creators[C_DELEGATE].getGenericParameterType(ix));
        }
        JavaType type = this._beanDesc.getType();
        if (maybeVanilla & (!this._hasNonDefaultCreator ? C_STRING : C_DEFAULT)) {
            Class<?> rawType = type.getRawClass();
            if (rawType == Collection.class || rawType == List.class || rawType == ArrayList.class) {
                return new Vanilla(C_STRING);
            }
            if (rawType == Map.class || rawType == LinkedHashMap.class) {
                return new Vanilla(C_INT);
            }
            if (rawType == HashMap.class) {
                return new Vanilla(C_LONG);
            }
        }
        ValueInstantiator inst = new StdValueInstantiator(config, type);
        inst.configureFromObjectSettings(this._creators[C_DEFAULT], this._creators[C_DELEGATE], delegateType, this._delegateArgs, this._creators[C_PROPS], this._propertyBasedArgs);
        inst.configureFromStringCreator(this._creators[C_STRING]);
        inst.configureFromIntCreator(this._creators[C_INT]);
        inst.configureFromLongCreator(this._creators[C_LONG]);
        inst.configureFromDoubleCreator(this._creators[C_DOUBLE]);
        inst.configureFromBooleanCreator(this._creators[C_BOOLEAN]);
        inst.configureIncompleteParameter(this._incompleteParameter);
        return inst;
    }

    public void setDefaultCreator(AnnotatedWithParams creator) {
        this._creators[C_DEFAULT] = (AnnotatedWithParams) _fixAccess(creator);
    }

    public void addStringCreator(AnnotatedWithParams creator, boolean explicit) {
        verifyNonDup(creator, C_STRING, explicit);
    }

    public void addIntCreator(AnnotatedWithParams creator, boolean explicit) {
        verifyNonDup(creator, C_INT, explicit);
    }

    public void addLongCreator(AnnotatedWithParams creator, boolean explicit) {
        verifyNonDup(creator, C_LONG, explicit);
    }

    public void addDoubleCreator(AnnotatedWithParams creator, boolean explicit) {
        verifyNonDup(creator, C_DOUBLE, explicit);
    }

    public void addBooleanCreator(AnnotatedWithParams creator, boolean explicit) {
        verifyNonDup(creator, C_BOOLEAN, explicit);
    }

    public void addDelegatingCreator(AnnotatedWithParams creator, boolean explicit, SettableBeanProperty[] injectables) {
        verifyNonDup(creator, C_DELEGATE, explicit);
        this._delegateArgs = injectables;
    }

    public void addPropertyCreator(AnnotatedWithParams creator, boolean explicit, SettableBeanProperty[] properties) {
        verifyNonDup(creator, C_PROPS, explicit);
        if (properties.length > C_STRING) {
            HashMap<String, Integer> names = new HashMap();
            int i = C_DEFAULT;
            int len = properties.length;
            while (i < len) {
                String name = properties[i].getName();
                if (name.length() != 0 || properties[i].getInjectableValueId() == null) {
                    Integer old = (Integer) names.put(name, Integer.valueOf(i));
                    if (old != null) {
                        throw new IllegalArgumentException("Duplicate creator property \"" + name + "\" (index " + old + " vs " + i + ")");
                    }
                }
                i += C_STRING;
            }
        }
        this._propertyBasedArgs = properties;
    }

    public void addIncompeteParameter(AnnotatedParameter parameter) {
        if (this._incompleteParameter == null) {
            this._incompleteParameter = parameter;
        }
    }

    @Deprecated
    public void addStringCreator(AnnotatedWithParams creator) {
        addStringCreator(creator, false);
    }

    @Deprecated
    public void addIntCreator(AnnotatedWithParams creator) {
        addBooleanCreator(creator, false);
    }

    @Deprecated
    public void addLongCreator(AnnotatedWithParams creator) {
        addBooleanCreator(creator, false);
    }

    @Deprecated
    public void addDoubleCreator(AnnotatedWithParams creator) {
        addBooleanCreator(creator, false);
    }

    @Deprecated
    public void addBooleanCreator(AnnotatedWithParams creator) {
        addBooleanCreator(creator, false);
    }

    @Deprecated
    public void addDelegatingCreator(AnnotatedWithParams creator, CreatorProperty[] injectables) {
        addDelegatingCreator(creator, false, injectables);
    }

    @Deprecated
    public void addPropertyCreator(AnnotatedWithParams creator, CreatorProperty[] properties) {
        addPropertyCreator(creator, false, properties);
    }

    public boolean hasDefaultCreator() {
        return this._creators[C_DEFAULT] != null;
    }

    public boolean hasDelegatingCreator() {
        return this._creators[C_DELEGATE] != null;
    }

    public boolean hasPropertyBasedCreator() {
        return this._creators[C_PROPS] != null;
    }

    private <T extends AnnotatedMember> T _fixAccess(T member) {
        if (member != null && this._canFixAccess) {
            ClassUtil.checkAndFixAccess((Member) member.getAnnotated());
        }
        return member;
    }

    protected void verifyNonDup(AnnotatedWithParams newOne, int typeIndex, boolean explicit) {
        boolean verify = true;
        int mask = C_STRING << typeIndex;
        this._hasNonDefaultCreator = true;
        AnnotatedWithParams oldOne = this._creators[typeIndex];
        if (oldOne != null) {
            if ((this._explicitCreators & mask) != 0) {
                if (explicit) {
                    verify = true;
                } else {
                    return;
                }
            } else if (explicit) {
                verify = false;
            }
            if (verify && oldOne.getClass() == newOne.getClass()) {
                Class<?> oldType = oldOne.getRawParameterType(C_DEFAULT);
                Class<?> newType = newOne.getRawParameterType(C_DEFAULT);
                if (oldType == newType) {
                    throw new IllegalArgumentException("Conflicting " + TYPE_DESCS[typeIndex] + " creators: already had explicitly marked " + oldOne + ", encountered " + newOne);
                } else if (newType.isAssignableFrom(oldType)) {
                    return;
                }
            }
        }
        if (explicit) {
            this._explicitCreators |= mask;
        }
        this._creators[typeIndex] = (AnnotatedWithParams) _fixAccess(newOne);
    }
}
