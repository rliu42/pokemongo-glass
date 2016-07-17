package com.fasterxml.jackson.core.sym;

import spacemadness.com.lunarconsole.BuildConfig;

public final class Name1 extends Name {
    private static final Name1 EMPTY;
    private final int f27q;

    static {
        EMPTY = new Name1(BuildConfig.FLAVOR, 0, 0);
    }

    Name1(String name, int hash, int quad) {
        super(name, hash);
        this.f27q = quad;
    }

    public static Name1 getEmptyName() {
        return EMPTY;
    }

    public boolean equals(int quad) {
        return quad == this.f27q;
    }

    public boolean equals(int quad1, int quad2) {
        return quad1 == this.f27q && quad2 == 0;
    }

    public boolean equals(int q1, int q2, int q3) {
        return false;
    }

    public boolean equals(int[] quads, int qlen) {
        return qlen == 1 && quads[0] == this.f27q;
    }
}
