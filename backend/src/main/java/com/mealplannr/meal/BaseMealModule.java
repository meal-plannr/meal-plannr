package com.mealplannr.meal;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

@Module
public abstract class BaseMealModule {

    @Provides
    @Singleton
    static DynamoDbClient dynamoDbClient(final DynamoDbClientBuilder builder) {
        return builder.build();
    }
}
