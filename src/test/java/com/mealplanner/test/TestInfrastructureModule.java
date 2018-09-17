package com.mealplanner.test;

import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

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
    String tableName() {
        return Optional.ofNullable(System.getProperty("tableName")).orElse("meals");
    }

    @Singleton
    @Provides
    @Named("awsRegion")
    String awsRegion() {
        return Optional.ofNullable(System.getProperty("region")).orElse("eu-west-1");
    }

    @Singleton
    @Provides
    @Named("runningTestsOnCi")
    boolean runningTestsOnCi() {
        return Optional.ofNullable(Boolean.valueOf(System.getProperty("runningTestsOnCi"))).orElse(false);
    }

    @Singleton
    @Provides
    public AmazonDynamoDB amazonDynamoDb(@Named("awsRegion") final String awsRegion, @Named("runningTestsOnCi") final boolean runningTestsOnCi) {
        final AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
        if (runningTestsOnCi) {
            builder.withRegion(awsRegion);

        } else {
            final String endpoint = "http://localhost:4569";
            builder.withEndpointConfiguration(new EndpointConfiguration(endpoint, awsRegion));
        }

        return builder.build();
    }

}
