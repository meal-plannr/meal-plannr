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
    private Deque<UserContext> userContextsStack = new ArrayDeque<>();

    @Inject
    SecurityService(final SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * Reset the authorisation stack and push the supplied user on to the stack
     *
     * @param userId The user ID to push to the stack
     * @return A {@link ThreadState} bound to a subject representing the user
     */
    public ThreadState initialiseAndPushUser(final String userId) {
        userContextsStack = new ArrayDeque<>();
        return pushUser(userId);
    }

    /**
     * Add a user to the top of the authorisation stack, making them to current user
     *
     * @param userId The ID of the user
     * @return A {@link ThreadState} bound to a subject representing the user
     */
    public ThreadState pushUser(final String userId) {
        userContextsStack.push(new UserContext(userId));
        return createThreadState(userId);
    }

    private ThreadState createThreadState(final String userId) {
        final Subject subject = new Subject.Builder(securityManager)
                .principals(new SimplePrincipalCollection(userId, DynamoDbAuthorisingRealm.NAME))
                .buildSubject();
        final SubjectThreadState subjectThreadState = new SubjectThreadState(subject);
        subjectThreadState.bind();
        return subjectThreadState;
    }

    /**
     * Remove the current user from the top of the stack. If there was a previous user on the stack, they become the current user
     *
     * @return
     */
    public ThreadState popUser() {
        if (!userContextsStack.isEmpty()) {
            userContextsStack.pop();
            final UserContext previousUserContext = userContextsStack.peek();
            if (previousUserContext != null) {
                return createThreadState(previousUserContext.userId);
            }
        }
        return null;
    }

    /**
     * Check if the user contexts stack is empty
     *
     * @return True if the stack is empty, false otherwise
     */
    public boolean userContextsStackIsEmpty() {
        return userContextsStack.isEmpty();
    }

    private class UserContext {
        private final String userId;

        private UserContext(final String userId) {
            this.userId = userId;
        }
    }
}
