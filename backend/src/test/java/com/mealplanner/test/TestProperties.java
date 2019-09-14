package com.mealplanner.test;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
public class TestProperties {

    private static final String PROPERTIES_FILENAME = "local.properties";

    private static final String AWS_REGION = "aws.region";
    private static final String DYNAMO_ENDPOINT = "dynamo.endpoint";

    private static final Logger LOGGER = LogManager.getLogger(TestProperties.class);

    private final Properties props = new Properties();

    @Inject
    public TestProperties() {
        try {
            props.load(getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME));
        } catch (final IOException e) {
            final String error = String.format("Error loading properties file [%s]", PROPERTIES_FILENAME);
            LOGGER.error(error, e);
            throw new IllegalStateException(error, e);
        }
    }

    public String getAwsRegion() {
        return props.getProperty(AWS_REGION);
    }

    public String getDynamoEndpoint() {
        return props.getProperty(DYNAMO_ENDPOINT);
    }
}