package com.mealplanner.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mealplanner.domain.Meal;

// @Singleton
public class MealRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MealRepository.class);
    private static final String MEALS_TABLE_NAME = System.getenv("tableName");

    private final DynamoDBMapper mapper;

    //@Inject
    public MealRepository(final DynamoDbAdapter dynamoDbAdapter) {
        LOGGER.info("MEALS_TABLE_NAME value: [{}]", MEALS_TABLE_NAME);

        final DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(MEALS_TABLE_NAME))
                .build();

        this.mapper = dynamoDbAdapter.createDbMapper(mapperConfig);
    }

    public Meal get(final String mealId, final String userId) {
        final Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":mealId", new AttributeValue().withS(mealId));
        attributeValues.put(":userId", new AttributeValue().withS(userId));

        final DynamoDBQueryExpression<Meal> queryExpression = new DynamoDBQueryExpression<Meal>()
                .withKeyConditionExpression("mealId = :mealId and userId = :userId")
                .withExpressionAttributeValues(attributeValues);

        final PaginatedQueryList<Meal> result = mapper.query(Meal.class, queryExpression);
        if (result.size() == 1) {
            return result.get(0);
        } else if (result.size() == 0) {
            throw new IllegalStateException(String.format("No Meal found for ID [%s] and user ID [{}]", mealId, userId));
        } else {
            throw new IllegalStateException(String.format("Multiple Meals found for ID [%s] and userID [{}]", mealId, userId));
        }
    }

    public List<Meal> getAllMealsForUser(final String userId) {
        final Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":userId", new AttributeValue().withS(userId));

        final DynamoDBQueryExpression<Meal> queryExpression = new DynamoDBQueryExpression<Meal>()
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(attributeValues);

        return mapper.query(Meal.class, queryExpression);
    }

    public void delete(final String mealId, final String userId) {
        final Meal meal = get(mealId, userId);
        if (meal != null) {
            mapper.delete(meal);
        }
    }

    public void save(final Meal meal) {
        LOGGER.info("Saving meal [{}]", meal);
        mapper.save(meal);
    }
}
