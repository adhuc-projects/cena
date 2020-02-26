@Functional @Ingredients
Feature: Retrieve ingredient details
  As a community user, I want to retrieve a specific ingredient in the system so that I can update it later

  @Edge
  Scenario: Unknown ingredient
    Given a community user
    And the following existing ingredients
      | name      | measurementTypes |
      | Cucumber  | WEIGHT, COUNT    |
      | Olive oil | VOLUME           |
      | Pepper    | AT_CONVENIENCE   |
    And a non-existent "Tomato" ingredient
    When he attempts retrieving "Tomato" ingredient
    Then an error notifies that ingredient has not been found

  @Smoke
  Scenario: Known ingredient
    Given a community user
    And the following existing ingredients
      | name      | measurementTypes |
      | Tomato    | WEIGHT, COUNT    |
      | Cucumber  | WEIGHT, COUNT    |
      | Olive oil | VOLUME           |
      | Pepper    | AT_CONVENIENCE   |
    When he retrieves "Tomato" ingredient
    Then the ingredient details is accessible
