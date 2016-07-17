package crittercism.android;

public final class cv {
    private long f697a;
    private long f698b;

    public cv(long j) {
        this.f697a = 0;
        this.f698b = j;
    }

    public final synchronized boolean m690a() {
        return System.nanoTime() - this.f697a > this.f698b;
    }

    public final synchronized void m691b() {
        this.f697a = System.nanoTime();
    }
}
