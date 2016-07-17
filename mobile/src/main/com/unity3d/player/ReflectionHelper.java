package com.unity3d.player;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import spacemadness.com.lunarconsole.BuildConfig;

final class ReflectionHelper {
    protected static boolean LOG;
    protected static final boolean LOGV = false;
    private static C0805a[] f44a;

    /* renamed from: com.unity3d.player.ReflectionHelper.1 */
    static class C08041 implements InvocationHandler {
        final /* synthetic */ int f37a;
        final /* synthetic */ Class[] f38b;

        C08041(int i, Class[] clsArr) {
            this.f37a = i;
            this.f38b = clsArr;
        }

        protected final void finalize() {
            try {
                ReflectionHelper.nativeProxyFinalize(this.f37a);
            } finally {
                super.finalize();
            }
        }

        public final Object invoke(Object obj, Method method, Object[] objArr) {
            return ReflectionHelper.nativeProxyInvoke(this.f37a, method.getName(), objArr);
        }
    }

    /* renamed from: com.unity3d.player.ReflectionHelper.a */
    private static class C0805a {
        public volatile Member f39a;
        private final Class f40b;
        private final String f41c;
        private final String f42d;
        private final int f43e;

        C0805a(Class cls, String str, String str2) {
            this.f40b = cls;
            this.f41c = str;
            this.f42d = str2;
            this.f43e = ((((this.f40b.hashCode() + 527) * 31) + this.f41c.hashCode()) * 31) + this.f42d.hashCode();
        }

