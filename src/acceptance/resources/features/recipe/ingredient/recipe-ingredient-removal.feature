@Functional @Recipes
Feature: Remove ingredients from recipe
  As an authenticated user, I want to remove ingredients from a recipe so that I can change my recipe

  @Smoke @Security
  Scenario: Remove an ingredient from an existing recipe
    Given an authenticated user
    And an existing "Tomato" ingredient
    And an existing "Tomato, cucumber and mozzarella salad" recipe
    And recipe contains ingredient
    When he removes the ingredient from the recipe
    Then the ingredient is removed from the recipe
    And the ingredient cannot be found in the recipe's ingredients list
