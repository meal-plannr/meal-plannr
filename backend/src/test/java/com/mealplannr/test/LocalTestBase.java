package com.mealplannr.test;

import com.mealplannr.AppTestComponent;
import com.mealplannr.DaggerAppTestComponent;

public class LocalTestBase {

    protected static final AppTestComponent APP_COMPONENT = DaggerAppTestComponent.create();

    public LocalTestBase() {
        APP_COMPONENT.inject(this);
    }

}