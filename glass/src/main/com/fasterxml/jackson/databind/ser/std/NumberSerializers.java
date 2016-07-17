package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import spacemadness.com.lunarconsole.C1391R;

public class NumberSerializers {

    /* renamed from: com.fasterxml.jackson.databind.ser.std.NumberSerializers.1 */
    static /* synthetic */ class C01541 {
        static final /* synthetic */ int[] $SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape;

        static {
            $SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape = new int[Shape.values().length];
            try {
                $SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape[Shape.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    protected static abstract class Base<T> extends StdScalarSerializer<T> implements ContextualSerializer {
        protected static final Integer EMPTY_INTEGER;
        protected final boolean _isInt;
        protected final NumberType _numberType;
        protected final String _schemaType;

        static {
            EMPTY_INTEGER = Integer.valueOf(0);
        }

        protected Base(Class<?> cls, NumberType numberType, String schemaType) {
            boolean z = false;
            super(cls, false);
            this._numberType = numberType;
            this._schemaType = schemaType;
            if (numberType == NumberType.INT || numberType == NumberType.LONG || numberType == NumberType.BIG_INTEGER) {
                z = true;
            }
            this._isInt = z;
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(this._schemaType, true);
        }

        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            if (this._isInt) {
                JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
                if (v2 != null) {
                    v2.numberType(this._numberType);
                    return;
                }
                return;
            }
            JsonNumberFormatVisitor v22 = visitor.expectNumberFormat(typeHint);
            if (v22 != null) {
                v22.numberType(this._numberType);
            }
        }

        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
            if (property == null) {
                return this;
            }
            AnnotatedMember m = property.getMember();
            if (m == null) {
                return this;
            }
            Value format = prov.getAnnotationIntrospector().findFormat(m);
            if (format == null) {
                return this;
            }
            switch (C01541.$SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape[format.getShape().ordinal()]) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    return ToStringSerializer.instance;
                default:
                    return this;
            }
        }
    }

    @JacksonStdImpl
    public static final class DoubleSerializer extends Base<Object> {
        private static final Double EMPTY;
        static final DoubleSerializer instance;

        public /* bridge */ /* synthetic */ void acceptJsonFormatVisitor(JsonFormatVisitorWrapper x0, JavaType x1) throws JsonMappingException {
            super.acceptJsonFormatVisitor(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonSerializer createContextual(SerializerProvider x0, BeanProperty x1) throws JsonMappingException {
            return super.createContextual(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonNode getSchema(SerializerProvider x0, Type x1) {
            return super.getSchema(x0, x1);
        }

        static {
            EMPTY = Double.valueOf(0.0d);
            instance = new DoubleSerializer();
        }

        public DoubleSerializer() {
            super(Double.class, NumberType.DOUBLE, "number");
        }

        public boolean isEmpty(SerializerProvider prov, Object value) {
            return EMPTY.equals(value);
        }

        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Double) value).doubleValue());
        }

        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            serialize(value, gen, provider);
        }
    }

    @JacksonStdImpl
    public static final class FloatSerializer extends Base<Object> {
        private static final Float EMPTY;
        static final FloatSerializer instance;

        public /* bridge */ /* synthetic */ void acceptJsonFormatVisitor(JsonFormatVisitorWrapper x0, JavaType x1) throws JsonMappingException {
            super.acceptJsonFormatVisitor(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonSerializer createContextual(SerializerProvider x0, BeanProperty x1) throws JsonMappingException {
            return super.createContextual(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonNode getSchema(SerializerProvider x0, Type x1) {
            return super.getSchema(x0, x1);
        }

        static {
            EMPTY = Float.valueOf(0.0f);
            instance = new FloatSerializer();
        }

        public FloatSerializer() {
            super(Float.class, NumberType.FLOAT, "number");
        }

        public boolean isEmpty(SerializerProvider prov, Object value) {
            return EMPTY.equals(value);
        }

        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Float) value).floatValue());
        }
    }

    @JacksonStdImpl
    public static final class IntLikeSerializer extends Base<Number> {
        static final IntLikeSerializer instance;

