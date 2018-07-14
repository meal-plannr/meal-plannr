package com.mealplanner.function;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;
import com.serverless.ApiGatewayResponse;

public class GetMealHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetMealHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(final Map<String, Object> request, final Context context) {
        try {
            final Map<String, String> pathParameters = (Map<String, String>) request.get("pathParameters");
            final String id = pathParameters.get("id");
            final MealRepository repository = new MealRepository();
            final Meal meal = repository.get(id);

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
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
