package rx.internal.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

public final class PlatformDependent {
    private static final boolean IS_ANDROID;

    /* renamed from: rx.internal.util.PlatformDependent.1 */
    static class C13411 implements PrivilegedAction<ClassLoader> {
        C13411() {
        }

        public ClassLoader run() {
            return ClassLoader.getSystemClassLoader();
        }
    }

    static {
        IS_ANDROID = isAndroid0();
    }

    public static boolean isAndroid() {
        return IS_ANDROID;
    }

    private static boolean isAndroid0() {
        try {
            Class.forName("android.app.Application", false, getSystemClassLoader());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new C13411());
    }
}
