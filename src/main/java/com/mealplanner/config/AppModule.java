package com.mealplanner.config;

import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.mealplanner.dal.MealRepositoryDynamo;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Singleton
    @Provides
    public AmazonDynamoDB amazonDynamoDb(@Named("awsRegion") final String awsRegion) {
        final AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();

        final String endpoint = System.getenv("ENDPOINT_OVERRIDE");
        if ((endpoint != null) && !endpoint.isEmpty()) {
            builder.withEndpointConfiguration(new EndpointConfiguration(endpoint, awsRegion));
        } else {
            builder.withRegion(awsRegion);
        }

        return builder.build();
    }

    @Singleton
    @Provides
    @Named("mealsTableName")
    String tableName() {
        return Optional.ofNullable(System.getenv("tableName")).orElse("meals");
    }

    @Singleton
    @Provides
    @Named("awsRegion")
    String awsRegion() {
        return Optional.ofNullable(System.getenv("region")).orElse("eu-west-1");
    }

    @Singleton
    @Provides
    public MealRepositoryDynamo mealRepositoryDynamo(final AmazonDynamoDB amazonDynamoDb, @Named("mealsTableName") final String tableName) {
        return new MealRepositoryDynamo(amazonDynamoDb, tableName);
    }
}
