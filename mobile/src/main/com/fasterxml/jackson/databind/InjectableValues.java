package com.fasterxml.jackson.databind;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class InjectableValues {

    public static class Std extends InjectableValues implements Serializable {
        private static final long serialVersionUID = 1;
        protected final Map<String, Object> _values;

        public Std() {
            this(new HashMap());
        }

        public Std(Map<String, Object> values) {
            this._values = values;
        }

        public Std addValue(String key, Object value) {
            this._values.put(key, value);
            return this;
        }

        public Std addValue(Class<?> classKey, Object value) {
            this._values.put(classKey.getName(), value);
            return this;
        }

        public Object findInjectableValue(Object valueId, DeserializationContext ctxt, BeanProperty forProperty, Object beanInstance) {
            if (valueId instanceof String) {
                String key = (String) valueId;
                Object ob = this._values.get(key);
                if (ob != null || this._values.containsKey(key)) {
                    return ob;
                }
                throw new IllegalArgumentException("No injectable id with value '" + key + "' found (for property '" + forProperty.getName() + "')");
            }
            throw new IllegalArgumentException("Unrecognized inject value id type (" + (valueId == null ? "[null]" : valueId.getClass().getName()) + "), expecting String");
        }
    }

    public abstract Object findInjectableValue(Object obj, DeserializationContext deserializationContext, BeanProperty beanProperty, Object obj2);
}
