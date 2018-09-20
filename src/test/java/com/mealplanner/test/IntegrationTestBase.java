package com.mealplanner.test;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTestBase.class);

    private static boolean localSetupComplete = false;

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
        appComponent = DaggerAppTestComponent.builder().build();
        appComponent.inject(this);
    }

    @BeforeEach
    public void setup() throws Exception {
        if (properties.isLocalEnvironment() && !localSetupComplete) {
            createMealsTable();

            //Kinesis in Localstack does not support Cbor so it must be disabled when running locally
            System.setProperty("com.amazonaws.sdk.disableCbor", "true");
            createKinesisStreamIfNecessary();
            localSetupComplete = true;
        }

        deleteMeals();
    }

    private void createKinesisStreamIfNecessary() {
        final String streamName = "savedMeals";
        if (!streamIsActive(streamName)) {
            final Integer streamSize = 1;

            final CreateStreamRequest createStreamRequest = new CreateStreamRequest();
            createStreamRequest.setStreamName(streamName);
            createStreamRequest.setShardCount(streamSize);
            amazonKinesis.createStream(createStreamRequest);

            Awaitility.await()
                    .atMost(10, TimeUnit.SECONDS)
                    .until(() -> streamIsActive(streamName));
        }
    }

    private boolean streamIsActive(final String myStreamName) {
        final DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest();
        describeStreamRequest.setStreamName(myStreamName);
        try {
            final DescribeStreamResult describeStreamResponse = amazonKinesis.describeStream(describeStreamRequest);
            return describeStreamResponse.getStreamDescription().getStreamStatus().equals("ACTIVE");
        } catch (final ResourceNotFoundException e) {
        }

        return false;
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