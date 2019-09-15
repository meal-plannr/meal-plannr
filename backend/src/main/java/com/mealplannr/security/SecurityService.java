package com.mealplannr.security;

import java.util.ArrayDeque;
import java.util.Deque;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;

/**
 * Sets up and interacts with Apache Shiro which is the base library providing authorisation capability
 */
@Singleton
public class SecurityService {

    private final SecurityManager securityManager;
    private Deque<UserContext> authStack = new ArrayDeque<>();

    @Inject
    SecurityService(final SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public ThreadState initialiseContext(final String userId) {
        authStack = new ArrayDeque<>();
        return pushUser(userId);
    }

    public ThreadState pushUser(final String userId) {
        authStack.push(new UserContext(userId));
        return createThreadState(userId);
    }

    private ThreadState createThreadState(final String userId) {
        final Subject subject = new Subject.Builder(securityManager)
                .principals(new SimplePrincipalCollection(userId, DynamoDbRealm.NAME))
                .buildSubject();
        final SubjectThreadState subjectThreadState = new SubjectThreadState(subject);
        subjectThreadState.bind();
        return subjectThreadState;
    }

    public ThreadState popUser() {
        if (!authStack.isEmpty()) {
            authStack.pop();
            final UserContext previousUserContext = authStack.peek();
            if (previousUserContext != null) {
                return pushUser(previousUserContext.userId);
            }
        }
        return null;
    }

    public boolean authStackIsEmpty() {
        return authStack.isEmpty();
    }

    private class UserContext {
        private final String userId;

        private UserContext(final String userId) {
            this.userId = userId;
        }
    }
}
