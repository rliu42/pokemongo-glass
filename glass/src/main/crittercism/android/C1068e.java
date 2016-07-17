package crittercism.android;

import crittercism.android.C1050c.C1049a;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/* renamed from: crittercism.android.e */
public final class C1068e {
    List f769a;
    final Set f770b;
    final Set f771c;
    private Executor f772d;

    /* renamed from: crittercism.android.e.a */
    class C1067a implements Runnable {
        final /* synthetic */ C1068e f767a;
        private C1050c f768b;

        private C1067a(C1068e c1068e, C1050c c1050c) {
            this.f767a = c1068e;
            this.f768b = c1050c;
        }

        public final void run() {
            if (!m758a(this.f768b)) {
                String a = this.f768b.m631a();
                if (m759a(a)) {
                    int indexOf = a.indexOf("?");
                    if (indexOf != -1) {
                        this.f768b.m637a(a.substring(0, indexOf));
                    }
                }
                synchronized (this.f767a.f769a) {
                    for (C1007f a2 : this.f767a.f769a) {
                        a2.m374a(this.f768b);
                    }
                }
            }
        }

        private boolean m758a(C1050c c1050c) {
            String a = c1050c.m631a();
            synchronized (this.f767a.f770b) {
                for (String contains : this.f767a.f770b) {
                    if (a.contains(contains)) {
                        return true;
                    }
                }
                return false;
            }
        }

        private boolean m759a(String str) {
            synchronized (this.f767a.f771c) {
                for (String contains : this.f767a.f771c) {
                    if (str.contains(contains)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private C1068e(Executor executor, List list, List list2) {
        this.f769a = new LinkedList();
        this.f770b = new HashSet();
        this.f771c = new HashSet();
        this.f772d = executor;
        m763a(list);
        m764b(list2);
    }

    public C1068e(Executor executor) {
        this(executor, new LinkedList(), new LinkedList());
    }

    public final void m762a(C1007f c1007f) {
        synchronized (this.f769a) {
            this.f769a.add(c1007f);
        }
    }

    public final void m763a(List list) {
        synchronized (this.f770b) {
            this.f770b.addAll(list);
            this.f770b.remove(null);
        }
    }

    public final void m764b(List list) {
        synchronized (this.f771c) {
            this.f771c.addAll(list);
            this.f771c.remove(null);
        }
    }

    @Deprecated
    public final void m760a(C1050c c1050c) {
        m761a(c1050c, C1049a.LEGACY_JAVANET);
    }

    public final void m761a(C1050c c1050c, C1049a c1049a) {
        if (!c1050c.f581b) {
            c1050c.f581b = true;
            c1050c.f582c = c1049a;
            this.f772d.execute(new C1067a(c1050c, (byte) 0));
        }
    }
}
