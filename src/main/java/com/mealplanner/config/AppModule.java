package com.mealplanner.config;

import com.mealplanner.dal.DynamoDbAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    public DynamoDbAdapter providesDynamoDbAdapter() {
        return new DynamoDbAdapter();
    }

}
