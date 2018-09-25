package com.mealplanner.dal.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.mealplanner.dal.LocalDateConverter;

public class LocalDateConverterTest {

    private final LocalDateConverter converter = new LocalDateConverter();

    @Test
    public void localdate_is_converted_to_string() {
        final LocalDate now = LocalDate.now();

        final String converted = converter.convert(now);

        assertThat(converted).isEqualTo(now.toString());
    }

    @Test
    public void string_is_converted_to_localdate() {
        final String date = "2018-09-25";

        final LocalDate converted = converter.unconvert(date);

        final LocalDate expected = LocalDate.of(2018, 9, 25);
        assertThat(converted).isEqualTo(expected);
    }
}
