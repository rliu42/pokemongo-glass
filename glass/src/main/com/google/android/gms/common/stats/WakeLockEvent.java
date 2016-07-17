package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.List;
import spacemadness.com.lunarconsole.BuildConfig;

public final class WakeLockEvent extends zzf implements SafeParcelable {
    public static final Creator<WakeLockEvent> CREATOR;
    private final long mTimeout;
    final int mVersionCode;
    private final long zzahn;
    private int zzaho;
    private final long zzahv;
    private long zzahx;
    private final String zzaia;
    private final int zzaib;
    private final List<String> zzaic;
    private final String zzaid;
    private int zzaie;
    private final String zzaif;
    private final String zzaig;
    private final float zzaih;

    static {
        CREATOR = new zzh();
    }

    WakeLockEvent(int versionCode, long timeMillis, int eventType, String wakelockName, int wakelockType, List<String> callingPackages, String eventKey, long elapsedRealtime, int deviceState, String secondaryWakeLockName, String hostPackageName, float beginPowerPercentage, long timeout) {
        this.mVersionCode = versionCode;
        this.zzahn = timeMillis;
        this.zzaho = eventType;
        this.zzaia = wakelockName;
        this.zzaif = secondaryWakeLockName;
        this.zzaib = wakelockType;
        this.zzahx = -1;
        this.zzaic = callingPackages;
        this.zzaid = eventKey;
        this.zzahv = elapsedRealtime;
        this.zzaie = deviceState;
        this.zzaig = hostPackageName;
        this.zzaih = beginPowerPercentage;
        this.mTimeout = timeout;
    }

    public WakeLockEvent(long timeMillis, int eventType, String wakelockName, int wakelockType, List<String> callingPackages, String eventKey, long elapsedRealtime, int deviceState, String secondaryWakeLockName, String hostPackageName, float beginPowerPercentage, long timeout) {
        this(1, timeMillis, eventType, wakelockName, wakelockType, callingPackages, eventKey, elapsedRealtime, deviceState, secondaryWakeLockName, hostPackageName, beginPowerPercentage, timeout);
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
        zzh.zza(this, out, flags);
    }

    public String zzqc() {
        return this.zzaid;
    }

    public long zzqd() {
        return this.zzahx;
    }

    public long zzqf() {
        return this.zzahv;
    }

    public String zzqg() {
        return "\t" + zzqj() + "\t" + zzql() + "\t" + (zzqm() == null ? BuildConfig.FLAVOR : TextUtils.join(",", zzqm())) + "\t" + zzqn() + "\t" + (zzqk() == null ? BuildConfig.FLAVOR : zzqk()) + "\t" + (zzqo() == null ? BuildConfig.FLAVOR : zzqo()) + "\t" + zzqp();
    }

    public String zzqj() {
        return this.zzaia;
    }

    public String zzqk() {
        return this.zzaif;
    }

    public int zzql() {
        return this.zzaib;
    }

    public List<String> zzqm() {
        return this.zzaic;
    }

    public int zzqn() {
        return this.zzaie;
    }

    public String zzqo() {
        return this.zzaig;
    }

    public float zzqp() {
        return this.zzaih;
    }

    public long zzqq() {
        return this.mTimeout;
    }
}
