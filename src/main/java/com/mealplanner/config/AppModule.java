package com.mealplanner.config;

import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private static final String AWS_REGION = System.getenv("region");

    @Provides
    @Singleton
    public AmazonDynamoDB providesAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(AWS_REGION)
                .build();
    }

    //    @Provides
    //    @Singleton
    //    public AmazonDynamoDB providesAmazonDynamoDB(@Named("awsRegion") final String awsRegion) {
    //        return AmazonDynamoDBClientBuilder.standard()
    //                .withRegion(awsRegion)
    //                .build();
    //    }
    //
    //    @Provides
    //    @Named("awsRegion")
    //    @Singleton
    //    public String provideAwsRegion() {
    //        return System.getenv("region");
    //    }
}
