package com.fasterxml.jackson.core.sym;

public final class Name3 extends Name {
    private final int q1;
    private final int q2;
    private final int q3;

    Name3(String name, int hash, int i1, int i2, int i3) {
        super(name, hash);
        this.q1 = i1;
        this.q2 = i2;
        this.q3 = i3;
    }

    public boolean equals(int quad) {
        return false;
    }

    public boolean equals(int quad1, int quad2) {
        return false;
    }

    public boolean equals(int quad1, int quad2, int quad3) {
        return this.q1 == quad1 && this.q2 == quad2 && this.q3 == quad3;
    }

    public boolean equals(int[] quads, int qlen) {
        return qlen == 3 && quads[0] == this.q1 && quads[1] == this.q2 && quads[2] == this.q3;
    }
}
