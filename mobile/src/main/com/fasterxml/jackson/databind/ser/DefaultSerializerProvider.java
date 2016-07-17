package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.None;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class DefaultSerializerProvider extends SerializerProvider implements Serializable {
    private static final long serialVersionUID = 1;
    protected transient ArrayList<ObjectIdGenerator<?>> _objectIdGenerators;
    protected transient Map<Object, WritableObjectId> _seenObjectIds;

    public static final class Impl extends DefaultSerializerProvider {
        private static final long serialVersionUID = 1;

        public Impl(Impl src) {
            super(src);
        }

        protected Impl(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
            super(src, config, f);
        }

        public DefaultSerializerProvider copy() {
            if (getClass() != Impl.class) {
                return super.copy();
            }
            return new Impl(this);
        }

        public Impl createInstance(SerializationConfig config, SerializerFactory jsf) {
            return new Impl(this, config, jsf);
        }
    }

    public abstract DefaultSerializerProvider createInstance(SerializationConfig serializationConfig, SerializerFactory serializerFactory);

    protected DefaultSerializerProvider() {
    }

    protected DefaultSerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
        super(src, config, f);
    }

    protected DefaultSerializerProvider(DefaultSerializerProvider src) {
        super(src);
    }

    public DefaultSerializerProvider copy() {
        throw new IllegalStateException("DefaultSerializerProvider sub-class not overriding copy()");
    }

    public void serializeValue(JsonGenerator gen, Object value) throws IOException {
        if (value == null) {
            _serializeNull(gen);
            return;
        }
        boolean wrap;
        JsonSerializer<Object> ser = findTypedValueSerializer((Class) value.getClass(), true, null);
        PropertyName rootName = this._config.getFullRootName();
        if (rootName == null) {
            wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
            if (wrap) {
                gen.writeStartObject();
                gen.writeFieldName(this._config.findRootName(value.getClass()).simpleAsEncoded(this._config));
            }
        } else if (rootName.isEmpty()) {
            wrap = false;
        } else {
            wrap = true;
            gen.writeStartObject();
            gen.writeFieldName(rootName.getSimpleName());
        }
        try {
            ser.serialize(value, gen, this);
            if (wrap) {
                gen.writeEndObject();
            }
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            String msg = e.getMessage();
            if (msg == null) {
                msg = "[no message for " + e.getClass().getName() + "]";
            }
            throw new JsonMappingException(msg, e);
        }
    }

    public void serializeValue(JsonGenerator gen, Object value, JavaType rootType) throws IOException {
        if (value == null) {
            _serializeNull(gen);
            return;
        }
        boolean wrap;
        if (!rootType.getRawClass().isAssignableFrom(value.getClass())) {
            _reportIncompatibleRootType(value, rootType);
        }
        JsonSerializer<Object> ser = findTypedValueSerializer(rootType, true, null);
        PropertyName rootName = this._config.getFullRootName();
        if (rootName == null) {
            wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
            if (wrap) {
                gen.writeStartObject();
                gen.writeFieldName(this._config.findRootName(value.getClass()).simpleAsEncoded(this._config));
            }
        } else if (rootName.isEmpty()) {
            wrap = false;
        } else {
            wrap = true;
            gen.writeStartObject();
            gen.writeFieldName(rootName.getSimpleName());
        }
        try {
            ser.serialize(value, gen, this);
            if (wrap) {
                gen.writeEndObject();
            }
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            String msg = e.getMessage();
            if (msg == null) {
                msg = "[no message for " + e.getClass().getName() + "]";
            }
            throw new JsonMappingException(msg, e);
        }
    }

    public void serializeValue(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> ser) throws IOException {
        if (value == null) {
            _serializeNull(gen);
            return;
        }
        boolean wrap;
        if (!(rootType == null || rootType.getRawClass().isAssignableFrom(value.getClass()))) {
            _reportIncompatibleRootType(value, rootType);
        }
        if (ser == null) {
            ser = findTypedValueSerializer(rootType, true, null);
        }
        PropertyName rootName = this._config.getFullRootName();
        if (rootName == null) {
            wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
            if (wrap) {
                gen.writeStartObject();
                gen.writeFieldName((rootType == null ? this._config.findRootName(value.getClass()) : this._config.findRootName(rootType)).simpleAsEncoded(this._config));
            }
        } else if (rootName.isEmpty()) {
            wrap = false;
        } else {
            wrap = true;
            gen.writeStartObject();
            gen.writeFieldName(rootName.getSimpleName());
        }
        try {
            ser.serialize(value, gen, this);
            if (wrap) {
                gen.writeEndObject();
            }
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            String msg = e.getMessage();
            if (msg == null) {
                msg = "[no message for " + e.getClass().getName() + "]";
            }
            throw new JsonMappingException(msg, e);
        }
    }

    public void serializePolymorphic(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> valueSer, TypeSerializer typeSer) throws IOException {
        if (value == null) {
            _serializeNull(gen);
            return;
        }
        boolean wrap;
        if (!(rootType == null || rootType.getRawClass().isAssignableFrom(value.getClass()))) {
            _reportIncompatibleRootType(value, rootType);
        }
        if (valueSer == null) {
            if (rootType == null || !rootType.isContainerType()) {
                valueSer = findValueSerializer(value.getClass(), null);
            } else {
                valueSer = findValueSerializer(rootType, null);
            }
        }
        PropertyName rootName = this._config.getFullRootName();
        if (rootName == null) {
            wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
            if (wrap) {
                gen.writeStartObject();
                gen.writeFieldName(this._config.findRootName(value.getClass()).simpleAsEncoded(this._config));
            }
        } else if (rootName.isEmpty()) {
            wrap = false;
        } else {
            wrap = true;
            gen.writeStartObject();
            gen.writeFieldName(rootName.getSimpleName());
        }
        try {
            valueSer.serializeWithType(value, gen, this, typeSer);
            if (wrap) {
                gen.writeEndObject();
            }
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            String msg = e.getMessage();
            if (msg == null) {
                msg = "[no message for " + e.getClass().getName() + "]";
            }
            throw new JsonMappingException(msg, e);
        }
    }

    @Deprecated
    public void serializePolymorphic(JsonGenerator gen, Object value, TypeSerializer typeSer) throws IOException {
        serializePolymorphic(gen, value, value == null ? null : this._config.constructType(value.getClass()), null, typeSer);
    }

    protected void _serializeNull(JsonGenerator gen) throws IOException {
        try {
            getDefaultNullValueSerializer().serialize(null, gen, this);
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            String msg = e.getMessage();
            if (msg == null) {
                msg = "[no message for " + e.getClass().getName() + "]";
            }
            throw new JsonMappingException(msg, e);
        }
    }

    @Deprecated
    public JsonSchema generateJsonSchema(Class<?> type) throws JsonMappingException {
        if (type == null) {
            throw new IllegalArgumentException("A class must be provided");
        }
        JsonSerializer<Object> ser = findValueSerializer((Class) type, null);
        JsonNode schemaNode = ser instanceof SchemaAware ? ((SchemaAware) ser).getSchema(this, null) : JsonSchema.getDefaultSchemaNode();
        if (schemaNode instanceof ObjectNode) {
            return new JsonSchema((ObjectNode) schemaNode);
        }
        throw new IllegalArgumentException("Class " + type.getName() + " would not be serialized as a JSON object and therefore has no schema");
    }

    public void acceptJsonFormatVisitor(JavaType javaType, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
        if (javaType == null) {
            throw new IllegalArgumentException("A class must be provided");
        }
        visitor.setProvider(this);
        findValueSerializer(javaType, null).acceptJsonFormatVisitor(visitor, javaType);
    }

    public boolean hasSerializerFor(Class<?> cls, AtomicReference<Throwable> cause) {
        try {
            if (_findExplicitUntypedSerializer(cls) != null) {
                return true;
            }
            return false;
        } catch (JsonMappingException e) {
            if (cause == null) {
                return false;
            }
            cause.set(e);
            return false;
        } catch (RuntimeException e2) {
            if (cause == null) {
                throw e2;
            }
            cause.set(e2);
            return false;
        }
    }

    public int cachedSerializersCount() {
        return this._serializerCache.size();
    }

    public void flushCachedSerializers() {
        this._serializerCache.flush();
    }

    public WritableObjectId findObjectId(Object forPojo, ObjectIdGenerator<?> generatorType) {
        WritableObjectId oid;
        if (this._seenObjectIds == null) {
            this._seenObjectIds = _createObjectIdMap();
        } else {
            oid = (WritableObjectId) this._seenObjectIds.get(forPojo);
            if (oid != null) {
                return oid;
            }
        }
        ObjectIdGenerator<?> generator = null;
        if (this._objectIdGenerators == null) {
            this._objectIdGenerators = new ArrayList(8);
        } else {
            int len = this._objectIdGenerators.size();
            for (int i = 0; i < len; i++) {
                ObjectIdGenerator<?> gen = (ObjectIdGenerator) this._objectIdGenerators.get(i);
                if (gen.canUseFor(generatorType)) {
                    generator = gen;
                    break;
                }
            }
        }
        if (generator == null) {
            generator = generatorType.newForSerialization(this);
            this._objectIdGenerators.add(generator);
        }
        oid = new WritableObjectId(generator);
        this._seenObjectIds.put(forPojo, oid);
        return oid;
    }

    protected Map<Object, WritableObjectId> _createObjectIdMap() {
        if (isEnabled(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID)) {
            return new HashMap();
        }
        return new IdentityHashMap();
    }

    public JsonSerializer<Object> serializerInstance(Annotated annotated, Object serDef) throws JsonMappingException {
        if (serDef == null) {
            return null;
        }
        JsonSerializer<?> ser;
        if (serDef instanceof JsonSerializer) {
            ser = (JsonSerializer) serDef;
        } else if (serDef instanceof Class) {
            Class<?> serClass = (Class) serDef;
            if (serClass == None.class || ClassUtil.isBogusClass(serClass)) {
                return null;
            }
            if (JsonSerializer.class.isAssignableFrom(serClass)) {
                HandlerInstantiator hi = this._config.getHandlerInstantiator();
                ser = hi == null ? null : hi.serializerInstance(this._config, annotated, serClass);
                if (ser == null) {
                    ser = (JsonSerializer) ClassUtil.createInstance(serClass, this._config.canOverrideAccessModifiers());
                }
            } else {
                throw new IllegalStateException("AnnotationIntrospector returned Class " + serClass.getName() + "; expected Class<JsonSerializer>");
            }
        } else {
            throw new IllegalStateException("AnnotationIntrospector returned serializer definition of type " + serDef.getClass().getName() + "; expected type JsonSerializer or Class<JsonSerializer> instead");
        }
        return _handleResolvable(ser);
    }
}
