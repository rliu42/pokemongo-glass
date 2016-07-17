package com.upsight.android.analytics.internal;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.internal.util.Opt;
import dagger.Module;
import dagger.Provides;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public final class BaseAnalyticsModule {
    public static final String OPT_UNCAUGHT_EXCEPTION_HANDLER = "optUncaughtExceptionHandler";
    private final UpsightContext mUpsight;

    /* renamed from: com.upsight.android.analytics.internal.BaseAnalyticsModule.1 */
    class C08681 implements Clock {
        C08681() {
        }

        public long currentTimeSeconds() {
            return TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    }

    public BaseAnalyticsModule(UpsightContext upsight) {
        this.mUpsight = upsight;
    }

    @Singleton
    @Provides
    public UpsightContext provideUpsightContext() {
        return this.mUpsight;
    }

    @Singleton
    @Provides
    public Clock provideClock() {
        return new C08681();
    }

    @Named("optUncaughtExceptionHandler")
    @Singleton
    @Provides
    public Opt<UncaughtExceptionHandler> provideUncaughtExceptionHandler() {
        return Opt.absent();
    }

    @Singleton
    @Provides
    public UpsightGooglePlayHelper provideGooglePlayHelper(UpsightContext upsight) {
        return new GooglePlayHelper(upsight, upsight.getCoreComponent().objectMapper());
    }
}
