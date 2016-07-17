package com.google.android.gms.location.internal;

import android.app.PendingIntent;
import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognitionApi;

public class zza implements ActivityRecognitionApi {

    private static abstract class zza extends com.google.android.gms.location.ActivityRecognition.zza<Status> {
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

    /* renamed from: com.google.android.gms.location.internal.zza.1 */
    class C06311 extends zza {
        final /* synthetic */ long zzaEX;
        final /* synthetic */ PendingIntent zzaEY;
        final /* synthetic */ zza zzaEZ;

        C06311(zza com_google_android_gms_location_internal_zza, GoogleApiClient googleApiClient, long j, PendingIntent pendingIntent) {
            this.zzaEZ = com_google_android_gms_location_internal_zza;
            this.zzaEX = j;
            this.zzaEY = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaEX, this.zzaEY);
            zzb(Status.zzabb);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zza.2 */
    class C06322 extends zza {
        final /* synthetic */ PendingIntent zzaEY;
        final /* synthetic */ zza zzaEZ;

        C06322(zza com_google_android_gms_location_internal_zza, GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
            this.zzaEZ = com_google_android_gms_location_internal_zza;
            this.zzaEY = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaEY);
            zzb(Status.zzabb);
        }
    }

    public PendingResult<Status> removeActivityUpdates(GoogleApiClient client, PendingIntent callbackIntent) {
        return client.zzb(new C06322(this, client, callbackIntent));
    }

    public PendingResult<Status> requestActivityUpdates(GoogleApiClient client, long detectionIntervalMillis, PendingIntent callbackIntent) {
        return client.zzb(new C06311(this, client, detectionIntervalMillis, callbackIntent));
    }
}
