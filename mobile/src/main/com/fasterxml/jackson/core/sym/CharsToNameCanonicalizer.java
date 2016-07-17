package com.fasterxml.jackson.core.sym;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.core.util.InternCache;
import java.util.Arrays;
import java.util.BitSet;
import spacemadness.com.lunarconsole.BuildConfig;

public final class CharsToNameCanonicalizer {
    protected static final int DEFAULT_T_SIZE = 64;
    public static final int HASH_MULT = 33;
    static final int MAX_COLL_CHAIN_LENGTH = 100;
    static final int MAX_ENTRIES_FOR_REUSE = 12000;
    protected static final int MAX_T_SIZE = 65536;
    static final CharsToNameCanonicalizer sBootstrapSymbolTable;
    protected Bucket[] _buckets;
    protected boolean _canonicalize;
    protected boolean _dirty;
    protected final int _flags;
    private final int _hashSeed;
    protected int _indexMask;
    protected int _longestCollisionList;
    protected BitSet _overflows;
    protected CharsToNameCanonicalizer _parent;
    protected int _size;
    protected int _sizeThreshold;
    protected String[] _symbols;

    static final class Bucket {
        public final int length;
        public final Bucket next;
        public final String symbol;

        public Bucket(String s, Bucket n) {
            this.symbol = s;
            this.next = n;
            this.length = n == null ? 1 : n.length + 1;
        }

        public String has(char[] buf, int start, int len) {
            if (this.symbol.length() != len) {
                return null;
            }
            int i = 0;
            while (this.symbol.charAt(i) == buf[start + i]) {
                i++;
                if (i >= len) {
                    return this.symbol;
                }
            }
            return null;
        }
    }

    static {
        sBootstrapSymbolTable = new CharsToNameCanonicalizer();
    }

    public static CharsToNameCanonicalizer createRoot() {
        long now = System.currentTimeMillis();
        return createRoot((((int) now) + ((int) (now >>> 32))) | 1);
    }

    protected static CharsToNameCanonicalizer createRoot(int hashSeed) {
        return sBootstrapSymbolTable.makeOrphan(hashSeed);
    }

    private CharsToNameCanonicalizer() {
        this._canonicalize = true;
        this._flags = -1;
        this._dirty = true;
        this._hashSeed = 0;
        this._longestCollisionList = 0;
        initTables(DEFAULT_T_SIZE);
    }

    private void initTables(int initialSize) {
        this._symbols = new String[initialSize];
        this._buckets = new Bucket[(initialSize >> 1)];
        this._indexMask = initialSize - 1;
        this._size = 0;
        this._longestCollisionList = 0;
        this._sizeThreshold = _thresholdSize(initialSize);
    }

    private static int _thresholdSize(int hashAreaSize) {
        return hashAreaSize - (hashAreaSize >> 2);
    }

    private CharsToNameCanonicalizer(CharsToNameCanonicalizer parent, int flags, String[] symbols, Bucket[] buckets, int size, int hashSeed, int longestColl) {
        this._parent = parent;
        this._flags = flags;
        this._canonicalize = Feature.CANONICALIZE_FIELD_NAMES.enabledIn(flags);
        this._symbols = symbols;
        this._buckets = buckets;
        this._size = size;
        this._hashSeed = hashSeed;
        int arrayLen = symbols.length;
        this._sizeThreshold = _thresholdSize(arrayLen);
        this._indexMask = arrayLen - 1;
        this._longestCollisionList = longestColl;
        this._dirty = false;
    }

    public CharsToNameCanonicalizer makeChild(int flags) {
        String[] symbols;
        Bucket[] buckets;
        int size;
        int hashSeed;
        int longestCollisionList;
        synchronized (this) {
            symbols = this._symbols;
            buckets = this._buckets;
            size = this._size;
            hashSeed = this._hashSeed;
            longestCollisionList = this._longestCollisionList;
        }
        return new CharsToNameCanonicalizer(this, flags, symbols, buckets, size, hashSeed, longestCollisionList);
    }

