package crittercism.android;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: crittercism.android.h */
public final class C1072h {
    public boolean f805a;
    public boolean f806b;
    public boolean f807c;
    public int f808d;

    public C1072h(Context context) {
        this.f805a = false;
        this.f806b = false;
        this.f807c = false;
        this.f808d = 10;
        if (C1072h.m780a(context).exists()) {
            this.f807c = true;
        }
    }

    public C1072h(JSONObject jSONObject) {
        this.f805a = false;
        this.f806b = false;
        this.f807c = false;
        this.f808d = 10;
        if (jSONObject.has("net")) {
            try {
                JSONObject jSONObject2 = jSONObject.getJSONObject("net");
                this.f805a = jSONObject2.optBoolean("enabled", false);
                this.f806b = jSONObject2.optBoolean("persist", false);
                this.f807c = jSONObject2.optBoolean("kill", false);
                this.f808d = jSONObject2.optInt("interval", 10);
            } catch (JSONException e) {
            }
        }
    }

    public static File m780a(Context context) {
        return new File(context.getFilesDir().getAbsolutePath() + "/.crittercism.apm.disabled.");
    }

    public static void m781b(Context context) {
        try {
            C1072h.m780a(context).createNewFile();
        } catch (IOException e) {
            dx.m754b("Unable to kill APM: " + e.getMessage());
        }
    }

    public final int hashCode() {
        int i;
        int i2 = 1231;
        int i3 = ((this.f807c ? 1231 : 1237) + 31) * 31;
        if (this.f805a) {
            i = 1231;
        } else {
            i = 1237;
        }
        i = (i + i3) * 31;
        if (!this.f806b) {
            i2 = 1237;
        }
        return ((i + i2) * 31) + this.f808d;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof C1072h)) {
            return false;
        }
        C1072h c1072h = (C1072h) obj;
        if (this.f807c != c1072h.f807c) {
            return false;
        }
        if (this.f805a != c1072h.f805a) {
            return false;
        }
        if (this.f806b != c1072h.f806b) {
            return false;
        }
        if (this.f808d != c1072h.f808d) {
            return false;
        }
        return true;
    }

    public final String toString() {
        return "OptmzConfiguration [\nisSendTaskEnabled=" + this.f805a + "\n, shouldPersist=" + this.f806b + "\n, isKilled=" + this.f807c + "\n, statisticsSendInterval=" + this.f808d + "]";
    }
}
