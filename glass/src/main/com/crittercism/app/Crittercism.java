package com.crittercism.app;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.os.Build.VERSION;
import com.crittercism.integrations.PluginException;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.voxelbusters.nativeplugins.defines.Keys.Ui;
import crittercism.android.az;
import crittercism.android.az.C09971;
import crittercism.android.az.C10004;
import crittercism.android.az.C10037;
import crittercism.android.bc;
import crittercism.android.bg;
import crittercism.android.bn.C1020a;
import crittercism.android.cf;
import crittercism.android.cf.C1053a;
import crittercism.android.dk;
import crittercism.android.dq;
import crittercism.android.dt;
import crittercism.android.dx;
import java.lang.reflect.Array;
import java.net.URL;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.C1391R;

public class Crittercism {
    private Crittercism() {
    }

    public static void performRateMyAppButtonAction(CritterRateMyAppButtons critterRateMyAppButtons) {
        try {
            if (az.m375A().f373f.m747b()) {
                dx.m757c("User has opted out of crittercism.  performRateMyAppButtonAction exiting.");
                return;
            }
            az A = az.m375A();
            if (VERSION.SDK_INT < 5) {
                dx.m757c("Rate my app not supported below api level 5");
                return;
            }
            String D = A.m381D();
            if (D == null) {
                dx.m754b("Cannot create proper URI to open app market.  Returning null.");
                return;
            }
            switch (C10004.f344a[critterRateMyAppButtons.ordinal()]) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    try {
                        A.m391a(D);
                    } catch (Exception e) {
                        dx.m757c("performRateMyAppButtonAction(CritterRateMyAppButtons.YES) failed.  Email support@crittercism.com.");
                        dx.m756c();
                    }
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    try {
                        A.m380C();
                    } catch (Exception e2) {
                        dx.m757c("performRateMyAppButtonAction(CritterRateMyAppButtons.NO) failed.  Email support@crittercism.com.");
                    }
                default:
            }
        } catch (ThreadDeath e3) {
            throw e3;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static AlertDialog generateRateMyAppAlertDialog(Context context, String str, String str2) {
        try {
            return az.m375A().m383a(context, str, str2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return null;
        }
    }

    public static AlertDialog generateRateMyAppAlertDialog(Context context) {
        AlertDialog alertDialog = null;
        try {
            String b;
            String c;
            az A = az.m375A();
            dt dtVar = A.f359A;
            if (A.f359A != null) {
                b = A.f359A.m735b();
                c = A.f359A.m736c();
            } else {
                c = null;
                b = null;
            }
            alertDialog = A.m383a(context, c, b);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
        return alertDialog;
    }

    public static synchronized void initialize(Context context, String str, CrittercismConfig crittercismConfig) {
        synchronized (Crittercism.class) {
            if (str == null) {
                try {
                    m21a(String.class.getCanonicalName());
                } catch (C1020a e) {
                    throw new IllegalArgumentException("Crittercism cannot be initialized. " + e.getMessage());
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.m752a(th);
                }
            } else if (context == null) {
                m21a(Context.class.getCanonicalName());
            } else if (crittercismConfig == null) {
                m21a(CrittercismConfig.class.getCanonicalName());
            } else if (!az.m375A().f369b) {
                long nanoTime = System.nanoTime();
                az.m375A().m386a(context, str, crittercismConfig);
                new StringBuilder("Crittercism finished initializing in ").append((System.nanoTime() - nanoTime) / 1000000).append("ms");
                dx.m753b();
            }
        }
    }

    public static synchronized void initialize(Context context, String appID) {
        synchronized (Crittercism.class) {
            initialize(context, appID, new CrittercismConfig());
        }
    }

    private static void m21a(String str) {
        dx.m755b("Crittercism cannot be initialized", new NullPointerException(str + " was null"));
    }

    public static void sendAppLoadData() {
        try {
            CrittercismConfig crittercismConfig = az.m375A().f388u;
            if (crittercismConfig == null) {
                m23b("sendAppLoadData");
            } else if (!crittercismConfig.delaySendingAppLoad()) {
                dx.m750a("sendAppLoadData() will only send data to Crittercism if \"delaySendingAppLoad\" is set to true in the configuration settings you include in the init call.");
            } else if (!az.m375A().f373f.m747b()) {
                az A = az.m375A();
                if (!A.f388u.delaySendingAppLoad()) {
                    dx.m757c("CrittercismConfig instance not set to delay sending app loads.");
                } else if (!A.f387t && !A.f361C) {
                    A.f361C = true;
                    Runnable c09971 = new C09971(A);
                    if (!A.f384q.m708a(c09971)) {
                        A.f386s.execute(c09971);
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static void logHandledException(Throwable th) {
        try {
            if (!az.m375A().f369b) {
                m23b("logHandledException");
            } else if (!az.m375A().f373f.m747b()) {
                az.m375A().m400b(th);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th2) {
            dx.m752a(th2);
        }
    }

    public static void _logHandledException(String name, String msg, String stack) {
        try {
            new StringBuilder("_logHandledException(name, msg, stack) called: ").append(name).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(msg).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(stack);
            dx.m753b();
            if (name == null || msg == null || stack == null) {
                dx.m757c("Calling logHandledException with null parameter(s). Nothing will be reported to Crittercism");
            } else {
                logHandledException(new PluginException(name, msg, stack));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static void _logHandledException(String name, String msg, String[] classStackElems, String[] methodStackElems, String[] fileStackElems, int[] lineNumberStackElems) {
        try {
            new StringBuilder("_logHandledException(name, msg, classes, methods, files, lines) called: ").append(name);
            dx.m753b();
            if (name == null || msg == null || classStackElems == null) {
                dx.m757c("Calling logHandledException with null parameter(s). Nothing will be reported to Crittercism");
            } else {
                logHandledException(new PluginException(name, msg, classStackElems, methodStackElems, fileStackElems, lineNumberStackElems));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static void logNetworkRequest(String method, URL url, long responseTime, long bytesRead, long bytesSent, int responseCode, Exception error) {
        try {
            long currentTimeMillis = System.currentTimeMillis() - responseTime;
            if (!az.m375A().f369b) {
                m23b("logEndpoint");
            } else if (!az.m375A().f373f.m747b()) {
                az.m375A().m394a(method, url, responseTime, bytesRead, bytesSent, responseCode, error, currentTimeMillis);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static boolean didCrashOnLastLoad() {
        boolean z = false;
        try {
            az A = az.m375A();
            if (!A.f369b) {
                m23b("didCrashOnLoad");
            } else if (!A.m379B()) {
                A.f372e.block();
                z = dq.f750a;
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
        return z;
    }

    @Deprecated
    public static void _logCrashException(Throwable exception) {
        try {
            new StringBuilder("_logCrashException(Throwable) called with throwable: ").append(exception.getMessage());
            dx.m753b();
            if (az.m375A().f369b) {
                az.m375A().m395a(exception);
            } else {
                m23b("_logCrashException");
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static void _logCrashException(String name, String msg, String stack) {
        if (name == null || msg == null || stack == null) {
            try {
                dx.m754b("Unable to handle application crash. Missing parameters");
                return;
            } catch (Throwable th) {
                dx.m752a(th);
                return;
            }
        }
        new StringBuilder("_logCrashException(name, msg, stack) called: ").append(name).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(msg).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(stack);
        dx.m753b();
        _logCrashException(new PluginException(name, msg, stack));
    }

    public static void _logCrashException(String msg, String stack) {
        if (msg == null || stack == null) {
            try {
                dx.m754b("Unable to handle application crash. Missing parameters");
                return;
            } catch (Throwable th) {
                dx.m752a(th);
                return;
            }
        }
        new StringBuilder("_logCrashException(msg, stack) called: ").append(msg).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(stack);
        dx.m753b();
        _logCrashException(new PluginException(msg, stack));
    }

    public static void _logCrashException(String name, String msg, String[] classStackElems, String[] methodStackElems, String[] fileStackElems, int[] lineNumberStackElems) {
        try {
            new StringBuilder("_logCrashException(String, String, String[], String[], String[], int[]) called: ").append(name).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(msg);
            dx.m753b();
            if (name == null || msg == null || classStackElems == null || methodStackElems == null || fileStackElems == null || lineNumberStackElems == null) {
                dx.m754b("Unable to handle application crash. Missing parameters");
                return;
            }
            if (m22a(classStackElems, methodStackElems, fileStackElems, lineNumberStackElems)) {
                _logCrashException(new PluginException(name, msg, classStackElems, methodStackElems, fileStackElems, lineNumberStackElems));
            } else {
                dx.m754b("Unable to handle application crash. Missing stack elements");
            }
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    private static boolean m22a(Object... objArr) {
        if (objArr.length <= 0 || objArr[0] == null) {
            return false;
        }
        int length = Array.getLength(objArr[0]);
        for (int i = 1; i < objArr.length; i++) {
            if (objArr[i] == null) {
                return false;
            }
            if (Array.getLength(objArr[i]) != length) {
                return false;
            }
        }
        return true;
    }

    public static boolean getOptOutStatus() {
        boolean z = false;
        try {
            az A = az.m375A();
            if (A.f369b) {
                z = A.m379B();
            } else {
                m23b("getOptOutStatus");
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
        return z;
    }

    public static void setOptOutStatus(boolean z) {
        try {
            if (az.m375A().f369b) {
                az A = az.m375A();
                Runnable dkVar = new dk(A.f370c, A, z);
                if (!A.f384q.m708a(dkVar)) {
                    A.f386s.execute(dkVar);
                    return;
                }
                return;
            }
            m23b("setOptOutStatus");
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static void setMetadata(JSONObject jSONObject) {
        try {
            if (az.m375A().f369b) {
                az.m375A().m396a(jSONObject);
            } else {
                m23b("setMetadata");
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static void setUsername(String str) {
        try {
            if (!az.m375A().f369b) {
                m23b("setUsername");
            } else if (str == null) {
                dx.m757c("Crittercism.setUsername() given invalid parameter: null");
            } else {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.putOpt(Ui.USER_NAME, str);
                    az.m375A().m396a(jSONObject);
                } catch (Throwable e) {
                    dx.m755b("Crittercism.setUsername()", e);
                }
            }
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable e3) {
            dx.m752a(e3);
        }
    }

    public static void leaveBreadcrumb(String str) {
        try {
            if (!az.m375A().f369b) {
                m23b("leaveBreadcrumb");
            } else if (str == null) {
                dx.m755b("Cannot leave null breadcrumb", new NullPointerException());
            } else {
                az A = az.m375A();
                if (!A.f373f.m747b()) {
                    Runnable c10037 = new C10037(A, new cf(str, C1053a.NORMAL));
                    if (!A.f384q.m708a(c10037)) {
                        new StringBuilder("SENDING ").append(str).append(" TO EXECUTOR");
                        dx.m753b();
                        A.f386s.execute(c10037);
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static void beginTransaction(String str) {
        try {
            az A = az.m375A();
            if (A.f387t) {
                dx.m757c("Transactions are not supported for services. Ignoring Crittercism.beginTransaction() call for " + str + ".");
                return;
            }
            Transaction a = Transaction.m30a(str);
            if (a instanceof bg) {
                synchronized (A.f393z) {
                    Transaction transaction = (Transaction) A.f393z.remove(str);
                    if (transaction != null) {
                        ((bg) transaction).m497h();
                    }
                    if (A.f393z.size() > 50) {
                        dx.m757c("Crittercism only supports a maximum of 50 concurrent transactions. Ignoring Crittercism.beginTransaction() call for " + str + ".");
                        return;
                    }
                    A.f393z.put(str, a);
                    a.m31a();
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static void endTransaction(String str) {
        try {
            az A = az.m375A();
            if (A.f387t) {
                dx.m757c("Transactions are not supported for services. Ignoring Crittercism.endTransaction() call for " + str + ".");
                return;
            }
            Transaction transaction;
            synchronized (A.f393z) {
                transaction = (Transaction) A.f393z.remove(str);
            }
            if (transaction != null) {
                transaction.m33b();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static void failTransaction(String str) {
        try {
            az A = az.m375A();
            if (A.f387t) {
                dx.m757c("Transactions are not supported for services. Ignoring Crittercism.failTransaction() call for " + str + ".");
                return;
            }
            Transaction transaction;
            synchronized (A.f393z) {
                transaction = (Transaction) A.f393z.remove(str);
            }
            if (transaction != null) {
                transaction.m34c();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static void setTransactionValue(String str, int i) {
        try {
            az A = az.m375A();
            if (A.f387t) {
                dx.m757c("Transactions are not supported for services. Ignoring Crittercism.setTransactionValue() call for " + str + ".");
                return;
            }
            synchronized (A.f393z) {
                Transaction transaction = (Transaction) A.f393z.get(str);
                if (transaction != null) {
                    transaction.m32a(i);
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public static int getTransactionValue(String str) {
        try {
            return az.m375A().m397b(str);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return -1;
        }
    }

    public static void updateLocation(Location curLocation) {
        if (!az.m375A().f369b) {
            m23b("updateLocation");
        } else if (curLocation == null) {
            dx.m755b("Cannot leave null location", new NullPointerException());
        } else {
            bc.m449a(curLocation);
        }
    }

    private static void m23b(String str) {
        dx.m755b("Must initialize Crittercism before calling " + Crittercism.class.getName() + "." + str + "().  Request is being ignored...", new IllegalStateException());
    }
}
