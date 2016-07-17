package com.upsight.android.analytics.internal.provider;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightDataStoreListener;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class LocationTracker extends UpsightLocationTracker {
    private static final String LOG_TAG;
    private UpsightDataStore mDataStore;
    private UpsightLogger mLogger;

    /* renamed from: com.upsight.android.analytics.internal.provider.LocationTracker.1 */
    class C08921 implements UpsightDataStoreListener<Set<Data>> {
        final /* synthetic */ Data val$newLocation;

        C08921(Data data) {
            this.val$newLocation = data;
        }

        public void onSuccess(Set<Data> result) {
            Data location = null;
            Iterator<Data> iterator = result.iterator();
            if (iterator.hasNext()) {
                location = (Data) iterator.next();
                location.setLatitude(this.val$newLocation.getLatitude());
                location.setLongitude(this.val$newLocation.getLongitude());
                location.setTimeZone(this.val$newLocation.getTimeZone());
            }
            if (location == null) {
                location = this.val$newLocation;
            }
            LocationTracker.this.mDataStore.store(location);
        }

        public void onFailure(UpsightException exception) {
            LocationTracker.this.mLogger.m200e(LocationTracker.LOG_TAG, exception, "Failed to fetch location data.", new Object[0]);
        }
    }

    /* renamed from: com.upsight.android.analytics.internal.provider.LocationTracker.2 */
    class C08932 implements UpsightDataStoreListener<Set<Data>> {
        C08932() {
        }

        public void onSuccess(Set<Data> result) {
            for (Data data : result) {
                LocationTracker.this.mDataStore.remove(data);
            }
        }

        public void onFailure(UpsightException exception) {
            LocationTracker.this.mLogger.m199e(Upsight.LOG_TAG, "Failed to remove stale location data.", exception);
        }
    }

    static {
        LOG_TAG = LocationTracker.class.getSimpleName();
    }

    @Inject
    LocationTracker(UpsightContext upsight) {
        this.mDataStore = upsight.getDataStore();
        this.mLogger = upsight.getLogger();
    }

    public void track(Data newLocation) {
        this.mDataStore.fetch(Data.class, new C08921(newLocation));
    }

    public void purge() {
        this.mDataStore.fetch(Data.class, new C08932());
    }
}
