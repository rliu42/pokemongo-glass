package com.crittercism.app;

import crittercism.android.az;
import crittercism.android.dg;
import crittercism.android.dl;
import crittercism.android.dx;
import crittercism.android.dy;
import java.util.HashMap;
import java.util.Map;

public class CritterUserDataRequest {
    private final CritterCallback f10a;
    private az f11b;
    private Map f12c;
    private dl f13d;

    /* renamed from: com.crittercism.app.CritterUserDataRequest.1 */
    class C01241 implements Runnable {
        final /* synthetic */ CritterUserDataRequest f9a;

        C01241(CritterUserDataRequest critterUserDataRequest) {
            this.f9a = critterUserDataRequest;
        }

        public final void run() {
            this.f9a.f13d.run();
            this.f9a.f12c = this.f9a.f13d.f740a;
            this.f9a.f10a.onCritterDataReceived(new CritterUserData(this.f9a.f12c, this.f9a.f11b.f373f.m747b()));
        }
    }

    public CritterUserDataRequest(CritterCallback cb) {
        this.f10a = cb;
        this.f11b = az.m375A();
        this.f12c = new HashMap();
        this.f13d = new dl(this.f11b);
    }

    public CritterUserDataRequest requestRateMyAppInfo() {
        this.f13d.m718e();
        return this;
    }

    public CritterUserDataRequest requestDidCrashOnLastLoad() {
        this.f13d.m716c();
        return this;
    }

    public CritterUserDataRequest requestUserUUID() {
        this.f13d.m717d();
        return this;
    }

    public CritterUserDataRequest requestOptOutStatus() {
        this.f13d.m715b();
        return this;
    }

    public synchronized void makeRequest() {
        dg dgVar = this.f11b.f384q;
        if (dgVar == null) {
            dx.m751a("Must initialize Crittercism before calling " + getClass().getName() + ".makeRequest()", new IllegalStateException());
        } else {
            Runnable c01241 = new C01241(this);
            if (!dgVar.m708a(c01241)) {
                new dy(c01241).start();
            }
        }
    }
}
