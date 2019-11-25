@Functional @Recipes
Feature: Create a recicpe in the system
  As an authenticated user, I want to create a recipe in the system so that I can retrieve it later to follow it

  Scenario: Create a recipe without name
    Given an authenticated user
    When he creates a recipe without name
    Then an error notifies that recipe must have a name
    And the recipe cannot be found in the list

  Scenario: Create a recipe without content
    Given an authenticated user
    When he creates a recipe without content
    Then an error notifies that recipe must have a content
    And the recipe cannot be found in the list

  @Smoke @Security
  Scenario: Create a recipe successfully
    Given an authenticated user
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he creates the recipe
    Then the recipe is created
    And the recipe can be found in the list
