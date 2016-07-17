package com.google.android.gms.location.places;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;

public class PlacePhotoResult implements Result, SafeParcelable {
    public static final Creator<PlacePhotoResult> CREATOR;
    private final Bitmap mBitmap;
    final int mVersionCode;
    private final Status zzSC;
    final BitmapTeleporter zzaGs;

    static {
        CREATOR = new zzi();
    }

    PlacePhotoResult(int versionCode, Status status, BitmapTeleporter teleporter) {
        this.mVersionCode = versionCode;
        this.zzSC = status;
        this.zzaGs = teleporter;
        if (this.zzaGs != null) {
            this.mBitmap = teleporter.zzos();
        } else {
            this.mBitmap = null;
        }
    }

    public PlacePhotoResult(Status status, BitmapTeleporter teleporter) {
        this.mVersionCode = 0;
        this.zzSC = status;
        this.zzaGs = teleporter;
        if (this.zzaGs != null) {
            this.mBitmap = teleporter.zzos();
        } else {
            this.mBitmap = null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public Status getStatus() {
        return this.zzSC;
    }

    public String toString() {
        return zzw.zzv(this).zzg(NotificationCompatApi21.CATEGORY_STATUS, this.zzSC).zzg("bitmap", this.mBitmap).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzi.zza(this, parcel, flags);
    }
}
