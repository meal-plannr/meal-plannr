package com.mealplanner.config;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class PropertiesFileSelectorService {

    public static final String CI_PROPERTIES_FILENAME = "ci.properties";
    public static final String LOCAL_PROPERTIES_FILENAME = "local.properties";
    public static final String ERROR_LOADING_PROPERTIES_FILE_WHEN_RUNNING_IN_PROD = "Trying to get the properties file when running in Production mode";

    private final boolean runningInProduction;
    private final boolean runningOnCi;

    @Inject
    public PropertiesFileSelectorService(@Named("runningTestsOnCi") final boolean runningTestsOnCi,
            @Named("runningInProduction") final boolean runningInProduction) {
        this.runningInProduction = runningInProduction;
        this.runningOnCi = runningTestsOnCi;
    }

    public boolean isRunningInProduction() {
        return runningInProduction;
    }

    public String getPropertiesFile() {
        if (runningInProduction) {
            throw new IllegalStateException(ERROR_LOADING_PROPERTIES_FILE_WHEN_RUNNING_IN_PROD);
        }

        if (runningOnCi) {
            return CI_PROPERTIES_FILENAME;
        }
        return LOCAL_PROPERTIES_FILENAME;
    }

}
