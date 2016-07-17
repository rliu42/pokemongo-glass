package com.google.android.gms.internal;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

abstract class zzmh<K, V> {
    zzb zzagI;
    zzc zzagJ;
    zze zzagK;

    final class zza<T> implements Iterator<T> {
        boolean mCanRemove;
        int mIndex;
        final int mOffset;
        int mSize;
        final /* synthetic */ zzmh zzagL;

        zza(zzmh com_google_android_gms_internal_zzmh, int i) {
            this.zzagL = com_google_android_gms_internal_zzmh;
            this.mCanRemove = false;
            this.mOffset = i;
            this.mSize = com_google_android_gms_internal_zzmh.colGetSize();
        }

        public boolean hasNext() {
            return this.mIndex < this.mSize;
        }

        public T next() {
            T colGetEntry = this.zzagL.colGetEntry(this.mIndex, this.mOffset);
            this.mIndex++;
            this.mCanRemove = true;
            return colGetEntry;
        }

        public void remove() {
            if (this.mCanRemove) {
                this.mIndex--;
                this.mSize--;
                this.mCanRemove = false;
                this.zzagL.colRemoveAt(this.mIndex);
                return;
            }
            throw new IllegalStateException();
        }
    }

    final class zzb implements Set<Entry<K, V>> {
        final /* synthetic */ zzmh zzagL;

        zzb(zzmh com_google_android_gms_internal_zzmh) {
            this.zzagL = com_google_android_gms_internal_zzmh;
        }

        public boolean add(Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends Entry<K, V>> collection) {
            int colGetSize = this.zzagL.colGetSize();
            for (Entry entry : collection) {
                this.zzagL.colPut(entry.getKey(), entry.getValue());
            }
            return colGetSize != this.zzagL.colGetSize();
        }

        public void clear() {
            this.zzagL.colClear();
        }

        public boolean contains(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) o;
            int colIndexOfKey = this.zzagL.colIndexOfKey(entry.getKey());
            return colIndexOfKey >= 0 ? zzmf.equal(this.zzagL.colGetEntry(colIndexOfKey, 1), entry.getValue()) : false;
        }

        public boolean containsAll(Collection<?> collection) {
            for (Object contains : collection) {
                if (!contains(contains)) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(Object object) {
            return zzmh.equalsSetHelper(this, object);
        }

        public int hashCode() {
            int colGetSize = this.zzagL.colGetSize() - 1;
            int i = 0;
            while (colGetSize >= 0) {
                Object colGetEntry = this.zzagL.colGetEntry(colGetSize, 0);
                Object colGetEntry2 = this.zzagL.colGetEntry(colGetSize, 1);
                colGetSize--;
                i += (colGetEntry2 == null ? 0 : colGetEntry2.hashCode()) ^ (colGetEntry == null ? 0 : colGetEntry.hashCode());
            }
            return i;
        }

        public boolean isEmpty() {
            return this.zzagL.colGetSize() == 0;
        }

        public Iterator<Entry<K, V>> iterator() {
            return new zzd(this.zzagL);
        }

        public boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return this.zzagL.colGetSize();
        }

        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        public <T> T[] toArray(T[] tArr) {
            throw new UnsupportedOperationException();
        }
    }

    final class zzc implements Set<K> {
        final /* synthetic */ zzmh zzagL;

        zzc(zzmh com_google_android_gms_internal_zzmh) {
            this.zzagL = com_google_android_gms_internal_zzmh;
        }

        public boolean add(K k) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            this.zzagL.colClear();
        }

        public boolean contains(Object object) {
            return this.zzagL.colIndexOfKey(object) >= 0;
        }

        public boolean containsAll(Collection<?> collection) {
            return zzmh.containsAllHelper(this.zzagL.colGetMap(), collection);
        }

        public boolean equals(Object object) {
            return zzmh.equalsSetHelper(this, object);
        }

