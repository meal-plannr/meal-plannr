package com.mealplanner.config;

import java.net.URI;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.mealplanner.dal.MealRepositoryDynamo;

import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.utils.StringUtils;

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

    @Singleton
    @Provides
    public DynamoDbClient dynamoDb(@Named("awsRegion") final String awsRegion) {
        final String endpoint = System.getenv("ENDPOINT_OVERRIDE");

        final DynamoDbClientBuilder builder = DynamoDbClient.builder();
        builder.httpClient(ApacheHttpClient.builder().build());

        if ((endpoint != null) && !endpoint.isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        if (StringUtils.isNotBlank(awsRegion)) {
            builder.region(Region.of(awsRegion));
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
    public MealRepositoryDynamo mealRepositoryDynamo(final DynamoDbClient dynamoDb, @Named("mealsTableName") final String tableName) {
        return new MealRepositoryDynamo(dynamoDb, tableName);
    }
}
