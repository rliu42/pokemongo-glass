package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParserSequence extends JsonParserDelegate {
    protected int _nextParser;
    protected final JsonParser[] _parsers;

    protected JsonParserSequence(JsonParser[] parsers) {
        super(parsers[0]);
        this._parsers = parsers;
        this._nextParser = 1;
    }

    public static JsonParserSequence createFlattened(JsonParser first, JsonParser second) {
        if ((first instanceof JsonParserSequence) || (second instanceof JsonParserSequence)) {
            ArrayList<JsonParser> p = new ArrayList();
            if (first instanceof JsonParserSequence) {
                ((JsonParserSequence) first).addFlattenedActiveParsers(p);
            } else {
                p.add(first);
            }
            if (second instanceof JsonParserSequence) {
                ((JsonParserSequence) second).addFlattenedActiveParsers(p);
            } else {
                p.add(second);
            }
            return new JsonParserSequence((JsonParser[]) p.toArray(new JsonParser[p.size()]));
        }
        return new JsonParserSequence(new JsonParser[]{first, second});
    }

    protected void addFlattenedActiveParsers(List<JsonParser> result) {
        int len = this._parsers.length;
        for (int i = this._nextParser - 1; i < len; i++) {
            JsonParser p = this._parsers[i];
            if (p instanceof JsonParserSequence) {
                ((JsonParserSequence) p).addFlattenedActiveParsers(result);
            } else {
                result.add(p);
            }
        }
    }

    public void close() throws IOException {
        do {
            this.delegate.close();
        } while (switchToNext());
    }

    public JsonToken nextToken() throws IOException, JsonParseException {
        JsonToken t = this.delegate.nextToken();
        if (t != null) {
            return t;
        }
        while (switchToNext()) {
            t = this.delegate.nextToken();
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    public int containedParsersCount() {
        return this._parsers.length;
    }

    protected boolean switchToNext() {
        if (this._nextParser >= this._parsers.length) {
            return false;
        }
        JsonParser[] jsonParserArr = this._parsers;
        int i = this._nextParser;
        this._nextParser = i + 1;
        this.delegate = jsonParserArr[i];
        return true;
    }
}
