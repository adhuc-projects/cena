Feature: Check service health
  As a developer, I want to check the service health when service is started so that I can ensure the service is running

  Scenario: Health check is ok
    Given a running service
    When I check the service health
    Then the service health is ok
    And the health detail is not available

  Scenario: Health check detail can be accessed by actuator manager
    Given a running service
    And an authenticated actuator manager
    When I check the service health
    Then the service health is ok
    And the health detail is available
