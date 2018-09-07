package com.mealplanner.config;

import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.mealplanner.dal.DynamoDbAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Singleton
    @Provides
    @Named("mealsDynamoDbMapper")
    public DynamoDBMapper mealsDynamoDbMapper(@Named("mealsTableName") final String tableName, final DynamoDbAdapter dynamoDbAdapter) {
        final DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName))
                .build();

        return dynamoDbAdapter.createDbMapper(mapperConfig);
    }
}