        public final boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof C0805a)) {
                return false;
            }
            C0805a c0805a = (C0805a) obj;
            return this.f43e == c0805a.f43e && this.f42d.equals(c0805a.f42d) && this.f41c.equals(c0805a.f41c) && this.f40b.equals(c0805a.f40b);
        }

        public final int hashCode() {
            return this.f43e;
        }
    }

    static {
        LOG = false;
        f44a = new C0805a[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
    }

    ReflectionHelper() {
    }

    private static float m38a(Class cls, Class cls2) {
        if (cls.equals(cls2)) {
            return 1.0f;
        }
        if (!(cls.isPrimitive() || cls2.isPrimitive())) {
            try {
                if (cls.asSubclass(cls2) != null) {
                    return 0.5f;
                }
            } catch (ClassCastException e) {
            }
            try {
                if (cls2.asSubclass(cls) != null) {
                    return 0.1f;
                }
            } catch (ClassCastException e2) {
            }
        }
        return 0.0f;
    }

    private static float m39a(Class cls, Class[] clsArr, Class[] clsArr2) {
        int i = 0;
        if (clsArr2.length == 0) {
            return 0.1f;
        }
        if ((clsArr == null ? 0 : clsArr.length) + 1 != clsArr2.length) {
            return 0.0f;
        }
        float f = 1.0f;
        if (clsArr != null) {
            int i2 = 0;
            while (i < clsArr.length) {
                f *= m38a(clsArr[i], clsArr2[i2]);
                i++;
                i2++;
            }
        }
        return f * m38a(cls, clsArr2[clsArr2.length - 1]);
    }

    private static Class m40a(String str, int[] iArr) {
        while (iArr[0] < str.length()) {
            int i = iArr[0];
            iArr[0] = i + 1;
            char charAt = str.charAt(i);
            if (charAt != '(' && charAt != ')') {
                if (charAt == 'L') {
                    i = str.indexOf(59, iArr[0]);
                    if (i != -1) {
                        String substring = str.substring(iArr[0], i);
                        iArr[0] = i + 1;
                        try {
                            return Class.forName(substring.replace(IOUtils.DIR_SEPARATOR_UNIX, FilenameUtils.EXTENSION_SEPARATOR));
                        } catch (ClassNotFoundException e) {
                        }
                    }
                } else if (charAt == 'Z') {
                    return Boolean.TYPE;
                } else {
                    if (charAt == 'I') {
                        return Integer.TYPE;
                    }
                    if (charAt == 'F') {
                        return Float.TYPE;
                    }
                    if (charAt == 'V') {
                        return Void.TYPE;
                    }
                    if (charAt == 'B') {
                        return Byte.TYPE;
                    }
                    if (charAt == 'S') {
                        return Short.TYPE;
                    }
                    if (charAt == 'J') {
                        return Long.TYPE;
                    }
                    if (charAt == 'D') {
                        return Double.TYPE;
                    }
                    if (charAt == '[') {
                        return Array.newInstance(m40a(str, iArr), 0).getClass();
                    }
                    C0843m.Log(5, "! parseType; " + charAt + " is not known!");
                }
                return null;
            }
        }
        return null;
    }

    private static void m43a(C0805a c0805a, Member member) {
        c0805a.f39a = member;
        f44a[c0805a.hashCode() & (f44a.length - 1)] = c0805a;
    }

    private static boolean m44a(C0805a c0805a) {
        C0805a c0805a2 = f44a[c0805a.hashCode() & (f44a.length - 1)];
        if (!c0805a.equals(c0805a2)) {
            return false;
        }
        c0805a.f39a = c0805a2.f39a;
        return true;
    }

    private static Class[] m45a(String str) {
        int[] iArr = new int[]{0};
        ArrayList arrayList = new ArrayList();
        while (iArr[0] < str.length()) {
            Class a = m40a(str, iArr);
            if (a == null) {
                break;
            }
            arrayList.add(a);
        }
        Class[] clsArr = new Class[arrayList.size()];
        Iterator it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            int i2 = i + 1;
            clsArr[i] = (Class) it.next();
            i = i2;
        }
        return clsArr;
    }

    protected static Constructor getConstructorID(Class cls, String str) {
        Member member = null;
        C0805a c0805a = new C0805a(cls, BuildConfig.FLAVOR, str);
        if (m44a(c0805a)) {
            Constructor constructor = (Constructor) c0805a.f39a;
        } else {
            Member member2;
            Class[] a = m45a(str);
            float f = 0.0f;
            Constructor[] constructors = cls.getConstructors();
            int length = constructors.length;
            int i = 0;
            while (i < length) {
                Member member3;
                member2 = constructors[i];
                float a2 = m39a(Void.TYPE, member2.getParameterTypes(), a);
                if (a2 > f) {
                    if (a2 == 1.0f) {
                        break;
                    }
                    member3 = member2;
                } else {
                    a2 = f;
                    member3 = member;
                }
                i++;
                member = member3;
                f = a2;
            }
            member2 = member;
            m43a(c0805a, member2);
            Member member4 = member2;
        }
        if (constructor != null) {
            return constructor;
        }
        throw new NoSuchMethodError("<init>" + str + " in class " + cls.getName());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static java.lang.reflect.Field getFieldID(java.lang.Class r11, java.lang.String r12, java.lang.String r13, boolean r14) {
        /*
        r1 = 0;
        r5 = new com.unity3d.player.ReflectionHelper$a;
        r5.<init>(r11, r12, r13);
        r0 = m44a(r5);
        if (r0 == 0) goto L_0x0035;
    L_0x000c:
        r0 = r5.f39a;
        r0 = (java.lang.reflect.Field) r0;
    L_0x0010:
        if (r0 != 0) goto L_0x00ab;
    L_0x0012:
        r1 = new java.lang.NoSuchFieldError;
        r2 = "no %s field with name='%s' signature='%s' in class L%s;";
        r0 = 4;
        r3 = new java.lang.Object[r0];
        r4 = 0;
        if (r14 == 0) goto L_0x00a7;
    L_0x001c:
        r0 = "non-static";
    L_0x001e:
        r3[r4] = r0;
        r0 = 1;
        r3[r0] = r12;
        r0 = 2;
        r3[r0] = r13;
        r0 = 3;
        r4 = r11.getName();
        r3[r0] = r4;
        r0 = java.lang.String.format(r2, r3);
        r1.<init>(r0);
        throw r1;
    L_0x0035:
        r6 = m45a(r13);
        r0 = 0;
        r10 = r0;
        r0 = r1;
        r1 = r10;
    L_0x003d:
        if (r11 == 0) goto L_0x00a2;
    L_0x003f:
        r7 = r11.getDeclaredFields();
        r8 = r7.length;
        r2 = 0;
        r4 = r2;
        r3 = r0;
    L_0x0047:
        if (r4 >= r8) goto L_0x00af;
    L_0x0049:
        r2 = r7[r4];
        r0 = r2.getModifiers();
        r0 = java.lang.reflect.Modifier.isStatic(r0);
        if (r14 != r0) goto L_0x00ac;
    L_0x0055:
        r0 = r2.getName();
        r0 = r0.compareTo(r12);
        if (r0 != 0) goto L_0x00ac;
    L_0x005f:
        r0 = r2.getType();
        r9 = 0;
        r0 = m39a(r0, r9, r6);
        r9 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r9 <= 0) goto L_0x00ac;
    L_0x006c:
        r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r1 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r1 == 0) goto L_0x0079;
    L_0x0072:
        r1 = r2;
    L_0x0073:
        r2 = r4 + 1;
        r4 = r2;
        r3 = r1;
        r1 = r0;
        goto L_0x0047;
    L_0x0079:
        r1 = r0;
        r0 = r2;
    L_0x007b:
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x00a2;
    L_0x0081:
        r2 = r11.isPrimitive();
        if (r2 != 0) goto L_0x00a2;
    L_0x0087:
        r2 = r11.isInterface();
        if (r2 != 0) goto L_0x00a2;
    L_0x008d:
        r2 = java.lang.Object.class;
        r2 = r11.equals(r2);
        if (r2 != 0) goto L_0x00a2;
    L_0x0095:
        r2 = java.lang.Void.TYPE;
        r2 = r11.equals(r2);
        if (r2 != 0) goto L_0x00a2;
    L_0x009d:
        r11 = r11.getSuperclass();
        goto L_0x003d;
    L_0x00a2:
        m43a(r5, r0);
        goto L_0x0010;
    L_0x00a7:
        r0 = "static";
        goto L_0x001e;
    L_0x00ab:
        return r0;
    L_0x00ac:
        r0 = r1;
        r1 = r3;
        goto L_0x0073;
    L_0x00af:
        r0 = r3;
        goto L_0x007b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.ReflectionHelper.getFieldID(java.lang.Class, java.lang.String, java.lang.String, boolean):java.lang.reflect.Field");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static java.lang.reflect.Method getMethodID(java.lang.Class r11, java.lang.String r12, java.lang.String r13, boolean r14) {
        /*
        r1 = 0;
        r5 = new com.unity3d.player.ReflectionHelper$a;
        r5.<init>(r11, r12, r13);
        r0 = m44a(r5);
        if (r0 == 0) goto L_0x0035;
    L_0x000c:
        r0 = r5.f39a;
        r0 = (java.lang.reflect.Method) r0;
    L_0x0010:
        if (r0 != 0) goto L_0x00ae;
    L_0x0012:
        r1 = new java.lang.NoSuchMethodError;
        r2 = "no %s method with name='%s' signature='%s' in class L%s;";
        r0 = 4;
        r3 = new java.lang.Object[r0];
        r4 = 0;
        if (r14 == 0) goto L_0x00aa;
    L_0x001c:
        r0 = "non-static";
    L_0x001e:
        r3[r4] = r0;
        r0 = 1;
        r3[r0] = r12;
        r0 = 2;
        r3[r0] = r13;
        r0 = 3;
        r4 = r11.getName();
        r3[r0] = r4;
        r0 = java.lang.String.format(r2, r3);
        r1.<init>(r0);
        throw r1;
    L_0x0035:
        r6 = m45a(r13);
        r0 = 0;
        r10 = r0;
        r0 = r1;
        r1 = r10;
    L_0x003d:
        if (r11 == 0) goto L_0x00a5;
    L_0x003f:
        r7 = r11.getDeclaredMethods();
        r8 = r7.length;
        r2 = 0;
        r4 = r2;
        r3 = r0;
    L_0x0047:
        if (r4 >= r8) goto L_0x00b2;
    L_0x0049:
        r2 = r7[r4];
        r0 = r2.getModifiers();
        r0 = java.lang.reflect.Modifier.isStatic(r0);
        if (r14 != r0) goto L_0x00af;
    L_0x0055:
        r0 = r2.getName();
        r0 = r0.compareTo(r12);
        if (r0 != 0) goto L_0x00af;
    L_0x005f:
        r0 = r2.getReturnType();
        r9 = r2.getParameterTypes();
        r0 = m39a(r0, r9, r6);
        r9 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r9 <= 0) goto L_0x00af;
    L_0x006f:
        r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r1 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r1 == 0) goto L_0x007c;
    L_0x0075:
        r1 = r2;
    L_0x0076:
        r2 = r4 + 1;
        r4 = r2;
        r3 = r1;
        r1 = r0;
        goto L_0x0047;
    L_0x007c:
        r1 = r0;
        r0 = r2;
    L_0x007e:
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x00a5;
    L_0x0084:
        r2 = r11.isPrimitive();
        if (r2 != 0) goto L_0x00a5;
    L_0x008a:
        r2 = r11.isInterface();
        if (r2 != 0) goto L_0x00a5;
    L_0x0090:
        r2 = java.lang.Object.class;
        r2 = r11.equals(r2);
        if (r2 != 0) goto L_0x00a5;
    L_0x0098:
        r2 = java.lang.Void.TYPE;
        r2 = r11.equals(r2);
        if (r2 != 0) goto L_0x00a5;
    L_0x00a0:
        r11 = r11.getSuperclass();
        goto L_0x003d;
    L_0x00a5:
        m43a(r5, r0);
        goto L_0x0010;
    L_0x00aa:
        r0 = "static";
        goto L_0x001e;
    L_0x00ae:
        return r0;
    L_0x00af:
        r0 = r1;
        r1 = r3;
        goto L_0x0076;
    L_0x00b2:
        r0 = r3;
        goto L_0x007e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.ReflectionHelper.getMethodID(java.lang.Class, java.lang.String, java.lang.String, boolean):java.lang.reflect.Method");
    }

    private static native void nativeProxyFinalize(int i);

    private static native Object nativeProxyInvoke(int i, String str, Object[] objArr);

    protected static Object newProxyInstance(int i, Class cls) {
        return newProxyInstance(i, new Class[]{cls});
    }

    protected static Object newProxyInstance(int i, Class[] clsArr) {
        return Proxy.newProxyInstance(ReflectionHelper.class.getClassLoader(), clsArr, new C08041(i, clsArr));
    }
}
