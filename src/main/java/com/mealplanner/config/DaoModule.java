package com.mealplanner.config;

import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.mealplanner.dal.MealRepositoryDynamo;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Singleton
    @Provides
    public MealRepositoryDynamo mealRepositoryDynamo(final AmazonDynamoDB amazonDynamoDb, @Named("mealsTableName") final String tableName) {
        return new MealRepositoryDynamo(amazonDynamoDb, tableName);
    }
}
