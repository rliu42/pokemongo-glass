package com.unity3d.player;

import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/* renamed from: com.unity3d.player.b */
abstract class C0821b implements Callback {
    private final Activity f148a;
    private final int f149b;
    private SurfaceView f150c;

    /* renamed from: com.unity3d.player.b.1 */
    class C08241 implements Runnable {
        final /* synthetic */ C0821b f164a;

        C08241(C0821b c0821b) {
            this.f164a = c0821b;
        }

        public final void run() {
            if (this.f164a.f150c == null) {
                this.f164a.f150c = new SurfaceView(C0855t.f228a.m177a());
                this.f164a.f150c.getHolder().setType(this.f164a.f149b);
                this.f164a.f150c.getHolder().addCallback(this.f164a);
                C0855t.f228a.m178a(this.f164a.f150c);
                this.f164a.f150c.setVisibility(0);
            }
        }
    }

    /* renamed from: com.unity3d.player.b.2 */
    class C08252 implements Runnable {
        final /* synthetic */ C0821b f165a;

        C08252(C0821b c0821b) {
            this.f165a = c0821b;
        }

        public final void run() {
            if (this.f165a.f150c != null) {
                C0855t.f228a.m179b(this.f165a.f150c);
            }
            this.f165a.f150c = null;
        }
    }

    C0821b(int i) {
        this.f148a = (Activity) C0855t.f228a.m177a();
        this.f149b = 3;
    }

    final void m101a() {
        this.f148a.runOnUiThread(new C08241(this));
    }

    final void m102b() {
        this.f148a.runOnUiThread(new C08252(this));
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
