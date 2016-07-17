package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import java.io.IOException;
import java.util.BitSet;

public class PropertyValueBuffer {
    protected PropertyValue _buffered;
    protected final DeserializationContext _context;
    protected final Object[] _creatorParameters;
    protected Object _idValue;
    protected final ObjectIdReader _objectIdReader;
    protected int _paramsNeeded;
    protected int _paramsSeen;
    protected final BitSet _paramsSeenBig;
    protected final JsonParser _parser;

    public PropertyValueBuffer(JsonParser jp, DeserializationContext ctxt, int paramCount, ObjectIdReader oir) {
        this._parser = jp;
        this._context = ctxt;
        this._paramsNeeded = paramCount;
        this._objectIdReader = oir;
        this._creatorParameters = new Object[paramCount];
        if (paramCount < 32) {
            this._paramsSeenBig = null;
        } else {
            this._paramsSeenBig = new BitSet();
        }
    }

    protected Object[] getParameters(SettableBeanProperty[] props) throws JsonMappingException {
        if (this._paramsNeeded > 0) {
            int len;
            int ix;
            if (this._paramsSeenBig != null) {
                len = this._creatorParameters.length;
                ix = 0;
                while (true) {
                    ix = this._paramsSeenBig.nextClearBit(ix);
                    if (ix >= len) {
                        break;
                    }
                    this._creatorParameters[ix] = _findMissing(props[ix]);
                    ix++;
                }
            } else {
                int mask = this._paramsSeen;
                ix = 0;
                len = this._creatorParameters.length;
                while (ix < len) {
                    if ((mask & 1) == 0) {
                        this._creatorParameters[ix] = _findMissing(props[ix]);
                    }
                    ix++;
                    mask >>= 1;
                }
            }
        }
        return this._creatorParameters;
    }

    protected Object _findMissing(SettableBeanProperty prop) throws JsonMappingException {
        if (prop.getInjectableValueId() != null) {
            return this._context.findInjectableValue(prop.getInjectableValueId(), prop, null);
        }
        if (prop.isRequired()) {
            throw this._context.mappingException("Missing required creator property '%s' (index %d)", prop.getName(), Integer.valueOf(prop.getCreatorIndex()));
        } else if (!this._context.isEnabled(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)) {
            return prop.getValueDeserializer().getNullValue(this._context);
        } else {
            throw this._context.mappingException("Missing creator property '%s' (index %d); DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES enabled", prop.getName(), Integer.valueOf(prop.getCreatorIndex()));
        }
    }

    public boolean readIdProperty(String propName) throws IOException {
        if (this._objectIdReader == null || !propName.equals(this._objectIdReader.propertyName.getSimpleName())) {
            return false;
        }
        this._idValue = this._objectIdReader.readObjectReference(this._parser, this._context);
        return true;
    }

    public Object handleIdValue(DeserializationContext ctxt, Object bean) throws IOException {
        if (this._objectIdReader == null) {
            return bean;
        }
        if (this._idValue != null) {
            ctxt.findObjectId(this._idValue, this._objectIdReader.generator, this._objectIdReader.resolver).bindItem(bean);
            SettableBeanProperty idProp = this._objectIdReader.idProperty;
            if (idProp != null) {
                return idProp.setAndReturn(bean, this._idValue);
            }
            return bean;
        }
        throw ctxt.mappingException("No _idValue when handleIdValue called, on instance of %s", bean.getClass().getName());
    }

    protected PropertyValue buffered() {
        return this._buffered;
    }

    public boolean isComplete() {
        return this._paramsNeeded <= 0;
    }

    public boolean assignParameter(SettableBeanProperty prop, Object value) {
        int ix = prop.getCreatorIndex();
        this._creatorParameters[ix] = value;
        int i;
        if (this._paramsSeenBig == null) {
            int old = this._paramsSeen;
            int newValue = old | (1 << ix);
            if (old != newValue) {
                this._paramsSeen = newValue;
                i = this._paramsNeeded - 1;
                this._paramsNeeded = i;
                if (i <= 0) {
                    return true;
                }
            }
        } else if (!this._paramsSeenBig.get(ix)) {
            i = this._paramsNeeded - 1;
            this._paramsNeeded = i;
            if (i <= 0) {
                return true;
            }
            this._paramsSeenBig.set(ix);
        }
        return false;
    }

    @Deprecated
    public boolean assignParameter(int index, Object value) {
        this._creatorParameters[index] = value;
        return false;
    }

    public void bufferProperty(SettableBeanProperty prop, Object value) {
        this._buffered = new Regular(this._buffered, value, prop);
    }

    public void bufferAnyProperty(SettableAnyProperty prop, String propName, Object value) {
        this._buffered = new Any(this._buffered, value, prop, propName);
    }

    public void bufferMapProperty(Object key, Object value) {
        this._buffered = new Map(this._buffered, value, key);
    }
}
