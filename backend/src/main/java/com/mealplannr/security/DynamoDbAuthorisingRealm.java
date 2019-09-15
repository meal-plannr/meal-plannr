package com.mealplannr.security;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

@Singleton
public class DynamoDbAuthorisingRealm extends AuthorizingRealm {

    public static final String NAME = "dyanmoDbRealm";

    static final String ERROR_AUTHENTICATION_NOT_SUPPORTED = "This realm does not support authentication";

    @Inject
    DynamoDbAuthorisingRealm() {

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        // TODO: Review if this implementation is correct. Unclear from Javadoc what it should do
        return new SimpleAuthorizationInfo();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) {
        throw new UnsupportedOperationException(ERROR_AUTHENTICATION_NOT_SUPPORTED);
    }

    @Override
    public boolean supports(final AuthenticationToken token) {
        return false;
    }
}