        public int hashCode() {
            int i = 0;
            for (int colGetSize = this.zzagL.colGetSize() - 1; colGetSize >= 0; colGetSize--) {
                Object colGetEntry = this.zzagL.colGetEntry(colGetSize, 0);
                i += colGetEntry == null ? 0 : colGetEntry.hashCode();
            }
            return i;
        }

        public boolean isEmpty() {
            return this.zzagL.colGetSize() == 0;
        }

        public Iterator<K> iterator() {
            return new zza(this.zzagL, 0);
        }

        public boolean remove(Object object) {
            int colIndexOfKey = this.zzagL.colIndexOfKey(object);
            if (colIndexOfKey < 0) {
                return false;
            }
            this.zzagL.colRemoveAt(colIndexOfKey);
            return true;
        }

        public boolean removeAll(Collection<?> collection) {
            return zzmh.removeAllHelper(this.zzagL.colGetMap(), collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return zzmh.retainAllHelper(this.zzagL.colGetMap(), collection);
        }

        public int size() {
            return this.zzagL.colGetSize();
        }

        public Object[] toArray() {
            return this.zzagL.toArrayHelper(0);
        }

        public <T> T[] toArray(T[] array) {
            return this.zzagL.toArrayHelper(array, 0);
        }
    }

    final class zzd implements Iterator<Entry<K, V>>, Entry<K, V> {
        int mEnd;
        boolean mEntryValid;
        int mIndex;
        final /* synthetic */ zzmh zzagL;

        zzd(zzmh com_google_android_gms_internal_zzmh) {
            this.zzagL = com_google_android_gms_internal_zzmh;
            this.mEntryValid = false;
            this.mEnd = com_google_android_gms_internal_zzmh.colGetSize() - 1;
            this.mIndex = -1;
        }

        public final boolean equals(Object o) {
            boolean z = true;
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            } else if (!(o instanceof Entry)) {
                return false;
            } else {
                Entry entry = (Entry) o;
                if (!(zzmf.equal(entry.getKey(), this.zzagL.colGetEntry(this.mIndex, 0)) && zzmf.equal(entry.getValue(), this.zzagL.colGetEntry(this.mIndex, 1)))) {
                    z = false;
                }
                return z;
            }
        }

