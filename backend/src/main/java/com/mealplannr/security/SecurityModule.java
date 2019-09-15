package com.mealplannr.security;

import java.util.Collection;

import javax.inject.Singleton;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;

import com.google.common.collect.ImmutableList;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class SecurityModule {

    @Singleton
    @Provides
    static SecurityManager securityManager(final Collection<Realm> realms) {
        final DefaultSecurityManager securityManager = new DefaultSecurityManager(realms);
        securityManager.setSessionManager(new DefaultSessionManager());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Singleton
    @Provides
    static Collection<Realm> realms(final DynamoDbAuthorisingRealm dynamoDbAuthorisingRealm) {
        return new ImmutableList.Builder<Realm>()
                .add(dynamoDbAuthorisingRealm)
                .build();
    }
}
