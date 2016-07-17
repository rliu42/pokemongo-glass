package com.fasterxml.jackson.core;

import spacemadness.com.lunarconsole.C1391R;

public abstract class JsonStreamContext {
    protected static final int TYPE_ARRAY = 1;
    protected static final int TYPE_OBJECT = 2;
    protected static final int TYPE_ROOT = 0;
    protected int _index;
    protected int _type;

    public abstract String getCurrentName();

    public abstract JsonStreamContext getParent();

    protected JsonStreamContext() {
    }

    public final boolean inArray() {
        return this._type == TYPE_ARRAY;
    }

    public final boolean inRoot() {
        return this._type == 0;
    }

    public final boolean inObject() {
        return this._type == TYPE_OBJECT;
    }

    public final String getTypeDesc() {
        switch (this._type) {
            case C1391R.styleable.AdsAttrs_adSize /*0*/:
                return "ROOT";
            case TYPE_ARRAY /*1*/:
                return "ARRAY";
            case TYPE_OBJECT /*2*/:
                return "OBJECT";
            default:
                return "?";
        }
    }

    public final int getEntryCount() {
        return this._index + TYPE_ARRAY;
    }

    public final int getCurrentIndex() {
        return this._index < 0 ? 0 : this._index;
    }

    public Object getCurrentValue() {
        return null;
    }

    public void setCurrentValue(Object v) {
    }
}
