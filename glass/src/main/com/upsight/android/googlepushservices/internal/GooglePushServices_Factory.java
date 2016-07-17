package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class GooglePushServices_Factory implements Factory<GooglePushServices> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !GooglePushServices_Factory.class.desiredAssertionStatus();
    }

    public GooglePushServices_Factory(Provider<UpsightContext> upsightProvider) {
        if ($assertionsDisabled || upsightProvider != null) {
            this.upsightProvider = upsightProvider;
            return;
        }
        throw new AssertionError();
    }

    public GooglePushServices get() {
        return new GooglePushServices((UpsightContext) this.upsightProvider.get());
    }

    public static Factory<GooglePushServices> create(Provider<UpsightContext> upsightProvider) {
        return new GooglePushServices_Factory(upsightProvider);
    }
}
