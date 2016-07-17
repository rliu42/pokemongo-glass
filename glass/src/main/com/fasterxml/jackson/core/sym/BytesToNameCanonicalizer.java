package com.fasterxml.jackson.core.sym;

import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.MotionEventCompat;
import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.core.util.InternCache;
import java.util.Arrays;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicReference;
import spacemadness.com.lunarconsole.C1391R;

@Deprecated
public final class BytesToNameCanonicalizer {
    private static final int DEFAULT_T_SIZE = 64;
    static final int INITIAL_COLLISION_LEN = 32;
    static final int LAST_VALID_BUCKET = 254;
    private static final int MAX_COLL_CHAIN_LENGTH = 200;
    static final int MAX_ENTRIES_FOR_REUSE = 6000;
    private static final int MAX_T_SIZE = 65536;
    static final int MIN_HASH_SIZE = 16;
    private static final int MULT = 33;
    private static final int MULT2 = 65599;
    private static final int MULT3 = 31;
    protected int _collCount;
    protected int _collEnd;
    protected Bucket[] _collList;
    private boolean _collListShared;
    protected int _count;
    protected final boolean _failOnDoS;
    protected int[] _hash;
    protected int _hashMask;
    private boolean _hashShared;
    protected boolean _intern;
    protected int _longestCollisionList;
    protected Name[] _mainNames;
    private boolean _namesShared;
    private transient boolean _needRehash;
    protected BitSet _overflows;
    protected final BytesToNameCanonicalizer _parent;
    private final int _seed;
    protected final AtomicReference<TableInfo> _tableInfo;

    private static final class Bucket {
        public final int hash;
        public final int length;
        public final Name name;
        public final Bucket next;

        Bucket(Name name, Bucket next) {
            this.name = name;
            this.next = next;
            this.length = next == null ? 1 : next.length + 1;
            this.hash = name.hashCode();
        }

        public Name find(int h, int firstQuad, int secondQuad) {
            if (this.hash == h && this.name.equals(firstQuad, secondQuad)) {
                return this.name;
            }
            for (Bucket curr = this.next; curr != null; curr = curr.next) {
                if (curr.hash == h) {
                    Name currName = curr.name;
                    if (currName.equals(firstQuad, secondQuad)) {
                        return currName;
                    }
                }
            }
            return null;
        }

        public Name find(int h, int q1, int q2, int q3) {
            if (this.hash == h && this.name.equals(q1, q2, q3)) {
                return this.name;
            }
            for (Bucket curr = this.next; curr != null; curr = curr.next) {
                if (curr.hash == h) {
                    Name currName = curr.name;
                    if (currName.equals(q1, q2, q3)) {
                        return currName;
                    }
                }
            }
            return null;
        }

        public Name find(int h, int[] quads, int qlen) {
            if (this.hash == h && this.name.equals(quads, qlen)) {
                return this.name;
            }
            for (Bucket curr = this.next; curr != null; curr = curr.next) {
                if (curr.hash == h) {
                    Name currName = curr.name;
                    if (currName.equals(quads, qlen)) {
                        return currName;
                    }
                }
            }
            return null;
        }
    }

    private static final class TableInfo {
        public final int collCount;
        public final int collEnd;
        public final Bucket[] collList;
        public final int count;
        public final int longestCollisionList;
        public final int[] mainHash;
        public final int mainHashMask;
        public final Name[] mainNames;

        public TableInfo(int count, int mainHashMask, int[] mainHash, Name[] mainNames, Bucket[] collList, int collCount, int collEnd, int longestCollisionList) {
            this.count = count;
            this.mainHashMask = mainHashMask;
            this.mainHash = mainHash;
            this.mainNames = mainNames;
            this.collList = collList;
            this.collCount = collCount;
            this.collEnd = collEnd;
            this.longestCollisionList = longestCollisionList;
        }

        public TableInfo(BytesToNameCanonicalizer src) {
            this.count = src._count;
            this.mainHashMask = src._hashMask;
            this.mainHash = src._hash;
            this.mainNames = src._mainNames;
            this.collList = src._collList;
            this.collCount = src._collCount;
            this.collEnd = src._collEnd;
            this.longestCollisionList = src._longestCollisionList;
        }
    }

