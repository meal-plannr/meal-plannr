package com.mealplannr.aws;

import javax.inject.Singleton;

import com.mealplannr.test.TestProperties;

import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.regions.Region;

@Module(includes = BaseAwsModule.class)
public class TestAwsModule {

    @Singleton
    @Provides
    static Region region(final TestProperties properties) {
        return Region.of(properties.getAwsRegion());
    }
}
