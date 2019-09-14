package com.mealplanner;

import javax.inject.Singleton;

import com.mealplanner.aws.AwsModule;
import com.mealplanner.meal.CreateMealHandler;
import com.mealplanner.meal.DeleteMealHandler;
import com.mealplanner.meal.GetMealHandler;
import com.mealplanner.meal.ListMealsHandler;
import com.mealplanner.meal.MealModule;
import com.mealplanner.meal.PutMealHandler;

import dagger.Component;

@Singleton
@Component(modules = {
        AwsModule.class,
        MealModule.class })
public interface AppComponent {

    void inject(ListMealsHandler handler);

    void inject(GetMealHandler handler);

    void inject(CreateMealHandler handler);

    void inject(PutMealHandler handler);

    void inject(DeleteMealHandler handler);
}
