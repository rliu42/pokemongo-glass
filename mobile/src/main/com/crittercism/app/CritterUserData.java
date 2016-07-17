package com.crittercism.app;

import com.voxelbusters.nativeplugins.defines.Keys;
import crittercism.android.dx;
import java.util.Map;

public class CritterUserData {
    private Map f7a;
    private final boolean f8b;

    CritterUserData(Map map, boolean isOptedOut) {
        this.f7a = map;
        this.f8b = isOptedOut;
    }

    public boolean shouldShowRateMyAppAlert() {
        if (this.f7a.containsKey("shouldShowRateAppAlert")) {
            return ((Boolean) this.f7a.get("shouldShowRateAppAlert")).booleanValue();
        }
        if (this.f8b) {
            dx.m757c("User has opted out of Crittercism.  Returning false.");
        } else {
            dx.m757c("CritterUserData instance has no value for shouldShowMyRateAppAlert().  Defaulting to false.");
        }
        return false;
    }

    public String getRateMyAppTitle() {
        if (!this.f7a.containsKey(Keys.TITLE)) {
            if (this.f8b) {
                dx.m757c("User has opted out of Crittercism.  Returning null.");
            } else {
                dx.m757c("CritterUserData instance has no value for getRateMyAppTitle().  Returning null.");
            }
        }
        return (String) this.f7a.get(Keys.TITLE);
    }

    public String getRateMyAppMessage() {
        if (!this.f7a.containsKey(Keys.MESSAGE)) {
            if (this.f8b) {
                dx.m757c("User has opted out of Crittercism.  Returning null.");
            } else {
                dx.m757c("CritterUserData instance has no value for getRateMyAppMessage().  Returning null.");
            }
        }
        return (String) this.f7a.get(Keys.MESSAGE);
    }

    public boolean isOptedOut() {
        if (this.f7a.containsKey("optOutStatus")) {
            return ((Boolean) this.f7a.get("optOutStatus")).booleanValue();
        }
        return this.f8b;
    }

    public boolean crashedOnLastLoad() {
        if (this.f7a.containsKey("crashedOnLastLoad")) {
            return ((Boolean) this.f7a.get("crashedOnLastLoad")).booleanValue();
        }
        if (this.f8b) {
            dx.m757c("User has opted out of Crittercism.  Returning false.");
        } else {
            dx.m757c("CritterUserData instance has no value for crashedOnLastLoad().  Defaulting to false.");
        }
        return false;
    }

    public String getUserUUID() {
        if (!this.f7a.containsKey("userUUID")) {
            if (this.f8b) {
                dx.m757c("User has opted out of Crittercism.  Returning null.");
            } else {
                dx.m757c("CritterUserData instance has no value for getUserUUID().  Returning null.");
            }
        }
        return (String) this.f7a.get("userUUID");
    }
}
