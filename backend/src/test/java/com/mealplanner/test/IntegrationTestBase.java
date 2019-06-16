package com.mealplanner.test;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.mealplanner.config.PropertiesService;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;

public class IntegrationTestBase {

    private static final Logger LOGGER = LogManager.getLogger(IntegrationTestBase.class);

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

    public IntegrationTestBase() {
        appComponent = DaggerAppTestComponent.builder().build();
        appComponent.inject(this);
    }

    @BeforeEach
    public void setup() throws Exception {
        if (properties.isLocalEnvironment() && !localSetupComplete) {
            LOGGER.debug("Starting local setup");

            createMealsTable();

            localSetupComplete = true;
            LOGGER.debug("Local setup complete");
        }

        deleteMeals();
    }

    private void createMealsTable() throws Exception {
        LOGGER.debug("Creating meals table if it doesn't already exist");

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

        LOGGER.debug("Meals table created");
    }

    private void deleteMeals() {
        LOGGER.debug("Deleting all meals");

        final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        final PaginatedScanList<Meal> result = mealsMapper.scan(Meal.class, scanExpression);
        for (final Meal meal : result) {
            mealsMapper.delete(meal,
                    new DynamoDBMapperConfig.Builder()
                            .withSaveBehavior(SaveBehavior.CLOBBER)
                            .build());
        }
    }
}