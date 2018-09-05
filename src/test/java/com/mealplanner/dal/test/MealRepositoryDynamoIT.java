package com.mealplanner.dal.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mealplanner.domain.Meal;
import com.mealplanner.test.IntegrationTestBase;

public class MealRepositoryDynamoIT extends IntegrationTestBase {

    @Test
    public void meal_id_and_user_id_are_used_to_retrieve_a_meal() {
        final Meal meal = mealRepositoryDynamo.get("m1", "u1");
        Assertions.assertThat(meal).isNotNull();
    }

}
