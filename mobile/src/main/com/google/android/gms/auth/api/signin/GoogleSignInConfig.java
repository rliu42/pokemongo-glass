package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.internal.zzc;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GoogleSignInConfig implements Optional, SafeParcelable {
    public static final Creator<GoogleSignInConfig> CREATOR;
    public static final Scope zzTe;
    public static final Scope zzTf;
    public static final Scope zzTg;
    public static final GoogleSignInConfig zzTh;
    final int versionCode;
    private Account zzQd;
    private final ArrayList<Scope> zzSX;
    private boolean zzTi;
    private final boolean zzTj;
    private final boolean zzTk;
    private String zzTl;

    public static final class zza {
        private Account zzQd;
        private boolean zzTi;
        private boolean zzTj;
        private boolean zzTk;
        private String zzTl;
        private Set<Scope> zzTm;

        public zza() {
            this.zzTm = new HashSet(Arrays.asList(new Scope[]{GoogleSignInConfig.zzTg}));
        }

        public GoogleSignInConfig zzmc() {
            return new GoogleSignInConfig(this.zzQd, this.zzTi, this.zzTj, this.zzTk, this.zzTl, null);
        }
    }

    static {
        zzTe = new Scope(Scopes.PROFILE);
        zzTf = new Scope(Scopes.EMAIL);
        zzTg = new Scope("openid");
        zzTh = new zza().zzmc();
        CREATOR = new zze();
    }

    GoogleSignInConfig(int versionCode, ArrayList<Scope> scopes, Account account, boolean idTokenRequested, boolean serverAuthCodeRequested, boolean forceCodeForRefreshToken, String serverClientId) {
        this.versionCode = versionCode;
        this.zzSX = scopes;
        this.zzQd = account;
        this.zzTi = idTokenRequested;
        this.zzTj = serverAuthCodeRequested;
        this.zzTk = forceCodeForRefreshToken;
        this.zzTl = serverClientId;
    }

    private GoogleSignInConfig(Set<Scope> scopes, Account account, boolean idTokenRequested, boolean serverAuthCodeRequested, boolean forceCodeForRefreshToken, String serverClientId) {
        this(1, new ArrayList(scopes), account, idTokenRequested, serverAuthCodeRequested, forceCodeForRefreshToken, serverClientId);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            GoogleSignInConfig googleSignInConfig = (GoogleSignInConfig) obj;
            if (this.zzSX.size() != googleSignInConfig.zzlS().size() || !this.zzSX.containsAll(googleSignInConfig.zzlS())) {
                return false;
            }
            if (this.zzQd == null) {
                if (googleSignInConfig.getAccount() != null) {
                    return false;
                }
            } else if (!this.zzQd.equals(googleSignInConfig.getAccount())) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzTl)) {
                if (!TextUtils.isEmpty(googleSignInConfig.zzmb())) {
                    return false;
                }
            } else if (!this.zzTl.equals(googleSignInConfig.zzmb())) {
                return false;
            }
            return this.zzTk == googleSignInConfig.zzma() && this.zzTi == googleSignInConfig.zzlY() && this.zzTj == googleSignInConfig.zzlZ();
        } catch (ClassCastException e) {
            return false;
        }
    }

    public Account getAccount() {
        return this.zzQd;
    }

    public int hashCode() {
        List arrayList = new ArrayList();
        Iterator it = this.zzSX.iterator();
        while (it.hasNext()) {
            arrayList.add(((Scope) it.next()).zznG());
        }
        Collections.sort(arrayList);
        return new zzc().zzl(arrayList).zzl(this.zzQd).zzl(this.zzTl).zzP(this.zzTk).zzP(this.zzTi).zzP(this.zzTj).zzmd();
    }

    public void writeToParcel(Parcel out, int flags) {
        zze.zza(this, out, flags);
    }

    public ArrayList<Scope> zzlS() {
        return new ArrayList(this.zzSX);
    }

    public boolean zzlY() {
        return this.zzTi;
    }

    public boolean zzlZ() {
        return this.zzTj;
    }

    public boolean zzma() {
        return this.zzTk;
    }

    public String zzmb() {
        return this.zzTl;
    }
}
