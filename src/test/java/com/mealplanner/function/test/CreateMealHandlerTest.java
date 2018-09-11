package com.mealplanner.function.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;
import com.mealplanner.function.CreateMealHandler;
import com.mealplanner.function.ListMealsHandler;
import com.mealplanner.function.util.ApiGatewayResponse;
import com.mealplanner.test.HandlerUnitTestBase;

public class CreateMealHandlerTest extends HandlerUnitTestBase {

    private static final String MEAL_DESCRIPTION = "meal description";

    private static final Meal MEAL = new Meal();

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private CreateMealHandler handler;

    @Override
    @BeforeEach
    public void setup() throws Exception {
        super.setup();

        when(mealRepository.create()).thenReturn(MEAL);

        final Map<String, String> bodyMap = ImmutableMap.of("description", MEAL_DESCRIPTION);
        final String body = new ObjectMapper().writeValueAsString(bodyMap);
        when(request.getBody()).thenReturn(body);
    }

    @Test
    public void user_id_is_set_on_meal() throws Exception {
        handler.handleRequest(request, context);

        assertThat(MEAL.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    public void description_from_request_body_is_set_on_meal() throws Exception {
        handler.handleRequest(request, context);

        assertThat(MEAL.getDescription()).isEqualTo(MEAL_DESCRIPTION);
    }

    @Test
    public void meal_is_created_when_description_and_user_id_are_set() throws Exception {
        handler.handleRequest(request, context);

        verify(mealRepository).save(MEAL);
    }

    @Test
    public void meal_is_returned_in_response_body() throws Exception {
        final ApiGatewayResponse response = handler.handleRequest(request, context);

        final Meal returnedMeal = new ObjectMapper().readValue(response.getBody(), Meal.class);
        assertThat(returnedMeal).isEqualTo(MEAL);
    }

    @Test
    public void access_control_allow_origin_open_to_all() throws Exception {
        final ApiGatewayResponse response = handler.handleRequest(request, context);

        final String accessControlHeader = response.getHeaders().get(ListMealsHandler.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN);
        assertThat(accessControlHeader).isEqualTo("*");
    }

    @Test
    public void http_status_code_200_returned_on_success() throws Exception {
        final ApiGatewayResponse response = handler.handleRequest(request, context);

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void http_status_code_500_returned_on_error() throws Exception {
        doThrow(RuntimeException.class).when(mealRepository).save(MEAL);

        final ApiGatewayResponse response = handler.handleRequest(request, context);

        assertThat(response.getStatusCode()).isEqualTo(500);
    }

    @Test
    public void body_contains_error_text_when_error_occurs() throws Exception {
        doThrow(RuntimeException.class).when(mealRepository).save(MEAL);

        final ApiGatewayResponse response = handler.handleRequest(request, context);

        final String expectedErrorResponse = String.format(CreateMealHandler.ERROR_MESSAGE_TEMPLATE, request);
        assertThat(response.getBody()).isEqualTo(expectedErrorResponse);
    }
}
