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
import com.mealplanner.function.util.HandlerUtil;

public class CreateMealHandler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    public static final String ERROR_MESSAGE_TEMPLATE = "Error saving meal with request [%s]";

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateMealHandler.class);

    @Inject
    MealRepository mealRepository;

    public CreateMealHandler() {
        final AppComponent component = DaggerAppComponent.builder().build();
        component.inject(this);
    }

    @Override
    public ApiGatewayResponse handleRequest(final ApiGatewayRequest request, final Context context) {
        try {
            final String userId = request.getRequestContext().getIdentity().getCognitoIdentityId();
            LOGGER.info("User ID [{}]", userId);

            final JsonNode body = new ObjectMapper().readTree(request.getBody());
            final Meal meal = mealRepository.create();
            meal.setUserId(userId);
            meal.setDescription(body.get("description").asText());
            mealRepository.save(meal);

            final Map<String, String> newHeaders = new HashMap<>();
            newHeaders.put(HandlerUtil.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setHeaders(newHeaders)
                    .setObjectBody(meal)
                    .build();
        } catch (final Exception e) {
            final String errorText = String.format(ERROR_MESSAGE_TEMPLATE, request);
            LOGGER.error(errorText, e);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setRawBody(errorText)
                    .build();
        }
    }
}
