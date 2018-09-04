package com.mealplanner.test;

import javax.inject.Inject;

import org.junit.Before;

import com.mealplanner.dal.MealRepositoryDynamo;

public class IntegrationTestBase {

    private final AppTestComponent appComponent;

    @Inject
    protected MealRepositoryDynamo mealRepositoryDynamo;

    public IntegrationTestBase() {
        appComponent = DaggerAppTestComponent.builder().build();
        appComponent.inject(this);
    }

    @Before
    public void setup() {
        mealRepositoryDynamo.get("m1", "u1");
    }
}