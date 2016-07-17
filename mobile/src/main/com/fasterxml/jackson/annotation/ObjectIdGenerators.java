package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import java.util.UUID;

public class ObjectIdGenerators {

    private static abstract class Base<T> extends ObjectIdGenerator<T> {
        protected final Class<?> _scope;

        public abstract T generateId(Object obj);

        protected Base(Class<?> scope) {
            this._scope = scope;
        }

        public final Class<?> getScope() {
            return this._scope;
        }

        public boolean canUseFor(ObjectIdGenerator<?> gen) {
            return gen.getClass() == getClass() && gen.getScope() == this._scope;
        }
    }

    public static final class IntSequenceGenerator extends Base<Integer> {
        private static final long serialVersionUID = 1;
        protected transient int _nextValue;

        public /* bridge */ /* synthetic */ boolean canUseFor(ObjectIdGenerator x0) {
            return super.canUseFor(x0);
        }

        public IntSequenceGenerator() {
            this(Object.class, -1);
        }

        public IntSequenceGenerator(Class<?> scope, int fv) {
            super(scope);
            this._nextValue = fv;
        }

        protected int initialValue() {
            return 1;
        }

        public ObjectIdGenerator<Integer> forScope(Class<?> scope) {
            return this._scope == scope ? this : new IntSequenceGenerator(scope, this._nextValue);
        }

        public ObjectIdGenerator<Integer> newForSerialization(Object context) {
            return new IntSequenceGenerator(this._scope, initialValue());
        }

        public IdKey key(Object key) {
            if (key == null) {
                return null;
            }
            return new IdKey(getClass(), this._scope, key);
        }

        public Integer generateId(Object forPojo) {
            if (forPojo == null) {
                return null;
            }
            int id = this._nextValue;
            this._nextValue++;
            return Integer.valueOf(id);
        }
    }

    public static abstract class None extends ObjectIdGenerator<Object> {
    }

    public static abstract class PropertyGenerator extends Base<Object> {
        private static final long serialVersionUID = 1;

        public /* bridge */ /* synthetic */ boolean canUseFor(ObjectIdGenerator x0) {
            return super.canUseFor(x0);
        }

        protected PropertyGenerator(Class<?> scope) {
            super(scope);
        }
    }

    public static final class UUIDGenerator extends Base<UUID> {
        private static final long serialVersionUID = 1;

        public UUIDGenerator() {
            this(Object.class);
        }

        private UUIDGenerator(Class<?> cls) {
            super(Object.class);
        }

        public ObjectIdGenerator<UUID> forScope(Class<?> cls) {
            return this;
        }

        public ObjectIdGenerator<UUID> newForSerialization(Object context) {
            return this;
        }

        public UUID generateId(Object forPojo) {
            return UUID.randomUUID();
        }

        public IdKey key(Object key) {
            if (key == null) {
                return null;
            }
            return new IdKey(getClass(), null, key);
        }

        public boolean canUseFor(ObjectIdGenerator<?> gen) {
            return gen.getClass() == getClass();
        }
    }
}