    private BytesToNameCanonicalizer(int sz, boolean intern, int seed, boolean failOnDoS) {
        this._parent = null;
        this._seed = seed;
        this._intern = intern;
        this._failOnDoS = failOnDoS;
        if (sz < MIN_HASH_SIZE) {
            sz = MIN_HASH_SIZE;
        } else if (((sz - 1) & sz) != 0) {
            int curr = MIN_HASH_SIZE;
            while (curr < sz) {
                curr += curr;
            }
            sz = curr;
        }
        this._tableInfo = new AtomicReference(initTableInfo(sz));
    }

    private BytesToNameCanonicalizer(BytesToNameCanonicalizer parent, boolean intern, int seed, boolean failOnDoS, TableInfo state) {
        this._parent = parent;
        this._seed = seed;
        this._intern = intern;
        this._failOnDoS = failOnDoS;
        this._tableInfo = null;
        this._count = state.count;
        this._hashMask = state.mainHashMask;
        this._hash = state.mainHash;
        this._mainNames = state.mainNames;
        this._collList = state.collList;
        this._collCount = state.collCount;
        this._collEnd = state.collEnd;
        this._longestCollisionList = state.longestCollisionList;
        this._needRehash = false;
        this._hashShared = true;
        this._namesShared = true;
        this._collListShared = true;
    }

    private TableInfo initTableInfo(int sz) {
        return new TableInfo(0, sz - 1, new int[sz], new Name[sz], null, 0, 0, 0);
    }

    public static BytesToNameCanonicalizer createRoot() {
        long now = System.currentTimeMillis();
        return createRoot((((int) now) + ((int) (now >>> INITIAL_COLLISION_LEN))) | 1);
    }

    protected static BytesToNameCanonicalizer createRoot(int seed) {
        return new BytesToNameCanonicalizer(DEFAULT_T_SIZE, true, seed, true);
    }

    public BytesToNameCanonicalizer makeChild(int flags) {
        return new BytesToNameCanonicalizer(this, Feature.INTERN_FIELD_NAMES.enabledIn(flags), this._seed, Feature.FAIL_ON_SYMBOL_HASH_OVERFLOW.enabledIn(flags), (TableInfo) this._tableInfo.get());
    }

    @Deprecated
    public BytesToNameCanonicalizer makeChild(boolean canonicalize, boolean intern) {
        return new BytesToNameCanonicalizer(this, intern, this._seed, true, (TableInfo) this._tableInfo.get());
    }

    public void release() {
        if (this._parent != null && maybeDirty()) {
            this._parent.mergeChild(new TableInfo(this));
            this._hashShared = true;
            this._namesShared = true;
            this._collListShared = true;
        }
    }

    private void mergeChild(TableInfo childState) {
        int childCount = childState.count;
        TableInfo currState = (TableInfo) this._tableInfo.get();
        if (childCount != currState.count) {
            if (childCount > MAX_ENTRIES_FOR_REUSE) {
                childState = initTableInfo(DEFAULT_T_SIZE);
            }
            this._tableInfo.compareAndSet(currState, childState);
        }
    }

    public int size() {
        if (this._tableInfo != null) {
            return ((TableInfo) this._tableInfo.get()).count;
        }
        return this._count;
    }

    public int bucketCount() {
        return this._hash.length;
    }

    public boolean maybeDirty() {
        return !this._hashShared;
    }

    public int hashSeed() {
        return this._seed;
    }

    public int collisionCount() {
        return this._collCount;
    }

    public int maxCollisionLength() {
        return this._longestCollisionList;
    }

    public static Name getEmptyName() {
        return Name1.getEmptyName();
    }

    public Name findName(int q1) {
        int hash = calcHash(q1);
        int ix = hash & this._hashMask;
        int val = this._hash[ix];
        if ((((val >> 8) ^ hash) << 8) == 0) {
            Name name = this._mainNames[ix];
            if (name == null) {
                return null;
            }
            if (name.equals(q1)) {
                return name;
            }
        } else if (val == 0) {
            return null;
        }
        val &= MotionEventCompat.ACTION_MASK;
        if (val > 0) {
            Bucket bucket = this._collList[val - 1];
            if (bucket != null) {
                return bucket.find(hash, q1, 0);
            }
        }
        return null;
    }

