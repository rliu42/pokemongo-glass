package com.crittercism.app;

import android.os.Build.VERSION;
import crittercism.android.dx;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONObject;

public class CrittercismConfig {
    public static final String API_VERSION = "5.0.8";
    protected String f14a;
    private String f15b;
    private boolean f16c;
    private boolean f17d;
    private boolean f18e;
    private boolean f19f;
    private boolean f20g;
    private String f21h;
    private String f22i;
    private List f23j;
    private List f24k;

    public CrittercismConfig() {
        this.f15b = null;
        this.f16c = false;
        this.f17d = false;
        this.f18e = true;
        this.f19f = false;
        this.f20g = m28b();
        this.f14a = "com.crittercism/dumps";
        this.f21h = "Developer Reply";
        this.f22i = null;
        this.f23j = new LinkedList();
        this.f24k = new LinkedList();
    }

    @Deprecated
    public CrittercismConfig(JSONObject config) {
        this.f15b = null;
        this.f16c = false;
        this.f17d = false;
        this.f18e = true;
        this.f19f = false;
        this.f20g = m28b();
        this.f14a = "com.crittercism/dumps";
        this.f21h = "Developer Reply";
        this.f22i = null;
        this.f23j = new LinkedList();
        this.f24k = new LinkedList();
        this.f15b = m25a(config, "customVersionName", this.f15b);
        this.f17d = m27a(config, "includeVersionCode", this.f17d);
        this.f18e = m27a(config, "installNdk", this.f18e);
        this.f16c = m27a(config, "delaySendingAppLoad", this.f16c);
        this.f19f = m27a(config, "shouldCollectLogcat", this.f19f);
        this.f14a = m25a(config, "nativeDumpPath", this.f14a);
        this.f21h = m25a(config, "notificationTitle", this.f21h);
        this.f20g = m27a(config, "installApm", this.f20g);
    }

    public CrittercismConfig(CrittercismConfig toCopy) {
        this.f15b = null;
        this.f16c = false;
        this.f17d = false;
        this.f18e = true;
        this.f19f = false;
        this.f20g = m28b();
        this.f14a = "com.crittercism/dumps";
        this.f21h = "Developer Reply";
        this.f22i = null;
        this.f23j = new LinkedList();
        this.f24k = new LinkedList();
        this.f15b = toCopy.f15b;
        this.f16c = toCopy.f16c;
        this.f17d = toCopy.f17d;
        this.f18e = toCopy.f18e;
        this.f19f = toCopy.f19f;
        this.f20g = toCopy.f20g;
        this.f14a = toCopy.f14a;
        this.f21h = toCopy.f21h;
        setURLBlacklistPatterns(toCopy.f23j);
        setPreserveQueryStringPatterns(toCopy.f24k);
        this.f22i = toCopy.f22i;
    }

    public List getURLBlacklistPatterns() {
        return new LinkedList(this.f23j);
    }

    public void setURLBlacklistPatterns(List patterns) {
        this.f23j.clear();
        if (patterns != null) {
            this.f23j.addAll(patterns);
        }
    }

    public void setPreserveQueryStringPatterns(List patterns) {
        this.f24k.clear();
        if (patterns != null) {
            this.f24k.addAll(patterns);
        }
    }

    public List getPreserveQueryStringPatterns() {
        return new LinkedList(this.f24k);
    }

    public boolean equals(Object o) {
        if (!(o instanceof CrittercismConfig)) {
            return false;
        }
        CrittercismConfig crittercismConfig = (CrittercismConfig) o;
        if (this.f16c == crittercismConfig.f16c && this.f19f == crittercismConfig.f19f && isNdkCrashReportingEnabled() == crittercismConfig.isNdkCrashReportingEnabled() && isOptmzEnabled() == crittercismConfig.isOptmzEnabled() && isVersionCodeToBeIncludedInVersionString() == crittercismConfig.isVersionCodeToBeIncludedInVersionString() && m26a(this.f15b, crittercismConfig.f15b) && m26a(this.f21h, crittercismConfig.f21h) && m26a(this.f14a, crittercismConfig.f14a) && this.f23j.equals(crittercismConfig.f23j) && this.f24k.equals(crittercismConfig.f24k) && m26a(this.f22i, crittercismConfig.f22i)) {
            return true;
        }
        return false;
    }

