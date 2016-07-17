package com.fasterxml.jackson.databind.util;

import android.support.v4.os.EnvironmentCompat;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.annotation.NoClass;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public final class ClassUtil {

    private static class EnumTypeLocator {
        static final EnumTypeLocator instance;
        private final Field enumMapTypeField;
        private final Field enumSetTypeField;

        static {
            instance = new EnumTypeLocator();
        }

        private EnumTypeLocator() {
            this.enumSetTypeField = locateField(EnumSet.class, "elementType", Class.class);
            this.enumMapTypeField = locateField(EnumMap.class, "elementType", Class.class);
        }

        public Class<? extends Enum<?>> enumTypeFor(EnumSet<?> set) {
            if (this.enumSetTypeField != null) {
                return (Class) get(set, this.enumSetTypeField);
            }
            throw new IllegalStateException("Can not figure out type for EnumSet (odd JDK platform?)");
        }

        public Class<? extends Enum<?>> enumTypeFor(EnumMap<?, ?> set) {
            if (this.enumMapTypeField != null) {
                return (Class) get(set, this.enumMapTypeField);
            }
            throw new IllegalStateException("Can not figure out type for EnumMap (odd JDK platform?)");
        }

        private Object get(Object bean, Field field) {
            try {
                return field.get(bean);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        private static Field locateField(Class<?> fromClass, String expectedName, Class<?> type) {
            Field found = null;
            Field[] fields = fromClass.getDeclaredFields();
            for (Field f : fields) {
                if (expectedName.equals(f.getName()) && f.getType() == type) {
                    found = f;
                    break;
                }
            }
            if (found == null) {
                for (Field f2 : fields) {
                    if (f2.getType() == type) {
                        if (found != null) {
                            return null;
                        }
                        found = f2;
                    }
                }
            }
            if (found != null) {
                try {
                    found.setAccessible(true);
                } catch (Throwable th) {
                }
            }
            return found;
        }
    }

    public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore) {
        return findSuperTypes(cls, endBefore, new ArrayList(8));
    }

    public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore, List<Class<?>> result) {
        _addSuperTypes(cls, endBefore, result, false);
        return result;
    }

    private static void _addSuperTypes(Class<?> cls, Class<?> endBefore, Collection<Class<?>> result, boolean addClassItself) {
        if (cls != endBefore && cls != null && cls != Object.class) {
            if (addClassItself) {
                if (!result.contains(cls)) {
                    result.add(cls);
                } else {
                    return;
                }
            }
            for (Class<?> intCls : cls.getInterfaces()) {
                _addSuperTypes(intCls, endBefore, result, true);
            }
            _addSuperTypes(cls.getSuperclass(), endBefore, result, true);
        }
    }

    public static String canBeABeanType(Class<?> type) {
        if (type.isAnnotation()) {
            return "annotation";
        }
        if (type.isArray()) {
            return "array";
        }
        if (type.isEnum()) {
            return "enum";
        }
        if (type.isPrimitive()) {
            return "primitive";
        }
        return null;
    }

    public static String isLocalType(Class<?> type, boolean allowNonStatic) {
        try {
            if (type.getEnclosingMethod() != null) {
                return "local/anonymous";
            }
            if (!(allowNonStatic || type.getEnclosingClass() == null || Modifier.isStatic(type.getModifiers()))) {
                return "non-static member class";
            }
            return null;
        } catch (SecurityException e) {
        } catch (NullPointerException e2) {
        }
    }

    public static Class<?> getOuterClass(Class<?> type) {
        Class<?> cls = null;
        try {
            if (type.getEnclosingMethod() == null && !Modifier.isStatic(type.getModifiers())) {
                cls = type.getEnclosingClass();
            }
        } catch (SecurityException e) {
        } catch (NullPointerException e2) {
        }
        return cls;
    }

    public static boolean isProxyType(Class<?> type) {
        String name = type.getName();
        if (name.startsWith("net.sf.cglib.proxy.") || name.startsWith("org.hibernate.proxy.")) {
            return true;
        }
        return false;
    }

    public static boolean isConcrete(Class<?> type) {
        return (type.getModifiers() & 1536) == 0;
    }

    public static boolean isConcrete(Member member) {
        return (member.getModifiers() & 1536) == 0;
    }

    public static boolean isCollectionMapOrArray(Class<?> type) {
        if (type.isArray() || Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    public static String getClassDescription(Object classOrInstance) {
        if (classOrInstance == null) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return (classOrInstance instanceof Class ? (Class) classOrInstance : classOrInstance.getClass()).getName();
    }

    @Deprecated
    public static Class<?> findClass(String className) throws ClassNotFoundException {
        if (className.indexOf(46) < 0) {
            if ("int".equals(className)) {
                return Integer.TYPE;
            }
            if ("long".equals(className)) {
                return Long.TYPE;
            }
            if ("float".equals(className)) {
                return Float.TYPE;
            }
            if ("double".equals(className)) {
                return Double.TYPE;
            }
            if ("boolean".equals(className)) {
                return Boolean.TYPE;
            }
            if ("byte".equals(className)) {
                return Byte.TYPE;
            }
            if ("char".equals(className)) {
                return Character.TYPE;
            }
            if ("short".equals(className)) {
                return Short.TYPE;
            }
            if ("void".equals(className)) {
                return Void.TYPE;
            }
        }
        Throwable prob = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            try {
                return Class.forName(className, true, loader);
            } catch (Exception e) {
                prob = getRootCause(e);
            }
        }
        try {
            return Class.forName(className);
        } catch (Exception e2) {
            if (prob == null) {
                prob = getRootCause(e2);
            }
            if (prob instanceof RuntimeException) {
                throw ((RuntimeException) prob);
            }
            throw new ClassNotFoundException(prob.getMessage(), prob);
        }
    }

    @Deprecated
    public static boolean hasGetterSignature(Method m) {
        if (Modifier.isStatic(m.getModifiers())) {
            return false;
        }
        Class<?>[] pts = m.getParameterTypes();
        if ((pts == null || pts.length == 0) && Void.TYPE != m.getReturnType()) {
            return true;
        }
        return false;
    }

    public static Throwable getRootCause(Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }

    public static void throwRootCause(Throwable t) throws Exception {
        t = getRootCause(t);
        if (t instanceof Exception) {
            throw ((Exception) t);
        }
        throw ((Error) t);
    }

    public static void throwAsIAE(Throwable t) {
        throwAsIAE(t, t.getMessage());
    }

    public static void throwAsIAE(Throwable t, String msg) {
        if (t instanceof RuntimeException) {
            throw ((RuntimeException) t);
        } else if (t instanceof Error) {
            throw ((Error) t);
        } else {
            throw new IllegalArgumentException(msg, t);
        }
    }

    public static void unwrapAndThrowAsIAE(Throwable t) {
        throwAsIAE(getRootCause(t));
    }

    public static void unwrapAndThrowAsIAE(Throwable t, String msg) {
        throwAsIAE(getRootCause(t), msg);
    }

    public static <T> T createInstance(Class<T> cls, boolean canFixAccess) throws IllegalArgumentException {
        Constructor<T> ctor = findConstructor(cls, canFixAccess);
        if (ctor == null) {
            throw new IllegalArgumentException("Class " + cls.getName() + " has no default (no arg) constructor");
        }
        try {
            return ctor.newInstance(new Object[0]);
        } catch (Exception e) {
            unwrapAndThrowAsIAE(e, "Failed to instantiate class " + cls.getName() + ", problem: " + e.getMessage());
            return null;
        }
    }

    public static <T> Constructor<T> findConstructor(Class<T> cls, boolean canFixAccess) throws IllegalArgumentException {
        try {
            Constructor<T> ctor = cls.getDeclaredConstructor(new Class[0]);
            if (canFixAccess) {
                checkAndFixAccess(ctor);
                return ctor;
            } else if (Modifier.isPublic(ctor.getModifiers())) {
                return ctor;
            } else {
                throw new IllegalArgumentException("Default constructor for " + cls.getName() + " is not accessible (non-public?): not allowed to try modify access via Reflection: can not instantiate type");
            }
        } catch (NoSuchMethodException e) {
            return null;
        } catch (Exception e2) {
            unwrapAndThrowAsIAE(e2, "Failed to find default constructor of class " + cls.getName() + ", problem: " + e2.getMessage());
            return null;
        }
    }

    public static Object defaultValue(Class<?> cls) {
        if (cls == Integer.TYPE) {
            return Integer.valueOf(0);
        }
        if (cls == Long.TYPE) {
            return Long.valueOf(0);
        }
        if (cls == Boolean.TYPE) {
            return Boolean.FALSE;
        }
        if (cls == Double.TYPE) {
            return Double.valueOf(0.0d);
        }
        if (cls == Float.TYPE) {
            return Float.valueOf(0.0f);
        }
        if (cls == Byte.TYPE) {
            return Byte.valueOf((byte) 0);
        }
        if (cls == Short.TYPE) {
            return Short.valueOf((short) 0);
        }
        if (cls == Character.TYPE) {
            return Character.valueOf('\u0000');
        }
        throw new IllegalArgumentException("Class " + cls.getName() + " is not a primitive type");
    }

    public static Class<?> wrapperType(Class<?> primitiveType) {
        if (primitiveType == Integer.TYPE) {
            return Integer.class;
        }
        if (primitiveType == Long.TYPE) {
            return Long.class;
        }
        if (primitiveType == Boolean.TYPE) {
            return Boolean.class;
        }
        if (primitiveType == Double.TYPE) {
            return Double.class;
        }
        if (primitiveType == Float.TYPE) {
            return Float.class;
        }
        if (primitiveType == Byte.TYPE) {
            return Byte.class;
        }
        if (primitiveType == Short.TYPE) {
            return Short.class;
        }
        if (primitiveType == Character.TYPE) {
            return Character.class;
        }
        throw new IllegalArgumentException("Class " + primitiveType.getName() + " is not a primitive type");
    }

    public static void checkAndFixAccess(Member member) {
        AccessibleObject ao = (AccessibleObject) member;
        try {
            ao.setAccessible(true);
        } catch (SecurityException se) {
            if (!ao.isAccessible()) {
                throw new IllegalArgumentException("Can not access " + member + " (from class " + member.getDeclaringClass().getName() + "; failed to set access: " + se.getMessage());
            }
        }
    }

    public static Class<? extends Enum<?>> findEnumType(EnumSet<?> s) {
        if (s.isEmpty()) {
            return EnumTypeLocator.instance.enumTypeFor((EnumSet) s);
        }
        return findEnumType((Enum) s.iterator().next());
    }

    public static Class<? extends Enum<?>> findEnumType(EnumMap<?, ?> m) {
        if (m.isEmpty()) {
            return EnumTypeLocator.instance.enumTypeFor((EnumMap) m);
        }
        return findEnumType((Enum) m.keySet().iterator().next());
    }

    public static Class<? extends Enum<?>> findEnumType(Enum<?> en) {
        Class<?> ec = en.getClass();
        if (ec.getSuperclass() != Enum.class) {
            return ec.getSuperclass();
        }
        return ec;
    }

    public static Class<? extends Enum<?>> findEnumType(Class<?> cls) {
        if (cls.getSuperclass() != Enum.class) {
            return cls.getSuperclass();
        }
        return cls;
    }

    public static boolean isJacksonStdImpl(Object impl) {
        return impl != null && isJacksonStdImpl(impl.getClass());
    }

    public static boolean isJacksonStdImpl(Class<?> implClass) {
        return implClass.getAnnotation(JacksonStdImpl.class) != null;
    }

    public static boolean isBogusClass(Class<?> cls) {
        return cls == Void.class || cls == Void.TYPE || cls == NoClass.class;
    }

    public static boolean isNonStaticInnerClass(Class<?> cls) {
        return (cls.getEnclosingClass() == null || Modifier.isStatic(cls.getModifiers())) ? false : true;
    }
}
