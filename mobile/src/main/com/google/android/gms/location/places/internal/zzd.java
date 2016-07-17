package com.google.android.gms.location.places.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.zzf;
import com.google.android.gms.location.places.zzf.zzb;
import com.google.android.gms.location.places.zzl;
import com.google.android.gms.location.places.zzl.zza;
import com.google.android.gms.location.places.zzl.zzc;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.Arrays;

public class zzd implements GeoDataApi {

    /* renamed from: com.google.android.gms.location.places.internal.zzd.1 */
    class C06571 extends zzc<zze> {
        final /* synthetic */ AddPlaceRequest zzaGV;
        final /* synthetic */ zzd zzaGW;

        C06571(zzd com_google_android_gms_location_places_internal_zzd, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, AddPlaceRequest addPlaceRequest) {
            this.zzaGW = com_google_android_gms_location_places_internal_zzd;
            this.zzaGV = addPlaceRequest;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zze com_google_android_gms_location_places_internal_zze) throws RemoteException {
            com_google_android_gms_location_places_internal_zze.zza(new zzl((zzc) this, com_google_android_gms_location_places_internal_zze.getContext()), this.zzaGV);
        }
    }

    /* renamed from: com.google.android.gms.location.places.internal.zzd.2 */
    class C06582 extends zzc<zze> {
        final /* synthetic */ zzd zzaGW;
        final /* synthetic */ String[] zzaGX;

        C06582(zzd com_google_android_gms_location_places_internal_zzd, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, String[] strArr) {
            this.zzaGW = com_google_android_gms_location_places_internal_zzd;
            this.zzaGX = strArr;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zze com_google_android_gms_location_places_internal_zze) throws RemoteException {
            com_google_android_gms_location_places_internal_zze.zza(new zzl((zzc) this, com_google_android_gms_location_places_internal_zze.getContext()), Arrays.asList(this.zzaGX));
        }
    }

    /* renamed from: com.google.android.gms.location.places.internal.zzd.3 */
    class C06593 extends zza<zze> {
        final /* synthetic */ zzd zzaGW;
        final /* synthetic */ LatLngBounds zzaGY;
        final /* synthetic */ AutocompleteFilter zzaGZ;
        final /* synthetic */ String zzaxk;

        C06593(zzd com_google_android_gms_location_places_internal_zzd, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, String str, LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter) {
            this.zzaGW = com_google_android_gms_location_places_internal_zzd;
            this.zzaxk = str;
            this.zzaGY = latLngBounds;
            this.zzaGZ = autocompleteFilter;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zze com_google_android_gms_location_places_internal_zze) throws RemoteException {
            com_google_android_gms_location_places_internal_zze.zza(new zzl((zza) this), this.zzaxk, this.zzaGY, this.zzaGZ);
        }
    }

    /* renamed from: com.google.android.gms.location.places.internal.zzd.4 */
    class C06604 extends zzb<zze> {
        final /* synthetic */ zzd zzaGW;
        final /* synthetic */ String zzaHa;

        C06604(zzd com_google_android_gms_location_places_internal_zzd, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, String str) {
            this.zzaGW = com_google_android_gms_location_places_internal_zzd;
            this.zzaHa = str;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zze com_google_android_gms_location_places_internal_zze) throws RemoteException {
            com_google_android_gms_location_places_internal_zze.zza(new zzf((zzb) this), this.zzaHa);
        }
    }

    public PendingResult<PlaceBuffer> addPlace(GoogleApiClient client, AddPlaceRequest addPlaceRequest) {
        return client.zzb(new C06571(this, Places.zzaGz, client, addPlaceRequest));
    }

    public PendingResult<AutocompletePredictionBuffer> getAutocompletePredictions(GoogleApiClient client, String query, LatLngBounds bounds, AutocompleteFilter filter) {
        return client.zza(new C06593(this, Places.zzaGz, client, query, bounds, filter));
    }

    public PendingResult<PlaceBuffer> getPlaceById(GoogleApiClient client, String... placeIds) {
        boolean z = true;
        if (placeIds == null || placeIds.length < 1) {
            z = false;
        }
        zzx.zzaa(z);
        return client.zza(new C06582(this, Places.zzaGz, client, placeIds));
    }

    public PendingResult<PlacePhotoMetadataResult> getPlacePhotos(GoogleApiClient client, String placeId) {
        return client.zza(new C06604(this, Places.zzaGz, client, placeId));
    }
}