    protected static boolean m26a(String str, String str2) {
        if (str == null) {
            return str2 == null;
        } else {
            return str.equals(str2);
        }
    }

    public int hashCode() {
        int i;
        int i2 = 1;
        int hashCode = this.f24k.hashCode() + ((((((((((m24a(this.f15b) + 0) * 31) + m24a(this.f21h)) * 31) + m24a(this.f14a)) * 31) + m24a(this.f22i)) * 31) + this.f23j.hashCode()) * 31);
        int i3 = ((this.f16c ? 1 : 0) + 0) << 1;
        if (this.f19f) {
            i = 1;
        } else {
            i = 0;
        }
        i3 = (i + i3) << 1;
        if (isNdkCrashReportingEnabled()) {
            i = 1;
        } else {
            i = 0;
        }
        i3 = (i + i3) << 1;
        if (isOptmzEnabled()) {
            i = 1;
        } else {
            i = 0;
        }
        i = (i + i3) << 1;
        if (!isVersionCodeToBeIncludedInVersionString()) {
            i2 = 0;
        }
        return Integer.valueOf(i + i2).hashCode() + (hashCode * 31);
    }

    private static int m24a(String str) {
        if (str != null) {
            return str.hashCode();
        }
        return 0;
    }

    private static String m25a(JSONObject jSONObject, String str, String str2) {
        if (jSONObject.has(str)) {
            try {
                str2 = jSONObject.getString(str);
            } catch (Exception e) {
            }
        }
        return str2;
    }

    private static boolean m27a(JSONObject jSONObject, String str, boolean z) {
        if (jSONObject.has(str)) {
            try {
                z = jSONObject.getBoolean(str);
            } catch (Exception e) {
            }
        }
        return z;
    }

    public final String getCustomVersionName() {
        return this.f15b;
    }

    public final void setCustomVersionName(String customVersionName) {
        this.f15b = customVersionName;
    }

    public final boolean delaySendingAppLoad() {
        return this.f16c;
    }

    public final void setDelaySendingAppLoad(boolean delaySendingAppLoad) {
        this.f16c = delaySendingAppLoad;
    }

    public final boolean isVersionCodeToBeIncludedInVersionString() {
        return this.f17d;
    }

    public final void setVersionCodeToBeIncludedInVersionString(boolean shouldIncludeVersionCode) {
        this.f17d = shouldIncludeVersionCode;
    }

    public final boolean isNdkCrashReportingEnabled() {
        return this.f18e;
    }

    public final void setNdkCrashReportingEnabled(boolean installNdk) {
        this.f18e = installNdk;
    }

    public final boolean isLogcatReportingEnabled() {
        return this.f19f;
    }

    public final void setLogcatReportingEnabled(boolean shouldCollectLogcat) {
        this.f19f = shouldCollectLogcat;
    }

    private static final boolean m28b() {
        return VERSION.SDK_INT >= 10 && VERSION.SDK_INT <= 21;
    }

    public final boolean isServiceMonitoringEnabled() {
        return isOptmzEnabled();
    }

    @Deprecated
    public final boolean isOptmzEnabled() {
        return this.f20g;
    }

    public final void setServiceMonitoringEnabled(boolean isServiceMonitoringEnabled) {
        setOptmzEnabled(isServiceMonitoringEnabled);
    }

    @Deprecated
    public final void setOptmzEnabled(boolean isOptmzEnabled) {
        if (m28b() || !isOptmzEnabled) {
            this.f20g = isOptmzEnabled;
        } else {
            dx.m750a("OPTMZ is currently only allowed for api levels 10 to 21.  APM will not be installed");
        }
    }

    public List m29a() {
        return getURLBlacklistPatterns();
    }

    public final void setRateMyAppTestTarget(String rateMyAppTestTarget) {
        this.f22i = rateMyAppTestTarget;
    }

    public final String getRateMyAppTestTarget() {
        return this.f22i;
    }
}
