package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzg;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class zza<T extends LifecycleDelegate> {
    private T zzapn;
    private Bundle zzapo;
    private LinkedList<zza> zzapp;
    private final zzf<T> zzapq;

    /* renamed from: com.google.android.gms.dynamic.zza.1 */
    class C04301 implements zzf<T> {
        final /* synthetic */ zza zzapr;

        C04301(zza com_google_android_gms_dynamic_zza) {
            this.zzapr = com_google_android_gms_dynamic_zza;
        }

        public void zza(T t) {
            this.zzapr.zzapn = t;
            Iterator it = this.zzapr.zzapp.iterator();
            while (it.hasNext()) {
                ((zza) it.next()).zzb(this.zzapr.zzapn);
            }
            this.zzapr.zzapp.clear();
            this.zzapr.zzapo = null;
        }
    }

    private interface zza {
        int getState();

        void zzb(LifecycleDelegate lifecycleDelegate);
    }

    /* renamed from: com.google.android.gms.dynamic.zza.2 */
    class C04312 implements zza {
        final /* synthetic */ zza zzapr;
        final /* synthetic */ Activity zzaps;
        final /* synthetic */ Bundle zzapt;
        final /* synthetic */ Bundle zzapu;

        C04312(zza com_google_android_gms_dynamic_zza, Activity activity, Bundle bundle, Bundle bundle2) {
            this.zzapr = com_google_android_gms_dynamic_zza;
            this.zzaps = activity;
            this.zzapt = bundle;
            this.zzapu = bundle2;
        }

        public int getState() {
            return 0;
        }

        public void zzb(LifecycleDelegate lifecycleDelegate) {
            this.zzapr.zzapn.onInflate(this.zzaps, this.zzapt, this.zzapu);
        }
    }

    /* renamed from: com.google.android.gms.dynamic.zza.3 */
    class C04323 implements zza {
        final /* synthetic */ zza zzapr;
        final /* synthetic */ Bundle zzapu;

        C04323(zza com_google_android_gms_dynamic_zza, Bundle bundle) {
            this.zzapr = com_google_android_gms_dynamic_zza;
            this.zzapu = bundle;
        }

        public int getState() {
            return 1;
        }

        public void zzb(LifecycleDelegate lifecycleDelegate) {
            this.zzapr.zzapn.onCreate(this.zzapu);
        }
    }

    /* renamed from: com.google.android.gms.dynamic.zza.4 */
    class C04334 implements zza {
        final /* synthetic */ zza zzapr;
        final /* synthetic */ Bundle zzapu;
        final /* synthetic */ FrameLayout zzapv;
        final /* synthetic */ LayoutInflater zzapw;
        final /* synthetic */ ViewGroup zzapx;

        C04334(zza com_google_android_gms_dynamic_zza, FrameLayout frameLayout, LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            this.zzapr = com_google_android_gms_dynamic_zza;
            this.zzapv = frameLayout;
            this.zzapw = layoutInflater;
            this.zzapx = viewGroup;
            this.zzapu = bundle;
        }

        public int getState() {
            return 2;
        }

        public void zzb(LifecycleDelegate lifecycleDelegate) {
            this.zzapv.removeAllViews();
            this.zzapv.addView(this.zzapr.zzapn.onCreateView(this.zzapw, this.zzapx, this.zzapu));
        }
    }

    /* renamed from: com.google.android.gms.dynamic.zza.5 */
    static class C04345 implements OnClickListener {
        final /* synthetic */ int zzapy;
        final /* synthetic */ Context zzry;

        C04345(Context context, int i) {
            this.zzry = context;
            this.zzapy = i;
        }

        public void onClick(View v) {
            this.zzry.startActivity(GooglePlayServicesUtil.zzbj(this.zzapy));
        }
    }

    /* renamed from: com.google.android.gms.dynamic.zza.6 */
    class C04356 implements zza {
        final /* synthetic */ zza zzapr;

        C04356(zza com_google_android_gms_dynamic_zza) {
            this.zzapr = com_google_android_gms_dynamic_zza;
        }

        public int getState() {
            return 4;
        }

        public void zzb(LifecycleDelegate lifecycleDelegate) {
            this.zzapr.zzapn.onStart();
        }
    }

    /* renamed from: com.google.android.gms.dynamic.zza.7 */
    class C04367 implements zza {
        final /* synthetic */ zza zzapr;

        C04367(zza com_google_android_gms_dynamic_zza) {
            this.zzapr = com_google_android_gms_dynamic_zza;
        }

        public int getState() {
            return 5;
        }

        public void zzb(LifecycleDelegate lifecycleDelegate) {
            this.zzapr.zzapn.onResume();
        }
    }

    public zza() {
        this.zzapq = new C04301(this);
    }

    private void zza(Bundle bundle, zza com_google_android_gms_dynamic_zza_zza) {
        if (this.zzapn != null) {
            com_google_android_gms_dynamic_zza_zza.zzb(this.zzapn);
            return;
        }
        if (this.zzapp == null) {
            this.zzapp = new LinkedList();
        }
        this.zzapp.add(com_google_android_gms_dynamic_zza_zza);
        if (bundle != null) {
            if (this.zzapo == null) {
                this.zzapo = (Bundle) bundle.clone();
            } else {
                this.zzapo.putAll(bundle);
            }
        }
        zza(this.zzapq);
    }

    public static void zzb(FrameLayout frameLayout) {
        Context context = frameLayout.getContext();
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        CharSequence zzc = zzg.zzc(context, isGooglePlayServicesAvailable, GooglePlayServicesUtil.zzaf(context));
        CharSequence zzh = zzg.zzh(context, isGooglePlayServicesAvailable);
        View linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        View textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(zzc);
        linearLayout.addView(textView);
        if (zzh != null) {
            View button = new Button(context);
            button.setLayoutParams(new LayoutParams(-2, -2));
            button.setText(zzh);
            linearLayout.addView(button);
            button.setOnClickListener(new C04345(context, isGooglePlayServicesAvailable));
        }
    }

    private void zzer(int i) {
        while (!this.zzapp.isEmpty() && ((zza) this.zzapp.getLast()).getState() >= i) {
            this.zzapp.removeLast();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        zza(savedInstanceState, new C04323(this, savedInstanceState));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(inflater.getContext());
        zza(savedInstanceState, new C04334(this, frameLayout, inflater, container, savedInstanceState));
        if (this.zzapn == null) {
            zza(frameLayout);
        }
        return frameLayout;
    }

    public void onDestroy() {
        if (this.zzapn != null) {
            this.zzapn.onDestroy();
        } else {
            zzer(1);
        }
    }

    public void onDestroyView() {
        if (this.zzapn != null) {
            this.zzapn.onDestroyView();
        } else {
            zzer(2);
        }
    }

    public void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
        zza(savedInstanceState, new C04312(this, activity, attrs, savedInstanceState));
    }

    public void onLowMemory() {
        if (this.zzapn != null) {
            this.zzapn.onLowMemory();
        }
    }

    public void onPause() {
        if (this.zzapn != null) {
            this.zzapn.onPause();
        } else {
            zzer(5);
        }
    }

    public void onResume() {
        zza(null, new C04367(this));
    }

    public void onSaveInstanceState(Bundle outState) {
        if (this.zzapn != null) {
            this.zzapn.onSaveInstanceState(outState);
        } else if (this.zzapo != null) {
            outState.putAll(this.zzapo);
        }
    }

    public void onStart() {
        zza(null, new C04356(this));
    }

    public void onStop() {
        if (this.zzapn != null) {
            this.zzapn.onStop();
        } else {
            zzer(4);
        }
    }

    protected void zza(FrameLayout frameLayout) {
        zzb(frameLayout);
    }

    protected abstract void zza(zzf<T> com_google_android_gms_dynamic_zzf_T);

    public T zzrZ() {
        return this.zzapn;
    }
}
