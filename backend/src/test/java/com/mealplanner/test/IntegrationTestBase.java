package com.mealplanner.test;

import com.mealplanner.AppTestComponent;
import com.mealplanner.DaggerAppTestComponent;

public class IntegrationTestBase {

    private static final AppTestComponent APP_COMPONENT = DaggerAppTestComponent.create();

    public IntegrationTestBase() {
        APP_COMPONENT.inject(this);
    }

}