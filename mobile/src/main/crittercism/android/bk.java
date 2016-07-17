package crittercism.android;

import com.crittercism.integrations.PluginException;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import crittercism.android.bx.C1022a;
import crittercism.android.bx.C1023b;
import crittercism.android.bx.C1024c;
import crittercism.android.bx.C1025d;
import crittercism.android.bx.C1026e;
import crittercism.android.bx.C1027f;
import crittercism.android.bx.C1029h;
import crittercism.android.bx.C1030i;
import crittercism.android.bx.C1031j;
import crittercism.android.bx.C1032k;
import crittercism.android.bx.C1033l;
import crittercism.android.bx.C1034m;
import crittercism.android.bx.C1035n;
import crittercism.android.bx.C1036o;
import crittercism.android.bx.C1037p;
import crittercism.android.bx.C1038q;
import crittercism.android.bx.C1039r;
import crittercism.android.bx.C1040s;
import crittercism.android.bx.C1041t;
import crittercism.android.bx.C1042u;
import crittercism.android.bx.C1043v;
import crittercism.android.bx.C1044w;
import crittercism.android.bx.C1045x;
import crittercism.android.bx.C1046y;
import crittercism.android.bx.C1047z;
import crittercism.android.bx.aa;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

public final class bk implements ch {
    public long f477a;
    public JSONArray f478b;
    public String f479c;
    public String f480d;
    public JSONArray f481e;
    public String f482f;
    public JSONObject f483g;
    private JSONObject f484h;
    private JSONArray f485i;
    private JSONArray f486j;
    private String f487k;
    private JSONArray f488l;
    private String f489m;
    private int f490n;
    private boolean f491o;
    private String f492p;

    public bk(Throwable th, long j) {
        int i = 0;
        this.f480d = BuildConfig.FLAVOR;
        this.f490n = -1;
        this.f491o = false;
        this.f491o = th instanceof PluginException;
        this.f492p = cg.f624a.m663a();
        this.f482f = "uhe";
        bu buVar = new bu();
        buVar.m557a(new C1022a()).m557a(new C1024c()).m557a(new C1023b()).m557a(new C1025d()).m557a(new C1026e()).m557a(new C1027f()).m557a(new C1036o()).m557a(new C1037p()).m557a(new C1030i()).m557a(new C1031j()).m557a(new C1029h()).m557a(new C1047z()).m557a(new aa()).m557a(new C1032k()).m557a(new C1033l()).m557a(new C1035n()).m557a(new C1034m()).m557a(new C1038q()).m557a(new C1039r()).m557a(new C1040s()).m557a(new C1041t()).m557a(new C1042u()).m557a(new C1043v()).m557a(new C1044w()).m557a(new C1045x()).m557a(new C1046y());
        this.f483g = buVar.m558a();
        this.f484h = new JSONObject();
        this.f477a = j;
        this.f479c = m515a(th);
        if (th.getMessage() != null) {
            this.f480d = th.getMessage();
        }
        if (!this.f491o) {
            this.f490n = m517c(th);
        }
        this.f487k = "android";
        this.f489m = ed.f792a.m771a();
        this.f488l = new JSONArray();
        String[] b = m516b(th);
        int length = b.length;
        while (i < length) {
            this.f488l.put(b[i]);
            i++;
        }
    }

    public final void m521a(String str, bs bsVar) {
        try {
            this.f484h.put(str, new bo(bsVar).f507a);
        } catch (JSONException e) {
        }
    }

    public final void m519a(bs bsVar) {
        this.f485i = new bo(bsVar).f507a;
    }

