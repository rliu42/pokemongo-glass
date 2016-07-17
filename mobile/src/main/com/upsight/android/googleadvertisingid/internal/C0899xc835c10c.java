package com.upsight.android.googleadvertisingid.internal;

import dagger.internal.Factory;

/* renamed from: com.upsight.android.googleadvertisingid.internal.GoogleAdvertisingProviderModule_ProvideGooglePlayAdvertisingProviderFactory */
public final class C0899xc835c10c implements Factory<GooglePlayAdvertisingProvider> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final GoogleAdvertisingProviderModule module;

    static {
        $assertionsDisabled = !C0899xc835c10c.class.desiredAssertionStatus();
    }

    public C0899xc835c10c(GoogleAdvertisingProviderModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public GooglePlayAdvertisingProvider get() {
        GooglePlayAdvertisingProvider provided = this.module.provideGooglePlayAdvertisingProvider();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<GooglePlayAdvertisingProvider> create(GoogleAdvertisingProviderModule module) {
        return new C0899xc835c10c(module);
    }
}
