package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
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
import java.util.Collection;

@JacksonStdImpl
public class StringCollectionSerializer extends StaticListSerializerBase<Collection<String>> {
    public static final StringCollectionSerializer instance;

    static {
        instance = new StringCollectionSerializer();
    }

    protected StringCollectionSerializer() {
        super(Collection.class);
    }

    protected StringCollectionSerializer(StringCollectionSerializer src, JsonSerializer<?> ser, Boolean unwrapSingle) {
        super(src, ser, unwrapSingle);
    }

    public JsonSerializer<?> _withResolved(BeanProperty prop, JsonSerializer<?> ser, Boolean unwrapSingle) {
        return new StringCollectionSerializer(this, ser, unwrapSingle);
    }

    protected JsonNode contentSchema() {
        return createSchemaNode("string", true);
    }

    protected void acceptContentVisitor(JsonArrayFormatVisitor visitor) throws JsonMappingException {
        visitor.itemsFormat(JsonFormatTypes.STRING);
    }

    public void serialize(Collection<String> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        int len = value.size();
        if (len == 1 && ((this._unwrapSingle == null && provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) || this._unwrapSingle == Boolean.TRUE)) {
            _serializeUnwrapped(value, gen, provider);
            return;
        }
        gen.writeStartArray(len);
        if (this._serializer == null) {
            serializeContents(value, gen, provider);
        } else {
            serializeUsingCustom(value, gen, provider);
        }
        gen.writeEndArray();
    }

    private final void _serializeUnwrapped(Collection<String> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (this._serializer == null) {
            serializeContents(value, gen, provider);
        } else {
            serializeUsingCustom(value, gen, provider);
        }
    }

    public void serializeWithType(Collection<String> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
        typeSer.writeTypePrefixForArray(value, jgen);
        if (this._serializer == null) {
            serializeContents(value, jgen, provider);
        } else {
            serializeUsingCustom(value, jgen, provider);
        }
        typeSer.writeTypeSuffixForArray(value, jgen);
    }

    private final void serializeContents(Collection<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (this._serializer != null) {
            serializeUsingCustom(value, jgen, provider);
            return;
        }
        int i = 0;
        for (String str : value) {
            if (str == null) {
                try {
                    provider.defaultSerializeNull(jgen);
                } catch (Exception e) {
                    wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                }
            } else {
                jgen.writeString(str);
            }
            i++;
        }
    }

    private void serializeUsingCustom(Collection<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        JsonSerializer<String> ser = this._serializer;
        for (String str : value) {
            if (str == null) {
                try {
                    provider.defaultSerializeNull(jgen);
                } catch (Exception e) {
                    wrapAndThrow(provider, (Throwable) e, (Object) value, 0);
                }
            } else {
                ser.serialize(str, jgen, provider);
            }
        }
    }
}
