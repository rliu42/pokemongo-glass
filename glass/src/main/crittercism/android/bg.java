package crittercism.android;

import android.os.Build.VERSION;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import com.crittercism.app.Transaction;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class bg extends Transaction implements ch {
    private static ExecutorService f435b;
    private static ScheduledExecutorService f436c;
    private static List f437o;
    private static volatile long f438p;
    private static volatile long f439q;
    private static final int[] f440r;
    private static bg f441s;
    private static bh f442t;
    private String f443d;
    private long f444e;
    private int f445f;
    private long f446g;
    private long f447h;
    private long f448i;
    private C1017a f449j;
    private Map f450k;
    private String f451l;
    private long f452m;
    private ScheduledFuture f453n;

    /* renamed from: crittercism.android.bg.1 */
    static class C10091 extends di {
        final /* synthetic */ List f412a;
        final /* synthetic */ az f413b;

        C10091(List list, az azVar) {
            this.f412a = list;
            this.f413b = azVar;
        }

        public final void m457a() {
            for (bg bgVar : this.f412a) {
                synchronized (bgVar) {
                    if (bgVar.f449j == C1017a.STARTED) {
                        this.f413b.f381n.m554b(bgVar);
                    }
                }
            }
        }
    }

    /* renamed from: crittercism.android.bg.2 */
    static class C10102 extends di {
        final /* synthetic */ az f414a;

        C10102(az azVar) {
            this.f414a = azVar;
        }

        public final void m458a() {
            ea eaVar = ea.TXN_CRASH_ALL_FAULT;
            this.f414a.f381n.m549a();
        }
    }

    /* renamed from: crittercism.android.bg.3 */
    static class C10113 extends di {
        final /* synthetic */ az f415a;
        final /* synthetic */ bg f416b;

        C10113(az azVar, bg bgVar) {
            this.f415a = azVar;
            this.f416b = bgVar;
        }

        public final void m459a() {
            this.f415a.f384q.f715a.block();
            this.f415a.f381n.m552a(this.f416b);
        }
    }

    /* renamed from: crittercism.android.bg.4 */
    class C10124 extends di {
        final /* synthetic */ bg f417a;
        final /* synthetic */ bg f418b;

        C10124(bg bgVar, bg bgVar2) {
            this.f418b = bgVar;
            this.f417a = bgVar2;
        }

        public final void m460a() {
            this.f418b.a.f384q.f715a.block();
            this.f418b.a.f381n.m552a(this.f417a);
        }
    }

    /* renamed from: crittercism.android.bg.5 */
    class C10135 extends di {
        final /* synthetic */ bg f419a;

        C10135(bg bgVar) {
            this.f419a = bgVar;
        }

        public final void m461a() {
            this.f419a.m488s();
        }
    }

    /* renamed from: crittercism.android.bg.6 */
    class C10156 extends di {
        final /* synthetic */ bg f421a;
        final /* synthetic */ bg f422b;

        /* renamed from: crittercism.android.bg.6.1 */
        class C10141 implements Runnable {
            final /* synthetic */ C10156 f420a;

            C10141(C10156 c10156) {
                this.f420a = c10156;
            }

            public final void run() {
            }
        }

        C10156(bg bgVar, bg bgVar2) {
            this.f422b = bgVar;
            this.f421a = bgVar2;
        }

        public final void m462a() {
            if (this.f421a.f449j != C1017a.SUCCESS) {
                Runnable c10141 = new C10141(this);
                Executor executor = this.f422b.a.f386s;
                Object futureTask = new FutureTask(c10141, null);
                executor.execute(futureTask);
                try {
                    futureTask.get();
                } catch (Throwable e) {
                    dx.m752a(e);
                } catch (Throwable e2) {
                    dx.m752a(e2);
                }
            }
            this.f422b.a.f384q.f715a.block();
            this.f422b.a.f381n.m551a(this.f422b.f451l);
            this.f422b.a.f382o.m552a(this.f421a);
        }
    }

    /* renamed from: crittercism.android.bg.7 */
    class C10167 extends di {
        final /* synthetic */ bg f423a;
        final /* synthetic */ bg f424b;

        C10167(bg bgVar, bg bgVar2) {
            this.f424b = bgVar;
            this.f423a = bgVar2;
        }

        public final void m463a() {
            this.f424b.a.f384q.f715a.block();
            this.f424b.a.f381n.m552a(this.f423a);
        }
    }

    /* renamed from: crittercism.android.bg.a */
    enum C1017a {
        CREATED,
        STARTED,
        SUCCESS,
        SLOW,
        FAILED,
        TIMEOUT,
        CRASHED,
        ABORTED,
        INTERRUPTED
    }

    static {
        f435b = Executors.newSingleThreadExecutor(new dz());
        f436c = Executors.newScheduledThreadPool(1, new dz());
        f437o = new LinkedList();
        f438p = 0;
        f439q = 0;
        f440r = new int[]{32, 544, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 8224};
        f441s = null;
        f442t = new bh();
    }

    public static void m472a(bh bhVar) {
        f442t = bhVar;
    }

    public bg(az azVar, String str) {
        int i = -1;
        this.f444e = -1;
        this.f445f = -1;
        this.f453n = null;
        if (str.length() > MotionEventCompat.ACTION_MASK) {
            dx.m757c("Transaction name exceeds 255 characters! Truncating to first 255 characters.");
            this.f443d = str.substring(0, MotionEventCompat.ACTION_MASK);
        } else {
            this.f443d = str;
        }
        this.f449j = C1017a.CREATED;
        this.f450k = new HashMap();
        this.a = azVar;
        this.f451l = cg.f624a.m663a();
        this.f444e = -1;
        JSONObject optJSONObject = f442t.f457d.optJSONObject(str);
        if (optJSONObject != null) {
            i = optJSONObject.optInt(GameServices.SCORE_VALUE, -1);
        }
        this.f445f = i;
    }

    private bg(bg bgVar) {
        this.f444e = -1;
        this.f445f = -1;
        this.f453n = null;
        this.f443d = bgVar.f443d;
        this.f444e = bgVar.f444e;
        this.f445f = bgVar.f445f;
        this.f446g = bgVar.f446g;
        this.f447h = bgVar.f447h;
        this.f449j = bgVar.f449j;
        this.f450k = bgVar.f450k;
        this.f451l = bgVar.f451l;
        this.f448i = bgVar.f448i;
        this.f452m = bgVar.f452m;
    }

    public bg(JSONArray jSONArray) {
        this.f444e = -1;
        this.f445f = -1;
        this.f453n = null;
        this.f443d = jSONArray.getString(0);
        this.f449j = C1017a.values()[jSONArray.getInt(1)];
        this.f444e = (long) ((int) (jSONArray.getDouble(2) * 1000.0d));
        this.f445f = jSONArray.optInt(3, -1);
        this.f450k = new HashMap();
        JSONObject jSONObject = jSONArray.getJSONObject(4);
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            this.f450k.put(str, jSONObject.getString(str));
        }
        this.f446g = ed.f792a.m770a(jSONArray.getString(5));
        this.f447h = ed.f792a.m770a(jSONArray.getString(6));
        this.f448i = (long) (jSONArray.optDouble(7, 0.0d) * Math.pow(10.0d, 9.0d));
        this.f451l = cg.f624a.m663a();
    }

    public static void m478f() {
        f438p = System.nanoTime();
        List<bg> linkedList = new LinkedList();
        synchronized (f437o) {
            linkedList.addAll(f437o);
        }
        if (f441s != null && f439q == 0) {
            synchronized (f441s) {
                bg bgVar = f441s;
                bgVar.f448i += f438p - f441s.f452m;
            }
        }
        for (bg bgVar2 : linkedList) {
            synchronized (bgVar2) {
                if (bgVar2.f449j == C1017a.STARTED) {
                    if (bgVar2.f453n != null && bgVar2.f453n.isCancelled()) {
                        bgVar2.m468a(bgVar2.f444e - TimeUnit.MILLISECONDS.convert(bgVar2.f448i, TimeUnit.NANOSECONDS));
                    } else if (bgVar2.f453n == null) {
                        bgVar2.m468a(bgVar2.f444e);
                    }
                }
            }
        }
    }

    private static boolean m481l() {
        return f438p > f439q;
    }

    public static void m470a(az azVar) {
        f439q = System.nanoTime();
        List<bg> linkedList = new LinkedList();
        synchronized (f437o) {
            linkedList.addAll(f437o);
        }
        for (bg bgVar : linkedList) {
            synchronized (bgVar) {
                if (bgVar.f449j == C1017a.STARTED) {
                    if (bgVar.f452m < f438p) {
                        bgVar.f448i += f439q - f438p;
                    } else if (bgVar.f452m <= f439q) {
                        bgVar.f448i += f439q - bgVar.f452m;
                    }
                }
                bgVar.m487r();
            }
        }
        Object futureTask = new FutureTask(new C10091(linkedList, azVar), null);
        synchronized (f435b) {
            f435b.execute(futureTask);
        }
        try {
            futureTask.get();
        } catch (Throwable e) {
            dx.m752a(e);
        } catch (Throwable e2) {
            dx.m752a(e2);
        }
    }

    public static List m467a(az azVar, boolean z) {
        List linkedList = new LinkedList();
        synchronized (f437o) {
            linkedList.addAll(f437o);
        }
        long currentTimeMillis = System.currentTimeMillis();
        long nanoTime = System.nanoTime();
        for (int size = linkedList.size() - 1; size >= 0; size--) {
            bg bgVar = (bg) linkedList.get(size);
            synchronized (bgVar) {
                if (bgVar.f449j == C1017a.STARTED) {
                    bgVar.f447h = currentTimeMillis;
                    bgVar.f449j = C1017a.CRASHED;
                    if (m481l()) {
                        bgVar.f448i = (nanoTime - Math.max(f438p, bgVar.f452m)) + bgVar.f448i;
                    }
                } else {
                    linkedList.remove(size);
                }
                bgVar.m487r();
            }
        }
        Object futureTask = new FutureTask(new C10102(azVar), null);
        synchronized (f435b) {
            f435b.execute(futureTask);
            if (z) {
                azVar.f393z.clear();
            } else {
                f435b.shutdown();
            }
        }
        try {
            futureTask.get();
        } catch (Throwable e) {
            dx.m752a(e);
        } catch (Throwable e2) {
            dx.m752a(e2);
        }
        return linkedList;
    }

    public static void m469a(aw awVar) {
        try {
            bs w = awVar.m356w();
            List<bq> c = w.m555c();
            long currentTimeMillis = System.currentTimeMillis();
            for (bq bqVar : c) {
                JSONArray jSONArray = (JSONArray) ((bz) bqVar).m629a();
                if (jSONArray != null) {
                    try {
                        ch bgVar = new bg(jSONArray);
                        bgVar.f447h = currentTimeMillis;
                        bgVar.f449j = C1017a.ABORTED;
                        awVar.m357x().m552a(bgVar);
                    } catch (Throwable e) {
                        dx.m752a(e);
                    } catch (Throwable e2) {
                        dx.m752a(e2);
                    }
                }
            }
            w.m549a();
        } catch (ThreadDeath e3) {
            throw e3;
        } catch (Throwable e22) {
            dx.m752a(e22);
        }
    }

    public static void m474b(az azVar) {
        try {
            bg bgVar = new bg(azVar, "App Load");
            f441s = bgVar;
            synchronized (bgVar) {
                long m = m482m();
                if (m != -1) {
                    f441s.f449j = C1017a.STARTED;
                    f441s.f446g = System.currentTimeMillis() - (SystemClock.elapsedRealtime() - m);
                    bg bgVar2 = f441s;
                    m = TimeUnit.NANOSECONDS.convert(m, TimeUnit.MILLISECONDS);
                    bgVar2.f452m = System.nanoTime() - (TimeUnit.NANOSECONDS.convert(SystemClock.elapsedRealtime(), TimeUnit.MILLISECONDS) - m);
                    f441s.f444e = f442t.m501a(f441s.f443d);
                    synchronized (f437o) {
                        f437o.add(f441s);
                    }
                    Runnable c10113 = new C10113(azVar, new bg(f441s));
                    synchronized (f435b) {
                        f435b.execute(c10113);
                        f441s.m468a(f441s.f444e);
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    private static long m482m() {
        long[] jArr = new long[1];
        String str = "/proc/" + Process.myPid() + "/stat";
        try {
            return !((Boolean) Process.class.getDeclaredMethod("readProcFile", new Class[]{String.class, int[].class, String[].class, long[].class, float[].class}).invoke(null, new Object[]{str, f440r, null, jArr, null})).booleanValue() ? -1 : jArr[0] * 10;
        } catch (Throwable e) {
            dx.m752a(e);
            return -1;
        } catch (Throwable e2) {
            dx.m752a(e2);
            return -1;
        } catch (Throwable e22) {
            dx.m752a(e22);
            return -1;
        } catch (Throwable e222) {
            dx.m752a(e222);
            return -1;
        }
    }

    public static void m479g() {
        try {
            if (f441s != null) {
                f441s.m493b();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    public final void m490a() {
        try {
            m483n();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    private synchronized void m483n() {
        if (this.f449j == C1017a.CREATED) {
            this.f449j = C1017a.STARTED;
            this.f446g = System.currentTimeMillis();
            this.f452m = System.nanoTime();
            this.f444e = f442t.m501a(this.f443d);
            synchronized (f437o) {
                f437o.add(this);
            }
            Runnable c10124 = new C10124(this, new bg(this));
            synchronized (f435b) {
                f435b.execute(c10124);
                m468a(this.f444e);
            }
        } else {
            dx.m755b("Transaction " + this.f443d + " has already been started.", new IllegalStateException("Transaction has already started"));
        }
    }

    public final void m493b() {
        try {
            m484o();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    private synchronized void m484o() {
        m471a(C1017a.SUCCESS);
    }

    public final void m494c() {
        try {
            m485p();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    private synchronized void m485p() {
        m471a(C1017a.FAILED);
    }

    public final void m497h() {
        try {
            m486q();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    private synchronized void m486q() {
        m471a(C1017a.INTERRUPTED);
    }

    private void m471a(C1017a c1017a) {
        if (!(c1017a == C1017a.SUCCESS || c1017a == C1017a.FAILED)) {
            C1017a c1017a2 = C1017a.INTERRUPTED;
        }
        if (this.f449j == C1017a.STARTED) {
            m487r();
            m475b(c1017a);
        } else if (this.f449j != C1017a.TIMEOUT) {
            dx.m755b("Transaction " + this.f443d + " is not running. Either it has not been started or it has been stopped.", new IllegalStateException("Transaction is not running"));
        }
    }

    public static void m480i() {
        List<bg> linkedList = new LinkedList();
        synchronized (f437o) {
            linkedList.addAll(f437o);
        }
        for (bg bgVar : linkedList) {
            synchronized (bgVar) {
                if (bgVar.f449j == C1017a.STARTED) {
                    bgVar.f444e = f442t.m501a(bgVar.f443d);
                    bgVar.m487r();
                    bgVar.m468a(bgVar.f444e);
                }
            }
        }
    }

    private void m468a(long j) {
        if (m481l()) {
            this.f453n = f436c.schedule(new C10135(this), j, TimeUnit.MILLISECONDS);
        }
    }

    private synchronized void m487r() {
        if (this.f453n != null) {
            this.f453n.cancel(false);
        }
    }

    private synchronized void m488s() {
        if (this.f449j == C1017a.STARTED) {
            m475b(C1017a.TIMEOUT);
        }
    }

    private void m475b(C1017a c1017a) {
        this.f449j = c1017a;
        this.f447h = System.currentTimeMillis();
        long nanoTime = System.nanoTime();
        if (m481l()) {
            this.f448i = (nanoTime - Math.max(f438p, this.f452m)) + this.f448i;
        }
        synchronized (f437o) {
            f437o.remove(this);
        }
        Runnable c10156 = new C10156(this, new bg(this));
        synchronized (f435b) {
            f435b.execute(c10156);
        }
    }

    public final void m491a(int i) {
        try {
            m473b(i);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
        }
    }

    private synchronized void m473b(int i) {
        if (i < 0) {
            dx.m757c("Ignoring Transaction.setValue(int) call. Negative parameter provided.");
        } else if (this.f449j == C1017a.CREATED) {
            this.f445f = i;
        } else if (this.f449j == C1017a.STARTED) {
            this.f445f = i;
            Runnable c10167 = new C10167(this, new bg(this));
            synchronized (f435b) {
                f435b.execute(c10167);
            }
        } else {
            dx.m755b("Transaction " + this.f443d + " no longer in progress. Ignoring setValue(int) call.", new IllegalStateException("Transaction no longer in progress"));
        }
    }

    public final int m495d() {
        try {
            return m489t();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m752a(th);
            return -1;
        }
    }

    private synchronized int m489t() {
        return this.f445f;
    }

    public final JSONArray m498j() {
        Object obj;
        JSONArray put = new JSONArray().put(this.f443d).put(this.f449j.ordinal()).put(((double) this.f444e) / 1000.0d);
        if (this.f445f == -1) {
            obj = JSONObject.NULL;
        } else {
            obj = Integer.valueOf(this.f445f);
        }
        JSONArray put2 = put.put(obj).put(new JSONObject(this.f450k)).put(ed.f792a.m772a(new Date(this.f446g))).put(ed.f792a.m772a(new Date(this.f447h)));
        if (VERSION.SDK_INT >= 14) {
            put2.put(((double) Math.round((((double) this.f448i) / Math.pow(10.0d, 9.0d)) * 1000.0d)) / 1000.0d);
        } else {
            put2.put(JSONObject.NULL);
        }
        return put2;
    }

    public final String m496e() {
        return this.f451l;
    }

    public final void m492a(OutputStream outputStream) {
        JSONArray jSONArray = null;
        try {
            jSONArray = m498j();
        } catch (JSONException e) {
        }
        if (jSONArray != null) {
            outputStream.write(jSONArray.toString().getBytes());
        }
    }

    public final C1017a m499k() {
        return this.f449j;
    }
}
