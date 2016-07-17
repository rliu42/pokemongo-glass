package com.fasterxml.jackson.databind.introspect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import spacemadness.com.lunarconsole.BuildConfig;

public final class MemberKey {
    static final Class<?>[] NO_CLASSES;
    final Class<?>[] _argTypes;
    final String _name;

    static {
        NO_CLASSES = new Class[0];
    }

    public MemberKey(Method m) {
        this(m.getName(), m.getParameterTypes());
    }

    public MemberKey(Constructor<?> ctor) {
        this(BuildConfig.FLAVOR, ctor.getParameterTypes());
    }

    public MemberKey(String name, Class<?>[] argTypes) {
        this._name = name;
        if (argTypes == null) {
            argTypes = NO_CLASSES;
        }
        this._argTypes = argTypes;
    }

    public String toString() {
        return this._name + "(" + this._argTypes.length + "-args)";
    }

    public int hashCode() {
        return this._name.hashCode() + this._argTypes.length;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        MemberKey other = (MemberKey) o;
        if (!this._name.equals(other._name)) {
            return false;
        }
        Class<?>[] otherArgs = other._argTypes;
        int len = this._argTypes.length;
        if (otherArgs.length != len) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (otherArgs[i] != this._argTypes[i]) {
                return false;
            }
        }
        return true;
    }
}
