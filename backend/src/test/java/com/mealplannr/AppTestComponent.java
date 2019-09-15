package com.mealplannr;

import javax.inject.Singleton;

import com.mealplannr.aws.TestAwsModule;
import com.mealplannr.meal.TestMealModule;
import com.mealplannr.test.IntegrationTestBase;

import dagger.Component;

@Singleton
@Component(modules = {
        TestAwsModule.class,
        TestMealModule.class })
public interface AppTestComponent {

    void inject(IntegrationTestBase integrationTestBase);
}
