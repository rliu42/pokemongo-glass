package com.google.vr.platform.unity;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.InputDeviceCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.google.unity.GoogleUnityActivity;
import com.google.unity.GoogleUnityActivity.AndroidInputListener;
import com.google.vr.cardboard.NFCUtils;
import com.google.vr.cardboard.UiLayer;
import com.google.vr.cardboard.UiUtils;
import com.unity3d.player.UnityPlayer;

public class UnityVrActivityListener implements AndroidInputListener {
    private static final long NO_DOWNTIME = -1;
    private static final String TAG;
    private static final long TAP_TIME_MS = 50;
    private boolean alignmentMarkerEnabled;
    private final NFCUtils nfcUtils;
    private boolean settingsButtonEnabled;
    private boolean showVrBackButtonOnlyInVR;
    private boolean tapInProgress;
    private boolean tapIsTrigger;
    private int touchX;
    private int touchY;
    private UiLayer uiLayer;
    private final GoogleUnityActivity unityActivity;
    private boolean vrBackButtonEnabled;
    private Runnable vrBackButtonListener;
    private boolean vrModeEnabled;

    /* renamed from: com.google.vr.platform.unity.UnityVrActivityListener.1 */
    class C07371 implements Runnable {
        C07371() {
        }

        public void run() {
            UnityVrActivityListener.vrBackButtonPressed();
        }
    }

    /* renamed from: com.google.vr.platform.unity.UnityVrActivityListener.2 */
    class C07382 implements Runnable {
        final /* synthetic */ long val$time;
        final /* synthetic */ int val$x;
        final /* synthetic */ int val$y;

        C07382(int i, int i2, long j) {
            this.val$x = i;
            this.val$y = i2;
            this.val$time = j;
        }

        public void run() {
            UnityVrActivityListener.this.injectTouchUp(this.val$x, this.val$y, this.val$time);
            UnityVrActivityListener.this.tapInProgress = false;
            if (!UnityVrActivityListener.this.tapIsTrigger) {
                return;
            }
            if (this.val$x != UnityVrActivityListener.this.touchX || this.val$y != UnityVrActivityListener.this.touchY) {
                UnityVrActivityListener.this.injectMouseMove(UnityVrActivityListener.this.touchX, UnityVrActivityListener.this.touchY);
            }
        }
    }

    /* renamed from: com.google.vr.platform.unity.UnityVrActivityListener.3 */
    class C07393 implements Runnable {
        final /* synthetic */ int val$keyCode;

        C07393(int i) {
            this.val$keyCode = i;
        }

        public void run() {
            UnityVrActivityListener.this.injectKeyUp(this.val$keyCode);
        }
    }

    private static native void setApplicationState(ClassLoader classLoader, Context context);

    private static native void vrBackButtonPressed();

    static {
        System.loadLibrary("vrunity");
        TAG = UnityVrActivityListener.class.getSimpleName();
    }

    public static void setUnityApplicationState() {
        Activity activity = UnityPlayer.currentActivity;
        setApplicationState(activity.getClass().getClassLoader(), activity.getApplicationContext());
    }

