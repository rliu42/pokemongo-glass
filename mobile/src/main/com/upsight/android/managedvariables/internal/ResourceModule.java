package com.upsight.android.managedvariables.internal;

import com.upsight.android.managedvariables.C0926R;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class ResourceModule {
    public static final String RES_UXM_SCHEMA = "resUxmSchema";

    @Singleton
    @Provides
    @Named("resUxmSchema")
    Integer provideUxmSchemaResource() {
        return Integer.valueOf(C0926R.raw.uxm_schema);
    }
}
