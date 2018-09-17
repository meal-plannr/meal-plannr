package com.mealplanner.test;

import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import dagger.Module;
import dagger.Provides;

@Module
public class TestInfrastructureModule {

    @Singleton
    @Provides
    @Named("mealsTableName")
    String tableName(final ConfigProperties properties) {
        return properties.getMealsTableName();
    }

    @Singleton
    @Provides
    @Named("runningTestsOnCi")
    boolean runningTestsOnCi() {
        return Optional.ofNullable(Boolean.valueOf(System.getProperty("runningTestsOnCi"))).orElse(false);
    }

    @Singleton
    @Provides
    public AmazonDynamoDB amazonDynamoDb(final ConfigProperties properties) {
        final AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();

        final String awsRegion = properties.getAwsRegion();
        final String endpoint = properties.getDynamoEndpoint();
        if (StringUtils.isNotBlank(endpoint)) {
            builder.withEndpointConfiguration(new EndpointConfiguration(endpoint, awsRegion));
        } else {
            builder.withRegion(awsRegion);

        }

        return builder.build();
    }

}
