package com.voxelbusters.nativeplugins.features.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.voxelbusters.nativeplugins.NativePluginHelper;
import com.voxelbusters.nativeplugins.defines.CommonDefines;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.utilities.Debug;
import com.voxelbusters.nativeplugins.utilities.StringUtility;
import java.util.ArrayList;
import java.util.HashMap;

public class UiHandler {
    private static UiHandler INSTANCE;
    private String currentDisplayedUiTag;
    private final ArrayList<String> queueList;
    private final HashMap<String, Bundle> uiElementsMap;

    /* renamed from: com.voxelbusters.nativeplugins.features.ui.UiHandler.1 */
    class C09901 implements Runnable {
        private final /* synthetic */ String val$message;
        private final /* synthetic */ int val$toastLength;

        C09901(String str, int i) {
            this.val$message = str;
            this.val$toastLength = i;
        }

        public void run() {
            Toast.makeText(NativePluginHelper.getCurrentContext(), this.val$message, this.val$toastLength).show();
        }
    }

    public static UiHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UiHandler();
        }
        return INSTANCE;
    }

    private UiHandler() {
        this.uiElementsMap = new HashMap();
        this.queueList = new ArrayList();
    }

    public void showAlertDialogWithMultipleButtons(String title, String message, String buttonsListJson, String tag) {
        Bundle bundle = new Bundle();
        bundle.putString(Keys.TITLE, title);
        bundle.putString(Keys.MESSAGE, message);
        bundle.putStringArray(Keys.BUTTON_LIST, StringUtility.convertJsonStringToStringArray(buttonsListJson));
        bundle.putString(Keys.TAG, tag);
        bundle.putInt(Keys.TYPE, eUiType.ALERT_DIALOG.ordinal());
        pushToActivityQueue(bundle, tag);
    }

    public void showSingleFieldPromptDialog(String title, String message, String placeHolder, boolean useSecureText, String buttonsListJson) {
        Bundle bundle = new Bundle();
        bundle.putString(Keys.TITLE, title);
        bundle.putString(Keys.MESSAGE, message);
        bundle.putStringArray(Keys.BUTTON_LIST, StringUtility.convertJsonStringToStringArray(buttonsListJson));
        bundle.putBoolean(Keys.IS_SECURE, useSecureText);
        bundle.putString(Keys.PLACE_HOLDER_TEXT_1, placeHolder);
        bundle.putInt(Keys.TYPE, eUiType.SINGLE_FIELD_PROMPT.ordinal());
        startActivity(bundle);
    }

    public void showLoginPromptDialog(String title, String message, String placeHolder1, String placeHolder2, String buttonsListJson) {
        Bundle bundle = new Bundle();
        bundle.putString(Keys.TITLE, title);
        bundle.putString(Keys.MESSAGE, message);
        bundle.putStringArray(Keys.BUTTON_LIST, StringUtility.convertJsonStringToStringArray(buttonsListJson));
        bundle.putString(Keys.PLACE_HOLDER_TEXT_1, placeHolder1);
        bundle.putString(Keys.PLACE_HOLDER_TEXT_2, placeHolder2);
        bundle.putInt(Keys.TYPE, eUiType.LOGIN_PROMPT.ordinal());
        startActivity(bundle);
    }

    public void showToast(String message, String lengthType) {
        NativePluginHelper.executeOnUIThread(new C09901(message, lengthType.equals("SHORT") ? 0 : 1));
    }

    public void pushToActivityQueue(Bundle bundleInfo, String tag) {
        if (this.currentDisplayedUiTag != null) {
            Debug.log(CommonDefines.UI_TAG, "Queuing this ui element");
            this.queueList.add(tag);
            return;
        }
        startActivity(bundleInfo);
        this.currentDisplayedUiTag = tag;
    }

    void startActivity(Bundle bundleInfo) {
        Intent intent = new Intent(NativePluginHelper.getCurrentContext(), UiActivity.class);
        intent.putExtras(bundleInfo);
        NativePluginHelper.startActivityOnUiThread(intent);
    }

    public void onFinish(String tag) {
        this.currentDisplayedUiTag = null;
        if (this.queueList.size() > 0) {
            String newTag = (String) this.queueList.remove(this.queueList.size() - 1);
            pushToActivityQueue((Bundle) this.uiElementsMap.get(newTag), newTag);
        }
    }
}
