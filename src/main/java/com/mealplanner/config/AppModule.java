package com.mealplanner.config;

import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.mealplanner.dal.DynamoDbAdapter;
import com.mealplanner.dal.MealRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private static final String AWS_REGION = System.getenv("region");

    @Provides
    @Singleton
    public AmazonDynamoDB providesAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(AWS_REGION)
                .build();
    }

    @Provides
    @Singleton
    public DynamoDbAdapter providesDynamoDbAdapter(final AmazonDynamoDB client) {
        return new DynamoDbAdapter(client);
    }

    @Provides
    @Singleton
    public MealRepository providesMealRepository(final DynamoDbAdapter dynamoDbAdapter) {
        return new MealRepository(dynamoDbAdapter);
    }
}
