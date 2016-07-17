package com.nianticlabs.nia.location;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.Location;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.contextservice.ServiceStatus;
import com.nianticlabs.nia.location.GpsProvider.GpsProviderListener;
import com.nianticlabs.nia.location.Provider.ProviderListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NianticLocationManager extends ContextService {
    static final boolean ENABLE_VERBOSE_LOGS = false;
    private static final String FUSED_PROVIDER_NAME = "fused";
    private static final float GPS_UPDATE_DISTANCE_M = 0.0f;
    private static final int GPS_UPDATE_TIME_MSEC = 1000;
    private static final long INITIALIZATION_WAIT_TIME_MS = 2000;
    private static final float NET_UPDATE_DISTANCE_M = 0.0f;
    private static final int NET_UPDATE_TIME_MSEC = 5000;
    private static final String TAG = "NianticLocationManager";
    private float gpsUpdateDistanceM;
    private int gpsUpdateTimeMs;
    private float netUpdateDistanceM;
    private int netUpdateTimeMs;
    private final List<Provider> providers;
    private boolean started;
    private final Map<String, ServiceStatus> statusMap;

    /* renamed from: com.nianticlabs.nia.location.NianticLocationManager.1 */
    class C07691 implements GpsProviderListener {
        final /* synthetic */ String val$name;

        C07691(String str) {
            this.val$name = str;
        }

        public void onGpsStatusUpdate(int timeToFix, GpsSatellite[] satellites) {
            NianticLocationManager.this.gpsStatusUpdate(timeToFix, satellites);
        }

        public void onProviderStatus(ServiceStatus status) {
            NianticLocationManager.this.statusMap.put(this.val$name, status);
            NianticLocationManager.this.locationUpdate(null, NianticLocationManager.this.statusArray());
        }

        public void onProviderLocation(Location location) {
            NianticLocationManager.this.locationUpdate(location, NianticLocationManager.this.statusArray());
        }
    }

    /* renamed from: com.nianticlabs.nia.location.NianticLocationManager.2 */
    class C07702 implements ProviderListener {
        final /* synthetic */ String val$name;

        C07702(String str) {
            this.val$name = str;
        }

        public void onProviderStatus(ServiceStatus status) {
            NianticLocationManager.this.statusMap.put(this.val$name, status);
        }

        public void onProviderLocation(Location location) {
            NianticLocationManager.this.locationUpdate(location, NianticLocationManager.this.statusArray());
        }
    }

    /* renamed from: com.nianticlabs.nia.location.NianticLocationManager.3 */
    class C07713 implements Runnable {
        final /* synthetic */ int val$gps_update_time_ms;
        final /* synthetic */ int val$net_update_time_ms;
        final /* synthetic */ double val$update_distance;

        C07713(int i, double d, int i2) {
            this.val$gps_update_time_ms = i;
            this.val$update_distance = d;
            this.val$net_update_time_ms = i2;
        }

        public void run() {
            if (NianticLocationManager.this.started) {
                throw new IllegalStateException("Already started.");
            }
            NianticLocationManager.this.gpsUpdateTimeMs = this.val$gps_update_time_ms;
            NianticLocationManager.this.gpsUpdateDistanceM = (float) this.val$update_distance;
            NianticLocationManager.this.netUpdateTimeMs = this.val$net_update_time_ms;
            NianticLocationManager.this.netUpdateDistanceM = (float) this.val$update_distance;
            NianticLocationManager.this.doStart();
        }
    }

    private native void nativeGpsStatusUpdate(int i, GpsSatellite[] gpsSatelliteArr);

    private native void nativeLocationUpdate(Location location, int[] iArr, Context context);

    public NianticLocationManager(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
        this.statusMap = new HashMap();
        this.gpsUpdateTimeMs = GPS_UPDATE_TIME_MSEC;
        this.gpsUpdateDistanceM = NET_UPDATE_DISTANCE_M;
        this.netUpdateTimeMs = NET_UPDATE_TIME_MSEC;
        this.netUpdateDistanceM = NET_UPDATE_DISTANCE_M;
        this.started = ENABLE_VERBOSE_LOGS;
        this.statusMap.put("gps", ServiceStatus.UNDEFINED);
        this.statusMap.put("network", ServiceStatus.UNDEFINED);
        this.statusMap.put(FUSED_PROVIDER_NAME, ServiceStatus.UNDEFINED);
        this.providers = new ArrayList(3);
    }

    private void createProviders() {
        if (this.providers.size() != 3) {
            addProvider(FUSED_PROVIDER_NAME, new FusedLocationProvider(this.context, this.gpsUpdateTimeMs, this.gpsUpdateDistanceM));
            addProvider("gps", new LocationManagerProvider(this.context, "gps", this.gpsUpdateTimeMs, this.gpsUpdateDistanceM));
            addProvider("network", new LocationManagerProvider(this.context, "network", this.netUpdateTimeMs, this.netUpdateDistanceM));
        }
    }

    private void addProvider(String name, Provider provider) {
        this.providers.add(provider);
        if (provider instanceof GpsProvider) {
            provider.setListener(new C07691(name));
        } else {
            provider.setListener(new C07702(name));
        }
    }

    private int[] statusArray() {
        return new int[]{((ServiceStatus) this.statusMap.get("gps")).ordinal(), ((ServiceStatus) this.statusMap.get("network")).ordinal(), ((ServiceStatus) this.statusMap.get(FUSED_PROVIDER_NAME)).ordinal()};
    }

    public void onStart() {
    }

    private void doStart() {
        if (!this.started) {
            createProviders();
            locationUpdate(null, statusArray());
            for (Provider provider : this.providers) {
                provider.onStart();
            }
            this.started = true;
        }
    }

    public void onStop() {
        for (Provider provider : this.providers) {
            provider.onStop();
        }
        this.started = ENABLE_VERBOSE_LOGS;
    }

    public void onPause() {
        for (Provider provider : this.providers) {
            provider.onPause();
        }
    }

    public void onResume() {
        if (!this.started) {
            doStart();
        }
        for (Provider provider : this.providers) {
            provider.onResume();
        }
    }

    public void configureLocationParameters(double update_distance, int gps_update_time_ms, int net_update_time_ms) {
        ContextService.runOnServiceHandler(new C07713(gps_update_time_ms, update_distance, net_update_time_ms));
    }

    private void locationUpdate(Location location, int[] status) {
        synchronized (this.callbackLock) {
            nativeLocationUpdate(location, status, this.context);
        }
    }

    private void gpsStatusUpdate(int timeToFix, GpsSatellite[] satellites) {
        synchronized (this.callbackLock) {
            nativeGpsStatusUpdate(timeToFix, satellites);
        }
    }
}
