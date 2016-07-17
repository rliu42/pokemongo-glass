package com.unity3d.player;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import com.google.android.gms.location.LocationStatusCodes;

/* renamed from: com.unity3d.player.d */
public final class C0831d implements C0830f {
    private static final SurfaceTexture f170a;
    private static final int f171b;
    private volatile boolean f172c;

    /* renamed from: com.unity3d.player.d.1 */
    class C08281 implements OnSystemUiVisibilityChangeListener {
        final /* synthetic */ View f166a;
        final /* synthetic */ C0831d f167b;

        C08281(C0831d c0831d, View view) {
            this.f167b = c0831d;
            this.f166a = view;
        }

        public final void onSystemUiVisibilityChange(int i) {
            this.f167b.m121a(this.f166a, (int) LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
        }
    }

    /* renamed from: com.unity3d.player.d.2 */
    class C08292 implements Runnable {
        final /* synthetic */ View f168a;
        final /* synthetic */ C0831d f169b;

        C08292(C0831d c0831d, View view) {
            this.f169b = c0831d;
            this.f168a = view;
        }

        public final void run() {
            this.f169b.m125a(this.f168a, this.f169b.f172c);
        }
    }

    static {
        f170a = new SurfaceTexture(-1);
        f171b = C0849q.f205f ? 5894 : 1;
    }

    private void m121a(View view, int i) {
        Handler handler = view.getHandler();
        if (handler == null) {
            m125a(view, this.f172c);
        } else {
            handler.postDelayed(new C08292(this, view), 1000);
        }
    }

    public final void m124a(View view) {
        if (!C0849q.f206g) {
            view.setOnSystemUiVisibilityChangeListener(new C08281(this, view));
        }
    }

    public final void m125a(View view, boolean z) {
        this.f172c = z;
        view.setSystemUiVisibility(this.f172c ? view.getSystemUiVisibility() | f171b : view.getSystemUiVisibility() & (f171b ^ -1));
    }

    public final boolean m126a(Camera camera) {
        try {
            camera.setPreviewTexture(f170a);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public final void m127b(View view) {
        if (!C0849q.f205f && this.f172c) {
            m125a(view, false);
            this.f172c = true;
        }
        m121a(view, (int) LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
    }
}
