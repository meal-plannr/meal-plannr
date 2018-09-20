package com.mealplanner.config.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

        final Properties props = new Properties.Builder()
                .awsRegion("my-region")
                .mealsTableName("the-table")
                .dynamoEndpoint(null)
                .needToCreateDynamoTables(false)
                .kinesisEndPoint(null)
                .build();
        assertPropertyValues(service, props);
    }

    @Test
    public void ci_values_take_precendence_over_local() {
        final PropertiesFileSelectorService propertiesFileSelectorService = new PropertiesFileSelectorService(true, false);
        final PropertiesService service = new PropertiesService(propertiesFileSelectorService);

        final Properties props = new Properties.Builder()
                .awsRegion("eu-west-2")
                .mealsTableName("ci-meals")
                .dynamoEndpoint(null)
                .needToCreateDynamoTables(false)
                .kinesisEndPoint(null)
                .build();
        assertPropertyValues(service, props);
    }

    @Test
    public void local_values_are_used_by_default() {
        final PropertiesFileSelectorService propertiesFileSelectorService = new PropertiesFileSelectorService(false, false);

        final PropertiesService service = new PropertiesService(propertiesFileSelectorService);

        final Properties props = new Properties.Builder()
                .awsRegion("eu-west-1")
                .mealsTableName("meals")
                .dynamoEndpoint("http://localhost:4569")
                .needToCreateDynamoTables(true)
                .kinesisEndPoint("http://localhost:4568")
                .build();
        assertPropertyValues(service, props);
    }

    private final void assertPropertyValues(final PropertiesService service, final Properties expectedProperties) {
        assertAll(() -> assertThat(service.getAwsRegion()).isEqualTo(expectedProperties.getAwsRegion()),
                () -> assertThat(service.getMealsTableName()).isEqualTo(expectedProperties.getMealsTableName()),
                () -> assertThat(service.getDynamoEndpoint()).isEqualTo(expectedProperties.getDynamoEndpoint()),
                () -> assertThat(service.needToCreateDynamoTables()).isEqualTo(expectedProperties.getNeedToCreateDynamoTables()),
                () -> assertThat(service.getKinesisEndpoint()).isEqualTo(expectedProperties.getKinesisEndpoint()));
    }

    static class Properties {
        private final String awsRegion;
        private final String mealsTableName;
        private final String dynamoEndpoint;
        private final boolean needToCreateDynamoTables;
        private final String kinesisEndpoint;

        public Properties(final Builder builder) {
            this.awsRegion = builder.awsRegion;
            this.mealsTableName = builder.mealsTableName;
            this.dynamoEndpoint = builder.dynamoEndpoint;
            this.needToCreateDynamoTables = builder.needToCreateDynamoTables;
            this.kinesisEndpoint = builder.kinesisEndpoint;
        }

        public String getAwsRegion() {
            return awsRegion;
        }

        public String getMealsTableName() {
            return mealsTableName;
        }

        public String getDynamoEndpoint() {
            return dynamoEndpoint;
        }

        public boolean getNeedToCreateDynamoTables() {
            return needToCreateDynamoTables;
        }

        public String getKinesisEndpoint() {
            return kinesisEndpoint;
        }

        static class Builder {
            private String awsRegion;
            private String mealsTableName;
            private String dynamoEndpoint;
            private boolean needToCreateDynamoTables;
            private String kinesisEndpoint;

            public Builder awsRegion(final String awsRegion) {
                this.awsRegion = awsRegion;
                return this;
            }

            public Builder mealsTableName(final String mealsTableName) {
                this.mealsTableName = mealsTableName;
                return this;
            }

            public Builder dynamoEndpoint(final String dynamoEndpoint) {
                this.dynamoEndpoint = dynamoEndpoint;
                return this;
            }

            public Builder needToCreateDynamoTables(final boolean needToCreateDynamoTables) {
                this.needToCreateDynamoTables = needToCreateDynamoTables;
                return this;
            }

            public Builder kinesisEndPoint(final String kinesisEndpoint) {
                this.kinesisEndpoint = kinesisEndpoint;
                return this;
            }

            public Properties build() {
                return new Properties(this);
            }

        }
    }
}