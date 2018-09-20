package com.mealplanner.config;

import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;

import dagger.Module;
import dagger.Provides;

@Module
public class InfrastructureModule {

    @Singleton
    @Provides
    @Named("runningTestsOnCi")
    boolean runningTestsOnCi() {
        return Optional.ofNullable(Boolean.valueOf(System.getProperty("runningTestsOnCi"))).orElse(false);
    }

    @Singleton
    @Provides
    @Named("runningInProduction")
    boolean runningInProduction() {
        return Optional.ofNullable(Boolean.valueOf(System.getenv("production"))).orElse(false);
    }

    @Singleton
    @Provides
    AmazonKinesis kinesisClient(final PropertiesService properties) {
        final AmazonKinesisClientBuilder builder = AmazonKinesisClientBuilder.standard();

        final String awsRegion = properties.getAwsRegion();
        final String endpoint = properties.getKinesisEndpoint();
        if (StringUtils.isNotBlank(endpoint)) {
            builder.withEndpointConfiguration(new EndpointConfiguration(endpoint, awsRegion));
        } else {
            builder.withRegion(awsRegion);

        }

        return builder.build();
    }
}
