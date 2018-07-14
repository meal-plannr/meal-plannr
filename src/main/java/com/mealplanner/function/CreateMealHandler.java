package com.mealplanner.function;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;
import com.serverless.ApiGatewayResponse;

public class CreateMealHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateMealHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(final Map<String, Object> request, final Context context) {
        try {
            final JsonNode body = new ObjectMapper().readTree((String) request.get("body"));
            final MealRepository repository = new MealRepository();
            final Meal meal = new Meal();
            meal.setDescription(body.get("description").asText());
            repository.save(meal);

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(meal)
                    .build();
        } catch (final Exception e) {
            final String errorText = String.format("Error saving meal with request [%s]", request);
            LOGGER.error(errorText, e);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(errorText)
                    .build();
        }
    }
}
