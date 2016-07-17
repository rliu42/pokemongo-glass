package crittercism.android;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/* renamed from: crittercism.android.p */
public final class C1084p extends C1081n {

    /* renamed from: crittercism.android.p.1 */
    class C10831 implements Comparator {
        final /* synthetic */ C1084p f847a;

        C10831(C1084p c1084p) {
            this.f847a = c1084p;
        }

        public final /* bridge */ /* synthetic */ int compare(Object x0, Object x1) {
            String str = (String) x0;
            String str2 = (String) x1;
            if (str == str2) {
                return 0;
            }
            if (str == null) {
                return -1;
            }
            return str2 == null ? 1 : String.CASE_INSENSITIVE_ORDER.compare(str, str2);
        }
    }

    public C1084p(Map map) {
        super(map);
        Map treeMap = new TreeMap(new C10831(this));
        treeMap.putAll(map);
        this.f845a = treeMap;
    }
}
