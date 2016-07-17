package com.google.android.gms.auth.api.proxy;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ProxyResponse implements SafeParcelable {
    public static final Creator<ProxyResponse> CREATOR;
    public static final int STATUS_CODE_NO_CONNECTION = -1;
    public final byte[] body;
    public final int googlePlayServicesStatusCode;
    public final PendingIntent recoveryAction;
    public final int statusCode;
    final int versionCode;
    final Bundle zzSK;

    static {
        CREATOR = new zzc();
    }

    ProxyResponse(int version, int googlePlayServicesStatusCode, PendingIntent recoveryAction, int statusCode, Bundle headers, byte[] body) {
        this.versionCode = version;
        this.googlePlayServicesStatusCode = googlePlayServicesStatusCode;
        this.statusCode = statusCode;
        this.zzSK = headers;
        this.body = body;
        this.recoveryAction = recoveryAction;
    }

    public ProxyResponse(int googlePlayServicesStatusCode, PendingIntent recoveryAction, int statusCode, Bundle headers, byte[] body) {
        this(1, googlePlayServicesStatusCode, recoveryAction, statusCode, headers, body);
    }

    private ProxyResponse(int responseCode, Bundle headers, byte[] body) {
        this(1, 0, null, responseCode, headers, body);
    }

    public ProxyResponse(int responseCode, Map<String, String> headers, byte[] body) {
        this(responseCode, zzE(headers), body);
    }

    public static ProxyResponse createErrorProxyResponse(int googlePlayServicesStatusCode, PendingIntent recoveryAction, int statusCode, Map<String, String> headers, byte[] body) {
        return new ProxyResponse(1, googlePlayServicesStatusCode, recoveryAction, statusCode, zzE(headers), body);
    }

    private static Bundle zzE(Map<String, String> map) {
        Bundle bundle = new Bundle();
        if (map == null) {
            return bundle;
        }
        for (Entry entry : map.entrySet()) {
            bundle.putString((String) entry.getKey(), (String) entry.getValue());
        }
        return bundle;
    }

    public int describeContents() {
        return 0;
    }

    public Map<String, String> getHeaders() {
        Map<String, String> hashMap = new HashMap();
        for (String str : this.zzSK.keySet()) {
            hashMap.put(str, this.zzSK.getString(str));
        }
        return hashMap;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzc.zza(this, parcel, flags);
    }
}
