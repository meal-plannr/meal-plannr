package com.mealplannr.security;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.junit.jupiter.api.Test;

import com.mealplannr.test.LocalTestBase;

public class SecurityServiceLT extends LocalTestBase {

    private static final String USER_ID = "user1";
    @Inject
    SecurityService securityService;

    SecurityServiceLT() {
        super();
        APP_COMPONENT.inject(this);
    }

    @Test
    void security_context_is_initialised() {
        final SubjectThreadState subjectThreadState = securityService.initialiseSubject(USER_ID);

        assertThat(subjectThreadState).isNotNull();
    }

    @Test
    void subject_is_set() {
        securityService.initialiseSubject(USER_ID);

        assertThat(SecurityUtils.getSubject()).isNotNull();
    }

    @Test
    void user_id_is_set_as_subject_principle() {
        securityService.initialiseSubject(USER_ID);

        assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo(USER_ID);
    }
}
