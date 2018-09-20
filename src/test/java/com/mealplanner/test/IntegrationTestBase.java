package com.mealplanner.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.CreateStreamRequest;
import com.amazonaws.services.kinesis.model.DescribeStreamRequest;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.ResourceNotFoundException;
import com.mealplanner.config.PropertiesService;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;

public class IntegrationTestBase {

    private static boolean localMealsTableCreated = false;

    private final AppTestComponent appComponent;

    @Inject
    protected MealRepository mealRepository;

    @Inject
    PropertiesService properties;

    @Inject
    protected AmazonDynamoDB amazonDynamoDb;

    @Inject
    @Named("mealsDynamoDbMapper")
    DynamoDBMapper mealsMapper;

    @Inject
    AmazonKinesis amazonKinesis;

    public IntegrationTestBase() {
        System.setProperty("com.amazonaws.sdk.disableCertChecking", "true");
        System.setProperty("com.amazonaws.sdk.disableCbor", "true");

        appComponent = DaggerAppTestComponent.builder().build();
        appComponent.inject(this);
    }

    @BeforeEach
    public void setup() throws Exception {
        if (!localMealsTableCreated && needToCreateTables()) {
            createMealsTable();
            localMealsTableCreated = true;

            createKinesisStream();
        }

        deleteMeals();
    }

    private void createKinesisStream() {
        final String streamName = "savedMeals";
        final Integer streamSize = 1;

        final CreateStreamRequest createStreamRequest = new CreateStreamRequest();
        createStreamRequest.setStreamName(streamName);
        createStreamRequest.setShardCount(streamSize);
        amazonKinesis.createStream(createStreamRequest);

        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(getStreamStatus(streamName)).isEqualTo("ACTIVE"));
    }

    private String getStreamStatus(final String myStreamName) {
        final DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest();
        describeStreamRequest.setStreamName(myStreamName);
        try {
            final DescribeStreamResult describeStreamResponse = amazonKinesis.describeStream(describeStreamRequest);
            return describeStreamResponse.getStreamDescription().getStreamStatus();
        } catch (final ResourceNotFoundException e) {
        }

        return null;
    }

    private boolean needToCreateTables() {
        return properties.needToCreateDynamoTables();
    }

    private void deleteMeals() {
        final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        final PaginatedScanList<Meal> result = mealsMapper.scan(Meal.class, scanExpression);
        for (final Meal meal : result) {
            mealsMapper.delete(meal);
        }
    }

    private void createMealsTable() throws Exception {
        final String mealsTableName = properties.getMealsTableName();

        TableUtils.createTableIfNotExists(amazonDynamoDb, new CreateTableRequest()
                .withTableName(mealsTableName)
                .withKeySchema(
                        new KeySchemaElement()
                                .withKeyType(KeyType.HASH)
                                .withAttributeName("userId"),
                        new KeySchemaElement()
                                .withKeyType(KeyType.RANGE)
                                .withAttributeName("mealId"))
                .withAttributeDefinitions(
                        new AttributeDefinition()
                                .withAttributeName("userId")
                                .withAttributeType(ScalarAttributeType.S),
                        new AttributeDefinition()
                                .withAttributeName("mealId")
                                .withAttributeType(ScalarAttributeType.S))
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L)));

        TableUtils.waitUntilActive(amazonDynamoDb, mealsTableName, 5000, 100);
    }
}