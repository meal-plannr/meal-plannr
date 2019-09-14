package com.mealplanner.function.test;

public class DeleteMealHandlerTest {

    //    private static final String MEAL_ID = "m1";
    //
    //    @Mock
    //    private Map<String, String> pathParameters;
    //
    //    @Mock
    //    private MealRepository mealRepository;
    //
    //    @InjectMocks
    //    private DeleteMealHandler handler;
    //
    //    @Override
    //    @BeforeEach
    //    public void setup() throws Exception {
    //        super.setup();
    //
    //        when(request.getPathParameters()).thenReturn(pathParameters);
    //        when(pathParameters.get(eq("id"))).thenReturn(MEAL_ID);
    //    }
    //
    //    @Test
    //    public void meal_is_deleted_when_id_and_user_id_are_present() throws Exception {
    //        handler.handleRequest(request, context);
    //
    //        verify(mealRepository).delete(MEAL_ID, USER_ID);
    //    }
    //
    //    @Test
    //    public void access_control_allow_origin_open_to_all() throws Exception {
    //        final ApiGatewayResponse response = handler.handleRequest(request, context);
    //
    //        final String accessControlHeader = response.getHeaders().get(HandlerUtil.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN);
    //        assertThat(accessControlHeader).isEqualTo("*");
    //    }
    //
    //    @Test
    //    public void http_status_code_200_returned_on_success() throws Exception {
    //        final ApiGatewayResponse response = handler.handleRequest(request, context);
    //
    //        assertThat(response.getStatusCode()).isEqualTo(200);
    //    }
    //
    //    @Test
    //    public void http_status_code_500_returned_on_error() throws Exception {
    //        doThrow(RuntimeException.class).when(mealRepository).delete(MEAL_ID, USER_ID);
    //
    //        final ApiGatewayResponse response = handler.handleRequest(request, context);
    //
    //        assertThat(response.getStatusCode()).isEqualTo(500);
    //    }
    //
    //    @Test
    //    public void body_contains_error_text_when_error_occurs() throws Exception {
    //        doThrow(RuntimeException.class).when(mealRepository).delete(MEAL_ID, USER_ID);
    //
    //        final ApiGatewayResponse response = handler.handleRequest(request, context);
    //
    //        final String expectedErrorResponse = String.format(DeleteMealHandler.ERROR_MESSAGE_TEMPLATE, request);
    //        assertThat(response.getBody()).isEqualTo(expectedErrorResponse);
    //    }
}
