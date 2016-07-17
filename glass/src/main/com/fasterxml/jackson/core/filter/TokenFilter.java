package com.fasterxml.jackson.core.filter;

import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TokenFilter {
    public static final TokenFilter INCLUDE_ALL;

    static {
        INCLUDE_ALL = new TokenFilter();
    }

    protected TokenFilter() {
    }

    public TokenFilter filterStartObject() {
        return this;
    }

    public TokenFilter filterStartArray() {
        return this;
    }

    public void filterFinishObject() {
    }

    public void filterFinishArray() {
    }

    public TokenFilter includeProperty(String name) {
        return this;
    }

    public TokenFilter includeElement(int index) {
        return this;
    }

    public TokenFilter includeRootValue(int index) {
        return this;
    }

    public boolean includeValue(JsonParser p) throws IOException {
        return _includeScalar();
    }

    public boolean includeBoolean(boolean value) {
        return _includeScalar();
    }

    public boolean includeNull() {
        return _includeScalar();
    }

    public boolean includeString(String value) {
        return _includeScalar();
    }

    public boolean includeNumber(int v) {
        return _includeScalar();
    }

    public boolean includeNumber(long v) {
        return _includeScalar();
    }

    public boolean includeNumber(float v) {
        return _includeScalar();
    }

    public boolean includeNumber(double v) {
        return _includeScalar();
    }

    public boolean includeNumber(BigDecimal v) {
        return _includeScalar();
    }

    public boolean includeNumber(BigInteger v) {
        return _includeScalar();
    }

    public boolean includeBinary() {
        return _includeScalar();
    }

    public boolean includeRawValue() {
        return _includeScalar();
    }

    public boolean includeEmbeddedValue(Object ob) {
        return _includeScalar();
    }

    public String toString() {
        if (this == INCLUDE_ALL) {
            return "TokenFilter.INCLUDE_ALL";
        }
        return super.toString();
    }

    protected boolean _includeScalar() {
        return true;
    }
}
