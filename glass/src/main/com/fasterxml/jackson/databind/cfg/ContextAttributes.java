package com.fasterxml.jackson.databind.cfg;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ContextAttributes {

    public static class Impl extends ContextAttributes implements Serializable {
        protected static final Impl EMPTY;
        protected static final Object NULL_SURROGATE;
        private static final long serialVersionUID = 1;
        protected transient Map<Object, Object> _nonShared;
        protected final Map<Object, Object> _shared;

        static {
            EMPTY = new Impl(Collections.emptyMap());
            NULL_SURROGATE = new Object();
        }

        protected Impl(Map<Object, Object> shared) {
            this._shared = shared;
            this._nonShared = null;
        }

        protected Impl(Map<Object, Object> shared, Map<Object, Object> nonShared) {
            this._shared = shared;
            this._nonShared = nonShared;
        }

        public static ContextAttributes getEmpty() {
            return EMPTY;
        }

        public ContextAttributes withSharedAttribute(Object key, Object value) {
            Map<Object, Object> m;
            if (this == EMPTY) {
                m = new HashMap(8);
            } else {
                m = _copy(this._shared);
            }
            m.put(key, value);
            return new Impl(m);
        }

        public ContextAttributes withSharedAttributes(Map<Object, Object> shared) {
            return new Impl(shared);
        }

        public ContextAttributes withoutSharedAttribute(Object key) {
            if (this._shared.isEmpty() || !this._shared.containsKey(key)) {
                return this;
            }
            if (this._shared.size() == 1) {
                return EMPTY;
            }
            Map<Object, Object> m = _copy(this._shared);
            m.remove(key);
            this(m);
            return this;
        }

        public Object getAttribute(Object key) {
            if (this._nonShared != null) {
                Object ob = this._nonShared.get(key);
                if (ob != null) {
                    if (ob == NULL_SURROGATE) {
                        return null;
                    }
                    return ob;
                }
            }
            return this._shared.get(key);
        }

        public ContextAttributes withPerCallAttribute(Object key, Object value) {
            if (value == null) {
                if (!this._shared.containsKey(key)) {
                    return this;
                }
                value = NULL_SURROGATE;
            }
            if (this._nonShared == null) {
                return nonSharedInstance(key, value);
            }
            this._nonShared.put(key, value);
            return this;
        }

        protected ContextAttributes nonSharedInstance(Object key, Object value) {
            Map<Object, Object> m = new HashMap();
            if (value == null) {
                value = NULL_SURROGATE;
            }
            m.put(key, value);
            return new Impl(this._shared, m);
        }

        private Map<Object, Object> _copy(Map<Object, Object> src) {
            return new HashMap(src);
        }
    }

    public abstract Object getAttribute(Object obj);

    public abstract ContextAttributes withPerCallAttribute(Object obj, Object obj2);

    public abstract ContextAttributes withSharedAttribute(Object obj, Object obj2);

    public abstract ContextAttributes withSharedAttributes(Map<Object, Object> map);

    public abstract ContextAttributes withoutSharedAttribute(Object obj);

    public static ContextAttributes getEmpty() {
        return Impl.getEmpty();
    }
}
