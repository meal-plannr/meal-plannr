package com.mealplanner.config;

import javax.inject.Singleton;

import com.mealplanner.app.MealPlannrApp;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    void inject(MealPlannrApp app);
}
