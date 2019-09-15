package com.mealplanner;

import javax.inject.Singleton;

import com.mealplanner.aws.AwsModule;
import com.mealplanner.meal.MealModule;

import dagger.Component;

@Singleton
@Component(modules = {
        AwsModule.class,
        MealModule.class })
public interface AppComponent {

}