    private CharsToNameCanonicalizer makeOrphan(int seed) {
        return new CharsToNameCanonicalizer(null, -1, this._symbols, this._buckets, this._size, seed, this._longestCollisionList);
    }

    private void mergeChild(CharsToNameCanonicalizer child) {
        if (child.size() > MAX_ENTRIES_FOR_REUSE) {
            synchronized (this) {
                initTables(AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
                this._dirty = false;
            }
        } else if (child.size() > size()) {
            synchronized (this) {
                this._symbols = child._symbols;
                this._buckets = child._buckets;
                this._size = child._size;
                this._sizeThreshold = child._sizeThreshold;
                this._indexMask = child._indexMask;
                this._longestCollisionList = child._longestCollisionList;
                this._dirty = false;
            }
        }
    }

    public void release() {
        if (maybeDirty() && this._parent != null && this._canonicalize) {
            this._parent.mergeChild(this);
            this._dirty = false;
        }
    }

    public int size() {
        return this._size;
    }

    public int bucketCount() {
        return this._symbols.length;
    }

    public boolean maybeDirty() {
        return this._dirty;
    }

    public int hashSeed() {
        return this._hashSeed;
    }

    public int collisionCount() {
        int count = 0;
        for (Bucket bucket : this._buckets) {
            if (bucket != null) {
                count += bucket.length;
            }
        }
        return count;
    }

    public int maxCollisionLength() {
        return this._longestCollisionList;
    }

    public String findSymbol(char[] buffer, int start, int len, int h) {
        if (len < 1) {
            return BuildConfig.FLAVOR;
        }
        if (!this._canonicalize) {
            return new String(buffer, start, len);
        }
        int index = _hashToIndex(h);
        String sym = this._symbols[index];
        if (sym != null) {
            if (sym.length() == len) {
                int i = 0;
                while (sym.charAt(i) == buffer[start + i]) {
                    i++;
                    if (i == len) {
                        return sym;
                    }
                }
            }
            Bucket b = this._buckets[index >> 1];
            if (b != null) {
                sym = b.has(buffer, start, len);
                if (sym != null) {
                    return sym;
                }
                sym = _findSymbol2(buffer, start, len, b.next);
                if (sym != null) {
                    return sym;
                }
            }
        }
        return _addSymbol(buffer, start, len, h, index);
    }

    private String _findSymbol2(char[] buffer, int start, int len, Bucket b) {
        while (b != null) {
            String sym = b.has(buffer, start, len);
            if (sym != null) {
                return sym;
            }
            b = b.next;
        }
        return null;
    }

    private String _addSymbol(char[] buffer, int start, int len, int h, int index) {
        if (!this._dirty) {
            copyArrays();
            this._dirty = true;
        } else if (this._size >= this._sizeThreshold) {
            rehash();
            index = _hashToIndex(calcHash(buffer, start, len));
        }
        String newSymbol = new String(buffer, start, len);
        if (Feature.INTERN_FIELD_NAMES.enabledIn(this._flags)) {
            newSymbol = InternCache.instance.intern(newSymbol);
        }
        this._size++;
        if (this._symbols[index] == null) {
            this._symbols[index] = newSymbol;
        } else {
            int bix = index >> 1;
            Bucket newB = new Bucket(newSymbol, this._buckets[bix]);
            int collLen = newB.length;
            if (collLen > MAX_COLL_CHAIN_LENGTH) {
                _handleSpillOverflow(bix, newB);
            } else {
                this._buckets[bix] = newB;
                this._longestCollisionList = Math.max(collLen, this._longestCollisionList);
            }
        }
        return newSymbol;
    }

    private void _handleSpillOverflow(int bindex, Bucket newBucket) {
        if (this._overflows == null) {
            this._overflows = new BitSet();
            this._overflows.set(bindex);
        } else if (this._overflows.get(bindex)) {
            if (Feature.FAIL_ON_SYMBOL_HASH_OVERFLOW.enabledIn(this._flags)) {
                reportTooManyCollisions(MAX_COLL_CHAIN_LENGTH);
            }
            this._canonicalize = false;
        } else {
            this._overflows.set(bindex);
        }
        this._symbols[bindex + bindex] = newBucket.symbol;
        this._buckets[bindex] = null;
        this._size -= newBucket.length;
        this._longestCollisionList = -1;
    }

    public int _hashToIndex(int rawHash) {
        rawHash += rawHash >>> 15;
        rawHash ^= rawHash << 7;
        return this._indexMask & (rawHash + (rawHash >>> 3));
    }

    public int calcHash(char[] buffer, int start, int len) {
        int hash = this._hashSeed;
        for (int i = start; i < start + len; i++) {
            hash = (hash * HASH_MULT) + buffer[i];
        }
        return hash == 0 ? 1 : hash;
    }

    public int calcHash(String key) {
        int len = key.length();
        int hash = this._hashSeed;
        for (int i = 0; i < len; i++) {
            hash = (hash * HASH_MULT) + key.charAt(i);
        }
        return hash == 0 ? 1 : hash;
    }

    private void copyArrays() {
        String[] oldSyms = this._symbols;
        this._symbols = (String[]) Arrays.copyOf(oldSyms, oldSyms.length);
        Bucket[] oldBuckets = this._buckets;
        this._buckets = (Bucket[]) Arrays.copyOf(oldBuckets, oldBuckets.length);
    }

    private void rehash() {
        int size = this._symbols.length;
        int newSize = size + size;
        if (newSize > MAX_T_SIZE) {
            this._size = 0;
            this._canonicalize = false;
            this._symbols = new String[DEFAULT_T_SIZE];
            this._buckets = new Bucket[32];
            this._indexMask = 63;
            this._dirty = true;
            return;
        }
        int i;
        int index;
        String[] oldSyms = this._symbols;
        Bucket[] oldBuckets = this._buckets;
        this._symbols = new String[newSize];
        this._buckets = new Bucket[(newSize >> 1)];
        this._indexMask = newSize - 1;
        this._sizeThreshold = _thresholdSize(newSize);
        int count = 0;
        int maxColl = 0;
        for (i = 0; i < size; i++) {
            String symbol = oldSyms[i];
            if (symbol != null) {
                count++;
                index = _hashToIndex(calcHash(symbol));
                if (this._symbols[index] == null) {
                    this._symbols[index] = symbol;
                } else {
                    int bix = index >> 1;
                    Bucket newB = new Bucket(symbol, this._buckets[bix]);
                    this._buckets[bix] = newB;
                    maxColl = Math.max(maxColl, newB.length);
                }
            }
        }
        size >>= 1;
        for (i = 0; i < size; i++) {
            for (Bucket b = oldBuckets[i]; b != null; b = b.next) {
                count++;
                symbol = b.symbol;
                index = _hashToIndex(calcHash(symbol));
                if (this._symbols[index] == null) {
                    this._symbols[index] = symbol;
                } else {
                    bix = index >> 1;
                    newB = new Bucket(symbol, this._buckets[bix]);
                    this._buckets[bix] = newB;
                    maxColl = Math.max(maxColl, newB.length);
                }
            }
        }
        this._longestCollisionList = maxColl;
        this._overflows = null;
        if (count != this._size) {
            throw new Error("Internal error on SymbolTable.rehash(): had " + this._size + " entries; now have " + count + ".");
        }
    }

    protected void reportTooManyCollisions(int maxLen) {
        throw new IllegalStateException("Longest collision chain in symbol table (of size " + this._size + ") now exceeds maximum, " + maxLen + " -- suspect a DoS attack based on hash collisions");
    }
}
