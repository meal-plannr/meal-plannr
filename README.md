# aws-api
API implementation based on AWS Lambdas

## Status

[![CircleCI](https://circleci.com/gh/meal-plannr/aws-api/tree/master.svg?style=svg)](https://circleci.com/gh/meal-plannr/aws-api/tree/master)

[![Test coverage](https://sonarcloud.io/api/project_badges/measure?project=aws-api&metric=coverage)](https://sonarcloud.io/dashboard?id=aws-api) [![Quality](https://sonarcloud.io/api/project_badges/measure?project=aws-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=aws-api) [![Tech debt](https://sonarcloud.io/api/project_badges/measure?project=aws-api&metric=sqale_index)](https://sonarcloud.io/dashboard?id=aws-api)

## Running integration tests

The integration tests rely on [Localstack](https://github.com/localstack/localstack) to provide mocked AWS services. Running tests does not automatically start Localstack, you need to run it locally.

### Running Localstack

1. Install Localstack

   `pip install localstack` or

   `pip install --user localstack` on Mac

2. Start Localstack

   `SERVICES=dynamodb localstack start --docker`
