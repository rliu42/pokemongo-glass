package crittercism.android;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class bj extends ci {
    private String f473a;
    private String f474b;
    private C1018a f475c;
    private String f476d;

    /* renamed from: crittercism.android.bj.a */
    public enum C1018a {
        ACTIVATED,
        DEACTIVATED
    }

    public bj(C1018a c1018a, String str) {
        this.f473a = cg.f624a.m663a();
        this.f474b = ed.f792a.m771a();
        this.f475c = c1018a;
        this.f476d = str;
    }

    public final String m514e() {
        return this.f473a;
    }

    public final JSONArray m513a() {
        Map hashMap = new HashMap();
        hashMap.put(SendEvent.EVENT, Integer.valueOf(this.f475c.ordinal()));
        hashMap.put("viewName", this.f476d);
        return new JSONArray().put(this.f474b).put(5).put(new JSONObject(hashMap));
    }
}
