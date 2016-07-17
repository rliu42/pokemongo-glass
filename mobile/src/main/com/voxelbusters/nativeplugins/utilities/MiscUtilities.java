package com.voxelbusters.nativeplugins.utilities;

import android.content.pm.ResolveInfo;
import com.voxelbusters.nativeplugins.features.sharing.SharingHelper;
import java.util.Comparator;

public class MiscUtilities {
    public static Comparator<ResolveInfo> resolveInfoComparator;

    /* renamed from: com.voxelbusters.nativeplugins.utilities.MiscUtilities.1 */
    class C09921 implements Comparator<ResolveInfo> {
        C09921() {
        }

        public int compare(ResolveInfo info1, ResolveInfo info2) {
            boolean info1Required = SharingHelper.isSocialNetwork(info1.activityInfo.packageName);
            boolean info2Required = SharingHelper.isSocialNetwork(info2.activityInfo.packageName);
            if ((info1Required && info2Required) || info1Required) {
                return -1;
            }
            if (info2Required) {
                return 1;
            }
            return 0;
        }
    }

    static {
        resolveInfoComparator = new C09921();
    }
}
