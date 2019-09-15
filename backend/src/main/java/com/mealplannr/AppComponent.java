package com.mealplannr;

import javax.inject.Singleton;

import com.mealplannr.aws.AwsModule;
import com.mealplannr.meal.MealModule;

import dagger.Component;

@Singleton
@Component(modules = {
        AwsModule.class,
        MealModule.class })
public interface AppComponent {

}
