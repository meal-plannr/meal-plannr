package com.mealplanner.test;

import javax.inject.Singleton;

import com.mealplanner.config.DaoModule;
import com.mealplanner.config.InfrastructureModule;

import dagger.Component;

@Singleton
@Component(modules = { InfrastructureModule.class, DaoModule.class })
public interface AppTestComponent {

    void inject(IntegrationTestBase integrationTestBase);
}
