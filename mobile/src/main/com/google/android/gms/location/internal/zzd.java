package com.google.android.gms.location.internal;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class zzd implements FusedLocationProviderApi {

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

    /* renamed from: com.google.android.gms.location.internal.zzd.1 */
    class C06351 extends zza {
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ LocationListener zzaFe;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.1.1 */
        class C06341 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06351 zzaFg;

            C06341(C06351 c06351) {
                this.zzaFg = c06351;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFg.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06351(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaFe = locationListener;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFd, this.zzaFe, null, new C06341(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.2 */
    class C06372 extends zza {
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ LocationCallback zzaFh;

        /* renamed from: com.google.android.gms.location.internal.zzd.2.1 */
        class C06361 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06372 zzaFi;

            C06361(C06372 c06372) {
                this.zzaFi = c06372;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFi.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06372(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationCallback locationCallback) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFh = locationCallback;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFh, new C06361(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.3 */
    class C06383 extends zza {
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ boolean zzaFj;

        C06383(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, boolean z) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFj = z;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zzah(this.zzaFj);
            zzb(Status.zzabb);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.4 */
    class C06394 extends zza {
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ Location zzaFk;

        C06394(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, Location location) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFk = location;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zzc(this.zzaFk);
            zzb(Status.zzabb);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.5 */
    class C06415 extends zza {
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ LocationListener zzaFe;
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ Looper zzaFl;

        /* renamed from: com.google.android.gms.location.internal.zzd.5.1 */
        class C06401 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06415 zzaFm;

            C06401(C06415 c06415) {
                this.zzaFm = c06415;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFm.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06415(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaFe = locationListener;
            this.zzaFl = looper;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFd, this.zzaFe, this.zzaFl, new C06401(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.6 */
    class C06436 extends zza {
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ LocationCallback zzaFh;
        final /* synthetic */ Looper zzaFl;

        /* renamed from: com.google.android.gms.location.internal.zzd.6.1 */
        class C06421 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06436 zzaFn;

            C06421(C06436 c06436) {
                this.zzaFn = c06436;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFn.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06436(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationCallback locationCallback, Looper looper) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaFh = locationCallback;
            this.zzaFl = looper;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(LocationRequestInternal.zzb(this.zzaFd), this.zzaFh, this.zzaFl, new C06421(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.7 */
    class C06457 extends zza {
        final /* synthetic */ PendingIntent zzaEY;
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.7.1 */
        class C06441 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06457 zzaFo;

            C06441(C06457 c06457) {
                this.zzaFo = c06457;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFo.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06457(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, PendingIntent pendingIntent) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaEY = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFd, this.zzaEY, new C06441(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.8 */
    class C06478 extends zza {
        final /* synthetic */ LocationListener zzaFe;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.8.1 */
        class C06461 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06478 zzaFp;

            C06461(C06478 c06478) {
                this.zzaFp = c06478;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFp.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06478(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationListener locationListener) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFe = locationListener;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFe, new C06461(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.9 */
    class C06499 extends zza {
        final /* synthetic */ PendingIntent zzaEY;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.9.1 */
        class C06481 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06499 zzaFq;

            C06481(C06499 c06499) {
                this.zzaFq = c06499;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFq.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06499(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaEY = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaEY, new C06481(this));
        }
    }

    public Location getLastLocation(GoogleApiClient client) {
        try {
            return LocationServices.zzd(client).getLastLocation();
        } catch (Exception e) {
            return null;
        }
    }

    public LocationAvailability getLocationAvailability(GoogleApiClient client) {
        try {
            return LocationServices.zzd(client).zzwD();
        } catch (Exception e) {
            return null;
        }
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient client, PendingIntent callbackIntent) {
        return client.zzb(new C06499(this, client, callbackIntent));
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient client, LocationCallback callback) {
        return client.zzb(new C06372(this, client, callback));
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient client, LocationListener listener) {
        return client.zzb(new C06478(this, client, listener));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient client, LocationRequest request, PendingIntent callbackIntent) {
        return client.zzb(new C06457(this, client, request, callbackIntent));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient client, LocationRequest request, LocationCallback callback, Looper looper) {
        return client.zzb(new C06436(this, client, request, callback, looper));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient client, LocationRequest request, LocationListener listener) {
        return client.zzb(new C06351(this, client, request, listener));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient client, LocationRequest request, LocationListener listener, Looper looper) {
        return client.zzb(new C06415(this, client, request, listener, looper));
    }

    public PendingResult<Status> setMockLocation(GoogleApiClient client, Location mockLocation) {
        return client.zzb(new C06394(this, client, mockLocation));
    }

    public PendingResult<Status> setMockMode(GoogleApiClient client, boolean isMockMode) {
        return client.zzb(new C06383(this, client, isMockMode));
    }
}
