package com.google.android.gms.location.places;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.maps.model.LatLng;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AddPlaceRequest implements SafeParcelable {
    public static final Creator<AddPlaceRequest> CREATOR;
    private final String mName;
    final int mVersionCode;
    private final LatLng zzaFS;
    private final List<Integer> zzaFT;
    private final String zzaFU;
    private final Uri zzaFV;
    private final String zzapU;

    static {
        CREATOR = new zzb();
    }

    AddPlaceRequest(int versionCode, String name, LatLng latLng, String address, List<Integer> placeTypes, String phoneNumber, Uri websiteUri) {
        boolean z = false;
        this.mVersionCode = versionCode;
        this.mName = zzx.zzcr(name);
        this.zzaFS = (LatLng) zzx.zzw(latLng);
        this.zzapU = zzx.zzcr(address);
        this.zzaFT = new ArrayList((Collection) zzx.zzw(placeTypes));
        zzx.zzb(!this.zzaFT.isEmpty(), (Object) "At least one place type should be provided.");
        if (!(TextUtils.isEmpty(phoneNumber) && websiteUri == null)) {
            z = true;
        }
        zzx.zzb(z, (Object) "One of phone number or URI should be provided.");
        this.zzaFU = phoneNumber;
        this.zzaFV = websiteUri;
    }

    public AddPlaceRequest(String name, LatLng latLng, String address, List<Integer> placeTypes, Uri uri) {
        this(name, latLng, address, placeTypes, null, (Uri) zzx.zzw(uri));
    }

    public AddPlaceRequest(String name, LatLng latLng, String address, List<Integer> placeTypes, String phoneNumber) {
        this(name, latLng, address, placeTypes, zzx.zzcr(phoneNumber), null);
    }

    public AddPlaceRequest(String name, LatLng latLng, String address, List<Integer> placeTypes, String phoneNumber, Uri uri) {
        this(0, name, latLng, address, placeTypes, phoneNumber, uri);
    }

    public int describeContents() {
        return 0;
    }

    public String getAddress() {
        return this.zzapU;
    }

    public LatLng getLatLng() {
        return this.zzaFS;
    }

    public String getName() {
        return this.mName;
    }

    public String getPhoneNumber() {
        return this.zzaFU;
    }

    public List<Integer> getPlaceTypes() {
        return this.zzaFT;
    }

    public Uri getWebsiteUri() {
        return this.zzaFV;
    }

    public String toString() {
        return zzw.zzv(this).zzg(Twitter.NAME, this.mName).zzg("latLng", this.zzaFS).zzg("address", this.zzapU).zzg("placeTypes", this.zzaFT).zzg("phoneNumer", this.zzaFU).zzg("websiteUri", this.zzaFV).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzb.zza(this, parcel, flags);
    }
}
