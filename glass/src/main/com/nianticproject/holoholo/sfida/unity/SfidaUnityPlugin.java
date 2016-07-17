package com.nianticproject.holoholo.sfida.unity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaFinderFragment;
import com.nianticproject.holoholo.sfida.SfidaFinderFragment.OnDeviceDiscoveredListener;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.nianticproject.holoholo.sfida.SfidaUtils;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants;
import com.nianticproject.holoholo.sfida.service.SfidaButtonDetector.OnClickListener;
import com.nianticproject.holoholo.sfida.service.SfidaService;
import com.nianticproject.holoholo.sfida.service.SfidaService.LocalBinder;
import com.unity3d.player.UnityPlayer;
import java.util.UUID;
import spacemadness.com.lunarconsole.BuildConfig;
import spacemadness.com.lunarconsole.C1391R;

public class SfidaUnityPlugin implements UnityInterface {
    private static final String TAG;
    private static final int TIMEOUT = 800;
    private static final String UNITY_GAME_OBJECT = "AndroidSfidaConnection";
    private static final String UNITY_METHOD_ENTER_ENCOUNTER_STATE = "EnterEncounterState";
    private static final String UNITY_METHOD_HACK_POKESTOP = "HackPokestop";
    private static final String UNITY_METHOD_NOTIFY_CONNECTED = "OnSfidaConnected";
    private static final String UNITY_METHOD_NOTIFY_DISCONNECTED = "OnSfidaDisconnected";
    private static final String UNITY_METHOD_THROW_POKEBALL = "ThrowPokeball";
    private static SfidaUnityPlugin instance;
    private Activity activity;
    private BluetoothDevice device;
    private BroadcastReceiver gattUpdateReceiver;
    private Handler handler;
    private boolean isReceiverRegistered;
    private boolean isServiceBound;
    private boolean isSfidaConnected;
    private PeriodicVibrateRunnable runnable;
    private ServiceConnection serviceConnection;
    private SfidaService sfidaService;

