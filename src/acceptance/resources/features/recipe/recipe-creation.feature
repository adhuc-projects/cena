@Functional @Recipes
Feature: Create a recicpe in the system
  As an authenticated user, I want to create a recipe in the system so that I can retrieve it later to follow it

  @Smoke @Security
  Scenario: Create a recipe successfully
    Given an authenticated user
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he creates the recipe
    Then the recipe is created
    And the recipe can be found in the list
