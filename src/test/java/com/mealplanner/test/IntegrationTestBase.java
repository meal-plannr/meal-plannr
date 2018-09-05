package com.mealplanner.test;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.mealplanner.dal.MealRepositoryDynamo;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

public class IntegrationTestBase {

    private final AppTestComponent appComponent;

    @Inject
    protected MealRepositoryDynamo mealRepositoryDynamo;

    @Inject
    DynamoDbClient dynamoDb;

    @Inject
    @Named("mealsTableName")
    String mealsTableName;

    public IntegrationTestBase() {
        appComponent = DaggerAppTestComponent.builder().build();
        appComponent.inject(this);
    }

    @BeforeEach
    public void setup() {
        final boolean tableExists = dynamoDb.listTables(ListTablesRequest.builder().build()).tableNames().stream()
                .filter(tableName -> tableName.equals(mealsTableName)).count() > 0;

        if (tableExists) {
            dynamoDb.deleteTable(DeleteTableRequest.builder()
                    .tableName(mealsTableName)
                    .build());
        }

        dynamoDb.createTable(CreateTableRequest.builder()
                .tableName(mealsTableName)
                .keySchema(
                        KeySchemaElement.builder()
                                .keyType(KeyType.HASH)
                                .attributeName("userId")
                                .build(),
                        KeySchemaElement.builder()
                                .keyType(KeyType.RANGE)
                                .attributeName("mealId")
                                .build())
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("userId")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("mealId")
                                .attributeType(ScalarAttributeType.S)
                                .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(1L)
                        .writeCapacityUnits(1L)
                        .build())
                .build());

        final Map<String, AttributeValue> mealMap = new HashMap<>();
        mealMap.put("userId", AttributeValue.builder().s("u1").build());
        mealMap.put("mealId", AttributeValue.builder().s("m1").build());

        int attempts = 0;
        while (attempts < 10) {
            try {
                dynamoDb.putItem(PutItemRequest.builder()
                        .tableName(mealsTableName)
                        .item(mealMap)
                        .build());
                return;
            } catch (final ResourceNotFoundException rnfe) {
                attempts++;
            }
        }
    }

    @AfterEach
    public void teardown() {
        dynamoDb.deleteTable(DeleteTableRequest.builder()
                .tableName(mealsTableName)
                .build());
    }
}