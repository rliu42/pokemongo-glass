package com.fasterxml.jackson.databind.util;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class EnumValues implements Serializable {
    private static final long serialVersionUID = 1;
    private transient EnumMap<?, SerializableString> _asMap;
    private final Class<Enum<?>> _enumClass;
    private final SerializableString[] _textual;
    private final Enum<?>[] _values;

    private EnumValues(Class<Enum<?>> enumClass, SerializableString[] textual) {
        this._enumClass = enumClass;
        this._values = (Enum[]) enumClass.getEnumConstants();
        this._textual = textual;
    }

    public static EnumValues construct(SerializationConfig config, Class<Enum<?>> enumClass) {
        if (config.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
            return constructFromToString(config, enumClass);
        }
        return constructFromName(config, enumClass);
    }

    public static EnumValues constructFromName(MapperConfig<?> config, Class<Enum<?>> enumClass) {
        Enum[] values = (Enum[]) ClassUtil.findEnumType((Class) enumClass).getEnumConstants();
        if (values != null) {
            SerializableString[] textual = new SerializableString[values.length];
            for (Enum<?> en : values) {
                textual[en.ordinal()] = config.compileString(config.getAnnotationIntrospector().findEnumValue(en));
            }
            return new EnumValues(enumClass, textual);
        }
        throw new IllegalArgumentException("Can not determine enum constants for Class " + enumClass.getName());
    }

    public static EnumValues constructFromToString(MapperConfig<?> config, Class<Enum<?>> enumClass) {
        Enum[] values = (Enum[]) ClassUtil.findEnumType((Class) enumClass).getEnumConstants();
        if (values != null) {
            SerializableString[] textual = new SerializableString[values.length];
            for (Enum<?> en : values) {
                textual[en.ordinal()] = config.compileString(en.toString());
            }
            return new EnumValues(enumClass, textual);
        }
        throw new IllegalArgumentException("Can not determine enum constants for Class " + enumClass.getName());
    }

    public SerializableString serializedValueFor(Enum<?> key) {
        return this._textual[key.ordinal()];
    }

    public Collection<SerializableString> values() {
        return Arrays.asList(this._textual);
    }

    public List<Enum<?>> enums() {
        return Arrays.asList(this._values);
    }

    public EnumMap<?, SerializableString> internalMap() {
        EnumMap<?, SerializableString> result = this._asMap;
        if (result != null) {
            return result;
        }
        Map<Enum<?>, SerializableString> map = new LinkedHashMap();
        for (Enum<?> en : this._values) {
            map.put(en, this._textual[en.ordinal()]);
        }
        return new EnumMap(map);
    }

    public Class<Enum<?>> getEnumClass() {
        return this._enumClass;
    }
}
