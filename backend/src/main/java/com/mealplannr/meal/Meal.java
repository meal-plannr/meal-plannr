package com.mealplannr.meal;

import java.time.LocalDate;

public class Meal {

    private String id;
    private String userId;
    private String description;
    private Long version;
    private LocalDate date;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Meal [id=" + id + ", userId=" + userId + ", description=" + description + ", version=" + version + ", date=" + date + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Meal other = (Meal)obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public static class Builder {
        private String mealId;
        private String userId;
        private String description;
        private LocalDate date;

        public Builder mealId(final String mealId) {
            this.mealId = mealId;
            return this;
        }

        public Builder userId(final String userId) {
            this.userId = userId;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public Builder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public Meal build() {
            final Meal meal = new Meal();
            meal.setId(mealId);
            meal.setUserId(userId);
            meal.setDescription(description);
            meal.setDate(date);
            return meal;
        }
    }
}
