package com.mealplanner.config;

public enum Environment {

    PRODUCTION("prod"),
    CI("ci"),
    LOCAL("local");

    private final String code;

    Environment(final String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}