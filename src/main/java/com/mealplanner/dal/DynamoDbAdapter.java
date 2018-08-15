package com.mealplanner.dal;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

// @Singleton
public class DynamoDbAdapter {

    private final AmazonDynamoDB client;

    //@Inject
    public DynamoDbAdapter(final AmazonDynamoDB client) {
        this.client = client;
    }

    public DynamoDBMapper createDbMapper(final DynamoDBMapperConfig mapperConfig) {
        return new DynamoDBMapper(client, mapperConfig);
    }
}
