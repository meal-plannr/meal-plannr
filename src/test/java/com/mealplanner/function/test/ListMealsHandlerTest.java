package com.mealplanner.function.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
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
    public void empty_list_is_returned_if_there_are_no_meals_for_user() throws Exception {
        final List<Meal> meals = Collections.emptyList();
        when(mealRepository.getAllMealsForUser(USER_ID)).thenReturn(meals);

        final ApiGatewayResponse response = handler.handleRequest(request, context);
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<Meal> actualMeals = objectMapper.readValue(response.getBody(), new TypeReference<List<Meal>>() {
        });

        assertThat(actualMeals).isEqualTo(meals);
    }

    @Test
    public void list_contains_only_one_element_if_only_one_meal_exists() throws Exception {
        final Meal user1Meal1 = new Meal.Builder().userId(USER_ID).build();

        final List<Meal> meals = Arrays.asList(user1Meal1);
        when(mealRepository.getAllMealsForUser(USER_ID)).thenReturn(meals);

        final ApiGatewayResponse response = handler.handleRequest(request, context);
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<Meal> actualMeals = objectMapper.readValue(response.getBody(), new TypeReference<List<Meal>>() {
        });

        assertThat(actualMeals).containsExactlyInAnyOrderElementsOf(meals);
    }

    @Test
    public void all_users_meals_are_returned() throws Exception {
        final Meal user1Meal1 = new Meal.Builder().userId(USER_ID).build();
        final Meal user1Meal2 = new Meal.Builder().userId(USER_ID).build();

        final List<Meal> meals = Arrays.asList(user1Meal1, user1Meal2);
        when(mealRepository.getAllMealsForUser(USER_ID)).thenReturn(meals);

        final ApiGatewayResponse response = handler.handleRequest(request, context);
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<Meal> actualMeals = objectMapper.readValue(response.getBody(), new TypeReference<List<Meal>>() {
        });

        assertThat(actualMeals).containsExactlyInAnyOrderElementsOf(meals);
    }

    @Test
    public void access_control_allow_origin_open_to_all() throws Exception {
        when(mealRepository.getAllMealsForUser(USER_ID)).thenReturn(Collections.emptyList());

        final ApiGatewayResponse response = handler.handleRequest(request, context);

        final String accessControlHeader = response.getHeaders().get(ListMealsHandler.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN);
        assertThat(accessControlHeader).isEqualTo("*");
    }

    @Test
    public void http_status_code_200_returned_on_success() throws Exception {
        when(mealRepository.getAllMealsForUser(USER_ID)).thenReturn(Collections.emptyList());

        final ApiGatewayResponse response = handler.handleRequest(request, context);

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void http_status_code_500_returned_on_error() throws Exception {
        when(mealRepository.getAllMealsForUser(USER_ID)).thenThrow(RuntimeException.class);

        final ApiGatewayResponse response = handler.handleRequest(request, context);

        assertThat(response.getStatusCode()).isEqualTo(500);
    }

    @Test
    public void body_contains_error_text_when_error_occurs() throws Exception {
        when(mealRepository.getAllMealsForUser(USER_ID)).thenThrow(RuntimeException.class);

        final ApiGatewayResponse response = handler.handleRequest(request, context);

        final String expectedErrorResponse = String.format(ListMealsHandler.ERROR_MESSAGE_TEMPLATE, request);
        assertThat(response.getBody()).isEqualTo(expectedErrorResponse);
    }
}