    public final void m522a(List list) {
        this.f486j = new JSONArray();
        for (bg j : list) {
            try {
                this.f486j.put(j.m498j());
            } catch (Throwable e) {
                dx.m752a(e);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String m515a(java.lang.Throwable r3) {
        /*
        r2 = this;
        r0 = r2.f491o;
        if (r0 == 0) goto L_0x000c;
    L_0x0004:
        r3 = (com.crittercism.integrations.PluginException) r3;
        r0 = r3.getExceptionName();
    L_0x000a:
        return r0;
    L_0x000b:
        r3 = r0;
    L_0x000c:
        r0 = r3.getClass();
        r1 = r0.getName();
        r0 = r3.getCause();
        if (r0 == 0) goto L_0x001c;
    L_0x001a:
        if (r0 != r3) goto L_0x000b;
    L_0x001c:
        r0 = r1;
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.bk.a(java.lang.Throwable):java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String[] m516b(java.lang.Throwable r3) {
        /*
        r1 = new java.io.StringWriter;
        r1.<init>();
        r2 = new java.io.PrintWriter;
        r2.<init>(r1);
    L_0x000a:
        r3.printStackTrace(r2);
        r0 = r3.getCause();
        if (r0 == 0) goto L_0x0015;
    L_0x0013:
        if (r0 != r3) goto L_0x0020;
    L_0x0015:
        r0 = r1.toString();
        r1 = "\n";
        r0 = r0.split(r1);
        return r0;
    L_0x0020:
        r3 = r0;
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.bk.b(java.lang.Throwable):java.lang.String[]");
    }

    private static int m517c(Throwable th) {
        StackTraceElement[] stackTrace = th.getStackTrace();
        int i = 0;
        while (i < stackTrace.length) {
            try {
                Object obj;
                Class cls = Class.forName(stackTrace[i].getClassName());
                for (ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader(); systemClassLoader != null; systemClassLoader = systemClassLoader.getParent()) {
                    if (cls.getClassLoader() == systemClassLoader) {
                        obj = 1;
                        break;
                    }
                }
                obj = null;
                if (obj == null) {
                    return i + 1;
                }
                i++;
            } catch (ClassNotFoundException e) {
            }
        }
        return -1;
    }

    public final void m518a() {
        this.f481e = new JSONArray();
        for (Entry entry : Thread.getAllStackTraces().entrySet()) {
            Map hashMap = new HashMap();
            Thread thread = (Thread) entry.getKey();
            if (thread.getId() != this.f477a) {
                hashMap.put(Twitter.NAME, thread.getName());
                hashMap.put(TriggerIfContentAvailable.ID, Long.valueOf(thread.getId()));
                hashMap.put(GameServices.STATE, thread.getState().name());
                hashMap.put("stacktrace", new JSONArray(Arrays.asList((Object[]) entry.getValue())));
                this.f481e.put(new JSONObject(hashMap));
            }
        }
    }

    public final JSONObject m523b() {
        Map hashMap = new HashMap();
        hashMap.put("app_state", this.f483g);
        hashMap.put("breadcrumbs", this.f484h);
        hashMap.put("current_thread_id", Long.valueOf(this.f477a));
        if (this.f485i != null) {
            hashMap.put("endpoints", this.f485i);
        }
        if (this.f478b != null) {
            hashMap.put("systemBreadcrumbs", this.f478b);
        }
        if (this.f486j != null && this.f486j.length() > 0) {
            hashMap.put("transactions", this.f486j);
        }
        hashMap.put("exception_name", this.f479c);
        hashMap.put("exception_reason", this.f480d);
        hashMap.put("platform", this.f487k);
        if (this.f481e != null) {
            hashMap.put("threads", this.f481e);
        }
        hashMap.put("ts", this.f489m);
        String str = Keys.TYPE;
        Object obj = this.f482f;
        if (this.f477a != 1) {
            obj = obj + "-bg";
        }
        hashMap.put(str, obj);
        hashMap.put("unsymbolized_stacktrace", this.f488l);
        if (!this.f491o) {
            hashMap.put("suspect_line_index", Integer.valueOf(this.f490n));
        }
        return new JSONObject(hashMap);
    }

    public final void m520a(OutputStream outputStream) {
        outputStream.write(m523b().toString().getBytes());
    }

    public final String m524e() {
        return this.f492p;
    }
}
