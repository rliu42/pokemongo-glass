package com.upsight.android;

import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnRegisterListener;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesComponent;
import com.upsight.android.googlepushservices.internal.DaggerGooglePushServicesComponent;
import com.upsight.android.googlepushservices.internal.PushModule;
import javax.inject.Inject;

public class UpsightGooglePushServicesExtension extends UpsightExtension<UpsightGooglePushServicesComponent, UpsightGooglePushServicesApi> {
    public static final String EXTENSION_NAME = "com.upsight.extension.googlepushservices";
    @Inject
    UpsightGooglePushServicesApi mUpsightPush;

    /* renamed from: com.upsight.android.UpsightGooglePushServicesExtension.1 */
    class C08621 implements OnRegisterListener {
        C08621() {
        }

        public void onSuccess(String registrationId) {
        }

        public void onFailure(UpsightException e) {
        }
    }

    UpsightGooglePushServicesExtension() {
    }

    protected UpsightGooglePushServicesComponent onResolve(UpsightContext upsight) {
        return DaggerGooglePushServicesComponent.builder().pushModule(new PushModule(upsight)).build();
    }

    public UpsightGooglePushServicesApi getApi() {
        return this.mUpsightPush;
    }

    protected void onPostCreate(UpsightContext upsight) {
        this.mUpsightPush.register(new C08621());
    }
}
