@Functional @Ingredients
Feature: Create an ingredient in the system
  As an ingredient manager, I want to create an ingredient in the system so that recipes can be composed of ingredients

  @Edge
  Scenario: Create an ingredient without name
    Given an authenticated ingredient manager
    When he creates an ingredient without name
    Then an error notifies that ingredient must have a name
    And the ingredient cannot be found in the list

  @Smoke @Security
  Scenario: Create an ingredient successfully
    Given an authenticated ingredient manager
    And a non-existent "Tomato" ingredient
    When he creates the ingredient
    Then the ingredient is created
    And the ingredient can be found in the list

  @Edge
  Scenario: Create an ingredient with already used name
    Given an authenticated ingredient manager
    And an existing "Tomato" ingredient
    When he creates the ingredient
    Then an error notifies that ingredient name already exists
    And the ingredient can be found in the list

  @Security
  Scenario: Create an ingredient as community user
    Given a community user
    And a non-existent "Tomato" ingredient
    When he creates the ingredient
    Then an error notifies that user is not authenticated
    And the ingredient cannot be found in the list

  @Security
  Scenario: Create an ingredient as authenticated user
    Given an authenticated user
    And a non-existent "Tomato" ingredient
    When he creates the ingredient
    Then an error notifies that user is not authorized
    And the ingredient cannot be found in the list

  @Security
  Scenario: Create an ingredient as super administrator
    Given an authenticated super administrator
    And a non-existent "Tomato" ingredient
    When he creates the ingredient
    Then the ingredient is created
    And the ingredient can be found in the list
