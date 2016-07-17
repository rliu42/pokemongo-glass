package com.google.vr.cardboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class UiLayer {
    private static final int ALIGNMENT_MARKER_LINE_COLOR = -13487566;
    private static final int ALIGNMENT_MARKER_LINE_WIDTH = 4;
    private static final int ICON_WIDTH_DP = 28;
    private static final String TAG;
    private static final float TOUCH_SLOP_FACTOR = 1.5f;
    private View alignmentMarker;
    private ImageView backButton;
    private volatile Runnable backButtonRunnable;
    private final Drawable backIconDrawable;
    private final Context context;
    private volatile boolean isAlignmentMarkerEnabled;
    private volatile boolean isSettingsButtonEnabled;
    private final DisplayMetrics metrics;
    private final RelativeLayout rootLayout;
    private ImageView settingsButton;
    private final Drawable settingsIconDrawable;

    /* renamed from: com.google.vr.cardboard.UiLayer.1 */
    class C07281 implements OnClickListener {
        C07281() {
        }

        public void onClick(View v) {
            UiUtils.launchOrInstallCardboard(v.getContext());
        }
    }

    /* renamed from: com.google.vr.cardboard.UiLayer.2 */
    class C07292 implements OnClickListener {
        C07292() {
        }

        public void onClick(View v) {
            Runnable runnable = UiLayer.this.backButtonRunnable;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    /* renamed from: com.google.vr.cardboard.UiLayer.3 */
    class C07303 implements Runnable {
        final /* synthetic */ ViewGroup val$parentView;

        C07303(ViewGroup viewGroup) {
            this.val$parentView = viewGroup;
        }

        public void run() {
            if (this.val$parentView == null) {
                ((Activity) UiLayer.this.context).addContentView(UiLayer.this.rootLayout, new LayoutParams(-1, -1));
            } else {
                this.val$parentView.addView(UiLayer.this.rootLayout);
            }
        }
    }

    /* renamed from: com.google.vr.cardboard.UiLayer.4 */
    class C07314 implements Runnable {
        final /* synthetic */ boolean val$enabled;

        C07314(boolean z) {
            this.val$enabled = z;
        }

        public void run() {
            UiLayer.this.rootLayout.setVisibility(this.val$enabled ? 0 : UiLayer.ALIGNMENT_MARKER_LINE_WIDTH);
        }
    }

    /* renamed from: com.google.vr.cardboard.UiLayer.5 */
    class C07325 implements Runnable {
        final /* synthetic */ boolean val$enabled;

        C07325(boolean z) {
            this.val$enabled = z;
        }

        public void run() {
            UiLayer.this.settingsButton.setVisibility(this.val$enabled ? 0 : UiLayer.ALIGNMENT_MARKER_LINE_WIDTH);
        }
    }

    /* renamed from: com.google.vr.cardboard.UiLayer.6 */
    class C07336 implements Runnable {
        final /* synthetic */ Runnable val$runnable;

        C07336(Runnable runnable) {
            this.val$runnable = runnable;
        }

        public void run() {
            UiLayer.this.backButton.setVisibility(this.val$runnable == null ? UiLayer.ALIGNMENT_MARKER_LINE_WIDTH : 0);
        }
    }

    /* renamed from: com.google.vr.cardboard.UiLayer.7 */
    class C07347 implements Runnable {
        final /* synthetic */ boolean val$enabled;

        C07347(boolean z) {
            this.val$enabled = z;
        }

        public void run() {
            UiLayer.this.alignmentMarker.setVisibility(this.val$enabled ? 0 : UiLayer.ALIGNMENT_MARKER_LINE_WIDTH);
        }
    }

    static {
        TAG = UiLayer.class.getSimpleName();
    }

    public UiLayer(Context context) {
        this.isSettingsButtonEnabled = true;
        this.isAlignmentMarkerEnabled = false;
        this.backButtonRunnable = null;
        if (context instanceof Activity) {
            this.context = context;
            this.settingsIconDrawable = decodeBitmapFromString(Base64Resources.SETTINGS_BUTTON_PNG_STRING);
            this.backIconDrawable = decodeBitmapFromString(Base64Resources.BACK_BUTTON_PNG_STRING);
            Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            this.metrics = new DisplayMetrics();
            if (VERSION.SDK_INT >= 17) {
                display.getRealMetrics(this.metrics);
            } else {
                display.getMetrics(this.metrics);
            }
            this.rootLayout = new RelativeLayout(context);
            initializeViews();
            return;
        }
        throw new RuntimeException("Context is not an instance of activity: Aborting.");
    }

    private Drawable decodeBitmapFromString(String bitmapString) {
        byte[] decodedBytes = Base64.decode(bitmapString, 0);
        return new BitmapDrawable(this.context.getResources(), BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
    }

    private void initializeViews() {
        int i = 0;
        int touchWidthPx = (int) (((float) ((int) (28.0f * this.metrics.density))) * TOUCH_SLOP_FACTOR);
        this.settingsButton = createButton(this.settingsIconDrawable, this.isSettingsButtonEnabled, 12, 13);
        this.settingsButton.setOnClickListener(new C07281());
        this.rootLayout.addView(this.settingsButton);
        this.backButton = createButton(this.backIconDrawable, getBackButtonEnabled(), 10, 9);
        this.backButton.setOnClickListener(new C07292());
        this.rootLayout.addView(this.backButton);
        this.alignmentMarker = new View(this.context);
        this.alignmentMarker.setBackground(new ColorDrawable(ALIGNMENT_MARKER_LINE_COLOR));
        LayoutParams layoutParams = new LayoutParams((int) (4.0f * this.metrics.density), -1);
        layoutParams.addRule(13);
        layoutParams.setMargins(0, touchWidthPx, 0, touchWidthPx);
        this.alignmentMarker.setLayoutParams(layoutParams);
        View view = this.alignmentMarker;
        if (!this.isAlignmentMarkerEnabled) {
            i = 8;
        }
        view.setVisibility(i);
        this.rootLayout.addView(this.alignmentMarker);
    }

    private ImageView createButton(Drawable iconDrawable, boolean isEnabled, int... layoutParams) {
        int iconWidthPx = (int) (28.0f * this.metrics.density);
        int touchWidthPx = (int) (((float) iconWidthPx) * TOUCH_SLOP_FACTOR);
        int padding = (touchWidthPx - iconWidthPx) / 2;
        ImageView buttonLayout = new ImageView(this.context);
        buttonLayout.setPadding(padding, padding, padding, padding);
        buttonLayout.setImageDrawable(iconDrawable);
        buttonLayout.setScaleType(ScaleType.FIT_CENTER);
        LayoutParams buttonParams = new LayoutParams(touchWidthPx, touchWidthPx);
        for (int layoutParam : layoutParams) {
            buttonParams.addRule(layoutParam);
        }
        buttonLayout.setLayoutParams(buttonParams);
        buttonLayout.setVisibility(isEnabled ? 0 : ALIGNMENT_MARKER_LINE_WIDTH);
        return buttonLayout;
    }

    public void attachUiLayer(ViewGroup parentView) {
        ((Activity) this.context).runOnUiThread(new C07303(parentView));
    }

    public void setEnabled(boolean enabled) {
        ((Activity) this.context).runOnUiThread(new C07314(enabled));
    }

    public void setSettingsButtonEnabled(boolean enabled) {
        this.isSettingsButtonEnabled = enabled;
        ((Activity) this.context).runOnUiThread(new C07325(enabled));
    }

    public void setBackButtonListener(Runnable runnable) {
        this.backButtonRunnable = runnable;
        ((Activity) this.context).runOnUiThread(new C07336(runnable));
    }

    public void setAlignmentMarkerEnabled(boolean enabled) {
        this.isAlignmentMarkerEnabled = enabled;
        ((Activity) this.context).runOnUiThread(new C07347(enabled));
    }

    public boolean getSettingsButtonEnabled() {
        return this.isSettingsButtonEnabled;
    }

    public boolean getBackButtonEnabled() {
        return this.backButtonRunnable != null;
    }

    public boolean getAlignmentMarkerEnabled() {
        return this.isAlignmentMarkerEnabled;
    }
}
