package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.google.android.gms.dynamic.zzc.zza;

public final class zzh extends zza {
    private Fragment zzafl;

    private zzh(Fragment fragment) {
        this.zzafl = fragment;
    }

    public static zzh zza(Fragment fragment) {
        return fragment != null ? new zzh(fragment) : null;
    }

    public Bundle getArguments() {
        return this.zzafl.getArguments();
    }

    public int getId() {
        return this.zzafl.getId();
    }

    public boolean getRetainInstance() {
        return this.zzafl.getRetainInstance();
    }

    public String getTag() {
        return this.zzafl.getTag();
    }

    public int getTargetRequestCode() {
        return this.zzafl.getTargetRequestCode();
    }

    public boolean getUserVisibleHint() {
        return this.zzafl.getUserVisibleHint();
    }

    public zzd getView() {
        return zze.zzy(this.zzafl.getView());
    }

    public boolean isAdded() {
        return this.zzafl.isAdded();
    }

    public boolean isDetached() {
        return this.zzafl.isDetached();
    }

    public boolean isHidden() {
        return this.zzafl.isHidden();
    }

    public boolean isInLayout() {
        return this.zzafl.isInLayout();
    }

    public boolean isRemoving() {
        return this.zzafl.isRemoving();
    }

    public boolean isResumed() {
        return this.zzafl.isResumed();
    }

    public boolean isVisible() {
        return this.zzafl.isVisible();
    }

    public void setHasOptionsMenu(boolean hasMenu) {
        this.zzafl.setHasOptionsMenu(hasMenu);
    }

    public void setMenuVisibility(boolean menuVisible) {
        this.zzafl.setMenuVisibility(menuVisible);
    }

    public void setRetainInstance(boolean retain) {
        this.zzafl.setRetainInstance(retain);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.zzafl.setUserVisibleHint(isVisibleToUser);
    }

    public void startActivity(Intent intent) {
        this.zzafl.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.zzafl.startActivityForResult(intent, requestCode);
    }

    public void zzn(zzd com_google_android_gms_dynamic_zzd) {
        this.zzafl.registerForContextMenu((View) zze.zzp(com_google_android_gms_dynamic_zzd));
    }

    public void zzo(zzd com_google_android_gms_dynamic_zzd) {
        this.zzafl.unregisterForContextMenu((View) zze.zzp(com_google_android_gms_dynamic_zzd));
    }

    public zzd zzsa() {
        return zze.zzy(this.zzafl.getActivity());
    }

    public zzc zzsb() {
        return zza(this.zzafl.getParentFragment());
    }

    public zzd zzsc() {
        return zze.zzy(this.zzafl.getResources());
    }

    public zzc zzsd() {
        return zza(this.zzafl.getTargetFragment());
    }
}
