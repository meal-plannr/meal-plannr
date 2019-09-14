package com.mealplanner.meal;

import java.net.URI;

import javax.inject.Singleton;

import com.mealplanner.test.TestProperties;

import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

@Module(includes = BaseMealModule.class)
public abstract class TestMealModule {

    @Provides
    @Singleton
    static DynamoDbClientBuilder dynamoDbClientBuilder(final TestProperties properties, final SdkHttpClient httpClient, final Region region) {
        return DynamoDbClient.builder()
                .httpClient(httpClient)
                .region(region)
                .endpointOverride(URI.create(properties.getDynamoEndpoint()));
    }
}