        public K getKey() {
            if (this.mEntryValid) {
                return this.zzagL.colGetEntry(this.mIndex, 0);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public V getValue() {
            if (this.mEntryValid) {
                return this.zzagL.colGetEntry(this.mIndex, 1);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public boolean hasNext() {
            return this.mIndex < this.mEnd;
        }

        public final int hashCode() {
            int i = 0;
            if (this.mEntryValid) {
                Object colGetEntry = this.zzagL.colGetEntry(this.mIndex, 0);
                Object colGetEntry2 = this.zzagL.colGetEntry(this.mIndex, 1);
                int hashCode = colGetEntry == null ? 0 : colGetEntry.hashCode();
                if (colGetEntry2 != null) {
                    i = colGetEntry2.hashCode();
                }
                return i ^ hashCode;
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public Entry<K, V> next() {
            this.mIndex++;
            this.mEntryValid = true;
            return this;
        }

        public void remove() {
            if (this.mEntryValid) {
                this.zzagL.colRemoveAt(this.mIndex);
                this.mIndex--;
                this.mEnd--;
                this.mEntryValid = false;
                return;
            }
            throw new IllegalStateException();
        }

        public V setValue(V object) {
            if (this.mEntryValid) {
                return this.zzagL.colSetValue(this.mIndex, object);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }
    }

    final class zze implements Collection<V> {
        final /* synthetic */ zzmh zzagL;

        zze(zzmh com_google_android_gms_internal_zzmh) {
            this.zzagL = com_google_android_gms_internal_zzmh;
        }

        public boolean add(V v) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            this.zzagL.colClear();
        }

        public boolean contains(Object object) {
            return this.zzagL.colIndexOfValue(object) >= 0;
        }

        public boolean containsAll(Collection<?> collection) {
            for (Object contains : collection) {
                if (!contains(contains)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty() {
            return this.zzagL.colGetSize() == 0;
        }

        public Iterator<V> iterator() {
            return new zza(this.zzagL, 1);
        }

        public boolean remove(Object object) {
            int colIndexOfValue = this.zzagL.colIndexOfValue(object);
            if (colIndexOfValue < 0) {
                return false;
            }
            this.zzagL.colRemoveAt(colIndexOfValue);
            return true;
        }

        public boolean removeAll(Collection<?> collection) {
            int i = 0;
            int colGetSize = this.zzagL.colGetSize();
            boolean z = false;
            while (i < colGetSize) {
                if (collection.contains(this.zzagL.colGetEntry(i, 1))) {
                    this.zzagL.colRemoveAt(i);
                    i--;
                    colGetSize--;
                    z = true;
                }
                i++;
            }
            return z;
        }

        public boolean retainAll(Collection<?> collection) {
            int i = 0;
            int colGetSize = this.zzagL.colGetSize();
            boolean z = false;
            while (i < colGetSize) {
                if (!collection.contains(this.zzagL.colGetEntry(i, 1))) {
                    this.zzagL.colRemoveAt(i);
                    i--;
                    colGetSize--;
                    z = true;
                }
                i++;
            }
            return z;
        }

        public int size() {
            return this.zzagL.colGetSize();
        }

        public Object[] toArray() {
            return this.zzagL.toArrayHelper(1);
        }

        public <T> T[] toArray(T[] array) {
            return this.zzagL.toArrayHelper(array, 1);
        }
    }

    zzmh() {
    }

    public static <K, V> boolean containsAllHelper(Map<K, V> map, Collection<?> collection) {
        for (Object containsKey : collection) {
            if (!map.containsKey(containsKey)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean equalsSetHelper(Set<T> set, Object object) {
        boolean z = true;
        if (set == object) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        Set set2 = (Set) object;
        try {
            if (!(set.size() == set2.size() && set.containsAll(set2))) {
                z = false;
            }
            return z;
        } catch (NullPointerException e) {
            return false;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public static <K, V> boolean removeAllHelper(Map<K, V> map, Collection<?> collection) {
        int size = map.size();
        for (Object remove : collection) {
            map.remove(remove);
        }
        return size != map.size();
    }

    public static <K, V> boolean retainAllHelper(Map<K, V> map, Collection<?> collection) {
        int size = map.size();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
            }
        }
        return size != map.size();
    }

    protected abstract void colClear();

    protected abstract Object colGetEntry(int i, int i2);

    protected abstract Map<K, V> colGetMap();

    protected abstract int colGetSize();

    protected abstract int colIndexOfKey(Object obj);

    protected abstract int colIndexOfValue(Object obj);

    protected abstract void colPut(K k, V v);

    protected abstract void colRemoveAt(int i);

    protected abstract V colSetValue(int i, V v);

    public Set<Entry<K, V>> getEntrySet() {
        if (this.zzagI == null) {
            this.zzagI = new zzb(this);
        }
        return this.zzagI;
    }

    public Set<K> getKeySet() {
        if (this.zzagJ == null) {
            this.zzagJ = new zzc(this);
        }
        return this.zzagJ;
    }

    public Collection<V> getValues() {
        if (this.zzagK == null) {
            this.zzagK = new zze(this);
        }
        return this.zzagK;
    }

    public Object[] toArrayHelper(int offset) {
        int colGetSize = colGetSize();
        Object[] objArr = new Object[colGetSize];
        for (int i = 0; i < colGetSize; i++) {
            objArr[i] = colGetEntry(i, offset);
        }
        return objArr;
    }

    public <T> T[] toArrayHelper(T[] array, int offset) {
        int colGetSize = colGetSize();
        if (array.length < colGetSize) {
            array = (Object[]) ((Object[]) Array.newInstance(array.getClass().getComponentType(), colGetSize));
        }
        for (int i = 0; i < colGetSize; i++) {
            array[i] = colGetEntry(i, offset);
        }
        if (array.length > colGetSize) {
            array[colGetSize] = null;
        }
        return array;
    }
}
