package com.mealplanner.meal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mealplanner.AppComponent;
import com.mealplanner.DaggerAppComponent;

public class GetMealHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public static final String ERROR_MESSAGE_TEMPLATE = "Error retrieving meal with request [%s]";

    private static final Logger LOGGER = LogManager.getLogger(GetMealHandler.class);

    public GetMealHandler() {
        final AppComponent component = DaggerAppComponent.builder().build();
        component.inject(this);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent request, final Context context) {
        LOGGER.debug("Entry point to retrieve single meal");
        return null;
        //        try {
        //            final Map<String, String> pathParameters = request.getPathParameters();
        //            final String id = pathParameters.get("id");
        //            final String userId = request.getRequestContext().getIdentity().getCognitoIdentityId();
        //
        //            LOGGER.debug("Retrieving meal with ID [{}] for user [{}]", id, userId);
        //
        //            final Meal meal = repository.get(id, userId);
        //
        //            LOGGER.debug("Finished retrieving meal with ID [{}] for user [{}]. Found meal [{}]", id, userId, meal != null);
        //
        //            final Map<String, String> newHeaders = new HashMap<>();
        //            newHeaders.put(HandlerUtil.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        //
        //            return new APIGatewayProxyResponseEvent()
        //                    .withStatusCode(200)
        //                    .withHeaders(newHeaders)
        //                    .withBody(meal);
        //        } catch (final Exception e) {
        //            final String errorText = String.format(ERROR_MESSAGE_TEMPLATE, request);
        //            LOGGER.error(errorText, e);
        //            return new APIGatewayProxyResponseEvent()
        //                    .withStatusCode(500)
        //                    .withBody(errorText);
        //        }
    }
}
