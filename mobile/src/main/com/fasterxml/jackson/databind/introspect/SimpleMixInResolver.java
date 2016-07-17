package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SimpleMixInResolver implements MixInResolver, Serializable {
    private static final long serialVersionUID = 1;
    protected Map<ClassKey, Class<?>> _localMixIns;
    protected final MixInResolver _overrides;

    public SimpleMixInResolver(MixInResolver overrides) {
        this._overrides = overrides;
    }

    protected SimpleMixInResolver(MixInResolver overrides, Map<ClassKey, Class<?>> mixins) {
        this._overrides = overrides;
        this._localMixIns = mixins;
    }

    public SimpleMixInResolver withOverrides(MixInResolver overrides) {
        return new SimpleMixInResolver(overrides, this._localMixIns);
    }

    public SimpleMixInResolver withoutLocalDefinitions() {
        return new SimpleMixInResolver(this._overrides, null);
    }

    public void setLocalDefinitions(Map<Class<?>, Class<?>> sourceMixins) {
        if (sourceMixins == null || sourceMixins.isEmpty()) {
            this._localMixIns = null;
        }
        Map<ClassKey, Class<?>> mixIns = new HashMap(sourceMixins.size());
        for (Entry<Class<?>, Class<?>> en : sourceMixins.entrySet()) {
            mixIns.put(new ClassKey((Class) en.getKey()), en.getValue());
        }
        this._localMixIns = mixIns;
    }

    public void addLocalDefinition(Class<?> target, Class<?> mixinSource) {
        if (this._localMixIns == null) {
            this._localMixIns = new HashMap();
        }
        this._localMixIns.put(new ClassKey(target), mixinSource);
    }

    public SimpleMixInResolver copy() {
        return new SimpleMixInResolver(this._overrides == null ? null : this._overrides.copy(), this._localMixIns == null ? null : new HashMap(this._localMixIns));
    }

    public Class<?> findMixInClassFor(Class<?> cls) {
        Class<?> mixin = this._overrides == null ? null : this._overrides.findMixInClassFor(cls);
        if (mixin != null || this._localMixIns == null) {
            return mixin;
        }
        return (Class) this._localMixIns.get(new ClassKey(cls));
    }

    public int localSize() {
        return this._localMixIns == null ? 0 : this._localMixIns.size();
    }
}
