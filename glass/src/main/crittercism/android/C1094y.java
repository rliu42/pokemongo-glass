package crittercism.android;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Provider.Service;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;

/* renamed from: crittercism.android.y */
public final class C1094y extends Service {
    public static final String[] f884a;
    private C1068e f885b;
    private C1058d f886c;
    private Service f887d;

    static {
        f884a = new String[]{"Default", "SSL", "TLSv1.1", "TLSv1.2", "SSLv3", "TLSv1", "TLS"};
    }

    private C1094y(Service service, C1068e c1068e, C1058d c1058d) {
        super(service.getProvider(), service.getType(), service.getAlgorithm(), service.getClassName(), null, null);
        this.f885b = c1068e;
        this.f886c = c1058d;
        this.f887d = service;
    }

    private static C1094y m846a(Service service, C1068e c1068e, C1058d c1058d) {
        C1094y c1094y = new C1094y(service, c1068e, c1058d);
        try {
            Field[] fields = Service.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                fields[i].set(c1094y, fields[i].get(service));
            }
            return c1094y;
        } catch (Exception e) {
            return null;
        }
    }

    private static Provider m847a() {
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            if (instance != null) {
                return instance.getProvider();
            }
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean m848a(C1068e c1068e, C1058d c1058d) {
        int i = 0;
        if (!C1095z.m853a()) {
            return false;
        }
        Provider a = C1094y.m847a();
        if (a == null) {
            return false;
        }
        boolean z = false;
        while (i < f884a.length) {
            Service service = a.getService("SSLContext", f884a[i]);
            if (!(service == null || (service instanceof C1094y))) {
                C1094y a2 = C1094y.m846a(service, c1068e, c1058d);
                if (a2 != null) {
                    z |= a2.m849b();
                }
            }
            i++;
        }
        return z;
    }

    private boolean m849b() {
        Provider provider = getProvider();
        if (provider == null) {
            return false;
        }
        try {
            Method declaredMethod = Provider.class.getDeclaredMethod("putService", new Class[]{Service.class});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(provider, new Object[]{this});
            String str = "SSLContext.DummySSLAlgorithm";
            provider.put(str, getClassName());
            provider.remove(getType() + "." + getAlgorithm());
            provider.remove(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public final Object newInstance(Object constructorParameter) {
        Object newInstance = super.newInstance(constructorParameter);
        try {
            if (!(newInstance instanceof SSLContextSpi)) {
                return newInstance;
            }
            C1095z a = C1095z.m850a((SSLContextSpi) newInstance, this.f885b, this.f886c);
            if (a != null) {
                return a;
            }
            return newInstance;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return newInstance;
        }
    }
}
