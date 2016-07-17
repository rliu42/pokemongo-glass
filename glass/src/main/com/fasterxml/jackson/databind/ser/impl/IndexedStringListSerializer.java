package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;
import java.io.IOException;
import java.util.List;

@JacksonStdImpl
public final class IndexedStringListSerializer extends StaticListSerializerBase<List<String>> {
    public static final IndexedStringListSerializer instance;
    private static final long serialVersionUID = 1;

    static {
        instance = new IndexedStringListSerializer();
    }

    protected IndexedStringListSerializer() {
        super(List.class);
    }

    public IndexedStringListSerializer(IndexedStringListSerializer src, JsonSerializer<?> ser, Boolean unwrapSingle) {
        super(src, ser, unwrapSingle);
    }

    public JsonSerializer<?> _withResolved(BeanProperty prop, JsonSerializer<?> ser, Boolean unwrapSingle) {
        return new IndexedStringListSerializer(this, ser, unwrapSingle);
    }

    protected JsonNode contentSchema() {
        return createSchemaNode("string", true);
    }

    protected void acceptContentVisitor(JsonArrayFormatVisitor visitor) throws JsonMappingException {
        visitor.itemsFormat(JsonFormatTypes.STRING);
    }

    public void serialize(List<String> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        int len = value.size();
        if (len == 1 && ((this._unwrapSingle == null && provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) || this._unwrapSingle == Boolean.TRUE)) {
            _serializeUnwrapped(value, gen, provider);
            return;
        }
        gen.writeStartArray(len);
        if (this._serializer == null) {
            serializeContents(value, gen, provider, len);
        } else {
            serializeUsingCustom(value, gen, provider, len);
        }
        gen.writeEndArray();
    }

    private final void _serializeUnwrapped(List<String> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (this._serializer == null) {
            serializeContents(value, gen, provider, 1);
        } else {
            serializeUsingCustom(value, gen, provider, 1);
        }
    }

    public void serializeWithType(List<String> value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        int len = value.size();
        typeSer.writeTypePrefixForArray(value, gen);
        if (this._serializer == null) {
            serializeContents(value, gen, provider, len);
        } else {
            serializeUsingCustom(value, gen, provider, len);
        }
        typeSer.writeTypeSuffixForArray(value, gen);
    }

    private final void serializeContents(List<String> value, JsonGenerator gen, SerializerProvider provider, int len) throws IOException {
        int i = 0;
        while (i < len) {
            try {
                String str = (String) value.get(i);
                if (str == null) {
                    provider.defaultSerializeNull(gen);
                } else {
                    gen.writeString(str);
                }
                i++;
            } catch (Exception e) {
                wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                return;
            }
        }
    }

    private final void serializeUsingCustom(List<String> value, JsonGenerator gen, SerializerProvider provider, int len) throws IOException {
        try {
            JsonSerializer<String> ser = this._serializer;
            for (int i = 0; i < len; i++) {
                String str = (String) value.get(i);
                if (str == null) {
                    provider.defaultSerializeNull(gen);
                } else {
                    ser.serialize(str, gen, provider);
                }
            }
        } catch (Exception e) {
            wrapAndThrow(provider, (Throwable) e, (Object) value, 0);
        }
    }
}
