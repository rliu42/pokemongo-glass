package crittercism.android;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import crittercism.android.bx.C1023b;
import crittercism.android.bx.C1024c;
import crittercism.android.bx.C1025d;
import crittercism.android.bx.C1027f;
import crittercism.android.bx.C1029h;
import crittercism.android.bx.C1031j;
import crittercism.android.bx.C1032k;
import crittercism.android.bx.C1034m;
import crittercism.android.bx.C1036o;
import crittercism.android.bx.C1037p;
import crittercism.android.bx.C1039r;
import crittercism.android.bx.C1040s;
import crittercism.android.bx.C1044w;
import crittercism.android.bx.C1045x;
import crittercism.android.bx.C1047z;
import crittercism.android.bx.aa;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class cd implements ch {
    private JSONObject f598a;
    private JSONObject f599b;
    private JSONArray f600c;
    private JSONArray f601d;
    private File f602e;
    private String f603f;

    public cd(File file, bs bsVar, bs bsVar2, bs bsVar3, bs bsVar4) {
        file.exists();
        this.f603f = cg.f624a.m663a();
        this.f602e = file;
        this.f598a = new bu().m557a(new C1024c()).m557a(new C1023b()).m557a(new C1025d()).m557a(new C1027f()).m557a(new C1036o()).m557a(new C1037p()).m557a(new C1031j()).m557a(new C1029h()).m557a(new C1047z()).m557a(new aa()).m557a(new C1032k()).m557a(new C1039r()).m557a(new C1034m()).m557a(new C1040s()).m557a(new C1044w()).m557a(new C1045x()).m558a();
        Map hashMap = new HashMap();
        hashMap.put("crashed_session", new bo(bsVar).f507a);
        if (bsVar2.m553b() > 0) {
            hashMap.put("previous_session", new bo(bsVar2).f507a);
        }
        this.f599b = new JSONObject(hashMap);
        this.f600c = new bo(bsVar3).f507a;
        this.f601d = new bo(bsVar4).f507a;
    }

    public final void m656a(OutputStream outputStream) {
        Map hashMap = new HashMap();
        hashMap.put("app_state", this.f598a);
        hashMap.put("breadcrumbs", this.f599b);
        hashMap.put("endpoints", this.f600c);
        hashMap.put("systemBreadcrumbs", this.f601d);
        Object obj = new byte[0];
        Object obj2 = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD];
        InputStream fileInputStream = new FileInputStream(this.f602e);
        while (true) {
            int read = fileInputStream.read(obj2);
            if (read != -1) {
                Object obj3 = new byte[(obj.length + read)];
                System.arraycopy(obj, 0, obj3, 0, obj.length);
                System.arraycopy(obj2, 0, obj3, obj.length, read);
                obj = obj3;
            } else {
                fileInputStream.close();
                Map hashMap2 = new HashMap();
                hashMap2.put("dmp_name", this.f602e.getName());
                hashMap2.put("dmp_file", cr.m670a(obj));
                hashMap.put("ndk_dmp_info", new JSONObject(hashMap2));
                outputStream.write(new JSONObject(hashMap).toString().getBytes());
                return;
            }
        }
    }

    public final String m657e() {
        return this.f603f;
    }
}
