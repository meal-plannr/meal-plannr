package com.mealplanner.config;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dagger.Module;
import dagger.Provides;

@Module
public class InfrastructureModule {

    private static final Logger LOGGER = LogManager.getLogger(InfrastructureModule.class);

    @Singleton
    @Provides
    @Named("environment")
    Environment environment() {
        LOGGER.debug("Determining environment");
        final String environmentEnvVar = System.getenv("MEAL_PLANNR_ENVIRONMENT");
        final Environment environment = environmentEnvVar != null ? Environment.getEnvironmentForCode(environmentEnvVar) : Environment.LOCAL;

        LOGGER.debug("Finished determining environment. Resolved to [{}]", environment);
        return environment;
    }
}
