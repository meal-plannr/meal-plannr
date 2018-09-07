package com.mealplanner.config;

import javax.inject.Singleton;

import com.mealplanner.function.ListMealsHandler;

import dagger.Component;

@Singleton
@Component(modules = { InfrastructureModule.class, DaoModule.class })
public interface AppComponent {

    void inject(ListMealsHandler handler);
}
