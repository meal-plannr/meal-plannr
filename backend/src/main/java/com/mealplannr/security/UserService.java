package com.mealplannr.security;

import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.shiro.SecurityUtils;

import com.google.common.collect.ImmutableSet;

@Singleton
public class UserService {

    static final String ERROR_NO_USER_ID = "No current user";

    @Inject
    UserService() {
    }

    public String getMandatoryUserId() {
        return getUserId().orElseThrow(() -> new IllegalStateException(ERROR_NO_USER_ID));
    }

    public Optional<String> getUserId() {
        return Optional.ofNullable((String)SecurityUtils.getSubject().getPrincipal());
    }

    public Set<String> getUserAuthorities() {
        return new ImmutableSet.Builder<String>()
                .add(getMandatoryUserId())
                .build();
    }
}
