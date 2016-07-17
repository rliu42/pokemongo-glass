package com.fasterxml.jackson.databind.util;

import java.io.Serializable;

public abstract class NameTransformer {
    public static final NameTransformer NOP;

    /* renamed from: com.fasterxml.jackson.databind.util.NameTransformer.1 */
    static class C01561 extends NameTransformer {
        final /* synthetic */ String val$prefix;
        final /* synthetic */ String val$suffix;

        C01561(String str, String str2) {
            this.val$prefix = str;
            this.val$suffix = str2;
        }

        public String transform(String name) {
            return this.val$prefix + name + this.val$suffix;
        }

        public String reverse(String transformed) {
            if (transformed.startsWith(this.val$prefix)) {
                String str = transformed.substring(this.val$prefix.length());
                if (str.endsWith(this.val$suffix)) {
                    return str.substring(0, str.length() - this.val$suffix.length());
                }
            }
            return null;
        }

        public String toString() {
            return "[PreAndSuffixTransformer('" + this.val$prefix + "','" + this.val$suffix + "')]";
        }
    }

    /* renamed from: com.fasterxml.jackson.databind.util.NameTransformer.2 */
    static class C01572 extends NameTransformer {
        final /* synthetic */ String val$prefix;

        C01572(String str) {
            this.val$prefix = str;
        }

        public String transform(String name) {
            return this.val$prefix + name;
        }

        public String reverse(String transformed) {
            if (transformed.startsWith(this.val$prefix)) {
                return transformed.substring(this.val$prefix.length());
            }
            return null;
        }

        public String toString() {
            return "[PrefixTransformer('" + this.val$prefix + "')]";
        }
    }

    /* renamed from: com.fasterxml.jackson.databind.util.NameTransformer.3 */
    static class C01583 extends NameTransformer {
        final /* synthetic */ String val$suffix;

        C01583(String str) {
            this.val$suffix = str;
        }

        public String transform(String name) {
            return name + this.val$suffix;
        }

        public String reverse(String transformed) {
            if (transformed.endsWith(this.val$suffix)) {
                return transformed.substring(0, transformed.length() - this.val$suffix.length());
            }
            return null;
        }

        public String toString() {
            return "[SuffixTransformer('" + this.val$suffix + "')]";
        }
    }

    public static class Chained extends NameTransformer implements Serializable {
        private static final long serialVersionUID = 1;
        protected final NameTransformer _t1;
        protected final NameTransformer _t2;

        public Chained(NameTransformer t1, NameTransformer t2) {
            this._t1 = t1;
            this._t2 = t2;
        }

        public String transform(String name) {
            return this._t1.transform(this._t2.transform(name));
        }

        public String reverse(String transformed) {
            transformed = this._t1.reverse(transformed);
            if (transformed != null) {
                return this._t2.reverse(transformed);
            }
            return transformed;
        }

        public String toString() {
            return "[ChainedTransformer(" + this._t1 + ", " + this._t2 + ")]";
        }
    }

    protected static final class NopTransformer extends NameTransformer implements Serializable {
        private static final long serialVersionUID = 1;

        protected NopTransformer() {
        }

        public String transform(String name) {
            return name;
        }

        public String reverse(String transformed) {
            return transformed;
        }
    }

    public abstract String reverse(String str);

    public abstract String transform(String str);

    static {
        NOP = new NopTransformer();
    }

    protected NameTransformer() {
    }

    public static NameTransformer simpleTransformer(String prefix, String suffix) {
        boolean hasPrefix;
        boolean hasSuffix = true;
        if (prefix == null || prefix.length() <= 0) {
            hasPrefix = false;
        } else {
            hasPrefix = true;
        }
        if (suffix == null || suffix.length() <= 0) {
            hasSuffix = false;
        }
        if (!hasPrefix) {
            return hasSuffix ? new C01583(suffix) : NOP;
        } else {
            if (hasSuffix) {
                return new C01561(prefix, suffix);
            }
            return new C01572(prefix);
        }
    }

    public static NameTransformer chainedTransformer(NameTransformer t1, NameTransformer t2) {
        return new Chained(t1, t2);
    }
}
