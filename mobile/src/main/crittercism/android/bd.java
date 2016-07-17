package crittercism.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.os.EnvironmentCompat;
import crittercism.android.ce.C1052a;

public final class bd extends BroadcastReceiver {
    private az f406a;
    private String f407b;
    private C1008b f408c;

    public bd(Context context, az azVar) {
        this.f406a = azVar;
        C1058d c1058d = new C1058d(context);
        this.f407b = c1058d.m693b();
        this.f408c = c1058d.m692a();
    }

    public final void onReceive(Context context, Intent intent) {
        new StringBuilder("CrittercismReceiver: INTENT ACTION = ").append(intent.getAction());
        dx.m753b();
        C1058d c1058d = new C1058d(context);
        C1008b a = c1058d.m692a();
        if (!(this.f408c == a || a == C1008b.UNKNOWN)) {
            if (a == C1008b.NOT_CONNECTED) {
                this.f406a.m389a(new ce(C1052a.INTERNET_DOWN));
            } else if (this.f408c == C1008b.NOT_CONNECTED || this.f408c == C1008b.UNKNOWN) {
                this.f406a.m389a(new ce(C1052a.INTERNET_UP));
            }
            this.f408c = a;
        }
        String b = c1058d.m693b();
        if (!b.equals(this.f407b)) {
            if (this.f407b.equals(EnvironmentCompat.MEDIA_UNKNOWN) || this.f407b.equals("disconnected")) {
                if (!(b.equals(EnvironmentCompat.MEDIA_UNKNOWN) || b.equals("disconnected"))) {
                    this.f406a.m389a(new ce(C1052a.CONN_TYPE_GAINED, b));
                }
            } else if (b.equals("disconnected")) {
                this.f406a.m389a(new ce(C1052a.CONN_TYPE_LOST, this.f407b));
            } else if (!b.equals(EnvironmentCompat.MEDIA_UNKNOWN)) {
                this.f406a.m389a(new ce(C1052a.CONN_TYPE_SWITCHED, this.f407b, b));
            }
            this.f407b = b;
        }
    }
}
