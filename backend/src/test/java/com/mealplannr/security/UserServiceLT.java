package com.mealplannr.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.apache.shiro.util.ThreadState;
import org.junit.jupiter.api.Test;

import com.mealplannr.test.LocalTestBase;

public class UserServiceLT extends LocalTestBase {

    private static final String USER_ID = "user1";
    @Inject
    UserService userService;

    @Inject
    SecurityService securityService;

    UserServiceLT() {
        super();
        APP_COMPONENT.inject(this);
    }

    @Test
    void getCurrentUserId_returns_empty_if_no_current_user() {
        final ThreadState threadState = securityService.initialiseAndPushUser(USER_ID);
        securityService.clearContext(threadState);

        final Optional<String> userId = userService.getUserId();

        assertThat(userId.isPresent()).isFalse();
    }

    @Test
    void getCurrentUserId_returns_current_user_id() {
        securityService.initialiseAndPushUser(USER_ID);

        final Optional<String> userId = userService.getUserId();

        assertThat(userId.isPresent()).isTrue();
        assertThat(userId.get()).isEqualTo(USER_ID);
    }

    @Test
    void getMandatoryUserId_throws_exception_if_no_current_user() {
        final ThreadState threadState = securityService.initialiseAndPushUser(USER_ID);
        securityService.clearContext(threadState);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> userService.getMandatoryUserId())
                .withMessage(UserService.ERROR_NO_USER_ID);
    }

    @Test
    void getMandatoryUserId_returns_current_user_id() {
        securityService.initialiseAndPushUser(USER_ID);

        final String userId = userService.getMandatoryUserId();

        assertThat(userId).isEqualTo(USER_ID);
    }

    @Test
    void getUserAuthorities_includes_user_id() {
        securityService.initialiseAndPushUser(USER_ID);

        final Set<String> userAuthorities = userService.getUserAuthorities();

        assertThat(userAuthorities).contains(USER_ID);
    }
}