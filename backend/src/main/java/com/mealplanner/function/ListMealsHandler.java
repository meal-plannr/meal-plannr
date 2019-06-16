package com.mealplanner.function;

import java.util.HashMap;
import java.util.List;
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

public class ListMealsHandler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    public static final String ERROR_MESSAGE_TEMPLATE = "Error retrieving meal with request [%s]";
    private static final Logger LOGGER = LogManager.getLogger(ListMealsHandler.class);

    @Inject
    MealRepository repository;

    public ListMealsHandler() {
        LOGGER.debug("Instantiating ListMealsHandler - before Dagger");
        final AppComponent component = DaggerAppComponent.builder().build();
        LOGGER.debug("Finished instantiating Dagger");

        LOGGER.debug("Injecting handler");
        component.inject(this);
        LOGGER.debug("Finished injecting handler");
    }

    @Override
    public ApiGatewayResponse handleRequest(final ApiGatewayRequest request, final Context context) {
        LOGGER.debug("Entry point for listing all meals");

        try {
            final String userId = request.getRequestContext().getIdentity().getCognitoIdentityId();
            LOGGER.debug("Retrieving all meals for user [{}]", userId);

            final List<Meal> meals = repository.getAllMealsForUser(userId);

            LOGGER.debug("Retrieved [{}] meals for user [{}]", meals.size(), userId);

            final Map<String, String> newHeaders = new HashMap<>();
            newHeaders.put(HandlerUtil.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setHeaders(newHeaders)
                    .setObjectBody(meals)
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