    public static float[] getDisplayMetrics() {
        Display display = UnityPlayer.currentActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            display.getRealMetrics(displayMetrics);
        } catch (NoSuchMethodError e) {
            display.getMetrics(displayMetrics);
        }
        return new float[]{(float) displayMetrics.widthPixels, (float) displayMetrics.heightPixels, displayMetrics.xdpi, displayMetrics.ydpi};
    }

    public UnityVrActivityListener() {
        this.nfcUtils = new NFCUtils();
        this.showVrBackButtonOnlyInVR = true;
        this.vrModeEnabled = true;
        this.settingsButtonEnabled = true;
        this.alignmentMarkerEnabled = true;
        this.vrBackButtonEnabled = true;
        this.tapIsTrigger = false;
        this.tapInProgress = false;
        this.touchX = 0;
        this.touchY = 0;
        this.vrBackButtonListener = new C07371();
        this.unityActivity = (GoogleUnityActivity) UnityPlayer.currentActivity;
        this.unityActivity.attachInputListener(this);
        this.nfcUtils.onCreate(this.unityActivity);
        this.uiLayer = new UiLayer(this.unityActivity);
        this.uiLayer.attachUiLayer(null);
        this.uiLayer.setEnabled(true);
        setVRModeEnabled(this.vrModeEnabled);
        onPause(false);
    }

    public void onPause(boolean paused) {
        if (paused) {
            this.nfcUtils.onPause(this.unityActivity);
        } else {
            this.nfcUtils.onResume(this.unityActivity);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return (keyCode == 24 || keyCode == 25) && this.vrModeEnabled;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == 24 || keyCode == 25) && this.vrModeEnabled;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.tapIsTrigger || !this.vrModeEnabled) {
            return false;
        }
        if (event.getAction() == 0) {
            injectSingleTap();
        }
        return true;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    public void onSystemUiVisibilityChange(int visibility) {
        if (this.tapIsTrigger && this.vrModeEnabled) {
            injectSingleTap();
        }
    }

    public void launchConfigureActivity() {
        UiUtils.launchOrInstallCardboard(this.unityActivity, false);
    }

    public void setVRModeEnabled(boolean enabled) {
        this.vrModeEnabled = enabled;
        setSettingsButtonEnabled(this.settingsButtonEnabled);
        setAlignmentMarkerEnabled(this.alignmentMarkerEnabled);
        setVRBackButtonEnabled(this.vrBackButtonEnabled);
    }

    public void setSettingsButtonEnabled(boolean enabled) {
        this.settingsButtonEnabled = enabled;
        UiLayer uiLayer = this.uiLayer;
        boolean z = this.settingsButtonEnabled && this.vrModeEnabled;
        uiLayer.setSettingsButtonEnabled(z);
    }

    public void setAlignmentMarkerEnabled(boolean enabled) {
        this.alignmentMarkerEnabled = enabled;
        UiLayer uiLayer = this.uiLayer;
        boolean z = this.alignmentMarkerEnabled && this.vrModeEnabled;
        uiLayer.setAlignmentMarkerEnabled(z);
    }

    public void setVRBackButtonEnabled(boolean enabled) {
        this.vrBackButtonEnabled = enabled;
        Runnable backButtonListener = null;
        if (this.vrBackButtonEnabled && (this.vrModeEnabled || !this.showVrBackButtonOnlyInVR)) {
            backButtonListener = this.vrBackButtonListener;
        }
        this.uiLayer.setBackButtonListener(backButtonListener);
    }

    public void setShowVrBackButtonOnlyInVR(boolean only) {
        this.showVrBackButtonOnlyInVR = only;
        setVRBackButtonEnabled(this.vrBackButtonEnabled);
    }

    public void setTapIsTrigger(boolean enabled) {
        this.tapIsTrigger = enabled;
    }

    public void setTouchCoordinates(int x, int y) {
        this.touchX = x;
        this.touchY = y;
        if (this.tapIsTrigger && !this.tapInProgress) {
            injectMouseMove(x, y);
        }
    }

    public long injectTouchDown(int x, int y) {
        return injectMotionEventInternal(0, x, y, InputDeviceCompat.SOURCE_TOUCHSCREEN, NO_DOWNTIME);
    }

    public void injectTouchUp(int x, int y, long downTime) {
        injectMotionEventInternal(1, x, y, InputDeviceCompat.SOURCE_TOUCHSCREEN, downTime);
    }

    public void injectMouseMove(int x, int y) {
        injectMotionEventInternal(7, x, y, InputDeviceCompat.SOURCE_MOUSE, NO_DOWNTIME);
    }

    private long injectMotionEventInternal(int eventID, int x, int y, int source, long downTime) {
        long eventTime = SystemClock.uptimeMillis();
        if (downTime == NO_DOWNTIME) {
            downTime = eventTime;
        }
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, eventID, (float) x, (float) y, 0);
        event.setSource(source);
        this.unityActivity.injectUnityEvent(event);
        event.recycle();
        return eventTime;
    }

    public void injectSingleTap() {
        if (!this.tapInProgress) {
            this.tapInProgress = true;
            int x = this.touchX;
            int y = this.touchY;
            this.unityActivity.getUnityPlayer().postDelayed(new C07382(x, y, injectTouchDown(x, y)), TAP_TIME_MS);
        }
    }

    public void injectKeyDown(int keyCode) {
        this.unityActivity.injectUnityEvent(new KeyEvent(0, keyCode));
    }

    public void injectKeyUp(int keyCode) {
        this.unityActivity.injectUnityEvent(new KeyEvent(1, keyCode));
    }

    public void injectKeyPress(int keyCode) {
        injectKeyDown(keyCode);
        this.unityActivity.getUnityPlayer().postDelayed(new C07393(keyCode), TAP_TIME_MS);
    }
}
