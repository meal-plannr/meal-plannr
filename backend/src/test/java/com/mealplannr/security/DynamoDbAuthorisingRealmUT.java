package com.mealplannr.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.ImmutableSet;

@ExtendWith(MockitoExtension.class)
class DynamoDbAuthorisingRealmUT {

    private static final String TEAM_ID = "team1";
    private static final String USER_ID = "user1";

    // Class dependencies
    @Mock
    private AclService aclService;
    @Mock
    private UserService userService;

    @InjectMocks
    private DynamoDbAuthorisingRealm realm;

    // Mocks for tests
    @Mock
    private AuthenticationToken authenticationToken;
    @Mock
    private PrincipalCollection principles;

    @Test
    void does_not_support_authentication() {
        assertThat(realm.supports(authenticationToken)).isFalse();
    }

    @Test
    void doGetAuthenticationInfo_throws_UnsupportedOperationException() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> realm.doGetAuthenticationInfo(authenticationToken))
                .withMessage(DynamoDbAuthorisingRealm.ERROR_AUTHENTICATION_NOT_SUPPORTED);
    }

    @Test
    void doGetAuthorizationInfo_returns_empty_info() {
        final AuthorizationInfo authorizationInfo = realm.doGetAuthorizationInfo(principles);

        assertThat(authorizationInfo.getRoles()).isNull();
        assertThat(authorizationInfo.getObjectPermissions()).isNull();
        assertThat(authorizationInfo.getStringPermissions()).isNull();
    }

    @Test
    void isPermitted_returns_true_if_user_is_direct_member_of_team() {
        final ImmutableSet<String> userAuthorities = ImmutableSet.of(USER_ID);
        when(userService.getUserAuthorities()).thenReturn(userAuthorities);
        when(aclService.getAcls(TEAM_ID, userAuthorities)).thenReturn(ImmutableSet.of(Acl.builder()
                .teamId(TEAM_ID)
                .authorityId(USER_ID)
                .build()));

        final boolean isPermitted = realm.isPermitted(principles, TEAM_ID);

        assertThat(isPermitted).isTrue();
    }

    @Test
    void isPermitted_returns_true_if_user_is_member_of_group_that_is_member_of_team() {

    }

    @Test
    void isPermitted_returns_false_if_user_is_not_direct_member_of_team_or_member_of_group_that_is_member_of_team() {

    }
}