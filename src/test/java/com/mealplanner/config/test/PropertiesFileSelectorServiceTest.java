package com.mealplanner.config.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

import com.mealplanner.config.PropertiesFileSelectorService;

public class PropertiesFileSelectorServiceTest {

    @Test
    public void is_running_in_production_returns_true_when_specified_in_constructor() {
        final PropertiesFileSelectorService service = new PropertiesFileSelectorService(false, true);

        assertThat(service.isRunningInProduction()).isTrue();
    }

    @Test
    public void is_running_in_production_returns_false_when_specified_in_constructor() {
        final PropertiesFileSelectorService service = new PropertiesFileSelectorService(false, false);

        assertThat(service.isRunningInProduction()).isFalse();
    }

    @Test
    public void ci_properties_file_is_returned_when_running_on_ci() {
        final PropertiesFileSelectorService service = new PropertiesFileSelectorService(true, false);

        assertThat(service.getPropertiesFile()).isEqualTo(PropertiesFileSelectorService.CI_PROPERTIES_FILENAME);
    }

    @Test
    public void local_properties_file_is_returned_when_running_locally() {
        final PropertiesFileSelectorService service = new PropertiesFileSelectorService(false, false);

        assertThat(service.getPropertiesFile()).isEqualTo(PropertiesFileSelectorService.LOCAL_PROPERTIES_FILENAME);
    }

    @Test
    public void exception_is_thrown_when_getting_properties_file_if_running_in_production() {
        final PropertiesFileSelectorService service = new PropertiesFileSelectorService(false, true);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> service.getPropertiesFile())
                .withMessage(PropertiesFileSelectorService.ERROR_LOADING_PROPERTIES_FILE_WHEN_RUNNING_IN_PROD);
    }
}