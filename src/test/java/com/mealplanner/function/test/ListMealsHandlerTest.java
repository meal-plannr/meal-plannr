package com.mealplanner.function.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;
import com.mealplanner.function.ListMealsHandler;
import com.mealplanner.function.util.ApiGatewayResponse;
import com.mealplanner.test.HandlerUnitTestBase;

public class ListMealsHandlerTest extends HandlerUnitTestBase {

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private ListMealsHandler handler;

    @Test
    public void all_users_meals_are_returned() throws Exception {
        final List<Meal> meals = Arrays.asList();
        when(mealRepository.getAllMealsForUser(USER_ID)).thenReturn(meals);

        final ApiGatewayResponse response = handler.handleRequest(request, context);
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<Meal> actualMeals = objectMapper.readValue(response.getBody(), new TypeReference<List<Meal>>() {
        });

        assertThat(actualMeals).containsExactlyInAnyOrderElementsOf(meals);
    }
}
