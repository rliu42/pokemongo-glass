package crittercism.android;

import android.location.Location;

public final class bc {
    private static Location f405a;

    public static synchronized void m449a(Location location) {
        synchronized (bc.class) {
            if (location != null) {
                location = new Location(location);
            }
            f405a = location;
        }
    }

    public static synchronized Location m448a() {
        Location location;
        synchronized (bc.class) {
            location = f405a;
        }
        return location;
    }

    public static synchronized boolean m450b() {
        boolean z;
        synchronized (bc.class) {
            z = f405a != null;
        }
        return z;
    }
}
