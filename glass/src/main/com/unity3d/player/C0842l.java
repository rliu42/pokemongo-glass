package com.unity3d.player;

import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* renamed from: com.unity3d.player.l */
public final class C0842l implements C0833h {
    private Choreographer f186a;
    private long f187b;
    private FrameCallback f188c;
    private Lock f189d;

    /* renamed from: com.unity3d.player.l.1 */
    class C08411 implements FrameCallback {
        final /* synthetic */ UnityPlayer f184a;
        final /* synthetic */ C0842l f185b;

        C08411(C0842l c0842l, UnityPlayer unityPlayer) {
            this.f185b = c0842l;
            this.f184a = unityPlayer;
        }

        public final void doFrame(long j) {
            UnityPlayer.lockNativeAccess();
            if (C0857v.m185c()) {
                this.f184a.nativeAddVSyncTime(j);
            }
            UnityPlayer.unlockNativeAccess();
            this.f185b.f189d.lock();
            if (this.f185b.f186a != null) {
                this.f185b.f186a.postFrameCallback(this.f185b.f188c);
            }
            this.f185b.f189d.unlock();
        }
    }

    public C0842l() {
        this.f186a = null;
        this.f187b = 0;
        this.f189d = new ReentrantLock();
    }

    public final void m144a() {
        this.f189d.lock();
        if (this.f186a != null) {
            this.f186a.removeFrameCallback(this.f188c);
        }
        this.f186a = null;
        this.f189d.unlock();
    }

    public final void m145a(UnityPlayer unityPlayer) {
        this.f189d.lock();
        if (this.f186a == null) {
            this.f186a = Choreographer.getInstance();
            if (this.f186a != null) {
                C0843m.Log(4, "Choreographer available: Enabling VSYNC timing");
                this.f188c = new C08411(this, unityPlayer);
                this.f186a.postFrameCallback(this.f188c);
            }
        }
        this.f189d.unlock();
    }
}
