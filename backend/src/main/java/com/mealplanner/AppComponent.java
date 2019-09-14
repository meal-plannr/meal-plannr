package com.mealplanner;

import javax.inject.Singleton;

import com.mealplanner.aws.AwsModule;
import com.mealplanner.function.CreateMealHandler;
import com.mealplanner.function.DeleteMealHandler;
import com.mealplanner.function.GetMealHandler;
import com.mealplanner.function.ListMealsHandler;
import com.mealplanner.function.PutMealHandler;
import com.mealplanner.meal.MealModule;

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
