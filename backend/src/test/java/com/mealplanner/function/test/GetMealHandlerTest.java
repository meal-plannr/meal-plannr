package com.mealplanner.function.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;
import com.mealplanner.function.GetMealHandler;
import com.mealplanner.function.util.ApiGatewayResponse;
import com.mealplanner.function.util.HandlerUtil;
import com.mealplanner.test.HandlerUnitTestBase;

public class GetMealHandlerTest extends HandlerUnitTestBase {

    private static final String MEAL_ID = "m1";
    private static final Meal MEAL = new Meal.Builder().mealId(MEAL_ID).userId(USER_ID).build();

    @Mock
    private Map<String, String> pathParameters;

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private GetMealHandler handler;

    @Override
    @BeforeEach
    public void setup() throws Exception {
        super.setup();

        when(request.getPathParameters()).thenReturn(pathParameters);
        when(pathParameters.get(eq("id"))).thenReturn(MEAL_ID);
    }

    @Test
    public void meal_matching_id_is_returned() throws Exception {
        when(mealRepository.get(MEAL_ID, USER_ID)).thenReturn(MEAL);

        final ApiGatewayResponse response = handler.handleRequest(request, context);

        final Meal returnedMeal = new ObjectMapper().readValue(response.getBody(), Meal.class);
        assertThat(returnedMeal).isEqualTo(MEAL);
    }

    @Test
    public void access_control_allow_origin_open_to_all() throws Exception {
        final ApiGatewayResponse response = handler.handleRequest(request, context);

        final String accessControlHeader = response.getHeaders().get(HandlerUtil.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN);
        assertThat(accessControlHeader).isEqualTo("*");
    }

    @Test
    public void http_status_code_200_returned_on_success() throws Exception {
        final ApiGatewayResponse response = handler.handleRequest(request, context);

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void http_status_code_500_returned_on_error() throws Exception {
        when(mealRepository.get(MEAL_ID, USER_ID)).thenThrow(RuntimeException.class);

        final ApiGatewayResponse response = handler.handleRequest(request, context);

        assertThat(response.getStatusCode()).isEqualTo(500);
    }

    @Test
    public void body_contains_error_text_when_error_occurs() throws Exception {
        when(mealRepository.get(MEAL_ID, USER_ID)).thenThrow(RuntimeException.class);

        final ApiGatewayResponse response = handler.handleRequest(request, context);

        final String expectedErrorResponse = String.format(GetMealHandler.ERROR_MESSAGE_TEMPLATE, request);
        assertThat(response.getBody()).isEqualTo(expectedErrorResponse);
    }
}
