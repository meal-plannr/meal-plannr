package com.mealplannr;

import javax.inject.Singleton;

import com.mealplannr.aws.TestAwsModule;
import com.mealplannr.meal.TestMealModule;
import com.mealplannr.security.SecurityModule;
import com.mealplannr.security.SecurityServiceLT;
import com.mealplannr.test.LocalTestBase;

import dagger.Component;

@Singleton
@Component(modules = {
        TestAwsModule.class,
        TestMealModule.class,
        SecurityModule.class })
public interface AppTestComponent {

    void inject(LocalTestBase localTestBase);

    void inject(SecurityServiceLT securityServiceLT);
}
