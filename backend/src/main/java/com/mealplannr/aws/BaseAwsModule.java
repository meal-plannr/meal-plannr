package com.mealplannr.aws;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;

@Module
public abstract class BaseAwsModule {

    @Singleton
    @Provides
    static SdkHttpClient sdkHttpClient() {
        return UrlConnectionHttpClient.create();
    }
}
