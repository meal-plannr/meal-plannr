package com.mealplanner.config;

import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.mealplanner.dal.DynamoDbAdapter;
import com.mealplanner.dal.DynamoDbFactory;
import com.mealplanner.domain.Meal;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Singleton
    @Provides
    public AmazonDynamoDB amazonDynamoDb(final PropertiesService properties) {
        final AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();

        final String awsRegion = properties.getAwsRegion();
        final String endpoint = properties.getDynamoEndpoint();
        if ((endpoint != null) && !endpoint.isEmpty()) {
            builder.withEndpointConfiguration(new EndpointConfiguration(endpoint, awsRegion));
        } else {
            builder.withRegion(awsRegion);

        }

        return builder.build();
    }

    @Singleton
    @Provides
    @Named("mealsDynamoDbMapper")
    public DynamoDBMapper mealsDynamoDbMapper(final PropertiesService propertiesService, final DynamoDbAdapter dynamoDbAdapter) {
        final DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(propertiesService.getMealsTableName()))
                .build();

        return dynamoDbAdapter.createDbMapper(mapperConfig);
    }

    @Singleton
    @Provides
    @Named("mealsDynamoDbFactory")
    public DynamoDbFactory<Meal> dynamoDbFactory() {
        return new DynamoDbFactory<>();
    }
}
