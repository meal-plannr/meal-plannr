package com.mealplannr.security;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.ThreadState;
import org.junit.jupiter.api.Test;

import com.mealplannr.test.LocalTestBase;

public class SecurityServiceLT extends LocalTestBase {

    private static final String USER_1_ID = "user1";
    private static final String USER_2_ID = "user2";
    private static final String USER_3_ID = "user3";
    @Inject
    SecurityService securityService;

    SecurityServiceLT() {
        super();
        APP_COMPONENT.inject(this);
    }

    @Test
    void security_context_is_initialised() {
        final ThreadState subjectThreadState = securityService.initialiseAndPushUser(USER_1_ID);

        assertThat(subjectThreadState).isNotNull();
    }

    @Test
    void subject_is_set() {
        securityService.initialiseAndPushUser(USER_1_ID);

        assertThat(SecurityUtils.getSubject()).isNotNull();
    }

    @Test
    void user_id_is_set_as_subject_principle() {
        securityService.initialiseAndPushUser(USER_1_ID);

        assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo(USER_1_ID);
    }

    @Test
    void users_teams_are_set_as_session_attributes() {

    }

    @Test
    void initialiseAndPushUser_resets_user_contexts_stack() {
        securityService.initialiseAndPushUser(USER_1_ID);

        securityService.popUser();

        assertThat(securityService.userContextsStackIsEmpty()).isTrue();
    }

    @Test
    void can_change_subject() {
        securityService.initialiseAndPushUser(USER_1_ID);

        securityService.pushUser(USER_2_ID);

        assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo(USER_2_ID);
    }

    @Test
    void can_change_subject_and_back_to_original() {
        securityService.initialiseAndPushUser(USER_1_ID);
        securityService.pushUser(USER_2_ID);

        securityService.popUser();

        assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo(USER_1_ID);
    }

    @Test
    void can_change_subject_multiple_times() {
        securityService.initialiseAndPushUser(USER_1_ID);
        assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo(USER_1_ID);

        securityService.pushUser(USER_2_ID);
        assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo(USER_2_ID);

        securityService.pushUser(USER_3_ID);
        assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo(USER_3_ID);

        securityService.popUser();
        assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo(USER_2_ID);

        securityService.popUser();
        assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo(USER_1_ID);
    }

    @Test
    void clearContext_clears_subject() {
        final ThreadState threadState = securityService.initialiseAndPushUser(USER_1_ID);

        securityService.clearContext(threadState);

        assertThat(SecurityUtils.getSubject().getPrincipal()).isNull();
    }
}
