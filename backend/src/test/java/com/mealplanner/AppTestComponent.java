package com.mealplanner;

import javax.inject.Singleton;

import com.mealplanner.aws.TestAwsModule;
import com.mealplanner.meal.TestMealModule;
import com.mealplanner.test.IntegrationTestBase;

import dagger.Component;

@Singleton
@Component(modules = {
        TestAwsModule.class,
        TestMealModule.class })
public interface AppTestComponent {

    void inject(IntegrationTestBase integrationTestBase);
}
