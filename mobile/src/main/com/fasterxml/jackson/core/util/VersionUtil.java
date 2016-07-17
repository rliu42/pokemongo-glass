package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

public class VersionUtil {
    private static final Pattern V_SEP;
    private final Version _v;

    static {
        V_SEP = Pattern.compile("[-_./;:]");
    }

    protected VersionUtil() {
        Version v = null;
        try {
            v = versionFor(getClass());
        } catch (Exception e) {
            System.err.println("ERROR: Failed to load Version information from " + getClass());
        }
        if (v == null) {
            v = Version.unknownVersion();
        }
        this._v = v;
    }

    public Version version() {
        return this._v;
    }

    public static Version versionFor(Class<?> cls) {
        return packageVersionFor(cls);
    }

    public static Version packageVersionFor(Class<?> cls) {
        Class<?> vClass;
        try {
            vClass = Class.forName(cls.getPackage().getName() + ".PackageVersion", true, cls.getClassLoader());
            return ((Versioned) vClass.newInstance()).version();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get Versioned out of " + vClass);
        } catch (Exception e2) {
            return null;
        }
    }

    @Deprecated
    public static Version mavenVersionFor(ClassLoader cl, String groupId, String artifactId) {
        InputStream pomProperties = cl.getResourceAsStream("META-INF/maven/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/pom.properties");
        if (pomProperties != null) {
            Version parseVersion;
            try {
                Properties props = new Properties();
                props.load(pomProperties);
                parseVersion = parseVersion(props.getProperty("version"), props.getProperty("groupId"), props.getProperty("artifactId"));
                return parseVersion;
            } catch (IOException e) {
                parseVersion = e;
            } finally {
                _close(pomProperties);
            }
        }
        return Version.unknownVersion();
    }

    public static Version parseVersion(String s, String groupId, String artifactId) {
        String str = null;
        int i = 0;
        if (s != null) {
            s = s.trim();
            if (s.length() > 0) {
                int parseVersionPart;
                String[] parts = V_SEP.split(s);
                int parseVersionPart2 = parseVersionPart(parts[0]);
                if (parts.length > 1) {
                    parseVersionPart = parseVersionPart(parts[1]);
                } else {
                    parseVersionPart = 0;
                }
                if (parts.length > 2) {
                    i = parseVersionPart(parts[2]);
                }
                if (parts.length > 3) {
                    str = parts[3];
                }
                return new Version(parseVersionPart2, parseVersionPart, i, str, groupId, artifactId);
            }
        }
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static int parseVersionPart(java.lang.String r6) {
        /*
        r3 = 0;
        r1 = 0;
        r2 = r6.length();
    L_0x0006:
        if (r1 >= r2) goto L_0x0014;
    L_0x0008:
        r0 = r6.charAt(r1);
        r4 = 57;
        if (r0 > r4) goto L_0x0014;
    L_0x0010:
        r4 = 48;
        if (r0 >= r4) goto L_0x0015;
    L_0x0014:
        return r3;
    L_0x0015:
        r4 = r3 * 10;
        r5 = r0 + -48;
        r3 = r4 + r5;
        r1 = r1 + 1;
        goto L_0x0006;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.util.VersionUtil.parseVersionPart(java.lang.String):int");
    }

    private static final void _close(Closeable c) {
        try {
            c.close();
        } catch (IOException e) {
        }
    }

    public static final void throwInternal() {
        throw new RuntimeException("Internal error: this code path should never get executed");
    }
}
