package com.mealplanner.meal;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

@Module(includes = BaseMealModule.class)
public abstract class MealModule {

    @Provides
    @Singleton
    static DynamoDbClientBuilder dynamoDbClientBuilder(final SdkHttpClient httpClient, final AwsCredentialsProvider credentialsProvider, final Region region) {
        return DynamoDbClient.builder()
                .httpClient(httpClient)
                .credentialsProvider(credentialsProvider)
                .region(region);
    }
}
