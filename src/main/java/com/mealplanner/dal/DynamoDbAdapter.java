package com.mealplanner.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class DynamoDbAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDbAdapter.class);
    private static final String AWS_REGION = System.getenv("region");

    private final AmazonDynamoDB client;

    public DynamoDbAdapter() {
        LOGGER.info("AWS_REGION value: [{}]", AWS_REGION);

        this.client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(AWS_REGION)
                .build();
    }

    public DynamoDBMapper createDbMapper(final DynamoDBMapperConfig mapperConfig) {
        return new DynamoDBMapper(client, mapperConfig);
    }
}
