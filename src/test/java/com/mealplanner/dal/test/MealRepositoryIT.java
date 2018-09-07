package com.mealplanner.dal.test;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mealplanner.domain.Meal;
import com.mealplanner.test.IntegrationTestBase;

public class MealRepositoryIT extends IntegrationTestBase {

    @Test
    public void all_meals_are_returned_for_a_user() {
        final Meal meal1 = new Meal();
        meal1.setUserId("u1");
        meal1.setDescription("Meal 1");
        mealRepository.save(meal1);

        final Meal meal2 = new Meal();
        meal2.setUserId("u1");
        meal2.setDescription("Meal 2");
        mealRepository.save(meal2);

        final List<Meal> meals = mealRepository.getAllMealsForUser("u1");
        Assertions.assertThat(meals).containsExactlyInAnyOrder(meal1, meal2);
    }

}
