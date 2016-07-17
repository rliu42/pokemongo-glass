package com.nianticproject.holoholo.sfida;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants.SfidaVersion;
import java.util.List;
import spacemadness.com.lunarconsole.C1391R;

@TargetApi(18)
public class SfidaFinderFragment extends Fragment {
    private static String BLE_NAME = null;
    private static final int REQUEST_ENABLE_BT = 10;
    private static final long SCAN_PERIOD = -559038737;
    public static final String TAG;
    private BluetoothAdapter bluetoothAdapter;
    private List<String> bluetoothAddressFilter;
    private boolean isFiltered;
    private boolean isScanningSfida;
    private LeScanCallback leScanCallback;
    private OnDeviceDiscoveredListener onDeviceDiscoveredListener;

    /* renamed from: com.nianticproject.holoholo.sfida.SfidaFinderFragment.1 */
    class C07801 implements LeScanCallback {
        C07801() {
        }

        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (SfidaFinderFragment.this.isScanningSfida) {
                Log.d(SfidaFinderFragment.TAG, "onLeScan() scanRecord : " + SfidaUtils.byteArrayToString(scanRecord));
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                int bondState = device.getBondState();
                if (deviceName == null || !deviceName.contains(SfidaFinderFragment.BLE_NAME)) {
                    Log.d(SfidaFinderFragment.TAG, "deviceName : [" + deviceName + "] was not contained GO PLUS name.");
                    return;
                }
                Log.d(SfidaFinderFragment.TAG, "SFIDA found, device bondState : " + SfidaUtils.getBondStateName(bondState));
                if (deviceAddress != null && SfidaFinderFragment.this.isFiltered && !SfidaFinderFragment.this.isFilteredDevice(deviceAddress)) {
                    Log.d(SfidaFinderFragment.TAG, deviceName + " was not filtered.");
                } else if (SfidaFinderFragment.this.onDeviceDiscoveredListener != null) {
                    SfidaFinderFragment.this.onDeviceDiscoveredListener.onDeviceDiscovered(device, SfidaFinderFragment.this.detectButtonPressed(scanRecord));
                }
            }
        }
    }

    /* renamed from: com.nianticproject.holoholo.sfida.SfidaFinderFragment.2 */
    static /* synthetic */ class C07812 {
        static final /* synthetic */ int[] f33xe9d8fa5;

        static {
            f33xe9d8fa5 = new int[SfidaVersion.values().length];
            try {
                f33xe9d8fa5[SfidaVersion.ALPHA_NO_SEC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f33xe9d8fa5[SfidaVersion.ALPHA_SEC.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f33xe9d8fa5[SfidaVersion.BETA1.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f33xe9d8fa5[SfidaVersion.BETA4.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public interface OnDeviceDiscoveredListener {
        void onDeviceDiscovered(BluetoothDevice bluetoothDevice, boolean z);
    }

    public SfidaFinderFragment() {
        this.isScanningSfida = false;
        this.isFiltered = false;
        this.leScanCallback = new C07801();
    }

    static {
        TAG = SfidaFinderFragment.class.getSimpleName();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        switch (C07812.f33xe9d8fa5[SfidaConstants.SFIDA_VERSION.ordinal()]) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                BLE_NAME = "SFIDA";
                break;
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                BLE_NAME = "EBISU";
                break;
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                BLE_NAME = "EBISU";
                break;
            case Place.TYPE_AQUARIUM /*4*/:
                BLE_NAME = "Pokemon GO Plus";
                break;
        }
        if (checkBluetoothSettingEnable(activity)) {
            init(activity);
        }
    }

    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        scanLeDevice(false);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onDetach() {
        super.onDetach();
        this.bluetoothAddressFilter = null;
    }

    public static SfidaFinderFragment createInstance() {
        return new SfidaFinderFragment();
    }

    public void setOnDeviceDiscoveredListener(OnDeviceDiscoveredListener onDeviceDiscoveredListener) {
        this.onDeviceDiscoveredListener = onDeviceDiscoveredListener;
    }

    public void setSfidaVersion(SfidaVersion version) {
        SfidaConstants.SFIDA_VERSION = version;
        switch (C07812.f33xe9d8fa5[version.ordinal()]) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                BLE_NAME = "SFIDA";
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                BLE_NAME = "EBISU";
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                BLE_NAME = "EBISU";
            case Place.TYPE_AQUARIUM /*4*/:
                BLE_NAME = "Pokemon GO Plus";
            default:
        }
    }

    public void executeFindSfida() {
        if (enableBt() && !this.isScanningSfida) {
            scanLeDevice(true);
        }
    }

    public void executeFindSfida(List<String> bluetoothAddressList) {
        this.bluetoothAddressFilter = bluetoothAddressList;
        this.isFiltered = true;
        executeFindSfida();
    }

    public void cancelFindSfida() {
        scanLeDevice(false);
    }

    private boolean isFilteredDevice(String bluetoothAddress) {
        return this.bluetoothAddressFilter != null && this.bluetoothAddressFilter.contains(bluetoothAddress);
    }

    private boolean detectButtonPressed(byte[] scanRecord) {
        if (scanRecord.length <= 27 || scanRecord[26] == null) {
            return false;
        }
        return true;
    }

    private boolean checkBluetoothSettingEnable(Activity activity) {
        if (activity.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            return true;
        }
        Toast.makeText(activity, "BluetoothLE not supported.", 0).show();
        return false;
    }

    private void init(Activity activity) {
        this.bluetoothAdapter = ((BluetoothManager) activity.getSystemService("bluetooth")).getAdapter();
    }

    private boolean enableBt() {
        if (this.bluetoothAdapter != null && this.bluetoothAdapter.isEnabled()) {
            return true;
        }
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), REQUEST_ENABLE_BT);
        return false;
    }

    private void scanLeDevice(boolean enable) {
        Log.d(TAG, "scanLeDevice() : " + (enable ? "start scan." : "cancel scan."));
        if (enable) {
            this.isScanningSfida = true;
            Log.d(TAG, "scanCallback : " + this.leScanCallback);
            this.bluetoothAdapter.startLeScan(this.leScanCallback);
            return;
        }
        this.isScanningSfida = false;
        this.bluetoothAdapter.stopLeScan(this.leScanCallback);
        this.bluetoothAdapter.cancelDiscovery();
    }
}
