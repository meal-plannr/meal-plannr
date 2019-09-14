package com.mealplanner.function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.mealplanner.AppComponent;
import com.mealplanner.DaggerAppComponent;

public class DeleteMealHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public static final String ERROR_MESSAGE_TEMPLATE = "Error deleting meal for request [%s]";

    private static final Logger LOGGER = LogManager.getLogger(DeleteMealHandler.class);

    public DeleteMealHandler() {
        final AppComponent component = DaggerAppComponent.builder().build();
        component.inject(this);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent request, final Context context) {
        return null;
        //        try {
        //            final Map<String, String> pathParameters = request.getPathParameters();
        //            final String id = pathParameters.get("id");
        //            final String userId = request.getRequestContext().getIdentity().getCognitoIdentityId();
        //
        //            repository.delete(id, userId);
        //
        //            final Map<String, String> newHeaders = new HashMap<>();
        //            newHeaders.put(HandlerUtil.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        //
        //            return new APIGatewayProxyResponseEvent()
        //                    .withStatusCode(200)
        //                    .withHeaders(newHeaders);
        //        } catch (final Exception e) {
        //            final String errorText = String.format(ERROR_MESSAGE_TEMPLATE, request);
        //            LOGGER.error(errorText, e);
        //            return new APIGatewayProxyResponseEvent()
        //                    .withStatusCode(500)
        //                    .withBody(errorText);
        //        }
    }
}
