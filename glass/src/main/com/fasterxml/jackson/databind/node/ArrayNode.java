package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.RawValue;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ArrayNode extends ContainerNode<ArrayNode> {
    private final List<JsonNode> _children;

    public ArrayNode(JsonNodeFactory nc) {
        super(nc);
        this._children = new ArrayList();
    }

    protected JsonNode _at(JsonPointer ptr) {
        return get(ptr.getMatchingIndex());
    }

    public ArrayNode deepCopy() {
        ArrayNode ret = new ArrayNode(this._nodeFactory);
        for (JsonNode element : this._children) {
            ret._children.add(element.deepCopy());
        }
        return ret;
    }

    public boolean isEmpty(SerializerProvider serializers) {
        return this._children.isEmpty();
    }

    public JsonNodeType getNodeType() {
        return JsonNodeType.ARRAY;
    }

    public JsonToken asToken() {
        return JsonToken.START_ARRAY;
    }

    public int size() {
        return this._children.size();
    }

    public Iterator<JsonNode> elements() {
        return this._children.iterator();
    }

    public JsonNode get(int index) {
        if (index < 0 || index >= this._children.size()) {
            return null;
        }
        return (JsonNode) this._children.get(index);
    }

    public JsonNode get(String fieldName) {
        return null;
    }

    public JsonNode path(String fieldName) {
        return MissingNode.getInstance();
    }

    public JsonNode path(int index) {
        if (index < 0 || index >= this._children.size()) {
            return MissingNode.getInstance();
        }
        return (JsonNode) this._children.get(index);
    }

    public boolean equals(Comparator<JsonNode> comparator, JsonNode o) {
        if (!(o instanceof ArrayNode)) {
            return false;
        }
        ArrayNode other = (ArrayNode) o;
        int len = this._children.size();
        if (other.size() != len) {
            return false;
        }
        List<JsonNode> l1 = this._children;
        List<JsonNode> l2 = other._children;
        for (int i = 0; i < len; i++) {
            if (comparator.compare(l1.get(i), l2.get(i)) != 0) {
                return false;
            }
        }
        return true;
    }

    public void serialize(JsonGenerator f, SerializerProvider provider) throws IOException {
        List<JsonNode> c = this._children;
        int size = c.size();
        f.writeStartArray(size);
        for (int i = 0; i < size; i++) {
            JsonNode n = (JsonNode) c.get(i);
            if (n instanceof BaseJsonNode) {
                ((BaseJsonNode) n).serialize(f, provider);
            } else {
                n.serialize(f, provider);
            }
        }
        f.writeEndArray();
    }

    public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForArray(this, jg);
        for (JsonNode n : this._children) {
            ((BaseJsonNode) n).serialize(jg, provider);
        }
        typeSer.writeTypeSuffixForArray(this, jg);
    }

    public JsonNode findValue(String fieldName) {
        for (JsonNode node : this._children) {
            JsonNode value = node.findValue(fieldName);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
        for (JsonNode node : this._children) {
            foundSoFar = node.findValues(fieldName, foundSoFar);
        }
        return foundSoFar;
    }

    public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        for (JsonNode node : this._children) {
            foundSoFar = node.findValuesAsText(fieldName, foundSoFar);
        }
        return foundSoFar;
    }

    public ObjectNode findParent(String fieldName) {
        for (JsonNode node : this._children) {
            JsonNode parent = node.findParent(fieldName);
            if (parent != null) {
                return (ObjectNode) parent;
            }
        }
        return null;
    }

    public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        for (JsonNode node : this._children) {
            foundSoFar = node.findParents(fieldName, foundSoFar);
        }
        return foundSoFar;
    }

    public JsonNode set(int index, JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        if (index >= 0 && index < this._children.size()) {
            return (JsonNode) this._children.set(index, value);
        }
        throw new IndexOutOfBoundsException("Illegal index " + index + ", array size " + size());
    }

    public ArrayNode add(JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        _add(value);
        return this;
    }

    public ArrayNode addAll(ArrayNode other) {
        this._children.addAll(other._children);
        return this;
    }

    public ArrayNode addAll(Collection<? extends JsonNode> nodes) {
        this._children.addAll(nodes);
        return this;
    }

    public ArrayNode insert(int index, JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        _insert(index, value);
        return this;
    }

    public JsonNode remove(int index) {
        if (index < 0 || index >= this._children.size()) {
            return null;
        }
        return (JsonNode) this._children.remove(index);
    }

    public ArrayNode removeAll() {
        this._children.clear();
        return this;
    }

    public ArrayNode addArray() {
        ArrayNode n = arrayNode();
        _add(n);
        return n;
    }

    public ObjectNode addObject() {
        ObjectNode n = objectNode();
        _add(n);
        return n;
    }

    public ArrayNode addPOJO(Object value) {
        if (value == null) {
            addNull();
        } else {
            _add(pojoNode(value));
        }
        return this;
    }

    public ArrayNode addRawValue(RawValue raw) {
        if (raw == null) {
            addNull();
        } else {
            _add(rawValueNode(raw));
        }
        return this;
    }

    public ArrayNode addNull() {
        _add(nullNode());
        return this;
    }

    public ArrayNode add(int v) {
        _add(numberNode(v));
        return this;
    }

    public ArrayNode add(Integer value) {
        if (value == null) {
            return addNull();
        }
        return _add(numberNode(value.intValue()));
    }

    public ArrayNode add(long v) {
        return _add(numberNode(v));
    }

    public ArrayNode add(Long value) {
        if (value == null) {
            return addNull();
        }
        return _add(numberNode(value.longValue()));
    }

    public ArrayNode add(float v) {
        return _add(numberNode(v));
    }

    public ArrayNode add(Float value) {
        if (value == null) {
            return addNull();
        }
        return _add(numberNode(value.floatValue()));
    }

    public ArrayNode add(double v) {
        return _add(numberNode(v));
    }

    public ArrayNode add(Double value) {
        if (value == null) {
            return addNull();
        }
        return _add(numberNode(value.doubleValue()));
    }

    public ArrayNode add(BigDecimal v) {
        if (v == null) {
            return addNull();
        }
        return _add(numberNode(v));
    }

    public ArrayNode add(String v) {
        if (v == null) {
            return addNull();
        }
        return _add(textNode(v));
    }

    public ArrayNode add(boolean v) {
        return _add(booleanNode(v));
    }

    public ArrayNode add(Boolean value) {
        if (value == null) {
            return addNull();
        }
        return _add(booleanNode(value.booleanValue()));
    }

    public ArrayNode add(byte[] v) {
        if (v == null) {
            return addNull();
        }
        return _add(binaryNode(v));
    }

    public ArrayNode insertArray(int index) {
        ArrayNode n = arrayNode();
        _insert(index, n);
        return n;
    }

    public ObjectNode insertObject(int index) {
        ObjectNode n = objectNode();
        _insert(index, n);
        return n;
    }

    public ArrayNode insertPOJO(int index, Object value) {
        if (value == null) {
            return insertNull(index);
        }
        return _insert(index, pojoNode(value));
    }

    public ArrayNode insertNull(int index) {
        _insert(index, nullNode());
        return this;
    }

    public ArrayNode insert(int index, int v) {
        _insert(index, numberNode(v));
        return this;
    }

    public ArrayNode insert(int index, Integer value) {
        if (value == null) {
            insertNull(index);
        } else {
            _insert(index, numberNode(value.intValue()));
        }
        return this;
    }

    public ArrayNode insert(int index, long v) {
        return _insert(index, numberNode(v));
    }

    public ArrayNode insert(int index, Long value) {
        if (value == null) {
            return insertNull(index);
        }
        return _insert(index, numberNode(value.longValue()));
    }

    public ArrayNode insert(int index, float v) {
        return _insert(index, numberNode(v));
    }

    public ArrayNode insert(int index, Float value) {
        if (value == null) {
            return insertNull(index);
        }
        return _insert(index, numberNode(value.floatValue()));
    }

    public ArrayNode insert(int index, double v) {
        return _insert(index, numberNode(v));
    }

    public ArrayNode insert(int index, Double value) {
        if (value == null) {
            return insertNull(index);
        }
        return _insert(index, numberNode(value.doubleValue()));
    }

    public ArrayNode insert(int index, BigDecimal v) {
        if (v == null) {
            return insertNull(index);
        }
        return _insert(index, numberNode(v));
    }

    public ArrayNode insert(int index, String v) {
        if (v == null) {
            return insertNull(index);
        }
        return _insert(index, textNode(v));
    }

    public ArrayNode insert(int index, boolean v) {
        return _insert(index, booleanNode(v));
    }

    public ArrayNode insert(int index, Boolean value) {
        if (value == null) {
            return insertNull(index);
        }
        return _insert(index, booleanNode(value.booleanValue()));
    }

    public ArrayNode insert(int index, byte[] v) {
        if (v == null) {
            return insertNull(index);
        }
        return _insert(index, binaryNode(v));
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof ArrayNode)) {
            return false;
        }
        return this._children.equals(((ArrayNode) o)._children);
    }

    protected boolean _childrenEqual(ArrayNode other) {
        return this._children.equals(other._children);
    }

    public int hashCode() {
        return this._children.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder((size() << 4) + 16);
        sb.append('[');
        int len = this._children.size();
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(((JsonNode) this._children.get(i)).toString());
        }
        sb.append(']');
        return sb.toString();
    }

    protected ArrayNode _add(JsonNode node) {
        this._children.add(node);
        return this;
    }

    protected ArrayNode _insert(int index, JsonNode node) {
        if (index < 0) {
            this._children.add(0, node);
        } else if (index >= this._children.size()) {
            this._children.add(node);
        } else {
            this._children.add(index, node);
        }
        return this;
    }
}