    public Name findName(int q1, int q2) {
        int hash = q2 == 0 ? calcHash(q1) : calcHash(q1, q2);
        int ix = hash & this._hashMask;
        int val = this._hash[ix];
        if ((((val >> 8) ^ hash) << 8) == 0) {
            Name name = this._mainNames[ix];
            if (name == null) {
                return null;
            }
            if (name.equals(q1, q2)) {
                return name;
            }
        } else if (val == 0) {
            return null;
        }
        val &= MotionEventCompat.ACTION_MASK;
        if (val > 0) {
            Bucket bucket = this._collList[val - 1];
            if (bucket != null) {
                return bucket.find(hash, q1, q2);
            }
        }
        return null;
    }

    public Name findName(int q1, int q2, int q3) {
        int hash = calcHash(q1, q2, q3);
        int ix = hash & this._hashMask;
        int val = this._hash[ix];
        if ((((val >> 8) ^ hash) << 8) == 0) {
            Name name = this._mainNames[ix];
            if (name == null) {
                return null;
            }
            if (name.equals(q1, q2, q3)) {
                return name;
            }
        } else if (val == 0) {
            return null;
        }
        val &= MotionEventCompat.ACTION_MASK;
        if (val > 0) {
            Bucket bucket = this._collList[val - 1];
            if (bucket != null) {
                return bucket.find(hash, q1, q2, q3);
            }
        }
        return null;
    }

    public Name findName(int[] q, int qlen) {
        int i = 0;
        if (qlen >= 4) {
            int hash = calcHash(q, qlen);
            int ix = hash & this._hashMask;
            int val = this._hash[ix];
            if ((((val >> 8) ^ hash) << 8) == 0) {
                Name name = this._mainNames[ix];
                if (name == null || name.equals(q, qlen)) {
                    return name;
                }
            } else if (val == 0) {
                return null;
            }
            val &= MotionEventCompat.ACTION_MASK;
            if (val > 0) {
                Bucket bucket = this._collList[val - 1];
                if (bucket != null) {
                    return bucket.find(hash, q, qlen);
                }
            }
            return null;
        } else if (qlen == 3) {
            return findName(q[0], q[1], q[2]);
        } else {
            int i2 = q[0];
            if (qlen >= 2) {
                i = q[1];
            }
            return findName(i2, i);
        }
    }

    public Name addName(String name, int q1, int q2) {
        if (this._intern) {
            name = InternCache.instance.intern(name);
        }
        int hash = q2 == 0 ? calcHash(q1) : calcHash(q1, q2);
        Name symbol = constructName(hash, name, q1, q2);
        _addSymbol(hash, symbol);
        return symbol;
    }

    public Name addName(String name, int[] q, int qlen) {
        int hash;
        if (this._intern) {
            name = InternCache.instance.intern(name);
        }
        if (qlen >= 4) {
            hash = calcHash(q, qlen);
        } else if (qlen == 1) {
            hash = calcHash(q[0]);
        } else if (qlen == 2) {
            hash = calcHash(q[0], q[1]);
        } else {
            hash = calcHash(q[0], q[1], q[2]);
        }
        Name symbol = constructName(hash, name, q, qlen);
        _addSymbol(hash, symbol);
        return symbol;
    }

    public int calcHash(int q1) {
        int hash = q1 ^ this._seed;
        hash += hash >>> 15;
        return hash ^ (hash >>> 9);
    }

    public int calcHash(int q1, int q2) {
        int hash = q1;
        hash = ((hash ^ (hash >>> 15)) + (q2 * MULT)) ^ this._seed;
        hash += hash >>> 7;
        return hash ^ (hash >>> 4);
    }

    public int calcHash(int q1, int q2, int q3) {
        int hash = q1 ^ this._seed;
        hash = (((hash + (hash >>> 9)) * MULT) + q2) * MULT2;
        hash = (hash + (hash >>> 15)) ^ q3;
        hash += hash >>> 17;
        hash += hash >>> 15;
        return hash ^ (hash << 9);
    }

