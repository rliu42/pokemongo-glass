package com.google.android.gms.auth.api.proxy;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Patterns;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import spacemadness.com.lunarconsole.BuildConfig;

public class ProxyRequest implements SafeParcelable {
    public static final Creator<ProxyRequest> CREATOR;
    public static final int HTTP_METHOD_DELETE;
    public static final int HTTP_METHOD_GET;
    public static final int HTTP_METHOD_HEAD;
    public static final int HTTP_METHOD_OPTIONS;
    public static final int HTTP_METHOD_PATCH;
    public static final int HTTP_METHOD_POST;
    public static final int HTTP_METHOD_PUT;
    public static final int HTTP_METHOD_TRACE;
    public static final int LAST_CODE;
    public static final int VERSION_CODE = 2;
    public final byte[] body;
    public final int httpMethod;
    public final long timeoutMillis;
    public final String url;
    final int versionCode;
    Bundle zzSK;

    public static class Builder {
        private String zzSL;
        private int zzSM;
        private long zzSN;
        private byte[] zzSO;
        private Bundle zzSP;

        public Builder(String url) {
            this.zzSM = ProxyRequest.HTTP_METHOD_GET;
            this.zzSN = 3000;
            this.zzSO = null;
            this.zzSP = new Bundle();
            zzx.zzcr(url);
            if (Patterns.WEB_URL.matcher(url).matches()) {
                this.zzSL = url;
                return;
            }
            throw new IllegalArgumentException("The supplied url [ " + url + "] is not match Patterns.WEB_URL!");
        }

        public ProxyRequest build() {
            if (this.zzSO == null) {
                this.zzSO = new byte[ProxyRequest.LAST_CODE];
            }
            return new ProxyRequest(ProxyRequest.VERSION_CODE, this.zzSL, this.zzSM, this.zzSN, this.zzSO, this.zzSP);
        }

        public Builder putHeader(String name, String value) {
            zzx.zzh(name, "Header name cannot be null or empty!");
            Bundle bundle = this.zzSP;
            if (value == null) {
                value = BuildConfig.FLAVOR;
            }
            bundle.putString(name, value);
            return this;
        }

        public Builder setBody(byte[] body) {
            this.zzSO = body;
            return this;
        }

        public Builder setHttpMethod(int method) {
            boolean z = method >= 0 && method <= ProxyRequest.LAST_CODE;
            zzx.zzb(z, (Object) "Unrecognized http method code.");
            this.zzSM = method;
            return this;
        }

        public Builder setTimeoutMillis(long timeoutMillis) {
            zzx.zzb(timeoutMillis >= 0, (Object) "The specified timeout must be non-negative.");
            this.zzSN = timeoutMillis;
            return this;
        }
    }

    static {
        CREATOR = new zzb();
        HTTP_METHOD_GET = LAST_CODE;
        HTTP_METHOD_POST = 1;
        HTTP_METHOD_PUT = VERSION_CODE;
        HTTP_METHOD_DELETE = 3;
        HTTP_METHOD_HEAD = 4;
        HTTP_METHOD_OPTIONS = 5;
        HTTP_METHOD_TRACE = 6;
        HTTP_METHOD_PATCH = 7;
        LAST_CODE = 7;
    }

    ProxyRequest(int version, String googleUrl, int httpMethod, long timeoutMillis, byte[] body, Bundle headers) {
        this.versionCode = version;
        this.url = googleUrl;
        this.httpMethod = httpMethod;
        this.timeoutMillis = timeoutMillis;
        this.body = body;
        this.zzSK = headers;
    }

    public int describeContents() {
        return LAST_CODE;
    }

    public Map<String, String> getHeaderMap() {
        Map linkedHashMap = new LinkedHashMap(this.zzSK.size());
        for (String str : this.zzSK.keySet()) {
            linkedHashMap.put(str, this.zzSK.getString(str));
        }
        return Collections.unmodifiableMap(linkedHashMap);
    }

    public String toString() {
        return "ProxyRequest[ url: " + this.url + ", method: " + this.httpMethod + " ]";
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzb.zza(this, parcel, flags);
    }
}
