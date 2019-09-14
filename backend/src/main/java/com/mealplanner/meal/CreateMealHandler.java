package com.mealplanner.meal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mealplanner.AppComponent;
import com.mealplanner.DaggerAppComponent;

public class CreateMealHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public static final String ERROR_MESSAGE_TEMPLATE = "Error saving meal with request [%s]";

    private static final Logger LOGGER = LogManager.getLogger(CreateMealHandler.class);

    public CreateMealHandler() {
        final AppComponent component = DaggerAppComponent.builder().build();
        component.inject(this);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent request, final Context context) {
        return null;
        //        try {
        //            final String userId = request.getRequestContext().getIdentity().getCognitoIdentityId();
        //            LOGGER.debug("User ID [{}]", userId);
        //
        //            final JsonNode body = new ObjectMapper().readTree(request.getBody());
        //            final Meal meal = mealRepository.create();
        //            meal.setUserId(userId);
        //            meal.setDescription(body.get("description").asText());
        //            mealRepository.save(meal);
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
