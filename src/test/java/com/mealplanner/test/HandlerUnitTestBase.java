package com.mealplanner.test;

import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.mealplanner.function.util.ApiGatewayRequest;
import com.mealplanner.function.util.Identity;
import com.mealplanner.function.util.RequestContext;

@ExtendWith(MockitoExtension.class)
public class HandlerUnitTestBase {

    protected static final String USER_ID = "user1";

    @Rule
    public static final EnvironmentVariables ENVIRONMENT_VARIABLES = new EnvironmentVariables();

    @Mock
    protected ApiGatewayRequest request;

    @Mock
    protected RequestContext requestContext;

    @Mock
    protected Identity identity;

    @Mock
    protected Context context;

    @BeforeAll
    public static void setEnvVars() {
        ENVIRONMENT_VARIABLES.set("region", "eu-west-2");
        ENVIRONMENT_VARIABLES.set("tableName", "meals");
    }

    @BeforeEach
    public void setup() throws Exception {
        when(request.getRequestContext()).thenReturn(requestContext);
        when(requestContext.getIdentity()).thenReturn(identity);
        when(identity.getCognitoIdentityId()).thenReturn(USER_ID);
    }
}
