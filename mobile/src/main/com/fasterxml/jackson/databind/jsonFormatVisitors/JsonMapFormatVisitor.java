package com.fasterxml.jackson.databind.jsonFormatVisitors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public interface JsonMapFormatVisitor extends JsonFormatVisitorWithSerializerProvider {

    public static class Base implements JsonMapFormatVisitor {
        protected SerializerProvider _provider;

        public Base(SerializerProvider p) {
            this._provider = p;
        }

        public SerializerProvider getProvider() {
            return this._provider;
        }

        public void setProvider(SerializerProvider p) {
            this._provider = p;
        }

        public void keyFormat(JsonFormatVisitable handler, JavaType keyType) throws JsonMappingException {
        }

        public void valueFormat(JsonFormatVisitable handler, JavaType valueType) throws JsonMappingException {
        }
    }

    void keyFormat(JsonFormatVisitable jsonFormatVisitable, JavaType javaType) throws JsonMappingException;

    void valueFormat(JsonFormatVisitable jsonFormatVisitable, JavaType javaType) throws JsonMappingException;
}
