package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class ce extends ci {
    private String f610a;
    private String f611b;
    private C1052a f612c;
    private String f613d;
    private String f614e;
    private String f615f;

    /* renamed from: crittercism.android.ce.a */
    public enum C1052a {
        INTERNET_UP,
        INTERNET_DOWN,
        CONN_TYPE_GAINED,
        CONN_TYPE_LOST,
        CONN_TYPE_SWITCHED
    }

    public ce(C1052a c1052a) {
        if (c1052a != C1052a.INTERNET_UP) {
            C1052a c1052a2 = C1052a.INTERNET_DOWN;
        }
        this.f610a = cg.f624a.m663a();
        this.f611b = ed.f792a.m771a();
        this.f612c = c1052a;
    }

    public ce(C1052a c1052a, String str) {
        if (c1052a != C1052a.CONN_TYPE_GAINED) {
            C1052a c1052a2 = C1052a.CONN_TYPE_LOST;
        }
        this.f610a = cg.f624a.m663a();
        this.f611b = ed.f792a.m771a();
        this.f612c = c1052a;
        this.f613d = str;
    }

    public ce(C1052a c1052a, String str, String str2) {
        C1052a c1052a2 = C1052a.CONN_TYPE_SWITCHED;
        this.f610a = cg.f624a.m663a();
        this.f611b = ed.f792a.m771a();
        this.f612c = c1052a;
        this.f614e = str;
        this.f615f = str2;
    }

    public final String m659e() {
        return this.f610a;
    }

    public final JSONArray m658a() {
        Map hashMap = new HashMap();
        hashMap.put("change", Integer.valueOf(this.f612c.ordinal()));
        if (this.f612c == C1052a.CONN_TYPE_GAINED || this.f612c == C1052a.CONN_TYPE_LOST) {
            hashMap.put(Keys.TYPE, this.f613d);
        } else if (this.f612c == C1052a.CONN_TYPE_SWITCHED) {
            hashMap.put("oldType", this.f614e);
            hashMap.put("newType", this.f615f);
        }
        return new JSONArray().put(this.f611b).put(4).put(new JSONObject(hashMap));
    }
}
