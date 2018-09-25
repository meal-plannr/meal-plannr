package com.mealplanner.function;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealplanner.config.AppComponent;
import com.mealplanner.config.DaggerAppComponent;
import com.mealplanner.dal.MealRepository;
import com.mealplanner.domain.Meal;

public class UpdateSavedMealDescriptionHandler implements RequestHandler<KinesisEvent, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSavedMealDescriptionHandler.class);

    @Inject
    MealRepository mealRepository;

    @Inject
    @Named("mealsDynamoDbMapper")
    DynamoDBMapper mapper;

    public UpdateSavedMealDescriptionHandler() {
        final AppComponent component = DaggerAppComponent.builder().build();
        component.inject(this);
    }

    @Override
    public Void handleRequest(final KinesisEvent event, final Context context) {
        LOGGER.info("Received Kinesis event");

        for (final KinesisEventRecord record : event.getRecords()) {
            LOGGER.info("Processing Kinesis event record");

            final byte[] data = record.getKinesis().getData().array();

            final ObjectMapper objectMapper = new ObjectMapper();
            try {
                final JsonNode node = objectMapper.readTree(data);

                LOGGER.info("JSON node {}", node);

                final String mealId = node.get("mealId").asText();
                final String userId = node.get("userId").asText();

                LOGGER.info("Looking up meal for ID [{}] and user [{}]", mealId, userId);

                saveWithRetry(mealId, userId);

                LOGGER.info("Saving meal with updated description");
            } catch (final IOException e) {
                LOGGER.error("Error updating meal date", e);
                throw new IllegalStateException("Error updating meal description", e);
            }
        }

        return null;
    }

    private void saveWithRetry(final String mealId, final String userId) {
        final int maxRetries = 10;
        int attempt = 0;

        while (attempt < maxRetries) {
            final Meal meal = mealRepository.get(mealId, userId);
            meal.setDescription("UPDATED");
            try {
                mapper.save(meal);
            } catch (final ConditionalCheckFailedException e) {
                LOGGER.info("Save failed due to old version. Attempt [[{}] of [{}]", attempt, maxRetries);
            }

            attempt++;
        }
    }
}
