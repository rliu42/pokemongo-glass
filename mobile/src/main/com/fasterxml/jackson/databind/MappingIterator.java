package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class MappingIterator<T> implements Iterator<T>, Closeable {
    protected static final MappingIterator<?> EMPTY_ITERATOR;
    protected static final int STATE_CLOSED = 0;
    protected static final int STATE_HAS_VALUE = 3;
    protected static final int STATE_MAY_HAVE_VALUE = 2;
    protected static final int STATE_NEED_RESYNC = 1;
    protected final boolean _closeParser;
    protected final DeserializationContext _context;
    protected final JsonDeserializer<T> _deserializer;
    protected final JsonParser _parser;
    protected final JsonStreamContext _seqContext;
    protected int _state;
    protected final JavaType _type;
    protected final T _updatedValue;

    static {
        EMPTY_ITERATOR = new MappingIterator(null, null, null, null, false, null);
    }

    protected MappingIterator(JavaType type, JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser, boolean managedParser, Object valueToUpdate) {
        this._type = type;
        this._parser = p;
        this._context = ctxt;
        this._deserializer = deser;
        this._closeParser = managedParser;
        if (valueToUpdate == null) {
            this._updatedValue = null;
        } else {
            this._updatedValue = valueToUpdate;
        }
        if (p == null) {
            this._seqContext = null;
            this._state = STATE_CLOSED;
            return;
        }
        JsonStreamContext sctxt = p.getParsingContext();
        if (managedParser && p.isExpectedStartArrayToken()) {
            p.clearCurrentToken();
        } else {
            JsonToken t = p.getCurrentToken();
            if (t == JsonToken.START_OBJECT || t == JsonToken.START_ARRAY) {
                sctxt = sctxt.getParent();
            }
        }
        this._seqContext = sctxt;
        this._state = STATE_MAY_HAVE_VALUE;
    }

    protected static <T> MappingIterator<T> emptyIterator() {
        return EMPTY_ITERATOR;
    }

    public boolean hasNext() {
        try {
            return hasNextValue();
        } catch (JsonMappingException e) {
            return ((Boolean) _handleMappingException(e)).booleanValue();
        } catch (IOException e2) {
            return ((Boolean) _handleIOException(e2)).booleanValue();
        }
    }

    public T next() {
        try {
            return nextValue();
        } catch (JsonMappingException e) {
            throw new RuntimeJsonMappingException(e.getMessage(), e);
        } catch (IOException e2) {
            throw new RuntimeException(e2.getMessage(), e2);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void close() throws IOException {
        if (this._state != 0) {
            this._state = STATE_CLOSED;
            if (this._parser != null) {
                this._parser.close();
            }
        }
    }

    public boolean hasNextValue() throws IOException {
        switch (this._state) {
            case STATE_CLOSED /*0*/:
                return false;
            case STATE_NEED_RESYNC /*1*/:
                _resync();
                break;
            case STATE_MAY_HAVE_VALUE /*2*/:
                break;
            default:
                return true;
        }
        if (this._parser.getCurrentToken() == null) {
            JsonToken t = this._parser.nextToken();
            if (t == null || t == JsonToken.END_ARRAY) {
                this._state = STATE_CLOSED;
                if (!this._closeParser || this._parser == null) {
                    return false;
                }
                this._parser.close();
                return false;
            }
        }
        this._state = STATE_HAS_VALUE;
        return true;
    }

    public T nextValue() throws IOException {
        switch (this._state) {
            case STATE_CLOSED /*0*/:
                return _throwNoSuchElement();
            case STATE_NEED_RESYNC /*1*/:
            case STATE_MAY_HAVE_VALUE /*2*/:
                if (!hasNextValue()) {
                    return _throwNoSuchElement();
                }
                break;
        }
        try {
            T value;
            if (this._updatedValue == null) {
                value = this._deserializer.deserialize(this._parser, this._context);
            } else {
                this._deserializer.deserialize(this._parser, this._context, this._updatedValue);
                value = this._updatedValue;
            }
            this._state = STATE_MAY_HAVE_VALUE;
            this._parser.clearCurrentToken();
            return value;
        } catch (Throwable th) {
            this._state = STATE_NEED_RESYNC;
            this._parser.clearCurrentToken();
        }
    }

    public List<T> readAll() throws IOException {
        return readAll(new ArrayList());
    }

    public <L extends List<? super T>> L readAll(L resultList) throws IOException {
        while (hasNextValue()) {
            resultList.add(nextValue());
        }
        return resultList;
    }

    public <C extends Collection<? super T>> C readAll(C results) throws IOException {
        while (hasNextValue()) {
            results.add(nextValue());
        }
        return results;
    }

    public JsonParser getParser() {
        return this._parser;
    }

    public FormatSchema getParserSchema() {
        return this._parser.getSchema();
    }

    public JsonLocation getCurrentLocation() {
        return this._parser.getCurrentLocation();
    }

    protected void _resync() throws IOException {
        JsonParser p = this._parser;
        if (p.getParsingContext() != this._seqContext) {
            while (true) {
                JsonToken t = p.nextToken();
                if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                    if (p.getParsingContext() == this._seqContext) {
                        p.clearCurrentToken();
                        return;
                    }
                } else if (t == JsonToken.START_ARRAY || t == JsonToken.START_OBJECT) {
                    p.skipChildren();
                } else if (t == null) {
                    return;
                }
            }
        }
    }

    protected <R> R _throwNoSuchElement() {
        throw new NoSuchElementException();
    }

    protected <R> R _handleMappingException(JsonMappingException e) {
        throw new RuntimeJsonMappingException(e.getMessage(), e);
    }

    protected <R> R _handleIOException(IOException e) {
        throw new RuntimeException(e.getMessage(), e);
    }
}
