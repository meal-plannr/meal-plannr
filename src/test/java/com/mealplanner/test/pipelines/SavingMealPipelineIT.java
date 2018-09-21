package com.mealplanner.test.pipelines;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import com.mealplanner.domain.Meal;
import com.mealplanner.test.IntegrationTestBase;

@EnabledIfEnvironmentVariable(named = "MEAL_PLANNR_ENVIRONMENT", matches = "ci")
public class SavingMealPipelineIT extends IntegrationTestBase {

    @Test
    public void meal_is_saved_to_database_and_updated_asynchronously() {
        final String userId = "user1";
        final Meal meal = new Meal.Builder().userId(userId).build();
        mealRepository.save(meal);

        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(getMealDescription(meal)).isEqualTo("UPDATED"));
    }

    private String getMealDescription(final Meal meal) {
        return mealRepository.get(meal.getId(), meal.getUserId()).getDescription();
    }
}