package com.google.android.gms.internal;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzx;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class zzlp extends Fragment implements OnCancelListener {
    private static final GoogleApiAvailability zzacJ;
    private boolean mStarted;
    private boolean zzacK;
    private int zzacL;
    private ConnectionResult zzacM;
    private final Handler zzacN;
    private zzll zzacO;
    private final SparseArray<zza> zzacP;

    private class zza implements OnConnectionFailedListener {
        public final int zzacQ;
        public final GoogleApiClient zzacR;
        public final OnConnectionFailedListener zzacS;
        final /* synthetic */ zzlp zzacT;

        public zza(zzlp com_google_android_gms_internal_zzlp, int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
            this.zzacT = com_google_android_gms_internal_zzlp;
            this.zzacQ = i;
            this.zzacR = googleApiClient;
            this.zzacS = onConnectionFailedListener;
            googleApiClient.registerConnectionFailedListener(this);
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            writer.append(prefix).append("GoogleApiClient #").print(this.zzacQ);
            writer.println(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR);
            this.zzacR.dump(prefix + "  ", fd, writer, args);
        }

        public void onConnectionFailed(ConnectionResult result) {
            this.zzacT.zzacN.post(new zzb(this.zzacT, this.zzacQ, result));
        }

        public void zzom() {
            this.zzacR.unregisterConnectionFailedListener(this);
            this.zzacR.disconnect();
        }
    }

    private class zzb implements Runnable {
        final /* synthetic */ zzlp zzacT;
        private final int zzacU;
        private final ConnectionResult zzacV;

        /* renamed from: com.google.android.gms.internal.zzlp.zzb.1 */
        class C06101 extends zzll {
            final /* synthetic */ Dialog zzacW;
            final /* synthetic */ zzb zzacX;

            C06101(zzb com_google_android_gms_internal_zzlp_zzb, Dialog dialog) {
                this.zzacX = com_google_android_gms_internal_zzlp_zzb;
                this.zzacW = dialog;
            }

            protected void zzoi() {
                this.zzacX.zzacT.zzok();
                this.zzacW.dismiss();
            }
        }

        public zzb(zzlp com_google_android_gms_internal_zzlp, int i, ConnectionResult connectionResult) {
            this.zzacT = com_google_android_gms_internal_zzlp;
            this.zzacU = i;
            this.zzacV = connectionResult;
        }

        public void run() {
            if (this.zzacT.mStarted && !this.zzacT.zzacK) {
                this.zzacT.zzacK = true;
                this.zzacT.zzacL = this.zzacU;
                this.zzacT.zzacM = this.zzacV;
                if (this.zzacV.hasResolution()) {
                    try {
                        this.zzacV.startResolutionForResult(this.zzacT.getActivity(), ((this.zzacT.getActivity().getSupportFragmentManager().getFragments().indexOf(this.zzacT) + 1) << 16) + 1);
                    } catch (SendIntentException e) {
                        this.zzacT.zzok();
                    }
                } else if (zzlp.zzacJ.isUserResolvableError(this.zzacV.getErrorCode())) {
                    GooglePlayServicesUtil.showErrorDialogFragment(this.zzacV.getErrorCode(), this.zzacT.getActivity(), this.zzacT, 2, this.zzacT);
                } else if (this.zzacV.getErrorCode() == 18) {
                    this.zzacT.zzacO = zzll.zza(this.zzacT.getActivity().getApplicationContext(), new C06101(this, zzlp.zzacJ.zza(this.zzacT.getActivity(), this.zzacT)));
                } else {
                    this.zzacT.zza(this.zzacU, this.zzacV);
                }
            }
        }
    }

    static {
        zzacJ = GoogleApiAvailability.getInstance();
    }

    public zzlp() {
        this.zzacL = -1;
        this.zzacN = new Handler(Looper.getMainLooper());
        this.zzacP = new SparseArray();
    }

    public static zzlp zza(FragmentActivity fragmentActivity) {
        zzx.zzci("Must be called from main thread of process");
        try {
            zzlp com_google_android_gms_internal_zzlp = (zzlp) fragmentActivity.getSupportFragmentManager().findFragmentByTag("GmsSupportLifecycleFragment");
            return (com_google_android_gms_internal_zzlp == null || com_google_android_gms_internal_zzlp.isRemoving()) ? null : com_google_android_gms_internal_zzlp;
        } catch (Throwable e) {
            throw new IllegalStateException("Fragment with tag GmsSupportLifecycleFragment is not a SupportLifecycleFragment", e);
        }
    }

    private void zza(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFragment", "Unresolved error while connecting client. Stopping auto-manage.");
        zza com_google_android_gms_internal_zzlp_zza = (zza) this.zzacP.get(i);
        if (com_google_android_gms_internal_zzlp_zza != null) {
            zzbp(i);
            OnConnectionFailedListener onConnectionFailedListener = com_google_android_gms_internal_zzlp_zza.zzacS;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
        zzok();
    }

    public static zzlp zzb(FragmentActivity fragmentActivity) {
        zzlp zza = zza(fragmentActivity);
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (zza != null) {
            return zza;
        }
        Fragment com_google_android_gms_internal_zzlp = new zzlp();
        supportFragmentManager.beginTransaction().add(com_google_android_gms_internal_zzlp, "GmsSupportLifecycleFragment").commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
        return com_google_android_gms_internal_zzlp;
    }

    private void zzok() {
        this.zzacK = false;
        this.zzacL = -1;
        this.zzacM = null;
        if (this.zzacO != null) {
            this.zzacO.unregister();
            this.zzacO = null;
        }
        for (int i = 0; i < this.zzacP.size(); i++) {
            ((zza) this.zzacP.valueAt(i)).zzacR.connect();
        }
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        for (int i = 0; i < this.zzacP.size(); i++) {
            ((zza) this.zzacP.valueAt(i)).dump(prefix, fd, writer, args);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityResult(int r5, int r6, android.content.Intent r7) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        switch(r5) {
            case 1: goto L_0x0019;
            case 2: goto L_0x000c;
            default: goto L_0x0005;
        };
    L_0x0005:
        r0 = r1;
    L_0x0006:
        if (r0 == 0) goto L_0x0029;
    L_0x0008:
        r4.zzok();
    L_0x000b:
        return;
    L_0x000c:
        r2 = zzacJ;
        r3 = r4.getActivity();
        r2 = r2.isGooglePlayServicesAvailable(r3);
        if (r2 != 0) goto L_0x0005;
    L_0x0018:
        goto L_0x0006;
    L_0x0019:
        r2 = -1;
        if (r6 == r2) goto L_0x0006;
    L_0x001c:
        if (r6 != 0) goto L_0x0005;
    L_0x001e:
        r0 = new com.google.android.gms.common.ConnectionResult;
        r2 = 13;
        r3 = 0;
        r0.<init>(r2, r3);
        r4.zzacM = r0;
        goto L_0x0005;
    L_0x0029:
        r0 = r4.zzacL;
        r1 = r4.zzacM;
        r4.zza(r0, r1);
        goto L_0x000b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzlp.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onCancel(DialogInterface dialogInterface) {
        zza(this.zzacL, new ConnectionResult(13, null));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.zzacK = savedInstanceState.getBoolean("resolving_error", false);
            this.zzacL = savedInstanceState.getInt("failed_client_id", -1);
            if (this.zzacL >= 0) {
                this.zzacM = new ConnectionResult(savedInstanceState.getInt("failed_status"), (PendingIntent) savedInstanceState.getParcelable("failed_resolution"));
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("resolving_error", this.zzacK);
        if (this.zzacL >= 0) {
            outState.putInt("failed_client_id", this.zzacL);
            outState.putInt("failed_status", this.zzacM.getErrorCode());
            outState.putParcelable("failed_resolution", this.zzacM.getResolution());
        }
    }

    public void onStart() {
        super.onStart();
        this.mStarted = true;
        if (!this.zzacK) {
            for (int i = 0; i < this.zzacP.size(); i++) {
                ((zza) this.zzacP.valueAt(i)).zzacR.connect();
            }
        }
    }

    public void onStop() {
        super.onStop();
        this.mStarted = false;
        for (int i = 0; i < this.zzacP.size(); i++) {
            ((zza) this.zzacP.valueAt(i)).zzacR.disconnect();
        }
    }

    public void zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzb((Object) googleApiClient, (Object) "GoogleApiClient instance cannot be null");
        zzx.zza(this.zzacP.indexOfKey(i) < 0, "Already managing a GoogleApiClient with id " + i);
        this.zzacP.put(i, new zza(this, i, googleApiClient, onConnectionFailedListener));
        if (this.mStarted && !this.zzacK) {
            googleApiClient.connect();
        }
    }

    public void zzbp(int i) {
        zza com_google_android_gms_internal_zzlp_zza = (zza) this.zzacP.get(i);
        this.zzacP.remove(i);
        if (com_google_android_gms_internal_zzlp_zza != null) {
            com_google_android_gms_internal_zzlp_zza.zzom();
        }
    }
}
