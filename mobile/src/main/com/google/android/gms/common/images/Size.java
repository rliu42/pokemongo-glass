package com.google.android.gms.common.images;

public final class Size {
    private final int zznQ;
    private final int zznR;

    public Size(int width, int height) {
        this.zznQ = width;
        this.zznR = height;
    }

    public static Size parseSize(String string) throws NumberFormatException {
        if (string == null) {
            throw new IllegalArgumentException("string must not be null");
        }
        int indexOf = string.indexOf(42);
        if (indexOf < 0) {
            indexOf = string.indexOf(120);
        }
        if (indexOf < 0) {
            throw zzch(string);
        }
        try {
            return new Size(Integer.parseInt(string.substring(0, indexOf)), Integer.parseInt(string.substring(indexOf + 1)));
        } catch (NumberFormatException e) {
            throw zzch(string);
        }
    }

    private static NumberFormatException zzch(String str) {
        throw new NumberFormatException("Invalid Size: \"" + str + "\"");
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Size)) {
            return false;
        }
        Size size = (Size) obj;
        if (!(this.zznQ == size.zznQ && this.zznR == size.zznR)) {
            z = false;
        }
        return z;
    }

    public int getHeight() {
        return this.zznR;
    }

    public int getWidth() {
        return this.zznQ;
    }

    public int hashCode() {
        return this.zznR ^ ((this.zznQ << 16) | (this.zznQ >>> 16));
    }

    public String toString() {
        return this.zznQ + "x" + this.zznR;
    }
}
