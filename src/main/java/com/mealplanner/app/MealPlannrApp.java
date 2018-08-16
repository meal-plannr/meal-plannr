package com.mealplanner.app;

import com.mealplanner.config.AppComponent;
import com.mealplanner.config.DaggerAppComponent;

public class MealPlannrApp {

    private static final AppComponent COMPONENT = DaggerAppComponent.builder().build();

    public MealPlannrApp() {
    }

    public static void main(final String[] args) {
        final MealPlannrApp app = new MealPlannrApp();
        COMPONENT.inject(app);
    }
}
