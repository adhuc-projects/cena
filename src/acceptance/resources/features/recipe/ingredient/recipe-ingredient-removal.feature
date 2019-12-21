@Functional @Recipes
Feature: Remove ingredients from recipe
  As an authenticated user, I want to remove ingredients from a recipe so that I can change my recipe

  @Edge
  Scenario: Remove an unknown ingredient from an existing recipe
    Given an authenticated user
    And a non-existent "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    When he attempts removing the ingredient from the recipe
    Then an error notifies that ingredient has not been found

  @Edge
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

  @Edge
  Scenario: Remove a not related ingredient from a recipe
    Given an authenticated user
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And recipe does not contain ingredient
    When he attempts removing the ingredient from the recipe
    Then an error notifies that recipe does not contain ingredient

  Scenario: Remove all ingredients from a recipe
    Given an authenticated user
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And the following existing ingredients
      | name       |
      | Tomato     |
      | Cucumber   |
      | Mozzarella |
    And the following ingredients in the recipe
      | name       |
      | Tomato     |
      | Cucumber   |
      | Mozzarella |
    When he removes all the ingredients from the recipe
    Then the ingredients are removed from the recipe
    And no ingredient is related to the recipe
