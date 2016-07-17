package com.upsight.android.analytics.internal;

import com.upsight.android.analytics.internal.session.Clock;
import dagger.internal.Factory;

public final class BaseAnalyticsModule_ProvideClockFactory implements Factory<Clock> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final BaseAnalyticsModule module;

    static {
        $assertionsDisabled = !BaseAnalyticsModule_ProvideClockFactory.class.desiredAssertionStatus();
    }

    public BaseAnalyticsModule_ProvideClockFactory(BaseAnalyticsModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Clock get() {
        Clock provided = this.module.provideClock();
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Clock> create(BaseAnalyticsModule module) {
        return new BaseAnalyticsModule_ProvideClockFactory(module);
    }
}
