package com.mealplanner.dal;

import com.mealplanner.domain.Meal;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class MealRepositoryDynamo {

    private final DynamoDbClient dynamoDb;
    private final String tableName;

    public MealRepositoryDynamo(final DynamoDbClient dynamoDb, final String tableName) {
        this.dynamoDb = dynamoDb;
        this.tableName = tableName;
    }

    public Meal get(final String mealId, final String userId) {
        return null;

    }

}
