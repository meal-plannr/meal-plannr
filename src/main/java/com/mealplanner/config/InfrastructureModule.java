package com.mealplanner.config;

import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;

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
    KinesisProducer kinesisProducer(final PropertiesService propertiesService) {
        final KinesisProducerConfiguration config = new KinesisProducerConfiguration()
                .setRegion(propertiesService.getAwsRegion());
        final String host = propertiesService.getKinesisHost();
        final Optional<Long> port = propertiesService.getKinesisPort();
        if (StringUtils.isNotBlank(host)) {
            config.setKinesisEndpoint(host);
        }
        if (port.isPresent()) {
            config.setKinesisPort(port.get());
        }

        return new KinesisProducer(config);
    }

    @Singleton
    @Provides
    AmazonKinesis kinesisClient(final PropertiesService properties) {
        final AmazonKinesisClientBuilder builder = AmazonKinesisClientBuilder.standard();

        final String awsRegion = properties.getAwsRegion();
        final String host = properties.getKinesisHost();
        final Optional<Long> port = properties.getKinesisPort();
        if (StringUtils.isNotBlank(host) && port.isPresent()) {
            final String endpoint = "https://" + host + ":" + port.get();
            builder.withEndpointConfiguration(new EndpointConfiguration(endpoint, awsRegion));
        } else {
            builder.withRegion(awsRegion);

        }

        return builder.build();
    }
}
