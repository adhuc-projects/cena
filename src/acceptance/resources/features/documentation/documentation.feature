Feature: Access to API documentation
  As an API consumer, I want to access to the API documentation so that I can use the API more easily

  Scenario: Documentation is available
    Given a running service
    When I access to the documentation
    Then the documentation is available
