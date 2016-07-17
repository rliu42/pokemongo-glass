package com.google.android.gms.location.internal;

import android.app.PendingIntent;
import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlb.zzb;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.GeofencingRequest.Builder;
import java.util.List;

public class zzf implements GeofencingApi {

    private static abstract class zza extends com.google.android.gms.location.LocationServices.zza<Status> {
        public zza(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        public /* synthetic */ Result zzb(Status status) {
            return zzd(status);
        }

        public Status zzd(Status status) {
            return status;
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzf.1 */
    class C06501 extends zza {
        final /* synthetic */ GeofencingRequest zzaFs;
        final /* synthetic */ zzf zzaFt;
        final /* synthetic */ PendingIntent zzarN;

        C06501(zzf com_google_android_gms_location_internal_zzf, GoogleApiClient googleApiClient, GeofencingRequest geofencingRequest, PendingIntent pendingIntent) {
            this.zzaFt = com_google_android_gms_location_internal_zzf;
            this.zzaFs = geofencingRequest;
            this.zzarN = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFs, this.zzarN, (zzb) this);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzf.2 */
    class C06512 extends zza {
        final /* synthetic */ zzf zzaFt;
        final /* synthetic */ PendingIntent zzarN;

        C06512(zzf com_google_android_gms_location_internal_zzf, GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
            this.zzaFt = com_google_android_gms_location_internal_zzf;
            this.zzarN = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzarN, (zzb) this);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzf.3 */
    class C06523 extends zza {
        final /* synthetic */ zzf zzaFt;
        final /* synthetic */ List zzaFu;

        C06523(zzf com_google_android_gms_location_internal_zzf, GoogleApiClient googleApiClient, List list) {
            this.zzaFt = com_google_android_gms_location_internal_zzf;
            this.zzaFu = list;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFu, (zzb) this);
        }
    }

    public PendingResult<Status> addGeofences(GoogleApiClient client, GeofencingRequest geofencingRequest, PendingIntent pendingIntent) {
        return client.zzb(new C06501(this, client, geofencingRequest, pendingIntent));
    }

    @Deprecated
    public PendingResult<Status> addGeofences(GoogleApiClient client, List<Geofence> geofences, PendingIntent pendingIntent) {
        Builder builder = new Builder();
        builder.addGeofences(geofences);
        builder.setInitialTrigger(5);
        return addGeofences(client, builder.build(), pendingIntent);
    }

    public PendingResult<Status> removeGeofences(GoogleApiClient client, PendingIntent pendingIntent) {
        return client.zzb(new C06512(this, client, pendingIntent));
    }

    public PendingResult<Status> removeGeofences(GoogleApiClient client, List<String> geofenceRequestIds) {
        return client.zzb(new C06523(this, client, geofenceRequestIds));
    }
}
