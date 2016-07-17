package com.fasterxml.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;
import java.util.TimeZone;
import spacemadness.com.lunarconsole.BuildConfig;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonFormat {
    public static final String DEFAULT_LOCALE = "##default";
    public static final String DEFAULT_TIMEZONE = "##default";

    public enum Feature {
        ACCEPT_SINGLE_VALUE_AS_ARRAY,
        WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
        WRITE_DATES_WITH_ZONE_ID,
        WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED,
        WRITE_SORTED_MAP_ENTRIES
    }

    public static class Features {
        private static final Features EMPTY;
        private final int disabled;
        private final int enabled;

        static {
            EMPTY = new Features(0, 0);
        }

        private Features(int e, int d) {
            this.enabled = e;
            this.disabled = d;
        }

        public static Features empty() {
            return EMPTY;
        }

        public static Features construct(JsonFormat f) {
            return construct(f.with(), f.without());
        }

        public static Features construct(Feature[] enabled, Feature[] disabled) {
            int e = 0;
            for (Feature f : enabled) {
                e |= 1 << f.ordinal();
            }
            int d = 0;
            for (Feature f2 : disabled) {
                d |= 1 << f2.ordinal();
            }
            return new Features(e, d);
        }

        public Features with(Feature... features) {
            int e = this.enabled;
            for (Feature f : features) {
                e |= 1 << f.ordinal();
            }
            return e == this.enabled ? this : new Features(e, this.disabled);
        }

        public Features without(Feature... features) {
            int d = this.disabled;
            for (Feature f : features) {
                d |= 1 << f.ordinal();
            }
            return d == this.disabled ? this : new Features(this.enabled, d);
        }

        public Boolean get(Feature f) {
            int mask = 1 << f.ordinal();
            if ((this.disabled & mask) != 0) {
                return Boolean.FALSE;
            }
            if ((this.enabled & mask) != 0) {
                return Boolean.TRUE;
            }
            return null;
        }
    }

    public enum Shape {
        ANY,
        SCALAR,
        ARRAY,
        OBJECT,
        NUMBER,
        NUMBER_FLOAT,
        NUMBER_INT,
        STRING,
        BOOLEAN;

        public boolean isNumeric() {
            return this == NUMBER || this == NUMBER_INT || this == NUMBER_FLOAT;
        }

        public boolean isStructured() {
            return this == OBJECT || this == ARRAY;
        }
    }

    public static class Value implements JacksonAnnotationValue<JsonFormat> {
        private TimeZone _timezone;
        private final Features features;
        private final Locale locale;
        private final String pattern;
        private final Shape shape;
        private final String timezoneStr;

        public Value() {
            this(BuildConfig.FLAVOR, Shape.ANY, BuildConfig.FLAVOR, BuildConfig.FLAVOR, Features.empty());
        }

        public Value(JsonFormat ann) {
            this(ann.pattern(), ann.shape(), ann.locale(), ann.timezone(), Features.construct(ann));
        }

        public Value(String p, Shape sh, String localeStr, String tzStr, Features f) {
            Locale locale = (localeStr == null || localeStr.length() == 0 || JsonFormat.DEFAULT_TIMEZONE.equals(localeStr)) ? null : new Locale(localeStr);
            String str = (tzStr == null || tzStr.length() == 0 || JsonFormat.DEFAULT_TIMEZONE.equals(tzStr)) ? null : tzStr;
            this(p, sh, locale, str, null, f);
        }

        public Value(String p, Shape sh, Locale l, TimeZone tz, Features f) {
            this.pattern = p;
            if (sh == null) {
                sh = Shape.ANY;
            }
            this.shape = sh;
            this.locale = l;
            this._timezone = tz;
            this.timezoneStr = null;
            if (f == null) {
                f = Features.empty();
            }
            this.features = f;
        }

        public Value(String p, Shape sh, Locale l, String tzStr, TimeZone tz, Features f) {
            this.pattern = p;
            if (sh == null) {
                sh = Shape.ANY;
            }
            this.shape = sh;
            this.locale = l;
            this._timezone = tz;
            this.timezoneStr = tzStr;
            if (f == null) {
                f = Features.empty();
            }
            this.features = f;
        }

        @Deprecated
        public Value(String p, Shape sh, Locale l, TimeZone tz) {
            this(p, sh, l, tz, Features.empty());
        }

        @Deprecated
        public Value(String p, Shape sh, String localeStr, String tzStr) {
            this(p, sh, localeStr, tzStr, Features.empty());
        }

        @Deprecated
        public Value(String p, Shape sh, Locale l, String tzStr, TimeZone tz) {
            this(p, sh, l, tzStr, tz, Features.empty());
        }

        public static Value forPattern(String p) {
            return new Value(p, null, null, null, null, Features.empty());
        }

        public Value withPattern(String p) {
            return new Value(p, this.shape, this.locale, this.timezoneStr, this._timezone, this.features);
        }

        public Value withShape(Shape s) {
            return new Value(this.pattern, s, this.locale, this.timezoneStr, this._timezone, this.features);
        }

        public Value withLocale(Locale l) {
            return new Value(this.pattern, this.shape, l, this.timezoneStr, this._timezone, this.features);
        }

        public Value withTimeZone(TimeZone tz) {
            return new Value(this.pattern, this.shape, this.locale, null, tz, this.features);
        }

        public Value withFeature(Feature f) {
            Features newFeats = this.features.with(f);
            return newFeats == this.features ? this : new Value(this.pattern, this.shape, this.locale, this.timezoneStr, this._timezone, newFeats);
        }

        public Value withoutFeature(Feature f) {
            Features newFeats = this.features.without(f);
            return newFeats == this.features ? this : new Value(this.pattern, this.shape, this.locale, this.timezoneStr, this._timezone, newFeats);
        }

        public Class<JsonFormat> valueFor() {
            return JsonFormat.class;
        }

        public String getPattern() {
            return this.pattern;
        }

        public Shape getShape() {
            return this.shape;
        }

        public Locale getLocale() {
            return this.locale;
        }

        public String timeZoneAsString() {
            if (this._timezone != null) {
                return this._timezone.getID();
            }
            return this.timezoneStr;
        }

        public TimeZone getTimeZone() {
            TimeZone tz = this._timezone;
            if (tz == null) {
                if (this.timezoneStr == null) {
                    return null;
                }
                tz = TimeZone.getTimeZone(this.timezoneStr);
                this._timezone = tz;
            }
            return tz;
        }

        public boolean hasShape() {
            return this.shape != Shape.ANY;
        }

        public boolean hasPattern() {
            return this.pattern != null && this.pattern.length() > 0;
        }

        public boolean hasLocale() {
            return this.locale != null;
        }

        public boolean hasTimeZone() {
            return (this._timezone == null && (this.timezoneStr == null || this.timezoneStr.isEmpty())) ? false : true;
        }

        public Boolean getFeature(Feature f) {
            return this.features.get(f);
        }
    }

    String locale() default "##default";

    String pattern() default "";

    Shape shape() default Shape.ANY;

    String timezone() default "##default";

    Feature[] with() default {};

    Feature[] without() default {};
}
