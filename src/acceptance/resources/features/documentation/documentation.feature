@Technical
Feature: Access to API documentation
  As an API consumer, I want to access to the API documentation so that I can use the API more easily

  @Smoke
  Scenario: Documentation is available
    Given a running service
    When I access to the documentation
    Then the documentation is available

  @Smoke
  Scenario: OpenAPI specification is available
    Given a running service
    When I access to the OpenAPI specification
    Then the OpenAPI specification is available
