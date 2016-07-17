package com.voxelbusters.nativeplugins.utilities;

import android.util.Log;
import android.widget.Toast;
import com.voxelbusters.nativeplugins.NativePluginHelper;

public class Debug {
    public static boolean ENABLED;

    /* renamed from: com.voxelbusters.nativeplugins.utilities.Debug.1 */
    class C09911 implements Runnable {
        private final /* synthetic */ String val$msg;

        C09911(String str) {
            this.val$msg = str;
        }

        public void run() {
            Toast.makeText(NativePluginHelper.getCurrentContext(), this.val$msg, 1).show();
        }
    }

    static {
        ENABLED = false;
    }

    public static void log(String tag, String msg, boolean showToast) {
        if (ENABLED) {
            Log.d(tag, msg);
        }
    }

    public static void log(String tag, String msg) {
        log(tag, msg, false);
    }

    public static void error(String tag, String msg) {
        if (ENABLED) {
            Log.e(tag, msg);
            toast("[" + tag + "]" + msg);
        }
    }

    public static void warning(String tag, String msg) {
        if (ENABLED) {
            Log.w(tag, msg);
        }
    }

    static void toast(String msg) {
        NativePluginHelper.executeOnUIThread(new C09911(msg));
    }
}
