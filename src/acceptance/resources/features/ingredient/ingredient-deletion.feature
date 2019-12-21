@Functional @Ingredients
Feature: Delete an ingredient from the system
  As an ingredient manager, I want to delete an ingredient in the system so that ingredient list is not polluted with misconfigured ingredients

  @Edge
  Scenario: Delete an unknown ingredient
    Given an authenticated ingredient manager
    And a non-existent "Tomato" ingredient
    When he attempts deleting the ingredient
    Then an error notifies that ingredient has not been found

  @Smoke @Security
  Scenario: Delete an ingredient successfully
    Given an authenticated ingredient manager
    And an existing "Tomato" ingredient
    When he deletes the ingredient
    Then the ingredient is deleted
    And the ingredient cannot be found in the list

  @Security
  Scenario: Delete an ingredient as community user
    Given a community user
    And an existing "Tomato" ingredient
    When he deletes the ingredient
    Then an error notifies that user is not authenticated
    And the ingredient can be found in the list

  @Security
  Scenario: Delete an ingredient as authenticated user
    Given an authenticated user
    And an existing "Tomato" ingredient
    When he deletes the ingredient
    Then an error notifies that user is not authorized
    And the ingredient can be found in the list

  @Security
  Scenario: Delete an ingredient as super administrator
    Given an authenticated super administrator
    And an existing "Tomato" ingredient
    When he deletes the ingredient
    Then the ingredient is deleted
    And the ingredient cannot be found in the list
