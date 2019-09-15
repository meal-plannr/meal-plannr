package com.mealplannr.security;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Acl {

    public static Builder builder() {
        return new AutoValue_Acl.Builder();
    }

    public abstract String teamId();

    public abstract String authorityId();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder teamId(final String teamId);

        public abstract Builder authorityId(final String authorityId);

        public abstract Acl build();
    }
}
