package com.mealplanner.function;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;
import com.mealplanner.function.util.ApiGatewayRequest;
import com.serverless.ApiGatewayResponse;

public class CreateMealHandler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateMealHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(final ApiGatewayRequest request, final Context context) {
        try {
            final String userId = request.getRequestContext().getIdentity().getCognitoIdentityId();
            LOGGER.info("User ID [{}]", userId);

            final JsonNode body = new ObjectMapper().readTree(request.getBody());
            final MealRepository repository = new MealRepository();
            final Meal meal = new Meal();
            meal.setUserId(userId);
            meal.setDescription(body.get("description").asText());
            repository.save(meal);

            final Map<String, String> newHeaders = new HashMap<>();
            newHeaders.put("Access-Control-Allow-Origin", "*");

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setHeaders(newHeaders)
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
