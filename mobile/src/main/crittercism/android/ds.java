package crittercism.android;

import org.json.JSONException;
import org.json.JSONObject;

public final class ds {
    private boolean f754a;
    private boolean f755b;

    /* renamed from: crittercism.android.ds.a */
    public static class C1065a {
        public static ds m731a(ax axVar) {
            JSONObject jSONObject;
            boolean optBoolean;
            JSONObject jSONObject2 = null;
            String a = axVar.m360a(cq.OPT_OUT_STATUS_SETTING.m668a(), cq.OPT_OUT_STATUS_SETTING.m669b());
            if (a != null) {
                try {
                    jSONObject = new JSONObject(a);
                } catch (JSONException e) {
                    dx.m753b();
                }
            } else {
                jSONObject = null;
            }
            jSONObject2 = jSONObject;
            if (jSONObject2 != null) {
                optBoolean = jSONObject2.optBoolean("optOutStatusSet", false);
            } else {
                optBoolean = false;
            }
            if (optBoolean) {
                optBoolean = jSONObject2.optBoolean("optOutStatus", false);
            } else {
                optBoolean = axVar.m364c(cq.OLD_OPT_OUT_STATUS_SETTING.m668a(), cq.OLD_OPT_OUT_STATUS_SETTING.m669b());
            }
            return new ds(optBoolean);
        }
    }

    public ds(boolean z) {
        this.f754a = z;
        this.f755b = true;
    }

    public final synchronized boolean m732a() {
        return this.f754a;
    }
}
