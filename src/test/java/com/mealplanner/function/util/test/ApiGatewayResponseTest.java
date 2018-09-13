package com.mealplanner.function.util.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.mealplanner.domain.Meal;
import com.mealplanner.function.util.ApiGatewayResponse;

public class ApiGatewayResponseTest {

    @Test
    public void status_code_is_set_from_constructor() {
        final ApiGatewayResponse response = new ApiGatewayResponse(101, null, null, false);
        assertThat(response.getStatusCode()).isEqualTo(101);
    }

    @Test
    public void body_is_set_from_constructor() {
        final String body = "This is the response body";
        final ApiGatewayResponse response = new ApiGatewayResponse(101, body, null, false);
        assertThat(response.getBody()).isEqualTo(body);
    }

    @Test
    public void headers_are_set_from_constructor() {
        final Map<String, String> headers = ImmutableMap.of("header1", "value1", "header2", "value2");
        final ApiGatewayResponse response = new ApiGatewayResponse(101, null, headers, false);
        assertThat(response.getHeaders()).isEqualTo(headers);
    }

    @Test
    public void is_base_64_encoded_is_set_from_constructor() {
        final ApiGatewayResponse response = new ApiGatewayResponse(101, null, null, true);
        assertThat(response.isIsBase64Encoded()).isTrue();
    }

    @Test
    public void status_code_is_set_from_builder() {
        final ApiGatewayResponse response = new ApiGatewayResponse.Builder().setStatusCode(101).build();
        assertThat(response.getStatusCode()).isEqualTo(101);
    }

    @Test
    public void headers_are_set_from_builder() {
        final Map<String, String> headers = ImmutableMap.of("header1", "value1", "header2", "value2");
        final ApiGatewayResponse response = new ApiGatewayResponse.Builder().setHeaders(headers).build();
        assertThat(response.getHeaders()).isEqualTo(headers);
    }

    @Test
    public void is_base_64_encoded_is_set_from_builder() {
        final ApiGatewayResponse response = new ApiGatewayResponse.Builder().setBase64Encoded(true).build();
        assertThat(response.isIsBase64Encoded()).isTrue();
    }

    @Test
    public void raw_body_takes_priority() {
        final String rawBody = "raw string";
        final Meal meal = new Meal();
        final String binaryBodyString = "binary body";
        final byte[] binaryBody = binaryBodyString.getBytes();

        final ApiGatewayResponse response = new ApiGatewayResponse.Builder()
                .setRawBody(rawBody)
                .setObjectBody(meal)
                .setBinaryBody(binaryBody)
                .build();

        assertThat(response.getBody()).isEqualTo(rawBody);
    }

    @Test
    public void object_body_is_used_if_no_raw_body_provided() throws Exception {
        final Meal meal = new Meal();
        final String binaryBodyString = "binary body";
        final byte[] binaryBody = binaryBodyString.getBytes();

        final ApiGatewayResponse response = new ApiGatewayResponse.Builder()
                .setObjectBody(meal)
                .setBinaryBody(binaryBody)
                .build();

        final String jsonRepresentation = new ObjectMapper().writeValueAsString(meal);
        assertThat(response.getBody()).isEqualTo(jsonRepresentation);
    }

    @Test
    public void binary_body_is_used_as_body_if_nothing_else_specified() {
        final String binaryBodyString = "binary body";
        final byte[] binaryBody = binaryBodyString.getBytes();
        final ApiGatewayResponse response = new ApiGatewayResponse.Builder()
                .setBinaryBody(binaryBody)
                .build();

        final String base64EncodedBinaryBody = new String(Base64.getEncoder().encode(binaryBody), StandardCharsets.UTF_8);
        assertThat(response.getBody()).isEqualTo(base64EncodedBinaryBody);
    }

    @Test
    public void is_base_64_encoded_when_binary_body_specified() {
        final String binaryBodyString = "binary body";
        final byte[] binaryBody = binaryBodyString.getBytes();
        final ApiGatewayResponse response = new ApiGatewayResponse.Builder()
                .setBinaryBody(binaryBody)
                .build();

        assertThat(response.isIsBase64Encoded()).isTrue();
    }

    @Test
    public void is_not_base_64_encoded_when_raw_body_specified() {
        final String rawBody = "raw string";
        final ApiGatewayResponse response = new ApiGatewayResponse.Builder()
                .setRawBody(rawBody)
                .build();

        assertThat(response.isIsBase64Encoded()).isFalse();
    }

    @Test
    public void is_not_base_64_encoded_when_object_body_specified() {
        final Meal meal = new Meal();
        final ApiGatewayResponse response = new ApiGatewayResponse.Builder()
                .setObjectBody(meal)
                .build();

        assertThat(response.isIsBase64Encoded()).isFalse();
    }

    @Test
    public void status_code_is_200_if_not_specified_in_builder() {
        final ApiGatewayResponse response = new ApiGatewayResponse.Builder().build();
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void headers_are_empty_if_not_specified_in_builder() {
        final ApiGatewayResponse response = new ApiGatewayResponse.Builder().build();
        assertThat(response.getHeaders()).isEmpty();
    }

    @Test
    public void body_is_null_if_not_specified() {
        final ApiGatewayResponse response = new ApiGatewayResponse.Builder().build();
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void exception_is_thrown_if_object_cannot_be_parsed() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ApiGatewayResponse.Builder().setObjectBody(new PrivateFields()).build())
                .withCauseInstanceOf(JsonProcessingException.class)
                .withMessage(ApiGatewayResponse.ERROR_SERIALISING_OBJECT);
    }

    /**
     * Used to cause an exception when serialising this class using Jackson. It fails because there are no public setters.
     */
    class PrivateFields {
        int id;
    }
}