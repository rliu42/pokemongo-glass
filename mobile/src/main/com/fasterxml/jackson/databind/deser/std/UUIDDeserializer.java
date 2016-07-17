package com.fasterxml.jackson.databind.deser.std;

import android.support.v4.media.TransportMediator;
import android.support.v4.view.MotionEventCompat;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class UUIDDeserializer extends FromStringDeserializer<UUID> {
    static final int[] HEX_DIGITS;
    private static final long serialVersionUID = 1;

    static {
        int i;
        HEX_DIGITS = new int[TransportMediator.KEYCODE_MEDIA_PAUSE];
        Arrays.fill(HEX_DIGITS, -1);
        for (i = 0; i < 10; i++) {
            HEX_DIGITS[i + 48] = i;
        }
        for (i = 0; i < 6; i++) {
            HEX_DIGITS[i + 97] = i + 10;
            HEX_DIGITS[i + 65] = i + 10;
        }
    }

    public UUIDDeserializer() {
        super(UUID.class);
    }

    protected UUID _deserialize(String id, DeserializationContext ctxt) throws IOException {
        if (id.length() != 36) {
            if (id.length() == 24) {
                return _fromBytes(Base64Variants.getDefaultVariant().decode(id), ctxt);
            }
            _badFormat(id);
        }
        if (!(id.charAt(8) == '-' && id.charAt(13) == '-' && id.charAt(18) == '-' && id.charAt(23) == '-')) {
            _badFormat(id);
        }
        return new UUID((((long) intFromChars(id, 0)) << 32) + ((((long) shortFromChars(id, 9)) << 16) | ((long) shortFromChars(id, 14))), (((long) ((shortFromChars(id, 19) << 16) | shortFromChars(id, 24))) << 32) | ((((long) intFromChars(id, 28)) << 32) >>> 32));
    }

    protected UUID _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
        if (ob instanceof byte[]) {
            return _fromBytes((byte[]) ob, ctxt);
        }
        super._deserializeEmbedded(ob, ctxt);
        return null;
    }

    private void _badFormat(String uuidStr) {
        throw new NumberFormatException("UUID has to be represented by the standard 36-char representation");
    }

    static int intFromChars(String str, int index) {
        return (((byteFromChars(str, index) << 24) + (byteFromChars(str, index + 2) << 16)) + (byteFromChars(str, index + 4) << 8)) + byteFromChars(str, index + 6);
    }

    static int shortFromChars(String str, int index) {
        return (byteFromChars(str, index) << 8) + byteFromChars(str, index + 2);
    }

    static int byteFromChars(String str, int index) {
        char c1 = str.charAt(index);
        char c2 = str.charAt(index + 1);
        if (c1 <= '\u007f' && c2 <= '\u007f') {
            int hex = (HEX_DIGITS[c1] << 4) | HEX_DIGITS[c2];
            if (hex >= 0) {
                return hex;
            }
        }
        if (c1 > '\u007f' || HEX_DIGITS[c1] < 0) {
            return _badChar(str, index, c1);
        }
        return _badChar(str, index + 1, c2);
    }

    static int _badChar(String uuidStr, int index, char c) {
        throw new NumberFormatException("Non-hex character '" + c + "', not valid character for a UUID String" + "' (value 0x" + Integer.toHexString(c) + ") for UUID String \"" + uuidStr + "\"");
    }

    private UUID _fromBytes(byte[] bytes, DeserializationContext ctxt) throws IOException {
        if (bytes.length != 16) {
            ctxt.mappingException("Can only construct UUIDs from byte[16]; got %d bytes", Integer.valueOf(bytes.length));
        }
        return new UUID(_long(bytes, 0), _long(bytes, 8));
    }

    private static long _long(byte[] b, int offset) {
        return (((long) _int(b, offset)) << 32) | ((((long) _int(b, offset + 4)) << 32) >>> 32);
    }

    private static int _int(byte[] b, int offset) {
        return (((b[offset] << 24) | ((b[offset + 1] & MotionEventCompat.ACTION_MASK) << 16)) | ((b[offset + 2] & MotionEventCompat.ACTION_MASK) << 8)) | (b[offset + 3] & MotionEventCompat.ACTION_MASK);
    }
}
