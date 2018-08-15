package com.mealplanner.config;

import javax.inject.Singleton;

import com.mealplanner.dal.MealRepository;
import com.mealplanner.function.ListMealsHandler;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    void inject(ListMealsHandler hanlder);

    void inject(MealRepository repository);
}
