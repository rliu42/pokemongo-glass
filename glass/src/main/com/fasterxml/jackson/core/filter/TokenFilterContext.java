package com.fasterxml.jackson.core.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

public class TokenFilterContext extends JsonStreamContext {
    protected TokenFilterContext _child;
    protected String _currentName;
    protected TokenFilter _filter;
    protected boolean _needToHandleName;
    protected final TokenFilterContext _parent;
    protected boolean _startHandled;

    protected TokenFilterContext(int type, TokenFilterContext parent, TokenFilter filter, boolean startHandled) {
        this._child = null;
        this._type = type;
        this._parent = parent;
        this._filter = filter;
        this._index = -1;
        this._startHandled = startHandled;
        this._needToHandleName = false;
    }

    protected TokenFilterContext reset(int type, TokenFilter filter, boolean startWritten) {
        this._type = type;
        this._filter = filter;
        this._index = -1;
        this._currentName = null;
        this._startHandled = startWritten;
        this._needToHandleName = false;
        return this;
    }

    public static TokenFilterContext createRootContext(TokenFilter filter) {
        return new TokenFilterContext(0, null, filter, true);
    }

    public TokenFilterContext createChildArrayContext(TokenFilter filter, boolean writeStart) {
        TokenFilterContext ctxt = this._child;
        if (ctxt != null) {
            return ctxt.reset(1, filter, writeStart);
        }
        ctxt = new TokenFilterContext(1, this, filter, writeStart);
        this._child = ctxt;
        return ctxt;
    }

    public TokenFilterContext createChildObjectContext(TokenFilter filter, boolean writeStart) {
        TokenFilterContext ctxt = this._child;
        if (ctxt != null) {
            return ctxt.reset(2, filter, writeStart);
        }
        ctxt = new TokenFilterContext(2, this, filter, writeStart);
        this._child = ctxt;
        return ctxt;
    }

    public TokenFilter setFieldName(String name) throws JsonProcessingException {
        this._currentName = name;
        this._needToHandleName = true;
        return this._filter;
    }

    public TokenFilter checkValue(TokenFilter filter) {
        if (this._type == 2) {
            return filter;
        }
        int ix = this._index + 1;
        this._index = ix;
        if (this._type == 1) {
            return filter.includeElement(ix);
        }
        return filter.includeRootValue(ix);
    }

    public void writePath(JsonGenerator gen) throws IOException {
        if (this._filter != null && this._filter != TokenFilter.INCLUDE_ALL) {
            if (this._parent != null) {
                this._parent._writePath(gen);
            }
            if (!this._startHandled) {
                this._startHandled = true;
                if (this._type == 2) {
                    gen.writeStartObject();
                    gen.writeFieldName(this._currentName);
                } else if (this._type == 1) {
                    gen.writeStartArray();
                }
            } else if (this._needToHandleName) {
                gen.writeFieldName(this._currentName);
            }
        }
    }

    public void writeImmediatePath(JsonGenerator gen) throws IOException {
        if (this._filter != null && this._filter != TokenFilter.INCLUDE_ALL) {
            if (!this._startHandled) {
                this._startHandled = true;
                if (this._type == 2) {
                    gen.writeStartObject();
                    if (this._needToHandleName) {
                        gen.writeFieldName(this._currentName);
                    }
                } else if (this._type == 1) {
                    gen.writeStartArray();
                }
            } else if (this._needToHandleName) {
                gen.writeFieldName(this._currentName);
            }
        }
    }

    private void _writePath(JsonGenerator gen) throws IOException {
        if (this._filter != null && this._filter != TokenFilter.INCLUDE_ALL) {
            if (this._parent != null) {
                this._parent._writePath(gen);
            }
            if (!this._startHandled) {
                this._startHandled = true;
                if (this._type == 2) {
                    gen.writeStartObject();
                    if (this._needToHandleName) {
                        this._needToHandleName = false;
                        gen.writeFieldName(this._currentName);
                    }
                } else if (this._type == 1) {
                    gen.writeStartArray();
                }
            } else if (this._needToHandleName) {
                this._needToHandleName = false;
                gen.writeFieldName(this._currentName);
            }
        }
    }

    public TokenFilterContext closeArray(JsonGenerator gen) throws IOException {
        if (this._startHandled) {
            gen.writeEndArray();
        }
        if (!(this._filter == null || this._filter == TokenFilter.INCLUDE_ALL)) {
            this._filter.filterFinishArray();
        }
        return this._parent;
    }

    public TokenFilterContext closeObject(JsonGenerator gen) throws IOException {
        if (this._startHandled) {
            gen.writeEndObject();
        }
        if (!(this._filter == null || this._filter == TokenFilter.INCLUDE_ALL)) {
            this._filter.filterFinishObject();
        }
        return this._parent;
    }

    public void skipParentChecks() {
        this._filter = null;
        for (TokenFilterContext ctxt = this._parent; ctxt != null; ctxt = ctxt._parent) {
            this._parent._filter = null;
        }
    }

    public Object getCurrentValue() {
        return null;
    }

    public void setCurrentValue(Object v) {
    }

    public final TokenFilterContext getParent() {
        return this._parent;
    }

    public final String getCurrentName() {
        return this._currentName;
    }

    public TokenFilter getFilter() {
        return this._filter;
    }

    public boolean isStartHandled() {
        return this._startHandled;
    }

    public JsonToken nextTokenToRead() {
        if (!this._startHandled) {
            this._startHandled = true;
            if (this._type == 2) {
                return JsonToken.START_OBJECT;
            }
            return JsonToken.START_ARRAY;
        } else if (!this._needToHandleName || this._type != 2) {
            return null;
        } else {
            this._needToHandleName = false;
            return JsonToken.FIELD_NAME;
        }
    }

    public TokenFilterContext findChildOf(TokenFilterContext parent) {
        if (this._parent == parent) {
            return this;
        }
        TokenFilterContext curr = this._parent;
        while (curr != null) {
            TokenFilterContext p = curr._parent;
            if (p == parent) {
                return curr;
            }
            curr = p;
        }
        return null;
    }

    protected void appendDesc(StringBuilder sb) {
        if (this._parent != null) {
            this._parent.appendDesc(sb);
        }
        if (this._type == 2) {
            sb.append('{');
            if (this._currentName != null) {
                sb.append('\"');
                sb.append(this._currentName);
                sb.append('\"');
            } else {
                sb.append('?');
            }
            sb.append('}');
        } else if (this._type == 1) {
            sb.append('[');
            sb.append(getCurrentIndex());
            sb.append(']');
        } else {
            sb.append("/");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        appendDesc(sb);
        return sb.toString();
    }
}
