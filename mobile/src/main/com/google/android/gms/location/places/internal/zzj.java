package com.google.android.gms.location.places.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.zzl;
import com.google.android.gms.location.places.zzl.zzd;
import com.google.android.gms.location.places.zzl.zzf;

public class zzj implements PlaceDetectionApi {

    /* renamed from: com.google.android.gms.location.places.internal.zzj.1 */
    class C06611 extends zzd<zzk> {
        final /* synthetic */ PlaceFilter zzaHf;
        final /* synthetic */ zzj zzaHg;

        C06611(zzj com_google_android_gms_location_places_internal_zzj, zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, PlaceFilter placeFilter) {
            this.zzaHg = com_google_android_gms_location_places_internal_zzj;
            this.zzaHf = placeFilter;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zzk com_google_android_gms_location_places_internal_zzk) throws RemoteException {
            com_google_android_gms_location_places_internal_zzk.zza(new zzl((zzd) this, com_google_android_gms_location_places_internal_zzk.getContext()), this.zzaHf);
        }
    }

    /* renamed from: com.google.android.gms.location.places.internal.zzj.2 */
    class C06622 extends zzf<zzk> {
        final /* synthetic */ zzj zzaHg;
        final /* synthetic */ PlaceReport zzaHh;

        C06622(zzj com_google_android_gms_location_places_internal_zzj, zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, PlaceReport placeReport) {
            this.zzaHg = com_google_android_gms_location_places_internal_zzj;
            this.zzaHh = placeReport;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zzk com_google_android_gms_location_places_internal_zzk) throws RemoteException {
            com_google_android_gms_location_places_internal_zzk.zza(new zzl((zzf) this), this.zzaHh);
        }
    }

    public PendingResult<PlaceLikelihoodBuffer> getCurrentPlace(GoogleApiClient client, PlaceFilter filter) {
        return client.zza(new C06611(this, Places.zzaGA, client, filter));
    }

    public PendingResult<Status> reportDeviceAtPlace(GoogleApiClient client, PlaceReport report) {
        return client.zzb(new C06622(this, Places.zzaGA, client, report));
    }
}
