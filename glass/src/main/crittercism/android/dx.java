package crittercism.android;

import android.util.Log;
import crittercism.android.ec.C10691;

public final class dx {
    public static C1066a f765a;
    private static ec f766b;

    /* renamed from: crittercism.android.dx.a */
    public enum C1066a {
        UNINITIALIZED,
        ON,
        OFF
    }

    static {
        f765a = C1066a.UNINITIALIZED;
    }

    public static void m749a(ec ecVar) {
        f766b = ecVar;
    }

    public static void m748a() {
    }

    public static void m753b() {
    }

    public static void m756c() {
    }

    public static void m750a(String str) {
        Log.i("Crittercism", str);
    }

    public static void m754b(String str) {
        Log.e("Crittercism", str);
    }

    public static void m751a(String str, Throwable th) {
        Log.e("Crittercism", str, th);
    }

    public static void m757c(String str) {
        Log.w("Crittercism", str);
    }

    public static void m755b(String str, Throwable th) {
        Log.w("Crittercism", str, th);
    }

    public static void m752a(Throwable th) {
        if (!(th instanceof cp)) {
            try {
                ec ecVar = f766b;
                if (f766b != null && f765a == C1066a.ON) {
                    ecVar = f766b;
                    Runnable c10691 = new C10691(ecVar, th, Thread.currentThread().getId());
                    if (!ecVar.f789c.m708a(c10691)) {
                        ecVar.f788b.execute(c10691);
                    }
                }
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th2) {
            }
        }
    }
}
