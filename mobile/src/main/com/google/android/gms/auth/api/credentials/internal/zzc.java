package com.google.android.gms.auth.api.credentials.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlb.zzb;

public final class zzc implements CredentialsApi {

    /* renamed from: com.google.android.gms.auth.api.credentials.internal.zzc.1 */
    class C02141 extends zzd<CredentialRequestResult> {
        final /* synthetic */ CredentialRequest zzSE;
        final /* synthetic */ zzc zzSF;

        /* renamed from: com.google.android.gms.auth.api.credentials.internal.zzc.1.1 */
        class C02131 extends zza {
            final /* synthetic */ C02141 zzSG;

            C02131(C02141 c02141) {
                this.zzSG = c02141;
            }

            public void zza(Status status, Credential credential) {
                this.zzSG.zzb(new zzb(status, credential));
            }

            public void zzg(Status status) {
                this.zzSG.zzb(zzb.zzh(status));
            }
        }

        C02141(zzc com_google_android_gms_auth_api_credentials_internal_zzc, GoogleApiClient googleApiClient, CredentialRequest credentialRequest) {
            this.zzSF = com_google_android_gms_auth_api_credentials_internal_zzc;
            this.zzSE = credentialRequest;
            super(googleApiClient);
        }

        protected void zza(Context context, zzh com_google_android_gms_auth_api_credentials_internal_zzh) throws RemoteException {
            com_google_android_gms_auth_api_credentials_internal_zzh.zza(new C02131(this), this.zzSE);
        }

        protected /* synthetic */ Result zzb(Status status) {
            return zzi(status);
        }

        protected CredentialRequestResult zzi(Status status) {
            return zzb.zzh(status);
        }
    }

    /* renamed from: com.google.android.gms.auth.api.credentials.internal.zzc.2 */
    class C02152 extends zzd<Status> {
        final /* synthetic */ zzc zzSF;
        final /* synthetic */ Credential zzSH;

        C02152(zzc com_google_android_gms_auth_api_credentials_internal_zzc, GoogleApiClient googleApiClient, Credential credential) {
            this.zzSF = com_google_android_gms_auth_api_credentials_internal_zzc;
            this.zzSH = credential;
            super(googleApiClient);
        }

        protected void zza(Context context, zzh com_google_android_gms_auth_api_credentials_internal_zzh) throws RemoteException {
            com_google_android_gms_auth_api_credentials_internal_zzh.zza(new zza(this), new SaveRequest(this.zzSH));
        }

        protected /* synthetic */ Result zzb(Status status) {
            return zzd(status);
        }

        protected Status zzd(Status status) {
            return status;
        }
    }

    /* renamed from: com.google.android.gms.auth.api.credentials.internal.zzc.3 */
    class C02163 extends zzd<Status> {
        final /* synthetic */ zzc zzSF;
        final /* synthetic */ Credential zzSH;

        C02163(zzc com_google_android_gms_auth_api_credentials_internal_zzc, GoogleApiClient googleApiClient, Credential credential) {
            this.zzSF = com_google_android_gms_auth_api_credentials_internal_zzc;
            this.zzSH = credential;
            super(googleApiClient);
        }

        protected void zza(Context context, zzh com_google_android_gms_auth_api_credentials_internal_zzh) throws RemoteException {
            com_google_android_gms_auth_api_credentials_internal_zzh.zza(new zza(this), new DeleteRequest(this.zzSH));
        }

        protected /* synthetic */ Result zzb(Status status) {
            return zzd(status);
        }

        protected Status zzd(Status status) {
            return status;
        }
    }

    /* renamed from: com.google.android.gms.auth.api.credentials.internal.zzc.4 */
    class C02174 extends zzd<Status> {
        final /* synthetic */ zzc zzSF;

        C02174(zzc com_google_android_gms_auth_api_credentials_internal_zzc, GoogleApiClient googleApiClient) {
            this.zzSF = com_google_android_gms_auth_api_credentials_internal_zzc;
            super(googleApiClient);
        }

        protected void zza(Context context, zzh com_google_android_gms_auth_api_credentials_internal_zzh) throws RemoteException {
            com_google_android_gms_auth_api_credentials_internal_zzh.zza(new zza(this));
        }

        protected /* synthetic */ Result zzb(Status status) {
            return zzd(status);
        }

        protected Status zzd(Status status) {
            return status;
        }
    }

    private static class zza extends zza {
        private zzb<Status> zzSI;

        zza(zzb<Status> com_google_android_gms_internal_zzlb_zzb_com_google_android_gms_common_api_Status) {
            this.zzSI = com_google_android_gms_internal_zzlb_zzb_com_google_android_gms_common_api_Status;
        }

        public void zzg(Status status) {
            this.zzSI.zzp(status);
        }
    }

    public PendingResult<Status> delete(GoogleApiClient client, Credential credential) {
        return client.zzb(new C02163(this, client, credential));
    }

    public PendingResult<Status> disableAutoSignIn(GoogleApiClient client) {
        return client.zzb(new C02174(this, client));
    }

    public PendingResult<CredentialRequestResult> request(GoogleApiClient client, CredentialRequest request) {
        return client.zza(new C02141(this, client, request));
    }

    public PendingResult<Status> save(GoogleApiClient client, Credential credential) {
        return client.zzb(new C02152(this, client, credential));
    }
}
