package crittercism.android;

import android.content.Context;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class bs {
    public final File f528a;
    public String f529b;
    public List f530c;
    private cj f531d;
    private int f532e;
    private int f533f;
    private int f534g;
    private C1021a f535h;
    private boolean f536i;

    /* renamed from: crittercism.android.bs.a */
    public static class C1021a {
        int f527a;

        public C1021a(int i) {
            this.f527a = i;
        }
    }

    public bs(Context context, br brVar) {
        this(new File(context.getFilesDir().getAbsolutePath() + "//com.crittercism//" + brVar.m535a()), brVar.m537c(), brVar.m538d(), brVar.m539e(), brVar.m536b(), brVar.m540f());
    }

    private bs(File file, C1021a c1021a, cj cjVar, int i, int i2, String str) {
        this.f536i = false;
        this.f535h = c1021a;
        this.f531d = cjVar;
        this.f534g = i;
        this.f533f = i2;
        this.f529b = str;
        this.f528a = file;
        file.mkdirs();
        m542d();
        this.f532e = m546h().length;
        this.f530c = new LinkedList();
    }

    public final bs m548a(Context context) {
        return new bs(new File(context.getFilesDir().getAbsolutePath() + "//com.crittercism/pending/" + (this.f528a.getName() + "_" + UUID.randomUUID().toString())), this.f535h, this.f531d, this.f534g, this.f533f, this.f529b);
    }

    private boolean m542d() {
        if (!this.f528a.isDirectory()) {
            this.f536i = true;
            String absolutePath = this.f528a.getAbsolutePath();
            if (this.f528a.exists()) {
                IOException iOException = new IOException(absolutePath + " is not a directory");
            } else {
                FileNotFoundException fileNotFoundException = new FileNotFoundException(absolutePath + " does not exist");
            }
        }
        if (this.f536i) {
            return false;
        }
        return true;
    }

    public final synchronized boolean m552a(ch chVar) {
        boolean z = false;
        synchronized (this) {
            if (m542d()) {
                if (this.f532e >= this.f534g) {
                    dx.m753b();
                } else {
                    int b = m553b();
                    if (b != m547i() || m544f()) {
                        if (b > m547i()) {
                            this.f536i = true;
                        } else {
                            boolean c = m541c(chVar);
                            if (c) {
                                this.f532e++;
                            }
                            synchronized (this.f530c) {
                                for (bt c2 : this.f530c) {
                                    c2.m502c();
                                }
                            }
                            z = c;
                        }
                    }
                }
            }
        }
        return z;
    }

    public final synchronized boolean m554b(ch chVar) {
        boolean c;
        if (m542d()) {
            new File(this.f528a, chVar.m465e()).delete();
            c = m541c(chVar);
        } else {
            c = false;
        }
        return c;
    }

    private boolean m541c(ch chVar) {
        File file = new File(this.f528a, chVar.m465e());
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            new StringBuilder("Could not open output stream to : ").append(file);
            dx.m748a();
        }
        try {
            chVar.m464a(outputStream);
            return true;
        } catch (Throwable e2) {
            file.delete();
            dx.m751a("Unable to write to " + file.getAbsolutePath(), e2);
            return false;
        } finally {
            try {
                outputStream.close();
            } catch (Throwable e22) {
                file.delete();
                dx.m751a("Unable to close " + file.getAbsolutePath(), e22);
                return false;
            }
        }
    }

    private void m543e() {
        while (m553b() > m547i()) {
            if (!m544f()) {
                return;
            }
        }
    }

    private boolean m544f() {
        C1021a c1021a = this.f535h;
        if (this.f535h == null) {
            return false;
        }
        C1021a c1021a2 = this.f535h;
        File[] g = m545g();
        File file = null;
        if (g.length > c1021a2.f527a) {
            file = g[c1021a2.f527a];
        }
        if (file == null || !file.delete()) {
            return false;
        }
        return true;
    }

    public final synchronized void m549a() {
        if (m542d()) {
            File[] h = m546h();
            for (File delete : h) {
                delete.delete();
            }
        }
    }

    private File[] m545g() {
        File[] h = m546h();
        Arrays.sort(h);
        return h;
    }

    private File[] m546h() {
        File[] listFiles = this.f528a.listFiles();
        if (listFiles == null) {
            return new File[0];
        }
        return listFiles;
    }

    public final synchronized int m553b() {
        return m546h().length;
    }

    private synchronized int m547i() {
        return this.f533f;
    }

    public final synchronized void m551a(String str) {
        if (m542d() && str != null) {
            File file = new File(this.f528a.getAbsolutePath(), str);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public final void m550a(bs bsVar) {
        if (bsVar != null) {
            int compareTo = this.f528a.getName().compareTo(bsVar.f528a.getName());
            if (compareTo != 0) {
                bs bsVar2;
                bs bsVar3;
                if (compareTo < 0) {
                    bsVar2 = bsVar;
                    bsVar3 = this;
                } else {
                    bsVar2 = this;
                    bsVar3 = bsVar;
                }
                synchronized (r2) {
                    synchronized (r1) {
                        if (m542d() && bsVar.m542d()) {
                            File[] g = m545g();
                            for (compareTo = 0; compareTo < g.length; compareTo++) {
                                g[compareTo].renameTo(new File(bsVar.f528a, g[compareTo].getName()));
                            }
                            bsVar.m543e();
                            for (bt d : this.f530c) {
                                d.m503d();
                            }
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    public final synchronized List m555c() {
        List arrayList;
        arrayList = new ArrayList();
        if (m542d()) {
            cj cjVar = this.f531d;
            File[] g = m545g();
            for (File a : g) {
                arrayList.add(this.f531d.m627a(a));
            }
        }
        return arrayList;
    }
}
