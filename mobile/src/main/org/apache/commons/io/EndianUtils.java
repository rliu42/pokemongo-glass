package org.apache.commons.io;

import android.support.v4.view.MotionEventCompat;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EndianUtils {
    public static short swapShort(short value) {
        return (short) ((((value >> 0) & MotionEventCompat.ACTION_MASK) << 8) + (((value >> 8) & MotionEventCompat.ACTION_MASK) << 0));
    }

    public static int swapInteger(int value) {
        return (((((value >> 0) & MotionEventCompat.ACTION_MASK) << 24) + (((value >> 8) & MotionEventCompat.ACTION_MASK) << 16)) + (((value >> 16) & MotionEventCompat.ACTION_MASK) << 8)) + (((value >> 24) & MotionEventCompat.ACTION_MASK) << 0);
    }

    public static long swapLong(long value) {
        return (((((((((value >> 0) & 255) << 56) + (((value >> 8) & 255) << 48)) + (((value >> 16) & 255) << 40)) + (((value >> 24) & 255) << 32)) + (((value >> 32) & 255) << 24)) + (((value >> 40) & 255) << 16)) + (((value >> 48) & 255) << 8)) + (((value >> 56) & 255) << 0);
    }

    public static float swapFloat(float value) {
        return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(value)));
    }

    public static double swapDouble(double value) {
        return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(value)));
    }

    public static void writeSwappedShort(byte[] data, int offset, short value) {
        data[offset + 0] = (byte) ((value >> 0) & MotionEventCompat.ACTION_MASK);
        data[offset + 1] = (byte) ((value >> 8) & MotionEventCompat.ACTION_MASK);
    }

    public static short readSwappedShort(byte[] data, int offset) {
        return (short) (((data[offset + 0] & MotionEventCompat.ACTION_MASK) << 0) + ((data[offset + 1] & MotionEventCompat.ACTION_MASK) << 8));
    }

    public static int readSwappedUnsignedShort(byte[] data, int offset) {
        return ((data[offset + 0] & MotionEventCompat.ACTION_MASK) << 0) + ((data[offset + 1] & MotionEventCompat.ACTION_MASK) << 8);
    }

    public static void writeSwappedInteger(byte[] data, int offset, int value) {
        data[offset + 0] = (byte) ((value >> 0) & MotionEventCompat.ACTION_MASK);
        data[offset + 1] = (byte) ((value >> 8) & MotionEventCompat.ACTION_MASK);
        data[offset + 2] = (byte) ((value >> 16) & MotionEventCompat.ACTION_MASK);
        data[offset + 3] = (byte) ((value >> 24) & MotionEventCompat.ACTION_MASK);
    }

    public static int readSwappedInteger(byte[] data, int offset) {
        return ((((data[offset + 0] & MotionEventCompat.ACTION_MASK) << 0) + ((data[offset + 1] & MotionEventCompat.ACTION_MASK) << 8)) + ((data[offset + 2] & MotionEventCompat.ACTION_MASK) << 16)) + ((data[offset + 3] & MotionEventCompat.ACTION_MASK) << 24);
    }

    public static long readSwappedUnsignedInteger(byte[] data, int offset) {
        return (((long) (data[offset + 3] & MotionEventCompat.ACTION_MASK)) << 24) + (4294967295L & ((long) ((((data[offset + 0] & MotionEventCompat.ACTION_MASK) << 0) + ((data[offset + 1] & MotionEventCompat.ACTION_MASK) << 8)) + ((data[offset + 2] & MotionEventCompat.ACTION_MASK) << 16))));
    }

    public static void writeSwappedLong(byte[] data, int offset, long value) {
        data[offset + 0] = (byte) ((int) ((value >> null) & 255));
        data[offset + 1] = (byte) ((int) ((value >> 8) & 255));
        data[offset + 2] = (byte) ((int) ((value >> 16) & 255));
        data[offset + 3] = (byte) ((int) ((value >> 24) & 255));
        data[offset + 4] = (byte) ((int) ((value >> 32) & 255));
        data[offset + 5] = (byte) ((int) ((value >> 40) & 255));
        data[offset + 6] = (byte) ((int) ((value >> 48) & 255));
        data[offset + 7] = (byte) ((int) ((value >> 56) & 255));
    }

    public static long readSwappedLong(byte[] data, int offset) {
        return (((long) (((((data[offset + 4] & MotionEventCompat.ACTION_MASK) << 0) + ((data[offset + 5] & MotionEventCompat.ACTION_MASK) << 8)) + ((data[offset + 6] & MotionEventCompat.ACTION_MASK) << 16)) + ((data[offset + 7] & MotionEventCompat.ACTION_MASK) << 24))) << 32) + (4294967295L & ((long) (((((data[offset + 0] & MotionEventCompat.ACTION_MASK) << 0) + ((data[offset + 1] & MotionEventCompat.ACTION_MASK) << 8)) + ((data[offset + 2] & MotionEventCompat.ACTION_MASK) << 16)) + ((data[offset + 3] & MotionEventCompat.ACTION_MASK) << 24))));
    }

    public static void writeSwappedFloat(byte[] data, int offset, float value) {
        writeSwappedInteger(data, offset, Float.floatToIntBits(value));
    }

    public static float readSwappedFloat(byte[] data, int offset) {
        return Float.intBitsToFloat(readSwappedInteger(data, offset));
    }

    public static void writeSwappedDouble(byte[] data, int offset, double value) {
        writeSwappedLong(data, offset, Double.doubleToLongBits(value));
    }

    public static double readSwappedDouble(byte[] data, int offset) {
        return Double.longBitsToDouble(readSwappedLong(data, offset));
    }

    public static void writeSwappedShort(OutputStream output, short value) throws IOException {
        output.write((byte) ((value >> 0) & MotionEventCompat.ACTION_MASK));
        output.write((byte) ((value >> 8) & MotionEventCompat.ACTION_MASK));
    }

    public static short readSwappedShort(InputStream input) throws IOException {
        return (short) (((read(input) & MotionEventCompat.ACTION_MASK) << 0) + ((read(input) & MotionEventCompat.ACTION_MASK) << 8));
    }

    public static int readSwappedUnsignedShort(InputStream input) throws IOException {
        return ((read(input) & MotionEventCompat.ACTION_MASK) << 0) + ((read(input) & MotionEventCompat.ACTION_MASK) << 8);
    }

    public static void writeSwappedInteger(OutputStream output, int value) throws IOException {
        output.write((byte) ((value >> 0) & MotionEventCompat.ACTION_MASK));
        output.write((byte) ((value >> 8) & MotionEventCompat.ACTION_MASK));
        output.write((byte) ((value >> 16) & MotionEventCompat.ACTION_MASK));
        output.write((byte) ((value >> 24) & MotionEventCompat.ACTION_MASK));
    }

    public static int readSwappedInteger(InputStream input) throws IOException {
        return ((((read(input) & MotionEventCompat.ACTION_MASK) << 0) + ((read(input) & MotionEventCompat.ACTION_MASK) << 8)) + ((read(input) & MotionEventCompat.ACTION_MASK) << 16)) + ((read(input) & MotionEventCompat.ACTION_MASK) << 24);
    }

    public static long readSwappedUnsignedInteger(InputStream input) throws IOException {
        return (((long) (read(input) & MotionEventCompat.ACTION_MASK)) << 24) + (4294967295L & ((long) ((((read(input) & MotionEventCompat.ACTION_MASK) << 0) + ((read(input) & MotionEventCompat.ACTION_MASK) << 8)) + ((read(input) & MotionEventCompat.ACTION_MASK) << 16))));
    }

    public static void writeSwappedLong(OutputStream output, long value) throws IOException {
        output.write((byte) ((int) ((value >> null) & 255)));
        output.write((byte) ((int) ((value >> 8) & 255)));
        output.write((byte) ((int) ((value >> 16) & 255)));
        output.write((byte) ((int) ((value >> 24) & 255)));
        output.write((byte) ((int) ((value >> 32) & 255)));
        output.write((byte) ((int) ((value >> 40) & 255)));
        output.write((byte) ((int) ((value >> 48) & 255)));
        output.write((byte) ((int) ((value >> 56) & 255)));
    }

    public static long readSwappedLong(InputStream input) throws IOException {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) read(input);
        }
        return readSwappedLong(bytes, 0);
    }

    public static void writeSwappedFloat(OutputStream output, float value) throws IOException {
        writeSwappedInteger(output, Float.floatToIntBits(value));
    }

    public static float readSwappedFloat(InputStream input) throws IOException {
        return Float.intBitsToFloat(readSwappedInteger(input));
    }

    public static void writeSwappedDouble(OutputStream output, double value) throws IOException {
        writeSwappedLong(output, Double.doubleToLongBits(value));
    }

    public static double readSwappedDouble(InputStream input) throws IOException {
        return Double.longBitsToDouble(readSwappedLong(input));
    }

    private static int read(InputStream input) throws IOException {
        int value = input.read();
        if (-1 != value) {
            return value;
        }
        throw new EOFException("Unexpected EOF reached");
    }
}
