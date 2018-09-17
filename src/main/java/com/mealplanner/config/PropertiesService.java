package com.mealplanner.config;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PropertiesService {

    public static final String AWS_REGION = "aws.region";
    public static final String DYNAMO_MEALS_TABLE_NAME = "dynamo.mealsTableName";
    public static final String DYNAMO_ENDPOINT = "dynamo.endpoint";
    public static final String DYNAMO_CREATE_TABLES = "dynamo.createTables";

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesService.class);

    private final Properties props = new Properties();

    @Inject
    public PropertiesService(@Named("runningTestsOnCi") final boolean runningTestsOnCi, @Named("runningInProduction") final boolean runningInProduction) {
        if (runningInProduction) {
            props.setProperty(AWS_REGION, System.getenv("region"));
            props.setProperty(DYNAMO_MEALS_TABLE_NAME, System.getenv("tableName"));
        } else {
            final String fileName = runningTestsOnCi ? "ci.properties" : "local.properties";
            try {
                props.load(getClass().getClassLoader().getResourceAsStream(fileName));
            } catch (final IOException e) {
                final String error = String.format("Error loading properties file [%s]", fileName);
                LOGGER.error(error, e);
                throw new IllegalStateException(error, e);
            }
        }
    }

    public String getAwsRegion() {
        return props.getProperty(AWS_REGION);
    }

    public String getMealsTableName() {
        return props.getProperty(DYNAMO_MEALS_TABLE_NAME);
    }

    public String getDynamoEndpoint() {
        return props.getProperty(DYNAMO_ENDPOINT);
    }

    public boolean needToCreateDynamoTables() {
        return Boolean.valueOf(props.getProperty(DYNAMO_CREATE_TABLES));
    }
}
