package com.mealplanner.config.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mealplanner.config.PropertiesFileSelectorService;
import com.mealplanner.config.PropertiesService;

@ExtendWith(MockitoExtension.class)
public class PropertiesServiceTest {

    @Rule
    public static final EnvironmentVariables ENVIRONMENT_VARIABLES = new EnvironmentVariables();

    @BeforeAll
    public static void setEnvVars() {
        ENVIRONMENT_VARIABLES.set("region", "my-region");
        ENVIRONMENT_VARIABLES.set("tableName", "the-table");
    }

    @Test
    public void production_values_take_precedence_over_everything() {
        final PropertiesFileSelectorService propertiesFileSelectorService = new PropertiesFileSelectorService(false, true);
        final PropertiesService service = new PropertiesService(propertiesFileSelectorService);

        assertThat(service.getAwsRegion()).isEqualTo("my-region");
        assertThat(service.getMealsTableName()).isEqualTo("the-table");
        assertThat(service.getDynamoEndpoint()).isBlank();
        assertThat(service.needToCreateDynamoTables()).isFalse();
    }

    @Test
    public void ci_values_take_precendence_over_local() {
        final PropertiesFileSelectorService propertiesFileSelectorService = new PropertiesFileSelectorService(true, false);
        final PropertiesService service = new PropertiesService(propertiesFileSelectorService);

        assertThat(service.getAwsRegion()).isEqualTo("eu-west-2");
        assertThat(service.getMealsTableName()).isEqualTo("ci-meals");
        assertThat(service.getDynamoEndpoint()).isBlank();
        assertThat(service.needToCreateDynamoTables()).isFalse();
    }

    @Test
    public void local_values_are_used_by_default() {
        final PropertiesFileSelectorService propertiesFileSelectorService = new PropertiesFileSelectorService(false, false);
        final PropertiesService service = new PropertiesService(propertiesFileSelectorService);

        assertThat(service.getAwsRegion()).isEqualTo("eu-west-1");
        assertThat(service.getMealsTableName()).isEqualTo("meals");
        assertThat(service.getDynamoEndpoint()).isEqualTo("http://localhost:4569");
        assertThat(service.needToCreateDynamoTables()).isTrue();
    }
}