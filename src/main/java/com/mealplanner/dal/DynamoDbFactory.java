package com.mealplanner.dal;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mealplanner.domain.Meal;

public class DynamoDbFactory {

    public Map<String, AttributeValue> createAttributesMap() {
        return new HashMap<>();
    }

    public AttributeValue createAttributeValue() {
        return new AttributeValue();
    }

    public DynamoDBQueryExpression<Meal> createQueryExpression() {
        return new DynamoDBQueryExpression<>();
    }
}