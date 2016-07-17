package com.voxelbusters.nativeplugins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.google.gson.Gson;
import com.unity3d.player.UnityPlayer;
import com.voxelbusters.nativeplugins.defines.UnityDefines;
import com.voxelbusters.nativeplugins.utilities.Debug;
import com.voxelbusters.nativeplugins.utilities.StringUtility;
import java.util.ArrayList;
import java.util.HashMap;
import spacemadness.com.lunarconsole.BuildConfig;

public class NativePluginHelper {

    /* renamed from: com.voxelbusters.nativeplugins.NativePluginHelper.1 */
    class C09801 implements Runnable {
        private final /* synthetic */ Intent val$intent;

        C09801(Intent intent) {
            this.val$intent = intent;
        }

        public void run() {
            NativePluginHelper.getCurrentContext().startActivity(this.val$intent);
        }
    }

    public static void sendMessage(String methodName) {
        sendMessage(methodName, BuildConfig.FLAVOR);
    }

    public static void sendMessage(String methodName, String message) {
        if (!StringUtility.isNullOrEmpty(methodName)) {
            Debug.log("UnitySendMessage", "Method Name : " + methodName + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "Message : " + message);
            if (getCurrentContext() != null) {
                UnityPlayer.UnitySendMessage(UnityDefines.NATIVE_BINDING_EVENT_LISTENER, methodName, message);
            }
        }
    }

    public static void sendMessage(String methodName, ArrayList dataList) {
        String message = BuildConfig.FLAVOR;
        if (dataList != null) {
            message = new Gson().toJson((Object) dataList);
        }
        sendMessage(methodName, message);
    }

    public static void sendMessage(String methodName, HashMap dataMap) {
        String message = BuildConfig.FLAVOR;
        if (dataMap != null) {
            message = new Gson().toJson((Object) dataMap);
        }
        sendMessage(methodName, message);
    }

    public static Context getCurrentContext() {
        return UnityPlayer.currentActivity;
    }

    public static Activity getCurrentActivity() {
        return (Activity) getCurrentContext();
    }

    public static void executeOnUIThread(Runnable runnableThread) {
        Activity currentActivity = (Activity) getCurrentContext();
        if (currentActivity != null) {
            currentActivity.runOnUiThread(runnableThread);
        }
    }

    public static void startActivityOnUiThread(Intent intent) {
        executeOnUIThread(new C09801(intent));
    }

    public static boolean isApplicationRunning() {
        return getCurrentContext() != null;
    }
}
