package com.mealplanner.config;

public enum Environment {

    PRODUCTION(Environment.CODE_PRODUCTION),
    CI(Environment.CODE_CI),
    LOCAL(Environment.CODE_LOCAL);

    private static final String CODE_PRODUCTION = "prod";
    private static final String CODE_CI = "ci";
    private static final String CODE_LOCAL = "local";

    private final String code;

    Environment(final String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    public static Environment getEnvironmentForCode(final String code) {
        switch (code) {
        case CODE_PRODUCTION:
            return Environment.PRODUCTION;
        case CODE_CI:
            return Environment.CI;
        case CODE_LOCAL:
            return LOCAL;
        default:
            throw new IllegalArgumentException("Unknown environment code [%s]");
        }
    }
}