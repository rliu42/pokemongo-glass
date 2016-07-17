package com.google.unity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;
import com.google.android.gms.location.places.Place;
import com.unity3d.player.UnityPlayer;
import spacemadness.com.lunarconsole.C1391R;

public class GoogleUnityActivity extends Activity {
    private static final int NAVIGATION_BAR_TIMEOUT_MS = 2000;
    static final String TAG;
    protected AndroidInputListener mAndroidInputListener;
    protected AndroidLifecycleListener mAndroidLifecycleListener;
    private View mAndroidView;
    private PopupWindow mPopupWindow;
    protected UnityPlayer mUnityPlayer;
    private boolean shouldUseImmersiveMode;

    /* renamed from: com.google.unity.GoogleUnityActivity.1 */
    class C07211 implements OnTouchListener {
        C07211() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return GoogleUnityActivity.this.onTouchEvent(event);
        }
    }

    /* renamed from: com.google.unity.GoogleUnityActivity.2 */
    class C07222 implements OnGenericMotionListener {
        C07222() {
        }

        public boolean onGenericMotion(View v, MotionEvent event) {
            return GoogleUnityActivity.this.onGenericMotionEvent(event);
        }
    }

    /* renamed from: com.google.unity.GoogleUnityActivity.3 */
    class C07233 implements OnKeyListener {
        C07233() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (event.getAction()) {
                case C1391R.styleable.AdsAttrs_adSize /*0*/:
                    return GoogleUnityActivity.this.onKeyDown(keyCode, event);
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    return GoogleUnityActivity.this.onKeyUp(keyCode, event);
                default:
                    return GoogleUnityActivity.this.injectUnityEvent(event);
            }
        }
    }

    /* renamed from: com.google.unity.GoogleUnityActivity.4 */
    class C07244 implements Runnable {
        final /* synthetic */ int val$layoutResId;
        final /* synthetic */ Activity val$self;

        C07244(Activity activity, int i) {
            this.val$self = activity;
            this.val$layoutResId = i;
        }

        public void run() {
            if (GoogleUnityActivity.this.mPopupWindow != null) {
                GoogleUnityActivity.this.mPopupWindow.dismiss();
                GoogleUnityActivity.this.mPopupWindow = null;
            }
            GoogleUnityActivity.this.mPopupWindow = new PopupWindow(this.val$self);
            GoogleUnityActivity.this.mPopupWindow.setWindowLayoutMode(-1, -1);
            GoogleUnityActivity.this.mPopupWindow.setClippingEnabled(false);
            GoogleUnityActivity.this.mPopupWindow.setBackgroundDrawable(null);
            GoogleUnityActivity.this.mAndroidView = LayoutInflater.from(this.val$self).inflate(this.val$layoutResId, null);
            GoogleUnityActivity.this.mPopupWindow.setContentView(GoogleUnityActivity.this.mAndroidView);
            GoogleUnityActivity.this.mPopupWindow.setTouchable(false);
            GoogleUnityActivity.this.mPopupWindow.showAtLocation(this.val$self.getWindow().getDecorView(), 80, 0, 0);
        }
    }

    /* renamed from: com.google.unity.GoogleUnityActivity.5 */
    class C07265 implements OnSystemUiVisibilityChangeListener {
        final /* synthetic */ Handler val$handler;

        /* renamed from: com.google.unity.GoogleUnityActivity.5.1 */
        class C07251 implements Runnable {
            C07251() {
            }

            public void run() {
                GoogleUnityActivity.this.setImmersiveMode();
            }
        }

        C07265(Handler handler) {
            this.val$handler = handler;
        }

        public void onSystemUiVisibilityChange(int visibility) {
            if ((visibility & 2) == 0) {
                this.val$handler.postDelayed(new C07251(), 2000);
            }
            if (GoogleUnityActivity.this.mAndroidInputListener != null) {
                GoogleUnityActivity.this.mAndroidInputListener.onSystemUiVisibilityChange(visibility);
            }
        }
    }

    public interface AndroidInputListener {
        boolean onGenericMotionEvent(MotionEvent motionEvent);

        boolean onKeyDown(int i, KeyEvent keyEvent);

        boolean onKeyUp(int i, KeyEvent keyEvent);

        void onSystemUiVisibilityChange(int i);

        boolean onTouchEvent(MotionEvent motionEvent);
    }

    public interface AndroidLifecycleListener {
        void onActivityResult(int i, int i2, Intent intent);

        void onPause();

        void onResume();
    }

    static {
        TAG = GoogleUnityActivity.class.getSimpleName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().takeSurface(null);
        getWindow().setFormat(4);
        this.mUnityPlayer = new UnityPlayer(this);
        if (this.mUnityPlayer.getSettings().getBoolean("hide_status_bar", true)) {
            getWindow().setFlags(Place.TYPE_SUBLOCALITY_LEVEL_2, Place.TYPE_SUBLOCALITY_LEVEL_2);
        }
        this.mUnityPlayer.setOnTouchListener(new C07211());
        this.mUnityPlayer.setOnGenericMotionListener(new C07222());
        this.mUnityPlayer.setOnKeyListener(new C07233());
        setContentView(this.mUnityPlayer);
        this.mUnityPlayer.requestFocus();
        this.shouldUseImmersiveMode = false;
        try {
            this.shouldUseImmersiveMode = getPackageManager().getApplicationInfo(getPackageName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS).metaData.getBoolean("IMMERSIVE_MODE");
        } catch (NameNotFoundException e) {
        } catch (NullPointerException e2) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e2.getMessage());
        }
        if (this.shouldUseImmersiveMode && VERSION.SDK_INT < 19) {
            startImmersiveMode();
        }
    }

    public void showAndroidViewLayer(int layoutResId) {
        runOnUiThread(new C07244(this, layoutResId));
    }

    public View getAndroidViewLayer() {
        return this.mAndroidView;
    }

    public UnityPlayer getUnityPlayer() {
        return this.mUnityPlayer;
    }

    public void launchIntent(String packageName, String className, String[] args, int requestcode) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        if (args != null) {
            for (String split : args) {
                String[] keyVal = split.split(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR);
                if (keyVal.length == 2) {
                    intent.putExtra(keyVal[0], keyVal[1]);
                }
            }
        }
        startActivityForResult(intent, requestcode);
    }

    public void attachLifecycleListener(AndroidLifecycleListener listener) {
        this.mAndroidLifecycleListener = listener;
    }

    public void attachInputListener(AndroidInputListener listener) {
        this.mAndroidInputListener = listener;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mAndroidLifecycleListener != null) {
            this.mAndroidLifecycleListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void onDestroy() {
        this.mUnityPlayer.quit();
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        if (this.mAndroidLifecycleListener != null) {
            this.mAndroidLifecycleListener.onPause();
        }
        this.mUnityPlayer.pause();
    }

    protected void onResume() {
        super.onResume();
        if (this.mAndroidLifecycleListener != null) {
            this.mAndroidLifecycleListener.onResume();
        }
        this.mUnityPlayer.resume();
    }

    public void logAndroidErrorMessage(String message) {
        Log.e(getPackageName(), message);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mUnityPlayer.configurationChanged(newConfig);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.mUnityPlayer.windowFocusChanged(hasFocus);
        if (hasFocus && this.shouldUseImmersiveMode) {
            setImmersiveMode();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 2) {
            return injectUnityEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mAndroidInputListener == null || !this.mAndroidInputListener.onKeyUp(keyCode, event)) {
            return injectUnityEvent(event);
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mAndroidInputListener == null || !this.mAndroidInputListener.onKeyDown(keyCode, event)) {
            return injectUnityEvent(event);
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mAndroidInputListener == null || !this.mAndroidInputListener.onTouchEvent(event)) {
            return injectUnityEvent(event);
        }
        return true;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if (this.mAndroidInputListener == null || !this.mAndroidInputListener.onGenericMotionEvent(event)) {
            return injectUnityEvent(event);
        }
        return true;
    }

    public boolean injectUnityEvent(InputEvent event) {
        return this.mUnityPlayer.injectEvent(event);
    }

    private void startImmersiveMode() {
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new C07265(new Handler()));
    }

    @TargetApi(19)
    private void setImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(5894);
    }
}