    public int calcHash(int[] q, int qlen) {
        if (qlen < 4) {
            throw new IllegalArgumentException();
        }
        int hash = q[0] ^ this._seed;
        hash = (((hash + (hash >>> 9)) * MULT) + q[1]) * MULT2;
        hash = (hash + (hash >>> 15)) ^ q[2];
        hash += hash >>> 17;
        for (int i = 3; i < qlen; i++) {
            hash = (hash * MULT3) ^ q[i];
            hash += hash >>> 3;
            hash ^= hash << 7;
        }
        hash += hash >>> 15;
        return hash ^ (hash << 9);
    }

    protected static int[] calcQuads(byte[] wordBytes) {
        int blen = wordBytes.length;
        int[] result = new int[((blen + 3) / 4)];
        int i = 0;
        while (i < blen) {
            int x = wordBytes[i] & MotionEventCompat.ACTION_MASK;
            i++;
            if (i < blen) {
                x = (x << 8) | (wordBytes[i] & MotionEventCompat.ACTION_MASK);
                i++;
                if (i < blen) {
                    x = (x << 8) | (wordBytes[i] & MotionEventCompat.ACTION_MASK);
                    i++;
                    if (i < blen) {
                        x = (x << 8) | (wordBytes[i] & MotionEventCompat.ACTION_MASK);
                    }
                }
            }
            result[i >> 2] = x;
            i++;
        }
        return result;
    }

    private void _addSymbol(int hash, Name symbol) {
        if (this._hashShared) {
            unshareMain();
        }
        if (this._needRehash) {
            rehash();
        }
        this._count++;
        int ix = hash & this._hashMask;
        if (this._mainNames[ix] == null) {
            this._hash[ix] = hash << 8;
            if (this._namesShared) {
                unshareNames();
            }
            this._mainNames[ix] = symbol;
        } else {
            if (this._collListShared) {
                unshareCollision();
            }
            this._collCount++;
            int entryValue = this._hash[ix];
            int bucket = entryValue & MotionEventCompat.ACTION_MASK;
            if (bucket == 0) {
                if (this._collEnd <= LAST_VALID_BUCKET) {
                    bucket = this._collEnd;
                    this._collEnd++;
                    if (bucket >= this._collList.length) {
                        expandCollision();
                    }
                } else {
                    bucket = findBestBucket();
                }
                this._hash[ix] = (entryValue & InputDeviceCompat.SOURCE_ANY) | (bucket + 1);
            } else {
                bucket--;
            }
            Bucket newB = new Bucket(symbol, this._collList[bucket]);
            if (newB.length > MAX_COLL_CHAIN_LENGTH) {
                _handleSpillOverflow(bucket, newB);
            } else {
                this._collList[bucket] = newB;
                this._longestCollisionList = Math.max(newB.length, this._longestCollisionList);
            }
        }
        int hashSize = this._hash.length;
        if (this._count > (hashSize >> 1)) {
            int hashQuarter = hashSize >> 2;
            if (this._count > hashSize - hashQuarter) {
                this._needRehash = true;
            } else if (this._collCount >= hashQuarter) {
                this._needRehash = true;
            }
        }
    }

    private void _handleSpillOverflow(int bindex, Bucket newBucket) {
        if (this._overflows == null) {
            this._overflows = new BitSet();
            this._overflows.set(bindex);
        } else if (this._overflows.get(bindex)) {
            if (this._failOnDoS) {
                reportTooManyCollisions(MAX_COLL_CHAIN_LENGTH);
            }
            this._intern = false;
        } else {
            this._overflows.set(bindex);
        }
        this._collList[bindex] = null;
        this._count -= newBucket.length;
        this._longestCollisionList = -1;
    }

