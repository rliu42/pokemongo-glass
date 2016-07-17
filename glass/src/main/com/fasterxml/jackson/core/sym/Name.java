package com.fasterxml.jackson.core.sym;

public abstract class Name {
    protected final int _hashCode;
    protected final String _name;

    public abstract boolean equals(int i);

    public abstract boolean equals(int i, int i2);

    public abstract boolean equals(int i, int i2, int i3);

    public abstract boolean equals(int[] iArr, int i);

    protected Name(String name, int hashCode) {
        this._name = name;
        this._hashCode = hashCode;
    }

    public String getName() {
        return this._name;
    }

    public String toString() {
        return this._name;
    }

    public final int hashCode() {
        return this._hashCode;
    }

    public boolean equals(Object o) {
        return o == this;
    }
}
