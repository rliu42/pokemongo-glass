package com.google.android.gms.internal;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ConsoleMessage.MessageLevel;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebView.WebViewTransport;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import org.apache.commons.io.FileUtils;
import spacemadness.com.lunarconsole.C1391R;

@zzgr
public class zzjf extends WebChromeClient {
    private final zziz zzoM;

    /* renamed from: com.google.android.gms.internal.zzjf.1 */
    static class C05861 implements OnCancelListener {
        final /* synthetic */ JsResult zzKD;

        C05861(JsResult jsResult) {
            this.zzKD = jsResult;
        }

        public void onCancel(DialogInterface dialog) {
            this.zzKD.cancel();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjf.2 */
    static class C05872 implements OnClickListener {
        final /* synthetic */ JsResult zzKD;

        C05872(JsResult jsResult) {
            this.zzKD = jsResult;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.zzKD.cancel();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjf.3 */
    static class C05883 implements OnClickListener {
        final /* synthetic */ JsResult zzKD;

        C05883(JsResult jsResult) {
            this.zzKD = jsResult;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.zzKD.confirm();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjf.4 */
    static class C05894 implements OnCancelListener {
        final /* synthetic */ JsPromptResult zzKE;

        C05894(JsPromptResult jsPromptResult) {
            this.zzKE = jsPromptResult;
        }

        public void onCancel(DialogInterface dialog) {
            this.zzKE.cancel();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjf.5 */
    static class C05905 implements OnClickListener {
        final /* synthetic */ JsPromptResult zzKE;

        C05905(JsPromptResult jsPromptResult) {
            this.zzKE = jsPromptResult;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.zzKE.cancel();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjf.6 */
    static class C05916 implements OnClickListener {
        final /* synthetic */ JsPromptResult zzKE;
        final /* synthetic */ EditText zzKF;

        C05916(JsPromptResult jsPromptResult, EditText editText) {
            this.zzKE = jsPromptResult;
            this.zzKF = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.zzKE.confirm(this.zzKF.getText().toString());
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjf.7 */
    static /* synthetic */ class C05927 {
        static final /* synthetic */ int[] zzKG;

        static {
            zzKG = new int[MessageLevel.values().length];
            try {
                zzKG[MessageLevel.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                zzKG[MessageLevel.WARNING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                zzKG[MessageLevel.LOG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                zzKG[MessageLevel.TIP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                zzKG[MessageLevel.DEBUG.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public zzjf(zziz com_google_android_gms_internal_zziz) {
        this.zzoM = com_google_android_gms_internal_zziz;
    }

    private static void zza(Builder builder, String str, JsResult jsResult) {
        builder.setMessage(str).setPositiveButton(17039370, new C05883(jsResult)).setNegativeButton(17039360, new C05872(jsResult)).setOnCancelListener(new C05861(jsResult)).create().show();
    }

    private static void zza(Context context, Builder builder, String str, String str2, JsPromptResult jsPromptResult) {
        View linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        View textView = new TextView(context);
        textView.setText(str);
        View editText = new EditText(context);
        editText.setText(str2);
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        builder.setView(linearLayout).setPositiveButton(17039370, new C05916(jsPromptResult, editText)).setNegativeButton(17039360, new C05905(jsPromptResult)).setOnCancelListener(new C05894(jsPromptResult)).create().show();
    }

    private final Context zzc(WebView webView) {
        if (!(webView instanceof zziz)) {
            return webView.getContext();
        }
        zziz com_google_android_gms_internal_zziz = (zziz) webView;
        Context zzgZ = com_google_android_gms_internal_zziz.zzgZ();
        return zzgZ == null ? com_google_android_gms_internal_zziz.getContext() : zzgZ;
    }

    private final boolean zzhE() {
        return zzp.zzbv().zza(this.zzoM.getContext().getPackageManager(), this.zzoM.getContext().getPackageName(), "android.permission.ACCESS_FINE_LOCATION") || zzp.zzbv().zza(this.zzoM.getContext().getPackageManager(), this.zzoM.getContext().getPackageName(), "android.permission.ACCESS_COARSE_LOCATION");
    }

    public final void onCloseWindow(WebView webView) {
        if (webView instanceof zziz) {
            zzd zzhc = ((zziz) webView).zzhc();
            if (zzhc == null) {
                zzb.zzaH("Tried to close an AdWebView not associated with an overlay.");
                return;
            } else {
                zzhc.close();
                return;
            }
        }
        zzb.zzaH("Tried to close a WebView that wasn't an AdWebView.");
    }

    public final boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        String str = "JS: " + consoleMessage.message() + " (" + consoleMessage.sourceId() + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR + consoleMessage.lineNumber() + ")";
        if (str.contains("Application Cache")) {
            return super.onConsoleMessage(consoleMessage);
        }
        switch (C05927.zzKG[consoleMessage.messageLevel().ordinal()]) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                zzb.m36e(str);
                break;
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                zzb.zzaH(str);
                break;
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
            case Place.TYPE_AQUARIUM /*4*/:
                zzb.zzaG(str);
                break;
            case Place.TYPE_ART_GALLERY /*5*/:
                zzb.zzaF(str);
                break;
            default:
                zzb.zzaG(str);
                break;
        }
        return super.onConsoleMessage(consoleMessage);
    }

    public final boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        WebViewTransport webViewTransport = (WebViewTransport) resultMsg.obj;
        WebView webView = new WebView(view.getContext());
        webView.setWebViewClient(this.zzoM.zzhe());
        webViewTransport.setWebView(webView);
        resultMsg.sendToTarget();
        return true;
    }

    public final void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, QuotaUpdater quotaUpdater) {
        long j = 5242880 - totalUsedQuota;
        if (j <= 0) {
            quotaUpdater.updateQuota(currentQuota);
            return;
        }
        if (currentQuota == 0) {
            if (estimatedSize > j || estimatedSize > FileUtils.ONE_MB) {
                estimatedSize = 0;
            }
        } else if (estimatedSize == 0) {
            estimatedSize = Math.min(Math.min(131072, j) + currentQuota, FileUtils.ONE_MB);
        } else {
            if (estimatedSize <= Math.min(FileUtils.ONE_MB - currentQuota, j)) {
                currentQuota += estimatedSize;
            }
            estimatedSize = currentQuota;
        }
        quotaUpdater.updateQuota(estimatedSize);
    }

    public final void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
        if (callback != null) {
            callback.invoke(origin, zzhE(), true);
        }
    }

    public final void onHideCustomView() {
        zzd zzhc = this.zzoM.zzhc();
        if (zzhc == null) {
            zzb.zzaH("Could not get ad overlay when hiding custom view.");
        } else {
            zzhc.zzeD();
        }
    }

    public final boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
        return zza(zzc(webView), url, message, null, result, null, false);
    }

    public final boolean onJsBeforeUnload(WebView webView, String url, String message, JsResult result) {
        return zza(zzc(webView), url, message, null, result, null, false);
    }

    public final boolean onJsConfirm(WebView webView, String url, String message, JsResult result) {
        return zza(zzc(webView), url, message, null, result, null, false);
    }

    public final boolean onJsPrompt(WebView webView, String url, String message, String defaultValue, JsPromptResult result) {
        return zza(zzc(webView), url, message, defaultValue, null, result, true);
    }

    public final void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, QuotaUpdater quotaUpdater) {
        long j = 131072 + spaceNeeded;
        if (5242880 - totalUsedQuota < j) {
            quotaUpdater.updateQuota(0);
        } else {
            quotaUpdater.updateQuota(j);
        }
    }

    public final void onShowCustomView(View view, CustomViewCallback customViewCallback) {
        zza(view, -1, customViewCallback);
    }

    protected final void zza(View view, int i, CustomViewCallback customViewCallback) {
        zzd zzhc = this.zzoM.zzhc();
        if (zzhc == null) {
            zzb.zzaH("Could not get ad overlay when showing custom view.");
            customViewCallback.onCustomViewHidden();
            return;
        }
        zzhc.zza(view, customViewCallback);
        zzhc.setRequestedOrientation(i);
    }

    protected boolean zza(Context context, String str, String str2, String str3, JsResult jsResult, JsPromptResult jsPromptResult, boolean z) {
        try {
            Builder builder = new Builder(context);
            builder.setTitle(str);
            if (z) {
                zza(context, builder, str2, str3, jsPromptResult);
            } else {
                zza(builder, str2, jsResult);
            }
        } catch (Throwable e) {
            zzb.zzd("Fail to display Dialog.", e);
        }
        return true;
    }
}
