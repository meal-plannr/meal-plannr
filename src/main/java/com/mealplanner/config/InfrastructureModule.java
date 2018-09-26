package com.mealplanner.config;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class InfrastructureModule {

    @Singleton
    @Provides
    @Named("environment")
    Environment environment() {
        final String environmentEnvVar = System.getenv("MEAL_PLANNR_ENVIRONMENT");
        return environmentEnvVar != null ? Environment.getEnvironmentForCode(environmentEnvVar) : Environment.LOCAL;
    }
}
