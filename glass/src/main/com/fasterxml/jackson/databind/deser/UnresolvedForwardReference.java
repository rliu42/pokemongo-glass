package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public final class UnresolvedForwardReference extends JsonMappingException {
    private static final long serialVersionUID = 1;
    private ReadableObjectId _roid;
    private List<UnresolvedId> _unresolvedIds;

    public UnresolvedForwardReference(String msg, JsonLocation loc, ReadableObjectId roid) {
        super(msg, loc);
        this._roid = roid;
    }

    public UnresolvedForwardReference(String msg) {
        super(msg);
        this._unresolvedIds = new ArrayList();
    }

    public ReadableObjectId getRoid() {
        return this._roid;
    }

    public Object getUnresolvedId() {
        return this._roid.getKey().key;
    }

    public void addUnresolvedId(Object id, Class<?> type, JsonLocation where) {
        this._unresolvedIds.add(new UnresolvedId(id, type, where));
    }

    public List<UnresolvedId> getUnresolvedIds() {
        return this._unresolvedIds;
    }

    public String getMessage() {
        String msg = super.getMessage();
        if (this._unresolvedIds == null) {
            return msg;
        }
        StringBuilder sb = new StringBuilder(msg);
        Iterator<UnresolvedId> iterator = this._unresolvedIds.iterator();
        while (iterator.hasNext()) {
            sb.append(((UnresolvedId) iterator.next()).toString());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(FilenameUtils.EXTENSION_SEPARATOR);
        return sb.toString();
    }
}
