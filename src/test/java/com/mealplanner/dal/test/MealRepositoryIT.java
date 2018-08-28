package com.mealplanner.dal.test;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;

public class MealRepositoryIT {

    @Inject
    MealRepository mealRepository;

    @Test
    public void meal_id_and_user_id_are_used_to_retrieve_a_meal() {
        final Meal meal = mealRepository.get("m1", "u1");
        Assertions.assertThat(meal).isNull();
    }

}
