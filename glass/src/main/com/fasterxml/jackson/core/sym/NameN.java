package com.fasterxml.jackson.core.sym;

import java.util.Arrays;

public final class NameN extends Name {
    private final int[] f28q;
    private final int q1;
    private final int q2;
    private final int q3;
    private final int q4;
    private final int qlen;

    NameN(String name, int hash, int q1, int q2, int q3, int q4, int[] quads, int quadLen) {
        super(name, hash);
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
        this.f28q = quads;
        this.qlen = quadLen;
    }

    public static NameN construct(String name, int hash, int[] q, int qlen) {
        if (qlen < 4) {
            throw new IllegalArgumentException();
        }
        int[] buf;
        int q1 = q[0];
        int q2 = q[1];
        int q3 = q[2];
        int q4 = q[3];
        if (qlen - 4 > 0) {
            buf = Arrays.copyOfRange(q, 4, qlen);
        } else {
            buf = null;
        }
        return new NameN(name, hash, q1, q2, q3, q4, buf, qlen);
    }

    public boolean equals(int quad) {
        return false;
    }

    public boolean equals(int quad1, int quad2) {
        return false;
    }

    public boolean equals(int quad1, int quad2, int quad3) {
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(int[] r7, int r8) {
        /*
        r6 = this;
        r5 = 3;
        r4 = 2;
        r1 = 1;
        r0 = 0;
        r2 = r6.qlen;
        if (r8 == r2) goto L_0x0009;
    L_0x0008:
        return r0;
    L_0x0009:
        r2 = r7[r0];
        r3 = r6.q1;
        if (r2 != r3) goto L_0x0008;
    L_0x000f:
        r2 = r7[r1];
        r3 = r6.q2;
        if (r2 != r3) goto L_0x0008;
    L_0x0015:
        r2 = r7[r4];
        r3 = r6.q3;
        if (r2 != r3) goto L_0x0008;
    L_0x001b:
        r2 = r7[r5];
        r3 = r6.q4;
        if (r2 != r3) goto L_0x0008;
    L_0x0021:
        switch(r8) {
            case 4: goto L_0x004d;
            case 5: goto L_0x0044;
            case 6: goto L_0x003b;
            case 7: goto L_0x0032;
            case 8: goto L_0x0029;
            default: goto L_0x0024;
        };
    L_0x0024:
        r0 = r6._equals2(r7);
        goto L_0x0008;
    L_0x0029:
        r2 = 7;
        r2 = r7[r2];
        r3 = r6.f28q;
        r3 = r3[r5];
        if (r2 != r3) goto L_0x0008;
    L_0x0032:
        r2 = 6;
        r2 = r7[r2];
        r3 = r6.f28q;
        r3 = r3[r4];
        if (r2 != r3) goto L_0x0008;
    L_0x003b:
        r2 = 5;
        r2 = r7[r2];
        r3 = r6.f28q;
        r3 = r3[r1];
        if (r2 != r3) goto L_0x0008;
    L_0x0044:
        r2 = 4;
        r2 = r7[r2];
        r3 = r6.f28q;
        r3 = r3[r0];
        if (r2 != r3) goto L_0x0008;
    L_0x004d:
        r0 = r1;
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.sym.NameN.equals(int[], int):boolean");
    }

    private final boolean _equals2(int[] quads) {
        int end = this.qlen - 4;
        for (int i = 0; i < end; i++) {
            if (quads[i + 4] != this.f28q[i]) {
                return false;
            }
        }
        return true;
    }
}