    /* renamed from: com.nianticproject.holoholo.sfida.unity.SfidaUnityPlugin.1 */
    class C07911 implements ServiceConnection {
        C07911() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(SfidaUnityPlugin.TAG, "onServiceConnected()");
            SfidaUnityPlugin.this.sfidaService = ((LocalBinder) service).getService();
            if (SfidaUnityPlugin.this.sfidaService.initialize()) {
                SfidaUnityPlugin.this.sfidaService.connect(SfidaUnityPlugin.this.device);
                SfidaUnityPlugin.this.isServiceBound = true;
                return;
            }
            Log.e(SfidaUnityPlugin.TAG, "Unable to initialize Bluetooth");
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(SfidaUnityPlugin.TAG, "[BLE] onServiceDisconnected()");
            SfidaUnityPlugin.this.sfidaService = null;
        }
    }

    /* renamed from: com.nianticproject.holoholo.sfida.unity.SfidaUnityPlugin.2 */
    class C07922 extends BroadcastReceiver {
        C07922() {
        }

        public void onReceive(Context context, Intent intent) {
            SfidaUnityPlugin.this.onBroadcastUpdated(intent);
        }
    }

    /* renamed from: com.nianticproject.holoholo.sfida.unity.SfidaUnityPlugin.3 */
    class C07933 implements Runnable {
        C07933() {
        }

        public void run() {
            Toast.makeText(SfidaUnityPlugin.this.getActivity(), "Finding Pok\u00e9mon GO Plus.", 0).show();
        }
    }

    /* renamed from: com.nianticproject.holoholo.sfida.unity.SfidaUnityPlugin.4 */
    class C07944 extends PokestopClickCallback {
        C07944(String pokestopId) {
            super(pokestopId);
        }

        public void onClick() {
            super.onClick();
            SfidaUnityPlugin.this.sfidaService.setOnClickSfidaListener(null);
            UnityPlayer.UnitySendMessage(SfidaUnityPlugin.UNITY_GAME_OBJECT, SfidaUnityPlugin.UNITY_METHOD_HACK_POKESTOP, getId());
        }
    }

    /* renamed from: com.nianticproject.holoholo.sfida.unity.SfidaUnityPlugin.5 */
    class C07955 extends EncounterPokemonClickCallback {
        C07955(String pokemonId) {
            super(pokemonId);
        }

        public void onClick() {
            super.onClick();
            SfidaUnityPlugin.this.sfidaService.setOnClickSfidaListener(null);
            UnityPlayer.UnitySendMessage(SfidaUnityPlugin.UNITY_GAME_OBJECT, SfidaUnityPlugin.UNITY_METHOD_ENTER_ENCOUNTER_STATE, getId());
        }
    }

    /* renamed from: com.nianticproject.holoholo.sfida.unity.SfidaUnityPlugin.6 */
    class C07966 extends EncounterPokemonClickCallback {
        C07966(String pokemonId) {
            super(pokemonId);
        }

        public void onClick() {
            super.onClick();
            SfidaUnityPlugin.this.sfidaService.setOnClickSfidaListener(null);
            UnityPlayer.UnitySendMessage(SfidaUnityPlugin.UNITY_GAME_OBJECT, SfidaUnityPlugin.UNITY_METHOD_ENTER_ENCOUNTER_STATE, getId());
        }
    }

    /* renamed from: com.nianticproject.holoholo.sfida.unity.SfidaUnityPlugin.7 */
    class C07977 extends EncounterPokemonClickCallback {
        C07977(String pokemonId) {
            super(pokemonId);
        }

        public void onClick() {
            super.onClick();
            SfidaUnityPlugin.this.sfidaService.setOnClickSfidaListener(null);
            UnityPlayer.UnitySendMessage(SfidaUnityPlugin.UNITY_GAME_OBJECT, SfidaUnityPlugin.UNITY_METHOD_ENTER_ENCOUNTER_STATE, getId());
        }
    }

    /* renamed from: com.nianticproject.holoholo.sfida.unity.SfidaUnityPlugin.8 */
    class C07988 implements OnClickListener {
        C07988() {
        }

        public void onClick() {
            SfidaUnityPlugin.this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getThrewPokeball());
            SfidaUnityPlugin.this.sfidaService.setOnClickSfidaListener(null);
            UnityPlayer.UnitySendMessage(SfidaUnityPlugin.UNITY_GAME_OBJECT, SfidaUnityPlugin.UNITY_METHOD_THROW_POKEBALL, BuildConfig.FLAVOR);
        }

        public void onPress() {
        }

        public void onRelease() {
        }
    }

    private class PeriodicVibrateRunnable implements Runnable {
        Handler handler;

        public PeriodicVibrateRunnable(Handler handler) {
            this.handler = handler;
        }

        public void run() {
            Log.d(SfidaUnityPlugin.TAG, getClass().getName() + " run()");
            SfidaUnityPlugin.this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getCaptureSucceed());
            if (this.handler != null) {
                this.handler.postDelayed(this, 1000);
            }
        }

        public void stop() {
            this.handler.removeCallbacks(null);
            this.handler = null;
        }
    }

    private class SfidaDiscoveredListener implements OnDeviceDiscoveredListener {
        private SfidaDiscoveredListener() {
        }

        public void onDeviceDiscovered(BluetoothDevice discoveredDevice, boolean pressed) {
            Toast.makeText(SfidaUnityPlugin.this.getActivity(), "Pok\u00e9mon GO Plus discovered.", 0).show();
            SfidaUnityPlugin.this.stopScanSfida();
            SfidaUnityPlugin.this.startSfidaConnection(discoveredDevice);
        }
    }

    static {
        TAG = SfidaUnityPlugin.class.getSimpleName();
        instance = new SfidaUnityPlugin();
    }

    public SfidaUnityPlugin() {
        this.isServiceBound = false;
        this.isReceiverRegistered = false;
        this.isSfidaConnected = false;
        this.serviceConnection = new C07911();
        this.gattUpdateReceiver = new C07922();
    }

    public static SfidaUnityPlugin getInstance() {
        return instance;
    }

    public boolean init() {
        Log.d(TAG, "init()");
        if (VERSION.SDK_INT < 18) {
            return false;
        }
        addSfidaFinderFragment();
        registerReceiver();
        return true;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void connect() {
        Log.d(TAG, "connect()");
        getActivity().runOnUiThread(new C07933());
        startScanSfida();
    }

    public void disconnect() {
        Log.d(TAG, "disconnect()");
        if (this.sfidaService != null && this.isSfidaConnected) {
            this.sfidaService.disconnectBluetooth();
        }
    }

    public void releaseSfida() {
        Log.d(TAG, "releaseSfida()");
        if (this.isReceiverRegistered) {
            getActivity().unregisterReceiver(this.gattUpdateReceiver);
        } else {
            Log.d(TAG, "releaseSfida() isNotReceiverRegistered");
        }
        if (this.isServiceBound) {
            getActivity().unbindService(this.serviceConnection);
            this.isServiceBound = false;
        }
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
            this.handler = null;
        }
    }

    public boolean notifyReachedPokestop(String pokestopId) {
        Log.d(TAG, "notifyReachedPokestop() id : " + pokestopId);
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(new C07944(pokestopId));
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getReachedPokestop());
    }

    public boolean notifyRewardItems(String itemCount) {
        Log.d(TAG, "notifyRewardItems() count : " + itemCount);
        if (isEnableSfida()) {
            return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getRewardItems(Integer.valueOf(itemCount).intValue()));
        }
        return false;
    }

    public boolean notifySpawnedPokemon(String encounterId) {
        Log.d(TAG, "notifySpawnedPokemon() id : " + encounterId);
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(new C07955(encounterId));
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getSpawnedPokemon());
    }

    public boolean notifySpawnedUncaughtPokemon(String encounterId) {
        Log.d(TAG, "notifySpawnedUncaughtPokemon() id : " + encounterId);
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(new C07966(encounterId));
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getSpawnedUncaughtPokemon());
    }

    public boolean notifySpawnedLegendaryPokemon(String encounterId) {
        Log.d(TAG, "notifySpawnedLegendaryPokemon() id : " + encounterId);
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(new C07977(encounterId));
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getSpawnedLegendaryPokemon());
    }

    public boolean notifyReadyForThrowPokeball(String ballType) {
        Log.d(TAG, "notifyReadyForThrowPokeball() : " + ballType);
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(new C07988());
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getReadyForThrowPokeball());
    }

    public boolean notifyNoPokeball() {
        Log.d(TAG, "notifyNoPokeball()");
        if (isEnableSfida()) {
            return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getNoPokeball());
        }
        return false;
    }

    public boolean notifyStartDowser() {
        Log.d(TAG, "notifyStartDowser()");
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getDonePattern());
    }

    public boolean notifyProximityDowser(String proximityLevel) {
        Log.d(TAG, "notifyProximityDowser()");
        if (!isEnableSfida()) {
            return false;
        }
        byte[] pattern;
        this.sfidaService.setOnClickSfidaListener(null);
        switch (Integer.valueOf(proximityLevel).intValue()) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                pattern = SfidaMessage.getDowserProximity1();
                break;
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                pattern = SfidaMessage.getDowserProximity2();
                break;
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                pattern = SfidaMessage.getDowserProximity3();
                break;
            case Place.TYPE_AQUARIUM /*4*/:
                pattern = SfidaMessage.getDowserProximity4();
                break;
            case Place.TYPE_ART_GALLERY /*5*/:
                pattern = SfidaMessage.getDowserProximity5();
                break;
            default:
                return false;
        }
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, pattern);
    }

    public boolean notifyCancelDowser() {
        Log.d(TAG, "notifyCancelDowser()");
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getDowserCancel());
    }

    public boolean notifyFoundDowser() {
        Log.d(TAG, "notifyFoundDowser()");
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getDowserVisible());
    }

    public boolean notifyPokeballShakeAndBroken(String shakeCount) {
        Log.d(TAG, "notifyPokeballShakeAndBroken() shakeCount : " + shakeCount);
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        switch (Integer.valueOf(shakeCount).intValue()) {
            case C1391R.styleable.AdsAttrs_adSize /*0*/:
                return true;
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                return notifyPokeballBrokenShakeOnce();
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                return notifyPokeballBrokenShakeTwice();
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                return notifyPokeballBrokenOneShakeThree();
            default:
                return false;
        }
    }

    public boolean notifyPokemonCaught() {
        Log.d(TAG, "notifyPokemonCaught()");
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getPokemonCaught());
    }

    public boolean notifyCancel() {
        Log.d(TAG, "notifyCancel()");
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getCancelPattern());
    }

    public boolean notifyError() {
        Log.d(TAG, "notifyError()");
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getError());
    }

    public boolean oneShotVibrate() {
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getCaptureSucceed());
    }

    private boolean isEnableSfida() {
        return this.sfidaService != null && this.isSfidaConnected;
    }

    private boolean notifyPokeballBrokenShakeOnce() {
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getPokeballShakeOnce());
    }

    private boolean notifyPokeballBrokenShakeTwice() {
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getPokeballShakeTwice());
    }

    private boolean notifyPokeballBrokenOneShakeThree() {
        if (!isEnableSfida()) {
            return false;
        }
        this.sfidaService.setOnClickSfidaListener(null);
        return this.sfidaService.sendDeviceControlMessage(SfidaMessage.UUID_LED_VIBRATE_CTRL_CHAR, SfidaMessage.getPokeballShakeThree());
    }

    private void addSfidaFinderFragment() {
        SfidaFinderFragment fragment = SfidaFinderFragment.createInstance();
        fragment.setOnDeviceDiscoveredListener(new SfidaDiscoveredListener());
        getActivity().getFragmentManager().beginTransaction().add(fragment, SfidaFinderFragment.class.getName()).commit();
    }

    private void startScanSfida() {
        SfidaFinderFragment fragment = getSfidaFinderFragment();
        if (fragment != null) {
            fragment.executeFindSfida();
        }
    }

    private void stopScanSfida() {
        SfidaFinderFragment fragment = getSfidaFinderFragment();
        if (fragment != null) {
            fragment.cancelFindSfida();
        }
    }

    private SfidaFinderFragment getSfidaFinderFragment() {
        return (SfidaFinderFragment) getActivity().getFragmentManager().findFragmentByTag(SfidaFinderFragment.class.getName());
    }

    private void registerReceiver() {
        getActivity().registerReceiver(this.gattUpdateReceiver, makeGattUpdateIntentFilter());
        this.isReceiverRegistered = true;
    }

    private Context getContext() {
        return getActivity().getApplicationContext();
    }

    private Activity getActivity() {
        return this.activity != null ? this.activity : UnityPlayer.currentActivity;
    }

    private IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SfidaConstants.ACTION_GATT_CONNECTED);
        intentFilter.addAction(SfidaConstants.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(SfidaConstants.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(SfidaConstants.ACTION_CERTIFICATE_COMPLETE);
        intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        return intentFilter;
    }

    private void onBroadcastUpdated(Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onBroadcastUpdated() " + action);
        boolean z = true;
        switch (action.hashCode()) {
            case -1535635066:
                if (action.equals(SfidaConstants.ACTION_GATT_CONNECTED)) {
                    z = false;
                    break;
                }
                break;
            case -1413011842:
                if (action.equals(SfidaConstants.ACTION_GATT_DISCONNECTED)) {
                    z = true;
                    break;
                }
                break;
            case -410892653:
                if (action.equals(SfidaConstants.ACTION_BOND_CANCELED)) {
                    z = true;
                    break;
                }
                break;
            case -391313386:
                if (action.equals(SfidaConstants.ACTION_DATA_AVAILABLE)) {
                    z = true;
                    break;
                }
                break;
            case -302160988:
                if (action.equals(SfidaConstants.ACTION_CREATE_BOND)) {
                    z = true;
                    break;
                }
                break;
            case 789901635:
                if (action.equals(SfidaConstants.ACTION_CERTIFICATE_COMPLETE)) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case C1391R.styleable.AdsAttrs_adSize /*0*/:
                this.isSfidaConnected = true;
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                this.isSfidaConnected = false;
                Toast.makeText(getActivity(), "PokemonGoPlus disconnected", 1).show();
                UnityPlayer.UnitySendMessage(UNITY_GAME_OBJECT, UNITY_METHOD_NOTIFY_DISCONNECTED, BuildConfig.FLAVOR);
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                Toast.makeText(getActivity(), "Pok\u00e9mon GO Plus connected.", 0).show();
                UnityPlayer.UnitySendMessage(UNITY_GAME_OBJECT, UNITY_METHOD_NOTIFY_CONNECTED, BuildConfig.FLAVOR);
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                Bundle args = intent.getExtras();
                if (args == null) {
                    Log.wtf(TAG, "ops!");
                    return;
                }
                UUID characteristicUUID = (UUID) args.getSerializable(SfidaService.EXTRA_DATA_CHARACTERISTIC);
                if (characteristicUUID == null) {
                    Log.d(TAG, "characteristicUUID is Null");
                } else if (characteristicUUID.equals(SfidaMessage.UUID_FW_VERSION_CHAR)) {
                    Log.d(TAG, "SFIDA Version : " + args.getString(SfidaService.EXTRA_DATA_RAW));
                } else {
                    Log.d(TAG, "[BLE] raw data " + SfidaUtils.byteArrayToString(args.getByteArray(SfidaService.EXTRA_DATA_RAW)));
                }
            case Place.TYPE_AQUARIUM /*4*/:
                Toast.makeText(getActivity(), "Pairing...\nClick Plus again.", 1).show();
            case Place.TYPE_ART_GALLERY /*5*/:
                Toast.makeText(getActivity(), "Canceled pairing. Retry or refresh Pok\u00e9mon GO Plus connection.", 1).show();
            default:
        }
    }

    private void startSfidaConnection(BluetoothDevice device) {
        this.device = device;
        if (this.isServiceBound) {
            this.sfidaService.connect(device);
            return;
        }
        getActivity().bindService(new Intent(getActivity(), SfidaService.class), this.serviceConnection, 1);
    }

    private void readFwVersion() {
        this.sfidaService.readFwVersionMessage();
    }
}
