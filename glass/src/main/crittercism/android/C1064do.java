package crittercism.android;

/* renamed from: crittercism.android.do */
public final class C1064do {

    /* renamed from: crittercism.android.do.a */
    public static class C1061a extends dm {
        private String f747a;

        /* renamed from: crittercism.android.do.a.a */
        public static class C1060a implements dn {
            public final /* synthetic */ dm m721a(String str) {
                if (str != null) {
                    return new C1061a((byte) 0);
                }
                throw new NullPointerException("packageName cannot be null");
            }
        }

        private C1061a(String str) {
            this.f747a = str;
        }

        public final String m722a() {
            return "http://www.amazon.com/gp/mas/dl/android?p=" + this.f747a;
        }
    }

    /* renamed from: crittercism.android.do.b */
    public static class C1063b extends dm {
        private String f748a;

        /* renamed from: crittercism.android.do.b.a */
        public static class C1062a implements dn {
            public final /* synthetic */ dm m723a(String str) {
                if (str != null) {
                    return new C1063b((byte) 0);
                }
                throw new NullPointerException("packageName cannot be null");
            }
        }

        private C1063b(String str) {
            this.f748a = str;
        }

        public final String m724a() {
            return "market://details?id=" + this.f748a;
        }
    }
}
