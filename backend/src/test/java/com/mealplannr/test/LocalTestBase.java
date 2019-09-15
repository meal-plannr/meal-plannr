package com.mealplannr.test;

import com.mealplannr.AppTestComponent;
import com.mealplannr.DaggerAppTestComponent;

public class IntegrationTestBase {

    private static final AppTestComponent APP_COMPONENT = DaggerAppTestComponent.create();

    public IntegrationTestBase() {
        APP_COMPONENT.inject(this);
    }

}