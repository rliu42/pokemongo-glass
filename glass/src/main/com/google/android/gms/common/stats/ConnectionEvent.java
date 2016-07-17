package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import spacemadness.com.lunarconsole.BuildConfig;

public final class ConnectionEvent extends zzf implements SafeParcelable {
    public static final Creator<ConnectionEvent> CREATOR;
    final int mVersionCode;
    private final long zzahn;
    private int zzaho;
    private final String zzahp;
    private final String zzahq;
    private final String zzahr;
    private final String zzahs;
    private final String zzaht;
    private final String zzahu;
    private final long zzahv;
    private final long zzahw;
    private long zzahx;

    static {
        CREATOR = new zza();
    }

    ConnectionEvent(int versionCode, long timeMillis, int eventType, String callingProcess, String callingService, String targetProcess, String targetService, String stackTrace, String connKey, long elapsedRealtime, long heapAlloc) {
        this.mVersionCode = versionCode;
        this.zzahn = timeMillis;
        this.zzaho = eventType;
        this.zzahp = callingProcess;
        this.zzahq = callingService;
        this.zzahr = targetProcess;
        this.zzahs = targetService;
        this.zzahx = -1;
        this.zzaht = stackTrace;
        this.zzahu = connKey;
        this.zzahv = elapsedRealtime;
        this.zzahw = heapAlloc;
    }

    public ConnectionEvent(long timeMillis, int eventType, String callingProcess, String callingService, String targetProcess, String targetService, String stackTrace, String connKey, long elapsedRealtime, long heapAlloc) {
        this(1, timeMillis, eventType, callingProcess, callingService, targetProcess, targetService, stackTrace, connKey, elapsedRealtime, heapAlloc);
    }

    public int describeContents() {
        return 0;
    }

    public int getEventType() {
        return this.zzaho;
    }

    public long getTimeMillis() {
        return this.zzahn;
    }

    public void writeToParcel(Parcel out, int flags) {
        zza.zza(this, out, flags);
    }

    public String zzpX() {
        return this.zzahp;
    }

    public String zzpY() {
        return this.zzahq;
    }

    public String zzpZ() {
        return this.zzahr;
    }

    public String zzqa() {
        return this.zzahs;
    }

    public String zzqb() {
        return this.zzaht;
    }

    public String zzqc() {
        return this.zzahu;
    }

    public long zzqd() {
        return this.zzahx;
    }

    public long zzqe() {
        return this.zzahw;
    }

    public long zzqf() {
        return this.zzahv;
    }

    public String zzqg() {
        return "\t" + zzpX() + "/" + zzpY() + "\t" + zzpZ() + "/" + zzqa() + "\t" + (this.zzaht == null ? BuildConfig.FLAVOR : this.zzaht) + "\t" + zzqe();
    }
}
