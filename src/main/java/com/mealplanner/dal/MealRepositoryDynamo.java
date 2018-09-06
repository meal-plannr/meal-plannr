package com.mealplanner.dal;

import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.mealplanner.domain.Meal;

public class MealRepositoryDynamo {

    private final AmazonDynamoDB amazonDynamoDb;
    private final String tableName;

    public MealRepositoryDynamo(final AmazonDynamoDB amazonDynamoDb, final String tableName) {
        this.amazonDynamoDb = amazonDynamoDb;
        this.tableName = tableName;
    }

    public Meal get(final String mealId, final String userId) {
        return null;

    }

    public List<Meal> getAllMealsForUser(final String string) {
        // TODO Auto-generated method stub
        return null;
    }
}