        public /* bridge */ /* synthetic */ void acceptJsonFormatVisitor(JsonFormatVisitorWrapper x0, JavaType x1) throws JsonMappingException {
            super.acceptJsonFormatVisitor(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonSerializer createContextual(SerializerProvider x0, BeanProperty x1) throws JsonMappingException {
            return super.createContextual(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonNode getSchema(SerializerProvider x0, Type x1) {
            return super.getSchema(x0, x1);
        }

        static {
            instance = new IntLikeSerializer();
        }

        public IntLikeSerializer() {
            super(Number.class, NumberType.INT, "integer");
        }

        public boolean isEmpty(SerializerProvider prov, Number value) {
            return value.intValue() == 0;
        }

        public void serialize(Number value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value.intValue());
        }
    }

    @JacksonStdImpl
    public static final class IntegerSerializer extends Base<Object> {
        public /* bridge */ /* synthetic */ void acceptJsonFormatVisitor(JsonFormatVisitorWrapper x0, JavaType x1) throws JsonMappingException {
            super.acceptJsonFormatVisitor(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonSerializer createContextual(SerializerProvider x0, BeanProperty x1) throws JsonMappingException {
            return super.createContextual(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonNode getSchema(SerializerProvider x0, Type x1) {
            return super.getSchema(x0, x1);
        }

        public IntegerSerializer() {
            super(Integer.class, NumberType.INT, "integer");
        }

        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Integer) value).intValue());
        }

        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            serialize(value, gen, provider);
        }

        public boolean isEmpty(SerializerProvider prov, Object value) {
            return EMPTY_INTEGER.equals(value);
        }
    }

    @JacksonStdImpl
    public static final class LongSerializer extends Base<Object> {
        private static final Long EMPTY;
        static final LongSerializer instance;

        public /* bridge */ /* synthetic */ void acceptJsonFormatVisitor(JsonFormatVisitorWrapper x0, JavaType x1) throws JsonMappingException {
            super.acceptJsonFormatVisitor(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonSerializer createContextual(SerializerProvider x0, BeanProperty x1) throws JsonMappingException {
            return super.createContextual(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonNode getSchema(SerializerProvider x0, Type x1) {
            return super.getSchema(x0, x1);
        }

        static {
            EMPTY = Long.valueOf(0);
            instance = new LongSerializer();
        }

        public LongSerializer() {
            super(Long.class, NumberType.LONG, "number");
        }

        public boolean isEmpty(SerializerProvider prov, Object value) {
            return EMPTY.equals(value);
        }

        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Long) value).longValue());
        }
    }

    @JacksonStdImpl
    public static final class ShortSerializer extends Base<Short> {
        private static final Short EMPTY;
        static final ShortSerializer instance;

        public /* bridge */ /* synthetic */ void acceptJsonFormatVisitor(JsonFormatVisitorWrapper x0, JavaType x1) throws JsonMappingException {
            super.acceptJsonFormatVisitor(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonSerializer createContextual(SerializerProvider x0, BeanProperty x1) throws JsonMappingException {
            return super.createContextual(x0, x1);
        }

        public /* bridge */ /* synthetic */ JsonNode getSchema(SerializerProvider x0, Type x1) {
            return super.getSchema(x0, x1);
        }

        static {
            EMPTY = Short.valueOf((short) 0);
            instance = new ShortSerializer();
        }

        public ShortSerializer() {
            super(Short.class, NumberType.INT, "number");
        }

        public boolean isEmpty(SerializerProvider prov, Short value) {
            return EMPTY.equals(value);
        }

        public void serialize(Short value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value.shortValue());
        }
    }

    protected NumberSerializers() {
    }

    public static void addAll(Map<String, JsonSerializer<?>> allDeserializers) {
        JsonSerializer<?> intS = new IntegerSerializer();
        allDeserializers.put(Integer.class.getName(), intS);
        allDeserializers.put(Integer.TYPE.getName(), intS);
        allDeserializers.put(Long.class.getName(), LongSerializer.instance);
        allDeserializers.put(Long.TYPE.getName(), LongSerializer.instance);
        allDeserializers.put(Byte.class.getName(), IntLikeSerializer.instance);
        allDeserializers.put(Byte.TYPE.getName(), IntLikeSerializer.instance);
        allDeserializers.put(Short.class.getName(), ShortSerializer.instance);
        allDeserializers.put(Short.TYPE.getName(), ShortSerializer.instance);
        allDeserializers.put(Float.class.getName(), FloatSerializer.instance);
        allDeserializers.put(Float.TYPE.getName(), FloatSerializer.instance);
        allDeserializers.put(Double.class.getName(), DoubleSerializer.instance);
        allDeserializers.put(Double.TYPE.getName(), DoubleSerializer.instance);
    }
}
