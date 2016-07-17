package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.filter.FilteringParserDelegate;
import com.fasterxml.jackson.core.filter.JsonPointerBasedFilter;
import com.fasterxml.jackson.core.filter.TokenFilter;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectReader extends ObjectCodec implements Versioned, Serializable {
    private static final JavaType JSON_NODE_TYPE;
    private static final long serialVersionUID = 1;
    protected final DeserializationConfig _config;
    protected final DefaultDeserializationContext _context;
    protected final DataFormatReaders _dataFormatReaders;
    private final TokenFilter _filter;
    protected final InjectableValues _injectableValues;
    protected final JsonFactory _parserFactory;
    protected final JsonDeserializer<Object> _rootDeserializer;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected final FormatSchema _schema;
    protected final boolean _unwrapRoot;
    protected final Object _valueToUpdate;
    protected final JavaType _valueType;

    static {
        JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
    }

    protected ObjectReader(ObjectMapper mapper, DeserializationConfig config) {
        this(mapper, config, null, null, null, null);
    }

    protected ObjectReader(ObjectMapper mapper, DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues) {
        this._config = config;
        this._context = mapper._deserializationContext;
        this._rootDeserializers = mapper._rootDeserializers;
        this._parserFactory = mapper._jsonFactory;
        this._valueType = valueType;
        this._valueToUpdate = valueToUpdate;
        if (valueToUpdate == null || !valueType.isArrayType()) {
            this._schema = schema;
            this._injectableValues = injectableValues;
            this._unwrapRoot = config.useRootWrapping();
            this._rootDeserializer = _prefetchRootDeserializer(valueType);
            this._dataFormatReaders = null;
            this._filter = null;
            return;
        }
        throw new IllegalArgumentException("Can not update an array value");
    }

    protected ObjectReader(ObjectReader base, DeserializationConfig config, JavaType valueType, JsonDeserializer<Object> rootDeser, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues, DataFormatReaders dataFormatReaders) {
        this._config = config;
        this._context = base._context;
        this._rootDeserializers = base._rootDeserializers;
        this._parserFactory = base._parserFactory;
        this._valueType = valueType;
        this._rootDeserializer = rootDeser;
        this._valueToUpdate = valueToUpdate;
        if (valueToUpdate == null || !valueType.isArrayType()) {
            this._schema = schema;
            this._injectableValues = injectableValues;
            this._unwrapRoot = config.useRootWrapping();
            this._dataFormatReaders = dataFormatReaders;
            this._filter = base._filter;
            return;
        }
        throw new IllegalArgumentException("Can not update an array value");
    }

    protected ObjectReader(ObjectReader base, DeserializationConfig config) {
        this._config = config;
        this._context = base._context;
        this._rootDeserializers = base._rootDeserializers;
        this._parserFactory = base._parserFactory;
        this._valueType = base._valueType;
        this._rootDeserializer = base._rootDeserializer;
        this._valueToUpdate = base._valueToUpdate;
        this._schema = base._schema;
        this._injectableValues = base._injectableValues;
        this._unwrapRoot = config.useRootWrapping();
        this._dataFormatReaders = base._dataFormatReaders;
        this._filter = base._filter;
    }

    protected ObjectReader(ObjectReader base, JsonFactory f) {
        this._config = base._config.with(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, f.requiresPropertyOrdering());
        this._context = base._context;
        this._rootDeserializers = base._rootDeserializers;
        this._parserFactory = f;
        this._valueType = base._valueType;
        this._rootDeserializer = base._rootDeserializer;
        this._valueToUpdate = base._valueToUpdate;
        this._schema = base._schema;
        this._injectableValues = base._injectableValues;
        this._unwrapRoot = base._unwrapRoot;
        this._dataFormatReaders = base._dataFormatReaders;
        this._filter = base._filter;
    }

    protected ObjectReader(ObjectReader base, TokenFilter filter) {
        this._config = base._config;
        this._context = base._context;
        this._rootDeserializers = base._rootDeserializers;
        this._parserFactory = base._parserFactory;
        this._valueType = base._valueType;
        this._rootDeserializer = base._rootDeserializer;
        this._valueToUpdate = base._valueToUpdate;
        this._schema = base._schema;
        this._injectableValues = base._injectableValues;
        this._unwrapRoot = base._unwrapRoot;
        this._dataFormatReaders = base._dataFormatReaders;
        this._filter = filter;
    }

    public Version version() {
        return PackageVersion.VERSION;
    }

    protected ObjectReader _new(ObjectReader base, JsonFactory f) {
        return new ObjectReader(base, f);
    }

    protected ObjectReader _new(ObjectReader base, DeserializationConfig config) {
        return new ObjectReader(base, config);
    }

    protected ObjectReader _new(ObjectReader base, DeserializationConfig config, JavaType valueType, JsonDeserializer<Object> rootDeser, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues, DataFormatReaders dataFormatReaders) {
        return new ObjectReader(base, config, valueType, rootDeser, valueToUpdate, schema, injectableValues, dataFormatReaders);
    }

    protected <T> MappingIterator<T> _newIterator(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser, boolean parserManaged) {
        return new MappingIterator(this._valueType, p, ctxt, deser, parserManaged, this._valueToUpdate);
    }

    protected JsonToken _initForReading(JsonParser p) throws IOException {
        if (this._schema != null) {
            p.setSchema(this._schema);
        }
        this._config.initialize(p);
        JsonToken t = p.getCurrentToken();
        if (t == null) {
            t = p.nextToken();
            if (t == null) {
                throw JsonMappingException.from(p, "No content to map due to end-of-input");
            }
        }
        return t;
    }

    protected void _initForMultiRead(JsonParser p) throws IOException {
        if (this._schema != null) {
            p.setSchema(this._schema);
        }
        this._config.initialize(p);
    }

    public ObjectReader with(DeserializationFeature feature) {
        return _with(this._config.with(feature));
    }

    public ObjectReader with(DeserializationFeature first, DeserializationFeature... other) {
        return _with(this._config.with(first, other));
    }

    public ObjectReader withFeatures(DeserializationFeature... features) {
        return _with(this._config.withFeatures(features));
    }

    public ObjectReader without(DeserializationFeature feature) {
        return _with(this._config.without(feature));
    }

    public ObjectReader without(DeserializationFeature first, DeserializationFeature... other) {
        return _with(this._config.without(first, other));
    }

    public ObjectReader withoutFeatures(DeserializationFeature... features) {
        return _with(this._config.withoutFeatures(features));
    }

    public ObjectReader with(Feature feature) {
        return _with(this._config.with(feature));
    }

    public ObjectReader withFeatures(Feature... features) {
        return _with(this._config.withFeatures(features));
    }

    public ObjectReader without(Feature feature) {
        return _with(this._config.without(feature));
    }

    public ObjectReader withoutFeatures(Feature... features) {
        return _with(this._config.withoutFeatures(features));
    }

    public ObjectReader with(DeserializationConfig config) {
        return _with(config);
    }

    public ObjectReader with(InjectableValues injectableValues) {
        if (this._injectableValues == injectableValues) {
            return this;
        }
        return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, this._schema, injectableValues, this._dataFormatReaders);
    }

    public ObjectReader with(JsonNodeFactory f) {
        return _with(this._config.with(f));
    }

    public ObjectReader with(JsonFactory f) {
        if (f == this._parserFactory) {
            return this;
        }
        ObjectReader r = _new(this, f);
        if (f.getCodec() == null) {
            f.setCodec(r);
        }
        return r;
    }

    public ObjectReader withRootName(String rootName) {
        return _with((DeserializationConfig) this._config.withRootName(rootName));
    }

    public ObjectReader withRootName(PropertyName rootName) {
        return _with(this._config.withRootName(rootName));
    }

    public ObjectReader withoutRootName() {
        return _with(this._config.withRootName(PropertyName.NO_NAME));
    }

    public ObjectReader with(FormatSchema schema) {
        if (this._schema == schema) {
            return this;
        }
        _verifySchemaType(schema);
        return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, schema, this._injectableValues, this._dataFormatReaders);
    }

    public ObjectReader forType(JavaType valueType) {
        if (valueType != null && valueType.equals(this._valueType)) {
            return this;
        }
        JsonDeserializer<Object> rootDeser = _prefetchRootDeserializer(valueType);
        DataFormatReaders det = this._dataFormatReaders;
        if (det != null) {
            det = det.withType(valueType);
        }
        return _new(this, this._config, valueType, rootDeser, this._valueToUpdate, this._schema, this._injectableValues, det);
    }

    public ObjectReader forType(Class<?> valueType) {
        return forType(this._config.constructType((Class) valueType));
    }

    public ObjectReader forType(TypeReference<?> valueTypeRef) {
        return forType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
    }

    @Deprecated
    public ObjectReader withType(JavaType valueType) {
        return forType(valueType);
    }

    @Deprecated
    public ObjectReader withType(Class<?> valueType) {
        return forType(this._config.constructType((Class) valueType));
    }

    @Deprecated
    public ObjectReader withType(Type valueType) {
        return forType(this._config.getTypeFactory().constructType(valueType));
    }

    @Deprecated
    public ObjectReader withType(TypeReference<?> valueTypeRef) {
        return forType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
    }

    public ObjectReader withValueToUpdate(Object value) {
        if (value == this._valueToUpdate) {
            return this;
        }
        if (value == null) {
            throw new IllegalArgumentException("cat not update null value");
        }
        JavaType t;
        if (this._valueType == null) {
            t = this._config.constructType(value.getClass());
        } else {
            t = this._valueType;
        }
        return _new(this, this._config, t, this._rootDeserializer, value, this._schema, this._injectableValues, this._dataFormatReaders);
    }

    public ObjectReader withView(Class<?> activeView) {
        return _with(this._config.withView((Class) activeView));
    }

    public ObjectReader with(Locale l) {
        return _with(this._config.with(l));
    }

    public ObjectReader with(TimeZone tz) {
        return _with(this._config.with(tz));
    }

    public ObjectReader withHandler(DeserializationProblemHandler h) {
        return _with(this._config.withHandler(h));
    }

    public ObjectReader with(Base64Variant defaultBase64) {
        return _with(this._config.with(defaultBase64));
    }

    public ObjectReader withFormatDetection(ObjectReader... readers) {
        return withFormatDetection(new DataFormatReaders(readers));
    }

    public ObjectReader withFormatDetection(DataFormatReaders readers) {
        return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, this._schema, this._injectableValues, readers);
    }

    public ObjectReader with(ContextAttributes attrs) {
        return _with(this._config.with(attrs));
    }

    public ObjectReader withAttributes(Map<Object, Object> attrs) {
        return _with((DeserializationConfig) this._config.withAttributes(attrs));
    }

    public ObjectReader withAttribute(Object key, Object value) {
        return _with((DeserializationConfig) this._config.withAttribute(key, value));
    }

    public ObjectReader withoutAttribute(Object key) {
        return _with((DeserializationConfig) this._config.withoutAttribute(key));
    }

    protected ObjectReader _with(DeserializationConfig newConfig) {
        if (newConfig == this._config) {
            return this;
        }
        ObjectReader r = _new(this, newConfig);
        if (this._dataFormatReaders != null) {
            r = r.withFormatDetection(this._dataFormatReaders.with(newConfig));
        }
        return r;
    }

    public boolean isEnabled(DeserializationFeature f) {
        return this._config.isEnabled(f);
    }

    public boolean isEnabled(MapperFeature f) {
        return this._config.isEnabled(f);
    }

    public boolean isEnabled(Feature f) {
        return this._parserFactory.isEnabled(f);
    }

    public DeserializationConfig getConfig() {
        return this._config;
    }

    public JsonFactory getFactory() {
        return this._parserFactory;
    }

    public TypeFactory getTypeFactory() {
        return this._config.getTypeFactory();
    }

    public ContextAttributes getAttributes() {
        return this._config.getAttributes();
    }

    public InjectableValues getInjectableValues() {
        return this._injectableValues;
    }

    public <T> T readValue(JsonParser jp) throws IOException, JsonProcessingException {
        return _bind(jp, this._valueToUpdate);
    }

    public <T> T readValue(JsonParser jp, Class<T> valueType) throws IOException, JsonProcessingException {
        return forType((Class) valueType).readValue(jp);
    }

    public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef) throws IOException, JsonProcessingException {
        return forType((TypeReference) valueTypeRef).readValue(jp);
    }

    public <T> T readValue(JsonParser jp, ResolvedType valueType) throws IOException, JsonProcessingException {
        return forType((JavaType) valueType).readValue(jp);
    }

    public <T> T readValue(JsonParser jp, JavaType valueType) throws IOException, JsonProcessingException {
        return forType(valueType).readValue(jp);
    }

    public <T> Iterator<T> readValues(JsonParser jp, Class<T> valueType) throws IOException, JsonProcessingException {
        return forType((Class) valueType).readValues(jp);
    }

    public <T> Iterator<T> readValues(JsonParser jp, TypeReference<?> valueTypeRef) throws IOException, JsonProcessingException {
        return forType((TypeReference) valueTypeRef).readValues(jp);
    }

    public <T> Iterator<T> readValues(JsonParser jp, ResolvedType valueType) throws IOException, JsonProcessingException {
        return readValues(jp, (JavaType) valueType);
    }

    public <T> Iterator<T> readValues(JsonParser jp, JavaType valueType) throws IOException, JsonProcessingException {
        return forType(valueType).readValues(jp);
    }

    public JsonNode createArrayNode() {
        return this._config.getNodeFactory().arrayNode();
    }

    public JsonNode createObjectNode() {
        return this._config.getNodeFactory().objectNode();
    }

    public JsonParser treeAsTokens(TreeNode n) {
        return new TreeTraversingParser((JsonNode) n, this);
    }

    public <T extends TreeNode> T readTree(JsonParser jp) throws IOException, JsonProcessingException {
        return _bindAsTree(jp);
    }

    public void writeTree(JsonGenerator jgen, TreeNode rootNode) {
        throw new UnsupportedOperationException();
    }

    public <T> T readValue(InputStream src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            return _detectBindAndClose(this._dataFormatReaders.findFormat(src), false);
        }
        return _bindAndClose(_considerFilter(this._parserFactory.createParser(src)));
    }

    public <T> T readValue(Reader src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            _reportUndetectableSource(src);
        }
        return _bindAndClose(_considerFilter(this._parserFactory.createParser(src)));
    }

    public <T> T readValue(String src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            _reportUndetectableSource(src);
        }
        return _bindAndClose(_considerFilter(this._parserFactory.createParser(src)));
    }

    public <T> T readValue(byte[] src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            return _detectBindAndClose(src, 0, src.length);
        }
        return _bindAndClose(_considerFilter(this._parserFactory.createParser(src)));
    }

    public <T> T readValue(byte[] src, int offset, int length) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            return _detectBindAndClose(src, offset, length);
        }
        return _bindAndClose(_considerFilter(this._parserFactory.createParser(src, offset, length)));
    }

    public <T> T readValue(File src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            return _detectBindAndClose(this._dataFormatReaders.findFormat(_inputStream(src)), true);
        }
        return _bindAndClose(_considerFilter(this._parserFactory.createParser(src)));
    }

    public <T> T readValue(URL src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            return _detectBindAndClose(this._dataFormatReaders.findFormat(_inputStream(src)), true);
        }
        return _bindAndClose(_considerFilter(this._parserFactory.createParser(src)));
    }

    public <T> T readValue(JsonNode src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            _reportUndetectableSource(src);
        }
        return _bindAndClose(_considerFilter(treeAsTokens(src)));
    }

    public JsonNode readTree(InputStream in) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            return _detectBindAndCloseAsTree(in);
        }
        return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(in)));
    }

    public JsonNode readTree(Reader r) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            _reportUndetectableSource(r);
        }
        return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(r)));
    }

    public JsonNode readTree(String json) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            _reportUndetectableSource(json);
        }
        return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(json)));
    }

    public <T> MappingIterator<T> readValues(JsonParser p) throws IOException, JsonProcessingException {
        DeserializationContext ctxt = createDeserializationContext(p);
        return _newIterator(p, ctxt, _findRootDeserializer(ctxt), false);
    }

    public <T> MappingIterator<T> readValues(InputStream src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            return _detectBindAndReadValues(this._dataFormatReaders.findFormat(src), false);
        }
        return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src)));
    }

    public <T> MappingIterator<T> readValues(Reader src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            _reportUndetectableSource(src);
        }
        JsonParser p = _considerFilter(this._parserFactory.createParser(src));
        _initForMultiRead(p);
        p.nextToken();
        DeserializationContext ctxt = createDeserializationContext(p);
        return _newIterator(p, ctxt, _findRootDeserializer(ctxt), true);
    }

    public <T> MappingIterator<T> readValues(String json) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            _reportUndetectableSource(json);
        }
        JsonParser p = _considerFilter(this._parserFactory.createParser(json));
        _initForMultiRead(p);
        p.nextToken();
        DeserializationContext ctxt = createDeserializationContext(p);
        return _newIterator(p, ctxt, _findRootDeserializer(ctxt), true);
    }

    public <T> MappingIterator<T> readValues(byte[] src, int offset, int length) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            return _detectBindAndReadValues(this._dataFormatReaders.findFormat(src, offset, length), false);
        }
        return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src)));
    }

    public final <T> MappingIterator<T> readValues(byte[] src) throws IOException, JsonProcessingException {
        return readValues(src, 0, src.length);
    }

    public <T> MappingIterator<T> readValues(File src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            return _detectBindAndReadValues(this._dataFormatReaders.findFormat(_inputStream(src)), false);
        }
        return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src)));
    }

    public <T> MappingIterator<T> readValues(URL src) throws IOException, JsonProcessingException {
        if (this._dataFormatReaders != null) {
            return _detectBindAndReadValues(this._dataFormatReaders.findFormat(_inputStream(src)), true);
        }
        return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src)));
    }

    public <T> T treeToValue(TreeNode n, Class<T> valueType) throws JsonProcessingException {
        try {
            return readValue(treeAsTokens(n), (Class) valueType);
        } catch (JsonProcessingException e) {
            throw e;
        } catch (IOException e2) {
            throw new IllegalArgumentException(e2.getMessage(), e2);
        }
    }

    public void writeValue(JsonGenerator gen, Object value) throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException("Not implemented for ObjectReader");
    }

    protected Object _bind(JsonParser p, Object valueToUpdate) throws IOException {
        Object result;
        JsonToken t = _initForReading(p);
        DeserializationContext ctxt;
        if (t == JsonToken.VALUE_NULL) {
            if (valueToUpdate == null) {
                ctxt = createDeserializationContext(p);
                result = _findRootDeserializer(ctxt).getNullValue(ctxt);
            } else {
                result = valueToUpdate;
            }
        } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            result = valueToUpdate;
        } else {
            ctxt = createDeserializationContext(p);
            JsonDeserializer<Object> deser = _findRootDeserializer(ctxt);
            if (this._unwrapRoot) {
                result = _unwrapAndDeserialize(p, ctxt, this._valueType, deser);
            } else if (valueToUpdate == null) {
                result = deser.deserialize(p, ctxt);
            } else {
                deser.deserialize(p, ctxt, valueToUpdate);
                result = valueToUpdate;
            }
        }
        p.clearCurrentToken();
        return result;
    }

    protected JsonParser _considerFilter(JsonParser p) {
        return (this._filter == null || FilteringParserDelegate.class.isInstance(p)) ? p : new FilteringParserDelegate(p, this._filter, false, false);
    }

    protected Object _bindAndClose(JsonParser p) throws IOException {
        try {
            Object result;
            JsonToken t = _initForReading(p);
            DeserializationContext ctxt;
            if (t == JsonToken.VALUE_NULL) {
                if (this._valueToUpdate == null) {
                    ctxt = createDeserializationContext(p);
                    result = _findRootDeserializer(ctxt).getNullValue(ctxt);
                } else {
                    result = this._valueToUpdate;
                }
            } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                result = this._valueToUpdate;
            } else {
                ctxt = createDeserializationContext(p);
                JsonDeserializer<Object> deser = _findRootDeserializer(ctxt);
                if (this._unwrapRoot) {
                    result = _unwrapAndDeserialize(p, ctxt, this._valueType, deser);
                } else if (this._valueToUpdate == null) {
                    result = deser.deserialize(p, ctxt);
                } else {
                    deser.deserialize(p, ctxt, this._valueToUpdate);
                    result = this._valueToUpdate;
                }
            }
            return result;
        } finally {
            try {
                p.close();
            } catch (IOException e) {
            }
        }
    }

    protected JsonNode _bindAndCloseAsTree(JsonParser p) throws IOException {
        try {
            JsonNode _bindAsTree = _bindAsTree(p);
            return _bindAsTree;
        } finally {
            try {
                p.close();
            } catch (IOException e) {
            }
        }
    }

    protected JsonNode _bindAsTree(JsonParser p) throws IOException {
        JsonNode result;
        JsonToken t = _initForReading(p);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            result = NullNode.instance;
        } else {
            DeserializationContext ctxt = createDeserializationContext(p);
            JsonDeserializer<Object> deser = _findTreeDeserializer(ctxt);
            if (this._unwrapRoot) {
                result = (JsonNode) _unwrapAndDeserialize(p, ctxt, JSON_NODE_TYPE, deser);
            } else {
                result = (JsonNode) deser.deserialize(p, ctxt);
            }
        }
        p.clearCurrentToken();
        return result;
    }

    protected <T> MappingIterator<T> _bindAndReadValues(JsonParser p) throws IOException {
        _initForMultiRead(p);
        p.nextToken();
        DeserializationContext ctxt = createDeserializationContext(p);
        return _newIterator(p, ctxt, _findRootDeserializer(ctxt), true);
    }

    protected Object _unwrapAndDeserialize(JsonParser p, DeserializationContext ctxt, JavaType rootType, JsonDeserializer<Object> deser) throws IOException {
        String expSimpleName = this._config.findRootName(rootType).getSimpleName();
        if (p.getCurrentToken() != JsonToken.START_OBJECT) {
            throw JsonMappingException.from(p, "Current token not START_OBJECT (needed to unwrap root name '" + expSimpleName + "'), but " + p.getCurrentToken());
        } else if (p.nextToken() != JsonToken.FIELD_NAME) {
            throw JsonMappingException.from(p, "Current token not FIELD_NAME (to contain expected root name '" + expSimpleName + "'), but " + p.getCurrentToken());
        } else {
            String actualName = p.getCurrentName();
            if (expSimpleName.equals(actualName)) {
                Object result;
                p.nextToken();
                if (this._valueToUpdate == null) {
                    result = deser.deserialize(p, ctxt);
                } else {
                    deser.deserialize(p, ctxt, this._valueToUpdate);
                    result = this._valueToUpdate;
                }
                if (p.nextToken() == JsonToken.END_OBJECT) {
                    return result;
                }
                throw JsonMappingException.from(p, "Current token not END_OBJECT (to match wrapper object with root name '" + expSimpleName + "'), but " + p.getCurrentToken());
            }
            throw JsonMappingException.from(p, "Root name '" + actualName + "' does not match expected ('" + expSimpleName + "') for type " + rootType);
        }
    }

    protected Object _detectBindAndClose(byte[] src, int offset, int length) throws IOException {
        Match match = this._dataFormatReaders.findFormat(src, offset, length);
        if (!match.hasMatch()) {
            _reportUnkownFormat(this._dataFormatReaders, match);
        }
        return match.getReader()._bindAndClose(match.createParserWithMatch());
    }

    protected Object _detectBindAndClose(Match match, boolean forceClosing) throws IOException {
        if (!match.hasMatch()) {
            _reportUnkownFormat(this._dataFormatReaders, match);
        }
        JsonParser p = match.createParserWithMatch();
        if (forceClosing) {
            p.enable(Feature.AUTO_CLOSE_SOURCE);
        }
        return match.getReader()._bindAndClose(p);
    }

    protected <T> MappingIterator<T> _detectBindAndReadValues(Match match, boolean forceClosing) throws IOException, JsonProcessingException {
        if (!match.hasMatch()) {
            _reportUnkownFormat(this._dataFormatReaders, match);
        }
        JsonParser p = match.createParserWithMatch();
        if (forceClosing) {
            p.enable(Feature.AUTO_CLOSE_SOURCE);
        }
        return match.getReader()._bindAndReadValues(p);
    }

    protected JsonNode _detectBindAndCloseAsTree(InputStream in) throws IOException {
        Match match = this._dataFormatReaders.findFormat(in);
        if (!match.hasMatch()) {
            _reportUnkownFormat(this._dataFormatReaders, match);
        }
        JsonParser p = match.createParserWithMatch();
        p.enable(Feature.AUTO_CLOSE_SOURCE);
        return match.getReader()._bindAndCloseAsTree(p);
    }

    protected void _reportUnkownFormat(DataFormatReaders detector, Match match) throws JsonProcessingException {
        throw new JsonParseException("Can not detect format from input, does not look like any of detectable formats " + detector.toString(), JsonLocation.NA);
    }

    protected void _verifySchemaType(FormatSchema schema) {
        if (schema != null && !this._parserFactory.canUseSchema(schema)) {
            throw new IllegalArgumentException("Can not use FormatSchema of type " + schema.getClass().getName() + " for format " + this._parserFactory.getFormatName());
        }
    }

    protected DefaultDeserializationContext createDeserializationContext(JsonParser p) {
        return this._context.createInstance(this._config, p, this._injectableValues);
    }

    protected void _reportUndetectableSource(Object src) throws JsonProcessingException {
        throw new JsonParseException("Can not use source of type " + src.getClass().getName() + " with format auto-detection: must be byte- not char-based", JsonLocation.NA);
    }

    protected InputStream _inputStream(URL src) throws IOException {
        return src.openStream();
    }

    protected InputStream _inputStream(File f) throws IOException {
        return new FileInputStream(f);
    }

    protected JsonDeserializer<Object> _findRootDeserializer(DeserializationContext ctxt) throws JsonMappingException {
        if (this._rootDeserializer != null) {
            return this._rootDeserializer;
        }
        JavaType t = this._valueType;
        if (t == null) {
            throw new JsonMappingException("No value type configured for ObjectReader");
        }
        JsonDeserializer<Object> deser = (JsonDeserializer) this._rootDeserializers.get(t);
        if (deser != null) {
            return deser;
        }
        deser = ctxt.findRootValueDeserializer(t);
        if (deser == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + t);
        }
        this._rootDeserializers.put(t, deser);
        return deser;
    }

    protected JsonDeserializer<Object> _findTreeDeserializer(DeserializationContext ctxt) throws JsonMappingException {
        JsonDeserializer<Object> deser = (JsonDeserializer) this._rootDeserializers.get(JSON_NODE_TYPE);
        if (deser == null) {
            deser = ctxt.findRootValueDeserializer(JSON_NODE_TYPE);
            if (deser == null) {
                throw new JsonMappingException("Can not find a deserializer for type " + JSON_NODE_TYPE);
            }
            this._rootDeserializers.put(JSON_NODE_TYPE, deser);
        }
        return deser;
    }

    protected JsonDeserializer<Object> _prefetchRootDeserializer(JavaType valueType) {
        JsonDeserializer<Object> jsonDeserializer = null;
        if (valueType != null && this._config.isEnabled(DeserializationFeature.EAGER_DESERIALIZER_FETCH)) {
            jsonDeserializer = (JsonDeserializer) this._rootDeserializers.get(valueType);
            if (jsonDeserializer == null) {
                try {
                    jsonDeserializer = createDeserializationContext(null).findRootValueDeserializer(valueType);
                    if (jsonDeserializer != null) {
                        this._rootDeserializers.put(valueType, jsonDeserializer);
                    }
                } catch (JsonProcessingException e) {
                }
            }
        }
        return jsonDeserializer;
    }

    public ObjectReader at(String value) {
        return new ObjectReader(this, new JsonPointerBasedFilter(value));
    }

    public ObjectReader at(JsonPointer pointer) {
        return new ObjectReader(this, new JsonPointerBasedFilter(pointer));
    }
}
