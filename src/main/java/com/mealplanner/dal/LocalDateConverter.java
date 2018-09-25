package com.mealplanner.dal;

import java.time.LocalDate;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class LocalDateConverter implements DynamoDBTypeConverter<String, LocalDate> {

    @Override
    public String convert(final LocalDate date) {
        return date.toString();
    }

    @Override
    public LocalDate unconvert(final String date) {
        return LocalDate.parse(date);
    }
}