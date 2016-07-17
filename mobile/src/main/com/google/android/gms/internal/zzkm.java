package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.proxy.ProxyApi.ProxyResult;
import com.google.android.gms.auth.api.proxy.ProxyRequest;
import com.google.android.gms.auth.api.proxy.ProxyResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.internal.zzx;

public class zzkm implements ProxyApi {

    /* renamed from: com.google.android.gms.internal.zzkm.1 */
    class C05951 extends zzkl {
        final /* synthetic */ ProxyRequest zzSQ;
        final /* synthetic */ zzkm zzSR;

        /* renamed from: com.google.android.gms.internal.zzkm.1.1 */
        class C05941 extends zzkh {
            final /* synthetic */ C05951 zzSS;

            C05941(C05951 c05951) {
                this.zzSS = c05951;
            }

            public void zza(ProxyResponse proxyResponse) {
                this.zzSS.zzb(new zzkn(proxyResponse));
            }
        }

        C05951(zzkm com_google_android_gms_internal_zzkm, GoogleApiClient googleApiClient, ProxyRequest proxyRequest) {
            this.zzSR = com_google_android_gms_internal_zzkm;
            this.zzSQ = proxyRequest;
            super(googleApiClient);
        }

        protected void zza(Context context, zzkk com_google_android_gms_internal_zzkk) throws RemoteException {
            com_google_android_gms_internal_zzkk.zza(new C05941(this), this.zzSQ);
        }
    }

    public PendingResult<ProxyResult> performProxyRequest(GoogleApiClient client, ProxyRequest request) {
        zzx.zzw(client);
        zzx.zzw(request);
        return client.zzb(new C05951(this, client, request));
    }
}
