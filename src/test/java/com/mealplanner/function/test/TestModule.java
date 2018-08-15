package com.mealplanner.function.test;

import org.mockito.Mockito;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.mealplanner.config.AppModule;

public class TestModule extends AppModule {

    @Override
    public AmazonDynamoDB providesAmazonDynamoDB() {
        return Mockito.mock(AmazonDynamoDB.class);
    }
}
