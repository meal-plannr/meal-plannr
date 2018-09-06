package com.mealplanner.config;

import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import dagger.Module;
import dagger.Provides;

@Module
public class InfrastructureModule {

    @Singleton
    @Provides
    @Named("mealsTableName")
    String tableName() {
        return System.getenv("tableName");
    }

    @Singleton
    @Provides
    @Named("awsRegion")
    String awsRegion() {
        return System.getenv("region");
    }

    @Singleton
    @Provides
    public AmazonDynamoDB amazonDynamoDb(@Named("awsRegion") final String awsRegion) {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(awsRegion)
                .build();
    }
}
