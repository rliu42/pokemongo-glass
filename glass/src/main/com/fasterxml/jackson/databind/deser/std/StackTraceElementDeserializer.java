package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import spacemadness.com.lunarconsole.BuildConfig;

public class StackTraceElementDeserializer extends StdScalarDeserializer<StackTraceElement> {
    private static final long serialVersionUID = 1;

    public StackTraceElementDeserializer() {
        super(StackTraceElement.class);
    }

    public StackTraceElement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            String className = BuildConfig.FLAVOR;
            String methodName = BuildConfig.FLAVOR;
            String fileName = BuildConfig.FLAVOR;
            int lineNumber = -1;
            while (true) {
                t = jp.nextValue();
                if (t == JsonToken.END_OBJECT) {
                    return new StackTraceElement(className, methodName, fileName, lineNumber);
                }
                String propName = jp.getCurrentName();
                if ("className".equals(propName)) {
                    className = jp.getText();
                } else if ("fileName".equals(propName)) {
                    fileName = jp.getText();
                } else if ("lineNumber".equals(propName)) {
                    if (!t.isNumeric()) {
                        break;
                    }
                    lineNumber = jp.getIntValue();
                } else if ("methodName".equals(propName)) {
                    methodName = jp.getText();
                } else if (!"nativeMethod".equals(propName)) {
                    handleUnknownProperty(jp, ctxt, this._valueClass, propName);
                }
            }
            throw JsonMappingException.from(jp, "Non-numeric token (" + t + ") for property 'lineNumber'");
        } else if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
            jp.nextToken();
            StackTraceElement value = deserialize(jp, ctxt);
            if (jp.nextToken() == JsonToken.END_ARRAY) {
                return value;
            }
            throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'java.lang.StackTraceElement' value but there was more than a single value in the array");
        } else {
            throw ctxt.mappingException(this._valueClass, t);
        }
    }
}
