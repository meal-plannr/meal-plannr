package com.mealplanner.dal.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.mealplanner.domain.Meal;
import com.mealplanner.test.IntegrationTestBase;

public class MealRepositoryDynamoIT extends IntegrationTestBase {

    @Test
    public void all_meals_are_returned_for_a_user() {
        final Meal meal1 = new Meal();
        meal1.setId(UUID.randomUUID().toString());
        meal1.setUserId("u1");
        meal1.setDescription("Meal 1");

        insertMeals(meal1);

        final List<Meal> meals = mealRepositoryDynamo.getAllMealsForUser("u1");
        Assertions.assertThat(meals).containsExactlyInAnyOrder(meal1);
    }

    private void insertMeals(final Meal... meals) {
        Stream.of(meals).forEach(this::insertMeal);
    }

    private void insertMeal(final Meal meal) {
        final Map<String, AttributeValue> mealMap = new HashMap<>();
        mealMap.put("userId", new AttributeValue().withS(meal.getUserId()));
        mealMap.put("mealId", new AttributeValue().withS(meal.getId()));
        mealMap.put("description", new AttributeValue().withS(meal.getDescription()));

        amazonDynamoDb.putItem(new PutItemRequest()
                .withTableName(mealsTableName)
                .withItem(mealMap));
    }
}
