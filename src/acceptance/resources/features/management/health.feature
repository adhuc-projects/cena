Feature: Check service health
  As a developer, I want to check the service health when service is started so that I can ensure the service is running

  Scenario: Health check is ok
    Given a running service
    When I check the service health
    Then the service health is ok
