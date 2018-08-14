package com.mealplanner.dal;

import dagger.Module;
import dagger.Provides;

@Module
public class DalModule {

    @Provides
    public static DynamoDbAdapter providesDynamoDbAdapter() {
        return new DynamoDbAdapter();
    }

}
