package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.AnyGetterWriter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import spacemadness.com.lunarconsole.BuildConfig;

public abstract class BeanSerializerBase extends StdSerializer<Object> implements ContextualSerializer, ResolvableSerializer, JsonFormatVisitable, SchemaAware {
    protected static final PropertyName NAME_FOR_OBJECT_REF;
    protected static final BeanPropertyWriter[] NO_PROPS;
    protected final AnyGetterWriter _anyGetterWriter;
    protected final BeanPropertyWriter[] _filteredProps;
    protected final ObjectIdWriter _objectIdWriter;
    protected final Object _propertyFilterId;
    protected final BeanPropertyWriter[] _props;
    protected final Shape _serializationShape;
    protected final AnnotatedMember _typeId;

    /* renamed from: com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.1 */
    static /* synthetic */ class C01531 {
        static final /* synthetic */ int[] $SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape;

        static {
            $SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape = new int[Shape.values().length];
            try {
                $SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape[Shape.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape[Shape.NUMBER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape[Shape.NUMBER_INT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    protected abstract BeanSerializerBase asArraySerializer();

    public abstract void serialize(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException;

    public abstract BeanSerializerBase withFilterId(Object obj);

    protected abstract BeanSerializerBase withIgnorals(String[] strArr);

    public abstract BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter);

    static {
        NAME_FOR_OBJECT_REF = new PropertyName("#object-ref");
        NO_PROPS = new BeanPropertyWriter[0];
    }

    protected BeanSerializerBase(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
        Shape shape = null;
        super(type);
        this._props = properties;
        this._filteredProps = filteredProperties;
        if (builder == null) {
            this._typeId = null;
            this._anyGetterWriter = null;
            this._propertyFilterId = null;
            this._objectIdWriter = null;
            this._serializationShape = null;
            return;
        }
        this._typeId = builder.getTypeId();
        this._anyGetterWriter = builder.getAnyGetter();
        this._propertyFilterId = builder.getFilterId();
        this._objectIdWriter = builder.getObjectIdWriter();
        Value format = builder.getBeanDescription().findExpectedFormat(null);
        if (format != null) {
            shape = format.getShape();
        }
        this._serializationShape = shape;
    }

    public BeanSerializerBase(BeanSerializerBase src, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
        super(src._handledType);
        this._props = properties;
        this._filteredProps = filteredProperties;
        this._typeId = src._typeId;
        this._anyGetterWriter = src._anyGetterWriter;
        this._objectIdWriter = src._objectIdWriter;
        this._propertyFilterId = src._propertyFilterId;
        this._serializationShape = src._serializationShape;
    }

    protected BeanSerializerBase(BeanSerializerBase src, ObjectIdWriter objectIdWriter) {
        this(src, objectIdWriter, src._propertyFilterId);
    }

    protected BeanSerializerBase(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId) {
        super(src._handledType);
        this._props = src._props;
        this._filteredProps = src._filteredProps;
        this._typeId = src._typeId;
        this._anyGetterWriter = src._anyGetterWriter;
        this._objectIdWriter = objectIdWriter;
        this._propertyFilterId = filterId;
        this._serializationShape = src._serializationShape;
    }

    protected BeanSerializerBase(BeanSerializerBase src, String[] toIgnore) {
        BeanPropertyWriter[] beanPropertyWriterArr = null;
        super(src._handledType);
        HashSet<String> ignoredSet = ArrayBuilders.arrayToSet(toIgnore);
        BeanPropertyWriter[] propsIn = src._props;
        BeanPropertyWriter[] fpropsIn = src._filteredProps;
        int len = propsIn.length;
        ArrayList<BeanPropertyWriter> propsOut = new ArrayList(len);
        ArrayList<BeanPropertyWriter> fpropsOut = fpropsIn == null ? null : new ArrayList(len);
        for (int i = 0; i < len; i++) {
            BeanPropertyWriter bpw = propsIn[i];
            if (!ignoredSet.contains(bpw.getName())) {
                propsOut.add(bpw);
                if (fpropsIn != null) {
                    fpropsOut.add(fpropsIn[i]);
                }
            }
        }
        this._props = (BeanPropertyWriter[]) propsOut.toArray(new BeanPropertyWriter[propsOut.size()]);
        if (fpropsOut != null) {
            beanPropertyWriterArr = (BeanPropertyWriter[]) fpropsOut.toArray(new BeanPropertyWriter[fpropsOut.size()]);
        }
        this._filteredProps = beanPropertyWriterArr;
        this._typeId = src._typeId;
        this._anyGetterWriter = src._anyGetterWriter;
        this._objectIdWriter = src._objectIdWriter;
        this._propertyFilterId = src._propertyFilterId;
        this._serializationShape = src._serializationShape;
    }

    protected BeanSerializerBase(BeanSerializerBase src) {
        this(src, src._props, src._filteredProps);
    }

    protected BeanSerializerBase(BeanSerializerBase src, NameTransformer unwrapper) {
        this(src, rename(src._props, unwrapper), rename(src._filteredProps, unwrapper));
    }

    private static final BeanPropertyWriter[] rename(BeanPropertyWriter[] props, NameTransformer transformer) {
        if (props == null || props.length == 0 || transformer == null || transformer == NameTransformer.NOP) {
            return props;
        }
        int len = props.length;
        BeanPropertyWriter[] result = new BeanPropertyWriter[len];
        for (int i = 0; i < len; i++) {
            BeanPropertyWriter bpw = props[i];
            if (bpw != null) {
                result[i] = bpw.rename(transformer);
            }
        }
        return result;
    }

    public void resolve(SerializerProvider provider) throws JsonMappingException {
        int filteredCount;
        if (this._filteredProps == null) {
            filteredCount = 0;
        } else {
            filteredCount = this._filteredProps.length;
        }
        int len = this._props.length;
        for (int i = 0; i < len; i++) {
            BeanPropertyWriter w2;
            BeanProperty prop = this._props[i];
            if (!(prop.willSuppressNulls() || prop.hasNullSerializer())) {
                JsonSerializer<Object> nullSer = provider.findNullValueSerializer(prop);
                if (nullSer != null) {
                    prop.assignNullSerializer(nullSer);
                    if (i < filteredCount) {
                        w2 = this._filteredProps[i];
                        if (w2 != null) {
                            w2.assignNullSerializer(nullSer);
                        }
                    }
                }
            }
            if (!prop.hasSerializer()) {
                JsonSerializer<Object> ser = findConvertingSerializer(provider, prop);
                if (ser == null) {
                    JavaType type = prop.getSerializationType();
                    if (type == null) {
                        type = provider.constructType(prop.getGenericPropertyType());
                        if (!type.isFinal()) {
                            if (type.isContainerType() || type.containedTypeCount() > 0) {
                                prop.setNonTrivialBaseType(type);
                            }
                        }
                    }
                    ser = provider.findValueSerializer(type, prop);
                    if (type.isContainerType()) {
                        TypeSerializer typeSer = (TypeSerializer) type.getContentType().getTypeHandler();
                        if (typeSer != null && (ser instanceof ContainerSerializer)) {
                            ser = ((ContainerSerializer) ser).withValueTypeSerializer(typeSer);
                        }
                    }
                }
                prop.assignSerializer(ser);
                if (i < filteredCount) {
                    w2 = this._filteredProps[i];
                    if (w2 != null) {
                        w2.assignSerializer(ser);
                    }
                }
            }
        }
        if (this._anyGetterWriter != null) {
            this._anyGetterWriter.resolve(provider);
        }
    }

    protected JsonSerializer<Object> findConvertingSerializer(SerializerProvider provider, BeanPropertyWriter prop) throws JsonMappingException {
        JsonSerializer<?> ser = null;
        AnnotationIntrospector intr = provider.getAnnotationIntrospector();
        if (intr == null) {
            return null;
        }
        AnnotatedMember m = prop.getMember();
        if (m == null) {
            return null;
        }
        Object convDef = intr.findSerializationConverter(m);
        if (convDef == null) {
            return null;
        }
        Converter<Object, Object> conv = provider.converterInstance(prop.getMember(), convDef);
        JavaType delegateType = conv.getOutputType(provider.getTypeFactory());
        if (!delegateType.isJavaLangObject()) {
            ser = provider.findValueSerializer(delegateType, (BeanProperty) prop);
        }
        return new StdDelegatingSerializer(conv, delegateType, ser);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.fasterxml.jackson.databind.JsonSerializer<?> createContextual(com.fasterxml.jackson.databind.SerializerProvider r33, com.fasterxml.jackson.databind.BeanProperty r34) throws com.fasterxml.jackson.databind.JsonMappingException {
        /*
        r32 = this;
        r17 = r33.getAnnotationIntrospector();
        if (r34 == 0) goto L_0x0008;
    L_0x0006:
        if (r17 != 0) goto L_0x00f8;
    L_0x0008:
        r4 = 0;
    L_0x0009:
        r5 = r33.getConfig();
        r25 = 0;
        if (r4 == 0) goto L_0x0040;
    L_0x0011:
        r0 = r17;
        r9 = r0.findFormat(r4);
        if (r9 == 0) goto L_0x0040;
    L_0x0019:
        r25 = r9.getShape();
        r0 = r32;
        r0 = r0._serializationShape;
        r27 = r0;
        r0 = r25;
        r1 = r27;
        if (r0 == r1) goto L_0x0040;
    L_0x0029:
        r0 = r32;
        r0 = r0._handledType;
        r27 = r0;
        r27 = r27.isEnum();
        if (r27 == 0) goto L_0x0040;
    L_0x0035:
        r27 = com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.C01531.$SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape;
        r28 = r25.ordinal();
        r27 = r27[r28];
        switch(r27) {
            case 1: goto L_0x00fe;
            case 2: goto L_0x00fe;
            case 3: goto L_0x00fe;
            default: goto L_0x0040;
        };
    L_0x0040:
        r0 = r32;
        r0 = r0._objectIdWriter;
        r21 = r0;
        r15 = 0;
        r19 = 0;
        if (r4 == 0) goto L_0x00a2;
    L_0x004b:
        r27 = 1;
        r0 = r17;
        r1 = r27;
        r15 = r0.findPropertiesToIgnore(r4, r1);
        r0 = r17;
        r20 = r0.findObjectIdInfo(r4);
        if (r20 != 0) goto L_0x0127;
    L_0x005d:
        if (r21 == 0) goto L_0x0082;
    L_0x005f:
        r27 = new com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
        r28 = NAME_FOR_OBJECT_REF;
        r29 = 0;
        r30 = 0;
        r31 = 0;
        r27.<init>(r28, r29, r30, r31);
        r0 = r17;
        r1 = r27;
        r20 = r0.findObjectReferenceInfo(r4, r1);
        r0 = r32;
        r0 = r0._objectIdWriter;
        r27 = r0;
        r28 = r20.getAlwaysAsId();
        r21 = r27.withAlwaysAsId(r28);
    L_0x0082:
        r0 = r17;
        r8 = r0.findFilterId(r4);
        if (r8 == 0) goto L_0x00a2;
    L_0x008a:
        r0 = r32;
        r0 = r0._propertyFilterId;
        r27 = r0;
        if (r27 == 0) goto L_0x00a0;
    L_0x0092:
        r0 = r32;
        r0 = r0._propertyFilterId;
        r27 = r0;
        r0 = r27;
        r27 = r8.equals(r0);
        if (r27 != 0) goto L_0x00a2;
    L_0x00a0:
        r19 = r8;
    L_0x00a2:
        r6 = r32;
        if (r21 == 0) goto L_0x00d0;
    L_0x00a6:
        r0 = r21;
        r0 = r0.idType;
        r27 = r0;
        r0 = r33;
        r1 = r27;
        r2 = r34;
        r24 = r0.findValueSerializer(r1, r2);
        r0 = r21;
        r1 = r24;
        r21 = r0.withSerializer(r1);
        r0 = r32;
        r0 = r0._objectIdWriter;
        r27 = r0;
        r0 = r21;
        r1 = r27;
        if (r0 == r1) goto L_0x00d0;
    L_0x00ca:
        r0 = r21;
        r6 = r6.withObjectIdWriter(r0);
    L_0x00d0:
        if (r15 == 0) goto L_0x00db;
    L_0x00d2:
        r0 = r15.length;
        r27 = r0;
        if (r27 == 0) goto L_0x00db;
    L_0x00d7:
        r6 = r6.withIgnorals(r15);
    L_0x00db:
        if (r19 == 0) goto L_0x00e3;
    L_0x00dd:
        r0 = r19;
        r6 = r6.withFilterId(r0);
    L_0x00e3:
        if (r25 != 0) goto L_0x00eb;
    L_0x00e5:
        r0 = r32;
        r0 = r0._serializationShape;
        r25 = r0;
    L_0x00eb:
        r27 = com.fasterxml.jackson.annotation.JsonFormat.Shape.ARRAY;
        r0 = r25;
        r1 = r27;
        if (r0 != r1) goto L_0x00f7;
    L_0x00f3:
        r6 = r6.asArraySerializer();
    L_0x00f7:
        return r6;
    L_0x00f8:
        r4 = r34.getMember();
        goto L_0x0009;
    L_0x00fe:
        r0 = r32;
        r0 = r0._handledType;
        r27 = r0;
        r0 = r27;
        r7 = r5.introspectClassAnnotations(r0);
        r0 = r32;
        r0 = r0._handledType;
        r27 = r0;
        r28 = r33.getConfig();
        r0 = r27;
        r1 = r28;
        r24 = com.fasterxml.jackson.databind.ser.std.EnumSerializer.construct(r0, r1, r7, r9);
        r0 = r33;
        r1 = r24;
        r2 = r34;
        r6 = r0.handlePrimaryContextualization(r1, r2);
        goto L_0x00f7;
    L_0x0127:
        r0 = r17;
        r1 = r20;
        r20 = r0.findObjectReferenceInfo(r4, r1);
        r16 = r20.getGeneratorType();
        r0 = r33;
        r1 = r16;
        r26 = r0.constructType(r1);
        r27 = r33.getTypeFactory();
        r28 = com.fasterxml.jackson.annotation.ObjectIdGenerator.class;
        r0 = r27;
        r1 = r26;
        r2 = r28;
        r27 = r0.findTypeParameters(r1, r2);
        r28 = 0;
        r14 = r27[r28];
        r27 = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class;
        r0 = r16;
        r1 = r27;
        if (r0 != r1) goto L_0x023c;
    L_0x0157:
        r27 = r20.getPropertyName();
        r23 = r27.getSimpleName();
        r13 = 0;
        r12 = 0;
        r0 = r32;
        r0 = r0._props;
        r27 = r0;
        r0 = r27;
        r0 = r0.length;
        r18 = r0;
    L_0x016c:
        r0 = r18;
        if (r12 != r0) goto L_0x01a7;
    L_0x0170:
        r27 = new java.lang.IllegalArgumentException;
        r28 = new java.lang.StringBuilder;
        r28.<init>();
        r29 = "Invalid Object Id definition for ";
        r28 = r28.append(r29);
        r0 = r32;
        r0 = r0._handledType;
        r29 = r0;
        r29 = r29.getName();
        r28 = r28.append(r29);
        r29 = ": can not find property with name '";
        r28 = r28.append(r29);
        r0 = r28;
        r1 = r23;
        r28 = r0.append(r1);
        r29 = "'";
        r28 = r28.append(r29);
        r28 = r28.toString();
        r27.<init>(r28);
        throw r27;
    L_0x01a7:
        r0 = r32;
        r0 = r0._props;
        r27 = r0;
        r22 = r27[r12];
        r27 = r22.getName();
        r0 = r23;
        r1 = r27;
        r27 = r0.equals(r1);
        if (r27 == 0) goto L_0x0238;
    L_0x01bd:
        r13 = r22;
        if (r12 <= 0) goto L_0x021b;
    L_0x01c1:
        r0 = r32;
        r0 = r0._props;
        r27 = r0;
        r28 = 0;
        r0 = r32;
        r0 = r0._props;
        r29 = r0;
        r30 = 1;
        r0 = r27;
        r1 = r28;
        r2 = r29;
        r3 = r30;
        java.lang.System.arraycopy(r0, r1, r2, r3, r12);
        r0 = r32;
        r0 = r0._props;
        r27 = r0;
        r28 = 0;
        r27[r28] = r13;
        r0 = r32;
        r0 = r0._filteredProps;
        r27 = r0;
        if (r27 == 0) goto L_0x021b;
    L_0x01ee:
        r0 = r32;
        r0 = r0._filteredProps;
        r27 = r0;
        r10 = r27[r12];
        r0 = r32;
        r0 = r0._filteredProps;
        r27 = r0;
        r28 = 0;
        r0 = r32;
        r0 = r0._filteredProps;
        r29 = r0;
        r30 = 1;
        r0 = r27;
        r1 = r28;
        r2 = r29;
        r3 = r30;
        java.lang.System.arraycopy(r0, r1, r2, r3, r12);
        r0 = r32;
        r0 = r0._filteredProps;
        r27 = r0;
        r28 = 0;
        r27[r28] = r10;
    L_0x021b:
        r14 = r13.getType();
        r11 = new com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
        r0 = r20;
        r11.<init>(r0, r13);
        r27 = 0;
        r27 = (com.fasterxml.jackson.databind.PropertyName) r27;
        r28 = r20.getAlwaysAsId();
        r0 = r27;
        r1 = r28;
        r21 = com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter.construct(r14, r0, r11, r1);
        goto L_0x0082;
    L_0x0238:
        r12 = r12 + 1;
        goto L_0x016c;
    L_0x023c:
        r0 = r33;
        r1 = r20;
        r11 = r0.objectIdGeneratorInstance(r4, r1);
        r27 = r20.getPropertyName();
        r28 = r20.getAlwaysAsId();
        r0 = r27;
        r1 = r28;
        r21 = com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter.construct(r14, r0, r11, r1);
        goto L_0x0082;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.createContextual(com.fasterxml.jackson.databind.SerializerProvider, com.fasterxml.jackson.databind.BeanProperty):com.fasterxml.jackson.databind.JsonSerializer<?>");
    }

    public Iterator<PropertyWriter> properties() {
        return Arrays.asList(this._props).iterator();
    }

    public boolean usesObjectId() {
        return this._objectIdWriter != null;
    }

    public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        if (this._objectIdWriter != null) {
            gen.setCurrentValue(bean);
            _serializeWithObjectId(bean, gen, provider, typeSer);
            return;
        }
        String typeStr = this._typeId == null ? null : _customTypeId(bean);
        if (typeStr == null) {
            typeSer.writeTypePrefixForObject(bean, gen);
        } else {
            typeSer.writeCustomTypePrefixForObject(bean, gen, typeStr);
        }
        gen.setCurrentValue(bean);
        if (this._propertyFilterId != null) {
            serializeFieldsFiltered(bean, gen, provider);
        } else {
            serializeFields(bean, gen, provider);
        }
        if (typeStr == null) {
            typeSer.writeTypeSuffixForObject(bean, gen);
        } else {
            typeSer.writeCustomTypeSuffixForObject(bean, gen, typeStr);
        }
    }

    protected final void _serializeWithObjectId(Object bean, JsonGenerator gen, SerializerProvider provider, boolean startEndObject) throws IOException {
        ObjectIdWriter w = this._objectIdWriter;
        WritableObjectId objectId = provider.findObjectId(bean, w.generator);
        if (!objectId.writeAsId(gen, provider, w)) {
            Object id = objectId.generateId(bean);
            if (w.alwaysAsId) {
                w.serializer.serialize(id, gen, provider);
                return;
            }
            if (startEndObject) {
                gen.writeStartObject();
            }
            objectId.writeAsField(gen, provider, w);
            if (this._propertyFilterId != null) {
                serializeFieldsFiltered(bean, gen, provider);
            } else {
                serializeFields(bean, gen, provider);
            }
            if (startEndObject) {
                gen.writeEndObject();
            }
        }
    }

    protected final void _serializeWithObjectId(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        ObjectIdWriter w = this._objectIdWriter;
        WritableObjectId objectId = provider.findObjectId(bean, w.generator);
        if (!objectId.writeAsId(gen, provider, w)) {
            Object id = objectId.generateId(bean);
            if (w.alwaysAsId) {
                w.serializer.serialize(id, gen, provider);
            } else {
                _serializeObjectId(bean, gen, provider, typeSer, objectId);
            }
        }
    }

    protected void _serializeObjectId(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer, WritableObjectId objectId) throws IOException {
        ObjectIdWriter w = this._objectIdWriter;
        String typeStr = this._typeId == null ? null : _customTypeId(bean);
        if (typeStr == null) {
            typeSer.writeTypePrefixForObject(bean, gen);
        } else {
            typeSer.writeCustomTypePrefixForObject(bean, gen, typeStr);
        }
        objectId.writeAsField(gen, provider, w);
        if (this._propertyFilterId != null) {
            serializeFieldsFiltered(bean, gen, provider);
        } else {
            serializeFields(bean, gen, provider);
        }
        if (typeStr == null) {
            typeSer.writeTypeSuffixForObject(bean, gen);
        } else {
            typeSer.writeCustomTypeSuffixForObject(bean, gen, typeStr);
        }
    }

    protected final String _customTypeId(Object bean) {
        Object typeId = this._typeId.getValue(bean);
        if (typeId == null) {
            return BuildConfig.FLAVOR;
        }
        return typeId instanceof String ? (String) typeId : typeId.toString();
    }

    protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonGenerationException {
        BeanPropertyWriter[] props;
        if (this._filteredProps == null || provider.getActiveView() == null) {
            props = this._props;
        } else {
            props = this._filteredProps;
        }
        int i = 0;
        try {
            int len = props.length;
            while (i < len) {
                BeanPropertyWriter prop = props[i];
                if (prop != null) {
                    prop.serializeAsField(bean, gen, provider);
                }
                i++;
            }
            if (this._anyGetterWriter != null) {
                this._anyGetterWriter.getAndSerialize(bean, gen, provider);
            }
        } catch (Exception e) {
            wrapAndThrow(provider, (Throwable) e, bean, i == props.length ? "[anySetter]" : props[i].getName());
        } catch (Throwable e2) {
            JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)", e2);
            mapE.prependPath(new Reference(bean, i == props.length ? "[anySetter]" : props[i].getName()));
            throw mapE;
        }
    }

    protected void serializeFieldsFiltered(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonGenerationException {
        BeanPropertyWriter[] props;
        if (this._filteredProps == null || provider.getActiveView() == null) {
            props = this._props;
        } else {
            props = this._filteredProps;
        }
        PropertyFilter filter = findPropertyFilter(provider, this._propertyFilterId, bean);
        if (filter == null) {
            serializeFields(bean, gen, provider);
            return;
        }
        int i = 0;
        try {
            int len = props.length;
            while (i < len) {
                BeanPropertyWriter prop = props[i];
                if (prop != null) {
                    filter.serializeAsField(bean, gen, provider, prop);
                }
                i++;
            }
            if (this._anyGetterWriter != null) {
                this._anyGetterWriter.getAndFilter(bean, gen, provider, filter);
            }
        } catch (Exception e) {
            wrapAndThrow(provider, (Throwable) e, bean, 0 == props.length ? "[anySetter]" : props[0].getName());
        } catch (Throwable e2) {
            JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)", e2);
            mapE.prependPath(new Reference(bean, 0 == props.length ? "[anySetter]" : props[0].getName()));
            throw mapE;
        }
    }

    @Deprecated
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        PropertyFilter filter;
        ObjectNode o = createSchemaNode("object", true);
        JsonSerializableSchema ann = (JsonSerializableSchema) this._handledType.getAnnotation(JsonSerializableSchema.class);
        if (ann != null) {
            String id = ann.id();
            if (id != null && id.length() > 0) {
                o.put(TriggerIfContentAvailable.ID, id);
            }
        }
        ObjectNode propertiesNode = o.objectNode();
        if (this._propertyFilterId != null) {
            filter = findPropertyFilter(provider, this._propertyFilterId, null);
        } else {
            filter = null;
        }
        for (PropertyWriter prop : this._props) {
            if (filter == null) {
                prop.depositSchemaProperty(propertiesNode, provider);
            } else {
                filter.depositSchemaProperty(prop, propertiesNode, provider);
            }
        }
        o.set("properties", propertiesNode);
        return o;
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        if (visitor != null) {
            JsonObjectFormatVisitor objectVisitor = visitor.expectObjectFormat(typeHint);
            if (objectVisitor == null) {
                return;
            }
            if (this._propertyFilterId != null) {
                PropertyFilter filter = findPropertyFilter(visitor.getProvider(), this._propertyFilterId, null);
                for (PropertyWriter depositSchemaProperty : this._props) {
                    filter.depositSchemaProperty(depositSchemaProperty, objectVisitor, visitor.getProvider());
                }
                return;
            }
            for (BeanPropertyWriter depositSchemaProperty2 : this._props) {
                depositSchemaProperty2.depositSchemaProperty(objectVisitor);
            }
        }
    }
}
