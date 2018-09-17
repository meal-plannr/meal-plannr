package com.mealplanner.config;

import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class InfrastructureModule {

    @Singleton
    @Provides
    @Named("runningTestsOnCi")
    boolean runningTestsOnCi() {
        return Optional.ofNullable(Boolean.valueOf(System.getProperty("runningTestsOnCi"))).orElse(false);
    }

    @Singleton
    @Provides
    @Named("runningInProduction")
    boolean runningInProduction() {
        return Optional.ofNullable(Boolean.valueOf(System.getenv("production"))).orElse(false);
    }
}
