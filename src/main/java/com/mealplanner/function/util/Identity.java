package com.mealplanner.function.util;

/**
 * From: https://willhamill.com/2016/12/12/aws-api-gateway-lambda-proxy-request-and-response-objects
 *
 * @author Stuart Leyland-Cole
 *
 */
public class Identity {

    private String cognitoIdentityPoolId;
    private String accountId;
    private String cognitoIdentityId;
    private String caller;
    private String apiKey;
    private String sourceIp;
    private String cognitoAuthenticationType;
    private String cognitoAuthenticationProvider;
    private String userArn;
    private String userAgent;
    private String user;

    public String getCognitoIdentityPoolId() {
        return cognitoIdentityPoolId;
    }

    public void setCognitoIdentityPoolId(final String cognitoIdentityPoolId) {
        this.cognitoIdentityPoolId = cognitoIdentityPoolId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }

    public String getCognitoIdentityId() {
        return cognitoIdentityId;
    }

    public void setCognitoIdentityId(final String cognitoIdentityId) {
        this.cognitoIdentityId = cognitoIdentityId;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(final String caller) {
        this.caller = caller;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(final String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getCognitoAuthenticationType() {
        return cognitoAuthenticationType;
    }

    public void setCognitoAuthenticationType(final String cognitoAuthenticationType) {
        this.cognitoAuthenticationType = cognitoAuthenticationType;
    }

    public String getCognitoAuthenticationProvider() {
        return cognitoAuthenticationProvider;
    }

    public void setCognitoAuthenticationProvider(final String cognitoAuthenticationProvider) {
        this.cognitoAuthenticationProvider = cognitoAuthenticationProvider;
    }

    public String getUserArn() {
        return userArn;
    }

    public void setUserArn(final String userArn) {
        this.userArn = userArn;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

}