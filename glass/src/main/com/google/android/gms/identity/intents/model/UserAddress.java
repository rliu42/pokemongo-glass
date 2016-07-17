package com.google.android.gms.identity.intents.model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.identity.intents.AddressConstants.Extras;

public final class UserAddress implements SafeParcelable {
    public static final Creator<UserAddress> CREATOR;
    private final int mVersionCode;
    String name;
    String phoneNumber;
    String zzGw;
    String zzaDk;
    String zzaDl;
    String zzaDm;
    String zzaDn;
    String zzaDo;
    String zzaDp;
    String zzaDq;
    String zzaDr;
    String zzaDs;
    boolean zzaDt;
    String zzaDu;
    String zzaDv;

    static {
        CREATOR = new zzb();
    }

    UserAddress() {
        this.mVersionCode = 1;
    }

    UserAddress(int versionCode, String name, String address1, String address2, String address3, String address4, String address5, String administrativeArea, String locality, String countryCode, String postalCode, String sortingCode, String phoneNumber, boolean isPostBox, String companyName, String emailAddress) {
        this.mVersionCode = versionCode;
        this.name = name;
        this.zzaDk = address1;
        this.zzaDl = address2;
        this.zzaDm = address3;
        this.zzaDn = address4;
        this.zzaDo = address5;
        this.zzaDp = administrativeArea;
        this.zzaDq = locality;
        this.zzGw = countryCode;
        this.zzaDr = postalCode;
        this.zzaDs = sortingCode;
        this.phoneNumber = phoneNumber;
        this.zzaDt = isPostBox;
        this.zzaDu = companyName;
        this.zzaDv = emailAddress;
    }

    public static UserAddress fromIntent(Intent data) {
        return (data == null || !data.hasExtra(Extras.EXTRA_ADDRESS)) ? null : (UserAddress) data.getParcelableExtra(Extras.EXTRA_ADDRESS);
    }

    public int describeContents() {
        return 0;
    }

    public String getAddress1() {
        return this.zzaDk;
    }

    public String getAddress2() {
        return this.zzaDl;
    }

    public String getAddress3() {
        return this.zzaDm;
    }

    public String getAddress4() {
        return this.zzaDn;
    }

    public String getAddress5() {
        return this.zzaDo;
    }

    public String getAdministrativeArea() {
        return this.zzaDp;
    }

    public String getCompanyName() {
        return this.zzaDu;
    }

    public String getCountryCode() {
        return this.zzGw;
    }

    public String getEmailAddress() {
        return this.zzaDv;
    }

    public String getLocality() {
        return this.zzaDq;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getPostalCode() {
        return this.zzaDr;
    }

    public String getSortingCode() {
        return this.zzaDs;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public boolean isPostBox() {
        return this.zzaDt;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
