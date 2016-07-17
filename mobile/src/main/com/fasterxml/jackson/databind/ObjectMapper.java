package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SegmentedStringWriter;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.Module.SetupContext;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.KeyDeserializers;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker.Std;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.Impl;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;
import com.fasterxml.jackson.databind.util.RootNameLookup;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ObjectMapper extends ObjectCodec implements Versioned, Serializable {
    protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR;
    protected static final BaseSettings DEFAULT_BASE;
    private static final JavaType JSON_NODE_TYPE;
    protected static final VisibilityChecker<?> STD_VISIBILITY_CHECKER;
    @Deprecated
    protected static final PrettyPrinter _defaultPrettyPrinter;
    private static final long serialVersionUID = 1;
    protected DeserializationConfig _deserializationConfig;
    protected DefaultDeserializationContext _deserializationContext;
    protected InjectableValues _injectableValues;
    protected final JsonFactory _jsonFactory;
    protected SimpleMixInResolver _mixIns;
    protected Set<Object> _registeredModuleTypes;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected SerializationConfig _serializationConfig;
    protected SerializerFactory _serializerFactory;
    protected DefaultSerializerProvider _serializerProvider;
    protected SubtypeResolver _subtypeResolver;
    protected TypeFactory _typeFactory;

    /* renamed from: com.fasterxml.jackson.databind.ObjectMapper.1 */
    class C01271 implements SetupContext {
        final /* synthetic */ ObjectMapper val$mapper;

        C01271(ObjectMapper objectMapper) {
            this.val$mapper = objectMapper;
        }

        public Version getMapperVersion() {
            return ObjectMapper.this.version();
        }

        public <C extends ObjectCodec> C getOwner() {
            return this.val$mapper;
        }

        public TypeFactory getTypeFactory() {
            return ObjectMapper.this._typeFactory;
        }

        public boolean isEnabled(MapperFeature f) {
            return this.val$mapper.isEnabled(f);
        }

        public boolean isEnabled(DeserializationFeature f) {
            return this.val$mapper.isEnabled(f);
        }

        public boolean isEnabled(SerializationFeature f) {
            return this.val$mapper.isEnabled(f);
        }

        public boolean isEnabled(Feature f) {
            return this.val$mapper.isEnabled(f);
        }

        public boolean isEnabled(JsonParser.Feature f) {
            return this.val$mapper.isEnabled(f);
        }

        public boolean isEnabled(JsonGenerator.Feature f) {
            return this.val$mapper.isEnabled(f);
        }

        public void addDeserializers(Deserializers d) {
            DeserializerFactory df = this.val$mapper._deserializationContext._factory.withAdditionalDeserializers(d);
            this.val$mapper._deserializationContext = this.val$mapper._deserializationContext.with(df);
        }

        public void addKeyDeserializers(KeyDeserializers d) {
            DeserializerFactory df = this.val$mapper._deserializationContext._factory.withAdditionalKeyDeserializers(d);
            this.val$mapper._deserializationContext = this.val$mapper._deserializationContext.with(df);
        }

        public void addBeanDeserializerModifier(BeanDeserializerModifier modifier) {
            DeserializerFactory df = this.val$mapper._deserializationContext._factory.withDeserializerModifier(modifier);
            this.val$mapper._deserializationContext = this.val$mapper._deserializationContext.with(df);
        }

        public void addSerializers(Serializers s) {
            this.val$mapper._serializerFactory = this.val$mapper._serializerFactory.withAdditionalSerializers(s);
        }

        public void addKeySerializers(Serializers s) {
            this.val$mapper._serializerFactory = this.val$mapper._serializerFactory.withAdditionalKeySerializers(s);
        }

        public void addBeanSerializerModifier(BeanSerializerModifier modifier) {
            this.val$mapper._serializerFactory = this.val$mapper._serializerFactory.withSerializerModifier(modifier);
        }

        public void addAbstractTypeResolver(AbstractTypeResolver resolver) {
            DeserializerFactory df = this.val$mapper._deserializationContext._factory.withAbstractTypeResolver(resolver);
            this.val$mapper._deserializationContext = this.val$mapper._deserializationContext.with(df);
        }

        public void addTypeModifier(TypeModifier modifier) {
            this.val$mapper.setTypeFactory(this.val$mapper._typeFactory.withModifier(modifier));
        }

        public void addValueInstantiators(ValueInstantiators instantiators) {
            DeserializerFactory df = this.val$mapper._deserializationContext._factory.withValueInstantiators(instantiators);
            this.val$mapper._deserializationContext = this.val$mapper._deserializationContext.with(df);
        }

        public void setClassIntrospector(ClassIntrospector ci) {
            this.val$mapper._deserializationConfig = this.val$mapper._deserializationConfig.with(ci);
            this.val$mapper._serializationConfig = this.val$mapper._serializationConfig.with(ci);
        }

        public void insertAnnotationIntrospector(AnnotationIntrospector ai) {
            this.val$mapper._deserializationConfig = this.val$mapper._deserializationConfig.withInsertedAnnotationIntrospector(ai);
            this.val$mapper._serializationConfig = this.val$mapper._serializationConfig.withInsertedAnnotationIntrospector(ai);
        }

        public void appendAnnotationIntrospector(AnnotationIntrospector ai) {
            this.val$mapper._deserializationConfig = this.val$mapper._deserializationConfig.withAppendedAnnotationIntrospector(ai);
            this.val$mapper._serializationConfig = this.val$mapper._serializationConfig.withAppendedAnnotationIntrospector(ai);
        }

        public void registerSubtypes(Class<?>... subtypes) {
            this.val$mapper.registerSubtypes((Class[]) subtypes);
        }

        public void registerSubtypes(NamedType... subtypes) {
            this.val$mapper.registerSubtypes(subtypes);
        }

        public void setMixInAnnotations(Class<?> target, Class<?> mixinSource) {
            this.val$mapper.addMixIn(target, mixinSource);
        }

        public void addDeserializationProblemHandler(DeserializationProblemHandler handler) {
            this.val$mapper.addHandler(handler);
        }

        public void setNamingStrategy(PropertyNamingStrategy naming) {
            this.val$mapper.setPropertyNamingStrategy(naming);
        }
    }

    /* renamed from: com.fasterxml.jackson.databind.ObjectMapper.2 */
    static /* synthetic */ class C01282 {
        static final /* synthetic */ int[] f29x3ef634e7;

        static {
            f29x3ef634e7 = new int[DefaultTyping.values().length];
            try {
                f29x3ef634e7[DefaultTyping.NON_CONCRETE_AND_ARRAYS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f29x3ef634e7[DefaultTyping.OBJECT_AND_NON_CONCRETE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f29x3ef634e7[DefaultTyping.NON_FINAL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public static class DefaultTypeResolverBuilder extends StdTypeResolverBuilder implements Serializable {
        private static final long serialVersionUID = 1;
        protected final DefaultTyping _appliesFor;

        public DefaultTypeResolverBuilder(DefaultTyping t) {
            this._appliesFor = t;
        }

        public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
            return useForType(baseType) ? super.buildTypeDeserializer(config, baseType, subtypes) : null;
        }

        public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
            return useForType(baseType) ? super.buildTypeSerializer(config, baseType, subtypes) : null;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean useForType(com.fasterxml.jackson.databind.JavaType r5) {
            /*
            r4 = this;
            r1 = 1;
            r0 = 0;
            r2 = com.fasterxml.jackson.databind.ObjectMapper.C01282.f29x3ef634e7;
            r3 = r4._appliesFor;
            r3 = r3.ordinal();
            r2 = r2[r3];
            switch(r2) {
                case 1: goto L_0x0014;
                case 2: goto L_0x001f;
                case 3: goto L_0x0039;
                default: goto L_0x000f;
            };
        L_0x000f:
            r0 = r5.isJavaLangObject();
        L_0x0013:
            return r0;
        L_0x0014:
            r2 = r5.isArrayType();
            if (r2 == 0) goto L_0x001f;
        L_0x001a:
            r5 = r5.getContentType();
            goto L_0x0014;
        L_0x001f:
            r2 = r5.isJavaLangObject();
            if (r2 != 0) goto L_0x0037;
        L_0x0025:
            r2 = r5.isConcrete();
            if (r2 != 0) goto L_0x0013;
        L_0x002b:
            r2 = com.fasterxml.jackson.core.TreeNode.class;
            r3 = r5.getRawClass();
            r2 = r2.isAssignableFrom(r3);
            if (r2 != 0) goto L_0x0013;
        L_0x0037:
            r0 = r1;
            goto L_0x0013;
        L_0x0039:
            r2 = r5.isArrayType();
            if (r2 == 0) goto L_0x0044;
        L_0x003f:
            r5 = r5.getContentType();
            goto L_0x0039;
        L_0x0044:
            r2 = r5.isFinal();
            if (r2 != 0) goto L_0x0058;
        L_0x004a:
            r2 = com.fasterxml.jackson.core.TreeNode.class;
            r3 = r5.getRawClass();
            r2 = r2.isAssignableFrom(r3);
            if (r2 != 0) goto L_0x0058;
        L_0x0056:
            r0 = r1;
            goto L_0x0013;
        L_0x0058:
            r1 = r0;
            goto L_0x0056;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder.useForType(com.fasterxml.jackson.databind.JavaType):boolean");
        }
    }

    public enum DefaultTyping {
        JAVA_LANG_OBJECT,
        OBJECT_AND_NON_CONCRETE,
        NON_CONCRETE_AND_ARRAYS,
        NON_FINAL
    }

    static {
        JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
        DEFAULT_ANNOTATION_INTROSPECTOR = new JacksonAnnotationIntrospector();
        STD_VISIBILITY_CHECKER = Std.defaultInstance();
        _defaultPrettyPrinter = new DefaultPrettyPrinter();
        DEFAULT_BASE = new BaseSettings(null, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, TypeFactory.defaultInstance(), null, StdDateFormat.instance, null, Locale.getDefault(), null, Base64Variants.getDefaultVariant());
    }

    public ObjectMapper() {
        this(null, null, null);
    }

    public ObjectMapper(JsonFactory jf) {
        this(jf, null, null);
    }

    protected ObjectMapper(ObjectMapper src) {
        this._rootDeserializers = new ConcurrentHashMap(64, 0.6f, 2);
        this._jsonFactory = src._jsonFactory.copy();
        this._jsonFactory.setCodec(this);
        this._subtypeResolver = src._subtypeResolver;
        this._typeFactory = src._typeFactory;
        this._injectableValues = src._injectableValues;
        SimpleMixInResolver mixins = src._mixIns.copy();
        this._mixIns = mixins;
        RootNameLookup rootNames = new RootNameLookup();
        this._serializationConfig = new SerializationConfig(src._serializationConfig, mixins, rootNames);
        this._deserializationConfig = new DeserializationConfig(src._deserializationConfig, mixins, rootNames);
        this._serializerProvider = src._serializerProvider.copy();
        this._deserializationContext = src._deserializationContext.copy();
        this._serializerFactory = src._serializerFactory;
        Set<Object> reg = this._registeredModuleTypes;
        if (reg == null) {
            this._registeredModuleTypes = null;
        } else {
            this._registeredModuleTypes = new LinkedHashSet(reg);
        }
    }

    public ObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc) {
        this._rootDeserializers = new ConcurrentHashMap(64, 0.6f, 2);
        if (jf == null) {
            this._jsonFactory = new MappingJsonFactory(this);
        } else {
            this._jsonFactory = jf;
            if (jf.getCodec() == null) {
                this._jsonFactory.setCodec(this);
            }
        }
        this._subtypeResolver = new StdSubtypeResolver();
        RootNameLookup rootNames = new RootNameLookup();
        this._typeFactory = TypeFactory.defaultInstance();
        SimpleMixInResolver mixins = new SimpleMixInResolver(null);
        this._mixIns = mixins;
        BaseSettings base = DEFAULT_BASE.withClassIntrospector(defaultClassIntrospector());
        this._serializationConfig = new SerializationConfig(base, this._subtypeResolver, mixins, rootNames);
        this._deserializationConfig = new DeserializationConfig(base, this._subtypeResolver, mixins, rootNames);
        boolean needOrder = this._jsonFactory.requiresPropertyOrdering();
        if ((this._serializationConfig.isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY) ^ needOrder) != 0) {
            configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, needOrder);
        }
        if (sp == null) {
            sp = new Impl();
        }
        this._serializerProvider = sp;
        if (dc == null) {
            dc = new DefaultDeserializationContext.Impl(BeanDeserializerFactory.instance);
        }
        this._deserializationContext = dc;
        this._serializerFactory = BeanSerializerFactory.instance;
    }

    protected ClassIntrospector defaultClassIntrospector() {
        return new BasicClassIntrospector();
    }

    public ObjectMapper copy() {
        _checkInvalidCopy(ObjectMapper.class);
        return new ObjectMapper(this);
    }

    protected void _checkInvalidCopy(Class<?> exp) {
        if (getClass() != exp) {
            throw new IllegalStateException("Failed copy(): " + getClass().getName() + " (version: " + version() + ") does not override copy(); it has to");
        }
    }

    protected ObjectReader _newReader(DeserializationConfig config) {
        return new ObjectReader(this, config);
    }

    protected ObjectReader _newReader(DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues) {
        return new ObjectReader(this, config, valueType, valueToUpdate, schema, injectableValues);
    }

    protected ObjectWriter _newWriter(SerializationConfig config) {
        return new ObjectWriter(this, config);
    }

    protected ObjectWriter _newWriter(SerializationConfig config, FormatSchema schema) {
        return new ObjectWriter(this, config, schema);
    }

    protected ObjectWriter _newWriter(SerializationConfig config, JavaType rootType, PrettyPrinter pp) {
        return new ObjectWriter(this, config, rootType, pp);
    }

    public Version version() {
        return PackageVersion.VERSION;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.fasterxml.jackson.databind.ObjectMapper registerModule(com.fasterxml.jackson.databind.Module r7) {
        /*
        r6 = this;
        r4 = com.fasterxml.jackson.databind.MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS;
        r4 = r6.isEnabled(r4);
        if (r4 == 0) goto L_0x0022;
    L_0x0008:
        r2 = r7.getTypeId();
        if (r2 == 0) goto L_0x0022;
    L_0x000e:
        r4 = r6._registeredModuleTypes;
        if (r4 != 0) goto L_0x0019;
    L_0x0012:
        r4 = new java.util.LinkedHashSet;
        r4.<init>();
        r6._registeredModuleTypes = r4;
    L_0x0019:
        r4 = r6._registeredModuleTypes;
        r4 = r4.add(r2);
        if (r4 != 0) goto L_0x0022;
    L_0x0021:
        return r6;
    L_0x0022:
        r1 = r7.getModuleName();
        if (r1 != 0) goto L_0x0030;
    L_0x0028:
        r4 = new java.lang.IllegalArgumentException;
        r5 = "Module without defined name";
        r4.<init>(r5);
        throw r4;
    L_0x0030:
        r3 = r7.version();
        if (r3 != 0) goto L_0x003e;
    L_0x0036:
        r4 = new java.lang.IllegalArgumentException;
        r5 = "Module without defined version";
        r4.<init>(r5);
        throw r4;
    L_0x003e:
        r0 = r6;
        r4 = new com.fasterxml.jackson.databind.ObjectMapper$1;
        r4.<init>(r0);
        r7.setupModule(r4);
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.databind.ObjectMapper.registerModule(com.fasterxml.jackson.databind.Module):com.fasterxml.jackson.databind.ObjectMapper");
    }

    public ObjectMapper registerModules(Module... modules) {
        for (Module module : modules) {
            registerModule(module);
        }
        return this;
    }

    public ObjectMapper registerModules(Iterable<Module> modules) {
        for (Module module : modules) {
            registerModule(module);
        }
        return this;
    }

    public static List<Module> findModules() {
        return findModules(null);
    }

    public static List<Module> findModules(ClassLoader classLoader) {
        ArrayList<Module> modules = new ArrayList();
        Iterator i$ = (classLoader == null ? ServiceLoader.load(Module.class) : ServiceLoader.load(Module.class, classLoader)).iterator();
        while (i$.hasNext()) {
            modules.add((Module) i$.next());
        }
        return modules;
    }

    public ObjectMapper findAndRegisterModules() {
        return registerModules(findModules());
    }

    public SerializationConfig getSerializationConfig() {
        return this._serializationConfig;
    }

    public DeserializationConfig getDeserializationConfig() {
        return this._deserializationConfig;
    }

    public DeserializationContext getDeserializationContext() {
        return this._deserializationContext;
    }

    public ObjectMapper setSerializerFactory(SerializerFactory f) {
        this._serializerFactory = f;
        return this;
    }

    public SerializerFactory getSerializerFactory() {
        return this._serializerFactory;
    }

    public ObjectMapper setSerializerProvider(DefaultSerializerProvider p) {
        this._serializerProvider = p;
        return this;
    }

    public SerializerProvider getSerializerProvider() {
        return this._serializerProvider;
    }

    public ObjectMapper setMixIns(Map<Class<?>, Class<?>> sourceMixins) {
        this._mixIns.setLocalDefinitions(sourceMixins);
        return this;
    }

    public ObjectMapper addMixIn(Class<?> target, Class<?> mixinSource) {
        this._mixIns.addLocalDefinition(target, mixinSource);
        return this;
    }

    public ObjectMapper setMixInResolver(MixInResolver resolver) {
        SimpleMixInResolver r = this._mixIns.withOverrides(resolver);
        if (r != this._mixIns) {
            this._mixIns = r;
            this._deserializationConfig = new DeserializationConfig(this._deserializationConfig, r);
            this._serializationConfig = new SerializationConfig(this._serializationConfig, r);
        }
        return this;
    }

    public Class<?> findMixInClassFor(Class<?> cls) {
        return this._mixIns.findMixInClassFor(cls);
    }

    public int mixInCount() {
        return this._mixIns.localSize();
    }

    @Deprecated
    public void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins) {
        setMixIns(sourceMixins);
    }

    @Deprecated
    public final void addMixInAnnotations(Class<?> target, Class<?> mixinSource) {
        addMixIn(target, mixinSource);
    }

    public VisibilityChecker<?> getVisibilityChecker() {
        return this._serializationConfig.getDefaultVisibilityChecker();
    }

    @Deprecated
    public void setVisibilityChecker(VisibilityChecker<?> vc) {
        setVisibility(vc);
    }

    public ObjectMapper setVisibility(VisibilityChecker<?> vc) {
        this._deserializationConfig = this._deserializationConfig.with((VisibilityChecker) vc);
        this._serializationConfig = this._serializationConfig.with((VisibilityChecker) vc);
        return this;
    }

    public ObjectMapper setVisibility(PropertyAccessor forMethod, Visibility visibility) {
        this._deserializationConfig = this._deserializationConfig.withVisibility(forMethod, visibility);
        this._serializationConfig = this._serializationConfig.withVisibility(forMethod, visibility);
        return this;
    }

    public SubtypeResolver getSubtypeResolver() {
        return this._subtypeResolver;
    }

    public ObjectMapper setSubtypeResolver(SubtypeResolver str) {
        this._subtypeResolver = str;
        this._deserializationConfig = this._deserializationConfig.with(str);
        this._serializationConfig = this._serializationConfig.with(str);
        return this;
    }

    public ObjectMapper setAnnotationIntrospector(AnnotationIntrospector ai) {
        this._serializationConfig = this._serializationConfig.with(ai);
        this._deserializationConfig = this._deserializationConfig.with(ai);
        return this;
    }

    public ObjectMapper setAnnotationIntrospectors(AnnotationIntrospector serializerAI, AnnotationIntrospector deserializerAI) {
        this._serializationConfig = this._serializationConfig.with(serializerAI);
        this._deserializationConfig = this._deserializationConfig.with(deserializerAI);
        return this;
    }

    public ObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy s) {
        this._serializationConfig = this._serializationConfig.with(s);
        this._deserializationConfig = this._deserializationConfig.with(s);
        return this;
    }

    public PropertyNamingStrategy getPropertyNamingStrategy() {
        return this._serializationConfig.getPropertyNamingStrategy();
    }

    public ObjectMapper setSerializationInclusion(Include incl) {
        this._serializationConfig = this._serializationConfig.withSerializationInclusion(incl);
        return this;
    }

    public ObjectMapper setDefaultPrettyPrinter(PrettyPrinter pp) {
        this._serializationConfig = this._serializationConfig.withDefaultPrettyPrinter(pp);
        return this;
    }

    public ObjectMapper enableDefaultTyping() {
        return enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping dti) {
        return enableDefaultTyping(dti, As.WRAPPER_ARRAY);
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping applicability, As includeAs) {
        if (includeAs != As.EXTERNAL_PROPERTY) {
            return setDefaultTyping(new DefaultTypeResolverBuilder(applicability).init(Id.CLASS, null).inclusion(includeAs));
        }
        throw new IllegalArgumentException("Can not use includeAs of " + includeAs);
    }

    public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName) {
        return setDefaultTyping(new DefaultTypeResolverBuilder(applicability).init(Id.CLASS, null).inclusion(As.PROPERTY).typeProperty(propertyName));
    }

    public ObjectMapper disableDefaultTyping() {
        return setDefaultTyping(null);
    }

    public ObjectMapper setDefaultTyping(TypeResolverBuilder<?> typer) {
        this._deserializationConfig = this._deserializationConfig.with((TypeResolverBuilder) typer);
        this._serializationConfig = this._serializationConfig.with((TypeResolverBuilder) typer);
        return this;
    }

    public void registerSubtypes(Class<?>... classes) {
        getSubtypeResolver().registerSubtypes((Class[]) classes);
    }

    public void registerSubtypes(NamedType... types) {
        getSubtypeResolver().registerSubtypes(types);
    }

    public TypeFactory getTypeFactory() {
        return this._typeFactory;
    }

    public ObjectMapper setTypeFactory(TypeFactory f) {
        this._typeFactory = f;
        this._deserializationConfig = this._deserializationConfig.with(f);
        this._serializationConfig = this._serializationConfig.with(f);
        return this;
    }

    public JavaType constructType(Type t) {
        return this._typeFactory.constructType(t);
    }

    public JsonNodeFactory getNodeFactory() {
        return this._deserializationConfig.getNodeFactory();
    }

    public ObjectMapper setNodeFactory(JsonNodeFactory f) {
        this._deserializationConfig = this._deserializationConfig.with(f);
        return this;
    }

    public ObjectMapper addHandler(DeserializationProblemHandler h) {
        this._deserializationConfig = this._deserializationConfig.withHandler(h);
        return this;
    }

    public ObjectMapper clearProblemHandlers() {
        this._deserializationConfig = this._deserializationConfig.withNoProblemHandlers();
        return this;
    }

    public ObjectMapper setConfig(DeserializationConfig config) {
        this._deserializationConfig = config;
        return this;
    }

    @Deprecated
    public void setFilters(FilterProvider filterProvider) {
        this._serializationConfig = this._serializationConfig.withFilters(filterProvider);
    }

    public ObjectMapper setFilterProvider(FilterProvider filterProvider) {
        this._serializationConfig = this._serializationConfig.withFilters(filterProvider);
        return this;
    }

    public ObjectMapper setBase64Variant(Base64Variant v) {
        this._serializationConfig = this._serializationConfig.with(v);
        this._deserializationConfig = this._deserializationConfig.with(v);
        return this;
    }

    public ObjectMapper setConfig(SerializationConfig config) {
        this._serializationConfig = config;
        return this;
    }

    public JsonFactory getFactory() {
        return this._jsonFactory;
    }

    @Deprecated
    public JsonFactory getJsonFactory() {
        return getFactory();
    }

    public ObjectMapper setDateFormat(DateFormat dateFormat) {
        this._deserializationConfig = this._deserializationConfig.with(dateFormat);
        this._serializationConfig = this._serializationConfig.with(dateFormat);
        return this;
    }

    public DateFormat getDateFormat() {
        return this._serializationConfig.getDateFormat();
    }

    public Object setHandlerInstantiator(HandlerInstantiator hi) {
        this._deserializationConfig = this._deserializationConfig.with(hi);
        this._serializationConfig = this._serializationConfig.with(hi);
        return this;
    }

    public ObjectMapper setInjectableValues(InjectableValues injectableValues) {
        this._injectableValues = injectableValues;
        return this;
    }

    public InjectableValues getInjectableValues() {
        return this._injectableValues;
    }

    public ObjectMapper setLocale(Locale l) {
        this._deserializationConfig = this._deserializationConfig.with(l);
        this._serializationConfig = this._serializationConfig.with(l);
        return this;
    }

    public ObjectMapper setTimeZone(TimeZone tz) {
        this._deserializationConfig = this._deserializationConfig.with(tz);
        this._serializationConfig = this._serializationConfig.with(tz);
        return this;
    }

    public boolean isEnabled(MapperFeature f) {
        return this._serializationConfig.isEnabled(f);
    }

    public ObjectMapper configure(MapperFeature f, boolean state) {
        SerializationConfig with;
        DeserializationConfig with2;
        if (state) {
            with = this._serializationConfig.with(f);
        } else {
            with = this._serializationConfig.without(f);
        }
        this._serializationConfig = with;
        if (state) {
            with2 = this._deserializationConfig.with(f);
        } else {
            with2 = this._deserializationConfig.without(f);
        }
        this._deserializationConfig = with2;
        return this;
    }

    public ObjectMapper enable(MapperFeature... f) {
        this._deserializationConfig = this._deserializationConfig.with(f);
        this._serializationConfig = this._serializationConfig.with(f);
        return this;
    }

    public ObjectMapper disable(MapperFeature... f) {
        this._deserializationConfig = this._deserializationConfig.without(f);
        this._serializationConfig = this._serializationConfig.without(f);
        return this;
    }

    public boolean isEnabled(SerializationFeature f) {
        return this._serializationConfig.isEnabled(f);
    }

    public ObjectMapper configure(SerializationFeature f, boolean state) {
        this._serializationConfig = state ? this._serializationConfig.with(f) : this._serializationConfig.without(f);
        return this;
    }

    public ObjectMapper enable(SerializationFeature f) {
        this._serializationConfig = this._serializationConfig.with(f);
        return this;
    }

    public ObjectMapper enable(SerializationFeature first, SerializationFeature... f) {
        this._serializationConfig = this._serializationConfig.with(first, f);
        return this;
    }

    public ObjectMapper disable(SerializationFeature f) {
        this._serializationConfig = this._serializationConfig.without(f);
        return this;
    }

    public ObjectMapper disable(SerializationFeature first, SerializationFeature... f) {
        this._serializationConfig = this._serializationConfig.without(first, f);
        return this;
    }

    public boolean isEnabled(DeserializationFeature f) {
        return this._deserializationConfig.isEnabled(f);
    }

    public ObjectMapper configure(DeserializationFeature f, boolean state) {
        this._deserializationConfig = state ? this._deserializationConfig.with(f) : this._deserializationConfig.without(f);
        return this;
    }

    public ObjectMapper enable(DeserializationFeature feature) {
        this._deserializationConfig = this._deserializationConfig.with(feature);
        return this;
    }

    public ObjectMapper enable(DeserializationFeature first, DeserializationFeature... f) {
        this._deserializationConfig = this._deserializationConfig.with(first, f);
        return this;
    }

    public ObjectMapper disable(DeserializationFeature feature) {
        this._deserializationConfig = this._deserializationConfig.without(feature);
        return this;
    }

    public ObjectMapper disable(DeserializationFeature first, DeserializationFeature... f) {
        this._deserializationConfig = this._deserializationConfig.without(first, f);
        return this;
    }

    public boolean isEnabled(JsonParser.Feature f) {
        return this._deserializationConfig.isEnabled(f, this._jsonFactory);
    }

    public ObjectMapper configure(JsonParser.Feature f, boolean state) {
        this._jsonFactory.configure(f, state);
        return this;
    }

    public ObjectMapper enable(JsonParser.Feature... features) {
        for (JsonParser.Feature f : features) {
            this._jsonFactory.enable(f);
        }
        return this;
    }

    public ObjectMapper disable(JsonParser.Feature... features) {
        for (JsonParser.Feature f : features) {
            this._jsonFactory.disable(f);
        }
        return this;
    }

    public boolean isEnabled(JsonGenerator.Feature f) {
        return this._serializationConfig.isEnabled(f, this._jsonFactory);
    }

    public ObjectMapper configure(JsonGenerator.Feature f, boolean state) {
        this._jsonFactory.configure(f, state);
        return this;
    }

    public ObjectMapper enable(JsonGenerator.Feature... features) {
        for (JsonGenerator.Feature f : features) {
            this._jsonFactory.enable(f);
        }
        return this;
    }

    public ObjectMapper disable(JsonGenerator.Feature... features) {
        for (JsonGenerator.Feature f : features) {
            this._jsonFactory.disable(f);
        }
        return this;
    }

    public boolean isEnabled(Feature f) {
        return this._jsonFactory.isEnabled(f);
    }

    public <T> T readValue(JsonParser jp, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(getDeserializationConfig(), jp, this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(getDeserializationConfig(), jp, this._typeFactory.constructType((TypeReference) valueTypeRef));
    }

    public final <T> T readValue(JsonParser jp, ResolvedType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(getDeserializationConfig(), jp, (JavaType) valueType);
    }

    public <T> T readValue(JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(getDeserializationConfig(), jp, valueType);
    }

    public <T extends TreeNode> T readTree(JsonParser jp) throws IOException, JsonProcessingException {
        DeserializationConfig cfg = getDeserializationConfig();
        if (jp.getCurrentToken() == null && jp.nextToken() == null) {
            return null;
        }
        T n = (JsonNode) _readValue(cfg, jp, JSON_NODE_TYPE);
        if (n == null) {
            n = getNodeFactory().nullNode();
        }
        return n;
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, ResolvedType valueType) throws IOException, JsonProcessingException {
        return readValues(jp, (JavaType) valueType);
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, JavaType valueType) throws IOException, JsonProcessingException {
        DeserializationContext ctxt = createDeserializationContext(jp, getDeserializationConfig());
        return new MappingIterator(valueType, jp, ctxt, _findRootDeserializer(ctxt, valueType), false, null);
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, Class<T> valueType) throws IOException, JsonProcessingException {
        return readValues(jp, this._typeFactory.constructType((Type) valueType));
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, TypeReference<?> valueTypeRef) throws IOException, JsonProcessingException {
        return readValues(jp, this._typeFactory.constructType((TypeReference) valueTypeRef));
    }

    public JsonNode readTree(InputStream in) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createParser(in), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(Reader r) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createParser(r), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(String content) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createParser(content), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(byte[] content) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createParser(content), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(File file) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createParser(file), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(URL source) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createParser(source), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public void writeValue(JsonGenerator g, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        SerializationConfig config = getSerializationConfig();
        if (config.isEnabled(SerializationFeature.INDENT_OUTPUT) && g.getPrettyPrinter() == null) {
            g.setPrettyPrinter(config.constructDefaultPrettyPrinter());
        }
        if (config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _writeCloseableValue(g, value, config);
            return;
        }
        _serializerProvider(config).serializeValue(g, value);
        if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
            g.flush();
        }
    }

    public void writeTree(JsonGenerator jgen, TreeNode rootNode) throws IOException, JsonProcessingException {
        SerializationConfig config = getSerializationConfig();
        _serializerProvider(config).serializeValue(jgen, rootNode);
        if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws IOException, JsonProcessingException {
        SerializationConfig config = getSerializationConfig();
        _serializerProvider(config).serializeValue(jgen, rootNode);
        if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public ObjectNode createObjectNode() {
        return this._deserializationConfig.getNodeFactory().objectNode();
    }

    public ArrayNode createArrayNode() {
        return this._deserializationConfig.getNodeFactory().arrayNode();
    }

    public JsonParser treeAsTokens(TreeNode n) {
        return new TreeTraversingParser((JsonNode) n, this);
    }

    public <T> T treeToValue(TreeNode n, Class<T> valueType) throws JsonProcessingException {
        try {
            if (valueType == Object.class || !valueType.isAssignableFrom(n.getClass())) {
                n = readValue(treeAsTokens(n), (Class) valueType);
            }
            return n;
        } catch (JsonProcessingException e) {
            throw e;
        } catch (IOException e2) {
            throw new IllegalArgumentException(e2.getMessage(), e2);
        }
    }

    public <T extends JsonNode> T valueToTree(Object fromValue) throws IllegalArgumentException {
        if (fromValue == null) {
            return null;
        }
        JsonGenerator buf = new TokenBuffer((ObjectCodec) this, false);
        if (isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
            buf = buf.forceUseOfBigDecimal(true);
        }
        try {
            writeValue(buf, fromValue);
            JsonParser jp = buf.asParser();
            JsonNode result = (JsonNode) readTree(jp);
            jp.close();
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public boolean canSerialize(Class<?> type) {
        return _serializerProvider(getSerializationConfig()).hasSerializerFor(type, null);
    }

    public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause) {
        return _serializerProvider(getSerializationConfig()).hasSerializerFor(type, cause);
    }

    public boolean canDeserialize(JavaType type) {
        return createDeserializationContext(null, getDeserializationConfig()).hasValueDeserializerFor(type, null);
    }

    public boolean canDeserialize(JavaType type, AtomicReference<Throwable> cause) {
        return createDeserializationContext(null, getDeserializationConfig()).hasValueDeserializerFor(type, cause);
    }

    public <T> T readValue(File src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(File src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(File src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), valueType);
    }

    public <T> T readValue(URL src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(URL src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(URL src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), valueType);
    }

    public <T> T readValue(String content, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(content), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(String content, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(content), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(String content, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(content), valueType);
    }

    public <T> T readValue(Reader src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(Reader src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(Reader src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), valueType);
    }

    public <T> T readValue(InputStream src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(InputStream src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(InputStream src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), valueType);
    }

    public <T> T readValue(byte[] src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src, offset, len), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(byte[] src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(byte[] src, int offset, int len, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src, offset, len), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(byte[] src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src), valueType);
    }

    public <T> T readValue(byte[] src, int offset, int len, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createParser(src, offset, len), valueType);
    }

    public void writeValue(File resultFile, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createGenerator(resultFile, JsonEncoding.UTF8), value);
    }

    public void writeValue(OutputStream out, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createGenerator(out, JsonEncoding.UTF8), value);
    }

    public void writeValue(Writer w, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createGenerator(w), value);
    }

    public String writeValueAsString(Object value) throws JsonProcessingException {
        Writer sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
        try {
            _configAndWriteValue(this._jsonFactory.createGenerator(sw), value);
            return sw.getAndClear();
        } catch (JsonProcessingException e) {
            throw e;
        } catch (IOException e2) {
            throw JsonMappingException.fromUnexpectedIOE(e2);
        }
    }

    public byte[] writeValueAsBytes(Object value) throws JsonProcessingException {
        OutputStream bb = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
        try {
            _configAndWriteValue(this._jsonFactory.createGenerator(bb, JsonEncoding.UTF8), value);
            byte[] result = bb.toByteArray();
            bb.release();
            return result;
        } catch (JsonProcessingException e) {
            throw e;
        } catch (IOException e2) {
            throw JsonMappingException.fromUnexpectedIOE(e2);
        }
    }

    public ObjectWriter writer() {
        return _newWriter(getSerializationConfig());
    }

    public ObjectWriter writer(SerializationFeature feature) {
        return _newWriter(getSerializationConfig().with(feature));
    }

    public ObjectWriter writer(SerializationFeature first, SerializationFeature... other) {
        return _newWriter(getSerializationConfig().with(first, other));
    }

    public ObjectWriter writer(DateFormat df) {
        return _newWriter(getSerializationConfig().with(df));
    }

    public ObjectWriter writerWithView(Class<?> serializationView) {
        return _newWriter(getSerializationConfig().withView((Class) serializationView));
    }

    public ObjectWriter writerFor(Class<?> rootType) {
        return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType((Type) rootType), null);
    }

    public ObjectWriter writerFor(TypeReference<?> rootType) {
        return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType((TypeReference) rootType), null);
    }

    public ObjectWriter writerFor(JavaType rootType) {
        return _newWriter(getSerializationConfig(), rootType, null);
    }

    public ObjectWriter writer(PrettyPrinter pp) {
        if (pp == null) {
            pp = ObjectWriter.NULL_PRETTY_PRINTER;
        }
        return _newWriter(getSerializationConfig(), null, pp);
    }

    public ObjectWriter writerWithDefaultPrettyPrinter() {
        SerializationConfig config = getSerializationConfig();
        return _newWriter(config, null, config.getDefaultPrettyPrinter());
    }

    public ObjectWriter writer(FilterProvider filterProvider) {
        return _newWriter(getSerializationConfig().withFilters(filterProvider));
    }

    public ObjectWriter writer(FormatSchema schema) {
        _verifySchemaType(schema);
        return _newWriter(getSerializationConfig(), schema);
    }

    public ObjectWriter writer(Base64Variant defaultBase64) {
        return _newWriter(getSerializationConfig().with(defaultBase64));
    }

    public ObjectWriter writer(CharacterEscapes escapes) {
        return _newWriter(getSerializationConfig()).with(escapes);
    }

    public ObjectWriter writer(ContextAttributes attrs) {
        return _newWriter(getSerializationConfig().with(attrs));
    }

    @Deprecated
    public ObjectWriter writerWithType(Class<?> rootType) {
        return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType((Type) rootType), null);
    }

    @Deprecated
    public ObjectWriter writerWithType(TypeReference<?> rootType) {
        return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType((TypeReference) rootType), null);
    }

    @Deprecated
    public ObjectWriter writerWithType(JavaType rootType) {
        return _newWriter(getSerializationConfig(), rootType, null);
    }

    public ObjectReader reader() {
        return _newReader(getDeserializationConfig()).with(this._injectableValues);
    }

    public ObjectReader reader(DeserializationFeature feature) {
        return _newReader(getDeserializationConfig().with(feature));
    }

    public ObjectReader reader(DeserializationFeature first, DeserializationFeature... other) {
        return _newReader(getDeserializationConfig().with(first, other));
    }

    public ObjectReader readerForUpdating(Object valueToUpdate) {
        return _newReader(getDeserializationConfig(), this._typeFactory.constructType(valueToUpdate.getClass()), valueToUpdate, null, this._injectableValues);
    }

    public ObjectReader readerFor(JavaType type) {
        return _newReader(getDeserializationConfig(), type, null, null, this._injectableValues);
    }

    public ObjectReader readerFor(Class<?> type) {
        return _newReader(getDeserializationConfig(), this._typeFactory.constructType((Type) type), null, null, this._injectableValues);
    }

    public ObjectReader readerFor(TypeReference<?> type) {
        return _newReader(getDeserializationConfig(), this._typeFactory.constructType((TypeReference) type), null, null, this._injectableValues);
    }

    public ObjectReader reader(JsonNodeFactory f) {
        return _newReader(getDeserializationConfig()).with(f);
    }

    public ObjectReader reader(FormatSchema schema) {
        _verifySchemaType(schema);
        return _newReader(getDeserializationConfig(), null, null, schema, this._injectableValues);
    }

    public ObjectReader reader(InjectableValues injectableValues) {
        return _newReader(getDeserializationConfig(), null, null, null, injectableValues);
    }

    public ObjectReader readerWithView(Class<?> view) {
        return _newReader(getDeserializationConfig().withView((Class) view));
    }

    public ObjectReader reader(Base64Variant defaultBase64) {
        return _newReader(getDeserializationConfig().with(defaultBase64));
    }

    public ObjectReader reader(ContextAttributes attrs) {
        return _newReader(getDeserializationConfig().with(attrs));
    }

    @Deprecated
    public ObjectReader reader(JavaType type) {
        return _newReader(getDeserializationConfig(), type, null, null, this._injectableValues);
    }

    @Deprecated
    public ObjectReader reader(Class<?> type) {
        return _newReader(getDeserializationConfig(), this._typeFactory.constructType((Type) type), null, null, this._injectableValues);
    }

    @Deprecated
    public ObjectReader reader(TypeReference<?> type) {
        return _newReader(getDeserializationConfig(), this._typeFactory.constructType((TypeReference) type), null, null, this._injectableValues);
    }

    public <T> T convertValue(Object fromValue, Class<T> toValueType) throws IllegalArgumentException {
        if (fromValue == null) {
            return null;
        }
        return _convert(fromValue, this._typeFactory.constructType((Type) toValueType));
    }

    public <T> T convertValue(Object fromValue, TypeReference<?> toValueTypeRef) throws IllegalArgumentException {
        return convertValue(fromValue, this._typeFactory.constructType((TypeReference) toValueTypeRef));
    }

    public <T> T convertValue(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        if (fromValue == null) {
            return null;
        }
        return _convert(fromValue, toValueType);
    }

    protected Object _convert(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        Class<?> targetType = toValueType.getRawClass();
        if (targetType != Object.class && !toValueType.hasGenericTypes() && targetType.isAssignableFrom(fromValue.getClass())) {
            return fromValue;
        }
        TokenBuffer buf = new TokenBuffer((ObjectCodec) this, false);
        if (isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
            buf = buf.forceUseOfBigDecimal(true);
        }
        try {
            Object nullValue;
            _serializerProvider(getSerializationConfig().without(SerializationFeature.WRAP_ROOT_VALUE)).serializeValue(buf, fromValue);
            JsonParser jp = buf.asParser();
            DeserializationConfig deserConfig = getDeserializationConfig();
            JsonToken t = _initForReading(jp);
            DeserializationContext ctxt;
            if (t == JsonToken.VALUE_NULL) {
                ctxt = createDeserializationContext(jp, deserConfig);
                nullValue = _findRootDeserializer(ctxt, toValueType).getNullValue(ctxt);
            } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                nullValue = null;
            } else {
                ctxt = createDeserializationContext(jp, deserConfig);
                nullValue = _findRootDeserializer(ctxt, toValueType).deserialize(jp, ctxt);
            }
            jp.close();
            return nullValue;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Deprecated
    public JsonSchema generateJsonSchema(Class<?> t) throws JsonMappingException {
        return _serializerProvider(getSerializationConfig()).generateJsonSchema(t);
    }

    public void acceptJsonFormatVisitor(Class<?> type, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
        acceptJsonFormatVisitor(this._typeFactory.constructType((Type) type), visitor);
    }

    public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
        if (type == null) {
            throw new IllegalArgumentException("type must be provided");
        }
        _serializerProvider(getSerializationConfig()).acceptJsonFormatVisitor(type, visitor);
    }

    protected DefaultSerializerProvider _serializerProvider(SerializationConfig config) {
        return this._serializerProvider.createInstance(config, this._serializerFactory);
    }

    @Deprecated
    protected PrettyPrinter _defaultPrettyPrinter() {
        return this._serializationConfig.constructDefaultPrettyPrinter();
    }

    protected final void _configAndWriteValue(JsonGenerator g, Object value) throws IOException {
        SerializationConfig cfg = getSerializationConfig();
        cfg.initialize(g);
        if (cfg.isEnabled(SerializationFeature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _configAndWriteCloseable(g, value, cfg);
            return;
        }
        boolean closed = false;
        try {
            _serializerProvider(cfg).serializeValue(g, value);
            closed = true;
            g.close();
            if (1 == null) {
                g.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
                try {
                    g.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (!closed) {
                g.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
                try {
                    g.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    protected final void _configAndWriteValue(JsonGenerator g, Object value, Class<?> viewClass) throws IOException {
        SerializationConfig cfg = getSerializationConfig().withView((Class) viewClass);
        cfg.initialize(g);
        if (cfg.isEnabled(SerializationFeature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _configAndWriteCloseable(g, value, cfg);
            return;
        }
        boolean closed = false;
        try {
            _serializerProvider(cfg).serializeValue(g, value);
            closed = true;
            g.close();
            if (1 == null) {
                g.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
                try {
                    g.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (!closed) {
                g.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
                try {
                    g.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    private final void _configAndWriteCloseable(JsonGenerator g, Object value, SerializationConfig cfg) throws IOException, JsonGenerationException, JsonMappingException {
        Closeable toClose = (Closeable) value;
        try {
            _serializerProvider(cfg).serializeValue(g, value);
            JsonGenerator tmpGen = g;
            g = null;
            tmpGen.close();
            Closeable tmpToClose = toClose;
            toClose = null;
            tmpToClose.close();
            if (g != null) {
                g.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
                try {
                    g.close();
                } catch (IOException e) {
                }
            }
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e2) {
                }
            }
        } catch (Throwable th) {
            if (g != null) {
                g.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
                try {
                    g.close();
                } catch (IOException e3) {
                }
            }
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e4) {
                }
            }
        }
    }

    private final void _writeCloseableValue(JsonGenerator g, Object value, SerializationConfig cfg) throws IOException, JsonGenerationException, JsonMappingException {
        Closeable toClose = (Closeable) value;
        try {
            _serializerProvider(cfg).serializeValue(g, value);
            if (cfg.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
                g.flush();
            }
            Closeable tmpToClose = toClose;
            toClose = null;
            tmpToClose.close();
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    protected DefaultDeserializationContext createDeserializationContext(JsonParser jp, DeserializationConfig cfg) {
        return this._deserializationContext.createInstance(cfg, jp, this._injectableValues);
    }

    protected Object _readValue(DeserializationConfig cfg, JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        Object nullValue;
        JsonToken t = _initForReading(jp);
        DeserializationContext ctxt;
        if (t == JsonToken.VALUE_NULL) {
            ctxt = createDeserializationContext(jp, cfg);
            nullValue = _findRootDeserializer(ctxt, valueType).getNullValue(ctxt);
        } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            nullValue = null;
        } else {
            ctxt = createDeserializationContext(jp, cfg);
            JsonDeserializer<Object> deser = _findRootDeserializer(ctxt, valueType);
            if (cfg.useRootWrapping()) {
                nullValue = _unwrapAndDeserialize(jp, ctxt, cfg, valueType, deser);
            } else {
                nullValue = deser.deserialize(jp, ctxt);
            }
        }
        jp.clearCurrentToken();
        return nullValue;
    }

    protected Object _readMapAndClose(JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        try {
            Object nullValue;
            JsonToken t = _initForReading(jp);
            DeserializationContext ctxt;
            if (t == JsonToken.VALUE_NULL) {
                ctxt = createDeserializationContext(jp, getDeserializationConfig());
                nullValue = _findRootDeserializer(ctxt, valueType).getNullValue(ctxt);
            } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                nullValue = null;
            } else {
                DeserializationConfig cfg = getDeserializationConfig();
                ctxt = createDeserializationContext(jp, cfg);
                JsonDeserializer<Object> deser = _findRootDeserializer(ctxt, valueType);
                if (cfg.useRootWrapping()) {
                    nullValue = _unwrapAndDeserialize(jp, ctxt, cfg, valueType, deser);
                } else {
                    nullValue = deser.deserialize(jp, ctxt);
                }
                ctxt.checkUnresolvedObjectId();
            }
            jp.clearCurrentToken();
            return nullValue;
        } finally {
            try {
                jp.close();
            } catch (IOException e) {
            }
        }
    }

    protected JsonToken _initForReading(JsonParser p) throws IOException {
        this._deserializationConfig.initialize(p);
        JsonToken t = p.getCurrentToken();
        if (t == null) {
            t = p.nextToken();
            if (t == null) {
                throw JsonMappingException.from(p, "No content to map due to end-of-input");
            }
        }
        return t;
    }

    protected Object _unwrapAndDeserialize(JsonParser p, DeserializationContext ctxt, DeserializationConfig config, JavaType rootType, JsonDeserializer<Object> deser) throws IOException {
        String expSimpleName = config.findRootName(rootType).getSimpleName();
        if (p.getCurrentToken() != JsonToken.START_OBJECT) {
            throw JsonMappingException.from(p, "Current token not START_OBJECT (needed to unwrap root name '" + expSimpleName + "'), but " + p.getCurrentToken());
        } else if (p.nextToken() != JsonToken.FIELD_NAME) {
            throw JsonMappingException.from(p, "Current token not FIELD_NAME (to contain expected root name '" + expSimpleName + "'), but " + p.getCurrentToken());
        } else {
            String actualName = p.getCurrentName();
            if (expSimpleName.equals(actualName)) {
                p.nextToken();
                Object result = deser.deserialize(p, ctxt);
                if (p.nextToken() == JsonToken.END_OBJECT) {
                    return result;
                }
                throw JsonMappingException.from(p, "Current token not END_OBJECT (to match wrapper object with root name '" + expSimpleName + "'), but " + p.getCurrentToken());
            }
            throw JsonMappingException.from(p, "Root name '" + actualName + "' does not match expected ('" + expSimpleName + "') for type " + rootType);
        }
    }

    protected JsonDeserializer<Object> _findRootDeserializer(DeserializationContext ctxt, JavaType valueType) throws JsonMappingException {
        JsonDeserializer<Object> deser = (JsonDeserializer) this._rootDeserializers.get(valueType);
        if (deser != null) {
            return deser;
        }
        deser = ctxt.findRootValueDeserializer(valueType);
        if (deser == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + valueType);
        }
        this._rootDeserializers.put(valueType, deser);
        return deser;
    }

    protected void _verifySchemaType(FormatSchema schema) {
        if (schema != null && !this._jsonFactory.canUseSchema(schema)) {
            throw new IllegalArgumentException("Can not use FormatSchema of type " + schema.getClass().getName() + " for format " + this._jsonFactory.getFormatName());
        }
    }
}
