package com.mealplanner.config;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger LOGGER = LogManager.getLogger(DaoModule.class);

    @Singleton
    @Provides
    public AmazonDynamoDB amazonDynamoDb(final PropertiesService properties) {
        LOGGER.debug("Creating AmazonDynamoDB instance");

        final AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();

        final String awsRegion = properties.getAwsRegion();
        final String endpoint = properties.getDynamoEndpoint();
        if ((endpoint != null) && !endpoint.isEmpty()) {
            builder.withEndpointConfiguration(new EndpointConfiguration(endpoint, awsRegion));
        } else {
            builder.withRegion(awsRegion);

        }

        final AmazonDynamoDB dynamoDB = builder.build();
        LOGGER.debug("Finished creating AmazonDynamoDB");

        return dynamoDB;
    }

    @Singleton
    @Provides
    @Named("mealsDynamoDbMapper")
    public DynamoDBMapper mealsDynamoDbMapper(final PropertiesService propertiesService, final DynamoDbAdapter dynamoDbAdapter) {
        LOGGER.debug("Creating mealsDynamoDbMapper instance");

        final DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(propertiesService.getMealsTableName()))
                .build();

        final DynamoDBMapper mealsMapper = dynamoDbAdapter.createDbMapper(mapperConfig);
        LOGGER.debug("Finished creating mealsDynamoDbMapper instance");
        return mealsMapper;
    }

    @Singleton
    @Provides
    @Named("mealsDynamoDbFactory")
    public DynamoDbFactory<Meal> dynamoDbFactory() {
        LOGGER.debug("Creating mealsDynamoDbFactory instance");
        final DynamoDbFactory<Meal> mealsDynamoDbFactory = new DynamoDbFactory<>();
        LOGGER.debug("Finished creating mealsDynamoDbFactory instance");
        return mealsDynamoDbFactory;
    }
}
