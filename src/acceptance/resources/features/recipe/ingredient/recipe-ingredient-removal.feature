@Functional @Recipes
Feature: Remove ingredients from recipe
  As an authenticated user, I want to remove ingredients from a recipe so that I can change my recipe

  Scenario: Remove an unknown ingredient from an existing recipe
    Given an authenticated user
    And a non-existent "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he attempts removing the ingredient from the recipe
    Then an error notifies that ingredient has not been found

  Scenario: Remove an existing ingredient from an unknown recipe
    Given an authenticated user
    And an existing "Tomato" ingredient
    And a non-existent "Tomato, cucumber and mozzarella salad" recipe
    When he attempts removing the ingredient from the recipe
    Then an error notifies that recipe has not been found

  @Smoke @Security
  Scenario: Remove an ingredient from an existing recipe
    Given an authenticated user
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And recipe contains ingredient
    When he removes the ingredient from the recipe
    Then the ingredient is removed from the recipe
    And the ingredient cannot be found in the recipe's ingredients list

  Scenario: Remove a not related ingredient from a recipe
    Given an authenticated user
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And recipe does not contain ingredient
    When he attempts removing the ingredient from the recipe
    Then an error notifies that recipe does not contain ingredient
