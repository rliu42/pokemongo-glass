package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ArrayBuilders.BooleanBuilder;
import com.fasterxml.jackson.databind.util.ArrayBuilders.ByteBuilder;
import com.fasterxml.jackson.databind.util.ArrayBuilders.DoubleBuilder;
import com.fasterxml.jackson.databind.util.ArrayBuilders.FloatBuilder;
import com.fasterxml.jackson.databind.util.ArrayBuilders.IntBuilder;
import com.fasterxml.jackson.databind.util.ArrayBuilders.LongBuilder;
import com.fasterxml.jackson.databind.util.ArrayBuilders.ShortBuilder;
import java.io.IOException;

public abstract class PrimitiveArrayDeserializers<T> extends StdDeserializer<T> {

    @JacksonStdImpl
    static final class BooleanDeser extends PrimitiveArrayDeserializers<boolean[]> {
        private static final long serialVersionUID = 1;

        public BooleanDeser() {
            super(boolean[].class);
        }

        public boolean[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            BooleanBuilder builder = ctxt.getArrayBuilders().getBooleanBuilder();
            Object chunk = (boolean[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                try {
                    int ix2;
                    boolean value = _parseBooleanPrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (boolean[]) builder.appendCompletedChunk(chunk, ix);
                        ix2 = 0;
                    } else {
                        ix2 = ix;
                    }
                    ix = ix2 + 1;
                    chunk[ix2] = value;
                } catch (Throwable e) {
                    throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
                }
            }
            return (boolean[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final boolean[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new boolean[]{_parseBooleanPrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class ByteDeser extends PrimitiveArrayDeserializers<byte[]> {
        private static final long serialVersionUID = 1;

        public ByteDeser() {
            super(byte[].class);
        }

        public byte[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_STRING) {
                return jp.getBinaryValue(ctxt.getBase64Variant());
            }
            if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = jp.getEmbeddedObject();
                if (ob == null) {
                    return null;
                }
                if (ob instanceof byte[]) {
                    return (byte[]) ob;
                }
            }
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ByteBuilder builder = ctxt.getArrayBuilders().getByteBuilder();
            Object chunk = (byte[]) builder.resetAndStart();
            int ix = 0;
            while (true) {
                try {
                    t = jp.nextToken();
                    if (t == JsonToken.END_ARRAY) {
                        return (byte[]) builder.completeAndClearBuffer(chunk, ix);
                    }
                    byte value;
                    int ix2;
                    if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                        value = jp.getByteValue();
                    } else if (t != JsonToken.VALUE_NULL) {
                        break;
                    } else {
                        value = (byte) 0;
                    }
                    if (ix >= chunk.length) {
                        chunk = (byte[]) builder.appendCompletedChunk(chunk, ix);
                        ix2 = 0;
                    } else {
                        ix2 = ix;
                    }
                    ix = ix2 + 1;
                    chunk[ix2] = value;
                } catch (Throwable e) {
                    throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
                }
            }
            throw ctxt.mappingException(this._valueClass.getComponentType());
        }

        private final byte[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                byte value;
                JsonToken t = jp.getCurrentToken();
                if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                    value = jp.getByteValue();
                } else if (t != JsonToken.VALUE_NULL) {
                    throw ctxt.mappingException(this._valueClass.getComponentType());
                } else {
                    value = (byte) 0;
                }
                return new byte[]{value};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class CharDeser extends PrimitiveArrayDeserializers<char[]> {
        private static final long serialVersionUID = 1;

        public CharDeser() {
            super(char[].class);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public char[] deserialize(com.fasterxml.jackson.core.JsonParser r12, com.fasterxml.jackson.databind.DeserializationContext r13) throws java.io.IOException {
            /*
            r11 = this;
            r10 = 0;
            r7 = r12.getCurrentToken();
            r8 = com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
            if (r7 != r8) goto L_0x001b;
        L_0x0009:
            r0 = r12.getTextCharacters();
            r3 = r12.getTextOffset();
            r1 = r12.getTextLength();
            r4 = new char[r1];
            java.lang.System.arraycopy(r0, r3, r4, r10, r1);
        L_0x001a:
            return r4;
        L_0x001b:
            r8 = r12.isExpectedStartArrayToken();
            if (r8 == 0) goto L_0x0079;
        L_0x0021:
            r5 = new java.lang.StringBuilder;
            r8 = 64;
            r5.<init>(r8);
        L_0x0028:
            r7 = r12.nextToken();
            r8 = com.fasterxml.jackson.core.JsonToken.END_ARRAY;
            if (r7 == r8) goto L_0x0070;
        L_0x0030:
            r8 = com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
            if (r7 == r8) goto L_0x003b;
        L_0x0034:
            r8 = java.lang.Character.TYPE;
            r8 = r13.mappingException(r8);
            throw r8;
        L_0x003b:
            r6 = r12.getText();
            r8 = r6.length();
            r9 = 1;
            if (r8 == r9) goto L_0x0068;
        L_0x0046:
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "Can not convert a JSON String of length ";
            r8 = r8.append(r9);
            r9 = r6.length();
            r8 = r8.append(r9);
            r9 = " into a char element of char array";
            r8 = r8.append(r9);
            r8 = r8.toString();
            r8 = com.fasterxml.jackson.databind.JsonMappingException.from(r12, r8);
            throw r8;
        L_0x0068:
            r8 = r6.charAt(r10);
            r5.append(r8);
            goto L_0x0028;
        L_0x0070:
            r8 = r5.toString();
            r4 = r8.toCharArray();
            goto L_0x001a;
        L_0x0079:
            r8 = com.fasterxml.jackson.core.JsonToken.VALUE_EMBEDDED_OBJECT;
            if (r7 != r8) goto L_0x00b0;
        L_0x007d:
            r2 = r12.getEmbeddedObject();
            if (r2 != 0) goto L_0x0085;
        L_0x0083:
            r4 = 0;
            goto L_0x001a;
        L_0x0085:
            r8 = r2 instanceof char[];
            if (r8 == 0) goto L_0x008f;
        L_0x0089:
            r2 = (char[]) r2;
            r2 = (char[]) r2;
            r4 = r2;
            goto L_0x001a;
        L_0x008f:
            r8 = r2 instanceof java.lang.String;
            if (r8 == 0) goto L_0x009a;
        L_0x0093:
            r2 = (java.lang.String) r2;
            r4 = r2.toCharArray();
            goto L_0x001a;
        L_0x009a:
            r8 = r2 instanceof byte[];
            if (r8 == 0) goto L_0x00b0;
        L_0x009e:
            r8 = com.fasterxml.jackson.core.Base64Variants.getDefaultVariant();
            r2 = (byte[]) r2;
            r2 = (byte[]) r2;
            r8 = r8.encode(r2, r10);
            r4 = r8.toCharArray();
            goto L_0x001a;
        L_0x00b0:
            r8 = r11._valueClass;
            r8 = r13.mappingException(r8);
            throw r8;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.databind.deser.std.PrimitiveArrayDeserializers.CharDeser.deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext):char[]");
        }
    }

    @JacksonStdImpl
    static final class DoubleDeser extends PrimitiveArrayDeserializers<double[]> {
        private static final long serialVersionUID = 1;

        public DoubleDeser() {
            super(double[].class);
        }

        public double[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            DoubleBuilder builder = ctxt.getArrayBuilders().getDoubleBuilder();
            Object chunk = (double[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                try {
                    int ix2;
                    double value = _parseDoublePrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (double[]) builder.appendCompletedChunk(chunk, ix);
                        ix2 = 0;
                    } else {
                        ix2 = ix;
                    }
                    ix = ix2 + 1;
                    chunk[ix2] = value;
                } catch (Throwable e) {
                    throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
                }
            }
            return (double[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final double[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new double[]{_parseDoublePrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class FloatDeser extends PrimitiveArrayDeserializers<float[]> {
        private static final long serialVersionUID = 1;

        public FloatDeser() {
            super(float[].class);
        }

        public float[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            FloatBuilder builder = ctxt.getArrayBuilders().getFloatBuilder();
            Object chunk = (float[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                try {
                    int ix2;
                    float value = _parseFloatPrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (float[]) builder.appendCompletedChunk(chunk, ix);
                        ix2 = 0;
                    } else {
                        ix2 = ix;
                    }
                    ix = ix2 + 1;
                    chunk[ix2] = value;
                } catch (Throwable e) {
                    throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
                }
            }
            return (float[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final float[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new float[]{_parseFloatPrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class IntDeser extends PrimitiveArrayDeserializers<int[]> {
        public static final IntDeser instance;
        private static final long serialVersionUID = 1;

        static {
            instance = new IntDeser();
        }

        public IntDeser() {
            super(int[].class);
        }

        public int[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            IntBuilder builder = ctxt.getArrayBuilders().getIntBuilder();
            Object chunk = (int[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                try {
                    int ix2;
                    int value = _parseIntPrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (int[]) builder.appendCompletedChunk(chunk, ix);
                        ix2 = 0;
                    } else {
                        ix2 = ix;
                    }
                    ix = ix2 + 1;
                    chunk[ix2] = value;
                } catch (Throwable e) {
                    throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
                }
            }
            return (int[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final int[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new int[]{_parseIntPrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class LongDeser extends PrimitiveArrayDeserializers<long[]> {
        public static final LongDeser instance;
        private static final long serialVersionUID = 1;

        static {
            instance = new LongDeser();
        }

        public LongDeser() {
            super(long[].class);
        }

        public long[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            LongBuilder builder = ctxt.getArrayBuilders().getLongBuilder();
            Object chunk = (long[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                try {
                    int ix2;
                    long value = _parseLongPrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (long[]) builder.appendCompletedChunk(chunk, ix);
                        ix2 = 0;
                    } else {
                        ix2 = ix;
                    }
                    ix = ix2 + 1;
                    chunk[ix2] = value;
                } catch (Throwable e) {
                    throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
                }
            }
            return (long[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final long[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new long[]{_parseLongPrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class ShortDeser extends PrimitiveArrayDeserializers<short[]> {
        private static final long serialVersionUID = 1;

        public ShortDeser() {
            super(short[].class);
        }

        public short[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ShortBuilder builder = ctxt.getArrayBuilders().getShortBuilder();
            Object chunk = (short[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                try {
                    int ix2;
                    short value = _parseShortPrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (short[]) builder.appendCompletedChunk(chunk, ix);
                        ix2 = 0;
                    } else {
                        ix2 = ix;
                    }
                    ix = ix2 + 1;
                    chunk[ix2] = value;
                } catch (Throwable e) {
                    throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
                }
            }
            return (short[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final short[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new short[]{_parseShortPrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    protected PrimitiveArrayDeserializers(Class<T> cls) {
        super((Class) cls);
    }

    public static JsonDeserializer<?> forType(Class<?> rawType) {
        if (rawType == Integer.TYPE) {
            return IntDeser.instance;
        }
        if (rawType == Long.TYPE) {
            return LongDeser.instance;
        }
        if (rawType == Byte.TYPE) {
            return new ByteDeser();
        }
        if (rawType == Short.TYPE) {
            return new ShortDeser();
        }
        if (rawType == Float.TYPE) {
            return new FloatDeser();
        }
        if (rawType == Double.TYPE) {
            return new DoubleDeser();
        }
        if (rawType == Boolean.TYPE) {
            return new BooleanDeser();
        }
        if (rawType == Character.TYPE) {
            return new CharDeser();
        }
        throw new IllegalStateException();
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }
}
