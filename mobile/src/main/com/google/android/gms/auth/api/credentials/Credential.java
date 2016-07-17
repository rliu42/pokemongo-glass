package com.google.android.gms.auth.api.credentials;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.util.Collections;
import java.util.List;

public class Credential implements SafeParcelable {
    public static final Creator<Credential> CREATOR;
    public static final String EXTRA_KEY = "com.google.android.gms.credentials.Credential";
    private final String mName;
    final int mVersionCode;
    private final Uri zzSh;
    private final List<IdToken> zzSi;
    private final String zzSj;
    private final String zzSk;
    private final String zzSl;
    private final String zzSm;
    private final String zzwN;

    public static class Builder {
        private String mName;
        private Uri zzSh;
        private List<IdToken> zzSi;
        private String zzSj;
        private String zzSk;
        private String zzSl;
        private String zzSm;
        private final String zzwN;

        public Builder(Credential credential) {
            this.zzwN = credential.zzwN;
            this.mName = credential.mName;
            this.zzSh = credential.zzSh;
            this.zzSi = credential.zzSi;
            this.zzSj = credential.zzSj;
            this.zzSk = credential.zzSk;
            this.zzSl = credential.zzSl;
            this.zzSm = credential.zzSm;
        }

        public Builder(String id) {
            this.zzwN = id;
        }

        public Credential build() {
            if (TextUtils.isEmpty(this.zzSj) || TextUtils.isEmpty(this.zzSk)) {
                return new Credential(3, this.zzwN, this.mName, this.zzSh, this.zzSi, this.zzSj, this.zzSk, this.zzSl, this.zzSm);
            }
            throw new IllegalStateException("Only one of password or accountType may be set");
        }

        public Builder setAccountType(String accountType) {
            String scheme = Uri.parse(accountType).getScheme();
            boolean z = Scheme.HTTP.equalsIgnoreCase(scheme) || Scheme.HTTPS.equalsIgnoreCase(scheme);
            zzx.zzaa(z);
            this.zzSk = accountType;
            return this;
        }

        public Builder setName(String name) {
            this.mName = name;
            return this;
        }

        public Builder setPassword(String password) {
            this.zzSj = password;
            return this;
        }

        public Builder setProfilePictureUri(Uri profilePictureUri) {
            this.zzSh = profilePictureUri;
            return this;
        }
    }

    static {
        CREATOR = new zza();
    }

    Credential(int version, String id, String name, Uri profilePictureUri, List<IdToken> idTokens, String password, String accountType, String generatedPassword, String generatedHintId) {
        this.mVersionCode = version;
        this.zzwN = (String) zzx.zzw(id);
        this.mName = name;
        this.zzSh = profilePictureUri;
        this.zzSi = idTokens == null ? Collections.emptyList() : Collections.unmodifiableList(idTokens);
        this.zzSj = password;
        this.zzSk = accountType;
        this.zzSl = generatedPassword;
        this.zzSm = generatedHintId;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Credential)) {
            return false;
        }
        Credential credential = (Credential) other;
        return TextUtils.equals(this.zzwN, credential.zzwN) && TextUtils.equals(this.mName, credential.mName) && zzw.equal(this.zzSh, credential.zzSh) && TextUtils.equals(this.zzSj, credential.zzSj) && TextUtils.equals(this.zzSk, credential.zzSk) && TextUtils.equals(this.zzSl, credential.zzSl);
    }

    public String getAccountType() {
        return this.zzSk;
    }

    public String getGeneratedPassword() {
        return this.zzSl;
    }

    public String getId() {
        return this.zzwN;
    }

    public List<IdToken> getIdTokens() {
        return this.zzSi;
    }

    public String getName() {
        return this.mName;
    }

    public String getPassword() {
        return this.zzSj;
    }

    public Uri getProfilePictureUri() {
        return this.zzSh;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzwN, this.mName, this.zzSh, this.zzSj, this.zzSk, this.zzSl);
    }

    public void writeToParcel(Parcel out, int flags) {
        zza.zza(this, out, flags);
    }

    public String zzlI() {
        return this.zzSm;
    }
}
