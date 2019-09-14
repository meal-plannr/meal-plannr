package com.mealplanner.aws;

import javax.inject.Singleton;

import com.mealplanner.meal.BaseMealModule;

import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;

@Module(includes = BaseMealModule.class)
public abstract class AwsModule {

    @Provides
    @Singleton
    static AwsCredentialsProvider credentialsProvider() {
        return EnvironmentVariableCredentialsProvider.create();
    }

    @Provides
    @Singleton
    static Region region() {
        return Region.of(System.getenv("MEAL_PLANNR_REGION"));
    }
}
