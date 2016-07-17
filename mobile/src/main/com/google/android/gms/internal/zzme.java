package com.google.android.gms.internal;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class zzme<K, V> extends zzmi<K, V> implements Map<K, V> {
    zzmh<K, V> zzagz;

    /* renamed from: com.google.android.gms.internal.zzme.1 */
    class C06191 extends zzmh<K, V> {
        final /* synthetic */ zzme zzagA;

        C06191(zzme com_google_android_gms_internal_zzme) {
            this.zzagA = com_google_android_gms_internal_zzme;
        }

        protected void colClear() {
            this.zzagA.clear();
        }

        protected Object colGetEntry(int index, int offset) {
            return this.zzagA.mArray[(index << 1) + offset];
        }

        protected Map<K, V> colGetMap() {
            return this.zzagA;
        }

        protected int colGetSize() {
            return this.zzagA.mSize;
        }

        protected int colIndexOfKey(Object key) {
            return key == null ? this.zzagA.indexOfNull() : this.zzagA.indexOf(key, key.hashCode());
        }

        protected int colIndexOfValue(Object value) {
            return this.zzagA.indexOfValue(value);
        }

        protected void colPut(K key, V value) {
            this.zzagA.put(key, value);
        }

        protected void colRemoveAt(int index) {
            this.zzagA.removeAt(index);
        }

        protected V colSetValue(int index, V value) {
            return this.zzagA.setValueAt(index, value);
        }
    }

    private zzmh<K, V> zzpx() {
        if (this.zzagz == null) {
            this.zzagz = new C06191(this);
        }
        return this.zzagz;
    }

    public Set<Entry<K, V>> entrySet() {
        return zzpx().getEntrySet();
    }

    public Set<K> keySet() {
        return zzpx().getKeySet();
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        ensureCapacity(this.mSize + map.size());
        for (Entry entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public Collection<V> values() {
        return zzpx().getValues();
    }
}
