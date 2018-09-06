package com.mealplanner.test;

import javax.inject.Singleton;

import com.mealplanner.config.DaoModule;

import dagger.Component;

@Singleton
@Component(modules = { TestInfrastructureModule.class, DaoModule.class })
public interface AppTestComponent {

    void inject(IntegrationTestBase integrationTestBase);
}
