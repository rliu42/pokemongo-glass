package com.google.gson.stream;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class JsonReader implements Closeable {
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final char[] NON_EXECUTE_PREFIX;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_EOF = 17;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private final char[] buffer;
    private final Reader in;
    private boolean lenient;
    private int limit;
    private int lineNumber;
    private int lineStart;
    private int peeked;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int pos;
    private int[] stack;
    private int stackSize;

    /* renamed from: com.google.gson.stream.JsonReader.1 */
    static class C07201 extends JsonReaderInternalAccess {
        C07201() {
        }

        public void promoteNameToValue(JsonReader reader) throws IOException {
            if (reader instanceof JsonTreeReader) {
                ((JsonTreeReader) reader).promoteNameToValue();
                return;
            }
            int p = reader.peeked;
            if (p == 0) {
                p = reader.doPeek();
            }
            if (p == JsonReader.PEEKED_DOUBLE_QUOTED_NAME) {
                reader.peeked = JsonReader.PEEKED_DOUBLE_QUOTED;
            } else if (p == JsonReader.PEEKED_SINGLE_QUOTED_NAME) {
                reader.peeked = JsonReader.PEEKED_SINGLE_QUOTED;
            } else if (p == JsonReader.PEEKED_UNQUOTED_NAME) {
                reader.peeked = JsonReader.PEEKED_UNQUOTED;
            } else {
                throw new IllegalStateException("Expected a name but was " + reader.peek() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + " at line " + reader.getLineNumber() + " column " + reader.getColumnNumber());
            }
        }
    }

    static {
        NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
        JsonReaderInternalAccess.INSTANCE = new C07201();
    }

    public JsonReader(Reader in) {
        this.lenient = false;
        this.buffer = new char[Place.TYPE_SUBLOCALITY_LEVEL_2];
        this.pos = PEEKED_NONE;
        this.limit = PEEKED_NONE;
        this.lineNumber = PEEKED_NONE;
        this.lineStart = PEEKED_NONE;
        this.peeked = PEEKED_NONE;
        this.stack = new int[32];
        this.stackSize = PEEKED_NONE;
        int[] iArr = this.stack;
        int i = this.stackSize;
        this.stackSize = i + PEEKED_BEGIN_OBJECT;
        iArr[i] = PEEKED_FALSE;
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.in = in;
    }

    public final void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public final boolean isLenient() {
        return this.lenient;
    }

    public void beginArray() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == PEEKED_BEGIN_ARRAY) {
            push(PEEKED_BEGIN_OBJECT);
            this.peeked = PEEKED_NONE;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
    }

    public void endArray() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == PEEKED_END_ARRAY) {
            this.stackSize--;
            this.peeked = PEEKED_NONE;
            return;
        }
        throw new IllegalStateException("Expected END_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
    }

    public void beginObject() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == PEEKED_BEGIN_OBJECT) {
            push(PEEKED_BEGIN_ARRAY);
            this.peeked = PEEKED_NONE;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
    }

    public void endObject() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == PEEKED_END_OBJECT) {
            this.stackSize--;
            this.peeked = PEEKED_NONE;
            return;
        }
        throw new IllegalStateException("Expected END_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
    }

    public boolean hasNext() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        return (p == PEEKED_END_OBJECT || p == PEEKED_END_ARRAY) ? false : true;
    }

    public JsonToken peek() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        switch (p) {
            case PEEKED_BEGIN_OBJECT /*1*/:
                return JsonToken.BEGIN_OBJECT;
            case PEEKED_END_OBJECT /*2*/:
                return JsonToken.END_OBJECT;
            case PEEKED_BEGIN_ARRAY /*3*/:
                return JsonToken.BEGIN_ARRAY;
            case PEEKED_END_ARRAY /*4*/:
                return JsonToken.END_ARRAY;
            case PEEKED_TRUE /*5*/:
            case PEEKED_FALSE /*6*/:
                return JsonToken.BOOLEAN;
            case PEEKED_NULL /*7*/:
                return JsonToken.NULL;
            case PEEKED_SINGLE_QUOTED /*8*/:
            case PEEKED_DOUBLE_QUOTED /*9*/:
            case PEEKED_UNQUOTED /*10*/:
            case PEEKED_BUFFERED /*11*/:
                return JsonToken.STRING;
            case PEEKED_SINGLE_QUOTED_NAME /*12*/:
            case PEEKED_DOUBLE_QUOTED_NAME /*13*/:
            case PEEKED_UNQUOTED_NAME /*14*/:
                return JsonToken.NAME;
            case PEEKED_LONG /*15*/:
            case PEEKED_NUMBER /*16*/:
                return JsonToken.NUMBER;
            case PEEKED_EOF /*17*/:
                return JsonToken.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    private int doPeek() throws IOException {
        int peekStack = this.stack[this.stackSize - 1];
        if (peekStack == PEEKED_BEGIN_OBJECT) {
            this.stack[this.stackSize - 1] = PEEKED_END_OBJECT;
        } else if (peekStack == PEEKED_END_OBJECT) {
            switch (nextNonWhitespace(true)) {
                case Place.TYPE_GYM /*44*/:
                    break;
                case Place.TYPE_LODGING /*59*/:
                    checkLenient();
                    break;
                case Place.TYPE_TRAVEL_AGENCY /*93*/:
                    this.peeked = PEEKED_END_ARRAY;
                    return PEEKED_END_ARRAY;
                default:
                    throw syntaxError("Unterminated array");
            }
        } else if (peekStack == PEEKED_BEGIN_ARRAY || peekStack == PEEKED_TRUE) {
            this.stack[this.stackSize - 1] = PEEKED_END_ARRAY;
            if (peekStack == PEEKED_TRUE) {
                switch (nextNonWhitespace(true)) {
                    case Place.TYPE_GYM /*44*/:
                        break;
                    case Place.TYPE_LODGING /*59*/:
                        checkLenient();
                        break;
                    case 125:
                        this.peeked = PEEKED_END_OBJECT;
                        return PEEKED_END_OBJECT;
                    default:
                        throw syntaxError("Unterminated object");
                }
            }
            int c = nextNonWhitespace(true);
            switch (c) {
                case Place.TYPE_ESTABLISHMENT /*34*/:
                    this.peeked = PEEKED_DOUBLE_QUOTED_NAME;
                    return PEEKED_DOUBLE_QUOTED_NAME;
                case Place.TYPE_FUNERAL_HOME /*39*/:
                    checkLenient();
                    this.peeked = PEEKED_SINGLE_QUOTED_NAME;
                    return PEEKED_SINGLE_QUOTED_NAME;
                case 125:
                    if (peekStack != PEEKED_TRUE) {
                        this.peeked = PEEKED_END_OBJECT;
                        return PEEKED_END_OBJECT;
                    }
                    throw syntaxError("Expected name");
                default:
                    checkLenient();
                    this.pos--;
                    if (isLiteral((char) c)) {
                        this.peeked = PEEKED_UNQUOTED_NAME;
                        return PEEKED_UNQUOTED_NAME;
                    }
                    throw syntaxError("Expected name");
            }
        } else if (peekStack == PEEKED_END_ARRAY) {
            this.stack[this.stackSize - 1] = PEEKED_TRUE;
            switch (nextNonWhitespace(true)) {
                case Place.TYPE_LOCKSMITH /*58*/:
                    break;
                case Place.TYPE_MEAL_TAKEAWAY /*61*/:
                    checkLenient();
                    if ((this.pos < this.limit || fillBuffer(PEEKED_BEGIN_OBJECT)) && this.buffer[this.pos] == '>') {
                        this.pos += PEEKED_BEGIN_OBJECT;
                        break;
                    }
                default:
                    throw syntaxError("Expected ':'");
            }
        } else if (peekStack == PEEKED_FALSE) {
            if (this.lenient) {
                consumeNonExecutePrefix();
            }
            this.stack[this.stackSize - 1] = PEEKED_NULL;
        } else if (peekStack == PEEKED_NULL) {
            if (nextNonWhitespace(false) == -1) {
                this.peeked = PEEKED_EOF;
                return PEEKED_EOF;
            }
            checkLenient();
            this.pos--;
        } else if (peekStack == PEEKED_SINGLE_QUOTED) {
            throw new IllegalStateException("JsonReader is closed");
        }
        switch (nextNonWhitespace(true)) {
            case Place.TYPE_ESTABLISHMENT /*34*/:
                if (this.stackSize == PEEKED_BEGIN_OBJECT) {
                    checkLenient();
                }
                this.peeked = PEEKED_DOUBLE_QUOTED;
                return PEEKED_DOUBLE_QUOTED;
            case Place.TYPE_FUNERAL_HOME /*39*/:
                checkLenient();
                this.peeked = PEEKED_SINGLE_QUOTED;
                return PEEKED_SINGLE_QUOTED;
            case Place.TYPE_GYM /*44*/:
            case Place.TYPE_LODGING /*59*/:
                break;
            case Place.TYPE_TAXI_STAND /*91*/:
                this.peeked = PEEKED_BEGIN_ARRAY;
                return PEEKED_BEGIN_ARRAY;
            case Place.TYPE_TRAVEL_AGENCY /*93*/:
                if (peekStack == PEEKED_BEGIN_OBJECT) {
                    this.peeked = PEEKED_END_ARRAY;
                    return PEEKED_END_ARRAY;
                }
                break;
            case 123:
                this.peeked = PEEKED_BEGIN_OBJECT;
                return PEEKED_BEGIN_OBJECT;
            default:
                this.pos--;
                if (this.stackSize == PEEKED_BEGIN_OBJECT) {
                    checkLenient();
                }
                int result = peekKeyword();
                if (result != 0) {
                    return result;
                }
                result = peekNumber();
                if (result != 0) {
                    return result;
                }
                if (isLiteral(this.buffer[this.pos])) {
                    checkLenient();
                    this.peeked = PEEKED_UNQUOTED;
                    return PEEKED_UNQUOTED;
                }
                throw syntaxError("Expected value");
        }
        if (peekStack == PEEKED_BEGIN_OBJECT || peekStack == PEEKED_END_OBJECT) {
            checkLenient();
            this.pos--;
            this.peeked = PEEKED_NULL;
            return PEEKED_NULL;
        }
        throw syntaxError("Unexpected value");
    }

    private int peekKeyword() throws IOException {
        String keyword;
        int peeking;
        char c = this.buffer[this.pos];
        String keywordUpper;
        if (c == 't' || c == 'T') {
            keyword = "true";
            keywordUpper = "TRUE";
            peeking = PEEKED_TRUE;
        } else if (c == 'f' || c == 'F') {
            keyword = "false";
            keywordUpper = "FALSE";
            peeking = PEEKED_FALSE;
        } else if (c != 'n' && c != 'N') {
            return PEEKED_NONE;
        } else {
            keyword = "null";
            keywordUpper = "NULL";
            peeking = PEEKED_NULL;
        }
        int length = keyword.length();
        int i = PEEKED_BEGIN_OBJECT;
        while (i < length) {
            if (this.pos + i >= this.limit && !fillBuffer(i + PEEKED_BEGIN_OBJECT)) {
                return PEEKED_NONE;
            }
            c = this.buffer[this.pos + i];
            if (c != keyword.charAt(i) && c != keywordUpper.charAt(i)) {
                return PEEKED_NONE;
            }
            i += PEEKED_BEGIN_OBJECT;
        }
        if ((this.pos + length < this.limit || fillBuffer(length + PEEKED_BEGIN_OBJECT)) && isLiteral(this.buffer[this.pos + length])) {
            return PEEKED_NONE;
        }
        this.pos += length;
        this.peeked = peeking;
        return peeking;
    }

    private int peekNumber() throws IOException {
        char[] buffer = this.buffer;
        int p = this.pos;
        int l = this.limit;
        long value = 0;
        boolean negative = false;
        boolean fitsInLong = true;
        int last = PEEKED_NONE;
        int i = PEEKED_NONE;
        while (true) {
            if (p + i == l) {
                if (i == buffer.length) {
                    return PEEKED_NONE;
                }
                if (fillBuffer(i + PEEKED_BEGIN_OBJECT)) {
                    p = this.pos;
                    l = this.limit;
                } else if (last != PEEKED_END_OBJECT && fitsInLong && (value != Long.MIN_VALUE || negative)) {
                    if (!negative) {
                        value = -value;
                    }
                    this.peekedLong = value;
                    this.pos += i;
                    this.peeked = PEEKED_LONG;
                    return PEEKED_LONG;
                } else if (last == PEEKED_END_OBJECT && last != PEEKED_END_ARRAY && last != PEEKED_NULL) {
                    return PEEKED_NONE;
                } else {
                    this.peekedNumberLength = i;
                    this.peeked = PEEKED_NUMBER;
                    return PEEKED_NUMBER;
                }
            }
            char c = buffer[p + i];
            switch (c) {
                case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                    if (last != PEEKED_TRUE) {
                        return PEEKED_NONE;
                    }
                    last = PEEKED_FALSE;
                    continue;
                case Place.TYPE_HAIR_CARE /*45*/:
                    if (last == 0) {
                        negative = true;
                        last = PEEKED_BEGIN_OBJECT;
                        continue;
                    } else if (last == PEEKED_TRUE) {
                        last = PEEKED_FALSE;
                        break;
                    } else {
                        return PEEKED_NONE;
                    }
                case Place.TYPE_HARDWARE_STORE /*46*/:
                    if (last != PEEKED_END_OBJECT) {
                        return PEEKED_NONE;
                    }
                    last = PEEKED_BEGIN_ARRAY;
                    continue;
                case Place.TYPE_PARK /*69*/:
                case 'e':
                    if (last != PEEKED_END_OBJECT && last != PEEKED_END_ARRAY) {
                        return PEEKED_NONE;
                    }
                    last = PEEKED_TRUE;
                    continue;
                default:
                    if (c >= '0' && c <= '9') {
                        if (last != PEEKED_BEGIN_OBJECT && last != 0) {
                            if (last != PEEKED_END_OBJECT) {
                                if (last != PEEKED_BEGIN_ARRAY) {
                                    if (last != PEEKED_TRUE && last != PEEKED_FALSE) {
                                        break;
                                    }
                                    last = PEEKED_NULL;
                                    break;
                                }
                                last = PEEKED_END_ARRAY;
                                break;
                            } else if (value != 0) {
                                long newValue = (10 * value) - ((long) (c - 48));
                                int i2 = (value > MIN_INCOMPLETE_INTEGER || (value == MIN_INCOMPLETE_INTEGER && newValue < value)) ? PEEKED_BEGIN_OBJECT : PEEKED_NONE;
                                fitsInLong &= i2;
                                value = newValue;
                                break;
                            } else {
                                return PEEKED_NONE;
                            }
                        }
                        value = (long) (-(c - 48));
                        last = PEEKED_END_OBJECT;
                        continue;
                    } else if (isLiteral(c)) {
                        return PEEKED_NONE;
                    }
                    break;
            }
            if (last != PEEKED_END_OBJECT) {
            }
            if (last == PEEKED_END_OBJECT) {
            }
            this.peekedNumberLength = i;
            this.peeked = PEEKED_NUMBER;
            return PEEKED_NUMBER;
            i += PEEKED_BEGIN_OBJECT;
        }
    }

    private boolean isLiteral(char c) throws IOException {
        switch (c) {
            case PEEKED_DOUBLE_QUOTED /*9*/:
            case PEEKED_UNQUOTED /*10*/:
            case PEEKED_SINGLE_QUOTED_NAME /*12*/:
            case PEEKED_DOUBLE_QUOTED_NAME /*13*/:
            case Place.TYPE_ELECTRONICS_STORE /*32*/:
            case Place.TYPE_GYM /*44*/:
            case Place.TYPE_LOCKSMITH /*58*/:
            case Place.TYPE_TAXI_STAND /*91*/:
            case Place.TYPE_TRAVEL_AGENCY /*93*/:
            case '{':
            case '}':
                break;
            case Place.TYPE_FINANCE /*35*/:
            case Place.TYPE_HEALTH /*47*/:
            case Place.TYPE_LODGING /*59*/:
            case Place.TYPE_MEAL_TAKEAWAY /*61*/:
            case Place.TYPE_TRAIN_STATION /*92*/:
                checkLenient();
                break;
            default:
                return true;
        }
        return false;
    }

    public String nextName() throws IOException {
        String result;
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == PEEKED_UNQUOTED_NAME) {
            result = nextUnquotedValue();
        } else if (p == PEEKED_SINGLE_QUOTED_NAME) {
            result = nextQuotedValue('\'');
        } else if (p == PEEKED_DOUBLE_QUOTED_NAME) {
            result = nextQuotedValue('\"');
        } else {
            throw new IllegalStateException("Expected a name but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
        this.peeked = PEEKED_NONE;
        return result;
    }

    public String nextString() throws IOException {
        String result;
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == PEEKED_UNQUOTED) {
            result = nextUnquotedValue();
        } else if (p == PEEKED_SINGLE_QUOTED) {
            result = nextQuotedValue('\'');
        } else if (p == PEEKED_DOUBLE_QUOTED) {
            result = nextQuotedValue('\"');
        } else if (p == PEEKED_BUFFERED) {
            result = this.peekedString;
            this.peekedString = null;
        } else if (p == PEEKED_LONG) {
            result = Long.toString(this.peekedLong);
        } else if (p == PEEKED_NUMBER) {
            result = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            throw new IllegalStateException("Expected a string but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
        this.peeked = PEEKED_NONE;
        return result;
    }

    public boolean nextBoolean() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == PEEKED_TRUE) {
            this.peeked = PEEKED_NONE;
            return true;
        } else if (p == PEEKED_FALSE) {
            this.peeked = PEEKED_NONE;
            return false;
        } else {
            throw new IllegalStateException("Expected a boolean but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
    }

    public void nextNull() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == PEEKED_NULL) {
            this.peeked = PEEKED_NONE;
            return;
        }
        throw new IllegalStateException("Expected null but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
    }

    public double nextDouble() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == PEEKED_LONG) {
            this.peeked = PEEKED_NONE;
            return (double) this.peekedLong;
        }
        if (p == PEEKED_NUMBER) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (p == PEEKED_SINGLE_QUOTED || p == PEEKED_DOUBLE_QUOTED) {
            this.peekedString = nextQuotedValue(p == PEEKED_SINGLE_QUOTED ? '\'' : '\"');
        } else if (p == PEEKED_UNQUOTED) {
            this.peekedString = nextUnquotedValue();
        } else if (p != PEEKED_BUFFERED) {
            throw new IllegalStateException("Expected a double but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
        this.peeked = PEEKED_BUFFERED;
        double result = Double.parseDouble(this.peekedString);
        if (this.lenient || !(Double.isNaN(result) || Double.isInfinite(result))) {
            this.peekedString = null;
            this.peeked = PEEKED_NONE;
            return result;
        }
        throw new MalformedJsonException("JSON forbids NaN and infinities: " + result + " at line " + getLineNumber() + " column " + getColumnNumber());
    }

    public long nextLong() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == PEEKED_LONG) {
            this.peeked = PEEKED_NONE;
            return this.peekedLong;
        }
        long parseLong;
        if (p == PEEKED_NUMBER) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (p == PEEKED_SINGLE_QUOTED || p == PEEKED_DOUBLE_QUOTED) {
            this.peekedString = nextQuotedValue(p == PEEKED_SINGLE_QUOTED ? '\'' : '\"');
            try {
                parseLong = Long.parseLong(this.peekedString);
                this.peeked = PEEKED_NONE;
                return parseLong;
            } catch (NumberFormatException e) {
            }
        } else {
            throw new IllegalStateException("Expected a long but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
        this.peeked = PEEKED_BUFFERED;
        double asDouble = Double.parseDouble(this.peekedString);
        parseLong = (long) asDouble;
        if (((double) parseLong) != asDouble) {
            throw new NumberFormatException("Expected a long but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
        this.peekedString = null;
        this.peeked = PEEKED_NONE;
        return parseLong;
    }

    private String nextQuotedValue(char quote) throws IOException {
        char[] buffer = this.buffer;
        StringBuilder builder = new StringBuilder();
        do {
            int p = this.pos;
            int l = this.limit;
            int start = p;
            int p2 = p;
            while (p2 < l) {
                p = p2 + PEEKED_BEGIN_OBJECT;
                char c = buffer[p2];
                if (c == quote) {
                    this.pos = p;
                    builder.append(buffer, start, (p - start) - 1);
                    return builder.toString();
                }
                if (c == '\\') {
                    this.pos = p;
                    builder.append(buffer, start, (p - start) - 1);
                    builder.append(readEscapeCharacter());
                    p = this.pos;
                    l = this.limit;
                    start = p;
                } else if (c == PEEKED_UNQUOTED) {
                    this.lineNumber += PEEKED_BEGIN_OBJECT;
                    this.lineStart = p;
                }
                p2 = p;
            }
            builder.append(buffer, start, p2 - start);
            this.pos = p2;
        } while (fillBuffer(PEEKED_BEGIN_OBJECT));
        throw syntaxError("Unterminated string");
    }

    private String nextUnquotedValue() throws IOException {
        StringBuilder builder = null;
        int i = PEEKED_NONE;
        while (true) {
            String result;
            if (this.pos + i < this.limit) {
                switch (this.buffer[this.pos + i]) {
                    case PEEKED_DOUBLE_QUOTED /*9*/:
                    case PEEKED_UNQUOTED /*10*/:
                    case PEEKED_SINGLE_QUOTED_NAME /*12*/:
                    case PEEKED_DOUBLE_QUOTED_NAME /*13*/:
                    case Place.TYPE_ELECTRONICS_STORE /*32*/:
                    case Place.TYPE_GYM /*44*/:
                    case Place.TYPE_LOCKSMITH /*58*/:
                    case Place.TYPE_TAXI_STAND /*91*/:
                    case Place.TYPE_TRAVEL_AGENCY /*93*/:
                    case '{':
                    case '}':
                        break;
                    case Place.TYPE_FINANCE /*35*/:
                    case Place.TYPE_HEALTH /*47*/:
                    case Place.TYPE_LODGING /*59*/:
                    case Place.TYPE_MEAL_TAKEAWAY /*61*/:
                    case Place.TYPE_TRAIN_STATION /*92*/:
                        checkLenient();
                        break;
                    default:
                        i += PEEKED_BEGIN_OBJECT;
                        continue;
                }
            } else if (i >= this.buffer.length) {
                if (builder == null) {
                    builder = new StringBuilder();
                }
                builder.append(this.buffer, this.pos, i);
                this.pos += i;
                i = PEEKED_NONE;
                if (fillBuffer(PEEKED_BEGIN_OBJECT)) {
                }
            } else if (fillBuffer(i + PEEKED_BEGIN_OBJECT)) {
            }
            if (builder == null) {
                result = new String(this.buffer, this.pos, i);
            } else {
                builder.append(this.buffer, this.pos, i);
                result = builder.toString();
            }
            this.pos += i;
            return result;
        }
    }

    private void skipQuotedValue(char quote) throws IOException {
        char[] buffer = this.buffer;
        do {
            int p = this.pos;
            int l = this.limit;
            int p2 = p;
            while (p2 < l) {
                p = p2 + PEEKED_BEGIN_OBJECT;
                char c = buffer[p2];
                if (c == quote) {
                    this.pos = p;
                    return;
                }
                if (c == '\\') {
                    this.pos = p;
                    readEscapeCharacter();
                    p = this.pos;
                    l = this.limit;
                } else if (c == PEEKED_UNQUOTED) {
                    this.lineNumber += PEEKED_BEGIN_OBJECT;
                    this.lineStart = p;
                }
                p2 = p;
            }
            this.pos = p2;
        } while (fillBuffer(PEEKED_BEGIN_OBJECT));
        throw syntaxError("Unterminated string");
    }

    private void skipUnquotedValue() throws IOException {
        do {
            int i = PEEKED_NONE;
            while (this.pos + i < this.limit) {
                switch (this.buffer[this.pos + i]) {
                    case PEEKED_DOUBLE_QUOTED /*9*/:
                    case PEEKED_UNQUOTED /*10*/:
                    case PEEKED_SINGLE_QUOTED_NAME /*12*/:
                    case PEEKED_DOUBLE_QUOTED_NAME /*13*/:
                    case Place.TYPE_ELECTRONICS_STORE /*32*/:
                    case Place.TYPE_GYM /*44*/:
                    case Place.TYPE_LOCKSMITH /*58*/:
                    case Place.TYPE_TAXI_STAND /*91*/:
                    case Place.TYPE_TRAVEL_AGENCY /*93*/:
                    case '{':
                    case '}':
                        break;
                    case Place.TYPE_FINANCE /*35*/:
                    case Place.TYPE_HEALTH /*47*/:
                    case Place.TYPE_LODGING /*59*/:
                    case Place.TYPE_MEAL_TAKEAWAY /*61*/:
                    case Place.TYPE_TRAIN_STATION /*92*/:
                        checkLenient();
                        break;
                    default:
                        i += PEEKED_BEGIN_OBJECT;
                }
                this.pos += i;
                return;
            }
            this.pos += i;
        } while (fillBuffer(PEEKED_BEGIN_OBJECT));
    }

    public int nextInt() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        int result;
        if (p == PEEKED_LONG) {
            result = (int) this.peekedLong;
            if (this.peekedLong != ((long) result)) {
                throw new NumberFormatException("Expected an int but was " + this.peekedLong + " at line " + getLineNumber() + " column " + getColumnNumber());
            }
            this.peeked = PEEKED_NONE;
            return result;
        }
        if (p == PEEKED_NUMBER) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (p == PEEKED_SINGLE_QUOTED || p == PEEKED_DOUBLE_QUOTED) {
            this.peekedString = nextQuotedValue(p == PEEKED_SINGLE_QUOTED ? '\'' : '\"');
            try {
                result = Integer.parseInt(this.peekedString);
                this.peeked = PEEKED_NONE;
                return result;
            } catch (NumberFormatException e) {
            }
        } else {
            throw new IllegalStateException("Expected an int but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
        this.peeked = PEEKED_BUFFERED;
        double asDouble = Double.parseDouble(this.peekedString);
        result = (int) asDouble;
        if (((double) result) != asDouble) {
            throw new NumberFormatException("Expected an int but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
        this.peekedString = null;
        this.peeked = PEEKED_NONE;
        return result;
    }

    public void close() throws IOException {
        this.peeked = PEEKED_NONE;
        this.stack[PEEKED_NONE] = PEEKED_SINGLE_QUOTED;
        this.stackSize = PEEKED_BEGIN_OBJECT;
        this.in.close();
    }

    public void skipValue() throws IOException {
        int count = PEEKED_NONE;
        do {
            int p = this.peeked;
            if (p == 0) {
                p = doPeek();
            }
            if (p == PEEKED_BEGIN_ARRAY) {
                push(PEEKED_BEGIN_OBJECT);
                count += PEEKED_BEGIN_OBJECT;
            } else if (p == PEEKED_BEGIN_OBJECT) {
                push(PEEKED_BEGIN_ARRAY);
                count += PEEKED_BEGIN_OBJECT;
            } else if (p == PEEKED_END_ARRAY) {
                this.stackSize--;
                count--;
            } else if (p == PEEKED_END_OBJECT) {
                this.stackSize--;
                count--;
            } else if (p == PEEKED_UNQUOTED_NAME || p == PEEKED_UNQUOTED) {
                skipUnquotedValue();
            } else if (p == PEEKED_SINGLE_QUOTED || p == PEEKED_SINGLE_QUOTED_NAME) {
                skipQuotedValue('\'');
            } else if (p == PEEKED_DOUBLE_QUOTED || p == PEEKED_DOUBLE_QUOTED_NAME) {
                skipQuotedValue('\"');
            } else if (p == PEEKED_NUMBER) {
                this.pos += this.peekedNumberLength;
            }
            this.peeked = PEEKED_NONE;
        } while (count != 0);
    }

    private void push(int newTop) {
        if (this.stackSize == this.stack.length) {
            int[] newStack = new int[(this.stackSize * PEEKED_END_OBJECT)];
            System.arraycopy(this.stack, PEEKED_NONE, newStack, PEEKED_NONE, this.stackSize);
            this.stack = newStack;
        }
        int[] iArr = this.stack;
        int i = this.stackSize;
        this.stackSize = i + PEEKED_BEGIN_OBJECT;
        iArr[i] = newTop;
    }

    private boolean fillBuffer(int minimum) throws IOException {
        char[] buffer = this.buffer;
        this.lineStart -= this.pos;
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(buffer, this.pos, buffer, PEEKED_NONE, this.limit);
        } else {
            this.limit = PEEKED_NONE;
        }
        this.pos = PEEKED_NONE;
        do {
            int total = this.in.read(buffer, this.limit, buffer.length - this.limit);
            if (total == -1) {
                return false;
            }
            this.limit += total;
            if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && buffer[PEEKED_NONE] == '\ufeff') {
                this.pos += PEEKED_BEGIN_OBJECT;
                this.lineStart += PEEKED_BEGIN_OBJECT;
                minimum += PEEKED_BEGIN_OBJECT;
            }
        } while (this.limit < minimum);
        return true;
    }

    private int getLineNumber() {
        return this.lineNumber + PEEKED_BEGIN_OBJECT;
    }

    private int getColumnNumber() {
        return (this.pos - this.lineStart) + PEEKED_BEGIN_OBJECT;
    }

    private int nextNonWhitespace(boolean throwOnEof) throws IOException {
        char[] buffer = this.buffer;
        int p = this.pos;
        int l = this.limit;
        while (true) {
            if (p == l) {
                this.pos = p;
                if (fillBuffer(PEEKED_BEGIN_OBJECT)) {
                    p = this.pos;
                    l = this.limit;
                } else if (!throwOnEof) {
                    return -1;
                } else {
                    throw new EOFException("End of input at line " + getLineNumber() + " column " + getColumnNumber());
                }
            }
            int p2 = p + PEEKED_BEGIN_OBJECT;
            int c = buffer[p];
            if (c == PEEKED_UNQUOTED) {
                this.lineNumber += PEEKED_BEGIN_OBJECT;
                this.lineStart = p2;
                p = p2;
            } else if (c == 32 || c == PEEKED_DOUBLE_QUOTED_NAME) {
                p = p2;
            } else if (c == PEEKED_DOUBLE_QUOTED) {
                p = p2;
            } else if (c == 47) {
                this.pos = p2;
                if (p2 == l) {
                    this.pos--;
                    boolean charsLoaded = fillBuffer(PEEKED_END_OBJECT);
                    this.pos += PEEKED_BEGIN_OBJECT;
                    if (!charsLoaded) {
                        p = p2;
                        return c;
                    }
                }
                checkLenient();
                switch (buffer[this.pos]) {
                    case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                        this.pos += PEEKED_BEGIN_OBJECT;
                        if (skipTo("*/")) {
                            p = this.pos + PEEKED_END_OBJECT;
                            l = this.limit;
                            break;
                        }
                        throw syntaxError("Unterminated comment");
                    case Place.TYPE_HEALTH /*47*/:
                        this.pos += PEEKED_BEGIN_OBJECT;
                        skipToEndOfLine();
                        p = this.pos;
                        l = this.limit;
                        break;
                    default:
                        p = p2;
                        return c;
                }
            } else if (c == 35) {
                this.pos = p2;
                checkLenient();
                skipToEndOfLine();
                p = this.pos;
                l = this.limit;
            } else {
                this.pos = p2;
                p = p2;
                return c;
            }
        }
    }

    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private void skipToEndOfLine() throws IOException {
        char c;
        do {
            if (this.pos < this.limit || fillBuffer(PEEKED_BEGIN_OBJECT)) {
                char[] cArr = this.buffer;
                int i = this.pos;
                this.pos = i + PEEKED_BEGIN_OBJECT;
                c = cArr[i];
                if (c == '\n') {
                    this.lineNumber += PEEKED_BEGIN_OBJECT;
                    this.lineStart = this.pos;
                    return;
                }
            } else {
                return;
            }
        } while (c != '\r');
    }

    private boolean skipTo(String toFind) throws IOException {
        while (true) {
            if (this.pos + toFind.length() > this.limit && !fillBuffer(toFind.length())) {
                return false;
            }
            if (this.buffer[this.pos] == '\n') {
                this.lineNumber += PEEKED_BEGIN_OBJECT;
                this.lineStart = this.pos + PEEKED_BEGIN_OBJECT;
            } else {
                int c = PEEKED_NONE;
                while (c < toFind.length()) {
                    if (this.buffer[this.pos + c] == toFind.charAt(c)) {
                        c += PEEKED_BEGIN_OBJECT;
                    }
                }
                return true;
            }
            this.pos += PEEKED_BEGIN_OBJECT;
        }
    }

    public String toString() {
        return getClass().getSimpleName() + " at line " + getLineNumber() + " column " + getColumnNumber();
    }

    private char readEscapeCharacter() throws IOException {
        if (this.pos != this.limit || fillBuffer(PEEKED_BEGIN_OBJECT)) {
            char[] cArr = this.buffer;
            int i = this.pos;
            this.pos = i + PEEKED_BEGIN_OBJECT;
            char escaped = cArr[i];
            switch (escaped) {
                case PEEKED_UNQUOTED /*10*/:
                    this.lineNumber += PEEKED_BEGIN_OBJECT;
                    this.lineStart = this.pos;
                    break;
                case 'b':
                    return '\b';
                case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                    return '\f';
                case 'n':
                    return '\n';
                case 'r':
                    return '\r';
                case 't':
                    return '\t';
                case 'u':
                    if (this.pos + PEEKED_END_ARRAY <= this.limit || fillBuffer(PEEKED_END_ARRAY)) {
                        char result = '\u0000';
                        int i2 = this.pos;
                        int end = i2 + PEEKED_END_ARRAY;
                        while (i2 < end) {
                            char c = this.buffer[i2];
                            result = (char) (result << PEEKED_END_ARRAY);
                            if (c >= '0' && c <= '9') {
                                result = (char) ((c - 48) + result);
                            } else if (c >= 'a' && c <= 'f') {
                                result = (char) (((c - 97) + PEEKED_UNQUOTED) + result);
                            } else if (c < 'A' || c > 'F') {
                                throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, PEEKED_END_ARRAY));
                            } else {
                                result = (char) (((c - 65) + PEEKED_UNQUOTED) + result);
                            }
                            i2 += PEEKED_BEGIN_OBJECT;
                        }
                        this.pos += PEEKED_END_ARRAY;
                        return result;
                    }
                    throw syntaxError("Unterminated escape sequence");
            }
            return escaped;
        }
        throw syntaxError("Unterminated escape sequence");
    }

    private IOException syntaxError(String message) throws IOException {
        throw new MalformedJsonException(message + " at line " + getLineNumber() + " column " + getColumnNumber());
    }

    private void consumeNonExecutePrefix() throws IOException {
        nextNonWhitespace(true);
        this.pos--;
        if (this.pos + NON_EXECUTE_PREFIX.length <= this.limit || fillBuffer(NON_EXECUTE_PREFIX.length)) {
            int i = PEEKED_NONE;
            while (i < NON_EXECUTE_PREFIX.length) {
                if (this.buffer[this.pos + i] == NON_EXECUTE_PREFIX[i]) {
                    i += PEEKED_BEGIN_OBJECT;
                } else {
                    return;
                }
            }
            this.pos += NON_EXECUTE_PREFIX.length;
        }
    }
}
