package com.mealplanner.function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mealplanner.AppComponent;
import com.mealplanner.DaggerAppComponent;

public class ListMealsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public static final String ERROR_MESSAGE_TEMPLATE = "Error retrieving meal with request [%s]";
    private static final Logger LOGGER = LogManager.getLogger(ListMealsHandler.class);

    public ListMealsHandler() {
        LOGGER.debug("Instantiating ListMealsHandler - before Dagger");
        final AppComponent component = DaggerAppComponent.builder().build();
        LOGGER.debug("Finished instantiating Dagger");

        LOGGER.debug("Injecting handler");
        component.inject(this);
        LOGGER.debug("Finished injecting handler");
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent request, final Context context) {
        LOGGER.debug("Entry point for listing all meals");
        return null;
        //        try {
        //            final String userId = request.getRequestContext().getIdentity().getCognitoIdentityId();
        //            LOGGER.debug("Retrieving all meals for user [{}]", userId);
        //
        //            final List<Meal> meals = repository.getAllMealsForUser(userId);
        //
        //            LOGGER.debug("Retrieved [{}] meals for user [{}]", meals.size(), userId);
        //
        //            final Map<String, String> newHeaders = new HashMap<>();
        //            newHeaders.put(HandlerUtil.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        //            return new APIGatewayProxyResponseEvent()
        //                    .withStatusCode(200)
        //                    .withHeaders(newHeaders)
        //                    .withBody(meals);
        //        } catch (final Exception e) {
        //            final String errorText = String.format(ERROR_MESSAGE_TEMPLATE, request);
        //            LOGGER.error(errorText, e);
        //            return new APIGatewayProxyResponseEvent()
        //                    .withStatusCode(500)
        //                    .withBody(errorText);
        //        }
    }
}
