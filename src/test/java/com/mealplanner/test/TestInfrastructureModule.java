package com.mealplanner.test;

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
        return "meals";
    }

    @Singleton
    @Provides
    @Named("awsRegion")
    String awsRegion() {
        return "eu-west-1";
    }

    @Singleton
    @Provides
    public AmazonDynamoDB amazonDynamoDb(@Named("awsRegion") final String awsRegion) {
        final String endpoint = "http://localhost:4569";
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new EndpointConfiguration(endpoint, awsRegion))
                .build();
    }

}
