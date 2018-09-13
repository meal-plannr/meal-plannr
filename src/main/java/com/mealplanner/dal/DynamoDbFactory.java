package com.mealplanner.dal;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class DynamoDbFactory<T> {

    public Map<String, AttributeValue> createAttributesMap() {
        return new HashMap<>();
    }

    public AttributeValue createAttributeValue() {
        return new AttributeValue();
    }

    public DynamoDBQueryExpression<T> createQueryExpression() {
        return new DynamoDBQueryExpression<>();
    }
}