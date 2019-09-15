package com.mealplannr.security;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;

/**
 * Sets up and interacts with Apache Shiro which is the base library performing authorisation capability
 */
@Singleton
public class SecurityService {

    private final SecurityManager securityManager;

    @Inject
    SecurityService(final SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public SubjectThreadState initialiseSubject(final String userId) {
        final Subject subject = new Subject.Builder(securityManager)
                .principals(new SimplePrincipalCollection(userId, DynamoDbRealm.NAME))
                .buildSubject();
        final SubjectThreadState subjectThreadState = new SubjectThreadState(subject);
        subjectThreadState.bind();
        return subjectThreadState;
    }
}
