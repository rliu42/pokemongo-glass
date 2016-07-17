package crittercism.android;

import com.google.android.gms.common.GooglePlayServicesUtil;
import crittercism.android.C1064do.C1061a.C1060a;
import crittercism.android.C1064do.C1063b.C1062a;
import java.util.HashMap;
import java.util.Map;

public final class dp {
    private static Map f749a;

    static {
        Map hashMap = new HashMap();
        f749a = hashMap;
        hashMap.put("com.amazon.venezia", new C1060a());
        f749a.put(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, new C1062a());
    }

    public static dn m725a(String str) {
        if (str == null || !f749a.containsKey(str)) {
            return null;
        }
        return (dn) f749a.get(str);
    }
}
