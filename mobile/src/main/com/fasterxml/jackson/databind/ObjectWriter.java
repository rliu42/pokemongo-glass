package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SegmentedStringWriter;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.util.Instantiatable;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.TypeWrappedSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class ObjectWriter implements Versioned, Serializable {
    protected static final PrettyPrinter NULL_PRETTY_PRINTER;
    private static final long serialVersionUID = 1;
    protected final SerializationConfig _config;
    protected final JsonFactory _generatorFactory;
    protected final GeneratorSettings _generatorSettings;
    protected final Prefetch _prefetch;
    protected final SerializerFactory _serializerFactory;
    protected final DefaultSerializerProvider _serializerProvider;

    public static final class GeneratorSettings implements Serializable {
        public static final GeneratorSettings empty;
        private static final long serialVersionUID = 1;
        public final CharacterEscapes characterEscapes;
        public final PrettyPrinter prettyPrinter;
        public final SerializableString rootValueSeparator;
        public final FormatSchema schema;

        static {
            empty = new GeneratorSettings(null, null, null, null);
        }

        public GeneratorSettings(PrettyPrinter pp, FormatSchema sch, CharacterEscapes esc, SerializableString rootSep) {
            this.prettyPrinter = pp;
            this.schema = sch;
            this.characterEscapes = esc;
            this.rootValueSeparator = rootSep;
        }

        public GeneratorSettings with(PrettyPrinter pp) {
            if (pp == null) {
                pp = ObjectWriter.NULL_PRETTY_PRINTER;
            }
            return pp == this.prettyPrinter ? this : new GeneratorSettings(pp, this.schema, this.characterEscapes, this.rootValueSeparator);
        }

        public GeneratorSettings with(FormatSchema sch) {
            return this.schema == sch ? this : new GeneratorSettings(this.prettyPrinter, sch, this.characterEscapes, this.rootValueSeparator);
        }

        public GeneratorSettings with(CharacterEscapes esc) {
            return this.characterEscapes == esc ? this : new GeneratorSettings(this.prettyPrinter, this.schema, esc, this.rootValueSeparator);
        }

        public GeneratorSettings withRootValueSeparator(String sep) {
            if (sep == null) {
                if (this.rootValueSeparator == null) {
                    return this;
                }
            } else if (sep.equals(this.rootValueSeparator)) {
                return this;
            }
            return new GeneratorSettings(this.prettyPrinter, this.schema, this.characterEscapes, sep == null ? null : new SerializedString(sep));
        }

        public GeneratorSettings withRootValueSeparator(SerializableString sep) {
            if (sep == null) {
                if (this.rootValueSeparator == null) {
                    return this;
                }
            } else if (this.rootValueSeparator != null && sep.getValue().equals(this.rootValueSeparator.getValue())) {
                return this;
            }
            return new GeneratorSettings(this.prettyPrinter, this.schema, this.characterEscapes, sep);
        }

        public void initialize(JsonGenerator gen) {
            PrettyPrinter pp = this.prettyPrinter;
            if (this.prettyPrinter != null) {
                if (pp == ObjectWriter.NULL_PRETTY_PRINTER) {
                    gen.setPrettyPrinter(null);
                } else {
                    if (pp instanceof Instantiatable) {
                        pp = (PrettyPrinter) ((Instantiatable) pp).createInstance();
                    }
                    gen.setPrettyPrinter(pp);
                }
            }
            if (this.characterEscapes != null) {
                gen.setCharacterEscapes(this.characterEscapes);
            }
            if (this.schema != null) {
                gen.setSchema(this.schema);
            }
            if (this.rootValueSeparator != null) {
                gen.setRootValueSeparator(this.rootValueSeparator);
            }
        }
    }

    public static final class Prefetch implements Serializable {
        public static final Prefetch empty;
        private static final long serialVersionUID = 1;
        private final JavaType rootType;
        private final TypeSerializer typeSerializer;
        private final JsonSerializer<Object> valueSerializer;

        static {
            empty = new Prefetch(null, null, null);
        }

        private Prefetch(JavaType rootT, JsonSerializer<Object> ser, TypeSerializer typeSer) {
            this.rootType = rootT;
            this.valueSerializer = ser;
            this.typeSerializer = typeSer;
        }

        public Prefetch forRootType(ObjectWriter parent, JavaType newType) {
            boolean noType = true;
            if (!(newType == null || newType.isJavaLangObject())) {
                noType = false;
            }
            if (noType) {
                if (this.rootType == null || this.valueSerializer == null) {
                    return this;
                }
                return new Prefetch(null, null, this.typeSerializer);
            } else if (newType.equals(this.rootType)) {
                return this;
            } else {
                if (parent.isEnabled(SerializationFeature.EAGER_SERIALIZER_FETCH)) {
                    try {
                        JsonSerializer<Object> ser = parent._serializerProvider().findTypedValueSerializer(newType, true, null);
                        if (ser instanceof TypeWrappedSerializer) {
                            return new Prefetch(newType, null, ((TypeWrappedSerializer) ser).typeSerializer());
                        }
                        return new Prefetch(newType, ser, null);
                    } catch (JsonProcessingException e) {
                    }
                }
                return new Prefetch(null, null, this.typeSerializer);
            }
        }

        public final JsonSerializer<Object> getValueSerializer() {
            return this.valueSerializer;
        }

        public final TypeSerializer getTypeSerializer() {
            return this.typeSerializer;
        }

        public boolean hasSerializer() {
            return (this.valueSerializer == null && this.typeSerializer == null) ? false : true;
        }

        public void serialize(JsonGenerator gen, Object value, DefaultSerializerProvider prov) throws IOException {
            if (this.typeSerializer != null) {
                prov.serializePolymorphic(gen, value, this.rootType, this.valueSerializer, this.typeSerializer);
            } else if (this.valueSerializer != null) {
                prov.serializeValue(gen, value, this.rootType, this.valueSerializer);
            } else {
                prov.serializeValue(gen, value);
            }
        }
    }

    static {
        NULL_PRETTY_PRINTER = new MinimalPrettyPrinter();
    }

    protected ObjectWriter(ObjectMapper mapper, SerializationConfig config, JavaType rootType, PrettyPrinter pp) {
        this._config = config;
        this._serializerProvider = mapper._serializerProvider;
        this._serializerFactory = mapper._serializerFactory;
        this._generatorFactory = mapper._jsonFactory;
        this._generatorSettings = pp == null ? GeneratorSettings.empty : new GeneratorSettings(pp, null, null, null);
        if (rootType == null || rootType.hasRawClass(Object.class)) {
            this._prefetch = Prefetch.empty;
        } else {
            this._prefetch = Prefetch.empty.forRootType(this, rootType.withStaticTyping());
        }
    }

    protected ObjectWriter(ObjectMapper mapper, SerializationConfig config) {
        this._config = config;
        this._serializerProvider = mapper._serializerProvider;
        this._serializerFactory = mapper._serializerFactory;
        this._generatorFactory = mapper._jsonFactory;
        this._generatorSettings = GeneratorSettings.empty;
        this._prefetch = Prefetch.empty;
    }

    protected ObjectWriter(ObjectMapper mapper, SerializationConfig config, FormatSchema s) {
        this._config = config;
        this._serializerProvider = mapper._serializerProvider;
        this._serializerFactory = mapper._serializerFactory;
        this._generatorFactory = mapper._jsonFactory;
        this._generatorSettings = s == null ? GeneratorSettings.empty : new GeneratorSettings(null, s, null, null);
        this._prefetch = Prefetch.empty;
    }

    protected ObjectWriter(ObjectWriter base, SerializationConfig config, GeneratorSettings genSettings, Prefetch prefetch) {
        this._config = config;
        this._serializerProvider = base._serializerProvider;
        this._serializerFactory = base._serializerFactory;
        this._generatorFactory = base._generatorFactory;
        this._generatorSettings = genSettings;
        this._prefetch = prefetch;
    }

    protected ObjectWriter(ObjectWriter base, SerializationConfig config) {
        this._config = config;
        this._serializerProvider = base._serializerProvider;
        this._serializerFactory = base._serializerFactory;
        this._generatorFactory = base._generatorFactory;
        this._generatorSettings = base._generatorSettings;
        this._prefetch = base._prefetch;
    }

    protected ObjectWriter(ObjectWriter base, JsonFactory f) {
        this._config = base._config.with(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, f.requiresPropertyOrdering());
        this._serializerProvider = base._serializerProvider;
        this._serializerFactory = base._serializerFactory;
        this._generatorFactory = base._generatorFactory;
        this._generatorSettings = base._generatorSettings;
        this._prefetch = base._prefetch;
    }

    public Version version() {
        return PackageVersion.VERSION;
    }

    protected ObjectWriter _new(ObjectWriter base, JsonFactory f) {
        return new ObjectWriter(base, f);
    }

    protected ObjectWriter _new(ObjectWriter base, SerializationConfig config) {
        return new ObjectWriter(base, config);
    }

    protected ObjectWriter _new(GeneratorSettings genSettings, Prefetch prefetch) {
        return new ObjectWriter(this, this._config, genSettings, prefetch);
    }

    protected SequenceWriter _newSequenceWriter(boolean wrapInArray, JsonGenerator gen, boolean managedInput) throws IOException {
        _configureGenerator(gen);
        return new SequenceWriter(_serializerProvider(), gen, managedInput, this._prefetch).init(wrapInArray);
    }

    public ObjectWriter with(SerializationFeature feature) {
        SerializationConfig newConfig = this._config.with(feature);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter with(SerializationFeature first, SerializationFeature... other) {
        SerializationConfig newConfig = this._config.with(first, other);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withFeatures(SerializationFeature... features) {
        SerializationConfig newConfig = this._config.withFeatures(features);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter without(SerializationFeature feature) {
        SerializationConfig newConfig = this._config.without(feature);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter without(SerializationFeature first, SerializationFeature... other) {
        SerializationConfig newConfig = this._config.without(first, other);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withoutFeatures(SerializationFeature... features) {
        SerializationConfig newConfig = this._config.withoutFeatures(features);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter with(Feature feature) {
        SerializationConfig newConfig = this._config.with(feature);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withFeatures(Feature... features) {
        SerializationConfig newConfig = this._config.withFeatures(features);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter without(Feature feature) {
        SerializationConfig newConfig = this._config.without(feature);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withoutFeatures(Feature... features) {
        SerializationConfig newConfig = this._config.withoutFeatures(features);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter forType(JavaType rootType) {
        Prefetch pf = this._prefetch.forRootType(this, rootType);
        return pf == this._prefetch ? this : _new(this._generatorSettings, pf);
    }

    public ObjectWriter forType(Class<?> rootType) {
        if (rootType == Object.class) {
            return forType((JavaType) null);
        }
        return forType(this._config.constructType((Class) rootType));
    }

    public ObjectWriter forType(TypeReference<?> rootType) {
        return forType(this._config.getTypeFactory().constructType(rootType.getType()));
    }

    @Deprecated
    public ObjectWriter withType(JavaType rootType) {
        return forType(rootType);
    }

    @Deprecated
    public ObjectWriter withType(Class<?> rootType) {
        return forType((Class) rootType);
    }

    @Deprecated
    public ObjectWriter withType(TypeReference<?> rootType) {
        return forType((TypeReference) rootType);
    }

    public ObjectWriter with(DateFormat df) {
        SerializationConfig newConfig = this._config.with(df);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withDefaultPrettyPrinter() {
        return with(this._config.getDefaultPrettyPrinter());
    }

    public ObjectWriter with(FilterProvider filterProvider) {
        return filterProvider == this._config.getFilterProvider() ? this : _new(this, this._config.withFilters(filterProvider));
    }

    public ObjectWriter with(PrettyPrinter pp) {
        GeneratorSettings genSet = this._generatorSettings.with(pp);
        return genSet == this._generatorSettings ? this : _new(genSet, this._prefetch);
    }

    public ObjectWriter withRootName(String rootName) {
        SerializationConfig newConfig = (SerializationConfig) this._config.withRootName(rootName);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withRootName(PropertyName rootName) {
        SerializationConfig newConfig = this._config.withRootName(rootName);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withoutRootName() {
        SerializationConfig newConfig = this._config.withRootName(PropertyName.NO_NAME);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter with(FormatSchema schema) {
        GeneratorSettings genSet = this._generatorSettings.with(schema);
        if (genSet == this._generatorSettings) {
            return this;
        }
        _verifySchemaType(schema);
        return _new(genSet, this._prefetch);
    }

    @Deprecated
    public ObjectWriter withSchema(FormatSchema schema) {
        return with(schema);
    }

    public ObjectWriter withView(Class<?> view) {
        SerializationConfig newConfig = this._config.withView((Class) view);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter with(Locale l) {
        SerializationConfig newConfig = this._config.with(l);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter with(TimeZone tz) {
        SerializationConfig newConfig = this._config.with(tz);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter with(Base64Variant b64variant) {
        SerializationConfig newConfig = this._config.with(b64variant);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter with(CharacterEscapes escapes) {
        GeneratorSettings genSet = this._generatorSettings.with(escapes);
        return genSet == this._generatorSettings ? this : _new(genSet, this._prefetch);
    }

    public ObjectWriter with(JsonFactory f) {
        return f == this._generatorFactory ? this : _new(this, f);
    }

    public ObjectWriter with(ContextAttributes attrs) {
        SerializationConfig newConfig = this._config.with(attrs);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withAttributes(Map<Object, Object> attrs) {
        SerializationConfig newConfig = (SerializationConfig) this._config.withAttributes(attrs);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withAttribute(Object key, Object value) {
        SerializationConfig newConfig = (SerializationConfig) this._config.withAttribute(key, value);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withoutAttribute(Object key) {
        SerializationConfig newConfig = (SerializationConfig) this._config.withoutAttribute(key);
        return newConfig == this._config ? this : _new(this, newConfig);
    }

    public ObjectWriter withRootValueSeparator(String sep) {
        GeneratorSettings genSet = this._generatorSettings.withRootValueSeparator(sep);
        return genSet == this._generatorSettings ? this : _new(genSet, this._prefetch);
    }

    public ObjectWriter withRootValueSeparator(SerializableString sep) {
        GeneratorSettings genSet = this._generatorSettings.withRootValueSeparator(sep);
        return genSet == this._generatorSettings ? this : _new(genSet, this._prefetch);
    }

    public SequenceWriter writeValues(File out) throws IOException {
        return _newSequenceWriter(false, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
    }

    public SequenceWriter writeValues(JsonGenerator gen) throws IOException {
        _configureGenerator(gen);
        return _newSequenceWriter(false, gen, false);
    }

    public SequenceWriter writeValues(Writer out) throws IOException {
        return _newSequenceWriter(false, this._generatorFactory.createGenerator(out), true);
    }

    public SequenceWriter writeValues(OutputStream out) throws IOException {
        return _newSequenceWriter(false, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
    }

    public SequenceWriter writeValuesAsArray(File out) throws IOException {
        return _newSequenceWriter(true, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
    }

    public SequenceWriter writeValuesAsArray(JsonGenerator gen) throws IOException {
        return _newSequenceWriter(true, gen, false);
    }

    public SequenceWriter writeValuesAsArray(Writer out) throws IOException {
        return _newSequenceWriter(true, this._generatorFactory.createGenerator(out), true);
    }

    public SequenceWriter writeValuesAsArray(OutputStream out) throws IOException {
        return _newSequenceWriter(true, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
    }

    public boolean isEnabled(SerializationFeature f) {
        return this._config.isEnabled(f);
    }

    public boolean isEnabled(MapperFeature f) {
        return this._config.isEnabled(f);
    }

    public boolean isEnabled(JsonParser.Feature f) {
        return this._generatorFactory.isEnabled(f);
    }

    public SerializationConfig getConfig() {
        return this._config;
    }

    public JsonFactory getFactory() {
        return this._generatorFactory;
    }

    public TypeFactory getTypeFactory() {
        return this._config.getTypeFactory();
    }

    public boolean hasPrefetchedSerializer() {
        return this._prefetch.hasSerializer();
    }

    public ContextAttributes getAttributes() {
        return this._config.getAttributes();
    }

    public void writeValue(JsonGenerator gen, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configureGenerator(gen);
        if (this._config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            Closeable toClose = (Closeable) value;
            try {
                this._prefetch.serialize(gen, value, _serializerProvider());
                if (this._config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
                    gen.flush();
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
        } else {
            this._prefetch.serialize(gen, value, _serializerProvider());
            if (this._config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
                gen.flush();
            }
        }
    }

    public void writeValue(File resultFile, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._generatorFactory.createGenerator(resultFile, JsonEncoding.UTF8), value);
    }

    public void writeValue(OutputStream out, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), value);
    }

    public void writeValue(Writer w, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._generatorFactory.createGenerator(w), value);
    }

    public String writeValueAsString(Object value) throws JsonProcessingException {
        Writer sw = new SegmentedStringWriter(this._generatorFactory._getBufferRecycler());
        try {
            _configAndWriteValue(this._generatorFactory.createGenerator(sw), value);
            return sw.getAndClear();
        } catch (JsonProcessingException e) {
            throw e;
        } catch (IOException e2) {
            throw JsonMappingException.fromUnexpectedIOE(e2);
        }
    }

    public byte[] writeValueAsBytes(Object value) throws JsonProcessingException {
        OutputStream bb = new ByteArrayBuilder(this._generatorFactory._getBufferRecycler());
        try {
            _configAndWriteValue(this._generatorFactory.createGenerator(bb, JsonEncoding.UTF8), value);
            byte[] result = bb.toByteArray();
            bb.release();
            return result;
        } catch (JsonProcessingException e) {
            throw e;
        } catch (IOException e2) {
            throw JsonMappingException.fromUnexpectedIOE(e2);
        }
    }

    public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
        if (type == null) {
            throw new IllegalArgumentException("type must be provided");
        }
        _serializerProvider().acceptJsonFormatVisitor(type, visitor);
    }

    public void acceptJsonFormatVisitor(Class<?> rawType, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
        acceptJsonFormatVisitor(this._config.constructType((Class) rawType), visitor);
    }

    public boolean canSerialize(Class<?> type) {
        return _serializerProvider().hasSerializerFor(type, null);
    }

    public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause) {
        return _serializerProvider().hasSerializerFor(type, cause);
    }

    protected DefaultSerializerProvider _serializerProvider() {
        return this._serializerProvider.createInstance(this._config, this._serializerFactory);
    }

    protected void _verifySchemaType(FormatSchema schema) {
        if (schema != null && !this._generatorFactory.canUseSchema(schema)) {
            throw new IllegalArgumentException("Can not use FormatSchema of type " + schema.getClass().getName() + " for format " + this._generatorFactory.getFormatName());
        }
    }

    protected final void _configAndWriteValue(JsonGenerator gen, Object value) throws IOException {
        _configureGenerator(gen);
        if (this._config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _writeCloseable(gen, value);
            return;
        }
        boolean closed = false;
        try {
            this._prefetch.serialize(gen, value, _serializerProvider());
            closed = true;
            gen.close();
            if (1 == null) {
                gen.disable(Feature.AUTO_CLOSE_JSON_CONTENT);
                try {
                    gen.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (!closed) {
                gen.disable(Feature.AUTO_CLOSE_JSON_CONTENT);
                try {
                    gen.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    private final void _writeCloseable(JsonGenerator gen, Object value) throws IOException {
        Closeable toClose = (Closeable) value;
        try {
            this._prefetch.serialize(gen, value, _serializerProvider());
            JsonGenerator tmpGen = gen;
            gen = null;
            tmpGen.close();
            Closeable tmpToClose = toClose;
            toClose = null;
            tmpToClose.close();
            if (gen != null) {
                gen.disable(Feature.AUTO_CLOSE_JSON_CONTENT);
                try {
                    gen.close();
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
            if (gen != null) {
                gen.disable(Feature.AUTO_CLOSE_JSON_CONTENT);
                try {
                    gen.close();
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

    protected final void _configureGenerator(JsonGenerator gen) {
        this._config.initialize(gen);
        this._generatorSettings.initialize(gen);
    }
}
