package com.mealplanner.function;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
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

public class SavedMealKinesis implements RequestHandler<KinesisEvent, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SavedMealKinesis.class);

    @Inject
    MealRepository mealRepository;

    @Inject
    @Named("mealsDynamoDbMapper")
    DynamoDBMapper mapper;

    public SavedMealKinesis() {
        final AppComponent component = DaggerAppComponent.builder().build();
        component.inject(this);
    }

    @Override
    public Void handleRequest(final KinesisEvent event, final Context context) {
        LOGGER.debug("Received Kinesis event");

        for (final KinesisEventRecord record : event.getRecords()) {
            LOGGER.debug("Processing Kinesis event record");

            final byte[] data = record.getKinesis().getData().array();

            final ObjectMapper objectMapper = new ObjectMapper();
            try {
                final JsonNode node = objectMapper.readTree(data);
                final String mealId = node.get("mealId").asText();
                final String userId = node.get("userId").asText();

                LOGGER.debug("Looking up meal for ID [{}] and user [{}]", mealId, userId);

                final Meal meal = mealRepository.get(mealId, userId);
                meal.setDescription("UPDATED");
                mapper.save(meal);

                LOGGER.debug("Saving meal with updated description");
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return null;
    }

}
