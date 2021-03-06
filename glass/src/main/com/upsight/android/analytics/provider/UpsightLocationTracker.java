package com.upsight.android.analytics.provider;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

public abstract class UpsightLocationTracker {

    @UpsightStorableType("upsight.model.location")
    public static final class Data {
        @UpsightStorableIdentifier
        String id;
        @JsonProperty
        double latitude;
        @JsonProperty
        double longitude;
        @JsonProperty
        String timeZone;

        public static Data create(double latitude, double longitude, String timeZone) {
            return new Data(latitude, longitude, timeZone);
        }

        public static Data create(double latitude, double longitude) {
            return new Data(latitude, longitude, null);
        }

        private Data(double latitude, double longitude, String timeZone) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.timeZone = timeZone;
        }

        Data() {
        }

        public double getLatitude() {
            return this.latitude;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public String getTimeZone() {
            return this.timeZone;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Data that = (Data) o;
            if (this.id != null) {
                if (this.id.equals(that.id)) {
                    return true;
                }
            } else if (that.id == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.id != null ? this.id.hashCode() : 0;
        }
    }

    public abstract void purge();

    public abstract void track(Data data);

    public static void track(UpsightContext upsight, Data locationData) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().trackLocation(locationData);
        } else {
            upsight.getLogger().m199e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }

    public static void purge(UpsightContext upsight) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().purgeLocation();
        } else {
            upsight.getLogger().m199e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }
}
