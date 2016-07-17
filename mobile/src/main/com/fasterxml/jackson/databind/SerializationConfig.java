package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Instantiatable;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfigBase;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.RootNameLookup;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public final class SerializationConfig extends MapperConfigBase<SerializationFeature, SerializationConfig> implements Serializable {
    protected static final PrettyPrinter DEFAULT_PRETTY_PRINTER;
    private static final long serialVersionUID = 1;
    protected final PrettyPrinter _defaultPrettyPrinter;
    protected final FilterProvider _filterProvider;
    protected final int _generatorFeatures;
    protected final int _generatorFeaturesToChange;
    protected final int _serFeatures;
    protected Include _serializationInclusion;

    static {
        DEFAULT_PRETTY_PRINTER = new DefaultPrettyPrinter();
    }

    public SerializationConfig(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames) {
        super(base, str, mixins, rootNames);
        this._serializationInclusion = null;
        this._serFeatures = MapperConfig.collectFeatureDefaults(SerializationFeature.class);
        this._filterProvider = null;
        this._defaultPrettyPrinter = DEFAULT_PRETTY_PRINTER;
        this._generatorFeatures = 0;
        this._generatorFeaturesToChange = 0;
    }

    private SerializationConfig(SerializationConfig src, SubtypeResolver str) {
        super((MapperConfigBase) src, str);
        this._serializationInclusion = null;
        this._serFeatures = src._serFeatures;
        this._serializationInclusion = src._serializationInclusion;
        this._filterProvider = src._filterProvider;
        this._defaultPrettyPrinter = src._defaultPrettyPrinter;
        this._generatorFeatures = src._generatorFeatures;
        this._generatorFeaturesToChange = src._generatorFeaturesToChange;
    }

    private SerializationConfig(SerializationConfig src, int mapperFeatures, int serFeatures, int generatorFeatures, int generatorFeatureMask) {
        super((MapperConfigBase) src, mapperFeatures);
        this._serializationInclusion = null;
        this._serFeatures = serFeatures;
        this._serializationInclusion = src._serializationInclusion;
        this._filterProvider = src._filterProvider;
        this._defaultPrettyPrinter = src._defaultPrettyPrinter;
        this._generatorFeatures = generatorFeatures;
        this._generatorFeaturesToChange = generatorFeatureMask;
    }

    private SerializationConfig(SerializationConfig src, BaseSettings base) {
        super((MapperConfigBase) src, base);
        this._serializationInclusion = null;
        this._serFeatures = src._serFeatures;
        this._serializationInclusion = src._serializationInclusion;
        this._filterProvider = src._filterProvider;
        this._defaultPrettyPrinter = src._defaultPrettyPrinter;
        this._generatorFeatures = src._generatorFeatures;
        this._generatorFeaturesToChange = src._generatorFeaturesToChange;
    }

    private SerializationConfig(SerializationConfig src, FilterProvider filters) {
        super(src);
        this._serializationInclusion = null;
        this._serFeatures = src._serFeatures;
        this._serializationInclusion = src._serializationInclusion;
        this._filterProvider = filters;
        this._defaultPrettyPrinter = src._defaultPrettyPrinter;
        this._generatorFeatures = src._generatorFeatures;
        this._generatorFeaturesToChange = src._generatorFeaturesToChange;
    }

    private SerializationConfig(SerializationConfig src, Class<?> view) {
        super((MapperConfigBase) src, (Class) view);
        this._serializationInclusion = null;
        this._serFeatures = src._serFeatures;
        this._serializationInclusion = src._serializationInclusion;
        this._filterProvider = src._filterProvider;
        this._defaultPrettyPrinter = src._defaultPrettyPrinter;
        this._generatorFeatures = src._generatorFeatures;
        this._generatorFeaturesToChange = src._generatorFeaturesToChange;
    }

    private SerializationConfig(SerializationConfig src, Include incl) {
        super(src);
        this._serializationInclusion = null;
        this._serFeatures = src._serFeatures;
        this._serializationInclusion = incl;
        this._filterProvider = src._filterProvider;
        this._defaultPrettyPrinter = src._defaultPrettyPrinter;
        this._generatorFeatures = src._generatorFeatures;
        this._generatorFeaturesToChange = src._generatorFeaturesToChange;
    }

    private SerializationConfig(SerializationConfig src, PropertyName rootName) {
        super((MapperConfigBase) src, rootName);
        this._serializationInclusion = null;
        this._serFeatures = src._serFeatures;
        this._serializationInclusion = src._serializationInclusion;
        this._filterProvider = src._filterProvider;
        this._defaultPrettyPrinter = src._defaultPrettyPrinter;
        this._generatorFeatures = src._generatorFeatures;
        this._generatorFeaturesToChange = src._generatorFeaturesToChange;
    }

    protected SerializationConfig(SerializationConfig src, ContextAttributes attrs) {
        super((MapperConfigBase) src, attrs);
        this._serializationInclusion = null;
        this._serFeatures = src._serFeatures;
        this._serializationInclusion = src._serializationInclusion;
        this._filterProvider = src._filterProvider;
        this._defaultPrettyPrinter = src._defaultPrettyPrinter;
        this._generatorFeatures = src._generatorFeatures;
        this._generatorFeaturesToChange = src._generatorFeaturesToChange;
    }

    protected SerializationConfig(SerializationConfig src, SimpleMixInResolver mixins) {
        super((MapperConfigBase) src, mixins);
        this._serializationInclusion = null;
        this._serFeatures = src._serFeatures;
        this._serializationInclusion = src._serializationInclusion;
        this._filterProvider = src._filterProvider;
        this._defaultPrettyPrinter = src._defaultPrettyPrinter;
        this._generatorFeatures = src._generatorFeatures;
        this._generatorFeaturesToChange = src._generatorFeaturesToChange;
    }

    protected SerializationConfig(SerializationConfig src, PrettyPrinter defaultPP) {
        super(src);
        this._serializationInclusion = null;
        this._serFeatures = src._serFeatures;
        this._serializationInclusion = src._serializationInclusion;
        this._filterProvider = src._filterProvider;
        this._defaultPrettyPrinter = defaultPP;
        this._generatorFeatures = src._generatorFeatures;
        this._generatorFeaturesToChange = src._generatorFeaturesToChange;
    }

    protected SerializationConfig(SerializationConfig src, SimpleMixInResolver mixins, RootNameLookup rootNames) {
        super(src, mixins, rootNames);
        this._serializationInclusion = null;
        this._serFeatures = src._serFeatures;
        this._serializationInclusion = src._serializationInclusion;
        this._filterProvider = src._filterProvider;
        this._defaultPrettyPrinter = src._defaultPrettyPrinter;
        this._generatorFeatures = src._generatorFeatures;
        this._generatorFeaturesToChange = src._generatorFeaturesToChange;
    }

    public SerializationConfig with(MapperFeature... features) {
        int newMapperFlags = this._mapperFeatures;
        for (MapperFeature f : features) {
            newMapperFlags |= f.getMask();
        }
        return newMapperFlags == this._mapperFeatures ? this : new SerializationConfig(this, newMapperFlags, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
    }

    public SerializationConfig without(MapperFeature... features) {
        int newMapperFlags = this._mapperFeatures;
        for (MapperFeature f : features) {
            newMapperFlags &= f.getMask() ^ -1;
        }
        return newMapperFlags == this._mapperFeatures ? this : new SerializationConfig(this, newMapperFlags, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
    }

    public SerializationConfig with(MapperFeature feature, boolean state) {
        int newMapperFlags;
        if (state) {
            newMapperFlags = this._mapperFeatures | feature.getMask();
        } else {
            newMapperFlags = this._mapperFeatures & (feature.getMask() ^ -1);
        }
        return newMapperFlags == this._mapperFeatures ? this : new SerializationConfig(this, newMapperFlags, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
    }

    public SerializationConfig with(AnnotationIntrospector ai) {
        return _withBase(this._base.withAnnotationIntrospector(ai));
    }

    public SerializationConfig withAppendedAnnotationIntrospector(AnnotationIntrospector ai) {
        return _withBase(this._base.withAppendedAnnotationIntrospector(ai));
    }

    public SerializationConfig withInsertedAnnotationIntrospector(AnnotationIntrospector ai) {
        return _withBase(this._base.withInsertedAnnotationIntrospector(ai));
    }

    public SerializationConfig with(ClassIntrospector ci) {
        return _withBase(this._base.withClassIntrospector(ci));
    }

    public SerializationConfig with(DateFormat df) {
        SerializationConfig cfg = new SerializationConfig(this, this._base.withDateFormat(df));
        if (df == null) {
            return cfg.with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }
        return cfg.without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public SerializationConfig with(HandlerInstantiator hi) {
        return _withBase(this._base.withHandlerInstantiator(hi));
    }

    public SerializationConfig with(PropertyNamingStrategy pns) {
        return _withBase(this._base.withPropertyNamingStrategy(pns));
    }

    public SerializationConfig withRootName(PropertyName rootName) {
        if (rootName == null) {
            if (this._rootName == null) {
                return this;
            }
        } else if (rootName.equals(this._rootName)) {
            return this;
        }
        return new SerializationConfig(this, rootName);
    }

    public SerializationConfig with(SubtypeResolver str) {
        return str == this._subtypeResolver ? this : new SerializationConfig(this, str);
    }

    public SerializationConfig with(TypeFactory tf) {
        return _withBase(this._base.withTypeFactory(tf));
    }

    public SerializationConfig with(TypeResolverBuilder<?> trb) {
        return _withBase(this._base.withTypeResolverBuilder(trb));
    }

    public SerializationConfig withView(Class<?> view) {
        return this._view == view ? this : new SerializationConfig(this, (Class) view);
    }

    public SerializationConfig with(VisibilityChecker<?> vc) {
        return _withBase(this._base.withVisibilityChecker(vc));
    }

    public SerializationConfig withVisibility(PropertyAccessor forMethod, Visibility visibility) {
        return _withBase(this._base.withVisibility(forMethod, visibility));
    }

    public SerializationConfig with(Locale l) {
        return _withBase(this._base.with(l));
    }

    public SerializationConfig with(TimeZone tz) {
        return _withBase(this._base.with(tz));
    }

    public SerializationConfig with(Base64Variant base64) {
        return _withBase(this._base.with(base64));
    }

    public SerializationConfig with(ContextAttributes attrs) {
        return attrs == this._attributes ? this : new SerializationConfig(this, attrs);
    }

    private final SerializationConfig _withBase(BaseSettings newBase) {
        return this._base == newBase ? this : new SerializationConfig(this, newBase);
    }

    public SerializationConfig with(SerializationFeature feature) {
        int newSerFeatures = this._serFeatures | feature.getMask();
        return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
    }

    public SerializationConfig with(SerializationFeature first, SerializationFeature... features) {
        int newSerFeatures = this._serFeatures | first.getMask();
        for (SerializationFeature f : features) {
            newSerFeatures |= f.getMask();
        }
        return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
    }

    public SerializationConfig withFeatures(SerializationFeature... features) {
        int newSerFeatures = this._serFeatures;
        for (SerializationFeature f : features) {
            newSerFeatures |= f.getMask();
        }
        return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
    }

    public SerializationConfig without(SerializationFeature feature) {
        int newSerFeatures = this._serFeatures & (feature.getMask() ^ -1);
        return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
    }

    public SerializationConfig without(SerializationFeature first, SerializationFeature... features) {
        int newSerFeatures = this._serFeatures & (first.getMask() ^ -1);
        for (SerializationFeature f : features) {
            newSerFeatures &= f.getMask() ^ -1;
        }
        return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
    }

    public SerializationConfig withoutFeatures(SerializationFeature... features) {
        int newSerFeatures = this._serFeatures;
        for (SerializationFeature f : features) {
            newSerFeatures &= f.getMask() ^ -1;
        }
        return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
    }

    public SerializationConfig with(Feature feature) {
        int newSet = this._generatorFeatures | feature.getMask();
        int newMask = this._generatorFeaturesToChange | feature.getMask();
        return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask);
    }

    public SerializationConfig withFeatures(Feature... features) {
        int newSet = this._generatorFeatures;
        int newMask = this._generatorFeaturesToChange;
        for (Feature f : features) {
            int mask = f.getMask();
            newSet |= mask;
            newMask |= mask;
        }
        return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask);
    }

    public SerializationConfig without(Feature feature) {
        int newSet = this._generatorFeatures & (feature.getMask() ^ -1);
        int newMask = this._generatorFeaturesToChange | feature.getMask();
        return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask);
    }

    public SerializationConfig withoutFeatures(Feature... features) {
        int newSet = this._generatorFeatures;
        int newMask = this._generatorFeaturesToChange;
        for (Feature f : features) {
            int mask = f.getMask();
            newSet &= mask ^ -1;
            newMask |= mask;
        }
        return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask);
    }

    public SerializationConfig withFilters(FilterProvider filterProvider) {
        return filterProvider == this._filterProvider ? this : new SerializationConfig(this, filterProvider);
    }

    public SerializationConfig withSerializationInclusion(Include incl) {
        return this._serializationInclusion == incl ? this : new SerializationConfig(this, incl);
    }

    public SerializationConfig withDefaultPrettyPrinter(PrettyPrinter pp) {
        return this._defaultPrettyPrinter == pp ? this : new SerializationConfig(this, pp);
    }

    public PrettyPrinter constructDefaultPrettyPrinter() {
        PrettyPrinter pp = this._defaultPrettyPrinter;
        if (pp instanceof Instantiatable) {
            return (PrettyPrinter) ((Instantiatable) pp).createInstance();
        }
        return pp;
    }

    public void initialize(JsonGenerator g) {
        if (SerializationFeature.INDENT_OUTPUT.enabledIn(this._serFeatures) && g.getPrettyPrinter() == null) {
            PrettyPrinter pp = constructDefaultPrettyPrinter();
            if (pp != null) {
                g.setPrettyPrinter(pp);
            }
        }
        boolean useBigDec = SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN.enabledIn(this._serFeatures);
        if (this._generatorFeaturesToChange != 0 || useBigDec) {
            int orig = g.getFeatureMask();
            int newFlags = ((this._generatorFeaturesToChange ^ -1) & orig) | this._generatorFeatures;
            if (useBigDec) {
                newFlags |= Feature.WRITE_BIGDECIMAL_AS_PLAIN.getMask();
            }
            if (orig != newFlags) {
                g.setFeatureMask(newFlags);
            }
        }
    }

    public boolean useRootWrapping() {
        if (this._rootName != null) {
            return !this._rootName.isEmpty();
        } else {
            return isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
        }
    }

    public AnnotationIntrospector getAnnotationIntrospector() {
        if (isEnabled(MapperFeature.USE_ANNOTATIONS)) {
            return super.getAnnotationIntrospector();
        }
        return AnnotationIntrospector.nopInstance();
    }

    public BeanDescription introspectClassAnnotations(JavaType type) {
        return getClassIntrospector().forClassAnnotations(this, type, this);
    }

    public BeanDescription introspectDirectClassAnnotations(JavaType type) {
        return getClassIntrospector().forDirectClassAnnotations(this, type, this);
    }

    public VisibilityChecker<?> getDefaultVisibilityChecker() {
        VisibilityChecker<?> vchecker = super.getDefaultVisibilityChecker();
        if (!isEnabled(MapperFeature.AUTO_DETECT_GETTERS)) {
            vchecker = vchecker.withGetterVisibility(Visibility.NONE);
        }
        if (!isEnabled(MapperFeature.AUTO_DETECT_IS_GETTERS)) {
            vchecker = vchecker.withIsGetterVisibility(Visibility.NONE);
        }
        if (isEnabled(MapperFeature.AUTO_DETECT_FIELDS)) {
            return vchecker;
        }
        return vchecker.withFieldVisibility(Visibility.NONE);
    }

    public final boolean isEnabled(SerializationFeature f) {
        return (this._serFeatures & f.getMask()) != 0;
    }

    public final boolean isEnabled(Feature f, JsonFactory factory) {
        if ((this._generatorFeaturesToChange & f.getMask()) != 0) {
            return (this._generatorFeatures & f.getMask()) != 0;
        } else {
            return factory.isEnabled(f);
        }
    }

    public final boolean hasSerializationFeatures(int featureMask) {
        return (this._serFeatures & featureMask) == featureMask;
    }

    public final int getSerializationFeatures() {
        return this._serFeatures;
    }

    public Include getSerializationInclusion() {
        if (this._serializationInclusion != null) {
            return this._serializationInclusion;
        }
        return Include.ALWAYS;
    }

    public FilterProvider getFilterProvider() {
        return this._filterProvider;
    }

    public PrettyPrinter getDefaultPrettyPrinter() {
        return this._defaultPrettyPrinter;
    }

    public <T extends BeanDescription> T introspect(JavaType type) {
        return getClassIntrospector().forSerialization(this, type, this);
    }

    public String toString() {
        return "[SerializationConfig: flags=0x" + Integer.toHexString(this._serFeatures) + "]";
    }
}