    private void rehash() {
        this._needRehash = false;
        this._namesShared = false;
        int len = this._hash.length;
        int newLen = len + len;
        if (newLen > MAX_T_SIZE) {
            nukeSymbols();
            return;
        }
        int i;
        this._hash = new int[newLen];
        this._hashMask = newLen - 1;
        Name[] oldNames = this._mainNames;
        this._mainNames = new Name[newLen];
        int symbolsSeen = 0;
        for (i = 0; i < len; i++) {
            Name symbol = oldNames[i];
            if (symbol != null) {
                symbolsSeen++;
                int hash = symbol.hashCode();
                int ix = hash & this._hashMask;
                this._mainNames[ix] = symbol;
                this._hash[ix] = hash << 8;
            }
        }
        int oldEnd = this._collEnd;
        if (oldEnd == 0) {
            this._longestCollisionList = 0;
            return;
        }
        this._collCount = 0;
        this._collEnd = 0;
        this._collListShared = false;
        int maxColl = 0;
        Bucket[] oldBuckets = this._collList;
        this._collList = new Bucket[oldBuckets.length];
        for (i = 0; i < oldEnd; i++) {
            for (Bucket curr = oldBuckets[i]; curr != null; curr = curr.next) {
                symbolsSeen++;
                symbol = curr.name;
                hash = symbol.hashCode();
                ix = hash & this._hashMask;
                int val = this._hash[ix];
                if (this._mainNames[ix] == null) {
                    this._hash[ix] = hash << 8;
                    this._mainNames[ix] = symbol;
                } else {
                    this._collCount++;
                    int bucket = val & MotionEventCompat.ACTION_MASK;
                    if (bucket == 0) {
                        int i2 = this._collEnd;
                        if (r0 <= LAST_VALID_BUCKET) {
                            bucket = this._collEnd;
                            this._collEnd++;
                            i2 = this._collList.length;
                            if (bucket >= r0) {
                                expandCollision();
                            }
                        } else {
                            bucket = findBestBucket();
                        }
                        this._hash[ix] = (val & InputDeviceCompat.SOURCE_ANY) | (bucket + 1);
                    } else {
                        bucket--;
                    }
                    Bucket newB = new Bucket(symbol, this._collList[bucket]);
                    this._collList[bucket] = newB;
                    maxColl = Math.max(maxColl, newB.length);
                }
            }
        }
        this._longestCollisionList = maxColl;
        if (symbolsSeen != this._count) {
            StringBuilder append = new StringBuilder().append("Internal error: count after rehash ");
            throw new RuntimeException(r19.append(symbolsSeen).append("; should be ").append(this._count).toString());
        }
    }

    private void nukeSymbols() {
        this._count = 0;
        this._longestCollisionList = 0;
        Arrays.fill(this._hash, 0);
        Arrays.fill(this._mainNames, null);
        Arrays.fill(this._collList, null);
        this._collCount = 0;
        this._collEnd = 0;
    }

    private int findBestBucket() {
        Bucket[] buckets = this._collList;
        int bestCount = Integer.MAX_VALUE;
        int bestIx = -1;
        int len = this._collEnd;
        for (int i = 0; i < len; i++) {
            Bucket b = buckets[i];
            if (b == null) {
                return i;
            }
            int count = b.length;
            if (count < bestCount) {
                if (count == 1) {
                    return i;
                }
                bestCount = count;
                bestIx = i;
            }
        }
        return bestIx;
    }

    private void unshareMain() {
        int[] old = this._hash;
        this._hash = Arrays.copyOf(old, old.length);
        this._hashShared = false;
    }

    private void unshareCollision() {
        Bucket[] old = this._collList;
        if (old == null) {
            this._collList = new Bucket[INITIAL_COLLISION_LEN];
        } else {
            this._collList = (Bucket[]) Arrays.copyOf(old, old.length);
        }
        this._collListShared = false;
    }

    private void unshareNames() {
        Name[] old = this._mainNames;
        this._mainNames = (Name[]) Arrays.copyOf(old, old.length);
        this._namesShared = false;
    }

    private void expandCollision() {
        Bucket[] old = this._collList;
        this._collList = (Bucket[]) Arrays.copyOf(old, old.length * 2);
    }

    private static Name constructName(int hash, String name, int q1, int q2) {
        if (q2 == 0) {
            return new Name1(name, hash, q1);
        }
        return new Name2(name, hash, q1, q2);
    }

    private static Name constructName(int hash, String name, int[] quads, int qlen) {
        if (qlen >= 4) {
            return NameN.construct(name, hash, quads, qlen);
        }
        switch (qlen) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                return new Name1(name, hash, quads[0]);
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                return new Name2(name, hash, quads[0], quads[1]);
            default:
                return new Name3(name, hash, quads[0], quads[1], quads[2]);
        }
    }

    protected void reportTooManyCollisions(int maxLen) {
        throw new IllegalStateException("Longest collision chain in symbol table (of size " + this._count + ") now exceeds maximum, " + maxLen + " -- suspect a DoS attack based on hash collisions");
    }
}
