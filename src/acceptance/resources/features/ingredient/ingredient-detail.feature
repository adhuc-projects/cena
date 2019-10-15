@Functional @Ingredients
Feature: Retrieve ingredient details
  As a community user, I want to retrieve a specific ingredient in the system so that I can update it later

  Scenario: Unknown ingredient
    Given a community user
    And the following existing ingredients
      | name       |
      | Cucumber   |
      | Mozzarella |
      | Olive      |
    And a non-existent "Tomato" ingredient
    When he attempts retrieving "Tomato" ingredient
    Then an error notifies that ingredient has not been found

  @Smoke
  Scenario: Known ingredient
    Given a community user
    And the following existing ingredients
      | name       |
      | Tomato     |
      | Cucumber   |
      | Mozzarella |
      | Olive      |
    When he retrieves "Tomato" ingredient
    Then the ingredient details is accessible
