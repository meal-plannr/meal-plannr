package com.mealplannr.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DynamoDbAuthorisingRealmUT {

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
}