# aws-api
API implementation based on AWS Lambdas

## Running integration tests

The integration tests rely on [Localstack](https://github.com/localstack/localstack) to provide mocked AWS services. Running tests does not automatically start Localstack, you need to run it locally.

### Running Localstack

1. Install Localstack

   `pip install localstack` or

   `pip install --user localstack` on Mac

2. Start Localstack

   `SERVICES=dynamodb localstack start --docker`
