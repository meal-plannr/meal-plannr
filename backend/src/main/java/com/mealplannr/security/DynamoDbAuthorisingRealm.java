package com.mealplannr.security;

import java.util.Set;

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

    private final AclService aclService;
    private final UserService userService;

    @Inject
    DynamoDbAuthorisingRealm(final AclService aclService, final UserService userService) {
        this.aclService = aclService;
        this.userService = userService;
    }

    @Override
    public boolean isPermitted(final PrincipalCollection principals, final String permission) {
        final Set<String> userAuthorities = userService.getUserAuthorities();

        // Find all of the ACLs for the team we're trying to access and the user's authorities (themself and any groups)
        // If any ACLs are found then the user has permission to access
        final Set<Acl> aclsForTeam = aclService.getAcls(permission, userAuthorities);
        return !aclsForTeam.isEmpty();
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
