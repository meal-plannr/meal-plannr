package com.mealplannr.security;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AclService {

    @Inject
    AclService() {

    }

    public Set<Acl> getAcls(final String teamId, final Set<String> userAuthorities) {
        return null;
    }
}
