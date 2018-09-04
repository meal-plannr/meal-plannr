package com.mealplanner.test;

import javax.inject.Singleton;

import com.mealplanner.config.AppModule;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class })
public interface AppTestComponent {

    void inject(IntegrationTestBase integrationTestBase);
}
