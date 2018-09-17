package com.mealplanner.test;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ConfigProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProperties.class);

    private final Properties props = new Properties();

    @Inject
    public ConfigProperties(@Named("runningTestsOnCi") final boolean runningTestsOnCi) {
        final String fileName = runningTestsOnCi ? "ci.properties" : "local.properties";

        try {
            props.load(getClass().getClassLoader().getResourceAsStream(fileName));
        } catch (final IOException e) {
            final String error = String.format("Error loading properties file [%s]", fileName);
            LOGGER.error(error, e);
        }
    }

    public String getAwsRegion() {
        return props.getProperty("aws.region");
    }

    public String getMealsTableName() {
        return props.getProperty("dynamo.mealsTableName");
    }

    public String getDynamoEndpoint() {
        return props.getProperty("dynamo.endpoint");
    }

    public boolean needToCreateDynamoTables() {
        return Boolean.valueOf(props.getProperty("dynamo.createTables"));
    }
}
