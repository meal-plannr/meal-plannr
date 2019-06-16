package com.mealplanner.function;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mealplanner.config.AppComponent;
import com.mealplanner.config.DaggerAppComponent;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;
import com.mealplanner.function.util.ApiGatewayRequest;
import com.mealplanner.function.util.ApiGatewayResponse;
import com.mealplanner.function.util.HandlerUtil;

public class GetMealHandler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    public static final String ERROR_MESSAGE_TEMPLATE = "Error retrieving meal with request [%s]";

    private static final Logger LOGGER = LogManager.getLogger(GetMealHandler.class);

    @Inject
    MealRepository repository;

    public GetMealHandler() {
        final AppComponent component = DaggerAppComponent.builder().build();
        component.inject(this);
    }

    @Override
    public ApiGatewayResponse handleRequest(final ApiGatewayRequest request, final Context context) {
        LOGGER.debug("Entry point to retrieve single meal");
        try {
            final Map<String, String> pathParameters = request.getPathParameters();
            final String id = pathParameters.get("id");
            final String userId = request.getRequestContext().getIdentity().getCognitoIdentityId();

            LOGGER.debug("Retrieving meal with ID [{}] for user [{}]", id, userId);

            final Meal meal = repository.get(id, userId);

            LOGGER.debug("Finished retrieving meal with ID [{}] for user [{}]. Found meal [{}]", id, userId, meal != null);

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
