package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.util.NameTransformer;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BeanPropertyMap implements Iterable<SettableBeanProperty>, Serializable {
    private static final long serialVersionUID = 2;
    protected final boolean _caseInsensitive;
    private Object[] _hashArea;
    private int _hashMask;
    private SettableBeanProperty[] _propsInOrder;
    private int _size;
    private int _spillCount;

    public BeanPropertyMap(boolean caseInsensitive, Collection<SettableBeanProperty> props) {
        this._caseInsensitive = caseInsensitive;
        this._propsInOrder = (SettableBeanProperty[]) props.toArray(new SettableBeanProperty[props.size()]);
        init(props);
    }

    protected void init(Collection<SettableBeanProperty> props) {
        this._size = props.size();
        int hashSize = findSize(this._size);
        this._hashMask = hashSize - 1;
        Object[] hashed = new Object[(((hashSize >> 1) + hashSize) * 2)];
        int spillCount = 0;
        for (SettableBeanProperty prop : props) {
            if (prop != null) {
                String key = getPropertyName(prop);
                int slot = _hashCode(key);
                int ix = slot << 1;
                if (hashed[ix] != null) {
                    ix = ((slot >> 1) + hashSize) << 1;
                    if (hashed[ix] != null) {
                        ix = (((hashSize >> 1) + hashSize) << 1) + spillCount;
                        spillCount += 2;
                        if (ix >= hashed.length) {
                            hashed = Arrays.copyOf(hashed, hashed.length + 4);
                        }
                    }
                }
                hashed[ix] = key;
                hashed[ix + 1] = prop;
            }
        }
        this._hashArea = hashed;
        this._spillCount = spillCount;
    }

    private static final int findSize(int size) {
        if (size <= 5) {
            return 8;
        }
        if (size <= 12) {
            return 16;
        }
        int result = 32;
        while (result < size + (size >> 2)) {
            result += result;
        }
        return result;
    }

    public static BeanPropertyMap construct(Collection<SettableBeanProperty> props, boolean caseInsensitive) {
        return new BeanPropertyMap(caseInsensitive, props);
    }

    public BeanPropertyMap withProperty(SettableBeanProperty newProp) {
        String key = getPropertyName(newProp);
        int end = this._hashArea.length;
        for (int i = 1; i < end; i += 2) {
            SettableBeanProperty prop = this._hashArea[i];
            if (prop != null && prop.getName().equals(key)) {
                this._hashArea[i] = newProp;
                this._propsInOrder[_findFromOrdered(prop)] = newProp;
                break;
            }
        }
        int slot = _hashCode(key);
        int hashSize = this._hashMask + 1;
        int ix = slot << 1;
        if (this._hashArea[ix] != null) {
            ix = ((slot >> 1) + hashSize) << 1;
            if (this._hashArea[ix] != null) {
                ix = (((hashSize >> 1) + hashSize) << 1) + this._spillCount;
                this._spillCount += 2;
                if (ix >= this._hashArea.length) {
                    this._hashArea = Arrays.copyOf(this._hashArea, this._hashArea.length + 4);
                }
            }
        }
        this._hashArea[ix] = key;
        this._hashArea[ix + 1] = newProp;
        int last = this._propsInOrder.length;
        this._propsInOrder = (SettableBeanProperty[]) Arrays.copyOf(this._propsInOrder, last + 1);
        this._propsInOrder[last] = newProp;
        return this;
    }

    public BeanPropertyMap assignIndexes() {
        int i = 1;
        int end = this._hashArea.length;
        int index = 0;
        while (i < end) {
            int index2;
            SettableBeanProperty prop = this._hashArea[i];
            if (prop != null) {
                index2 = index + 1;
                prop.assignIndex(index);
            } else {
                index2 = index;
            }
            i += 2;
            index = index2;
        }
        return this;
    }

    public BeanPropertyMap renameAll(NameTransformer transformer) {
        if (transformer == null || transformer == NameTransformer.NOP) {
            return this;
        }
        ArrayList<SettableBeanProperty> newProps = new ArrayList(len);
        for (SettableBeanProperty prop : this._propsInOrder) {
            if (prop == null) {
                newProps.add(prop);
            } else {
                newProps.add(_rename(prop, transformer));
            }
        }
        return new BeanPropertyMap(this._caseInsensitive, newProps);
    }

    public void replace(SettableBeanProperty newProp) {
        String key = getPropertyName(newProp);
        int i = 1;
        int end = this._hashArea.length;
        while (i < end) {
            SettableBeanProperty prop = this._hashArea[i];
            if (prop == null || !prop.getName().equals(key)) {
                i += 2;
            } else {
                this._hashArea[i] = newProp;
                this._propsInOrder[_findFromOrdered(prop)] = newProp;
                return;
            }
        }
        throw new NoSuchElementException("No entry '" + newProp.getName() + "' found, can't replace");
    }

    private List<SettableBeanProperty> properties() {
        ArrayList<SettableBeanProperty> p = new ArrayList(this._size);
        int end = this._hashArea.length;
        for (int i = 1; i < end; i += 2) {
            SettableBeanProperty prop = this._hashArea[i];
            if (prop != null) {
                p.add(prop);
            }
        }
        return p;
    }

    public Iterator<SettableBeanProperty> iterator() {
        return properties().iterator();
    }

    public SettableBeanProperty[] getPropertiesInInsertionOrder() {
        return this._propsInOrder;
    }

    protected final String getPropertyName(SettableBeanProperty prop) {
        return this._caseInsensitive ? prop.getName().toLowerCase() : prop.getName();
    }

    public SettableBeanProperty find(int index) {
        int end = this._hashArea.length;
        for (int i = 1; i < end; i += 2) {
            SettableBeanProperty prop = this._hashArea[i];
            if (prop != null && index == prop.getPropertyIndex()) {
                return prop;
            }
        }
        return null;
    }

    public SettableBeanProperty find(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Can not pass null property name");
        }
        if (this._caseInsensitive) {
            key = key.toLowerCase();
        }
        int slot = key.hashCode() & this._hashMask;
        int ix = slot << 1;
        String match = this._hashArea[ix];
        if (match == key || key.equals(match)) {
            return (SettableBeanProperty) this._hashArea[ix + 1];
        }
        return _find2(key, slot, match);
    }

    private final SettableBeanProperty _find2(String key, int slot, Object match) {
        if (match == null) {
            return null;
        }
        int hashSize = this._hashMask + 1;
        int ix = ((slot >> 1) + hashSize) << 1;
        match = this._hashArea[ix];
        if (key.equals(match)) {
            return (SettableBeanProperty) this._hashArea[ix + 1];
        }
        if (match == null) {
            return null;
        }
        int i = ((hashSize >> 1) + hashSize) << 1;
        int end = i + this._spillCount;
        while (i < end) {
            String match2 = this._hashArea[i];
            if (match2 == key || key.equals(match2)) {
                return (SettableBeanProperty) this._hashArea[i + 1];
            }
            i += 2;
        }
        return null;
    }

    public int size() {
        return this._size;
    }

    public void remove(SettableBeanProperty propToRm) {
        ArrayList<SettableBeanProperty> props = new ArrayList(this._size);
        String key = getPropertyName(propToRm);
        boolean found = false;
        int end = this._hashArea.length;
        for (int i = 1; i < end; i += 2) {
            SettableBeanProperty prop = this._hashArea[i];
            if (prop != null) {
                if (!found) {
                    found = key.equals(prop.getName());
                    if (found) {
                        this._propsInOrder[_findFromOrdered(prop)] = null;
                    }
                }
                props.add(prop);
            }
        }
        if (found) {
            init(props);
            return;
        }
        throw new NoSuchElementException("No entry '" + propToRm.getName() + "' found, can't remove");
    }

    public boolean findDeserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean, String key) throws IOException {
        SettableBeanProperty prop = find(key);
        if (prop == null) {
            return false;
        }
        try {
            prop.deserializeAndSet(p, ctxt, bean);
        } catch (Exception e) {
            wrapAndThrow(e, bean, key, ctxt);
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Properties=[");
        int count = 0;
        Iterator<SettableBeanProperty> it = iterator();
        while (it.hasNext()) {
            SettableBeanProperty prop = (SettableBeanProperty) it.next();
            int count2 = count + 1;
            if (count > 0) {
                sb.append(", ");
            }
            sb.append(prop.getName());
            sb.append('(');
            sb.append(prop.getType());
            sb.append(')');
            count = count2;
        }
        sb.append(']');
        return sb.toString();
    }

    protected SettableBeanProperty _rename(SettableBeanProperty prop, NameTransformer xf) {
        if (prop == null) {
            return prop;
        }
        prop = prop.withSimpleName(xf.transform(prop.getName()));
        JsonDeserializer<?> deser = prop.getValueDeserializer();
        if (deser != null) {
            JsonDeserializer<Object> newDeser = deser.unwrappingDeserializer(xf);
            if (newDeser != deser) {
                prop = prop.withValueDeserializer(newDeser);
            }
        }
        return prop;
    }

    protected void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt) throws IOException {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = ctxt == null || ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            if (!(wrap && (t instanceof JsonProcessingException))) {
                throw ((IOException) t);
            }
        } else if (!wrap && (t instanceof RuntimeException)) {
            throw ((RuntimeException) t);
        }
        throw JsonMappingException.wrapWithPath(t, bean, fieldName);
    }

    private int _findFromOrdered(SettableBeanProperty prop) {
        int end = this._propsInOrder.length;
        for (int i = 0; i < end; i++) {
            if (this._propsInOrder[i] == prop) {
                return i;
            }
        }
        throw new IllegalStateException("Illegal state: property '" + prop.getName() + "' missing from _propsInOrder");
    }

    private final int _hashCode(String key) {
        return key.hashCode() & this._hashMask;
    }
}
