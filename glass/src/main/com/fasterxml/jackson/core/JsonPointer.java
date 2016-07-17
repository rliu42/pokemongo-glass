package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.NumberInput;
import org.apache.commons.io.IOUtils;
import spacemadness.com.lunarconsole.BuildConfig;

public class JsonPointer {
    protected static final JsonPointer EMPTY;
    protected final String _asString;
    protected volatile JsonPointer _head;
    protected final int _matchingElementIndex;
    protected final String _matchingPropertyName;
    protected final JsonPointer _nextSegment;

    static {
        EMPTY = new JsonPointer();
    }

    protected JsonPointer() {
        this._nextSegment = null;
        this._matchingPropertyName = BuildConfig.FLAVOR;
        this._matchingElementIndex = -1;
        this._asString = BuildConfig.FLAVOR;
    }

    protected JsonPointer(String fullString, String segment, JsonPointer next) {
        this._asString = fullString;
        this._nextSegment = next;
        this._matchingPropertyName = segment;
        this._matchingElementIndex = _parseIndex(segment);
    }

    protected JsonPointer(String fullString, String segment, int matchIndex, JsonPointer next) {
        this._asString = fullString;
        this._nextSegment = next;
        this._matchingPropertyName = segment;
        this._matchingElementIndex = matchIndex;
    }

    public static JsonPointer compile(String input) throws IllegalArgumentException {
        if (input == null || input.length() == 0) {
            return EMPTY;
        }
        if (input.charAt(0) == IOUtils.DIR_SEPARATOR_UNIX) {
            return _parseTail(input);
        }
        throw new IllegalArgumentException("Invalid input: JSON Pointer expression must start with '/': \"" + input + "\"");
    }

    public static JsonPointer valueOf(String input) {
        return compile(input);
    }

    public boolean matches() {
        return this._nextSegment == null;
    }

    public String getMatchingProperty() {
        return this._matchingPropertyName;
    }

    public int getMatchingIndex() {
        return this._matchingElementIndex;
    }

    public boolean mayMatchProperty() {
        return this._matchingPropertyName != null;
    }

    public boolean mayMatchElement() {
        return this._matchingElementIndex >= 0;
    }

    public JsonPointer last() {
        if (this == EMPTY) {
            return null;
        }
        while (true) {
            JsonPointer current;
            JsonPointer next = current._nextSegment;
            if (next == EMPTY) {
                return current;
            }
            current = next;
        }
    }

    public JsonPointer append(JsonPointer tail) {
        if (this == EMPTY) {
            return tail;
        }
        if (tail == EMPTY) {
            return this;
        }
        String currentJsonPointer = this._asString;
        if (currentJsonPointer.endsWith("/")) {
            currentJsonPointer = currentJsonPointer.substring(0, currentJsonPointer.length() - 1);
        }
        return compile(currentJsonPointer + tail._asString);
    }

    public boolean matchesProperty(String name) {
        return this._nextSegment != null && this._matchingPropertyName.equals(name);
    }

    public JsonPointer matchProperty(String name) {
        if (this._nextSegment == null || !this._matchingPropertyName.equals(name)) {
            return null;
        }
        return this._nextSegment;
    }

    public boolean matchesElement(int index) {
        return index == this._matchingElementIndex && index >= 0;
    }

    public JsonPointer matchElement(int index) {
        if (index != this._matchingElementIndex || index < 0) {
            return null;
        }
        return this._nextSegment;
    }

    public JsonPointer tail() {
        return this._nextSegment;
    }

    public JsonPointer head() {
        JsonPointer h = this._head;
        if (h == null) {
            if (this != EMPTY) {
                h = _constructHead();
            }
            this._head = h;
        }
        return h;
    }

    public String toString() {
        return this._asString;
    }

    public int hashCode() {
        return this._asString.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof JsonPointer)) {
            return false;
        }
        return this._asString.equals(((JsonPointer) o)._asString);
    }

    private static final int _parseIndex(String str) {
        int i = 0;
        int len = str.length();
        if (len == 0 || len > 10) {
            return -1;
        }
        char c = str.charAt(0);
        if (c <= '0') {
            if (!(len == 1 && c == '0')) {
                i = -1;
            }
            return i;
        } else if (c > '9') {
            return -1;
        } else {
            for (int i2 = 1; i2 < len; i2++) {
                c = str.charAt(i2);
                if (c > '9' || c < '0') {
                    return -1;
                }
            }
            if (len != 10 || NumberInput.parseLong(str) <= 2147483647L) {
                return NumberInput.parseInt(str);
            }
            return -1;
        }
    }

    protected static JsonPointer _parseTail(String input) {
        int end = input.length();
        int i = 1;
        while (i < end) {
            char c = input.charAt(i);
            if (c == IOUtils.DIR_SEPARATOR_UNIX) {
                return new JsonPointer(input, input.substring(1, i), _parseTail(input.substring(i)));
            }
            i++;
            if (c == '~' && i < end) {
                return _parseQuotedTail(input, i);
            }
        }
        return new JsonPointer(input, input.substring(1), EMPTY);
    }

    protected static JsonPointer _parseQuotedTail(String input, int i) {
        int end = input.length();
        StringBuilder sb = new StringBuilder(Math.max(16, end));
        if (i > 2) {
            sb.append(input, 1, i - 1);
        }
        int i2 = i + 1;
        _appendEscape(sb, input.charAt(i));
        i = i2;
        while (i < end) {
            char c = input.charAt(i);
            if (c == IOUtils.DIR_SEPARATOR_UNIX) {
                return new JsonPointer(input, sb.toString(), _parseTail(input.substring(i)));
            }
            i++;
            if (c != '~' || i >= end) {
                sb.append(c);
            } else {
                i2 = i + 1;
                _appendEscape(sb, input.charAt(i));
                i = i2;
            }
        }
        return new JsonPointer(input, sb.toString(), EMPTY);
    }

    protected JsonPointer _constructHead() {
        JsonPointer last = last();
        if (last == this) {
            return EMPTY;
        }
        int suffixLength = last._asString.length();
        return new JsonPointer(this._asString.substring(0, this._asString.length() - suffixLength), this._matchingPropertyName, this._matchingElementIndex, this._nextSegment._constructHead(suffixLength, last));
    }

    protected JsonPointer _constructHead(int suffixLength, JsonPointer last) {
        if (this == last) {
            return EMPTY;
        }
        JsonPointer next = this._nextSegment;
        String str = this._asString;
        return new JsonPointer(str.substring(0, str.length() - suffixLength), this._matchingPropertyName, this._matchingElementIndex, next._constructHead(suffixLength, last));
    }

    private static void _appendEscape(StringBuilder sb, char c) {
        if (c == '0') {
            c = '~';
        } else if (c == '1') {
            c = IOUtils.DIR_SEPARATOR_UNIX;
        } else {
            sb.append('~');
        }
        sb.append(c);
    }
}
