package com.fasterxml.jackson.databind.module;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SimpleDeserializers implements Deserializers, Serializable {
    private static final long serialVersionUID = -3006673354353448880L;
    protected HashMap<ClassKey, JsonDeserializer<?>> _classMappings;
    protected boolean _hasEnumDeserializer;

    public SimpleDeserializers() {
        this._classMappings = null;
        this._hasEnumDeserializer = false;
    }

    public SimpleDeserializers(Map<Class<?>, JsonDeserializer<?>> desers) {
        this._classMappings = null;
        this._hasEnumDeserializer = false;
        addDeserializers(desers);
    }

    public <T> void addDeserializer(Class<T> forClass, JsonDeserializer<? extends T> deser) {
        ClassKey key = new ClassKey(forClass);
        if (this._classMappings == null) {
            this._classMappings = new HashMap();
        }
        this._classMappings.put(key, deser);
        if (forClass == Enum.class) {
            this._hasEnumDeserializer = true;
        }
    }

    public void addDeserializers(Map<Class<?>, JsonDeserializer<?>> desers) {
        for (Entry<Class<?>, JsonDeserializer<?>> entry : desers.entrySet()) {
            addDeserializer((Class) entry.getKey(), (JsonDeserializer) entry.getValue());
        }
    }

    public JsonDeserializer<?> findArrayDeserializer(ArrayType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        return this._classMappings == null ? null : (JsonDeserializer) this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        return this._classMappings == null ? null : (JsonDeserializer) this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    public JsonDeserializer<?> findCollectionDeserializer(CollectionType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        return this._classMappings == null ? null : (JsonDeserializer) this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    public JsonDeserializer<?> findCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        return this._classMappings == null ? null : (JsonDeserializer) this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        JsonDeserializer<?> deser = (JsonDeserializer) this._classMappings.get(new ClassKey(type));
        if (deser == null && this._hasEnumDeserializer && type.isEnum()) {
            return (JsonDeserializer) this._classMappings.get(new ClassKey(Enum.class));
        }
        return deser;
    }

    public JsonDeserializer<?> findMapDeserializer(MapType type, DeserializationConfig config, BeanDescription beanDesc, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        return this._classMappings == null ? null : (JsonDeserializer) this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    public JsonDeserializer<?> findMapLikeDeserializer(MapLikeType type, DeserializationConfig config, BeanDescription beanDesc, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        return this._classMappings == null ? null : (JsonDeserializer) this._classMappings.get(new ClassKey(type.getRawClass()));
    }

    public JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> nodeType, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        return this._classMappings == null ? null : (JsonDeserializer) this._classMappings.get(new ClassKey(nodeType));
    }
}
