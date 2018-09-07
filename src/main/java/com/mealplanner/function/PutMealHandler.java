package com.mealplanner.function;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealplanner.config.AppComponent;
import com.mealplanner.config.DaggerAppComponent;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;
import com.mealplanner.function.util.ApiGatewayRequest;
import com.mealplanner.function.util.ApiGatewayResponse;

public class PutMealHandler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PutMealHandler.class);

    @Inject
    MealRepository repository;

    public PutMealHandler() {
        final AppComponent component = DaggerAppComponent.builder().build();
        component.inject(this);
    }

    @Override
    public ApiGatewayResponse handleRequest(final ApiGatewayRequest request, final Context context) {
        try {
            final Map<String, String> pathParameters = request.getPathParameters();
            final String id = pathParameters.get("id");
            final String userId = request.getRequestContext().getIdentity().getCognitoIdentityId();

            final Meal meal = repository.get(id, userId);

            final JsonNode body = new ObjectMapper().readTree(request.getBody());
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
            final String errorText = String.format("Error retrieving meal with request [%s]", request);
            LOGGER.error(errorText, e);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(errorText)
                    .build();
        }
    }
}
