package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public final class CredentialRequest implements SafeParcelable {
    public static final Creator<CredentialRequest> CREATOR;
    final int mVersionCode;
    private final boolean zzSo;
    private final String[] zzSp;
    private final CredentialPickerConfig zzSq;
    private final CredentialPickerConfig zzSr;

    public static final class Builder {
        private boolean zzSo;
        private String[] zzSp;
        private CredentialPickerConfig zzSq;
        private CredentialPickerConfig zzSr;

        public CredentialRequest build() {
            if (this.zzSp == null) {
                this.zzSp = new String[0];
            }
            if (this.zzSo || this.zzSp.length != 0) {
                return new CredentialRequest();
            }
            throw new IllegalStateException("At least one authentication method must be specified");
        }

        public Builder setAccountTypes(String... accountTypes) {
            this.zzSp = accountTypes;
            return this;
        }

        public Builder setCredentialHintPickerConfig(CredentialPickerConfig config) {
            this.zzSr = config;
            return this;
        }

        public Builder setCredentialPickerConfig(CredentialPickerConfig config) {
            this.zzSq = config;
            return this;
        }

        public Builder setSupportsPasswordLogin(boolean supportsPasswordLogin) {
            this.zzSo = supportsPasswordLogin;
            return this;
        }
    }

    static {
        CREATOR = new zzc();
    }

    CredentialRequest(int version, boolean supportsPasswordLogin, String[] accountTypes, CredentialPickerConfig credentialPickerConfig, CredentialPickerConfig credentialHintPickerConfig) {
        this.mVersionCode = version;
        this.zzSo = supportsPasswordLogin;
        this.zzSp = (String[]) zzx.zzw(accountTypes);
        if (credentialPickerConfig == null) {
            credentialPickerConfig = new com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Builder().build();
        }
        this.zzSq = credentialPickerConfig;
        if (credentialHintPickerConfig == null) {
            credentialHintPickerConfig = new com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Builder().build();
        }
        this.zzSr = credentialHintPickerConfig;
    }

    private CredentialRequest(Builder builder) {
        this(2, builder.zzSo, builder.zzSp, builder.zzSq, builder.zzSr);
    }

    public int describeContents() {
        return 0;
    }

    public String[] getAccountTypes() {
        return this.zzSp;
    }

    public CredentialPickerConfig getCredentialHintPickerConfig() {
        return this.zzSr;
    }

    public CredentialPickerConfig getCredentialPickerConfig() {
        return this.zzSq;
    }

    public boolean getSupportsPasswordLogin() {
        return this.zzSo;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }
}
