package com.nianticproject.holoholo.sfida.service;

import android.util.Log;

public class SfidaButtonDetector {
    private static final int NOTIFY_BIT_SIZE = 10;
    private static final int SFIDA_BUTTON_NOTIFY_BYTE_ARRAY_SIZE = 2;
    private static final String TAG;
    int count;
    private OnClickListener onClickListener;
    private ButtonStatus preButtonStatus;

    private static class ButtonStatus {
        int clickedCount;
        int pressedCount;
        int releasedCount;
        boolean[] value;

        public ButtonStatus() {
            this.value = new boolean[SfidaButtonDetector.NOTIFY_BIT_SIZE];
        }
    }

    public interface OnClickListener {
        void onClick();

        void onPress();

        void onRelease();
    }

    static {
        TAG = SfidaButtonDetector.class.getSimpleName();
    }

    public SfidaButtonDetector() {
        this.preButtonStatus = new ButtonStatus();
        this.count = 0;
    }

    public void setOnclickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void release() {
        setOnclickListener(null);
    }

    public void setButtonStatus(byte[] buttonValue, boolean isOnBackground) {
        if (buttonValue != null && buttonValue.length == SFIDA_BUTTON_NOTIFY_BYTE_ARRAY_SIZE) {
            byte count;
            int i;
            boolean z;
            boolean[] result = new boolean[NOTIFY_BIT_SIZE];
            Integer value = Integer.valueOf(buttonValue[0]);
            for (count = (byte) 0; count < (byte) 2; count = (byte) (count + 1)) {
                i = 1 - count;
                if ((value.intValue() & (1 << count)) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                result[i] = z;
            }
            value = Integer.valueOf(buttonValue[1]);
            for (count = (byte) 0; count < 8; count = (byte) (count + 1)) {
                i = 9 - count;
                if ((value.intValue() & (1 << count)) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                result[i] = z;
            }
            this.preButtonStatus = getButtonStatus(result, isOnBackground);
        }
    }

    private ButtonStatus getButtonStatus(boolean[] states, boolean isOnBackground) {
        ButtonStatus buttonStatus = new ButtonStatus();
        boolean isPreValuePressed = this.preButtonStatus.value[9];
        Log.d(TAG, "isPreValuePressed start with : " + isPreValuePressed);
        for (boolean isPressed : states) {
            if (isPressed) {
                if (!isPreValuePressed) {
                    buttonStatus.pressedCount++;
                    if (this.onClickListener != null) {
                        this.onClickListener.onPress();
                    }
                }
            } else if (isPreValuePressed) {
                buttonStatus.clickedCount++;
                buttonStatus.releasedCount++;
                if (this.onClickListener != null) {
                    this.onClickListener.onClick();
                }
                if (this.onClickListener != null) {
                    this.onClickListener.onRelease();
                }
            }
            isPreValuePressed = isPressed;
        }
        buttonStatus.value = states;
        return buttonStatus;
    }

    private boolean[] createTestCase() {
        boolean[] result = new boolean[NOTIFY_BIT_SIZE];
        if (this.count == 0) {
            result[0] = false;
            result[1] = false;
            result[SFIDA_BUTTON_NOTIFY_BYTE_ARRAY_SIZE] = false;
            result[3] = false;
            result[4] = false;
            result[5] = false;
            result[6] = false;
            result[7] = false;
            result[8] = false;
            result[9] = true;
        } else if (this.count == 1) {
            result[0] = false;
            result[1] = true;
            result[SFIDA_BUTTON_NOTIFY_BYTE_ARRAY_SIZE] = false;
            result[3] = true;
            result[4] = false;
            result[5] = true;
            result[6] = false;
            result[7] = true;
            result[8] = false;
            result[9] = true;
        }
        this.count++;
        return result;
    }
}
