package com.mealplanner.function;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mealplanner.config.AppComponent;
import com.mealplanner.config.DaggerAppComponent;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.function.util.ApiGatewayRequest;
import com.mealplanner.function.util.ApiGatewayResponse;

public class DeleteMealHandler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    public static final String ERROR_MESSAGE_TEMPLATE = "Error deleting meal for request [%s]";

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteMealHandler.class);

    @Inject
    MealRepository repository;

    public DeleteMealHandler() {
        final AppComponent component = DaggerAppComponent.builder().build();
        component.inject(this);
    }

    @Override
    public ApiGatewayResponse handleRequest(final ApiGatewayRequest request, final Context context) {
        try {
            final Map<String, String> pathParameters = request.getPathParameters();
            final String id = pathParameters.get("id");
            final String userId = request.getRequestContext().getIdentity().getCognitoIdentityId();

            repository.delete(id, userId);

            final Map<String, String> newHeaders = new HashMap<>();
            newHeaders.put("Access-Control-Allow-Origin", "*");

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setHeaders(newHeaders)
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
