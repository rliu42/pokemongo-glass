package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.nianticproject.holoholo.sfida.constants.BluetoothGattSupport;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import java.io.IOException;
import spacemadness.com.lunarconsole.C1391R;

/* compiled from: JsonNodeDeserializer */
abstract class BaseNodeDeserializer<T extends JsonNode> extends StdDeserializer<T> {
    public BaseNodeDeserializer(Class<T> vc) {
        super((Class) vc);
    }

    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromAny(p, ctxt);
    }

    public boolean isCachable() {
        return true;
    }

    protected void _reportProblem(JsonParser p, String msg) throws JsonMappingException {
        throw new JsonMappingException(msg, p.getTokenLocation());
    }

    protected void _handleDuplicateField(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory, String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue) throws JsonProcessingException {
        if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)) {
            _reportProblem(p, "Duplicate field '" + fieldName + "' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled");
        }
    }

    protected final ObjectNode deserializeObject(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
        String key;
        ObjectNode node = nodeFactory.objectNode();
        if (p.isExpectedStartObjectToken()) {
            key = p.nextFieldName();
        } else {
            JsonToken t = p.getCurrentToken();
            if (t != JsonToken.END_OBJECT) {
                if (t != JsonToken.FIELD_NAME) {
                    throw ctxt.mappingException(handledType(), p.getCurrentToken());
                }
                key = p.getCurrentName();
            }
            return node;
        }
        while (key != null) {
            JsonNode value;
            switch (p.nextToken().id()) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    value = deserializeObject(p, ctxt, nodeFactory);
                    break;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    value = deserializeArray(p, ctxt, nodeFactory);
                    break;
                case Place.TYPE_ATM /*6*/:
                    value = nodeFactory.textNode(p.getText());
                    break;
                case Place.TYPE_BAKERY /*7*/:
                    value = _fromInt(p, ctxt, nodeFactory);
                    break;
                case Place.TYPE_BAR /*9*/:
                    value = nodeFactory.booleanNode(true);
                    break;
                case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                    value = nodeFactory.booleanNode(false);
                    break;
                case Place.TYPE_BICYCLE_STORE /*11*/:
                    value = nodeFactory.nullNode();
                    break;
                case Place.TYPE_BOOK_STORE /*12*/:
                    value = _fromEmbedded(p, ctxt, nodeFactory);
                    break;
                default:
                    value = deserializeAny(p, ctxt, nodeFactory);
                    break;
            }
            JsonNode old = node.replace(key, value);
            if (old != null) {
                _handleDuplicateField(p, ctxt, nodeFactory, key, node, old, value);
            }
            key = p.nextFieldName();
        }
        return node;
    }

    protected final ArrayNode deserializeArray(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
        ArrayNode node = nodeFactory.arrayNode();
        while (true) {
            JsonToken t = p.nextToken();
            if (t == null) {
                throw ctxt.mappingException("Unexpected end-of-input when binding data into ArrayNode");
            }
            switch (t.id()) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    node.add(deserializeObject(p, ctxt, nodeFactory));
                    continue;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    node.add(deserializeArray(p, ctxt, nodeFactory));
                    continue;
                case Place.TYPE_AQUARIUM /*4*/:
                    return node;
                case Place.TYPE_ATM /*6*/:
                    break;
                case Place.TYPE_BAKERY /*7*/:
                    node.add(_fromInt(p, ctxt, nodeFactory));
                    continue;
                case Place.TYPE_BAR /*9*/:
                    node.add(nodeFactory.booleanNode(true));
                    continue;
                case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                    node.add(nodeFactory.booleanNode(false));
                    continue;
                case Place.TYPE_BICYCLE_STORE /*11*/:
                    node.add(nodeFactory.nullNode());
                    continue;
                case Place.TYPE_BOOK_STORE /*12*/:
                    node.add(_fromEmbedded(p, ctxt, nodeFactory));
                    break;
                default:
                    node.add(deserializeAny(p, ctxt, nodeFactory));
                    continue;
            }
            node.add(nodeFactory.textNode(p.getText()));
        }
    }

    protected final JsonNode deserializeAny(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
        switch (p.getCurrentTokenId()) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                return deserializeObject(p, ctxt, nodeFactory);
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                return deserializeArray(p, ctxt, nodeFactory);
            case Place.TYPE_ART_GALLERY /*5*/:
                return deserializeObject(p, ctxt, nodeFactory);
            case Place.TYPE_ATM /*6*/:
                return nodeFactory.textNode(p.getText());
            case Place.TYPE_BAKERY /*7*/:
                return _fromInt(p, ctxt, nodeFactory);
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                return _fromFloat(p, ctxt, nodeFactory);
            case Place.TYPE_BAR /*9*/:
                return nodeFactory.booleanNode(true);
            case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                return nodeFactory.booleanNode(false);
            case Place.TYPE_BICYCLE_STORE /*11*/:
                return nodeFactory.nullNode();
            case Place.TYPE_BOOK_STORE /*12*/:
                return _fromEmbedded(p, ctxt, nodeFactory);
            default:
                throw ctxt.mappingException(handledType());
        }
    }

    protected final JsonNode _fromInt(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
        NumberType nt;
        int feats = ctxt.getDeserializationFeatures();
        if ((F_MASK_INT_COERCIONS & feats) == 0) {
            nt = p.getNumberType();
        } else if (DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.enabledIn(feats)) {
            nt = NumberType.BIG_INTEGER;
        } else if (DeserializationFeature.USE_LONG_FOR_INTS.enabledIn(feats)) {
            nt = NumberType.LONG;
        } else {
            nt = p.getNumberType();
        }
        if (nt == NumberType.INT) {
            return nodeFactory.numberNode(p.getIntValue());
        }
        if (nt == NumberType.LONG) {
            return nodeFactory.numberNode(p.getLongValue());
        }
        return nodeFactory.numberNode(p.getBigIntegerValue());
    }

    protected final JsonNode _fromFloat(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
        if (p.getNumberType() == NumberType.BIG_DECIMAL || ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
            return nodeFactory.numberNode(p.getDecimalValue());
        }
        return nodeFactory.numberNode(p.getDoubleValue());
    }

    protected final JsonNode _fromEmbedded(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory) throws IOException {
        Object ob = p.getEmbeddedObject();
        if (ob == null) {
            return nodeFactory.nullNode();
        }
        if (ob.getClass() == byte[].class) {
            return nodeFactory.binaryNode((byte[]) ob);
        }
        if (ob instanceof RawValue) {
            return nodeFactory.rawValueNode((RawValue) ob);
        }
        if (ob instanceof JsonNode) {
            return (JsonNode) ob;
        }
        return nodeFactory.pojoNode(ob);
    }
}
